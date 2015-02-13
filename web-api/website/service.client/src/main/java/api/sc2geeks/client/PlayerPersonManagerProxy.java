package api.sc2geeks.client;

import api.sc2geeks.entity.SearchInput;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.entity.playerperson.PlayerPersonRequestInfo;
import api.sc2geeks.entity.playerperson.PlayerPersonWithRelatedInfo;
import api.sc2geeks.service.PlayerPersonManager;
import com.google.gson.reflect.TypeToken;
import com.sc2geeks.commons.rest.RestServiceHelper;
import com.sc2geeks.commons.web.UrlUTF8Encoder;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/31/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPersonManagerProxy implements PlayerPersonManager
{
	ServiceConfig serviceConfig = ServiceConfig.getPlayerServiceConfig();
	private static Type searchResultType = new TypeToken<SearchResult<PlayerPerson>>(){}.getType();
	private static SearchResult<PlayerPerson> dummyResult = new SearchResult<PlayerPerson>();

	@Override
	public SearchResult<PlayerPerson> getPlayerPersons(SearchInput searchInput, boolean withReplaysOnly)
	{
		String url = serviceConfig.getServiceUrl() + "search.json?" +
				ServiceUrlBuilder.getParameterString(ServiceUrlBuilder.buildUrlParameters(searchInput, serviceConfig));
		if (withReplaysOnly)
			url = url + "&withReplaysOnly=true";

		return RestServiceHelper.restGet(url, dummyResult.getClass(), searchResultType);
	}

	private static Type listStringType = new TypeToken<List<String>>(){}.getType();
	private static List<String> dummyListString = new ArrayList<String>(0);
	@Override
	public List<String> getAllTeams()
	{
		String url = serviceConfig.getServiceUrl() + "getAllTeams.json?";

		return RestServiceHelper.restGet(url, dummyListString.getClass(), listStringType);
	}

	@Override
	public PlayerPerson getPlayerPerson(int personId)
	{
		String url = serviceConfig.getServiceUrl() + "getInfo.json?playerId=" + personId;

		return RestServiceHelper.restGet(url, PlayerPerson.class, new TypeToken<PlayerPerson>(){}.getType());
	}

	@Override
	public PlayerPersonWithRelatedInfo getPlayerInfo(PlayerPersonRequestInfo requestInfo)
	{
		String url = serviceConfig.getServiceUrl() + "getInfo.json?playerId=" + requestInfo.getPersonId()
				+ "&showRelatedInfo=true&numberOfReplays=" + requestInfo.getNumberOfReplays()
				+ "&numberOfTeamMates=" + requestInfo.getNumberOfTeamMates()
				+ "&numberOfSameRacePlayers=" + requestInfo.getNumberOfSameRacePlayers();

		return RestServiceHelper.restGet(url, PlayerPersonWithRelatedInfo.class, new TypeToken<PlayerPersonWithRelatedInfo>(){}.getType());
	}
	@Override
	public void saveViewCounter(String replayId, String fromIP, Date inDate)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm S");
		// String
		String url = serviceConfig.getServiceUrl() + "saveViewCounter.json?replayId=" +
				replayId + "&fromIP=" + UrlUTF8Encoder.encode(fromIP) +
				"&inDate=" + UrlUTF8Encoder.encode(formatter.format(inDate));

		RestServiceHelper.restGet(url, null);
	}
}
