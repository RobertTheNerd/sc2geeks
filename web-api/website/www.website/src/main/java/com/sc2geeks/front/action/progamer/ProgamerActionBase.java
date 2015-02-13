package com.sc2geeks.front.action.progamer;

import com.sc2geeks.front.action.ActionBase;
import com.sc2geeks.front.ui.PageTabManager;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 10/13/12
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProgamerActionBase extends ActionBase
{
	protected String getPageTab()
	{
		return PageTabManager.Tab_Progamer;
	}
}
