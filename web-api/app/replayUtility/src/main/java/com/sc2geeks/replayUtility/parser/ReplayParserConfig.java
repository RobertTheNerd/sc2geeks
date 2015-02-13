package com.sc2geeks.replayUtility.parser;

import com.sc2geeks.replayUtility.SpringConfigProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayParserConfig
{
	private int batchSize;
	private String parseXmlFolder;
	private String downloadedReplayFolder;
	private String uploadReplayFolder;
	private int maxFailedRetries;

	/**
	 *
	 * @return folder with a trailing '/'
	 */
	public String getParseXmlFolder()
	{
		return parseXmlFolder;
	}
	public void setParseXmlFolder(String parseXmlFolder)
	{
		this.parseXmlFolder = StringUtils.removeEnd(parseXmlFolder, "/") + "/";
	}

	/**
	 *
	 * @return folder with a trailing '/'
	 */
	public String getDownloadedReplayFolder()
	{
		return downloadedReplayFolder;
	}

	public void setDownloadedReplayFolder(String downloadedReplayFolder)
	{
		this.downloadedReplayFolder = StringUtils.removeEnd(downloadedReplayFolder, "/") + "/";
	}

	public String getUploadReplayFolder()
	{
		return uploadReplayFolder;
	}

	public void setUploadReplayFolder(String uploadReplayFolder)
	{
		this.uploadReplayFolder = StringUtils.removeEnd(uploadReplayFolder, "/") + "/";
	}

	public int getBatchSize()
	{
		return batchSize;
	}

	public void setBatchSize(int batchSize)
	{
		this.batchSize = batchSize;
	}

	public int getMaxFailedRetries()
	{
		return maxFailedRetries;
	}

	public void setMaxFailedRetries(int maxFailedRetries)
	{
		this.maxFailedRetries = maxFailedRetries;
	}

	private ReplayParserConfig(){}

	private static ReplayParserConfig instance;
	public static ReplayParserConfig getInstance(){
		if (instance == null)
		{
			ApplicationContext context = new ClassPathXmlApplicationContext(SpringConfigProvider.getSpringConfigFile());
			instance = context.getBean("replayParserConfig", ReplayParserConfig.class);
		}
		return instance;
	}
}
