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
	 * taxon name
	 *
	 * @param taxonName -- the 
	 *
 	 */
		public Vector getPlantTaxonSummary(String taxonName )
		{
			Vector returnVector = new Vector();
			try 
			{
				Connection conn = lb.manageLocalDbConnectionBroker("getConn");
				Statement query = conn.createStatement();
				ResultSet results = null;


				//create and issue the query to 
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("select acceptedsynonym, status, concatenatedName from ");
				sqlBuf.append("veg_taxa_summary where concatenatedName like '%"+taxonName+"%'");
			
				//issue the query
				results = query.executeQuery( sqlBuf.toString() );
			
				//retrieve the results
				while (results.next()) 
				{
					String acceptedSynonym = results.getString(1);
					String status = results.getString(2);
					String concatenatedName = results.getString(3);
					returnVector.addElement( consolidateTaxaSummaryInstance( 
						acceptedSynonym, status, concatenatedName) );
					
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
		 	String status, String concatenatedName)
		 {
			 Hashtable returnHash = new Hashtable();
			 if (acceptedSynonym != null)
			 {
			 	returnHash.put("acceptedSynonym", acceptedSynonym);
				returnHash.put("status", status);
				returnHash.put("concatenatedName", concatenatedName);
			 }
			 return( returnHash );
		 }
		
		
		
	}
