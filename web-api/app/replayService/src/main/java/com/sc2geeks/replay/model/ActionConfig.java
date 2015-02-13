package com.sc2geeks.replay.model;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/20/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionConfig
{
	static Logger logger = Logger.getLogger(ActionConfig.class);
	public enum ActionType
	{
		Unit,
		Structure,
		Cast,   // special ability
		Upgrade,
		Research
	}

	private ActionConfig(){}

	static List<ActionConfigUnit> actionList = new ArrayList<ActionConfigUnit>();
	static List<ActionConfigUnit> publicActionList;
	static HashMap<String, ActionConfigUnit> actionsByName = new HashMap<String, ActionConfigUnit>();
	static SpecialActionConfig specialActionConfig;
	static
	{
		// load file
		InputStream stream = ActionConfig.class.getResourceAsStream("ActionConfig.txt");
		try
		{
			Scanner scanner = new Scanner(stream);
			while (scanner.hasNext())
			{
				ActionConfigUnit unit = ActionConfigUnit.fromString(scanner.nextLine(), "\t");
				if (unit == null)
					continue;

				actionList.add(unit);
			}
			publicActionList = Collections.unmodifiableList(actionList);
		}
		catch(Exception e)
		{
			logger.error("Failed to parse action list.", e);
		}
		finally
		{
			try
			{
				if (stream != null)
					stream.close();
			}catch (Exception ex){}
		}
		init();

		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
		specialActionConfig = context.getBean("specialActions", SpecialActionConfig.class);
	}

	private static void init()
	{
		for (ActionConfigUnit action : actionList)
		{
			actionsByName.put(action.actionName, action);
		}
	}


	public static ActionConfigUnit getActionByName(String name)
	{
		return actionsByName.get(name);
	}

	public static List<ActionConfigUnit> getActionList()
	{
		return publicActionList;
	}

	public static List<SpecialActionUnit> getSpecialActionConfigList()
	{
		return specialActionConfig.getSpecialActionConfigs();
	}

}