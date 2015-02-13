package sc2geeks.front.action;

import api.sc2geeks.entity.Map;
import api.sc2geeks.entity.Player;
import api.sc2geeks.entity.PlayerTeam;
import api.sc2geeks.entity.Replay;
import com.sc2geeks.front.action.ActionHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/9/12
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionHelperTest
{
	// @Test
	public void testGetReplayId() throws Exception
	{
		assertEquals(ActionHelper.getReplayId("a-vs-b-lost-temple-tvp-blizzcon2012-1213"), 1213);
		assertEquals(ActionHelper.getReplayId("a-vs-b-lost-temple-tvp-blizzcon2012-diu"), -1);
	}

	// @Test
	public void testGetReplayDetailActionName() throws Exception
	{
		Replay replay = new Replay();
		replay.setGameId("1538");
		Map map = new Map();
		map.setMapName("Lost temple");
		replay.setMap(map);

		Player player = new Player();
		player.setPlayerName("OgsMC");
		PlayerTeam team = new PlayerTeam();
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		team.setPlayers(players);

		List<PlayerTeam> teams = new ArrayList<PlayerTeam>();
		teams.add(team);


		player = new Player();
		player.setPlayerName("Immvp");
		team = new PlayerTeam();
		players = new ArrayList<Player>();
		players.add(player);
		team.setPlayers(players);

		teams.add(team);

		replay.setPlayerTeams(teams);

		assertEquals(ActionHelper.getReplayDetailActionName(replay), "OgsMC-vs-Immvp-Lost temple-1538");

	}
}
