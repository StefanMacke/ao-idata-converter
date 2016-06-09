package net.aokv.idataconverter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Converter
{
	private final Map<Class<?>, CustomConverter<?>> customConverters;

	protected Converter()
	{
		customConverters = new HashMap<Class<?>, CustomConverter<?>>();
	}

	public <T> void addCustomConverter(final Class<T> clazz, final CustomConverter<T> converter)
	{
		customConverters.put(clazz, converter);
	}

	protected Optional<CustomConverter<?>> findCustomConverter(
			final AnnotatedElement element, final Class<?> clazz)
			throws Exception
	{
		CustomConverter<?> cc = customConverters.get(clazz);
		if (cc == null)
		{
			if (element.isAnnotationPresent(UseCustomConverter.class))
			{
				final Class<?> ccClass = element.getAnnotation(UseCustomConverter.class).value();
				cc = (CustomConverter<?>) ccClass.newInstance();
			}
		}
		return Optional.ofNullable(cc);
	}

	protected <T> boolean isPrimitiveType(final Class<T> objectType)
	{
		if (objectType == null)
		{
			return false;
		}
		return objectType.equals(String.class)
				|| objectType.equals(Object.class)
				|| objectType.equals(Integer.TYPE)
				|| objectType.equals(Short.TYPE)
				|| objectType.equals(Byte.TYPE)
				|| objectType.equals(Long.TYPE)
				|| objectType.equals(Float.TYPE)
				|| objectType.equals(Double.TYPE)
				|| objectType.equals(Character.TYPE)
				|| objectType.equals(Boolean.TYPE)
				|| objectType.equals(Integer.class)
				|| objectType.equals(Short.class)
				|| objectType.equals(Byte.class)
				|| objectType.equals(Long.class)
				|| objectType.equals(Float.class)
				|| objectType.equals(Double.class)
				|| objectType.equals(Character.class)
				|| objectType.equals(Boolean.class)
				|| objectType.equals(int[].class)
				|| objectType.equals(short[].class)
				|| objectType.equals(byte[].class)
				|| objectType.equals(long[].class)
				|| objectType.equals(float[].class)
				|| objectType.equals(double[].class)
				|| objectType.equals(char[].class)
				|| objectType.equals(boolean[].class);
	}

	protected String generateFieldName(final Field field, final String originalFieldName)
	{
		if (field.isAnnotationPresent(PipelineName.class))
		{
			return field.getAnnotation(PipelineName.class).value();
		}
		return originalFieldName;
	}

	protected void sortFields(final Field[] fields)
	{
		Arrays.sort(fields, (field1, field2) -> field1.getName().compareTo(field2.getName()));
	}

	protected void sortMethods(final Method[] methods)
	{
		Arrays.sort(methods, (method1, method2) -> method1.getName().compareTo(method2.getName()));
	}

	protected Method[] findMethodsStartingWith(final Method[] methods, final String prefix)
	{
		return Arrays.stream(methods)
				.filter(method -> method.getName().startsWith(prefix))
				.toArray(Method[]::new);
	}

	protected Method[] getMethods(final Class<?> objectType)
	{
		final Method[] methods = removeMethodsFromClassObject(objectType.getMethods());
		sortMethods(methods);
		return methods;
	}

	private Method[] removeMethodsFromClassObject(final Method[] methods)
	{
		return Arrays.stream(methods)
				.filter(method -> !method.getDeclaringClass().equals(Object.class))
				.toArray(Method[]::new);
	}

	private Optional<Field> getField(final Class<?> objectType, final String fieldName)
	{
		try
		{
			return Optional.of(objectType.getDeclaredField(fieldName));
		}
		catch (final NoSuchFieldException e)
		{
			return Optional.empty();
		}
	}

	protected Field getField(
			final Class<?> objectType, final String originalFieldName, final String fieldName)
			throws NoSuchFieldException
	{
		Optional<Field> field = getField(objectType, originalFieldName);
		if (!field.isPresent())
		{
			field = getField(objectType, fieldName);
		}
		if (!field.isPresent())
		{
			throw new NoSuchFieldException(String.format(
					"Field <%s> not found for class <%s>",
					originalFieldName, objectType));
		}
		return field.get();
	}

	protected Field findField(final Method method, final Class<?> objectType)
			throws NoSuchFieldException
	{
		final String methodName = method.getName();
		final String originalFieldName = methodName.substring(3);
		final String fieldName = methodName.substring(3, 4).toLowerCase(Locale.getDefault())
				+ methodName.substring(4);
		return getField(objectType, originalFieldName, fieldName);
	}
}
