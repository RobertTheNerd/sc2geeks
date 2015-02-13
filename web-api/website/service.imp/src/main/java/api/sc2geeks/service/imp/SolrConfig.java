package api.sc2geeks.service.imp;

import api.sc2geeks.entity.RefinementField;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/11/11
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @org.apache.xbean.XBean element="simple" rootElement="true"
 */
public class SolrConfig
{
	private String solrServerUrl;
	private int defaultPageSize;
	private List<SolrField> solrFields;
	private List<SolrSortInfo> extraSortInfo;
	private Hashtable<String, SolrField> solrFieldMapByName;
	private Hashtable<RefinementField, SolrField> solrFieldMapByField;
	private Hashtable<String, RefinementField> urlParamRefinementMap;

	private static SolrConfig _replaySolrConfig = new SolrConfig();
	private static SolrConfig _playerPersonSolrConfig = new SolrConfig();

	static
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
		_replaySolrConfig = context.getBean("replaySolrConfig", SolrConfig.class);
		_playerPersonSolrConfig = context.getBean("playerPersonSolrConfig", SolrConfig.class);
	}

	private SolrConfig(){}

	public static SolrConfig getReplayInstance()
	{
		return _replaySolrConfig;
	}
	public static SolrConfig getPlayerInstance()
	{
		return _playerPersonSolrConfig;
	}

	public int getDefaultPageSize()
	{
		return defaultPageSize;
	}

	public void setDefaultPageSize(int defaultPageSize)
	{
		this.defaultPageSize = defaultPageSize;
	}

	public String getSolrServerUrl()
	{
		return solrServerUrl;
	}

	public void setSolrServerUrl(String solrServerUrl)
	{
		this.solrServerUrl = solrServerUrl;
	}

	public List<SolrField> getSolrFields()
	{
		return solrFields;
	}

	/**
	 * @org.apache.xbean.Property alias="solrFields" nestedType="api.sc2geeks.service.imp.SolrField"

	 * @param solrFields
	 */
	public void setSolrFields(List<SolrField> solrFields)
	{
		this.solrFields = solrFields;
		generateSolrFieldMap();
		// todo:
	}

	public List<SolrSortInfo> getExtraSortInfo()
	{
		return extraSortInfo;
	}

	public void setExtraSortInfo(List<SolrSortInfo> extraSortInfo)
	{
		this.extraSortInfo = extraSortInfo;
	}

	public SolrField getSolrField(String fieldName)
	{
		if (solrFieldMapByName == null)
			return null;
		return solrFieldMapByName.get(fieldName);
	}

	public SolrField getSolrField(RefinementField field)
	{
		if (solrFieldMapByField == null)
			return null;
		return solrFieldMapByField.get(field);
	}

	public RefinementField getRefinementFieldByUrlParamName(String param)
	{
		if (urlParamRefinementMap == null || param == null)
			return null;
		return urlParamRefinementMap.get(param.toLowerCase().trim());
	}

	private void generateSolrFieldMap()
	{
		if (solrFields == null || solrFields.size() == 0)
		{
			solrFieldMapByField = solrFieldMapByField = null;
			return;
		}

		solrFieldMapByName = new Hashtable<String, SolrField>(solrFields.size());
		solrFieldMapByField = new Hashtable<RefinementField, SolrField>(solrFields.size());
		urlParamRefinementMap = new Hashtable<String, RefinementField>(solrFields.size());

		for(SolrField solrField : solrFields)
		{
			solrFieldMapByName.put(solrField.getFieldName(), solrField);
			solrFieldMapByField.put(solrField.getRefinementField(), solrField);
			urlParamRefinementMap.put(solrField.getUrlParameterName().toLowerCase().trim(), solrField.getRefinementField());
		}
	}
}
