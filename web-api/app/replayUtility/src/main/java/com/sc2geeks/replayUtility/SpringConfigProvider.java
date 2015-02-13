package com.sc2geeks.replayUtility;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/3/11
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public final class SpringConfigProvider {
	public static String getSpringConfigFile() {
		return _springConfigFile;
	}

	public static void setSpringConfigFile(String _springConfigFile) {
		SpringConfigProvider._springConfigFile = _springConfigFile;
	}

	private static String _springConfigFile;
}
