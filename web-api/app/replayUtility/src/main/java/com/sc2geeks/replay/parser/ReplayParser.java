package com.sc2geeks.replay.parser;

import com.sc2geeks.commons.serializer.XmlSerializer;
import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayParser
{
	private static Logger logger = Logger.getLogger(ReplayParser.class);

	public static boolean parse(GameEntity game, String replayFolder, String parseXmlFolder,
	                            boolean parseMap, boolean parsePlayer)
	{
		if (game == null || StringUtils.isEmpty(game.getDownloadedReplayFileName()))
			return false;

		String repFileName = replayFolder + game.getDownloadedReplayFileName().trim();

		SC2GearsReplayResponse parseResponse = null;

		// first, get result from file
		String xmlResult = getParseXmlFromFile(game.getGameId(), parseXmlFolder);
		if (StringUtils.isNotEmpty(xmlResult))
		{
			parseResponse = XmlSerializer.deserializeFromString(xmlResult, SC2GearsReplayResponse.class);
		}

		// local file non existent or invalid file content, get result from remote api service
		if (parseResponse == null || parseResponse.resultLine == null || parseResponse.resultLine.code != 0)
		{
			xmlResult = SC2GearsParsingService.parseReplayFile(repFileName);
			if (StringUtils.isEmpty(xmlResult))
			{
				return false;
			}
			saveParseXml(game.getGameId(), parseXmlFolder, xmlResult);
		}

		parseResponse = XmlSerializer.deserializeFromString(xmlResult, SC2GearsReplayResponse.class);

		if (parseResponse == null)
			return false;

		return convertParseResponseToGame(parseResponse, game, parseMap, parsePlayer);
	}
	public static boolean parse(GameEntity game, String replayFolder, String parseXmlFolder)
	{
		return parse(game, replayFolder, parseXmlFolder, true, true);
	}

	private static boolean convertParseResponseToGame(SC2GearsReplayResponse response, GameEntity game,
	                                                  boolean parseMap, boolean parsePlayer)
	{
		// invalid response
		if (response.resultLine == null || response.resultLine.code != 0)
			return false;

		try
		{
			// transfer game info
			game.setDuration(response.getGameLength());
			game.setGameDate(response.repInfo.getGameDate());
			game.setFormat(response.repInfo.format.value);
			game.setGameType(response.repInfo.gameType.value);
			game.setGateway(response.repInfo.gateway.value);
			game.setSpeed(response.repInfo.gameSpeed.value);
			game.setTimeZone(response.repInfo.saveTimeZone.value);
			game.setVersion(response.repInfo.version.value);

			if (parseMap)
			{
				// game map
				MapEntity map = ReplayDAO.getMap(response.repInfo.mapFile.value, response.repInfo.mapName.value);
				if (map == null)
					map = MapParser.parseMap(response.repInfo.mapFile.value, response.repInfo.mapName.value);
				game.setMap(map);
			}

			// game team & player
			HashMap<Integer, PlayerEntity> playerTable = new HashMap<Integer, PlayerEntity>();
			if (parsePlayer)
				ReplayDAO.deleteGameteamByGame(game.getGameId());
			Set<GameteamEntity> gameteamSet = new HashSet<GameteamEntity>(response.repInfo.players.count);
			for (SC2GearsReplayResponse.PlayerContainer.PlayerInfo player : response.repInfo.players.playerList)
			{
				SC2GearsReplayResponse.PlayerContainer.PlayerInfo.BnetPlayer bplayer = player.playerId;
				PlayerEntity playerEntity = ReplayDAO.getPlayer(bplayer.bnetId, bplayer.gateway, bplayer.name, bplayer.profileUrl);
				if (playerEntity == null)
				{
					playerEntity = new PlayerEntity(bplayer.bnetId, bplayer.bnetSubid, bplayer.gateway, bplayer.gwCode, bplayer.region,
							bplayer.profileUrl, bplayer.name);
					playerEntity.setLastEditDate(new Timestamp(new Date().getTime()));
					if (parsePlayer)
						ReplayDAO.saveOrUpdateObject(playerEntity);
				}
				GameteamEntity gameteam = new GameteamEntity(player.getTeamId(), player.type.value, player.race.value, player.finalRace.value, player.isWinner(),
						player.getApm(), player.getEAmp(), player.color.name,
						player.color.red, player.color.green, player.color.blue, playerEntity);

				gameteam.setPlayer(playerEntity);
				gameteam.setGameId(game.getGameId());
				playerTable.put(player.index, playerEntity);

				// winning team
				if (player.isWinner())
					game.setWinnerTeam(player.getTeamId());

				if (parsePlayer)
					ReplayDAO.saveOrUpdateObject(gameteam);

				gameteamSet.add(gameteam);
			}
			game.setGameTeams(gameteamSet);

			// action items
			if (response.actions != null && response.actions.actionList != null && response.actions.actionList.size() > 0)
			{
				List<ActionEntity> actionList = new ArrayList<ActionEntity>();
				for (SC2GearsReplayResponse.ActionContainer.ActionLine line : response.actions.actionList)
				{

					String actionName = line.getActionName();
					if (StringUtils.isBlank(actionName))
						continue;

					// remove leading strings
					actionName = StringUtils.removeStart(actionName, "[Queued]").trim();
					actionName = StringUtils.removeStart(actionName, "[MinimapClick]").trim();

					if ("Right click".compareToIgnoreCase(actionName) == 0
							|| "Attack".compareToIgnoreCase(actionName) == 0
							|| "Stop".compareToIgnoreCase(actionName) == 0
							|| "Cancel".compareToIgnoreCase(actionName) == 0
							|| actionName.startsWith("[Failed]")
							|| actionName.startsWith("Hotkey Assign")
							|| actionName.startsWith("[WireframeClick]")
							|| actionName.startsWith("[Toggle]")
							)
						continue;

					ActionConfigUnit unit = ActionConfig.getActionByName(actionName);
					if (unit == null)
						continue;

					ActionEntity action = line.toActionEntity();
					action.setActionUnitId(unit.getId());
					action.setGameId(game.getGameId());
					PlayerEntity p = playerTable.get(action.getPlayerIndex());
					if (p != null)
						action.setPlayerId(playerTable.get(action.getPlayerIndex()).getPlayerId());

					actionList.add(action);
				}
				ReplayDAO.saveOrUpdateObjectList(actionList, false);
				ReplayDAO.calculateActionSummary(game.getGameId());
			}

			return true;
		} catch (Exception e)
		{
			logger.error("Failed to convert parse result to db object.", e);
			return false;
		}
	}

	private static String getParseXmlFromFile(int gameId, String parseXmlFolder)
	{
		String xmlFileName = parseXmlFolder + gameId + ".xml";
		File file = new File(xmlFileName);
		if (file.exists())
		{
			try
			{
				return FileUtils.readFileToString(file, "UTF-8");
			} catch (Exception e)
			{
				logger.warn("Failed to read xml parsing result. Game: " + gameId + ", xml file: " + xmlFileName, e);
			}
		}
		return null;
	}
	private static void saveParseXml(int gameId, String parseXmlFolder, String xmlResult)
	{
		String xmlFileName = parseXmlFolder + gameId + ".xml";
		File file = new File(xmlFileName);
		if (StringUtils.isNotEmpty(xmlResult))
		{
			try
			{
				File folder = new File(parseXmlFolder);
				if (!folder.exists())
				{
					logger.info("Creating xml file folder: " + folder.getAbsolutePath());
					folder.mkdirs();
				}
				logger.info("Writing parsing result to file: " + xmlFileName);
				FileUtils.writeStringToFile(file, xmlResult, "UTF-8");
			} catch (Exception e)
			{
				logger.warn("Failed to write parsing result to file: " + xmlFileName, e);
			}
		}

	}
}