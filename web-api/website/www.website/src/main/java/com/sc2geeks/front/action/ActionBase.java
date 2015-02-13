package com.sc2geeks.front.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.sc2geeks.front.RequestContext;
import com.sc2geeks.front.WebsiteConfig;
import com.sc2geeks.front.ui.PageSEOInfo;
import com.sc2geeks.front.ui.PageUrlHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/15/12
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionBase extends ActionSupport implements ServletRequestAware
{
	protected static Logger logger = Logger.getLogger(ActionBase.class);

	protected final String REDIRECT = "redirect";
	public final String NOT_FOUND = "not-found";

	protected HttpServletRequest request;
	protected ActionContext actionContext;
	protected ValueStack valueStack;
	protected RequestContext requestContext = new RequestContext();
	protected PageSEOInfo pageSEOInfo;
	protected PageUrlHelper pageUrlHelper;

	protected static WebsiteConfig websiteConfig = WebsiteConfig.getInstance();

	public ActionBase()
	{
		pageSEOInfo = new PageSEOInfo(websiteConfig.getDefaultSEOInfo());
	}

	public String execute() throws Exception
	{
		try
		{
			init();

			String result = doExecute();

			postExecute();
			return result;
		} catch (Exception e)
		{
			logger.error("Failed to execute action: " + request.getRequestURI(), e);
			throw e;
		}
	}

	/**
	 * subclass must call super.init() first.
	 */
	protected void init()
	{
		actionContext = ActionContext.getContext();
		valueStack = actionContext.getValueStack();

		// request context
		String fullUrl = StringUtils.removeEndIgnoreCase(request.getRequestURI(), "/index." + websiteConfig.getDefaultExtension());
		ActionMapping actionMapping = (ActionMapping)actionContext.get("struts.actionMapping");
		requestContext.setActionExtension(actionMapping.getExtension());
		requestContext.setActionNameSpace(actionMapping.getNamespace());
		requestContext.setAppContextPath(request.getContextPath());
		String actionPath = StringUtils.removeStartIgnoreCase(StringUtils.removeStartIgnoreCase(fullUrl, requestContext.getAppContextPath()),
				requestContext.getActionNameSpace());
		actionPath = StringUtils.removeEndIgnoreCase(actionPath, "." + requestContext.getActionExtension());
		requestContext.setQueryString(request.getQueryString());
		requestContext.setActionPath(actionPath);
		requestContext.setPageTab(getPageTab());
		requestContext.setRemoteIP(getRemoteIP(request));
		setValue("requestContext", requestContext);

		// parse url
		pageUrlHelper = parseUrl();

		// get search submit url
		requestContext.setSearchSubmitUrl(pageUrlHelper.removeKeyword());

		// dumpHeaders();
	}

	// this somewhat violates separation of responsibility as it depends on UI package.
	protected void postExecute()
	{
		// seo
		setValue("pageSEOInfo", pageSEOInfo);
	}

	protected void setValue(String name, Object value)
	{
		valueStack.set(name, value);
	}

	protected PageUrlHelper parseUrl()
	{
		return new PageUrlHelper(requestContext.getActionNameSpace(), requestContext.getActionPath(), requestContext.getQueryString());
	}
	abstract protected String doExecute();

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	protected String getPageTab(){return "";};

	private static String getRemoteIP(HttpServletRequest request)
	{
		String ip;
		// Get Cloudflare client ip
		// https://support.cloudflare.com/hc/en-us/articles/200170986-How-does-CloudFlare-handle-HTTP-Request-headers-
		ip = request.getHeader("CF-Connecting-IP");
		if (StringUtils.isNotBlank(ip))
			return ip;


		ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip))
			return ip;

		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(ip))
			return ip;

		return request.getRemoteAddr();
	}

	private void dumpHeaders()
	{
		logger.info("Dumping headers");
		logger.info("\t==============================");
		java.util.Enumeration<java.lang.String> names = request.getHeaderNames();
		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			logger.info("\t" + StringUtils.rightPad(name, 16, ' ') + ": " + request.getHeader(name));
		}
		logger.info("\t==============================");
	}
}
