import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;



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
			try 
			{
				Connection conn = lb.manageLocalDbConnectionBroker("getConn");
				Statement query = conn.createStatement();
				ResultSet results = null;


				//create and issue the query --
				StringBuffer sqlBuf = new StringBuffer();
				if (taxonNameType.trim().equals("scientificName") )
				{
					sqlBuf.append("SELECT acceptedsynonym, status, concatenatedName, commonName, startDate, stopDate");
					sqlBuf.append(" from VEG_TAXA_SUMMARY where concatenatedName like '%"+taxonName+"%'");
				}
				else if ( taxonNameType.trim().equals("commonName")  ) 
				{
					sqlBuf.append("SELECT acceptedsynonym, status, concatenatedName, commonName, startDate, stopDate");
					sqlBuf.append(" from VEG_TAXA_SUMMARY where commonName like '%"+taxonName+"%'");
				}
				else 
				{
					System.out.println(" unrecognized taxonNameType: "+taxonNameType);
				}
				//issue the query
				results = query.executeQuery( sqlBuf.toString() );
			
				//retrieve the results
				while (results.next()) 
				{
					String acceptedSynonym = results.getString(1);
					String status = results.getString(2);
					String concatenatedName = results.getString(3);
					String commonName = results.getString(4);
					String startDate = results.getString(5);
					String stopDate = results.getString(6);
					returnVector.addElement( consolidateTaxaSummaryInstance( 
						acceptedSynonym, status, concatenatedName, commonName, startDate, 
						stopDate) );
					
					//System.out.println( acceptedSynonym+ " "+ status+ " "+concatenatedName);
				}
			
				//remember to close the connections etc..
			}
			catch (Exception e) 
			{
				System.out.println("failed " 
				+e.getMessage());
				e.printStackTrace();
			}
			return( returnVector );
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
			 if (acceptedSynonym != null)
			 {
			 	returnHash.put("acceptedSynonym", acceptedSynonym);
				returnHash.put("status", status);
				returnHash.put("concatenatedName", concatenatedName);
				returnHash.put("commonName", commonName);
				returnHash.put("startDate", startDate);
				returnHash.put("stopDate", stopDate);
			 }
			 return( returnHash );
		 }
		
	}
