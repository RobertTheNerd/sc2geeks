package com.sc2geeks.replay.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgamerEntity
{
	private int progamerId;

	public int getProgamerId()
	{
		return progamerId;
	}

	public void setProgamerId(int progamerId)
	{
		this.progamerId = progamerId;
	}

	private String gameName;

	public String getGameName()
	{
		return gameName;
	}

	public void setGameName(String gameName)
	{
		this.gameName = gameName;
	}

	private String enFullName;

	public String getEnFullName()
	{
		return enFullName;
	}

	public void setEnFullName(String enFullName)
	{
		this.enFullName = enFullName;
	}

	private String nativeFullName;

	public String getNativeFullName()
	{
		return nativeFullName;
	}

	public void setNativeFullName(String nativeFullName)
	{
		this.nativeFullName = nativeFullName;
	}

	private String team;

	public String getTeam()
	{
		return team;
	}

	public void setTeam(String team)
	{
		this.team = team;
	}

	private String altIds;

	public String getAltIds()
	{
		return altIds;
	}

	public void setAltIds(String altIds)
	{
		this.altIds = altIds;
	}

	private String race;

	public String getRace()
	{
		return race;
	}

	public void setRace(String race)
	{
		this.race = race;
	}

	private String wikiUrl;

	public String getWikiUrl()
	{
		return wikiUrl;
	}

	public void setWikiUrl(String wikiUrl)
	{
		this.wikiUrl = wikiUrl;
	}

	private String wikiImageUrl;

	public String getWikiImageUrl()
	{
		return wikiImageUrl;
	}

	public void setWikiImageUrl(String wikiImageUrl)
	{
		this.wikiImageUrl = wikiImageUrl;
	}

	private String stream;

	public String getStream()
	{
		return stream;
	}

	public void setStream(String stream)
	{
		this.stream = stream;
	}

	private String twitterHandle;

	public String getTwitterHandle()
	{
		return twitterHandle;
	}

	public void setTwitterHandle(String twitterHandle)
	{
		this.twitterHandle = twitterHandle;
	}

	private String localImage;

	public String getLocalImage()
	{
		return localImage;
	}

	public void setLocalImage(String localImage)
	{
		this.localImage = localImage;
	}

	private String country;

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	private String fanPage;

	public String getFanPage()
	{
		return fanPage;
	}

	public void setFanPage(String fanPage)
	{
		this.fanPage = fanPage;
	}

	private String birthDay;

	public String getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(String birthDay)
	{
		this.birthDay = birthDay;
	}

	private Timestamp lastEditDate;

	public Timestamp getLastEditDate()
	{
		return lastEditDate;
	}

	public void setLastEditDate(Timestamp lastEditDate)
	{
		this.lastEditDate = lastEditDate;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProgamerEntity that = (ProgamerEntity) o;

		if (altIds != null ? !altIds.equals(that.altIds) : that.altIds != null) return false;
		if (birthDay != null ? !birthDay.equals(that.birthDay) : that.birthDay != null) return false;
		if (country != null ? !country.equals(that.country) : that.country != null) return false;
		if (enFullName != null ? !enFullName.equals(that.enFullName) : that.enFullName != null) return false;
		if (fanPage != null ? !fanPage.equals(that.fanPage) : that.fanPage != null) return false;
		if (gameName != null ? !gameName.equals(that.gameName) : that.gameName != null) return false;
		if (localImage != null ? !localImage.equals(that.localImage) : that.localImage != null) return false;
		if (nativeFullName != null ? !nativeFullName.equals(that.nativeFullName) : that.nativeFullName != null)
			return false;
		if (race != null ? !race.equals(that.race) : that.race != null) return false;
		if (stream != null ? !stream.equals(that.stream) : that.stream != null) return false;
		if (team != null ? !team.equals(that.team) : that.team != null) return false;
		if (twitterHandle != null ? !twitterHandle.equals(that.twitterHandle) : that.twitterHandle != null)
			return false;
		if (wikiImageUrl != null ? !wikiImageUrl.equals(that.wikiImageUrl) : that.wikiImageUrl != null) return false;
		if (wikiUrl != null ? !wikiUrl.equals(that.wikiUrl) : that.wikiUrl != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = progamerId;
		result = 31 * result + (gameName != null ? gameName.hashCode() : 0);
		result = 31 * result + (enFullName != null ? enFullName.hashCode() : 0);
		result = 31 * result + (nativeFullName != null ? nativeFullName.hashCode() : 0);
		result = 31 * result + (team != null ? team.hashCode() : 0);
		result = 31 * result + (altIds != null ? altIds.hashCode() : 0);
		result = 31 * result + (race != null ? race.hashCode() : 0);
		result = 31 * result + (wikiUrl != null ? wikiUrl.hashCode() : 0);
		result = 31 * result + (wikiImageUrl != null ? wikiImageUrl.hashCode() : 0);
		result = 31 * result + (stream != null ? stream.hashCode() : 0);
		result = 31 * result + (twitterHandle != null ? twitterHandle.hashCode() : 0);
		result = 31 * result + (localImage != null ? localImage.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (fanPage != null ? fanPage.hashCode() : 0);
		result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
		return result;
	}
}
