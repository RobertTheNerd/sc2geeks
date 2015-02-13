package com.sc2geeks.front.action.replay;

import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.SearchInput;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.SearchType;
import api.sc2geeks.service.ReplayManager;
import com.sc2geeks.front.action.ServiceFactory;
import com.sc2geeks.front.ui.PageUrlHelper;
import com.sc2geeks.front.ui.SearchUrlHelper;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/9/12
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchAction extends ReplayActionBase
{
	private static Logger logger = Logger.getLogger(SearchAction.class);

	protected SearchInput searchInput;
	protected SearchResult<Replay> searchResult;
	private SearchUrlHelper searchUrlHelper;

	protected void init()
	{
		super.init();

		// get search input
		searchInput = searchUrlHelper.getSearchInput();
		valueStack.set("searchInput", searchInput);

		requestContext.setSearchTerms(searchInput.hasEmptySearchTerm() ? "" : searchInput.getSearchTerms());
		pageSEOInfo.setCanonicalUrl(searchUrlHelper.toString());
	}

	@Override
	protected PageUrlHelper parseUrl()
	{
		searchUrlHelper = new SearchUrlHelper(requestContext.getActionNameSpace(), requestContext.getActionPath(), requestContext.getQueryString(),
				websiteConfig.getReplayLeftNavRefinementProvider(),
				websiteConfig.getReplayShowAllRefinementProvider());
		setValue("searchUrlHelper", searchUrlHelper);
		return searchUrlHelper;
	}

	@Override
	protected String doExecute()
	{
		try
		{
			if (searchUrlHelper.isLegacyUrlDetected())
			{
				setValue("redirectUrl", pageSEOInfo.getCanonicalUrl());
				setValue("statusCode", 301);
				return REDIRECT;
			}

			ReplayManager manager = ServiceFactory.createReplayManager();
			searchResult = manager.search(searchInput);
			valueStack.set("searchResult", searchResult);
		}catch (Exception e){
			logger.warn("Fail to get replay list. Switching to 404.", e);
			return NOT_FOUND;
		}
		return searchInput.getSearchType() == SearchType.RefinementOnly ? "refinement" : SUCCESS;
	}

}
