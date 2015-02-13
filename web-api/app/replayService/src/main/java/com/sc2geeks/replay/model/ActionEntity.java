package com.sc2geeks.replay.model;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/19/12
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionEntity
{
	private int actionId;

	public int getActionId()
	{
		return actionId;
	}

	public void setActionId(int actionId)
	{
		this.actionId = actionId;
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

	private int playerId;

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	private int frame;

	public int getFrame()
	{
		return frame;
	}

	public void setFrame(int frame)
	{
		this.frame = frame;
	}

	private int playerIndex;

	public int getPlayerIndex()
	{
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex)
	{
		this.playerIndex = playerIndex;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ActionEntity that = (ActionEntity) o;

		if (actionId != that.actionId) return false;
		if (frame != that.frame) return false;
		if (gameId != that.gameId) return false;
		if (playerId != that.playerId) return false;
		if (playerIndex != that.playerIndex) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = actionId;
		result = 31 * result + gameId;
		result = 31 * result + playerId;
		result = 31 * result + frame;
		result = 31 * result + playerIndex;
		return result;
	}

	private int actionUnitId;

	public int getActionUnitId()
	{
		return actionUnitId;
	}

	public void setActionUnitId(int actionUnitId)
	{
		this.actionUnitId = actionUnitId;
	}
}
