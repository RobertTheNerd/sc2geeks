package com.sc2geeks.front;

import api.sc2geeks.entity.RefinementField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sc2geeks.front.ui.PageSEOInfo;
import com.sc2geeks.front.ui.PageUrlAlias;
import com.sc2geeks.front.ui.PageUrlSetting;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/25/11
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebsiteConfig
{
	private static WebsiteConfig _instance;

	private String websiteName;
	private String websiteRoot;
	private String jsRoot;
	private String cssRoot;
	private String replayFileRoot;
	private String imageRoot;
	private String mapImageRoot;
	private String map46ImageRoot;
	private String map124ImageRoot;

	private String playerImageRoot;

	private String defaultExtension = "html";
	private int leftNavTolerateCount;
	private int maxReplayCountInRelatedTab = 5;
	private String replayFilePrefix = "";

	// switches
	private boolean renderGoogleAnalytics;
	private boolean renderStatCounter;
	private boolean renderFootPrintTag;
	private boolean renderFeedback;

	private String siteMapFile;

	private List<PageUrlSetting> pageUrlAliasList;
	private Hashtable<PageUrlAlias, String> urlByPageAlias;
	private Hashtable<String, PageUrlAlias> pageAliasByUrl;

	private Hashtable<RefinementField, RefinementSetting> refinementSettingMap;
	private Hashtable<String, RefinementSetting> prefixRefinementMap;
	private Hashtable<String, RefinementSetting> legacyParameterRefinementMap;

	private List<RefinementSetting> refinementSettings;

	private PageSEOInfo defaultSEOInfo;

	private IRefinementInfoProvider replayLeftNavRefinementProvider;
	private IRefinementInfoProvider replayShowAllRefinementProvider;
	private IRefinementInfoProvider playerLeftNavRefinementProvider;

	private Hashtable<String, AbilityType> abilities;

	private List<StatsContainerSetting> statsContainerSettings;

	public IRefinementInfoProvider getReplayLeftNavRefinementProvider()
	{
		return replayLeftNavRefinementProvider;
	}

	public void setReplayLeftNavRefinementProvider(IRefinementInfoProvider replayLeftNavRefinementProvider)
	{
		this.replayLeftNavRefinementProvider = replayLeftNavRefinementProvider;
	}

	public IRefinementInfoProvider getReplayShowAllRefinementProvider()
	{
		return replayShowAllRefinementProvider;
	}

	public void setReplayShowAllRefinementProvider(IRefinementInfoProvider replayShowAllRefinementProvider)
	{
		this.replayShowAllRefinementProvider = replayShowAllRefinementProvider;
	}

	public IRefinementInfoProvider getPlayerLeftNavRefinementProvider()
	{
		return playerLeftNavRefinementProvider;
	}

	public void setPlayerLeftNavRefinementProvider(IRefinementInfoProvider playerLeftNavRefinementProvider)
	{
		this.playerLeftNavRefinementProvider = playerLeftNavRefinementProvider;
	}

	private WebsiteConfig()
	{
	}

	static
	{
		try
		{
			ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
			_instance = context.getBean("websiteConfig", WebsiteConfig.class);
			_instance.loadAbilities();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static WebsiteConfig getInstance()
	{
		return _instance;
	}

	/***
	 * used in seo & page title
	 * @return
	 */
	public String getWebsiteName()
	{
		return websiteName;
	}

	public void setWebsiteName(String websiteName)
	{
		this.websiteName = websiteName;
	}

	/**
	 *
	 * @return website root with trailing /
	 */
	public String getWebsiteRoot()
	{
		return websiteRoot;
	}

	/**
	 *
	 * @param websiteRoot
	 */
	public void setWebsiteRoot(String websiteRoot)
	{
		this.websiteRoot = processUrlFolder(websiteRoot);
	}

	/**
	 *
	 * @return js root with trailing /
	 */
	public String getJsRoot()
	{
		return jsRoot;
	}

	public void setJsRoot(String jsRoot)
	{
		this.jsRoot = processUrlFolder(jsRoot);
	}

	public String getCssRoot()
	{
		return cssRoot;
	}

	public void setCssRoot(String cssRoot)
	{
		this.cssRoot = processUrlFolder(cssRoot);
	}

	public String getReplayFileRoot()
	{
		return replayFileRoot;
	}

	public void setReplayFileRoot(String replayFileRoot)
	{
		this.replayFileRoot = processUrlFolder(replayFileRoot);
	}

	public String getImageRoot()
	{
		return imageRoot;
	}

	public void setImageRoot(String imageRoot)
	{
		this.imageRoot = processUrlFolder(imageRoot);
	}

	public String getMapImageRoot()
	{
		return mapImageRoot;
	}

	public boolean isRenderGoogleAnalytics()
	{
		return renderGoogleAnalytics;
	}

	public void setRenderGoogleAnalytics(boolean renderGoogleAnalytics)
	{
		this.renderGoogleAnalytics = renderGoogleAnalytics;
	}

	public boolean isRenderStatCounter()
	{
		return renderStatCounter;
	}

	public void setRenderStatCounter(boolean renderStatCounter)
	{
		this.renderStatCounter = renderStatCounter;
	}

	public boolean isRenderFeedback()
	{
		return renderFeedback;
	}

	public void setRenderFeedback(boolean renderFeedback)
	{
		this.renderFeedback = renderFeedback;
	}

	public void setMapImageRoot(String mapImageRoot)
	{
		this.mapImageRoot = processUrlFolder(mapImageRoot);
	}

	public String getMap46ImageRoot()
	{
		return map46ImageRoot;
	}

	public void setMap46ImageRoot(String map46ImageRoot)
	{
		this.map46ImageRoot = processUrlFolder(map46ImageRoot);
	}

	public String getMap124ImageRoot()
	{
		return map124ImageRoot;
	}

	public void setMap124ImageRoot(String map124ImageRoot)
	{
		this.map124ImageRoot = processUrlFolder(map124ImageRoot);
	}

	public String getPlayerImageRoot()
	{
		return playerImageRoot;
	}

	public void setPlayerImageRoot(String playerImageRoot)
	{
		this.playerImageRoot = processUrlFolder(playerImageRoot);
	}

	private static String processUrlFolder(String input)
	{
		input = input.trim();
		if (input.endsWith("/"))
			return input;
		return input + "/";
	}

	public String getReplayFilePrefix() {
		return replayFilePrefix;
	}

	public void setReplayFilePrefix(String replayFilePrefix) {
		this.replayFilePrefix = replayFilePrefix;
	}

	public List<PageUrlSetting> getPageUrlAliasList()
	{
		return pageUrlAliasList;
	}

	public void setPageUrlAliasList(List<PageUrlSetting> pageUrlAliasList)
	{
		this.pageUrlAliasList = pageUrlAliasList;
		urlByPageAlias = new Hashtable<PageUrlAlias, String>(pageUrlAliasList.size());
		pageAliasByUrl = new Hashtable<String, PageUrlAlias>(pageUrlAliasList.size());
		for (PageUrlSetting alias : pageUrlAliasList)
		{
			urlByPageAlias.put(alias.getAlias(), alias.getUrl());
			pageAliasByUrl.put(alias.getUrl().toLowerCase(), alias.getAlias());
		}
	}

	public String getUrlByPageAlias(PageUrlAlias alias)
	{
		return urlByPageAlias.get(alias);
	}

	/*
	public List<RefinementInfo> getLeftNavRefinementSetting()
	{
		return leftNavRefinementSetting;
	}

	public void setLeftNavRefinementSetting(List<RefinementInfo> leftNavRefinementSetting)
	{
		if (leftNavRefinementSetting == null)
		{
			return;
		}
		this.leftNavRefinementSetting = leftNavRefinementSetting;
		leftNavRefinementMap = new Hashtable<RefinementField, RefinementInfo>(leftNavRefinementSetting.size());
		for (RefinementInfo info : leftNavRefinementSetting)
		{
			leftNavRefinementMap.put(info.getRefinementField(), info);
		}
	}

	public List<RefinementInfo> getShowAllRefinementSetting()
	{
		return showAllRefinementSetting;
	}

	public void setShowAllRefinementSetting(List<RefinementInfo> showAllRefinementSetting)
	{
		if (showAllRefinementSetting == null)
			return;
		this.showAllRefinementSetting = showAllRefinementSetting;
		showAllRefinementMap = new Hashtable<RefinementField, RefinementInfo>(showAllRefinementSetting.size());
		for (RefinementInfo info : showAllRefinementSetting)
		{
			showAllRefinementMap.put(info.getRefinementField(), info);
		}
	}

	public RefinementInfo getLeftNavRefinementSetting(RefinementField refinementField)
	{
		if (leftNavRefinementMap == null)
			return null;
		return new RefinementInfo(leftNavRefinementMap.get(refinementField));
	}

	public RefinementInfo getShowAllRefinementSetting(RefinementField refinementField)
	{
		if (showAllRefinementMap == null)
			return null;
		RefinementInfo info = showAllRefinementMap.get(refinementField);
		return info == null ? getLeftNavRefinementSetting(refinementField) : new RefinementInfo(info);
	}
	*/

	public List<RefinementSetting> getRefinementSettings()
	{
		return refinementSettings;
	}

	public void setRefinementSettings(List<RefinementSetting> refinementSettings)
	{
		this.refinementSettings = refinementSettings;
		if (refinementSettings == null || refinementSettings.size() == 0)
			return;
		refinementSettingMap = new Hashtable<RefinementField, RefinementSetting>(refinementSettings.size());
		prefixRefinementMap = new Hashtable<String, RefinementSetting>(refinementSettings.size());
		legacyParameterRefinementMap = new Hashtable<String, RefinementSetting>(refinementSettings.size());
		for (RefinementSetting setting : refinementSettings)
		{
			refinementSettingMap.put(setting.getRefinementField(), setting);
			prefixRefinementMap.put(setting.getUrlPrefix().trim().toLowerCase(), setting);
			if (StringUtils.isNotBlank(setting.getLegacyParameter()))
				legacyParameterRefinementMap.put(setting.getLegacyParameter().trim().toLowerCase(), setting);
		}
	}

	public PageSEOInfo getDefaultSEOInfo()
	{
		return defaultSEOInfo;
	}

	public void setDefaultSEOInfo(PageSEOInfo defaultSEOInfo)
	{
		this.defaultSEOInfo = defaultSEOInfo;
	}

	public String getDefaultExtension()
	{
		return defaultExtension;
	}

	public void setDefaultExtension(String defaultExtension)
	{
		this.defaultExtension = defaultExtension;
	}

	public int getLeftNavTolerateCount()
	{
		return leftNavTolerateCount;
	}

	public void setLeftNavTolerateCount(int leftNavTolerateCount)
	{
		this.leftNavTolerateCount = leftNavTolerateCount;
	}

	public int getMaxReplayCountInRelatedTab()
	{
		return maxReplayCountInRelatedTab;
	}

	public void setMaxReplayCountInRelatedTab(int maxReplayCountInRelatedTab)
	{
		this.maxReplayCountInRelatedTab = maxReplayCountInRelatedTab;
	}

	public boolean isRenderFootPrintTag()
	{
		return renderFootPrintTag;
	}

	public void setRenderFootPrintTag(boolean renderFootPrintTag)
	{
		this.renderFootPrintTag = renderFootPrintTag;
	}

	public String getSiteMapFile()
	{
		return siteMapFile;
	}

	public void setSiteMapFile(String siteMapFile)
	{
		this.siteMapFile = siteMapFile;
	}

	public String getRefinementUrlPrefix(RefinementField field)
	{
		if (refinementSettingMap == null)
			return field.toString();
		RefinementSetting setting = refinementSettingMap.get(field);
		return setting == null ? field.toString() : setting.getUrlPrefix();
	}
	public String getRefinementDisplayName(RefinementField field)
	{
		if (refinementSettingMap == null)
			return field.toString();
		RefinementSetting setting = refinementSettingMap.get(field);
		return setting == null ? field.toString() : setting.getDisplayName();
	}

	public RefinementSetting getRefinementSettingByUrlPrefix(String prefix)
	{
		if (prefixRefinementMap == null || prefix == null)
			return null;
		return prefixRefinementMap.get(prefix.trim().toLowerCase());
	}

	public RefinementSetting getRefinementSettingByLegacyParameter(String paramName)
	{
		if (legacyParameterRefinementMap == null || StringUtils.isBlank(paramName))
			return null;
		return legacyParameterRefinementMap.get(paramName.trim().toLowerCase());
	}

	public List<StatsContainerSetting> getStatsContainerSettings() {
		return statsContainerSettings;
	}

	public void setStatsContainerSettings(List<StatsContainerSetting> statsContainerSettings) {
		this.statsContainerSettings = statsContainerSettings;
	}

	public boolean containsAbility(String ability) {
		if (abilities == null)
			return false;
		return abilities.containsKey(ability.trim().toLowerCase());
	}

		public String getAbilityType(String ability) {
		if (abilities == null)
			return null;
		return abilities.containsKey(ability.trim().toLowerCase()) ?
				abilities.get(ability.trim().toLowerCase()).getType() : null;
	}

	private void loadAbilities() {
		try {
			String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("abilities.json"));
			ArrayList<AbilityType> list = new Gson().fromJson(json, new TypeToken<ArrayList<AbilityType>>() {
			}.getType());
			abilities = new Hashtable<String, AbilityType>();
			for (AbilityType ability : list) {
				abilities.put(ability.getName().toLowerCase().trim(), ability);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
