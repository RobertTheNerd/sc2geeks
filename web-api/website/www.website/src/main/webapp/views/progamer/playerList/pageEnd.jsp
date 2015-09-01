<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%--
  User: robert
  Date: 8/19/12
  Time: 8:27 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<script src="<%= ResourceHelper.buildJsUrl("jquery.lazyload.min.1.8.1-dev.js")%>"></script>
<script src="<%= ResourceHelper.buildJsUrl("chosen.jquery.min.v2012.10.01.js")%>"></script>
<script src="<%= ResourceHelper.buildJsUrl("jsuri-1.1.1.min.js")%>"></script>
<script>
	$(document).ready(function(){
		$(".player_image").show().lazyload();
	});

	function changeUrl(){
		$("#withReplaysOnly").attr("disabled", true);

		var uri = new Uri("<%= PageUrlBuilder.getProgamerHomePage()%>");

		if ($("#playerTeam").val())
			uri.replaceQueryParam("playerTeam", $("#playerTeam").val());
		else
			uri.deleteQueryParam("playerTeam");

		if ($("#race").val())
			uri.replaceQueryParam("race", $("#race").val());
		else
			uri.deleteQueryParam("race");

		if ($("#country").val())
			uri.replaceQueryParam("country", $("#country").val());
		else
			uri.deleteQueryParam("country");

		$("#withReplaysOnly").is(":checked") ? uri.replaceQueryParam("withReplaysOnly", "true") : uri.deleteQueryParam("withReplaysOnly");

		window.location = uri;
	}
	$(window).load(function(){
		$(".chzn-select").chosen({allow_single_deselect:true}); $(".chzn-select-deselect").chosen({allow_single_deselect:true});
		$("#searchOptions").show();
		$(".chzn-select").chosen().change(changeUrl);
		$("#withReplaysOnly").change(changeUrl);

	});
</script>