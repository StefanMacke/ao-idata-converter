package net.aokv.idataconverter;

import com.wm.data.IData;

public interface CustomConverter<T extends Object>
{
	IData convertToIData(Object object) throws ObjectConversionException;

	T convertToObject(IData iData) throws IDataConversionException;
}
