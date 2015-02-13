package com.sc2geeks.front;

import api.sc2geeks.entity.RefinementField;
import api.sc2geeks.entity.RefinementInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/31/12
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class RefinementInfoProvider implements IRefinementInfoProvider
{
	List<RefinementInfo> refinementInfoList;
	Map<RefinementField, RefinementInfo> refinementInfoMap;

	public RefinementInfoProvider(){}
	public RefinementInfoProvider(List<RefinementInfo> refinementInfoList)
	{
		this.refinementInfoList = refinementInfoList;
	}

	public void setRefinementInfoList(List<RefinementInfo> refinementInfoList)
	{
		this.refinementInfoList = refinementInfoList;
		parse();
	}

	private void parse()
	{
		if (refinementInfoList == null || refinementInfoList.size() == 0)
		{
			refinementInfoMap = null;
			return;
		}

		refinementInfoMap = new HashMap<RefinementField, RefinementInfo>();
		for (RefinementInfo info : refinementInfoList)
		{
			refinementInfoMap.put(info.getRefinementField(), info);
		}
	}

	public RefinementInfo getRefinementInfo(RefinementField field)
	{
		if (refinementInfoMap == null || !refinementInfoMap.containsKey(field))
			return null;

		return new RefinementInfo(refinementInfoMap.get(field));
	}

	@Override
	public Iterator<RefinementInfo> iterator()
	{
		return refinementInfoList.iterator();
	}
}
