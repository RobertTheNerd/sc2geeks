<%@ page import="javax.xml.ws.Response" %>
<%--
  User: robert
  Date: 5/6/12
  Time: 6:43 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="base.jsp" %>
<%
	int statusCode = 302;

	try
	{ statusCode = (Integer) getValue("statusCode");}
	catch(Exception e){e.printStackTrace();}

	String url = (String) getValue("redirectUrl");

	response.setStatus(statusCode);
	response.setHeader("Location", url);
%>