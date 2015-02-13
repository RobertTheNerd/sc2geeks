<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>

<%--
  User: robert
  Date: 8/19/12
  Time: 8:26 AM
--%>
<%@ include file="playerDetailBase.jsp" %>
<div>
	Sorry, the specified player is not found. Please visit <a href="<%= PageUrlBuilder.getProgamerHomePage() %>">the full list of players</a>.
</div>