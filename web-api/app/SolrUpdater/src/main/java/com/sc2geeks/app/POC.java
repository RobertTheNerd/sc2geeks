package com.sc2geeks.app;

import com.google.gson.Gson;
import com.sc2geeks.commons.util.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by robert on 9/3/14.
 */
public class POC {
	public static void main(String[] args) {
		// testProperty();
		//parseJson();
		parseSharedPath();
	}

	private static void parseSharedPath() {

		String file = "2014/01/somefile.jpg";
		int end = file.lastIndexOf('/');
		String sharedPath = file.substring(0, end + 1);
		System.out.print(sharedPath);
	}

	private static void testProperty() {
		String val = System.getProperty("config");
		System.out.print(val);
	}


	private static void parseJson() {
		try {
			String content = FileUtil.readFile("/Users/robert/data/imageinfo.txt", StandardCharsets.UTF_8);
			ImageInfo imageInfo = new Gson().fromJson(content, ImageInfo.class);
			System.out.print(imageInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void testJson() {
		Dictionary<String, SizeInfo> sizes = new Hashtable<String, SizeInfo>();
		sizes.put("medium", new SizeInfo(100, 200, "image/jpeg", "/hello.txt"));
		sizes.put("large", new SizeInfo(300, 400, "image/jpeg", "/Lhello.txt"));

		String json = new Gson().toJson(sizes);
		System.out.print(json);

	}

	public static class SizeInfo {
		int width;
		int height;
		String mimeTye;
		String file;

		public SizeInfo(int width, int height, String mimeTye, String file) {
			this.width = width;
			this.height = height;
			this.mimeTye = mimeTye;
			this.file = file;
		}
	}

	public static class ImageInfo {
		Hashtable<String, SizeInfo> sizes;
		int width;
		int height;
		String file;
	}

}
