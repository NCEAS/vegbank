/*
*  '$RCSfile: DatabaseAccess.java,v $'
 *  Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             			National Center for Ecological Analysis and Synthesis
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-17 20:31:40 $'
 *	'$Revision: 1.8 $'
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author gabriel
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatabaseAccess
{

	//Global variables for the database connection
	
	/**
	 * Runs select against the database
	 *
	 * @param inputStatement
	 * @return ResutSet
	 */
	public ResultSet issueSelect(String inputStatement) throws SQLException 
	{
		DBConnection dbConn = null;//DBConnection
		int serialNumber = -1;//DBConnection serial number
		PreparedStatement pstmt = null;
		ResultSet results = null;
		//	Get DBConnection
		try
		{
			dbConn=DBConnectionPool.getDBConnection("This is an empty string");
			serialNumber=dbConn.getCheckOutSerialNumber();
			
			System.out.println("DatabaseAccess > Running query: " + inputStatement);
			
			Statement query = dbConn.createStatement();
			results = query.executeQuery(inputStatement);
		}
		finally
		{
			//Return dbconnection too pool
			DBConnectionPool.returnDBConnection(dbConn, serialNumber);
		}
		return results;
	} //end method
	
	
	/**
	 * Runs select against the database
	 *
	 * @param inputStatement
	 * @return ResutSet
	 */
	public int issueUpdate(String inputStatement) throws SQLException 
	{
		DBConnection dbConn = null;//DBConnection
		int serialNumber = -1;//DBConnection serial number
		PreparedStatement pstmt = null;
		int results = -1;
		//	Get DBConnection
		try
		{
			dbConn=DBConnectionPool.getDBConnection("This is an empty string");
			serialNumber=dbConn.getCheckOutSerialNumber();
			
			System.out.println("DatabaseAccess > Running query: " + inputStatement);
			
			Statement query = dbConn.createStatement();
			results = query.executeUpdate(inputStatement);
		}
		finally
		{
			//Return dbconnection too pool
			DBConnectionPool.returnDBConnection(dbConn, serialNumber);
		}
		return results;
	} //end method
	
	/**
	 * 
	 * @return
	 * @deprecated
	 */
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
