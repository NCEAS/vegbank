/*
 *	'$RCSfile: LoadCommunities.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-17 22:09:14 $'
 *	'$Revision: 1.6 $'
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
 
package org.vegbank.communities.datasource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Iterator;

import org.vegbank.common.model.Community;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.ObjectToXML;
import org.vegbank.communities.datasink.DBCommunityWriter;

/**
 * @author farrell
 */

public class LoadCommunities
{
	
	public static void main(String[] args)
	{
		String mode = "";
		String databaseHost = "localhost"; 
		DBConnection conn = null;
		
		if (args.length < 2)
		{
			System.out.println("USAGE: LoadCommunities [xml | db ] [databaseHost]");
			System.exit(0);
		}
		else
		{
			mode = args[0];
			databaseHost = args[1];
		}
		try
		{
			System.out.println("Get connection to database on " + databaseHost);
			conn = LoadCommunities.getDBConnection();
			
			LoadCommunities lc = new LoadCommunities();
			EcoArtCommunityReader cr = new EcoArtCommunityReader(lc.getConnection());
			AbstractList communityList = cr.getAllCommunities();
			System.out.println( "Number of Communities => " + communityList.size() );
			Iterator i = communityList.iterator();
			while( i.hasNext() ) 
			{ 
				if ( mode.toUpperCase().equals("DB") )
				{
					DBCommunityWriter dbw = new DBCommunityWriter( (Community) i.next(), conn);
				}
				else
				{
					ObjectToXML o2X = new ObjectToXML( (Community) i.next());
					System.out.println( o2X.getXML() );
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private static DBConnection getDBConnection() throws SQLException
	{
		return DBConnectionPool.getDBConnection("Need connection for inserting community");
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException
	{
		String dbUrl = "jdbc:odbc:ecoart";
		// Load the sun jdbc-odbc bridge driver
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	
		// connect to the jdbc-odbc bridge driver
		Connection conn = DriverManager.getConnection(dbUrl, "user", "pass");
	
		// Get the DatabaseMetaData object and display
		// some information about the connection
		DatabaseMetaData dma = conn.getMetaData();
	
		System.out.println("EcoartVegCommunitySource > Connected to " + dma.getURL() );
		System.out.println("EcoartVegCommunitySource > Driver       " + dma.getDriverName() );
		System.out.println("EcoartVegCommunitySource > Version      " + dma.getDriverVersion() );
		System.out.println("EcoartVegCommunitySource > Catalog      " + conn.getCatalog() );
		
		return conn;
	}

}
