/*
 *	'$RCSfile: UserDatabaseAccess.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-25 19:29:25 $'
 *	'$Revision: 1.2 $'
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

/**
 * 
 *    Purpose: Work with the user tables 
 * 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 * 		
 *		'$Author: farrell $'
 *     '$Date: 2003-11-25 19:29:25 $'
 *     '$Revision: 1.2 $'
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Telephone;
import org.vegbank.common.model.WebUser;

public class UserDatabaseAccess 
{	
	/**
	 * method that, for an email address, will get the user's priveledge level
	 * @param email -- the user's email address
	 */
	 public int getUserPermissionLevel(String email)
	 {
		 int level = 0;
		 try
		 {
			 WebUser user = this.getUser(email);
			 String buf =  user.getPermissiontype();
			 try
			 {
				 level = Integer.parseInt(buf);
			 }
			 catch (Exception e1) 
			 {
				 System.out.println("Exception parsing permission level: " + e1.getMessage());
			 }
		 }
		 catch (Exception e) 
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		 return(level);
	 }
	
	/**
	 * method to get the password from the database based on an email address
	 * 
	 * @param emailAddress
	 * @deprecated Use getUser and get password from that object
	 */
	public String getPassword(String emailAddress)
	{
		System.out.println("UserDatabaseAccess > looking up the password for user: " + emailAddress);
		String s = null;
		try 
		{
			//get the connections etc
			DBConnection conn = getConnection();
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
			conn.close();
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(s);
	}
	
	/**
	 * method that returns true if the input user alraedy has an entry in the 
	 * certification table
	 * @param  emailAddress -- the email address of the user
	 */
	public boolean userCertificationExists(String emailAddress)
	{
		//System.out.println("UserDatabaseAccess > looking up the user id for user: " + emailAddress);
		int s = 0;
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the connections etc
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			sb.append("select CERTIFICATION_ID from USER_CERTIFICATION ");
			sb.append("where upper(EMAIL_ADDRESS) like '"+emailAddress.toUpperCase()+"'");
			//System.out.println("sql: " + sb.toString() );
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			
			int resultCnt = 0;
			while (rs.next() ) 
			{
				resultCnt++;
     		s = rs.getInt(1);
				System.out.println("result: " + s);
				if ( s > 0 )
				{
					return( true );
				}
				else
				{
					return(false);
				}
    	}
			// if there were zero results returned then return false
			if ( resultCnt == 0) 
			{
				return(false);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString() );
			e.printStackTrace();
			return(false);
		}
		return(true);
	 }
	 
	/**
	 * method that inserts the certification info to the user database
	 * The following parameters can be passed as input <br>
	 * 
	 * @param emailAddress
	 * @param surName 
	 * @param givenName
	 * @param phoneNumber
	 * @param phoneType
	 * @param currentCertLevel
	 * @param cvDoc
	 * @param highestDegree
	 * @param degreeYear
	 * @param degreeInst
	 * @param currentInst
	 * @param currentPos
	 * @param esaPos
	 * @param profExperienceDoc
	 * @param relevantPubs
	 * @param vegSamplingDoc
	 * @param vegAnalysisDoc
	 * @param usnvcExpDoc
	 * @param vegbankExpDoc
	 * @param plotdbDoc
	 * @param nvcExpRegionA
	 * @param nvcExpExpVegA
	 * @param nvcExpFloristicsA
	 * @param nvcExpNVCA
	 * @param esaSponsorNameA
	 * @param esaSponsorEmailA
	 * @param esaSponsorNameB
	 * @param esaSponsorEmailB
	 * @param peerReview
	 * @param additionalStatements
	 *
	 */
	public boolean insertUserCertificationInfo(
		String emailAddress,
		String surName,
		String givenName,
		String phoneNumber,
		String phoneType,
		String currentCertLevel,
		String cvDoc,
		String highestDegree,
		String degreeYear,
		String degreeInst,
		String currentInst,
		String currentPos,
		String esaPos,
		String profExperienceDoc,
		String relevantPubs,
		String vegSamplingDoc,
		String vegAnalysisDoc,
		String usnvcExpDoc,
		String vegbankExpDoc,
		String plotdbDoc,
		String nvcExpRegionA,
		String nvcExpVegA,
		String nvcExpFloristicsA,
		String nvcExpNVCA,
		String esaSponsorNameA,
		String esaSponsorEmailA,
		String esaSponsorNameB,
		String esaSponsorEmailB,
		String peerReview,
		String additionalStatements)
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {	
			 System.out.println("UserDatabaseAccess > inserting user cert info");
			 //get the connections etc
			 DBConnection conn = getConnection();
			 //see if this user has an entry in this table and ifso then update it
			 if ( this.userCertificationExists(emailAddress) == true )
			 {
				 return(false);
			 }
			 else
			 {
				 //get the userId of the user 
				 int userid = this.getUserId( emailAddress );
				 // now make the entry in the download table
				 // this is the postgresql date function
				 String sysdate = "now()";
				 sb.append("INSERT into USER_CERTIFICATION ");
				 sb.append(" (email_address, sur_name, given_name, phone_number, phone_type, current_cert_level,  ");
				 sb.append("  cv_documentation, highest_degree, degree_year, degree_institution, current_institution, ");
				 sb.append(" current_position, esa_certified, prof_experience_doc, relevant_pubs, veg_sampling_doc, ");
				 sb.append(" veg_analysis_doc, usnvc_experience_doc, vegbank_experience_doc, plotdb_experience_doc, ");
				 sb.append(" nvc_exp_region_a, nvc_exp_vegetation_a, nvc_exp_floristics_a, nvc_exp_usnvc_a, ");
				 sb.append(" esa_sponsor_name_a,  esa_sponsor_email_a, ");
				 sb.append("  peer_review, additional_statements ) ");
				 sb.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
				 // create the statement
				 PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				 // Bind the values to the query and execute it
				 pstmt.setString(1, emailAddress);
				 pstmt.setString(2, surName);
				 pstmt.setString(3, givenName);
				 pstmt.setString(4, phoneNumber);
				 pstmt.setString(5, phoneType);
				 pstmt.setString(6, currentCertLevel);
				 pstmt.setString(7, cvDoc);
				 pstmt.setString(8, highestDegree);
				 pstmt.setString(9, degreeYear);
				 pstmt.setString(10, degreeInst);
				 pstmt.setString(11, currentInst);
				 pstmt.setString(12, currentPos);
				 pstmt.setString(13, esaPos);
				 pstmt.setString(14, profExperienceDoc);
				 pstmt.setString(15, relevantPubs);
				 pstmt.setString(16, vegSamplingDoc);
				 pstmt.setString(17, vegAnalysisDoc);
				 pstmt.setString(18, usnvcExpDoc);
				 pstmt.setString(19, vegbankExpDoc);
				 pstmt.setString(20, plotdbDoc);
				 pstmt.setString(21, nvcExpRegionA);
				 pstmt.setString(22, nvcExpVegA);
				 pstmt.setString(23, nvcExpFloristicsA);
				 pstmt.setString(24, nvcExpNVCA);
				 pstmt.setString(25, esaSponsorNameA);
				 pstmt.setString(26, esaSponsorEmailA);
				 pstmt.setString(27, peerReview);
				 pstmt.setString(28, additionalStatements);
				
				 
				 // execute the insert
				pstmt.execute();
				return(true);
			 }
		 }
		 catch (Exception e) 
		 {
			 System.out.println("Exception: " + e.getMessage());
			 System.out.println("sql: " + sb.toString() );
			 e.printStackTrace();
			 return(false);
		 }
	 }
	 
	/**
	 * method to create a new user in the vegetation user 
	 * database.  This is used to create the intial instance 
	 * of a user.   
	 * @param emailAddress
	 * @param passWord
	 * @param givenName
	 * @param surName 
	 * @param remoteAddress
	 * @param inst
	 * @param address
	 * @param city
	 * @param state
	 * @param country
	 * @param phone
	 * @param zip
	 * @return b -- true or false result realted to the success of the user creation
	 */
	public boolean createUser(
		String emailAddress,
		String passWord,
		String givenName,
		String surName,
		String remoteAddress,
		String inst,
		String address,
		String city,
		String state,
		String country,
		String phone,
		String zip,
		Vector errors)
	{
		StringBuffer sb = new StringBuffer();
		boolean success = true;
		try 
		{
			// FIGURE OUT IF THE USER HAS AN ACCOUNT
			int i = this.getUserId(emailAddress);
			//System.out.println("userid: " + i);
			if (i == 0)
			{
				//get the connections etc
				DBConnection conn = getConnection();
				Statement query = conn.createStatement();
				sb.append("INSERT into USER_INFO (EMAIL_ADDRESS, PASSWORD, GIVEN_NAME, SUR_NAME, REMOTE_ADDRESS, TICKET_COUNT, ");
				sb.append("INSTITUTION, ADDRESS, CITY, STATE, COUNTRY, PHONE_NUMBER, ZIP_CODE, permission_type) ");
				sb.append("VALUES ('"+emailAddress+"', '"+passWord+"', '"+givenName+"', '"+surName+"', '"+remoteAddress+"', "+"1");
				sb.append(", '"+inst+"', '"+address+"', '"+city+"', '"+state+"','"+country+"','"+phone+"','"+zip+"',1)" );
				//issue the query
				query.executeUpdate(sb.toString());
				
				// ALSO NEED TO WRITE TO THE PARTY TABLE !!!!
				Party party = new Party();
				Address partyAddress = new Address();
				Telephone telephone = new Telephone();

				partyAddress.setDeliverypoint(address);
				partyAddress.setCity(city);
				partyAddress.setAdministrativearea(state);
				partyAddress.setCountry(country); 
				partyAddress.setPostalcode(zip);
				
				telephone.setPhonenumber(phone);
				telephone.setPhonetype("work");
								
				party.setEmail(emailAddress);
				party.setGivenname(givenName);
				party.setSurname(surName);
				party.setOrganizationname(inst);
				party.addparty_telephone(telephone);
				party.addparty_address( partyAddress );

				// Write to database
				VBModelBeanToDB party2db = new VBModelBeanToDB();
				party2db.insert(party);	
			}
			else
			{
				//User already exits
				errors.add("The username '" + emailAddress + "' is already taken, choose another"
				+ "<br/> If you have already registered using that email address, "
				+ " you can request that the password be sent to your email address by <a href=\"/vegbank/forms/fetch-password.html\">clicking here</a>");
				success = false;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString() );
			e.printStackTrace();
			success = false;
		}
		return(success);
	}
 

 
 /**
  * method that retrives a users id from the database and 
	* returns the value
	* @param email -- the email address of the user
	*/
	public int getUserId(String emailAddress)
	{
		System.out.println("UserDatabaseAccess > looking up the user id for user: " + emailAddress);
		int s = 0;
		try 
		{
			//get the connections etc
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select user_id from USER_INFO ");
			sb.append("where upper(EMAIL_ADDRESS) like '"+emailAddress.toUpperCase()+"' ");
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			while (rs.next()) 
			{
     		s = rs.getInt(1);
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
  * method that determines if the user has downloaded the xxx
	* described in the input parameters before and returns true if 
	* they have
	* @param emailAddress -- the email address
	* @param downloadType -- the type of software requested
	*/
	public boolean userDownloadEntryExists(String emailAddress, String downloadType)
	{
		System.out.println("UserDatabaseAccess > looking up the user id for user: " + emailAddress);
		int s = 0;
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the connections etc
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			sb.append("select user_id from USER_DOWNLOADS ");
			sb.append("where upper(EMAIL_ADDRESS) like '"+emailAddress.toUpperCase()+"' AND ");
			sb.append(" DOWNLOAD_TYPE like '"+downloadType+"'");
			
			//System.out.println("sql: " + sb.toString() );
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			
			
			int resultCnt = 0;
			while (rs.next() ) 
			{
				resultCnt++;
     		s = rs.getInt(1);
				System.out.println("result: " + s);
				if ( s > 0 )
				{
					return( true );
				}
				else
				{
					return(false);
				}
    	}
			// if there were zero results returned then return false
			if ( resultCnt == 0) 
			{
				return(false);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString() );
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	/**
	 * method that updates a users download status for a given xxx
	 * 
	 */
	 public void updateDownloadInfo(String emailAddress, String downloadType)
	 {
		 System.out.println("UserDatabaseAccess > updating download info");
		 StringBuffer sb = new StringBuffer();
		try
		{	
			//get the connections etc
			DBConnection conn = getConnection();
			//get the userId of the user 
			int userid = this.getUserId( emailAddress );
			// now make the entry in the download table
			// this is the postgresql date function
			String sysdate = "now()";
			sb.append("UPDATE USER_DOWNLOADS set download_count = DOWNLOAD_COUNT + 1 ");
			sb.append("WHERE EMAIL_ADDRESS like '"+emailAddress+"'  and download_type like '");
			sb.append(downloadType+"'");
			
			System.out.println("sql: " + sb.toString() );
			
			// create the statement
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			
			// execute the insert
			pstmt.execute();
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString() );
			e.printStackTrace();
		}
	 }
	 
 
 /**
  * method that inserts or updates the download table in the 
	* user database
	*/
	public void insertDownloadInfo(String emailAddress, String downloadType )
	{
		StringBuffer sb = new StringBuffer();
		try
		{	
			System.out.println("UserDatabaseAccess > inserting download info");
			//get the connections etc
			DBConnection conn = getConnection();
			// update the ticket count
			this.updateTicketCount(emailAddress);
			
			//see if this user has an entry in this table and ifso then update it
			if ( this.userDownloadEntryExists(emailAddress,  downloadType) == true )
			{
				this.updateDownloadInfo(emailAddress, downloadType);
			}
			
			else
			{
				//get the userId of the user 
				int userid = this.getUserId( emailAddress );
				// now make the entry in the download table
				// this is the postgresql date function
				String sysdate = "now()";
				sb.append("INSERT into USER_DOWNLOADS (user_id, download_type, last_download, ");
				sb.append(" download_count, email_address ) ");
				sb.append(" values(?,?,?,?,?) ");
			
				// create the statement
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				// Bind the values to the query and execute it
				pstmt.setInt(1, userid);
				pstmt.setString(2, downloadType);
				pstmt.setString(3, sysdate);
				pstmt.setInt(4, 1);
				pstmt.setString(5, emailAddress);
			
				// execute the insert
				pstmt.execute();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			System.out.println("sql: " + sb.toString() );
			e.printStackTrace();
		}
		
	}
 
	public void updatePassword(String password, String emailAddress)
	{
		try
		{
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			query.execute("UPDATE USER_INFO set PASSWORD =  '" 
				+ password + "' WHERE EMAIL_ADDRESS = '"+emailAddress+"' ");	
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println("Exception: " + e.getMessage());
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
			DBConnection conn = getConnection();
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
			System.out.println("Exception: " + e.getMessage());
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
			DBConnection conn = getConnection();
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
	 * utility method to provide this class a connection to the 
	 * database.
	 */
	private DBConnection getConnection() throws SQLException
	{
		DBConnectionPool dbCP = DBConnectionPool.getInstance();
		return dbCP.getDBConnection("Need Connection for User database requests");
	}
	
	/**
	 * method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 * 
	 * @param user -- the email address of the user
	 * @return WebUser or null if none found
	 */
	 public WebUser getUser(String emailAddress ) throws Exception
	 {
		 Hashtable h = new Hashtable();
		 WebUser userBean = new WebUser();
		 
		LogUtility.log(
			"UserDatabaseAccess: Searching for user with email of: "
				+ emailAddress);

		 //get the connections etc
		 DBConnection conn = getConnection();
		 Statement query = conn.createStatement ();
		 ResultSet results= null;
		 StringBuffer sb = new StringBuffer();
			sb.append("SELECT email_address, password, surname, givenname, ");
			sb.append(" permission_type, organizationname, ticket_count, deliverypoint, ");
			sb.append(" city, administrativearea, country, postalcode, phonenumber, usr_id, party_id");
			sb.append(" FROM usr NATURAL JOIN (party LEFT JOIN  ");
			sb.append(" ( telephone NATURAL JOIN address ) USING (party_id) )");
			sb.append(" WHERE ( currentflag = true  OR currentflag IS NULL ) AND ");
			sb.append(" ( phonetype = '" + Telephone.PHONETYPE_WORK + "' OR phonetype IS NULL )   AND EMAIL_ADDRESS = '"+emailAddress+"'");
	
		LogUtility.log("UserDatabaseAccess: SQL > " + sb.toString() );
		
		//issue the query
		results = query.executeQuery(sb.toString());
		
		//get the results -- assumming 0 or 1 rows returned
		if (results.next()) 
		{
			String DBEmailAddress = results.getString(1); 
			String DBPassWord = results.getString(2);
			String surName = results.getString(3);
			String givenName = results.getString(4);
			String permissionType = results.getString(5);
			String institution = results.getString(6);
			String ticketCount = results.getString(7);
			
			String address = results.getString(8);
			String city = results.getString(9);
			String state = results.getString(10);
			String country = results.getString(11);
			String zipCode = results.getString(12);
			String dayPhone = results.getString(13);
			String userId = new Integer( results.getInt(14) ).toString();
			String partyId = new Integer( results.getInt(15) ).toString();
						 
			h.put("email", ""+DBEmailAddress);
			h.put("password", ""+DBPassWord);
			h.put("surname", ""+surName);
			h.put("givenname", ""+givenName);
			h.put("permissiontype", ""+permissionType);
			h.put("institution", ""+institution); 
			h.put("ticketcount", ""+ticketCount); 
			h.put("address", ""+address); 
			h.put("city", ""+city); 
			h.put("state", ""+state); 
			h.put("country", ""+country); 
			h.put("zipcode", ""+zipCode);
			h.put("dayphone", ""+dayPhone);
			h.put("userid",  userId);
			h.put("partyid",  partyId);		
			
			//System.out.println(">>>> " + userId + "  " + dayPhone);
		}
		else
		{
			return null;	
		}
		
		conn.close();
		
		Iterator it = h.keySet().iterator();
		while ( it.hasNext() )
		{
			String name = (String) it.next();
			String value = (String) h.get(name);
			//System.out.println("DBObservationReader > name: '" + name + "' value: '" + value + "'" );
					
			// Populate Object with value
			String property = name;
			BeanUtils.copyProperty(userBean, property, value);
			//System.out.println(BeanUtils.getSimpleProperty(object, property) );
		}
		
		return userBean;
	 }
	 
	/**
	 * method that updates the user info database.  The input to 
	 * this method is a hashatble with the following parameters: <br> <br>
	 *
	 * "emailAddress"
	 * "password"
	 * "surName"
	 * "givenName"
	 * "permissionType"
	 * "institution"
	 * "ticketCount"
	 * "address"
	 * "city"
	 * "state"
	 * "country"
	 * "zipCode"
	 * "dayPhone"
	 *
	 */
	public boolean updateUserInfo(Hashtable h)
	{
		try
		{
			System.out.println("Attempting to update Profile");

			String surName = (String) h.get("surName");
			String givenName = (String) h.get("givenName");
			String emailAddress = (String) h.get("emailAddress");
			String institution = (String) h.get("institution");
			String address = (String) h.get("address");
			String city = (String) h.get("city");
			String state = (String) h.get("state");
			String country = (String) h.get("country");
			String phoneNumber = (String) h.get("phoneNumber");
			String zipCode = (String) h.get("zipCode");
			String userId = (String) h.get("userId");
			String partyId = (String) h.get("partyId");
						
			if ((!emailAddress.trim().equals("null"))
				&& (emailAddress != null)
				&& (emailAddress.length() > 2))
			{
				StringBuffer sb = new StringBuffer();
				//get the connections etc
				DBConnection conn = getConnection();
				conn.setAutoCommit(false);

				//int userid = this.getUserId( emailAddress );
				sb.append("UPDATE USER_INFO set SUR_NAME =  '" + surName + "', ");
				sb.append("GIVEN_NAME = '" + givenName + "', ");

				sb.append("INSTITUTION = '" + institution + "', ");
				sb.append("ADDRESS= '" + address + "', ");
				sb.append("CITY= '" + city + "', ");
				sb.append("STATE= '" + state + "', ");
				sb.append("COUNTRY= '" + country + "', ");
				sb.append("ZIP_CODE= '" + zipCode + "', ");
				sb.append("PHONE_NUMBER = '" + phoneNumber + "', ");
				sb.append("EMAIL_ADDRESS = '" + emailAddress + "' ");

				sb.append("WHERE USER_ID = '" + userId + "' ");
				
				System.out.println("sql: " + sb.toString());
				// create the statement
				PreparedStatement pstmt = conn.prepareStatement(sb.toString());
				// execute the insert
				pstmt.execute();

				// Also update the Party and related tables
				DBConnection vbconn = getConnection();
				vbconn.setAutoCommit(false);

				String partyIdSubselect = "party_id = '" +  partyId + "' ";
				String partyUpdateSQL =
					"update party set givenname=?, surname=?, organizationname=?,"
						+ "email=? where "+ partyIdSubselect;
				String telephoneUpdateSQL =
					"update telephone set phonenumber =? where " + partyIdSubselect;
				String addressUpdateSQL =
					"update address set deliverypoint =?, city=?, administrativearea=?, "
						+ " country=?, postalcode=? where " + partyIdSubselect;

				PreparedStatement partyUpdate = vbconn.prepareStatement(partyUpdateSQL);
				PreparedStatement telephoneUpdate = vbconn.prepareStatement(telephoneUpdateSQL);
				PreparedStatement addressUpdate = vbconn.prepareStatement(addressUpdateSQL);
				
				partyUpdate.setString(1, givenName );
				partyUpdate.setString(2, surName );
				partyUpdate.setString(3, institution );
				partyUpdate.setString(4, emailAddress );
				partyUpdate.execute();

				telephoneUpdate.setString(1, phoneNumber );
				telephoneUpdate.execute();
				
				addressUpdate.setString(1, address );
				addressUpdate.setString(2, city );
				addressUpdate.setString(3, state );
				addressUpdate.setString(4, country );
				addressUpdate.setString(5, zipCode );
				addressUpdate.execute();

				conn.commit();
				vbconn.commit();
				
				System.out.println("Updated Profile");
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
			return (false);
		}
		return (true);
	}
	
	public  boolean isEmailUnique(String emailAddress) throws SQLException
	{
		boolean result = true;
		// Get connection, query
		DBConnection conn = this.getConnection();
		Statement query = conn.createStatement();
		
		// Create query
		ResultSet rs = query.executeQuery("SELECT usr_id from usr where email_address = '" + emailAddress + "'");
		
		// If any results returned then email address is not unique
		if (rs.next())
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * main method for testing 
	 */
	public static void main(String[] args) 
	{
		UserDatabaseAccess udb = new UserDatabaseAccess();
		WebUser user = null;
		try
		{
			user = udb.getUser("harris02@hotmail.com");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("UserDatabaseAccess > user info: " + user );
		//h.put("surName", "Harris");
		//h.put("givenName", "John");
		//udb.updateUserInfo(h);
	}

}
