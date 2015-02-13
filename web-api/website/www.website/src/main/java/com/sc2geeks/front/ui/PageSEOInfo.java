package com.sc2geeks.front.ui;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/15/12
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PageSEOInfo
{
	String pageTitle;
	String pageKeyword;
	String pageDescription;
	String canonicalUrl;


	public PageSEOInfo(){}

	public PageSEOInfo(PageSEOInfo seoInfo)
	{
		this.pageTitle = seoInfo.pageTitle;
		this.pageKeyword = seoInfo.pageKeyword;
		this.pageDescription = seoInfo.pageDescription;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public void setPageTitle(String pageTitle)
	{
		this.pageTitle = pageTitle;
	}

	public String getPageKeyword()
	{
		return pageKeyword;
	}

	public void setPageKeyword(String pageKeyword)
	{
		this.pageKeyword = pageKeyword;
	}

	public String getPageDescription()
	{
		return pageDescription;
	}

	public void setPageDescription(String pageDescription)
	{
		this.pageDescription = pageDescription;
	}

	public String getCanonicalUrl()
	{
		return canonicalUrl;
	}

	public void setCanonicalUrl(String canonicalUrl)
	{
		this.canonicalUrl = canonicalUrl;
	}
}
