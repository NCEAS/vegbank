/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 * 				the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 * 		
 *		'$Author: farrell $' 
 *     '$Date: 2003-10-17 22:09:14 $'
 *     '$Revision: 1.4 $'
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
 *
 * @deprecated -- this class is now part of the servlet.authentication package
 */

package org.vegbank.databaseAccess;

import java.sql.ResultSet;
import java.sql.Statement;

import org.vegbank.common.utility.*;

public class UserDatabaseAccess 
{
	
	//constructor -- define as static the LocalDbConnectionBroker 
	//so that methods called by this class can access the 'local' 
	//pool of database connections
	//static LocalDbConnectionBroker lb;
	
	
	/**
	 * method to create a new user in the vegetation user 
	 * database.  This is used to create the intial instance 
	 * of a user
	 * @param emailAddress
	 * @param passWord
	 * @param givenName
	 * @param surName 
	 * @param remoteAddress
	 */
	public void createUser(String emailAddress, String passWord, String givenName, 
		String surName, String remoteAddress)
	{
		try 
		{
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			DBConnection conn = DBConnectionPool.getDBConnection("Need connection for inserting user");
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into USER_INFO (EMAIL_ADDRESS, PASSWORD, GIVEN_NAME, SUR_NAME, REMOTE_ADDRESS, TICKET_COUNT) ");
			sb.append("VALUES ('"+emailAddress+"', '"+passWord+"', '"+givenName+"', '"+surName+"', '"+remoteAddress+"', "+"1 )");
			
			//issue the query
			query.executeUpdate(sb.toString());
			DBConnectionPool.returnDBConnection(conn, conn.getCheckOutSerialNumber());
		}
		catch (Exception e) 
		{
			System.out.println("failed " 
			+e.getMessage());
		}
	}
 
 
 
 
	/**
	 * method to update the ticket count of a user -- 
	 * something that is done every time the user logs
	 * into the system
	 * @param emailAddress
	 */
	public void updateTicketCount(String emailAddress)
	{
		try 
		{
	//		lb.manageLocalDbConnectionBroker("initiate");
	//		System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			DBConnection conn = DBConnectionPool.getDBConnection("Need connection for updating ticket count");
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			
	//		java.util.SimpleDateFormat formatter = new java.util.SimpleDateFormat("yy-MM-dd HH:mm:ss");
 			java.util.Date localtime = new java.util.Date();
 	//		String dateString = formatter.format(localtime);
			
			System.out.println("Database date: "+localtime);
			
			sb.append("UPDATE USER_INFO set TICKET_COUNT = TICKET_COUNT + 1 ");
			sb.append("WHERE EMAIL_ADDRESS like '"+emailAddress+"' ");
						
			//issue the query
			query.executeUpdate(sb.toString());
	//		lb.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e) 
		{
			System.out.println("failed " 
			+e.getMessage());
		}
	}
 
 
 
 
 
	/**
	 * methosd to validate the user given a email address and a 
	 * password with the correponding email - password pairs in
	 * the database
	 * @param emailAddress -- the email address that a user passes to the client
	 * @param passWord -- the password that the user passes to the client	 
	 */

	public boolean authenticateUser(String emailAddress, String passWord)
	{
		try 
		{
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			DBConnection conn = DBConnectionPool.getDBConnection("Need connection for authenticating user");
			Statement query = conn.createStatement ();
			ResultSet results= null;
			StringBuffer sb = new StringBuffer();
				sb.append("SELECT EMAIL_ADDRESS, PASSWORD FROM USER_INFO ");
				sb.append("WHERE 	EMAIL_ADDRESS like '"+emailAddress+"'");
		
			//issue the query
			results = query.executeQuery(sb.toString());
			
			//get the results
			while (results.next()) 
			{
				if (results.getString(1) != null) 
				{
					String DBEmailAddress = results.getString(1); 
					String DBPassWord = results.getString(2); 
					System.out.println("Retrieved Name: "+emailAddress);
					//validate the email and passwords
					if ( emailAddress.trim().equals(DBEmailAddress) 
						&& passWord.trim().equals(DBPassWord) )
						{
							System.out.println("Accepted at the db level");
							//update the ticket count
							updateTicketCount( DBEmailAddress );
							return(true);
						}
						else 
						{
							System.out.println("accepted user but denied access");
							return(false);
						}
				}
				else 
				{
					System.out.println("User not known in database");
				}
			}
			DBConnectionPool.returnDBConnection(conn, conn.getCheckOutSerialNumber());
		}
		catch (Exception e) 
		{
			System.out.println("failed > " 
			+e.getMessage());
			e.printStackTrace();
		}
		//default is false
		return(false);
	}


}
