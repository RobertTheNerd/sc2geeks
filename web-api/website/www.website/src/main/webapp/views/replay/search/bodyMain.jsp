<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%--
  User: robert
  Date: 4/15/12
  Time: 7:57 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="searchBase.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<hs:Breadcrumb searchInput="<%= searchInput %>" searchUrlHelper="<%= searchUrlHelper %>" />
<c:set var="searchResult" value="<%= searchResult %>" />
<div id="contentContainer">
	<div id="replay-list">
		<div id="navigationContainer"<c:if test="${searchResult.totalMatches == 0}"> class="noborder"</c:if>>
			<h:SearchCountSummary searchResult="${searchResult}" searchInput="<%= searchInput%>" searchUrlHelper="<%= searchUrlHelper %>" />
			<c:if test="${searchResult.totalMatches > 0}">
			<div id="showWinner">
				<input type="checkbox" name="revealWinner" id="revealWinner">
				<label for="revealWinner">Reveal winner</label>
			</div>
			</c:if>
			<div id="showStats">
				<input type="checkbox" name="showStats" id="toggleStats">
				<label for="toggleStats">Show stats & charts</label>
			</div>
		</div>
		<c:if test="${searchResult.totalMatches > 0}">
			<div id="replay-stats">
				<hs:Stats navigationNodes="<%= searchResult.getNavigationNodes() %>" />
			</div>
		</c:if>
		<c:if test="${searchResult != null}">
			<h:ReplayResultList replays="${searchResult.entityList}"/>
		</c:if>
		<h:Pager pageUrlHelper="<%= searchUrlHelper %>" totalPages="<%= totalPage %>" currentPage="<%= currentPage %>"/>
	</div>
</div>
<h:SearchResultLeftNav searchUrlHelper="<%= searchUrlHelper %>" searchInput="<%= searchInput%>" searchResult="<%= searchResult%>"/>
<div id="allNavOverLay">
	<a class="close" href="javascript:void(0);"><img src="<%= ResourceHelper.buildImageUrl("close-32.png")%>" /></a>
	<div id="navWrapper"></div>
</div>
