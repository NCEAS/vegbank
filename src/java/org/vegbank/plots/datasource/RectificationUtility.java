package org.vegbank.plots.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vegbank.common.utility.DBConnection;

/*
 * '$RCSfile: RectificationUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-17 20:31:40 $'
 *	'$Revision: 1.2 $'
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
/**
 * @author farrell
 *
 * Utilities to assist with rectification of loaded data with data in the database
 */
public class RectificationUtility 
{
	
	public static String GETCOMMCONCEPTID = "select commconcept_id from commusage where commname =";
	public static String GETPLANTNAMEID = "select plantname_id from plantname where plantname = ";
	public static String GETPLANTCONCEPTID = "select plantconcept_id from plantusage where plantname_id =";


	/**
	 * Get a FK from the database use a precanned SQL statement and a search parameter.
	 * Returns 0 if no or to many records match
	 * 
	 * @param String -- search parameter
	 * @param Connection -- an active JDBC connection to use
	 * @param String -- Hard Coded SQL Statement to use
	 * @return int -- the value of the FK
	 */
	public static int getForiegnKey(DBConnection conn, String SQL, Object searchParameter)
	{
		int result = 0;
		ResultSet rs = null;
		String sql = null;
		
		if ( searchParameter instanceof String )
		{
			sql = SQL + "'" + searchParameter + "'";
		}
		else if ( searchParameter instanceof  Integer )
		{
			sql = SQL + searchParameter;
		}
		
		try
		{
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			// Just get the first match 
			if ( rs.next() )
			{
				result = rs.getInt(1);
			}
		
			// If another record found then ambiguous match 
			if ( rs.next() )
			{
				System.out.println("Many records match: cannot rectify, SQL: " + sql );
				// Just return 0 here and let the client handle it
				result = 0;
			}
		}
		catch (SQLException e)
		{ 
			System.out.println("RectificationUtility > Could not find Id with SQL: " + sql );
			System.out.println("RectificationUtility > error was: " + e.getMessage() );
		}
		return result;
	}	
	
	/**
	 * Exception thrown when an Error is encoutered trying 
	 * to rectify input with database contents
	 */
	public class RetificationException extends Exception
	{

		/**
		 * @param string
		 */
		public RetificationException(String string)
		{
			super(string);
		}

	}

}
