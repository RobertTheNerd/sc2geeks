<%@ page import="static com.sc2geeks.front.ui.ResourceHelper.buildImageUrl" %>
<%@ page import="static com.sc2geeks.front.ui.ResourceHelper.buildCssUrl" %>
<%@ page import="static com.sc2geeks.front.ui.ResourceHelper.*" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%--
  User: robert
  Date: 4/15/12
  Time: 12:01 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="base.jsp"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title><%= StringEscapeUtils.escapeHtml4(pageSEOInfo.getPageTitle()) %></title>
	<meta name="keywords" content="<%= StringEscapeUtils.escapeHtml4(pageSEOInfo.getPageKeyword()) %>">
	<meta name="description" content="<%= StringEscapeUtils.escapeHtml4(pageSEOInfo.getPageDescription()) %>">
	<link rel="shortcut icon" href="<%= buildImageUrl("sc2geeks.ico")%>" />
	<%
		if (
				StringUtils.isNotBlank(pageSEOInfo.getCanonicalUrl()))
		{
			%>
	<link rel="canonical" href="<%= pageSEOInfo.getCanonicalUrl() %>">
	<%
		}
	%>
	<link href="<%= buildCssUrl("base.v20150106.css")%>" rel="stylesheet" type="text/css" />
	<link href="<%= buildCssUrl("jquery.ui.v20141105.css")%>" rel="stylesheet" type="text/css" />
	<%
	    // extra css files
        if (cssFiles != null && cssFiles.size() > 0)
        {
            for (String cssFile : cssFiles)
            {
                %>
    <link href="<%= buildCssUrl(cssFile)%>" rel="stylesheet" type="text/css" />
    <%
            }
        }
    %>
	<script src="<%= buildJsUrl("modernizr-2.5.3.min.js")%>"></script>
	<%
		// extra js files
		if (jsFiles != null && jsFiles.size() > 0)
		{
			for (String jsFile : jsFiles)
			{
	%>
	<link href="<%= buildCssUrl(jsFile)%>" rel="stylesheet" type="text/css" />
	<%
			}
		}
	%>

	<script>
		var pageContext={
			tab:'<%= requestContext.getPageTab() %>'
		};
	</script>

</head>