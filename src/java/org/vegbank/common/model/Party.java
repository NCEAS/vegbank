/*
 *	'$RCSfile: Party.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-07 22:28:43 $'
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
 
package org.vegbank.common.model;

/**
 * @author farrell
 */

public class Party
{

	private String email = null;
	private String givenName = null;
	private String surname = null;
	private String organizationName = null;
	private String contactInstructions = null;
	private String salutation = null;
	private String suffix  = null;
	private String currentParty = null;
	private String type = null;
	
	/**
	 * @return String
	 */
	public String getContactInstructions()
	{
		return contactInstructions;
	}

	/**
	 * @return String
	 */
	public String getCurrentParty()
	{
		return currentParty;
	}

	/**
	 * @return String
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @return String
	 */
	public String getGivenName()
	{
		return givenName;
	}

	/**
	 * @return String
	 */
	public String getOrganizationName()
	{
		return organizationName;
	}

	/**
	 * @return String
	 */
	public String getSalutation()
	{
		return salutation;
	}

	/**
	 * @return String
	 */
	public String getSuffix()
	{
		return suffix;
	}

	/**
	 * @return String
	 */
	public String getSurname()
	{
		return surname;
	}

	/**
	 * @return String
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the contactInstructions.
	 * @param contactInstructions The contactInstructions to set
	 */
	public void setContactInstructions(String contactInstructions)
	{
		this.contactInstructions = contactInstructions;
	}

	/**
	 * Sets the currentParty.
	 * @param currentParty The currentParty to set
	 */
	public void setCurrentParty(String currentParty)
	{
		this.currentParty = currentParty;
	}

	/**
	 * Sets the email.
	 * @param email The email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Sets the givenName.
	 * @param givenName The givenName to set
	 */
	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	/**
	 * Sets the organizationName.
	 * @param organizationName The organizationName to set
	 */
	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}

	/**
	 * Sets the salutation.
	 * @param salutation The salutation to set
	 */
	public void setSalutation(String salutation)
	{
		this.salutation = salutation;
	}

	/**
	 * Sets the suffix.
	 * @param suffix The suffix to set
	 */
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	/**
	 * Sets the surname.
	 * @param surname The surname to set
	 */
	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

}
