<%@ tag import="java.util.List" %>
<%@ tag import="api.sc2geeks.entity.NavigationNode" %>
<%@ tag import="freemarker.template.utility.StringUtil" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>

<%@ attribute name="searchResult" type="api.sc2geeks.entity.SearchResult<api.sc2geeks.entity.Replay>" required="true" %>
<%@ attribute name="searchInput" type="api.sc2geeks.entity.SearchInput" required="true" %>
<%@ attribute name="searchUrlHelper" type="com.sc2geeks.front.ui.SearchUrlHelper" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="base.tag" %>

<%
	StringBuilder sb = new StringBuilder();
	boolean showKeyword = false;
	String newLink = "";
	if (searchResult == null || searchResult.getTotalMatches() == 0)
	{
		sb.append("No replays found");
		if (StringUtils.isNotBlank(searchInput.getSearchTerms()) && searchInput.getSearchTerms().compareTo("*") != 0)
			sb.append(" for \"").append(StringEscapeUtils.escapeHtml4(searchInput.getSearchTerms())).append("\"");
		sb.append(".");
	}
	else
	{
		int first, last;
		first = (searchResult.getCurrentPage() - 1) * searchInput.getPageSize() + 1;
		last = Math.min((int) searchResult.getCurrentPage() * searchInput.getPageSize(), (int)searchResult.getTotalMatches());
		if (searchResult.getCurrentPage() == 1 && last == searchResult.getTotalMatches())
		{
			sb.append(searchResult.getTotalMatches()).append(" replays found.");
		}
		else
		{
			sb.append("Showing ").append(first).append(" - ").append(last).append(" of ").append(searchResult.getTotalMatches()).append(" Starcraft II replays.");

		}
	}
%>
<div id="showing"><%= sb.toString() %></div>