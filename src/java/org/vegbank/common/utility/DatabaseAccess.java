/*
 * Created on Apr 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vegbank.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author gabriel
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatabaseAccess
{

	//Global variables for the database connection

	public String outValueString;
	public String outReturnFields[] = new String[80000];
	//array containg returned vals
	public int outReturnFieldsNum; //number of lines returned
	public Vector returnedValues = new Vector();

	/**
	 * Runs select against the database
	 *
	 * @param inputStatement
	 * @return ResutSet
	 */

	public ResultSet issueSelect(String inputStatement)
	{
		ResultSet results = null;
		try
		{
			//compose and issue a prepared statement for loading a table	
			//execute the query
			Statement query = this.getConnection().createStatement();
			results = query.executeQuery(inputStatement);

		} // end try 
		catch (Exception e)
		{
			System.out.println("DatabaseAccess issueSelect sql: " + inputStatement);
			e.printStackTrace();
		}
		return results;
	} //end method
	
	public void issueInsert(String inputStatement)
	{
		
	}
	
	public Connection getConnection() 
	{
		//define a connection
		Connection conn = null;
		Statement query = null;
		
		//grab a connection from the local connection pooling manager
		try
		{
			//this class should have been initialized by the servlet initialization
			conn = LocalDbConnectionBroker.manageLocalDbConnectionBroker("getConn");
		}

		catch (Exception e)
		{
			System.out.println("DatabaseAccess > Failed obtaining a connection: "
					+ e.getMessage());
		}
		return conn;

	}
	
}
