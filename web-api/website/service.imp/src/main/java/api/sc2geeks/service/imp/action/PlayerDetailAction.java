package api.sc2geeks.service.imp.action;

import api.sc2geeks.entity.playerperson.PlayerPersonRequestInfo;
import api.sc2geeks.service.imp.PlayerPersonManagerImp;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 9/24/12
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerDetailAction extends ActionBase
{
	public static final String ACTION_GET_PLAYER = "GET-PLAYER";
	public static final String ACTION_GET_ALL_TEAMS = "GET-ALL-TEAMS";


	private int playerId = -1;
	private boolean showRelatedInfo = false;
	private int numberOfReplays = 10;
	private int numberOfTeamMates = 4;
	private int numberOfSameRacePlayers = 4;
	private String actionName;


	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public void setShowRelatedInfo(boolean showRelatedInfo)
	{
		this.showRelatedInfo = showRelatedInfo;
	}

	public void setNumberOfReplays(int numberOfReplays)
	{
		this.numberOfReplays = numberOfReplays;
	}

	public void setNumberOfTeamMates(int numberOfTeamMates)
	{
		this.numberOfTeamMates = numberOfTeamMates;
	}

	public void setNumberOfSameRacePlayers(int numberOfSameRacePlayers)
	{
		this.numberOfSameRacePlayers = numberOfSameRacePlayers;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	@Override
	protected String doExecute()
	{
		if (ACTION_GET_PLAYER.compareToIgnoreCase(actionName) == 0)
			return getPlayer();

		if (ACTION_GET_ALL_TEAMS.compareToIgnoreCase(actionName) == 0)
			return getAllTeams();

		return SUCCESS;
	}

	private String getPlayer()
	{
		if (playerId != -1)
		{
			if (showRelatedInfo)
			{
				PlayerPersonRequestInfo request = new PlayerPersonRequestInfo();
				request.setPersonId(playerId);
				request.setNumberOfReplays(numberOfReplays);
				request.setNumberOfTeamMates(numberOfTeamMates);
				request.setNumberOfSameRacePlayers(numberOfSameRacePlayers);
				setValue("returnValue", new PlayerPersonManagerImp().getPlayerInfo(request));

			}
			else
				setValue("returnValue", new PlayerPersonManagerImp().getPlayerPerson(playerId));
		}
		return SUCCESS;
	}

	private String getAllTeams()
	{
		setValue("returnValue", new PlayerPersonManagerImp().getAllTeams());
		return SUCCESS;
	}
}
