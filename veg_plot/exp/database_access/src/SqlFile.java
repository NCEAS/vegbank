package databaseAccess;

/**
 *  '$RCSfile: SqlFile.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-03-14 00:00:54 $'
 * '$Revision: 1.1 $'
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
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


        
public class SqlFile 
{
	private String sqlFile = null;


	/**
	 * Generic method to make a connection to a postgresql database
	 * this database can be changed by chaging the source code
	 */
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//the community database
			System.out.println("CommunityQueryStore > connecting to db on: vegbank ");
			c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/communities_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("CommunityQueryStore > exception: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	/**
	 * long method to do the nust and bolts
	 */
	public StringBuffer issueSqlFile( String fileName)
	{
		StringBuffer rsb = new StringBuffer();
		Connection conn = null;
		Statement stmt = null;
		try 
		{
			// connect to the jdbc-odbc bridge driver
			//conn = DriverManager.getConnection(dbUrl, "user", "pass");
			conn=getConnection();
			// Get the DatabaseMetaData object and display
			// some information about the connection
			DatabaseMetaData dma = conn.getMetaData();
			System.out.println("AccessVegBank> Connected to " + dma.getURL() );
			System.out.println("AccessVegBank> Driver       " + dma.getDriverName() );
			System.out.println("AccessVegBank> Version      " + dma.getDriverVersion() );
			System.out.println("AccessVegBank> Catalog      " + conn.getCatalog() );
		} 
		catch (Exception e) 
		{
			System.out.println("AccessVegBank> " + e.getMessage());
			e.printStackTrace();
		}

		
		ResultSet results = null;
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName), 8192);
			System.out.println("loading the sql file\n");
			String s = null;
			String property_string="";
			String command_array[]=new String[1000];
			int array_cnt=0;
			StringBuffer sb = new StringBuffer();
			while ((s=in.readLine()) != null) 
			{
			 if (! s.startsWith("--") )
			 {
				if (s.indexOf(";")>0) 
				{
						//Statement stmt = null;
						try 
						{
							// Create a Statement so we can submit SQL statements to the driver
							stmt = conn.createStatement();
							//create the result set		
							sb.append(s);
							System.out.println("statement: " + sb.toString() );
							stmt.executeUpdate( sb.toString() );
						}
						catch (Exception x) 
						{
							System.out.println("AccessVegBank> Exception: " + x.getMessage() );
							x.printStackTrace();
							//System.exit(0);
						}
						//make a new instance of teh sb
						sb = new StringBuffer();
				}
				else 
				{
					sb.append(s+" ");
				}
			}
		} 
	}
	catch (Exception ex) 
	{
 	 System.out.println("AccessVegBank> Exception "+ex.getMessage() );
 		ex.printStackTrace(); 
		//System.exit(0);
	}
	return(rsb);
	}
	
	
	
	//main method to run this app
	public static void main(String args[]) 
	{
		try
		{
			SqlFile sql = new SqlFile();
			String file = args[0];
			sql.issueSqlFile( file );
		}
		catch (Exception ex) 
		{
  		System.out.println("AccessVegBank> Exception "+ex.getMessage() );
 			ex.printStackTrace(); 
			//System.exit(0);
		}
		
	}

}
