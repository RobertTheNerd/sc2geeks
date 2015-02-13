package api.sc2geeks.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Hashtable;


public class ImageInfo {
	Hashtable<String, SizeInfo> sizes;
	int width;
	int height;
	String file;
	String sharedPath;
	boolean sharedPathUpdated = false;


	public String getImageUrl(int size) {
		if (sizes == null)
			return null;

		if (!sharedPathUpdated) {
			parseSharedPath();
			updateImageUrls();
			sharedPathUpdated = true;
		}

		SizeInfo sizeInfo = sizes.get("size-" + size);
		if (sizeInfo == null)
			return null;

		return sizeInfo.getFile();
	}

	public Hashtable<String, ImageInfo.SizeInfo> getSizes() {
		return sizes;
	}

	public void setSizes(Hashtable<String, ImageInfo.SizeInfo> sizes) {
		this.sizes = sizes;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	private void parseSharedPath() {
		if (file == null)
			return;

		int end = file.lastIndexOf('/');
		sharedPath = file.substring(0, end + 1);
	}

	private void updateImageUrls() {
		if (StringUtils.isBlank(sharedPath) || sizes == null)
			return;

		for (String key : sizes.keySet()) {
			SizeInfo size = sizes.get(key);
			size.setFile(sharedPath + size.getFile());
		}
	}

	public static class SizeInfo {
		int width;
		int height;
		String mimeTye;
		String file;

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getMimeTye() {
			return mimeTye;
		}

		public void setMimeTye(String mimeTye) {
			this.mimeTye = mimeTye;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}
	}
}