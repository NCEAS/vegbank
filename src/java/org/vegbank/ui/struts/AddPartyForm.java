/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddPartyForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:27 $'
 *	'$Revision: 1.1 $'
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

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.model.Party;

/**
 * @author farrell
 *
 * Struts ActionForm for the AddReference form.
 */
public class AddPartyForm extends ValidatorForm
{
	
	private Party  party = new Party();
	
	// Phone Number
	private String[] phoneNumber = new String[4];
	private String[] phoneType = new String[4];
	
	// Address stuff
	private String[] organization_ID = new String[2];
	private String[] orgPosition = new String[2];
	private String[] email = new String[2];	
	private String[] deliveryPoint= new String[2];
	private String[] city = new String[2];
	private String[] administrativeArea = new String[2];
	private String[] postalCode = new String[2];
	private String[] country = new String[2];
	private String[] startDate = new String[2];
	//private boolean[] currentAddress = {false, false};

		
	/**
	 * @return
	 */
	public Party getParty()
	{
		return party;
	}



	/**
	 * @return
	 */
	public String[] getCity()
	{
		return city;
	}

	/**
	 * @return
	 */
	public String[] getCountry()
	{
		return country;
	}

	/**
	 * @return
	 */
	public String[] getEmail()
	{
		return email;
	}

	/**
	 * @return
	 */
	public String[] getOrganization_ID()
	{
		return organization_ID;
	}

	/**
	 * @return
	 */
	public String[] getOrgPosition()
	{
		return orgPosition;
	}

	/**
	 * @return
	 */
	public String[] getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @return
	 */
	public String[] getPhoneType()
	{
		return phoneType;
	}

	/**
	 * @return
	 */
	public String[] getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @return
	 */
	public String[] getStartDate()
	{
		return startDate;
	}

	/**
	 * @param strings
	 */
	public void setCity(String[] strings)
	{
		city = strings;
	}

	/**
	 * @param strings
	 */
	public void setCountry(String[] strings)
	{
		country = strings;
	}

	/**
	 * @param strings
	 */
	public void setEmail(String[] strings)
	{
		email = strings;
	}

	/**
	 * @param strings
	 */
	public void setOrganization_ID(String[] strings)
	{
		organization_ID = strings;
	}

	/**
	 * @param strings
	 */
	public void setOrgPosition(String[] strings)
	{
		orgPosition = strings;
	}

	/**
	 * @param party
	 */
	public void setParty(Party party)
	{
		this.party = party;
	}

	/**
	 * @param strings
	 */
	public void setPhoneNumber(String[] strings)
	{
		phoneNumber = strings;
	}

	/**
	 * @param strings
	 */
	public void setPhoneType(String[] strings)
	{
		phoneType = strings;
	}

	/**
	 * @param strings
	 */
	public void setPostalCode(String[] strings)
	{
		postalCode = strings;
	}

	/**
	 * @param strings
	 */
	public void setStartDate(String[] strings)
	{
		startDate = strings;
	}

	/**
	 * @return
	 */
	public String[] getDeliveryPoint()
	{
		return deliveryPoint;
	}

	/**
	 * @param strings
	 */
	public void setDeliveryPoint(String[] strings)
	{
		deliveryPoint = strings;
	}

	/**
	 * @return
	 */
	public String[] getAdministrativeArea()
	{
		return administrativeArea;
	}

	/**
	 * @param strings
	 */
	public void setAdministrativeArea(String[] strings)
	{
		administrativeArea = strings;
	}

//	/**
//	 * @return
//	 */
//	public boolean[] getCurrentAddress()
//	{
//		return currentAddress;
//	}
//
//	/**
//	 * @param bs
//	 */
//	public void setCurrentAddress(boolean[] bs)
//	{
//		currentAddress = bs;
//	}

}
