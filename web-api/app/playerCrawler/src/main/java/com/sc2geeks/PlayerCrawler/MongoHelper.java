package com.sc2geeks.PlayerCrawler;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by robert on 9/1/14.
 */
public class MongoHelper {
	private static Logger logger = Logger.getLogger(MongoHelper.class);

	private static String mongoHost;
	private static int mongoPort;
	private static String mongoDBName;
	private static String collectionName;
	private static MongoClient client;

	static {
		CrawlerConfig config = CrawlerConfig.getInstance();
		mongoHost = config.getMongoHost();
		mongoPort = config.getMongoPort();
		mongoDBName = config.getMongoDBName();
		collectionName = config.getCrawledProgamerCollectionName();
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

	public static void saveProgamers(List<DBObject> progamers) {
		try {
			logger.info("Saving progamers. Total count: " + progamers.size());
			ensureConnection();
			DBCollection coll = client.getDB(mongoDBName).getCollection(collectionName);
			coll.drop();
			coll.insert(progamers);
			logger.info("Done!");
		} catch (Exception e) {
			logger.error("Error occurred", e);
		}
	}

}
