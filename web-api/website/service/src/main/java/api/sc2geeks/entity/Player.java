package api.sc2geeks.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player
{
	private int playerId;
	private String playerName;
	private String playerRace;
	private int apm;
	private boolean isRandom;
	private boolean isWinner;
	private int color;
	private String hexColorValue;
	private int progamerId = -1;
	private String progamerName;
	private int pid;

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public String getPlayerRace()
	{
		return playerRace;
	}

	public void setPlayerRace(String playerRace)
	{
		this.playerRace = playerRace;
	}

	public int getApm()
	{
		return apm;
	}

	public void setApm(int apm)
	{
		this.apm = apm;
	}

	public boolean isRandom()
	{
		return isRandom;
	}

	public void setRandom(boolean random)
	{
		isRandom = random;
	}

	public boolean isWinner()
	{
		return isWinner;
	}

	public void setWinner(boolean winner)
	{
		isWinner = winner;
	}

	public int getColor()
	{
		return color;
	}

	public int getProgamerId() {
		return progamerId;
	}

	public void setProgamerId(int progamerId) {
		this.progamerId = progamerId;
	}

	public String getProgamerName() {
		return progamerName;
	}

	public void setProgamerName(String progamerName) {
		this.progamerName = progamerName;
	}

	public String getDisplayName() {
		return StringUtils.isNotBlank(progamerName) ? progamerName : playerName;
	}

	public void setColor(int color)
	{
		this.color = color;
		hexColorValue = String.format("%1$02X%2$02X%3$02X", color >> 16, (color >> 8) & 0x00FF, color & 0x0000FF);
	}

	public String getHexColorValue()
	{
		return hexColorValue;
	}

	/**
	 * pid in replay used by event, usually 1 and 2
	 * @return
	 */
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
}
