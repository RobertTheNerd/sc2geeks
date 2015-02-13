package api.sc2geeks.service.imp.dal;

import api.sc2geeks.entity.BuildAbility;
import api.sc2geeks.entity.Replay;
import api.sc2geeks.service.imp.MongoConfig;
import com.mongodb.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by robert on 9/5/14.
 */
public class MongoHelper {
	private static Logger logger = Logger.getLogger(MongoHelper.class);
	private static Hashtable<String, MongoHelper> instances = new Hashtable<>();


	private String mongoHost;
	private int mongoPort;
	private String mongoDBName;
	private MongoClient client;

	private MongoHelper(String host, int port, String dbName) {
		mongoHost = host;
		mongoPort = port;
		mongoDBName = dbName;
	}

	public synchronized static MongoHelper createHelper(String host, int port, String dbName) {
		String key = host + port + dbName;
		if (instances.containsKey(key)) {
			return instances.get(key);
		}
		MongoHelper helper = new MongoHelper(host, port, dbName);
		instances.put(key, helper);
		return helper;
	}
	public static MongoHelper createHelper(MongoConfig config) {
		return createHelper(config.getMongoHost(), config.getMongoPort(), config.getMongoDBName());
	}

	private synchronized void ensureConnection() throws Exception {
		if (client == null) {
			try {
				MongoClientOptions options = MongoClientOptions.builder()
						.connectionsPerHost(100)
						.autoConnectRetry(true)
						.build();
				// MongoClientOptions options = new MongoClientOptions();
				client = new MongoClient(new ServerAddress(mongoHost, mongoPort), options);
			} catch (Exception e) {
				logger.error("Failed to connect to mongo server: " + mongoHost + ":" + mongoPort, e);
			}
		}
	}

	public void addReplayDownloadCount(int replayId, String fromIP, Date inDate) {
		try {
			ensureConnection();
			client.getDB(mongoDBName).getCollection("actionlog").insert(new BasicDBObject("action", "replay_download_" + replayId)
					.append("ip", fromIP)
					.append("date", inDate));
			client.getDB(mongoDBName).getCollection("summary").update(new BasicDBObject("_id", "replay_download_" + replayId),
					new BasicDBObject("$inc", new BasicDBObject("count", 1)), true, false);

		} catch (Exception e) {
			logger.error("Error occurred while addReplayDownloadCount:" + replayId + fromIP + inDate, e);
		}
	}

	public void addReplayViewCount(int replayId, String fromIP, Date inDate) {
		try {
			ensureConnection();
			client.getDB(mongoDBName).getCollection("actionlog").insert(new BasicDBObject("action", "replay_view_" + replayId)
					.append("ip", fromIP)
					.append("date", inDate));
			client.getDB(mongoDBName).getCollection("summary").update(new BasicDBObject("_id", "replay_view_" + replayId),
					new BasicDBObject("$inc", new BasicDBObject("count", 1)), true, false);

		} catch (Exception e) {
			logger.error("Error occurred while addReplayDownloadCount:" + replayId + fromIP + inDate, e);
		}
	}

	public void addProgamerViewCount(int replayId, String fromIP, Date inDate) {
		try {
			ensureConnection();
			client.getDB(mongoDBName).getCollection("actionlog").insert(new BasicDBObject("action", "progamer_view_" + replayId)
					.append("ip", fromIP)
					.append("date", inDate));
			client.getDB(mongoDBName).getCollection("summary").update(new BasicDBObject("_id", "progamer_view_" + replayId),
					new BasicDBObject("$inc", new BasicDBObject("count", 1)), true, false);

		} catch (Exception e) {
			logger.error("Error occurred while addReplayDownloadCount:" + replayId + fromIP + inDate, e);
		}
	}


	public List<Replay> getReplayDownloadCounts(List<Replay> replayList) {
		if (replayList == null || replayList.size() == 0)
			return replayList;

		try {
			ArrayList<String> ids = new ArrayList(replayList.size());
			for (Replay replay : replayList) {
				ids.add("replay_download_" + replay.getGameId());
			}
			ensureConnection();
			DBCursor cursor = client.getDB(mongoDBName).getCollection("summary").find(new BasicDBObject("_id", new BasicDBObject("$in", ids)));
			if (cursor != null) {
				while (cursor.hasNext()) {
					DBObject summary = cursor.next();
					String id = summary.get("_id").toString().replaceFirst("replay_download_", "");
					int count = (int) summary.get("count");
					Replay replay = findReplay(replayList, id);
					if (replay != null)
						replay.setDownloadCount(count);
				}
				cursor.close();
			}
		} finally {
			return replayList;
		}
	}

	public List<BuildAbility> getReplayAbilityEvents(int replayId) {
		ArrayList<BuildAbility> list = new ArrayList<BuildAbility>();
		try {
			ensureConnection();
			DBCursor cursor = client.getDB(mongoDBName).getCollection("replay_ability_events").find(new BasicDBObject("_id", replayId));
			if (cursor.hasNext()) {
				DBObject replayEvents = cursor.next();
				BasicDBList events = (BasicDBList) replayEvents.get("events");
				if (events != null) {
					for (Object obj : events) {
						DBObject event = (DBObject) obj;
						BuildAbility ability = new BuildAbility();
						ability.setAbility(event.get("ability").toString());
						ability.setFrame(Integer.parseInt(event.get("frame").toString()));
						ability.setPid(Integer.parseInt(event.get("pid").toString()));
						list.add(ability);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occurred while getReplayAbilityEvents:" + replayId, e);
		}
		return list;
	}

	private static Replay findReplay(List<Replay> replays, String replayId) {
		for (Replay replay : replays) {
			if (replayId.compareToIgnoreCase(replay.getGameId()) == 0)
				return replay;
		}
		return null;
	}
}
