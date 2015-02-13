<%@ page import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%--
  User: robert
  Date: 4/15/12
  Time: 8:07 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="base.jsp" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<c:set var="websiteConfig" value="<%= websiteConfig %>" />
<tiles:insertAttribute name="preRender" ignore="true"></tiles:insertAttribute>
<!doctype html>
<html class="no-js" lang="en">
<tiles:insertAttribute name="htmlHead" ignore="false"></tiles:insertAttribute>
<body>
	<div id="topContainer">
		<div id="wrap_shadow" class="wrap_style">
			<!--[if lt IE 8]><p class="chromeframe">Hi Grandpa, your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->
			<div id="pageContainer">
				<header>
					<tiles:insertAttribute name="pageHeader" ignore="false"></tiles:insertAttribute>
				</header>
				<div id="bodyContainer" role="body">
					<tiles:insertAttribute name="pageBodyMain" ignore="true"></tiles:insertAttribute>
				</div>

				<footer>
					<tiles:insertAttribute name="pageFooter" ignore="true"></tiles:insertAttribute>
				</footer>
			</div>
		</div>
	</div>
	<script src="<%= buildJsUrl("jquery-1.7.1.min.js")%> "></script>
	<script src="<%= buildJsUrl("jquery-ui.1.8.18.min.js") %> "></script>
	<script>window.jQuery || document.write('<script src="<%= ResourceHelper.buildJsUrl("jquery-1.7.1.min.js") %>"><\/script>')</script>
	<script src="<%= ResourceHelper.buildJsUrl("common.v2015.01.02.js") %>"></script>


	<script>
		var baseUrl = '<%= PageUrlBuilder.getPage(PageUrlAlias.ReplaySearch) %>';
		var extension = '.<%= websiteConfig.getDefaultExtension() %>';

		$(window).load(function(){
			<!-- facebook like button -->
			(function (d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) return;
				js = d.createElement(s); js.id = id;
				js.src = "//connect.facebook.net/en_US/all.js#xfbml=1";
				fjs.parentNode.insertBefore(js, fjs);
			} (document, 'script', 'facebook-jssdk'));

			<!-- google plus one  -->
			(function () {
				var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
				po.src = 'https://apis.google.com/js/plusone.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
				$("div.fb-like").show();
			})();

			<%-- search for submission, validation --%>
			$('#formSearch').submit(function(){
				var term = $('#searchbox').val();
				if (term.trim() == '')
				{
					$('#searchbox').focus();
					return false;
				}
				return true;
			});

		});
	</script>
	<tiles:insertAttribute name="scriptAtPageEnd" ignore="true"></tiles:insertAttribute>
	<c:if test="${websiteConfig.renderFeedback}">
		<script type="text/javascript">
		  var uvOptions = {};
		  (function() {
		    var uv = document.createElement('script'); uv.type = 'text/javascript'; uv.async = true;
		    uv.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'widget.uservoice.com/q4DOLzWSrKVPSrjtOJUWRw.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(uv, s);
		  })();
		</script>
	</c:if>
	<c:if test="${websiteConfig.renderGoogleAnalytics}">
		<script type="text/javascript">

			var _gaq = _gaq || [];
			_gaq.push(['_setAccount', 'UA-31727125-1']);
			_gaq.push(['_trackPageview']);

			(function() {
				var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			})();

		</script>
	</c:if>
	<c:if test="${websiteConfig.renderStatCounter}">
		<!-- Start of StatCounter Code for Default Guide -->
		<script type="text/javascript">
			var sc_project=7944680;
			var sc_invisible=1;
			var sc_security="70dd1e0e";
		</script>
		<script type="text/javascript" src="http://www.statcounter.com/counter/counter.js"></script>
		<noscript><div class="statcounter"><a title="tumblr counter"
		                                      href="http://statcounter.com/tumblr/" target="_blank"><img
				class="statcounter"
				src="http://c.statcounter.com/7944680/0/70dd1e0e/1/"
				alt="tumblr counter"></a></div></noscript>
		<!-- End of StatCounter Code for Default Guide -->
	</c:if>
	<c:if test="${websiteConfig.renderFootPrintTag}">
		<!-- FOOTPRINT HTML START -->
		<script type="text/javascript" src="http://script.footprintlive.com/?site=www.sc2geeks.com"></script><noscript><a href="http://www.footprintlive.com" target="_blank"><img src="http://img.footprintlive.com/?cmd=nojs&amp;site=www.sc2geeks.com" alt="free tracking" border="0" /></a></noscript>
		<!-- FOOTPRINT HTML END -->
	</c:if>
</body>
</html>