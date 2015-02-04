package net.aokv.idataconverter.examples;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class Employee extends Person
{
	public int Age;
	private double salary;
	private boolean isRetired;

	public double getSalary()
	{
		return salary;
	}

	public void setSalary(final double salary)
	{
		this.salary = salary;
	}

	public boolean getIsRetired()
	{
		return isRetired;
	}

	public void setIsRetired(final boolean isRetired)
	{
		this.isRetired = isRetired;
	}

	@Override
	public String toString()
	{
		return "Employee [Age=" + Age + ", salary=" + salary + ", isRetired=" + isRetired + ", FirstName=" + FirstName + ", LastName=" + LastName + "]";
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() + Objects.hashCode(Age, salary, isRetired);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Employee other = (Employee) obj;
		return Objects.equal(Age, other.Age)
				&& Objects.equal(salary, other.salary)
				&& Objects.equal(isRetired, other.isRetired);
	}

}
