/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 * 				the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 * 		
 *		 '$Author: harris $'
 *     '$Date: 2002-08-29 17:22:51 $'
 *     '$Revision: 1.2 $'
 * @deprecated -- this class is now part of the servlet.authentication package
 */

package databaseAccess;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

import databaseAccess.*;

public class UserDatabaseAccess 
{
	
	//constructor -- define as static the LocalDbConnectionBroker 
	//so that methods called by this class can access the 'local' 
	//pool of database connections
	static LocalDbConnectionBroker lb;
	
	
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
			lb.manageLocalDbConnectionBroker("initiate");
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into USER_INFO (EMAIL_ADDRESS, PASSWORD, GIVEN_NAME, SUR_NAME, REMOTE_ADDRESS, TICKET_COUNT) ");
			sb.append("VALUES ('"+emailAddress+"', '"+passWord+"', '"+givenName+"', '"+surName+"', '"+remoteAddress+"', "+"1 )");
			
			//issue the query
			query.executeUpdate(sb.toString());
			lb.manageLocalDbConnectionBroker("destroy");
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
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
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
		lb.manageLocalDbConnectionBroker("initiate");
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
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
			conn.close();
			lb.manageLocalDbConnectionBroker("destroy");
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
