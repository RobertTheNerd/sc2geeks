package api.sc2geeks.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/11/11
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class SearchInput
{
	private static final int PAGE_SIZE = 20;
	private String searchTerms = "*";

	List<SearchFilter> searchFilters;

	List<RefinementInfo> refinementFields;

	private int pageSize = 20;
	private int currentPage = 1;

	private SearchType searchType = SearchType.WithRefinement;

	public String getSearchTerms()
	{
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms)
	{
		this.searchTerms = StringUtils.isBlank(searchTerms) ? "*" : searchTerms.trim();
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize == 0 ? PAGE_SIZE : pageSize;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public SearchType getSearchType()
	{
		return searchType;
	}

	public void setSearchType(SearchType searchType)
	{
		this.searchType = searchType;
	}

	public List<SearchFilter> addSearchFilter(RefinementField refinementField, String value)
	{
		if (searchFilters == null)
			searchFilters = new ArrayList<SearchFilter>();
		SearchFilter filter = new SearchFilter();
		filter.setRefinementField(refinementField);
		filter.setValues(Arrays.asList(value));
		searchFilters.add(filter);
		return searchFilters;
	}
	public List<SearchFilter> getSearchFilters()
	{
		return searchFilters;
	}

	public void setSearchFilters(List<SearchFilter> searchFilters)
	{
		this.searchFilters = searchFilters;
	}

	public List<RefinementInfo> getRefinementFields()
	{
		return refinementFields;
	}

	public void setRefinementFields(List<RefinementInfo> refinementFields)
	{
		this.refinementFields = refinementFields;
	}

	public List<RefinementInfo> addRefinementInfo(RefinementInfo info)
	{
		return refinementFields = addElement(refinementFields, info);
	}
	/* filter end */

	public boolean hasEmptySearchTerm()
	{
		return StringUtils.isBlank(searchTerms) || searchTerms.trim().compareTo("*") == 0;
	}

	public String toString()
	{
		return super.toString();
	}

	private static <E> List<E> addElement(List<E> list, E element)
	{
		if (list == null)
			list = new ArrayList<E>();
		list.add(element);
		return list;
	}
}
