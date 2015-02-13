<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ page import="api.sc2geeks.entity.playerperson.PlayerPerson" %>
<%@ page import="com.sc2geeks.front.ui.PageUrlAlias" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="api.sc2geeks.entity.NavigationNode" %>
<%@ page import="api.sc2geeks.entity.RefinementField" %>
<%--
  User: robert
  Date: 8/19/12
  Time: 8:26 AM
--%>
<%@ include file="playerbase.jsp" %>
<div class="breadcrumb">
	<ol class="crumb">
		<li>
			<h1>Starcraft II Progamers</h1>
		</li>
	</ol>
	<div class="source">
		Source: Liquipedia
	</div>
	<div class="clear"></div>
</div>
<div id="playerlistContainer">
	<!-- tab "panes" -->
	<div class="panes">
		<div id="searchOptions" style="display:none;">
		<pd:ProgamerDropdown id="race" navigationNodes="<%= races %>" selectedValue="<%= race %>" prompt="All Races" width="150" />
		<pd:ProgamerDropdown id="playerTeam" navigationNodes="<%= playerTeams %>" selectedValue="<%= playerTeam %>" prompt="All Teams" width="250" />
		<pd:ProgamerDropdown id="country" navigationNodes="<%= countries %>" selectedValue="<%= country %>" prompt="All Countries" width="180" />
			<div id="replayOnlySection">
				<input id="withReplaysOnly" type="checkbox"<%= replayOnlyChecked %> value="Show those having replays only."/>
				<label for="withReplaysOnly">Pro-gamers with replays only</label>

			</div>
		</div>
		<div>
			<%
				if (searchResult != null && searchResult.getEntityList() != null
						&& searchResult.getEntityList().size() > 0)
				{
					%>
			<ul class="liqui_list">
				<%
					for (int i = 0; i < searchResult.getEntityList().size(); i ++)
					{
						PlayerPerson player = searchResult.getEntityList().get(i);
				%>
				<pd:LiquidPlayerCell playerPerson="<%= player %>" lazyLoadImage="<%= i >= 21 %>"></pd:LiquidPlayerCell>
			<%
					}
					%>
			</ul>
			<%
				}
			%>

		</div>
	</div>
</div>