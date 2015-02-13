package com.sc2geeks.replay.model;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameteamEntity
{
	private int gameTeamId;

	public int getGameTeamId()
	{
		return gameTeamId;
	}

	public void setGameTeamId(int gameTeamId)
	{
		this.gameTeamId = gameTeamId;
	}

	private int gameId;

	public int getGameId()
	{
		return gameId;
	}

	public void setGameId(int gameId)
	{
		this.gameId = gameId;
	}

	private int teamId;

	public int getTeamId()
	{
		return teamId;
	}

	public void setTeamId(int teamId)
	{
		this.teamId = teamId;
	}

	private String type;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	private String startRace;

	public String getStartRace()
	{
		return startRace;
	}

	public void setStartRace(String startRace)
	{
		this.startRace = startRace;
	}

	private String finalRace;

	public String getFinalRace()
	{
		return finalRace;
	}

	public void setFinalRace(String finalRace)
	{
		this.finalRace = finalRace;
	}

	private boolean isWinner;

	public boolean getIsWinner()
	{
		return isWinner;
	}

	public void setIsWinner(boolean winner)
	{
		isWinner = winner;
	}

	private int apm;

	public int getApm()
	{
		return apm;
	}

	public void setApm(int apm)
	{
		this.apm = apm;
	}

	private int eApm;

	public int geteApm()
	{
		return eApm;
	}

	public void seteApm(int eApm)
	{
		this.eApm = eApm;
	}

	private String colorName;

	public String getColorName()
	{
		return colorName;
	}

	public void setColorName(String colorName)
	{
		this.colorName = colorName;
	}

	private short colorR;

	public short getColorR()
	{
		return colorR;
	}

	public void setColorR(short colorR)
	{
		this.colorR = colorR;
	}

	private short colorG;

	public short getColorG()
	{
		return colorG;
	}

	public void setColorG(short colorG)
	{
		this.colorG = colorG;
	}

	private short colorB;

	public short getColorB()
	{
		return colorB;
	}

	public void setColorB(short colorB)
	{
		this.colorB = colorB;
	}

	private PlayerEntity player;

	public PlayerEntity getPlayer()
	{
		return player;
	}

	public void setPlayer(PlayerEntity player)
	{
		this.player = player;
	}

	public GameteamEntity()
	{
	}

	public GameteamEntity(int teamId, String type, String startRace, String finalRace, boolean winner, int apm, int eApm, String colorName, short colorR, short colorG, short colorB, PlayerEntity player)
	{
		this.teamId = teamId;
		this.type = type;
		this.startRace = startRace;
		this.finalRace = finalRace;
		isWinner = winner;
		this.apm = apm;
		this.eApm = eApm;
		this.colorName = colorName;
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.player = player;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameteamEntity that = (GameteamEntity) o;

		if (apm != that.apm) return false;
		if (colorB != that.colorB) return false;
		if (colorG != that.colorG) return false;
		if (colorR != that.colorR) return false;
		if (eApm != that.eApm) return false;
		if (gameId != that.gameId) return false;
		if (gameTeamId != that.gameTeamId) return false;
		if (teamId != that.teamId) return false;
		if (colorName != null ? !colorName.equals(that.colorName) : that.colorName != null) return false;
		if (finalRace != null ? !finalRace.equals(that.finalRace) : that.finalRace != null) return false;
		if (startRace != null ? !startRace.equals(that.startRace) : that.startRace != null) return false;
		if (type != null ? !type.equals(that.type) : that.type != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = gameTeamId;
		result = 31 * result + gameId;
		result = 31 * result + teamId;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (startRace != null ? startRace.hashCode() : 0);
		result = 31 * result + (finalRace != null ? finalRace.hashCode() : 0);
		result = 31 * result + apm;
		result = 31 * result + eApm;
		result = 31 * result + (colorName != null ? colorName.hashCode() : 0);
		result = 31 * result + (int) colorR;
		result = 31 * result + (int) colorG;
		result = 31 * result + (int) colorB;
		return result;
	}
}
