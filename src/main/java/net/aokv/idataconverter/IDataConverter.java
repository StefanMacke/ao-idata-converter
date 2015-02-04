package net.aokv.idataconverter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFormatter;
import com.wm.data.IDataUtil;

public class IDataConverter extends Converter
{
	@SuppressWarnings("unchecked")
	public <T> T convertToObject(final Object iData, final Class<T> objectType)
			throws IDataConversionException
	{
		if (iData == null)
		{
			return null;
		}
		if (isPrimitiveType(objectType))
		{
			return (T) iData;
		}

		try
		{
			if (objectType.isArray())
			{
				return convertArray((Object[]) iData, objectType);
			}

			return convertObject((IData) iData, objectType);
		}
		catch (final IDataConversionException e)
		{
			throw createException(iData, objectType, e);
		}
	}

	private <T> IDataConversionException createException(
			final Object iData, final Class<T> objectType, final Exception exception)
	{
		String object = iData.toString();
		if (iData instanceof IData)
		{
			object = new IDataFormatter().format((IData) iData);
		}
		return new IDataConversionException(
				String.format("IData could not be converted to object of class <%s>:%nError message: %s%n%s",
						objectType, exception.getMessage(), object),
				exception);
	}

	public <T> T convertToObject(
			final IData iData, final String fieldName, final Class<T> fieldType, final Class<?> elementType)
			throws IDataConversionException
	{
		if (iData == null)
		{
			return null;
		}
		if (!iData.getCursor().hasMoreData())
		{
			return null;
		}

		checkWhetherFieldExists(iData, fieldName);
		return convertFieldOrCollection(iData, fieldName, fieldType, elementType);
	}

	public <T> T convertToObject(final IData iData, final String fieldName, final Class<T> fieldType)
			throws IDataConversionException
	{
		return convertToObject(iData, fieldName, fieldType, null);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public <T> T convertToObject(final Object[] objects, final Class<T> collectionType, final Class<?> elementType)
			throws IDataConversionException
	{
		if (objects == null)
		{
			return null;
		}
		final Collection collection;
		if (collectionType.equals(List.class))
		{
			collection = new ArrayList<>();
		}
		else
		{
			try
			{
				collection = (Collection) collectionType.newInstance();
			}
			catch (final InstantiationException | IllegalAccessException e)
			{
				throw new IDataConversionException(e.getMessage(), e);
			}
		}
		for (final Object object : objects)
		{
			collection.add(convertToObject(object, elementType));
		}
		return (T) collection;
	}

	private <T> T convertFieldOrCollection(final IData iData, final String fieldName, final Class<T> fieldType,
			final Class<?> elementType)
			throws IDataConversionException
	{
		final IDataCursor idc = iData.getCursor();
		if (elementType != null && Collection.class.isAssignableFrom(fieldType))
		{
			final Object[] field = IDataUtil.getObjectArray(idc, fieldName);
			return convertToObject(field, fieldType, elementType);
		}
		else
		{
			final Object field = IDataUtil.get(idc, fieldName);
			return convertToObject(field, fieldType);
		}
	}

	private void checkWhetherFieldExists(final IData iData, final String fieldName)
			throws IDataConversionException
	{
		final IDataCursor idc = iData.getCursor();
		while (idc.hasMoreData())
		{
			idc.next();
			if (idc.getKey().equals(fieldName))
			{
				return;
			}
		}
		throw new IDataConversionException(
				String.format("Field <%s> does not exist in IData: %s",
						fieldName, new IDataFormatter().format(iData)));
	}

	@SuppressWarnings("unchecked")
	private <T> T convertArray(final Object[] objects, final Class<T> objectType)
			throws IDataConversionException
	{
		final Class<?> componentType = objectType.getComponentType();
		final Object array = Array.newInstance(componentType, objects.length);
		for (int i = 0; i < objects.length; i++)
		{
			final Object value = convertToObject(objects[i], componentType);
			Array.set(array, i, value);
		}
		return (T) array;
	}

	private <T> T convertObject(final IData iData, final Class<T> objectType)
			throws IDataConversionException
	{
		try
		{
			final T instance = objectType.newInstance();
			convertFields(iData, objectType, instance);
			convertSetters(iData, objectType, instance);
			return instance;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IDataConversionException(e.getMessage(), e);
		}
	}

	private <T> void convertFields(final IData iData, final Class<T> objectType, final T instance)
			throws IDataConversionException
	{
		try
		{
			for (final Field field : objectType.getFields())
			{
				final String fieldName = generateFieldName(field, field.getName());
				final Class<?> fieldType = field.getType();
				final Class<?> elementType = findelementType(field, fieldType);

				field.set(instance, convertFieldOrCollection(iData, fieldName, fieldType, elementType));
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new IDataConversionException(e.getMessage(), e);
		}
	}

	private Class<?> findelementType(final Field field, final Class<?> fieldType)
	{
		Class<?> elementType = null;
		if (Collection.class.isAssignableFrom(fieldType))
		{
			final ParameterizedType listType = (ParameterizedType) field.getGenericType();
			elementType = (Class<?>) listType.getActualTypeArguments()[0];
		}
		return elementType;
	}

	private <T> void convertSetters(final IData iData, final Class<T> objectType, final T instance)
			throws IDataConversionException
	{
		final Method[] methods = findMethodsStartingWith(getMethods(objectType), "set");
		try
		{
			for (final Method method : methods)
			{
				final Field field = findField(method, objectType);
				final String fieldName = generateFieldName(field, method.getName().substring(3));
				final Class<?> fieldType = field.getType();
				final Class<?> elementType = findelementType(field, fieldType);
				final Object fieldValue = convertToObject(iData, fieldName, fieldType, elementType);
				method.invoke(instance, fieldValue);
			}
		}
		catch (final NoSuchFieldException | IllegalAccessException | InvocationTargetException e)
		{
			throw new IDataConversionException(e.getMessage(), e);
		}
	}

}
