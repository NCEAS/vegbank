/*
 *	'$RCSfile: CitationContributor.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:34:14 $'
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

public class CitationContributor
{
	

	
	private String roleType;
	private String order;
	private Reference reference;

	// Right now does the job for citationParty also	
	private String type;
	private String positionName;
	private String salutation;
	private String givenName;
	private String surname;
	private String suffix;
	private String organizationName;

	/**
	 * @return String
	 */
	public String getOrder()
	{
		return order;
	}

	/**
	 * @return Reference
	 */
	public Reference getReference()
	{
		return reference;
	}

	/**
	 * @return String
	 */
	public String getRoleType()
	{
		return roleType;
	}

	/**
	 * Sets the order.
	 * @param order The order to set
	 */
	public void setOrder(String order)
	{
		this.order = order;
	}


	/**
	 * Sets the reference.
	 * @param reference The reference to set
	 */
	public void setReference(Reference reference)
	{
		this.reference = reference;
	}

	/**
	 * Sets the roleType.
	 * @param roleType The roleType to set
	 */
	public void setRoleType(String roleType)
	{
		this.roleType = roleType;
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
	public String getPositionName()
	{
		return positionName;
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
	 * Sets the positionName.
	 * @param positionName The positionName to set
	 */
	public void setPositionName(String positionName)
	{
		this.positionName = positionName;
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
