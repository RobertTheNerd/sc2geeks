package com.sc2geeks.app;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by robert on 9/2/14.
 */
public class PartialUpdateDBObjectProvider implements Iterator<DBObject> {

	private static Logger logger = Logger.getLogger(PartialUpdateDBObjectProvider.class);

	private List<Iterator<DBObject>> iterators;

	public PartialUpdateDBObjectProvider(List<Iterator<DBObject>> iterators) {
		this.iterators = iterators;
	}

	int currentIndex = 0;

	@Override
	public boolean hasNext() {
		if (iterators == null || iterators.size() == 0 || currentIndex >= iterators.size())
			return false;

		while (!iterators.get(currentIndex).hasNext()) {
			if ( ++ currentIndex >= iterators.size())
				return false;
		}
		return true;
	}

	@Override
	public DBObject next() {
		return iterators.get(currentIndex).next();
	}

	@Override
	public void remove() {}


	////--------- Base class for nested Iterators with Mongo helper. ------------
	////--------- Things would be a lot easier if Java supports yield. ----------
	public static abstract class MongoProviderBase implements Iterator<DBObject> {
		Config config = Config.getInstance();
	}
	public static abstract class NestedProvider extends MongoProviderBase {
		DBCursor cursor;
		NestedProvider innerProvider;

		@Override
		public boolean hasNext() {
			boolean res;
			if (cursor == null)
				return false;

			if (innerProvider == null) {
				res = cursor == null ? false : cursor.hasNext();
			} else {
				res = true;
				while (!innerProvider.hasNext()) {
					if (!cursor.hasNext()) {
						res = false;
						break;
					}
					DBObject parentObj = cursor.next();
					innerProvider.setParentObject(parentObj);
				}
			}

			if (!res && cursor != null) {
				logger.info("Last row fetched. Closing cursor.");
				try {
					cursor.close();
				} catch (Exception e) {
					logger.error("Failed to close cursor.", e);
				}
				cursor = null;
			}
			return res;
		}

		@Override
		public DBObject next() {
			if (innerProvider == null)
				return cursor == null ? null : cursor.next();

			return innerProvider.next();
		}

		@Override
		public void remove() {
			return;
		}

		void setParentObject(DBObject obj) {
			if (cursor != null || innerProvider != null) {
				if (cursor.hasNext())
					innerProvider.setParentObject(cursor.next());
			}
		};

	}


	////------------ Event => task => replays ---------------
	public static class RecentEventProvider extends NestedProvider {
		Date lastUpdateDate;

		RecentEventProvider(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
			cursor = MongoHelper.getRecentUpdatedEventIds(lastUpdateDate);
			innerProvider = new EventDrivenTaskIdProvider();

			// the outer most iterator needs to start the ball rolling
			if (cursor != null && cursor.hasNext())
				innerProvider.setParentObject(cursor.next());
		}
	}
	public static class EventDrivenTaskIdProvider extends NestedProvider {
		EventDrivenTaskIdProvider() {
			innerProvider = new TaskDrivenReplayProvider();
		}

		/**
		 * getting task ids by a given eventId
		 *
		 * @param obj: : indicating one event id
		 *             _id: int
		 */
		@Override
		void setParentObject(DBObject obj) {
			if (obj == null || obj.get("_id") == null) {
				cursor = null;
			} else {
				int eventId = (Integer) obj.get("_id");
				cursor = MongoHelper.getTaskIdsByEventId(eventId);
			}
			super.setParentObject(obj);
		}
	}
	public static class TaskDrivenReplayProvider extends NestedProvider {

		/**
		 * getting the actual replays by a given taskId
		 *
		 * @param obj: indicating one taskId
		 *             _id: int
		 */
		@Override
		void setParentObject(DBObject obj) {
			if (obj == null || obj.get("_id") == null) {
				cursor = null;
			} else {
				int taskId = (Integer) obj.get("_id");
				cursor = MongoHelper.getReplaysByTaskId(taskId);
			}
		}
	}


	////----------- Map => replays ------------
	public static class RecentMapProvider extends NestedProvider {
		Date lastUpdateDate;

		RecentMapProvider(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
			cursor = MongoHelper.getRecentlyUpdatedMapIds(lastUpdateDate);
			innerProvider = new MapDrivenReplayProvider();

			// the outer most iterator needs to start the ball rolling
			if (cursor != null && cursor.hasNext())
				innerProvider.setParentObject(cursor.next());
		}
	}
	public static class MapDrivenReplayProvider extends NestedProvider {

		/**
		 * getting the actual replays by a given mapId
		 *
		 * @param obj: indicating one map Id
		 *             _id: int
		 */
		@Override
		void setParentObject(DBObject obj) {
			if (obj == null || obj.get("_id") == null) {
				cursor = null;
			} else {
				int mapId = (Integer) obj.get("_id");
				cursor = MongoHelper.getReplaysByMapId(mapId);
			}
		}
	}

	////----------- progamers => player => replays ------------
	//// Note: player => replays does not make much sense. Won't implement.
	public static class RecentProgamerProvider extends NestedProvider {
		Date lastUpdateDate;

		RecentProgamerProvider(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
			cursor = MongoHelper.getRecentlyUpdatedProgamerIds(lastUpdateDate);
			innerProvider = new ProgamerDrivenPlayerProvider();

			// the outer most iterator needs to start the ball rolling
			if (cursor != null && cursor.hasNext())
				innerProvider.setParentObject(cursor.next());
		}
	}
	public static class ProgamerDrivenPlayerProvider extends NestedProvider {
		public ProgamerDrivenPlayerProvider() { innerProvider = new PlayerDrivenReplayProvider(); }

		/**
		 * getting the actual replays by a given progamer Id
		 *
		 * @param obj: indicating one progamer Id
		 *             _id: int
		 */
		@Override
		void setParentObject(DBObject obj) {
			if (obj == null || obj.get("_id") == null) {
				cursor = null;
			} else {
				int mapId = (Integer) obj.get("_id");
				cursor = MongoHelper.getPlayersByProgamerId(mapId);
			}
			super.setParentObject(obj);
		}
	}
	public static class PlayerDrivenReplayProvider extends NestedProvider {

		/**
		 * getting the actual replays by a given player Id
		 *
		 * @param obj: indicating one player Id
		 *             _id: int
		 */
		@Override
		void setParentObject(DBObject obj) {
			if (obj == null || obj.get("_id") == null) {
				cursor = null;
			} else {
				int mapId = (Integer) obj.get("_id");
				cursor = MongoHelper.getReplaysByPlayerId(mapId);
			}
		}
	}





}
