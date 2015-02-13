package com.sc2geeks.replay.parser;

import com.sc2geeks.commons.serializer.XmlSerializer;
import com.sc2geeks.commons.util.ImageUtil;
import com.sc2geeks.commons.web.UrlUTF8Encoder;
import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.MapEntity;
import com.sc2geeks.replayUtility.mapWorker.MapWorkerConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/13/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapParser
{
	private static final String IMAGE_PREFIX = "map_";
	private static final String IMAGE_SUFFIX_RESIZE = ".png";
	private static final String IMAGE_SUFFIX_ORIGINAL = ".jpg";
	static Logger logger = Logger.getLogger(MapParser.class);
	static MapWorkerConfig config = MapWorkerConfig.getInstance();

	public static MapEntity parseMap(String url, String name)
	{

		MapEntity sampleMap = ReplayDAO.getMapWithImageName(url);
		if (sampleMap != null)
		{
			MapEntity map = new MapEntity();
			map.setBnetUrl(url);
			map.setName(name);
			map.setImageFile(sampleMap.getImageFile());
			map.setWidth(sampleMap.getWidth());
			map.setHeight(sampleMap.getHeight());
			map.setLastEditDate(new Timestamp(new Date().getTime()));
			ReplayDAO.saveOrUpdateObject(map);
			return map;
		} else
		{
			MapEntity map = parseNewMap(url);
			if (map == null)
			{
				map = createUnknownMap(url);
				map.setName(name);
			}
			ReplayDAO.saveOrUpdateObject(map);
			return map;
		}
	}
	public static MapEntity parseNewMap(String url)
	{
		String xmlResponse = SC2GearsParsingService.parseMap(url);
		if (StringUtils.isBlank(xmlResponse))
		{
			return null;
		}

		SC2GearsMapResponse response = XmlSerializer.deserializeFromString(xmlResponse, SC2GearsMapResponse.class);
		if (response == null || response.resultLine == null)
		{
			logger.error("Failed to parse map for: " + url);
			return null;
		}

		// not recognized
		if (response.resultLine.code == 3)
		{
			logger.info("Map not recognized.");
			return null;
		}

		// create folder
		File folder = new File(config.getParseXmlFolder());
		if (!folder.exists())
		{
			folder.mkdirs();
		}

		// save response file content
		logger.info("Writing api reponse to xml file.");
		String fullResponseFileName = config.getParseXmlFolder() + UrlUTF8Encoder.encode(StringUtils.removeEnd(url, ".s2ma")) + ".xml";
		File xmlFile = new File(fullResponseFileName);
		try
		{
			FileUtils.write(xmlFile, xmlResponse, "UTF-8");
		} catch (IOException e)
		{
			logger.error("Failed to save xml content for map: " + url + "\nContent:" + xmlResponse + "\n file:" + fullResponseFileName, e);
		}

		if (response.mapImage == null || StringUtils.isBlank(response.mapImage.base64Image))
		{
			logger.error("No image found in response.");
			return null;
		}

		// save image file
		logger.info("Saving original image file.");
		String imageName = generateMapFileName();
		String originalImage = config.getOriginalFolder() + imageName + IMAGE_SUFFIX_ORIGINAL;
		String mapImageName = imageName + IMAGE_SUFFIX_RESIZE;
		Base64FileEncoder.decodeAndSave(response.mapImage.base64Image, originalImage);

		// resize
		for (ResizeOption option : config.getResizeOptions())
		{
			File resizeFolder = new File(option.getFolder());
			if (!resizeFolder.exists())
			{
				logger.info("Folder not exists: " + resizeFolder.getAbsolutePath());
				resizeFolder.mkdirs();
				logger.info("Created.");
			}
			String resizedImageName = option.getFolder() + mapImageName;
			ImageUtil.scaleToSquare(originalImage, option.getSize(), resizedImageName);
		}
		MapEntity map = new MapEntity();
		map.setBnetUrl(url);
		map.setName(response.map.name);
		map.setImageFile(mapImageName);
		map.setWidth(response.map.width);
		map.setHeight(response.map.height);
		map.setLastEditDate(new Timestamp(new Date().getTime()));
		return map;
	}

	public static MapEntity parseExistingMap(String url)
	{
		MapEntity map = parseNewMap(url);
		if (map == null)
			map = createUnknownMap(url);
		ReplayDAO.updateMap(map.getBnetUrl(), map.getName(), map.getImageFile(), map.getWidth(), map.getHeight());
		return map;
	}

	private static MapEntity createUnknownMap(String url)
	{
		MapEntity map = new MapEntity();
		map.setBnetUrl(url);
		map.setLastEditDate(new Timestamp(new Date().getTime()));
		map.setImageFile(ReplayDAO.UNKNOWN_MAP_IMAGE);
		map.setWidth(0);
		map.setHeight(0);
		return map;
	}

	public static String generateMapFileName()
	{
		String maxName = ReplayDAO.getMaxMapFileName();
		int maxNumber = 0;
		if (!StringUtils.isEmpty(maxName))
		{
			maxName = maxName.replace(IMAGE_PREFIX, "").replace(IMAGE_SUFFIX_RESIZE, "");
			maxNumber = Integer.parseInt(maxName);
		}

		return IMAGE_PREFIX + String.format("%1$06d", maxNumber + 1);
	}
}
