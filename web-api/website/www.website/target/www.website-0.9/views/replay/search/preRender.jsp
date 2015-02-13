<%@ page import="api.sc2geeks.entity.SearchFilter" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%--
  User: robert
  Date: 4/27/12
  Time: 12:22 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="searchBase.jsp" %>
<%
	String searchTerms = searchInput.getSearchTerms().compareTo("*") == 0 ? null : searchInput.getSearchTerms();
	if (searchTerms != null || (searchInput.getSearchFilters() != null && searchInput.getSearchFilters().size() > 0))
	{
		StringBuilder sb = new StringBuilder();
		if (searchTerms != null)
			sb.append(searchTerms).append(" / ");

		for (SearchFilter filter : searchInput.getSearchFilters())
		{
			String name = websiteConfig.getRefinementDisplayName(filter.getRefinementField());
			String value = "";
			for (String val : filter.getValues())
				value += val + ",";
			value = StringUtils.removeEnd(value, ",");
			sb.append(name).append(":").append(value).append(" / ");
		}
		String title = StringUtils.removeEnd(sb.toString(), " / ");
		pageSEOInfo.setPageTitle("Starcraft II replays - " + title + " - " + websiteConfig.getWebsiteName());
		pageSEOInfo.setPageDescription(pageSEOInfo.getPageTitle());
		pageSEOInfo.setPageKeyword(pageSEOInfo.getPageKeyword() + " " + title);
	}
%>