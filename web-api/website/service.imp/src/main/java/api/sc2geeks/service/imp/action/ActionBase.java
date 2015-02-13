package api.sc2geeks.service.imp.action;

import api.sc2geeks.service.imp.SolrConfig;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/6/12
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionBase extends ActionSupport implements ServletRequestAware
{
	protected HttpServletRequest request;
	protected ActionContext actionContext;
	protected ValueStack valueStack;

	protected static SolrConfig solrConfig = SolrConfig.getReplayInstance();


	public String execute()
	{
		init();

		String result = doExecute();

		preRender();
		return result;
	}

	protected void init()
	{
		actionContext = ActionContext.getContext();
		valueStack = actionContext.getValueStack();
	}

	// this somewhat violates separation of responsibility as it depends on UI package.
	protected void preRender()
	{
	}

	protected void setValue(String name, Object value)
	{
		valueStack.set(name, value);
	}

	abstract protected String doExecute();

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}
}
