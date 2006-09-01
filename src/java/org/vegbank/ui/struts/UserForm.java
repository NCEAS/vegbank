/**	
 *  '$RCSfile: UserForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-09-01 21:12:17 $'
 *	'$Revision: 1.3 $'
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
 
package org.vegbank.ui.struts;

import java.sql.*;

import org.apache.struts.validator.ValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.PermComparison;

/**
 * @author anderson
 */

public class UserForm extends ValidatorForm 
			implements java.io.Serializable
{
	private static Log log = LogFactory.getLog(CertificationForm.class);

	// prepopulated fields
	private long usrId;
	private String emailAddress;
	private String surName;
	private String givenName;
  private int permissionType;
  private String accessionCode;
  private String organization;

	/**
	 * Constructor
	 */
	public UserForm() {
		super();
	}

	/**
	 * Get value of 'usrId' property.
	 */
	public long getUsrId() {
		return this.usrId;
	}

	/**
	 * Set the 'usrId' property.
	 */
	public void setUsrId(long usrId) {
		this.usrId = usrId;
	}

	/**
	 * @return
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param string
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return
	 */
	public String getSurName() {
		return surName;
	}

	/**
	 * @param string
	 */
	public void setSurName(String surName) {
		this.surName = surName;
	}

	/**
	 * @return
	 */
	public String getGivenName() {
		return givenName;
	}
  
  	/**
	 * @param string
	 */
	public void setGivenName(String givenName ) {
		this.givenName  = givenName;
	}
  
  /**
	 * @param string
	 */
	public void setPermissionType(int permType) {
		this.permissionType = permType;
	}

	/**
	 * @return int
	 */
	public int getPermissionType() {
		return permissionType;
	}
  
  /**
	 * @param string
	 */
	public void setAccessionCode(String accCode) {
		this.accessionCode = accCode;
	}

	/**
	 * @return String
	 */
	public String getAccessionCode() {
		return accessionCode;
	}

  /**
	 * @param string
	 */
	public void setOrganization(String org) {
		this.organization = org;
	}

	/**
	 * @return String
	 */
	public String getOrganization() {
		return organization;
	}
  
	/**
	 * return need fields from the usr table
	 */
	public static String getSQL(String sortBy) {
		StringBuffer sb = new StringBuffer(512);
    
    sb.append("select usr.usr_id, usr.email_address, party.surname, party.givenname, " +
      "usr.permission_type, party.accessioncode, party.organizationname " + 
      "from usr, party where usr.party_id = party.party_id order by " + sortBy);
		return sb.toString();
	}

	/**
	 * 
	 */
	public void initFromResultSet(ResultSet results) 
			throws SQLException
	{
		if (results == null) {
			return;
		}
    this.setUsrId(results.getInt(1));
		this.setEmailAddress(results.getString(2));
    this.setSurName(results.getString(3));
    this.setGivenName(results.getString(4));
    this.setPermissionType(results.getInt(5));
    this.setAccessionCode(results.getString(6));
    this.setOrganization(results.getString(7));
	}
}
