/*
 *	'$RCSfile: UserDatabaseAccess.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-09-01 23:50:50 $'
 *	'$Revision: 1.27 $'
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
 *		'$Author: mlee $'
 *     '$Date: 2006-09-01 23:50:50 $'
 *     '$Revision: 1.27 $'
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.security.*;

import org.apache.commons.beanutils.BeanUtils;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Telephone;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.Utility;
import org.vegbank.ui.struts.CertificationForm;
import org.vegbank.ui.struts.UserForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserDatabaseAccess
{
	private static Log log = LogFactory.getLog(UserDatabaseAccess.class);


	/**
	 * method that, for an email address, will get the user's priveledge level
	 * @param email -- the user's email address
	 */
	 public long getUserPermissionLevel(String email)
	 {
		 long level = 0;
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
		 return level;
	 }

	/**
	 * method that returns true if the input user alraedy has an entry in the
	 * certification table
	 * @param  usrId -- the usr_id of the user
	 */
	public boolean userCertificationExists(long usrId)
			throws SQLException {
		long s = 0;
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
		if (rs.next() )
		{
			s = rs.getLong(1);
            rs.close();
            query.close();
			return (s > 0);
		}

		DBConnectionPool.returnDBConnection(conn);
		return(false);
	 }

  /**
   * delete the user with the given usrId
   */
  public void deleteUser(long usrId)
    throws SQLException
  {
    StringBuffer sb = new StringBuffer();
    DBConnection conn = getConnection();
    Statement stmt = conn.createStatement();
    sb.append("delete from usr where usr_id = '"+usrId+"';");
    sb.append("delete from usercertification where usr_id = '"+usrId+"';");
    sb.append("delete from usernotify where usr_id = '"+usrId+"';");
    sb.append("delete from userdataset where usr_id = '"+usrId+"';");
    sb.append("delete from userpermission where usr_id = '"+usrId+"';");
    sb.append("delete from userpreference where usr_id = '"+usrId+"';");
    sb.append("delete from userquery where usr_id = '"+usrId+"';");
    sb.append("delete from userrecordowner where usr_id = '"+usrId+"';");
    stmt.execute(sb.toString());
    stmt.close();
    conn.commit();
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
			.append(" peer_review, addl_stmt, usr_id, certificationstatus) ")
			.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		// log.debug("UserDatabaseAccess > inserting user cert info with " + sqlInsert.toString());

		// create the statement
		PreparedStatement pstmt = conn.prepareStatement( sqlInsert.toString() );

       // pstmt.setEscapeProcessing(true); //SQL safing is not working?

		pstmt.setLong(1, form.getCurrentCertLevel());
		pstmt.setLong(2, form.getRequestedCert());
		pstmt.setString(3, Utility.encodeForDBToTickMark(form.getHighestDegree()));
		pstmt.setString(4, Utility.encodeForDBToTickMark(form.getDegreeYear()));
		pstmt.setString(5, Utility.encodeForDBToTickMark(form.getDegreeInst()));
		pstmt.setString(6, Utility.encodeForDBToTickMark(form.getCurrentOrg()));
		pstmt.setString(7, Utility.encodeForDBToTickMark(form.getCurrentPos()));
		pstmt.setString(8, Utility.encodeForDBToTickMark(form.getEsaMember()));
		pstmt.setString(9, Utility.encodeForDBToTickMark(form.getProfExp()));
		pstmt.setString(10,Utility.encodeForDBToTickMark(form.getRelevantPubs()));
		pstmt.setString(11, Utility.encodeForDBToTickMark(form.getVegSamplingExp()));
		pstmt.setString(12, Utility.encodeForDBToTickMark(form.getVegAnalysisExp()));
		pstmt.setString(13, Utility.encodeForDBToTickMark(form.getUsnvcExp()));
		pstmt.setString(14, Utility.encodeForDBToTickMark(form.getVbExp()));
		pstmt.setString(15, Utility.encodeForDBToTickMark(form.getToolsExp()));

		pstmt.setString(16, Utility.encodeForDBToTickMark(form.getVbIntention()));
		pstmt.setString(17, Utility.encodeForDBToTickMark(form.getExpRegionA()));
		pstmt.setString(18, Utility.encodeForDBToTickMark(form.getExpRegionAVeg()));
		pstmt.setString(19, Utility.encodeForDBToTickMark(form.getExpRegionAFlor()));
		pstmt.setString(20, Utility.encodeForDBToTickMark(form.getExpRegionANVC()));
		pstmt.setString(21, Utility.encodeForDBToTickMark(form.getExpRegionB()));
		pstmt.setString(22, Utility.encodeForDBToTickMark(form.getExpRegionBVeg()));
		pstmt.setString(23, Utility.encodeForDBToTickMark(form.getExpRegionBFlor()));
		pstmt.setString(24, Utility.encodeForDBToTickMark(form.getExpRegionBNVC()));
		pstmt.setString(25, Utility.encodeForDBToTickMark(form.getExpRegionC()));
		pstmt.setString(26, Utility.encodeForDBToTickMark(form.getExpRegionCVeg()));
		pstmt.setString(27, Utility.encodeForDBToTickMark(form.getExpRegionCFlor()));
		pstmt.setString(28, Utility.encodeForDBToTickMark(form.getExpRegionCNVC()));
		pstmt.setString(29, Utility.encodeForDBToTickMark(form.getEsaSponsorNameA()));
		pstmt.setString(30, Utility.encodeForDBToTickMark(form.getEsaSponsorEmailA()));
		pstmt.setString(31, Utility.encodeForDBToTickMark(form.getEsaSponsorNameB()));
		pstmt.setString(32, Utility.encodeForDBToTickMark(form.getEsaSponsorEmailB()));
		pstmt.setString(33, Utility.encodeForDBToTickMark(form.getPeerReview()));
		pstmt.setString(34, Utility.encodeForDBToTickMark(form.getAddlStmt()));
		pstmt.setLong(35, form.getUsrId());
		pstmt.setString(36, Utility.encodeForDBToTickMark(form.getCertificationstatus()));


		// execute the insert



		log.info("UserDatabaseAccess: saving cert. app");
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

        rs.close();
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
        rs.close();
        query.close();

		DBConnectionPool.returnDBConnection(conn);
		return(usrId);
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
            rs.close();
            query.close();
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
	 * This method updates, umm - what was it?  Hmmm.
	 * Oh yeah the user's password.
	 * @param password new password
	 * @param emailAddress of the user
	 */
	public void updatePassword(String password, String emailAddress)
	{
		try
		{ //create a digest of the password and store that
      String digest = getDigest(password, emailAddress);
      //System.out.println("digest for user " + emailAddress + ": " + digest);
			DBConnection conn = getConnection();
			Statement query = conn.createStatement();
      //store the digest string
			query.execute("UPDATE usr set password='" + digest +
					"' WHERE email_address='" + emailAddress + "' ");
            query.close();
			DBConnectionPool.returnDBConnection(conn);
		}
		catch (SQLException e)
		{
			log.debug("Exception: " + e.getMessage());
			e.printStackTrace();
		}
    catch (GeneralSecurityException gse)
    {
      log.debug("Error getting digest of password: " + gse.getMessage());
      System.out.println("Error getting digest of password: " + gse.getMessage());
      gse.printStackTrace();
    }
	}

  /**
   * return the digest of a username and a password
   */
  public String getDigest(String password, String emailAddress)
    throws GeneralSecurityException
  {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    //create the digest from the emailAddress and the password since both
    //must be correct
    byte b[] = digest.digest((emailAddress + password).getBytes());
    String s = "";
    //s += Long.toHexString((new Byte(b)).longValue());
    for(int i=0; i<b.length; i++)
    {
      s += Long.toHexString((new Byte(b[i])).longValue());
    }
    return s;
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

        sb.append("UPDATE usr SET ticket_count = ticket_count + 1, last_connect=now() ");
		sb.append("WHERE email_address LIKE '"+emailAddress+"' ");

		//issue the query
		query.executeUpdate(sb.toString());
        query.close();
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
	 private WebUser getAllUserData(String uniqueClause) throws SQLException
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
			userBean.setPermissiontype( results.getLong(6) );
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
        results.close();
        query.close();
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
	 public void updateCertificationStatus(long usercertification_id, String status, String comment)
		 	throws SQLException
	 {
		DBConnection conn = getConnection();
		Statement stmt = conn.createStatement();
		StringBuffer sb = new StringBuffer(256)
			.append("UPDATE usercertification SET certificationstatus='")
			.append(Utility.encodeForDB(status));

		if (!Utility.isStringNullOrEmpty(comment)) {
			sb.append("', certificationstatuscomments='")
				.append(Utility.encodeForDB(comment));
		}

		sb.append("' WHERE usercertification_id='")
			.append(usercertification_id).append("'");

		log.debug("setting cert status: " + sb.toString());
		stmt.executeUpdate(sb.toString());
        stmt.close();
		conn.close();
		DBConnectionPool.returnDBConnection(conn);
	 }

	/**
	 * Returns List of all header CertificationForms in the system.
	 *
	 * @param sortBy
	 * @return List of partially complete CertificationForms
	 */
	 public List getAllCertificationAppHeaders(String sortBy)
		 	throws java.sql.SQLException
	 {
	 	return getAllCertificationApps(sortBy, true);
	 }


	/**
	 * Returns List of all CertificationForms in the system.
	 *
	 * @param sortBy
	 * @return List of CertificationForms
	 */
	 public List getAllCertificationApps(String sortBy)
		 	throws java.sql.SQLException
	 {
	 	return getAllCertificationApps(sortBy, false);
	 }

	 /**
	  * The private method that actually gets the cert. apps.
	  *
	  * @param sortBy
	  * @return List of CertificationForms
	  */
	 private List getAllCertificationApps(String sortBy, boolean headersOnly)
		 	throws java.sql.SQLException
	 {
		List allApps = new ArrayList();
		DBConnection conn = getConnection();
		CertificationForm tmpCertForm;

		// make sure give sort string is safe
		sortBy = (Utility.isStringNullOrEmpty(sortBy) ? "usercertification_id" : sortBy);
		if (sortBy.indexOf(' ') != -1) {
			sortBy = "usercertification_id";
		}

		Statement query = conn.createStatement();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(CertificationForm.getDBFieldNames(headersOnly));
		sb.append(" FROM usercertification ORDER BY " + sortBy);

		//issue the query
		ResultSet results = query.executeQuery(sb.toString());

		while (results.next()) {
			tmpCertForm = new CertificationForm();
			tmpCertForm.initFromResultSet(results, headersOnly);

			// fill in the blanks
			fillInCertUserInfo(tmpCertForm);
			allApps.add(tmpCertForm);
		}
        results.close();
        query.close();

		return allApps;
	 }

	/**
	 * Private method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 *
	 * @param usercertification_id
	 * @return CertificationForm or null if none found
	 */
	 public CertificationForm getCertificationApp(long usercertification_id)
		 	throws SQLException
	 {
		//get the connections etc
		DBConnection conn = getConnection();

		Statement query = conn.createStatement();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
			.append(CertificationForm.getDBFieldNames(false))
			.append(" FROM usercertification ")
			.append(" WHERE usercertification_id = ").append(usercertification_id);

		 //issue the query
		//log.debug("UDA.getCert: QUERY: " + sb.toString());
		ResultSet results = query.executeQuery(sb.toString());

		//get the results -- assumming 0 or 1 rows returned
		CertificationForm certForm = new CertificationForm();
		if (results.next()) {
			certForm.initFromResultSet(results, false);
		} else {
			log.debug("UDA.getCert: NOT FOUND # " + usercertification_id);
            results.close();
            query.close();
            conn.close();
			return null;
		}

        query.close();
        results.close();
		conn.close();
		DBConnectionPool.returnDBConnection(conn);

		// fill in the blanks
		fillInCertUserInfo(certForm);

		return certForm;
	 }

   /**
    * get the user list info
    */
   public List getFullUserList(String sortBy)
     throws java.sql.SQLException
   {
      List allUsrList = new ArrayList();
      DBConnection conn = getConnection();
      UserForm usrForm = new UserForm();

      if(Utility.isStringNullOrEmpty(sortBy))
      {
        sortBy = "usr_id";
      }

      Statement query = conn.createStatement();
      String sql = usrForm.getSQL(sortBy);

      //issue the query
      ResultSet results = query.executeQuery(sql);

      while (results.next()) {
        usrForm = new UserForm();
        usrForm.initFromResultSet(results);

        // fill in the blanks
        //fillInCertUserInfo(tmpCertForm);
        allUsrList.add(usrForm);
      }
      results.close();
      query.close();

      return allUsrList;
   }

	 /**
	  *
	  */
	 private void fillInCertUserInfo(CertificationForm certForm)
		 	throws SQLException
	{
		WebUser user = getUser(certForm.getUsrId());
		if (Utility.isStringNullOrEmpty( certForm.getEmailAddress() )) {
			certForm.setEmailAddress( user.getEmail() );
		}
		if (Utility.isStringNullOrEmpty( certForm.getSurName() )) {
			certForm.setSurName( user.getSurname() );
		}
		if (Utility.isStringNullOrEmpty( certForm.getGivenName() )) {
			certForm.setGivenName( user.getGivenname() );
		}
		if (Utility.isStringNullOrEmpty( certForm.getPhoneNumber() )) {
			certForm.setPhoneNumber( user.getDayphone() );
		}
	 }

	/**
	 * method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 *
	 * @param usrId -- the usr.usr_id of the user
	 * @return WebUser or null if none found
	 */
	 public WebUser getUser(long usrId) throws SQLException
	 {
		return getAllUserData(" usr_id=" + usrId);
	 }

	/**
	 * method to return the user info for a user in the user database.  The
	 * user data will be returned as a WebUser bean.
	 *
	 * @param usrId -- the usr.usr_id of the user
	 * @return WebUser or null if none found
	 */
	 public WebUser getUser(Long usrId) throws SQLException
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
	 public WebUser getUser(String emailAddress ) throws SQLException
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
                rs.close();
                stmt.close();

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
        rs.close();
        query.close();

		DBConnectionPool.returnDBConnection(conn);
		return result;
	}

	/**
	 * Set sum of permissions.
	 *
	 * @param usrId -- the usr.usr_id of the user
	 * @param sum -- the sum of roles
	 */
		public void setUserPermissionSum(long usrId, long sum) throws Exception
		{
			if (usrId == 0) {
				throw new Exception("Can't set permissions: null usr_id");
			}

			DBConnection conn = getConnection();
			Statement stmt = conn.createStatement();
            String sql = "UPDATE usr SET permission_type=" + sum +
                                " WHERE usr_id=" + usrId;
            stmt.executeUpdate(sql);
            // if this is not 1, then should make the PARTY associated with this public:
            if (usrId > 1 ) {
              String sqlPty = "UPDATE party SET partypublic=true WHERE party_id = (select party_ID from usr where usr_id=" + usrId + ")";
              stmt.executeUpdate(sqlPty);
            }
			stmt.close();
			DBConnectionPool.returnDBConnection(conn);
		}

	/**
	 *
	 * @param usrId -- the usr.usr_id of the user
	 * @return sum of permissions
	 */
	 public long getUserPermissionSum(long usrId) throws SQLException
	 {
		DBConnection conn = getConnection();
		Statement query = conn.createStatement();
		query.executeQuery("SELECT permission_type FROM usr WHERE usr_id=" + usrId);
		ResultSet rs = query.getResultSet();

		long sum = 0;
		if (rs.next() ) {
			sum = rs.getLong(1);
		}

		rs.close();
		query.close();
		DBConnectionPool.returnDBConnection(conn);
		return sum;
	 }

	/**
   * this method seeds the admin account with the default password.
   */
  private void seedAdminAccountPassword()
    throws Exception
  {
    System.out.println("Seeding admin acct");
    UserDatabaseAccess udb = new UserDatabaseAccess();
    udb.updatePassword("vbadmin", "admin@vegbank.org");
    System.out.println("admin account seeded with default password.");
  }

  /**
   * this method converts all passwords in the database to MD5 digests
   * from clear text.
   */
  private void convertPasswords()
    throws Exception
  {
    String sql = "select email_address, password from usr";
    DBConnection conn = getConnection();
		Statement query = conn.createStatement();
		query.executeQuery(sql);
		ResultSet rs = query.getResultSet();
    while(rs.next())
    {
      String email = rs.getString(1);
      String pass = rs.getString(2);
      updatePassword(pass, email);
    }
  }

	/**
	 * main method for testing
	 */
	public static void main(String[] args)
	{
    System.out.println("Usage:");
    System.out.println("UserDatabaseAccess seedAdmin --seeds the " +
      "admin account with the default password");
    System.out.println("UserDatabaseAccess convertPasswords --converts " +
      "existing clear text passwords to the new digest MD5 format.");

    try
    {
      UserDatabaseAccess uda = new UserDatabaseAccess();
      if(args[0].equals("seedAdmin"))
      {
        uda.seedAdminAccountPassword();
      }
      else if(args[0].equals("convertPasswords"))
      {
        uda.convertPasswords();
      }
    }
    catch(Exception e)
    {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }

    System.exit(0);
	}

}
