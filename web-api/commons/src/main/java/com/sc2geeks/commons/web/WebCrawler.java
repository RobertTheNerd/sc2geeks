package com.sc2geeks.commons.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
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
import java.io.FileOutputStream;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/15/12
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebCrawler
{
	public static Node standardizeXml(String input) throws Exception
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

	public static String getHtml(String url, String encoding) throws Exception
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
	public static XPath createXPath()
	{
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		MutableNamespaceContext nc = new MutableNamespaceContext();
		nc.setNamespace("html", "http://www.w3.org/1999/xhtml");

		xpath.setNamespaceContext(nc);
		return xpath;
	}

	public static boolean download(String url, String fileName)
	{
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.useragent",
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

			HttpContext localContext = new BasicHttpContext();
			HttpGet httpget;
			HttpResponse response;
			HttpEntity entity;

			httpget = new HttpGet(url);
			response = httpClient.execute(httpget, localContext);
			entity = response.getEntity();
			if (entity != null)
			{
				FileOutputStream fos = new FileOutputStream(fileName);
				entity.writeTo(fos);
				fos.close();
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
