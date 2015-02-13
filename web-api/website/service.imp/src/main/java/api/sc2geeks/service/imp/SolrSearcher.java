package api.sc2geeks.service.imp;

import api.sc2geeks.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.net.MalformedURLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/31/12
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrSearcher
{
	SolrConfig solrConfig;

	public SolrConfig getSolrConfig()
	{
		return solrConfig;
	}

	public void setSolrConfig(SolrConfig solrConfig)
	{
		this.solrConfig = solrConfig;
	}

	public SolrSearcher(SolrConfig solrConfig)
	{
		this.solrConfig = solrConfig;
	}

	public <T> SearchResult<T> search(SearchInput searchInput, Class<T> type)
	{
		return search(searchInput, null, type);
	}

	public <T> SearchResult<T> search(SearchInput searchInput, List<SolrSortInfo> sortInfoList, Class<T> type)
	{
		return search(searchInput, null, null, type);
	}

	public <T> SearchResult<T> search(SearchInput searchInput, java.util.Map<String, String> extraParams, List<SolrSortInfo> sortInfoList, Class<T> type)
	{
		if (searchInput == null)
			return new SearchResult<T>();

		Hashtable<SolrField, List<String>> filterFieldMap = getSolrFilterFieldMap(searchInput);
		SearchResult<T> result = null;
		try
		{
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(solrConfig.getSolrServerUrl());
			SolrQuery query = new SolrQuery();

			// setup search parameters
			query.setQuery(StringUtils.isBlank(searchInput.getSearchTerms()) ? "*" : searchInput.getSearchTerms());

			boolean allowFacet = false;

			// faceted search
			if (searchInput.getRefinementFields() != null && searchInput.getRefinementFields().size() > 0)
			{
				switch (searchInput.getSearchType())
				{
					case WithRefinement:
					case RefinementOnly:
						for (RefinementInfo refinementInfo : searchInput.getRefinementFields())
						{
							allowFacet = true;
							String fieldName = solrConfig.getSolrField(refinementInfo.getRefinementField()).getFieldName();
							query.add("facet.field", fieldName);
							query.add("f." + fieldName + ".facet.sort", getSortString(refinementInfo));
							if (refinementInfo.getMaxCount() > 0 && isSortingSupported(refinementInfo))
							{
								query.add("f." + fieldName + ".facet.limit", refinementInfo.getMaxCount() + "");
							}
							else
							{
								query.add("f." + fieldName + ".facet.limit", "-1");
							}
						}
						break;
					case NoRefinement:  // do noting for faceting
					default:
						break;
				}
			}
			if (allowFacet)
			{
				query.add("facet", "true");
				query.setFacetMinCount(1);
			}

			// filter query
			if (filterFieldMap != null && filterFieldMap.size() > 0)
			{
				for (SolrField filter : filterFieldMap.keySet())
				{
					query.addFilterQuery(getFilterQuery(filter, filterFieldMap.get(filter)));
				}
			}

			// extra parameters
			if (extraParams != null)
			{
				for (String name : extraParams.keySet())
				{
					query.add(name.trim(), extraParams.get(name));
				}
			}

			// sorting
			if (searchInput.getSearchType() != SearchType.RefinementOnly)
			{
				if (sortInfoList != null)
				{
					for (SolrSortInfo sortInfo : sortInfoList)
					{
						query.addSortField(sortInfo.getSortField(), sortInfo.getSortOrder());
					}
				}

				if (solrConfig.getExtraSortInfo() != null)
				{
					for (SolrSortInfo sortInfo : solrConfig.getExtraSortInfo())
					{
						query.addSortField(sortInfo.getSortField(), sortInfo.getSortOrder());
					}
				}

				// set page size & page no.
				query.setRows(searchInput.getPageSize());
				query.setStart(searchInput.getPageSize() * (searchInput.getCurrentPage() - 1));
			}

			if (searchInput.getSearchType() == SearchType.RefinementOnly)
			{
				// do not get replays
				query.setRows(0);
			}

			QueryResponse rsp = null;
			try
			{
				rsp = server.query(query);

				// for zero search result, search using wildcard
				if (rsp.getResults().getNumFound() == 0 && searchInput.getSearchTerms().compareTo("*") != 0)
				{
					query.setQuery("*" + query.getQuery() + "*");
					rsp = server.query(query);
				}

				// if start is bigger than total number of items
				if (rsp.getResults().getNumFound() > 0 && rsp.getResults().getNumFound() <= rsp.getResults().getStart())
				{
					searchInput.setCurrentPage((int) (rsp.getResults().getNumFound() / searchInput.getPageSize()));
					if (rsp.getResults().getNumFound() % searchInput.getPageSize() != 0)
					{
						searchInput.setCurrentPage(searchInput.getCurrentPage() + 1);
					}
					query.setStart(searchInput.getPageSize() * (searchInput.getCurrentPage() - 1));
					rsp = server.query(query);
				}

				result = SearchResultParser.parseSearchResult(searchInput, rsp, solrConfig, type);

				// page size & page no.
				result.setPageSize(searchInput.getPageSize());
				result.setCurrentPage(searchInput.getCurrentPage());

			} catch (SolrServerException e)
			{
				e.printStackTrace();
			}

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public <T> List<T> search(String searchTerms, java.util.Map<String, String> params, Class<T> typeClass)
	{
		try
		{
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(solrConfig.getSolrServerUrl());
			SolrQuery query = new SolrQuery();

			// setup search parameters
			query.setQuery(searchTerms);

			if (params != null)
			{
				for (String name : params.keySet())
				{
					if (StringUtils.isNotBlank(name))
						query.add(name.trim(), params.get(name));
				}
			}

			QueryResponse rsp = server.query(query);

			// for zero search result, search using wildcard
			if (rsp.getResults().getNumFound() == 0 )
			{
				return null;
			}
			List<T> list = new ArrayList<T>((int)rsp.getResults().getNumFound());
			for (SolrDocument doc : rsp.getResults())
			{
				T entity = SearchResultParser.parseEntity(doc, typeClass);
				list.add(entity);
			}

			return list;

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	private Hashtable<SolrField, List<String>> getSolrFilterFieldMap(SearchInput input)
	{
		if (input.getSearchFilters() == null || input.getSearchFilters().size() == 0)
			return null;
		Hashtable<SolrField, List<String>> result = new Hashtable<SolrField, List<String>>(input.getSearchFilters().size());
		for (SearchFilter filter : input.getSearchFilters())
		{
			result.put(solrConfig.getSolrField(filter.getRefinementField()), filter.getValues());
		}
		return result;
	}

	private static String getSortString(RefinementInfo refinementInfo)
	{
        // special fix: multi-valued property does not support sort by index with faceting.
		if (refinementInfo.getRefinementField() == RefinementField.PlayerName ||
                refinementInfo.getRefinementField() == RefinementField.WinnerName)
            return "count";

        String method = refinementInfo.getSortMethod() == RefinementSortMethod.Count ? "count" : "index";
		return method;
	}

	static boolean isSortingSupported(RefinementInfo rInfo)
	{
        // special fix: multi-valued property does not support sort by index with faceting.
        if (rInfo.getRefinementField() == RefinementField.PlayerName ||
                rInfo.getRefinementField() == RefinementField.WinnerName) {
            if (rInfo.getSortMethod() == RefinementSortMethod.Name)
                return false;
        }

        return (rInfo.getSortMethod() == RefinementSortMethod.Count && rInfo.getSortOrder() == SortOrder.Desc)
                ||
                (rInfo.getSortMethod() == RefinementSortMethod.Name && rInfo.getSortOrder() == SortOrder.Asc);
    }

    private static String getFilterQuery(SolrField solrField, List<String> values)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(solrField.getFieldName()).append((":("));
		for (int i = 0; i < values.size(); i++)
		{
			sb.append("\"").append(values.get(i)).append("\"");
			if (i != values.size() - 1)
			{
				sb.append(" OR ");
			}
		}
		sb.append(")");

		return sb.toString();
	}
}
