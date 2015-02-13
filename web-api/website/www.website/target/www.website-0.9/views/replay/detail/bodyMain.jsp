<%@ page import="com.sc2geeks.front.view.RelatedReplayInfo" %>
<%@ page import="api.sc2geeks.entity.*" %>
<%@ page import="org.apache.commons.lang3.tuple.ImmutablePair" %>
<%@ page import="java.util.*" %>
<%--
  User: robert
  Date: 4/15/12
  Time: 12:38 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%!

%>
<%
	ReplayWithAllInfo replayWithAllInfo = getValue("replayWithAllInfo");
	java.util.Hashtable<Integer, List<ImmutablePair<Integer, String>>> abilityEvents = getValue("abilityEvents");

%>
<c:set var="replayInfo" value="<%= replayWithAllInfo %>"></c:set>

<c:choose>
	<c:when test="${replayInfo != null}">
		<rd:breadcrumb replayInfo="<%= replayWithAllInfo %>" />

		<div id="detailContainer">
		<rd:ReplayRow replay="${replayInfo.replay}" />
		<rd:OtherInfo replayWithAllInfo="<%= replayWithAllInfo %>" abilityEvents="<%= abilityEvents %>" />
		</div>
	</c:when>
	<c:otherwise>
		Sorry, the replay you specified does not exist.
	</c:otherwise>
</c:choose>

