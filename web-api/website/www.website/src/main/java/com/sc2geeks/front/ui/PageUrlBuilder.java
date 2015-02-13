package com.sc2geeks.front.ui;

import api.sc2geeks.entity.Player;
import api.sc2geeks.entity.RefinementField;
import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import com.sc2geeks.commons.web.UrlUTF8Encoder;
import com.sc2geeks.front.WebsiteConfig;
import gumi.builders.UrlBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/25/12
 * Time: 2:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PageUrlBuilder
{
	private static WebsiteConfig websiteConfig = WebsiteConfig.getInstance();

	private static HashMap<PageUrlAlias, String> pageUrlMap;
	private static HashMap<PageUrlAlias, PageUrlSetting> urlSettingMap;

	static
	{
		pageUrlMap = new HashMap<PageUrlAlias, String>(websiteConfig.getPageUrlAliasList().size());
		urlSettingMap = new HashMap<PageUrlAlias, PageUrlSetting>(websiteConfig.getPageUrlAliasList().size());
		for (PageUrlSetting setting : websiteConfig.getPageUrlAliasList())
		{
			String url = websiteConfig.getWebsiteRoot() + StringUtils.removeStart(setting.getUrl(), "/");
			pageUrlMap.put(setting.getAlias(), url);
			urlSettingMap.put(setting.getAlias(), setting);
		}
	}

	public static String getPage(PageUrlAlias alias)
	{
		return pageUrlMap.get(alias);
	}
	public static PageUrlSetting getPageUrlSetting(PageUrlAlias alias) {
		return urlSettingMap.get(alias);
	}

	public static String getReplayHomePage()
	{
		return buildIndexForPageAlias(PageUrlAlias.ReplayHome);
	}

	public static String getReplayDetailPage(Replay replay)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pageUrlMap.get(PageUrlAlias.ReplayDetail));
		sb.append(getReplayPageName(replay));
		return sb.toString();
	}

	public static String getReplayDetailPage_Old(Replay replay)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pageUrlMap.get(PageUrlAlias.ReplayDetail));
		sb.append(getReplayPageName_Old(replay));
		return sb.toString();
	}
	public static String getReplayDownloadPage(Replay replay)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pageUrlMap.get(PageUrlAlias.ReplayDownload));
		sb.append(getReplayPageName(replay));
		return sb.toString();
	}

	private static String getReplayPageName_Old(Replay replay)
	{
		return String.format("%1$s-vs-%2$s-%3$s-%4$s-%5$s.html", replay.getPlayerTeams().get(0).getPlayers().get(0).getPlayerName(),
						replay.getPlayerTeams().get(1).getPlayers().get(0).getPlayerName(),
						replay.getMap().getMapName(),
						replay.getMatchUp(),
						replay.getGameId());
	}
	private static String getReplayPageName(Replay replay)
	{
		return String.format("%1$s-vs-%2$s-%3$s-%4$s-%5$s.%6$s",
				UrlUTF8Encoder.encode(replay.getPlayerTeams().get(0).getPlayers().get(0).getPlayerName()),
				UrlUTF8Encoder.encode(replay.getPlayerTeams().get(1).getPlayers().get(0).getPlayerName()),
				UrlUTF8Encoder.encode(replay.getMap().getMapName()),
				replay.getMatchUp(),
				replay.getGameId(),
				websiteConfig.getDefaultExtension());
	}


	/*** replay search urls ***/
	public static String getReplaySearchPage()
	{
		return buildIndexForPageAlias(PageUrlAlias.ReplaySearch);
	}

	// todo: refactor and integrate with SearchUrlHelper
	public static String getReplaySearchPage(RefinementField field, String value)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pageUrlMap.get(PageUrlAlias.ReplaySearch));
		String prefix = websiteConfig.getRefinementUrlPrefix(field);
		sb.append(prefix).append("-").append(UrlUTF8Encoder.encode(value)).append(".").append(websiteConfig.getDefaultExtension());
		return sb.toString();
	}

	public static String getReplaySearchPageByPlayer(Player player) {
		if (StringUtils.isNotBlank(player.getProgamerName())) {
			return getReplaySearchPage(RefinementField.ProgamerName, player.getProgamerName() + "-" + player.getProgamerId());
		} else {
			return getReplaySearchPage(RefinementField.PlayerName, player.getPlayerName());
		}
	}
	public static String getReplaySearchPageByProgamer(PlayerPerson progamer) {
		return getReplaySearchPage(RefinementField.ProgamerName, progamer.getGameId());
	}

	/**** liquid progamer urls ******/
	public static String getProgamerHomePage()
	{
		return buildIndexForPageAlias(PageUrlAlias.ProgamerHome);
	}

	public static String getProgamerDetailPage(PlayerPerson playerPerson)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pageUrlMap.get(PageUrlAlias.ProgamerDetail));

		sb.append(String.format("%1$s-%2$s.%3$s", playerPerson.getGameId(), playerPerson.getPersonId(), websiteConfig.getDefaultExtension()));

		return sb.toString();
	}

	public static String getProgamerListByTeam(String team)
	{
		return UrlBuilder.fromString(getProgamerHomePage())
				.addParameter(QueryStringManager.Progamer_List_Param_Team, team)
				.encodeAs("UTF-8")
				.toString();
	}

	public static String getProgamerListByRace(String race)
	{
		return UrlBuilder.fromString(getProgamerHomePage())
				.addParameter(QueryStringManager.Progamer_List_Param_Race, race)
				.encodeAs("UTF-8")
				.toString();
	}

	private static String buildIndexForPageAlias(PageUrlAlias alias)
	{
		String url = pageUrlMap.get(alias);
		if (url != null && url.endsWith("/"))
			url = url + "index." + websiteConfig.getDefaultExtension();
		return url;
	}

	private static String buildPageUrl(PageUrlAlias alias) {
		String url = pageUrlMap.get(alias);
		if (StringUtils.isBlank(url))
			return url;

		PageUrlSetting setting = urlSettingMap.get(alias);
		if (setting == null || !setting.isSkipExtension()) {
			if (url.endsWith("/"))
				url = url + "index";
			url = StringUtils.stripEnd(url, ".");
			url = url + "." + websiteConfig.getDefaultExtension();
		}
		return url;
	}

	private static String buildPageUrl(PageUrlAlias alias, String param1, String value1) {
		return UrlBuilder.fromString(buildPageUrl(alias))
				.addParameter(param1, value1)
				.encodeAs("UTF-8")
				.toString();
	}

	public static String buildViewReplayStatsPage(String replayId) {
		return buildPageUrl(PageUrlAlias.StatsViewReplay, "id", replayId);
	}
	public static String buildViewProgamerStatsPage(String replayId) {
		return buildPageUrl(PageUrlAlias.StatsViewProgamer, "id", replayId);
	}
}
