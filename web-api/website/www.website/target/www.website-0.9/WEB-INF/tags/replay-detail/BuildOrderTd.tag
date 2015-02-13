<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ attribute name="actions" type="java.lang.String[]" required="true" %>

<td>
	<%
		if (actions != null) {
			String className = actions[2] + " " + actions[1];
			String ability;
			if (StringUtils.isBlank(actions[0])) {
				ability = actions[1];
			} else {
				ability = actions[0] + " " + actions[1];
			}
	%>
	<span class="thumbnail-25-container"><span class="<%= className.toLowerCase() %>"></span></span>
	<span class="unit-name"><%= ability %></span>
	<%
		}
	%>
</td>
