package api.sc2geeks.client;

import api.sc2geeks.entity.RefinementInfo;
import api.sc2geeks.entity.SearchFilter;
import api.sc2geeks.entity.SearchInput;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sc2geeks.commons.web.UrlUTF8Encoder.encode;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/15/12
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
class ServiceUrlBuilder
{

	public static Map<String, String> buildUrlParameters(SearchInput searchInput, ServiceConfig serviceConfig)
	{
		try
		{
			HashMap<String, String> params = new HashMap<String, String>();

			params.put("q", searchInput.getSearchTerms());
			params.put("searchType", searchInput.getSearchType().toString());

			params.put("pageSize", Integer.toString(searchInput.getPageSize()));
			params.put("page", Integer.toString(searchInput.getCurrentPage()));

			if (searchInput.getSearchFilters() != null)
			{
				for (SearchFilter filter : searchInput.getSearchFilters())
				{
					params.put(serviceConfig.getRefinementParamName(filter.getRefinementField()),
							listToString(filter.getValues()));
				}
			}

			if (!isListEmpty(searchInput.getRefinementFields()))
			{
				List<String> fieldNames, sortOrders, maxCounts;
				fieldNames = new ArrayList<String>();
				sortOrders = new ArrayList<String>();
				maxCounts = new ArrayList<String>();
				for (RefinementInfo info : searchInput.getRefinementFields())
				{
					fieldNames.add(info.getRefinementField().toString());
					sortOrders.add(getSortOrderString(info));
					maxCounts.add(Integer.toString(info.getMaxCount()));
				}
				addListToMap(params, "rFields", fieldNames);
				addListToMap(params, "rSort", sortOrders);
				addListToMap(params, "rMaxCounts", maxCounts);
			}

			return params;
		} catch (Exception e)
		{
			return null;
		}

	}

	private static List<NameValuePair> processList(List<NameValuePair> list, String name, List<String> values)
	{
		if (!isListEmpty(values))
		{
			BasicNameValuePair pair = new BasicNameValuePair(name, getStringValues(values));
			list.add(pair);
		}
		return list;
	}

	private static String getStringValues(List<String> input)
	{
		StringBuilder sb = new StringBuilder();
		for (String s : input)
		{
			sb.append(s).append(",");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
	private static <E> boolean isListEmpty(List<E> list)
	{
		return list == null || list.size() == 0;
	}

	private static String getSortOrderString(RefinementInfo info)
	{
		return info.getSortMethod().toString() + " " + info.getSortOrder().toString();
	}

	private static String listToString(List list)
	{
		StringBuilder sb = new StringBuilder();
		for (Object o : list)
		{
			if (o != null)
				sb.append(o).append(",");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}

	private static void addListToMap(Map<String, String> map, String name, List list)
	{
		if (list == null || list.size() == 0)
			return;

		map.put(name, listToString(list));
	}

	public static String getParameterString(Map<String, String> params)
	{
		if (params == null || params.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet())
		{
			sb.append(encode(key)).append("=").append(encode(params.get(key))).append("&");
		}
		return StringUtils.removeEnd(sb.toString(), "&");
	}

}
