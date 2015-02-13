<%@ tag import="com.opensymphony.xwork2.ActionContext" %>
<%@ tag import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ tag import="com.sc2geeks.front.RequestContext" %>
<%@ tag import="static com.sc2geeks.front.ui.ResourceHelper.*" %>
<%@ tag import="static com.sc2geeks.front.ui.PageUrlBuilder.*" %>
<%@ tag import="com.sc2geeks.front.WebsiteConfig" %>
<%--
  User: robert
  Date: 4/14/12
  Time: 8:25 PM
--%>
<%@ tag language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="rd" tagdir="/WEB-INF/tags/replay-detail" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%!
	private ValueStack valueStack;
	private WebsiteConfig websiteConfig = WebsiteConfig.getInstance();
	private RequestContext requestContext;

	private <T> T getValue(String name)
	{
		return (T) valueStack.findValue(name);
	}
%>
<%
	valueStack = ActionContext.getContext().getValueStack();
	requestContext = getValue("requestContext");
%>