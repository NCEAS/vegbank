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
 *     '$Date: 2001-10-11 10:21:35 $'
 *     '$Revision: 1.1 $'
 */
package vegclient.databasemanager;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;

import vegclient.databasemanager.*;



public class HypersonicSQLPlugin implements DatabaseManagerPluginInterface
{

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
			//Thread thread = new Thread();
		
			Process listener;
			DataInputStream listenerStream;
			listener = Runtime.getRuntime().exec("./bin/HypersonicManager start");
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
		System.out.println("stopting the HypersonicSQL database");
		
		
	}
	

}
