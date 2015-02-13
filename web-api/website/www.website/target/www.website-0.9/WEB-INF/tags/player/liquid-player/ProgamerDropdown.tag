<%@ tag import="api.sc2geeks.entity.NavigationNode" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="navigationNodes" type="java.util.List<api.sc2geeks.entity.NavigationNode>" required="true" %>
<%@ attribute name="selectedValue" type="java.lang.String" required="true" %>
<%@ attribute name="prompt" type="java.lang.String" required="true" %>
<%@ attribute name="width" type="java.lang.Integer" required="true" %>

<%@ include file="../../base.tag" %>
<select id="<%= id %>" data-placeholder="<%= prompt %>" class="chzn-select" style="width:<%= width %>px;">
	<option value=""></option>
	<%
		if (navigationNodes != null && navigationNodes.size() > 0)
		{
			for(NavigationNode node : navigationNodes)
			{
				String nodeValue = node.getNodeName();
				if (StringUtils.isBlank(nodeValue) || nodeValue.trim().compareTo("-") == 0)
					continue;

				String selected = "";
				if (selectedValue != null && selectedValue.compareToIgnoreCase(node.getNodeName()) == 0)
					selected = " selected=\"selected\"";
	%>
	<option value="<%= node.getNodeName() %>"<%= selected %>><%= node.getNodeName()%> (<%= node.getCount() %>)</option>
	<%
			}
		}
		else if (StringUtils.isNotBlank(selectedValue))
		{
	%>
	<option value="<%= selectedValue %>" selected="selected"><%= selectedValue %></option>
	<%
		}
	%>
</select><span style="width:3px;"></span>