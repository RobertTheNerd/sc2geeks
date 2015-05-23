<%@ page import="api.sc2geeks.entity.SearchInput" %>
<%@ page import="api.sc2geeks.entity.SearchResult" %>
<%@ page import="com.sc2geeks.front.ui.SearchUrlHelper" %>
<%@ page import="api.sc2geeks.entity.Replay" %>
<%@ page import="api.sc2geeks.entity.RefinementField" %>
<%--
  User: robert
  Date: 5/21/12
  Time: 8:50 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%@ taglib prefix="hs" tagdir="/WEB-INF/tags/replay-search" %>

<%
	SearchInput searchInput = getValue("searchInput");
	SearchResult<Replay> searchResult = getValue("searchResult");

	SearchUrlHelper searchUrlHelper = getValue("searchUrlHelper");
	int currentPage = 0, totalPage = 0;
	if (searchResult != null) {
		currentPage = searchResult.getCurrentPage();
		totalPage = (int)Math.ceil((double)searchResult.getTotalMatches() / searchInput.getPageSize());
	}
%>
