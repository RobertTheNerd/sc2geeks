package com.sc2geeks.commons.logging;

import org.apache.log4j.FileAppender;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by robert on 9/1/14.
 */
public class CurrentTimeFileAppender extends FileAppender {

	@Override
	public void setFile(String fileName)
	{
		if (fileName.indexOf("%timestamp") >= 0) {
			Date d = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			fileName = fileName.replaceAll("%timestamp", format.format(d));
		}
		super.setFile(fileName);
	}
}