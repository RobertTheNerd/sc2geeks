package com.sc2geeks.replay.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/21/12
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionConfigUnit
{
	String actionName;
	ActionConfig.ActionType actionType;
	String race;
	int id;

	public static ActionConfigUnit fromString(String line, String delimiter)
	{
		if (StringUtils.isBlank(line))
			return null;

		String[] parts = line.split(delimiter);
		if (parts == null || parts.length != 4)
			return null;

		try
		{
			ActionConfigUnit unit = new ActionConfigUnit();
			unit.actionName = parts[0];
			unit.race = parts[2];
			unit.actionType = ActionConfig.ActionType.valueOf(parts[1]);
			unit.id = Integer.valueOf(parts[3]);
			return unit;
		} catch (Exception e)
		{
			return null;
		}
	}

	public ActionConfigUnit(){}
	public ActionConfigUnit(ActionConfigUnit unit)
	{
		this.actionName = unit.actionName;
		this.actionType = unit.actionType;
		this.race = unit.race;
		this.id = unit.id;
	}

	public String toString()
	{
		return actionName + "\t" + actionType + "\t" + race + "\t" + id;
	}

	public String getActionName()
	{
		return actionName;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	public ActionConfig.ActionType getActionType()
	{
		return actionType;
	}

	public void setActionType(ActionConfig.ActionType actionType)
	{
		this.actionType = actionType;
	}

	public String getRace()
	{
		return race;
	}

	public void setRace(String race)
	{
		this.race = race;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
