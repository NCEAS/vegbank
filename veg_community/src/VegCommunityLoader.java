/**
 *  '$RCSfile: VegCommunityLoader.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-02-21 21:40:42 $'
 * '$Revision: 1.2 $'
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
public class VegCommunityLoader
{
		Connection conn = null;
		boolean commit = true;
		
		//constructor
		public VegCommunityLoader()
		{
			try
			{
				conn = getConnection();
			}
			catch (Exception e)
		 	{
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 	}
		}
		
		
/**
	* method that will return a database connection for use with the database
	*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//the framework database
			c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/test", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("failed making db connection: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	/**
	 * method that inserts a community
	 */
	 private void insertCommunity(String conceptCode, String conceptLevel, 
	 String commName, String otherCitationDetails, String dateEntered, 
	 String parentCommunity )
	 {
		 try
		 {
			//turn off autocommit
			conn.setAutoCommit(false);
			int refId = insertCommunityReference(otherCitationDetails);
			System.out.println("VegCommunityLoader > refId: " + refId);
			int commNameId = insertCommunityName(commName, refId, dateEntered);
			System.out.println("VegCommunityLoader > commNameId: " + commNameId);
			int commConceptId = insertCommunityConcept(conceptCode, commNameId, 
			conceptLevel, parentCommunity, commName);
			System.out.println("VegCommunityLoader > commConceptId: " + commConceptId);
			//if the class variable 'commit' is still true then commit
			//otherwise rollback the connection
			if (this.commit == true)
			{
				conn.commit();
			}
			else
			{
				conn.rollback();
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	 
	 /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityName(String commName, int refId, String dateEntered)
	  {
		 int commNameId = 0; 
		 try
		 {
			boolean commExists = communityNameExists(commName);
			System.out.println("VegCommunityLoader > commExists: " + commExists); 
			
			if (commExists == true)
			{
				commNameId = getCommunityNameId(commName);
				//throw new Exception("reference already exists");
			}
			else
			{
				StringBuffer sb = new StringBuffer();
				//insert the VALS
				sb.append("INSERT into COMMNAME (commname, commreference_id, dateEntered) "
				+" values(?, ?, ?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  			// Bind the values to the query and execute it
  			pstmt.setString(1, commName);
				pstmt.setInt(2, refId);
				pstmt.setString(3, dateEntered);
				
				//execute the p statement
  			pstmt.execute();
  			// pstmt.close();
				//get the refId
				sb = new StringBuffer();
				sb.append("SELECT commname_id from COMMNAME where commname"
				+" like '"+commName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					commNameId = rs.getInt(1);
					cnt++;
				}
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commNameId);
	 }
	 
	 
	 /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityConcept(String commConceptCode, int commNameId, 
		String conceptLevel, String parentCommunity, String conceptDescription )
	  {
		 int commConceptId = 0; 
		 try
		 {
			StringBuffer sb = new StringBuffer();
				
			//insert the strata values
			sb.append("INSERT into COMMCONCEPT (commname_id, ceglcode, commlevel, "
			+" conceptDescription) "
			+" values(?, ?, ?, ?)");
				
			
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		// Bind the values to the query and execute it
  		pstmt.setInt(1, commNameId);
			pstmt.setString(2, commConceptCode);
			pstmt.setString(3, conceptLevel);
			pstmt.setString(4, conceptDescription);
				
			//execute the p statement
  		pstmt.execute();
			
			//update the parentCommunity
			if (parentCommunity != null)
			{
				System.out.println("VegCommunityLoader > updating parentCommunity" );
				sb = new StringBuffer();
				sb.append("UPDATE commconcept set commparent = ");
				sb.append("(select commconcept_id from commconcept where ceglcode = '"+parentCommunity+"')");
				sb.append(" where ceglcode ='"+commConceptCode+"'");
				PreparedStatement pstmt2 = conn.prepareStatement( sb.toString() );
				pstmt2.execute();
			}
			
			 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commConceptId);
	 }
	 
	 
	 /** 
	  * method that inserts the reference data 
		*/
		private int insertCommunityReference(String otherCitationDetails)
	  {
		 int refId = 0; 
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//first see if the reference already exists
			boolean refExists = communityReferenceExists(otherCitationDetails);
			System.out.println("VegCommunityLoader > refExists: " + refExists); 
			
			if (refExists == true)
			{
				refId = getCommunityReferenceId(otherCitationDetails);
				//throw new Exception("reference already exists");
			}
			else
			{
				//insert the strata values
				sb.append("INSERT into COMMREFERENCE (othercitationdetails) "
				+" values(?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				// Bind the values to the query and execute it
  			pstmt.setString(1, otherCitationDetails);
				//execute the p statement
  			pstmt.execute();
  			// pstmt.close();
				//get the refId
				sb = new StringBuffer();
				sb.append("SELECT commreference_id from COMMREFERENCE where othercitationdetails"
				+" like '"+otherCitationDetails+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					refId = rs.getInt(1);
					cnt++;
				}
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(refId);
	 }
	 
	 
	 
	 
	  /** 
	  * method that inserts the reference data 
		*/
		private int getCommunityReferenceId(String otherCitationDetails)
	  {
		 	int refId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commreference_id from COMMREFERENCE where othercitationdetails"
				+" like '"+otherCitationDetails+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					refId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(refId);
	 }
	 
	   /** 
	  * method that inserts the reference data 
		*/
		private int getCommunityNameId(String commName)
	  {
		 	int commNameId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commname_id from COMMNAME where commName"
				+" like '"+commName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					commNameId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commNameId);
	 }
	 
	 
	  /** 
	  * method that inserts the reference data 
		*/
		private boolean communityReferenceExists(String otherCitationDetails)
	  {
			boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT commreference_id from COMMREFERENCE where othercitationdetails"
			+" like '"+otherCitationDetails+"'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				cnt++;
			}
			if (cnt > 0)
			{
				System.out.println("VegCommunityLoader > matching reference:  " + cnt  );
				exists = true;
			}
			else
			{
				exists = false;
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(exists);
	 }
	 
	 
	 private boolean communityNameExists(String commName)
	  {
		boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT commname_id from COMMNAME where commname"
			+" like '"+commName+"'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				//commNameId = rs.getInt(1);
				cnt++;
			}
			if (cnt > 0)
			{
				System.out.println("VegCommunityLoader > matching NAME:  " + cnt  );
				exists = true;
			}
			else
			{
				exists = false;
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(exists);
	 }
	 
	 /**
	  * method to load the ecoart associations
		*/
		private void loadEcoartAssociations()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				Vector v = source.getCommunityCodes("association");
				//this is a hack to restart the ecoart connections after 100 uses
				int connCnt = 0;
				//for (int i =0; i < v.size(); i++)
				for (int i =0; i < 600; i++)
				{
					connCnt++;
					if (connCnt == 100)
					{
						//close the connection or run out of memory soon
						source.con.close();
						source = new EcoartVegCommunitySource();
						connCnt = 0;
					}
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "association";
					String dateEntered = "11-FEB-2002";
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, "ecoart", 
						dateEntered, parentCommunity);
					}
				}
				//close the connections 
				source.con.close();
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	 
	  /**
	  * method to load the ecoart alliances
		*/
		private void loadEcoartAlliances()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				Vector v = source.getCommunityCodes("alliance");
				//this is a hack to restart the ecoart connections after 100 uses
				int connCnt = 0;
				for (int i =0; i < v.size(); i++)
				{
					connCnt++;
					if (connCnt == 100)
					{
						//close the connection or run out of memory soon
						source.con.close();
						source = new EcoartVegCommunitySource();
						connCnt = 0;
					}
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "alliance";
					String dateEntered = "11-FEB-2002";
					String parentCommunity = null; //this means top level
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, "ecoart", 
						dateEntered, parentCommunity);
					}
				}
				//close the connections 
				source.con.close();
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	 
	 
	 
	 /**
	  * main method for testing this code
		*/
		public static void main(String[] args)
		{
			try
			{
				VegCommunityLoader loader = new VegCommunityLoader();
				//loader.insertCommunity("TEST", "ecoart");
				loader.loadEcoartAlliances();
				loader.loadEcoartAssociations();
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	 
 
}
