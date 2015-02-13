package com.sc2geeks.front;

import api.sc2geeks.entity.RefinementField;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/24/12
 * Time: 2:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class RefinementSetting
{
	RefinementField refinementField;
	String urlPrefix;
	String displayName;
	String legacyParameter;

	public RefinementField getRefinementField()
	{
		return refinementField;
	}

	public void setRefinementField(RefinementField refinementField)
	{
		this.refinementField = refinementField;
	}

	public String getUrlPrefix()
	{
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix)
	{
		this.urlPrefix = urlPrefix;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getLegacyParameter()
	{
		return legacyParameter;
	}

	public void setLegacyParameter(String legacyParameter)
	{
		this.legacyParameter = legacyParameter;
	}
}
