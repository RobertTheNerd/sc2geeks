<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="playerPerson" type="api.sc2geeks.entity.playerperson.PlayerPerson" required="true" %>
<%@ attribute name="lazyLoadImage" type="java.lang.Boolean" required="false" %>

<%@ include file="../../base.tag" %>

<%
	String replayUrl = PageUrlBuilder.getReplaySearchPage(RefinementField.Progamer, playerPerson.getGameId() + "-" + playerPerson.getPersonId());
	String playerImage = playerPerson.getImageUrl(80);
	playerImage = StringUtils.isBlank(playerImage) ? "missing-player-photo.v1.gif" : playerImage;
	lazyLoadImage = lazyLoadImage == null ? false : lazyLoadImage;
	String imageClassName = lazyLoadImage ? "player_image lazyload" : "player_image";
	String imageUrl = lazyLoadImage ? ResourceHelper.buildImageUrl("ph.gif") : ResourceHelper.buildPlayerImageUrl(playerImage);
%>

<li>
	<div class="player-image-container">
		<a href="<%= PageUrlBuilder.getProgamerDetailPage(playerPerson) %>" title="Starcraft II Progamer : <%= playerPerson.getGameId() %>">
		<img title="Starcraft II Progamer : <%= playerPerson.getGameId() %>" class="<%= imageClassName %>" src="<%= imageUrl %>" <% if (lazyLoadImage) {%> data-original="<%= ResourceHelper.buildPlayerImageUrl(playerImage) %>" <%}%>>
		</a>
	</div>
	<div class="info-container">
		<%
			if(StringUtils.isNotBlank(playerPerson.getRace()))
			{
				%>
		<img class="pricon" src="<%= ResourceHelper.buildImageUrl("icon_" + playerPerson.getRace().toLowerCase() + "_40.png") %>">
		<%

			}
		%>
		<span class="pname"><%= playerPerson.getGameId()%></span>
		<span class="title">Name:</span><span><%= playerPerson.getNativeFullName() %></span>
		<span class="title">Race:</span> <span><%= playerPerson.getRace() %></span>
		<%
			if (StringUtils.isNotBlank(playerPerson.getTeam()))
			{
				%>
		<span class="title">Team:</span> <span><%= playerPerson.getTeam() %></span>
		<%
			}
		%>
		<span class="links"><a class="underlineLink" href="<%= PageUrlBuilder.getProgamerDetailPage(playerPerson) %>">Detail</a>
		<%
			if (playerPerson.getReplayCount() > 0)
			{
		%>
		<a class="underlineLink" href="<%= replayUrl %>">Replays (<%= playerPerson.getReplayCount() %>)</a>
		<%
			}
		%>
		</span>
	</div>
	<a href="<%= PageUrlBuilder.getProgamerDetailPage(playerPerson) %>"><span class="click-container"></span></a>
</li>