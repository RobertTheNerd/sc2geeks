package com.sc2geeks.replayUtility;

import com.sc2geeks.replayUtility.crawler.CrawlerBase;
import com.sc2geeks.replayUtility.crawler.CrawlerFactory;
import com.sc2geeks.replayUtility.downloader.DownloadWorker;
import com.sc2geeks.replayUtility.mapWorker.MapWorker;
import com.sc2geeks.replayUtility.parser.ParseWorker;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/21/11
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class MainWorker extends StarterBase
{

	static Logger logger = Logger.getLogger(MainWorker.class);

	private static MainWorkerConfig _workerConfig;

	/**
	 * @param args the first arguments:
	 *             crawl
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		init(args);

		work();
	}

	private static void init(String[] args)
	{
		if (args.length > 0)
		{
			System.out.println(args[0]);
			SpringConfigProvider.setSpringConfigFile(args[0]);
		} else
		{
			SpringConfigProvider.setSpringConfigFile("config.xml");
		}
		ApplicationContext context = new ClassPathXmlApplicationContext(SpringConfigProvider.getSpringConfigFile());
		_workerConfig = context.getBean("workerConfig", MainWorkerConfig.class);
	}

	private static void work()
	{
		if (_workerConfig.isDoCrawl())
		{
			try
			{
				logger.info("Crawler stared.");
				List<CrawlerBase> crawlers = CrawlerFactory.createCrawlers();
				for (CrawlerBase crawler : crawlers)
				{
					crawler.start();
				}
				logger.info("Crawler done.");
			} catch (Exception e)
			{
				logger.error("Crawler failed.", e);
			}
		}
		if (_workerConfig.isDoDownload())
		{
			try
			{
				logger.info("Downloader stared.");
				DownloadWorker.start();
				logger.info("Downloader done.");
			} catch (Exception e)
			{
				logger.error("Downloader failed.", e);
			}
		}
		if (_workerConfig.isDoReplayParse())
		{
			try
			{
				logger.info("Parser stared.");
				ParseWorker.start();
				logger.info("Parser done.");
			} catch (Exception e)
			{
				logger.error("Parser failed.", e);
			}
		}

		if (_workerConfig.isDoMapParse())
		{
			try
			{
				logger.info("Map parser stared.");
				MapWorker.start();
				logger.info("Map parser done.");
			} catch (Exception e)
			{
				logger.error("Map parser failed.", e);
			}
		}

		if (_workerConfig.isDoPostProcessing())
		{
			logger.info("PostProcessor stared.");
			Postprocessor.start();
			logger.info("PostProcessor done.");
		}
	}
}
