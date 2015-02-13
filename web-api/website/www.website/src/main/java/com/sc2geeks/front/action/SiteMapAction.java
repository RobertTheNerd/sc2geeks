package com.sc2geeks.front.action;

import api.sc2geeks.entity.*;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.service.PlayerPersonManager;
import api.sc2geeks.service.ReplayManager;
import com.sc2geeks.front.RefinementSetting;
import com.sc2geeks.front.ui.PageUrlAlias;
import com.sc2geeks.front.ui.PageUrlBuilder;
import com.sc2geeks.front.ui.QueryStringManager;
import com.sc2geeks.front.util.sitemap.SiteMapWriter;
import gumi.builders.UrlBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/20/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class SiteMapAction extends ActionBase
{
	private static Logger logger = Logger.getLogger(SiteMapAction.class);
	SearchInput searchInput;
	SearchResult<Replay> searchResult;
	int pageSize = 100;

	@Override
	protected String doExecute()
	{

		String fromIP = request.getRemoteAddr().trim().toLowerCase();
		if (fromIP.compareTo("127.0.0.1") != 0 && fromIP.compareTo("localhost") != 0)
		{
			setValue("message", "404");
			return SUCCESS;
		}
		searchInput = new SearchInput();

		if (websiteConfig.getRefinementSettings() == null)
			return SUCCESS;

		List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>();
		for (RefinementSetting setting : websiteConfig.getRefinementSettings())
		{
			RefinementInfo info = websiteConfig.getReplayLeftNavRefinementProvider().getRefinementInfo(setting.getRefinementField());

			if (info != null)
			{
				info.setMaxCount(0);
				refinementInfoList.add(info);
			}
		}

		searchInput.setRefinementFields(refinementInfoList);
		searchInput.setPageSize(pageSize);
		int currentPage = 1;
		searchInput.setCurrentPage(currentPage);

		ReplayManager replayManager = ServiceFactory.createReplayManager();
		searchResult = replayManager.search(searchInput);

		if (searchResult == null || searchResult.getTotalMatches() == 0)
		{
			setValue("message", "No result found.");
			return SUCCESS;
		}

		String fileName = request.getSession().getServletContext().getRealPath(websiteConfig.getSiteMapFile());
		SiteMapWriter writer = new SiteMapWriter(fileName);

		try
		{

			// write website root
			writer.writeUrl(PageUrlBuilder.getPage(PageUrlAlias.Homepage), null, "hourly");

			// all the top level filters
			if (searchResult.getNavigationNodes() != null)
			{
				for (RefinementField field : searchResult.getNavigationNodes().keySet())
				{
					for (NavigationNode node : searchResult.getNavigationNodes().get(field))
					{
						String url = PageUrlBuilder.getReplaySearchPage(field, node.getNodeName());
						writer.writeUrl(url, null, "hourly");
					}
				}
			}

			// all replays
			searchInput.setRefinementFields(null);
			while (searchResult != null && searchResult.getTotalMatches() > 0
					&& searchResult.getEntityList() != null && searchResult.getEntityList().size() > 0)
			{
				for (Replay replay : searchResult.getEntityList())
				{
					String url = PageUrlBuilder.getReplayDetailPage(replay);
					writer.writeUrl(url, null, "daily");
				}

				if (currentPage * pageSize >= searchResult.getTotalMatches())
					break;

				searchInput.setCurrentPage(++currentPage);
				searchResult = replayManager.search(searchInput);
			}

			// progamer
			doProgamer(writer);

			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}


		setValue("message", "done.");
		return SUCCESS;
	}

	private static void doProgamer(SiteMapWriter writer)
	{
		try
		{
			String progamerListPage = PageUrlBuilder.getProgamerHomePage();
			// progamer list page
			logger.info("Generating progamer listing page.");
			writer.writeUrl(progamerListPage, null, "daily");

			// progamers
			PlayerPersonManager manager = ServiceFactory.createPlayerPersonManager();
			SearchInput searchInput = new SearchInput();
			searchInput.setPageSize(9999);
			List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>();
			for (RefinementInfo info : websiteConfig.getPlayerLeftNavRefinementProvider())
			{
				refinementInfoList.add(info);
			}
			searchInput.setRefinementFields(refinementInfoList);
			logger.info("Querying solr for progamers.");
			SearchResult<PlayerPerson> result = manager.getPlayerPersons(searchInput, false);
			if (result == null)
				return;

			// progamers of individual filters
			logger.info("Generating progamers by Race");
			List<NavigationNode> races = result.getNavigationNodes() == null ? null :
					result.getNavigationNodes().get(RefinementField.PlayerRace);
			if (races != null)
			{
				for (NavigationNode node : races)
				{
					String raceUrl = UrlBuilder.fromString(progamerListPage)
							.addParameter(QueryStringManager.Progamer_List_Param_Race, node.getNodeName())
							.toString();
					writer.writeUrl(raceUrl, null, "daily");
				}
			}
			logger.info("Generating progamers by teams");
			List<NavigationNode> teams = result.getNavigationNodes() == null ? null :
					result.getNavigationNodes().get(RefinementField.Team);
			if (teams != null)
			{
				for (NavigationNode team : teams)
				{
					String teamUrl = UrlBuilder.fromString(progamerListPage)
							.addParameter(QueryStringManager.Progamer_List_Param_Team, team.getNodeName())
							.toString();
					writer.writeUrl(teamUrl, null, "daily");
				}
			}
			logger.info("Generating progamers by countries");
			List<NavigationNode> countries = result.getNavigationNodes() == null ? null :
					result.getNavigationNodes().get(RefinementField.Country);
			if (countries != null)
			{
				for (NavigationNode country : countries)
				{
					String countryUrl = UrlBuilder.fromString(progamerListPage)
							.addParameter(QueryStringManager.Progamer_List_Param_Country, country.getNodeName())
							.toString();
					writer.writeUrl(countryUrl, null, "daily");
				}
			}

			if (result != null && result.getEntityList() != null)
				for (PlayerPerson person : result.getEntityList())
				{
					writer.writeUrl(PageUrlBuilder.getProgamerDetailPage(person), null, "daily");
				}

		} catch(Exception e)
		{
			logger.error("fail to generate progamer urls", e);
		}

	}
}
