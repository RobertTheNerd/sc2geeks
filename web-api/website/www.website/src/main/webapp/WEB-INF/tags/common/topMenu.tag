<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="com.sc2geeks.front.ui.PageTabManager" %>
<%@ tag import="freemarker.template.utility.HtmlEscape" %>
<%@ tag import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ attribute name="pageTab" type="java.lang.String" required="true" %>

<%@ include file="../base.tag" %>

<%!
	private static String getActiveTabStyle(String menuTab, String activeTab)
	{
		return menuTab.compareTo(activeTab) == 0 ? "currentTab" : "";
	}
%>
<%
	String replayStyle = getActiveTabStyle(PageTabManager.Tab_Replay, pageTab);
	String progamerStyle = getActiveTabStyle(PageTabManager.Tab_Progamer, pageTab);

%>
<div id='topmenu'>
	<ul>

		<li class="<%= replayStyle %>"><a href="<%= PageUrlBuilder.getReplayHomePage()%>"><span>Replays</span></a></li>
		<li class="<%= progamerStyle %>"><a href="<%= PageUrlBuilder.getProgamerHomePage() %>"><span>Pro-gamers</span></a></li>
	</ul>
	<div id="searchBar">
		<form id="formSearch" method="get" action="<%= PageUrlBuilder.getReplaySearchPage() %>">
			<input type="text" name="q" maxlength="200" value="<%= StringEscapeUtils.escapeHtml4(requestContext.getSearchTerms() == null ? "" : requestContext.getSearchTerms()) %>" id="searchbox" />
			<input type="submit" value="Search" id="searchBtn" />
		</form>
	</div>
</div>
