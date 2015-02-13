package com.sc2geeks.front.ui;

import com.sc2geeks.front.WebsiteConfig;
import org.springframework.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/15/12
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceHelper
{
	private static WebsiteConfig _websiteConfig = WebsiteConfig.getInstance();

	public static String buildWebsiteRootUrl()
	{
		return _websiteConfig.getWebsiteRoot();
	}

	public static String buildCssUrl(String cssFile)
	{
		return getPath(_websiteConfig.getCssRoot(), cssFile);
	}

	public static String buildImageUrl(String imgFile)
	{
		return getPath(_websiteConfig.getImageRoot(), imgFile);
	}

	public static String buildPageUrl(String pageUrl)
	{
		return getPath(_websiteConfig.getWebsiteRoot(), pageUrl);
	}

	public static String buildJsUrl(String jsFile)
	{
		return getPath(_websiteConfig.getJsRoot(), jsFile);
	}

	public static String buildReplayUrl(String replayFile)
	{
		return getPath(_websiteConfig.getReplayFileRoot(), StringUtils.replace(replayFile, _websiteConfig.getReplayFilePrefix(), ""));
	}

	public static String buildMapImageUrl(String mapImageFile)
	{
		return getPath(_websiteConfig.getMapImageRoot(), mapImageFile);
	}

	public static String buildMap46ImageUrl(String mapImageFile)
	{
		return getPath(_websiteConfig.getMap46ImageRoot(), mapImageFile);
	}

	public static String buildMap124ImageUrl(String mapImageFile)
	{
		return getPath(_websiteConfig.getMap124ImageRoot(), mapImageFile);
	}

	public static String buildPlayerImageUrl(String imageFile)
	{
		return getPath(_websiteConfig.getPlayerImageRoot(), imageFile);
	}
	private static String getPath(String folder, String file)
	{
		if (!folder.endsWith("/"))
			folder = folder + "/";
		return folder + file;
	}
}
