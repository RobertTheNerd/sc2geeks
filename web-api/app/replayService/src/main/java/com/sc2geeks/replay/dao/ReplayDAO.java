package com.sc2geeks.replay.dao;

import com.sc2geeks.replay.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayDAO
{
	public static final String UNKNOWN_MAP_IMAGE = "unknown_map.jpg";


	private static SessionFactory sessionFactory = new Configuration().configure("sc2geeks.replayDAO.cfg.xml").buildSessionFactory();
	private static Logger logger = Logger.getLogger(ReplayDAO.class);

	public static GameEntity getGame(int gameId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where gameId = :gameId");
			query.setParameter("gameId", gameId);
			GameEntity result = (GameEntity) query.uniqueResult();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to get game for gameid: " + gameId, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getActiveGames()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status = 'M'");
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get games.");
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static GameEntity getGameByGameUrl(String gameUrl)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where gameUrl = :gameUrl and status <> 'V'");
			query.setParameter("gameUrl", gameUrl);
			GameEntity result = (GameEntity) query.uniqueResult();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to get game for url: " + gameUrl, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getFailedCrawlGames(int maxRetries, String sourceName)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status='CF' and externalSource = :externalSource " +
					"and crawlTimes < :crawlTimes");
			query.setParameter("crawlTimes", maxRetries);
			query.setParameter("externalSource", sourceName);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get failed crawl games.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getUndownloadedGames(int batchSize)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status='C'");
			query.setMaxResults(batchSize);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get undownloaded games.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getFailedDownloadGames(int maxRetries)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status='DF' " +
					"and downloadTimes < :downloadTimes");
			query.setParameter("downloadTimes", maxRetries);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get failed download games.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getUnparsedGames(int batchSize)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status = 'D'").setMaxResults(batchSize);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get unparsed games", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<GameEntity> getFailedParseGames(int maxRetries)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from GameEntity where status='PF' " +
					"and parseTimes < :parseTimes");
			query.setParameter("parseTimes", maxRetries);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to get failed download games.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static boolean mergeDuplicateGames()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createSQLQuery("call JOB_Merge_Duplicate_Replays() ");
			session.beginTransaction();
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		}
		catch(Exception e)
		{
			logger.error("Failed to execute mergeDuplicateGames at sp JOB_Merge_Duplicate_Replays.", e);
			return false;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static boolean processSeries()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createSQLQuery("call Job_ProcessGameSeries(1000) ");
			session.beginTransaction();
			query.executeUpdate();
			session.getTransaction().commit();

			return true;
		}
		catch(Exception e)
		{
			logger.error("Failed to execute processSeries at sp Job_ProcessGameSeries.", e);
			return false;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static void deleteGameteamByGame(int gameId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("delete from GameteamEntity where gameId = :gameId");
			query.setParameter("gameId", gameId);
			session.beginTransaction();
			query.executeUpdate();
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			logger.error("Failed to delete game for gameid: " + gameId, e);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static PlayerEntity getPlayer(String BnetId, String gateway, String name, String profileUrl)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from PlayerEntity where bnetId = :bnetId " +
					"and gateway = :gateway " +
					"and playerName = :name " +
					"and profileUrl = :profileUrl " +
					"and status = true" );
			query.setParameter("bnetId", BnetId);
			query.setParameter("gateway", gateway);
			query.setParameter("name", name);
			query.setParameter("profileUrl", profileUrl);
			query.setMaxResults(1);
			//query.se
			PlayerEntity result = (PlayerEntity) query.list().get(0);
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to get player.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	public static List<PlayerEntity> getPlayersByName(String name)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from PlayerEntity where status = true and playerName = :playerName");
			query.setParameter("playerName", name);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to getPlayerByName ", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	/********* progamer functions begin **********/
	public static List<ProgamerEntity> getProgamers()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from ProgamerEntity ");
			List<ProgamerEntity> result = query.list();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to getProgamers.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static ProgamerEntity getProgamerById(int progamerId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from ProgamerEntity where progamerId=:progamerId");
			query.setParameter("progamerId", progamerId);
			ProgamerEntity result = (ProgamerEntity) query.uniqueResult();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to getProgamerById.", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static List<ProgamerEntity> getProgamersWithImageLogs()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			List<ProgamerEntity> result = session.createCriteria(ProgamerEntity.class).list();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to getProgamersWithImageLogs", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	public static List<ProgamerImageLogEntity> getProgamerImageLogs(int progamerId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from ProgamerImageLogEntity where progamerId=:progamerId order by logId desc");
			query.setParameter("progamerId", progamerId);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to getProgamerImageLogs", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}

	}
	/********* progamer functions end **********/

	/********* general entity manipulation start **********/
	public static boolean saveOrUpdateObject(Object obj)
	{
		if (obj == null)
			return true;

		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
			return true;
		}
		catch (Exception e)
		{
			logger.error("Failed to save/update Object: " + obj.toString(), e);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return false;
	}
	public static boolean saveOrUpdateObjectList(List list)
	{
		return saveOrUpdateObjectList(list, true);
	}
	public static boolean saveOrUpdateObjectList(List list, boolean ownTransaction)
	{
		if (list == null || list.size() == 0)
			return true;

		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			if (!ownTransaction)
				session.beginTransaction();

			for (Object object : list)
			{
				try
				{
					if (ownTransaction)
						session.beginTransaction();
					session.saveOrUpdate(object);
					if (ownTransaction)
						session.getTransaction().commit();
				} catch (Exception e)
				{
					try
					{
						if (ownTransaction)
						{
						Transaction tran = session.getTransaction();
						if (tran != null && !tran.wasCommitted())
							tran.rollback();
						}
					}catch(Exception ex){}

					logger.error("Error occurred while persisting entity:" + object + ".");
				}
			}

			if (!ownTransaction)
				session.getTransaction().commit();
			return true;
		}
		catch (Exception e)
		{
			try
			{
				if (!ownTransaction)
				{
					Transaction tran = session.getTransaction();
					if (tran != null && !tran.wasCommitted())
						tran.rollback();
				}
			}catch (Exception ex){}
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return false;
	}
	/********* general entity manipulation end **********/

	/**** map functions begin ****/

	public static MapEntity getMap(String bnetUrl, String name)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from MapEntity where bnetUrl = :bnetUrl and name = :name");
			query.setParameter("bnetUrl", bnetUrl);
			query.setParameter("name", name);
			MapEntity result = (MapEntity) query.uniqueResult();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to get map for bneturl: " + bnetUrl + ", name: " + name, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static MapEntity getMapWithImageName(String bnetUrl)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from MapEntity where bnetUrl = :bnetUrl and imageFile is not null");
			query.setParameter("bnetUrl", bnetUrl);
			query.setMaxResults(1);
			MapEntity result = (MapEntity) query.uniqueResult();
			return result;
		}
		catch(Exception e)
		{
			logger.error("Failed to get map image for bneturl: " + bnetUrl, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	public static void updateMap(String bnetUrl, String imageFile, int width, int height)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("update MapEntity set imageFile = :imageFile, " +
					"width=:width, height=:height, lastEditDate=:lastEditDate " +
					"where bnetUrl = :bnetUrl");
			query.setParameter("bnetUrl", bnetUrl);
			query.setParameter("width", width);
			query.setParameter("height", height);
			query.setParameter("imageFile", imageFile);
			query.setParameter("lastEditDate", new Timestamp(new Date().getTime()));
			session.beginTransaction();
			query.executeUpdate();
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			logger.error("Failed to update map image " + imageFile + " for bneturl: " + bnetUrl +
					" width: " + width + " height: " + height, e);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}

	}
	public static void updateMap(String bnetUrl, String name, String imageFile, int width, int height)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("update MapEntity set imageFile = :imageFile, " +
					"name=:name, width=:width, height=:height, lastEditDate=:lastEditDate " +
					"where bnetUrl = :bnetUrl");
			query.setParameter("bnetUrl", bnetUrl);
			query.setParameter("name", name);
			query.setParameter("width", width);
			query.setParameter("height", height);
			query.setParameter("imageFile", imageFile);
			query.setParameter("lastEditDate", new Timestamp(new Date().getTime()));
			session.beginTransaction();
			query.executeUpdate();
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			logger.error("Failed to update map image " + imageFile + " for bneturl: " + bnetUrl +
					" width: " + width + " height: " + height, e);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}

	}
	public static String getMaxMapFileName()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("select max(imageFile) from MapEntity where imageFile <> :unknown");
			query.setParameter("unknown", UNKNOWN_MAP_IMAGE);
			return (String)query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error("Failed to getMaxMapFileName", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}


	public static List<String> getMapUrlsWithoutImage()
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("select distinct bnetUrl from MapEntity where imageFile is null or imageFile = ''");
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to getMapUrlsWithoutImage", e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	public static MapEntity getMapById(int mapId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from MapEntity where mapId = :mapId");
			query.setInteger("mapId", mapId);
			return (MapEntity) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error("Failed to getMapById: " + mapId, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	/**** map functions end ****/


	/**
	 * * Action related functions begin ****
	 */
	public static List<ActionEntity> getActions(int gameId, int actionId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from ActionEntity where gameId = :gameId and actionUnitId = :actionUnitId " +
					"order by frame asc");
			query.setInteger("gameId", gameId);
			query.setInteger("actionUnitId", actionId);
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("Failed to getActions gameid: " + gameId + ", action unit id: " + actionId, e);
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	public static void calculateActionSummary(int gameId)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.getTransaction().setTimeout(3600);

			// remove existing summary
			Query query = session.createSQLQuery("delete from rep_game_actionsummary where gameid=" + gameId + ";");
			query.executeUpdate();

			query = session.createSQLQuery("delete from rep_gameplayer_actionsummary where gameid=" + gameId + ";");
			query.executeUpdate();

			// calculate special actions
			LinkedHashMap<Integer, HashMap<Integer, Integer>> actionPlayerCount = new LinkedHashMap<Integer, HashMap<Integer, Integer>>();
			for (SpecialActionUnit action : ActionConfig.getSpecialActionConfigList())
			{
				actionPlayerCount.put(action.getId(), getSpecialUnitCount(gameId, action));
			}
			StringBuilder specialValueSb = new StringBuilder();
			for (Integer actionUnitId : actionPlayerCount.keySet())
			{
				specialValueSb.append(getPlayerValue(actionPlayerCount.get(actionUnitId))).append(",");
			}
			String specialValues = StringUtils.removeEnd(specialValueSb.toString(), ",");

			// insert game player summary
			String sql = "insert into rep_gameplayer_actionsummary(GameId, PlayerId, " + insertColumnList + ") \n" +
					"select " + gameId + ", PlayerId," + valueColumnList + "," + specialValues + "\n" +
					"from rep_action where GameId=" + gameId + "\n" +
					"group by PlayerId;";
			query = session.createSQLQuery(sql);
			query.executeUpdate();

			// insert game summary
			sql = "insert into rep_game_actionsummary(GameId, " + insertColumnList + ") \n" +
					"select " + gameId + "," + sumColumnList + "\n" +
					"from rep_gameplayer_actionsummary where GameId=" + gameId + ";";
			query = session.createSQLQuery(sql);
			query.executeUpdate();
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			logger.error("Failed to calculate Action summary for game: " + gameId, e);
		}
		finally
		{
			if (session != null)
			{
				try {
					if (session.getTransaction() != null && session.getTransaction().isActive())
						session.getTransaction().commit();
					session.close();
				}catch(Exception ex)
				{
					logger.error("Failed to finalize session.", ex);
				}
			}
		}
	}

	private static HashMap<Integer, Integer> getSpecialUnitCount(int gameId, SpecialActionUnit specialAction)
	{
		List<ActionEntity> specialActions = getActions(gameId, specialAction.getId());
		if (specialActions == null || specialActions.size() == 0)
			return null;

		HashMap<Integer, List<ActionEntity>> playerActions = new HashMap<Integer, List<ActionEntity>>();
		for (ActionEntity action: specialActions)
		{
			List<ActionEntity> list;
			if (!playerActions.containsKey(action.getPlayerId()))
			{
				list = new ArrayList<ActionEntity>();
				playerActions.put(action.getPlayerId(), list);
			}
			else
				list = playerActions.get(action.getPlayerId());
			list.add(action);
		}

		HashMap<Integer, Integer> playerCount = new HashMap<Integer, Integer>();

		for (int playerId : playerActions.keySet())
		{
			List<ActionEntity> actions = playerActions.get(playerId);
			int total = 0;
			for (int pos = 0, head = 0; pos < actions.size(); pos++)
			{
				total++;
				ActionEntity startAction = actions.get(pos);
				head = pos;
				while (++head < actions.size())
				{
					ActionEntity currentAction = actions.get(head);
					if (currentAction.getFrame() > startAction.getFrame() + specialAction.getBuildTime())
						break;
				}
				pos = head;
			}
			playerCount.put(playerId, total);
		}

		return playerCount;
	}

	private static String getPlayerValue(HashMap<Integer, Integer> playerCount)
	{
		if (playerCount == null || playerCount.size() == 0)
			return "0";

		StringBuilder sb = new StringBuilder("CASE PlayerId");
		for (int playerId : playerCount.keySet())
		{
			sb.append(" when ").append(playerId).append(" then ").append(playerCount.get(playerId));
		}
		sb.append(" else 0 end");
		return sb.toString();
	}

	private static String insertColumnList;
	private static String valueColumnList;
	private static String sumColumnList;
	private static void constructColumnList()
	{
		Set<Integer> specialIds = new HashSet<Integer>(ActionConfig.getSpecialActionConfigList().size());
		for (SpecialActionUnit unit : ActionConfig.getSpecialActionConfigList())
		{
			specialIds.add(unit.getId());
		}

		StringBuilder insertsb = new StringBuilder();
		StringBuilder updatesb = new StringBuilder();
		StringBuilder sumsb = new StringBuilder();

		// non-special ids
		List<ActionConfigUnit> list = ActionConfig.getActionList();
		for (ActionConfigUnit action : list)
		{
			if (specialIds.contains(action.getId()))
				continue;
			insertsb.append("a").append(action.getId()).append(",");

			updatesb.append("\tsum(1-abs(sign(ActionUnitId - ").append(action.getId());
			updatesb.append("))) as a").append(action.getId()).append(",\n");

			sumsb.append("sum(a").append(action.getId()).append("),");
		}

		// special ids
		for (SpecialActionUnit action : ActionConfig.getSpecialActionConfigList())
		{
			insertsb.append("a").append(action.getId()).append(",");
			sumsb.append("sum(a").append(action.getId()).append("),");
		}


		insertColumnList = StringUtils.removeEnd(insertsb.toString(), ",");
		valueColumnList = StringUtils.removeEnd(updatesb.toString(), ",\n");
		sumColumnList = StringUtils.removeEnd(sumsb.toString(), ",");
	}

	static {
		constructColumnList();
	}
	/**** Action related functions end *****/
}
