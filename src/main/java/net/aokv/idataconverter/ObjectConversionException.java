package net.aokv.idataconverter;

public class ObjectConversionException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ObjectConversionException(final String message, final Exception innerException)
	{
		super(message, innerException);
	}
}
