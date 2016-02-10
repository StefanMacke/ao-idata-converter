package net.aokv.idataconverter.examples.customconverters;

import com.google.common.base.Objects;

import net.aokv.idataconverter.UseCustomConverter;
import net.aokv.idataconverter.examples.Address;

@SuppressWarnings("PMD")
public class CustomWrapper
{
	@UseCustomConverter(AddressCustomConverter.class)
	public Address Address;

	@UseCustomConverter(AddressCustomConverter.class)
	public Address[] Addresses;

	public String test;

	@Override
	public int hashCode()
	{
		return Objects.hashCode(Address, test);
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
		final CustomWrapper other = (CustomWrapper) obj;
		return Objects.equal(Address, other.Address)
				&& Objects.equal(test, other.test);
	}

}
