package api.sc2geeks.service.imp.action;


import api.sc2geeks.entity.SearchResult;
import api.sc2geeks.entity.playerperson.PlayerPerson;
import api.sc2geeks.service.imp.PlayerPersonManagerImp;
import api.sc2geeks.service.imp.SolrConfig;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/30/12
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerSearchAction extends CommonSearchBase
{
	private static SolrConfig solrConfig = SolrConfig.getPlayerInstance();
	private boolean withReplaysOnly = false;

	public void setWithReplaysOnly(boolean withReplaysOnly)
	{
		this.withReplaysOnly = withReplaysOnly;
	}

	@Override
	protected String doExecute()
	{
		try
		{
			SearchResult<PlayerPerson> result = new PlayerPersonManagerImp().getPlayerPersons(searchInput, withReplaysOnly);
			setValue("returnValue", result);
		} catch (Exception e)
		{
		}

		return SUCCESS;
	}

	protected SolrConfig getSolrConfig()
	{
		return solrConfig;
	}
}
