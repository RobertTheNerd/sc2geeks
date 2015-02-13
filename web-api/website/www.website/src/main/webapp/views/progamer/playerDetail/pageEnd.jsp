<%@ page import="api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo" %>
<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%--
  User: robert
  Date: 8/19/12
  Time: 8:27 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%
	PlayerPersonWithRelatedInfo playerInfo = getValue("playerInfo");
%>
<c:if test="${showTabs}">
	<script src="<%= ResourceHelper.buildJsUrl("jquery.tools.min.1.2.7.js")%>"></script>
</c:if>

<script>
	$(window).load(function(){
		$('#revealWinner').click(function(){
			checkRevealWinner();
		});
		<c:if test="${showTabs}">
		$("ul.tabs").tabs("div.panes > div");
		</c:if>
		$('body').append('<img src="<%= PageUrlBuilder.buildViewProgamerStatsPage(((Integer)playerInfo.getPlayerPerson().getPersonId()).toString()) %>" />')

	});

	function checkRevealWinner() {
		if ($('#revealWinner').is(':checked'))
			$('.fullRow img.trophy').show();
		else
			$('.fullRow img.trophy').hide();

	}
</script>