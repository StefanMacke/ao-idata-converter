package net.aokv.idataconverter.examples;

import net.aokv.idataconverter.PipelineName;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class StockPrice
{
	public String CompanyName;

	@PipelineName("SymbolName")
	public String Symbol;

	@Override
	public String toString()
	{
		return "StockPrice [CompanyName=" + CompanyName + ", Symbol=" + Symbol + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(CompanyName, Symbol);
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
		final StockPrice other = (StockPrice) obj;
		return Objects.equal(CompanyName, other.CompanyName)
				&& Objects.equal(Symbol, other.Symbol);
	}

}
