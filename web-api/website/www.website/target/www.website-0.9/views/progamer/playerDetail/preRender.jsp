<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%--
  User: robert
  Date: 8/19/12
  Time: 8:26 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="playerDetailBase.jsp" %>

<%
	addExtraCss("playerlist.v20150106.css");
	addExtraCss("playerdetail.v20141222.css");

	PlayerPerson player = playerInfo.getPlayerPerson();
	String title = "Starcraft II ProGamer : " + player.getGameId();
	if (StringUtils.isNotBlank(player.getRace()))
		title = title + " , Race: " + player.getRace();
	if (StringUtils.isNotBlank(player.getTeam()))
		title = title + " , Team: " + player.getTeam();
	pageSEOInfo.setPageTitle(title + " - " + websiteConfig.getWebsiteName());
	pageSEOInfo.setPageDescription(pageSEOInfo.getPageTitle());
	pageSEOInfo.setPageKeyword(title + " "  + pageSEOInfo.getPageKeyword());
%>