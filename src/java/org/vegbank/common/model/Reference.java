/*
 *	'$RCSfile: Reference.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-07 22:28:44 $'
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

public class Reference
{
	private String authors = null;
	private String title = null;
	private String pubdate = null;
	private String edition = null;
	private String otherCitationDetails = null;
	private String organization = null;
	private String email = null;
	private String contactInstructions = null;
	private String seriesName = null;
	private String ISBN = null;
	private String ISSN = null;
	private String page = null;
	private String volume = null;
			
	/**
	 * @return String
	 */
	public String getAuthors()
	{
		return authors;
	}

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
	public String getEdition()
	{
		return edition;
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
	public String getOrganization()
	{
		return organization;
	}

	/**
	 * @return String
	 */
	public String getOtherCitationDetails()
	{
		return otherCitationDetails;
	}

	/**
	 * @return String
	 */
	public String getPubdate()
	{
		return pubdate;
	}

	/**
	 * @return String
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the authors.
	 * @param authors The authors to set
	 */
	public void setAuthors(String authors)
	{
		this.authors = authors;
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
	 * Sets the edition.
	 * @param edition The edition to set
	 */
	public void setEdition(String edition)
	{
		this.edition = edition;
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
	 * Sets the organization.
	 * @param organization The organization to set
	 */
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	/**
	 * Sets the otherCitationDetails.
	 * @param otherCitationDetails The otherCitationDetails to set
	 */
	public void setOtherCitationDetails(String otherCitationDetails)
	{
		this.otherCitationDetails = otherCitationDetails;
	}

	/**
	 * Sets the pubdate.
	 * @param pubdate The pubdate to set
	 */
	public void setPubdate(String pubdate)
	{
		this.pubdate = pubdate;
	}

	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return String
	 */
	public String getISBN()
	{
		return ISBN;
	}

	/**
	 * @return String
	 */
	public String getPage()
	{
		return page;
	}

	/**
	 * @return String
	 */
	public String getSeriesName()
	{
		return seriesName;
	}

	/**
	 * @return String
	 */
	public String getVolume()
	{
		return volume;
	}

	/**
	 * Sets the iSBN.
	 * @param iSBN The iSBN to set
	 */
	public void setISBN(String iSBN)
	{
		ISBN = iSBN;
	}

	/**
	 * Sets the page.
	 * @param page The page to set
	 */
	public void setPage(String page)
	{
		this.page = page;
	}

	/**
	 * Sets the seriesName.
	 * @param seriesName The seriesName to set
	 */
	public void setSeriesName(String seriesName)
	{
		this.seriesName = seriesName;
	}

	/**
	 * Sets the volume.
	 * @param volume The volume to set
	 */
	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	/**
	 * @return String
	 */
	public String getISSN()
	{
		return ISSN;
	}

	/**
	 * Sets the iSSN.
	 * @param iSSN The iSSN to set
	 */
	public void setISSN(String iSSN)
	{
		ISSN = iSSN;
	}

}
