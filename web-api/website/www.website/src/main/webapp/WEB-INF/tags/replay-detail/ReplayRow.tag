<%@ tag import="api.sc2geeks.entity.Player" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="java.text.DateFormat" %>
<%@ tag import="java.text.SimpleDateFormat" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ attribute name="replay" type="api.sc2geeks.entity.Replay" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../base.tag" %>

<%
	// players will be sorted by pid
	Player player1 = replay.getPlayers().get(0);
	Player player2 = replay.getPlayers().get(1);

	boolean showEvent = StringUtils.isNotBlank(replay.getEvent());
	String gameDate;
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	gameDate = format.format(replay.getGameDate());
	String duration;
	int length = replay.getDuration();
	if (length > 3600)
	{
		duration = String.format("%1$d:%2$02d:%3$2d", length / 3600, (length / 60) % 60, length % 60);
	} else
	{
		duration = String.format("%1$02d:%2$02d", length / 60, length % 60);
	}
	String mapImageName = replay.getMap().getImageUrl(124);
	String mapUrl = ResourceHelper.buildMap124ImageUrl(StringUtils.isBlank(mapImageName) ?
			"unknown_map.jpg" : mapImageName);

	String eventUrl = showEvent ? PageUrlBuilder.getReplaySearchPage(RefinementField.Event, replay.getEvent()) : null;
%>

<c:set var="player1" value="<%= player1 %>" />
<c:set var="player2" value="<%= player2 %>" />
<c:set var="showEvent" value="<%= showEvent %>" />
<c:set var="player1InGameId" value="<%= player1.getPlayerName().compareToIgnoreCase(player1.getProgamerName()) != 0 %>" />
<c:set var="player2InGameId" value="<%= player2.getPlayerName().compareToIgnoreCase(player2.getProgamerName()) != 0 %>" />
<div id="replayRow">
	<div id="main">
		<div>
			<div class="playerSection">
				<div class="floatRight">
					<p class="playerName" title="Starcraft II player - <%= player1.getDisplayName() %>" style="text-align: left;">${player1.displayName}</p>
					<div class="floatLeft inGameId"><c:if test="${player1InGameId}">Replay Id: <%= player1.getPlayerName() %></c:if></div>
					<div class="floatLeft clear">
						<span class="apm floatLeft">APM:${player1.apm}</span>
						<c:if test="${player1.winner}">
							<span class="floatLeft"><img class="trophy" style="display:none;" src="<%= ResourceHelper.buildImageUrl("trophy_25.png")%>" /></span>
						</c:if>
					</div>
				</div>
				<img class="floatRight" style="border-left: 10px solid <%= "#" + player1.getHexColorValue() %>; margin-right: 10px;"
					src="<%= ResourceHelper.buildImageUrl("icon_" + player1.getPlayerRace().toLowerCase() + "_M.png") %>"
				    title="Starcraft II Race - ${player1.playerRace}" alt="Starcraft II Race - ${player1.playerRace}"
						/>
			</div>
			<div class="map-image-124-container">
				<img class="map-image-124" alt="Starcraft II Map - ${replay.map.mapName}" title="Starcraft II Map - ${replay.map.mapName}" src="<%= mapUrl %>" />
				<span class="span.vertical-aligned"></span>
			</div>
			<div class="playerSection">
				<div class="floatLeft playerName">
					<p class="playerName" title="Starcraft II player - <%= player2.getDisplayName() %>" style="text-align: right;">${player2.displayName}</p>
					<div class="floatRight inGameId"><c:if test="${player2InGameId}">Replay Id: <%= player2.getPlayerName() %></c:if></div>
					<div class="floatRight clear">
						<span class="apm floatRight">APM:${player2.apm}</span>
						<c:if test="${player2.winner}">
							<span class="floatRight"><img class="trophy" style="display:none;" src="<%= ResourceHelper.buildImageUrl("trophy_25.png")%>" /></span>
						</c:if>
					</div>
				</div>
				<img class="floatLeft" style="border-right: 10px solid <%= "#" + player2.getHexColorValue() %>; margin-left: 10px;"
				     src="<%= ResourceHelper.buildImageUrl("icon_" + player2.getPlayerRace().toLowerCase() + "_M.png") %>"
				     title="Starcraft II Race - ${player2.playerRace}" alt="Starcraft II Race - ${player2.playerRace}"
							/>
			</div>
		</div>
		<br class="clear" />
		<div id="action">
			<a class="download sc2font" title="Download this Starcraft II replay" href="<%= PageUrlBuilder.getReplayDownloadPage(replay)%> ">Download</a>
			<a id="revealWinner" class="action sc2font" href="#" title="Reveal Winner" ><img src="<%= ResourceHelper.buildImageUrl("trophy_16.png") %>" /> Reveal Winner </a>
		</div>
	</div>
	<div id="summary">
		<div id="summaryTitle">
			Game Information</div>
		<div>
			<span class="summaryName">Map:</span> <span class="summaryText"><a class="underlineLink" title="More Starcraft II replays on ${replay.map.mapName}" href="<%= PageUrlBuilder.getReplaySearchPage(RefinementField.MapName, replay.getMap().getMapName()) %>">${replay.map.mapName}</a></span>
		</div>
		<div>
			<span class="summaryName">Date:</span> <span class="summaryText"><%= gameDate %></span>
		</div>
		<div>
			<span class="summaryName">Length:</span> <span class="summaryText"><%= duration %></span>
		</div>
		<c:if test="${showEvent}">
		<div>
			<span class="summaryName">Event:</span> <span class="summaryText"><a class="underlineLink" title="More Starcraft II replays from <%= replay.getEvent() %>"
			href="<%= eventUrl %>">${replay.event}</a></span>
		</div>
		</c:if>
		<div>
			<span class="summaryName">Series:</span> <span class="summaryText">${replay.seriesNumber} of ${replay.seriesCount}</span>
		</div>
		<div>
			<span class="summaryName">Region:</span> <span class="summaryText">${replay.gateway}</span>
		</div>
		<%-- todo: wait till there is enough data
		<div>
			<span class="summaryName" title="Downloads">DLs:</span> <span class="summaryText">3158 downloads</span>
		</div>
		--%>
	</div>
</div>
