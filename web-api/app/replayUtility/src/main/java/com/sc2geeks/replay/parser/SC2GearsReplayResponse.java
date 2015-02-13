package com.sc2geeks.replay.parser;

import com.sc2geeks.replay.model.ActionEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("response")
class SC2GearsReplayResponse extends SC2GearsResponseBase
{
	@XStreamAsAttribute
	String docVer;


	SingleLineWithValue engineVer;
	ReplayInfo repInfo;
	ActionContainer actions;

	class SingleLineWithValue
	{
		@XStreamAsAttribute
		String value;

		int toInt()
		{
			return Integer.parseInt(value);
		}
		short toShort()
		{
			return Short.parseShort(value);
		}
		public String toString()
		{
			return value;
		}
	}

	class ReplayInfo
	{
		class GameLength
		{
			@XStreamAsAttribute
			String unit;

			@XStreamAsAttribute
			String value;

			@XStreamAsAttribute
			String gameTimeValue;
		}

		class ReplayTime extends SingleLineWithValue
		{
			@XStreamAsAttribute
			String pattern;
		}

		SingleLineWithValue version;

		@XStreamImplicit(itemFieldName="gameLength")
		List<GameLength> gameLengthList;

		SingleLineWithValue gameType;
		SingleLineWithValue gameSpeed;
		SingleLineWithValue format;
		SingleLineWithValue gateway;
		SingleLineWithValue mapFile;

		// ignore <clients count="2">

		SingleLineWithValue mapName;
		ReplayTime saveTime;
		SingleLineWithValue saveTimeZone;

		PlayerContainer players;
		public Timestamp getGameDate()
		{
			if (saveTime == null || StringUtils.isEmpty(saveTime.pattern) || StringUtils.isEmpty(saveTime.value))
				return null;
			try
			{
				Date saveDate = new SimpleDateFormat(saveTime.pattern).parse(saveTime.value);
				return new Timestamp(saveDate.getTime() - getRealSeconds() * 1000);
			}
			catch(Exception e)
			{

			}
			return null;
		}
		public long getRealSeconds()
		{
			if (gameLengthList == null)
				return 0;
			try
			{
			for (GameLength gameLength : gameLengthList)
			{
				if (gameLength.unit.compareToIgnoreCase("sec") == 0)
					return Long.parseLong(gameLength.value);
			}
			}catch(Exception e){}
			return 0;
		}
	}

	class PlayerContainer
	{
		class PlayerInfo
		{
			class BnetPlayer
			{
				@XStreamAsAttribute
				String bnetId;

				@XStreamAsAttribute
				String bnetSubid;

				@XStreamAsAttribute
				String gateway;

				@XStreamAsAttribute
				String gwCode;

				@XStreamAsAttribute
				String name;

				@XStreamAsAttribute
				String profileUrl;

				@XStreamAsAttribute
				String region;
			}
			public class Color
			{
				@XStreamAsAttribute
				short red;

				@XStreamAsAttribute
				short blue;

				@XStreamAsAttribute
				short green;

				@XStreamAsAttribute
				String name;
			}

			@XStreamAsAttribute
			int index;

			BnetPlayer playerId;
			SingleLineWithValue team;
			SingleLineWithValue race;
			SingleLineWithValue finalRace;
			Color color;
			SingleLineWithValue type;
			SingleLineWithValue difficulty;
			SingleLineWithValue handicap;
			SingleLineWithValue isWinner;
			SingleLineWithValue apm;
			SingleLineWithValue eapm;

			boolean isWinner()
			{
				return Boolean.parseBoolean(isWinner.value);
			}
			int getApm()
			{
				return apm.toInt();
			}
			int getEAmp()
			{
				return eapm.toInt();
			}
			int getTeamId()
			{
				return team.toInt();
			}
		}
		@XStreamAsAttribute
		int count;

		@XStreamImplicit(itemFieldName="player")
		List<PlayerInfo> playerList;
	}

	class ActionContainer
	{
		class ActionLine
		{
			@XStreamAsAttribute
			int f;

			@XStreamAsAttribute
			int p;

			@XStreamAsAttribute
			String s;

			@XStreamAsAttribute
			String t;

			public String getActionName()
			{
				if (StringUtils.isNotBlank(s))
				{
					int pos = s.indexOf(";");
					if (pos == -1)
						return s;
					else
						 return s.substring(0, pos);
				}
				return null;
			}

			ActionEntity toActionEntity()
			{
				ActionEntity action = new ActionEntity();
				action.setFrame(f);
				action.setPlayerIndex(p);
				return action;
			}
		}

		@XStreamAsAttribute
		int allActionsCount;

		@XStreamAsAttribute
		int count;

		@XStreamAsAttribute
		boolean errorParsing;

		@XStreamImplicit(itemFieldName="a")
		List<ActionLine> actionList;
	}

	public int getGameLength()
	{
		try
		{
		if (repInfo != null && repInfo.gameLengthList != null)
		{
			for (ReplayInfo.GameLength length : repInfo.gameLengthList)
			{
				if (length.unit.compareToIgnoreCase("sec") == 0)
					return Integer.parseInt(length.gameTimeValue);
			}
		}
		}
		catch(Exception e)
		{
			System.out.println("Failed to get game length:" + e);
		}
		return 0;
	}
}