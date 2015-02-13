<%@ page import="api.sc2geeks.entity.Player" %>
<%@ page import="api.sc2geeks.entity.Replay" %>
<%@ page import="api.sc2geeks.entity.ReplayWithRelatedInfo" %>
<%@ page import="com.sc2geeks.front.view.ReplayHelper" %>
<%--
  User: robert
  Date: 5/19/12
  Time: 1:37 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%
	ReplayWithRelatedInfo replayWithAllInfo = getValue("replayWithAllInfo");
	Replay replay = replayWithAllInfo.getReplay();
	String title = ReplayHelper.getReplaySummary(replay);

	pageSEOInfo.setPageTitle("Starcraft II replay - " + title + " - " + websiteConfig.getWebsiteName());
	pageSEOInfo.setPageDescription(pageSEOInfo.getPageTitle());
	pageSEOInfo.setPageKeyword(title + " "  + pageSEOInfo.getPageKeyword());
	addExtraCss("replaydetail.v20141105.css");
%>
