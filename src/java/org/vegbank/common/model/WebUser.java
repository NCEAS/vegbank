package org.vegbank.common.model;

import org.vegbank.common.utility.LogUtility;

/*
 * '$RCSfile: WebUser.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-12-02 19:53:13 $'
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
/**
 * Hold details about the current users as a convience for web code.
 * 
 * @author farrell
 */
public class WebUser
{
	private String givenname = "";
	private String middlename = "";
	private String surname = "";
	private String organizationname = "";
	private String email = "";
	private String password = "";
	private String permissiontype = "";
	private String institution = "";
	private String salutation = "";
	private String ticketcount = "";
	private String address = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String zipcode = "";
	private String dayphone = "";
	private int userid = 0;
	private int partyid = 0;
	private boolean guestUser = false;

	/**
	 * @return
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @return
	 */
	public String getGivenname()
	{
		return givenname;
	}

	/**
	 * @return
	 */
	public String getMiddlename()
	{
		return middlename;
	}

	/**
	 * @return
	 */
	public String getOrganizationname()
	{
		return organizationname;
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
		return surname;
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
	
	public String getUsername()
	{
		return this.getEmail();
	}

	/**
	 * @return
	 */
	public String getPermissiontype()
	{
		return permissiontype;
	}

	/**
	 * @param string
	 */
	public void setPermissiontype(String string)
	{
		permissiontype = string;
	}
	/**
	 * @return
	 */
	public String getInstitution()
	{
		return institution;
	}

	/**
	 * @return
	 */
	public String getSalutation()
	{
		return salutation;
	}

	/**
	 * @param string
	 */
	public void setInstitution(String string)
	{
		institution = string;
	}

	/**
	 * @param string
	 */
	public void setSalutation(String string)
	{
		salutation = string;
	}
	
	/**
	 * @return certificationLevel
	 */
	public int getCertificationLevel()
	{
		int result = 0;
		try
		{
			result =  new Integer( this.getPermissiontype() ).intValue();
		}
		catch ( Exception e)
		{
			LogUtility.log(
				"WebUser: Error thrown getting certification level from '"
					+ this.getPermissiontype()
					+ "'",
				e);
		}
		return result;
	}


	/**
	 * @return
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @return
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @return
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @return
	 */
	public String getDayphone()
	{
		return dayphone;
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
		return state;
	}

	/**
	 * @return
	 */
	public String getTicketcount()
	{
		return ticketcount;
	}

	/**
	 * @return
	 */
	public int getUserid()
	{
		return userid;
	}

	/**
	 * @return
	 */
	public String getZipcode()
	{
		return zipcode;
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
	 * @param string
	 */
	public void setTicketcount(String string)
	{
		ticketcount = string;
	}

	/**
	 * @param i
	 */
	public void setUserid(int i)
	{
		userid = i;
	}

	/**
	 * @param string
	 */
	public void setZipcode(String string)
	{
		zipcode = string;
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

}
