package api.sc2geeks.service.imp;

import api.sc2geeks.entity.RefinementField;

/**
 * Created with IntelliJ IDEA.
 * @org.apache.xbean.XBean
 * User: robert
 * Date: 4/12/12
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrField
{
	private String fieldName;
	private RefinementField refinementField;
	private String urlParameterName;

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public RefinementField getRefinementField()
	{
		return refinementField;
	}

	public String getUrlParameterName()
	{
		return urlParameterName;
	}

	public void setUrlParameterName(String urlParameterName)
	{
		this.urlParameterName = urlParameterName;
	}

	public void setRefinementField(RefinementField refinementField)
	{
		this.refinementField = refinementField;
	}
}
