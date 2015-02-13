package com.sc2geeks.replayUtility.crawler;

import com.sc2geeks.replay.model.GameEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 10/21/12
 * Time: 8:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Crawler_GosugamersNet extends CrawlerBase
{
	private int start = 0;
	private static final String BASE_URL_REPLAY_LIST = "http://www.gosugamers.net/starcraft2/replays.php?&start=";
	private static final String BASE_URL_REPLAY_DETAIL = "http://www.gosugamers.net/starcraft2";

	@Override
	protected void getReplayFromUrl(String gameUrl, GameEntity game) throws Exception
	{
		XPath xpath = createXPath();
		String source = GetHtml(gameUrl, "UTF-8");
		Node node = standardizeXml(source);

		game.setExternalId(getReplayId(gameUrl));
		game.setEvent(getGameEvent(node, xpath));
		game.setExternalRepFile(getDownloadUrl(node, xpath));
	}


	private static String getReplayId(String gameUrl)
	{
		return gameUrl.toLowerCase().replace(BASE_URL_REPLAY_DETAIL + "/replays/", "");
	}

	private static String getGameEvent(Node node, XPath xpath)
	{
		try
		{
			NodeList trList = (NodeList) xpath.evaluate("//div[@id='cont']//td[@class='cont_middle']//tr", node, XPathConstants.NODESET);
			for (int i = 0; i < trList.getLength(); i ++)
			{
				Node trNode = trList.item(i);
				Node firstTd = (Node) xpath.evaluate(".//td", trNode, XPathConstants.NODE);
				if (firstTd == null || firstTd.getTextContent() == null)
					continue;

				if (firstTd.getTextContent().trim().compareToIgnoreCase("event") == 0)
				{
					return trNode.getChildNodes().item(1).getTextContent().trim();
				}

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private static String getDownloadUrl(Node node, XPath xpath) throws XPathExpressionException
	{
		Node downloadNode = (Node) xpath.evaluate("//div[@id='cont']//td[@class='cont_middle']//p//a", node, XPathConstants.NODE);
		return BASE_URL_REPLAY_DETAIL + "/replays/" + downloadNode.getAttributes().getNamedItem("href").getNodeValue();
	}

	@Override
	protected List<String> getReplayUrls()
	{
		String listUrl = BASE_URL_REPLAY_LIST + Integer.toString(start);
		start += 100;

		try
		{
			String source = GetHtml(listUrl, "UTF-8");

			Node node = standardizeXml(source);

			XPath xpath = createXPath();

			NodeList nodes = (NodeList) xpath.evaluate("//table[@class='multitable']//tr[@class='wide_middle']", node, XPathConstants.NODESET);

			LinkedHashSet<String> linkSet = new LinkedHashSet<String>();
			for (int i = 0; i < nodes.getLength(); i++)
			{
				String link = nodes.item(i).getAttributes().getNamedItem("id").getNodeValue();
				linkSet.add(BASE_URL_REPLAY_DETAIL + link);
			}
			return Arrays.asList(linkSet.toArray(new String[0]));
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected String getSourceName()
	{
		return "gosugamers.net";
	}
}
