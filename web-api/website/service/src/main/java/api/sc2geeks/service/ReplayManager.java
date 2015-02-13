package api.sc2geeks.service;


import api.sc2geeks.entity.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReplayManager
{
	/* methods for front end */
	SearchResult<Replay> search(SearchInput input);
	void saveDownloadCounter(String replayId, String fromIP, Date inDate);
	void saveViewCounter(String replayId, String fromIP, Date inDate);
	ReplayWithRelatedInfo getReplayWithRelatedInfo(String replayId);
	ReplayWithAllInfo getReplayWithAllInfo(String replayId);
	Replay getReplay(String replayId);

	/* methods for crawler & backend */


}
