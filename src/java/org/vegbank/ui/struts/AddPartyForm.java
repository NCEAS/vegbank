/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddPartyForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:48:34 $'
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
package org.vegbank.ui.struts;

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Address;
import org.vegbank.common.model.Telephone;
import java.util.Collection;

/**
 * @author farrell
 *
 * Struts ActionForm for the AddReference form.
 */
public class AddPartyForm extends ValidatorForm
{
	
	private Party  party = new Party();
	
	private Collection phoneTypes;

	private String[] phoneNumber = new String[4];
	private String[] phoneType = new String[4];

  private Address address = new Address();
  private boolean currentFlag = false;

		
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
	 * @return
	 */
	public Address getAddress()
	{
		return this.address;
	}

	/**
	 * @param address 
	 */
	public void setAddress(Address address)
	{
		this.address = address;
	}

  /**
   * @return
   */
   public Collection getPhoneTypes()
   {
     if ( phoneTypes == null )
     {
       // Need to create this object
       Telephone tel = new Telephone();
       phoneTypes = tel.getPhoneTypePickList();
     }
     return phoneTypes;
   }
                                                

	/**
	 * @return
	 */
	public boolean isCurrentFlag()
	{
		return currentFlag;
	}

	/**
	 * @param b
	 */
	public void setCurrentFlag(boolean b)
	{
		currentFlag = b;
	}

}
