package com.sc2geeks.replay.parser;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/12/12
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("response")
public class SC2GearsMapResponse extends SC2GearsResponseBase
{
	public class MapInfo
	{
		@XStreamAsAttribute
		public int height;

		@XStreamAsAttribute
		public String name;

		@XStreamAsAttribute
		public int width;
	}

	@XStreamConverter(value=ToAttributedValueConverter.class, strings={"base64Image"})
	public class MapImageInfo
	{
		@XStreamAsAttribute
		public String format;

		@XStreamAsAttribute
		public int height;

		@XStreamAsAttribute
		public int size;

		@XStreamAsAttribute
		public int width;

		public String base64Image;
	}

	public MapInfo map;

	public MapImageInfo mapImage;
}
