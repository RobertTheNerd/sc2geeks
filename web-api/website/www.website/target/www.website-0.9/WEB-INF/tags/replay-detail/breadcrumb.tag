<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="api.sc2geeks.entity.Replay" %>
<%@ tag import="api.sc2geeks.entity.Player" %>
<%@ tag import="com.sc2geeks.front.view.ReplayHelper" %>
<%@ attribute name="replayInfo" type="api.sc2geeks.entity.ReplayWithRelatedInfo" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../base.tag" %>

<%
	Replay replay;
	if (replayInfo != null && (replay = replayInfo.getReplay()) != null)
	{
		String linkReplays = PageUrlBuilder.getReplaySearchPage();
		String linkMatchup = PageUrlBuilder.getReplaySearchPage(RefinementField.MatchUpType, replayInfo.getReplay().getMatchUp());
		String linkMap = PageUrlBuilder.getReplaySearchPage(RefinementField.MapName, replay.getMap().getMapName());
		Player player1 = replay.getPlayerTeams().get(0).getPlayers().get(0);
		Player player2 = replay.getPlayerTeams().get(1).getPlayers().get(0);
		String linkPlayer1 = PageUrlBuilder.getReplaySearchPageByPlayer(player1);
		String linkPlayer2 = PageUrlBuilder.getReplaySearchPageByPlayer(player2);
%>

<div id="breadcrumb">
	<h1 class="mapTitle"> <%= ReplayHelper.getReplaySummary(replay)%></h1>
	<ol class="crumb">
		<li><div><a class="underlineLink" title="See all Starcraft II replays" href="<%= linkReplays %>">Starcraft II Replays</a></div><div class="seperator">></div></li>
		<li><div><h2 class="propName">Match Up:</h2><h2 class="propValue"><a class="underlineLink" title="See all Starcraft II replays of matchup: <%= replay.getMatchUp()%>" href="<%= linkMatchup %>"><%= replay.getMatchUp() %></a></h2></div><div class="seperator">></div></li>
		<li><div><h2 class="propName">Map:</h2><h2 class="propValue"><a class="underlineLink" title="See all Starcraft II replays on <%= replay.getMap().getMapName() %>" href="<%= linkMap %>"><%= replay.getMap().getMapName() %></a></h2></div><div class="seperator">></div></li>
		<li>
			<div>
				<h2 class="propValue"><a class="underlineLink" title="See all Starcraft II replays of <%= player1.getDisplayName() %>" href="<%= linkPlayer1 %>">
					<%= player1.getDisplayName() %>
				</a></h2>
			vs
				<h2 class="propValue"><a class="underlineLink" title="See all Starcraft II replays of <%= player2.getDisplayName() %>" href="<%= linkPlayer2 %>">
					<%= player2.getDisplayName() %>
				</a></h2>
			</div>
		</li>
	</ol>
</div>

<%
	}
%>