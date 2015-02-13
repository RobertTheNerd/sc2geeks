package com.sc2geeks.replayUtility.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/3/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerFactory
{
	public static List<CrawlerBase> createCrawlers()
	{
		List<CrawlerBase> crawlers = new ArrayList<CrawlerBase>();
		crawlers.add(new Crawler_GosugamersNet());
		crawlers.add(new Crawler_Sc2replayNet());
		return crawlers;
	}
}
