/*
 *	'$RCSfile: Reference.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:34:16 $'
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

import java.util.AbstractList;

/**
 * @author farrell
 */

public class Reference
{
	private String shortName;
	private String plantReferenceType;
	private String title;
	private String titleSuperior;
	private String pubDate;
	private String accessDate;
	private String conferenceDate;
	private String volume;
	private String issue;
	private String pageRange;
	private String totalPages;
	private String publisher;
	private String publicationPlace;
	private String isbn;
	private String edition;
	private String numberOfVolumes;
	private String chapterNumber;
	private String reportNumber;
	private String communicationType;
	private String degree;
	private String url; 
	
	// Handles citation journal itself
	private String journal;
	private String issn;
	private String abbreviation;
		
	// Probably should not be here
	private AbstractList citationContributors;
	
	

		
	/**
	 * @return String
	 */
	public String getAccessDate()
	{
		return accessDate;
	}

	/**
	 * @return String
	 */
	public String getChapterNumber()
	{
		return chapterNumber;
	}

	/**
	 * @return String
	 */
	public String getCommunicationType()
	{
		return communicationType;
	}

	/**
	 * @return String
	 */
	public String getConferenceDate()
	{
		return conferenceDate;
	}

	/**
	 * @return String
	 */
	public String getDegree()
	{
		return degree;
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
	public String getIsbn()
	{
		return isbn;
	}

	/**
	 * @return String
	 */
	public String getIssue()
	{
		return issue;
	}

	/**
	 * @return String
	 */
	public String getNumberOfVolumes()
	{
		return numberOfVolumes;
	}

	/**
	 * @return String
	 */
	public String getPageRange()
	{
		return pageRange;
	}

	/**
	 * @return String
	 */
	public String getPlantReferenceType()
	{
		return plantReferenceType;
	}

	/**
	 * @return String
	 */
	public String getPubDate()
	{
		return pubDate;
	}

	/**
	 * @return String
	 */
	public String getPublicationPlace()
	{
		return publicationPlace;
	}

	/**
	 * @return String
	 */
	public String getPublisher()
	{
		return publisher;
	}

	/**
	 * @return String
	 */
	public String getReportNumber()
	{
		return reportNumber;
	}

	/**
	 * @return String
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * @return String
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @return String
	 */
	public String getTitleSuperior()
	{
		return titleSuperior;
	}

	/**
	 * @return String
	 */
	public String getTotalPages()
	{
		return totalPages;
	}

	/**
	 * @return String
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return String
	 */
	public String getVolume()
	{
		return volume;
	}

	/**
	 * Sets the accessDate.
	 * @param accessDate The accessDate to set
	 */
	public void setAccessDate(String accessDate)
	{
		this.accessDate = accessDate;
	}

	/**
	 * Sets the chapterNumber.
	 * @param chapterNumber The chapterNumber to set
	 */
	public void setChapterNumber(String chapterNumber)
	{
		this.chapterNumber = chapterNumber;
	}

	/**
	 * Sets the communicationType.
	 * @param communicationType The communicationType to set
	 */
	public void setCommunicationType(String communicationType)
	{
		this.communicationType = communicationType;
	}

	/**
	 * Sets the conferenceDate.
	 * @param conferenceDate The conferenceDate to set
	 */
	public void setConferenceDate(String conferenceDate)
	{
		this.conferenceDate = conferenceDate;
	}

	/**
	 * Sets the degree.
	 * @param degree The degree to set
	 */
	public void setDegree(String degree)
	{
		this.degree = degree;
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
	 * Sets the isbn.
	 * @param isbn The isbn to set
	 */
	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}

	/**
	 * Sets the issue.
	 * @param issue The issue to set
	 */
	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	/**
	 * Sets the numberOfVolumes.
	 * @param numberOfVolumes The numberOfVolumes to set
	 */
	public void setNumberOfVolumes(String numberOfVolumes)
	{
		this.numberOfVolumes = numberOfVolumes;
	}

	/**
	 * Sets the pageRange.
	 * @param pageRange The pageRange to set
	 */
	public void setPageRange(String pageRange)
	{
		this.pageRange = pageRange;
	}

	/**
	 * Sets the plantReferenceType.
	 * @param plantReferenceType The plantReferenceType to set
	 */
	public void setPlantReferenceType(String plantReferenceType)
	{
		this.plantReferenceType = plantReferenceType;
	}

	/**
	 * Sets the pubDate.
	 * @param pubDate The pubDate to set
	 */
	public void setPubDate(String pubDate)
	{
		this.pubDate = pubDate;
	}

	/**
	 * Sets the publicationPlace.
	 * @param publicationPlace The publicationPlace to set
	 */
	public void setPublicationPlace(String publicationPlace)
	{
		this.publicationPlace = publicationPlace;
	}

	/**
	 * Sets the publisher.
	 * @param publisher The publisher to set
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	/**
	 * Sets the reportNumber.
	 * @param reportNumber The reportNumber to set
	 */
	public void setReportNumber(String reportNumber)
	{
		this.reportNumber = reportNumber;
	}

	/**
	 * Sets the shortName.
	 * @param shortName The shortName to set
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
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
	 * Sets the titleSuperior.
	 * @param titleSuperior The titleSuperior to set
	 */
	public void setTitleSuperior(String titleSuperior)
	{
		this.titleSuperior = titleSuperior;
	}

	/**
	 * Sets the totalPages.
	 * @param totalPages The totalPages to set
	 */
	public void setTotalPages(String totalPages)
	{
		this.totalPages = totalPages;
	}

	/**
	 * Sets the url.
	 * @param url The url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
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
	 * @return AbstractList
	 */
	public AbstractList getCitationContributors()
	{
		return citationContributors;
	}

	/**
	 * @return String
	 */
	public String getIssn()
	{
		return issn;
	}

	/**
	 * @return String
	 */
	public String getJournal()
	{
		return journal;
	}

	/**
	 * Sets the citationContributors.
	 * @param citationContributors The citationContributors to set
	 */
	public void setCitationContributors(AbstractList citationContributors)
	{
		this.citationContributors = citationContributors;
	}

	/**
	 * Sets the issn.
	 * @param issn The issn to set
	 */
	public void setIssn(String issn)
	{
		this.issn = issn;
	}

	/**
	 * Sets the journal.
	 * @param journal The journal to set
	 */
	public void setJournal(String journal)
	{
		this.journal = journal;
	}

}
