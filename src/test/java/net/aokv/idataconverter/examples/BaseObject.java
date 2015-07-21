package net.aokv.idataconverter.examples;

import java.util.Arrays;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class BaseObject
{
	public Object Element;
	public Object[] Elements;

	@Override
	public int hashCode()
	{
		return Objects.hashCode(Element, Elements);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final BaseObject other = (BaseObject) obj;
		return Objects.equal(Element, other.Element)
				&& Arrays.deepEquals(Elements, other.Elements);
	}
}
