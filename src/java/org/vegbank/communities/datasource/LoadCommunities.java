/*
 *	'$RCSfile: LoadCommunities.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:57:38 $'
 *	'$Revision: 1.1 $'
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
		
		if (args.length < 1)
		{
			System.out.println("USAGE: LoadCommunities [xml | db ]");
			System.exit(0);
		}
		else
		{
			mode = args[0];
		}
		try
		{
			LoadCommunities lc = new LoadCommunities();
			EcoArtCommunityReader cr = new EcoArtCommunityReader(lc.getConnection());
			AbstractList communityList = cr.getAllCommunities();
			Iterator i = communityList.iterator();
			while( i.hasNext() ) 
			{ 
				if ( mode.toUpperCase().equals("DB") )
				{
					DBCommunityWriter dbw = new DBCommunityWriter( (Community) i.next());
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
