package com.sc2geeks.replay.parser;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/12/12
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class SC2GearsResponseBase
{
	@XStreamConverter(value=ToAttributedValueConverter.class, strings={"description"})
	public class ResultLine
	{
		String description;

		@XStreamAsAttribute
		public int code;

	}
	@XStreamAlias("result")
	public ResultLine resultLine;



}
