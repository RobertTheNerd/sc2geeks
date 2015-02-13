package com.sc2geeks.replayUtility.downloader;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.GameEntity;
import com.sc2geeks.replayUtility.SpringConfigProvider;
import com.sc2geeks.replayUtility.UserInfo;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/29/11
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DownloadWorker
{
	private static Logger logger = Logger.getLogger(DownloadWorker.class);
	private static DownloaderConfig _downloaderConfig;
	static
	{
		ApplicationContext context = new ClassPathXmlApplicationContext(SpringConfigProvider.getSpringConfigFile());
		_downloaderConfig = context.getBean("downloaderConfig", DownloaderConfig.class);
		if (!_downloaderConfig.getDownloadFolder().endsWith("/"))
			_downloaderConfig.setDownloadFolder(_downloaderConfig.getDownloadFolder().trim() + "/");
	}
	public static void start() throws Exception
	{
		List<GameEntity> gameList;

		logger.info("Retry failed games.");
		if (_downloaderConfig.isProcessFailedReplays())
		{
			gameList = ReplayDAO.getFailedDownloadGames(_downloaderConfig.getMaxFailedRetries());
			if (gameList!= null && gameList.size() > 0)
			{
				downloadReplays(gameList);
			}
		}
		logger.info("Retry failed games - done!");


		logger.info("Downloading crawled games.");
		while ((gameList = ReplayDAO.getUndownloadedGames(_downloaderConfig.getBatchSize())) != null &&
			gameList.size() > 0)
		{
			downloadReplays(gameList);
		}
		logger.info("Downloading crawled games - done!");
	}

	private static void downloadReplays(List<GameEntity> games)
	{
		File folder = new File(_downloaderConfig.getDownloadFolder());
		if (!folder.exists())
		{
			logger.info("Download folder being created:" + folder.getAbsolutePath());
			folder.mkdirs();
		}

		for (GameEntity game : games)
		{
			String localFileShort = game.getGameId() + ".sc2Replay";
			String localFileFull = _downloaderConfig.getDownloadFolder() + localFileShort;
			logger.info("Downloading replay: " + game.getExternalRepFile());
			if (downloadReplay(game.getExternalRepFile(), localFileFull))
			{
				game.setDownloadedReplayFileName(localFileShort);
				game.setStatus("D");
				logger.info("Downloaded.");
			}
			else
			{
				game.setStatus("DF");
			}
			game.setDownloadTimes(game.getDownloadTimes() + 1);
			game.setLastEditDate(new Timestamp(new Date().getTime()));
			game.setLastEditUser(UserInfo.getCurrentUserName());
			ReplayDAO.saveOrUpdateObject(game);
		}
	}

	public static boolean downloadReplay(String address, String localFileName)
	{
		OutputStream out = null;
		InputStream inStream = null;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.useragent",
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			HttpGet httpget = new HttpGet(address);

			if (address.contains("163.com") && _downloaderConfig.getSleepFor163() > 0)
			{
				Thread.sleep(_downloaderConfig.getSleepFor163());
			}

			inStream = httpClient.execute(httpget).getEntity().getContent();

			// Open an output stream to the destination file on our local filesystem
			out = new BufferedOutputStream(new FileOutputStream(localFileName));

			// Get the data
			byte[] buffer = new byte[1024 * 15];
			int numRead;
			while ((numRead = inStream.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
			}
			return true;
		} catch (Exception exception) {
			logger.error("Failed to download replay: " + address, exception);
			return false;
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				// Shouldn't happen, maybe add some logging here if you are not
				// fooling around ;)
				ioe.printStackTrace();
			}
		}
	}


}
