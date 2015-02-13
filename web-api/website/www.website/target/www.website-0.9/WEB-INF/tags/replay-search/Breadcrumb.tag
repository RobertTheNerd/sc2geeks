<%@ tag import="api.sc2geeks.entity.SearchFilter" %>
<%@ tag import="com.sc2geeks.front.RefinementSetting" %>
<%@ tag import="java.util.List" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ attribute name="searchInput" type="api.sc2geeks.entity.SearchInput" required="true" %>
<%@ attribute name="searchUrlHelper" type="com.sc2geeks.front.ui.SearchUrlHelper" required="true" %>

<%@ include file="../base.tag" %>
<%!
	private static String getFilterDisplayValue(SearchFilter filter)
	{
		String value = filter.getValues().get(0);
		if (filter.getRefinementField() == RefinementField.ProgamerName)
		{
			int dashPos = value.indexOf("-");
			if (dashPos > 0)
				value = value.substring(0, dashPos);
		}
		return value;
	}
%>
<%
	boolean withFilter = (searchInput.getSearchFilters() != null && searchInput.getSearchFilters().size() > 0)
			|| !searchInput.hasEmptySearchTerm();
%>

<c:set var="withFilter" value="<%= withFilter %>"></c:set>
<div class="breadcrumb">
	<ol class="crumb">
		<li>
			<h1>
		<c:choose>
			<c:when test="${withFilter}">
				<a title="See all Starcraft II replays" href="<%= PageUrlBuilder.getReplayHomePage()  %>">Starcraft II Replays</a>
			</c:when>
			<c:otherwise>
				Starcraft II Replays
			</c:otherwise>
		</c:choose>
			</h1>
		</li>
		<%
		if (searchInput.getSearchFilters() != null)
		{
			String removeImageUrl = ResourceHelper.buildImageUrl("remove-red.png");
			for(SearchFilter filter : searchInput.getSearchFilters())
			{
				String filterName = websiteConfig.getRefinementDisplayName(filter.getRefinementField());
				String filterValue = filter.getValues().get(0);
				String filterDisplayValue = getFilterDisplayValue(filter);
				String newUrl = searchUrlHelper.removeFields(filter.getRefinementField());
				String singleUrl = PageUrlBuilder.getReplaySearchPage(filter.getRefinementField(), filterValue);
		%>

		<li>
			<span>&gt;</span>
			<a href="<%= singleUrl %>" title="<%=filterName + ": " + filterDisplayValue %>"><%= filterName %>: <%= filterDisplayValue %></a>
			<sup><a title="Remove <%= filterDisplayValue %>" href="<%= newUrl %>">(<img src="<%= removeImageUrl %>">)</a></sup>
		</li>
		<%
			}
			if (!searchInput.hasEmptySearchTerm())
			{
				String newUrl = searchUrlHelper.removeKeyword();
		%>
		<li>
			<span>&gt;</span>
			Search Term: <%= StringEscapeUtils.escapeHtml4(searchInput.getSearchTerms()) %>
			<sup><a title="Remove <%= StringEscapeUtils.escapeHtml4(searchInput.getSearchTerms()) %>" href="<%= newUrl %>">(<img src="<%= removeImageUrl %>">)</a></sup>
		</li>
		<%
			}
		}
		%>
	</ol>
	<div class="clear"></div>
</div>
