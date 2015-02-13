package api.sc2geeks.entity.playerperson;

import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.SearchResult;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 9/29/12
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPersonWithRelatedInfo
{
	private PlayerPerson playerPerson;
	SearchResult<Replay> replays;
	SearchResult<PlayerPerson> teamMates;
	SearchResult<PlayerPerson> playersOfSameRace;

	public PlayerPerson getPlayerPerson()
	{
		return playerPerson;
	}

	public void setPlayerPerson(PlayerPerson playerPerson)
	{
		this.playerPerson = playerPerson;
	}

	public SearchResult<Replay> getReplays()
	{
		return replays;
	}

	public void setReplays(SearchResult<Replay> replays)
	{
		this.replays = replays;
	}

	public SearchResult<PlayerPerson> getTeamMates()
	{
		return teamMates;
	}

	public void setTeamMates(SearchResult<PlayerPerson> teamMates)
	{
		this.teamMates = teamMates;
	}

	public SearchResult<PlayerPerson> getPlayersOfSameRace()
	{
		return playersOfSameRace;
	}

	public void setPlayersOfSameRace(SearchResult<PlayerPerson> playersOfSameRace)
	{
		this.playersOfSameRace = playersOfSameRace;
	}
}
