package api.sc2geeks.entity.playerperson;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 9/29/12
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPersonRequestInfo
{
	int personId;
	int numberOfReplays;
	int numberOfTeamMates;
	int numberOfSameRacePlayers;

	public int getPersonId()
	{
		return personId;
	}

	public void setPersonId(int personId)
	{
		this.personId = personId;
	}

	public int getNumberOfReplays()
	{
		return numberOfReplays;
	}

	public void setNumberOfReplays(int numberOfReplays)
	{
		this.numberOfReplays = numberOfReplays;
	}

	public int getNumberOfTeamMates()
	{
		return numberOfTeamMates;
	}

	public void setNumberOfTeamMates(int numberOfTeamMates)
	{
		this.numberOfTeamMates = numberOfTeamMates;
	}

	public int getNumberOfSameRacePlayers()
	{
		return numberOfSameRacePlayers;
	}

	public void setNumberOfSameRacePlayers(int numberOfSameRacePlayers)
	{
		this.numberOfSameRacePlayers = numberOfSameRacePlayers;
	}
}
