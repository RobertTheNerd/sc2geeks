<%--
  User: robert
  Date: 8/19/12
  Time: 8:26 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>

<%
	addExtraCss("playerlist.v20150106.css");
	addExtraCss("chosen.v2012.10.01.css");
	pageSEOInfo.setPageTitle("Startcraft II progamers - " + websiteConfig.getWebsiteName());
	pageSEOInfo.setPageDescription(pageSEOInfo.getPageTitle());
	pageSEOInfo.setPageKeyword(pageSEOInfo.getPageKeyword() + " " + pageSEOInfo.getPageTitle());

%>