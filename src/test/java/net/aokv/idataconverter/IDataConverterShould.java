package net.aokv.idataconverter;

import static net.aokv.idataconverter.TestHelper.createAddressIData;
import static net.aokv.idataconverter.TestHelper.createBaseObjectIData;
import static net.aokv.idataconverter.TestHelper.createBoss;
import static net.aokv.idataconverter.TestHelper.createBossIData;
import static net.aokv.idataconverter.TestHelper.createCompanyIData;
import static net.aokv.idataconverter.TestHelper.createEmployee;
import static net.aokv.idataconverter.TestHelper.createEmployeeIData;
import static net.aokv.idataconverter.TestHelper.createIData;
import static net.aokv.idataconverter.TestHelper.createPerson;
import static net.aokv.idataconverter.TestHelper.createPersonIData;
import static net.aokv.idataconverter.TestHelper.createStockPriceIData;
import static net.aokv.idataconverter.TestHelper.createWrapperIData;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.aokv.idataconverter.examples.Address;
import net.aokv.idataconverter.examples.BaseObject;
import net.aokv.idataconverter.examples.Boss;
import net.aokv.idataconverter.examples.Company;
import net.aokv.idataconverter.examples.Employee;
import net.aokv.idataconverter.examples.Person;
import net.aokv.idataconverter.examples.StockPrice;
import net.aokv.idataconverter.examples.Wrapper;
import net.aokv.idataconverter.examples.Wrapper.Input;
import net.aokv.idataconverter.examples.Wrapper.Output;

import org.junit.Before;
import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class IDataConverterShould
{
	private IDataConverter sut;

	@Before
	public void setUp()
	{
		sut = new IDataConverter();
	}

	@Test
	public void convertNullToNull() throws IDataConversionException
	{
		final Object actual = sut.convertToObject(null, "irrelevant", String.class);
		assertThat(actual, nullValue());
	}

	@Test
	public void convertNullWithoutFieldNameToNull() throws IDataConversionException
	{
		final Object actual = sut.convertToObject(null, String.class);
		assertThat(actual, nullValue());
	}

	@Test
	public void convertEmptyIDataToNull() throws IDataConversionException
	{
		final IData iData = IDataFactory.create();
		final Object actual = sut.convertToObject(iData, "irrelevant", String.class);
		assertThat(actual, nullValue());
	}

	@Test
	public void convertNullValueInIDataToNull() throws IDataConversionException
	{
		final IData iData = createIData("value", null);
		final Object actual = sut.convertToObject(iData, "value", String.class);
		assertThat(actual, nullValue());
	}

	@Test
	public void throwsExceptionIfFieldDoesNotExistInIData() throws IDataConversionException
	{
		final IData iData = createIData("value", 1);

		try
		{
			sut.convertToObject(iData, "doesnotexist", String.class);
			fail("Exception should be thrown if field does not exist in IData");
		}
		catch (final IDataConversionException e)
		{
			assertThat(e.getMessage(), containsString("Field <doesnotexist> does not exist"));
		}
	}

	@Test
	public void convertStrings() throws IDataConversionException
	{
		final IData iData = createIData("value", "The String");
		final String actual = sut.convertToObject(iData, "value", String.class);
		assertThat(actual, is("The String"));
	}

	@Test
	public void convertPrimitiveDataTypes() throws IDataConversionException
	{
		final IData iData = IDataFactory.create();
		final IDataCursor iDataCursor = iData.getCursor();
		IDataUtil.put(iDataCursor, "byte", Byte.MAX_VALUE);
		IDataUtil.put(iDataCursor, "short", Short.MAX_VALUE);
		IDataUtil.put(iDataCursor, "int", Integer.MAX_VALUE);
		IDataUtil.put(iDataCursor, "long", Long.MAX_VALUE);
		IDataUtil.put(iDataCursor, "float", Float.MAX_VALUE);
		IDataUtil.put(iDataCursor, "double", Double.MAX_VALUE);
		IDataUtil.put(iDataCursor, "char", 'a');
		IDataUtil.put(iDataCursor, "boolean", true);
		iDataCursor.destroy();

		assertThat(sut.convertToObject(iData, "byte", Byte.class), is(Byte.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "short", Short.class), is(Short.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "int", Integer.class), is(Integer.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "long", Long.class), is(Long.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "float", Float.class), is(Float.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "double", Double.class), is(Double.MAX_VALUE));
		assertThat(sut.convertToObject(iData, "char", Character.class), is('a'));
		assertThat(sut.convertToObject(iData, "boolean", Boolean.class), is(true));
	}

	@Test
	public void convertStringArrays() throws IDataConversionException
	{
		final String[] array = new String[]
		{ "String1", "String2" };
		final IData iData = createIData("array", array);
		final String[] actual = sut.convertToObject(iData, "array", String[].class);
		assertThat(actual, is(array));
	}

	@Test
	public void convertNullArrayToNull() throws IDataConversionException
	{
		final IData iData = createIData("array", null);
		final String[] actual = sut.convertToObject(iData, "array", String[].class);
		assertThat(actual, nullValue());
	}

	@Test
	public void convertSimpleArrays() throws IDataConversionException
	{
		final int[] array = new int[]
		{ 1, 2 };
		final IData iData = createIData("array", array);
		final int[] actual = sut.convertToObject(iData, "array", int[].class);
		assertThat(actual, is(array));

		final Double[] dArray = new Double[]
		{ 1.1, 2.2 };
		final IData dIData = createIData("array", dArray);
		final Double[] dActual = sut.convertToObject(dIData, "array", Double[].class);
		assertThat(dActual, is(dArray));
	}

	@Test
	public void convertTwoDimensionalStringArrays() throws IDataConversionException
	{
		final String[][] array = new String[][]
		{
				{ "String1", "String2" },
				{ "String3", "String4" }
		};
		final IData iData = createIData("value", array);
		final String[][] actual = sut.convertToObject(iData, "value", String[][].class);
		assertThat(actual, is(array));
	}

	@Test
	public void convertObjectsWithStringFields() throws IDataConversionException
	{
		final Person personObject = createPerson("Stefan", "Macke");
		final IData personIData = createPersonIData(personObject);
		final Person actual = sut.convertToObject(personIData, Person.class);
		assertThat(actual, is(personObject));
	}

	@Test
	public void convertObjectsWithFieldName() throws IDataConversionException
	{
		final Person personObject = createPerson("Stefan", "Macke");
		final IData personIData = createPersonIData(personObject);
		final IData pipeline = createIData("person", personIData);
		final Person actual = sut.convertToObject(pipeline, "person", Person.class);
		assertThat(actual, is(personObject));
	}

	@Test
	public void convertObjectsFromPipelineWithSuperfluousFields() throws IDataConversionException
	{
		final Person personObject = createPerson("Stefan", "Macke");
		final IData personIData = createPersonIData(personObject);

		final IData pipeline = IDataFactory.create();
		final IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put(pipelineCursor, "irrelevant1", "Test");
		IDataUtil.append(personIData, pipeline);
		IDataUtil.put(pipelineCursor, "irrelevant2", "Test");

		final Person actual = sut.convertToObject(personIData, Person.class);
		assertThat(actual, is(personObject));
	}

	@Test
	public void convertStringLists() throws IDataConversionException
	{
		final String[] array = new String[]
		{ "String1", "String2" };
		final IData iData = createIData("array", array);
		final List<String> expected = new ArrayList<>();
		expected.add("String1");
		expected.add("String2");

		@SuppressWarnings("unchecked")
		final List<String> actual = sut.convertToObject(iData, "array", expected.getClass(), String.class);

		assertThat(actual, is(expected));
	}

	@Test
	public void convertNullListsToNull() throws IDataConversionException
	{
		final IData iData = createIData("array", null);

		@SuppressWarnings("unchecked")
		final List<String> actual = sut.convertToObject(iData, "array", ArrayList.class, String.class);

		assertThat(actual, nullValue());
	}

	@Test
	public void convertObjectArrays() throws IDataConversionException
	{
		final Person person1 = createPerson("Stefan", "Macke");
		final Person person2 = createPerson("Hans", "Meier");
		final IData[] personIData = new IData[]
		{
				createPersonIData(person1),
				createPersonIData(person2)
		};

		final Person[] actual = sut.convertToObject(personIData, Person[].class);
		assertThat(actual, is(new Person[]
		{ person1, person2 }));
	}

	@Test
	public void convertObjectLists() throws IDataConversionException
	{
		final Person person1 = createPerson("Stefan", "Macke");
		final Person person2 = createPerson("Hans", "Meier");
		final IData[] personIData = new IData[]
		{
				createPersonIData(person1),
				createPersonIData(person2)
		};

		final List<Person> expected = new ArrayList<>();
		expected.add(person1);
		expected.add(person2);

		@SuppressWarnings("unchecked")
		final List<Person> actual = sut.convertToObject(personIData, expected.getClass(), Person.class);
		assertThat(actual, is(expected));
	}

	@Test
	public void convertObjectsWithObjectFields() throws IDataConversionException
	{
		final Person personObject = createPerson("Stefan", "Macke");
		final Boss bossObject = createBoss("Hans", "Meier", personObject);
		final IData bossIData = createBossIData(bossObject);
		final Boss actual = sut.convertToObject(bossIData, Boss.class);
		assertThat(actual, is(bossObject));
	}

	@Test
	public void convertObjectsWithNullObjectFields() throws IDataConversionException
	{
		final Boss bossObject = createBoss("Hans", "Meier", null);
		final IData bossIData = createBossIData(bossObject);
		final Boss actual = sut.convertToObject(bossIData, Boss.class);
		assertThat(actual, is(bossObject));
	}

	@Test
	public void convertComplexObjects() throws IDataConversionException
	{
		final Person employee1 = createPerson("Hans", "Meier");
		final Person employee2 = createPerson("Gustav", "Gans");
		final Boss boss = createBoss("Stefan", "Macke", employee1);
		final Company companyObject = new Company();
		companyObject.Name = "My Company";
		companyObject.Address = new String[]
				{ "Street 1", "City" };
		companyObject.StockName = null;
		companyObject.setBoss(boss);
		companyObject.Employees = new ArrayList<>();
		companyObject.Employees.add(employee1);
		companyObject.Employees.add(employee2);
		final IData companyIData = createCompanyIData(companyObject);

		final Company actual = sut.convertToObject(companyIData, Company.class);

		assertThat(actual, is(companyObject));
	}

	@Test
	public void convertComplexObjectsWithNullValues() throws IDataConversionException
	{
		final Company companyObject = new Company();
		final IData companyIData = createCompanyIData(companyObject);

		final Company actual = sut.convertToObject(companyIData, Company.class);

		companyObject.Employees = new ArrayList<>();
		assertThat(actual, is(companyObject));
	}

	@Test
	public void useAnnotatedPipelineNamesIfPresent() throws IDataConversionException
	{
		final StockPrice stockPriceObject = new StockPrice();
		stockPriceObject.CompanyName = "My Company";
		stockPriceObject.Symbol = "MCO";
		final IData stockPriceIData = createStockPriceIData(stockPriceObject);
		final StockPrice actual = sut.convertToObject(stockPriceIData, StockPrice.class);
		assertThat(actual, is(stockPriceObject));
	}

	@Test
	public void useGettersAndSettersIfPresentAfterUsingFields() throws IDataConversionException
	{
		final Address addressObject = new Address();
		addressObject.setStreet("My Street 123");
		addressObject.ZipCode = "12345";
		addressObject.setCity("My City");
		final IData addressIData = createAddressIData(addressObject);
		final Address actual = sut.convertToObject(addressIData, Address.class);
		assertThat(actual, is(addressObject));
	}

	@Test
	public void convertSubclassesWithPrimitiveFieldsOtherThanString() throws IDataConversionException
	{
		final Employee employeeObject = createEmployee("Stefan", "Macke", 30, 123.12, false);
		final IData employeeIData = createEmployeeIData(employeeObject);
		final Employee actual = sut.convertToObject(employeeIData, Employee.class);
		assertThat(actual, is(employeeObject));
	}

	@Test
	public void convertInnerClasses() throws IDataConversionException
	{
		final Wrapper wrapperObject = new Wrapper();
		wrapperObject.Input = new Input();
		wrapperObject.Input.Text = "Hallo";
		wrapperObject.Input.setNumber(42);
		wrapperObject.Output = new Output();
		wrapperObject.Output.Result = "Message";

		final IData wrapperIData = createWrapperIData(wrapperObject);
		final Wrapper actual = sut.convertToObject(wrapperIData, Wrapper.class);
		assertThat(actual, is(wrapperObject));
	}

	@Test
	public void convertObjectFields() throws IDataConversionException
	{
		final BaseObject baseObject = new BaseObject();
		baseObject.Element = "Test";
		baseObject.Elements = new Object[]
				{ "Test", 123 };

		final IData baseObjectIData = createBaseObjectIData(baseObject);
		final BaseObject actual = sut.convertToObject(baseObjectIData, BaseObject.class);
		assertThat(actual, is(baseObject));
	}
}