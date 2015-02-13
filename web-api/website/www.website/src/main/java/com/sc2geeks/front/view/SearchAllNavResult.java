package com.sc2geeks.front.view;

import api.sc2geeks.entity.*;
import com.sc2geeks.front.WebsiteConfig;
import com.sc2geeks.front.ui.SearchUrlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/5/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchAllNavResult
{
	private static WebsiteConfig websiteConfig = WebsiteConfig.getInstance();

	String filterName;
	int totalCount;
	List<NavigationNode> nodes;
	List<String> links;

	public String getFilterName()
	{
		return filterName;
	}

	public void setFilterName(String filterName)
	{
		this.filterName = filterName;
	}

	public int getTotalCount()
	{
		return totalCount;
	}

	public List<NavigationNode> getNavigationNodes()
	{
		return nodes;
	}

	public List<String> getLinks()
	{
		return links;
	}

	public SearchAllNavResult(SearchInput searchInput, SearchResult<Replay> searchResult, SearchUrlHelper searchUrlHelper)
	{
		if (searchInput.getRefinementFields() == null || searchInput.getRefinementFields().size() == 0)
			return;

		RefinementField field = searchInput.getRefinementFields().get(0).getRefinementField();
		filterName = websiteConfig.getRefinementDisplayName(field);

		totalCount = searchResult.getNavigationNodes() == null ||
				searchResult.getNavigationNodes().size() == 0 ||
				(nodes = searchResult.getNavigationNodes().get(field)) == null
				? 0 : nodes.size();

		if (totalCount > 0)
		{
			links = new ArrayList<String>(totalCount);
			for (int i = 0; i < totalCount; i++)
			{
				links.add(searchUrlHelper.addField(field, nodes.get(i).getNodeName()));
			}
		}
	}
}