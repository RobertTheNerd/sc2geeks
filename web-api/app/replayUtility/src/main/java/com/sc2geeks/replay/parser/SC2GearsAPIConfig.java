package com.sc2geeks.replay.parser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SC2GearsAPIConfig
{
	private String apiKey;
	private String url;
	private String protVer;


	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getProtVer()
	{
		return protVer;
	}

	public void setProtVer(String protVer)
	{
		this.protVer = protVer;
	}

	private SC2GearsAPIConfig(){}

	private static SC2GearsAPIConfig instance;
	public static SC2GearsAPIConfig getInstance(){
		if (instance == null)
		{
			ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
			instance = context.getBean("SC2GearsAPIConfig", SC2GearsAPIConfig.class);
		}
		return instance;
	}
}
