package com.sc2geeks.app;

import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by robert on 8/27/14.
 */
public class SolrUpdater
{
	private static Config config = Config.getInstance();
	private static Logger logger = Logger.getLogger(SolrUpdater.class);

	public static void replayFullUpdate() {
		logger.info("Doing full update for replay.");
		SolrDocIterator iterator = new SolrDocIterator(new BasicDBObjIterator(MongoHelper.getAllReplays()), new ReplayMongoConverter());
		doFullUpdate(config.getSolrReplayUrl(), iterator);
		logger.info("Full update is done for replay.");
	}

	public static void replayPartialUpdate(Date lastUpdateDate) {
		logger.info("Doing partial update for replay.");

		MongoObjConverter converter = new ReplayMongoConverter();

		ArrayList<Iterator<DBObject>> iterators = new ArrayList<>();

		// recent replays
		iterators.add(new BasicDBObjIterator(MongoHelper.getRecentlyUpdatedReplays(lastUpdateDate)));

		// event => taskIds => replays
		iterators.add(new PartialUpdateDBObjectProvider.RecentEventProvider(lastUpdateDate));

		// map => replays
		iterators.add(new PartialUpdateDBObjectProvider.RecentMapProvider(lastUpdateDate));

		// progamer => player => replays
		iterators.add(new PartialUpdateDBObjectProvider.RecentProgamerProvider(lastUpdateDate));

		Iterator<SolrInputDocument> docs = new SolrDocIterator(new PartialUpdateDBObjectProvider(iterators), converter);

		doPartialUpdate(config.getSolrReplayUrl(), docs, converter);
		logger.info("Partial update is done for replay.");
	}


	public static void progamerFullUpdate() {
		logger.info("Doing full update for progamer.");
		SolrDocIterator iterator = new SolrDocIterator(new BasicDBObjIterator(MongoHelper.getAllProgamers()), new ProgamerMongoConverter());
		doFullUpdate(config.getSolrPlayerUrl(), iterator);
		logger.info("Full update is done for progamer.");
	}

	private static void doFullUpdate(String solrUrl, Iterator<SolrInputDocument> solrInputDocumentIterator) {
		try {
			HttpSolrServer solrServer = new HttpSolrServer(solrUrl);
			solrServer.deleteByQuery("*:*");
			solrServer.add(solrInputDocumentIterator);
			solrServer.commit();
		} catch(Exception e) {
			logger.error("Error occurred during full update.", e);
		}
	}

	private static void doPartialUpdate(String solrUrl, Iterator<SolrInputDocument> docs, MongoObjConverter converter) {
		try {
			HttpSolrServer solrServer = new HttpSolrServer(solrUrl);

			int total = 0;
			while (docs.hasNext()) {
				total ++;
				SolrInputDocument doc = docs.next();
				solrServer.deleteById(getDocumentId(doc, converter));
				solrServer.add(doc);
			}

			logger.info("Total [" + total + "] docs updated.");

			solrServer.commit();
		} catch(Exception e) {
			logger.error("Error occurred during partial update.", e);
		}
	}

	private static String getDocumentId(SolrInputDocument solrInputDocument, MongoObjConverter converter) {
		return solrInputDocument.getFieldValue(converter.getUniqueId()).toString();
	}
}
