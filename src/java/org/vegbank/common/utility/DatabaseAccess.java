/*
*  '$RCSfile: DatabaseAccess.java,v $'
 *  Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             			National Center for Ecological Analysis and Synthesis
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-17 00:15:54 $'
 *	'$Revision: 1.15 $'
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author gabriel
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatabaseAccess
{
	private static Log log = LogFactory.getLog(DatabaseAccess.class);
	private Statement lastStmt = null;

	//Global variables for the database connection
	
	/**
	 * Runs select against the database
	 * 
	 * Kept in for backward compatibility at older call sites that don't call the
	 * full method signature (String, int, int). ResultSet.CONCUR_READ_ONLY, and 
	 * ResultSet.TYPE_FORWARD_ONLY are the default flags.
	 *
	 * @param inputStatement The statement to run
	 * @return ResutSet
	 */
	public ResultSet issueSelect(String inputStatement) throws SQLException 
	{
		return issueSelect(inputStatement, ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_FORWARD_ONLY);
	} //end method
	
	/**
	 * Runs select against the database
	 *
	 * @param inputStatement The statement to run
	 * @param resultSetType The result set type, i.e., read only
	 * @param resultSetConcurrency The result set concurrency, i.e., scrollable
	 * @return ResutSet
	 */
	public ResultSet issueSelect(String inputStatement, 
		int resultSetType, 
		int resultSetConcurrency) throws SQLException 
	{
		DBConnection dbConn = null;//DBConnection
		int serialNumber = -1;//DBConnection serial number
		ResultSet results = null;
		Statement query = null;
		//	Get DBConnection
		try {
			dbConn=DBConnectionPool.getInstance().getDBConnection("Select from database");
			//serialNumber=dbConn.getCheckOutSerialNumber();
			
			log.debug(":*: " + inputStatement);
			
			query = dbConn.createStatement(resultSetType, resultSetConcurrency);
			results = query.executeQuery(inputStatement);
		} finally {
			//Return dbconnection too pool
            //log.debug("@ calling returnDBConnection()");
            lastStmt = query;
			DBConnectionPool.returnDBConnection(dbConn);
		}
		return results;
	}
	
	/**
	 * Runs select against the database
	 *
	 * @param inputStatement
	 * @return ResutSet
	 */
	public long issueUpdate(String inputStatement) throws SQLException 
	{
		DBConnection dbConn = null;
		int serialNumber = -1;
		long results = -1;
		Statement query = null;

		try {
			dbConn=DBConnectionPool.getInstance().getDBConnection("This is an empty string");
			//serialNumber=dbConn.getCheckOutSerialNumber();
			
			log.debug(":+: " + inputStatement);
			
			query = dbConn.createStatement();
			results = query.executeUpdate(inputStatement);
		} finally {
			//Return dbconnection to pool
            query.close();
            log.debug("@ calling returnDBConnection()");
			DBConnectionPool.returnDBConnection(dbConn);
		}
		return results;
	}


    /**
     * Closes most recent query.
     */
    public void closeStatement() {
        try {
            if (lastStmt != null) {
                lastStmt.close();
            }
        } catch (SQLException ex) {
            log.debug("Unable to close previous statment: " + ex.toString());
        }
    }

    /**
     * Returns recent query.
     */
    public Statement getLastStatement() {
        return lastStmt;
    }

}
