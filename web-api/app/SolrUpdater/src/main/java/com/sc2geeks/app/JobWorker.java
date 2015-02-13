package com.sc2geeks.app;

import com.mongodb.*;
import com.sc2geeks.commons.util.FileUtil;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by robert on 8/28/14.
 */
public class JobWorker {
	private static Config config;
	private static Logger logger = Logger.getLogger(JobWorker.class);
	private static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	static {
		config = Config.getInstance();
	}

	public static void main(String[] args) {
		// PropertyConfigurator.configure("log4j.properties");

		doUpdate(args);
		// SolrUpdater.progamerFullUpdate();
		//  testMongo();
	}

	private static void doUpdate(String[] args) {
		boolean fullUpdate = false;
		Date currentTime = new Date();
		Date lastUpdateTime = getLastUpdateDate();
		if (args.length > 0 && args[0].compareToIgnoreCase("fullupdate") == 0) {
			fullUpdate = true;
		}

		try {
			// call python updater
			doPythonUpdateJob(fullUpdate);

			// then update solr
			if (fullUpdate) {
				SolrUpdater.replayFullUpdate();
				SolrUpdater.progamerFullUpdate();
			} else {
				SolrUpdater.replayPartialUpdate(lastUpdateTime);
				SolrUpdater.progamerFullUpdate();   // only full update.
			}
		} catch(Exception e) {}
		saveLastUpdateDate(currentTime);
	}

	private static Date getLastUpdateDate() {
		logger.info("Getting last update from file: " + config.getTimeStampFileName());
		try {
			String dateString = FileUtil.readFile(config.getTimeStampFileName(), StandardCharsets.UTF_8);
			return new SimpleDateFormat(DATE_FORMAT_STRING).parse(dateString);

		} catch (Exception e) {
			logger.error("Fail to get last update time. Resort to full update.", e);
		}
		return new GregorianCalendar(1900, 1, 1).getTime();
	}

	private static void saveLastUpdateDate(Date date) {
		logger.info("Saving last update date: " + date);
		try (PrintWriter pw = new PrintWriter(config.getTimeStampFileName())) {
			pw.write(new SimpleDateFormat(DATE_FORMAT_STRING).format(date));
		} catch (Exception e) {
			logger.error("Error during saving last update time.", e);
		}
		logger.info("Done.");
	}

	private static void doPythonUpdateJob(boolean fullUpdate) {
		logger.info("Calling python updater.");
		try{
			String[] cmd = fullUpdate ? new String[]{config.getPythonBin(), config.getPythonUpdateScript(), "fullUpdate"}
					: new String[] {config.getPythonBin(), config.getPythonUpdateScript()};
			Process process = Runtime.getRuntime().exec(cmd);
			int retval = process.waitFor();
			logger.info("Done. Result: " + retval);
		} catch(Exception e) {
			logger.error("Failed during python update script.", e);
		}
	}

	public static void testMongo(){
		try {
			MongoClient client = new MongoClient();
			DB db = client.getDB("sc2");

			Date lastUpdateTime = getLastUpdateDate();

			DBCursor event_cursor = db.getCollection("event").find(new BasicDBObject("last_edit_date", new BasicDBObject("$gt", lastUpdateTime)),
					new BasicDBObject("_id", 1));
			ArrayList<Integer> event_id_list = new ArrayList<Integer>();
			while (event_cursor.hasNext()) {
				DBObject event = event_cursor.next();
				event_id_list.add((Integer)event.get("_id"));
			}

			DBObject replay = db.getCollection("replay").findOne(new BasicDBObject("_id", 1924));
			BasicDBList taskIds = (BasicDBList)replay.get("taskIds");
			DBCursor cursor = db.getCollection("upload_task").find(new BasicDBObject("_id", new BasicDBObject("$in", replay.get("taskIds"))));
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				System.out.print(obj);
			}
			System.out.print(taskIds);
		} catch (Exception e) {
			return ;
		}
	}

	public static void testSolr()
	{
		SolrServer solrServer = new HttpSolrServer("http://localhost:6806/solr/replay");
	}

}
