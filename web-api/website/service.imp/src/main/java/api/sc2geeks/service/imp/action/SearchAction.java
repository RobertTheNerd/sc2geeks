package api.sc2geeks.service.imp.action;

import api.sc2geeks.entity.Replay;
import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.service.imp.ReplayManagerImp;
import api.sc2geeks.service.imp.SolrConfig;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchAction extends CommonSearchBase
{
	private static SolrConfig solrConfig = SolrConfig.getReplayInstance();

	protected SolrConfig getSolrConfig()
	{
		return solrConfig;
	}

	@Override
	protected String doExecute()
	{
		try
		{
			SearchResult<Replay> result = new ReplayManagerImp().search(searchInput);
			setValue("returnValue", result);
		} catch (Exception e){}

		return SUCCESS;
	}
}
