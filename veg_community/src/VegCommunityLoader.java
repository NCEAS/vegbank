/**
 *  '$RCSfile: VegCommunityLoader.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-03-08 18:33:11 $'
 * '$Revision: 1.13 $'
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
		
		//class variables needed for loading the community databases
		private String otherCitationDetails = null;
		private String refAuthors = null;
		private String refTitle = null;
		private String refPubDate = null;
		private String partyOrgName = null;
	
		
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
			c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/communities_dev", "datauser", "");
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
	 String parentCommunity, String allianceTransName )
	 {
		 try
		 {
			//turn off autocommit
			conn.setAutoCommit(false);
			int partyId = insertCommunityParty(this.partyOrgName);
			int refId = insertCommunityReference(this.refAuthors, this.refTitle, 
			this.refPubDate, this.otherCitationDetails);
			
			// INSERT THE NAMES
			int commNameId = insertCommunityName(commName, refId, dateEntered);
			int commCodeId = insertCommunityName(conceptCode, refId, dateEntered);
			
			//load the alliance trans if this is an alliance that trans name is
			//basically a common name used to describe the association 
			int commTransId = 0;
			if (conceptLevel.equals("alliance") &&  allianceTransName != null )
				commTransId = insertCommunityName(allianceTransName, refId, dateEntered);
			
			
			// INSERT THE CONCEPT
			int commConceptId = insertCommunityConcept(conceptCode, commCodeId, 
			conceptLevel, parentCommunity, commName, refId );

			// THE STATUS -- there may be a proble with the date entered
			int commStatusId = insertCommunityStatus(commConceptId, "accepted", 
			dateEntered, partyId, refId, parentCommunity );
			
			
			//UPDATE THE USAGE FOR THE NAMES 
			// - the code
			insertCommunityUsage( commCodeId, commConceptId, partyId, "standard"
			, dateEntered, "NVC", conceptCode, conceptCode);
			// - the name
			insertCommunityUsage( commNameId, commConceptId, partyId, "standard"
			, dateEntered, "NVC", commName, conceptCode);
			//the trans name (alliance only)
			if ( conceptLevel.equals("alliance") &&  allianceTransName != null )
				insertCommunityUsage( commTransId, commConceptId, partyId, "standard"
				, dateEntered, "NVC", allianceTransName ,conceptCode);
			
			
			// DO THE TRANSACTION
			if (this.commit == true)
			{
				conn.commit();
			}
			else
			{
				conn.rollback();
				debugInstanceFailure( commName, conceptCode, allianceTransName);
				//fail
				System.exit(0);
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	 
	 
	 /**
	  * method for printing to the system some of the debugging 
		* information related to the community instance that failed 
		* to load
		*/
		private void debugInstanceFailure(String commName, String conceptCode, 
		String allianceTransName)
		{
			System.out.println("VegCommunityLoader > failed to load instance:");
			System.out.println(" commName: " + commName);
			System.out.println(" codeName: " + conceptCode);
			System.out.println(" transName: " + allianceTransName );
		}
		
		
	   /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityUsage(int commNameId, int commConceptId, 
		int commPartyId, String commNameStatus, String usageStart, 
		String classSystem, String commName, String commCode)
	  {
		 int usageId = 0; 
		 try
		 {
				StringBuffer sb = new StringBuffer();
				//insert the VALS
				sb.append("INSERT into COMMUSAGE( commname_id, commconcept_id, "
				+" commparty_id, commNameStatus, usageStart, classSystem, commName, ceglcode) "
				+" values(?,?,?,?,?,?, ?, ?)"); 
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  			// Bind the values to the query and execute it
  			pstmt.setInt(1, commNameId);
				pstmt.setInt(2, commConceptId);
				pstmt.setInt(3, commPartyId);
				pstmt.setString(4, commNameStatus);
				pstmt.setString(5, usageStart);
				pstmt.setString(6, classSystem);
				pstmt.setString(7, commName);
				pstmt.setString(8, commCode);
				//execute the p statement
  			pstmt.execute();
  			pstmt.close();
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(usageId);
	 }
	 
	 
	 
	  /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityStatus(int commConceptId, String status,  
			String startDate, int partyId, int refId, String parentCommunity)
	  {
		 int statusId = 0; 
		 try
		 {
				StringBuffer sb = new StringBuffer();
				//insert the VALS
				sb.append("INSERT into COMMSTATUS( commconcept_id, commreference_id, "
				+" commconceptstatus, startDate, commparty_id) "
				+" values(?,?,?,?,?)"); 
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  			// Bind the values to the query and execute it
  			pstmt.setInt(1, commConceptId);
				pstmt.setInt(2, refId);
				pstmt.setString(3, status);
				pstmt.setString(4, startDate);
				pstmt.setInt(5, partyId);
				//execute the p statement
  			pstmt.execute();
  			pstmt.close();
				
				//update the parentCommunity
				if (parentCommunity != null)
				{
					//System.out.println("VegCommunityLoader > updating parentCommunity" );
					sb = new StringBuffer();
					sb.append("UPDATE commstatus set commparent = ");
					sb.append("(select commconcept_id from commconcept where ceglcode = '"+parentCommunity+"')");
					sb.append(" where commconcept_id = "+commConceptId+"");
					PreparedStatement pstmt2 = conn.prepareStatement( sb.toString() );
					pstmt2.execute();
					//pstmt2.close();
				}
				
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(partyId);
	 }
	 
	 
	 
	 
	  /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityParty(String partyOrgName)
	  {
		 int partyId = 0; 
		 try
		 {

			 boolean partyExists = communityPartyExists(partyOrgName);
			//System.out.println("VegCommunityLoader > party Exists " ); 
			
			if (partyExists == true)
			{
				partyId = getCommunityPartyId(partyOrgName);
				//throw new Exception("reference already exists");
			}
			else
			{

				StringBuffer sb = new StringBuffer();
				//insert the VALS
				sb.append("INSERT into COMMPARTY (organizationName) "
				+" values(?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  			// Bind the values to the query and execute it
  			pstmt.setString(1, partyOrgName);
				
				//execute the p statement
  			pstmt.execute();
  			// pstmt.close();
				//get the refId
				sb = new StringBuffer();
				sb.append("SELECT commparty_id from COMMPARTY where organizationName"
				+" like '"+partyOrgName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					partyId = rs.getInt(1);
					cnt++;
				}
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(partyId);
	 }
	 
	 
	 /**
	  * method to insert data into the commname table
		*
		* @param commName -- the community name or code
		* @param refId -- the fk value of the reference 
		* @param dateEntered -- the date that the community was loaded to the source
		* 	data base
		*/
		private int insertCommunityName(String commName, int refId, String dateEntered)
	  {
		 int commNameId = 0; 
		 try
		 {
			boolean commExists = communityNameExists(commName);
			//System.out.println("VegCommunityLoader > commExists: " + commExists); 
			
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
			 commit = false;
		 }
		 return(commNameId);
	 }
	 
	 
	 /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityConcept(String commConceptCode, int commNameId, 
		String conceptLevel, String parentCommunity, String conceptDescription, 
		int refId )
	  {
		 int commConceptId = 0; 
		 try
		 {
			StringBuffer sb = new StringBuffer();
				
			//insert the strata values
			sb.append("INSERT into COMMCONCEPT (commname_id, ceglcode, commlevel, "
			+" conceptDescription, commreference_id) "
			+" values(?, ?, ?, ?,?)");
				
			
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  		// Bind the values to the query and execute it
  		pstmt.setInt(1, commNameId);
			pstmt.setString(2, commConceptCode);
			pstmt.setString(3, conceptLevel);
			pstmt.setString(4, conceptDescription);
			pstmt.setInt(5, refId);
			//execute the p statement
  		pstmt.execute();
			
			//update the parentCommunity
			if (parentCommunity != null)
			{
				//System.out.println("VegCommunityLoader > updating parentCommunity" );
				sb = new StringBuffer();
				sb.append("UPDATE commconcept set commparent = ");
				sb.append("(select commconcept_id from commconcept where ceglcode = '"+parentCommunity+"')");
				sb.append(" where ceglcode ='"+commConceptCode+"'");
				PreparedStatement pstmt2 = conn.prepareStatement( sb.toString() );
				pstmt2.execute();
			}
			//get the concept id for return
			commConceptId = getCommunityConceptId(conceptDescription);
			
			
			 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commConceptId);
	 }
	 
	 
	 /** 
	  * method that inserts the reference data into the community refernce 
		* table.  The method returns the primary key value for this data set.
		*
		* @param authors -- string refering to the authors
		* @param pubDate -- the publication date
		* @param  title -- the title of the reference
		* @param otherCitationDetails -- other citation details
		* @return int -- the referenceId primary key for the data described above
		*/
		private int insertCommunityReference(String authors, String title, 
		 String pubDate, String otherCitationDetails)
	  {
		 int refId = 0; 
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//first see if the reference already exists
			boolean refExists = communityReferenceExists(otherCitationDetails);
			//System.out.println("VegCommunityLoader > refExists: " + refExists); 
			
			if (refExists == true)
			{
				refId = getCommunityReferenceId(otherCitationDetails);
				//throw new Exception("reference already exists");
			}
			else
			{
				//insert the strata values
				sb.append("INSERT into COMMREFERENCE (authors, pubDate, title, othercitationdetails) "
				+" values(?,?,?,?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				// Bind the values to the query and execute it
  			pstmt.setString(1, authors);
				pstmt.setString(2, pubDate);
				pstmt.setString(3, title);
				pstmt.setString(4, otherCitationDetails);
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
	  * method that returns the party id based on an org name 
		*/
		private int getCommunityPartyId(String partyName)
	  {
		 	int commPartyId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commparty_id from COMMPARTY where organizationName"
				+" like '"+partyName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					commPartyId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commPartyId);
	 }
	 
	 
	  /** 
	  * method that returns the concept id based on an org name 
		*/
		private int getCommunityConceptId(String conceptDescription)
	  {
		 	int commConceptId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commconcept_id from COMMCONCEPT where conceptDescription"
				+" like '"+conceptDescription+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					commConceptId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > comm desc: " +  conceptDescription );
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
			 
		 }
		 return(commConceptId);
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
				//System.out.println("VegCommunityLoader > matching reference:  " + cnt  );
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
	  * method that returns true if the party already exists in the 
		* database
		*/
		private boolean communityPartyExists(String partyName)
	  {
			boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT commparty_id from COMMPARTY where organizationName"
			+" like '"+partyName+"'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				cnt++;
			}
			if (cnt > 0)
			{
				//System.out.println("VegCommunityLoader > matching partyId:  " + cnt  );
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
				//System.out.println("VegCommunityLoader > matching NAME:  " + cnt  );
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
				this.otherCitationDetails = source.otherCitationDetails;
				Vector v = source.getCommunityCodes("association");
				//this is a hack to restart the ecoart connections after 100 uses
				int connCnt = 0;
				for (int i =0; i < v.size(); i++)
				//for (int i =0; i < 600; i++)
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
					//this variable should be moved from here
					String allianceTransName = "";
					String level = "association";
					//String dateEntered = "11-FEB-2002";
					String dateEntered = source.getDateEntered(communityCode, level);
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, allianceTransName );
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
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
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
					String allianceTransName = source.getAllianceNameTrans(communityCode);
					String level = "alliance";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "alliance");
					String parentCommunity = null; //this means top level
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, allianceTransName);
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
		private void loadEcoartFormations()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
				Vector v = source.getCommunityCodes("formation");
				for (int i =0; i < v.size(); i++)
				{
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "formation";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "formation");
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, "");
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
		private void loadEcoartSubGroups()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
				Vector v = source.getCommunityCodes("subgroup");
				for (int i =0; i < v.size(); i++)
				{
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "subgroup";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "subgroup");
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, "");
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
		private void loadEcoartGroups()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
				Vector v = source.getCommunityCodes("group");
				for (int i =0; i < v.size(); i++)
				{
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "group";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "group");
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, "");
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
		private void loadEcoartSubClasses()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
				Vector v = source.getCommunityCodes("subclass");
				for (int i =0; i < v.size(); i++)
				{
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "subclass";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "subclass");
					String parentCommunity = source.getParentCode(communityCode);
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, "");
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
		private void loadEcoartClasses()
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = source.otherCitationDetails;
				this.refAuthors =source.refAuthors;
				this.refTitle = source.refTitle;
				this.refPubDate = source.refPubDate;
				this.partyOrgName = source.partyOrgName;
				
				Vector v = source.getCommunityCodes("class");
				for (int i =0; i < v.size(); i++)
				{
					String communityCode = v.elementAt(i).toString();
					String commName = source.getCommunityName(communityCode);
					String level = "class";
					//pass the code and the level to the method below
					String dateEntered = source.getDateEntered(communityCode, "class");
					String parentCommunity = null; //this means top level
					if (commName != null)
					{
						this.insertCommunity(communityCode, level, commName, otherCitationDetails, 
						dateEntered, parentCommunity, "");
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
				
				loader.loadEcoartClasses();
				loader.loadEcoartSubClasses();
				loader.loadEcoartGroups();
				loader.loadEcoartSubGroups();
				loader.loadEcoartFormations();
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
