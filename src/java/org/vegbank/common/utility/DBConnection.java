/**
 *  '$RCSfile: DBConnection.java,v $'
 *    Purpose: A class represent a connection object, it includes connction 
 *    itself, index, status, age and usageCount.
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *   '$Date: 2004-02-27 17:09:30 $'
 *   '$Revision: 1.3 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.vegbank.common.utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * A class represent a connection object, it includes connection itself, 
 * index, status, age, createtime, connection time, usageCount, warning message
 */

public class DBConnection
{
	private Connection conn;
	private String tag; //to idenify this object
	private int status; // free or using
	private long age;
	private long createTime;
	private long connectionTime; //how long it use for connections, 
	//it is accumulated
	private long checkOutTime; //the time when check it out
	private int usageCount; // how many time the connection was used
	private int checkOutSerialNumber; // a number to identify same check out.
	//for a connection
	private SQLWarning warningMessage;
	private String checkOutMethodName;

	private static String DBDriver = DatabaseUtility.getOption("driverClass");
	private static String DBConnectedJDBC = DatabaseUtility.getOption("connectString");
	private static String userName = DatabaseUtility.getOption("user");
	private static String passWord = DatabaseUtility.getOption("password");

	/**
	 * Default constructor of the DBConnection class 
	 * 
	 */
	public DBConnection() throws SQLException
	{
		conn = openConnection();
		tag = conn.toString();
		status = 0;
		age = 0;
		createTime = System.currentTimeMillis();
		connectionTime = 0;
		checkOutTime = 0;
		usageCount = 0;
		checkOutSerialNumber = 0;
		warningMessage = null;
		checkOutMethodName = null;

	}

	/**
	 * get the  connetion from the object
	 */
	public Connection getConnections()
	{
		return conn;
	}
	
	public boolean isClosed() throws SQLException
	{
		return conn.isClosed();
	}

	/**
	 * Set a connection to this object
	 * @param myDBConnection, the connection which will be assign to this object
	 */
	public void setConnections(Connection myConnection)
	{
		this.conn = myConnection;
	}

	/**
	 * get the db connetion tag from the object
	 */
	public String getTag()
	{
		return tag;
	}

	/**
	 * Set a connection status to this object
	 * @param myTag, the tag which will be assign to this object
	 */
	public void setTag(String myTag)
	{
		this.tag = myTag;
	}

	/**
	 * get the db connetion status from the object
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * Set a connection status to this object
	 * @param myStatus, the status which will be assign to this object
	 * 0 is free, 1 is using
	 */
	public void setStatus(int myStatus)
	{
		this.status = myStatus;
	}

	/**
	 * get the db connetion age from the object
	 */
	public long getAge()
	{
		return (System.currentTimeMillis() - createTime);
	}

	/**
	 * Set a connection age to this object
	 * @param myAge, the Age which will be assign to this object
	 */
	public void setAge(long myAge)
	{
		this.age = myAge;
	}

	/**
	 * get the db connetion created time from the object
	 */
	public long getCreateTime()
	{
		return createTime;
	}

	/**
	 * Set a usage number to this object
	 * @param myCreateTime, the create time which will be assign to this object
	 */
	public void setCreateTime(long myCreateTime)
	{
		this.createTime = myCreateTime;
	}

	/**
	 * get the how long db connetion used for the object
	 */
	public long getConnectionTime()
	{
		return connectionTime;
	}

	/**
	 * Set a connection time to this object
	 * It is accumulated
	 * @param myConnectionTime, the connection time which will assign to
	 * this object
	 */
	public void setConnectionTime(long myConnectionTime)
	{
		this.connectionTime = this.connectionTime + myConnectionTime;
	}

	/**
	 * get the when a db connetion was checked out
	 */
	public long getCheckOutTime()
	{
		return checkOutTime;
	}

	/**
	 * Set check out time to this object
	
	 * @param myCheckOutTime, the check out time which will assign to
	 * this object
	 */
	public void setCheckOutTime(long myCheckOutTime)
	{
		this.checkOutTime = myCheckOutTime;
	}

	/**
	 * get the db connetion usage times from the object
	 */
	public int getUsageCount()
	{
		return usageCount;
	}

	/**
	 * Set a usage number to this object
	 * @param myUsageCount, number of usage which will be assign to this object
	 */
	public void setUsageCount(int myUsageCount)
	{
		this.usageCount = myUsageCount;
	}

	/**
	 * Increase a usage number to this object
	 * @param myUsageCount, number of usage which will be add to this object
	 */
	public void increaseUsageCount(int myUsageCount)
	{
		this.usageCount = this.usageCount + myUsageCount;
	}

	/**
	 * get the check out serial number
	 */
	public int getCheckOutSerialNumber()
	{
		return checkOutSerialNumber;
	}

	/**
	 * Set check out serial number to this object
	
	 * @param myCheckOutSerialNumber, the check out serial number which will 
	 * assign to this object
	 */
	public void setCheckOutSerialNumber(int myCheckOutSerialNumber)
	{
		this.checkOutSerialNumber = myCheckOutSerialNumber;
	}

	/**
	 * Increase a usage number to this object
	 * @param myUsageCount, number of usage which will be add to this object
	 */
	public void increaseCheckOutSerialNumber(int myCheckOutSerialNumber)
	{
		this.checkOutSerialNumber =
			this.checkOutSerialNumber + myCheckOutSerialNumber;
	}

	/**
	 * get the db connetion waring message from the object
	 */
	public SQLWarning getWarningMessage() throws SQLException
	{
		//should increase 1 UsageCount
		increaseUsageCount(1);
		return conn.getWarnings();
	}

	/**
	 * Set a warning message to this object
	 * @param myWarningMessage, the waring which will be assign to this object
	 */
	public void setWarningMessage(SQLWarning myWarningMessage)
	{
		this.warningMessage = myWarningMessage;
	}

	/**
	 * get the the name of method checked out the connection from the object
	 */
	public String getCheckOutMethodName()
	{
		return checkOutMethodName;
	}

	/**
	 * Set a method name to the checkOutMethodName 
	 * @param myCheckOutMethodName, the name of method will assinged to it
	 */
	public void setCheckOutMethodName(String myCheckOutMethodName)
	{
		this.checkOutMethodName = myCheckOutMethodName;
	}

	/**
	 * Close a DBConnection object
	 */
	public void close() throws SQLException
	{
		conn.close();
		tag = null;
		status = 0;
		age = 0;
		createTime = System.currentTimeMillis();
		connectionTime = 0;
		checkOutTime = 0;
		usageCount = 0;
		warningMessage = null;
	}

	/** 
	* Method to establish DBConnection 
	*/
	public static Connection openConnection() throws SQLException
	{
		return openConnection(DBDriver, DBConnectedJDBC, userName, passWord);
	} //openDBConnection

	/** 
	 * Method to establish a JDBC database connection 
	 *
	 * @param dbDriver the string representing the database driver
	 * @param connection the string representing the database connectin parameters
	 * @param user name of the user to use for database connection
	 * @param password password for the user to use for database connection
	 */
	private static Connection openConnection(
		String dbDriver,
		String connection,
		String user,
		String password)
		throws SQLException
	{
		try
		{
			Class.forName(dbDriver);
		}
		catch (ClassNotFoundException e)
		{
			LogUtility.log(
				"DBConnectionPool: Error in DBConnectionPool " + e.getMessage(),
				e);
			return null;
		}
		// Connect to the database
		//LogUtility.log("Get a connection using " + connection + ", " + user + ", " + password);
		Connection connLocal = null;
		connLocal = DriverManager.getConnection(connection, user, password);
		return connLocal;
	} //OpenDBConnection

	/**
	 * Method to create a PreparedStatement by sending a sql statement
	 * @Param sql, the sql statement which will be sent to db
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return conn.prepareStatement(sql);
	} //prepareStatement

	/**
	 * Method to create a Statement
	 */
	public Statement createStatement() throws SQLException
	{
		return conn.createStatement();
	} //prepareStatement
	
	/**
	 * Create a statement with a non-default resultSetType and resultSetConcurency set.
	 * 
	 * Useful for creating a `TYPE_SCROLL_INSENSITIVE` query in order to make result
	 * sets scrollable.
	 * 
	 * @param resultSetType The result set type, i.e., read only
	 * @param resultSetConcurrency The result set concurrency, i.e., scrollable
	 * @return The Statement
	 * @throws SQLException
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return conn.createStatement(resultSetType, resultSetConcurrency);
	} //prepareStatement

	/**
	 * Method to make a commit command
	 */
	public void commit() throws SQLException
	{
		conn.commit();
	} //commit

	/**
	 * Method to set commit mode
	 * @param autocommit, true of false to auto commit
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		conn.setAutoCommit(autoCommit);
	} //setAutoCommit

	/**
	 * Method to roll back
	 */
	public void rollback() throws SQLException
	{
		conn.rollback();
	} //rollback

	/**
	 * Method to get meta data
	 */
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return conn.getMetaData();
	} //getMetaData
	
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		conn.setReadOnly(readOnly);
	}

} //DBConnection class
