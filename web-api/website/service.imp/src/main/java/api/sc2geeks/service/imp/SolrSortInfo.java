package api.sc2geeks.service.imp;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 8/1/12
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class SolrSortInfo
{
	private String sortField;
	private SolrQuery.ORDER sortOrder;

	public SolrSortInfo(){}

	public SolrSortInfo(String sortField, SolrQuery.ORDER sortOrder)
	{
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public String getSortField()
	{
		return sortField;
	}

	public void setSortField(String sortField)
	{
		this.sortField = sortField;
	}

	public SolrQuery.ORDER getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(SolrQuery.ORDER sortOrder)
	{
		this.sortOrder = sortOrder;
	}
}
