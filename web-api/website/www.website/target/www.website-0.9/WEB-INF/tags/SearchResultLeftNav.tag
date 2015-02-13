<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="java.util.List" %>
<%@ tag import="com.sc2geeks.front.RefinementSetting" %>
<%@ attribute name="searchUrlHelper" type="com.sc2geeks.front.ui.SearchUrlHelper" required="true" %>
<%@ attribute name="searchInput" type="api.sc2geeks.entity.SearchInput" required="true" %>
<%@ attribute name="searchResult" type="api.sc2geeks.entity.SearchResult<api.sc2geeks.entity.Replay>" required="true" %>

<%@ include file="base.tag" %>
<%
	if (searchResult.hasNavigation())
	{
%>
<div id="leftnav_wrapper">
	<div id="narrow_title">Narrow Result</div>
	<div id="leftNavigationContainer">
	<%
		if (searchResult.getNavigationNodes() != null)
		{
			List<RefinementField> refinementFields = new ArrayList<RefinementField>(searchResult.getNavigationNodes().keySet().size());
			for(RefinementSetting setting : websiteConfig.getRefinementSettings())
			{
				if (searchResult.getNavigationNodes().containsKey(setting.getRefinementField()))
					refinementFields.add(setting.getRefinementField());
			}
			int pos = 0;
			for (RefinementField field : refinementFields)
			{
	%>
	<h:leftNavUnit refinementField="<%= field %>" navigationNodes="<%= searchResult.getNavigationNodes().get(field)%>"
			searchUrlHelper="<%= searchUrlHelper %>" showTopLiner="<%= pos++ != 0%>"/>
	<%
			}
		}
	%>
</div>
</div>
<%
	}
%>