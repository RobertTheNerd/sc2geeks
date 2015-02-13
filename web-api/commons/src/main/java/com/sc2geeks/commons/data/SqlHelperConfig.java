package com.sc2geeks.commons.data;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 9/25/11
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
class SqlHelperConfig
{
	private List<DatabaseConfig> databaseConfigList;
	private Hashtable<String, DatabaseConfig> dicConfigList;
	private Set<String> driverList;

	public List<DatabaseConfig> getDatabaseConfigList()
	{
		return databaseConfigList;
	}

	public void setDatabaseConfigList(List<DatabaseConfig> databaseConfigList)
	{
		this.databaseConfigList = databaseConfigList;
		dicConfigList = new Hashtable<String, DatabaseConfig>(databaseConfigList.size());
		driverList = new HashSet<String>();
		for (DatabaseConfig c : databaseConfigList)
		{
			dicConfigList.put(c.getName().toLowerCase().trim(), c);
			System.out.println("Adding database: " + c.getName().toLowerCase());
			driverList.add(c.getDriverName());
			System.out.println("driver: " + c.getDriverName());

		}
	}

	public DatabaseConfig getDatabaseConfig(String name)
	{
		name = name.toLowerCase();
		if (dicConfigList == null || !dicConfigList.containsKey(name))
		{
			return null;
		}
		return dicConfigList.get(name);
	}

	public Set<String> getDriverList()
	{
		return driverList;
	}
}
