package com.sc2geeks.replayUtility.downloader;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/29/11
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DownloaderConfig {
	private int batchSize = 100;
	private boolean processFailedReplays = true;
	private int maxFailedRetries = 5;
	private int sleepFor163;

	public String getDownloadFolder() {
		return downloadFolder;
	}

	public void setDownloadFolder(String downloadFolder) {
		this.downloadFolder = downloadFolder;
	}

	private String downloadFolder = "/tmp/sc2replay/";

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public boolean isProcessFailedReplays() {
		return processFailedReplays;
	}

	public void setProcessFailedReplays(boolean processFailedReplays) {
		this.processFailedReplays = processFailedReplays;
	}

	public int getMaxFailedRetries() {
		return maxFailedRetries;
	}

	public void setMaxFailedRetries(int maxFailedRetries) {
		this.maxFailedRetries = maxFailedRetries;
	}

	public int getSleepFor163()
	{
		return sleepFor163;
	}

	public void setSleepFor163(int sleepFor163)
	{
		this.sleepFor163 = sleepFor163;
	}
}
