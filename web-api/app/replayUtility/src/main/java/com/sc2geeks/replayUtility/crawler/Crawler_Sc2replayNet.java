package com.sc2geeks.replayUtility.crawler;

import com.sc2geeks.replay.model.GameEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/3/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Crawler_Sc2replayNet extends CrawlerBase
{
	private int page = 1;
	private static final String BASE_URL_REPLAY_LIST = "http://www.sc2-replays.net/en/replays?page=";

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

	@Override
	protected List<String> getReplayUrls()
	{
		String listUrl = BASE_URL_REPLAY_LIST + Integer.toString(page ++);

		try
		{
			String source = GetHtml(listUrl, "UTF-8");

			Node node = standardizeXml(source);

			XPath xpath = createXPath();

			NodeList nodes = (NodeList) xpath.evaluate("//a[@class='rep_l']", node, XPathConstants.NODESET);

			LinkedHashSet<String> linkSet = new LinkedHashSet<String>();
			for (int i = 0; i < nodes.getLength(); i++)
			{
				String link = nodes.item(i).getAttributes().getNamedItem("href").getNodeValue();
				linkSet.add(link);
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
		return "sc2-replays.net";
	}

	private static String getReplayId(String detailUrl)
	{
		String url = detailUrl.toLowerCase().replace("http://www.sc2-replays.net/en/replays/", "");
		String[] parts = url.split("-");
		if (parts == null || parts.length == 0)
			return null;

		return parts[0];
	}

	private static String getGameEvent(Node node, XPath xpath) throws Exception
	{
		try
		{
			NodeList nodes = ((NodeList) xpath.evaluate("//div[@class='cbox_in']/div[@class='rep_info_left']", node, XPathConstants.NODESET));
			for (int i = 0; i < nodes.getLength(); i ++)
			{
				Node divNode = nodes.item(i);
				if (divNode.getTextContent().toLowerCase().contains("event"))
				{
					Node textNode = divNode.getNextSibling();
					return textNode.getTextContent().trim();
				}
			}
			return null;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String getDownloadUrl(Node node, XPath xpath) throws Exception
	{
		Node linkNode = ((NodeList) xpath.evaluate("//div[@class='cbox_rels_item']/a", node, XPathConstants.NODESET)).item(0);
		return linkNode.getAttributes().getNamedItem("href").getNodeValue();
	}


}
