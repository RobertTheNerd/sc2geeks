<%@ page import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ page import="api.sc2geeks.entity.SearchInput" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%--
  User: robert
  Date: 4/15/12
  Time: 12:21 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="base.jsp" %>
<%@ taglib prefix="hc" tagdir="/WEB-INF/tags/common" %>

<%
	SearchInput searchInput = getValue("searchInput");
	String searchTerms = searchInput == null || searchInput.getSearchTerms() == "*" ? ""
			: StringEscapeUtils.escapeHtml4(searchInput.getSearchTerms());
%>
	<div id="headerContainer">
		<div id="leftHeader">
			<div id="logoContainer">
				<a href="<%= PageUrlBuilder.getPage(PageUrlAlias.Homepage)%>">
				<img class="logo" src="<%= buildImageUrl("logo_beta.png")%>" alt="<%= websiteConfig.getWebsiteName() %>" title="<%= websiteConfig.getWebsiteName() %>" /></a></div>
		</div>
		<div id="rightHeader">
			<div id="rightHeaderTop">
				<div id="socialUp">
					<!-- Place this tag where you want the +1 button to render -->
					<div>
						<g:plusone style="margin-top: 3px;" size="medium" href="<%= buildWebsiteRootUrl() %>"></g:plusone>
					</div>
					<div style="display: none;" class="fb-like" data-href="<%= buildWebsiteRootUrl() %>" data-send="false"
						data-layout="button_count" data-width="30" data-show-faces="false" data-font="arial">
					</div>
				</div>
			</div>
			<%--<div id="rightHeaderBottom">
				<div id="menuContainer">
					<ul>
						<li>
							<img src="Theme/geek2011/img/separator.png" /></li>
						<li>
							<img alt="Replays" title="Replays" src="Theme/geek2011/img/menu_reply.png" />
						</li>
						<li>
							<img src="Theme/geek2011/img/separator.png" /></li>
						<li>
							<img alt="Bookmarks" title="Bookmarks" src="Theme/geek2011/img/menu_bookmark.png" />
						</li>
						<li>
							<img src="Theme/geek2011/img/separator.png" /></li>
						<li>
							<img alt="Bookmarks" title="Bookmarks" src="Theme/geek2011/img/menu_gears.png" />
						</li>
						<li>
							<img src="Theme/geek2011/img/separator.png" /></li>
						<li>
							<img alt="About us" title="About us" src="Theme/geek2011/img/menu_aboutus.png" />
						</li>
						<li>
							<img src="Theme/geek2011/img/separator.png" /></li>
					</ul>
				</div>
				<div id="socialIconContainer">
					<a class="facebook" target="_blank" href="http://www.facebook.com/SC2Geek">
						<img class="bgIcon" border="0" alt="Facebook" title="Facebook" src="Theme/geek2011/img/none.gif"></a>
					<a class="twitter" target="_blank" href="http://twitter.com/SC2Geek">
						<img class="bgIcon" border="0" alt="Twitter" title="Twitter" src="Theme/geek2011/img/none.gif"></a>
				</div>
			</div>--%>
		</div>
		<div id="searchBarContainer">
			<hc:topMenu pageTab="<%= requestContext.getPageTab() %>"></hc:topMenu>
			<%-- wait till it makes sense.
			<div id="hotKeywords">
				<ul>
					<li>
						<img src="<%= buildImageUrl("hotsearch.png") %>" width="25" title="hot keywords" alt="hot keywords" />
					</li>
					<li><a href="#" class="underlineLink">MVP</a></li>
					<li><a href="#" class="underlineLink">Nestea</a></li>
					<li><a href="#" class="underlineLink">Leenock</a></li>
					<li><a href="#" class="underlineLink">Blizzon 2011</a></li>
					<li><a href="#" class="underlineLink">MLG Providence</a></li>
				</ul>
			</div>
			--%>
		</div>
	</div>