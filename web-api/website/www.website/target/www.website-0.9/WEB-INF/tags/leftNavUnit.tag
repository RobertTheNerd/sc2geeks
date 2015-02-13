<%@ tag import="java.util.List" %>
<%@ tag import="api.sc2geeks.entity.NavigationNode" %>
<%@ tag import="com.sc2geeks.front.RefinementSetting" %>
<%@ tag import="api.sc2geeks.entity.RefinementInfo" %>
<%@ tag import="com.sc2geeks.front.ui.QueryStringManager" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>

<%@ attribute name="refinementField" type="api.sc2geeks.entity.RefinementField" required="true" %>
<%@ attribute name="navigationNodes" type="java.util.List<api.sc2geeks.entity.NavigationNode>" required="true" %>
<%@ attribute name="searchUrlHelper" type="com.sc2geeks.front.ui.SearchUrlHelper" required="true" %>
<%@ attribute name="showTopLiner" type="java.lang.Boolean" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="base.tag" %>
<%
	if (navigationNodes.size() > 1)
	{
		String nodeName = websiteConfig.getRefinementDisplayName(refinementField);
		RefinementInfo refinementInfo = websiteConfig.getReplayLeftNavRefinementProvider().getRefinementInfo(refinementField);
		String prefix = websiteConfig.getRefinementUrlPrefix(refinementField);
		int navCount = Math.min(navigationNodes.size(),
				refinementInfo.getMaxCount() == 0 ? 999999 : refinementInfo.getMaxCount() + websiteConfig.getLeftNavTolerateCount());
%>
<div class="navigationBlock">
	<c:if test="${ showTopLiner }">
	<div class="navigationTop">
	</div>
	</c:if>
	<div class="navigationTitle"><%= nodeName %></div>
	<nav class="navigationItemList">
		<ul>
			<%
				for (int i = 0; i < navCount; i ++)
				{
					NavigationNode node = navigationNodes.get(i);
					String link = searchUrlHelper.addField(refinementField, node.getNodeName());
			%>
			<li><h4><a href="<%= link %>"><%= node.getDisplayNodeName() %> (<%= node.getCount()%>)</a></h4></li>
			<%
				}
				if (navigationNodes.size() > navCount)
				{
					%>
			<li><a class="moreNav" style="visibility:hidden;" rel="#allNavOverLay" href="<%= searchUrlHelper.setQueryStringParameter(QueryStringManager.Param_ShowAllFor, prefix) %>">More ...</a></li>
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
