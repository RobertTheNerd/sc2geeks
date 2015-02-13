<%@ page import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ page import="com.sc2geeks.front.StatsContainerSetting" %>
<%@ page import="api.sc2geeks.entity.*" %>
<%--
  User: robert
  Date: 4/29/12
  Time: 10:59 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="searchBase.jsp" %>
<%
	String searchResultType = searchResult.getTotalMatches() > 0 ? "Successful" : "NotFound";
	Boolean showCharts = (Boolean)valueStack.findValue("showCharts", false);

%>

<c:set var="showCharts" value="<%= showCharts%>"/>

<c:if test="${showCharts}">
	<script src="<%= buildJsUrl("highcharts.v4.0.4.js") %>"></script>
	<script src="<%= buildJsUrl("highcharts-exporting.v4.0.4.js") %>"></script>
	<script>
		function buildChart() {

			initChart();

			<%
				for (StatsContainerSetting setting : websiteConfig.getStatsContainerSettings()) {
					List<NavigationNode> navNodes = searchResult.getNavigationNodes().get(setting.getRefinementField());
					if (navNodes == null) { continue; }

					String funcName = setting.getChartType().compareToIgnoreCase("pie") == 0 ? "buildPieChart" : "buildBarChart";
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					boolean first = true;
					for (NavigationNode node : navNodes) {
						if (!first) {
							sb.append(",");
						}
						sb.append("['").append(node.getDisplayNodeName().replace("'", "\\'")).append("',").append(node.getCount()).append("]");
						first = false;
					}
					sb.append("]");
			%>
			<%= funcName %>('<%= setting.getDivId()%>', '<%= setting.getTitle()%>', <%= sb.toString() %>);
			<%
				}
			%>
		}
	</script>
</c:if>

<script>
	if (!String.trim) {String.prototype.trim = function() {return this.replace(/^\s+|\s+$/g, "");};}

	$(document).ready(function(){
		// show/hide winner
		$('#revealWinner').change(function(){
			if ($('#revealWinner').is(':checked')) {
				$("img.trophy").show();
				$.cookie('asw', '1', {expires:365, path:'/'});

			} else {
				$("img.trophy").hide();
				$.cookie('asw', '0', {expires:365, path:'/'});
			}
		});

		$('#revealWinner').attr('checked', $.cookie('asw') === '1');
		$('#revealWinner').change();


		// show/hide stats
		$('#toggleStats').change(function(){
			if ($('#toggleStats').is(':checked')) {
				$('#replay-stats').show();
				$.cookie('sc', '1', { path:'/'});   // session cookie
			} else {
				$('#replay-stats').hide();
				$.cookie('sc', '0', { path:'/'});   // session cookie
			}
		});
		$('#toggleStats').attr('checked', $.cookie('sc') === '1' || $.cookie('sc') === null);
		$('#toggleStats').change();

	});

    $(window).load(function () {
		<%-- overlay for all navigation values --%>
		$('a.moreNav').overlay({
			// some mask tweaks suitable for modal dialogs
			mask: {
				color: '#171717',
				loadSpeed: 200,
				opacity: 0.9
			},
			// close: $('#navAllClose img'),
			closeOnClick: true,
			top: 'center',

			onBeforeLoad: function() {
				// grab wrapper element inside content
				var wrap = this.getOverlay().find("#navWrapper");

				// load the page specified in the trigger
				wrap.text('Loading ...');
				wrap.load(this.getTrigger().attr("href"));
			},
			onLoad : function() {
            }
		});
		$('a.moreNav').attr("style", "");

	    <c:if test="${showCharts}">
	    buildChart();
	    </c:if>
<%
if (websiteConfig.isRenderGoogleAnalytics())
{
%>
		_gaq.push('_trackEvent', 'SearchResult', '<%= searchResultType %>', '<%= searchInput.getSearchTerms() %>', <%= searchResult.getTotalMatches()%>, true);
<%
}
%>

	});
</script>
