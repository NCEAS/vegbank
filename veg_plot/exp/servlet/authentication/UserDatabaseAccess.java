package servlet.authentication;
/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 * 				the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 * 		
 *		 '$Author: harris $'
 *     '$Date: 2002-04-04 03:00:07 $'
 *     '$Revision: 1.3 $'
 */


import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;



public class UserDatabaseAccess 
{
	
	
	/**
	 * method to get the password from the database based on an email address
	 * 
	 * @param emailAddress
	 */
	public String getPassword(String emailAddress)
	{
		System.out.println("UserDatabaseAccess > looking up the password for user: " + emailAddress);
		String s = null;
		try 
		{
			//get the connections etc
			Connection conn = getConnection();
			Statement query = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select password from USER_INFO ");
			sb.append("where upper(EMAIL_ADDRESS) like '"+emailAddress.toUpperCase()+"' ");
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			while (rs.next()) 
			{
     		s = rs.getString(1);
    	}
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(s);
	}
	
	
	
	
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
			//get the connections etc
			Connection conn = getConnection();
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into USER_INFO (EMAIL_ADDRESS, PASSWORD, GIVEN_NAME, SUR_NAME, REMOTE_ADDRESS, TICKET_COUNT) ");
			sb.append("VALUES ('"+emailAddress+"', '"+passWord+"', '"+givenName+"', '"+surName+"', '"+remoteAddress+"', "+"1 )");
			
			//issue the query
			query.executeUpdate(sb.toString());
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " 
			+e.getMessage());
			e.printStackTrace();
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
			//get the connections etc
			Connection conn = getConnection();
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			
	//		java.util.SimpleDateFormat formatter = new java.util.SimpleDateFormat("yy-MM-dd HH:mm:ss");
 			java.util.Date localtime = new java.util.Date();
 	//		String dateString = formatter.format(localtime);
			
			System.out.println("UserDatabaseAccess > Database date: "+localtime);
			
			sb.append("UPDATE USER_INFO set TICKET_COUNT = TICKET_COUNT + 1 ");
			sb.append("WHERE EMAIL_ADDRESS like '"+emailAddress+"' ");
						
			//issue the query
			query.executeUpdate(sb.toString());
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " 
			+e.getMessage());
			e.printStackTrace();
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
		
			//get the connections etc
			Connection conn = getConnection();
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
					System.out.println("UserDatabaseAccess > Retrieved Name: "+emailAddress);
					//validate the email and passwords
					if ( emailAddress.trim().equals(DBEmailAddress) 
						&& passWord.trim().equals(DBPassWord) )
						{
							System.out.println("UserDatabaseAccess > Accepted at the db level");
							//update the ticket count
							updateTicketCount( DBEmailAddress );
							return(true);
						}
						else 
						{
							System.out.println("UserDatabaseAccess > accepted user but denied access");
							return(false);
						}
				}
				else 
				{
					System.out.println("UserDatabaseAccess > User not known in database");
				}
			}
			conn.close();
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " 
			+e.getMessage());
			e.printStackTrace();
		}
		//default is false
		return(false);
	}
	
	/** 
	 * method that returns a cookie value, as a string, for a username
	 * for now the cookie value will just be the name of the user
	 */
	public String getUserCookieValue(String userName)
	{
		return(userName);
	}

	/**
	 * utility method to provide this class a connection to the 
	 * 'framework' database, a database that will contain the 
	 * database authentication tables etc
	 */
	private Connection getConnection()
	{

		Connection conn = null;
 		try 
 		{
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/framework", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("UserDatabaseAccess > failed making db connection: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
		return(conn);
	}

}
