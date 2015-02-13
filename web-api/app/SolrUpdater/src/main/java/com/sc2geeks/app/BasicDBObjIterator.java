package com.sc2geeks.app;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * Created by robert on 9/2/14.
 */
public class BasicDBObjIterator implements Iterator<DBObject> {
	private static Logger logger = Logger.getLogger(BasicDBObjIterator.class);

	private DBCursor cursor;

	public BasicDBObjIterator(DBCursor cursor) {
		this.cursor = cursor;
	}

	@Override
	public boolean hasNext() {
		if (cursor == null) return false;
		boolean res = false;
		try {
			res = cursor.hasNext();
			if (!res) {
				logger.info("Last row fetched, closing cursor.");
				cursor.close();
				cursor = null;
			}
		} catch(Exception e) {
			logger.error("Failed to close cursor.", e);
		} // just in case
		return res;
	}

	@Override
	public DBObject next() {
		if (cursor == null) return null;
		return cursor.next();
	}

	@Override
	public void remove() {
		return;
	}
}
