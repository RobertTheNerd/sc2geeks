<%@ page import="api.sc2geeks.entity.RefinementField" %>
<%@ page import="com.sc2geeks.front.ui.QueryStringManager" %>
<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ page import="gumi.builders.UrlBuilder" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>

<%--
  User: robert
  Date: 8/19/12
  Time: 8:26 AM
--%>
<%@ include file="playerDetailBase.jsp" %>

<%
	PlayerPerson playerPerson = playerInfo.getPlayerPerson();
	SearchResult<Replay> replaySearchResult = playerInfo.getReplays();
	String race = StringUtils.isBlank(playerPerson.getRace()) ? "random" : playerPerson.getRace().toLowerCase();
	String raceImage = ResourceHelper.buildImageUrl("icon_" + race + "_40.png");
	String playerImage = playerPerson.getImageUrl(240);
	playerImage = StringUtils.isBlank(playerImage) ? "missing-player-photo.v1-240.gif" : playerImage;
	String progamerHomePage = PageUrlBuilder.getProgamerHomePage();
%>
<%!
	private static String getDomainName(String url)
	{
		url = StringUtils.removeStart(url, "http://");
		url = StringUtils.removeStart(url, "www.");
		int pos = url.indexOf('/');
		if (pos != -1)
			url = url.substring(0, pos);

		return url;
	}
%>


<div class="breadcrumb">
	<h1>
	<ol class="crumb">
		<li>
			<a href="<%= PageUrlBuilder.getProgamerHomePage() %>">Starcraft II Progamers</a>
		</li>
		<li>
			<span>&nbsp;&gt;</span>
			<img src="<%= raceImage %>" style="width:16px;height:20px">
			<%= playerPerson.getGameId() %>
		</li>
	</ol>
	</h1>
	<div class="source">
		Source: Liquipedia
	</div>
	<div class="clear"></div>
</div>

<div id="player_detail">
	<div class="sectionTitle"><img src="<%= raceImage %>" title="<%= playerInfo.getPlayerPerson().getRace()%> - Starcraft II progamer <%= playerInfo.getPlayerPerson().getGameId() %>">
		<h1 class="titleText" style="left:40px;" title="Starcraft II progamer <%= playerInfo.getPlayerPerson().getGameId() %>"><%= playerPerson.getGameId() %></h1>
	</div>
	<div id="imageSection" class="progamer-image-240-container">
		<img id="mainImage" class="progamer-image-240" title="Starcraft II progamer <%= playerInfo.getPlayerPerson().getGameId() %>" src="<%= ResourceHelper.buildPlayerImageUrl(playerImage)%>">
		<span class="span.vertical-aligned"></span>
	</div>
	<!-- progamer image -->
	<div id="summarySection">
		<span class="title">Name:</span>
		<%
			String progamerName = StringUtils.isNotBlank(playerPerson.getNativeFullName())
					? playerPerson.getNativeFullName().trim() + "/" : "";
			if (StringUtils.isBlank(playerPerson.getEnFullName()))
				progamerName = progamerName + playerPerson.getEnFullName().trim();
			progamerName = StringUtils.removeEnd(progamerName, "/");
		%>
		<span><%= progamerName %></span>
		<%
			if (StringUtils.isNotBlank(playerPerson.getTeam()))
			{
				String teamUrl = UrlBuilder.fromString(progamerHomePage).
						addParameter(QueryStringManager.Progamer_List_Param_Team, playerPerson.getTeam())
						.toString();

		%>
		<span class="title">Team:</span>
		<span><a class="underlineLink" href="<%= teamUrl%>" title="See all progamers from <%= playerPerson.getTeam() %>"><%= playerPerson.getTeam() %></a></span>
		<%
			}
		%>
		<%
			if (StringUtils.isNotBlank(playerPerson.getRace()))
			{
				String raceUrl = UrlBuilder.fromString(progamerHomePage).
						addParameter(QueryStringManager.Progamer_List_Param_Race, playerPerson.getRace())
						.toString();
		%>
		<span class="title">Race:</span>
		<span><a class="underlineLink" href="<%= raceUrl %>" title="See all progamers playing <%= playerPerson.getRace() %>"><%= playerPerson.getRace() %></a></span>
		<%
			}
		%>

		<%
			if (StringUtils.isNotBlank(playerPerson.getAltIds()))
			{
		%>
		<span class="title">Alternate IDs:</span>
		<span><%= playerPerson.getAltIds() %></span>
		<%
			}
		%>

		<%
			if (StringUtils.isNotBlank(playerPerson.getCountry()))
			{
				String countryUrl = UrlBuilder.fromString(progamerHomePage).
						addParameter(QueryStringManager.Progamer_List_Param_Country, playerPerson.getCountry())
						.toString();
		%>
		<span class="title">Country:</span>
		<span><a class="underlineLink" href="<%= countryUrl %>" title="See all progamers from <%= playerPerson.getCountry() %>"><%= playerPerson.getCountry() %></a></span>
		<%
			}
		%>

		<%
			if (playerPerson.getBirthday() != null)
			{
				SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
				String birthDay = playerPerson.getBirthday().trim();
		%>
		<span class="title">Birthday:</span>
		<span><%= birthDay %></span>
		<%
			}
		%>

		<%
			if (StringUtils.isNotBlank(playerPerson.getStream()))
			{

		%>
		<span class="title">Stream:</span>
		<span><a href="<%= playerPerson.getStream() %>" class="external"><%= getDomainName(playerPerson.getStream()) %></a></span>
		<%
			}
		%>

		<%
			if (StringUtils.isNotBlank(playerPerson.getTwitterHandle()))
			{

		%>
		<span class="title">Twitter:</span>
		<span><a href="<%= playerPerson.getTwitterHandle() %>" class="external"><%= playerPerson.getTwitterHandle() %></a></span>
		<%
			}
		%>

	</div>
	<div style="display:block; height:1px;clear:both;"></div>
</div>

<%
	boolean hasReplay = playerInfo.getReplays() != null && playerInfo.getReplays().getEntityList() != null && playerInfo.getReplays().getEntityList().size() > 0;
	boolean hasTeammate = playerInfo.getTeamMates() != null && playerInfo.getTeamMates().getEntityList() != null && playerInfo.getTeamMates().getEntityList().size() > 0;
	boolean hasPlayerOfSameRace = playerInfo.getPlayersOfSameRace() != null && playerInfo.getPlayersOfSameRace().getEntityList() != null && playerInfo.getPlayersOfSameRace().getEntityList().size() > 0;
	boolean showTabs = hasReplay || hasTeammate || hasPlayerOfSameRace;
	%>
<c:set var="hasReplay" value="<%= hasReplay %>" />
<c:set var="playerPerson" value="<%= playerPerson %>" />
<c:set var="hasTeammate" value="<%= hasTeammate %>" />
<c:set var="hasPlayerOfSameRace" value="<%= hasPlayerOfSameRace %>" />
<c:set var="showTabs" value="${hasReplay || hasTeammate || hasPlayerOfSameRace}" scope="request"/>

<c:if test="${showTabs}">
	<ul class="tabs">
		<c:if test="${hasReplay}">
			<li><a href="#replayPane">Recent replays</a></li>
		</c:if>
		<c:if test="${hasTeammate}">
			<li><a href="#teammatePane">Teammates</a></li>
		</c:if>
		<c:if test="${hasPlayerOfSameRace}">
			<li><a href="#sameRacePane">More <%= playerInfo.getPlayerPerson().getRace() %> progamers</a></li>
		</c:if>
	</ul>
	<div class="panes">
		<c:if test="${hasReplay}">
			<div id="replayPane">
				<%
					{
						String replayUrl = PageUrlBuilder.getReplaySearchPageByProgamer(playerPerson);
				%>
				<div id="contentContainer">
					<div id="navigationContainer">
						<div id="showing">Recent replays of <%= playerPerson.getGameId() %>.
							<%
								if (replaySearchResult.getTotalMatches() > replaySearchResult.getEntityList().size())
								{
							%>
							<a class="underlineLink" href="<%= replayUrl %>">(See all <%= replaySearchResult.getTotalMatches() %>)</a>
							<%
								}
							%>
						</div>

						<div id="showWinner">
							<input id="revealWinner" type="checkbox" name="revealWinner">
							<label for="revealWinner">Reveal winner</label>
						</div>
					</div>
					<h:ReplayResultList replays="<%= replaySearchResult.getEntityList() %>" classNameForLastOne="noborder" />
				</div>
				<%
					}
				%>

			</div>
		</c:if>
		<c:if test="${hasTeammate}">
			<%
				boolean hasMoreTeammates = playerInfo.getTeamMates().getTotalMatches() > playerInfo.getTeamMates().getEntityList().size();
			%>
			<c:set var="hasMoreTeammates" value="<%= hasMoreTeammates%>"></c:set>
			<div id="teammatePane">
				<ul class="liqui_list">
					<%
						SearchResult<PlayerPerson> searchResult = playerInfo.getTeamMates();
						for (int i = 0; i < searchResult.getEntityList().size(); i ++)
						{
							PlayerPerson player = searchResult.getEntityList().get(i);
					%>
					<pd:LiquidPlayerCell playerPerson="<%= player %>"></pd:LiquidPlayerCell>
					<%
						}
					%>
				</ul>
				<div class="clear"></div>
				<c:if test="${hasMoreTeammates}">
				<div class="hasMore">
					<a class="underlineLink" href="<%= PageUrlBuilder.getProgamerListByTeam(playerPerson.getTeam()) %>">See All <%= playerInfo.getTeamMates().getTotalMatches() %></a>
				</div>
				</c:if>
			</div>
		</c:if>
		<c:if test="${hasPlayerOfSameRace}">
			<%
				boolean hasMoreSameRacePlayers = playerInfo.getPlayersOfSameRace().getTotalMatches() > playerInfo.getPlayersOfSameRace().getEntityList().size();
			%>
			<c:set var="hasMoreSameRacePlayers" value="<%= hasMoreSameRacePlayers%>"></c:set>
			<%-- same race players --%>
			<div id="sameRacePane">
				<ul class="liqui_list">
					<%
						SearchResult<PlayerPerson> searchResult = playerInfo.getPlayersOfSameRace();
						for (int i = 0; i < searchResult.getEntityList().size(); i ++)
						{
							PlayerPerson player = searchResult.getEntityList().get(i);
					%>
					<pd:LiquidPlayerCell playerPerson="<%= player %>"></pd:LiquidPlayerCell>
					<%
						}
					%>
				</ul>
				<div class="clear"></div>
				<c:if test="${hasMoreSameRacePlayers}">
				<div class="hasMore">
					<a class="underlineLink" href="<%= PageUrlBuilder.getProgamerListByRace(playerPerson.getRace()) %>">See All <%= playerInfo.getPlayersOfSameRace().getTotalMatches() %></a>
				</div>
				</c:if>
			</div>
		</c:if>
	</div>
</c:if>
