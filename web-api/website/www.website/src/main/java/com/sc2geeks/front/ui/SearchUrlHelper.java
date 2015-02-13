package com.sc2geeks.front.ui;

import api.sc2geeks.entity.*;
import com.sc2geeks.commons.web.UrlUTF8Encoder;
import com.sc2geeks.front.IRefinementInfoProvider;
import com.sc2geeks.front.RefinementSetting;
import com.sc2geeks.front.WebsiteConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/27/12
 * Time: 5:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchUrlHelper extends PageUrlHelper
{
    private static WebsiteConfig websiteConfig = WebsiteConfig.getInstance();

    private LinkedHashMap<String, String> fieldValueMap;
    private RefinementField fieldToShowAllValues = null;
    protected Set<RefinementField> filterFields;
    IRefinementInfoProvider leftNavRefinementProvider;
    IRefinementInfoProvider showAllRefinementProvider;
    boolean legacyUrlDetected = false;

    private SearchInput searchInput;
    private String urlString;

    public SearchUrlHelper(String namespace, String actionPath, String queryString,
                           IRefinementInfoProvider leftNavRefinementProvider,
                           IRefinementInfoProvider showAllRefinementProvider)
    {
        super(namespace, actionPath, queryString);
        this.leftNavRefinementProvider = leftNavRefinementProvider;
        this.showAllRefinementProvider = showAllRefinementProvider;
        parseSearchInput();

        // rewrite actionPath;
        this.actionPath = regenerateActionPath(null);
    }

    public SearchInput getSearchInput()
    {
        return searchInput;
    }

    public Set<RefinementField> getFilterFields()
    {
        return filterFields;
    }


    public String removeFields(RefinementField... fields)
    {
        if (fields.length == 0)
            return toString();

        String actionPath, queryString;
        actionPath = generateActionPath(fields);
        queryString = generateQueryString(QueryStringManager.Param_Page, QueryStringManager.Param_ShowAllFor);
        return generateWholeUrl(actionPath, queryString);
    }

    public String removeAllFields()
    {
        String actionPath, queryString;

	    actionPath = "";

	    queryString = generateQueryString(QueryStringManager.Param_Page, QueryStringManager.Param_ShowAllFor);
        return generateWholeUrl(actionPath, queryString);
    }

    public String addField(RefinementField field, String value)
    {
        String actionPath, queryString;
        actionPath = generateActionPath(field, value);
        queryString = generateQueryString(QueryStringManager.Param_Page, QueryStringManager.Param_ShowAllFor);
        return generateWholeUrl(actionPath, queryString);
    }

    public boolean isLegacyUrlDetected()
    {
        return legacyUrlDetected;
    }

    protected String getBaseUrl()
    {
        return PageUrlBuilder.getPage(PageUrlAlias.ReplaySearch);
    }

    private void parseSearchInput()
    {
        // normal search or list nav values
        String navName = getQueryStringValue(QueryStringManager.Param_ShowAllFor);
        if (StringUtils.isNotBlank(navName))
        {
            RefinementSetting setting = websiteConfig.getRefinementSettingByUrlPrefix(navName);
            if (setting != null)
                fieldToShowAllValues = setting.getRefinementField();
        }

        searchInput = new SearchInput();

        searchInput.setSearchType(fieldToShowAllValues != null ? SearchType.RefinementOnly : SearchType.WithRefinement);

        // get all filters
        if (!StringUtils.isBlank(actionPath))
        {
            String[] parts = StringUtils.removeEnd(StringUtils.removeStart(actionPath, "/"), "/").split("/");
            fieldValueMap = new LinkedHashMap<String, String>(parts.length);
            for (String part : parts)
            {
                String[] fieldValue = part.split("-", 2);
	            String val = fieldValue.length > 1 ? UrlUTF8Encoder.decode(fieldValue[1]) : null;
	            if (fieldValue[0].compareToIgnoreCase(QueryStringManager.Param_Search) == 0)
	            {
		            // found legacy url
		            legacyUrlDetected = true;
		            if (StringUtils.isNotBlank(val))
		            {
			            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			            map.put(QueryStringManager.Param_Search, val);
			            map.putAll(queryStringMap);
			            queryStringMap = map;
		            }
	            }
	            else
	            {
                    fieldValueMap.put(fieldValue[0].toLowerCase().trim(), val);
	            }
            }
        } else
        {
            fieldValueMap = new LinkedHashMap<String, String>();
        }

        searchInput.setSearchTerms(queryStringMap.get(QueryStringManager.Param_Search));
        parseSearchFilters();

        // pagination
        String page = getQueryStringValue(QueryStringManager.Param_Page);
        String pageSize = getQueryStringValue(QueryStringManager.Param_PageSize);
        if (StringUtils.isNotBlank(page))
        {
            try
            {
                searchInput.setCurrentPage(Integer.parseInt(page));
            } catch (Exception e)
            {
            }
        }
        if (StringUtils.isNotBlank(pageSize))
        {
            try
            {
                searchInput.setPageSize(Integer.parseInt(pageSize));
            } catch (Exception e)
            {
            }
        }

        // refinement
        parseRefinementInfo();
    }

    private void parseSearchFilters()
    {
        parseLegacyFilter();

        if (fieldValueMap == null || fieldValueMap.size() == 0)
            return;

        List<SearchFilter> searchFilters = searchInput.getSearchFilters() == null ?
                new ArrayList<SearchFilter>(fieldValueMap.size()) : searchInput.getSearchFilters();
        filterFields = new HashSet<RefinementField>(fieldValueMap.size());
        for (String paramName : fieldValueMap.keySet())
        {
            RefinementSetting setting = websiteConfig.getRefinementSettingByUrlPrefix(paramName);
            if (setting == null)
                continue;
            SearchFilter filter = new SearchFilter();
            filter.setRefinementField(setting.getRefinementField());
            filter.setValues(convertStringToList(fieldValueMap.get(paramName)));
            searchFilters.add(filter);
            filterFields.add(filter.getRefinementField());
        }
        searchInput.setSearchFilters(searchFilters);
    }

    private void parseLegacyFilter()
    {
        if (queryStringMap == null)
            return;

        List<SearchFilter> searchFilters = searchInput.getSearchFilters() == null ?
                new ArrayList<SearchFilter>(fieldValueMap.size()) : searchInput.getSearchFilters();
        for (String paramName : queryStringMap.keySet())
        {
            if (!paramName.startsWith("f."))
                continue;

            String legacyName = StringUtils.removeStart(paramName, "f.");
            if (StringUtils.isBlank(legacyName))
                continue;

            RefinementSetting setting = websiteConfig.getRefinementSettingByLegacyParameter(legacyName);
            if (setting == null)
                continue;

            SearchFilter filter = new SearchFilter();
            filter.setRefinementField(setting.getRefinementField());
            filter.setValues(convertStringToList(queryStringMap.get(paramName)));
            searchFilters.add(filter);
            if (filterFields == null)
                filterFields = new HashSet<RefinementField>();
            filterFields.add(filter.getRefinementField());
            legacyUrlDetected = true;
        }
        searchInput.setSearchFilters(searchFilters);
    }


    private void parseRefinementInfo()
    {
        if (websiteConfig.getRefinementSettings() == null)
            return;

        List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>();

        if (fieldToShowAllValues != null && showAllRefinementProvider != null)
        {
            RefinementInfo info = showAllRefinementProvider.getRefinementInfo(this.fieldToShowAllValues);
            info.setMaxCount(0);
            refinementInfoList.add(info);
        } else if (leftNavRefinementProvider != null)
        {
            for (RefinementSetting setting : websiteConfig.getRefinementSettings())
            {
                if (filterFields != null && filterFields.contains(setting.getRefinementField()))
                    continue;
                RefinementInfo info = leftNavRefinementProvider.getRefinementInfo(setting.getRefinementField());
	            if (info == null)
		            continue;

                info.setMaxCount(info.getMaxCount() == 0 ? 0 : info.getMaxCount() + websiteConfig.getLeftNavTolerateCount() + 1);

                refinementInfoList.add(info);
            }
        }
        searchInput.setRefinementFields(refinementInfoList);
    }

    private static List<String> convertStringToList(String input)
    {
        if (StringUtils.isBlank(input))
            return null;

        String[] parts = input.split(",");
        if (parts == null || parts.length == 0)
            return null;

        return Arrays.asList(parts);
    }

    private static String convertListToString(List<String> input)
    {
        StringBuilder sb = new StringBuilder();
        for (String str : input)
        {
            sb.append(str).append(",");
        }
        return StringUtils.removeEnd(sb.toString(), ",");
    }

    private String generateActionPath(RefinementField... fieldsToRemove)
    {
        if (fieldsToRemove.length == 0)
        {
            return generateActionPath((HashSet<String>) null);
        } else
        {
            HashSet<String> prefixSet = new HashSet<String>(fieldsToRemove.length);
            for (RefinementField field : fieldsToRemove)
            {
                String prefix = websiteConfig.getRefinementUrlPrefix(field);
                if (prefix != null)
                    prefixSet.add(prefix.toLowerCase());
            }
            return generateActionPath(prefixSet);
        }
    }

    private String generateActionPath(HashSet<String> prefixSet)
    {
        String path;

        if (prefixSet == null || prefixSet.size() == 0)
        {
            path = actionPath;
        } else
        {
            path = regenerateActionPath(prefixSet);
        }

        return path;
    }

    private String regenerateActionPath(HashSet<String> prefixSet)
    {
        StringBuilder sb = new StringBuilder();
        for (String prefix : fieldValueMap.keySet())
        {
            String name, value;
            value = fieldValueMap.get(prefix);

            if (StringUtils.isBlank(value))
                continue;

            if (prefixSet != null && prefixSet.contains(prefix.toLowerCase()))
                continue;

	        RefinementSetting setting = websiteConfig.getRefinementSettingByUrlPrefix(prefix);
	        if (setting == null)
		        continue;

	        name = websiteConfig.getRefinementUrlPrefix(setting.getRefinementField());
            sb.append(name).append("-").append(UrlUTF8Encoder.encode(value)).append("/");
        }
        return sb.toString();
    }


    // todo: figure out & fix
    private String generateActionPath(RefinementField field, String value)
    {
        String path = generateActionPath();

        String prefix = websiteConfig.getRefinementUrlPrefix(field);
        if (StringUtils.isBlank(prefix))
            return path;

        if (StringUtils.isNotBlank(path))
            path = StringUtils.removeEndIgnoreCase(path, "." + websiteConfig.getDefaultExtension());

        if (!path.endsWith("/"))
            path = path + "/";

        return path + prefix + "-" + UrlUTF8Encoder.encode(value);
    }

    public String toString()
    {
        if (urlString == null && searchInput != null)
        {
            boolean isRoot = true;
            StringBuilder sb = new StringBuilder();
            sb.append(PageUrlBuilder.getPage(PageUrlAlias.ReplaySearch));

            // deal with filter
            if (searchInput.getSearchFilters() != null && searchInput.getSearchFilters().size() > 0)
            {
                for (SearchFilter filter : searchInput.getSearchFilters())
                {
                    String path = getSearchFilterPath(filter.getRefinementField(), convertListToString(filter.getValues()));
                    if (StringUtils.isBlank(path))
                        continue;
                    sb.append(path).append("/");
                }

                isRoot = false;
            }


            if (isRoot)
            {
                sb.append("index." + websiteConfig.getDefaultExtension());
            } else
            {
                sb = new StringBuilder(StringUtils.removeEnd(sb.toString(), "/")).append(".").append(websiteConfig.getDefaultExtension());
            }

            // deal with query string parameter, in specific order. hmm, maybe needs revision
	        copyQueryStringParameter(sb, QueryStringManager.Param_Search);
	        copyQueryStringParameter(sb, QueryStringManager.Param_Page);
            copyQueryStringParameter(sb, QueryStringManager.Param_PageSize);
            copyQueryStringParameter(sb, QueryStringManager.Param_ShowAllFor);
            urlString = sb.toString();

        }
        return urlString;
    }

    private StringBuilder copyQueryStringParameter(StringBuilder sb, String param)
    {
        if (queryStringMap != null && queryStringMap.containsKey(param))
        {
            if (sb.toString().endsWith("." + websiteConfig.getDefaultExtension()))
                sb.append("?");
            else
                sb.append("&");
            sb.append(param).append("=").append(UrlUTF8Encoder.encode(queryStringMap.get(param)));
        }
        return sb;
    }

    private static String getSearchFilterPath(RefinementField field, String value)
    {
        String prefix = websiteConfig.getRefinementUrlPrefix(field);
        if (StringUtils.isBlank(prefix))
        {
            return null;
        }
        return prefix + "-" + UrlUTF8Encoder.encode(value);
    }
}
