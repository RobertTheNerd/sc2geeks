<%@ tag import="api.sc2geeks.entity.NavigationNode" %>
<%@ tag import="com.sc2geeks.front.StatsContainerSetting" %>
<%@ tag import="java.util.List" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ attribute name="navigationNodes" type="java.util.HashMap<api.sc2geeks.entity.RefinementField, java.util.List<api.sc2geeks.entity.NavigationNode>>" required="true" %>

<%@ include file="../base.tag" %>
<%
	boolean showCharts = false;
	for (StatsContainerSetting setting : websiteConfig.getStatsContainerSettings()) {
		List<NavigationNode> navNodes = navigationNodes.get(setting.getRefinementField());
		if (navNodes != null && navNodes.size() > 0) {
			showCharts = true;
			String className = StringUtils.isNotBlank(setting.getClassName()) ? setting.getClassName() : "stats-container";
			className = " class=\"" + className + "\"";
			String customStyle = StringUtils.isNotBlank(setting.getCustomStyle()) ? " style=\"" + setting.getCustomStyle() + "\"" : "";
%>
<div id="<%= setting.getDivId() %>"<%= className %><%= customStyle %>></div>
<%
		}
	}
	valueStack.set("showCharts", showCharts);
%>