package net.aokv.idataconverter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

public class Converter
{
	protected <T> boolean isPrimitiveType(final Class<T> objectType)
	{
		if (objectType.isArray())
		{
			final Class<?> componentType = objectType.getComponentType();
			return isPrimitiveType(componentType);
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
				|| objectType.equals(Boolean.class);
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

	private Field getField(final Class<?> objectType, final String fieldName)
	{
		try
		{
			return objectType.getDeclaredField(fieldName);
		}
		catch (final NoSuchFieldException e)
		{
			return null;
		}
	}

	protected Field getField(
			final Class<?> objectType, final String originalFieldName, final String fieldName)
					throws NoSuchFieldException
	{
		Field field = getField(objectType, originalFieldName);
		if (field == null)
		{
			field = getField(objectType, fieldName);
		}
		if (field == null)
		{
			throw new NoSuchFieldException(String.format(
					"Field <%s> not found for class <%s>",
					originalFieldName, objectType));
		}
		else
		{
			return field;
		}
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
