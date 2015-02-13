package com.sc2geeks.replayUtility.batchUploader;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.GameEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 12/4/11
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Uploader
{
	private static final String REPLAY_EXTENSION = "sc2replay";
	private static final String REPLAY_EXTERNAL_SOURCE = "GeekBatchUpload";
	private static final String REPLAY_EDIT_USER = "GeekBatchUpload";
	private static BatchUploadConfig uploadConfig;
	private static Logger logger = Logger.getLogger(Uploader.class);
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext("batchUpload.xml");
		uploadConfig = context.getBean("batchUploadConfig", BatchUploadConfig.class);

	}
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		File folder = new File(uploadConfig.getInputFolder());
		if (!folder.exists())
		{
			logger.error("Folder does not exist: " + uploadConfig.getInputFolder());
			return;
		}

		Collection<File> files = FileUtils.listFiles(folder, null, true);

		if (files == null || files.size() == 0)
		{
			logger.error("Folder is empty");
			return;
		}

		int total = 0;
		for (File replayFile : files)
		{
			if (uploadFile(replayFile))
				total ++;
		}
		logger.info(total +  " replays uploaded.");
	}

	private static boolean uploadFile(File file)
	{
		if (!file.getName().trim().toLowerCase().endsWith(REPLAY_EXTENSION))
		{
			// not a replay
			return false;
		}

		// copy to target folder
		File newFile = copyWithUniqueName(file, uploadConfig.getTargetFolder());

		GameEntity game = new GameEntity();
		game.setDownloadedReplayFileName(newFile.getName());
		game.setEvent(uploadConfig.getEvent());
		game.setExternalSource(REPLAY_EXTERNAL_SOURCE);
		game.setLastEditDate(new Timestamp(new Date().getTime()));
		game.setLastEditUser(REPLAY_EDIT_USER);
		if (ReplayDAO.saveOrUpdateObject(game))
		{
			logger.info("Replay added: " + newFile);
			return true;
		}
		else
		{
			logger.info("Couldn't add replay: " + newFile);
			return false;
		}
	}

	private static File copyWithUniqueName(File srcFile, String targetFolder)
	{
		File targetFile = getUniqueFileName(srcFile.getName(), targetFolder);
		if (targetFile == null)
		{
			logger.warn("Fail to get unique target file name for: " + srcFile.getAbsolutePath());
			return null;
		}

		try
		{
			FileUtils.copyFile(srcFile, targetFile);
			logger.info("File copied. From: " + srcFile.getAbsolutePath() + " <to> " + targetFile.getAbsolutePath());
			return targetFile;
		}
		catch(Exception e)
		{
			logger.warn(e);
			return null;
		}
	}

	private static File getUniqueFileName(String fileName, String folderName)
	{
		fileName = fileName.trim();
		folderName = StringUtils.removeEnd(folderName, "/") + "/";
		File file = new File(folderName + fileName);
		Random random = new Random();

		// 88 retries ^^
		for (int i = 0; i < 88; i ++)
		{
			if (!file.exists())
			{
				return file;
			}

			int number = random.nextInt() % 1000000;
			file = new File(folderName + "u_" + String.format("%06d", number) + "_" + fileName);
		}
		return null;
	}
}
