package com.sc2geeks.commons.data;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.*;
import java.util.ArrayList;

public final class SqlHelper
{

	/*
	public enum CommandType{
		Text,
		StoredProcedure,
	}
	*/

	private static ApplicationContext context;
	private static SqlHelperConfig _sqlHelperConfig;

	/**
	 * 获得数据库连接
	 *
	 * @return
	 */
	public static Connection getConnection(String dbName)
	{
		try
		{
			DatabaseConfig config = _sqlHelperConfig.getDatabaseConfig(dbName);
			if (config != null)
			{
				System.out.println("config obtained: " + config.toString());
			}
			Connection conn;
			conn = DriverManager.getConnection(config.getUrl(), config.getUser(),
					config.getPassword());
			return conn;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	static
	{
		try
		{
			context = new ClassPathXmlApplicationContext("config.xml");
			_sqlHelperConfig = context.getBean("sqlHelperConfig", SqlHelperConfig.class);

			for (String driverName : _sqlHelperConfig.getDriverList())
			{
				Class.forName(driverName);
			}
		} catch (Exception e)
		{
		}
	}


	public static int ExecuteNonQuery(String dbName, String cmdtext, Object... params) throws Exception
	{
		PreparedStatement pstmt = null;
		Connection conn = null;

		try
		{
			System.out.println("getting connection... ");
			conn = getConnection(dbName);
			System.out.println("conn: " + conn.toString());
			pstmt = conn.prepareStatement(cmdtext);
			PrepareCommand(pstmt, params);

			return pstmt.executeUpdate();

		} catch (Exception e)
		{
			throw new Exception("executeNonQuery方法出错:" + e.getMessage());
		} finally
		{
			try
			{
				if (pstmt != null && (!pstmt.isClosed()))
					pstmt.close();
				if (conn != null && (!conn.isClosed()))
					conn.close();
			} catch (Exception e)
			{
				throw new Exception("执行executeNonQuery方法出错:" + e.getMessage());
			}
		}
	}
	public static int ExecuteNonQuery(Connection conn, String cmdtext, Object... params) throws Exception
	{
		PreparedStatement pstmt = null;

		try
		{
			pstmt = conn.prepareStatement(cmdtext);
			PrepareCommand(pstmt, params);

			return pstmt.executeUpdate();

		} catch (Exception e)
		{
			throw new Exception("executeNonQuery方法出错:" + e.getMessage());
		} finally
		{
			try
			{
				if (pstmt != null && (!pstmt.isClosed()))
					pstmt.close();
			} catch (Exception e)
			{
				throw new Exception("执行executeNonQuery方法出错:" + e.getMessage());
			}
		}
	}

	/**
	 * 用于获取结果集语句（eg：selete * from table）
	 *
	 * @param cmdtext
	 * @param params
	 * @return ResultSet
	 * @throws Exception
	 */
	public static ArrayList ExecuteReader(String dbName, String cmdtext, Object... params) throws Exception
	{
		PreparedStatement pstmt = null;
		Connection conn = null;

		try
		{
			conn = getConnection(dbName);

			pstmt = conn.prepareStatement(cmdtext);

			PrepareCommand(pstmt, params);
			ResultSet rs = pstmt.executeQuery();

			ArrayList al = new ArrayList();
			ResultSetMetaData rsmd = rs.getMetaData();
			int column = rsmd.getColumnCount();

			while (rs.next())
			{
				Object[] ob = new Object[column];
				for (int i = 1; i <= column; i++)
				{
					ob[i - 1] = rs.getObject(i);
				}
				al.add(ob);
			}

			rs.close();
			pstmt.close();
			conn.close();

			return al;

		} catch (Exception e)
		{
			throw new Exception("executeSqlResultSet方法出错:" + e.getMessage());
		} finally
		{
			try
			{
				if (pstmt != null && (pstmt.isClosed()))
					pstmt.close();
				if (conn != null && (conn.isClosed()))
					conn.close();
			} catch (Exception e)
			{
				throw new Exception("executeSqlResultSet方法出错:" + e.getMessage());
			}
		}
	}


	/**
	 * 用于获取单字段值语句（用序号指定字段）
	 *
	 * @param cmdtext SQL语句
	 * @param params  OracleParameter[]
	 * @return Object
	 * @throws Exception
	 */
	public static Object ExecuteScalar(String dbName, String cmdtext, Object... params)
			throws Exception
	{
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try
		{
			conn = getConnection(dbName);

			pstmt = conn.prepareStatement(cmdtext);
			/*
			if (commandType == CommandType.Text){
				pstmt = conn.prepareStatement(cmdtext);
			}
			else{
				pstmt = conn.prepareCall(cmdtext);
			}
			*/
			PrepareCommand(pstmt, params);

			rs = pstmt.executeQuery();
			if (rs.next())
			{
				return rs.getObject(1);
			} else
			{
				return null;
			}
		} catch (Exception e)
		{
			throw new Exception("executeSqlObject方法出错:" + e.getMessage());
		} finally
		{
			try
			{
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e)
			{
				throw new Exception("executeSqlObject方法出错:" + e.getMessage());
			}
		}
	}


	/**
	 * 准备SQL参数
	 *
	 * @param pstm
	 * @param params
	 */
	public static void PrepareCommand(PreparedStatement pstm, Object[] params)
	{
		if (params == null || params.length == 0)
		{
			return;
		}

		try
		{
			for (int i = 0; i < params.length; i++)
			{
				int parameterIndex = i + 1;
//String
				if (params[i].getClass() == String.class)
				{
					pstm.setString(parameterIndex, params[i].toString());
				}
//Short
				else if (params[i].getClass() == short.class)
				{
					pstm.setShort(parameterIndex, Short.parseShort(params[i].toString()));
				}
//Long
				else if (params[i].getClass() == long.class)
				{
					pstm.setLong(parameterIndex, Long.parseLong(params[i].toString()));
				}
//Integer
				else if (params[i].getClass() == Integer.class)
				{
					pstm.setInt(parameterIndex, Integer.parseInt(params[i].toString()));
				}
//Date
				else if (params[i].getClass() == java.util.Date.class)
				{
					java.util.Date dt = (java.util.Date) params[i];
					pstm.setTimestamp(parameterIndex, new Timestamp(dt.getTime()));
				}
// timestamp
				else if (params[i].getClass() == Timestamp.class)
				{
					Timestamp dt = (Timestamp) params[i];
					pstm.setTimestamp(parameterIndex, dt);
				}
//Byte
				else if (params[i].getClass() == byte.class)
				{
					pstm.setByte(parameterIndex, (Byte) params[i]);

				}
//Float
				else if (params[i].getClass() == float.class)
				{
					pstm.setFloat(parameterIndex, Float.parseFloat(params[i].toString()));
				}
//Boolean
				else if (params[i].getClass() == boolean.class)
				{
					pstm.setBoolean(parameterIndex, Boolean.parseBoolean(params[i].toString()));
				} else
				{
					throw new Exception("参数准备出错:数据类型不可见" + params[i].getClass().toString());
				}
			}
		} catch (Exception e)
		{
		}
	}
}

