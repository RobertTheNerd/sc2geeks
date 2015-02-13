package com.sc2geeks.front.ui;

import com.sc2geeks.commons.web.UrlUTF8Encoder;
import com.sc2geeks.front.WebsiteConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/27/12
 * Time: 5:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class PageUrlHelper
{
	protected static WebsiteConfig websiteConfig = WebsiteConfig.getInstance();

	protected String baseUrl;
	protected String actionPath;
	protected String queryString;
	protected LinkedHashMap<String, String> queryStringMap = new LinkedHashMap<String, String>();

	protected String stringValue;


	public PageUrlHelper(String namespace, String actionPath, String queryString)
	{
		baseUrl = StringUtils.removeEnd(websiteConfig.getWebsiteRoot(), "/") + (namespace == null ? "" : namespace);
		if (!baseUrl.endsWith("/"))
			baseUrl += "/";

		this.actionPath = actionPath;
		this.queryString = queryString;
		parse();
	}

	private void parse()
	{
		if (StringUtils.isBlank(queryString))
			return;
		String[] parts = queryString.split("&");
		for (String part : parts)
		{
			String[] nameValue = part.split("=", 2);
			if (nameValue == null || nameValue.length == 0)
				continue;

			String name = nameValue[0].toLowerCase().trim();
			if (StringUtils.isBlank(name))
				continue;

			String value;
			if (nameValue.length > 1)
				value = nameValue[1];
			else
				value = part.contains("=") ? "" : null;

			// do not add empty search terms
			if (name.compareToIgnoreCase(QueryStringManager.Param_Search) == 0 && StringUtils.isBlank(value))
				continue;

			queryStringMap.put(name, UrlUTF8Encoder.decode(value));
		}
	}

	public String removeKeyword()
	{
		queryString = generateQueryString(QueryStringManager.Param_Search, QueryStringManager.Param_Page, QueryStringManager.Param_ShowAllFor);
		return generateWholeUrl(actionPath, queryString);
	}

	public String removeQueryParameter(String... paramNames)
	{
		if (paramNames.length == 0)
		{
			return toString();
		}

		return generateStringValue(paramNames);

	}

	public String setQueryStringParameter(String name, String value)
	{
		return generateWholeUrl(actionPath, generateQueryStringWithParameter(name, value));
	}

	/**
	 *
	 * @param paramName
	 * @return null if parameter is not present
	 */
	public String getQueryStringValue(String paramName)
	{
		if (queryStringMap == null || queryStringMap.size() == 0)
			return null;

		return queryStringMap.get(paramName.toLowerCase().trim());
	}

	public String toString()
	{
		if (stringValue == null)
			stringValue = generateStringValue();

		return stringValue;
	}

	private String generateStringValue(String... parametersToRemove)
	{
		return generateWholeUrl(actionPath, generateQueryString(parametersToRemove));
	}

	protected String generateQueryString(String... parametersToRemove)
	{
		if (parametersToRemove.length == 0 || queryStringMap == null || queryStringMap.size() == 0)
			return queryString;

		StringBuilder sb = new StringBuilder();
		HashSet<String> removeSet = new HashSet<String>();
		for (String param : parametersToRemove) removeSet.add(param.toLowerCase().trim());
		for (String name : queryStringMap.keySet())
		{
			if (removeSet.contains(name))
				continue;
			String value = queryStringMap.get(name);
			sb.append(UrlUTF8Encoder.encode(name));
			if (value != null)
				sb.append("=").append(UrlUTF8Encoder.encode(value));
			sb.append("&");
		}

		return StringUtils.removeEnd(sb.toString(), "&");
	}

	protected String generateQueryStringWithParameter(String paramName, String paramValue)
	{
		if (queryStringMap == null || queryStringMap.size() == 0)
		{
			return paramName + "=" + paramValue;
		}

		StringBuilder sb = new StringBuilder();
		paramName = paramName.toLowerCase().trim();
		for (String name : queryStringMap.keySet())
		{
			sb.append(UrlUTF8Encoder.encode(name));
			if (name.compareToIgnoreCase(paramName) == 0)
			{
				sb.append("=").append(paramValue);
			} else
			{
				String value = queryStringMap.get(name);
				if (value != null)
					sb.append("=").append(UrlUTF8Encoder.encode(value));
			}
			sb.append("&");
		}
		if (!queryStringMap.containsKey(paramName))
			sb.append(UrlUTF8Encoder.encode(paramName)).append("=").append(UrlUTF8Encoder.encode(paramValue));

		return StringUtils.removeEnd(sb.toString(), "&");

	}

	protected String getBaseUrl()
	{
		return baseUrl;
	}


	protected String generateWholeUrl(String actionPath, String queryString)
	{
		return generateWholeUrl(getBaseUrl(), actionPath, queryString);
	}

	protected String generateWholeUrl(String baseUrl, String actionPath, String queryString)
	{
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isBlank(actionPath))
		{
			actionPath = "index." + websiteConfig.getDefaultExtension();
		}
		else
		{
			actionPath = StringUtils.removeEnd(StringUtils.removeStart(actionPath, "/"), "/");
			actionPath = actionPath + "." + websiteConfig.getDefaultExtension();
		}

		sb.append(getBaseUrl()).append(actionPath);

		if (StringUtils.isNotBlank(queryString))
			sb.append("?").append(queryString);

		return sb.toString();

	}
}