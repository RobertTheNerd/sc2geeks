package com.sc2geeks.front.view;

import api.sc2geeks.entity.Player;
import api.sc2geeks.entity.Replay;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/19/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayHelper
{
	public static String getReplaySummary(Replay replay)
	{
		Player player1 = replay.getPlayerTeams().get(0).getPlayers().get(0);
		Player player2 = replay.getPlayerTeams().get(1).getPlayers().get(0);
		String res = String.format("%1$s vs %2$s on Map %3$s. Match up: %4$s.",
					player1.getPlayerName(), player2.getPlayerName(), replay.getMap().getMapName(), replay.getMatchUp());

		if (replay.getSeriesCount() > 1)
		{
			res = res + " " + replay.getSeriesNumber() + " of " + replay.getSeriesCount();
		}
		return res;
	}
}
