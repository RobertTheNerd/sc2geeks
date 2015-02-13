package com.sc2geeks.app;

import com.google.gson.Gson;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by robert on 9/1/14.
 */
public class MongoHelper {
	private static Logger logger = Logger.getLogger(MongoHelper.class);

	private static String mongoHost;
	private static int mongoPort;
	private static String mongoDBName;
	private static MongoClient client;

	static {
		Config config = Config.getInstance();
		mongoHost = config.getMongoHost();
		mongoPort = config.getMongoPort();
		mongoDBName = config.getMongoDBName();
	}

	private static void ensureConnection() throws Exception{
		if (client == null) {
			try {
				client = new MongoClient(mongoHost, mongoPort);
			} catch (Exception e) {
				logger.error("Failed to connect to mongo server: " + mongoHost + ":" + mongoPort, e);
			}
		}
	}

	public static String getEventByTaskIds(BasicDBList taskIds) {
		if (taskIds == null || taskIds.size() == 0)
			return "";

		String eventName = "";
		try {
			ensureConnection();
			DBCursor cursor = client.getDB(mongoDBName).getCollection("upload_task").find(new BasicDBObject("_id",
					new BasicDBObject("$in", taskIds)));

			while (cursor.hasNext()) {
				DBObject task = cursor.next();
				Object event_name = task.get("event_name");
				if (event_name != null && StringUtils.isNotBlank(event_name.toString())) {
					eventName = event_name.toString();
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Error occurred while getting event by taskIds:" + taskIds.toString(), e);
		}
		return eventName;
	}

	public static int getPlayerByUrl(String url) {
		if (StringUtils.isBlank(url))
			return -1;

		try {
			ensureConnection();
			DBObject obj = client.getDB(mongoDBName).getCollection("player").findOne(new BasicDBObject("url", url));
			Object id = obj.get("_id");
			if (id == null)
				return -1;

			return (int)id;

		} catch (Exception e) {
			logger.error("Error occurred while getting player by url: " + url, e);
		}
		return -1;
	}

	public static int getProgamerIdByPlayerId(int playerId) {
		if (playerId == -1)
			return -1;

		int progamerId = -1;
		try {
			ensureConnection();
			DBObject obj = client.getDB(mongoDBName).getCollection("player").findOne(new BasicDBObject("_id", playerId));
			if (obj != null) {
				Object progamer = obj.get("progamer");
				if (progamer == null)
					return progamerId;

				return (int)progamer;
			}

		} catch (Exception e) {
			logger.error("Error occurred while getting Progamer by playerId: " + playerId, e);
		}
		return progamerId;
	}

	public static DBObject getProgamerByPlayerId(int playerId) {
		if (playerId == -1)
			return null;

		int progamerId = getProgamerIdByPlayerId(playerId);

		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("progamer").findOne(new BasicDBObject("_id", progamerId));
		} catch (Exception e) {
			logger.error("Error occurred while getting Progamer by playerId: " + playerId, e);
		}
		return null;
	}

	public static DBCursor getRecentUpdatedEventIds(Date lastUpdateDate) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("event").find(new BasicDBObject("last_edit_date",
					new BasicDBObject("$gt", lastUpdateDate)), new BasicDBObject("_id", 1));
		} catch (Exception e) {
			logger.error("Error occurred while getting recent updated event ids. Last date:" + lastUpdateDate, e);
		}
		return null;
	}


	public static DBCursor getTaskIdsByEventId(int eventId) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("upload_task").find(new BasicDBObject("event_id", eventId),
					new BasicDBObject("_id", 1));
		} catch (Exception e) {
			logger.error("Error occurred while getting taskids by eventId:" + eventId, e);
		}
		return null;
	}
	public static DBCursor getReplaysByTaskId(int taskId) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("replay").find(new BasicDBObject("taskIds", taskId));
		} catch (Exception e) {
			logger.error("Error occurred while getting replay by taskId:" + taskId, e);
		}
		return null;
	}
	public static DBCursor getRecentlyUpdatedReplays(Date lastUpdateDate) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("replay").find(new BasicDBObject("last_edit_date",
					new BasicDBObject("$gt", lastUpdateDate)));
		} catch (Exception e) {
			logger.error("Error occurred while getting recently updated replay:" + lastUpdateDate, e);
		}
		return null;
	}

	public static DBCursor getRecentlyUpdatedMapIds(Date lastUpdateDate) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("map").find(new BasicDBObject("last_edit_date",
					new BasicDBObject("$gt", lastUpdateDate)), new BasicDBObject("_id", 1));
		} catch (Exception e) {
			logger.error("Error occurred while getting recent updated map ids. Last date:" + lastUpdateDate, e);
		}
		return null;
	}
	public static DBCursor getReplaysByMapId(int mapId) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("replay").find(new BasicDBObject("map.id", mapId));
		} catch (Exception e) {
			logger.error("Error occurred while getting replay by mapId:" + mapId, e);
		}
		return null;
	}

	public static String getMapImage(int mapId) {
		try {
			ensureConnection();
			DBObject map = client.getDB(mongoDBName).getCollection("map").findOne(new BasicDBObject("_id", mapId));
			if (map == null)
				return null;
			DBObject image = (DBObject)map.get("image");
			if (image != null)
				return new Gson().toJson(image);
		} catch (Exception e) {
			logger.error("Error occurred while getting image by mapId:" + mapId, e);
		}
		return null;
	}

	public static DBObject getMap(int mapId) {
		return getObjectById("map", mapId);
	}

	public static DBCursor getReplaysByPlayerId(int playerId) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("replay").find(new BasicDBObject("teams.players.id", playerId));
		} catch (Exception e) {
			logger.error("Error occurred while getting replay by playerId:" + playerId, e);
		}
		return null;
	}
	public static DBCursor getRecentlyUpdatedProgamerIds(Date lastUpdateDate) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("progamer").find(new BasicDBObject("last_edit_date",
					new BasicDBObject("$gt", lastUpdateDate)), new BasicDBObject("_id", 1));
		} catch (Exception e) {
			logger.error("Error occurred while getting recent updated progamer ids. Last date:" + lastUpdateDate, e);
		}
		return null;
	}

	public static DBCursor getPlayersByProgamerId(int progamerId) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection("player").find(new BasicDBObject("progamer", progamerId));
		} catch (Exception e) {
			logger.error("Error occurred while getting player by progamerId:" + progamerId, e);
		}
		return null;
	}

	public static DBObject getProgamerForReplay(BasicDBList taskIds, String playerUrl) {
		try {
			ensureConnection();

			// String progamerId = null;
			ArrayList<Integer> eventList = null;

			// get the eventIds
			if (taskIds != null && taskIds.size() > 0) {
				DBCursor event_cursor = client.getDB(mongoDBName).getCollection("upload_task")
						.find(new BasicDBObject("_id", new BasicDBObject("$in", taskIds)), new BasicDBObject("event_id", "1"));

				if (event_cursor != null) {
					eventList = new ArrayList<>();
					while (event_cursor.hasNext()) {
						DBObject event = event_cursor.next();
						if (event.containsField("event_id") && event.get("event_id") != null)
							eventList.add((Integer)event.get("event_id"));
					}
					event_cursor.close();
				}
				// get progamers for an event
				BasicDBObject query = new BasicDBObject("player_url", playerUrl)
						.append("event_id", new BasicDBObject("$in", eventList))
						.append("progamer_id", new BasicDBObject("$ne", null));
				DBObject progamer = client.getDB(mongoDBName).getCollection("event_players")
						.findOne(query);
				if (progamer != null && progamer.containsField("progamer_id") && progamer.get("progamer_id") != null) {
					Integer progamerId = (Integer) progamer.get("progamer_id");
					return getObjectById("progamer", progamerId);
				}
			}

			// else get progamer by player url
			BasicDBObject query = new BasicDBObject("url", playerUrl);
			DBObject player = getSingleObjectByQuery("player", query);
			if (player == null || !player.containsField("progamer") || player.get("progamer") == null)
				return null;

			return getObjectById("progamer", (Integer) player.get("progamer"));
		} catch (Exception e) {
			logger.error("Error occurred while getProgamerForReplay taskIds:" + taskIds + " player: " + playerUrl, e);
		}
		return null;
	}

	private static DBCursor getAllObjects(String collectionName) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection(collectionName).find();
		} catch (Exception e) {
			logger.error("Error occurred while getting all objects from collection:" + collectionName, e);
		}
		return null;
	}

	private static DBObject getObjectById(String collectionName, int id) {
		BasicDBObject query = new BasicDBObject("_id", id);
		return getSingleObjectByQuery(collectionName, query);
	}

	private static DBObject getSingleObjectByQuery(String collectionName, BasicDBObject query) {
		try {
			ensureConnection();
			return client.getDB(mongoDBName).getCollection(collectionName)
					.findOne(query);
		} catch (Exception e) {
			logger.error("Error occurred while getting an object from :" + collectionName + " with query: " + query.toString(), e);
		}
		return null;
	}

	public static DBCursor getAllReplays() {
		return getAllObjects("replay");
	}
	public static DBCursor getAllProgamers() {
		return getAllObjects("progamer");
	}


	public static int getReplayCountForProgamer(int progamerId) {
		try {
			ensureConnection();

			// get player ids
			DBCursor cursor = client.getDB(mongoDBName).getCollection("player").find(new BasicDBObject("progamer", progamerId), new BasicDBObject("_id", 1));
			ArrayList<Integer> playerIds = new ArrayList<>();
			while (cursor.hasNext())
				playerIds.add((Integer) cursor.next().get("_id"));
			cursor.close();

			// get count
			cursor = client.getDB(mongoDBName).getCollection("replay").find(new BasicDBObject("teams.players.id", new BasicDBObject("$in", playerIds)));
			int count = cursor.count();
			cursor.close();
			return count;

		} catch (Exception e) {
			logger.error("Error occurred while getting progamer replay count:" + progamerId, e);
		}
		return 0;
	}
}
