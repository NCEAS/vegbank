/**
 *  '$RCSfile: PlantTaxaLoader.java,v $'
 *   '$Author: harris $'
 *     '$Date: 2002-05-29 19:12:45 $'
 * '$Revision: 1.2 $'
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
	
	
	
	/**
	 * this method will take a hashtable that represents a taxon (see below)
	 * and insert the elements of the hashtable into the database and will 
	 * return an xml receipt for the loading process.  This method is intended 
	 * to be a 'generic' method that may be called by a number of loaders to 
	 * be used for loading custom datasets
	 */
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
			
			//get the connection stuff
			conn = this.getConnection();
			conn.setAutoCommit(false);
			
			//insert the plant reference information and the plant party
			int refId = this.insertPlantReference(email, citationDetails);
			int partyId = this.insertPlantPartyInstance(salutation, givenName, surName, orgName, email);
			
			// check to see if the names exists 
			boolean longNameExist = this.plantNameExists(longName);
			boolean shortNameExist = this.plantNameExists(shortName);
			boolean codeExist = this.plantNameExists(code);
			
			// if so then get the nameId 
			if ( longNameExist == true )
			{
				longNameId = getPlantNameId(longName);
				System.out.println("PlantTaxaLoader > longNameId: " + longNameId );
			}
			else
			{
				longNameId = loadPlantNameInstance(refId, longName, "", dateEntered);
			}
			if ( shortNameExist == true )
			{
				shortNameId = getPlantNameId(shortName);
				System.out.println("PlantTaxaLoader > shortNameId: " + shortNameId );
			}
			else
			{
				shortNameId = loadPlantNameInstance(refId, shortName, "", dateEntered);
			}
			if ( codeExist == true )
			{
				codeId = getPlantNameId(code);
				System.out.println("PlantTaxaLoader > codeId: " + codeId );
			}
			else
			{
				codeId = loadPlantNameInstance(refId, code, "", dateEntered);
			}
			
			//update the concept table with the long name and then
			int conceptId;
			String rank ="";
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
			int statusId = loadPlantStatusInstance(conceptId, "accepted", dateEntered, 
			"2005-May-29", email, partyId, "");
			
			loadPlantUsageInstance(longName, longNameId, conceptId, "STANDARD", 
			dateEntered, "30-JUN-2001",  "SCIENTIFICNAME", partyId);
			
			//fix this to work with the other two too
			loadPlantUsageInstance(shortName, shortNameId, conceptId, "STANDARD", 
			dateEntered, "30-JUN-2001",  "SHORTNAME", partyId);
			
			loadPlantUsageInstance(code, codeId, conceptId, "STANDARD", 
			dateEntered, "30-JUN-2001",  "CODE", partyId);
			
			conn.commit();
			//conn.rollback();
			
			
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(true);
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
			pstmt.setString(6, stopDate);
			pstmt.setString(7, classSystem);
			pstmt.setInt(8, partyId);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();	
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			System.out.println("plantNameId: " + name_id );
			System.out.println("plantConceptId: " + concept_id );
			e.printStackTrace();
			//System.exit(0);
		}
		return(0);	
	}
	
/**
 * method that returns the primary key value
 * from the plant concept key based on the 
 * input parameters
 * 
 * @param plantDescription -- the plantDescription
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
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
			//System.exit(0);
		}
		return(plantConceptId);
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
	String plantParent)
	{
		
		try
		{
			int parentConceptId = 0;
			
			//get the plant parentId
			if (plantParent !=null && plantParent.length() > 1 )
			{
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
				pstmt.setString(4, endDate);
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
				pstmt.setInt(8, parentConceptId);
			
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
				
			}
			else
			{
			
				String s = "insert into PLANTSTATUS "
				+"(plantConcept_id, plantconceptstatus, startDate, stopDate, "
				+" plantPartyComments, plantParty_id, plantParentName)  values(?,?,?,?,?,?,?) ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				//bind the values
				pstmt.setInt(1, conceptId);
				pstmt.setString(2, status);
				pstmt.setString(3, startDate);
				pstmt.setString(4, endDate);
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
			
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
			}
		
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	return(0);	
}

	/**
 	* method that loads a new instance of a 
 	* plant concept then returns to the calling
 	* method the primary key for that concept
 	*/	
	private int loadPlantConceptInstance(int plantNameId, int plantReferenceId, 
	String plantDescription, String plantCode, String rank, String plantName)
	{
		int plantConceptId = -999;
		try
		{
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
			String s1 = "select plantconcept_id from plantconcept where plantDescription = '"
			+plantDescription+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantConceptId = rs.getInt(1);
				//System.out.println("USDAPlantsLoader >>> plantconcept_id: " + plantConceptId);
			}
			if (cnt > 1)
			throw new Exception("multiple plantconceptID's found");
			pstmt1.close();
		}
		catch(Exception e)
		{
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
			throw new Exception("multiple plantnameID's found");
			pstmt1.close();
		}
		catch(Exception e )
		{
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
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(partyId);
	 }

	
	
	/**
	 * method that loads a party occurence into the party table
	 */
	private int insertPlantPartyInstance(String salutation, String givenName, String surName,
	String orgName, String email)
	{
	 int partyId = 0; 
	 try
	 {
	 	StringBuffer sb = new StringBuffer();
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
 			
			partyId = getPlantPartyId(salutation, givenName, surName, orgName, email);
/*			
			//get the partyId
			sb = new StringBuffer();
			sb.append("SELECT plantParty_id from PLANTPARTY where organizationName"
			+" like '"+partyName+"'");
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			int cnt = 0;
			while ( rs.next() ) 
			{
				partyId = rs.getInt(1);
				cnt++;
			}
*/			
		}
	 }
		catch (Exception e)
	 {
		 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
		 e.printStackTrace();
	 }
	 return(partyId);
	}
	 
	 /** 
	  * method that inserts the reference data 
		*/
		private boolean plantReferenceExists(String authors, String otherCitationDetails)
	  {
		boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			sb = new StringBuffer();
			sb.append("SELECT plantreference_id from PLANTREFERENCE where othercitationdetails");
			sb.append(" like '"+otherCitationDetails+"' ");
			sb.append(" and authors like '");
			sb.append(authors+"'");
			
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
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(exists);
	 }
	 
	 /** 
	  * method that grabs and retuns the plantReferenceId based on the 
	  * authors' name and the otherCitationDetails
	  */
		private int getPlantReferenceId(String authors, String otherCitationDetails)
	  {
		 	int refId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT plantreference_id from PLANTREFERENCE where othercitationdetails");
				sb.append(" like '"+otherCitationDetails+"' and authors like '");
				sb.append(authors+"'");
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
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(refId);
	 }

	 
 /** 
  * method that inserts the reference data into the 'plantreference'
	* table, if it does not already exist and it it does it returns the 
	* referenece id
	*/
	private int insertPlantReference(String authors, String otherCitationDetails )
  {
		int refId = 0; 
	 	try
	 	{
	 		StringBuffer sb = new StringBuffer();
			//first see if the reference already exists
			boolean refExists = plantReferenceExists(authors, otherCitationDetails);
			System.out.println("PlantTaxaLoader > ref exists: " + refExists);
			if (refExists == true)
			{
				refId = getPlantReferenceId(authors, otherCitationDetails);
			}
			else
			{
				//insert the strata values
				sb.append("INSERT into PLANTREFERENCE (authors, othercitationdetails) ");
				sb.append(" values(?,?)");
				PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
				// Bind the values to the query and execute it
 				pstmt.setString(1, authors);
 				pstmt.setString(2, otherCitationDetails);
				//execute the p statement
 				pstmt.execute();

				//now get the reference id to return
				refId = getPlantReferenceId(authors, otherCitationDetails);
				System.out.println("PlantTaxaLoader > new refId: " + refId);	
		}
	 }
		catch (Exception e)
	 {
		 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
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
			 System.out.println("PlantTaxaLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(plantNameId);
	 }
	 
	 
	
	/**
 	* method that returns true if an instance of a plant
 	* name exists that is identical to the plantName 
 	* input into the method
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
			//c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/plants_dev", "datauser", "");
			c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/plants_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	

	/**
	 * Main method -- for testing 
	 */
	public static void main(String[] args) 
	{
		PlantTaxaLoader loader = new PlantTaxaLoader();
		Hashtable h = new Hashtable();
		h.put("longName", "Abies bifoliaxxx");
		h.put("shortName", "green stem tree");
		h.put("code", "ABGS");
		h.put("taxonDescription", "A wicked green-stemed plant");
		h.put("salutation", "Mr.");
		h.put("givenName", "John");
		h.put("middleName", "H.");
		h.put("surName", "Harris");
		h.put("orgName", "NCEAS");
		h.put("email", "harris02@hotmail.com");
		h.put("citationDetails", "VB20020529");
		h.put("dateEntered", "2002-MAY-29");
		// load this plant
		loader.loadGenericPlantTaxa(h);
	}



	
}
