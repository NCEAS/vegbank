package databaseAccess;

/**
 *  '$RCSfile: TaxonomyQueryStore.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 03:33:34 $'
 *	'$Revision: 1.6 $'
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.utility.*;

/**
 * this class has been implemented to contain methods which are to be
 * used to query the plant taxonomy database - a database that follows 
 * the 'concept-based taxonomy' design following: Taswell, Peet, Jones
 * and others.
 * 
 * @author John Harris
 *
 */
public class TaxonomyQueryStore implements Constants
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
			this.dbConnectString = rb.getString("connectString");
			String driverClass = rb.getString("driverClass");
			String user = rb.getString("user");
			String password = rb.getString("password");
			
			Class.forName(driverClass);
			System.out.println("TaxonomyQueryStore > db connect string: " + dbConnectString);
			c = DriverManager.getConnection(dbConnectString, user, password);
		}
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (c);
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
			sqlBuf.append(
				"SELECT plantname from PLANTNAME where upper(plantname) like '%"
					+ name.toUpperCase()
					+ "%'  order by plantname");

			results = query.executeQuery(sqlBuf.toString());
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
			System.out.println("failed " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(
			"TaxonomyQueryStore > returning near matches: " + returnVector.size());
		return (returnVector);
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
	public Vector getPlantTaxonSummary(
		String taxonName,
		String taxonNameType,
		String taxonLevel,
		String party,
		String startDate,
		String stopDate)
	{
		Vector returnVector = new Vector();
		try
		{
			// CONNECTION STUFF
			Connection conn = this.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = null;

			//create and issue the query --
			StringBuffer sqlBuf = new StringBuffer();
			
			
			sqlBuf.append(
				"SELECT plantconcept.plantname_id, plantconcept.plantconcept_id, "
				+ "plantusage.plantname, plantconcept.plantdescription, " 
				+ "plantstatus.plantconceptstatus,  plantusage.classsystem, "
				+ "plantstatus.plantlevel, plantstatus.plantparentname, "
				+ "plantusage.acceptedsynonym,  plantstatus.startdate, plantstatus.stopdate, "
				+ "plantparty.organizationname "
				+ "FROM plantconcept, plantstatus, plantusage, plantparty "
				+ "WHERE plantconcept.plantconcept_id = plantusage.plantconcept_id "
				+ "and plantconcept.plantconcept_id = plantstatus.plantconcept_id "
				+ "and plantusage.plantparty_id = plantparty.plantparty_id"
			);
			
			sqlBuf.append(
				" and upper(plantusage.plantname) like '" + taxonName.toUpperCase()+ "'");

			// Add the party if needed
			if (party != null  && ! (party.trim()).equals("")  )
			{
				sqlBuf.append(
					" and upper(plantparty.organizationname) like '%"+party.toUpperCase()+"%' "
					);
			}
					
			// Add date restrictions if needed
			if (startDate != null  && ! (startDate.trim()).equals("")  )
			{
				sqlBuf.append(" and plantstatus.startdate <'" + startDate + "'" );
			}
			if (stopDate != null && ! (stopDate.trim()).equals("") )
			{
				sqlBuf.append("and ( plantstatus.stopdate > '" + stopDate + "' or plantstatus.stopdate IS NULL )" );
			}
			
			// Add the plantlevel if needed
			if ( taxonLevel != null && ! (taxonLevel.trim()).equals("") )
			{
				sqlBuf.append(
					" and upper(plantstatus.plantlevel) =  '" + taxonLevel.toUpperCase() + "'"
				);
			}

			//		Add the nametype if needed
			if ( taxonNameType != null && ! (taxonNameType.trim()).equals("") )
			{
				sqlBuf.append(
					" and  upper(plantusage.classsystem) = '" + taxonNameType.toUpperCase() + "' "
				);				
			}


			System.out.println("TaxonomyQueryStore > query: " + sqlBuf.toString());
			results = query.executeQuery(sqlBuf.toString());

			//retrieve the results
			while (results.next())
			{
				int plantNameId = results.getInt(1);
				int plantConceptId = results.getInt(2);
				String plantName = this.removeSensitiveChars(results.getString(3));
				String plantDescription = this.removeSensitiveChars(results.getString(4));
				String status = results.getString(5);
				String classSystem = results.getString(6);
				String plantLevel = results.getString(7);
				String parentName = this.removeSensitiveChars(results.getString(8));
				String acceptedSynonym = 
					this.removeSensitiveChars(results.getString(9));
				startDate = results.getString(10);
				stopDate = results.getString(11);
//				String plantNameAlias =  
//					this.removeSensitiveChars(results.getString(12));
//				String plantConceptRefAuthor =
//					this.removeSensitiveChars(results.getString(13));
//				String plantConceptRefDate = results.getString(14);
//				String plantUsagePartyOrgName = results.getString(15);

				//little trick to keep from breaking the code
				String commonName = plantName;
				String concatenatedName = plantName;

				// GET THE OTHER ATTRIBUTES 
				String code = 
					this.removeSensitiveChars( this.getTaxonCode(plantConceptId) );

				String comName = 
					this.removeSensitiveChars( this.getCommonName(plantConceptId) );

				String scientificName = 
					this.removeSensitiveChars( this.getScientificName(plantConceptId) );
					
				String scientificNameWA = 
					this.removeSensitiveChars( 
						this.getScientificNameWithAuthors(plantConceptId) 
					);

				Hashtable h =
					consolidateTaxaSummaryInstance(
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
						null, //plantNameAlias,
						code,
						comName,
						null, //plantConceptRefAuthor,
						null, //plantConceptRefDate,
						null, //plantUsagePartyOrgName,
						scientificName,
						scientificNameWA);
				returnVector.addElement(h);
			}
			//remember to close the connections etc..
			results.close();
			query.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(
			"TaxonomyQueryStore > returning results: " + returnVector.size());
		return (returnVector);
	}

	/**
	 *	Overloads getPlantTaxonSummary to handle requests that have a targetDate
	 *
	 * @param taxonName -- the plant name
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
	public Vector getPlantTaxonSummary(
		String taxonName,
		String taxonNameType,
		String taxonLevel,
		String party,
		String targetDate)
	{
		Vector returnVector =
			this.getPlantTaxonSummary(
				taxonName,
				taxonNameType,
				taxonLevel,
				party,
				targetDate,
				targetDate);

		return (returnVector);
	}

	
	public Vector getPlantTaxonSummary(String taxonName, String taxonNameType )
	{
		Vector returnVector =
					this.getPlantTaxonSummary(
						taxonName,
						taxonNameType,
						null,
						null,
						null,
						null);

				return (returnVector);
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
			if (inString != null && inString.length() > 1)
			{
				outString = inString.replace('&', '_');
			}
		}
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception " + e.getMessage());
			e.printStackTrace();
		}
		return (outString);
	}

	/**
	 * method to lookup a commonname based on a concept id.
	 * The concept for a given plant offen correponds to multiple 
	 * names in the database.  So this method will look up those 
	 * names that correspond to the concept that are common names 
	 * or short names.
	 * @param conceptId -- the unique plant concept
	 */
	private String getCommonName(int conceptId)
	{
		String commonName =
			this.getTaxonName(conceptId, USAGE_NAME_COMMON);

		return (commonName);
	}

	/**
	 * method to lookup a scientific name based on a concept id.
	 * The concept for a given plant offen correponds to multiple 
	 * names in the database.  So this method will look up those 
	 * names that correspond to the concept that are scientific names 
	 * or long names.
	 * @param conceptId -- the unique plant concept
	 */
	private String getScientificName(int conceptId)
	{
		String scientificName = 
			this.getTaxonName(conceptId, USAGE_NAME_SCIENTIFIC_NOAUTHORS);
		return (scientificName);
	}
	
	/**
	 * method to lookup a scientific name with authors  based on a concept id.
	 * The concept for a given plant offen correponds to multiple 
	 * names in the database.  So this method will look up those 
	 * names that correspond to the concept that are scientific names 
	 * or long names.
	 * @param conceptId -- the unique plant concept
	 */
	private String getScientificNameWithAuthors(int conceptId)
	{
		String scientificNameWA = 
			this.getTaxonName(conceptId, USAGE_NAME_SCIENTIFIC);
		return (scientificNameWA);
	}	

	/**
	 * method to lookup a taxon code
	 */
	private String getTaxonCode(int conceptId)
	{
		String code = this.getTaxonName(conceptId, USAGE_NAME_CODE);
		return (code);
	}
	
	private String getTaxonName (int conceptId, String classType)
	{
		String plantName = null;
		StringBuffer sb = new StringBuffer();
		try
		{
			Connection conn = this.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = null;
			sb.append(
				"select plantname from plantusage where plantconcept_id = "
					+ conceptId
					+ " and classsystem = '" + classType + "'");
			results = query.executeQuery(sb.toString());
			while (results.next())
			{
				plantName = results.getString(1);
				System.out.println("TaxonomyQueryStore > " + classType + " : " + plantName);
			}
			query.close();
			results.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception " + e.getMessage());
			e.printStackTrace();
		}		
		
		return plantName;
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
	private Hashtable consolidateTaxaSummaryInstance(
		int plantNameId,
		int plantConceptId,
		String plantName,
		String plantDescription,
		String status,
		String classSystem,
		String plantLevel,
		String parentName,
		String acceptedSynonym,
		String startDate,
		String stopDate,
		String plantNameAlias,
		String code,
		String commonName,
		String plantConceptRefAuthor,
		String plantConceptRefDate,
		String plantUsagePartyOrgName,
		String scientificName,
		String scientificNameWA)
	{
		Hashtable returnHash = new Hashtable();
		try
		{
			//replace the null values that were retrieved from the database
			if (plantName == null)
			{
				plantName = "";
			}
			if (plantDescription == null)
			{
				plantDescription = "";
			}
			if (status == null)
			{
				status = "";
			}
			if (classSystem == null)
			{
				classSystem = "";
			}
			if (plantLevel == null)
			{
				plantLevel = "";
			}
			if (parentName == null)
			{
				parentName = "";
			}
			if (acceptedSynonym == null)
			{
				acceptedSynonym = "";
			}
			if (startDate == null)
			{
				startDate = "";
			}
			if (stopDate == null)
			{
				stopDate = "";
			}
			if (plantNameAlias == null)
			{
				plantNameAlias = "";
			}
			if (plantDescription == null
				|| plantDescription.trim().startsWith("null"))
			{
				plantDescription = "";
			}
			if (plantNameAlias == null || plantNameAlias.trim().startsWith("null"))
			{
				plantNameAlias = "";
			}
			if (code == null || code.trim().startsWith("null"))
			{
				code = "";
			}
			if (commonName == null || commonName.trim().startsWith("null"))
			{
				commonName = "";
			}
			if (plantConceptRefAuthor == null
				|| plantConceptRefAuthor.trim().startsWith("null"))
			{
				plantConceptRefAuthor = "";
			}
			if (plantConceptRefDate == null
				|| plantConceptRefDate.trim().startsWith("null"))
			{
				plantConceptRefDate = "";
			}
			if (plantUsagePartyOrgName == null
				|| plantUsagePartyOrgName.trim().startsWith("null"))
			{
				plantUsagePartyOrgName = "";
			}
			if (scientificName == null || scientificName.trim().startsWith("null"))
			{
				scientificName = "";
			}
			if (scientificNameWA == null || scientificNameWA.trim().startsWith("null"))
			{
				scientificNameWA = "";
			}


			returnHash.put("plantNameId", "" + plantNameId);
			returnHash.put("plantConceptId", "" + plantConceptId);
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
			returnHash.put("plantConceptRefAuthor", plantConceptRefAuthor);
			returnHash.put("plantConceptRefDate", plantConceptRefDate);
			returnHash.put("plantUsagePartyOrgName", plantUsagePartyOrgName);
			returnHash.put("scientificName", scientificName);
			returnHash.put("scientificNameWithAuthors", scientificNameWA);
		}
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return (returnHash);
	}

	/**
	 * method that consolidates the summary data from the database into 
	 * a hashtable, with appropriate key names and passes back the hashtable
	 * which is in turn appended to the results vector
	 *
	 * @param acceptedSynonym -- the accepted name if the taxon is, itself,
	 *		not accepted
	 */

	private Hashtable consolidateTaxaSummaryInstance(
		String acceptedSynonym,
		String status,
		String concatenatedName,
		String commonName,
		String startDate,
		String stopDate)
	{
		Hashtable returnHash = new Hashtable();
		try
		{
			//replace the null values that were retrieved from the database
			if (acceptedSynonym == null)
			{
				acceptedSynonym = "";
			}
			if (status == null)
			{
				status = "";
			}
			if (concatenatedName == null)
			{
				concatenatedName = "";
			}
			if (commonName == null)
			{
				commonName = "";
			}
			if (startDate == null)
			{
				startDate = "";
			}
			if (stopDate == null)
			{
				stopDate = "";
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
		catch (Exception e)
		{
			System.out.println("TaxonomyQueryStore > Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return (returnHash);
	}

	/** 
	 * method that retuns the name reference for a plant name
		*/
	public Hashtable getPlantNameReference(String name)
	{
		Hashtable h = new Hashtable();
		h.put("plantName", "" + name);
		StringBuffer sqlBuf = new StringBuffer();
		try
		{
			// connection stuff
			Connection conn = this.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = null;


			String referenenceQuery = 
				"select  reference.REFERENCE_ID, additionalinfo, TITLE, PUBDATE, " +				"EDITION, PAGERANGE, ISBN from REFERENCE, referencecontributor, " +				"referenceparty where reference.REFERENCE_ID = " +				"(  select REFERENCE_ID from PLANTNAME where PLANTNAME ="; 
			// create and issue the query --
			sqlBuf.append(referenenceQuery);
			sqlBuf.append("'" + name + "' )");
			results = query.executeQuery(sqlBuf.toString());

			//retrieve the results
			while (results.next())
			{
				int plantReferenceId = results.getInt(1);
				//String authors = results.getString(2);
				String otherCitationDetails = results.getString(2);
				String title = results.getString(3);
				String pubDate = results.getString(4);
				String edition = results.getString(5);
				//String seriesName = results.getString(7);
				//String issueIdentification = results.getString(8);
				String page = results.getString(6);
				//String tableCited = results.getString(10);
				String isbn = results.getString(7);
				//String issn = results.getString(12);
				//String plantDescription = results.getString(13);

				h.put("plantReferenceId", "" + plantReferenceId);
				//h.put("authors", "" + authors);
				h.put("otherCitationDetails", "" + otherCitationDetails);
				h.put("title", "" + title);
				h.put("pubDate", "" + pubDate);
				h.put("edition", "" + edition);
				//h.put("seriesName", "" + seriesName);
				//h.put("issueIdentification", "" + issueIdentification);
				h.put("page", "" + page);
				//h.put("tableCited", "" + tableCited);
				h.put("isbn", "" + isbn);
				//h.put("issn", "" + issn);

			}
			//remember to close the connections etc..
		}
		catch (Exception e)
		{
			System.out.println("failed " + e.getMessage());
			System.out.println("sql: " + sqlBuf.toString());
			e.printStackTrace();
		}
		return (h);
	}

	/**
	 * Main method for testing
	 */
	public static void main(String[] args)
	{
		//test the get plant name reference method
		TaxonomyQueryStore qs = new TaxonomyQueryStore();
		Hashtable h = qs.getPlantNameReference("Big Bush");
		System.out.println(
			"TaxonomyQueryStore > plantname reference" + h.toString());
	}

}
