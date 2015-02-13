package api.sc2geeks.entity;

import org.apache.commons.lang3.StringUtils;

public class EntityWithImageInfo {
	protected ImageInfo imageInfo;

	public ImageInfo getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}

	public boolean hasImage(int size) {
		if (imageInfo == null)
			return false;

		String sizeKey = "size-" + size;
		if (!imageInfo.getSizes().containsKey(sizeKey))
			return false;

		ImageInfo.SizeInfo sizeInfo = imageInfo.getSizes().get(sizeKey);
		return StringUtils.isNotBlank(sizeInfo.file);
	}

	public String getImageUrl(int size) {
		if (imageInfo == null)
			return null;

		return imageInfo.getImageUrl(size);
	}
}