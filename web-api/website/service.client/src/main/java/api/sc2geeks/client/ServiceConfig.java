package api.sc2geeks.client;

import api.sc2geeks.entity.RefinementField;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/15/12
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
class ServiceConfig
{
	String serviceUrl;

	private static ServiceConfig replayServiceConfig;
	private static ServiceConfig playerServiceConfig;

	private Hashtable<RefinementField, String> refinementFieldSetting;

	static {
		try
		{
		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
		replayServiceConfig = context.getBean("replayServiceConfig", ServiceConfig.class);
		playerServiceConfig = context.getBean("playerServiceConfig", ServiceConfig.class);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ServiceConfig getReplayServiceConfig()
	{
		return replayServiceConfig;
	}

	public static ServiceConfig getPlayerServiceConfig()
	{
		return playerServiceConfig;
	}

	private ServiceConfig(){}

	public String getServiceUrl()
	{
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl)
	{
		this.serviceUrl = processPath(serviceUrl);
	}

	public Hashtable<RefinementField, String> getRefinementFieldSetting()
	{
		return refinementFieldSetting;
	}

	public void setRefinementFieldSetting(Hashtable<RefinementField, String> refinementFieldSetting)
	{
		this.refinementFieldSetting = refinementFieldSetting;
	}

	public String getRefinementParamName(RefinementField field)
	{
		if (refinementFieldSetting == null)
			return null;
		return refinementFieldSetting.get(field);
	}

	static String processPath(String input)
	{
		return input.endsWith("/") ? input : input + "/";
	}
}
