package com.sc2geeks.app;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by robert on 8/28/14.
 */
public class Config {

	private static Config instance;
	private Config(){}

	public static Config getInstance() {
		return instance;
	}
	static {
		ApplicationContext context = null;

		String configFile = System.getProperty("config");
		if (StringUtils.isNotBlank(configFile)) {
			try {
				context = new FileSystemXmlApplicationContext(configFile);
			}catch(Exception e){
				context = null;
			}
		}
		if (context == null)
			context	= new ClassPathXmlApplicationContext("config.xml");
		instance = context.getBean("appConfig", Config.class);
	}

	private String solrReplayUrl;
	private String solrPlayerUrl;
	private String mongoHost;
	private int mongoPort;
	private String timeStampFileName;
	private String mongoDBName;
	private String replayCollectionName;
	private String timeZone;
	private String pythonBin;
	private String pythonUpdateScript;

	public String getTimeStampFileName() {
		return timeStampFileName;
	}

	public void setTimeStampFileName(String timeStampFileName) {
		this.timeStampFileName = timeStampFileName;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getSolrReplayUrl() {
		return solrReplayUrl;
	}

	public void setSolrReplayUrl(String solrReplayUrl) {
		this.solrReplayUrl = solrReplayUrl;
	}

	public String getSolrPlayerUrl() {
		return solrPlayerUrl;
	}

	public void setSolrPlayerUrl(String solrPlayerUrl) {
		this.solrPlayerUrl = solrPlayerUrl;
	}

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}


	public String getMongoDBName() {
		return mongoDBName;
	}

	public void setMongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}

	public String getReplayCollectionName() {
		return replayCollectionName;
	}

	public void setReplayCollectionName(String replayCollectionName) {
		this.replayCollectionName = replayCollectionName;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPythonBin() {
		return pythonBin;
	}

	public void setPythonBin(String pythonBin) {
		this.pythonBin = pythonBin;
	}

	public String getPythonUpdateScript() {
		return pythonUpdateScript;
	}

	public void setPythonUpdateScript(String pythonUpdateScript) {
		this.pythonUpdateScript = pythonUpdateScript;
	}
}
