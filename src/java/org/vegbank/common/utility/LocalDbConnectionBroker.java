package org.vegbank.common.utility;

import java.sql.Connection;

public class LocalDbConnectionBroker 
{

	//constructor -- define as static
	static DbConnectionBroker myBroker;
	//number of times the class has been accessed - for book keeping
	public static int classUses = 0;
	//number of times a given connection has been passed out
	public static int currentConnectionUses = 0;
	//the current connection number (out of the total in the pool)
	public static int curentConnectionNum = 0;
	//define the connection - these are static so that they can be
	//used by various methods
	public static Connection pconn=null; 
	//the Utility class
	private static DatabaseUtility dbUtil =new DatabaseUtility();
	private static  boolean initialized = false;

	/**
	 * main method for testing the connection
	 * pooling class
	 */
	public static void main(String[] args) 
	{
		//iniate the local broker
		String action = "initiate";
		manageLocalDbConnectionBroker(action);
		//call a new method that will send a query and use the connection from this
		// broker
		//Query q = new Query();
		//q.sendQuery();
	}

	
	/**
	 * method that handles requests that are made 
	 * related to connection pooling -- this method 
	 * should be broken up into many smaller methods
	 */
	public static Connection manageLocalDbConnectionBroker(String action) 
	{
		classUses++;
		//define the connection
		///Connection pconn=null; 

		if (action.equals("initiate") ) 
		{	
			try 
			{
				//get the database management parameter settings
				dbUtil.getDatabaseParameters("database", "query");
				System.out.println("LocalDbConnectionBroker > connection string: " + dbUtil.connectionString);
				myBroker = 
					new DbConnectionBroker(
						dbUtil.driverClass, 
						dbUtil.connectionString,
						dbUtil.login,
						dbUtil.passwd,
						dbUtil.minConnections,
						dbUtil.maxConnections, 
						dbUtil.logFile,
						1.0);
						
				pconn=myBroker.getConnection();
				setInitialized(true);
			}
			catch( Exception e ) 
			{
				System.out.println(" failed in: manageLocalDbConnectionBroker "
				+e.getMessage() );
				e.printStackTrace();
			}
		}

		else if (action.equals("getConn") ) 
		{
			// has this been initialized ?
			if (! isInitialized() )
			{
				manageLocalDbConnectionBroker("initiate");
			}
			
			//increment the use of the current connection
			currentConnectionUses++;
	
			///	System.out.println("LocalDbConnectionBroker.currentConnectionUses: "+currentConnectionUses);
			///	System.out.println("LocalDbConnectionBroker.poolSize: "+myBroker.getSize() );
			///	System.out.println("LocalDbConnectionBroker.currentConnectionUses: "+currentConnectionUses);
			///	System.out.println("LocalDbConnectionBroker.currentConnectionNum: "+curentConnectionNum);
	
	
			//use the current connection about 100 times
			if (currentConnectionUses < dbUtil.maxConnectionUses ) 
			{
				return pconn;
			}
	
			//if the current number of connections has reachd the max limit get another
			// from the DBConnectionBroker
			if (currentConnectionUses == dbUtil.maxConnectionUses ) 
			{
				System.out.println("LocalDbConnectionBroker > grabbing a new connection from pool");
		
				//the next logic is to determine if the max number of connections has been
				// exceeded the pool numbber and if so then create a new pool
				if ( myBroker.getSize() == dbUtil.maxConnections ) 
				{
					try 
					{
						System.out.println("LocalDbConnectionBroker > re-starting the connection pooling");
						myBroker = new DbConnectionBroker(dbUtil.driverClass, dbUtil.connectionString,
						dbUtil.login,dbUtil.passwd,dbUtil.minConnections,dbUtil.maxConnections, 
						dbUtil.logFile,1.0);
					} 
					catch( Exception e ) 
					{
						System.out.println(e.getMessage() );
					}
				}
		
				//grab the new connection from the pool
				pconn=myBroker.getConnection();
				currentConnectionUses=0;
				return pconn;
			}
	}

	else if (action.equals("releaseConn") ) 
	{
		try 
		{
			//System.out.println("releasing the connection: "+myBroker.idOfConnection(pconn));
			//System.out.println("age of this connection "+myBroker.getAge(pconn) );
			//pconn.close();
			myBroker.freeConnection(pconn);
		} 
		catch( Exception e ) 
		{
			System.out.println("LocalDbConnectionBroker > Exception: manageLocalDbConnectionBroker realease "
			+e.getMessage() ); e.printStackTrace();
		}
	}

	else if (action.equals("destroy") ) 
	{
		System.out.println("LocalDbConnectionBroker > closing the connection pool");
		try 
		{
			//close the open connection 
			myBroker.freeConnection(pconn);
			pconn.close();
			//destroy the pool
			myBroker.destroy();
		}
		catch( Exception e ) 
		{
			System.out.println("failed in: manageLocalDbConnectionBroker destroy"
			+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	else 
	{ 
		System.out.println("LocalDbConnectionBroker > manageLocalDbConnectionBroker: unrecognized action"
		+action);
	}
	return pconn;
}

	/**
	 * @return
	 */
	public static boolean isInitialized()
	{
		return initialized;
	}

	/**
	 * @param b
	 */
	public static void setInitialized(boolean b)
	{
		initialized = b;
	}

}

