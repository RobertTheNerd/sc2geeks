<%@ page import="api.sc2geeks.entity.Replay" %>
<%@ page import="api.sc2geeks.entity.SearchResult" %>
<%@ page import="api.sc2geeks.entity.playerperson.PlayerPerson" %>
<%@ page import="api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%@ taglib prefix="pd" tagdir="/WEB-INF/tags/player/liquid-player" %>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>

<%
	PlayerPersonWithRelatedInfo playerInfo = getValue("playerInfo");
%>