package net.aokv.idataconverter;

import static net.aokv.idataconverter.TestHelper.createAddressIData;
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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataFormatter;
import com.wm.data.IDataUtil;

import net.aokv.idataconverter.examples.Address;
import net.aokv.idataconverter.examples.Boss;
import net.aokv.idataconverter.examples.Company;
import net.aokv.idataconverter.examples.Country;
import net.aokv.idataconverter.examples.Employee;
import net.aokv.idataconverter.examples.Person;
import net.aokv.idataconverter.examples.StockPrice;
import net.aokv.idataconverter.examples.Wrapper;
import net.aokv.idataconverter.examples.Wrapper.Input;
import net.aokv.idataconverter.examples.Wrapper.Output;
import net.aokv.idataconverter.examples.customconverters.AddressCustomConverter;
import net.aokv.idataconverter.examples.customconverters.AnnotatedAddress;
import net.aokv.idataconverter.examples.customconverters.CustomWrapper;

public class ObjectConverterShould
{
	private ObjectConverter sut;

	@Before
	public void setUp()
	{
		sut = new ObjectConverter();
	}

	@SuppressWarnings("unchecked")
	private <T> void assertIDataContains(final IData idata, final String key, final T expectedValue)
	{
		final IDataCursor cursor = idata.getCursor();
		final T actualValue = (T) IDataUtil.get(cursor, key);
		assertThat(String.format("Value for key <%s> should be <%s>, but was <%s>.",
				key, expectedValue, actualValue),
				actualValue, is(expectedValue));
		assertTypesEqual(actualValue, expectedValue);
	}

	private void assertTypesEqual(final Object actual, final Object expected)
	{
		if (actual != null && expected != null)
		{
			// make sure that also the types are identical (Object[] != String[])
			// [ISC.0082.9030] Type mismatch, String expected'
			assertThat(actual.getClass().toString(), is(expected.getClass().toString()));
		}
	}

	private void assertIDataOnlyContainsKeys(final IData idata, final String... keys)
	{
		final IDataCursor cursor = idata.getCursor();
		int keyCounter = 0;
		for (final String expectedKey : keys)
		{
			keyCounter++;
			assertKeyExists(cursor, keyCounter, expectedKey);
		}
		failIfAnyMoreData(cursor);
	}

	private void assertKeyExists(final IDataCursor cursor, final int keyCounter, final String expectedKey)
	{
		if (cursor.next())
		{
			final String actualKey = cursor.getKey();
			assertThat(String.format("Key at position %d should be <%s>, but was <%s>.",
					keyCounter, expectedKey, actualKey),
					actualKey, is(expectedKey));
			assertTypesEqual(actualKey, expectedKey);
		}
		else
		{
			fail(String.format("Key at position %d should be <%s>, but does not exist.",
					keyCounter, expectedKey));
		}
	}

	private void failIfAnyMoreData(final IDataCursor cursor)
	{
		if (cursor.hasMoreData())
		{
			cursor.next();
			final String unexpectedKey = cursor.getKey();
			fail(String.format("IData should not contain any more data, but contains key <%s>.", unexpectedKey));
		}
	}

	private <T> void assertIDataOnlyContains(final IData idata, final String key, final T value)
	{
		assertIDataOnlyContainsKeys(idata, key);
		assertIDataContains(idata, key, value);
	}

	private void assertIDataEquals(final IData actual, final IData expected)
	{
		final IDataFormatter formatter = new IDataFormatter();
		assertThat(
				String.format("IData should be %n<%s>%n, but was %n<%s>%n.",
						formatter.format(expected),
						formatter.format(actual)),
				IDataUtil.equals(actual, expected), is(true));
	}

	@Test
	public void convertNull() throws Exception
	{
		final IData idata = sut.convertToIData("Null", null);
		assertIDataOnlyContains(idata, "Null", null);
	}

	@Test
	public void convertStrings() throws Exception
	{
		final IData idata = sut.convertToIData("name", "Stefan");
		assertIDataOnlyContains(idata, "name", "Stefan");
	}

	@Test
	public void convertPrimitiveDataTypes() throws Exception
	{
		IData idata = sut.convertToIData("int", 123);
		assertIDataOnlyContains(idata, "int", "123");

		idata = sut.convertToIData("double", 123.45);
		assertIDataOnlyContains(idata, "double", "123.45");

		idata = sut.convertToIData("bool", true);
		assertIDataOnlyContains(idata, "bool", "true");

		idata = sut.convertToIData("char", 'a');
		assertIDataOnlyContains(idata, "char", "a");
	}

	@Test
	public void convertEnumerations() throws Exception
	{
		final IData idata = sut.convertToIData("country", Country.Germany);
		assertIDataOnlyContains(idata, "country", "Germany");
	}

	@Test
	public void convertStringArrays() throws Exception
	{
		final IData idata = sut.convertToIData("array", new String[]
		{ "1", "2", "3" });
		assertIDataOnlyContains(idata, "array", new String[]
		{ "1", "2", "3" });
	}

	@Test
	public void convertSimpleArrays() throws Exception
	{
		IData idata = sut.convertToIData("array", new int[]
		{ 1, 2, 3 });
		assertIDataOnlyContains(idata, "array", new String[]
		{ "1", "2", "3" });

		idata = sut.convertToIData("array", new Double[]
		{ 1.1, 2.2, 3.3 });
		assertIDataOnlyContains(idata, "array", new String[]
		{ "1.1", "2.2", "3.3" });
	}

	@Test
	public void convertTwoDimensionalStringArrays() throws Exception
	{
		final IData idata = sut.convertToIData("array", new String[][]
		{
				{ "1", "2", "3" },
				{ "4", "5", "6" },
				{ "7", "8", "9" }
		});
		assertIDataOnlyContains(idata, "array", new String[][]
		{
				{ "1", "2", "3" },
				{ "4", "5", "6" },
				{ "7", "8", "9" }
		});
	}

	@Test
	public void convertTwoDimensionalPrimitiveArrays() throws Exception
	{
		final IData idata = sut.convertToIData("array", new int[][]
		{
				{ 1, 2, 3 },
				{ 4, 5, 6 },
				{ 7, 8, 9 }
		});
		assertIDataOnlyContains(idata, "array", new String[][]
		{
				{ "1", "2", "3" },
				{ "4", "5", "6" },
				{ "7", "8", "9" }
		});
	}

	@Test
	public void convertObjectsWithStringFields() throws Exception
	{
		final Person personObject = createPerson("Stefan", "Macke");

		final IData personIData = createPersonIData(personObject);
		final IData expected = createIData("person", personIData);

		final IData actual = sut.convertToIData("person", personObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void convertObjectsWithoutFieldName() throws Exception
	{
		final Person personObject = createPerson("Stefan", "Macke");
		final IData expected = createPersonIData(personObject);

		final Object actual = sut.convertToIDataValue(personObject);

		assertIDataEquals((IData) actual, expected);
	}

	@Test
	public void convertObjectArrays() throws Exception
	{
		final Person person1 = createPerson("Stefan", "Macke");
		final Person person2 = createPerson("Hans", "Meier");

		final IData person1IData = createPersonIData(person1);
		final IData person2IData = createPersonIData(person2);
		final IData expected = createIData("people", new IData[]
		{ person1IData, person2IData });

		final IData actual = sut.convertToIData("people", new Person[]
		{ person1, person2 });

		assertIDataEquals(actual, expected);

		final IData[] actualArray = IDataUtil.getIDataArray(actual.getCursor(), "people");
		final IData[] expectedArray = IDataUtil.getIDataArray(expected.getCursor(), "people");
		assertThat(actualArray.getClass().toString(), is(expectedArray.getClass().toString()));
	}

	@Test
	public void convertEmptyObjectArrays() throws Exception
	{
		final IData[] expected = new IData[0];
		final IData actual = sut.convertToIData("people", new Person[] {});
		assertIDataContains(actual, "people", expected);
	}

	@Test
	public void convertStringLists() throws Exception
	{
		final List<String> names = new ArrayList<>();
		names.add("Stefan");
		names.add("Hans");

		final IData actual = sut.convertToIData("names", names);

		assertIDataOnlyContains(actual, "names", new String[]
		{ "Stefan", "Hans" });
	}

	@Test
	public void convertEmptyStringArrays() throws Exception
	{
		final String[] names = new String[0];
		final IData actual = sut.convertToIData("names", names);
		assertIDataOnlyContains(actual, "names", new String[] {});
	}

	@Test
	public void convertEmptyStringLists() throws Exception
	{
		final List<String> names = new ArrayList<>();
		final IData actual = sut.convertToIData("names", names, String.class);
		assertIDataOnlyContains(actual, "names", new String[] {});
	}

	@Test
	public void convertObjectLists() throws Exception
	{
		final Person person1 = createPerson("Stefan", "Macke");
		final Person person2 = createPerson("Hans", "Meier");

		final IData person1IData = createPersonIData(person1);
		final IData person2IData = createPersonIData(person2);
		final IData expected = createIData("people", new IData[]
		{ person1IData, person2IData });

		final IData actual = sut.convertToIData("people", Arrays.asList(new Person[]
		{ person1, person2 }));

		assertIDataEquals(actual, expected);

		final IData[] actualArray = IDataUtil.getIDataArray(actual.getCursor(), "people");
		final IData[] expectedArray = IDataUtil.getIDataArray(expected.getCursor(), "people");
		assertThat(actualArray.getClass().toString(), is(expectedArray.getClass().toString()));
	}

	@Test
	public void convertEmptyObjectLists() throws Exception
	{
		final IData[] expected = new IData[0];
		final IData actual = sut.convertToIData("people", new ArrayList<Person>());
		assertIDataContains(actual, "people", expected);
	}

	@Test
	public void convertObjectWithObjectFields() throws Exception
	{
		final Person personObject = createPerson("Hans", "Meier");
		final Boss bossObject = createBoss("Stefan", "Macke", personObject);

		final IData bossIData = createBossIData(bossObject);
		final IData expected = createIData("boss", bossIData);

		final IData actual = sut.convertToIData("boss", bossObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void convertComplexObjects() throws Exception
	{
		final Person employee1 = createPerson("Hans", "Meier");
		final Person employee2 = createPerson("Gustav", "Gans");
		final Boss boss = createBoss("Stefan", "Macke", employee1);
		final Company companyObject = new Company();
		companyObject.Name = "My Company";
		companyObject.Address = new String[]
		{ "Street 1", "City" };
		companyObject.aList = new ArrayList<String>();
		companyObject.aList.add("aValue");
		companyObject.aList.add("anotherValue");
		companyObject.anEmptyList = new ArrayList<Integer>();
		companyObject.StockName = null;
		companyObject.setBoss(boss);
		companyObject.Employees = new ArrayList<>();
		companyObject.Employees.add(employee1);
		companyObject.Employees.add(employee2);
		companyObject.EmployeesArray = new Person[]
		{ employee1, employee2 };

		final IData companyIData = createCompanyIData(companyObject);
		final IData expected = createIData("company", companyIData);

		final IData actual = sut.convertToIData("company", companyObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void useAnnotatedPipelineNamesIfPresent() throws Exception
	{
		final StockPrice stockPriceObject = new StockPrice();
		stockPriceObject.CompanyName = "My Company";
		stockPriceObject.Symbol = "MCO";
		final IData stockPriceIData = createStockPriceIData(stockPriceObject);
		final IData expected = createIData("stockprice", stockPriceIData);

		final IData actual = sut.convertToIData("stockprice", stockPriceObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void useGettersAndSettersIfPresentAfterUsingFields() throws Exception
	{
		final Address addressObject = TestHelper.createExampleAddress();
		final IData addressIData = createAddressIData(addressObject);
		final IData expected = createIData("address", addressIData);

		final IData actual = sut.convertToIData("address", addressObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void convertSubclassesWithPrimitiveFieldsOtherThanString() throws Exception
	{
		final Employee employeeObject = createEmployee("Stefan", "Macke", 30, 123.12, false);
		final IData employeeIData = createEmployeeIData(employeeObject);
		final IData expected = createIData("employee", employeeIData);

		final IData actual = sut.convertToIData("employee", employeeObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void convertInnerClasses() throws Exception
	{
		final Wrapper wrapperObject = new Wrapper();
		wrapperObject.Input = new Input();
		wrapperObject.Input.Text = "Hallo";
		wrapperObject.Input.setNumber(42);
		wrapperObject.Output = new Output();
		wrapperObject.Output.Result = "Message";
		final IData wrapperIData = createWrapperIData(wrapperObject);
		final IData expected = createIData("wrapper", wrapperIData);

		final IData actual = sut.convertToIData("wrapper", wrapperObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void convertDates() throws Exception
	{
		final Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
		final IData expected = createIData("date", "2015-01-01");
		final IData actual = sut.convertToIData("date", date);
		assertIDataEquals(actual, expected);
	}

	@Test
	public void useCustomConverterIfPresent() throws Exception
	{
		final Address addressObject = TestHelper.createExampleAddress();

		final IData addressIData = IDataFactory.create();
		final IDataCursor addressCursor = addressIData.getCursor();
		IDataUtil.put(addressCursor, "ShortStreet", addressObject.getStreet());
		IDataUtil.put(addressCursor, "ShortCity",
				addressObject.country.toString() + "-" + addressObject.ZipCode + " " + addressObject.getCity());

		final IData expected = createIData("address", addressIData);

		sut.addCustomConverter(Address.class, new AddressCustomConverter());
		final IData actual = sut.convertToIData("address", addressObject);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void useCustomConverterForAnnotatedFields() throws Exception
	{
		final Address addressObject = TestHelper.createExampleAddress();
		final CustomWrapper wrapper = new CustomWrapper();
		wrapper.Address = addressObject;
		wrapper.Addresses = new Address[]
		{ TestHelper.createExampleAddress(), TestHelper.createExampleAddress() };
		wrapper.test = "A test string";

		final IData expected = TestHelper.createCustomWrapperIData(wrapper);
		final IData actual = sut.convertToIData(wrapper);

		assertIDataEquals(actual, expected);
	}

	@Test
	public void useCustomConverterForAnnotatedClasses() throws Exception
	{
		final AnnotatedAddress addressObject = TestHelper.createExampleAnnotatedAddress();

		final IData expected = TestHelper.createShortAddressIData(TestHelper.createExampleAddress());
		final IData actual = sut.convertToIData(addressObject);

		assertIDataEquals(actual, expected);
	}

}