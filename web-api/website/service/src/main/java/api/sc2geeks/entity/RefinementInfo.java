package api.sc2geeks.entity;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/11/12
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RefinementInfo
{
	RefinementField refinementField;
	RefinementSortMethod sortMethod;
	SortOrder sortOrder;
	int maxCount;

	public RefinementInfo(){}

	public RefinementInfo(RefinementInfo info)
	{
		this.refinementField = info.refinementField;
		this.sortMethod = info.sortMethod;
		this.sortOrder = info.sortOrder;
		this.maxCount = info.maxCount;
	}

	public RefinementField getRefinementField()
	{
		return refinementField;
	}

	public void setRefinementField(RefinementField refinementField)
	{
		this.refinementField = refinementField;
	}

	public RefinementSortMethod getSortMethod()
	{
		return sortMethod;
	}

	public void setSortMethod(RefinementSortMethod sortMethod)
	{
		this.sortMethod = sortMethod;
	}

	public SortOrder getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	/**
	 *
	 * @return max number of refinements to be returned.
	 */
	public int getMaxCount()
	{
		return maxCount;
	}

	public void setMaxCount(int maxCount)
	{
		this.maxCount = maxCount;
	}
}
