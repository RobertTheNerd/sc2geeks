package api.sc2geeks.entity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/3/12
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayWithRelatedInfo
{
	private Replay replay;
	private List<Replay> replaysInSeries;
	private List<Replay> replaysOnMap;
	private List<Replay> replaysOfMatchup;
	private List<Replay> replaysFromPlayer1;
	private List<Replay> replaysFromPlayer2;

	public Replay getReplay()
	{
		return replay;
	}

	public void setReplay(Replay replay)
	{
		this.replay = replay;
	}

	public List<Replay> getReplaysInSeries()
	{
		return replaysInSeries;
	}

	public void setReplaysInSeries(List<Replay> replaysInSeries)
	{
		this.replaysInSeries = replaysInSeries;
	}

	public List<Replay> getReplaysOnMap()
	{
		return replaysOnMap;
	}

	public void setReplaysOnMap(List<Replay> replaysOnMap)
	{
		this.replaysOnMap = replaysOnMap;
	}

	public List<Replay> getReplaysOfMatchup()
	{
		return replaysOfMatchup;
	}

	public void setReplaysOfMatchup(List<Replay> replaysOfMatchup)
	{
		this.replaysOfMatchup = replaysOfMatchup;
	}

	public List<Replay> getReplaysFromPlayer1()
	{
		return replaysFromPlayer1;
	}

	public void setReplaysFromPlayer1(List<Replay> replaysFromPlayer1)
	{
		this.replaysFromPlayer1 = replaysFromPlayer1;
	}

	public List<Replay> getReplaysFromPlayer2()
	{
		return replaysFromPlayer2;
	}

	public void setReplaysFromPlayer2(List<Replay> replaysFromPlayer2)
	{
		this.replaysFromPlayer2 = replaysFromPlayer2;
	}
}
