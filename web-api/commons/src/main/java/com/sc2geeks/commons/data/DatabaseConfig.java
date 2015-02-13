package com.sc2geeks.commons.data;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/28/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
class DatabaseConfig
{
	private String name;
	private String url;
	private String driverName;
	private String user;
	private String password;


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDriverName()
	{
		return driverName;
	}

	public void setDriverName(String driverName)
	{
		this.driverName = driverName;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
