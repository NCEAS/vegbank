/*
 *	'$RCSfile: UserDatabaseAccess.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-15 02:03:31 $'
 *	'$Revision: 1.13 $'
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
 *     '$Date: 2004-04-15 02:03:31 $'
 *     '$Revision: 1.13 $'
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
import org.vegbank.common.utility.Utility;
import org.vegbank.ui.struts.CertificationForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserDatabaseAccess 
{	
	private static Log log = LogFactory.getLog(UserDatabaseAccess.class);


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
			log.error("Exception: " + e.getMessage());
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
		log.debug("UserDatabaseAccess > looking up the password for user: " + emailAddress);
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
			DBConnectionPool.returnDBConnection(conn);
		}
		catch (Exception e) 
		{
			log.error("Exception: " + e.getMessage());
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
			throws SQLException {
		int s = 0;
		StringBuffer sb = new StringBuffer();
		DBConnection conn = getConnection();
		//get the connections etc
		Statement query = conn.createStatement();
		sb.append("SELECT usercertification_id FROM usercertification ");
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

		DBConnectionPool.returnDBConnection(conn);
		return(false);
	 }
	 
	/**
	 * method that inserts the certification info to the user database.
	 * 
	 * @param form
	 * @return new certId
	 *
	 */
	public long insertUserCertificationInfo(CertificationForm form) 
			throws SQLException {

		StringBuffer sqlInsert = new StringBuffer();
		
		log.debug("UserDatabaseAccess > inserting user cert info");
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
		
		log.debug("UserDatabaseAccess: saving cert. app");
		Statement idStmt = conn.createStatement();

		// insert
		pstmt.execute();

		// get new ID
		idStmt.executeQuery("SELECT MAX(usercertification_id) FROM usercertification WHERE usr_id="+form.getUsrId());

		ResultSet rs = idStmt.getResultSet();
		long certId = 0;
		if (rs.next()) {
			certId = rs.getInt(1);
		}

		pstmt.close();
		idStmt.close();

		log.debug("UserDatabaseAccess: created new certification app #" + certId);

		DBConnectionPool.returnDBConnection(conn);
		return certId;
	 }
	 
 /**
  * method that retrives a users id from the database and 
	* returns the value
	* @param email -- the email address of the user
	*/
	public int getUserId(String emailAddress)
			throws SQLException {
		log.debug("UDA.getUserId(): getting usr_id for: " + emailAddress);
		int usrId = 0;
		
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

		DBConnectionPool.returnDBConnection(conn);
		return(usrId);
	}
 
 
	/**
	 * This method updates, umm - what was it?  Hmmm.  
	 * Oh yeah the user's password.
	 * @param password new password
	 * @param emailAddress of the user
	 */
	public void updatePassword(String password, String emailAddress)
	{
		try
		{
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
			query.execute("UPDATE usr set password='" + password + 
					"' WHERE email_address='"+emailAddress+"' ");	
			DBConnectionPool.returnDBConnection(conn);
		}
		catch (SQLException e)
		{
			log.debug("Exception: " + e.getMessage());
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
			throws SQLException {
		//get the connections etc
		DBConnection conn = getConnection();
		Statement query = conn.createStatement ();
		StringBuffer sb = new StringBuffer();
		
//		java.util.SimpleDateFormat formatter = new java.util.SimpleDateFormat("yy-MM-dd HH:mm:ss");
		java.util.Date localtime = new java.util.Date();
//		String dateString = formatter.format(localtime);
		
		log.debug("UserDatabaseAccess > Database date: "+localtime);
		
		sb.append("UPDATE usr SET ticket_count = ticket_count + 1 ");
		sb.append("WHERE email_address LIKE '"+emailAddress+"' ");
					
		//issue the query
		query.executeUpdate(sb.toString());
		DBConnectionPool.returnDBConnection(conn);
	}
 

	/**
	 * utility method to provide this class a connection to the 
	 * database.
	 */
	private DBConnection getConnection() throws SQLException
	{
		DBConnectionPool dbCP = DBConnectionPool.getInstance();
		DBConnection conn = dbCP.getDBConnection("UserDatabaseAccess");

		//log.debug("UDA.getConnection(): DB connection tag: " + conn.getTag() );
/*  // This makes sure a connection is established
		int loopCount = 0;
		while (conn.getTag() == null) {
			log.debug("UDA.getConnection(): waiting for free DB conn");
			try { Thread.sleep(1000); } catch (InterruptedException iex) { }
			if (++loopCount > 4) {
				log.debug("UDA.getConnection(): couldn't get DB conn!");
				return null;
			}
			conn = dbCP.getDBConnection("UserDatabaseAccess");
		}
*/
		return conn;
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
		//get the connections etc
		DBConnection conn = getConnection();
		 
		 Statement query = conn.createStatement();
		 StringBuffer sb = new StringBuffer();
		sb.append("SELECT email_address, password, surname, givenname, preferred_name,");
		sb.append(" permission_type, organizationname, ticket_count, deliverypoint, ");
		sb.append(" city, administrativearea, country, postalcode, usr_id, party_id, address_id");
		sb.append(" FROM usr NATURAL JOIN (party LEFT JOIN address USING (party_id) )");
		sb.append(" WHERE ( currentflag = true  OR currentflag IS NULL ) AND " + uniqueClause);
		 /*
		sb.append("SELECT email_address, password, surname, givenname, preferred_name,");
		sb.append(" permission_type, organizationname, ticket_count, deliverypoint, ");
		sb.append(" city, administrativearea, country, postalcode, phonenumber, ");
		sb.append(" usr_id, party_id, address_id");
		sb.append(" FROM usr NATURAL JOIN (party LEFT JOIN  ");
		sb.append(" ( telephone NATURAL JOIN address ) USING (party_id) )");
		sb.append(" WHERE ( currentflag = true  OR currentflag IS NULL ) AND ");
		sb.append(" ( lower(phonetype) = '" + Telephone.PHONETYPE_WORK.toLowerCase()); 
		sb.append("' OR phonetype IS NULL ) AND " + uniqueClause);
		*/
	
		//log.debug("UDA.getAllUserData(): " + sb.toString() );
		
		 //issue the query
		 ResultSet results = query.executeQuery(sb.toString());
		
		//get the results -- assumming 0 or 1 rows returned
		WebUser userBean = null;
		if (results.next()) 
		{
			// build the WebUser
			userBean = new WebUser();
			userBean.setEmail( results.getString(1) );
			userBean.setPassword( results.getString(2) );
			userBean.setSurname( results.getString(3) );
			userBean.setGivenname( results.getString(4) );
			userBean.setPreferredname( results.getString(5) );
			userBean.setPermissiontype( results.getInt(6) );
			userBean.setOrganizationname( results.getString(7) ); 
			userBean.setTicketcount( results.getInt(8) ); 
			userBean.setAddress( results.getString(9) ); 
			userBean.setCity( results.getString(10) ); 
			userBean.setState( results.getString(11) ); 
			userBean.setCountry( results.getString(12) ); 
			userBean.setPostalcode( results.getString(13) );
			userBean.setUserid( results.getInt(14) );
			userBean.setPartyid( results.getInt(15) );		
			userBean.setAddressid( results.getInt(16) );		

		}
		
		conn.close();
		DBConnectionPool.returnDBConnection(conn);
		return userBean;
	 }
	 

	/**
	 * Updates a certification application's status and comment.
	 * 
	 * @param usercertification_id
	 * @param status
	 * @param comment
	 */
	 public void updateCertificationStatus(int usercertification_id, String status, String comment) 
		 	throws Exception
	 {
		DBConnection conn = getConnection();
		Statement stmt = conn.createStatement();
		StringBuffer sb = new StringBuffer(256)
			.append("UPDATE usercertification SET ")
			.append("certificationstatus='")
			.append(Utility.encodeForDB(status))
			.append("', certificationstatuscomments='")
			.append(Utility.encodeForDB(comment))
			.append("' WHERE usercertification_id='")
			.append(usercertification_id)
			.append("'");

		log.debug("setting cert status: " + sb.toString());
		stmt.executeUpdate(sb.toString());
		conn.close();
		DBConnectionPool.returnDBConnection(conn);
	 }

	/**
	 * Private method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 * 
	 * @param usercertification_id
	 * @return CertificationForm or null if none found
	 */
	 public CertificationForm getCertificationApp(int usercertification_id) throws Exception
	 {
		//get the connections etc
		DBConnection conn = getConnection();
		 
		Statement query = conn.createStatement();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
			.append(" current_cert_level, requested_cert_level, ")
			.append(" highest_degree, degree_year, degree_institution, current_org, ")
			.append(" current_pos, esa_member, prof_exp, relevant_pubs, veg_sampling_exp, ")
			.append(" veg_analysis_exp, usnvc_exp, vb_exp, tools_exp, vb_intention,")
			.append(" exp_region_a, exp_region_a_veg, exp_region_a_flor, exp_region_a_nvc, ")
			.append(" exp_region_b, exp_region_b_veg, exp_region_b_flor, exp_region_b_nvc, ")
			.append(" exp_region_c, exp_region_c_veg, exp_region_c_flor, exp_region_c_nvc, ")
			.append(" esa_sponsor_name_a,  esa_sponsor_email_a, ")
			.append(" esa_sponsor_name_b,  esa_sponsor_email_b, ")
			.append(" peer_review, addl_stmt, usr_id ")
			.append(" FROM usercertification ")
			.append(" WHERE usercertification_id = ").append(usercertification_id);
	
		 //issue the query
		log.debug("UDA.getCert: QUERY: " + sb.toString());
		ResultSet results = query.executeQuery(sb.toString());
		
		//get the results -- assumming 0 or 1 rows returned
		CertificationForm certForm = new CertificationForm();
		if (results.next()) 
		{
			certForm.setCurrentCertLevel( results.getInt(1) );
			certForm.setRequestedCert( results.getString(2) );
			certForm.setHighestDegree( results.getString(3) );
			certForm.setDegreeYear( results.getString(4) );
			certForm.setDegreeInst( results.getString(5) );
			certForm.setCurrentOrg( results.getString(6) );
			certForm.setCurrentPos( results.getString(7) );
			certForm.setEsaMember( results.getString(8) );
			certForm.setProfExp( results.getString(9) );
			certForm.setRelevantPubs( results.getString(10) );
			certForm.setVegSamplingExp( results.getString(11) );
			certForm.setVegAnalysisExp( results.getString(12) );
			certForm.setUsnvcExp( results.getString(13) );
			certForm.setVbExp( results.getString(14) );
			certForm.setToolsExp( results.getString(15) );
			certForm.setVbIntention( results.getString(16) );
			certForm.setExpRegionA( results.getString(17) );
			certForm.setExpRegionAVeg( results.getString(18) );
			certForm.setExpRegionAFlor( results.getString(19) );
			certForm.setExpRegionANVC( results.getString(20) );
			certForm.setExpRegionB( results.getString(21) );
			certForm.setExpRegionBVeg( results.getString(22) );
			certForm.setExpRegionBFlor( results.getString(23) );
			certForm.setExpRegionBNVC( results.getString(24) );
			certForm.setExpRegionC( results.getString(25) );
			certForm.setExpRegionCVeg( results.getString(26) );
			certForm.setExpRegionCFlor( results.getString(27) );
			certForm.setExpRegionCNVC( results.getString(28) );
			certForm.setEsaSponsorNameA( results.getString(29) );
			certForm.setEsaSponsorEmailA( results.getString(30) );
			certForm.setEsaSponsorNameB( results.getString(31) );
			certForm.setEsaSponsorEmailB( results.getString(32) );
			certForm.setPeerReview( results.getString(33) );
			certForm.setAddlStmt( results.getString(34) );
			certForm.setUsrId( results.getInt(35) );


		} else {
			log.debug("UDA.getCert: NOT FOUND # " + usercertification_id);
			return null;
		}

		conn.close();
		DBConnectionPool.returnDBConnection(conn);

		WebUser user = getUser(certForm.getUsrId());
		certForm.setEmailAddress( user.getEmail() );
		certForm.setSurName( user.getSurname() );
		certForm.setGivenName( user.getGivenname() );
		certForm.setPhoneNumber( user.getDayphone() );

		return certForm;
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
		log.debug("UserDatabaseAccess.getUser: usr_id= " + usrId);
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
		log.debug("UDA.getUser: email_address= " + emailAddress);
		return getAllUserData(" LOWER(email_address)='" + emailAddress.toLowerCase() + "'");
	 }
	 
	/**
	 * Updates the user info database. 
	 *
	 */
	public boolean updateUserInfo(WebUser webuser)
	{
		DBConnection conn = null;
		try
		{
			log.debug("Attempting to update Profile");
			StringBuffer sb = new StringBuffer();

			//get the connections etc
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			conn.setAutoCommit(false);
			webuser.setSQLSafe(true);
			

			// check for extant address
			String currentFlagSQL = "";
			boolean doAddressUpdate = true;
			if (webuser.getAddressid() == 0) {
				doAddressUpdate = false;
				// double check the db
				rs = stmt.executeQuery(
						"SELECT address_id from address where party_id = " + 
						webuser.getPartyid() );
				if (rs.next()) {
					// found a record, so there was a problem building the webuser
     				int extantAddressId = rs.getInt(1);
					log.debug("UDA.updateUserInfo: possible problem: address #" +
							+ extantAddressId + " extant in db but not webuser");
					webuser.setAddressid(extantAddressId);
					doAddressUpdate = true;
					currentFlagSQL = ", currentflag=true ";

				} else {
					// let's create a new address record
					sb = new StringBuffer()
						.append("INSERT INTO address (deliverypoint,city,")
						.append("administrativearea,country,postalcode,party_id,currentflag) VALUES (")
						.append("'" + webuser.getAddress() + "', ")
						.append("'" + webuser.getCity() + "', ")
						.append("'" + webuser.getState() + "', ")
						.append("'" + webuser.getCountry() + "', ")
						.append("'" + webuser.getPostalcode() + "', ")
						.append(webuser.getPartyid() + ", ")
						.append("true)");
						log.debug("UDA.updateUserInfo: NEW address: " + sb.toString());
						stmt.executeUpdate(sb.toString());
				}

			} 

			if (doAddressUpdate) {
				// update address as normal
				sb = new StringBuffer();
				sb.append("UPDATE address SET ");
				sb.append("deliverypoint='" + webuser.getAddress() + "', ");
				sb.append("city='" + webuser.getCity() + "', ");
				sb.append("administrativearea='" + webuser.getState() + "', ");
				sb.append("country='" + webuser.getCountry() + "', ");
				sb.append("postalcode='" + webuser.getPostalcode() + "' ");
				sb.append(currentFlagSQL);
				sb.append("WHERE address_id='" + webuser.getAddressid() + "' ");
				log.debug("UDA.updateUserInfo: address: " + sb.toString());
				stmt.executeUpdate(sb.toString());
			}


			// usr
			sb = new StringBuffer();
			sb.append("UPDATE usr SET preferred_name='" + webuser.getPreferredname() + "', ");
			sb.append("email_address='" + webuser.getEmail() + "' ");
			sb.append("WHERE usr_id='" + webuser.getUserid() + "' ");
			log.debug("UDA.updateUserInfo: usr: " + sb.toString());
			stmt.executeUpdate(sb.toString());

			// party
			sb = new StringBuffer();
			sb.append("UPDATE party SET organizationname='" + webuser.getOrganizationname() + "' ");
			sb.append("WHERE party_id='" + webuser.getPartyid() + "' ");
			log.debug("UDA.updateUserInfo: party: " + sb.toString());
			stmt.executeUpdate(sb.toString());
			
			
			stmt.close();
			conn.commit();
			
			log.debug("Updated Profile");
		}
		catch (Exception e)
		{
			DBConnectionPool.returnDBConnection(conn);
			log.error("Exception: " + e.getMessage());
			e.printStackTrace();
			try { if (conn != null)  conn.rollback(); }
			catch (SQLException sex) { log.error("Exception: " + e.getMessage()); }
			
			return false;
		}
		DBConnectionPool.returnDBConnection(conn);
		return true;
	}
	
	public boolean isEmailUnique(String emailAddress) throws SQLException
	{
		boolean result = true;
		// Get connection, query
		DBConnection conn = this.getConnection();
		Statement query = conn.createStatement();
		
		// Create query
		ResultSet rs = query.executeQuery("SELECT usr_id from usr where email_address = '" + emailAddress + "'");
		
		// If any results returned then email address is not unique
		if (rs.next()) {
			result = false;
		}
		
		DBConnectionPool.returnDBConnection(conn);
		return result;
	}

	/**
	 * Set sum of permissions.
	 * 
	 * @param usrId -- the usr.usr_id of the user
	 * @param sum -- the sum of roles
	 */
		public void setUserPermissionSum(long usrId, int sum) throws Exception
		{
			if (usrId == 0) {
				throw new Exception("Can't set permissions: null usr_id");
			}

			DBConnection conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE usr SET permission_type=" + sum + 
					" WHERE usr_id=" + usrId);

			stmt.close();
			DBConnectionPool.returnDBConnection(conn);
		}
	 
	/**
	 * 
	 * @param usrId -- the usr.usr_id of the user
	 * @return sum of permissions
	 */
	 public int getUserPermissionSum(long usrId) throws Exception
	 {
		DBConnection conn = getConnection();
		Statement query = conn.createStatement();
		query.executeQuery("SELECT permission_type FROM usr WHERE usr_id=" + usrId);
		ResultSet rs = query.getResultSet();
		
		int sum = 0;
		if (rs.next() ) {
			sum = rs.getInt(1);
		}

		rs.close();
		query.close();
		DBConnectionPool.returnDBConnection(conn);
		return sum;
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
