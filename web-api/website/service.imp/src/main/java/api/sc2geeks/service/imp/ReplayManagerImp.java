package api.sc2geeks.service.imp;

import api.sc2geeks.entity.*;
import api.sc2geeks.service.imp.dal.MongoHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.util.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayManagerImp
{
	private static Logger logger = Logger.getLogger(ReplayManagerImp.class);
	private static MongoHelper statsMongoHelper = MongoHelper.createHelper(MongoConfig.getStatsConfig());
	private static MongoHelper replayMongoHelper = MongoHelper.createHelper(MongoConfig.getReplayConfig());
	private static SolrConfig _solrConfig = SolrConfig.getReplayInstance();
	private SolrSearcher solrSearcher;

	public ReplayManagerImp()
	{
		solrSearcher = new SolrSearcher(_solrConfig);
	}

	public SearchResult<Replay> search(SearchInput searchInput)
	{
		SearchResult<Replay> result = solrSearcher.search(searchInput, Replay.class);

		if (result != null)
		{
			statsMongoHelper.getReplayDownloadCounts(result.getEntityList());
		}
		return result;
	}

	public void saveDownloadCounter(String replayId, String fromIP, Date inDate) {
		statsMongoHelper.addReplayDownloadCount(Integer.parseInt(replayId), fromIP, inDate);
	}



	public ReplayWithRelatedInfo getReplayWithRelatedInfo(String replayId)
	{
		ReplayWithRelatedInfo result = new ReplayWithRelatedInfo();

		result.setReplay(getReplay(replayId));
		if (result.getReplay() == null)
			return result;

		// series
		if (StringUtils.isNotBlank(result.getReplay().getSeriesGameId()))
		{
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", "p_series_replay_id:" + result.getReplay().getSeriesGameId());
			param.put("fq ", "p_game_id:(-" + result.getReplay().getGameId() + ")");
			param.put("sort", "p_series_number desc");

			result.setReplaysInSeries(getReplays("*", param));
		}

		// on map
		result.setReplaysOnMap(getReplaysOnMap(result.getReplay()));

		// matchup
		result.setReplaysOfMatchup(getReplaysOfMatchup(result.getReplay()));


		Player player1 = result.getReplay().getPlayerTeams().get(0).getPlayers().get(0);
		Player player2 = result.getReplay().getPlayerTeams().get(1).getPlayers().get(0);
		result.setReplaysFromPlayer1(getReplaysOfPlayer(result.getReplay(), player1.getPlayerId()));
		result.setReplaysFromPlayer2(getReplaysOfPlayer(result.getReplay(), player2.getPlayerId()));

		return result;
	}

	public ReplayWithAllInfo getReplayWithAllInfo(String replayId)
	{
		ReplayWithAllInfo result = new ReplayWithAllInfo();

		result.setReplay(getReplay(replayId));
		if (result.getReplay() == null)
			return result;

		// series
		if (StringUtils.isNotBlank(result.getReplay().getSeriesGameId()))
		{
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", "p_series_replay_id:" + result.getReplay().getSeriesGameId());
			param.put("fq ", "p_game_id:(-" + result.getReplay().getGameId() + ")");
			param.put("sort", "p_series_number desc");

			result.setReplaysInSeries(getReplays("*", param));
		}

		// on map
		result.setReplaysOnMap(getReplaysOnMap(result.getReplay()));

		// matchup
		result.setReplaysOfMatchup(getReplaysOfMatchup(result.getReplay()));


		Player player1 = result.getReplay().getPlayerTeams().get(0).getPlayers().get(0);
		Player player2 = result.getReplay().getPlayerTeams().get(1).getPlayers().get(0);
		result.setReplaysFromPlayer1(getReplaysOfPlayer(result.getReplay(), player1.getPlayerId()));
		result.setReplaysFromPlayer2(getReplaysOfPlayer(result.getReplay(), player2.getPlayerId()));

		result.setAbilityEvents(replayMongoHelper.getReplayAbilityEvents(Integer.parseInt(replayId)));

		return result;
	}

	private static List<Replay> getReplaysOnMap(Replay replay)
	{
		try
		{
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", "p_map_id:" + replay.getMap().getMapId());
			param.put("fq ", "p_game_id:(-" + replay.getGameId() + ")");
			if (StringUtils.isNotBlank(replay.getSeriesGameId()))
				param.put("fq  ", "p_series_replay_id:(-" + replay.getSeriesGameId() + ")");
			param.put("sort", "p_series_last_date desc,p_series_number desc");
			return getReplays("*", param);
		} catch (Exception e)
		{
			return null;
		}
	}

	private static List<Replay> getReplaysOfMatchup(Replay replay)
	{
		try
		{
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", "pf_race_type:" + replay.getMatchUp());
			param.put("fq ", "p_game_id:(-" + replay.getGameId() + ")");
			if (StringUtils.isNotBlank(replay.getSeriesGameId()))
				param.put("fq  ", "p_series_replay_id:(-" + replay.getSeriesGameId() + ")");
			param.put("sort", "p_series_last_date desc,p_series_number desc");
			return getReplays("*", param);
		} catch (Exception e)
		{
			return null;
		}
	}
	private static List<Replay> getReplaysOfPlayer(Replay replay, int playerId)
	{
		try
		{
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", "f_player_id:" + playerId);
			param.put("fq ", "p_game_id:(-" + replay.getGameId() + ")");
			if (StringUtils.isNotBlank(replay.getSeriesGameId()))
				param.put("fq  ", "p_series_replay_id:(-" + replay.getSeriesGameId() + ")");
			param.put("sort", "p_series_last_date desc,p_series_number desc");
			return getReplays("*", param);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Replay getReplay(String replayId)
	{
		try
		{
			String filter = String.format("p_game_id:(\"%1$s\")", replayId);
			LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
			param.put("fq", filter);
			List<Replay> replays = getReplays("*", param);
			if (replays == null || replays.size() == 0)
				return null;

			return replays.get(0);

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static List<Replay> getReplays(String searchTerms, Map<String, String> params)
	{
		try
		{
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(_solrConfig.getSolrServerUrl());
			SolrQuery query = new SolrQuery();

			// setup search parameters
			query.setQuery(searchTerms);

			if (params != null)
			{
				for (String name : params.keySet())
				{
					if (StringUtils.isNotBlank(name))
						query.add(name.trim(), params.get(name));
				}
			}

			QueryResponse rsp = server.query(query);

			// for zero search result, search using wildcard
			if (rsp.getResults().getNumFound() == 0 )
			{
				return null;
			}
			List<Replay> replays = new ArrayList<Replay>((int)rsp.getResults().getNumFound());
			for (SolrDocument doc : rsp.getResults())
			{
				Replay replay = SearchResultParser.parseReplay(doc);
				replays.add(replay);
			}

			return replays;

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}




}
