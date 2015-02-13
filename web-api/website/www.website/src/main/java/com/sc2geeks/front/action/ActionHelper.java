package com.sc2geeks.front.action;

import api.sc2geeks.entity.Player;
import api.sc2geeks.entity.PlayerTeam;
import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.SearchInput;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/9/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionHelper
{
	public static SearchInput getSearchInput(String actionName, Map params)
	{
		return null;
	}

	public static String getSearchActionName(SearchInput searchInput)
	{
		return null;
	}

	public static String getReplayDetailActionName(Replay replay)
	{
		if (replay == null)
			return null;

		StringBuilder sb = new StringBuilder();

		for (PlayerTeam team : replay.getPlayerTeams())
		{
			for (Player p : team.getPlayers())
			{
				sb.append(p.getPlayerName());
				if (p != team.getPlayers().get(team.getPlayers().size() - 1))
					sb.append("+");
			}
			if (team != replay.getPlayerTeams().get(replay.getPlayerTeams().size() - 1))
			{
				sb.append("-vs-");
			}
		}

		sb.append("-").append(replay.getMap().getMapName());
		// sb.append("-").append(replay.getMatchup)
		// sb.append("-").append(replay.getEvent());
		sb.append("-").append(replay.getGameId());

		return sb.toString();
	}

	/**
	 * @param actionName
	 * @return -1 if n/a
	 */
	public static int getReplayId(String actionName)
	{
		String[] parts = actionName.split("-");
		if (parts == null || parts.length == 0)
		{
			return -1;
		}
		try
		{
			return Integer.parseInt(parts[parts.length - 1]);
		} catch (Exception e)
		{
			return -1;
		}
	}
}
