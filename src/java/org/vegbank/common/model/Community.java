/*
 *	'$RCSfile: Community.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:26 $'
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
import java.util.Vector;

import org.vegbank.common.Constants;

/**
 * @author farrell
 */

public class Community implements Constants
{
	private String classLevel = null;
	private String description = null;
	private String status = null;
	private String code = null;
	private String commonName = null;
	private Party party = null;
	private String name = null;
	private String dateEntered = null;
	private String parentCode = null;
	private Vector usages = new Vector();
	private String usageStartDate = null;
	private String statusStartDate = null;
	private String statusStopDate = null;
	private String statusComment = null;

	private Reference conceptReference;
	private Reference commonNameReference;
	private Reference nameReference;
	private Reference codeNameReference;
	/**
	 * @return String
	 */
	public String getClassLevel()
	{
		return classLevel;
	}

	/**
	 * @return String
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @return String
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Party
	 */
	public Party getParty()
	{
		return party;
	}

	/**
	 * @return String
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the classLevel.
	 * @param classLevel The classLevel to set
	 */
	public void setClassLevel(String classLevel)
	{
		this.classLevel = classLevel;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
		this.createUsage(code, USAGE_NAME_CODE);
	}
	
	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code, String classSystem)
	{
		this.code = code;
		this.createUsage(code, classSystem);
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
		this.createUsage(name, USAGE_NAME_SCIENTIFIC);
	}
	
	/**
	 * Sets the name and the classification system of the name.
	 * 
	 * @param name The name to set
	 * @param classSystem The type of name usage
	 */	
	public void setName(String name, String classSystem)
	{
		this.name = name;
		this.createUsage(name, classSystem);
	}
	
	/**
	 * Creates an instance of the CommunityUsage class using input params
	 * and some defaults
	 * 
	 * @param name
	 * @param classSystem
	 */
	private void createUsage(String name, String classSystem)
	{
		CommunityUsage cu = new CommunityUsage();
		cu.setName(name);
		cu.setNameStatus(USAGE_NAME_STATUS_STANDARD);
		cu.setClassSystem(classSystem);	
		cu.setStartDate(this.usageStartDate); // TODO: This needs to be exposed
		//cu.setStopDate(this.statusStopDate);
		this.addCommunityUsage(cu);
	}

	/**
	 * Sets the party.
	 * @param party The party to set
	 */
	public void setParty(Party party)
	{
		this.party = party;
	}

	/**
	 * Sets the status.
	 * @param status The status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return String
	 */
	public String getDateEntered()
	{
		return dateEntered;
	}

	/**
	 * Sets the dateEntered.
	 * @param dateEntered The dateEntered to set
	 */
	public void setDateEntered(String dateEntered)
	{
		this.dateEntered = dateEntered;
	}

	/**
	 * @return String
	 */
	public String getParentCode()
	{
		return parentCode;
	}

	/**
	 * Sets the parentCode.
	 * @param parentCode The parentCode to set
	 */
	public void setParentCode(String parentCode)
	{
		this.parentCode = parentCode;
	}
	
	/**
	 * @return AbstractList CommunityUsages 
	 */
	public AbstractList getCommunityUsages()
	{
		System.out.println("Communty > returning Usages: " + usages);
		return usages;
	}

	/**
	 * Sets the CommunityUsages.
	 * @param CommunityUsages The CommunityUsages to set
	 */
	public void setCommunityUsages(Vector  commUsages)
	{
		this.usages = commUsages;
	}
	
	/**
	 * Adds a Usage to the  usages list.
	 * @param plantUsages The plantUsages to set
	 */
	public void addCommunityUsage(CommunityUsage commUsage)
	{
		this.usages.add(commUsage);
	}
	

	/**
	 * @return String
	 */
	public String getCommonName()
	{
		return commonName;
	}

	/**
	 * @return Vector
	 */
	public Vector getUsages()
	{
		return usages;
	}

	/**
	 * Sets the commonName.
	 * @param commonName The commonName to set
	 */
	public void setCommonName(String commonName)
	{
		this.commonName = commonName;
		this.createUsage(commonName, USAGE_NAME_COMMON);
	}

	/**
	 * Sets the usages.
	 * @param usages The usages to set
	 */
	public void setUsages(Vector usages)
	{
		this.usages = usages;
	}

	/**
	 * @return String
	 */
	public String getStatusStartDate()
	{
		return statusStartDate;
	}

	/**
	 * @return String
	 */
	public String getStatusStopDate()
	{
		return statusStopDate;
	}

	/**
	 * Sets the statusStartDate.
	 * @param statusStartDate The statusStartDate to set
	 */
	public void setStatusStartDate(String statusStartDate)
	{
		this.statusStartDate = statusStartDate;
	}

	/**
	 * Sets the statusStopDate.
	 * @param statusStopDate The statusStopDate to set
	 */
	public void setStatusStopDate(String statusStopDate)
	{
		this.statusStopDate = statusStopDate;
	}

	/**
	 * @return String
	 */
	public String getStatusComment()
	{
		return statusComment;
	}

	/**
	 * Sets the statusComment.
	 * @param statusComment The statusComment to set
	 */
	public void setStatusComment(String statusComment)
	{
		this.statusComment = statusComment;
	}

	/**
	 * @return String
	 */
	public String getUsageStartDate()
	{
		return usageStartDate;
	}

	/**
	 * Sets the usageStartDate.
	 * @param usageStartDate The usageStartDate to set
	 */
	public void setUsageStartDate(String usageStartDate)
	{
		this.usageStartDate = usageStartDate;
	}
	
	/**
	 * Convience method that sets all the References to a single reference.
	 * @param conceptReference The conceptReference to set
	 */
	public void setAllReferences(Reference reference)
	{
		this.conceptReference = reference;
		this.commonNameReference = reference;
		this.nameReference = reference;
		this.codeNameReference = reference;
	}

	/**
	 * @return Reference
	 */
	public Reference getCodeNameReference()
	{
		return codeNameReference;
	}

	/**
	 * @return Reference
	 */
	public Reference getCommonNameReference()
	{
		return commonNameReference;
	}

	/**
	 * @return Reference
	 */
	public Reference getConceptReference()
	{
		return conceptReference;
	}

	/**
	 * @return Reference
	 */
	public Reference getNameReference()
	{
		return nameReference;
	}

	/**
	 * Sets the codeNameReference.
	 * @param codeNameReference The codeNameReference to set
	 */
	public void setCodeNameReference(Reference codeNameReference)
	{
		this.codeNameReference = codeNameReference;
	}

	/**
	 * Sets the commonNameReference.
	 * @param commonNameReference The commonNameReference to set
	 */
	public void setCommonNameReference(Reference commonNameReference)
	{
		this.commonNameReference = commonNameReference;
	}

	/**
	 * Sets the conceptReference.
	 * @param conceptReference The conceptReference to set
	 */
	public void setConceptReference(Reference conceptReference)
	{
		this.conceptReference = conceptReference;
	}

	/**
	 * Sets the nameReference.
	 * @param nameReference The nameReference to set
	 */
	public void setNameReference(Reference nameReference)
	{
		this.nameReference = nameReference;
	}

}
