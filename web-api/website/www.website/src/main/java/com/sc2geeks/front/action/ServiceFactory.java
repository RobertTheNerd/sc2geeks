package com.sc2geeks.front.action;

import api.sc2geeks.client.PlayerPersonManagerProxy;
import api.sc2geeks.client.ReplayManagerProxy;
import api.sc2geeks.service.PlayerPersonManager;
import api.sc2geeks.service.ReplayManager;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/21/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceFactory
{
	public static ReplayManager createReplayManager()
	{
		return new ReplayManagerProxy();
	}

	public static PlayerPersonManager createPlayerPersonManager()
	{
		return new PlayerPersonManagerProxy();
	}
}
