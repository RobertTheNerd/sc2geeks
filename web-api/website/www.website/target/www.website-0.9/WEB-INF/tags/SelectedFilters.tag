<%@ tag import="api.sc2geeks.entity.SearchFilter" %>
<%@ tag import="com.sc2geeks.front.RefinementSetting" %>
<%@ tag import="java.util.List" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ attribute name="searchInput" type="api.sc2geeks.entity.SearchInput" required="true" %>
<%@ attribute name="searchUrlHelper" type="com.sc2geeks.front.ui.SearchUrlHelper" required="true" %>

<%@ include file="base.tag" %>
<%
	String removeAllUrl = searchUrlHelper.removeAllFields();
%>
<%
	if (searchInput.getSearchFilters() != null && searchInput.getSearchFilters().size() > 0)
	{
%>
<div id="conditionFilterBlock">
	<div class="navigationTop">
	</div>

	<div id="conditionFilterTitle">
		Selected Filters: (<a href="<%= removeAllUrl %>">Remove all</a>)</div>
	<nav id="conditionFilterList">
		<ul>
			<%
				for(SearchFilter filter : searchInput.getSearchFilters())
				{
					String filterName = websiteConfig.getRefinementDisplayName(filter.getRefinementField());
					String newUrl = searchUrlHelper.removeFields(filter.getRefinementField());
			%>
			<li><%= filterName %>: <%= filter.getValues().get(0) %> (<a href="<%= newUrl%>" title="remove <%= filter.getValues().get(0)%>"><img src="<%= ResourceHelper.buildImageUrl("remove-red.png")%>" /></a>)</li>
			<%
				}
			%>
		</ul>
	</nav>
	<div class="navigationBottom">
	</div>
</div>
<%
	}
%>