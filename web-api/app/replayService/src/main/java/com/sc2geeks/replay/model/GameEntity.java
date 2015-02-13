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
public class GameEntity
{
	private int gameId;

	public int getGameId()
	{
		return gameId;
	}

	public void setGameId(int gameId)
	{
		this.gameId = gameId;
	}

	private String version;

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	private Timestamp gameDate;

	public Timestamp getGameDate()
	{
		return gameDate;
	}

	public void setGameDate(Timestamp gameDate)
	{
		this.gameDate = gameDate;
	}

	private String replayFile;

	public String getReplayFile()
	{
		return replayFile;
	}

	public void setReplayFile(String replayFile)
	{
		this.replayFile = replayFile;
	}

	private String downloadedReplayFileName;

	public String getDownloadedReplayFileName()
	{
		return downloadedReplayFileName;
	}

	public void setDownloadedReplayFileName(String downloadedReplayFileName)
	{
		this.downloadedReplayFileName = downloadedReplayFileName;
	}

	private Integer winnerTeam;

	public Integer getWinnerTeam()
	{
		return winnerTeam;
	}

	public void setWinnerTeam(Integer winnerTeam)
	{
		this.winnerTeam = winnerTeam;
	}

	private String format;

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	private String event;

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}

	private String gameDescription;

	public String getGameDescription()
	{
		return gameDescription;
	}

	public void setGameDescription(String gameDescription)
	{
		this.gameDescription = gameDescription;
	}

	private Integer duration;

	/**
	 * in game seconds, not real life seconds.
	 * @return
	 */
	public Integer getDuration()
	{
		return duration;
	}

	public void setDuration(Integer duration)
	{
		this.duration = duration;
	}

	private Integer downloads;

	public Integer getDownloads()
	{
		return downloads == null ? 0 : downloads;
	}

	public void setDownloads(Integer downloads)
	{
		this.downloads = downloads;
	}

	private String externalId;

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	private String externalSource;

	public String getExternalSource()
	{
		return externalSource;
	}

	public void setExternalSource(String externalSource)
	{
		this.externalSource = externalSource;
	}

	private String externalRepFile;

	public String getExternalRepFile()
	{
		return externalRepFile;
	}

	public void setExternalRepFile(String externalRepFile)
	{
		this.externalRepFile = externalRepFile;
	}

	private String externalDescription;

	public String getExternalDescription()
	{
		return externalDescription;
	}

	public void setExternalDescription(String externalDescription)
	{
		this.externalDescription = externalDescription;
	}

	private String gameUrl;

	public String getGameUrl()
	{
		return gameUrl;
	}

	public void setGameUrl(String gameUrl)
	{
		this.gameUrl = gameUrl;
	}

	private String timeZone;

	public String getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}

	private String speed;

	public String getSpeed()
	{
		return speed;
	}

	public void setSpeed(String speed)
	{
		this.speed = speed;
	}

	private String gameType;

	public String getGameType()
	{
		return gameType;
	}

	public void setGameType(String gameType)
	{
		this.gameType = gameType;
	}

	private String status;

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	private String gateway;

	public String getGateway()
	{
		return gateway;
	}

	public void setGateway(String gateway)
	{
		this.gateway = gateway;
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

	private Timestamp lastEditDate;

	public Timestamp getLastEditDate()
	{
		return lastEditDate;
	}

	public void setLastEditDate(Timestamp lastEditDate)
	{
		this.lastEditDate = lastEditDate;
	}

	private Set<GameteamEntity> gameTeams = new HashSet<GameteamEntity>(0);

	public Set<GameteamEntity> getGameTeams()
	{
		return gameTeams;
	}

	public void setGameTeams(Set<GameteamEntity> gameTeams)
	{
		this.gameTeams = gameTeams;
	}

	private MapEntity map;

	public MapEntity getMap()
	{
		return map;
	}

	public void setMap(MapEntity map)
	{
		this.map = map;
	}

	private Integer crawlTimes;

	public Integer getCrawlTimes()
	{
		return crawlTimes == null ? 0 : crawlTimes;
	}

	public void setCrawlTimes(Integer crawlTimes)
	{
		this.crawlTimes = crawlTimes;
	}

	private Integer downloadTimes;

	public Integer getDownloadTimes()
	{
		return downloadTimes == null ? 0 : downloadTimes;
	}

	public void setDownloadTimes(Integer downloadTimes)
	{
		this.downloadTimes = downloadTimes;
	}

	private Integer parseTimes;

	public Integer getParseTimes()
	{
		return parseTimes == null ? 0 : parseTimes;
	}

	public void setParseTimes(Integer parseTimes)
	{
		this.parseTimes = parseTimes;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameEntity that = (GameEntity) o;

		if (downloads != that.downloads) return false;
		if (duration != that.duration) return false;
		if (gameId != that.gameId) return false;
		if (winnerTeam != that.winnerTeam) return false;
		if (downloadedReplayFileName != null ? !downloadedReplayFileName.equals(that.downloadedReplayFileName) : that.downloadedReplayFileName != null)
			return false;
		if (event != null ? !event.equals(that.event) : that.event != null) return false;
		if (externalDescription != null ? !externalDescription.equals(that.externalDescription) : that.externalDescription != null)
			return false;
		if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
		if (externalRepFile != null ? !externalRepFile.equals(that.externalRepFile) : that.externalRepFile != null)
			return false;
		if (externalSource != null ? !externalSource.equals(that.externalSource) : that.externalSource != null)
			return false;
		if (format != null ? !format.equals(that.format) : that.format != null) return false;
		if (gameDate != null ? !gameDate.equals(that.gameDate) : that.gameDate != null) return false;
		if (gameDescription != null ? !gameDescription.equals(that.gameDescription) : that.gameDescription != null)
			return false;
		if (gameType != null ? !gameType.equals(that.gameType) : that.gameType != null) return false;
		if (gameUrl != null ? !gameUrl.equals(that.gameUrl) : that.gameUrl != null) return false;
		if (gateway != null ? !gateway.equals(that.gateway) : that.gateway != null) return false;
		if (lastEditDate != null ? !lastEditDate.equals(that.lastEditDate) : that.lastEditDate != null) return false;
		if (lastEditUser != null ? !lastEditUser.equals(that.lastEditUser) : that.lastEditUser != null) return false;
		if (replayFile != null ? !replayFile.equals(that.replayFile) : that.replayFile != null) return false;
		if (speed != null ? !speed.equals(that.speed) : that.speed != null) return false;
		if (status != null ? !status.equals(that.status) : that.status != null) return false;
		if (timeZone != null ? !timeZone.equals(that.timeZone) : that.timeZone != null) return false;
		if (version != null ? !version.equals(that.version) : that.version != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = gameId;
		result = 31 * result + (version != null ? version.hashCode() : 0);
		result = 31 * result + (gameDate != null ? gameDate.hashCode() : 0);
		result = 31 * result + (replayFile != null ? replayFile.hashCode() : 0);
		result = 31 * result + (downloadedReplayFileName != null ? downloadedReplayFileName.hashCode() : 0);
		result = 31 * result + winnerTeam;
		result = 31 * result + (format != null ? format.hashCode() : 0);
		result = 31 * result + (event != null ? event.hashCode() : 0);
		result = 31 * result + (gameDescription != null ? gameDescription.hashCode() : 0);
		result = 31 * result + duration;
		result = 31 * result + downloads;
		result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
		result = 31 * result + (externalSource != null ? externalSource.hashCode() : 0);
		result = 31 * result + (externalRepFile != null ? externalRepFile.hashCode() : 0);
		result = 31 * result + (externalDescription != null ? externalDescription.hashCode() : 0);
		result = 31 * result + (gameUrl != null ? gameUrl.hashCode() : 0);
		result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
		result = 31 * result + (speed != null ? speed.hashCode() : 0);
		result = 31 * result + (gameType != null ? gameType.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (gateway != null ? gateway.hashCode() : 0);
		result = 31 * result + (lastEditUser != null ? lastEditUser.hashCode() : 0);
		result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
		return result;
	}
}
