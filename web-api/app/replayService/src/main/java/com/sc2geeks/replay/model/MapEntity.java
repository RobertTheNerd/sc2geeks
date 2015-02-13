package com.sc2geeks.replay.model;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapEntity
{
	private int mapId;

	public int getMapId()
	{
		return mapId;
	}

	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private String bnetUrl;

	public String getBnetUrl()
	{
		return bnetUrl;
	}

	public void setBnetUrl(String bnetUrl)
	{
		this.bnetUrl = bnetUrl;
	}

	private String imageFile;

	public String getImageFile()
	{
		return imageFile;
	}

	public void setImageFile(String imageFile)
	{
		this.imageFile = imageFile;
	}

	public Integer height;
	public Integer width;

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public Timestamp lastEditDate;

	public Timestamp getLastEditDate()
	{
		return lastEditDate;
	}

	public void setLastEditDate(Timestamp lastEditDate)
	{
		this.lastEditDate = lastEditDate;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MapEntity mapEntity = (MapEntity) o;

		if (mapId != mapEntity.mapId) return false;
		if (bnetUrl != null ? !bnetUrl.equals(mapEntity.bnetUrl) : mapEntity.bnetUrl != null) return false;
		if (imageFile != null ? !imageFile.equals(mapEntity.imageFile) : mapEntity.imageFile != null) return false;
		if (name != null ? !name.equals(mapEntity.name) : mapEntity.name != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = mapId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (bnetUrl != null ? bnetUrl.hashCode() : 0);
		result = 31 * result + (imageFile != null ? imageFile.hashCode() : 0);
		return result;
	}
}
