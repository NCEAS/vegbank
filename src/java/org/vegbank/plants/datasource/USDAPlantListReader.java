/*
 *	'$RCSfile: USDAPlantListReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-07 22:17:55 $'
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

package org.vegbank.plants.datasource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.model.*;
import org.vegbank.common.model.Reference;

import com.Ostermiller.util.CSVParser;

/**
 * @author farrell
 */

public class USDAPlantListReader implements Constants, PlantListReader
{

	// The plantlist is a csv file
	//CSVParser csvp = new CSVParser();
	private String[][] plants;

	// Some hardcoded values
	private final String statusStartDate = "20-AUG-2002";
	
	// Reference Information
	private static final String authors = "USDA, NRCS";
	private static final String title = "The Plants Database";
	private static final String pubdate = "30-JUN-2002";
	private static final String edition = "Version 3.5";
	private static final String otherCitationDetails =
		"(http://plants.usda.gov) National Plant Data Center, Baton Rouge, LA 70874-4490 USA.";
	private static final String organization = "USDA-NRCS-PLANTS-2002";
	private static final String email = "plants@plants.usda.gov";
	private static final String contactInstructions = "http://plants.usda.gov";
	
	private static Reference ref = null;

	public USDAPlantListReader(Reader reader)
	{
		// Initialize the refence if needed
		if (ref == null)
		{
			USDAPlantListReader.initializeReference();
		}
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
			// Get Plant
			plantsV.add(this.getPlant(plants[i]));
		}
		return plantsV;
	}

	private Plant getPlant(String[] singlePlant)
	{
		Plant plant = new Plant();

		plant.setStatusStartDate(statusStartDate);
		plant.setAllReferences(ref);

		/* Examples:
		 * code, name, common name, genus
		*"ABMO3","Abama montana Small ","=Narthecium americanum","Liliaceae"
		*"ABGR4","Abelia xgrandiflora (André) Rehd. ","glossy abelia","Caprifoliaceae"
		*/

		// First field always Code
		plant.setCode(singlePlant[0]);

		// Second Entry needs parsing to get at all its  information
		plant.setClassLevel(
			getClassificationLevel(plant.getCode(), singlePlant[1]));
		this.parseNameField(plant, singlePlant[1]);

		// Third Field can be the common name or the accepted synonym
		// Synonym is not used at present
		this.parseOtherNameField(plant, singlePlant[2]);

		// Fourth field is the family name .... it is not used at present 

		//plant.setParentName( getParentName(singlePlant[1], plant) );

		return plant;
	}

	/**
	 * @param String -- nameField
	 * @param Plant
	 * @return String
	 */
	private String getParentName(String nameField, Plant plant)
	{
		String parentName = null;

		if (plant.getClassLevel().equals(PLANT_CLASS_SUBSPECIES))
		{
			parentName = "";
		}
		else
			if (plant.getClassLevel().equals(PLANT_CLASS_VARIETY))
			{

			}

		return parentName;
	}

	/**
	 * @param plant
	 * @param string
	 */
	private void parseOtherNameField(Plant plant, String otherNameField)
	{
		// This ethier the common name or the accepted synomyn
		if (otherNameField.startsWith("="))
		{
			// We have an unaccepted plant with an accepted synomyn 
			plant.setStatus("not accepted");
			plant.setSynonymName(otherNameField.substring(1));
		}
		else
		{
			// This is the common name of an accepted plant
			plant.setStatus("accepted");
			plant.setCommonName(otherNameField);
		}

	}

	private void parseNameField(Plant plant, String nameField)
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
			this.parseNameField(plant, nameField, "var.");
		}
		else
			if (nameField.indexOf(" ssp. ") > 0)
			{
				// I am a sub-species
				this.parseNameField(plant, nameField, "ssp.");
			}
			else
			{
				// I must be a species / genus  or hybrid
				this.parseNameField(plant, nameField, null);
			}

		// Get the first two tokens in the name field.
		//scientificNameNoAuthors = this.spaceStringTokenizer( nameField.trim(), 1 )
		//	+ " " +  this.spaceStringTokenizer( nameField.trim(), 2 );
		//plant.setPlantName("");

	}

	private void parseNameField(
		Plant plant,
		String nameField,
		String classIndicator)
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
				parentName = "";
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

				if (tokensCount == 2) // String has two word 
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
				// first letter of either of the first two tokens is '×' ASCII Code 215 ( not the letter x) 
				else
					if (getToken(nameField, 1).startsWith("×")
						|| getToken(nameField, 2).startsWith("×"))
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
	
	private static void initializeReference()
	{
			// Initialize the Reference
			ref = new Reference();
			ref.setAuthors(authors);
			ref.setContactInstructions(contactInstructions);
			ref.setEdition(edition);
			ref.setEmail(email);
			ref.setOrganization(organization);
			ref.setOtherCitationDetails(otherCitationDetails);
			ref.setPubdate(pubdate);
			ref.setTitle(title);
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
