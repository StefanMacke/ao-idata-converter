package net.aokv.idataconverter;

public class IDataConversionException extends Exception
{
	private static final long serialVersionUID = 1L;

	public IDataConversionException(final String message)
	{
		super(message);
	}

	public IDataConversionException(final String message, final Exception innerException)
	{
		super(message, innerException);
	}
}
