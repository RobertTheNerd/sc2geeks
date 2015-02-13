package com.sc2geeks.PlayerCrawler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/16/12
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerConfig
{
	private static CrawlerConfig instance;

	static {
		ApplicationContext context = null;

		String configFile = System.getProperty("crawlerConfig");
		if (StringUtils.isNotBlank(configFile)) {
			try {
				context = new FileSystemXmlApplicationContext(configFile);
			}catch(Exception e){
				context = null;
			}
		}
		if (context == null)
			context	= new ClassPathXmlApplicationContext("config.xml");
		instance = context.getBean("crawlerConfig", CrawlerConfig.class);
	}

	public static CrawlerConfig getInstance()
	{
		return instance;
	}
	private String mongoHost;
	private int mongoPort;
	private String mongoDBName;
	private String crawledProgamerCollectionName;
	String allPlayerUrl;
	String wikiRootUrl;

	public String getAllPlayerUrl()
	{
		return allPlayerUrl;
	}

	public void setAllPlayerUrl(String allPlayerUrl)
	{
		this.allPlayerUrl = allPlayerUrl;
	}


	public String getWikiRootUrl()
	{
		return wikiRootUrl;
	}

	public void setWikiRootUrl(String wikiRootUrl)
	{
		this.wikiRootUrl = wikiRootUrl;
	}
	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDBName() {
		return mongoDBName;
	}

	public void setMongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}

	public String getCrawledProgamerCollectionName() {
		return crawledProgamerCollectionName;
	}

	public void setCrawledProgamerCollectionName(String crawledProgamerCollectionName) {
		this.crawledProgamerCollectionName = crawledProgamerCollectionName;
	}
}
