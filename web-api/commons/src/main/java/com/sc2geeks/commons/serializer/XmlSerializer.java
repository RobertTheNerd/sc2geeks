package com.sc2geeks.commons.serializer;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class XmlSerializer
{
	private static Logger logger = Logger.getLogger(XmlSerializer.class);

	public static <T> T deserializeFromString(String input)
	{
		return deserializeFromString(input, null);
	}

	public static <T> T deserializeFromString(String input, Class<T> c)
	{
		try
		{
			XStream xstream = new CustomXStream();
			if (c != null)
				xstream.processAnnotations(c);
			return (T) xstream.fromXML(input);
		} catch (Exception e)
		{
			logger.error("Failed to deserialize from string.", e);
			return null;
		}
	}

	public static <T> T deserializeFromFile(String fileName, Class<T> c)
	{
		return deserializeFromFile(fileName, "UTF-8", c);
	}
	public static <T> T deserializeFromFile(String fileName, String encoding, Class<T> c)
	{
		try
		{
			String content = FileUtils.readFileToString(new File(fileName), encoding);
			return deserializeFromString(content, c);
		}
		catch (Exception e)
		{
			logger.error("Failed to deserialize from file : " + fileName, e);
			return null;
		}
	}

}
