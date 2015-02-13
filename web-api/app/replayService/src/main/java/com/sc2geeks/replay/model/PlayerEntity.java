package com.sc2geeks.replay.model;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerEntity
{
	private int playerId;

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	private String playerName;

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	private String bnetId;

	public String getBnetId()
	{
		return bnetId;
	}

	public void setBnetId(String bnetId)
	{
		this.bnetId = bnetId;
	}

	private String bnetSubId;

	public String getBnetSubId()
	{
		return bnetSubId;
	}

	public void setBnetSubId(String bnetSubId)
	{
		this.bnetSubId = bnetSubId;
	}

	private String profileUrl;

	public String getProfileUrl()
	{
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl)
	{
		this.profileUrl = profileUrl;
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

	private String region;

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	private String gwCode;

	public String getGwCode()
	{
		return gwCode;
	}

	public void setGwCode(String gwCode)
	{
		this.gwCode = gwCode;
	}

	private Integer progamerId;

	public Integer getProgamerId()
	{
		return progamerId;
	}

	public void setProgamerId(Integer progamerId)
	{
		this.progamerId = progamerId;
	}

	private boolean status = true;

	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
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

	public PlayerEntity()
	{
	}

	public PlayerEntity(String bnetId, String bnetSubId, String gateway, String gwCode, String region, String profileUrl, String playerName)
	{
		this.gwCode = gwCode;
		this.region = region;
		this.gateway = gateway;
		this.profileUrl = profileUrl;
		this.bnetSubId = bnetSubId;
		this.bnetId = bnetId;
		this.playerName = playerName;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PlayerEntity that = (PlayerEntity) o;

		if (playerId != that.playerId) return false;
		if (progamerId != that.progamerId) return false;
		if (status != that.status) return false;
		if (bnetId != null ? !bnetId.equals(that.bnetId) : that.bnetId != null) return false;
		if (bnetSubId != null ? !bnetSubId.equals(that.bnetSubId) : that.bnetSubId != null) return false;
		if (gateway != null ? !gateway.equals(that.gateway) : that.gateway != null) return false;
		if (gwCode != null ? !gwCode.equals(that.gwCode) : that.gwCode != null) return false;
		if (playerName != null ? !playerName.equals(that.playerName) : that.playerName != null) return false;
		if (profileUrl != null ? !profileUrl.equals(that.profileUrl) : that.profileUrl != null) return false;
		if (region != null ? !region.equals(that.region) : that.region != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = playerId;
		result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
		result = 31 * result + (bnetId != null ? bnetId.hashCode() : 0);
		result = 31 * result + (bnetSubId != null ? bnetSubId.hashCode() : 0);
		result = 31 * result + (profileUrl != null ? profileUrl.hashCode() : 0);
		result = 31 * result + (gateway != null ? gateway.hashCode() : 0);
		result = 31 * result + (region != null ? region.hashCode() : 0);
		result = 31 * result + (gwCode != null ? gwCode.hashCode() : 0);
		result = 31 * result + progamerId;
		result = 31 * result + (status ? 1 : 0);
		return result;
	}
}
