package com.sc2geeks.replay.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the replay parser that relies on sc2gears parsing service. https://sites.google.com/site/sc2gears/parsing-service
 * User: robert
 * Date: 12/10/12
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SC2GearsParsingService
{
	private static Logger logger = Logger.getLogger(SC2GearsParsingService.class);
	private static SC2GearsAPIConfig apiConfig = SC2GearsAPIConfig.getInstance();

	public static String parseReplayFile(String replayFileName)
	{
		try
		{
			logger.info("Calling sc2gears api for replay: " + replayFileName);
			HttpClient httpclient = new DecompressingHttpClient(new DefaultHttpClient());

			HttpPost httpPost = new HttpPost(apiConfig.getUrl());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("protVer", apiConfig.getProtVer()));
			nvps.add(new BasicNameValuePair("apiKey", apiConfig.getApiKey()));
			nvps.add(new BasicNameValuePair("op", "parseRep"));
			File file = new File(replayFileName);
			nvps.add(new BasicNameValuePair("fileLength", Long.toString(file.length())));
			nvps.add(new BasicNameValuePair("fileContent", Base64FileEncoder.Base64Encode(replayFileName)));

			// options
			nvps.add(new BasicNameValuePair("parseMessages", "true"));
			nvps.add(new BasicNameValuePair("parseActions", "true"));
			nvps.add(new BasicNameValuePair("sendActionsSelect", "false"));
			nvps.add(new BasicNameValuePair("sendActionsBuild", "true"));
			nvps.add(new BasicNameValuePair("sendActionsTrain", "true"));
			nvps.add(new BasicNameValuePair("sendActionsResearch", "true"));
			nvps.add(new BasicNameValuePair("sendActionsUpgrade", "true"));
			nvps.add(new BasicNameValuePair("sendActionsOther", "true"));
			nvps.add(new BasicNameValuePair("sendActionsInaction", "false"));

			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);

			try {
				if (response.getStatusLine().getStatusCode() != 200)
				{
					logger.warn("Failed to get replay result with status code:" + response.getStatusLine().getStatusCode() + ". Replay file: " + replayFileName);
					return null;
				}
				HttpEntity entity2 = response.getEntity();
				String resultXml = EntityUtils.toString(entity2, "UTF-8");

				logger.info("Done!");
				return resultXml;
			} finally {
				httpPost.releaseConnection();
			}

		}
		catch(Exception e)
		{
			logger.warn("Failed to parseReplayFile replay: " + replayFileName, e);
		}
		return null;
	}

	public static String parseMap(String mapUrl)
	{
		try
		{
			logger.info("Calling sc2gears api for map: " + mapUrl);
			HttpClient httpclient = new DecompressingHttpClient(new DefaultHttpClient());

			HttpPost httpPost = new HttpPost(apiConfig.getUrl());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("protVer", apiConfig.getProtVer()));
			nvps.add(new BasicNameValuePair("apiKey", apiConfig.getApiKey()));
			nvps.add(new BasicNameValuePair("op", "mapInfo"));
			nvps.add(new BasicNameValuePair("mapFileName", mapUrl));

			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);

			try {
				if (response.getStatusLine().getStatusCode() != 200)
				{
					logger.warn("Failed with status code:" + response.getStatusLine().getStatusCode());
					return null;
				}
				HttpEntity entity2 = response.getEntity();
				String resultXml = EntityUtils.toString(entity2, "UTF-8");

				logger.info("Done!");
				return resultXml;
			} finally {
				httpPost.releaseConnection();
			}

		}
		catch(Exception e)
		{
			logger.warn("Failed.", e);
		}
		return null;
	}
}
