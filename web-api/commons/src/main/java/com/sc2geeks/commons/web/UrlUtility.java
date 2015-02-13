package com.sc2geeks.commons.web;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 10/23/11
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class UrlUtility
{
		public static String removeParameter(String baseUrl, String paramName)
	{
		int startPos;
		if ((startPos = baseUrl.indexOf(paramName + "=")) == -1)
			return baseUrl;

		int endPos = baseUrl.indexOf("&", startPos);
		if (endPos == -1)
			endPos = baseUrl.length();
		String subString = baseUrl.substring(startPos, endPos);
		baseUrl = baseUrl.replace(subString, "");

		if (baseUrl.endsWith("?"))
			baseUrl = StringUtils.removeEnd(baseUrl, "?");

		if (baseUrl.endsWith("&"))
			baseUrl = StringUtils.removeEnd(baseUrl, "&");

		return baseUrl;
	}

	public static String setParameter(String baseUrl, String paramName, String paramValue)
	{
		baseUrl = removeParameter(baseUrl, paramName);
		if (!baseUrl.contains("?"))
			baseUrl = baseUrl + "?";
		else if (!baseUrl.endsWith("&"))
			baseUrl = baseUrl + "&";

		return baseUrl + paramName + "=" + paramValue;
	}

	public static String Utf8Encode(String input)
	{
		return UrlUTF8Encoder.encode(input);
	}
}
