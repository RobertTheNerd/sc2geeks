package api.sc2geeks.service.imp;

import api.sc2geeks.entity.*;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.entity.playerperson.PlayerPersonRequestInfo;
import api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/29/12
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPersonManagerImp
{
	private static SolrConfig solrConfig = SolrConfig.getPlayerInstance();

	private SolrSearcher solrSearcher;

	public PlayerPersonManagerImp()
	{
		this.solrSearcher = new SolrSearcher(solrConfig);
	}

	public SearchResult<PlayerPerson> getPlayerPersons(SearchInput searchInput, boolean withReplaysOnly)
	{
		Map<String, String> extraParam = null;
		if (withReplaysOnly)
		{
			extraParam = new HashMap<String, String>(1);
			extraParam.put("fq","-p_replay_count:0");
		}

		SearchResult<PlayerPerson> result = solrSearcher.search(searchInput, extraParam, null, PlayerPerson.class);

		return result;
	}

	public List<String> getAllTeams()
	{
		SearchInput searchInput = new SearchInput();
		searchInput.setSearchType(SearchType.RefinementOnly);
		RefinementInfo refinementInfo = new RefinementInfo();
		refinementInfo.setMaxCount(0);
		refinementInfo.setRefinementField(RefinementField.Team);
		refinementInfo.setSortMethod(RefinementSortMethod.Name);
		refinementInfo.setSortOrder(SortOrder.Asc);
		searchInput.addRefinementInfo(refinementInfo);

		SearchResult<PlayerPerson> result = solrSearcher.search(searchInput, PlayerPerson.class);
		if (result == null || result.getNavigationNodes() == null)
			return null;

		List<NavigationNode> nodes = result.getNavigationNodes().get(RefinementField.Team);
		if (nodes == null || nodes.size() == 0)
			return null;

		List<String> teams = new ArrayList<String>(nodes.size());
		for (NavigationNode node : nodes)
		{
			teams.add(node.getNodeName());
		}
		return teams;
	}

	public PlayerPerson getPlayerPerson(int personId)
	{
		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("fq", "p_person_id:" + Integer.toString(personId));
		List<PlayerPerson> list = solrSearcher.search("*", param, PlayerPerson.class);
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	public PlayerPersonWithRelatedInfo getPlayerInfo(PlayerPersonRequestInfo requestInfo)
	{
		PlayerPerson playerPerson = getPlayerPerson(requestInfo.getPersonId());
		if (playerPerson == null)
			return null;

		PlayerPersonWithRelatedInfo result = new PlayerPersonWithRelatedInfo();
		result.setPlayerPerson(playerPerson);

		// get replays
		ReplayManagerImp replayManager = new ReplayManagerImp();
		SearchInput searchInput = new SearchInput();
		searchInput.addSearchFilter(RefinementField.ProgamerId, Integer.toString(requestInfo.getPersonId()));
		searchInput.setPageSize(requestInfo.getNumberOfReplays());
		SearchResult<Replay> replays = replayManager.search(searchInput);
		result.setReplays(replays);

		// get team mates
		searchInput = new SearchInput();
		searchInput.addSearchFilter(RefinementField.Team, playerPerson.getTeam());
		searchInput.setPageSize(requestInfo.getNumberOfTeamMates());
		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>(2);
		param.put("fq", "-p_person_id:\"" + playerPerson.getPersonId() + "\"");
		List<SolrSortInfo> sortInfoList = new ArrayList<SolrSortInfo>(1);
		sortInfoList.add(new SolrSortInfo("p_replay_count", SolrQuery.ORDER.desc));
		// sortInfoList.add(new SolrSortInfo("p_liquipedia_name", SolrQuery.ORDER.asc)); already in config
		SearchResult<PlayerPerson> teamMates = solrSearcher.search(searchInput, param, sortInfoList, PlayerPerson.class);
		result.setTeamMates(teamMates);

		// get player of same race
		searchInput = new SearchInput();
		searchInput.addSearchFilter(RefinementField.PlayerRace, playerPerson.getRace());
		searchInput.setPageSize(requestInfo.getNumberOfSameRacePlayers());
		if (teamMates != null && teamMates.getEntityList() != null && teamMates.getEntityList().size() > 0)
			param.put("fq", "-p_person_id:(" + getPlayerPersonIdList(teamMates.getEntityList()) + ")");
		SearchResult<PlayerPerson> playersOfSameRace = solrSearcher.search(searchInput, param, sortInfoList, PlayerPerson.class);
		result.setPlayersOfSameRace(playersOfSameRace);

		return result;
	}

	private static String getPlayerPersonIdList(List<PlayerPerson> list)
	{
		StringBuilder sb = new StringBuilder();
		for (PlayerPerson person : list)
		{
			sb.append("\"").append(person.getPersonId()).append("\"").append(" OR ");
		}
		return StringUtils.removeEnd(sb.toString(), " OR ");
	}
}
