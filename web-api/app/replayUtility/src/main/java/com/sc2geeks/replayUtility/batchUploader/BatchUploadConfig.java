package com.sc2geeks.replayUtility.batchUploader;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/4/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchUploadConfig
{
	private String inputFolder;
	private String event;
	private String targetFolder;

	public String getInputFolder()
	{
		return inputFolder;
	}

	public void setInputFolder(String inputFolder)
	{
		this.inputFolder = inputFolder;
	}

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}

	public String getTargetFolder()
	{
		return targetFolder;
	}

	public void setTargetFolder(String targetFolder)
	{
		this.targetFolder = targetFolder;
	}
}
