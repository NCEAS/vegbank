package databaseAccess;

/**
 *  '$RCSfile: TaxonomyQueryStore.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2002-11-27 22:11:27 $'
 * '$Revision: 1.1 $'
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


import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import databaseAccess.*;

/**
 * this class has been implemented to contain methods which are to be
 * used to query the plant taxonomy database - a database that follows 
 * the 'concept-based taxonomy' design following: Taswell, Peet, Jones
 * and others.
 * 
 * @author John Harris
 *
 */
	public class  TaxonomyQueryStore
	{
		LocalDbConnectionBroker lb = new LocalDbConnectionBroker();
		private Connection c = null;
		private String dbConnectString = ""; // the db connect string
		private ResourceBundle rb = ResourceBundle.getBundle("database");
	
	
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
			String s = rb.getString("plantdbconnectstring");
			this.dbConnectString = s;
			System.out.println("TaxonomyQueryStore > db connect string: " + s);
			//c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/plants_dev", "datauser", "");
			c = DriverManager.getConnection(s, "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	/**
	 * this method will return near matches for a given taxon name using the 
	 * logic the Mike Lee and J Harris came up with on June 8, 2002
	 * @param name -- the plant name
	 * @return v -- a vector with the near matches
	 */
	 public Vector getNameNearMatches(String name)
	 {
		Vector returnVector = new Vector();
		try
		{
			Connection conn = this.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = null;
			//create and issue the query --
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("SELECT plantname from PLANTNAME where upper(plantname) like '%" + name.toUpperCase()+ "%'  order by plantname");
			
			results = query.executeQuery( sqlBuf.toString() );
			//retrieve the results
			while (results.next()) 
			{
				String plantName = results.getString(1);
				returnVector.addElement(plantName);
			}
			//remember to close the connections etc..
	 	}
		catch (Exception e) 
		{
			System.out.println("failed " +e.getMessage());
			e.printStackTrace();
		}
		System.out.println("TaxonomyQueryStore > returning near matches: " + returnVector.size()  );
		return( returnVector );
	 }
	 
	 
	
	/**
 	 * method to query the plant taxonomy database using as input a 
	 * taxon name.  The structure of the vector that is returned is meant
	 * to parallel the structure of the 'taxa.dtd'
	 *
	 * @param taxonName -- the 
	 * @param taxonNameType -- the type of taxon name input by the client, may
	 * include: scientificName, commonName, symbolCode (the usda code)
	 *
 	 */
		public Vector getPlantTaxonSummary(String taxonName, String taxonNameType )
		{
			Vector returnVector = new Vector();
			StringBuffer sqlBuf = new StringBuffer();
			try 
			{
				
				Connection conn = this.getConnection();
				Statement query = conn.createStatement();
				ResultSet results = null;
				
				sqlBuf.append("SELECT plantname_id, plantconcept_id, plantName, ");
				sqlBuf.append(" plantDescription, plantNameStatus, classsystem, ");
				sqlBuf.append(" plantlevel, parentName, acceptedsynonym, plantConceptRefAuthor, plantConceptRefDate, plantUsagePartyOrgName");
				sqlBuf.append(" ,startDate, stopDate, plantNameAlias ");
				sqlBuf.append(" from VEG_TAXA_SUMMARY where upper(plantName) like '"+taxonName.toUpperCase()+"'");

				//issue the query
				results = query.executeQuery( sqlBuf.toString() );

				//retrieve the results
				while (results.next()) 
				{
					int plantNameId = results.getInt(1);
					int plantConceptId = results.getInt(2);
					String plantName = results.getString(3);
					String plantDescription = results.getString(4);
					String status = results.getString(5);
					String classSystem = results.getString(6);
					String plantLevel = results.getString(7);
					String parentName = results.getString(8);
					String acceptedSynonym = results.getString(9);
					String startDate = results.getString(10);
					String stopDate = results.getString(11);
					String plantNameAlias = results.getString(12);
					String plantConceptRefAuthor = results.getString(13);
					String plantConceptRefDate = results.getString(14);
					String plantUsagePartyOrgName = results.getString(15);
					
					//little trick to keep from breaking the code
					String commonName = plantName;
					String concatenatedName = plantName;
					
						// GET THE OTHER ATTRIBUTES 
					String code = this.getTaxonCode(plantConceptId);
					String comName = this.getCommonName(plantConceptId);
					String scientificName = this.getScientificName(plantConceptId);
					
					
					Hashtable h = consolidateTaxaSummaryInstance( 
						plantNameId,
						plantConceptId,
						plantName,
						plantDescription,
						status,
						classSystem,
						plantLevel,
						parentName,
						acceptedSynonym,
						startDate,
						stopDate, 
						plantNameAlias, code, comName,
						plantConceptRefAuthor,	plantConceptRefDate,plantUsagePartyOrgName,
						scientificName);
						
					returnVector.addElement(h);
					//System.out.println("TaxonomyQueryStore > hash: " + h.toString()   );
				}
			
				//remember to close the connections etc..
				query.close();
				conn.close();
			}
			catch (Exception e) 
			{
				System.out.println("failed " + e.getMessage());
				System.out.println("sql: " + sqlBuf.toString() );
				e.printStackTrace();
			}
			System.out.println("TaxonomyQueryStore > returning results: " + returnVector.size()  );
			return( returnVector );
		}

		
		
		
		
	/**
	 * method that is overloading the method above that in that this one
	 * allows an explicit request of a name, name type (eg, scientific, common)
	 * and level in the heirarchy (eg., genus, species) 
	 * method to query the plant taxonomy database using as input a 
	 * taxon name.  The structure of the vector that is returned is meant
	 * to parallel the structure of the 'taxa.dtd'
	 *
	 * @param taxonName -- the 
	 * @param taxonNameType -- the type of taxon name input by the client, may
	 * include: scientificName, commonName, symbolCode (the usda code)
	 * @param taxonLevel -- the level in the heirarchy (eg, species, genus)
	 * @param party -- the name of the party
	 * @param startDate -- the usage startDate 
	 * @param stopDate -- the usage stop date 
	 * 
	 * @see xmlWriter.writePlantTaxonSummary -- the xml writer used to write the 
	 * contents of the Vector returned by this method
	 * 
 	 */
		public Vector getPlantTaxonSummary(String taxonName, String taxonNameType,
		String taxonLevel, String party, String startDate, String stopDate)
		{
			Vector returnVector = new Vector();
			try 
			{
				// CONNECTION STUFF
				Connection conn = this.getConnection();
				Statement query = conn.createStatement();
				ResultSet results = null;
				
				// MAKE SURE THE INPUT CRITERIA ARE OK
				if (party == null || party.length() == 0 || party.trim().equals("null")){
					party = "%";
				}
				if (startDate == null || startDate.length() <= 4 ){
					startDate = "01-JAN-1900";
				}
				if (stopDate == null || stopDate.length() <= 4 ){
					stopDate = "01-JAN-2100";
				}
				
				//create and issue the query --
				StringBuffer sqlBuf = new StringBuffer();

				sqlBuf.append("SELECT plantname_id, plantconcept_id, plantName, ");
				sqlBuf.append(" plantDescription, plantNameStatus, classsystem, ");
				sqlBuf.append(" plantlevel, parentName, acceptedsynonym,  ");
				sqlBuf.append(" startDate, stopDate, plantNameAlias, plantConceptRefAuthor, plantConceptRefDate, plantUsagePartyOrgName  ");
				sqlBuf.append(" from VEG_TAXA_SUMMARY where upper(plantName) like '"+taxonName.toUpperCase()+"'");
				
				// ADD THE PARTY ELEMENT
				sqlBuf.append(" and upper(PLANTUSAGEPARTYORGNAME) like '%"+party.toUpperCase()+"%' ");
				// ADD THE DATE RESTRICTION
				sqlBuf.append(" and STARTDATE > '"+startDate+"' and STOPDATE < '"+stopDate+"'  ");
				// ADD THE PLANTLEVEL 
				sqlBuf.append(" and upper(PLANTLEVEL) like  '"+taxonLevel.toUpperCase()+"'");
				// ADD THE NAME TYPE
				sqlBuf.append(" and ( CLASSSYSTEM like '"+taxonNameType+"' ");
				sqlBuf.append(" or  upper(CLASSSYSTEM) like '"+taxonNameType.toUpperCase()+"' ");
				sqlBuf.append(")");

				System.out.println("TaxonomyQueryStore > query: " + sqlBuf.toString() );	
				results = query.executeQuery( sqlBuf.toString() );
			
				//retrieve the results
				while (results.next()) 
				{
					int plantNameId = results.getInt(1);
					int plantConceptId = results.getInt(2);
					String plantName = results.getString(3);
					plantName = this.removeSensitiveChars(plantName);
					String plantDescription = results.getString(4);
					plantDescription = this.removeSensitiveChars(plantDescription);
					String status = results.getString(5);
					String classSystem = results.getString(6);
					String plantLevel = results.getString(7);
					String parentName = results.getString(8);
					parentName = this.removeSensitiveChars(parentName);
					String acceptedSynonym = results.getString(9);
					acceptedSynonym = this.removeSensitiveChars(acceptedSynonym);
					startDate = results.getString(10);
					stopDate = results.getString(11);
					String plantNameAlias = results.getString(12);
					plantNameAlias = this.removeSensitiveChars(plantNameAlias);
					String plantConceptRefAuthor = results.getString(13);
					plantConceptRefAuthor = this.removeSensitiveChars(plantConceptRefAuthor);
					String plantConceptRefDate = results.getString(14);
					String plantUsagePartyOrgName = results.getString(15);
					
					//little trick to keep from breaking the code
					String commonName = plantName;
					String concatenatedName = plantName;
					
					// GET THE OTHER ATTRIBUTES 
					String code = this.getTaxonCode(plantConceptId);
					code = this.removeSensitiveChars(code);
					String comName = this.getCommonName(plantConceptId);
					comName = this.removeSensitiveChars(comName);
					String scientificName = this.getScientificName(plantConceptId);
					scientificName = this.removeSensitiveChars(scientificName);
					
					Hashtable h = consolidateTaxaSummaryInstance( 
						plantNameId,
						plantConceptId,
						plantName,
						plantDescription,
						status,
						classSystem,
						plantLevel,
						parentName,
						acceptedSynonym,
						startDate,
						stopDate,
						plantNameAlias,
						code,
						comName, 
						plantConceptRefAuthor, plantConceptRefDate, plantUsagePartyOrgName, 
						scientificName);	
					returnVector.addElement(h);
				}
				//remember to close the connections etc..
				results.close();
				query.close();
				conn.close();
			}
			catch (Exception e) 
			{
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			}
			System.out.println("TaxonomyQueryStore > returning results: " + returnVector.size()  );
			return( returnVector );
		}
		
		
				
	/**
	 * method that is overloading the method above that in that this one
	 * allows an explicit request of a name, name type (eg, scientific, common)
	 * and level in the heirarchy (eg., genus, species) 
	 * method to query the plant taxonomy database using as input a 
	 * taxon name.  The structure of the vector that is returned is meant
	 * to parallel the structure of the 'taxa.dtd'
	 *
	 * @param taxonName -- the 
	 * @param taxonNameType -- the type of taxon name input by the client, may
	 * include: scientificName, commonName, symbolCode (the usda code)
	 * @param taxonLevel -- the level in the heirarchy (eg, species, genus)
	 * @param party -- the name of the party
	 * @param targetDate -- the targetDate which is to be between the start and the 
	 * stop date (for example if the date 01-JAN-1997 is enterered then the plants 
	 * associated with that time interval will be queried 
	 * 
	 * @see xmlWriter.writePlantTaxonSummary -- the xml writer used to write the 
	 * contents of the Vector returned by this method
	 * 
 	 */
		public Vector getPlantTaxonSummary(String taxonName, String taxonNameType,
		String taxonLevel, String party, String targetDate)
		{
			Vector returnVector = new Vector();
			try 
			{
				// CONNECTION STUFF
				Connection conn = this.getConnection();
				Statement query = conn.createStatement();
				ResultSet results = null;
				
				// MAKE SURE THE INPUT CRITERIA ARE OK
				if (party == null || party.length() == 0 || party.trim().equals("null"))
				{
					party = "%";
				}
				if (targetDate == null || targetDate.length() <= 4 )
				{
					targetDate = "now()";
				}
				
				
				//create and issue the query --
				StringBuffer sqlBuf = new StringBuffer();

				sqlBuf.append("SELECT plantname_id, plantconcept_id, plantName, ");
				sqlBuf.append(" plantDescription, plantNameStatus, classsystem, ");
				sqlBuf.append(" plantlevel, parentName, acceptedsynonym,  ");
				sqlBuf.append(" startDate, stopDate, plantNameAlias, plantConceptRefAuthor, plantConceptRefDate, plantUsagePartyOrgName  ");
				sqlBuf.append(" from VEG_TAXA_SUMMARY where upper(plantName) like '"+taxonName.toUpperCase()+"'");
				
				// ADD THE PARTY ELEMENT
				sqlBuf.append(" and upper(PLANTUSAGEPARTYORGNAME) like '%"+party.toUpperCase()+"%' ");
				// ADD THE DATE RESTRICTION
				sqlBuf.append(" and STARTDATE <= '"+targetDate+"' and STOPDATE >= '"+targetDate+"'  ");
				// ADD THE PLANTLEVEL 
				sqlBuf.append(" and upper(PLANTLEVEL) like  '"+taxonLevel.toUpperCase()+"'");
				// ADD THE NAME TYPE
				sqlBuf.append(" and ( CLASSSYSTEM like '"+taxonNameType+"' ");
				sqlBuf.append(" or  upper(CLASSSYSTEM) like '"+taxonNameType.toUpperCase()+"' ");
				sqlBuf.append(")");

				System.out.println("TaxonomyQueryStore > query: " + sqlBuf.toString() );	
				results = query.executeQuery( sqlBuf.toString() );
			
				//retrieve the results
				while (results.next()) 
				{
					int plantNameId = results.getInt(1);
					int plantConceptId = results.getInt(2);
					String plantName = results.getString(3);
					plantName = this.removeSensitiveChars(plantName);
					String plantDescription = results.getString(4);
					plantDescription = this.removeSensitiveChars(plantDescription);
					String status = results.getString(5);
					String classSystem = results.getString(6);
					String plantLevel = results.getString(7);
					String parentName = results.getString(8);
					parentName = this.removeSensitiveChars(parentName);
					String acceptedSynonym = results.getString(9);
					acceptedSynonym = this.removeSensitiveChars(acceptedSynonym);
					String startDate = results.getString(10);
					String stopDate = results.getString(11);
					String plantNameAlias = results.getString(12);
					plantNameAlias = this.removeSensitiveChars(plantNameAlias);
					String plantConceptRefAuthor = results.getString(13);
					plantConceptRefAuthor = this.removeSensitiveChars(plantConceptRefAuthor);
					String plantConceptRefDate = results.getString(14);
					String plantUsagePartyOrgName = results.getString(15);
					
					//little trick to keep from breaking the code
					String commonName = plantName;
					String concatenatedName = plantName;
					
					// GET THE OTHER ATTRIBUTES 
					String code = this.getTaxonCode(plantConceptId);
					code = this.removeSensitiveChars(code);
					String comName = this.getCommonName(plantConceptId);
					comName = this.removeSensitiveChars(comName);
					String scientificName = this.getScientificName(plantConceptId);
					scientificName = this.removeSensitiveChars(scientificName);
					
					Hashtable h = consolidateTaxaSummaryInstance( 
						plantNameId,
						plantConceptId,
						plantName,
						plantDescription,
						status,
						classSystem,
						plantLevel,
						parentName,
						acceptedSynonym,
						startDate,
						stopDate,
						plantNameAlias,
						code,
						comName, 
						plantConceptRefAuthor, plantConceptRefDate, plantUsagePartyOrgName, 
						scientificName);	
					returnVector.addElement(h);
				}
				//remember to close the connections etc..
				results.close();
				query.close();
				conn.close();
			}
			catch (Exception e) 
			{
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			}
			System.out.println("TaxonomyQueryStore > returning results: " + returnVector.size()  );
			return( returnVector );
		}
		
		
		
		/**
		 * this is a utility method that is used to remove or replace sensitive 
		 * charaters which cause the xml parser to fail
		 * @param inString -- the original input string containing, possibly, sensitive 
		 * characters
		 * @param outString -- the reformated outString
		 */
		 private String removeSensitiveChars(String inString)
		 {
			 String outString = "";
			 try 
			 {
				 if ( inString != null && inString.length() > 1 )
				 {
					 outString = inString.replace('&', '_');
				 }
			 }
			 catch (Exception e) 
			 {
				 System.out.println("Exception " + e.getMessage());
				 e.printStackTrace();
			 }
			 return(outString);
		 }
		 
		/**
		 * method to lookup a commonname based on a concept id.
		 * The concept for a given plant offen correponds to multiple 
		 * names in the database.  So this method will look up those 
		 * names that correspond to the concept that are common names 
		 * or short names.
		 * @param conceptId -- the unique plant concept
		 */
		 private String getCommonName(int conceptId )
		 {
			 String commonName = "";
			 StringBuffer sb = new StringBuffer();
			 try
			 {
				 Connection conn = this.getConnection();
				 Statement query = conn.createStatement();
				 ResultSet results = null;
				 sb.append("select plantname from plantusage where plantconcept_id = "+conceptId+" and ");
				 sb.append(" ( upper(classsystem) like 'COMMON NAME' or upper(classsystem) like 'SHORTNAME' ) ");
				 results = query.executeQuery( sb.toString() );
				 while (results.next()) 
				 {
					 String plantName = results.getString(1);
					 System.out.println("## > " + plantName);
					 commonName = plantName;
				 }
				 query.close();
				 results.close();
				 conn.close();
			 }
			 catch (Exception e) 
			 {
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			 }
			 return( commonName );
		 }
		
		
		/**
		 * method to lookup a scientific name based on a concept id.
		 * The concept for a given plant offen correponds to multiple 
		 * names in the database.  So this method will look up those 
		 * names that correspond to the concept that are scientific names 
		 * or long names.
		 * @param conceptId -- the unique plant concept
		 */
		 private String getScientificName(int conceptId )
		 {
			 String sciName = "";
			 StringBuffer sb = new StringBuffer();
			 try
			 {
				 Connection conn = this.getConnection();
				 Statement query = conn.createStatement();
				 ResultSet results = null;
				 sb.append("select plantname from plantusage where plantconcept_id = "+conceptId+" and ");
				 sb.append(" ( upper(classsystem)  like 'SCI%' or upper(classsystem) like 'LONG%') ");
				 results = query.executeQuery( sb.toString() );
				 while (results.next()) 
				 {
					 String plantName = results.getString(1);
					 System.out.println("## > " + plantName);
					 sciName = plantName;
				 }
				 query.close();
				 results.close();
				 conn.close();
			 }
			 catch (Exception e) 
			 {
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			 }
			 return( sciName );
		 }
		
		
		
		/**
		 * method to lookup a taxon code
		 */
		 private String getTaxonCode(int conceptId )
		 {
			 String code = "";
			 StringBuffer sb = new StringBuffer();
			 try
			 {
				 Connection conn = this.getConnection();
				 Statement query = conn.createStatement();
				 ResultSet results = null;
				 sb.append("select plantname from plantusage where plantconcept_id = "+conceptId+" and ");
				 sb.append(" classsystem like 'CODE'");
				 results = query.executeQuery( sb.toString() );
				 while (results.next()) 
				 {
					 String plantName = results.getString(1);
					 System.out.println("## > " + plantName);
					 code = plantName;
				 }
				 query.close();
				 results.close();
				 conn.close();
			 }
			 catch (Exception e) 
			 {
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			 }
			 return( code );
		 }
		
	


	
		/**
		 * method that consolidates the summary data from the database into 
		 * a hashtable, with appropriate key names and passes back the hashtable
		 * which is in turn appended to the results vector
		 *
		 * @param acceptedSynonym -- the accepted name if the taxon is, itself,
		 *		not accepted
		 * @param plantNameAlias -- this is the plantName with the author token too.
		 */
		private Hashtable consolidateTaxaSummaryInstance( int plantNameId,
		int plantConceptId, String plantName, String plantDescription,
		String status, String	classSystem, String	plantLevel, String parentName,
		String acceptedSynonym, String startDate, String stopDate, String plantNameAlias,
		String code, String commonName, String plantConceptRefAuthor, String 	plantConceptRefDate,
		String plantUsagePartyOrgName, String scientificName)
		{
			Hashtable returnHash = new Hashtable();
			 try
			 {
				 //replace the null values that were retrieved from the database
					if (plantName == null )
					{
						plantName ="";
					}
					if (plantDescription == null )
					{
						 plantDescription="";
					}
					if (status == null )
					{
						 status="";
					}
					if (classSystem == null )
					{
						 classSystem="";
					}
					if (plantLevel == null )
					{
						 plantLevel="";
					}
					if (parentName == null )
					{
						 parentName="";
					}
					if (acceptedSynonym == null )
					{
						 acceptedSynonym="";
					}
					if (startDate == null )
					{
						 startDate="";
					}
					if (stopDate == null )
					{
						 stopDate="";
					}
					if (plantNameAlias == null )
					{
						 plantNameAlias="";
					}
					if ( plantDescription  == null || plantDescription.trim().startsWith("null") )
					{
						plantDescription = "";
					}
					if ( plantNameAlias == null || plantNameAlias.trim().startsWith("null") )
					{
						plantNameAlias  = "";
					}
					if ( code == null || code.trim().startsWith("null") )
					{
						code = "";
					}
					if ( commonName == null || commonName.trim().startsWith("null") )
					{
						commonName = "";
					}
					if ( plantConceptRefAuthor == null || plantConceptRefAuthor.trim().startsWith("null")  )
					{
						plantConceptRefAuthor = "";
					}
					if ( plantConceptRefDate == null || plantConceptRefDate.trim().startsWith("null")  )
					{
						 plantConceptRefDate = "";
					}
					if ( plantUsagePartyOrgName == null || plantUsagePartyOrgName.trim().startsWith("null") )
					{
						plantUsagePartyOrgName = "";
					}
					if ( scientificName == null || scientificName.trim().startsWith("null") )
					{
						scientificName = "";
					}
					
					returnHash.put("plantNameId", ""+plantNameId);
					returnHash.put("plantConceptId", ""+plantConceptId);
			 		returnHash.put("plantName", plantName);
					returnHash.put("status", status);
					returnHash.put("classSystem", classSystem);
			 		returnHash.put("plantLevel", plantLevel);
					returnHash.put("parentName", parentName);
					returnHash.put("acceptedSynonym", acceptedSynonym);
					returnHash.put("startDate", startDate);
					returnHash.put("stopDate", stopDate);
					returnHash.put("plantDescription", plantDescription);
					returnHash.put("plantNameAlias", plantNameAlias);
					returnHash.put("code", code);
					returnHash.put("commonName", commonName);
					returnHash.put("plantConceptRefAuthor",	plantConceptRefAuthor);
					returnHash.put("plantConceptRefDate", plantConceptRefDate);
					returnHash.put("plantUsagePartyOrgName", plantUsagePartyOrgName);
					returnHash.put("scientificName", scientificName);
					
			 }
			 catch(Exception e)
			 {
				 System.out.println("Exception : " + e.getMessage());
				 e.printStackTrace();
			 }
			 return( returnHash );
		 }
		
		
		/**
		 * method that consolidates the summary data from the database into 
		 * a hashtable, with appropriate key names and passes back the hashtable
		 * which is in turn appended to the results vector
		 *
		 * @param acceptedSynonym -- the accepted name if the taxon is, itself,
		 *		not accepted
		 */
		 
		 private Hashtable consolidateTaxaSummaryInstance ( String acceptedSynonym,
		 	String status, String concatenatedName, String commonName, 
			String startDate, String stopDate)
		 {
			 Hashtable returnHash = new Hashtable();
			 try
			 {
				 //replace the null values that were retrieved from the database
					if (acceptedSynonym == null )
					{
						acceptedSynonym ="";
					}
					if (status == null )
					{
						status ="";
					}
					if (concatenatedName == null )
					{
						concatenatedName ="";
					}
					if (commonName == null )
					{
						commonName ="";
					}
					if (startDate == null )
					{
						startDate ="";
					}
					if (stopDate == null )
					{
						stopDate ="";
					}
					
					if (concatenatedName != null)
			 		{
			 			returnHash.put("acceptedSynonym", acceptedSynonym);
						returnHash.put("status", status);
						returnHash.put("concatenatedName", concatenatedName);
						returnHash.put("commonName", commonName);
						returnHash.put("startDate", startDate);
						returnHash.put("stopDate", stopDate);
			 		}
			 }
			 catch(Exception e)
			 {
				 System.out.println("Exception : " + e.getMessage());
				 e.printStackTrace();
			 }
			 return( returnHash );
		 }
		 
		 /** 
		  * method that retuns the name reference for a plant name
			*/
			public Hashtable getPlantNameReference(String name)
			{
				Hashtable h = new Hashtable();
				h.put("plantName", ""+name);
				StringBuffer sqlBuf = new StringBuffer();
				try 
				{
					// connection stuff
					Connection conn = this.getConnection();
					Statement query = conn.createStatement();
					ResultSet results = null;

					// create and issue the query --
					sqlBuf.append("select  PLANTREFERENCE_ID, AUTHORS, OTHERCITATIONDETAILS, ");
					sqlBuf.append("TITLE, PUBDATE, EDITION, SERIESNAME, ISSUEIDENTIFICATION, ");
					sqlBuf.append(" PAGE, TABLECITED, ISBN, ISSN, PLANTDESCRIPTION ");
					sqlBuf.append(" from PLANTREFERENCE where PLANTREFERENCE_ID = ( ");
					sqlBuf.append(" select PLANTREFERENCE_ID from PLANTNAME where PLANTNAME like ");
					sqlBuf.append( "'" +name+"' )");
					results = query.executeQuery( sqlBuf.toString() );
					
					//retrieve the results
					while (results.next()) 
					{
						int plantReferenceId = results.getInt(1);
						String authors = results.getString(2);
						String otherCitationDetails = results.getString(3);
						String title = results.getString(4);
						String pubDate = results.getString(5);
						String edition = results.getString(6);
						String seriesName = results.getString(7);
						String issueIdentification = results.getString(8);
						String page = results.getString(9);
						String tableCited = results.getString(10);
						String isbn = results.getString(11);
						String issn = results.getString(12);
						String plantDescription= results.getString(13);
						
						h.put("plantReferenceId", ""+plantReferenceId);
						h.put("authors", ""+authors);
						h.put("otherCitationDetails", ""+otherCitationDetails);
						h.put("title", ""+title);
						h.put("pubDate", ""+pubDate);
						h.put("edition", ""+edition);
						h.put("seriesName", ""+seriesName);
						h.put("issueIdentification", ""+issueIdentification );
						h.put("page", ""+page);
						h.put("tableCited", ""+tableCited );
						h.put("isbn", ""+isbn);
						h.put("issn", ""+issn);
						
					}
					//remember to close the connections etc..
				}
				catch (Exception e) 
				{
					System.out.println("failed " + e.getMessage() );
					System.out.println("sql: " + sqlBuf.toString() );
					e.printStackTrace();
				}
				return( h );
			}
		 
		 
		 
		/**
		 * Main method for testing
		 */  
		 public static void main(String[] args)
		 {
			 //test the get plant name reference method
			 TaxonomyQueryStore qs = new TaxonomyQueryStore();
			 Hashtable h = qs.getPlantNameReference("Big Bush");
			 System.out.println("TaxonomyQueryStore > plantname reference" + h.toString()  );
		 }
		
	}
