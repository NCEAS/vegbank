/**
 *  '$RCSfile: DBConnectionPool.java,v $'
 *    Purpose: A class represent a DBConnection pool. Another user can use the
 *    object to initial a connection pool, get db connection or return it. 
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Jing Tao
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-07-23 21:51:18 $'
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

package org.vegbank.common.utility;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Vector;

/** 
 * A class represent a DBConnection pool. Another user can use the
 * object to initial a connection pool, get db connection or return it.
 * This a singleton class, this means only one instance of this class could
 * be in the program at one time. 
 */
public class DBConnectionPool implements Runnable
{

	//static attributes
	private static DBConnectionPool instance;
	private static Vector connectionPool;
	private static Thread runner;
	private static DatabaseUtility dbUtil = new DatabaseUtility();
	  
	//maximum connection number in the connection pool
	final static int MAXIMUMCONNECTIONNUMBER=
									Integer.parseInt(DatabaseUtility.getOption("maximumConnections"));
  
	//inintila connection number int the connection pool
	final static int INITIALCONNECTIONNUMBER=
									Integer.parseInt(DatabaseUtility.getOption("initialConnections"));
 
	//the number to increase connection pool size
	final static int INCREASECONNECTIONNUMBER=
								Integer.parseInt(DatabaseUtility.getOption("incrementConnections"));
                
	//maximum age for a connection (in milli seconds)
	final static long MAXIMUMAGE =
									Long.parseLong(DatabaseUtility.getOption("maximumConnectionAge"));
	//maximum connection time for a connection ( in milli second)
	final static long MAXIMUMCONNECTIONTIME =
								 Long.parseLong(DatabaseUtility.getOption("maximumConnectionTime"));
	//maximum number for using a connection.
	final static int MAXIMUMUSAGENUMBER =
									Integer.parseInt(DatabaseUtility.getOption("maximumUsageNumber"));
	//the parameter if run dbconncestionrecyclethread or not
	final static String DBCONNECTIONRECYCLETHREAD =
	DatabaseUtility.getOption("runDBConnectionRecycleThread");
	//the cycle time of connection recycle action
	final static long CYCLETIMEOFDBCONNECTION = 
							 Long.parseLong(DatabaseUtility.getOption("cycleTimeOfDBConnection"));
               
	//the number for trying to check out a connection in the pool in 
	//getDBConnection method
	final static int LIMIT = 2;
  
	final static int FREE = 0; //status of a connection
	final static int BUSY = 1; //statis of a connection
	/**
	 * Returns the single instance, creating one if it's the
	 * first time this method is called.
	 */
	public static synchronized DBConnectionPool getInstance()
																 throws SQLException 
	{
		System.out.println
			("MaximumConnectionNumber: "+MAXIMUMCONNECTIONNUMBER);
		System.out.println
			("Intial connection number: "+INITIALCONNECTIONNUMBER);
		System.out.println
			("Increated connection Number: "+INCREASECONNECTIONNUMBER);
		System.out.println
			("Maximum connection age: "+MAXIMUMAGE);
		System.out.println
			("Maximum connection time: "+MAXIMUMCONNECTIONTIME);
		System.out.println
			("Maximum usage count: "+MAXIMUMUSAGENUMBER);
		System.out.println
			("Running recycle thread or not: "+DBCONNECTIONRECYCLETHREAD);
		System.out.println
			("Cycle time of recycle: "+CYCLETIMEOFDBCONNECTION); 
		if (instance == null) 
		{
			instance = new DBConnectionPool();
		}
		return instance;
	}//getInstance


	/**
	 * This is a private constructor since it is singleton
	 */
   
	private DBConnectionPool()  throws SQLException 
	{
		connectionPool = new Vector();
		initialDBConnectionPool();
		//running the thread to recycle DBConnection
		if (DBCONNECTIONRECYCLETHREAD.equals("on"))
		{
			runner = new Thread(this);
			runner.start();
		}
	}//DBConnection

	/**
	 * Method to get the size of DBConnectionPool
	 */
	public int getSizeOfDBConnectionPool()
	{
		return connectionPool.size();
	}
  
  
	/**
	 * Method to initial a pool of DBConnection objects 
	 */
	private void initialDBConnectionPool() throws SQLException 
	{

		DBConnection dbConn = null;
    
		for ( int i = 0; i < INITIALCONNECTIONNUMBER; i++ ) 
		{
			//create a new object of DBConnection
			//this DBConnection object has a new connection in it
			//it automatically generate the createtime and tag
			dbConn = new DBConnection();
			//put DBConnection into vetor
			connectionPool.add(dbConn);
		}    
    
  
	}//initialDBConnectionPool
  
	/**
	 * Method to get Connection object (Not DBConnection)
	 */
	/*public static Connection getConnection() throws SQLException
	{
		DBConnection dbConn = null;
		//get a DBConnection
		dbConn = getDBConnection();
		//get connection object in DBConnection object
		//The following getConnections method is in DBConnection class
		return dbConn.getConnections();
	}*/
  
 
	/**
	 * Method to get a DBConnection in connection pool
	 * 1) try to get a DBConnection from DBConnection pool
	 * 2) if 1) failed, then check the size of pool. If the size reach the
	 *    maximum number of connection, throw a exception: couldn't get one
	 * 3) If the size is less than the maximum number of connectio, create some
	 *    new connections and recursive get one
	 * @param methodName, the name of method which will check connection out
	 */
	public static synchronized DBConnection getDBConnection(String methodName) 
																								throws SQLException
	{
		DBConnection db = null;
		int random = 0; //random number
		int index = 0; //index
		int size = 0; //size of connection pool
		System.out.println("Try to checking out connection...");
		size = connectionPool.size();
		System.out.println("size of connection pool: "+size);
    
		 //try every DBConnection in the pool
		//every DBConnection will be try LIMITE times
		for (int j=0 ; j<LIMIT; j++)
		{
			 //create a random number as the started index for connection pool
			//So that the connection ofindex of 0 wouldn't be a the heaviest user
			random = (new Double (Math.random()*100)).intValue();
			for (int i=0; i<size; i++)
			{
				index =(i+random)%size;
				db = (DBConnection) connectionPool.elementAt(index);
				System.out.println("Index: "+index);
				System.out.println("Tag: "+db.getTag());
				System.out.println("Status: "+db.getStatus());
				//check if the connection is free
				if (db.getStatus()==FREE)
				{
					//If this connection is good, return this DBConnection
					if (validateDBConnection(db))
					{
            
						//set this DBConnection status
						db.setStatus(BUSY);
						//increase checkout serial number
						db.increaseCheckOutSerialNumber(1);
						//increase one usageCount
						db.increaseUsageCount(1);
						//set method name to DBConnection
						db.setCheckOutMethodName(methodName);
						db.setAutoCommit(true);
						//debug message
						System.out.println("The connection is checked out: "
																			 +db.getTag());
						System.out.println("The method for checking is: " 
																							+db.getCheckOutMethodName());
						System.out.println("The age is "+db.getAge());
						System.out.println("The usage is "+db.getUsageCount());
						System.out.println("The conection time it has: "
																								+db.getConnectionTime());
						//set check out time
						db.setCheckOutTime(System.currentTimeMillis());
						//check it out
						return db;
					}//if
					else//The DBConnection has some problem
					{
						//close this DBConnection
						db.close();
						//remove it form connection pool
						connectionPool.remove(index);
						//insert a new DBConnection to same palace
						db = new DBConnection();
						connectionPool.insertElementAt(db, index);
					}//else
				}//if
			}//for
		}//for
    
		//if couldn't get a connection, we should increase DBConnection pool
		//if the connection pool size is less than maximum connection number
   
		if ( size < MAXIMUMCONNECTIONNUMBER )
		{
			 if ((size+INCREASECONNECTIONNUMBER) < MAXIMUMCONNECTIONNUMBER)
			 { 
				 //if we can create INCREASECONNECTIONNUMBER of new DBConnection
				 //add to connection pool
				 for ( int i=0; i<INCREASECONNECTIONNUMBER; i++)
				 {
					 DBConnection dbConn = new DBConnection();
					 connectionPool.add(dbConn);
				 }//for
			 }//if
			 else
			 {
				 //There is no enough room to increase INCREASECONNECTIONNUMBER 
				 //we create new DBCoonection to Maximum connection number
				 for (int i= size+1; i<= MAXIMUMCONNECTIONNUMBER; i++)
				 {
					 DBConnection dbConn = new DBConnection();
					 connectionPool.add(dbConn);
				 }//for
			 }//else
   
		}//if
		else
		{
			/*throw new SQLException("The maximum of " +MAXIMUMCONNECTIONNUMBER + 
														" open db connections is reached." +
														" New db connection to MetaCat" +
														" cannot be established.");*/
			 System.out.println("The maximum of " +MAXIMUMCONNECTIONNUMBER + 
														" open db connections is reached." +
														" New db connection to MetaCat" +
														" cannot be established.");
			 return null;
			 //if couldn't get a connection, sleep 20 seconds and try again.
			 /*try
			 {
				 System.out.println("sleep");
				 Thread.sleep(2000);
			 }
			 catch (Exception e)
			 {
			 }*/
         
      
		}//else
    
		//recursive to get new connection    
		return getDBConnection(methodName); 
	}//getDBConnection

 
  
  
	/** 
	 * Method to check if a db connection works fine or not
	 * Check points include:
	 * 1. check the usageCount if it is too many
	 * 2. check the dbconne age if it is too old
	 * 3. check the connection time if it is too long
	 * 4. run simple sql query
	 *
	 * @param dbConn, the DBConnection object need to check
	 */
	private static boolean validateDBConnection (DBConnection dbConn)
	{
    
    
		//Check if the DBConnection usageCount if it is too many
		if (dbConn.getUsageCount() >= MAXIMUMUSAGENUMBER )
		{
			System.out.println("Connection usageCount is too many: "+
			dbConn.getUsageCount());
			return false;
		}
    
		//Check if the DBConnection has too much connection time
		if (dbConn.getConnectionTime() >= MAXIMUMCONNECTIONTIME)
		{
			System.out.println("Connection has too much connection time: "+
			dbConn.getConnectionTime());
			return false;
		}
    
		//Check if the DBConnection is too old
		if (dbConn.getAge() >=MAXIMUMAGE)
		{
			System.out.println("Connection is too old: "+dbConn.getAge());
			return false;
		}
    
		//Try to run a simple query
		try
		{
			long startTime=System.currentTimeMillis();
			DatabaseMetaData metaData = dbConn.getMetaData();
			long stopTime=System.currentTimeMillis();
			//increase one usagecount
			dbConn.increaseUsageCount(1);
			//increase connection time
			dbConn.setConnectionTime(stopTime-startTime);
  
		}
		catch (Exception e)
		{
			System.out.println("Error in validateDBConnection: "
																+e.getMessage());
			return false;
		}
    
		return true;
    
	}//validateDBConnection()
  
	/**
	 * Method to return a connection to DBConnection pool.
	 * @param conn, the Connection object need to check in
	 */
	public static synchronized void returnDBConnection(DBConnection conn, 
																															int serialNumber)
	{
		int index = -1;
		DBConnection dbConn = null;
  
		index = getIndexOfPoolForConnection(conn);
		if ( index ==-1 )
		{
			System.out.println("Couldn't find a DBConnection in the pool"
																	+" which have same tag to the returned"
																	+" DBConnetion object");
			return;
                                  
		}//if
		else
		{
			//check the paramter - serialNumber which will be keep in calling method
			//if it is as same as the object's checkoutserial number.
			//if it is same return it. If it is not same, maybe the connection already
			// was returned ealier.
			System.out.println("serial number in Connection: "
																			+conn.getCheckOutSerialNumber());
			System.out.println("serial number in local: "+serialNumber);
			if (conn.getCheckOutSerialNumber() == serialNumber)
			{
				dbConn = (DBConnection) connectionPool.elementAt(index);
				//set status to free
				dbConn.setStatus(FREE);
				//count connection time
				dbConn.setConnectionTime
													(System.currentTimeMillis()-dbConn.getCheckOutTime());
                          
				//set check out time to 0
				dbConn.setCheckOutTime(0);
        
				System.out.println("Connection: "+dbConn.getTag()+" checked in.");
				System.out.println("Connection: "+dbConn.getTag()+"'s status: "
																										+dbConn.getStatus());
                                                                       
			}//if
			else
			{
				System.out.println("This DBConnection couldn't return");
			}//else
		}//else
   
 
	}//returnConnection
  
	/**
	 * Given a returned DBConnection, try to find the index of DBConnection object
	 * in dbConnection pool by comparing DBConnection' tag and conn.toString.
	 * If couldn't find , -1 will be returned.
	 * @param conn, the connection need to be found
	 */
	private static synchronized int getIndexOfPoolForConnection(DBConnection conn)
	{
		int index = -1;
		String info = null;
		//if conn is null return -1 too
		if (conn==null)
		{
			return -1;
		}
		//get tag of this returned DBConnection
		info = conn.getTag();
		//if the tag is null or empty, -1 will be returned
		if (info==null || info.equals(""))
		{
			return index;
		}
		//compare this info to the tag of every DBConnection in the pool
		for ( int i=0; i< connectionPool.size(); i++)
		{
			DBConnection dbConn = (DBConnection) connectionPool.elementAt(i);
			if (info.equals(dbConn.getTag()))
			{
				index = i;
				break;
			}//if
		}//for
    
		return index;
	}//getIndexOfPoolForConnection  
  
	/**
	 * Method to shut down all connections
	 */
	public static void release()
	{
    
		//shut down the backgroud recycle thread
		if (DBCONNECTIONRECYCLETHREAD.equals("on"))
		{
			runner.interrupt();
		}
		//cose every dbconnection in the pool
		synchronized(connectionPool)
		{
			for (int i=0;i<connectionPool.size();i++)
			{
				try
				{
					DBConnection dbConn= (DBConnection) connectionPool.elementAt(i);
					dbConn.close();
				}//try
				catch (SQLException e)
				{
					System.out.println("Error in release connection: "
																						+e.getMessage());
				}//catch
			}//for
		}//synchronized
	}//release()
  
	/**
	 * periodically to recycle the connection
	 */
	public void run()
	{
		DBConnection dbConn = null;
		//keep the thread running
		while (true)
		{
			//check every dbconnection in the pool
			synchronized(connectionPool)
			{
				for (int i=0; i<connectionPool.size(); i++)
				{
					dbConn = (DBConnection) connectionPool.elementAt(i);
          
					//if a DBConnection conncectioning time for one check out is greater 
					//than 30000 milliseconds print it out
					if (dbConn.getStatus()==BUSY && 
						(System.currentTimeMillis()-dbConn.getCheckOutTime())>=30000)
					{
						System.out.println("This DBConnection is checked out for: "
						+(System.currentTimeMillis()-dbConn.getCheckOutTime())/1000
						+" secs" );
						System.out.println(dbConn.getTag());
						System.out.println("method: "
																					+dbConn.getCheckOutMethodName());
            
					}
          
					//check the validation of free connection in the pool
					if (dbConn.getStatus() == FREE)
					{
						try
						{
							//try to print out the warning message for every connection
							if (dbConn.getWarningMessage()!=null)
							{
								System.out.println("Warning for connection "
									+dbConn.getTag()+" : "+ dbConn.getWarningMessage());
							}
							//check if it is valiate, if not create new one and replace old one
							if (!validateDBConnection(dbConn))
							{
								System.out.println("Recyle it: "+ dbConn.getTag());
								//close this DBConnection
								dbConn.close();
								//remove it form connection pool
								connectionPool.remove(i);
								//insert a new DBConnection to same palace
								dbConn = new DBConnection();
								connectionPool.insertElementAt(dbConn, i);
							 }//if
						}//try
						catch (SQLException e)
						{
							System.out.println("Error in DBConnectionPool.run: "
																							+e.getMessage());
						}//catch
					}//if
				}//for
			}//synchronize   
			//Thread sleep 
			try
			{
				Thread.sleep(CYCLETIMEOFDBCONNECTION);
			}
			catch (Exception e)
			{
				System.out.println("Error in DBConnectionPool.run: "
																							+e.getMessage());
			}
		}//while
	}//run
  
	/**
	 * Method to get the number of free DBConnection in DBConnection pool
	 */
	public static synchronized int getFreeDBConnectionNumber()
	{
		int numberOfFreeDBConnetion = 0; //return number
		DBConnection db = null; //single DBconnection
		int poolSize = 0; //size of connection pool
		//get the size of DBConnection pool
		poolSize = connectionPool.size();
		//Check every DBConnection in the pool
		for ( int i=0; i<poolSize; i++)
		{
      
			db = (DBConnection) connectionPool.elementAt(i);
			//check the status of db. If it is free, count it
			if (db.getStatus() == FREE)
			{
				numberOfFreeDBConnetion++;
			}//if
		}//for
		//return the count result
		return numberOfFreeDBConnetion;
	}//getFreeDBConnectionNumber
      
	 /**
	 * Method to print out the method name which have busy DBconnection
	 */
	public void printMethodNameHavingBusyDBConnection()
	{
    
		DBConnection db = null; //single DBconnection
		int poolSize = 0; //size of connection pool
		//get the size of DBConnection pool
		poolSize = connectionPool.size();
		//Check every DBConnection in the pool
		for ( int i=0; i<poolSize; i++)
		{
      
			db = (DBConnection) connectionPool.elementAt(i);
			//check the status of db. If it is free, count it
			if (db.getStatus() == BUSY)
			{
				System.out.println("This method having a busy DBConnection: "
																		+db.getCheckOutMethodName());
				System.out.println("The busy DBConnection tag is: "
																		+db.getTag());
			}//if
		}//for
  
	}//printMethodNameHavingBusyDBConnection
  
	/**
	 * Method to decrease dbconnection pool size when all dbconnections are idle
	 * If all connections are free and connection pool size greater than 
	 * initial value, shrink connection pool size to intital value
	 */
	public static synchronized boolean shrinkConnectionPoolSize() 
	{
		 int connectionPoolSize = 0; //store the number of dbconnection pool size
		 int freeConnectionSize = 0; //store the number of free dbconnection in pool
		 int difference = 0; // store the difference number between connection size
												 // and free connection
		 boolean hasException = false; //to check if has a exception happend
		 boolean result = false; //result
		 DBConnection conn = null; // the dbconnection
		 connectionPoolSize = connectionPool.size();
		 freeConnectionSize = getFreeDBConnectionNumber();
		 System.out.println("Connection pool size: " +connectionPoolSize);
		 System.out.println("Free Connection number: "+freeConnectionSize);
		 difference = connectionPoolSize - freeConnectionSize;
     
		 //If all connections are free and connection pool size greater than 
		 //initial value, shrink connection pool size to intital value
		 if (difference == 0 && connectionPoolSize > INITIALCONNECTIONNUMBER)
		 {
			 //db connection having index from  to connectionpoolsize -1
			 //intialConnectionnumber should be close and remove from pool
			 for ( int i=connectionPoolSize-1; i >= INITIALCONNECTIONNUMBER ; i--)
			 {
        
				 //get the dbconnection from pool
				 conn = (DBConnection) connectionPool.elementAt(i);
         
				 try
				 {
					 //close conn
					 conn.close();
				 }//try
				 catch (SQLException e)
				 { 
					 // set hadException ture
					 hasException = true;
					 System.out.println("Couldn't close a DBConnection in " +
														"DBConnectionPool.shrinkDBConnectionPoolSize: " +
														e.getMessage());
				 }//catch
                                        
				//remove it from pool
				connectionPool.remove(i);
				// becuase enter the loop, set result true
				result = true;
			 }//for
		 }//if
     
		 //if hasException is true ( there at least once exception happend)
		 // the result should be false
		 if (hasException)
		 {
			 result =false;
		 }//if
		 // return result
		 return result;
	}//shrinkDBConnectionPoolSize
   
		/**
	 * Method to decrease dbconnection pool size when all dbconnections are idle
	 * If all connections are free and connection pool size greater than 
	 * initial value, shrink connection pool size to intital value
	 */
	public static synchronized void shrinkDBConnectionPoolSize() 
	{
		 int connectionPoolSize = 0; //store the number of dbconnection pool size
		 int freeConnectionSize = 0; //store the number of free dbconnection in pool
		 int difference = 0; // store the difference number between connection size
												 // and free connection
    
		 DBConnection conn = null; // the dbconnection
		 connectionPoolSize = connectionPool.size();
		 freeConnectionSize = getFreeDBConnectionNumber();
		 System.out.println("Connection pool size: " +connectionPoolSize);
		 System.out.println("Free Connection number: "+freeConnectionSize);
		 difference = connectionPoolSize - freeConnectionSize;
     
		 //If all connections are free and connection pool size greater than 
		 //initial value, shrink connection pool size to intital value
		 if (difference == 0 && connectionPoolSize > INITIALCONNECTIONNUMBER)
		 {
			 //db connection having index from  to connectionpoolsize -1
			 //intialConnectionnumber should be close and remove from pool
			 for ( int i=connectionPoolSize-1; i >= INITIALCONNECTIONNUMBER ; i--)
			 {
        
				 //get the dbconnection from pool
				 conn = (DBConnection) connectionPool.elementAt(i);
				 //make sure again the DBConnection status is free
				 if (conn.getStatus()==FREE)
				 {
					 try
					 {
						 //close conn
						 conn.close();
					 }//try
					 catch (SQLException e)
					 { 
          
						 System.out.println("Couldn't close a DBConnection in " +
														"DBConnectionPool.shrinkDBConnectionPoolSize: " +
														e.getMessage());
					 }//catch
        
					 //remove it from pool
					 connectionPool.remove(i);
				 }//if
       
			 }//for
		 }//if
     
    
	}//shrinkDBConnectionPoolSize
   
  
}//DBConnectionPoold have been added by the plant Loader, I think, not the preloadin