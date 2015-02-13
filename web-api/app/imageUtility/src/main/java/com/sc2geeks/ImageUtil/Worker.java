package com.sc2geeks.ImageUtil;

import com.sc2geeks.commons.util.ImageUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/25/11
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Worker
{
	private static Logger logger = Logger.getLogger(Worker.class);

	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		start("config.xml");
	}

	public static void start(String configFile)
	{
		try
		{
			ApplicationContext context = new ClassPathXmlApplicationContext(configFile);
			ImageProcessConfig config = context.getBean("imageProcessConfig", ImageProcessConfig.class);

			File inputFolder = new File(config.getInputFolder());
			File[] images = inputFolder.listFiles();
			for (File image : images)
			{
				processImage(image, config);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static String processImage(File image, ImageProcessConfig config)
	{
		logger.info("Processing image:" + image.getAbsolutePath());
		String imgName = image.getName().replaceFirst("[.][^.]+$", "") + ".png";
		for (ResizeOption option : config.getResizeOptions())
		{
			String targetImageFile = option.getFolder() + imgName;
			logger.info("\tResizing " + option.getSize() + " to file: " + targetImageFile);
			if (!ImageUtil.scaleToSquare(image.getAbsolutePath(), option.getSize(), targetImageFile))
				return null;
		}

		String archivedFile = config.getOriginalFolder() + image.getName();
		logger.info("\tMoving to original folder: " + archivedFile);
		image.renameTo(new File(archivedFile));
		return imgName;
	}

	private static void test()
	{
		try
		{
			ImageUtil.scaleToSquare("/Users/robert/Documents/devel/idea/sc2nerd/assets/map/original/Jungle_Basin.jpg",
					60,
					"/Users/robert/Documents/devel/idea/sc2nerd/assets/map/60/Jungle_Basin.jpg");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
