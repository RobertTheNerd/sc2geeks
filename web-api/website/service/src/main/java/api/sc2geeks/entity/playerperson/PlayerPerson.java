package api.sc2geeks.entity.playerperson;

import api.sc2geeks.entity.EntityWithImageInfo;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/29/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPerson extends EntityWithImageInfo
{
	private int personId;

	public int getPersonId()
	{
		return personId;
	}

	private String gameId;

	public String getGameId()
	{
		return gameId;
	}

	public void setGameId(String gameId)
	{
		this.gameId = gameId;
	}

	public void setPersonId(int personId)
	{
		this.personId = personId;
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

	private String country;

	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}

	private String birthday;

	public String getBirthday()
	{
		return birthday;
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
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

	public void setTwitterHandle(String twitterHandle)
	{
		this.twitterHandle = twitterHandle;
	}

	private Timestamp inDate;

	public Timestamp getInDate()
	{
		return inDate;
	}

	public void setInDate(Timestamp inDate)
	{
		this.inDate = inDate;
	}

	private String inUser;

	public String getInUser()
	{
		return inUser;
	}

	public void setInUser(String inUser)
	{
		this.inUser = inUser;
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

	private String lastEditUser;

	public String getLastEditUser()
	{
		return lastEditUser;
	}

	public void setLastEditUser(String lastEditUser)
	{
		this.lastEditUser = lastEditUser;
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

	private String localImageName;

	public String getLocalImageName()
	{
		return localImageName;
	}

	public void setLocalImageName(String localImageName)
	{
		this.localImageName = localImageName;
	}

	private int ReplayCount;

	public int getReplayCount()
	{
		return ReplayCount;
	}

	public void setReplayCount(int replayCount)
	{
		ReplayCount = replayCount;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PlayerPerson that = (PlayerPerson) o;

		// if (personId != that.personId) return false;
		if (gameId != null ? !gameId.equals(that.gameId) : that.gameId!= null) return false;
		if (altIds != null ? !altIds.equals(that.altIds) : that.altIds != null) return false;
		if (enFullName != null ? !enFullName.equals(that.enFullName) : that.enFullName != null) return false;
		if (wikiImageUrl != null ? !wikiImageUrl.equals(that.wikiImageUrl) : that.wikiImageUrl != null) return false;
		// if (inDate != null ? !inDate.equals(that.inDate) : that.inDate != null) return false;
		// if (inUser != null ? !inUser.equals(that.inUser) : that.inUser != null) return false;
		// if (lastEditDate != null ? !lastEditDate.equals(that.lastEditDate) : that.lastEditDate != null) return false;
		// if (lastEditUser != null ? !lastEditUser.equals(that.lastEditUser) : that.lastEditUser != null) return false;
		if (nativeFullName != null ? !nativeFullName.equals(that.nativeFullName) : that.nativeFullName != null)
			return false;
		if (race != null ? !race.equals(that.race) : that.race != null) return false;
		if (stream != null ? !stream.equals(that.stream) : that.stream != null) return false;
		if (team != null ? !team.equals(that.team) : that.team != null) return false;
		if (twitterHandle != null ? !twitterHandle.equals(that.twitterHandle) : that.twitterHandle != null)
			return false;
		if (wikiUrl != null ? !wikiUrl.equals(that.wikiUrl) : that.wikiUrl != null) return false;
		if (fanPage != null ? !fanPage.equals(that.fanPage) : that.fanPage != null) return false;
		if (country!= null ? !country.equals(that.country) : that.country != null) return false;
		if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;


		return true;
	}

	@Override
	public int hashCode()
	{
		int result = personId;
		result = 31 * result + (enFullName != null ? enFullName.hashCode() : 0);
		result = 31 * result + (nativeFullName != null ? nativeFullName.hashCode() : 0);
		result = 31 * result + (team != null ? team.hashCode() : 0);
		result = 31 * result + (altIds != null ? altIds.hashCode() : 0);
		result = 31 * result + (race != null ? race.hashCode() : 0);
		result = 31 * result + (wikiUrl != null ? wikiUrl.hashCode() : 0);
		result = 31 * result + (stream != null ? stream.hashCode() : 0);
		result = 31 * result + (twitterHandle != null ? twitterHandle.hashCode() : 0);
		result = 31 * result + (inDate != null ? inDate.hashCode() : 0);
		result = 31 * result + (inUser != null ? inUser.hashCode() : 0);
		result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
		result = 31 * result + (lastEditUser != null ? lastEditUser.hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return "id" + personId + ", FullName:" + enFullName + ", nativeFullName:" + nativeFullName;
	}
}
