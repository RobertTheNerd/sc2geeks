<%@ tag import="api.sc2geeks.entity.Player" %>
<%@ tag import="api.sc2geeks.entity.RefinementField" %>
<%@ tag import="api.sc2geeks.entity.Replay" %>
<%@ tag import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ tag import="com.sc2geeks.front.view.RelatedReplayInfo" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="org.apache.commons.lang3.tuple.ImmutablePair" %>
<%@ tag import="org.apache.commons.lang3.tuple.MutablePair" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="java.util.Collections" %>
<%@ tag import="java.util.Hashtable" %>
<%@ tag import="java.util.List" %>
<%@ attribute name="replayWithAllInfo" type="api.sc2geeks.entity.ReplayWithAllInfo" required="true" %>
<%@ attribute name="abilityEvents" type="java.util.Hashtable<java.lang.Integer, java.util.List<org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.lang.String>>>" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../base.tag" %>

<%
	List<ImmutablePair<Integer, MutablePair<String[], String[]>>> lines = null;
	List<Player> players = replayWithAllInfo.getReplay().getPlayers();
	if (abilityEvents != null) {
		lines = getEventsToDisplay(abilityEvents);
	}
	boolean hasBuildOrders = (lines != null && lines.size() > 0);

	List<RelatedReplayInfo> relatedReplays = null;
	if (replayWithAllInfo != null)
	{
		Player player1 = replayWithAllInfo.getReplay().getPlayerTeams().get(0).getPlayers().get(0);
		Player player2 = replayWithAllInfo.getReplay().getPlayerTeams().get(1).getPlayers().get(0);

		String url;
		relatedReplays = processRelatedReplays(relatedReplays, "In the series", null, replayWithAllInfo.getReplaysInSeries());

		url = PageUrlBuilder.getReplaySearchPage(RefinementField.MapName, replayWithAllInfo.getReplay().getMap().getMapName());
		relatedReplays = processRelatedReplays(relatedReplays, "On " + replayWithAllInfo.getReplay().getMap().getMapName(), url, replayWithAllInfo.getReplaysOnMap());

		url = PageUrlBuilder.getReplaySearchPage(RefinementField.MatchUpType, replayWithAllInfo.getReplay().getMatchUp());
		relatedReplays = processRelatedReplays(relatedReplays, replayWithAllInfo.getReplay().getMatchUp(), url, replayWithAllInfo.getReplaysOfMatchup());

		url = PageUrlBuilder.getReplaySearchPage(RefinementField.PlayerName, player1.getPlayerName());
		relatedReplays = processRelatedReplays(relatedReplays, player1.getPlayerName(), url, replayWithAllInfo.getReplaysFromPlayer1());

		url = PageUrlBuilder.getReplaySearchPage(RefinementField.PlayerName, player2.getPlayerName());
		relatedReplays = processRelatedReplays(relatedReplays, player2.getPlayerName(), url, replayWithAllInfo.getReplaysFromPlayer2());
	}

%>

<c:set var="hasBuildOrders" value="<%= hasBuildOrders %>" />
<c:set var="lines" value="<%= lines %>" />
<div id="otherInfoRow">
	<div id="topTabHeaders">
		<ul>
			<li><a href="#related">More Replays</a></li>
			<li><a href="#buildOrders">Build Order & Game Play</a></li>
		</ul>
	</div>
	<div id="topTabContainers">
		<rd:RelatedReplays relatedReplays="<%= relatedReplays %>"></rd:RelatedReplays>
		<div id="buildOrders">
			<c:choose>
				<c:when test="${hasBuildOrders}">
					<table>
						<tr>
							<th class="time">Time</th>
							<th class="player"><%= players.get(0).getDisplayName() %></th>
							<th class="player"><%= players.get(1).getDisplayName() %></th>
						</tr>

						<%
							for (ImmutablePair<Integer, MutablePair<String[], String[]>> line : lines) {
								String ability1[] = null, ability2[] = null;
								if (null != line.getRight().getLeft())
									ability1 = line.getRight().getLeft();
								if (null != line.getRight().getRight())
									ability2 = line.getRight().getRight();

						%>
						<tr>
							<td class="time"><%= formatFrameToTime(line.getLeft())%></td>
							<rd:BuildOrderTd actions="<%= line.getRight().getLeft() %>" />
							<rd:BuildOrderTd actions="<%= line.getRight().getRight() %>" />
						</tr>
						<%
							}
						%>

					</table>
				</c:when>
				<c:otherwise>
				No information is available for this replay.
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>


<%!
	// ------------------------------ frame ------------- ability1, ability2 --------------
	private List<ImmutablePair<Integer, MutablePair<String[], String[]>>> getEventsToDisplay(Hashtable<Integer, List<ImmutablePair<Integer, String>>> abilityEvents) {
		List<List<ImmutablePair<Integer, String[]>>> playerEvents = new ArrayList<>(2);
		List<Integer> pids = new ArrayList<>(abilityEvents.keySet());
		Collections.sort(pids);
		for (Integer id : pids) {

			List<ImmutablePair<Integer, String[]>> abilities = new ArrayList<>();
			for (ImmutablePair<Integer, String> ability : abilityEvents.get(id)) {
				String[] actions = parseAction(ability.getRight());
				if (actions != null) {
					abilities.add(new ImmutablePair<>(ability.getLeft(), actions));
				}
			}
			playerEvents.add(abilities);
		}
		int[] indexes = {0, 0};

		// get intermediate list
		// ------------     Pid, ----------------- Frame,  Ability -------------
		List<ImmutablePair<Integer, ImmutablePair<Integer, String[]>>> interlacingList = new ArrayList<>();

		int currentPlayer = 0;
		while (true) {
			// to terminate
			if (indexes[0] == playerEvents.get(0).size()
					&& indexes[1] == playerEvents.get(1).size()) {
				break;
			}

			int opposingPlayer = getOpposingNumber(currentPlayer);

			// if one side is done, assign to opposing side
			if (indexes[0] == playerEvents.get(0).size()
					|| indexes[1] == playerEvents.get(1).size()) {
				if (indexes[currentPlayer] == playerEvents.get(currentPlayer).size()) {
					currentPlayer = opposingPlayer;
				}

				// then loop through the list
				while (indexes[currentPlayer] < playerEvents.get(currentPlayer).size()) {
					// ---------   frame,  ability -------------
					ImmutablePair<Integer, String[]> event = playerEvents.get(currentPlayer).get(indexes[currentPlayer]);
					interlacingList.add(new ImmutablePair<Integer, ImmutablePair<Integer, String[]>>(currentPlayer, event));
					indexes[currentPlayer] ++;
				}
				break;
			}

			// neither list is done, so will move forward one step on either side
			// ----------  frame,  ability --------------
			ImmutablePair<Integer, String[]> currentEvent = playerEvents.get(currentPlayer).get(indexes[currentPlayer]);
			ImmutablePair<Integer, String[]> opposingEvent = playerEvents.get(opposingPlayer).get(indexes[opposingPlayer]);

			if (currentEvent.getLeft() < opposingEvent.getLeft()) {
				// this is the only case we'll get current event.
				interlacingList.add(new ImmutablePair<Integer, ImmutablePair<Integer, String[]>>(currentPlayer, currentEvent));
				indexes[currentPlayer] ++;
			} else {
				// for all other cases, get the opposing event
				interlacingList.add(new ImmutablePair<Integer, ImmutablePair<Integer, String[]>>(opposingPlayer, opposingEvent));
				currentPlayer = opposingPlayer;
				indexes[currentPlayer] ++;
			}
		}

		// get the final list to display
		// ---------------- frame ------------- ability1, ability2 --------------
		List<ImmutablePair<Integer, MutablePair<String[], String[]>>> lines = new ArrayList<>();
		int index = 0;
		while (index < interlacingList.size()) {
			// initialize a new line, getting the first event
			ImmutablePair<Integer, ImmutablePair<Integer, String[]>> event = interlacingList.get(index++);
			int firstPid = event.getLeft();
			int firstSecond = event.getRight().getLeft() >> 4;  // 1 second = 16 frames
			MutablePair<String[], String[]> abilities = new MutablePair<>();
			if (firstPid == 0) {
				abilities.setLeft(event.getRight().getRight());
			} else {
				abilities.setRight(event.getRight().getRight());
			}
			ImmutablePair<Integer, MutablePair<String[], String[]>> line = new ImmutablePair<>(firstSecond, abilities);
			lines.add(line);

			// no more event, wrap up
			if (index >= interlacingList.size()) {
				break;
			}

			// try the second event
			ImmutablePair<Integer, ImmutablePair<Integer, String[]>> secondEvent = interlacingList.get(index);
			int secondSecond = secondEvent.getRight().getLeft() >> 4; // hilarious, isn't it?
			int secondPid = secondEvent.getLeft();
			if (secondSecond == firstSecond && secondPid != firstPid) {
				// move forward the index
				index++;
				if (secondPid == 0) {
					abilities.setLeft(secondEvent.getRight().getRight());
				} else {
					abilities.setRight(secondEvent.getRight().getRight());
				}
			}
		}

		return lines;
	}

	private static int getOpposingNumber(int number) {
		return number == 0 ? 1 : 0;
	}

	/**
	 *
	 * @param action
	 * @return
	 * null if not recognized, or
	 * string[] with 3 elements:
	 *  0 - verb, like Build, Hallicinate, etc, null if none
	 *  1 - none, like Drone, Probe, etc
	 *  2 - type, 'unit', 'building', 'ability', 'upgrade'
	 */
	private static final String[] verbs = {
			"Build",
			"Calldown",
			"MorphTo",
			"Morph",
			"Hallucinate",
			"Spawn",
			"WarpIn",
			"WarpSelection",
			"TransformTo",
			"Train",
			"UpgradeTo"
	};

	private String[] parseAction(String action) {

		if (StringUtils.isBlank(action)) {
			return null;
		}

		String verb = null, noun = null;
		for (String v : verbs) {
			if (action.contains(v)) {
				verb = v;
				noun = action.replace(v, "");
				break;
			}
		}
		String type = websiteConfig.getAbilityType(verb == null ? action : noun);

		if (type == null || "ability".compareToIgnoreCase(type) == 0)
			return null;

		if (StringUtils.isBlank(noun)) {
			noun = action;
		}

		return new String[]{verb, noun, type};
	}

	private String formatFrameToTime(int second) {
		return String.format("%02d:%02d:%02d", second / 3600, (second % 3600) / 60, (second % 3600) % 60);
	}

	private static List<RelatedReplayInfo> processRelatedReplays(List<RelatedReplayInfo> list, String name, String url, List<Replay> replays)
	{
		if (replays != null && replays.size() > 0)
		{
			if (list == null)
				list = new ArrayList<RelatedReplayInfo>();

			RelatedReplayInfo relatedReplayInfo = new RelatedReplayInfo();
			relatedReplayInfo.setTabName(name);
			relatedReplayInfo.setTabId("tab" + Integer.toString(list.size() + 1));
			relatedReplayInfo.setReplays(replays);
			relatedReplayInfo.setUrlForAll(url);
			list.add(relatedReplayInfo);

		}
		return list;
	}
%>