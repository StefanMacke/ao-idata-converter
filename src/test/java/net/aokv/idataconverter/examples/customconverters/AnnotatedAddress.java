package net.aokv.idataconverter.examples.customconverters;

import com.google.common.base.Objects;

import net.aokv.idataconverter.PipelineName;
import net.aokv.idataconverter.UseCustomConverter;
import net.aokv.idataconverter.examples.Country;

@UseCustomConverter(AnnotatedAddressCustomConverter.class)
public class AnnotatedAddress
{
	private String street;

	public String ZipCode;

	@PipelineName("CityName")
	private String city;

	public Country country;

	public String getStreet()
	{
		return street;
	}

	public void setStreet(final String street)
	{
		this.street = street;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	@Override
	public String toString()
	{
		return "Address [street=" + street + ", ZipCode=" + ZipCode + ", city=" + city + ", country=" + country + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(street, ZipCode, city);
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
		final AnnotatedAddress other = (AnnotatedAddress) obj;
		return Objects.equal(street, other.street)
				&& Objects.equal(ZipCode, other.ZipCode)
				&& Objects.equal(city, other.city)
				&& Objects.equal(country, other.country);
	}
}
