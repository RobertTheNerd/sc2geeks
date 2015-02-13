package com.sc2geeks.front.action.replay;

import api.sc2geeks.entity.BuildAbility;
import api.sc2geeks.entity.Player;
import api.sc2geeks.entity.PlayerTeam;
import api.sc2geeks.entity.ReplayWithAllInfo;
import api.sc2geeks.service.ReplayManager;
import com.sc2geeks.front.action.ServiceFactory;
import com.sc2geeks.front.ui.PageUrlBuilder;
import com.sc2geeks.front.ui.ResourceHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/6/12
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayDetailAction extends ReplayActionBase
{
	private static Logger logger = Logger.getLogger(ReplayDetailAction.class);

	private String replayId;
	private ReplayWithAllInfo replayWithAllInfo;

	@Override
	protected String doExecute()
	{
		ReplayManager replayManager = ServiceFactory.createReplayManager();
		parseReplayId();

		if (StringUtils.isNotBlank(replayId))
		{
			replayWithAllInfo = replayManager.getReplayWithAllInfo(replayId);
		}

		if (replayWithAllInfo == null || replayWithAllInfo.getReplay() == null)
		{
			logger.info("No replay found for request: " + request.getRequestURI());
			return NOT_FOUND;
		}

		// process old url with space
		if (requestContext.getActionPath().contains("%20"))
		{
			String newUrl = PageUrlBuilder.getReplayDetailPage(replayWithAllInfo.getReplay());
			setValue("redirectUrl", newUrl);
			setValue("statusCode", 301);
			return REDIRECT;
		}

		// download logic
		if (StringUtils.startsWithIgnoreCase(requestContext.getActionPath(), "/download/"))
		{
			String downloadUrl = ResourceHelper.buildReplayUrl(replayWithAllInfo.getReplay().getReplayFile());
			setValue("redirectUrl", downloadUrl);

			// save download log
			String fromIP = requestContext.getRemoteIP();
			replayManager.saveDownloadCounter(replayId, fromIP, new Date());

			setValue("statusCode", 302);
			return REDIRECT;
		}
		else
		{
			Hashtable<Integer, List<ImmutablePair<Integer, String>>> abilityEvents = parseReplayEvents(replayWithAllInfo);
			setValue("replayWithAllInfo", replayWithAllInfo);
			setValue("abilityEvents", abilityEvents);
			pageSEOInfo.setCanonicalUrl(PageUrlBuilder.getReplayDetailPage(replayWithAllInfo.getReplay()));
			return "detail";
		}

	}

	private void parseReplayId()
	{
		String actionName = requestContext.getActionPath();

		String[] parts = actionName.split("-");
		if (parts == null || parts.length == 0)
			return;

		replayId = parts[parts.length - 1];
	}

	private static Hashtable<Integer, List<ImmutablePair<Integer, String>>> parseReplayEvents(ReplayWithAllInfo replay) {
		try {
			Hashtable<Integer, List<ImmutablePair<Integer, String>>> abilityEvents = new Hashtable<>(2);

			for (PlayerTeam team : replay.getReplay().getPlayerTeams()) {
				for (Player player : team.getPlayers())
					abilityEvents.put(player.getPid(), new ArrayList<ImmutablePair<Integer, String>>());
			}

			for (BuildAbility ability : replay.getAbilityEvents()) {
				if (abilityEvents.containsKey(ability.getPid()))
					abilityEvents.get(ability.getPid()).add(new ImmutablePair<Integer, String>(ability.getFrame(), ability.getAbility()));
			}
			return abilityEvents;
		} catch(Exception e) {
			logger.error("Failed to parse replay events.", e);
			return null;
		}
	}
}
