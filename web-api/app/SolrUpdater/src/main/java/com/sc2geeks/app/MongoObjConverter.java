package com.sc2geeks.app;

import com.mongodb.DBObject;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by robert on 8/28/14.
 */
public interface MongoObjConverter {
	SolrInputDocument convertToSolrDoc(DBObject obj);
	String getUniqueId();
}
