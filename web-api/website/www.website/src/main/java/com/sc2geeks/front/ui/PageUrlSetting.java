package com.sc2geeks.front.ui;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 10/1/11
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class PageUrlSetting
{
	private PageUrlAlias alias;
	private String url;
	private boolean skipExtension = false;

	public PageUrlAlias getAlias()
	{
		return alias;
	}

	public void setAlias(PageUrlAlias alias)
	{
		this.alias = alias;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		if (url.trim().startsWith("/"))
		{
			this.url = url.trim().replaceFirst("/", "");
		} else
		{
			this.url = url.trim();
		}
	}

	public boolean isSkipExtension() {
		return skipExtension;
	}

	public void setSkipExtension(boolean skipExtension) {
		this.skipExtension = skipExtension;
	}
}
