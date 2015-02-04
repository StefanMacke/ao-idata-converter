package net.aokv.idataconverter.examples;

import net.aokv.idataconverter.PipelineName;

import com.google.common.base.Objects;

@SuppressWarnings("PMD")
public class Wrapper
{
	public static class Input
	{
		public String Text;

		@PipelineName("TheNumber")
		private int number;

		public int getNumber()
		{
			return number;
		}

		public void setNumber(final int number)
		{
			this.number = number;
		}
	}

	public static class Output
	{
		public String Result;
	}

	public Input Input;
	public Output Output;

	@Override
	public int hashCode()
	{
		return Objects.hashCode(Input.Text, Input.getNumber(), Output.Result);
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
		final Wrapper other = (Wrapper) obj;
		return Objects.equal(Input.Text, other.Input.Text)
				&& Objects.equal(Input.getNumber(), other.Input.getNumber())
				&& Objects.equal(Output.Result, other.Output.Result);
	}

}
