<%@ attribute name="relatedReplays" type="java.util.List<com.sc2geeks.front.view.RelatedReplayInfo>" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../base.tag" %>

<c:if test="${! empty relatedReplays}">
<div id="related">

	<div id="moreGames">
		<span class="sc2font relatedTitle">More Starcraft II replays</span>
		<label for="moreRevealWinnerBox">Reveal Winner</label>
		<input type="checkbox" id="moreRevealWinnerBox">
	</div>
	<div id="tabHeader">
		<ul>
			<c:forEach var="i" begin="0" end="${fn:length(relatedReplays) - 1}">
				<li><a href="#${relatedReplays[i].tabId}">${relatedReplays[i].tabName}</a></li>
			</c:forEach>
		</ul>
	</div>
	<div id="tabContainer">
	<c:forEach var="relatedReplay" items="${relatedReplays}">
		<rd:RelatedReplayTab relatedReplayInfo="${relatedReplay}"></rd:RelatedReplayTab>
	</c:forEach>
	</div>
</div>
</c:if>
