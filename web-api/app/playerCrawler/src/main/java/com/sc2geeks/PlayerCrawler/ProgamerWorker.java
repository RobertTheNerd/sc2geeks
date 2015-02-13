package com.sc2geeks.PlayerCrawler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sc2geeks.commons.web.WebCrawler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ProgamerWorker
{
	private static Logger logger = Logger.getLogger(ProgamerWorker.class);

	private static CrawlerConfig config = CrawlerConfig.getInstance();

	private static Hashtable<String, String> nameDict = new Hashtable<>();

	static {
		nameDict.put("name", "native_name");
		nameDict.put("romanized name", "romanized_name");
		nameDict.put("birth", "birthday");
		nameDict.put("country", "country");
		nameDict.put("race", "race");
		nameDict.put("team", "team");
		nameDict.put("twitter", "twitter_url");
		nameDict.put("fanclub", "fan_page");
	}

	public static void start()
	{
		List<String> urls = getAllPlayerUrlsFromWiki();
		List<DBObject> progamers = new ArrayList<DBObject>(urls.size());
		int total = progamers.size();
		int current = 1;
		for (String url : urls) {
			logger.info("Process " + current + " of " + total + "players");
			BasicDBObject progamer = crawlProGamer(url);
			if (progamer != null)
				progamers.add(progamer);
			current ++;
		}
		MongoHelper.saveProgamers(progamers);
	}


	private static List<String> getAllPlayerUrlsFromWiki()
	{
		String url = config.getAllPlayerUrl();

		try {
			logger.info("Getting player urls from: " + url);
			String source = WebCrawler.getHtml(url, "UTF-8").replace("g:plusone", "plusone");
			Node node = WebCrawler.standardizeXml(source);
			XPath xpath = WebCrawler.createXPath();
			NodeList tableNodes = (NodeList) xpath.evaluate("//div[@class='mw-content-ltr']/table", node, XPathConstants.NODESET);

			List<String> urls = new ArrayList<String>();
			for (int i = 0; i < tableNodes.getLength(); i++) {
				Node tableNode = tableNodes.item(i);
				if (tableNode.getChildNodes() == null || tableNode.getChildNodes().getLength() < 2)
					continue;

				// do not get retired/inactive players
				String className = tableNode.getAttributes().getNamedItem("class").getNodeValue();
				if (className.contains("collapsible"))
					continue;

				for (int j = 1; j < tableNode.getChildNodes().getLength(); j++) {
					Node trNode = tableNode.getChildNodes().item(j);

					if (trNode.getChildNodes() == null || trNode.getChildNodes().getLength() < 2)
						continue;

					Node tdNode = trNode.getChildNodes().item(0);
					Node linkNode = (Node) xpath.evaluate(".//a", tdNode, XPathConstants.NODE);
					if (linkNode == null)
						continue;

					String linkUrl = linkNode.getAttributes().getNamedItem("href").getNodeValue();
					linkUrl = composeLiquidUrl(url, linkUrl);

					urls.add(linkUrl);

				}

			}
			return urls;

		} catch (Exception e)
		{
			logger.error(e);
			return null;
		}
	}

	public static BasicDBObject crawlProGamer(String url)
	{
		try {
			BasicDBObject progamer = new BasicDBObject();

			logger.info("Getting user: " + url);
			String source = WebCrawler.getHtml(url, "UTF-8").replace("g:plusone", "plusone");
			Node node = WebCrawler.standardizeXml(source);
			XPath xpath = WebCrawler.createXPath();

			logger.info("\tGetting name.");
			Node h1Node = (Node) xpath.evaluate("//h1[@class='firstHeading']", node, XPathConstants.NODE);
			String name = h1Node.getTextContent().trim().replaceFirst("^[\\w]*/", "");
			progamer.append("name", name);
			logger.info("\tName: " + name);

			logger.info("\tGetting image.");
			Node imgNode = (Node) xpath.evaluate("//table[@class='infobox']//td[@class='boximage']//img", node, XPathConstants.NODE);
			String src = imgNode.getAttributes().getNamedItem("src").getNodeValue();
			src = composeLiquidUrl(url, src);
			progamer.append("image", src);
			logger.info("\tImage: " + src);

			// get large image
			Node imagePageLinkNode = imgNode.getParentNode().getAttributes().getNamedItem("href");
			if (imagePageLinkNode != null) {
				String imagePageLink = imagePageLinkNode.getNodeValue();
				imagePageLink = composeLiquidUrl(url, imagePageLink);
				String largeImageUrl = getLargeImage(imagePageLink);
				if (StringUtils.isNotBlank(largeImageUrl)) {
					progamer.append("large_image", largeImageUrl);
				}
			}

			// get progamer info
			Node tableNode = (Node) xpath.evaluate("//table[@class='infobox']", node, XPathConstants.NODE);
			if (tableNode != null && tableNode.getChildNodes() != null && tableNode.getChildNodes().getLength() > 0) {
				// attributes in nameSet
				logger.info("\tGetting attributes.");
				for (int i = 0; i < tableNode.getChildNodes().getLength(); i ++) {
					Node trNode = tableNode.getChildNodes().item(i);
					if (trNode.getChildNodes() == null || trNode.getChildNodes().getLength() < 2)
						continue;

					String attName = trNode.getChildNodes().item(0).getTextContent().trim().toLowerCase().replace(":", "");
					if (nameDict.containsKey(attName)) {
						attName = nameDict.get(attName);
						String attValue = trNode.getChildNodes().item(1).getTextContent().trim();
						logger.info("\t\tGetting attribute: " + attName + " = " + attValue);
						progamer.append(attName, attValue);
					}
				}

				// get links
				logger.info("\tGetting links.");
				Node divNode = (Node) xpath.evaluate(".//tr[@class='boxcontent']//div[@class='plainlinks']", tableNode, XPathConstants.NODE);
				if (divNode != null) {
					NodeList linkNodes = (NodeList) xpath.evaluate(".//a", divNode, XPathConstants.NODESET);
					for (int i = 0; i < linkNodes.getLength(); i++) {
						Node linkNode = linkNodes.item(i);
						Node titleNode = linkNode.getAttributes().getNamedItem("title");
						if (titleNode == null)
							continue;

						String title = titleNode.getNodeValue().trim().toLowerCase();
						if (nameDict.containsKey(title)) {
							title = nameDict.get(title);
							String value = linkNode.getAttributes().getNamedItem("href").getNodeValue();
							logger.info("\t\tGetting link: " + title + " - " + value);
							progamer.append(title, value);
						}
					}
				}
			}

			progamer.append("link", url);
			return progamer;

		} catch (Exception e)
		{
			return null;
		}
	}

	private static String getLargeImage(String url) {
		try {
			logger.info("\t\t\tGetting large image from : " + url);
			String source = WebCrawler.getHtml(url, "UTF-8").replace("g:plusone", "plusone");
			Node node = WebCrawler.standardizeXml(source);
			XPath xpath = WebCrawler.createXPath();

			Node linkNode = (Node) xpath.evaluate("//div[@class='fullImageLink']/a", node, XPathConstants.NODE);
			if (linkNode == null)
				return null;

			String href = linkNode.getAttributes().getNamedItem("href").getNodeValue();

			return composeLiquidUrl(url, href);

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private static String composeLiquidUrl(String baseUrl, String url) {
		if (url.startsWith("/")) {
			return config.getWikiRootUrl() + url;
		} else if (!url.startsWith("http")) {
			return baseUrl + "/" + url;
		}
		return url;
	}


	public static void main(String[] args) {
		start();
	}
}
