/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 * 				the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 * 		
 *		 '$Author: harris $'
 *     '$Date: 2001-06-14 05:43:31 $'
 *     '$Revision: 1.1 $'
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
	
	//constructor -- define as static the LocalDbConnectionBroker 
	//so that methods called by this class can access the 'local' 
	//pool of database connections
	static LocalDbConnectionBroker lb;
	
	
	public void createUser(String emailAddress, String passWord)
	{
		try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
			Statement query = conn.createStatement ();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into USER_INFO (EMAIL_ADDRESS, PASSWORD) ");
			sb.append("VALUES ('"+emailAddress+"', '"+passWord+"')");
			
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
}
