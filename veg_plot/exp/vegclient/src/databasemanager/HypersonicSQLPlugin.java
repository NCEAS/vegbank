 /**
 * class that hadles all the RDBMS management utilities for the 
 * HypersonicSQL database, which is a pure-java database available 
 * www.sourceforge.net, and that runs on any os, it is small, relatively 
 * fast and open-source
 *
 *
 * 		@author @author@ 
 * 		@version @release@ 
 *
 *     '$Author: harris $'
 *     '$Date: 2001-10-11 12:38:37 $'
 *     '$Revision: 1.4 $'
 */
package vegclient.databasemanager;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;

import vegclient.framework.sql2db;
import vegclient.databasemanager.*;
import org.hsqldb.*;



public class HypersonicSQLPlugin implements DatabaseManagerPluginInterface
{
	
	//used classes
	sql2db sqlFile = new sql2db();
	
	public String baseTablesScript = "./lib/vegPlot2001DBTables_hsqldb.sql";
	public String summaryTablesScript = "./lib/makePlotSummaryTables_hsqldb.sql";

	/** 
	 * method that creates the database  -- named something like 'nvc'
	 */
	public void createDatabase( String databaseName )
	{
		System.out.println("creating HypersonicSQL database: "+databaseName);
	
	}
	
	
	
	/** 
	 * method that creates the database  -- named something like 'nvc'
	 */
	public void createUser( String userName )
	{
		System.out.println("creating HypersonicSQL user: "+userName);
	
		
	}
	
	
	
	
	/** 
	 * method that initiates the database
	 */
	public void initDatabase( String database )
	{
		System.out.println("initilizing HypersonicSQL: " + database);
		
	}
	
	
	
	
	
	/** 
	 * method that starts the database
	 *
	 */
	public void startDatabase( String database)
	{
		System.out.println("starting the HypersonicSQL database: "+database);
		 try 
		{
			Thread t = new Thread("runner") 
			{
			public void run() 
			{

				try 
				{ 
					String[] cmd = new String[0];
					//cmd[0] = "-?" ;
					Server server = new Server();
					server.main(cmd);
					//Server.printHelp();
				} 
	
				catch (Exception e) 
				{ 
					e.printStackTrace(); 
				}
			}
			}; //end thread
			t.setDaemon(false);
			t.start();
			System.out.println("started  the thread");
		
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
		System.out.println("stopting the HypersonicSQL database");
		
		
	}
	
	
	/** 
	 * method that creates the base tables
	 *
	 */
	public void createBaseTables()
	{
		System.out.println("creating the base tables on the HypersonicSQL database platform");
		
		sqlFile.insertStatement(baseTablesScript);
		
	}
	
		
	/** 
	 * method that creates the summary tables -- used for querying
	 *
	 */
	public void createSummaryTables()
	{
		System.out.println("creating the base tables on the HypersonicSQL database platform");
		
		sqlFile.insertStatement(summaryTablesScript);
		
	}

}
