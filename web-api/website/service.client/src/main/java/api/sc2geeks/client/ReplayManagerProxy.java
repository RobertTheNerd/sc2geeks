package api.sc2geeks.client;

import api.sc2geeks.entity.*;
import api.sc2geeks.service.ReplayManager;
import com.google.gson.reflect.TypeToken;
import com.sc2geeks.commons.rest.RestServiceHelper;
import com.sc2geeks.commons.web.UrlUTF8Encoder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayManagerProxy implements ReplayManager
{
	private static ServiceConfig serviceConfig = ServiceConfig.getReplayServiceConfig();
	private static Type searchResultType = new TypeToken<SearchResult<Replay>>(){}.getType();
	private static SearchResult<Replay> dummyResult = new SearchResult<Replay>();


	@Override
	public SearchResult<Replay> search(SearchInput input)
	{
		String url = serviceConfig.getServiceUrl() + "search.json?" +
				ServiceUrlBuilder.getParameterString(ServiceUrlBuilder.buildUrlParameters(input, serviceConfig)) ;

		return RestServiceHelper.restGet(url, dummyResult.getClass(), searchResultType);
	}

	@Override
	public void saveDownloadCounter(String replayId, String fromIP, Date inDate)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm S");
		// String
		String url = serviceConfig.getServiceUrl() + "saveDownloadCounter.json?replayId=" +
				replayId + "&fromIP=" + UrlUTF8Encoder.encode(fromIP) +
				"&inDate=" + UrlUTF8Encoder.encode(formatter.format(inDate));

		RestServiceHelper.restGet(url, null);
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

	@Override
	public ReplayWithRelatedInfo getReplayWithRelatedInfo(String replayId)
	{
		if (StringUtils.isNotBlank(replayId))
		{
			String url = serviceConfig.getServiceUrl() + "getInfo.json?replayId=" + UrlUTF8Encoder.encode(replayId) + "&withRelatedInfo=true";
			return RestServiceHelper.restGet(url, ReplayWithRelatedInfo.class);
		}

		return null;
	}

	@Override
	public ReplayWithAllInfo getReplayWithAllInfo(String replayId)
	{
		if (StringUtils.isNotBlank(replayId))
		{
			String url = serviceConfig.getServiceUrl() + "getInfo.json?replayId=" + UrlUTF8Encoder.encode(replayId) + "&withAllInfo=true";
			return RestServiceHelper.restGet(url, ReplayWithAllInfo.class);
		}

		return null;
	}

	@Override
	public Replay getReplay(String replayId)
	{
		if (StringUtils.isNotBlank(replayId))
		{
			String url = serviceConfig.getServiceUrl() + "getInfo.json?replayId=" + UrlUTF8Encoder.encode(replayId);
			return RestServiceHelper.restGet(url, Replay.class);
		}
		return null;
	}


}
