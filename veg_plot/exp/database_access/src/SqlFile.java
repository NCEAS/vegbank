package databaseAccess;

/**
 *  '$RCSfile: SqlFile.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-03-28 16:28:22 $'
 * '$Revision: 1.2 $'
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
	//private String rdbmsType = "access"; //ms access database
	private String rdbmsType = "postgresql"; //postgres database

	/**
	 * Generic method to make a connection to a postgresql database
	 * this database can be changed by chaging the source code
	 */
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			if ( this.rdbmsType.equals("postgresql") )
			{
				Class.forName("org.postgresql.Driver");
				//the community database
				System.out.println("SqlFile> connecting to db on: vegbank ");
				c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/communities_dev", "datauser", "");
			}
			else if ( this.rdbmsType.equals("access") )
			{
				// specific settings for the access vers. of vegbank
				String dbUrl = "jdbc:odbc:vegbank_access"; 	
 				// Load the sun jdbc-odbc bridge driver
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				// connect to the jdbc-odbc bridge driver
				c = DriverManager.getConnection(dbUrl, "user", "pass");																																 
			}
			else
			{
				System.out.println("SqlFile> unrecognized RDBMS Type " + rdbmsType);
			}
		}
		catch ( Exception e )
		{
			System.out.println("SqlFile> exception: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	/**
	 *
	 * long method to do the nuts and bolts of running the 
	 * sqlfile against the rdbms
	 * @param filename -- the name of the sql file -- with path
	 *
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
			System.out.println("SqlFile> Connected to " + dma.getURL() );
			System.out.println("SqlFile> Driver       " + dma.getDriverName() );
			System.out.println("SqlFile> Version      " + dma.getDriverVersion() );
			System.out.println("SqlFile> Catalog      " + conn.getCatalog() );
		} 
		catch (Exception e) 
		{
			System.out.println("SqlFile> " + e.getMessage());
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
							System.out.println("SqlFile> Exception: " + x.getMessage() );
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
 	 System.out.println("SqlFile> Exception "+ex.getMessage() );
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
			if ( args.length != 1 )
			{
				System.out.println("Usage SqlFile [filename]");
			}
			else
			{
				SqlFile sql = new SqlFile();
				String file = args[0];
				sql.issueSqlFile( file );
			}
		}
		catch (Exception ex) 
		{
  		System.out.println("SqlFile> Exception "+ex.getMessage() );
 			ex.printStackTrace(); 
			//System.exit(0);
		}
		
	}

}
