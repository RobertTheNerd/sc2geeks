<%@ tag import="api.sc2geeks.entity.Player" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="java.text.DateFormat" %>
<%@ tag import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="position" type="java.lang.Integer" required="true" %>
<%@ attribute name="replay" type="api.sc2geeks.entity.Replay" required="true" %>
<%@ attribute name="extraClassName" type="java.lang.String" required="false"     %>

<%@ include file="base.tag" %>
<%!
	private String getIconImage(String playerRace)
	{
		if (StringUtils.isBlank(playerRace))
			return null;

		return "icon_" + playerRace.toLowerCase().trim() + "_40.png";
	}

%>
<%
	String player1Icon, player2Icon;
	Player player1, player2;
	player1 = replay.getPlayerTeams().get(0).getPlayers().get(0);
	player2 = replay.getPlayerTeams().get(1).getPlayers().get(0);
	player1Icon = ResourceHelper.buildImageUrl(getIconImage(player1.getPlayerRace()));
	player2Icon = ResourceHelper.buildImageUrl(getIconImage(player2.getPlayerRace()));
	String replayMap;
	String mapImage = replay.getMap().getImageUrl(46);
	replayMap = ResourceHelper.buildMap46ImageUrl(StringUtils.isBlank(mapImage) ? "unknown_map.jpg" : mapImage);
	boolean showEvent = StringUtils.isNotBlank(replay.getEvent());
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	String gameDate = df.format(replay.getGameDate());
	String totalTitle = "";
	if (replay.getSeriesCount() > 1 && replay.getSeriesNumber() > 0)
	{
		gameDate = String.format("%1$s&nbsp;&nbsp;%2$d/%3$d", gameDate, replay.getSeriesNumber(), replay.getSeriesCount());
		totalTitle = "There are total " + replay.getSeriesCount() + " Starcraft II games in this match-up.";
	}
	String replayDetailUrl = PageUrlBuilder.getReplayDetailPage(replay);
	String replayDownloadUrl = PageUrlBuilder.getReplayDownloadPage(replay);
	String className = StringUtils.isBlank(extraClassName) ? "" : " " + extraClassName;
	String replayDescription = player1.getDisplayName() + " vs " + player2.getDisplayName()  + " on " +
			replay.getMap().getMapName() + ".";
%>
<c:set var="player1" value="${replay.playerTeams[0].players[0]}"/>
<c:set var="player2" value="${replay.playerTeams[1].players[0]}"/>
<c:set var="showEvent" value="<%= showEvent%>"/>

<div class="contentListItem<%= className %>">
	<div class="fullRow">
		<div class="playerSection floatLeft">
			<div class="floatLeft">
				<span class="colorBar floatLeft" style="border-left-color:<%= "#" + Integer.toHexString(player1.getColor())%>;"></span>
				<img class="floatLeft race" src="<%= player1Icon %>" title="Starcraft II Race - ${player1.playerRace}" alt="Starcraft II Race - ${player1.playerRace}" />
				<div class="floatLeft">
					<p class="playerName" title="Starcraft II player - <%= player1.getDisplayName() %>"><%= player1.getDisplayName() %></p>
					<div>
						<span class="apm floatLeft">APM:<%= player1.getApm() %></span>
						<c:if test="${player1.winner}"><span><img class="trophy floatLeft" src="<%= ResourceHelper.buildImageUrl("trophy_16.png") %>" /></span></c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="map-image-46-container">
			<img class="map-image-46" title="Starcraft II Map - ${replay.map.mapName}" alt="Starcraft II Map - ${replay.map.mapName}" src="<%= replayMap %>" />
			<span class="span.vertical-aligned"></span>
		</div>
		<div class="playerSection floatRight">
			<div class="floatRight">
				<span class="colorBar floatRight" style="border-left-color:<%= "#" + player2.getHexColorValue() %>;"></span>
				<img class="floatRight race" src="<%= player2Icon %>" title="Starcraft II Race - ${player2.playerRace}" alt="Starcraft II Race - ${player2.playerRace}" />
				<div class="floatRight">
					<p class="playerName" title="Starcraft II player - <%= player2.getDisplayName() %>">${player2.displayName}</p>
					<div>
						<span class="apm floatRight">APM:<%= player2.getApm() %></span>
						<c:if test="${player2.winner}"><span><img class="trophy floatRight" src="<%= ResourceHelper.buildImageUrl("trophy_16.png") %>" /></span></c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="action">
			<div class="downloadRow">
				<a href="<%= replayDetailUrl %>" title="<%= replayDescription %>" class="underlineLink">Detail</a>
				<a href="<%= replayDownloadUrl %>" title="Download replay : <%= replayDescription %>" class="underlineLink"><img class="" src="<%= ResourceHelper.buildImageUrl("icon_download.png") %>" title="Download this Starcraft II replay" alt="Download this Starcraft II replay" />Download</a>
			</div>
			<div class="fullRow">
				<p title="<%= totalTitle %>"><%= gameDate %></p>
				<p>Map: ${replay.map.mapName}</p>
				<c:if test="${showEvent}"><p>Event: ${replay.event}</p></c:if>
			</div>
			<div class="fullRow">
			</div>
		</div>
	</div>
	<div class="fullRow">
	</div>
	<a href="<%= replayDetailUrl %>" title="<%= replayDescription %>"><span class="click-container"></span></a>
	<br class="clear"/>
</div>
