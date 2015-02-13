package com.sc2geeks.front;

/**
 *
 *  A full url can consists of:
 *      - websiteRoot
 *      - namespace
 *      - action path
 *      - query string
 */
public class RequestContext
{
	public static final String Tab_Replay = "Replay";
	public static final String Tab_Progamer = "Progamer";


	String appContextPath;
	String actionPath;
	String actionNameSpace;
	String actionExtension;
	String queryString;
	String pageTab;
	String remoteIP;
	String searchTerms;
	String searchSubmitUrl;

	public String getAppContextPath()
	{
		return appContextPath;
	}

	public void setAppContextPath(String appContextPath)
	{
		this.appContextPath = appContextPath;
	}

	/**
	 * Get the action path without website root, namespace, possible index.html, and any extension. e.g
	 *      http://test.sc2geeks.com:7080/replay/nav/Event-ZOTAC+Cup/MatchUp-PvP.html?page=4
	 * action path is:
	 *      /Event-ZOTAC+Cup/MatchUp-PvP
	 * @return
	 */
	public String getActionPath()
	{
		return actionPath;
	}

	public void setActionPath(String actionPath)
	{
		this.actionPath = actionPath;
	}

	/**
	 * Get namespace of the url. e.g:
	 *      http://test.sc2geeks.com:7080/replay/nav/Event-ZOTAC+Cup/MatchUp-PvP.html?page=4
	 * namespace is:
	 *      /replay/nav
	 *
	 * based on setting:
	 * 	&lt;package name=&quot;replay_nav&quot; namespace=&quot;/replay/nav&quot; extends=&quot;sc2geeks-default&quot;&gt;
	 &lt;action name=&quot;**&quot; class=&quot;com.sc2geeks.front.action.replay.SearchAction&quot;&gt;
	 &lt;result name=&quot;success&quot; type=&quot;tiles&quot;&gt;searchResultLayout&lt;/result&gt;
	 &lt;result name=&quot;refinement&quot;&gt;/views/replay/search/allNavs.jsp&lt;/result&gt;
	 &lt;/action&gt;
	 &lt;/package&gt;
	 *
	 *
	 * @return
	 */
	public String getActionNameSpace()
	{
		return actionNameSpace;
	}

	public void setActionNameSpace(String actionNameSpace)
	{
		this.actionNameSpace = actionNameSpace;
	}

	public String getActionExtension()
	{
		return actionExtension;
	}

	public void setActionExtension(String actionExtension)
	{
		this.actionExtension = actionExtension;
	}

	/**
	 * Query string of a url. e.g:
	 *      http://test.sc2geeks.com:7080/replay/nav/Event-ZOTAC+Cup/MatchUp-PvP.html?page=4
	 * query string is
	 *      page=4
	 * @return
	 */
	public String getQueryString()
	{
		return queryString;
	}

	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}

	public String getPageTab()
	{
		return pageTab;
	}

	public void setPageTab(String pageTab)
	{
		this.pageTab = pageTab;
	}

	public String getRemoteIP()
	{
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP)
	{
		this.remoteIP = remoteIP;
	}

	public String getSearchTerms()
	{
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms)
	{
		this.searchTerms = searchTerms;
	}

	public String getSearchSubmitUrl()
	{
		return searchSubmitUrl;
	}

	public void setSearchSubmitUrl(String searchSubmitUrl)
	{
		this.searchSubmitUrl = searchSubmitUrl;
	}
}
