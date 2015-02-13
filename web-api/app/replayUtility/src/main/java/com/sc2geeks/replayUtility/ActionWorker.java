package com.sc2geeks.replayUtility;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.GameEntity;
import com.sc2geeks.replay.parser.ReplayParser;
import com.sc2geeks.replayUtility.parser.ReplayParserConfig;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/19/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionWorker extends StarterBase
{
	static Logger logger = Logger.getLogger(MainWorker.class);
	static
	{
		SpringConfigProvider.setSpringConfigFile("config.xml");
		parserConfig = ReplayParserConfig.getInstance();
	}
	private static ReplayParserConfig parserConfig;

	public static void main(String[] args)
	{
		if (args == null || args.length == 0)
		{
			parseActions();
			return;
		}

		String arg = args[0];
		if (arg.compareToIgnoreCase("--ActionSummary") == 0)
		{
			parseActionSummary();
		}
	}

	private static void parseActions()
	{
		// get replays
		logger.info("Getting replays.");
		List<GameEntity> activeGames = ReplayDAO.getActiveGames();
		for (GameEntity game : activeGames)
		{
			logger.info("Parsing replay " + game.getGameId());
			ReplayParser.parse(game, parserConfig.getDownloadedReplayFolder(),
					parserConfig.getParseXmlFolder(), false, false);
		}
		logger.info("All done!");
	}
	private static void parseActionSummary()
	{
		// get replays
		logger.info("Getting replays.");
		List<GameEntity> activeGames = ReplayDAO.getActiveGames();
		for (GameEntity game : activeGames)
		{
			logger.info("Calculating action summary " + game.getGameId());
			ReplayDAO.calculateActionSummary(game.getGameId());
		}
		logger.info("All done!");
	}
}
