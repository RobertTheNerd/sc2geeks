package api.sc2geeks.entity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/19/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchFilter
{
	RefinementField refinementField;
	List<String> values;

	public RefinementField getRefinementField()
	{
		return refinementField;
	}

	public void setRefinementField(RefinementField refinementField)
	{
		this.refinementField = refinementField;
	}

	public List<String> getValues()
	{
		return values;
	}

	public void setValues(List<String> values)
	{
		this.values = values;
	}
}
