/**
 * class that hadles all the RDBMS management utilities
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-09 22:37:47 $'
 * 	'$Revision: 1.1 $'
 */
 
package vegclient.databasemanager;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;


import vegclient.databasemanager.*;
import vegclient.framework.sql2db;
import vegclient.framework.XMLparse;
import vegclient.framework.utility;



public class DatabaseManager
{
	
	
	public String RDBMSType = null;
	public String OSType = null;
	public String DBManagerPlugin = null;
	public String workingDirectory = null;
	public String dataDirectory = null;
	public String baseTablesScript = "vegPlot2001DBTables_postgres.sql";
	public String summaryTablesScript = "makePlotSummaryTables_postgres.sql";
	public String databaseName = "nvc";
	public String databaseUserName = "datauser";
	
	public String configFile = "./lib/config.xml";
	
	//this is the plugin object that will be used
	Object pluginObj = null;
	
	//used classes
	XMLparse parse = new XMLparse();
	utility util = new utility();
	sql2db sqlFile = new sql2db();
	
	/** 
	 *  constructor method
	 */
	public DatabaseManager()
	{
		RDBMSType = parse.getNodeValue(configFile, "RDBMSType");
		OSType = parse.getNodeValue(configFile, "OSType");
		DBManagerPlugin = parse.getNodeValue(configFile, "DBManagerPlugin");
		workingDirectory = parse.getNodeValue(configFile, "workingDirectory");
		
		//this is the postgres data directory
		if ( workingDirectory.endsWith("/") )
		{
			dataDirectory = workingDirectory+"database";
		}
		else
		{
			dataDirectory = workingDirectory+"/database";
		}
		
		
		
    try
    { //create a generic object of the type specified in the config file.
      pluginObj = util.createObject(DBManagerPlugin);
    }
    catch(Exception e)
    {
      System.out.println("Error getting plugin: " + e.getMessage());
    }
		
	}
	
	
	
	/** 
	 * method to create the base tables
	 */
	 
	public void createBaseTables( )
	{
		sqlFile.insertStatement(baseTablesScript);
	}
	
	/** 
	 * method to create the base tables
	 */
	 
	public void createSummaryTables( )
	{
		sqlFile.insertStatement(summaryTablesScript);
	}
	
	
	/** 
	 * method to create the base tables
	 */ 
	public void createUser( )
	{
		try 
		{
    	//cast the object and make the call to the interface specified runCode
   	 //method. 
	 	  ((DatabaseManagerPluginInterface)pluginObj).createUser( databaseUserName );
		}
		
		 catch(Exception e)
    {
      System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
    }
		
	}
	
		
	/** 
	 * method to create the database 
	 */ 
	public void createDatabase( )
	{
		try 
		{
    	//cast the object and make the call to the interface specified runCode
   	 //method. 
	 	  ((DatabaseManagerPluginInterface)pluginObj).createDatabase( databaseName );
		}
		
		 catch(Exception e)
    {
      System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
    }
		
		
	}
	
	
	
	/** 
	 * method to initialize the database
	 */
	 
	public void initDatabase( )
	{
		try 
		{
    	//cast the object and make the call to the interface specified runCode
   	 //method. 
	 	  ((DatabaseManagerPluginInterface)pluginObj).initDatabase( dataDirectory );
		}
		
		 catch(Exception e)
    {
      System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
    }
	}
	
	/** 
	 * method to start the database
	 */
	 
	public void startDatabase()
	{
			try 
		{
    	//cast the object and make the call to the interface specified runCode
   	 //method. 
	 	  ((DatabaseManagerPluginInterface)pluginObj).startDatabase( dataDirectory );
		}
		
		 catch(Exception e)
    {
      System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
    }
	}
	
	
	/** 
	 * method to stop the database
	 */
	 
	public void stopDatabase()
	{
			try 
		{
    	//cast the object and make the call to the interface specified runCode
   	 //method. 
	 	  ((DatabaseManagerPluginInterface)pluginObj).stopDatabase();
		}
		
		 catch(Exception e)
    {
      System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
    }
	}
	

	
	
	/**
	 * main method for testing 
	 */
	public static void main(String[] args) 
	{
		DatabaseManager manager = new DatabaseManager();
		
		if (args.length != 1) 
		{
			System.out.println("USAGE java DataManager action {init, start, stop}");
		}
		else
		{
			
			try 
			{
				String action = args[0].trim();
			//	DatabaseManager manager = new DatabaseManager();
				System.out.println("RDBMS TYPE: "+ manager.RDBMSType);
				System.out.println("OS TYPE: "+ manager.OSType);
				System.out.println("WORKING DIRECTORY: " + manager.workingDirectory);
				System.out.println("DBManagerPlugin: "+manager.DBManagerPlugin);
				//use the action to figure out what to run
				if ( action.equals("start") )
				{
					manager.startDatabase();
				}
				else if ( action.equals("stop") )
				{
					manager.stopDatabase();
				}
				else if ( action.equals("init") )
				{
					manager.initDatabase(  );
				}
				else 
				{System.out.println("unrecognized action: "+ action); }
			}
			catch (Exception e) 
			{
				System.out.println("Exception: " 
				+ e.getMessage());
			}
		}
	}
}
	
	
