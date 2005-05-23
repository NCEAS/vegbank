/*
 * '$RCSfile: WebUser.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-23 07:50:52 $'
 *	'$Revision: 1.15 $'
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

package org.vegbank.common.model;

import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DateUtility;
import org.vegbank.common.utility.PermComparison;
import java.io.*;


/**
 * Hold details about the current users as a convenience for web code.
 * 
 * @author farrell
 */
public class WebUser
{
    public static final String DEFAULT_VB_DATA_DIR = "/usr/vegbank/upload";
    public static final String DEFAULT_VB_EXPORT_DIR = "/usr/www/vegbank/htdocs/export";

	private String givenname = "";
	private String middlename = "";
	private String surname = "";
	private String preferredname = "";
	private String organizationname = "";
	private String email = "";
	private String password = "";
	private long permissiontype = 0;
	private String salutation = "";
	private int ticketcount = 0;
	private String address = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String postalcode = "";
	private String dayphone = "";
	private long userid = 0;
	private long addressid = 0;
	private int partyid = 0;
	private boolean guestUser = false;
	private boolean sqlSafe = false;

	/**
	 * @return
	 */
	public String getEmail()
	{
		if (sqlSafe)
			return Utility.encodeForDB(email);
		else
			return Utility.decodeFromDB(email);
	}

	/**
	 * @return
	 */
	public String getGivenname()
	{
		if (sqlSafe)
			return Utility.encodeForDB(givenname);
		else
			return Utility.decodeFromDB(givenname);
	}

	/**
	 * @return
	 */
	public String getMiddlename()
	{
		if (sqlSafe)
			return Utility.encodeForDB(middlename);
		else
			return Utility.decodeFromDB(middlename);
	}

	/**
	 * @return
	 */
	public String getPreferredname() {
		if (sqlSafe)
			return Utility.encodeForDB(preferredname);
		else
			return Utility.decodeFromDB(preferredname);
	}

	/**
	 * @return
	 */
	public String getOrganizationname()
	{
		if (sqlSafe)
			return Utility.encodeForDB(organizationname);
		else
			return Utility.decodeFromDB(organizationname);
	}


	/**
	 * @param string
	 */
	public void setEmail(String string)
	{
		email = string;
	}

	/**
	 * @param string
	 */
	public void setGivenname(String string)
	{
		givenname = string;
	}

	/**
	 * @param string
	 */
	public void setMiddlename(String string)
	{
		middlename = string;
	}

	/**
	 * @param string
	 */
	public void setPreferredname(String string) {
		this.preferredname = string;
	}

	/**
	 * @param string
	 */
	public void setOrganizationname(String string)
	{
		organizationname = string;
	}

	/**
	 * Construct the fullName for parts
	 * @param string
	 */
	public String getFullname()
	{
		return this.getGivenname() + " " + this.getSurname();
	}
	
	/**
	 * @return
	 */
	public String getSurname()
	{
		if (sqlSafe)
			return Utility.encodeForDB(surname);
		else
			return Utility.decodeFromDB(surname);
	}

	/**
	 * @param string
	 */
	public void setSurname(String string)
	{
		surname = string;
	}

	/**
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string)
	{
		password = string;
	}
	
	/**
	 * Alias for getEmail().
	 */
	public String getUsername()
	{
		return this.getEmail();
	}

	/**
	 * @return
	 */
	public String getSalutation()
	{
		if (sqlSafe)
			return Utility.encodeForDB(salutation);
		else
			return Utility.decodeFromDB(salutation);
	}

	/**
	 * @param string
	 */
	public void setSalutation(String string)
	{
		salutation = string;
	}
	
	/**
	 * @return
	 */
	public long getPermissiontype()
	{
		return permissiontype;
	}

	/**
	 * Get all permissions.
	 * @return Comma separated list of permission names.
	 */
	public String getPermissions() {
		return PermComparison.getRoleNamesCSVFromSum(this.permissiontype);
	}

	/**
	 * @param string
	 */
	public void setPermissiontype(long i)
	{
		permissiontype = i;
	}

	/**
	 * @return
	 */
	public String getAddress()
	{
		if (sqlSafe)
			return Utility.encodeForDB(address);
		else
			return Utility.decodeFromDB(address);
	}

	/**
	 * @return
	 */
	public String getCity()
	{
		if (sqlSafe)
			return Utility.encodeForDB(city);
		else
			return Utility.decodeFromDB(city);
	}

	/**
	 * @return
	 */
	public String getCountry()
	{
		if (sqlSafe)
			return Utility.encodeForDB(country);
		else
			return Utility.decodeFromDB(country);
	}

	/**
	 * @return
	 */
	public String getDayphone()
	{
		if (sqlSafe)
			return Utility.encodeForDB(dayphone);
		else
			return Utility.decodeFromDB(dayphone);
	}

	/**
	 * @return
	 */
	public int getPartyid()
	{
		return partyid;
	}

	/**
	 * @return
	 */
	public String getState()
	{
		if (sqlSafe)
			return Utility.encodeForDB(state);
		else
			return Utility.decodeFromDB(state);
	}

	/**
	 * @return
	 */
	public int getTicketcount()
	{
		return ticketcount;
	}

	/**
	 * @return
	 */
	public long getUserid()
	{
		return userid;
	}

	/**
	 * @return
	 */
	public long getAddressid()
	{
		return addressid;
	}

	/**
	 * @return
	 */
	public Long getUseridLong()
	{
		return new Long(userid);
	}

	/**
	 * @return
	 */
	public String getPostalcode()
	{
		if (sqlSafe)
			return Utility.encodeForDB(postalcode);
		else
			return Utility.decodeFromDB(postalcode);
	}

	/**
	 * @param string
	 */
	public void setAddress(String string)
	{
		address = string;
	}

	/**
	 * @param string
	 */
	public void setCity(String string)
	{
		city = string;
	}

	/**
	 * @param string
	 */
	public void setCountry(String string)
	{
		country = string;
	}

	/**
	 * @param string
	 */
	public void setDayphone(String string)
	{
		dayphone = string;
	}

	/**
	 * @param i
	 */
	public void setPartyid(int i)
	{
		partyid = i;
	}

	/**
	 * @param string
	 */
	public void setState(String string)
	{
		state = string;
	}

	/**
	 * @param i
	 */
	public void setTicketcount(int i)
	{
		ticketcount = i;
	}

	/**
	 * @param l
	 */
	public void setUserid(long l)
	{
		userid = l;
	}

	/**
	 * Alias for setEmail().
	 * @param string
	 */
	public void setUsername(String string)
	{
		this.setEmail(string);
	}

	/**
	 * @param l
	 */
	public void setAddressid(long l)
	{
		addressid = l;
	}

	/**
	 * @param string
	 */
	public void setPostalcode(String string)
	{
		postalcode = string;
	}

	/**
	 * Encodes SQL for DB or not.
	 */
	public void setSQLSafe(boolean b)
	{
		sqlSafe = b;
	}
	
	/**
	 * @param b
	 */
	public void isGuest(boolean b)
	{
		this.guestUser = b;	
	}
	
	/**
	 * @return is this user a guest
	 */
	public boolean isGuest()
	{
		return this.guestUser;
	}

	/**
	 * Encodes SQL for DB or not.
	 */
	public boolean isSQLSafe()
	{
		return this.sqlSafe;
	}	
	

	/**
     * Can append a timestamp to the upload dir, but doesn't mkdir.
     * @param appendTstamp boolean FORMAT:  /basePath/userId/timestamp/
	 * @return path to directory in file system where this user's file go
	 */
	public String getUploadDir(boolean appendTstamp)
	{
		String dir = Utility.VB_DATA_DIR;
        if (Utility.isStringNullOrEmpty(dir)) {
            dir = DEFAULT_VB_DATA_DIR;
        }

		if (!dir.endsWith(File.separator)) {
			 dir += File.separator;
		}

		dir += getUserid();

		if (!dir.endsWith(File.separator)) {
			 dir += File.separator;
		}

        if (appendTstamp) {
		    dir += DateUtility.getTimestamp() + File.separator;
        }
        return dir;
	}


	/**
     * Get path to this user's files on the server.
	 * @return path to directory in file system where this user's export files are
	 */
	public String getDownloadDir()
	{
		String dir = Utility.VB_EXPORT_DIR;
        if (Utility.isStringNullOrEmpty(dir)) {
            dir = DEFAULT_VB_EXPORT_DIR;
        }

		if (!dir.endsWith(File.separator)) {
			 dir += File.separator;
		}

		dir += getUserid();

		if (!dir.endsWith(File.separator)) {
			 dir += File.separator;
		}

        return dir;
	}
}
