package api.sc2geeks.service;

import api.sc2geeks.entity.SearchInput;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.entity.playerperson.PlayerPersonRequestInfo;
import api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/29/12
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PlayerPersonManager
{
	SearchResult<PlayerPerson> getPlayerPersons(SearchInput searchInput, boolean withReplaysOnly);

	@Deprecated
	List<String> getAllTeams();

	PlayerPerson getPlayerPerson(int personId);

	PlayerPersonWithRelatedInfo getPlayerInfo(PlayerPersonRequestInfo requestInfo);
	void saveViewCounter(String replayId, String fromIP, Date inDate);
}
