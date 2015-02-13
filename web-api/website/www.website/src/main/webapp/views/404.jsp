<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.sc2geeks.front.WebsiteConfig" %>
<%--
  User: robert
  Date: 7/31/12
  Time: 1:07 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Object originalPage = request.getAttribute("javax.servlet.forward.request_uri");
	if (originalPage != null && StringUtils.isNotBlank(originalPage.toString()))
	{
		String originalUrl = (String) originalPage;
		int lastDot = StringUtils.lastIndexOf(originalUrl, ".");
		int lastSlash = StringUtils.lastIndexOf(originalUrl, "/");

		if (lastDot < lastSlash)
		{
			WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
			String redirectUrl = websiteConfig.getWebsiteRoot() +
					StringUtils.removeStart(originalUrl, "/");
			if (!originalUrl.endsWith("/"))
				redirectUrl += "/";

			redirectUrl += "index." + websiteConfig.getDefaultExtension();
			response.setStatus(301);
			response.setHeader("Location", redirectUrl);
			response.getOutputStream().close();
		}

	}

%>
<%@ include file="404.html" %>
