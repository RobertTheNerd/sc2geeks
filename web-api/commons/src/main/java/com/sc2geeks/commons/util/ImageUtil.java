package com.sc2geeks.commons.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/12/12
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageUtil
{
	public static boolean scaleToSquare(String srcImgFile, int size, String targetImgFile)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(srcImgFile));

			if (img.getWidth() != img.getHeight())
			{
				img = makeToSquare(img);
			}

			img = Scalr.resize(img, Scalr.Method.QUALITY, size, size);

			ImageIO.write(img, "png", new File(targetImgFile));
			return true;

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private static BufferedImage makeToSquare(BufferedImage img)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		int padding_x = 0, padding_y = 0;
		int padding = (int)Math.ceil(Math.abs(width - height)/2.0);
		if (width > height)
			padding_y = padding;
		else
			padding_x = padding;

		return Scalr.pad(img, padding_x, padding_y, new Color(0, 0, 0, 0));
	}
}
