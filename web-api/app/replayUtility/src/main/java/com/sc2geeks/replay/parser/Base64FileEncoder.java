package com.sc2geeks.replay.parser;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Base64FileEncoder
{
	static Logger logger = Logger.getLogger(Base64FileEncoder.class);

	public static String Base64Encode(String fileName)
			throws IOException
	{

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	public static boolean decodeAndSave(String base64Conent, String fileName)
	{
		byte[] result = Base64.decodeBase64(base64Conent);
		try
		{
			File folder = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));
			if (!folder.exists())
			{
				logger.info("Folder not exists: " + folder.getAbsolutePath());
				folder.mkdirs();
				logger.info("Created.");
			}
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(result);
			fos.close();
			return true;
		}
		catch(Exception e)
		{
			logger.error("Failed to decode. content: " + base64Conent + "\n file:" + fileName, e);
			return false;
		}
	}

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int)length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}

		is.close();
		return bytes;
	}
}
