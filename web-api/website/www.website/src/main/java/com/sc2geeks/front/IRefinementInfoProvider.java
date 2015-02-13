package com.sc2geeks.front;

import api.sc2geeks.entity.RefinementField;
import api.sc2geeks.entity.RefinementInfo;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/31/12
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IRefinementInfoProvider extends Iterable<RefinementInfo>
{
	RefinementInfo getRefinementInfo(RefinementField field);
}
