/*
 *	'$RCSfile: UserDatabaseAccess.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-07 06:44:37 $'
 *	'$Revision: 1.6 $'
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
 *		'$Author: anderson $'
 *     '$Date: 2004-02-07 06:44:37 $'
 *     '$Revision: 1.6 $'
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
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.UserDatabaseAccess;
import org.vegbank.ui.struts.CertificationForm;

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
			 level = user.getPermissiontype();
		 }
		 catch (Exception e) 
		 {
			LogUtility.log("Exception: " + e.getMessage());
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
		LogUtility.log("UserDatabaseAccess > looking up the password for user: " + emailAddress);
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
			LogUtility.log("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(s);
	}
	
	/**
	 * method that returns true if the input user alraedy has an entry in the 
	 * certification table
	 * @param  usrId -- the usr_id of the user
	 */
	public boolean userCertificationExists(int usrId)
	{
		int s = 0;
		StringBuffer sb = new StringBuffer();
		try 
		{
			//get the connections etc
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			sb.append("select CERTIFICATION_ID from USERCERTIFICATION ");
			sb.append("where usr_id=" + usrId);
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			
			int resultCnt = 0;
			while (rs.next() ) 
			{
				s = rs.getInt(1);
				return (s > 0);
			}
		}
		catch (Exception e) 
		{
			LogUtility.log("Exception: " + e.getMessage());
			LogUtility.log("sql: " + sb.toString() );
		}
		return(false);
	 }
	 
	/**
	 * method that inserts the certification info to the user database
	 * 
	 * @param form
	 *
	 */
	public boolean insertUserCertificationInfo(CertificationForm form) 
			throws SQLException {

		StringBuffer sqlInsert = new StringBuffer();
		
		LogUtility.log("UserDatabaseAccess > inserting user cert info");
		DBConnection conn = getConnection();

		sqlInsert.append("INSERT into usercertification ")
			.append(" (current_cert_level, requested_cert_level, ")
			.append(" highest_degree, degree_year, degree_institution, current_org, ")
			.append(" current_pos, esa_member, prof_exp, relevant_pubs, veg_sampling_exp, ")
			.append(" veg_analysis_exp, usnvc_exp, vb_exp, tools_exp, vb_intention,")
			.append(" exp_region_a, exp_region_a_veg, exp_region_a_flor, exp_region_a_nvc, ")
			.append(" exp_region_b, exp_region_b_veg, exp_region_b_flor, exp_region_b_nvc, ")
			.append(" exp_region_c, exp_region_c_veg, exp_region_c_flor, exp_region_c_nvc, ")
			.append(" esa_sponsor_name_a,  esa_sponsor_email_a, ")
			.append(" esa_sponsor_name_b,  esa_sponsor_email_b, ")
			.append(" peer_review, addl_stmt, usr_id) ")
			.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		// create the statement
		PreparedStatement pstmt = conn.prepareStatement( sqlInsert.toString() );
		
		pstmt.setInt(1, form.getCurrentCertLevel());
		pstmt.setInt(2, PermComparison.getRoleConstant(form.getRequestedCert()));
		pstmt.setString(3, form.getHighestDegree());
		pstmt.setString(4, form.getDegreeYear());
		pstmt.setString(5, form.getDegreeInst());
		pstmt.setString(6, form.getCurrentOrg());
		pstmt.setString(7, form.getCurrentPos());
		pstmt.setString(8, form.getEsaMember());
		pstmt.setString(9, form.getProfExp());
		pstmt.setString(10, form.getRelevantPubs());
		pstmt.setString(11, form.getVegSamplingExp());
		pstmt.setString(12, form.getVegAnalysisExp());
		pstmt.setString(13, form.getUsnvcExp());
		pstmt.setString(14, form.getVbExp());
		pstmt.setString(15, form.getToolsExp());
		pstmt.setString(16, form.getVbIntention());
		pstmt.setString(17, form.getExpRegionA());
		pstmt.setString(18, form.getExpRegionAVeg());
		pstmt.setString(19, form.getExpRegionAFlor());
		pstmt.setString(20, form.getExpRegionANVC());
		pstmt.setString(21, form.getExpRegionB());
		pstmt.setString(22, form.getExpRegionBVeg());
		pstmt.setString(23, form.getExpRegionBFlor());
		pstmt.setString(24, form.getExpRegionBNVC());
		pstmt.setString(25, form.getExpRegionC());
		pstmt.setString(26, form.getExpRegionCVeg());
		pstmt.setString(27, form.getExpRegionCFlor());
		pstmt.setString(28, form.getExpRegionCNVC());
		pstmt.setString(29, form.getEsaSponsorNameA());
		pstmt.setString(30, form.getEsaSponsorEmailA());
		pstmt.setString(31, form.getEsaSponsorNameB());
		pstmt.setString(32, form.getEsaSponsorEmailB());
		pstmt.setString(33, form.getPeerReview());
		pstmt.setString(34, form.getAddlStmt());
		pstmt.setLong(35, form.getUsrId());
		
		// execute the insert
		
		LogUtility.log("UserDatabaseAccess: saving cert. app");
		pstmt.execute();
		return(true);
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
			long l = this.getUserId(emailAddress);
			//LogUtility.log("userid: " + l);
			if (l == 0)
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
			LogUtility.log("Exception: " + e.getMessage());
			LogUtility.log("sql: " + sb.toString() );
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
		LogUtility.log("UDA.getUserId(): getting usr_id for: " + emailAddress);
		int usrId = 0;
		try {
			//get the connections etc
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT usr_id FROM usr WHERE LOWER(email_address) LIKE '" +
					emailAddress.toLowerCase()+"' ");
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			if (rs.next()) {
     			usrId = rs.getInt(1);
    		}

		} catch (Exception e) {
			LogUtility.log("UDA.getUserId(): Exception: " + e.getMessage(), e);
		}
		return(usrId);
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
		LogUtility.log("UserDatabaseAccess > looking up the user id for user: " + emailAddress);
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
			
			//LogUtility.log("sql: " + sb.toString() );
			
			//issue the query
			query.executeQuery(sb.toString());
			ResultSet rs = query.getResultSet();
			
			
			int resultCnt = 0;
			while (rs.next() ) 
			{
				resultCnt++;
     		s = rs.getInt(1);
				LogUtility.log("result: " + s);
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
			LogUtility.log("Exception: " + e.getMessage());
			LogUtility.log("sql: " + sb.toString() );
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
		 LogUtility.log("UserDatabaseAccess > updating download info");
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
			
			LogUtility.log("sql: " + sb.toString() );
			
			// create the statement
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			
			// execute the insert
			pstmt.execute();
		}
		catch (Exception e) 
		{
			LogUtility.log("Exception: " + e.getMessage());
			LogUtility.log("sql: " + sb.toString() );
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
			LogUtility.log("UserDatabaseAccess > inserting download info");
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
			LogUtility.log("Exception: " + e.getMessage());
			LogUtility.log("sql: " + sb.toString() );
			e.printStackTrace();
		}
		
	}
 
	public void updatePassword(String password, String emailAddress)
	{
		try
		{
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			query.execute("UPDATE usr set password='" + password + 
					"' WHERE email_address='"+emailAddress+"' ");	
			conn.close();
		}
		catch (SQLException e)
		{
			LogUtility.log("Exception: " + e.getMessage());
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
			
			LogUtility.log("UserDatabaseAccess > Database date: "+localtime);
			
			sb.append("UPDATE USER_INFO set TICKET_COUNT = TICKET_COUNT + 1 ");
			sb.append("WHERE EMAIL_ADDRESS like '"+emailAddress+"' ");
						
			//issue the query
			query.executeUpdate(sb.toString());
		}
		catch (Exception e) 
		{
			LogUtility.log("Exception: " + e.getMessage());
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
					LogUtility.log("UserDatabaseAccess > Retrieved Name: "+emailAddress);
					//validate the email and passwords
					if ( emailAddress.trim().equals(DBEmailAddress) 
						&& passWord.trim().equals(DBPassWord) )
						{
							LogUtility.log("UserDatabaseAccess > Accepted at the db level");
							//update the ticket count
							updateTicketCount( DBEmailAddress );
							return(true);
						}
						else 
						{
							LogUtility.log("UserDatabaseAccess > accepted user but denied access");
							return(false);
						}
				}
				else 
				{
					LogUtility.log("UserDatabaseAccess > User not known in database");
				}
			}
			conn.close();
		}
		catch (Exception e) 
		{
			LogUtility.log("Exception: " 
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
	 * Private method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 * 
	 * @param uniqueClause - part of SQL WHERE that gets user
	 * @return WebUser or null if none found
	 */
	 private WebUser getAllUserData(String uniqueClause) throws Exception
	 {
		 Hashtable h = new Hashtable();
		 WebUser userBean = new WebUser();
		 
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
			sb.append(" ( lower(phonetype) = '" + Telephone.PHONETYPE_WORK.toLowerCase()); 
			sb.append("' OR phonetype IS NULL ) AND " + uniqueClause);
	
		LogUtility.log("UDA.getAllUserData(): " + sb.toString() );
		
		//issue the query
		results = query.executeQuery(sb.toString());
		
		//get the results -- assumming 0 or 1 rows returned
		if (results.next()) 
		{
			h.put("email", ""+results.getString(1));
			h.put("password", ""+results.getString(2));
			h.put("surname", ""+results.getString(3));
			h.put("givenname", ""+results.getString(4));
			h.put("permissiontype", ""+results.getString(5));
			h.put("institution", ""+results.getString(6)); 
			h.put("ticketcount", ""+results.getString(7)); 
			h.put("address", ""+results.getString(8)); 
			h.put("city", ""+results.getString(9)); 
			h.put("state", ""+results.getString(10)); 
			h.put("country", ""+results.getString(11)); 
			h.put("postalcode", ""+results.getString(12));
			h.put("dayphone", ""+results.getString(13));
			h.put("userid",  new Integer( results.getInt(14) ).toString());
			h.put("partyid",  new Integer( results.getInt(15) ).toString());		
			
			LogUtility.log(">>>> UDA: Got user: " + h.toString());
		}
		else
		{
			conn.close();
			return null;	
		}
		
		conn.close();
		
		Iterator it = h.keySet().iterator();
		while ( it.hasNext() )
		{
			String name = (String) it.next();
			String value = (String) h.get(name);
			//LogUtility.log("DBObservationReader > name: '" + name + "' value: '" + value + "'" );
					
			// Populate Object with value
			BeanUtils.copyProperty(userBean, name, value);
			//LogUtility.log(BeanUtils.getSimpleProperty(object, name) );
		}
		
		return userBean;
	 }
	 
	/**
	 * method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 * 
	 * @param usrId -- the usr.usr_id of the user
	 * @return WebUser or null if none found
	 */
	 public WebUser getUser(long usrId) throws Exception
	 {
		LogUtility.log("UserDatabaseAccess.getUser: usr_id= " + usrId);
		return getAllUserData(" usr_id=" + usrId);
	 }
	 
	/**
	 * method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 * 
	 * @param usrId -- the usr.usr_id of the user
	 * @return WebUser or null if none found
	 */
	 public WebUser getUser(Long usrId) throws Exception
	 {
		return getUser(usrId.longValue());
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
		LogUtility.log("UDA.getUser: email_address= " + emailAddress);
		return getAllUserData(" LOWER(email_address)='" + emailAddress.toLowerCase() + "'");
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
	 * "postalcode"
	 * "dayPhone"
	 *
	 */
	public boolean updateUserInfo(Hashtable h)
	{
		try
		{
			LogUtility.log("Attempting to update Profile");

			String surName = (String) h.get("surName");
			String givenName = (String) h.get("givenName");
			String emailAddress = (String) h.get("emailAddress");
			String institution = (String) h.get("institution");
			String address = (String) h.get("address");
			String city = (String) h.get("city");
			String state = (String) h.get("state");
			String country = (String) h.get("country");
			String phoneNumber = (String) h.get("phoneNumber");
			String postalcode = (String) h.get("postalcode");
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
				sb.append("UPDATE usr SET sur_name='" + surName + "', ");
				sb.append("given_name='" + givenName + "', ");
				sb.append("institution='" + institution + "', ");
				sb.append("address='" + address + "', ");
				sb.append("city='" + city + "', ");
				sb.append("state='" + state + "', ");
				sb.append("country='" + country + "', ");
				sb.append("postalcode='" + postalcode + "', ");
				sb.append("phone_number='" + phoneNumber + "', ");
				sb.append("email_address='" + emailAddress + "' ");

				sb.append("WHERE usr_id='" + userId + "' ");
				
				LogUtility.log("sql: " + sb.toString());
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
				addressUpdate.setString(5, postalcode );
				addressUpdate.execute();

				conn.commit();
				vbconn.commit();
				
				LogUtility.log("Updated Profile");
			}
		}
		catch (Exception e)
		{
			LogUtility.log("Exception: " + e.getMessage());
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

		LogUtility.log("UserDatabaseAccess > user info: " + user );
		//h.put("surName", "Harris");
		//h.put("givenName", "John");
		//udb.updateUserInfo(h);
	}

}
