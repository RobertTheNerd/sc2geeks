<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ page import="com.google.gson.JsonArray" %>
<%@ page import="com.google.gson.Gson" %>
<%--
  User: robert
  Date: 4/5/12
  Time: 10:25 PM
--%>
<%@ page contentType="application/json" pageEncoding="UTF-8" language="java" trimDirectiveWhitespaces="true" session="false" %>
<%
	ValueStack vs = ActionContext.getContext().getValueStack();
	Object obj = vs.findValue("returnValue");
	String jsonString = "";
	if (obj != null)
	{
		Gson gson = new Gson();
		jsonString = gson.toJson(obj);
	}
%>
<%= jsonString %>