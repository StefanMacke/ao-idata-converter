package net.aokv.idataconverter;

import net.aokv.idataconverter.examples.Address;
import net.aokv.idataconverter.examples.BaseObject;
import net.aokv.idataconverter.examples.Boss;
import net.aokv.idataconverter.examples.Company;
import net.aokv.idataconverter.examples.Employee;
import net.aokv.idataconverter.examples.Person;
import net.aokv.idataconverter.examples.StockPrice;
import net.aokv.idataconverter.examples.Wrapper;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public final class TestHelper
{
	public static IData createPersonIData(final Person person)
	{
		if (person == null)
		{
			return null;
		}
		final IData personIData = IDataFactory.create();
		final IDataCursor personCursor = personIData.getCursor();
		IDataUtil.put(personCursor, "FirstName", person.FirstName);
		IDataUtil.put(personCursor, "LastName", person.LastName);
		return personIData;
	}

	public static IData createBossIData(final Boss boss)
	{
		if (boss == null)
		{
			return null;
		}
		final IData bossIData = IDataFactory.create();
		final IDataCursor bossCursor = bossIData.getCursor();
		IDataUtil.put(bossCursor, "Employee", createPersonIData(boss.Employee));
		IDataUtil.put(bossCursor, "FirstName", boss.FirstName);
		IDataUtil.put(bossCursor, "LastName", boss.LastName);
		return bossIData;
	}

	public static IData createCompanyIData(final Company company)
	{
		int numberOfemployees = 0;
		if (company.Employees != null)
		{
			numberOfemployees = company.Employees.size();
		}
		final IData[] employees = new IData[numberOfemployees];
		for (int i = 0; i < employees.length; i++)
		{
			employees[i] = createPersonIData(company.Employees.get(i));
		}

		final IData companyIData = IDataFactory.create();
		final IDataCursor companyCursor = companyIData.getCursor();
		IDataUtil.put(companyCursor, "AddressLines", company.Address);
		IDataUtil.put(companyCursor, "Employees", employees);
		IDataUtil.put(companyCursor, "Name", company.Name);
		IDataUtil.put(companyCursor, "StockName", company.StockName);
		IDataUtil.put(companyCursor, "TheBoss", createBossIData(company.getBoss()));
		return companyIData;
	}

	public static IData createStockPriceIData(final StockPrice stockprice)
	{
		final IData stockPriceIData = IDataFactory.create();
		final IDataCursor stockPriceCursor = stockPriceIData.getCursor();
		IDataUtil.put(stockPriceCursor, "CompanyName", stockprice.CompanyName);
		IDataUtil.put(stockPriceCursor, "SymbolName", stockprice.Symbol);
		return stockPriceIData;
	}

	public static IData createAddressIData(final Address address)
	{
		final IData addressIData = IDataFactory.create();
		final IDataCursor addressCursor = addressIData.getCursor();
		IDataUtil.put(addressCursor, "ZipCode", address.ZipCode);
		IDataUtil.put(addressCursor, "CityName", address.getCity());
		IDataUtil.put(addressCursor, "Street", address.getStreet());
		return addressIData;
	}

	public static IData createEmployeeIData(final Employee employee)
	{
		final IData employeeIData = IDataFactory.create();
		final IDataCursor employeeCursor = employeeIData.getCursor();
		IDataUtil.put(employeeCursor, "Age", employee.Age);
		IDataUtil.put(employeeCursor, "FirstName", employee.FirstName);
		IDataUtil.put(employeeCursor, "LastName", employee.LastName);
		IDataUtil.put(employeeCursor, "IsRetired", employee.getIsRetired());
		IDataUtil.put(employeeCursor, "Salary", employee.getSalary());
		return employeeIData;
	}

	public static IData createWrapperIData(final Wrapper wrapper)
	{
		final IData input = IDataFactory.create();
		final IDataCursor inputCursor = input.getCursor();
		IDataUtil.put(inputCursor, "Text", wrapper.Input.Text);
		IDataUtil.put(inputCursor, "TheNumber", wrapper.Input.getNumber());
		inputCursor.destroy();
		final IData output = IDataFactory.create();
		final IDataCursor outputCursor = output.getCursor();
		IDataUtil.put(outputCursor, "Result", wrapper.Output.Result);
		outputCursor.destroy();
		final IData expected = IDataFactory.create();
		final IDataCursor expectedCursor = expected.getCursor();
		IDataUtil.put(expectedCursor, "Input", input);
		IDataUtil.put(expectedCursor, "Output", output);
		expectedCursor.destroy();
		return expected;
	}

	public static IData createBaseObjectIData(final BaseObject baseobject)
	{
		final IData baseObject = IDataFactory.create();
		final IDataCursor baseObjectCursor = baseObject.getCursor();
		IDataUtil.put(baseObjectCursor, "Element", baseobject.Element);
		IDataUtil.put(baseObjectCursor, "Elements", baseobject.Elements);
		baseObjectCursor.destroy();
		return baseObject;
	}

	public static IData createIData(final String key, final Object object)
	{
		final IData expected = IDataFactory.create();
		final IDataCursor idc = expected.getCursor();
		IDataUtil.put(idc, key, object);
		return expected;
	}

	public static Person createPerson(final String firstName, final String lastName)
	{
		final Person person = new Person();
		person.FirstName = firstName;
		person.LastName = lastName;
		return person;
	}

	public static Boss createBoss(final String firstName, final String lastName, final Person employee)
	{
		final Boss boss = new Boss();
		boss.FirstName = firstName;
		boss.LastName = lastName;
		boss.Employee = employee;
		return boss;
	}

	public static Employee createEmployee(
			final String firstName, final String lastName, final int age, final double salary, final boolean isRetired)
	{
		final Employee employee = new Employee();
		employee.FirstName = firstName;
		employee.LastName = lastName;
		employee.Age = age;
		employee.setSalary(salary);
		employee.setIsRetired(isRetired);
		return employee;
	}

}
