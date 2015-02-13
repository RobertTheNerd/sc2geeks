package com.sc2geeks.front.view;

import api.sc2geeks.entity.Replay;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/6/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class RelatedReplayInfo
{
	String tabId;
	String tabName;
	String urlForAll;
	List<Replay> replays;

	public String getTabId()
	{
		return tabId;
	}

	public void setTabId(String tabId)
	{
		this.tabId = tabId;
	}

	public String getTabName()
	{
		return tabName;
	}

	public void setTabName(String tabName)
	{
		this.tabName = tabName;
	}

	public List<Replay> getReplays()
	{
		return replays;
	}

	public void setReplays(List<Replay> replays)
	{
		this.replays = replays;
	}

	public String getUrlForAll()
	{
		return urlForAll;
	}

	public void setUrlForAll(String urlForAll)
	{
		this.urlForAll = urlForAll;
	}
}
