package com.sc2geeks.commons.serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/3/11
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomXStream extends XStream
{
	@Override
	protected MapperWrapper wrapMapper(MapperWrapper next)
	{
		return new MapperWrapper(next)
		{
			@Override
			public boolean shouldSerializeMember(Class definedIn,
			                                     String fieldName)
			{
				if (definedIn == Object.class)
				{
					return false;
				}
				return super.shouldSerializeMember(definedIn, fieldName);
			}
		};
	}
}
