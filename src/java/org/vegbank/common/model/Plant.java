/*
 *	'$RCSfile: Plant.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:39:50 $'
 *	'$Revision: 1.4 $'
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
	private String statusStartDate = null;
	private String statusStopDate = null;
	private String commonName = null;
	private Vector plantUsages = new Vector();
	private Vector plantNames = new Vector();
	private Reference scientificNameReference = null;
	private Reference scientificNameNoAuthorsReference = null;
	private Reference codeNameReference = null;
	private Reference commonNameReference = null;
	private Reference conceptReference = null;	
	private String scientificNameReferenceId = null;
	private String scientificNameNoAuthorsReferenceId = null;
	private String codeNameReferenceId = null;
	private String commonNameReferenceId = null;
	private String conceptReferenceId = null;	
	private String plantDescription = null;
	
	private PlantParty plantParty = null;
	private String plantPartyId = null;
	private String statusPartyComments = null;
	
	
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
		//System.out.println("Adding a new plantUsage, now have "+this.plantUsages.size() );
		this.plantUsages.add(plantUsage);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		Plant plant = (Plant) object;
		int result = 0;
		// Sort on the rank first 
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
			// the rank is equal so accepted status is tybreaker
//			if ( this.getStatusRank() > plant.getStatusRank() )
//			{
//				result = -1;
//			}
//			else if (this.getStatusRank() < plant.getStatusRank())
//			{
//				result = 1;
//			}
//			else
//			{
//				result = 0;
//			}
			 result = 0;
		}
		return result;
	}

//	/**
//	 * @return int
//	 */
//	private int getStatusRank()
//	{
//		PlantStatus ps = new PlantStatus();
//		Vector v = (Vector) ps.getPlantConceptStatusPickList();
//		
//		// Being clever here ( a bad sign )
//		// The order of the picklist I stored in v is what I want ( currently )
//		// i.e [accepted, not accepted, undetermined]
//		// I use the counter in a loop to access the value and to return a larger 
//		// number a higher ranking status 
//		
//		for ( int i = 0 ; i < v.size() ; i++)
//		{
//			if ( this.getStatus().equals(v.get(i) ) )
//			{
//				//System.out.println("" + v.get(i) + " got a score of "  + (10 - i) );
//				return (10 - i );
//			}
//			else
//			{
//				// Ignored 
//				//System.out.println("Ignored >>" + v.get(i) + " vs. " + this.getStatus());
//			}
//		}
//		
//		return 0;
//	}

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
			// same as variety
			rank = 60;
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
	public PlantParty getPlantParty()
	{
		return plantParty;
	}

	/**
	 * Sets the party.
	 * @param party The party to set
	 */
	public void setPlantParty(PlantParty plantParty)
	{
		this.plantParty = plantParty;
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
	 * Convience method that sets all the References to a single reference.
	 * @param conceptReference The conceptReference to set
	 */
	public void setAllReferenceIds(String id)
	{
		this.conceptReferenceId = id;
		this.commonNameReferenceId =id;
		this.scientificNameReferenceId = id;
		this.codeNameReferenceId = id;
		this.scientificNameNoAuthorsReferenceId = id;
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

	/**
	 * @return
	 */
	public String getCodeNameReferenceId()
	{
		return codeNameReferenceId;
	}

	/**
	 * @return
	 */
	public String getCommonNameReferenceId()
	{
		return commonNameReferenceId;
	}


	/**
	 * @return
	 */
	public String getScientificNameNoAuthorsReferenceId()
	{
		return scientificNameNoAuthorsReferenceId;
	}

	/**
	 * @param string
	 */
	public void setCodeNameReferenceId(String string)
	{
		codeNameReferenceId = string;
	}

	/**
	 * @param string
	 */
	public void setCommonNameReferenceId(String string)
	{
		commonNameReferenceId = string;
	}


	/**
	 * @param string
	 */
	public void setScientificNameNoAuthorsReferenceId(String string)
	{
		scientificNameNoAuthorsReferenceId = string;
	}

	/**
	 * @return
	 */
	public String getScientificNameReferenceId()
	{
		return scientificNameReferenceId;
	}

	/**
	 * @param string
	 */
	public void setScientificNameReferenceId(String string)
	{
		scientificNameReferenceId = string;
	}

	/**
	 * @return
	 */
	public String getConceptReferenceId()
	{
		return conceptReferenceId;
	}

	/**
	 * @param string
	 */
	public void setConceptReferenceId(String string)
	{
		conceptReferenceId = string;
	}

	/**
	 * @return
	 */
	public String getPlantDescription()
	{
		return plantDescription;
	}

	/**
	 * @param string
	 */
	public void setPlantDescription(String string)
	{
		plantDescription = string;
	}

	/**
	 * @return
	 */
	public String getStatusPartyComments()
	{
		return statusPartyComments;
	}

	/**
	 * @param string
	 */
	public void setStatusPartyComments(String string)
	{
		statusPartyComments = string;
	}

	/**
	 * @return
	 */
	public String getPlantPartyId()
	{
		return plantPartyId;
	}

	/**
	 * @param string
	 */
	public void setPlantPartyId(String string)
	{
		plantPartyId = string;
	}

}
