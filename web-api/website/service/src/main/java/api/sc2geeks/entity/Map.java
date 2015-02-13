package api.sc2geeks.entity;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Map extends EntityWithImageInfo
{
	private int mapId;
	private String mapName;
	private String mapImageName;

	public int getMapId()
	{
		return mapId;
	}

	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	public String getMapName()
	{
		return mapName;
	}

	public void setMapName(String mapName)
	{
		this.mapName = mapName;
	}

	public String getMapImageName()
	{
		return mapImageName;
	}

	public void setMapImageName(String mapImageName)
	{
		this.mapImageName = mapImageName;
	}

}
