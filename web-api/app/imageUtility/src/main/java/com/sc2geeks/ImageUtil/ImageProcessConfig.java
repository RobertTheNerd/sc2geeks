package com.sc2geeks.ImageUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/25/11
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageProcessConfig
{
	private String inputFolder;
	private String originalFolder;

	List<ResizeOption> resizeOptions;

	public String getInputFolder()
	{
		return inputFolder;
	}

	public void setInputFolder(String inputFolder)
	{
		this.inputFolder = padFolder(inputFolder);
	}

	public String getOriginalFolder()
	{
		return originalFolder;
	}

	public void setOriginalFolder(String originalFolder)
	{
		this.originalFolder = padFolder(originalFolder);
	}

	public List<ResizeOption> getResizeOptions()
	{
		return resizeOptions;
	}

	public void setResizeOptions(List<ResizeOption> resizeOptions)
	{
		this.resizeOptions = resizeOptions;
	}

	private static String padFolder(String input)
	{
		return input.endsWith("/") ? input : input + "/";
	}
}
