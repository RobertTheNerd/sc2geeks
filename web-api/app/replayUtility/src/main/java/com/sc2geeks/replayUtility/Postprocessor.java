package com.sc2geeks.replayUtility;

import com.sc2geeks.replay.dao.ReplayDAO;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/4/11
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class Postprocessor
{
	static Logger logger = Logger.getLogger(Postprocessor.class);

	public static void start()
	{
		mergeDuplicateReplays();
	}

	private static void mergeDuplicateReplays()
	{
		logger.info("Merging duplicate replays...");
		ReplayDAO.mergeDuplicateGames();
		logger.info("Done.");

		logger.info("Processing replay series...");
		ReplayDAO.processSeries();
		logger.info("Done.");
	}
}
