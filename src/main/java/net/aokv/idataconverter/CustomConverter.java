package net.aokv.idataconverter;

import com.wm.data.IData;

public interface CustomConverter<T extends Object>
{
	IData convertToIData(Object object);

	T convertToObject(IData iData);
}
