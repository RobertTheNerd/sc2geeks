package com.sc2geeks.replay.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/27/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpecialActionConfig
{

	List<SpecialActionUnit> specialActionConfigs;

	public List<SpecialActionUnit> getSpecialActionConfigs()
	{
		return specialActionConfigs;
	}

	public void setSpecialActionConfigs(List<SpecialActionUnit> specialActionConfigs)
	{
		this.specialActionConfigs = specialActionConfigs;
	}
}
