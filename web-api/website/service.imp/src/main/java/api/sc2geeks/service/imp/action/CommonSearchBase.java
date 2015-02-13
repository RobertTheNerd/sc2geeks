package api.sc2geeks.service.imp.action;

import api.sc2geeks.entity.*;
import api.sc2geeks.service.imp.SolrConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 7/30/12
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CommonSearchBase extends ActionBase
{

	SearchType searchType = SearchType.WithRefinement;
	List<Integer> refinementMaxCounts;
	List<RefinementField> refinementFields;
	List<RefinementSortMethod> sortMethods;
	List<SortOrder> sortOrders;
	int pageSize = 20;
	int pageNo = 1;
	String searchTerm;
	SearchInput searchInput;


	protected void init()
	{
		super.init();
		parseSearchInput();
	}

	/* parameters */
	public void setSearchType(String searchType)
	{
		try
		{
			this.searchType = SearchType.valueOf(searchType);
		}
		catch(Exception e){}
	}

    public void setQ(String input)
    {
        searchTerm = input;
    }

	/**
	 * rFields=xxx,xxx
	 * @param input
	 */
	public void setRFields(String input)
	{
		refinementFields = getValues(input);
	}

	public void setRMaxCounts(String input)
	{
		List<String> parts = getStrings(input);
		refinementMaxCounts = new ArrayList<Integer>(parts.size());
		for (String s : parts)
		{
			try
			{
				refinementMaxCounts.add(Integer.parseInt(s));
			}catch(Exception e){}
		}
	}
	public void setRSort(String input)
	{
		List<String> sortInfoList = getStrings(input);

		// parse sort info
		if (sortInfoList != null && sortInfoList.size() > 0)
		{
			sortMethods = new ArrayList<RefinementSortMethod>(sortInfoList.size());
			sortOrders = new ArrayList<SortOrder>(sortInfoList.size());
			for (String s : sortInfoList)
			{
				String[] parts = s.split(" ");
				if (parts == null || parts.length != 2)
					continue;

				try
				{
					sortMethods.add(RefinementSortMethod.valueOf(parts[0]));
					sortOrders.add(SortOrder.valueOf(parts[1]));
				}catch(Exception e){}
			}
		}
	}

	public void setPageSize(String ps)
	{
		try
		{
			pageSize = Integer.parseInt(ps);
		}catch (Exception e){}
	}
	public void setPage(String pn)
	{
		try
		{
			pageNo = Integer.parseInt(pn);
		}catch (Exception e){}
	}
	/** parameter regions end **/

	protected static List<String> getStrings(String input)
	{
		String[] parts = input.split(",");
		if (parts == null || parts.length == 0)
			return null;

		return Arrays.asList(parts);
	}
	protected static List<RefinementField> getValues(String input)
	{
		String[] parts = input.split(",");
		if (parts == null || parts.length == 0)
			return null;


		ArrayList result = new ArrayList<RefinementField>(parts.length);
		for (String s: parts)
		{
			try
			{
				result.add(Enum.valueOf(RefinementField.class, s));

			}catch(Exception e){}
		}
		return result;
	}

	protected SearchInput parseSearchInput()
	{
		try
		{
			searchInput = new SearchInput();
			searchInput.setSearchTerms(searchTerm);

			searchInput.setSearchFilters(parseSearchFilters());

			searchInput.setSearchType(searchType);
			searchInput.setPageSize(pageSize);
			searchInput.setCurrentPage(pageNo);

			// assign refinement info
			if (refinementFields == null ||
					sortMethods == null ||
					sortOrders == null ||
					refinementMaxCounts == null ||
					refinementFields.size() != sortMethods.size() ||
					refinementFields.size() != sortOrders.size() ||
					refinementFields.size() != refinementMaxCounts.size())
			{
			} else
			{
				List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>(refinementFields.size());
				for (int i = 0; i < refinementFields.size(); i++)
				{
					RefinementInfo info = new RefinementInfo();
					info.setRefinementField(refinementFields.get(i));
					info.setSortMethod(sortMethods.get(i));
					info.setSortOrder(sortOrders.get(i));
					info.setMaxCount(refinementMaxCounts.get(i));
					refinementInfoList.add(info);
				}
				searchInput.setRefinementFields(refinementInfoList);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return searchInput;
	}

	protected List<SearchFilter> parseSearchFilters()
	{
		java.util.Map paramMap = request.getParameterMap();
		if (paramMap == null)
			return null;

		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		for (Object name : paramMap.keySet())
		{
			RefinementField field = getSolrConfig().getRefinementFieldByUrlParamName(name.toString());
			if (field == null)
				continue;
			String[] values = (String[]) paramMap.get(name);
			if (values == null || values.length == 0)
				continue;

			SearchFilter filter = new SearchFilter();
			filter.setRefinementField(field);
			filter.setValues(Arrays.asList(values));
			filterList.add(filter);
		}
		return filterList;
	}

	abstract protected SolrConfig getSolrConfig();
}
