package com.sc2geeks.replay.model;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/3/12
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgamerImageLogEntity
{
	private int logId;

	public int getLogId()
	{
		return logId;
	}

	public void setLogId(int logId)
	{
		this.logId = logId;
	}


	private String remoteImageUrl;

	public String getRemoteImageUrl()
	{
		return remoteImageUrl;
	}

	public void setRemoteImageUrl(String remoteImageUrl)
	{
		this.remoteImageUrl = remoteImageUrl;
	}

	private Timestamp crawledDate;

	public Timestamp getCrawledDate()
	{
		return crawledDate;
	}

	public void setCrawledDate(Timestamp crawledDate)
	{
		this.crawledDate = crawledDate;
	}



	private ProgamerEntity progamer;

	public ProgamerEntity getProgamer()
	{
		return progamer;
	}

	public void setProgamer(ProgamerEntity progamer)
	{
		this.progamer = progamer;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProgamerImageLogEntity that = (ProgamerImageLogEntity) o;

		if (logId != that.logId) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = logId;
		result = 31 * result + (remoteImageUrl != null ? remoteImageUrl.hashCode() : 0);
		result = 31 * result + (crawledDate != null ? crawledDate.hashCode() : 0);
		return result;
	}
}
