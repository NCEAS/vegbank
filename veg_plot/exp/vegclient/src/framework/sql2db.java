/**
 * This utility will read a file containing a sql statement and issue that statemet 
 * to a database
 * JHH 8/2000 - pass a file containing sql command(s) to a database
 *  -currently this works with Oracle 8i
 *	-currently this only allows for update commands (add function for query)
 *
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-10-10 18:12:43 $'
 * '$Revision: 1.1 $'
 */
package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;

import vegclient.framework.*;


        
public class sql2db 
{
	
	

	//the database connection pooling class
	static LocalDbConnectionBroker connectionBroker = new LocalDbConnectionBroker(); 	
		Connection conn=null;
		Statement query = null;
		Statement stmt = null;
		ResultSet results = null;
		private String sqlFileName = null;
			
	/**
	 * a cunstructor
	 */
	public sql2db(String fileName)
	{
		sqlFileName = fileName;
	}
	
	
	/**
	 * another constructor
	 */
	public sql2db()
	{
		
	}
	
	
	//main method for testing
	public static void main(String args[]) 
	{
		try 
		{
			String fileName = args[0];
			sql2db sql = new sql2db(fileName);
			sql.insertStatement( );
		} 
		catch (Exception e) 
		{
			System.out.println("Exception: " 
			+ e.getMessage());
		}

	}


public void insertStatement() 
{
	insertStatement(sqlFileName);
}

public void insertStatement(String fileName) 
{
		try
		{
			
			//initialize the database connection manager
			connectionBroker.manageLocalDbConnectionBroker("initiate");
			conn = connectionBroker.manageLocalDbConnectionBroker("getConn");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			//conn=m.outConn;
			//query=m.outStmt;
	
			BufferedReader in = new BufferedReader(new FileReader(fileName), 8192);
			System.out.println("loading the sql file\n");

			String s = null;
			String property_string="";
			String command_array[]=new String[1000];
			int array_cnt=0;

			// read all the lines contained within the sql file and parse the individual commands into
			// an array structure


			while ((s=in.readLine()) != null) 
			{
				if (s.indexOf(";")>0) 
				{	
					//strip out all the database settings
					if (s.trim().startsWith("set")) 
					{	
						System.out.println("reading a setting value");
					}
					
					else if( s.trim().startsWith("-") )
					{
						System.out.println("reading a setting value");
					}

					else 
					{
						property_string=property_string+s.replace(';',' ');
						command_array[array_cnt]=property_string+" ";
						array_cnt++;
						property_string=" ";
					}
				}
				else if( s.trim().startsWith("--") )
				{
						System.out.println("reading a comment value");
				}
				else if( s.trim().startsWith("/*") || s.trim().startsWith("*") 
					|| s.trim().startsWith("*\\"))
				{
						System.out.println("reading a comment value");
				}				

				else 
				{
					property_string=property_string+" "+s;
				}
			} //end while


			/*send the individual commands to the DB*/
			for (int i=0; i<array_cnt; i++) 
			{
				try
				{
					System.out.println(command_array[i]);
					stmt.executeUpdate(command_array[i]);
					//conn.commit();
				}
				catch (SQLException sqle)
				{
					System.out.println("Caught SQL Exception: "+sqle.getMessage() ); 
					sqle.printStackTrace();
					conn.rollback();
				}
				conn.commit();
		
			}
		}
	
		catch (Exception ex) 
		{
  		System.out.println("Exception. "+ex.getMessage());
			ex.printStackTrace();
  		//System.exit(1);
		}
		
		//close down the connection pooling
		conn = connectionBroker.manageLocalDbConnectionBroker("destroy");

	}
}
