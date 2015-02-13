<%@ tag import="api.sc2geeks.entity.Replay" %>
<%@ attribute name="replays" type="java.util.List<api.sc2geeks.entity.Replay>" required="true" %>
<%@ attribute name="classNameForLastOne" type="java.lang.String" required="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="base.tag" %>

<c:if test="${! empty replays}">
	<c:forEach var="i" begin="0" end="${fn:length(replays) - 1}">
		<c:choose>
			<c:when test="${i == (fn:length(replays) - 1)}">
				<h:SingleReplayUnit position="${i}" replay="${replays[i]}" extraClassName="${classNameForLastOne}"></h:SingleReplayUnit>
			</c:when>
			<c:otherwise>
				<h:SingleReplayUnit position="${i}" replay="${replays[i]}"></h:SingleReplayUnit>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</c:if>
