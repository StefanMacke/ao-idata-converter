package net.aokv.idataconverter.examples;

import java.util.Arrays;
import java.util.List;

import net.aokv.idataconverter.PipelineName;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class Company
{
	public String Name;

	@PipelineName("AddressLines")
	public String[] Address;

	public List<String> aList;

	public List<Integer> anEmptyList;

	public String StockName;

	@PipelineName("TheBoss")
	private Boss boss;

	public List<Person> Employees;

	public Person[] EmployeesArray;

	public Boss getBoss()
	{
		return boss;
	}

	public void setBoss(final Boss boss)
	{
		this.boss = boss;
	}

	@Override
	public String toString()
	{
		return "Company [Name=" + Name
				+ ", Address=" + Arrays.toString(Address)
				+ ", aList = " + aList
				+ ", anEmptyList = " + anEmptyList
				+ ", StockName=" + StockName
				+ ", boss=" + boss
				+ ", Employees=" + Employees
				+ ", EmployeesArray=" + EmployeesArray + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(Name, Address, aList, anEmptyList, StockName, boss, Employees, EmployeesArray);
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
		final Company other = (Company) obj;
		return Objects.equal(Name, other.Name)
				&& Arrays.equals(Address, other.Address)
				&& Objects.equal(StockName, other.StockName)
				&& Objects.equal(aList, other.aList)
				&& Objects.equal(anEmptyList, other.anEmptyList)
				&& Objects.equal(boss, other.boss)
				&& Objects.equal(Employees, other.Employees)
				&& Objects.equal(EmployeesArray, other.EmployeesArray);
	}
}
