package api.sc2geeks.service.imp;

import api.sc2geeks.entity.RefinementSortMethod;
import api.sc2geeks.entity.SortOrder;
import org.apache.solr.client.solrj.response.FacetField;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/14/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrCountComparator implements Comparator<FacetField.Count>
{
	RefinementSortMethod sortMethod;
	SortOrder sortOrder;

	public SolrCountComparator(RefinementSortMethod method, SortOrder order)
	{
		sortMethod = method;
		sortOrder = order;
	}

	@Override
	public int compare(FacetField.Count o1, FacetField.Count o2)
	{
		int diff;
		switch (sortMethod)
		{
			case Count:
				diff = (int)(o1.getCount() - o2.getCount());
				break;
			case Name:
			default:
				diff = o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
				break;
		}
		return sortOrder == SortOrder.Asc ? diff : 0 - diff;
	}
}
