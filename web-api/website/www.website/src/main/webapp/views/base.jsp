<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ page import="com.sc2geeks.front.RequestContext" %>
<%@ page import="static com.sc2geeks.front.ui.ResourceHelper.*" %>
<%@ page import="static com.sc2geeks.front.ui.PageUrlBuilder.*" %>
<%@ page import="com.sc2geeks.front.WebsiteConfig" %>
<%@ page import="com.sc2geeks.front.ui.PageSEOInfo" %>
<%@ page import="com.sc2geeks.front.ui.PageUrlBuilder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.LinkedHashSet" %>
<%--
  User: robert
  Date: 4/14/12
  Time: 8:25 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="rd" tagdir="/WEB-INF/tags/replay-detail" %>

<%!
    private ValueStack valueStack;
    private WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
    private RequestContext requestContext;
    private PageSEOInfo pageSEOInfo;
    private PageUrlBuilder pageUrlBuilder;
    private LinkedHashSet<String> cssFiles;
    private LinkedHashSet<String> jsFiles;


    private <T> T getValue(String name)
    {
        return (T) valueStack.findValue(name);
    }

    private void setValue(String name, Object value)
    {
        valueStack.set(name, value);
    }

    private LinkedHashSet<String> addExtraCss(String fileName)
    {
	    LinkedHashSet<String> cssFiles = getValue("cssFiles");
        if (cssFiles == null)
            setValue("cssFiles", cssFiles = new LinkedHashSet<String>());

        cssFiles.add(fileName);
        return cssFiles;
    }

	private LinkedHashSet<String> addExtraJs(String fileName)
	{
		LinkedHashSet<String> jsFiles = getValue("jsFiles");
		if (cssFiles == null)
			setValue("jsFiles", cssFiles = new LinkedHashSet<String>());

		jsFiles.add(fileName);
		return jsFiles;
	}
%>
<%
	valueStack = ActionContext.getContext().getValueStack();
	requestContext = getValue("requestContext");
	pageSEOInfo = getValue("pageSEOInfo");
	pageUrlBuilder = getValue("pageUrlBuilder");
	cssFiles = getValue("cssFiles");
	jsFiles = getValue("jsFiles");
%>