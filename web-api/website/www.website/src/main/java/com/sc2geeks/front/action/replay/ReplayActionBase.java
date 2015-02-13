package com.sc2geeks.front.action.replay;

import com.sc2geeks.front.action.ActionBase;
import com.sc2geeks.front.ui.PageTabManager;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 10/13/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReplayActionBase extends ActionBase
{
	protected String getPageTab()
	{
		return PageTabManager.Tab_Replay;
	}
}
