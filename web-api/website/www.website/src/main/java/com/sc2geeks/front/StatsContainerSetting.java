package com.sc2geeks.front;

import api.sc2geeks.entity.RefinementField;

/**
 * Created by robert on 1/2/15.
 */
public class StatsContainerSetting {
	RefinementField refinementField;
	String divId;
	String customStyle;
	String className;
	String chartType;
	String title;

	public RefinementField getRefinementField() {
		return refinementField;
	}

	public void setRefinementField(RefinementField refinementField) {
		this.refinementField = refinementField;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public String getCustomStyle() {
		return customStyle;
	}

	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
