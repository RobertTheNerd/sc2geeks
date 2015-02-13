package api.sc2geeks.service.imp;

import api.sc2geeks.entity.*;
import api.sc2geeks.entity.Map;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/14/11
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
class SearchResultParser
{
	private static SolrConfig _solrConfig = SolrConfig.getReplayInstance();

	public static Replay parseReplay(SolrDocument doc)
	{
		try
		{
			Gson gson = new Gson();
			Replay replay = new Replay();
			replay.setGameId((String) doc.getFieldValue("p_game_id"));
			replay.setGameDate((Date) doc.getFieldValue("p_game_date"));
			replay.setDuration((Integer) doc.getFieldValue("p_duration"));
			replay.setGateway((String) doc.getFieldValue("p_gateway"));
			replay.setEvent((String) doc.getFieldValue("f_event"));

			replay.setReplayFile((String) doc.getFieldValue("p_replay_file"));
			replay.setVersion((String) doc.getFieldValue("f_game_version"));

			// map
			Map map = new Map();
			map.setMapId((Integer) doc.getFieldValue("p_map_id"));
			map.setMapName((String) doc.getFieldValue("pf_map_name"));
			String map_image = (String) doc.getFieldValue("p_map_image");
			map.setMapImageName(map_image);
			if (StringUtils.isNotBlank(map_image)) {
				map.setImageInfo(gson.fromJson(map_image, ImageInfo.class));
			}
			replay.setMap(map);

			// game teams
			ArrayList<PlayerTeam> teams = new ArrayList<PlayerTeam>(2);

			// game team1
			PlayerTeam team1 = new PlayerTeam();
			ArrayList<Player> players = new ArrayList<Player>(1);
			Player player1 = getPlayer(doc, 1);
			players.add(player1);
			team1.setPlayers(players);
			teams.add(team1);

			// game team2
			PlayerTeam team2 = new PlayerTeam();
			players = new ArrayList<Player>(1);
			Player player2 = getPlayer(doc, 2);
			players.add(player2);
			team2.setPlayers(players);
			teams.add(team2);

			// series
			Integer seriesReplayId = getDocFieldValue(doc, "p_series_replay_id");
			if (seriesReplayId != null)
			{
				replay.setSeriesGameId(seriesReplayId.toString());
			}
			replay.setSeriesCount(getIntValue((Integer) getDocFieldValue(doc, "p_series_count")));
			replay.setSeriesNumber(getIntValue((Integer) getDocFieldValue(doc, "p_series_number")));
			replay.setSeriesFirstGameDate((Date) doc.getFieldValue("p_series_first_date"));
			replay.setSeriesLastGameDate((Date) doc.getFieldValue("p_series_last_date"));

			replay.setPlayerTeams(teams);

			return replay;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Player getPlayer(SolrDocument doc, int index) {
		Player player = new Player();
		String prefix = "p_player" + index + "_";
		player.setPlayerId((Integer) doc.getFieldValue(prefix + "id"));
		player.setApm((Integer) doc.getFieldValue(prefix + "apm"));
		player.setPlayerName((String) doc.getFieldValue(prefix + "name"));
		player.setPlayerRace((String) doc.getFieldValue(prefix + "race"));
		player.setRandom((Boolean) doc.getFieldValue(prefix + "is_random"));
		player.setWinner((Boolean) doc.getFieldValue(prefix + "is_winner"));
		player.setColor((Integer) doc.getFieldValue(prefix + "color"));
		player.setPid((Integer) doc.getFieldValue(prefix + "pid"));
		Integer progamer_id = getDocFieldValue(doc, prefix + "progamer_id");
		if (progamer_id != null) {
			player.setProgamerId(progamer_id);
			player.setProgamerName((String) getDocFieldValue(doc, prefix + "progamer_name"));
		}

		return player;
	}

	public static PlayerPerson parsePlayerPerson(SolrDocument doc)
	{
		try
		{
			PlayerPerson person = new PlayerPerson();

			person.setPersonId(Integer.parseInt((String)doc.getFieldValue("p_person_id")));
			person.setGameId((String)doc.getFieldValue("p_liquipedia_name"));
			person.setEnFullName((String)doc.getFieldValue("p_en_fullname"));
			person.setNativeFullName((String)doc.getFieldValue("p_native_fullname"));
			person.setTeam((String)doc.getFieldValue("pf_team"));
			person.setAltIds((String)doc.getFieldValue("p_alt_ids"));
			person.setLocalImageName((String)doc.getFieldValue("p_image_name"));
			person.setStream((String)doc.getFieldValue("p_stream_url"));
			person.setWikiUrl((String)doc.getFieldValue("p_wiki_url"));
			person.setTwitterHandle((String)doc.getFieldValue("p_twitter_url"));
			person.setFanPage((String)doc.getFieldValue("p_fanpage"));
			person.setCountry((String)doc.getFieldValue("pf_country"));
			person.setBirthday((String)doc.getFieldValue("p_birthday"));
			person.setRace((String)doc.getFieldValue("pf_race"));
			person.setReplayCount(getIntValue(doc, "p_replay_count", 0));

			String imageContent = (String)doc.getFieldValue("p_image");
			if (StringUtils.isNotBlank(imageContent)) {
				person.setImageInfo(new Gson().fromJson(imageContent, ImageInfo.class));
			}

			return person;

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	private static int getIntValue(SolrDocument doc, String fieldName, int defaultValue)
	{
		try
		{
			Integer value = (Integer) doc.getFieldValue(fieldName);
			if (value == null)
				return defaultValue;
			return value;
		} catch (Exception e)
		{
			return defaultValue;
		}
	}

	public static <EntityType> EntityType parseEntity(SolrDocument doc, Class<EntityType> typeClass)
	{
		if (typeClass == Replay.class)
			return (EntityType) parseReplay(doc);
		// todo

		if (typeClass == PlayerPerson.class)
			return (EntityType) parsePlayerPerson(doc);

		return null;
	}

	private static <T> T getDocFieldValue(SolrDocument doc, String field)
	{
		if (!doc.getFieldNames().contains(field))
			return null;
		return (T) doc.getFieldValue(field);
	}

	public static int getIntValue(Integer value)
	{
		if (value == null)
			return 0;
		return value;
	}


	/**
	 * construct a search result based on solr response
	 *
	 * @param rsp, should be QueryResponse.getResponse()
	 * @return
	 */
	public static <EntityType> SearchResult<EntityType> parseSearchResult(SearchInput si, QueryResponse rsp,
	         SolrConfig solrConfig, Class<EntityType> typeClass)
	{
		if (rsp == null || rsp.getStatus() != 0)
		{
			return null;
		}
		HashMap<RefinementField, RefinementInfo> refinementInfoMapByField = createIndexedMap(si.getRefinementFields());

		SearchResult<EntityType> result = new SearchResult<EntityType>();

		// replay list
		SolrDocumentList docs = rsp.getResults();

		// total number of result
		result.setTotalMatches(docs.getNumFound());

		// start from
		result.setStartFrom(docs.getStart());

		if (si.getSearchType() != SearchType.RefinementOnly)
		{
			ArrayList<EntityType> entityList = new ArrayList<EntityType>(docs.size());
			for (SolrDocument doc : docs)
			{
				entityList.add(parseEntity(doc, typeClass));
			}
			result.setEntityList(entityList);
		}

		// facets
		List<FacetField> facets = (List<FacetField>) rsp.getFacetFields();
		if (facets != null && facets.size() > 0)
		{
			for (FacetField facet : facets)
			{
				processFacetFieldForSearchResult(result, facet, refinementInfoMapByField, solrConfig);
			}
		}

		return result;
	}

	private static void removeEmptyFacetCount(List<FacetField.Count> list)
	{
		ArrayList<FacetField.Count> toRemove = new ArrayList<FacetField.Count>();
		for (FacetField.Count count : list)
		{
			if (StringUtils.isBlank(count.getName()))
				toRemove.add(count);
		}
		if (toRemove.size() > 0)
		{
			list.removeAll(toRemove);
		}
	}

	private static long min(long a, long b)
	{
		return a > b ? b : a;
	}

	private static SearchResult processFacetFieldForSearchResult(SearchResult searchResult, FacetField facetField,
	                                                             HashMap<RefinementField, RefinementInfo> map,
	                                                             SolrConfig solrConfig)
	{
		if (facetField == null || facetField.getValueCount() <= 0)
			return searchResult;

		try
		{
			RefinementField refinementField = solrConfig.getSolrField(facetField.getName()).getRefinementField();
			RefinementInfo refinementInfo = map.get(refinementField);
			searchResult.addNavigationNode(refinementField, createNodesFromFacetField(refinementInfo, facetField));
		}
		catch(Exception e){}
		finally
		{
			return searchResult;
		}
	}

	private static HashMap<RefinementField, RefinementInfo> createIndexedMap(List<RefinementInfo> infoList)
	{
		if (infoList == null)
			return null;

		HashMap<RefinementField, RefinementInfo> map = new HashMap(infoList.size());
		for (RefinementInfo info : infoList)
		{
			map.put(info.getRefinementField(), info);
		}
		return map;
	}

	private static List<NavigationNode> createNodesFromFacetField(RefinementInfo refinementInfo, FacetField facetField)
	{
		List<FacetField.Count> facetValues = getNonEmptyValues(facetField.getValues());
		if (!SolrSearcher.isSortingSupported(refinementInfo))
		{
			Collections.sort(facetValues, new SolrCountComparator(refinementInfo.getSortMethod(), refinementInfo.getSortOrder()));
		}
		int count = refinementInfo.getMaxCount() == 0 ? facetValues.size() : Math.min(refinementInfo.getMaxCount(), facetValues.size());
		List<NavigationNode> nodes = new ArrayList<NavigationNode>(count);
		for (int i = 0; i < count; i++)
		{
			FacetField.Count fc = facetValues.get(i);
			NavigationNode node = new NavigationNode();
			node.setNodeName(fc.getName());
			node.setCount(fc.getCount());
			nodes.add(node);
		}
		return nodes;
	}

	private static List<FacetField.Count> getNonEmptyValues(List<FacetField.Count> list)
	{
		List<FacetField.Count> result = new ArrayList<FacetField.Count>(list.size());
		for (FacetField.Count count : list)
		{
			if (count == null || StringUtils.isBlank(count.getName()))
				continue;
			result.add(count);
		}
		return result;
	}
}
