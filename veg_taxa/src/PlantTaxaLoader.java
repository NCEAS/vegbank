/**
 *  '$RCSfile: PlantTaxaLoader.java,v $'
 *   '$Author: harris $'
 *     '$Date: 2002-08-12 19:01:46 $'
 * '$Revision: 1.15 $'
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

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public class PlantTaxaLoader 
{
	private Connection conn = null;
	private String salutation;
	private String givenName;
	private String surName;
	private String orgName;
	private String email;
	private String longName;
	private int longNameId;
	private String shortName;
	private int shortNameId;
	private String code;
	private int codeId;
	private String taxonDescription;
	private String citationDetails;
	private String dateEntered;
	private String usageStopDate;
	private String rank;
	private boolean commit;	
	private Vector usageVec = new Vector();
	private String	longNameRefAuthors = "";
	private String	longNameRefTitle = "";
	private String	longNameRefDate  = "";
	private String	longNameRefEdition = "";
	private String	longNameRefSeriesName = "";
	private String	longNameRefVolume = "";
	private String	longNameRefPage = "";
	private String	longNameRefISSN = "";
	private String	longNameRefISBN = "";
	private String	longNameRefOtherCitDetails = "";
	 		
	private String	shortNameRefAuthors = "";
	private String	shortNameRefTitle = "";
	private String	shortNameRefDate = "";
	private String	shortNameRefEdition = "";
	private String	shortNameRefSeriesName = "";
	private String	shortNameRefVolume = "";
	private String	shortNameRefPage = "";
	private String	shortNameRefISSN  = "";
	private String	shortNameRefISBN  = "";
	private String	shortNameRefOtherCitDetails = "";
	 		
	private String	codeRefAuthors = "";
	private String	codeRefTitle = "";
	private String	codeRefDate = "";
	private String	codeRefEdition = "";
	private String	codeRefSeriesName = "";
	private String	codeRefVolume = "";
	private String	codeRefPage = "";
	private String	codeRefISSN = "";
	private String	codeRefISBN = "";
	private String	codeRefOtherCitDetails = "";
	 
	private String	conceptDescription = "";
	private String	conceptRefAuthors = "";
	private String	conceptRefTitle = "";
	private String	conceptRefDate = "";
	private String	conceptRefEdition = "";
	private String	conceptRefSeriesName = "";
	private String	conceptRefVolume = "";
	private String	conceptRefPage = "";
	private String	conceptRefISSN = "";
	private String	conceptRefISBN = "";
	private String	conceptRefOtherCitDetails = "";
	 	
	private String	conceptStatus = "";
	private String	statusStartDate = "";
	private String	statusStopDate = "";
	private String	statusDescription = "";
	private String	taxonLevel =  "";
	private String	plantParentName = "";
	private String	plantParentRefTitle =  "";
	private String	plantParentRefAuthors = "";
	
	
	/**
	 * this method will take a hashtable that represents a taxon (see below)
	 * and insert the elements of the hashtable into the database and will 
	 * return an xml receipt for the loading process.  This method is intended 
	 * to be a 'generic' method that may be called by a number of loaders to 
	 * be used for loading custom datasets
	 * the incomming hashatbale should contain the following keys:
	 * longName
	 * shortName
	 * code
	 * taxonDescription
	 * salutation
	 * givenName
	 * surName
	 * orgName
	 * email
	 * citationDetails
	 * dateEntered
	 * usageStopDate
	 * rank
	 *
	 *  givenName
	 *	surName
	 *	institution
	 *		
	 *	longName
	 *	shortName
	 *	code
	 *		
	 *	longNameRefAuthors
	 *	longNameRefTitle
	 *	longNameRefDate 
	 *	longNameRefEdition 
	 *	longNameRefSeriesName
	 *	longNameRefVolume
	 *	longNameRefPage
	 *	longNameRefISSN
	 *	longNameRefISBN
	 *	longNameRefOtherCitDetails
	 *	
	 *	shortNameRefAuthors
	 *	shortNameRefTitle
	 *	shortNameRefDate
	 *	shortNameRefEdition
	 *	shortNameRefSeriesName
	 *	shortNameRefVolume
	 *	shortNameRefPage
	 *	shortNameRefISSN
	 *	shortNameRefISBN
	 *	shortNameRefOtherCitDetails
	 *	
	 *	codeRefAuthors
	 *	codeRefTitle
	 *	codeRefDate
	 *	codeRefEdition
	 *	codeRefSeriesName
	 *	codeRefVolume
	 *	codeRefPage
	 *	codeRefISSN
	 *	codeRefISBN 
	 *	codeRefOtherCitDetails
	 *
	 *	conceptDescription
	 *	conceptRefAuthors
	 *	conceptRefTitle
	 *	conceptRefDate
	 *	conceptRefEdition
	 *	conceptRefSeriesName
	 *	conceptRefVolume
	 *	conceptRefPage
	 *	conceptRefISSN
	 *	conceptRefISBN
	 *	conceptRefOtherCitDetails
	 *
	 *	conceptStatus
	 *	statusStartDate
	 *	statusStopDate
	 *	statusDescription
	 *	taxonLevel
	 *	plantParentName
	 *	plantParentRefTitle
	 *	plantParentRefAuthors
	 *
	 */
	public boolean loadGenericPlantTaxa(Hashtable plantTaxon)
	{
		try
		{
			// DETERMINE IF THE INPUT HASHTABLE HAS THE CORRECT ELEMENTS
			if ( this.isValidPlantHash(plantTaxon) == true )
			{
				//set the commit boolean to true, which can be changed by any of the 
				// methods called from this one if an exception is thrown
				commit = true;
				//get the connection
				conn = this.getConnection();
				conn.setAutoCommit(false);
				
				//get the parameters from the hashtable
				salutation = (String)plantTaxon.get("salutation");
				givenName = (String)plantTaxon.get("givenName");
				surName = (String)plantTaxon.get("surName");
				email = (String)plantTaxon.get("email");
				longName = (String)plantTaxon.get("longName");
				shortName = (String)plantTaxon.get("shortName");
				code = (String)plantTaxon.get("code");
				
				longNameRefAuthors = (String)plantTaxon.get("longNameRefAuthors");
	 			longNameRefTitle = (String)plantTaxon.get("longNameRefTitle");
	 			longNameRefDate  =  (String)plantTaxon.get("longNameRefDate");
	 			longNameRefEdition =  (String)plantTaxon.get("longNameRefEdition");
	 			longNameRefSeriesName =  (String)plantTaxon.get("longNameRefSeriesName");
	 			longNameRefVolume =  (String)plantTaxon.get("longNameRefVolume");
	 			longNameRefPage =  (String)plantTaxon.get("longNameRefPage");
	 			longNameRefISSN =  (String)plantTaxon.get("longNameRefISSN");
	 			longNameRefISBN =  (String)plantTaxon.get("longNameRefISBN");
	 			longNameRefOtherCitDetails =  (String)plantTaxon.get("longNameRefOtherCitDetails");
	 		
	 			shortNameRefAuthors =  (String)plantTaxon.get("shortNameRefAuthors");
	 			shortNameRefTitle =  (String)plantTaxon.get("shortNameRefTitle");
	 			shortNameRefDate =  (String)plantTaxon.get("shortNameRefDate");
	 			shortNameRefEdition =  (String)plantTaxon.get("shortNameRefEdition");
	 			shortNameRefSeriesName =  (String)plantTaxon.get("shortNameRefSeriesName");
	 			shortNameRefVolume =  (String)plantTaxon.get("shortNameRefVolume");
	 			shortNameRefPage =  (String)plantTaxon.get("shortNameRefPage");
	 			shortNameRefISSN  =  (String)plantTaxon.get("shortNameRefISSN");
	 			shortNameRefISBN  =  (String)plantTaxon.get("shortNameRefISBN");
	 			shortNameRefOtherCitDetails =  (String)plantTaxon.get("shortNameRefOtherCitDetails");
	 		
	 			codeRefAuthors =  (String)plantTaxon.get("codeRefAuthors");
	 			codeRefTitle =  (String)plantTaxon.get("codeRefTitle");
	 			codeRefDate =  (String)plantTaxon.get("codeRefDate");
	 			codeRefEdition =  (String)plantTaxon.get("codeRefEdition");
	 			codeRefSeriesName =  (String)plantTaxon.get("codeRefSeriesName");
	 			codeRefVolume =  (String)plantTaxon.get("codeRefVolume");
	 			codeRefPage =  (String)plantTaxon.get("codeRefPage");
	 			codeRefISSN =  (String)plantTaxon.get("codeRefISSN");
	 			codeRefISBN =  (String)plantTaxon.get("codeRefISBN");
	 			codeRefOtherCitDetails =  (String)plantTaxon.get("codeRefOtherCitDetails");
	 
	 			conceptDescription =  (String)plantTaxon.get("conceptDescription");
	 			conceptRefAuthors =  (String)plantTaxon.get("conceptRefAuthors");
	 			conceptRefTitle =  (String)plantTaxon.get("conceptRefTitle");
	 			conceptRefDate =  (String)plantTaxon.get("conceptRefDate");
	 			conceptRefEdition =  (String)plantTaxon.get("conceptRefEdition");
	 			conceptRefSeriesName =  (String)plantTaxon.get("conceptRefSeriesName");
	 			conceptRefVolume =  (String)plantTaxon.get("conceptRefVolume");
	 			conceptRefPage =  (String)plantTaxon.get("conceptRefPage");
	 			conceptRefISSN =  (String)plantTaxon.get("conceptRefISSN");
	 			conceptRefISBN =  (String)plantTaxon.get("conceptRefISBN");
	 			conceptRefOtherCitDetails =  (String)plantTaxon.get("conceptRefOtherCitDetails");
	 	
	 			conceptStatus =  (String)plantTaxon.get("conceptStatus");
	 			statusStartDate =  (String)plantTaxon.get("statusStartDate");
	 			statusStopDate =  (String)plantTaxon.get("statusStopDate");
	 			statusDescription =  (String)plantTaxon.get("statusDescription");
	 			taxonLevel =  (String)plantTaxon.get("taxonLevel");
	 			plantParentName =  (String)plantTaxon.get("plantParentName");
	 			plantParentRefTitle =  (String)plantTaxon.get("plantParentRefTitle");
	 			plantParentRefAuthors =  (String)plantTaxon.get("plantParentRefAuthors");	
				
				// REMOVE THESE OLD ATTRIBUTES 
				orgName = (String)plantTaxon.get("orgName");
				taxonDescription = (String)plantTaxon.get("taxonDescription");
				citationDetails = (String)plantTaxon.get("citationDetails");
				dateEntered = (String)plantTaxon.get("dateEntered");
				usageStopDate = (String)plantTaxon.get("usageStopDate");
				rank = (String)plantTaxon.get("rank");
				
			
				// INSERT THE PLANT NAME / CONCEPT REFERENCES AND THE PARTY INFORMATION
				int refId = this.insertPlantReference(longNameRefAuthors, longNameRefTitle, longNameRefDate, 
					longNameRefEdition, longNameRefSeriesName, longNameRefOtherCitDetails, longNameRefPage, 
					longNameRefISBN, longNameRefISSN, conceptDescription);
				int shortNameRefId = this.insertPlantReference(shortNameRefAuthors, shortNameRefTitle, shortNameRefDate, 
					shortNameRefEdition, shortNameRefSeriesName, shortNameRefOtherCitDetails, shortNameRefPage, 
					shortNameRefISBN, shortNameRefISSN, conceptDescription);
				int codeRefId = this.insertPlantReference(codeRefAuthors, codeRefTitle, codeRefDate, 
					codeRefEdition, codeRefSeriesName, codeRefOtherCitDetails, codeRefPage, 
					codeRefISBN, codeRefISSN, conceptDescription);
				int conceptRefId = this.insertPlantReference(conceptRefAuthors, conceptRefTitle, conceptRefDate, 
					conceptRefEdition, conceptRefSeriesName, conceptRefOtherCitDetails, conceptRefPage, 
					conceptRefISBN, conceptRefISSN, conceptDescription);
				
				int partyId = this.insertPlantPartyInstance(salutation, givenName, surName, orgName, email);
			
				// INSERT THE PLANT NAME ATTRIBUTES
				boolean longNameExist;
				boolean shortNameExist;
				boolean codeExist;
				// THE LONG NAME
				if ( longName.length() < 1 )
				{
					System.out.println("long name is short");
					longNameExist = false;
				}
				else
				{
					longNameExist = this.plantNameExists(longName);
					if ( longNameExist == true )
					{
						longNameId = getPlantNameId(longName);
						System.out.println("PlantTaxaLoader > longNameId: " + longNameId );
					}
					else
					{
						longNameId = loadPlantNameInstance(refId, longName, "", dateEntered);
					}
				}
				// THE SHORT NAME
				if ( shortName.length() < 1 )
				{
					System.out.println("short name is short");
					 shortNameExist = false;
				}
				else
				{
					shortNameExist = this.plantNameExists(shortName);
					if ( shortNameExist == true )
					{
						shortNameId = getPlantNameId(shortName);
						System.out.println("PlantTaxaLoader > shortNameId: " + shortNameId );
					}
					else
					{
						shortNameId = loadPlantNameInstance(shortNameRefId, shortName, "", dateEntered);
					}
				}
				// THE CODE NAME
				if ( code.length() < 1 )
				{
					System.out.println("code name is short");
					 codeExist = false;
				}
				else
				{
					codeExist = this.plantNameExists(code);
					if ( codeExist == true )
					{
						codeId = getPlantNameId(code);
						System.out.println("PlantTaxaLoader > codeId: " + codeId );
					}
					else
					{
						codeId = loadPlantNameInstance(codeRefId, code, "", dateEntered);
					}
				}
				
				
				//LOAD THE CONCEPT -- USE THE REFERENCE ASSOCIATED WITH THE CONCEPT
				int conceptId;
				int statusId;
				if (plantConceptExists(longNameId, conceptRefId, conceptDescription) == false)
				{
					System.out.println("PlantTaxaLoader > concept does not exist -- adding it");
					conceptId = loadPlantConceptInstance(longNameId, conceptRefId, conceptDescription, 
					code, taxonLevel, longName );
				}
				else
				{
					System.out.println("PlantTaxaLoader > concept previously exists");
					conceptId = getPlantConceptId(longNameId, conceptRefId, conceptDescription, 
					code, taxonLevel, longName );
				}
				System.out.println("PlantTaxaLoader > conceptId: " + conceptId);
				
				
				// LOAD THE STATUS TABLE
				System.out.println("PlantTaxaLoader > status start / stop " + statusStartDate + " " + statusStopDate);
				if ( plantStatusExists(conceptId, conceptStatus, statusStartDate, statusStopDate, 
					email, partyId, plantParentName, refId) == false )
				{
					System.out.println("PlantTaxaLoader > status does not exist ");
					statusId = loadPlantStatusInstance(conceptId, conceptStatus, statusStartDate, 
					statusStopDate, email, partyId, plantParentName, refId);
				}
				
				// LOAD THE USAGE TABLES
				//check to see if there are already usage id's in the database
				if ( plantUsageExists(longName, longNameId, conceptId, "STANDARD", 
					dateEntered, usageStopDate, "LONGNAME", partyId) == false )
				{
					int uid = loadPlantUsageInstance(longName, longNameId, conceptId, "STANDARD", 
					dateEntered, usageStopDate, "LONGNAME", partyId);
					System.out.println("PlantTaxonomyLoader > uid: " + uid);
					usageVec.addElement(""+uid);
				}
					System.out.println("PlantTaxonomyLoader > codeNameId : " + codeId );
				
			
				// if the nameid are '0' then the names were not inserted and thus the 
				// usage tables does not need to be updated
				if ( shortNameId != 0 )
				{
					if (plantUsageExists(shortName, shortNameId, conceptId, "STANDARD", 
						dateEntered, usageStopDate,  "SHORTNAME", partyId) == false )
					{
						int uid = 	loadPlantUsageInstance(shortName, shortNameId, conceptId, "STANDARD", 
						dateEntered, usageStopDate,  "SHORTNAME", partyId);
						System.out.println("PlantTaxonomyLoader > uid: " + uid);
						usageVec.addElement(""+uid);
					}
				}
				if ( codeId != 0 )
				{
					if  (plantUsageExists(code, codeId, conceptId, "STANDARD", 
					dateEntered, usageStopDate,  "CODE", partyId) == false )
					{
						int uid = loadPlantUsageInstance(code, codeId, conceptId, "STANDARD", 
						dateEntered, usageStopDate,  "CODE", partyId);
						System.out.println("PlantTaxonomyLoader > uid: " + uid);
						usageVec.addElement(""+uid);
					}
				}
				
				
				// last thing to do is to denormalize the database for querying
				// but dont mess with the lengthy stuff if we have failed already
				if (commit == true )
				{
					this.denormTaxonDB();
				}
				// decide on the transaction
				if (commit == true )
				{
					System.out.println("PlantTaxonomyLoader > commiting transaction");
					conn.commit();
				}
				else
				{
					System.out.println("PlantTaxonomyLoader >  not commiting transaction");
					conn.rollback();
				}
			}
		}
		catch ( Exception e )
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(commit);
	}
	
	/**
	 * method that returns true if all the elements in the hashtable
	 * which need to exist do exist, otherwise it returns false
	 * @param plant -- the hashtable with the plant elements
	 * @return valid -- whether the plant is valid or not
	 * @see loadGenericPlantTaxa method description of the anticipated input 
	 * 	elements
	 */
	private boolean isValidPlantHash(Hashtable plantTaxon)
	{
		boolean valid = true;
		if (plantTaxon.containsKey("longNameRefAuthors") &&  plantTaxon.containsKey("longNameRefTitle")
		 && plantTaxon.containsKey("longNameRefDate") && plantTaxon.containsKey("longNameRefEdition") 
		 && plantTaxon.containsKey("longNameRefSeriesName") && plantTaxon.containsKey("longNameRefVolume")
		 && plantTaxon.containsKey("longNameRefPage") && plantTaxon.containsKey("longNameRefISSN")
		 && plantTaxon.containsKey("longNameRefISBN") && plantTaxon.containsKey("longNameRefOtherCitDetails"))
		{
			System.out.println("PlantTaxaLoader > contains the long name reference");
			if (plantTaxon.containsKey("shortNameRefAuthors") &&  plantTaxon.containsKey("shortNameRefTitle")
			 && plantTaxon.containsKey("shortNameRefDate") && plantTaxon.containsKey("shortNameRefEdition") 
			 && plantTaxon.containsKey("shortNameRefSeriesName") && plantTaxon.containsKey("shortNameRefVolume")
			 && plantTaxon.containsKey("shortNameRefPage") && plantTaxon.containsKey("shortNameRefISSN")
			 && plantTaxon.containsKey("shortNameRefISBN") && plantTaxon.containsKey("shortNameRefOtherCitDetails"))
			{
				System.out.println("PlantTaxaLoader > contains the short name reference");
				if (plantTaxon.containsKey("codeRefAuthors") &&  plantTaxon.containsKey("codeRefTitle")
				 && plantTaxon.containsKey("codeRefDate") && plantTaxon.containsKey("codeRefEdition") 
			   && plantTaxon.containsKey("codeRefSeriesName") && plantTaxon.containsKey("codeRefVolume")
			   && plantTaxon.containsKey("codeRefPage") && plantTaxon.containsKey("codeRefISSN")
			   && plantTaxon.containsKey("codeRefISBN") && plantTaxon.containsKey("codeRefOtherCitDetails"))
				{
					System.out.println("PlantTaxaLoader > contains the code reference");
					if (plantTaxon.containsKey("conceptRefAuthors") &&  plantTaxon.containsKey("conceptRefTitle")
				 	 && plantTaxon.containsKey("conceptRefDate") && plantTaxon.containsKey("conceptRefEdition") 
			   	 && plantTaxon.containsKey("conceptRefSeriesName") && plantTaxon.containsKey("conceptRefVolume")
			  	 && plantTaxon.containsKey("conceptRefPage") && plantTaxon.containsKey("conceptRefISSN")
			   	 && plantTaxon.containsKey("conceptRefISBN") && plantTaxon.containsKey("conceptRefOtherCitDetails"))
					{
						System.out.println("PlantTaxaLoader > contains the concept reference");
						if (plantTaxon.containsKey("conceptStatus") &&  plantTaxon.containsKey("statusStartDate")
				 	 	 && plantTaxon.containsKey("statusStopDate") && plantTaxon.containsKey("statusDescription") 
			   	 	 && plantTaxon.containsKey("taxonLevel") && plantTaxon.containsKey("plantParentName")
			  	 	 && plantTaxon.containsKey("plantParentRefTitle") && plantTaxon.containsKey("plantParentRefAuthors"))
						{
							System.out.println("PlantTaxaLoader > contains the status info ");
							// LASTLY GET THE NAME AND USER INFO
							if (plantTaxon.containsKey("longName") &&  plantTaxon.containsKey("shortName")
				 	 	 	 && plantTaxon.containsKey("code") && plantTaxon.containsKey("salutation") 
			   	 	 	 && plantTaxon.containsKey("givenName") && plantTaxon.containsKey("surName"))
							{
								System.out.println("PlantTaxaLoader > contains names and user info ");
							}
							else
							{
								valid = false;
							}
						}
						else
						{
							valid = false;
						}
					}
					else
					{
						valid = false;
					}
				}
				else
				{
					valid=false;
				}
			}
			else
			{
				valid=false;
			}
		}
		else
		{
			valid=false;
		}
		return(valid);	
	}

	
	/**
	public boolean loadGenericPlantTaxa(Hashtable plantTaxon)
	{
		try
		{
			//get the parameters from the hashtable
			salutation = (String)plantTaxon.get("salutation");
			givenName = (String)plantTaxon.get("givenName");
			surName = (String)plantTaxon.get("surName");
			orgName = (String)plantTaxon.get("orgName");
			email = (String)plantTaxon.get("email");
			longName = (String)plantTaxon.get("longName");
			shortName = (String)plantTaxon.get("shortName");
			code = (String)plantTaxon.get("code");
			taxonDescription = (String)plantTaxon.get("taxonDescription");
			citationDetails = (String)plantTaxon.get("citationDetails");
			dateEntered = (String)plantTaxon.get("dateEntered");
			usageStopDate = (String)plantTaxon.get("usageStopDate");
			rank = (String)plantTaxon.get("rank");
			//set the commit boolean to true, which can be changed by any of the 
			// methods called from this one if an exception is thrown
			commit = true;
			
			//get the connection stuff
			conn = this.getConnection();
			conn.setAutoCommit(false);
			
			//insert the plant reference information and the plant party
			int refId = this.insertPlantReference(email, citationDetails);
			int partyId = this.insertPlantPartyInstance(salutation, givenName, surName, orgName, email);
			
			// check to see if the names exists in the database after 
			// checking that the attributes have valid names
			boolean longNameExist;
			boolean shortNameExist;
			boolean codeExist;
			
			if ( longName.length() < 1 )
			{
				System.out.println("long name is short");
				longNameExist = false;
			}
			else
			{
				longNameExist = this.plantNameExists(longName);
				if ( longNameExist == true )
				{
					longNameId = getPlantNameId(longName);
					System.out.println("PlantTaxaLoader > longNameId: " + longNameId );
				}
				else
				{
					longNameId = loadPlantNameInstance(refId, longName, "", dateEntered);
				}
			}
			
			if ( shortName.length() < 1 )
			{
				System.out.println("short name is short");
				 shortNameExist = false;
			}
			else
			{
				shortNameExist = this.plantNameExists(shortName);
				if ( shortNameExist == true )
				{
					shortNameId = getPlantNameId(shortName);
					System.out.println("PlantTaxaLoader > shortNameId: " + shortNameId );
				}
				else
				{
					shortNameId = loadPlantNameInstance(refId, shortName, "", dateEntered);
				}
			}
			
			if ( code.length() < 1 )
			{
				System.out.println("code name is short");
				 codeExist = false;
			}
			else
			{
				codeExist = this.plantNameExists(code);
				if ( codeExist == true )
				{
					codeId = getPlantNameId(code);
					System.out.println("PlantTaxaLoader > codeId: " + codeId );
				}
				else
				{
					codeId = loadPlantNameInstance(refId, code, "", dateEntered);
				}
			}

			
			//update the concept table with the long name and then
			int conceptId;
			int statusId;
			if (plantConceptExists(longNameId, refId, taxonDescription) == false)
			{
				conceptId = loadPlantConceptInstance(longNameId, refId, taxonDescription, 
				code, rank, longName );
			}
			else
			{
				conceptId = getPlantConceptId(longNameId, refId, taxonDescription, 
				code, rank, longName );
			}
			System.out.println("PlantTaxaLoader > conceptId: " + conceptId);
			//fix the date end date here and do a check here to make sure that it does not exist
			if ( plantStatusExists(conceptId, "accepted", dateEntered, "2005-May-29", 
				email, partyId, "", refId) == false )
			{
				statusId = loadPlantStatusInstance(conceptId, "accepted", dateEntered, 
				usageStopDate, email, partyId, "", refId);
			}
			
			//check to see if there are already usage id's in the database
			if ( plantUsageExists(longName, longNameId, conceptId, "STANDARD", 
				dateEntered, usageStopDate,  "LONGNAME", partyId) == false )
			{
				int uid = loadPlantUsageInstance(longName, longNameId, conceptId, "STANDARD", 
				dateEntered, usageStopDate,  "LONGNAME", partyId);
				System.out.println("PlantTaxonomyLoader > uid: " + uid);
				usageVec.addElement(""+uid);
			}
				System.out.println("PlantTaxonomyLoader > codeNameId : " + codeId );
			
			
			// if the nameid are '0' then the names were not inserted and thus the 
			// usage tables does not need to be updated
			if ( shortNameId != 0 )
			{
				if (plantUsageExists(shortName, shortNameId, conceptId, "STANDARD", 
					dateEntered, usageStopDate,  "SHORTNAME", partyId) == false )
				{
					int uid = 	loadPlantUsageInstance(shortName, shortNameId, conceptId, "STANDARD", 
					dateEntered, usageStopDate,  "SHORTNAME", partyId);
					System.out.println("PlantTaxonomyLoader > uid: " + uid);
					usageVec.addElement(""+uid);
				}
			}
			if ( codeId != 0 )
			{
				if  (plantUsageExists(code, codeId, conceptId, "STANDARD", 
				dateEntered, usageStopDate,  "CODE", partyId) == false )
				{
					int uid = loadPlantUsageInstance(code, codeId, conceptId, "STANDARD", 
					dateEntered, usageStopDate,  "CODE", partyId);
					System.out.println("PlantTaxonomyLoader > uid: " + uid);
					usageVec.addElement(""+uid);
				}
			}

			// last thing to do is to denormalize the database for querying
			// but dont mess with the lengthy stuff if we have failed already
			if (commit == true )
			{
				this.denormTaxonDB();
			}
			// decide on the transaction
			if (commit == true )
			{
				System.out.println("PlantTaxonomyLoader > commiting transaction");
				conn.commit();
			}
			else
			{
				System.out.println("PlantTaxonomyLoader >  not commiting transaction");
				conn.rollback();
			}
			
		}
		catch ( Exception e )
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(commit);
	}
	
	*/
	
	/**
	 * method to denormalize the plant taxonomy database for querying
	 * by the VegBank servlet.  This method mimics the database procedures 
	 * carried out by the sql script: denormPlantTaxonDb_pg.sql
	 */
	 private void denormTaxonDB()
	 {
		StringBuffer sb = new StringBuffer();
	 	try
		{
			//get the max usage in the summary table
			//get the usageId's to be used for updating the summary table 
			// from the usageVec vector

			System.out.println("PlantTaxonomyLoader > updating summary with usage Ids: " + usageVec.toString() );
		 	if ( usageVec.size() > 0 )
			{
			
				int minUsageId  = Integer.parseInt( usageVec.elementAt(0).toString() );
				System.out.println("PlantTaxonomyLoader > min usage id: " + minUsageId );

			//insert the data into the summary table
			sb = new StringBuffer();
			sb.append("INSERT INTO veg_taxa_summary ");
			sb.append("(plantusage_id, plantname_id, plantconcept_id, plantName, classsystem, ");
			sb.append(" plantnamestatus, startdate, stopDate, acceptedSynonym, plantparty_id) ");
			sb.append(" SELECT plantusage_id, plantname_id, plantconcept_id, plantname, ");
			sb.append(" classsystem, plantnamestatus, usagestart, usagestop, acceptedSynonym, ");
			sb.append(" plantparty_id");
			sb.append(" from plantusage where plantusage_id >= " + minUsageId );
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();	

			//do the updates
			sb = new StringBuffer();
			sb.append(" update  veg_taxa_summary ");
			sb.append(" set plantDescription = ");
			sb.append("  (select plantDescription from plantConcept where veg_taxa_summary.plantconcept_id ");
			sb.append("  = plantconcept.plantconcept_id ) where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();	
			
			sb = new StringBuffer();
			sb.append(" update  veg_taxa_summary ");
			sb.append(" set plantlevel = ");
			sb.append("  (select plantlevel from plantconcept where veg_taxa_summary.plantconcept_id ");
			sb.append("  = plantconcept.plantconcept_id  ) where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();	
			
			sb = new StringBuffer();
			sb.append(" update  veg_taxa_summary ");
			sb.append("  set parentName =");
			sb.append("  (select plantparentname from plantstatus where veg_taxa_summary.plantconcept_id ");
			sb.append("  = plantstatus.plantconcept_id ) where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();	
			
			//--UPDATE THE CONCEPT AUTHOR AND THE DATE
			sb = new StringBuffer();
			sb.append(" update  veg_taxa_summary "); 
			sb.append("  set plantConceptRefAuthor = (select authors from plantreference where plantreference_id = ");
			sb.append("(select plantreference_id from plantconcept where  veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id  ) ) ");
			sb.append(" where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();	
			
			sb = new StringBuffer();
			sb.append(" update  veg_taxa_summary "); 
			sb.append(" set plantConceptRefDate = (select pubdate from plantreference where plantreference_id = ");
			sb.append("  (select plantreference_id from plantconcept where  veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id  ) )");
			sb.append(" where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();
					
			//--UPDATE THE PARTY OR NAME FOR THE USAGE
			sb = new StringBuffer();
			sb.append("update  veg_taxa_summary "); 
			sb.append("set plantUsagePartyOrgName = "); 
			sb.append("(select organizationname from plantparty where veg_taxa_summary.plantparty_id = plantparty.plantparty_id )");
			sb.append(" where  plantusage_id >= " + minUsageId );
			pstmt = conn.prepareStatement( sb.toString() );
			pstmt.execute();
			pstmt.close();
									
			}

		}
		catch ( Exception e )
		{
			commit = false;
			System.out.println("Erroneous sql: " + sb.toString() );
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	 }
		
	
 /**
	* method that returs true if a usage instance exists 
	* 
 	*/	
	private boolean plantUsageExists(String concatenatedName, int nameId, 
	int conceptId, String usage, String startDate, String stopDate, 
	String classSystem, int partyId )
	{
		boolean exists = false;
		StringBuffer sb = new StringBuffer();
		try
		{
			sb.append("select PLANTUSAGE_ID from PLANTUSAGE ");
			sb.append("where plantConcept_id = " +conceptId+" and ");
			sb.append("plantname_id = " +nameId+" and ");
			sb.append("classSystem like '" +classSystem+"' and ");
			sb.append("plantnamestatus like  '"+usage+"'" );
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			ResultSet rs = pstmt.executeQuery();
			int statusId = 0;
			int cnt = 0;
				
			while	( rs.next() )
			{	
				statusId = rs.getInt(1);
				cnt++;
			}
			if ( cnt >= 1 )
			{
				System.out.println("PlantTaxonomyLoader > usage key exists");
				exists = true;
			}
			//if there are no returned results
			else if ( cnt == 0 )
			{
				exists = false;
			}
			
			pstmt.close();
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Erroneous sql: " + sb.toString() );
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(exists);
	}

	/**
 	* method that loads a new instance of a 
 	* plant concept - name usage returns to 
 	* the calling method the primary key for 
 	* that concept - name usage
	* 
	* @param concatenatedName
	* @param name_id
	* @param concept_id
	* @param plantNameStatus -- standard or non standard
	* @param usageStart
	* @param usageStop
	* @param classSystem -- common name or code or scientific name
 	*/	
	private int loadPlantUsageInstance(String concatenatedName, int name_id, 
	int concept_id, String usage, String startDate, String stopDate, 
	String classSystem, int partyId)
	{
		int usageId = 0;
		try
		{
			String s = "insert into PLANTUSAGE (plantName, plantName_id, plantConcept_id, "+
				" plantNameStatus, usageStart, usageStop, classSystem, plantParty_id) "
				+" values(?,?,?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setString(1, concatenatedName);
			pstmt.setInt(2, name_id);
			pstmt.setInt(3, concept_id);
			pstmt.setString(4, usage);
			pstmt.setString(5, startDate);
			if ( stopDate == null || stopDate.length() < 2 )
			{
				pstmt.setNull(6, 6);
			}
			else
			{	
				pstmt.setString(6, stopDate);
			}
			pstmt.setString(7, classSystem);
			pstmt.setInt(8, partyId);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();	
			
			// lest
			// now get the usage ID for the inserted plant and return it to 
			// the calling method so that it can be used at the end of the
			// transaction for updating the summary/query tables 
			StringBuffer sb = new StringBuffer();
			sb.append("select max( PLANTUSAGE_ID ) from plantusage ");
			pstmt = conn.prepareStatement( sb.toString() );
			ResultSet rs = pstmt.executeQuery();
			while	( rs.next() )
			{	
				usageId = rs.getInt(1);
			}
			
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			System.out.println("plantNameId: " + name_id );
			System.out.println("plantConceptId: " + concept_id );
			e.printStackTrace();
			//System.exit(0);
		}
		return(usageId);	
	}
	
/**
 * method that returns the primary key value
 * from the plant concept key based on the 
 * input parameters
 * 
 * @param plantDescription -- the plantDescription
 * @param longNameId -- the long name of the plant
 * @param refId -- the reference ID for the concept
 * @param code -- the codeName for the plant
 * @param rank -- the heirarchical rank of the concept
 * @param longName -- the longName of the plantName
 * fix -- the name if the input plantDesc to taxonDes
 */
	private int getPlantConceptId(int longNameId, int refId, String plantDescription, 
	String code, String rank, String longName)
	{
		int plantConceptId = -999;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantconcept_id from plantconcept where plantDescription = '"
			+plantDescription+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{	
				plantConceptId = rs.getInt(1);
				cnt++;
			}
			if ( cnt > 1 )
			{
				System.out.println("USDAPlantsLoader > plantConceptKey violation:  more than one"
				+" keys returned");
			}
			//if there are no returned results
			else if ( cnt == 0 )
			{
				System.out.println("USDAPlantsLoader > no  "+plantDescription+" found in the concept table");
			}
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
			//System.exit(0);
		}
		return(plantConceptId);
	}


 /**
	*
	* method that returns true if the plant instance already exists in
	* the taxonomy database.
	*
	* @param conceptId
	* @param status
	* @param startDate
	* @param endDate
	* @param event
	* @param plantPartyId
 	*/	
	private boolean plantStatusExists(int conceptId, String status, 
	String startDate, String endDate, String event, int plantPartyId, 
	String plantParent, int refId)
	{
		boolean exists = false;
		StringBuffer sb = new StringBuffer();
		try
		{
			sb.append("select PLANTSTATUS_ID from PLANTSTATUS ");
			sb.append("where plantConcept_id = " +conceptId+" and ");
			sb.append("plantconceptstatus like '" +status+"' and ");
			//fix the block below
			//sb.append("startDate like '" +startDate+"' and ");
			//sb.append("endDate like '" +endDate.setNull()+"' and ");
			sb.append("plantPartyComments like '" +event+"' and ");
			sb.append("plantparty_id = " +plantPartyId+" and ");
			sb.append("plantreference_id = " +refId);
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			ResultSet rs = pstmt.executeQuery();
			int statusId = 0;
			int cnt = 0;
				
			while	( rs.next() )
			{	
				statusId = rs.getInt(1);
				cnt++;
			}
			if ( cnt >= 1 )
			{
				System.out.println("PlantTaxonomyLoader > status key exists");
				exists = true;
			}
			//if there are no returned results
			else if ( cnt == 0 )
			{
				exists = false;
			}
			
			pstmt.close();
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Erroneous sql: " + sb.toString() );
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(exists);
	}


	
	/**
 	* method that loads a new instance of a 
 	* plant usage - name usage returns to 
 	* the calling method the primary key for 
 	* that concept - name usage
	*
	* @param conceptId
	* @param status
	* @param startDate
	* @param endDate
	* @param event
	* @param plantPartyId
 	*/	
	private int loadPlantStatusInstance(int conceptId, String status, 
	String startDate, String endDate, String event, int plantPartyId, 
	String plantParent, int refId)
	{
		try
		{
			int parentConceptId = 0;
			// GET THE PLANT PARENT IF ONE IS PASSED
			if (plantParent !=null && plantParent.length() > 1 )
			{
				System.out.println("PlantTaxaLoader > plant parent to be insert into status: " + plantParentName);
				String s1 = "select plantConcept_id from plantConcept where plantDescription = '"
				+plantParent+"'";
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( s1 );
				int cnt = 0;
				while ( rs.next() ) 
				{
					parentConceptId = rs.getInt(1);
					cnt++;
				}
				if (cnt > 1 )
				{
					System.out.println("USDAPlantsLoader > warning: to many parent concept id's");	
				}
				
				String s = "insert into PLANTSTATUS "
				+"(plantConcept_id, plantconceptstatus, startDate, stopDate, "
				+" plantPartyComments, plantParty_id, plantParentName, "
				+" plantParentConcept_id)  values(?,?,?,?,?,?,?,?) ";
				
				PreparedStatement pstmt = conn.prepareStatement(s);
				//bind the values
				pstmt.setInt(1, conceptId);
				pstmt.setString(2, status);
				pstmt.setString(3, startDate);
				if ( endDate == null || endDate.length() < 2 )
				{
					pstmt.setNull(4, 4);
				}
				else
				{	
					pstmt.setString(4, endDate);
				}
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
				pstmt.setInt(8, parentConceptId);
			
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
			}
			// IF IT DOES NOT EXIST THEN SUBMIT WITHOUT IT
			else
			{
				String s = "insert into PLANTSTATUS "
				+"(plantConcept_id, plantconceptstatus, startDate, stopDate, "
				+" plantPartyComments, plantParty_id, plantParentName, plantreference_Id) "
				+" values(?,?,?,?,?,?,?,?) ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				//bind the values
				pstmt.setInt(1, conceptId);
				pstmt.setString(2, status);
				pstmt.setString(3, startDate);
				if ( endDate == null || endDate.length() < 2 )
				{
					pstmt.setNull(4, 4);
				}
				else
				{	
					pstmt.setString(4, endDate);
				}
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
				pstmt.setInt(8, refId);
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
			}
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	return(0);	
}






	/**
 	* method that loads a new instance of a plant concept 
	* then returns to the calling method the primary key for 
	* that concept
	* @param plantNameId
	* @param plantReferenceId
	* @param plantDescription
	* @param plantCode
	* @param rank
	* @param plantName
	* @return plantConceptId -- the PK value for this table
 	*/	
	private int loadPlantConceptInstance(int plantNameId, int plantReferenceId, 
	String plantDescription, String plantCode, String rank, String plantName)
	{
		int plantConceptId = 0;
		try
		{
			// MAKE SURE THAT THE CONCEPTDESCRIPTION IS NOT NULL
			if ( plantDescription == null || plantDescription.trim().equals("null") )
			{
				plantDescription = "";
			}
			String s = "insert into PLANTCONCEPT (PLANTNAME_ID, PLANTDESCRIPTION, "
			+" PLANTREFERENCE_ID, PLANTCODE, PLANTLEVEL, PLANTNAME) values(?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, plantNameId);
			pstmt.setString(2, plantDescription);
			pstmt.setInt(3, plantReferenceId);
			pstmt.setString(4, plantCode);
			pstmt.setString(5, rank);
			pstmt.setString(6, plantName);
			pstmt.execute();
			pstmt.close();	
		
			//now get the associated primary key value -- fix this to use a set method
			StringBuffer sb = new StringBuffer();
			sb.append("select PLANTCONCEPT_ID from PLANTCONCEPT where ");
			sb.append("PLANTDESCRIPTION like '"+plantDescription+"' and ");
			sb.append("PLANTNAME like '"+plantName+"'");
		
			
			//String s1 = "select plantconcept_id from plantconcept where plantDescription = '"
			//+plantDescription+"'";
			PreparedStatement pstmt1 = conn.prepareStatement( sb.toString() );
			System.out.println("PlantTaxaLoader > sql to get conceptid: " + sb.toString() );
			
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantConceptId = rs.getInt(1);
				//System.out.println("USDAPlantsLoader >>> plantconcept_id: " + plantConceptId);
			}
			if (cnt > 1)
			{
				throw new Exception("multiple plantconceptID's found");
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() 
			+ "\n conceptId: " +  plantConceptId 
			+"\n plantDescription: " + plantDescription);
			e.printStackTrace();
			//System.exit(0);
		}
		return(plantConceptId);
	}
	
	/**
 	* method that returns true if an instance of a plant
 	* concept exists that is identical to the input critreria
	*
	*
	* @return exists -- boolean 
 	*/	
	private boolean plantConceptExists(int longNameId, int refId, String taxonDescription)
	{
		boolean result = false;
		try
		{
			//now get the associated primary key value
			StringBuffer sb = new StringBuffer();
			sb.append("select PLANTCONCEPT_ID from PLANTCONCEPT where PLANTDESCRIPTION like '");
			sb.append(taxonDescription+"' and PLANTREFERENCE_ID = ");
			sb.append(refId + " and PLANTNAME_ID = ");
			sb.append(longNameId);
			PreparedStatement pstmt1 = conn.prepareStatement(sb.toString() );
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
			}
			//figure out how many 
			if (cnt > 0 )
			{
				result =true;
			}
			else 
			{
				result = false;
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(result);
	}

	/**
 	* method that loads a new instance of a 
 	* plant name and then returns the primary
 	* key value of that plant name to the calling
 	* method
	*
	* @param refId -- the reference Id
	* @param plantName -- the plantname (w/o the author bit)
	* @param plantNameWithAuthor
	* @param dateEntered -- the data thate the plant name is to be entered
	* @return plantNameId -- the plant name Id assocaited with this plant
	*
 	*/	
	private int loadPlantNameInstance(int refId, String plantName, 
	String plantNameWithAuthor, String dateEntered)
	{
		int plantNameId = -999;
		try
		{
			String s = "insert into PLANTNAME (plantreference_id, plantName, "
			+"plantNameWithAuthor, dateEntered) "
			+" values(?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, refId);
			pstmt.setString(2, plantName);
			pstmt.setString(3, plantNameWithAuthor);
			pstmt.setString(4, dateEntered);
			pstmt.execute();
			pstmt.close();	
		
			//now get the associated primary key value
			String s1 = "select plantname_id from PLANTNAME where plantname = '"+ plantName+ "' ";
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantNameId = rs.getInt(1);
				//System.out.println("USDAPlantsLoader >>> plantname_id: " + plantNameId);
			}
			if (cnt > 1)
			throw new Exception("multiple plantnameID's found for: '"+plantName+"'");
			pstmt1.close();
		}
		catch(Exception e )
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(plantNameId);
	}

	
	 /** 
	  * method that returns true if the party already exists 
		*/
		private boolean plantPartyExists(String salutation, String givenName, String surName,
		String orgName, String email)
	  {
			StringBuffer sb = new StringBuffer();
			boolean exists = false;
		 try
		 {
			sb = new StringBuffer();
			sb.append("SELECT plantparty_id from PLANTPARTY where organizationName like '");
			sb.append(orgName+"' ");
			sb.append("and salutation like '"+salutation+"' ");
			sb.append("and givenName like '"+givenName+"' ");
			sb.append("and surName like '"+surName+"' ");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				cnt++;
			}
			if (cnt > 0)
			{
				//System.out.println("USDAPlantsLoader > matching party:  " + cnt  );
				exists = true;
			}
			else
			{
				exists = false;
			}
		 }
			catch (Exception e)
		 {
			commit = false;
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
			 System.out.println("Erroneous sql: " + sb.toString() );
		 }
		 return(exists);
	 }
	
	/** 
	 * method that returns the party nameId for an organization name
	 *
	 * @param partyName -- the name of the party
	 * @return plantpartyId -- the plantPartyId
	 * 
	 */
		private int getPlantPartyId(String salutation, String givenName, 
		String surName, String orgName, String email)
	  {
		 	int partyId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb = new StringBuffer();
				sb.append("SELECT plantparty_id from PLANTPARTY where organizationName like '");
				sb.append(orgName+"' ");
				sb.append("and salutation like '"+salutation+"' ");
				sb.append("and givenName like '"+givenName+"' ");
				sb.append("and surName like '"+surName+"' ");
			
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					partyId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			commit = false;
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(partyId);
	 }

	
	
	/**
	 * method that loads a party occurence into the party table.  First the method
	 * determines if the party already exits in the database, and if it does not 
	 * then it is loaded.
	 *
	 * @param salutation
	 * @param givenName
	 * @param surName
	 * @param orgName
	 * @param email
	 * @return partyid -- the primary key value for the party
	 */
	private int insertPlantPartyInstance(String salutation, String givenName, String surName,
	String orgName, String email)
	{
	 int partyId = 0; 
	 StringBuffer sb = new StringBuffer();
	 try
	 {
		//first see if the reference already exists
		boolean partyExists = plantPartyExists(salutation, givenName, surName, orgName, email);
		System.out.println("PlantTaxaLoader > partyExists: " + partyExists); 
		
		if (partyExists == true)
		{
			partyId = getPlantPartyId(salutation, givenName, surName, orgName, email);
		}

		else
		{
			//insert the strata values
			sb.append("INSERT into PLANTPARTY (organizationName, salutation, surName, givenName) "
			+" values(?,?,?,?)");
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			// Bind the values to the query and execute it
 			pstmt.setString(1, orgName);
			pstmt.setString(2, salutation);
 			pstmt.setString(3, surName);
 			pstmt.setString(4, givenName);
			pstmt.execute();
 			// GET THE PARTY ID NOW
			partyId = getPlantPartyId(salutation, givenName, surName, orgName, email);
		}
		System.out.println("PlantTaxaLoader > returning a plantPartyId: " + partyId);
	 }
		catch (Exception e)
	 {
		 commit = false;
		 System.out.println("PlantTaxaLoader > Exception: " + e.getMessage() );
		 System.out.println("sql: " + sb.toString() );
		 e.printStackTrace();
	 }
	 return(partyId);
	}
	 
	 /** 
	  * method that inserts the reference data 
		*/
		private boolean plantReferenceExists(String authors, String title, String date)
	  {
			boolean exists = false;
			StringBuffer sb = new StringBuffer();
			try
			{
				sb = new StringBuffer();
				sb.append("SELECT plantreference_id from PLANTREFERENCE where authors");
				sb.append(" like '"+authors+"' ");
				sb.append(" and title like '" + title + "'");
				// IF THE DATE IS A VALID DATE THEN QUERY BY IT TOO
				if ( date.length() > 4 )
				{
					sb.append(" and PUBDATE = '" + date + "'");
				}
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					cnt++;
				}
				if (cnt > 0)
				{
					exists = true;
				}
				else
				{
					exists = false;
				}
			}
			catch (Exception e)
		 {
			 commit = false;
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 System.out.println("sql: " + sb.toString() );
			 e.printStackTrace();
		 }
		 return(exists);
	 }
	 
	 /** 
	  * method that grabs and retuns the plantReferenceId based on the 
	  * authors' name and the otherCitationDetails
	  */
		private int getPlantReferenceId(String authors, String title, String date)
	  {
			StringBuffer sb = new StringBuffer();
		 	int refId = 0; 
			try
			{
				sb.append("SELECT plantreference_id from PLANTREFERENCE where title");
				sb.append(" like '"+title+"' and authors like '"+authors+"'" );
				// IF THE DATE IS VALID USE IT ELSE DONT
				if ( date.length() > 4 )
				{
					sb.append(" and pubdate = '" + date +"'");
				}
				//System.out.println("getPlantReferenceId sql: " + sb.toString() );
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					refId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 commit = false;
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 System.out.println("sql: " + sb.toString() );
			 e.printStackTrace();
		 }
		 return(refId);
	 }

	 
 /** 
  * method that inserts the reference data into the 'plantreference'
	* table, if it does not already exist and it it does it returns the 
	* referenece id
	*/
	private int insertPlantReference(String authors, String title, String date, 
	String edition, String seriesName, String	otherCitationDetails, String page, 
	String isbn, String issn, String conceptDescription)
  {
		int refId = 0; 
	 	StringBuffer sb = new StringBuffer();
		PreparedStatement pstmt;
		try
	 	{
			System.out.println("title: " +title+" authors: " + authors );
			
			//first see if the reference already exists
			boolean refExists = plantReferenceExists(authors, title, date);
			System.out.println("PlantTaxaLoader > ref exists: " + refExists);
			if (refExists == true)
			{
				refId = getPlantReferenceId(authors, title, date);
			}
			else
			{
				// IF THE DATE IS VALID THEN USE ONE QUERY ELSE USE ANOTHER
				if ( date.length() > 4 )
				{	
					sb.append("INSERT into PLANTREFERENCE (AUTHORS, TITLE, PUBDATE) ");
					sb.append(" values(?,?,?)");
					pstmt = conn.prepareStatement( sb.toString() );
					// Bind the values to the query and execute it
					pstmt.setString(1, authors);
					pstmt.setString(2, title);
					pstmt.setString(3, date);
 				}
				else
				{
					sb.append("INSERT into PLANTREFERENCE (AUTHORS, TITLE) ");
					sb.append(" values(?,?)");
					pstmt = conn.prepareStatement( sb.toString() );
					// Bind the values to the query and execute it
					pstmt.setString(1, authors);
					pstmt.setString(2, title);
				}
				pstmt.execute();

				//now get the reference id to return
				refId = getPlantReferenceId(authors, title, date);
				System.out.println("PlantTaxaLoader > new refId: " + refId);	
		}
	 }
		catch (Exception e)
	 {
		 commit = false;
		 System.out.println("PlantTaxaLoader > Exception: " + e.getMessage() );
		 System.out.println("sql: " + sb.toString() );
		 e.printStackTrace();
	 }
	 return(refId);
 }
	
	/** 
	 * method that returns the plantnameId based on a plant name
	 *
	 * @param plantName -- the name of the plant
	 * @return plantnameId -- the plantnameId
	 * 
	 */
		private int getPlantNameId(String plantName)
	  {
		 	int plantNameId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT plantname_id from PLANTNAME where plantname"
				+" like '"+plantName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					plantNameId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			commit = false;
			 System.out.println("PlantTaxaLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(plantNameId);
	 }
	 
	 
	
	/**
 	* method that returns true if an instance of a plant
 	* name exists that is identical to the plantName 
 	* input into the method
	* @param -- concatenatedName -- the plant name
 	*/	
	private boolean plantNameExists(String concatenatedName)
	{
		boolean result = false;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantName from plantName where plantname = '"
			+concatenatedName+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
			}
			//figure out how many 
			if (cnt > 0 )
			{
				result =true;
			}
			else 
			{
				result = false;
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(result);
	}
	

	 
	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/tmp2", "datauser", "");
			c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/plants_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			commit = false;
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	

	/**
	 * Main method -- for testing 
	 */
	public static void main(String argv[]) 
	{
		if (argv.length != 0) 
		{
			System.out.println("Usage: java PlantTaxaLoader \n "
			+"[longName] ");
				
			PlantTaxaLoader loader = new PlantTaxaLoader();
			Hashtable h = new Hashtable();
			h.put("longName", argv[0]);
			h.put("shortName", argv[1]);
			h.put("code", argv[2]);
			h.put("taxonDescription", argv[3]);
			h.put("salutation", argv[4]);
			h.put("givenName", argv[5]);
			h.put("surName", argv[6]);
			h.put("orgName", argv[7]);
			h.put("email", argv[8]);
			h.put("citationDetails", argv[9]);
			h.put("dateEntered", argv[10]);
			h.put("usageStopDate", argv[11]);
			h.put("rank", argv[12]);
			
			System.out.println("long name: " + argv[0] );
			System.out.println("short name: " + argv[1]  );
			System.out.println("code: " + argv[2] );
			System.out.println("taxon desc.: " + argv[3]);
			System.out.println("salutation: " +argv[4]  );
			System.out.println("given name: " +argv[5]  );
			System.out.println("sur name: " +argv[6]  );
			System.out.println("org name: " +argv[7]  );
			System.out.println("email: " + argv[8] );
			System.out.println("cit. details: " +argv[9]  );
			System.out.println("date entered: " +argv[10]  );
			System.out.println("usage stopdate: " + argv[11] );
			System.out.println("rank: " + argv[12] );
			loader.loadGenericPlantTaxa(h);
			
		}
		else
		{
			PlantTaxaLoader loader = new PlantTaxaLoader();
			Hashtable h = new Hashtable();
			
			String givenName = "John";
	 		String surName = "harris";
	 
	 		String	institution = "nceas";
	 		String	longName = "Pinus ponderinguou";
	 		String	shortName = "pondering pineuou";
	 		String	code = "PPPuou";
	 
	 		String	longNameRefAuthors = "John Harris, Robert Peet";
	 		String	longNameRefTitle = "Big Trees in California by the sea";
	 		String	longNameRefDate  = "23-JAN-2002";
	 		String	longNameRefEdition = "";
	 		String	longNameRefSeriesName = "";
	 		String	longNameRefVolume = "";
	 		String	longNameRefPage = "11-12";
	 		String	longNameRefISSN = "";
	 		String	longNameRefISBN = "";
	 		String	longNameRefOtherCitDetails = "";
	 		
	 		String	shortNameRefAuthors = "";
	 		String	shortNameRefTitle = "";
	 		String	shortNameRefDate = "";
	 		String	shortNameRefEdition = "";
	 		String	shortNameRefSeriesName = "";
	 		String	shortNameRefVolume = "";
	 		String	shortNameRefPage = "";
	 		String	shortNameRefISSN  = "";
	 		String	shortNameRefISBN  = "";
	 		String	shortNameRefOtherCitDetails = "";
	 		
	 		String	codeRefAuthors = "";
	 		String	codeRefTitle = "";
	 		String	codeRefDate = "";
	 		String	codeRefEdition = "";
	 		String	codeRefSeriesName = "";
	 		String	codeRefVolume = "";
	 		String	codeRefPage = "";
	 		String	codeRefISSN = "";
	 		String	codeRefISBN = "";
	 		String	codeRefOtherCitDetails = "";
	 
	 		String	conceptDescription = "";
	 		String	conceptRefAuthors = "Bob Peet, John Harris";
	 		String	conceptRefTitle = "Big Trees in the State of California by the coast";
	 		String	conceptRefDate = "24-Jan-2002";
	 		String	conceptRefEdition = "";
	 		String	conceptRefSeriesName = "";
	 		String	conceptRefVolume = "";
	 		String	conceptRefPage = "";
	 		String	conceptRefISSN = "";
	 		String	conceptRefISBN = "";
	 		String	conceptRefOtherCitDetails = "";
	 	
	 		String	conceptStatus = "accepted";
	 		String	statusStartDate = "01-JAN-2001";
	 		String	statusStopDate = "";
	 		String	statusDescription = "accepted till it is figured out sometime";
	 		String	taxonLevel =  "Species";
	 		String	plantParentName = "Pinus";
	 		String	plantParentRefTitle =  "";
	 		String	plantParentRefAuthors = "";
			
			//new elements
			h.put("longNameRefAuthors", longNameRefAuthors);
			h.put("longNameRefTitle",longNameRefTitle );
			h.put("longNameRefDate", longNameRefDate );
			h.put("longNameRefEdition", longNameRefEdition );
			h.put("longNameRefSeriesName", longNameRefSeriesName );
			h.put("longNameRefVolume", longNameRefVolume );
			h.put("longNameRefPage", longNameRefPage );
			h.put("longNameRefISSN", longNameRefISSN );
			h.put("longNameRefISBN", longNameRefISBN );
			h.put("longNameRefOtherCitDetails", longNameRefOtherCitDetails );
			h.put("shortNameRefAuthors", shortNameRefAuthors );
			h.put("shortNameRefTitle", shortNameRefTitle );
			h.put("shortNameRefDate", shortNameRefDate );
			h.put("shortNameRefEdition", shortNameRefEdition );
			h.put("shortNameRefSeriesName", shortNameRefSeriesName );
			h.put("shortNameRefVolume", shortNameRefVolume );
			h.put("shortNameRefPage", shortNameRefPage );
			h.put("shortNameRefISSN", shortNameRefISSN );
			h.put("shortNameRefISBN", shortNameRefISBN );
			h.put("shortNameRefOtherCitDetails", shortNameRefOtherCitDetails );
			h.put("codeRefAuthors", codeRefAuthors);
			h.put("codeRefTitle", codeRefTitle );
			h.put("codeRefDate", codeRefDate );
			h.put("codeRefEdition", codeRefEdition );
			h.put("codeRefSeriesName", codeRefSeriesName );
			h.put("codeRefVolume", codeRefVolume);
			h.put("codeRefPage", codeRefPage );
			h.put("codeRefISSN", codeRefISSN );
			h.put("codeRefISBN", codeRefISBN );
			h.put("codeRefOtherCitDetails",codeRefOtherCitDetails );
			
			h.put("conceptDescription", conceptDescription );
			h.put("conceptRefAuthors", conceptRefAuthors );
			h.put("conceptRefTitle", conceptRefTitle );
			h.put("conceptRefDate", conceptRefDate);
			h.put("conceptRefEdition", conceptRefEdition);
			h.put("conceptRefSeriesName", conceptRefSeriesName );
			h.put("conceptRefVolume", conceptRefVolume );
			h.put("conceptRefPage", conceptRefPage );
			h.put("conceptRefISSN",conceptRefISSN );
			h.put("conceptRefISBN", conceptRefISBN );
			h.put("conceptRefOtherCitDetails", conceptRefOtherCitDetails );
			
			
			h.put("conceptStatus", conceptStatus );
			h.put("statusStartDate", statusStartDate);
			h.put("statusStopDate", statusStopDate );
			h.put("statusDescription",statusDescription );
			h.put("taxonLevel", taxonLevel );
			h.put("plantParentName", plantParentName );
			h.put("plantParentRefTitle", plantParentRefTitle);
			h.put("plantParentRefAuthors",plantParentRefAuthors );

			
		
			
			h.put("longName", longName);
			h.put("shortName", shortName);
			h.put("code", code);
			h.put("taxonDescription", conceptDescription);
			h.put("salutation", "Mr.");
			h.put("givenName", givenName);
			h.put("middleName", "H");
			h.put("surName", surName);
			h.put("orgName", institution);
			h.put("email", "harris02@hotmail.com");
			h.put("citationDetails", "VB20020529");
			h.put("dateEntered", "2002-MAY-29");
			h.put("usageStopDate", "2005-MAY-29");
			h.put("rank", taxonLevel);
			
			// load this plant
			loader.loadGenericPlantTaxa(h);
		}
	}



	
}
