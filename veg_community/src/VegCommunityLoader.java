/**
 *  '$RCSfile: VegCommunityLoader.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-02-13 01:03:08 $'  
 *	'$Revision: 1.25 $'
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import org.vegbank.common.utility.Utility;

	
	
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
		private static String host = "localhost";


		
		private static String[] levelNames = {  "class",  "subclass", "group",  "subgroup",  "formation", "alliance", "association" };
		//private static String[] levelNames = {  "association" };

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
			//c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/communities_dev", "datauser", "");
			c = DriverManager.getConnection("jdbc:postgresql://"+host+"/communities_dev", "datauser", "");
			//c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/communities_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	/**
	 * method that inserts a community -- this method is primarily targeted
	 * for use with the ecoart data there is another method that will new data
	 * (ie, from an entry on the website into the database), and this 
	 * method should really be called load ecoart (or nvc) community
	 */
	 private void insertCommunity(	String conceptCode, 
											 																	String commLevel, 
											 																	String commName, 
											 																	String otherCitationDetails, 
											 																	String dateEntered, 
											 																	String parentCommunity, 
											 																	String allianceTransName,
											 																	String communityDescription
	 																										)
	 {
		 try
		 {
			//turn off autocommit
			conn.setAutoCommit(false);
			int partyId = insertCommunityParty(this.partyOrgName);
			int refId = insertCommunityReference( 	this.refAuthors, 
																																				this.refTitle, 
																																				this.refPubDate, 
																																				this.otherCitationDetails
																																			);
			
			// INSERT THE NAMES
			int commNameId = insertCommunityName(commName, refId, dateEntered);
			int commCodeId = insertCommunityName(conceptCode, refId, dateEntered);
			
			//load the alliance trans if this is an alliance that trans name is
			//basically a common name used to describe the association 
			int commTransId = 0;
			if (commLevel.equals("alliance") &&  allianceTransName != null )
				commTransId = insertCommunityName(allianceTransName, refId, dateEntered);
			
			
			// INSERT THE CONCEPT
			int commConceptId = insertCommunityConcept(	conceptCode, 
																																												commCodeId,  
																																												communityDescription, 
																																												refId );

			// THE STATUS -- there may be a proble with the date entered
			int commStatusId = insertCommunityStatus(	commConceptId, 
																																									"accepted", 
																																									dateEntered, 
																																									partyId, 
																																									refId,
																																									commLevel, 
																																									parentCommunity );
			
			
			//UPDATE THE USAGE FOR THE NAMES 
			
			// - name = Code
			insertCommunityUsage( 	commCodeId, 
																								commConceptId, 
																								partyId, 
																								"standard", 
																								dateEntered, 
																								"Code", 
																								conceptCode, 
																								conceptCode
																							);
			// - name = Scientific 
			insertCommunityUsage( 	commNameId, 
																								commConceptId, 
																								partyId, 
																								"standard",
																								dateEntered, 
																								"Scientific", 
																								commName, 
																								conceptCode
																							);
			// name = Scientific translated (alliance only)
			if ( commLevel.equals("alliance") &&  allianceTransName != null )
			{
				insertCommunityUsage( 	commTransId, 
																									commConceptId, 
																									partyId, 
																									"standard", 
																									dateEntered, 
																									"Scientific translated", 
																									allianceTransName,
																									conceptCode
																								);
			}
			
			
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
	 * HINT: METHOD TO INSERT THE COMMUNITY NAME AND CONCEPT FROM WEB SITE
	 *
	 * method that inserts a community -- this is the method that is for loading
	 * generic communities into the database ie, those enetered through the 
	 * web site.  It is imaginned that the form on the website is to have 
	 * a name for the plant, a description, level, code, and the name of the 
	 * person / party that recognises this community.  All the other attributes 
	 * either have to be simulated or looked up in the database.  This particular
	 * method is used in the case where the plant name does not exist and for that 
	 * reason the reference to both the name and the concept will be equal to vegbank
	 *
	 * @param conceptCode -- the code associated with a new concept
	 *
	 *
	 *
	 */
	 public StringBuffer insertGenericCommunity(	String partySalutation, 
																	                                              String partyGivenName, 
																	                                              String partySurName, 
																	                                              String partyMiddleName, 
																	                                              String orgName,
																		                                            String contactInstructions, 
																		                                            String conceptReferenceTitle, 
																	                                              String conceptReferenceAuthor,
																		                                            String conceptReferenceDate,
																		                                            String nameReferenceTitle, 
																	                                              String nameReferenceAuthor,
																		                                            String nameReferenceDate,
																		                                            String conceptCode, 
																	                                              String commLevel, 
																		                                            String commName, 
																	                                              String dateEntered, 
																	                                              String parentCommunity, 
																		                                            String otherName  )
	 {
	 	StringBuffer sb = new StringBuffer();
    System.out.println( "VegCommunityLoader > conceptCode = '" + conceptCode + 
                        "' commLevel = '" + commLevel+ 
                        "' commName= '" + commName+
                        "' dateEntered= '" + dateEntered+
                        "' parentCommunity= '" + parentCommunity+
                        "' nameReferenceTitle = '" + nameReferenceTitle +
                        "' partyName= '" + partySalutation + partyGivenName + partyMiddleName + partySurName + "'");
 
		 try
		 {
		 	
			//turn off autocommit
			conn.setAutoCommit(false);
			int partyId = insertCommunityPartyIndividual(partySalutation, 
			partyGivenName, partySurName, partyMiddleName, orgName
			, contactInstructions
			);
			System.out.println("VegCommunityLoader > party id: " + partyId);
			if (partyId == 0) this.commit = false;
			
			//GET THE NAME AND CONCEPT REFERENCE
			int conceptRefId = insertCommunityReference(conceptReferenceAuthor, 
			conceptReferenceTitle, conceptReferenceDate,  conceptReferenceAuthor);
			System.out.println("VegCommunityLoader >  concept ref id: " + conceptRefId);
			
			int nameRefId = insertCommunityReference(nameReferenceAuthor, 
			nameReferenceTitle, nameReferenceDate,  nameReferenceAuthor);
			System.out.println("VegCommunityLoader > name ref id: " + nameRefId);
			if (conceptRefId == 0 || nameRefId == 0  ) this.commit = false;
			
			// INSERT THE NAMES
			int commNameId = insertCommunityName(commName, nameRefId, dateEntered);
			System.out.println("VegCommunityLoader > name id: " + commNameId);
			if (commNameId == 0  ) this.commit = false;

			//FORGET THE CODE NAMES FOR NOW
			
			// INSERT THE CONCEPT
			int commConceptId = insertCommunityConcept(	conceptCode, 
																																												commNameId, 
																																												commName, 
																																												conceptRefId 
																																											);
			System.out.println("VegCommunityLoader > concept id: " + commConceptId);
			if (commConceptId == 0  ) this.commit = false;
			
			// THE STATUS -- there may be a proble with the date entered
			int commStatusId = insertCommunityStatus(	commConceptId, 
																																									"accepted", 
																																									dateEntered, 
																																									partyId, 
																																									conceptRefId, 
																																									commLevel,
																																									parentCommunity 
																								);
																								
			System.out.println("VegCommunityLoader > status id: " + commStatusId);
			if (commStatusId == 0  ) this.commit = false;
						
			//UPDATE THE USAGE FOR THE NAMES 
			// - the name
			insertCommunityUsage( commNameId, commConceptId, partyId, "standard"
			, dateEntered, "VEGBANK", commName, conceptCode);
			System.out.println("VegCommunityLoader > usage id: " );
			//if (commStatusId == 0  ) this.commit = false;
			
			// DO THE TRANSACTION -- ROLLBACK ALL FOR TESTING
			if (this.commit == true)
			{
				conn.commit();
				sb.append("<commit>true</commit>");
			}
			else
			{
				conn.rollback();
				sb.append("<commit>false</commit>");
				//debugInstanceFailure( commName, conceptCode, allianceTransName);
				//System.exit(0);
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(sb);
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
		 * method that is sort of out ouf plave because it is used to get a status
		 * id out of the database in order to perform a correlation
		 *
		 * @param refTitle -- the title of the reference, which in the case of the 
		 *  addition of a correlation will refer to that person who entered the
		 *	concept -- the conceptRef
		 
		 */
		public int getStatusId(String refTitle, String refAuthor, 
		String status, String commName, String salutation, String givenName, 
		String surName, String emailAddress, String orgName )
		{
		int statusId = 0; 
		 try
		 {
				//GET THE REFERENCE ID OF THE CONCEPT WHICH IN THE CASE OF A CORRELATION
				//THIS WILL BE THE REFERENCE RELATED TO THE PERSON ENTERING THE NEW
				//CONCEPT
				int refId = getCommunityReferenceId(refAuthor, refTitle);
				System.out.println("VegCommunityLoader > ref id: " + refId );
				
				//GET THE NAME ID -- THERE SHOULD BE ONLY ONE
				int nameId = getCommunityNameId(commName);
				System.out.println("VegCommunityLoader > name id: " + nameId );
				
				String middleName = "";
				String contactInstructions = emailAddress;
				//GET THE PARTY ID FOR THE KEY IN THE STATUS TABLE
				int partyId = getCommunityPartyId(salutation, givenName, 
				surName,  middleName, orgName, contactInstructions);
		 		System.out.println("VegCommunityLoader > party id: " + partyId );
				
				int conceptId = getCommunityConceptId(nameId, refId);
		 		System.out.println("VegCommunityLoader > concept id: " + conceptId );
				
				statusId = getCommunityStatusId(conceptId, partyId, status);
				System.out.println("VegCommunityLoader > status id: " + statusId );
		 }
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(statusId);
		}
		
		/**
		 * public method to update the correlation table.  This is intended to be 
		 * used by the servlet to update a correlation between a new concept status
		 * defiend by a vegbank user and a usage that is generally supported by the 
		 * NVC
		 */
		 public boolean insertCommunityCorrelation(int statusId, int conceptId, 
		 String convergence, String startDate, String stopDate)
		 {
				boolean result = true; 
		 		try
		 		{
					StringBuffer sb = new StringBuffer();
					//insert the strata values
					sb.append("INSERT into commcorrelation ( ");
					sb.append(" commstatus_id, commconcept_id, commconvergence, ");
					sb.append(" correlationStart, correlationStop )");
					sb.append(" values(?,?,?,?,?)" );
					PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
					// Bind the values to the query and execute it
  				pstmt.setInt(1, statusId);
					pstmt.setInt(2, conceptId);
					pstmt.setString(3, convergence);
					Utility.insertDateField(startDate, pstmt, 4); // pstmt.setString(4, startDate);
					Utility.insertDateField(stopDate, pstmt, 5); //pstmt.setString(5, stopDate);
					//execute the p statement
  				pstmt.execute();
		 	}
			catch (Exception e)
		 	{
				 result = false;
				 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
				 e.printStackTrace();
		 	}
		 	return(result);	 	
		}
		
		/**
		 * method that returns the status id
		 */
		 private int getCommunityStatusId(int conceptId, int partyId,  String status)
	  	{
		 	int statusId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commstatus_id from COMMSTATUS where ");
				sb.append(" commconcept_id = "+conceptId);
				sb.append(" and commparty_id = "+partyId);
				sb.append(" and upper(commconceptstatus)  =  '"+status.toUpperCase()+"'");
				
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				
				int cnt = 0;
				while ( rs.next() ) 
				{
					statusId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(statusId);
	 }
		
		
		
	   /**
	  * method to insert data into the commname table
		*/
		private int insertCommunityUsage(	int commNameId, 
																																int commConceptId, 
																																int commPartyId, 
																																String commNameStatus, 
																																String usageStart, 
																																String classSystem, 
																																String commName, 
																																String commCode
																															)
	  {
		 int usageId = 0; 
		 try
		 {
			// Get Primary key for table		
			usageId =  
				(int) Utility.dbAdapter.getNextUniqueID(conn, "COMMUSAGE", "commusage_id");
		 	
				PreparedStatement pstmt = conn.prepareStatement(
					"INSERT into COMMUSAGE( commname_id, commconcept_id, commparty_id, commNameStatus,"
					+"  usageStart, classSystem, commName, ceglcode, commusage_id) "
					+" values(?,?,?,?,?,?, ?, ?,?)");
					
  			// Bind the values to the query and execute it
  			pstmt.setInt(1, commNameId);
				pstmt.setInt(2, commConceptId);
				pstmt.setInt(3, commPartyId);
				pstmt.setString(4, commNameStatus);
				Utility.insertDateField(usageStart, pstmt, 5); //pstmt.setString(5, usageStart);
				pstmt.setString(6, classSystem);
				pstmt.setString(7, commName);
				pstmt.setString(8, commCode);
				pstmt.setInt(9, usageId);
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
		 * Inserts values into the commstatus table of the database
		 * 
		 * @param commConceptId
		 * @param status
		 * @param startDate
		 * @param partyId
		 * @param refId
		 * @param commLevel
		 * @param parentCommunity
		 * @return int
		 */
		private int insertCommunityStatus(	int commConceptId, 
																																String status,  
																																String startDate, 
																																int partyId, 
																																int refId, 
																																String commLevel,
																																String parentCommunity)
	  {
		 int statusId = 0; 
		 try
		 {
				
			// Get Primary key for table		
			statusId =  
				(int) Utility.dbAdapter.getNextUniqueID(conn, "COMMSTATUS", "commstatus_id");
		 	
				PreparedStatement pstmt = conn.prepareStatement(
					"INSERT into COMMSTATUS( commconcept_id, commreference_id, "
					+" commconceptstatus, startDate, commparty_id, commlevel, commstatus_id) "
					+" values(?,?,?,?,?,?,?)");

  			// Bind the values to the query and execute it
  			pstmt.setInt(1, commConceptId);
				pstmt.setInt(2, refId);
				pstmt.setString(3, status);
				Utility.insertDateField(startDate, pstmt, 4); //pstmt.setString(4, startDate);
				pstmt.setInt(5, partyId);
				pstmt.setString(6, commLevel);
				pstmt.setInt(7, statusId);
				//execute the p statement
  			pstmt.executeUpdate();
  			pstmt.close();
				
				//update the parentCommunity
				if (parentCommunity != null)
				{
					//System.out.println("VegCommunityLoader > updating parentCommunity" );
					PreparedStatement pstmt2 = conn.prepareStatement(
						"UPDATE commstatus set commparent = "
						+ "(select commconcept_id from commconcept where ceglcode = '" + parentCommunity +"')"
						+ " where commconcept_id = "+commConceptId+""
					);
					pstmt2.execute();
					//pstmt2.close();
				}
				
      }
		  catch (Exception e)
		  {
			  System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(statusId);
	 }
	 
	 
	 
	 
	  /**
	  * method to insert data into the commname table where the only data taken as
		* input is the organizationName, so this is generally to be used when
		* entering an organization rather than a person
		*
		* @param partyOrgName
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
	  * this is the method that is to be used when entering a party that is
		* specific to a given individual.  When a web form is used to fill out 
		* information related to a new community the party will be the person
		* filling out the form and this is the method to be used 
		* @see insertCommunityParty
		*
		* @param partyOrgName
		*/
		private int insertCommunityPartyIndividual(String partySalutation, 
		String partyGivenName, String partySurName, String partyMiddleName, 
		String orgName, String contactInstructions)
	  {
		 int partyId = 0; 
		 try
		 {
			 boolean partyExists = communityPartyExists( partySalutation, 
				partyGivenName, partySurName, partyMiddleName, 
				orgName, contactInstructions);
			
			if (partyExists == true)
			{
				System.out.println("VegCommunityLoader > party exists" );
				partyId = getCommunityPartyId(partySalutation, partyGivenName, 
				partySurName, partyMiddleName, orgName, contactInstructions);
			}
			
			else
			{
				StringBuffer sb = new StringBuffer();
				//insert the VALS
				sb.append("INSERT into COMMPARTY ( salutation, givenName, surName, ");
				sb.append(" middleName, organizationName, contactInstructions ) ");
				sb.append(" values(?,?,?,?,?,?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
  			// Bind the values to the query and execute it
  			pstmt.setString(1, partySalutation );
				pstmt.setString(2, partyGivenName);
				pstmt.setString(3, partySurName);
				pstmt.setString(4, partyMiddleName);
				pstmt.setString(5, partyOrgName);
				pstmt.setString(6, contactInstructions);
				
				//execute the p statement
  			pstmt.execute();
  			// pstmt.close();
				//get the refId
				sb = new StringBuffer();
				sb.append("SELECT commparty_id from COMMPARTY where givenName "
				+" like '"+partyGivenName+"' and surName like'"+partySurName+"'");
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
				+" = '"+commName+"'");
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
		private int insertCommunityConcept(	String commConceptCode, 
																																		int commNameId, 
																																		String conceptDescription, 
																																		int refId )
	  {
		 int commConceptId = 0; 
		 try
		 {
		 	// Get Primary key for table		
			commConceptId =  
				(int) Utility.dbAdapter.getNextUniqueID(conn, "COMMCONCEPT", "commconcept_id");
		 	
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT into COMMCONCEPT (commname_id, ceglcode, "
						+" conceptDescription, commreference_id, commconcept_id) "
						+" values(?, ?, ?, ?, ?)");

	  		// Bind the values to the query and execute it
	  		pstmt.setInt(1, commNameId);
				pstmt.setString(2, commConceptCode);
				pstmt.setString(3, conceptDescription);
				pstmt.setInt(4, refId);
				pstmt.setInt(5, commConceptId);			
				
				//execute the insert statement getting the "row count in return"
				pstmt.executeUpdate();     

			
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
		 * Method getCommunityReferenceId.
		 * 
		 * Finds the primaryKey for a reference.
		 * 
		 * @param otherCitationDetails - used to lookup the PK
		 * 
		 * @return int	- The primary key of the reference
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
	  * method that overloads the above method to get a reference ID
		* uses the authors and a title to get the refId
		*/
		private int getCommunityReferenceId(String authors, String title)
	  {
		 	int refId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commreference_id from COMMREFERENCE where upper(authors)"
				+" like '"+authors.toUpperCase()+"' and upper(title) like '%"+title.toUpperCase()+"%'");
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
				+" = '"+commName+"'");
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
				+" = '"+partyName+"'");
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
	  * overloaded method of above, this method is intended for use with 
		* parties consisteng of individuals
		* 
		* partySalutation, partyGivenName, 
		*		partySurName, partyMiddleName, orgName, contactInstructions
		*/
		private int getCommunityPartyId(String salutation, String givenName, 
				String surName, String middleName, String orgName, 
				String contactInstructions)
	  {
		 	int commPartyId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();

				sb.append("SELECT commparty_id from COMMPARTY where salutation "
				+" like '"+salutation+"' and givenName like '"+givenName+"' and surname like'"
				+surName+"'");	
				
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
				sb.append("SELECT commconcept_id from COMMCONCEPT where conceptDescription = '"  
													+ conceptDescription + "'");
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
	  * method to get the concept id
		*
		*/
		private int getCommunityConceptId(int commNameId, int commReferenceId)
	  {
		 	int commConceptId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT commconcept_id from COMMCONCEPT where commName_Id ="
				+commNameId+" and commReference_id = "+commReferenceId);
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
			 //System.out.println("VegCommunityLoader > comm desc: " +  conceptDescription );
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
				//System.out.println("VegCommunityLoader > matching reference: " + otherCitationDetails  );
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
	 
	 /**
	  * overloaded method of above but intended for use with an individual 
		* reather than an organization
		* @see communityPartyExists
		* @param  partySalutation, 
		*	@param partyGivenName
		*	@param  partySurName, 
		*	@param  partyMiddleName, 
		*	@param orgName, 
		*	@param contactInstructions)
		*/
		private boolean communityPartyExists( String salutation, String givenName, 
		String surName, String middleName, String orgName, String contactInstructions)
	  {
			boolean exists = false;
		 	try
		 	{
		 		StringBuffer sb = new StringBuffer();
				//get the refId
				sb = new StringBuffer();
				sb.append("SELECT commparty_id from COMMPARTY where salutation "
				+" like '"+salutation+"' and givenName like '"+givenName+"' and surname like'"
				+surName+"'");
			
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
	  *  Load  the ecoart alliances
	  * 
	  * 	@param String levelName -  The level to load this time
		*/
		private void loadEcoartLevel ( String levelName )
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//update the class varaibles
				this.otherCitationDetails = Utility.escapeCharacters( source.otherCitationDetails );
				this.refAuthors = Utility.escapeCharacters( source.refAuthors );
				this.refTitle = Utility.escapeCharacters( source.refTitle );
				this.refPubDate = Utility.escapeCharacters( source.refPubDate );
				this.partyOrgName = Utility.escapeCharacters( source.partyOrgName );
				
				Vector v = source.getCommunityCodes( levelName );

				//	this is a hack to restart the ecoart connections after 100 uses
				int connCnt = 0;	
				
				for (int i =0; i < v.size(); i++)
        //for (int i =0; i < 200; i++)  // hack tor testing
				{

					connCnt++;
					if (connCnt == 100)
					{
						//close the connection or run out of memory soon
						source.con.close();
						source = new EcoartVegCommunitySource();
						connCnt = 0;
					}							
					
					String communityCode =  Utility.escapeCharacters( v.elementAt(i).toString() );
					String commName = 
						Utility.escapeCharacters( source.getCommunityName(communityCode) );
					String dateEntered = source.getDateEntered(communityCode, levelName);
					String communityDescription = 
						Utility.escapeCharacters(source.getCommunityDescription(communityCode) );					
					String parentCommunity = source.getParentCode(communityCode);
					String allianceTransName = 
							Utility.escapeCharacters(source.getAllianceNameTrans(communityCode) );
					
					
					if (commName != null)
					{
						this.insertCommunity(	communityCode, 
																									levelName, 
																									commName, 
																									otherCitationDetails, 
																									dateEntered, 
																									parentCommunity, 
																								  allianceTransName,
																									communityDescription
																								);
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
				if (args.length == 2) 
				{
					host = args[1];
				  VegCommunityLoader loader = new VegCommunityLoader();
					String dataset = args[0];
					if ( dataset.trim().equals("ecoart") )
					{	
						for ( int i=0; i < levelNames.length; i++ )
						{
							System.out.println("\n#########################");
							System.out.println("VegCommunityLoader > loading: " +  levelNames[i]);
							System.out.println("#########################\n");
							loader.loadEcoartLevel( levelNames[i] );
						}
					}
					else if ( dataset.trim().equals("test") )
					{
						System.out.println("VegCommunityLoader > loading test data ");
						System.out.println("VegCommunityLoader > host: " + host);					
						String conceptCode = "";
						String conceptLevel = "level1";
						String commName = "Grassy Coastal Terrace";
						String dateEntered = "03-2002-08";
						String parentCommunity = "";
						String otherName = "";
						String partyName = "John Harris";
						//laod the community
						//loader.insertGenericCommunity(conceptCode, conceptLevel, commName, 
					 	//													dateEntered, parentCommunity, otherName,
						//													partyName );
					}
					else
					{
						System.out.println(" usage  > inputs {ecoart | test} hostname"); 
					}
					
				}
				else
				{
					System.out.println(" usage  > inputs {ecoart | test} hostname"); 
				}
				
				
			}
			catch (Exception e)
		 {
			 System.out.println("VegCommunityLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	 
 
}
