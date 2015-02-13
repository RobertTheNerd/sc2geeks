package com.sc2geeks.front.action.progamer;

import api.sc2geeks.entity.RefinementField;
import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.SearchInput;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.playerperson.PlayerPersonRequestInfo;
import api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo;
import api.sc2geeks.service.PlayerPersonManager;
import api.sc2geeks.service.ReplayManager;
import com.sc2geeks.front.action.ServiceFactory;
import com.sc2geeks.front.ui.PageUrlBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 9/25/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgamerDetailAction extends ProgamerActionBase
{

	private int playerPersonId = -1;

	@Override
	protected String doExecute()
	{
		parsePlayerPersonId();

		if (playerPersonId == -1)
		{
			return NOT_FOUND;
		}

		// get player with related info
		PlayerPersonManager manager = ServiceFactory.createPlayerPersonManager();
		PlayerPersonRequestInfo request = new PlayerPersonRequestInfo();
		request.setPersonId(playerPersonId);
		request.setNumberOfReplays(10);
		request.setNumberOfTeamMates(6);
		request.setNumberOfSameRacePlayers(6);
		PlayerPersonWithRelatedInfo playerInfo = manager.getPlayerInfo(request);

		if (playerInfo == null || playerInfo.getPlayerPerson() == null)
			return NOT_FOUND;

		setValue("playerInfo", playerInfo);
		setValue("playerPerson", playerInfo.getPlayerPerson());
		setValue("replaySearchResult", playerInfo.getReplays());

		pageSEOInfo.setCanonicalUrl(PageUrlBuilder.getProgamerDetailPage(playerInfo.getPlayerPerson()));

		return SUCCESS;
	}


	private void parsePlayerPersonId()
	{
		String actionName = requestContext.getActionPath();

		String[] parts = actionName.split("-");
		if (parts == null || parts.length == 0)
			return;

		try
		{
			playerPersonId = Integer.parseInt(parts[parts.length - 1]);
		}
		catch(Exception e){}
	}

	private SearchResult<Replay> getReplaysByPlayerPerson(int personId)
	{
		ReplayManager replayManager = ServiceFactory.createReplayManager();
		SearchInput searchInput = new SearchInput();

		searchInput.addSearchFilter(RefinementField.ProgamerId, Integer.toString(personId));
		searchInput.setPageSize(10);

		return replayManager.search(searchInput);
	}
}
