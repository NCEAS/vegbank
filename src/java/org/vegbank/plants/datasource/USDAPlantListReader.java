/*
 *	'$RCSfile: USDAPlantListReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:14 $'
 *	'$Revision: 1.8 $'
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

package org.vegbank.plants.datasource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.model.*;
import org.vegbank.common.model.Reference;
import org.vegbank.common.utility.ObjectToDB;

import com.Ostermiller.util.CSVParser;

/**
 * @author farrell
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
/**
 * @author farrell
 */

public class USDAPlantListReader implements Constants, PlantListReader
{

	// The plantlist is a csv file
	//CSVParser csvp = new CSVParser();
	private String[][] plants;
	
	// To hold a the family Names
	Set  familyNames = new TreeSet();

	// Some hardcoded values
	private final String statusStartDate = "20-AUG-2002";
	
	// Reference Information
	private static final String authors = "USDA, NRCS";
	private static final String title = "The Plants Database";
	private static final String pubdate = "30-JUN-2002";
	private static final String edition = "Version 3.5";
	private static final String otherCitationDetails =
		"(http://plants.usda.gov) National Plant Data Center, Baton Rouge, LA 70874-4490 USA.";
	private static final String email = "plants@plants.usda.gov";

	// Party Information
	private static final String organization = "USDA-NRCS-PLANTS-2002";
	private static final String contactInstructions = "http://plants.usda.gov";
	
	private static Reference ref;
	private static int refId;
	{
		// Initialize the Reference
		ref = new Reference();
		ref.setEdition(edition);
		ref.setPubdate(pubdate);
		ref.setTitle(title);

		// Write the reference into the database
		ObjectToDB ref2db = new ObjectToDB(ref);
		try 
		{
			ref2db.insert();
			refId = ref2db.getPrimaryKey();
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static Referenceparty rp = new Referenceparty();
	private static int refPartyId;
	{
		// Initailize ReferenceParty
		rp.setOrganizationname(organization);
		// Write the ReferenceParty into the database
		ObjectToDB refp2db = new ObjectToDB(rp);
		try 
		{
			refp2db.insert();
			refPartyId = refp2db.getPrimaryKey();
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
	}

	private static Referencecontributor rc = new Referencecontributor();
	{
		// Initailize ReferenceContributor
		rc.setReference_id(new Integer(refId).intValue() );	
		rc.setReferenceparty_id(refPartyId);
		rc.setPosition("0");
		
		// Write the ReferenceContributor into the database
		ObjectToDB refc2db = new ObjectToDB(rc);
		try 
		{
			refc2db.insert();
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static Plantparty plantParty=new Plantparty();
	private static int plantPartyId;
	{
		// Initialize the PlantParty
		plantParty.setOrganizationname(organization);
		plantParty.setContactinstructions(contactInstructions);
		
		// Write the PlantParty into the database
		ObjectToDB pp2db = new ObjectToDB(plantParty);
		try 
		{
			pp2db.insert();
			plantPartyId = pp2db.getPrimaryKey();
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Takes the USDA standard .csv file reads it and generates vegbank datamodel objects 
	 * that can then be writtian to the database or to XML.
	 * 
	 * @param reader
	 */
	public USDAPlantListReader(Reader reader)
	{
		try
		{
			plants = CSVParser.parse(reader);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plants.datasource.PlantListReader#getAllPlants()
	 */
	public AbstractList getAllPlants()
	{
		Vector plantsV = new Vector();

		if (plants == null)
		{
			return null;
		}

		// Loop over the array of plants
		for (int i = 0; i < plants.length; i++)
		{
			if (i == 0)
			{
				// Ignore first record that I assume is the column names
				StringBuffer firstRow = new StringBuffer();
				for (int ii=0; ii<4; ii++ )
				{
					firstRow.append(plants[i][ii]);
				}
				System.out.println("Ignoring the first row of column names: "  + firstRow );
			}
			else
			{
				// Get Plant
				plantsV.add(this.getPlant(plants[i]));
			}
		}
		// Need to add in the Families
		plantsV.addAll(this.getAllFamillies());
		return plantsV;
	}

	/**
	 * @return
	 */
	private Collection getAllFamillies()
	{
		Vector results = new Vector();
				
		Iterator famillies = familyNames.iterator();
		while ( famillies.hasNext() )
		{
			// Always the same for families
			Plant plant = new Plant();
			plant.setStatusStartDate(statusStartDate);
			plant.setAllReferenceIds( new Integer(refId).toString() );
			plant.setPlantPartyId( new Integer(plantPartyId).toString());
			plant.setClassLevel(PLANT_CLASS_FAMILY);
			plant.setStatus(CONCEPT_STATUS_ACCEPTED);
			
			String family = (String) famillies.next();			
			plant.setScientificNameNoAuthors(family);
			
			results.add(plant);
		}

		return results;
	}

	private Plant getPlant(String[] singlePlant)
	{
		Plant plant = new Plant();

		plant.setStatusStartDate(statusStartDate);
		plant.setAllReferenceIds( new Integer(refId).toString() );
		plant.setPlantPartyId( new Integer(plantPartyId).toString());
		/* Examples:
		 * code, name, common name, genus
		*"ABMO3","Abama montana Small ","=Narthecium americanum","Liliaceae"
		*"ABGR4","Abelia xgrandiflora (André) Rehd. ","glossy abelia","Caprifoliaceae"
		*/

		// Fourth field is the family name .... 
		String familyName = singlePlant[3];

		// First field always Code
		plant.setCode(singlePlant[0]);

		// Second Entry needs parsing to get at all its  information
		plant.setClassLevel(
			getClassificationLevel(plant.getCode(), singlePlant[1]));
		this.parseNameField(plant, singlePlant[1], familyName);


		// Third Field can be the common name or the accepted synonym
		// Synonym is not used at present
		this.parseOtherNameField(plant, singlePlant[2]);


		// Add name to a list with no duplicates
		familyNames.add(familyName);

		return plant;
	}

	/**
	 * @param plant
	 * @param string
	 */
	private void parseOtherNameField(Plant plant, String otherNameField)
	{
		// This ethier the common name or the accepted synomyn
		if ( otherNameField.startsWith("=")   )
		{
			// We have an unaccepted plant with an accepted synomyn 
			plant.setStatus(CONCEPT_STATUS_NOT_ACCEPTED);
			plant.setSynonymName(otherNameField.substring(1));
		}
		else if ( otherNameField.startsWith(">>") )
		{
			// We have an unaccepted plant with an accepted synomyn 
			plant.setStatus(CONCEPT_STATUS_NOT_ACCEPTED);
			plant.setSynonymName(otherNameField.substring(2));
		}
		else
		{
			// This is the common name of an accepted plant
			plant.setStatus(CONCEPT_STATUS_ACCEPTED);
			plant.setCommonName(otherNameField);
		}

	}

	private void parseNameField(Plant plant, String nameField, String familyName)
	{
		// A few rules here
		// Abelia xgrandiflora (André) Rehd.
		// scientific without authors -> Abelia xgrandiflora  
		// authors -> (André) Rehd.
		// scientific -> Abelia xgrandiflora (André) Rehd.

		// This all depends on if it is a species, variety or sub-species		

		if (nameField.indexOf(" var. ") > 0)
		{
			// I am a variety
			this.parseNameField(plant, nameField, "var.", familyName);
		}
		else
			if (nameField.indexOf(" ssp. ") > 0)
			{
				// I am a sub-species
				this.parseNameField(plant, nameField, "ssp.",  familyName);
			}
			else
			{
				// I must be a species / genus  or hybrid
				this.parseNameField(plant, nameField, null, familyName);
			}

		// Get the first two tokens in the name field.
		//scientificNameNoAuthors = this.spaceStringTokenizer( nameField.trim(), 1 )
		//	+ " " +  this.spaceStringTokenizer( nameField.trim(), 2 );
		//plant.setPlantName("");

	}

	private void parseNameField(
		Plant plant,
		String nameField,
		String classIndicator,
		String familyName)
	{
		String scientificName = nameField.trim();
		String scientificNameNoAuthors = "";
		String author = "";
		String parentName = "";

		if (classIndicator == null || classIndicator.equals(""))
		{
			String classLevel = plant.getClassLevel();
			if (classLevel.equals(PLANT_CLASS_GENUS))
			{
				//has no parent in our system currently -- could use family
				parentName = familyName;
				// The first token
				scientificNameNoAuthors = this.getToken(nameField, 1);
			}
			else if (classLevel.equals(PLANT_CLASS_SPECIES) // A Species 
					|| classLevel.equals(PLANT_CLASS_HYBRID)) // or a Hybrid
			{
				// parentName is the first token
				parentName = getToken(nameField, 1);
				// Get the first two tokens in the name field.
				scientificNameNoAuthors = this.getTokenRange(nameField, 1, 2);
			}
			else
			{
				// TODO: Throw an exception or something
				System.out.println("I never expect to get here !!!");
			}

			// author is token 3 onward
			author =
				getTokenRange(
					nameField,
					3,
					new StringTokenizer(nameField).countTokens());

		}
		else  // we got ssp. or var. in the field
		{
			// split the string into two on the classIndicator string
			String before = nameField.substring(0, nameField.indexOf(classIndicator));
			String after =
				nameField.substring(
					nameField.indexOf(classIndicator) + classIndicator.length());

			// Remove authors 
			parentName = getTokenRange(before, 1, 2);
			String subName = getTokenRange(after, 1, 1);
			author =
				getTokenRange(after, 2, new StringTokenizer(after).countTokens());

			scientificNameNoAuthors =
				parentName + " " + classIndicator + " " + subName;
			// The before string the same as the species unsplit string
			//this.parseNameField(plant, before, null );	
		}
		
		// Set the plant values
		plant.setScientificName(scientificName);
		plant.setScientificNameNoAuthors(scientificNameNoAuthors);
		plant.setParentName(parentName);
		plant.setAuthor(author);
	}

	private String getTokenRange(String s, int firstPos, int lastPos)
	{
		StringBuffer result = new StringBuffer();

		for (int i = firstPos; i <= lastPos; i++)
		{
			result.append(this.getToken(s, i));
			result.append(" "); // Add in the space again
		}
		return (result.toString().trim());
	}

	/**
	 * method to tokenize elements from a space delimeted string
	 */
	private String getToken(String pipeString, int tokenPosition)
	{
		String token = "nullToken";
		if (pipeString != null)
		{
			StringTokenizer t = new StringTokenizer(pipeString.trim(), " ");
			int i = 1;
			while (i <= tokenPosition)
			{
				if (t.hasMoreTokens())
				{
					token = t.nextToken().replace('"', ' ').trim();
					i++;
				}
				else
				{
					token = "nullToken";
					i++;
				}
			}
			return (token);
		}
		else
		{
			return (token);
		}
	}

	/**
	 * Works out the classification level of the plant using some rules.
	 * VARIETY and SUBSPECIES have a simple rule, does the nameField contain a 
	 * ssp. or var. String.<br/>
	 * 
	 * <pre>
	 * 	"AGGLD","Agoseris glauca (Pursh) Raf. var. dasycephala (Torr. & Gray) Jepson ","pale agoseris","Asteraceae"
	 * 	"AGHEN","Agoseris heterophylla (Nutt.) Greene ssp. normalis Piper ",">>Agoseris heterophylla var. heterophylla","Asteraceae"
	 * </pre>
	 * 
	 * GENUS is more complicated. 
	 * <ul>
	 * 	<li>Is the name field only two tokens long e.g. "this that"</li>
	 * 	<li>Is the name fields second token capitalized e.g. "this That authors</li>
	 * 	<li>Is the code at least 5 characters long with the fifth char not a digit</li>
	 * </ul>
	 * <br/>
	 * HYBRID is when either the first of second token of the name field begins 
	 * with the '×' character (ASCII Code 215)
	 * <br/>
	 * SPECIES is everything else.
	 * 
	 * 
	 * 
	 * @param codeField
	 * @param nameField
	 * @return String  --  the classification level of the plant
	 */
	private String getClassificationLevel(String codeField, String nameField)
	{
		String classification = "";

		if (nameField.indexOf(" var. ") > 0)
		{
			// I am a variety
			//this.parseNameField(plant, nameField, "var.");
			classification = PLANT_CLASS_VARIETY;
		}
		else
			if (nameField.indexOf(" ssp. ") > 0)
			{
				// I am a sub-species
				//this.parseNameField(plant, nameField, "ssp.");
				classification = PLANT_CLASS_SUBSPECIES;
			}
			else
			{
				// Could be a genus, species or hybrid

				StringTokenizer t = new StringTokenizer(nameField);
				int tokensCount = t.countTokens();

				if (tokensCount <= 2) // String has two words or less
				{
					//System.out.println("c -->" + tokensCount );
					classification = PLANT_CLASS_GENUS;
				}
				// the Code has 5 or more chars and  fifth char is not a digit ABCDE2343
				else
					if (codeField.length() >= 5
						&& !Character.isDigit(codeField.charAt(4)))
					{
						//System.out.println("Char 5 Non digit -->"  +  codeField.charAt(4) + " from " + codeField );
						classification = PLANT_CLASS_GENUS;
					}
				// the second word is capitalized e.g. "this That"
				else
					if (Character.isUpperCase(getToken(nameField, 2).charAt(0)))
					{
						//System.out.println("LC -->" + Character.isLowerCase( getToken(fieldTwo, 2).charAt(0) ) );
						classification = PLANT_CLASS_GENUS;
					}
				else
					if (isHybrid(nameField))
					{
						classification = PLANT_CLASS_HYBRID;
					}
					else
					{
						classification = PLANT_CLASS_SPECIES;
					}
			}

		return classification;
	}

	private boolean isHybrid(String nameField)
	{
		boolean isHybrid = false;
		String nameFirst =  getToken(nameField, 1);
		String nameSecond = getToken(nameField, 2);
		
		// first letter of either of the first two tokens is '×' ASCII Code 215 ( not the letter x) 
		if (nameFirst.startsWith("×") || nameSecond.startsWith("×") )
		{
			isHybrid = true;
		}
		// also check the letter lowwercase x
		if (nameFirst.startsWith("x") || nameSecond.startsWith("x") )
		{
			isHybrid = true;
		}
		
		return isHybrid;
	}

	public static void main(String[] args)
	{
		String filename = args[0];

		try
		{
			PlantListReader plr = new USDAPlantListReader(new FileReader(filename));

			AbstractList list = plr.getAllPlants();

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
