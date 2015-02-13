package com.sc2geeks.replayUtility;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/10/11
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWorkerConfig
{
	private boolean doCrawl;
	private boolean doDownload;
	private boolean doReplayParse;
	private boolean doMapParse;
	private boolean doPostProcessing;

	public boolean isDoCrawl() {
		return doCrawl;
	}

	public void setDoCrawl(boolean doCrawl) {
		this.doCrawl = doCrawl;
	}

	public boolean isDoDownload() {
		return doDownload;
	}

	public void setDoDownload(boolean doDownload) {
		this.doDownload = doDownload;
	}

	public boolean isDoReplayParse() {
		return doReplayParse;
	}

	public void setDoReplayParse(boolean doReplayParse) {
		this.doReplayParse = doReplayParse;
	}

	public boolean isDoPostProcessing()
	{
		return doPostProcessing;
	}

	public void setDoPostProcessing(boolean doPostProcessing)
	{
		this.doPostProcessing = doPostProcessing;
	}

	public boolean isDoMapParse()
	{
		return doMapParse;
	}

	public void setDoMapParse(boolean doMapParse)
	{
		this.doMapParse = doMapParse;
	}
}
