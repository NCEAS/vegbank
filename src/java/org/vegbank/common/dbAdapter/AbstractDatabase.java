package org.vegbank.common.dbAdapter;

/**
 * '$RCSfile: AbstractDatabase.java,v $'    
 * Purpose: An abstract class that encapsulates access to any RDBMS.
 * This allows to swap easily between databases without any modification to the
 * application.  
 * 
 *
 * '$Author: farrell $'     
 * '$Date: 2003-11-25 19:21:06 $' 
 * '$Revision: 1.4 $'
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

import java.sql.*;

import org.vegbank.common.utility.DBConnection;

/**
 * Java offers uniform database access through the use of JDBC.
 * But many databases still use different SQL implementations and 
 * conventions. Thus this class offers extended programming interface
 * that all subclasses should implement to gain access to different
 * databases.
 *
 * To add a new database adapter class you must create a new class 
 * <dbname>Adapter that extends edu.ucsb.nceas.dbadapter.AbstarctDatabase
 * (where dbname is the name of the database or database driver you wish
 * to add to your application). AbstarctDatabase is an abstract class,
 * thus the subclasses need to implement the abstract methods.
 * 
 * Stolen from Metacat.
 */

public abstract class AbstractDatabase
{

	/**
	 * Unique ID generator
	 *
	 * @param conn db connection in which the unique id was generated
	 * @param tableName the table which unique id was generate
	 * @param primaryKeyName the pk which unique id was generate
	 * @exception SQLException <br/> any SQLException that can be thrown 
	 *            during the db operation
	 * @return return the generated unique id as a long type
	 */
	public abstract long getNextUniqueID(
		DBConnection conn, 
		String tableName, 
		String primarykeyName)
			throws SQLException;

	/**
	 * The function name that gets the current date and time
	 * from the database server
	 *
	 * @return return the current date and time function name
	 */
	public abstract String getDateTimeFunction();

	/**
	 * The function name that is used to return non-NULL value
	 *
	 * @return return the non-NULL function name
	 */
	public abstract String getIsNULLFunction();

	/**
	 * The character that the specific database implementation uses to 
	 * indicate string literals in SQL. This will usually be a single
	 * qoute (').
	 *
	 * @return return the string delimiter
	 */
	public abstract String getStringDelimiter();

	/**
	 * Instantiate a class using the name of the class at runtime
	 *
	 * @param className the fully qualified name of the class to instantiate
	 */
	static public Object createObject(String className) throws Exception
	{

		Object object = null;
		try
		{
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		}
		catch (InstantiationException e)
		{
			throw e;
		}
		catch (IllegalAccessException e)
		{
			throw e;
		}
		catch (ClassNotFoundException e)
		{
			throw e;
		}
		return object;
	}

	/**
	 * the main routine used to test the dbadapter utility.
	 */
	static public void main(String[] args)
	{

		// Determine our db adapter class and
		// create an instance of that class
		try
		{
			String dbAdapter = "postgresql";
			AbstractDatabase dbAdapterObj =
				(AbstractDatabase) createObject(dbAdapter);

			// test if they work correctly
			String date = dbAdapterObj.getDateTimeFunction();

		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

}

