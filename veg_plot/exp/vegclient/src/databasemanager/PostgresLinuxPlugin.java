 /**
 * class that hadles all the RDBMS management utilities for the 
 * postgres database on the linux operating system
 *
 *
 * 		@author @author@ 
 * 		@version @release@ 
 *
 *     '$Author: harris $'
 *     '$Date: 2001-10-11 12:38:37 $'
 *     '$Revision: 1.3 $'
 */
package vegclient.databasemanager;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;

import vegclient.framework.sql2db;
import vegclient.databasemanager.*;



public class PostgresLinuxPlugin implements DatabaseManagerPluginInterface
{
	
	//used classes
	sql2db sqlFile = new sql2db();

	public String baseTablesScript = "./lib/vegPlot2001DBTables_postgres.sql";
	public String summaryTablesScript = "./lib/makePlotSummaryTables_postgres.sql";
	
	/** 
	 * method that creates the database  -- named something like 'nvc'
	 */
	public void createDatabase( String databaseName )
	{
		System.out.println("creating database: "+databaseName);
		 try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("createdb "+ databaseName);
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while (( response=in.readLine() ) != null  ) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				if ( t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					System.out.println(buf);
				}
				else
				{
					System.out.println( "Executable returned a blank line: ");
				}
			}

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/** 
	 * method that creates the database  -- named something like 'nvc'
	 */
	public void createUser( String userName )
	{
		System.out.println("creating user: "+userName);
		 try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("createuser -d -a "+ userName);
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while (( response=in.readLine() ) != null  ) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				if ( t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					System.out.println(buf);
				}
				else
				{
					System.out.println( "Executable returned a blank line: ");
				}
			}

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	
	/** 
	 * method that initiates the database
	 */
	public void initDatabase( String database )
	{
		System.out.println("initilizing the postgres database on the linux os: " + database);
		 try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("initdb "+ database);
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while (( response=in.readLine() ) != null  ) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				if ( t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					System.out.println(buf);
				}
				else
				{
					System.out.println( "Executable returned a blank line: ");
				}
			}

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/** 
	 * method that starts the database
	 *
	 */
	public void startDatabase( String database)
	{
		System.out.println("starting the postgres database on the linux os");
		 try 
		{
			Thread thread = new Thread();
		
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("postmaster -S -d2 -i -D " + database +" & ");
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			
			while (( response=in.readLine() ) != null  ) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				if ( t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					System.out.println(buf);
				}
				else
				{
					System.out.println( "Executable returned a blank line: ");
				}
			}
		System.out.println("started ");
		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
		}
		
		
	}

	
	/** 
	 * method that stops the database
	 *
	 */
	public void stopDatabase()
	{
		System.out.println("stopting the postgres database on the linux os");
		
		 try 
		{
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("killall postmaster");
			listenerStream = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in=new BufferedReader(new InputStreamReader(listenerStream));
			String response = null;
	
			while ((response=in.readLine()) !=null) 
			{
				StringTokenizer t = new StringTokenizer(response, "	\t");
				String buf = t.nextToken();
				System.out.println(buf);
			}

		}
		catch (Exception e) 
		{
			System.out.println("Exception " 
			+ e.getMessage());
		}
		
	}
	
	/** 
	 * method that creates the base tables
	 *
	 */
	public void createBaseTables()
	{
		System.out.println("creating the base tables on the Postgres database platform");
		//sqlFile.insertStatement(baseTablesScript);
	}
	
	/** 
	 * method that creates the summary tables -- used for querying
	 *
	 */
	public void createSummaryTables()
	{
		System.out.println("creating the base tables on the Postgres database platform");
		
		sqlFile.insertStatement(summaryTablesScript);
		
	}
}
