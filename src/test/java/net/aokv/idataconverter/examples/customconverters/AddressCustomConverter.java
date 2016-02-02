package net.aokv.idataconverter.examples.customconverters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

import net.aokv.idataconverter.CustomConverter;
import net.aokv.idataconverter.examples.Address;
import net.aokv.idataconverter.examples.Country;

public class AddressCustomConverter implements CustomConverter<Address>
{
	@Override
	public IData convertToIData(final Object object)
	{
		final Address address = (Address) object;
		final IData iData = IDataFactory.create();
		final IDataCursor addressCursor = iData.getCursor();
		IDataUtil.put(addressCursor, "ShortStreet", address.getStreet());
		IDataUtil.put(addressCursor, "ShortCity",
				address.country.toString() + "-" + address.ZipCode + " " + address.getCity());
		return iData;
	}

	@Override
	public Address convertToObject(final IData iData)
	{
		final Address a = new Address();

		final IDataCursor addressCursor = iData.getCursor();
		final String shortCity = IDataUtil.getString(addressCursor, "ShortCity");
		final String shortStreet = IDataUtil.getString(addressCursor, "ShortStreet");
		addressCursor.destroy();

		a.setStreet(shortStreet);
		final Pattern p = Pattern.compile("(.+)-([0-9]+) (.*)");
		final Matcher m = p.matcher(shortCity);
		if (m.matches())
		{
			a.country = Country.valueOf(m.group(1));
			a.ZipCode = m.group(2);
			a.setCity(m.group(3));
		}

		return a;
	}
}
