/*
 *	'$RCSfile: PartySummary.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-09 18:11:46 $'
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

package org.vegbank.common.model;

import java.io.Serializable;

import org.vegbank.common.command.QueryParties;

/**
 * @author farrell
 */

/**
 * @author farrell
 *
 * JavaBean to hold the party summary.
 */
public class PartySummary  implements Serializable
{
	private QueryParties parties;
	private String salutation;
	private String id;
	private String surname;
	private String givenName;
  private String middleName;
	private String organization;
	private String contactinstructions;

	public PartySummary(QueryParties parties)
	{
		this.parties = parties;
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return
	 */
	public String getSalutation()
	{
		return salutation;
	}

	/**
	 * @param key
	 */
	public void setId(String key)
	{
		id = key;
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
	public String getGivenName()
	{
		return givenName;
	}
	/**
	 * @param string
	 */
	public void setGivenName(String string)
	{
		givenName = string;
	}
	/**
		 * @return
		 */
		public String getMiddleName()
		{
			return middleName;
		}
		/**
		 * @param string
		 */
		public void setMiddleName(String string)
		{
			middleName = string;
	}
	/**
		 * @return
		 */
		public String getOrganization()
		{
			return organization;
		}
		/**
		 * @param string
		 */
		public void setOrganization(String string)
		{
			organization = string;
	}
	/**
		 * @return
		 */
		public String getContactInstructions()
		{
			return contactinstructions;
		}
		/**
		 * @param string
		 */
		public void setContactInstructions(String string)
		{
			contactinstructions = string;
		}
}