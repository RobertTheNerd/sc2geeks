package com.sc2geeks.replayUtility.mapWorker;

import com.sc2geeks.replay.parser.ResizeOption;
import com.sc2geeks.replayUtility.SpringConfigProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/12/12
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapWorkerConfig
{
	private String originalFolder;
	private String parseXmlFolder;

	List<ResizeOption> resizeOptions;

	public String getOriginalFolder()
	{
		return originalFolder;
	}

	public void setOriginalFolder(String originalFolder)
	{
		this.originalFolder = padFolder(originalFolder);
	}

	public String getParseXmlFolder()
	{
		return parseXmlFolder;
	}

	public void setParseXmlFolder(String parseXmlFolder)
	{
		this.parseXmlFolder = padFolder(parseXmlFolder);
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

	private MapWorkerConfig(){}

	private static MapWorkerConfig instance;
	public static MapWorkerConfig getInstance(){
		if (instance == null)
		{
			ApplicationContext context = new ClassPathXmlApplicationContext(SpringConfigProvider.getSpringConfigFile());
			instance = context.getBean("mapWorkerConfig", MapWorkerConfig.class);
		}
		return instance;
	}
}
