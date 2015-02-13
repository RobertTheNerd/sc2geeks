package api.sc2geeks.service.imp;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 10/13/12
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Log4jInitServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException
	{
		System.out.println("Log4JInitServlet is initializing log4j");
		String log4jLocation = config.getInitParameter("log4j-properties-location");

		ServletContext sc = config.getServletContext();

		if (log4jLocation == null)
		{
			System.err.println("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else
		{
			String webAppPath = sc.getRealPath("/");
			String log4jProp = webAppPath + log4jLocation;
			File configFile = new File(log4jProp);
			if (configFile.exists())
			{
				System.out.println("Initializing log4j with: " + log4jProp);
				PropertyConfigurator.configure(log4jProp);
			} else
			{
				System.err.println("*** " + log4jProp + " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		super.init(config);

		Logger logger = Logger.getLogger(this.getClass());
		logger.info("Log4j initialized.");
	}
}
