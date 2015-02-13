package com.sc2geeks.replayUtility.crawler;

import com.sc2geeks.replay.model.GameEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/3/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Crawler163 extends CrawlerBase
{
	private static final String SOURCE_163 = "163.com";
	private static final String BASE_URL_GAME_LIST = "http://rep.s.163.com/replaylist.aspx?gamerace=0&level=0&pageno=";
	private static final String BASE_URL_DETAIL = "http://rep.s.163.com/";
	private int page;

	public Crawler163()
	{
		super();
		page = crawlerConfig.getStartingPage_163();
	}
	@Override
	protected void getReplayFromUrl(String gameUrl, GameEntity game) throws Exception
	{
		XPath xpath = createXPath();
		String source = GetHtml(gameUrl, "gb2312");
		Node node = standardizeXml(source);

		game.setExternalId(getParameterValue(gameUrl, "replayid"));
		game.setEvent(getGameEvent(node, xpath));
		game.setExternalDescription(getGameDescription(node, xpath));
		game.setExternalRepFile(getDownloadUrl(node, xpath));
	}


	@Override
	protected List<String> getReplayUrls()
	{
		String pageUrl = BASE_URL_GAME_LIST + Integer.toString(page ++);

		return getGamesInPage(pageUrl);
	}

	@Override
	protected String getSourceName()
	{
		return SOURCE_163;
	}

	/* helper functions */
	private static String getGameDescription(Node node, XPath xpath) throws Exception
	{
		try
		{
			return ((Node) xpath.evaluate(".//div[@class='pagedload_info']//h2", node, XPathConstants.NODE)).getTextContent().trim();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String getGameEvent(Node node, XPath xpath) throws Exception
	{
		try
		{
			Node liNode = ((NodeList) xpath.evaluate("//ul[@class='info']/li", node, XPathConstants.NODESET)).item(4);
			return ((Node) xpath.evaluate("./span/a", liNode, XPathConstants.NODE)).getTextContent().trim();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String getDownloadUrl(Node node, XPath xpath) throws Exception
	{
		Node linkNode = (Node) xpath.evaluate("//a[@class='pagedloadbt clearfix']", node, XPathConstants.NODE);
		return BASE_URL_DETAIL + linkNode.getAttributes().getNamedItem("href").getNodeValue();
	}

	private static String getParameterValue(String url, String param)
	{
		int startPos = url.indexOf(param + "=");
		if (startPos == -1)
			return null;

		url = url.substring(startPos + param.length() + 1);
		int endPos = url.indexOf("&");
		return (endPos == -1 ? url : url.substring(0, endPos));
	}

	private static List<String> getGamesInPage(String pageUrl)
	{
		try
		{
			String source = GetHtml(pageUrl, "gb2312");

			Node node = standardizeXml(source);

			XPath xpath = createXPath();

			NodeList nodes = (NodeList) xpath.evaluate("//ul[@class='replay-list']/li//a[@class='downbutton']", node, XPathConstants.NODESET);

			List<String> urls = new ArrayList<String>(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++)
			{
				String link = nodes.item(i).getAttributes().getNamedItem("href").getNodeValue();
				urls.add(BASE_URL_DETAIL + link);
			}
			return urls;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
