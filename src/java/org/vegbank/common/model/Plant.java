/*
 *	'$RCSfile: Plant.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:26 $'
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
 
package org.vegbank.common.model;

import java.util.AbstractList;
import java.util.Vector;

import org.vegbank.common.Constants;

/**
 * @author farrell
 */

public class Plant implements Constants, Comparable
{

	private String code = null;
	private String scientificName = null;
	private String scientificNameNoAuthors = null;
	private String parentName = null;
	private String author = null;
	private String familyName = null;
	private String status = null;
	private String classLevel = null;
	private String synonymName = null;
	private String description = null;
	private String statusStartDate = null;
	private String commonName = null;
	private Vector plantUsages = new Vector();
	private Vector plantNames = new Vector();
	private Reference scientificNameReference = null;
	private Reference scientificNameNoAuthorsReference = null;
	private Reference codeNameReference = null;
	private Reference commonNameReference = null;
	private Reference conceptReference = null;	
	private Party party = null;
	private String statusStopDate = null;
		
	//private String  = "";
	
	public void setPlantName( String plantName)
	{
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
	public String getScientificName()
	{
		return scientificName;
	}

	/**
	 * @return String
	 */
	public String getScientificNameNoAuthors()
	{
		return scientificNameNoAuthors;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
		PlantUsage pu = new PlantUsage();
		pu.setPlantName(code);
		pu.setPlantNameStatus(USAGE_NAME_STATUS_STANDARD);
		pu.setClassSystem(USAGE_NAME_CODE);	
		this.addPlantUsage(pu);
	}

	/**
	 * Sets the scientificName.
	 * @param scientificName The scientificName to set
	 */
	public void setScientificName(String scientificName)
	{
		this.scientificName = scientificName;
		PlantUsage pu = new PlantUsage();
		pu.setPlantName(scientificName);
		pu.setPlantNameStatus(USAGE_NAME_STATUS_STANDARD);
		pu.setClassSystem(USAGE_NAME_SCIENTIFIC);	
		this.addPlantUsage(pu);
	}

	/**
	 * Sets the scientificNameNoAuthors.
	 * @param scientificNameNoAuthors The scientificNameNoAuthors to set
	 */
	public void setScientificNameNoAuthors(String scientificNameNoAuthors)
	{
		this.scientificNameNoAuthors = scientificNameNoAuthors;
		PlantUsage pu = new PlantUsage();
		pu.setPlantName(scientificNameNoAuthors);
		pu.setPlantNameStatus(USAGE_NAME_STATUS_STANDARD);
		pu.setClassSystem(USAGE_NAME_SCIENTIFIC_NOAUTHORS);	
		this.addPlantUsage(pu);
	}

	/**
	 * @return String
	 */
	public String getAuthor()
	{
		return author;
	}

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
	public String getFamilyName()
	{
		return familyName;
	}

	/**
	 * @return String
	 */
	public String getParentName()
	{
		return parentName;
	}

	/**
	 * @return String
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @return String
	 */
	public String getSynonymName()
	{
		return synonymName;
	}

	/**
	 * Sets the author.
	 * @param author The author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
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
	 * Sets the familyName.
	 * @param familyName The familyName to set
	 */
	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
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
	 * Sets the synonymName.
	 * @param synonymName The synonymName to set
	 */
	public void setSynonymName(String synonymName)
	{
		this.synonymName = synonymName;
	}



	/**
	 * @return String
	 */
	public String getDescription()
	{
		return description;
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
	 * @return String
	 */
	public String getStatusStartDate()
	{
		return statusStartDate;
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
	 * @return String
	 */
	public String getCommonName()
	{
		return commonName;
	}

	/**
	 * Sets the commonName.
	 * @param commonName The commonName to set
	 */
	public void setCommonName(String commonName)
	{
		this.commonName = commonName;

		PlantName pn = new PlantName();
		pn.setPlantName(commonName);
		this.addPlantName(pn);

		PlantUsage pu = new PlantUsage();
		pu.setPlantName(commonName);
		pu.setPlantNameStatus(USAGE_NAME_STATUS_STANDARD);
		pu.setClassSystem(USAGE_NAME_COMMON);
		this.addPlantUsage(pu);
		

	}



	/**
	 * @return AbstractList PlantUsage 
	 */
	public AbstractList getPlantUsages()
	{
		return plantUsages;
	}

	/**
	 * Sets the plantUsages.
	 * @param plantUsages The plantUsages to set
	 */
	public void setPlantUsages(Vector  plantUsages)
	{
		this.plantUsages = plantUsages;
	}

	/**
	 * Adds a plantUsage to the  plantUsages array.
	 * @param plantUsages The plantUsages to set
	 */
	public void addPlantUsage(PlantUsage plantUsage)
	{
		this.plantUsages.add(plantUsage);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		Plant plant = (Plant) object;
		int result = 0;
		if (this.getRank() > plant.getRank())
		{
			result = -1;
		}
		else if ( this.getRank() < plant.getRank() )
		{
			result = 1;
		}
		else
		{
			result = 0;
		}
		return result;
	}

	/**
	 * @return int
	 */
	private int getRank()
	{
		int rank;
		// Natural order is:
		// Genus, Species, SubSpecies, Variety, Hybrid
		if (this.classLevel.equals(PLANT_CLASS_GENUS) )
		{
			rank = 100;
		}
		else if (this.classLevel.equals(PLANT_CLASS_SPECIES) )
		{
			rank = 90;
		}		
		else if (this.classLevel.equals(PLANT_CLASS_SUBSPECIES) )
		{
			rank = 80;
		}
		else if (this.classLevel.equals(PLANT_CLASS_VARIETY) )
		{
			rank = 60;
		}
		else if (this.classLevel.equals(PLANT_CLASS_HYBRID) )
		{
			rank = 50;
		}
		else
		{
			rank = 1;  
			System.out.println("Cannot get here !!");
		}

		return rank;
	}

	/**
	 * @return Reference
	 */
	public Reference getScientificNameReference()
	{
		return scientificNameReference;
	}

	/**
	 * Sets the ref.
	 * @param ref The ref to set
	 */
	public void setScientificNameReference(Reference ref)
	{
		this.scientificNameReference = ref;
	}

	/**
	 * @return Party
	 */
	public Party getParty()
	{
		return party;
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
	public Reference getScientificNameNoAuthorsReference()
	{
		return scientificNameNoAuthorsReference;
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
	 * Sets the scientificNameNoAuthorsReference.
	 * @param scientificNameNoAuthorsReference The scientificNameNoAuthorsReference to set
	 */
	public void setScientificNameNoAuthorsReference(Reference scientificNameNoAuthorsReference)
	{
		this.scientificNameNoAuthorsReference =
			scientificNameNoAuthorsReference;
	}

	/**
	 * @return Reference
	 */
	public Reference getConceptReference()
	{
		return conceptReference;
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
	 * Convience method that sets all the References to a single reference.
	 * @param conceptReference The conceptReference to set
	 */
	public void setAllReferences(Reference reference)
	{
		this.conceptReference = reference;
		this.commonNameReference = reference;
		this.scientificNameNoAuthorsReference = reference;
		this.codeNameReference = reference;
	}

	/**
	 * @return String
	 */
	public String getStatusStopDate()
	{
		return statusStopDate;
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
	 * @return
	 */
	public AbstractList getPlantNames()
	{
		return plantNames;
	}

	/**
	 * @param vector
	 */
	public void setPlantNames(AbstractList plantNameList)
	{
		plantNames = (Vector) plantNameList;
	}
	
	public void addPlantName(PlantName plantName)
	{
		plantNames.add(plantName);
	}

}
