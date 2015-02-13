package com.sc2geeks.replayUtility.mapWorker;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.MapEntity;
import com.sc2geeks.replay.parser.MapParser;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/12/12
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapWorker
{

	static Logger logger = Logger.getLogger(MapWorker.class);

	public static void start()
	{
		logger.info("Assigning existing map images to unparsed ones.");
		evenOutMapImages();
		logger.info("Done.");

		logger.info("Processing un-parsed maps.");
		List<String> mapUrls = ReplayDAO.getMapUrlsWithoutImage();
		if (mapUrls == null || mapUrls.size() == 0)
		{
			logger.info("No un-parsed maps found.");
		} else
		{
			logger.info(mapUrls.size() + " map urls found.");
			for (String url : mapUrls)
			{
				logger.info("Parsing map: " + url);
				MapParser.parseExistingMap(url);
				logger.info("map " + url + " is parsed.");
			}
		}

		logger.info("Processing un-parsed maps - done.");
	}

	public static void evenOutMapImages()
	{
		List<String> mapUrls = ReplayDAO.getMapUrlsWithoutImage();
		if (mapUrls == null || mapUrls.size() == 0)
			return;

		for (String url : mapUrls)
		{
			MapEntity sampleMap = ReplayDAO.getMapWithImageName(url);
			if (sampleMap == null)
				continue;

			ReplayDAO.updateMap(url, sampleMap.getImageFile(), sampleMap.getWidth(), sampleMap.getHeight());
		}
	}
}
