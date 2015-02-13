package api.sc2geeks.entity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/11/11
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTeam
{
	private List<Player> players;

	public List<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(List<Player> players)
	{
		this.players = players;
	}
}
