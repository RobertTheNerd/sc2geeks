package api.sc2geeks.entity;

import java.util.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/11/11
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult<EntityType>
{
	private long totalMatches;
	private long startFrom;
	private int pageSize;
	private int currentPage;
	private List<EntityType> replayList;
	private List<EntityType> entityList;
	private HashMap<RefinementField, List<NavigationNode>> navigationNodes;

	public long getTotalMatches()
	{
		return totalMatches;
	}

	public void setTotalMatches(long totalMatches)
	{
		this.totalMatches = totalMatches;
	}

	public long getStartFrom()
	{
		return startFrom;
	}

	public void setStartFrom(long startFrom)
	{
		this.startFrom = startFrom;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	/*
	public List<EntityType> getReplayList()
	{
		return replayList;
	}
	*/

	public void setReplayList(List<EntityType> replayList)
	{
		this.replayList = replayList;
	}

	public List<EntityType> getEntityList()
	{
		return entityList;
	}

	public void setEntityList(List<EntityType> entityList)
	{
		this.entityList = entityList;
	}

	public HashMap<RefinementField, List<NavigationNode>> getNavigationNodes()
	{
		return navigationNodes;
	}

	public HashMap<RefinementField, List<NavigationNode>> addNavigationNode(RefinementField field, List<NavigationNode> nodes)
	{
		if (nodes == null || nodes.size() == 1)
			return navigationNodes;
		if (navigationNodes == null)
			navigationNodes = new HashMap<RefinementField, List<NavigationNode>>();

		navigationNodes.put(field, nodes);
		return navigationNodes;
	}

	public boolean hasNavigation()
	{
		if (navigationNodes == null || navigationNodes.size() == 0)
			return false;

		for (Map.Entry<RefinementField, List<NavigationNode>>  entry : navigationNodes.entrySet())
		{
			List<NavigationNode> nodes = entry.getValue();
			if (nodes.size() > 1)
				return true;
		}
		return false;
	}
}
