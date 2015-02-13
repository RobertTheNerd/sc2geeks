package com.sc2geeks.replayUtility;

import org.apache.log4j.PropertyConfigurator;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/21/12
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class StarterBase
{
	static
	{
		PropertyConfigurator.configure(StarterBase.class.getResource("/log4j.properties"));
	}
}
