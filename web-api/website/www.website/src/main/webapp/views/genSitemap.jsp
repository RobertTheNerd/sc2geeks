<%@ page import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="base.jsp" %>
<%@ taglib prefix="sm" tagdir="/WEB-INF/tags/sitemap" %>
<%
	String message = getValue("message");
	if (message == "404")
	{
		response.setStatus(404);
	}
%>
<!doctype html>
<html class="no-js" lang="en">
<head>
	<title>sitemap generator result</title>
</head>
<body>
<%= message %>
</body>
