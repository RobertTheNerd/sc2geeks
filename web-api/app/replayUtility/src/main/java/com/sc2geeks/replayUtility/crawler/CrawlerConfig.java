package com.sc2geeks.replayUtility.crawler;

import com.sc2geeks.replayUtility.SpringConfigProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/29/11
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerConfig {
	private int startingPage_sc2rep;
	private int startingPage_163;
	private int maxExistedGameBeforeStop;
	private Date stopAtDate;
	private int sleepFor163;
	private int maxRetries;

	static private CrawlerConfig instance;
	public static CrawlerConfig getInstance()
	{
		if (instance == null)
		{
			ApplicationContext context = new ClassPathXmlApplicationContext(SpringConfigProvider.getSpringConfigFile());
			instance = context.getBean("crawlerConfig", CrawlerConfig.class);
		}
		return instance;
	}

	public int getStartingPage_sc2rep() {
		return startingPage_sc2rep;
	}

	public void setStartingPage_sc2rep(int startingPage_sc2rep) {
		this.startingPage_sc2rep = startingPage_sc2rep;
	}

	public int getMaxExistedGameBeforeStop() {
		return maxExistedGameBeforeStop;
	}

	public void setMaxExistedGameBeforeStop(int maxExistedGameBeforeStop) {
		this.maxExistedGameBeforeStop = maxExistedGameBeforeStop;
	}

	public Date getStopAtDate()
	{
		return stopAtDate;
	}

	public void setStopAtDate(Date stopAtDate)
	{
		this.stopAtDate = stopAtDate;
	}

	public int getStartingPage_163()
	{
		return startingPage_163;
	}

	public void setStartingPage_163(int startingPage_163)
	{
		this.startingPage_163 = startingPage_163;
	}

	public int getSleepFor163()
	{
		return sleepFor163;
	}

	public void setSleepFor163(int sleepFor163)
	{
		this.sleepFor163 = sleepFor163;
	}

	public int getMaxRetries()
	{
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries)
	{
		this.maxRetries = maxRetries;
	}
}
