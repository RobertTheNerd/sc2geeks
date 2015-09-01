package com.sc2geeks.front.action.progamer;

import api.sc2geeks.entity.RefinementField;
import api.sc2geeks.entity.RefinementInfo;
import api.sc2geeks.entity.SearchInput;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.service.PlayerPersonManager;
import com.sc2geeks.front.action.ServiceFactory;
import com.sc2geeks.front.ui.PageUrlBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 8/18/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgamerListAction extends ProgamerActionBase
{
	private String searchTerm;
	private SearchInput searchInput;
	private SearchResult<PlayerPerson> searchResult;
	private Map<PlayerPerson, Long> personReplayCountMap;
	private boolean withReplaysOnly = false;


	public void setQ(String q)
	{
		searchTerm = q;
		if (searchTerm != null)
			searchTerm = searchTerm.trim();
	}

	private String playerTeam;

	public void setPlayerTeam(String teamName)
	{
		this.playerTeam = teamName;
	}

	private String race;

	public void setRace(String race)
	{
		this.race = race;
	}

	private String country;

	public void setCountry(String country)
	{
		this.country = country;
	}

	public void setWithReplaysOnly(boolean withReplaysOnly)
	{
		this.withReplaysOnly = withReplaysOnly;
	}

	@Override
	protected String doExecute()
	{
		// get player list
		PlayerPersonManager manager = ServiceFactory.createPlayerPersonManager();
		searchResult = manager.getPlayerPersons(searchInput, withReplaysOnly);
		setValue("searchResult", searchResult);

		setValue("playerTeam", playerTeam);
		setValue("race", race);
		setValue("country", country);
		setValue("withReplaysOnly", withReplaysOnly);

		return SUCCESS;
	}

	protected void init()
	{
		super.init();
		parseSearchInput();
		requestContext.setSearchTerms(searchTerm);
	}

	protected void postExecute()
	{
		pageSEOInfo.setCanonicalUrl(PageUrlBuilder.getProgamerHomePage());
		super.postExecute();
	}


	private void parseSearchInput()
	{
		searchInput = new SearchInput();
		searchInput.setSearchTerms(searchTerm);

		searchInput.setPageSize(9999);
		List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>();
		for (RefinementInfo info : websiteConfig.getPlayerLeftNavRefinementProvider())
		{
			refinementInfoList.add(info);
		}
		searchInput.setRefinementFields(refinementInfoList);

		if (StringUtils.isNotBlank(playerTeam))
		{
			searchInput.addSearchFilter(RefinementField.Team, playerTeam);
		}
		if (StringUtils.isNotBlank(race))
		{
			searchInput.addSearchFilter(RefinementField.PlayerRace, race);
		}
		if (StringUtils.isNotBlank(country))
		{
			searchInput.addSearchFilter(RefinementField.Country, country);
		}
	}
}
