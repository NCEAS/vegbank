/**
 *  '$RCSfile: EcoartVegCommunitySource.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-02-13 18:41:21 $'
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

//package edu.ucsb.nceas.vegcommunity;
import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
	
	/**
 	 * This class is the basic interface to the vegetation community data 
 	 * stored in the ecoart database and is to be used by the class that loads the
 	 * VegBank veg community database tables  
 	 */
	public class EcoartVegCommunitySource
	{
	private String dbUrl = "jdbc:odbc:ecoart"; 
	private Connection con = null;
	//the level in the heirarchy that that community is in
	private String level = null;
	
	
	
	//constructor to create the database connections etc..
	public EcoartVegCommunitySource()
	{
			try 
		{
			// Load the sun jdbc-odbc bridge driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// connect to the jdbc-odbc bridge driver
			con = DriverManager.getConnection(dbUrl, "user", "pass");

			// Get the DatabaseMetaData object and display
			// some information about the connection
			DatabaseMetaData dma = con.getMetaData();

			System.out.println("EcoartVegCommunitySource > Connected to " + dma.getURL() );
			System.out.println("EcoartVegCommunitySource > Driver       " + dma.getDriverName() );
			System.out.println("EcoartVegCommunitySource > Version      " + dma.getDriverVersion() );
			System.out.println("EcoartVegCommunitySource > Catalog      " + con.getCatalog() );
			
		}
		catch (SQLException ex) 
		{
			// Error, a SQLException was generated. Display the error information
			System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
			while (ex != null)
			{
				System.out.println ("EcoartVegCommunitySource > ErrorCode: " + ex.getErrorCode () + "<BR>");
				System.out.println ("EcoartVegCommunitySource > SQLState:  " + ex.getSQLState () + "<BR>");
				System.out.println ("EcoartVegCommunitySource > Message:   " + ex.getMessage () + "<BR>");
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception ex) 
		{   // All other types of exceptions
			System.out.println("EcoartVegCommunitySource > Exception: " + ex );
		}
	}

	/**
 	 * method that takes a community code, in this case the ecoart code and
	 * returns the community name that is associated with the ecoart code.
	 * specifically there is an 'Elcode' that is associated with the 
	 * community Associations, each code represents a unique community concept
	 * and thus by using these codes the attributes realeted to the community
	 * entity can be returned to the calling method
	 *
	 * @param community -- the ecoart community code, can be an alliance code or a
	 * 	elcode
	 * @return commName -- the name of the community
	 */
	 private String getCommunityName(String community)
	 {
		 String commName = null;
		 try
		 {
			// Create a Statement so we can submit SQL statements to the driver
			Statement stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select "
			+" ([Gname]) "
			+" from ETC where ([Elcode]) like '"+community+"'");
			while (rs.next()) 
			{
				commName = rs.getString(1);
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commName);
	 }
	 
	 /**
	  * method that returns the community codes for the entire ecoart 
		* database
		*
		* @return communities -- a vector containing all the communities
		*/
	 private Vector getCommunityCodes()
	 {
		Vector communities = new Vector();
		 try
		 {
			// Create a Statement so we can submit SQL statements to the driver
			Statement stmt = con.createStatement();
			//create the result set
			ResultSet rs = stmt.executeQuery("select distinct "
			+" ([Elcode]) "
			+" from ETC");
			while (rs.next()) 
			{
				 communities.addElement(rs.getString(1));
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(communities);
	 }
	 
	 /**
	  * main method for testing this code
		*/
		public static void main(String[] args)
		{
			EcoartVegCommunitySource source = new EcoartVegCommunitySource();
			String name = source.getCommunityName("CEGL000001");
			Vector v = source.getCommunityCodes();
			System.out.println("EcoartVegCommunitySource > name: " + name);
			System.out.println("EcoartVegCommunitySource > name: " + v);
		}
	 
	 
	 
	 


}
