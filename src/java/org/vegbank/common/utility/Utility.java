package org.vegbank.common.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.vegbank.common.dbAdapter.*;

/**
 * '$RCSfile: Utility.java,v $'
 * 
 * Purpose: An utility class for Vegbank project.
 * 
 * '$Author: farrell $'
 * '$Date: 2003-03-20 19:34:17 $'
 * '$Revision: 1.6 $'
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

public class Utility
{
	
	public static AbstractDatabase dbAdapter;
	// Make a configuration option
	public static  String dbAdapterName = "org.vegbank.common.dbAdapter.PostgresqlAdapter";
	
	/** 
	 * Determine our db adapter class and create an instance of that class
	 */
	static {
		try {
			dbAdapter = (AbstractDatabase) createObject(dbAdapterName);
		} catch (Exception e) {
			System.err.println("Error in Vegbank Util static block:" + e.getMessage());
		}
	}
	
	public static String escapeCharacters(String s)
	{
		// Handle nulls
		if ( s == null)
			return null;
			
		String origString = s;
		// List of characters to escape
		char[] specialChar = {'\'', '$', '^'};
		
		for (int i = 0; i < specialChar.length ; i++)
		{
			char currentChar = specialChar[i];
			//System.out.println("----->" + currentChar);
			
			if ( s.indexOf ( currentChar) != -1 ) 
			{
				StringBuffer hold = new StringBuffer();
				char c;
				for ( int ii = 0; ii < s.length(); ii++ ) 
				{
					if ( (c=s.charAt(ii)) == currentChar  )
						hold.append ("\\" + currentChar );
					else
						hold.append(c);
				}
				s = hold.toString();
			}
		}
		//System.out.println("---->" + origString + " VS. " + s );
		return s;
	}
  
  /**
	* return a database connection for use with the database
	*/
	public Connection getConnection(String databaseName)
	{
		String host = "127.0.0.1";  // Default
		return getConnection(databaseName, host);
	}
	
	public Connection getConnection(String databaseName, String host)
	{
		String driverClassName = "org.postgresql.Driver";
		String databaseUserName = "datauser";
		String databasePassword = "";
		String connectionString = "jdbc:postgresql://" + host + "/" + databaseName;
		Connection c = null;
    
		try 
		{
			Class.forName(driverClassName);
			c = DriverManager.getConnection(connectionString, "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	/*
		private Connection getConnection()
	{
		Connection c = null;
		try
		{
			// FIXME: Pull in from Properties files
			Class.forName("org.postgresql.Driver");
			c =
				DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1/plants_dev",
					"datauser",
					"");
		}
		catch (Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (c);
	}

	*/
		
	/**
	 * Thin wrapper around setting a date field in a PreparedStatement to handle
	 * adding nulls when needed, e.g. empty string.
	 * 
	 * @param date
	 * @param psmnt
	 * @param i
	 * @throws SQLException
	 */
	public static void insertDateField ( String date, PreparedStatement psmnt, int i)
		throws SQLException
	{
		int sqlDateType = java.sql.Types.DATE;
		if ( date == "" )
		{
			psmnt.setNull(i, sqlDateType);
		}
		else
		{
			// this maybe should be setDate
			psmnt.setString(i, date);
		}
	}	
	
	/**
	 * Instantiate a class using the name of the class at runtime
	 *
	 * @param className the fully qualified name of the class to instantiate
	 */
	public static Object createObject(String className) throws Exception {
 
		Object object = null;
		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
		return object;
	}
	
}
