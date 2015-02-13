package com.sc2geeks.front.util.sitemap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/20/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class SiteMapWriter
{
	String fileName;
	SiteMapWriterStatus status = SiteMapWriterStatus.Uninitialized;
	Writer writer = null;

	public SiteMapWriter(String fileName)
	{
		this.fileName = fileName;
	}

	public void writeUrl(String url)
	{
		writeUrl(url, null, null, null);
	}

	public void writeUrl(String url, Date lastMod)
	{
		writeUrl(url, lastMod, null, null);
	}

	public void writeUrl(String url, Date lastMod, String changeFreq)
	{
		writeUrl(url, lastMod, changeFreq, null);
	}
	public void writeUrl(String url, Date lastMod, String changeFreq, String priority)
	{
		if (status == SiteMapWriterStatus.Uninitialized)
		{
			initialize();
		}

		if (status == SiteMapWriterStatus.Error)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\t<url>\n");
		sb.append("\t\t<loc>").append(StringEscapeUtils.escapeXml(url)).append("</loc>\n");
		if (lastMod != null)
		{
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date = formatter.format(lastMod);
			sb.append("\t\t<lastmod>").append(date).append("</lastmod>\n");
		}
		if (StringUtils.isNotBlank(changeFreq))
		{
			sb.append("\t\t<changefreq>").append(StringEscapeUtils.escapeXml(changeFreq)).append("</changefreq>\n");
		}
		if (StringUtils.isNotBlank(priority))
		{
			sb.append("\t\t<priority>").append(StringEscapeUtils.escapeXml(priority)).append("</priority>\n");
		}
		sb.append("\t</url>\n");
		try
		{
		writer.write(sb.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			writer.write("</urlset>");
			writer.close();
		}
		catch(Exception e){}

		writer = null;
		status = SiteMapWriterStatus.Uninitialized;
	}

	private void initialize()
	{
		if (status == SiteMapWriterStatus.Initialized)
			return;

		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		}catch(Exception e){writer = null;}
		status = writer == null ? SiteMapWriterStatus.Error : SiteMapWriterStatus.Initialized;
	}
}
