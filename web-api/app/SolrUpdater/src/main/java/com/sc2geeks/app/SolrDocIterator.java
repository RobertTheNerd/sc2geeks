package com.sc2geeks.app;

import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;

import java.util.Iterator;

/**
 * Created by robert on 8/27/14.
 */
public class SolrDocIterator implements Iterator<SolrInputDocument> {
	private static Logger logger = Logger.getLogger(SolrDocIterator.class);

	private Iterator<DBObject> dbObjectIterator;
	MongoObjConverter converter;


	public SolrDocIterator(Iterator<DBObject> dbObjectIterator, MongoObjConverter converter) {
		this.dbObjectIterator = dbObjectIterator;
		this.converter = converter;
	}

	@Override
	public boolean hasNext() {
		return dbObjectIterator == null ? false : dbObjectIterator.hasNext();
	}

	@Override
	public SolrInputDocument next() {
		try {
			if (dbObjectIterator == null)
				return null;
			DBObject replay = dbObjectIterator.next();
			if (replay == null)
				return null;

			SolrInputDocument doc = converter.convertToSolrDoc(replay);
			logger.info("Getting solr document.");
			logger.debug(replay);
			// logger.debug(doc);
			return doc;
		} catch(Exception e) {
			logger.error("Error getting solr doc.", e);
			return null;
		}
	}

	@Override
	public void remove() {
		return;
	}

}
