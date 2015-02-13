<%@ page import="api.sc2geeks.entity.ReplayWithRelatedInfo" %>
<%--
  User: robert
  Date: 5/6/12
  Time: 1:06 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%
	ReplayWithRelatedInfo replayWithAllInfo = getValue("replayWithAllInfo");
%>
<c:set var="replayInfo" value="<%= replayWithAllInfo %>"></c:set>

<script>
	$(window).load(function(){
		$('#revealWinner').click(function(){
			$('#replayRow img.trophy').show();
		});
		$('#moreRevealWinnerBox').click(function(){
			if ($('#moreRevealWinnerBox').is(':checked'))
				$('.fullRow img.trophy').show();
			else
				$('.fullRow img.trophy').hide();
		});

		$('#related').tabs();
		$('#related').show();
		$('#otherInfoRow').tabs();
		// $('#related').fadeIn(100);

		<c:if test="${replayInfo != null}">
		$('body').append('<img src="<%= PageUrlBuilder.buildViewReplayStatsPage(replayWithAllInfo.getReplay().getGameId()) %>" />');
		</c:if>
	});
</script>