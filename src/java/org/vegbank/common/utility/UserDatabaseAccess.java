/*
 *	'$RCSfile: UserDatabaseAccess.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-28 11:19:11 $'
 *	'$Revision: 1.9 $'
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
 *     '$Date: 2004-02-28 11:19:11 $'
 *     '$Revision: 1.9 $'
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
			DBConnectionPool.returnDBConnection(conn);
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
			throws SQLException {
		int s = 0;
		StringBuffer sb = new StringBuffer();
		DBConnection conn = getConnection();
		//get the connections etc
		Statement query = conn.createStatement();
		sb.append("SELECT certification_id FROM usercertification ");
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
		DBConnectionPool.returnDBConnection(conn);
		return(true);
	 }
	 
 /**
  * method that retrives a users id from the database and 
	* returns the value
	* @param email -- the email address of the user
	*/
	public int getUserId(String emailAddress)
			throws SQLException {
		LogUtility.log("UDA.getUserId(): getting usr_id for: " + emailAddress);
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
			throws SQLException {
		//get the connections etc
		DBConnection conn = getConnection();
		Statement query = conn.createStatement ();
		StringBuffer sb = new StringBuffer();
		
//		java.util.SimpleDateFormat formatter = new java.util.SimpleDateFormat("yy-MM-dd HH:mm:ss");
		java.util.Date localtime = new java.util.Date();
//		String dateString = formatter.format(localtime);
		
		LogUtility.log("UserDatabaseAccess > Database date: "+localtime);
		
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
		LogUtility.log("UDA.getConnection(): getting DB connection");
		DBConnectionPool dbCP = DBConnectionPool.getInstance();
		DBConnection conn = dbCP.getDBConnection("UserDatabaseAccess");

		LogUtility.log("UDA.getConnection(): DB connection tag: " + conn.getTag() );
/*  // This makes sure a connection is established
		int loopCount = 0;
		while (conn.getTag() == null) {
			LogUtility.log("UDA.getConnection(): waiting for free DB conn");
			try { Thread.sleep(1000); } catch (InterruptedException iex) { }
			if (++loopCount > 4) {
				LogUtility.log("UDA.getConnection(): couldn't get DB conn!");
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
			sb.append(" city, administrativearea, country, postalcode, phonenumber, ");
			sb.append(" usr_id, party_id, address_id");
			sb.append(" FROM usr NATURAL JOIN (party LEFT JOIN  ");
			sb.append(" ( telephone NATURAL JOIN address ) USING (party_id) )");
			sb.append(" WHERE ( currentflag = true  OR currentflag IS NULL ) AND ");
			sb.append(" ( lower(phonetype) = '" + Telephone.PHONETYPE_WORK.toLowerCase()); 
			sb.append("' OR phonetype IS NULL ) AND " + uniqueClause);
	
		LogUtility.log("UDA.getAllUserData(): " + sb.toString() );
		
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
			userBean.setDayphone( results.getString(14) );
			userBean.setUserid( results.getInt(15) );
			userBean.setPartyid( results.getInt(16) );		
			userBean.setAddressid( results.getInt(17) );		

		}
		
		conn.close();
		DBConnectionPool.returnDBConnection(conn);
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
	 * Updates the user info database. 
	 *
	 */
	public boolean updateUserInfo(WebUser webuser)
	{
		DBConnection conn = null;
		try
		{
			LogUtility.log("Attempting to update Profile");

			//get the connections etc
			conn = getConnection();
			Statement update = conn.createStatement();
			conn.setAutoCommit(false);

			webuser.setSQLSafe(true);
			
			// usr
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE usr SET preferred_name='" + webuser.getPreferredname() + "', ");
			sb.append("email_address='" + webuser.getEmail() + "' ");
			sb.append("WHERE usr_id='" + webuser.getUserid() + "' ");
			LogUtility.log("UDA.updateUserInfo: usr: " + sb.toString());
			update.executeUpdate(sb.toString());

			// address
			sb = new StringBuffer();
			sb.append("UPDATE address SET ");
			sb.append("deliverypoint='" + webuser.getAddress() + "', ");
			sb.append("city='" + webuser.getCity() + "', ");
			sb.append("administrativearea='" + webuser.getState() + "', ");
			sb.append("country='" + webuser.getCountry() + "', ");
			sb.append("postalcode='" + webuser.getPostalcode() + "' ");
			sb.append("WHERE address_id='" + webuser.getAddressid() + "' ");
			LogUtility.log("UDA.updateUserInfo: address: " + sb.toString());
			update.executeUpdate(sb.toString());
			
			// party
			sb = new StringBuffer();
			sb.append("UPDATE party SET organizationname='" + webuser.getOrganizationname() + "' ");
			sb.append("WHERE party_id='" + webuser.getPartyid() + "' ");
			LogUtility.log("UDA.updateUserInfo: party: " + sb.toString());
			update.executeUpdate(sb.toString());
			
			
			update.close();
			conn.commit();
			
			LogUtility.log("Updated Profile");
		}
		catch (Exception e)
		{
			DBConnectionPool.returnDBConnection(conn);
			LogUtility.log("Exception: " + e.getMessage());
			e.printStackTrace();
			try { if (conn != null)  conn.rollback(); }
			catch (SQLException sex) { LogUtility.log("Exception: " + e.getMessage()); }
			
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
