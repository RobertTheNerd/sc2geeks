package api.sc2geeks.entity;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/11/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Replay {
	private String gameId;
	private Map map;
	private int duration;
	private int downloadCount;
	private String version;
	private Date gameDate;
	private String replayFile;
	private String gateway;
	private String event;
	private String matchUp;

	// series
	private String seriesGameId;
	private int seriesCount;
	private int seriesNumber;
	private Date seriesLastGameDate;
	private Date seriesFirstGameDate;

	private List<Player> winners;

	private List<PlayerTeam> playerTeams;
	private List<Player> players;


	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMatchUp() {
		return matchUp;
	}

	public void setMatchUp(String matchUp) {
		this.matchUp = matchUp;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	public String getReplayFile() {
		return replayFile;
	}

	public void setReplayFile(String replayFile) {
		this.replayFile = replayFile;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public List<PlayerTeam> getPlayerTeams() {
		return playerTeams;
	}

	public void setPlayerTeams(List<PlayerTeam> playerTeams) {
		this.playerTeams = playerTeams;
		for (PlayerTeam team : playerTeams) {
			if (team.getPlayers().get(0).isWinner()) {
				winners = team.getPlayers();
				break;
			}
		}

		// get matchup
		Player player = playerTeams.get(0).getPlayers().get(0);
		char firstRace = player.getPlayerRace().toUpperCase().charAt(0);
		player = playerTeams.get(1).getPlayers().get(0);
		char secondRace = player.getPlayerRace().toUpperCase().charAt(0);

		matchUp = firstRace <= secondRace ? firstRace + "v" + secondRace : secondRace + "v" + firstRace;
	}

	public List<Player> getWinners() {
		return winners;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getSeriesGameId() {
		return seriesGameId;
	}

	public void setSeriesGameId(String seriesGameId) {
		this.seriesGameId = seriesGameId;
	}

	public int getSeriesCount() {
		return seriesCount;
	}

	public void setSeriesCount(int seriesCount) {
		this.seriesCount = seriesCount;
	}

	public int getSeriesNumber() {
		return seriesNumber;
	}

	public void setSeriesNumber(int seriesNumber) {
		this.seriesNumber = seriesNumber;
	}

	public Date getSeriesLastGameDate() {
		return seriesLastGameDate;
	}

	public void setSeriesLastGameDate(Date seriesLastGameDate) {
		this.seriesLastGameDate = seriesLastGameDate;
	}

	public Date getSeriesFirstGameDate() {
		return seriesFirstGameDate;
	}

	public void setSeriesFirstGameDate(Date seriesFirstGameDate) {
		this.seriesFirstGameDate = seriesFirstGameDate;
	}

	public String toString() {
		try {
			Player player1 = getPlayerTeams().get(0).getPlayers().get(0);
			Player player2 = getPlayerTeams().get(1).getPlayers().get(0);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			return String.format("%1$s(%2$s) vs %3$s(%4$s) on %5$s, %6$s",
					player1.getPlayerName(),
					player1.getPlayerRace(),
					player2.getPlayerName(),
					player2.getPlayerRace(),
					getMap().getMapName(),
					format.format(getGameDate())
			);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Guaranteed to sort players by pid asc
	 * @return
	 */
	public List<Player> getPlayers() {
		if (players == null) {
			players = new ArrayList<>();
			List<Integer> ids = new ArrayList<>();
			Hashtable<Integer, Player> playerHashtable = new Hashtable<>();
			for (PlayerTeam team : this.getPlayerTeams()) {
				for (Player player : team.getPlayers()) {
					ids.add(player.getPid());
					playerHashtable.put(player.getPid(), player);
				}
			}
			Collections.sort(ids);
			for (Integer id : ids) {
				players.add(playerHashtable.get(id));
			}
		}
		return players;
	}
}
