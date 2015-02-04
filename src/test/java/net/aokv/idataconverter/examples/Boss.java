package net.aokv.idataconverter.examples;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class Boss
{
	public String FirstName;
	public String LastName;
	public Person Employee;

	@Override
	public String toString()
	{
		return "Boss [FirstName=" + FirstName + ", LastName=" + LastName + ", Employee=" + Employee + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(FirstName, LastName, Employee);
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
		final Boss other = (Boss) obj;
		return Objects.equal(FirstName, other.FirstName)
				&& Objects.equal(LastName, other.LastName)
				&& Objects.equal(Employee, other.Employee);
	}

}
