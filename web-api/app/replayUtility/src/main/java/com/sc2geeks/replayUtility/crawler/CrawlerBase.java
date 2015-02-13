package com.sc2geeks.replayUtility.crawler;

import com.sc2geeks.replay.dao.ReplayDAO;
import com.sc2geeks.replay.model.GameEntity;
import com.sc2geeks.replayUtility.MutableNamespaceContext;
import com.sc2geeks.replayUtility.UserInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/3/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CrawlerBase
{
	protected static Logger logger = Logger.getLogger(CrawlerBase.class);

	protected static CrawlerConfig crawlerConfig = CrawlerConfig.getInstance();
	protected int existingCount = 0;

	public void start()
	{
		logger.info(getSourceName() + " - Retry failed games.");
		retryFailedCrawl();
		logger.info(getSourceName() + " - Retry failed games - done!");


		logger.info(getSourceName() + " - Crawling new games.");
		crawlNewReplays();
		logger.info(getSourceName() + " - Crawling new games - done!");
	}

	private void crawlNewReplays()
	{
		do
		{
			List<String> replayUrls = getReplayUrls();

			for (String gameUrl : replayUrls)
			{
				if (gameExists(gameUrl))
				{
					existingCount++;
					if (shouldStopCrawling())
						break;
					else
						continue;
				}
				crawlReplay(gameUrl, null);
			}
		}
		while (!shouldStopCrawling());
	}

	private void retryFailedCrawl()
	{
		try
		{
			List<GameEntity> retryGames = ReplayDAO.getFailedCrawlGames(crawlerConfig.getMaxRetries(), getSourceName());
			for (GameEntity game : retryGames)
			{
				crawlReplay(game.getGameUrl(), game);
			}
		} catch (Exception e)
		{
			logger.error(getSourceName() + " - Failed to do retry failed games.", e);
		}
	}

	private void crawlReplay(String gameUrl, GameEntity game)
	{
		if (game == null)
		{
			game = new GameEntity();
			game.setGameUrl(gameUrl);
			game.setExternalSource(getSourceName());
		}

		try
		{
			getReplayFromUrl(gameUrl, game);
			game.setStatus("C");

		} catch (Exception e)
		{
			game.setStatus("CF");
			logger.error(getSourceName() + " : Failed to get replay from " + gameUrl, e);
		}
		game.setCrawlTimes(game.getCrawlTimes() + 1);
		game.setLastEditDate(new Timestamp(new Date().getTime()));
		game.setLastEditUser(UserInfo.getCurrentUserName());
		ReplayDAO.saveOrUpdateObject(game);
	}

	/**
	 * override when needed.
	 *
	 * @return
	 */
	protected boolean shouldStopCrawling()
	{
		return existingCount >= crawlerConfig.getMaxExistedGameBeforeStop();
	}

	/**
	 * default implementation is to check if a game with the pageurl already exists.
	 * Override might be to extract the replay id and check against the replay id in case the game url may change.
	 *
	 * @param gameUrl
	 * @return
	 */
	protected boolean gameExists(String gameUrl)
	{
		try
		{
			GameEntity game = ReplayDAO.getGameByGameUrl(gameUrl);
			return game != null;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected abstract void getReplayFromUrl(String gameUrl, GameEntity game) throws Exception;


	/**
	 * normally this method returns all replay urls in one replay list page.
	 *
	 * @return
	 */
	protected abstract List<String> getReplayUrls();

	protected abstract String getSourceName();

	/* helper functions */
	/**
	 * @param input
	 * @return
	 */
	protected static Node standardizeXml(String input) throws Exception
	{
		XMLReader reader = new Parser();
		reader.setFeature(Parser.namespacesFeature, false);
		reader.setFeature(Parser.namespacePrefixesFeature, false);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


		DOMResult result = new DOMResult();

		InputSource inputSource = new InputSource(new StringReader(input));
		transformer.transform(new SAXSource(reader, inputSource),
				result);

		return result.getNode();
	}

	protected static String GetHtml(String url, String encoding) throws Exception
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.useragent",
				"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

		HttpContext localContext = new BasicHttpContext();
		HttpGet httpget;
		HttpResponse response;
		HttpEntity entity;
		String source;

		httpget = new HttpGet(url);
		response = httpClient.execute(httpget, localContext);
		entity = response.getEntity();
		source = EntityUtils.toString(entity, encoding);
		return source;
	}
	protected static XPath createXPath()
	{
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		MutableNamespaceContext nc = new MutableNamespaceContext();
		nc.setNamespace("html", "http://www.w3.org/1999/xhtml");

		xpath.setNamespaceContext(nc);
		return xpath;
	}}
