package com.sc2geeks.replayUtility.parser;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.GameEntity;
import com.sc2geeks.replay.parser.ReplayParser;
import com.sc2geeks.replayUtility.UserInfo;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/8/11
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseWorker
{
	private static Logger logger = Logger.getLogger(ParseWorker.class);

	private static ReplayParserConfig parserConfig = ReplayParserConfig.getInstance();

	public static void start() throws Exception
	{
		List<GameEntity> gameList;

		// failed games
		logger.info("Processing failed games.");
		gameList = ReplayDAO.getFailedParseGames(parserConfig.getMaxFailedRetries());
		if (gameList != null && gameList.size() > 0)
		{
			for (GameEntity game : gameList)
			{
				parseAndRenameReplay(game);
			}
		}
		logger.info("Processing failed games - done!");

		logger.info("Processing downloaded games.");
		while ((gameList = ReplayDAO.getUnparsedGames(parserConfig.getBatchSize())) != null
				&& gameList.size() > 0)
		{
			for (GameEntity game : gameList)
			{
				logger.info("Parsing replay for game: " + game.getGameId());
				parseAndRenameReplay(game);
			}
		}
		logger.info("Processing downloaded games - done!");

	}

	private static boolean parseAndRenameReplay(GameEntity game)
	{
		boolean result = false;
		if (!ReplayParser.parse(game, parserConfig.getDownloadedReplayFolder(), parserConfig.getParseXmlFolder()))
		{
			game.setStatus("PF");
			logger.info("Parsing failed.");
		}
		else
		{
			logger.info("Parsed.");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String newFileShort = "sc2geeks_" + format.format(game.getGameDate()) + "_" + game.getGameId() + ".sc2Replay";
			String newFileFull = parserConfig.getUploadReplayFolder() + newFileShort;

			String oldFile = parserConfig.getDownloadedReplayFolder() + game.getDownloadedReplayFileName();
			if (copyFile(oldFile, newFileFull))
			{
				game.setReplayFile(newFileShort);
				game.setStatus("R");
				result = true;
				logger.info("Replay file renamed to " + newFileShort);
			} else
			{
				// no more auto-process to handle this status
				game.setStatus("RF");
				result = false;
				logger.info("Replay file rename failed.");
			}
		}
		game.setParseTimes(game.getParseTimes() + 1);
		game.setLastEditUser(UserInfo.getCurrentUserName());
		game.setLastEditDate(new Timestamp(new Date().getTime()));
		ReplayDAO.saveOrUpdateObject(game);
		return result;
	}

	private static boolean copyFile(String oldName, String newName)
	{
		// todo
		File oldFile = new File(oldName);
		File newFile = new File(newName);

		if (!oldFile.exists())
		{
			logger.error("Failed to copy non-existing file: " + oldName);
			return false;
		}
		try
		{
			FileUtils.copyFile(oldFile, newFile, false);
		} catch (Exception e)
		{
			logger.error("Failed to copy file from  " + oldName + " to " + newName, e);
			return false;
		}

		if (newFile.exists())
		{
			return true;
		} else
		{
			return false;
		}
	}
}
