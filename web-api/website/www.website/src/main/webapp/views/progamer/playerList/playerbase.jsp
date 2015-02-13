<%@ page import="api.sc2geeks.entity.SearchResult" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%@ taglib prefix="pd" tagdir="/WEB-INF/tags/player/liquid-player" %>

<%
	SearchResult<PlayerPerson> searchResult = getValue("searchResult");
	String playerTeam = getValue("playerTeam");
	String race = getValue("race");
	if (race == null)
		race = "";

	String country = getValue("country");
	if (country == null) country = "";

	boolean withReplaysOnly = getValue("withReplaysOnly");
	List<NavigationNode> races = searchResult.getNavigationNodes() == null ? null :
			searchResult.getNavigationNodes().get(RefinementField.PlayerRace);

	List<NavigationNode> playerTeams = searchResult.getNavigationNodes() == null ? null :
			searchResult.getNavigationNodes().get(RefinementField.Team);

	List<NavigationNode> countries = searchResult.getNavigationNodes() == null ? null :
			searchResult.getNavigationNodes().get(RefinementField.Country);

	String replayOnlyChecked = withReplaysOnly ? " checked=\"checked\"" : "";
%>