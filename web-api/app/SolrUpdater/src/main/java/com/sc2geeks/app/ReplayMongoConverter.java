package com.sc2geeks.app;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by robert on 8/27/14.
 */
public class ReplayMongoConverter implements MongoObjConverter{
	private static Logger logger = Logger.getLogger(ReplayMongoConverter.class);

	@Override
	public SolrInputDocument convertToSolrDoc(DBObject replay)
	{
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("p_game_id", replay.get("_id"));
		doc.addField("f_game_version", replay.get("release"));
		doc.addField("f_event", getReplayEvent(replay));
		doc.addField("p_replay_file", getReplayFile(replay));

		doc.addField("p_game_date", (Date)replay.get("start_time"));
		doc.addField("p_duration", (int)replay.get("real_length"));
		doc.addField("f_game_length", getGameLengthCategory((int)replay.get("real_length")));


		ArrayList<DBObject> players = getPlayers(replay);

		addPlayer(replay, doc, players.get(0), 1);
		addPlayer(replay, doc, players.get(1), 2);

		doc.addField("f_race", players.get(0).get("play_race"));
		doc.addField("f_race", players.get(1).get("play_race"));

		doc.addField("p_gateway", (String)players.get(0).get("region"));
		doc.addField("pf_race_type", getMatchUpType(players.get(0), players.get(1)));

		// map
		DBObject map = (DBObject)replay.get("map");
		fillMapInfo(doc, map);

		// series
		doc.addField("p_series_replay_id", replay.get("series_first_replay_id"));
		doc.addField("p_series_count", replay.get("series_count"));
		doc.addField("p_series_number", replay.get("series_number"));
		doc.addField("p_series_first_date", replay.get("series_first_start_time"));
		doc.addField("p_series_last_date", replay.get("series_last_start_time"));

		return doc;
	}

	@Override
	public String getUniqueId() {
		return "p_game_id";
	}

	private  String getReplayEvent(DBObject replay)
	{
		return MongoHelper.getEventByTaskIds((BasicDBList) replay.get("taskIds"));
	}

	/**
	 *
	 * @return
	 *  res[0] - winner
	 *  res[1] - player1
	 *  res[2] - player2
	 *  or null if anything is wrong with either the replay or the parsing
	 */
	private static ArrayList<DBObject> getPlayers(DBObject replay) {
		try {
			List<DBObject> teams = (List<DBObject>) replay.get("teams");
			DBObject player1 = getTeamPlayer(teams, 0, 0);
			DBObject player2 = getTeamPlayer(teams, 1, 0);

			ArrayList<DBObject> list = new ArrayList<>(2);

			list.add(player1);
			list.add(player2);

			return list;
		} catch(Exception e) {
			logger.error("Error occurred while getting players.", e);
			return null;
		}
	}

	private static DBObject getTeamPlayer(List<DBObject> teams, int teamIndex, int playerIndex) {
		DBObject team = teams.get(teamIndex);
		DBObject player = ((List<DBObject>) team.get("players")).get(playerIndex);
		if (!player.containsField("result") && team.containsField("result")) {
			player.put("result", team.get("result"));
		}
		if (!player.containsField("url") || player.get("url") == null)
			player.put("url", "");
		if (!player.containsField("avg_apm") || player.get("avg_apm") == null)
			player.put("avg_apm", 0);

		return player;
	}

	private SolrInputDocument addPlayer(DBObject replay, SolrInputDocument doc, DBObject player, int index) {
		try {
			String playerPrefix = "p_player" + index + "_";
			doc.addField(playerPrefix + "id", getPlayerId(player.get("url").toString()));
			doc.addField(playerPrefix + "name", player.get("name").toString());
			doc.addField(playerPrefix + "race", player.get("play_race").toString());
			doc.addField(playerPrefix + "apm", Math.round(Double.parseDouble(player.get("avg_apm").toString())));
			doc.addField(playerPrefix + "is_random", player.get("pick_race") == "Random");
			doc.addField(playerPrefix + "pid", player.get("pid"));
			String result = (String)player.get("result");
			if (result == null)
				result = "Unknown";
			doc.addField(playerPrefix + "is_winner",  "Win".compareToIgnoreCase(result) == 0);
			doc.addField(playerPrefix + "color", getColorCode((DBObject) player.get("color")));

			int playerId = getPlayerId(player.get("url").toString());

			doc.addField("f_player_id", playerId);
			doc.addField("f_player", player.get("name").toString());

			String winnerNameLong, winnerNameShort;

			// getting taskIds
			BasicDBList taskIds = (BasicDBList) replay.get("taskIds");
			String playerUrl = (String) player.get("url");
			DBObject progamer = MongoHelper.getProgamerForReplay(taskIds, playerUrl);
			// DBObject progamer = MongoHelper.getProgamerByPlayerId(playerId);
			if (progamer != null) {
				int progamerId = (Integer)progamer.get("_id");
				winnerNameLong = progamer.get("name").toString() + '-' + progamerId;
				winnerNameShort = progamer.get("name").toString();
				doc.addField("f_player_person_game_id", winnerNameShort);
				doc.addField("f_player_person_id", progamerId);
				doc.addField(playerPrefix + "progamer_id", progamerId);
				doc.addField(playerPrefix + "progamer_name", winnerNameShort);
			} else {
				winnerNameLong = player.get("name").toString() + '-' + playerId;
				winnerNameShort =  player.get("name").toString();
				doc.addField("f_player_person_game_id", winnerNameShort);
				doc.addField("f_player_person_id", playerId);
				doc.addField(playerPrefix + "progamer_id", 0);
				doc.addField(playerPrefix + "progamer_name", winnerNameShort);
			}

			// winner logic
			if ("Win".compareToIgnoreCase(result) == 0) {
				doc.addField("f_winning_race", player.get("play_race"));
				doc.addField("f_winner", winnerNameShort);
				doc.addField("p_winner_name", winnerNameShort);
			}
		} catch(Exception e) {
			logger.error("Error addPlayer.", e);
			logger.info(doc);
		}

		return doc;
	}

	private int getPlayerId(String playerUrl) {
		return MongoHelper.getPlayerByUrl(playerUrl.toLowerCase());
	}
	private  int getProgamerId(int playerId) {
		return MongoHelper.getProgamerIdByPlayerId(playerId);
	}

	private static int getColorCode(DBObject color) {
		return ((Integer)color.get("r") << 16) + ((Integer)color.get("g") << 8) + (Integer)color.get("b");
	}

	private static String getReplayFile(DBObject replay) {
		return (String)replay.get("replayFile");
	}

	private static String getGameLengthCategory(int gameLength) {
		return "Average";
	}

	private static String getMatchUpType(DBObject player1, DBObject player2) {
		String matchUp = player1.get("pick_race").toString().charAt(0) + "v" + player2.get("pick_race").toString().charAt(0);
		if ("ZvT".compareToIgnoreCase(matchUp) == 0)
			return "TvZ";

		if ("ZvP".compareToIgnoreCase(matchUp) == 0)
			return "PvZ";

		if ("PvT".compareToIgnoreCase(matchUp) == 0)
			return "TvP";

		return matchUp;
	}

	private SolrInputDocument fillMapInfo(SolrInputDocument doc, DBObject map) {
		try{
			doc.addField("pf_map_name", map.get("name").toString());

			// TODO: p_map_id, p_map_image''
			// int mapId = (Integer) map.get("id");
			int mapId = (int) Double.parseDouble(map.get("id").toString());
			doc.addField("p_map_id", mapId);

			DBObject mongoMap = MongoHelper.getMap(mapId);
			if (mongoMap != null) {
				// overwrite map name from replay parser.
				doc.setField("pf_map_name", mongoMap.get("name").toString());

				DBObject image = (DBObject)mongoMap.get("image");
				if (image != null)
					doc.addField("p_map_image", new Gson().toJson(image));
			}
		} catch(Exception e) {
			logger.error("Error while getting mapInfo", e);
			logger.info(map.toString());
		}
		return doc;
	}
}
