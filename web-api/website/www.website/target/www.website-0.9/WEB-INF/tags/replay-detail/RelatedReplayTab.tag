<%@ tag import="api.sc2geeks.entity.Replay" %>
<%@ tag import="java.util.List" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ attribute name="relatedReplayInfo" type="com.sc2geeks.front.view.RelatedReplayInfo" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../base.tag" %>

<%
	List<Replay> replays;
	if (relatedReplayInfo.getReplays().size() <= websiteConfig.getMaxReplayCountInRelatedTab())
		replays = relatedReplayInfo.getReplays();
	else
		replays = relatedReplayInfo.getReplays().subList(0, websiteConfig.getMaxReplayCountInRelatedTab());
	boolean showAllButton = (relatedReplayInfo.getReplays().size() > websiteConfig.getMaxReplayCountInRelatedTab())
			&& (StringUtils.isNotBlank(relatedReplayInfo.getUrlForAll()));
%>
<c:set var="showAllButton" value="<%= showAllButton %>" />
<div id="${relatedReplayInfo.tabId}" class="relatedSection">
	<h:ReplayResultList replays="<%= replays %>" classNameForLastOne="noborder" />
	<c:if test="${showAllButton}">
		<div class="showAll"><a class="sc2font underlineLink" href="${relatedReplayInfo.urlForAll}">Show All</a></div>
	</c:if>
</div>
