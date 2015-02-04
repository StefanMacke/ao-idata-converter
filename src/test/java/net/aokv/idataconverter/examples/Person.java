package net.aokv.idataconverter.examples;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class Person
{
	public String FirstName;
	public String LastName;

	@Override
	public String toString()
	{
		return "Person [FirstName=" + FirstName + ", LastName=" + LastName + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(FirstName, LastName);
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
		final Person other = (Person) obj;
		return Objects.equal(FirstName, other.FirstName)
				&& Objects.equal(LastName, other.LastName);
	}
}
