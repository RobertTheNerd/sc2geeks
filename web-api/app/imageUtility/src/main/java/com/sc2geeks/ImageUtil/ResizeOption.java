package com.sc2geeks.ImageUtil;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/25/11
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResizeOption
{
	int size;
	String folder;

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder.endsWith("/") ? folder : folder + "/";
	}
}
