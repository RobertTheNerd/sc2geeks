package com.sc2geeks.front.action;

import com.sc2geeks.front.ui.PageUrlBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/31/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebsiteRootAction extends ActionBase
{
	@Override
	protected String doExecute()
	{
		setValue("statusCode", 302);
		setValue("redirectUrl", PageUrlBuilder.getReplayHomePage());
		return "redirect";
	}
}
