package databaseAccess;

/**
 *  '$RCSfile: TaxonomyQueryStore.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-02-27 00:15:54 $'
 * '$Revision: 1.4 $'
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
		Connection c = null;
	
	
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
			c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/plants_dev", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
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
			try 
			{
				
				//get rid of the dependence on the connection broker
				//Connection conn = lb.manageLocalDbConnectionBroker("getConn");
				
				Connection conn = this.getConnection();
				Statement query = conn.createStatement();
				ResultSet results = null;
				
				//create and issue the query --
				StringBuffer sqlBuf = new StringBuffer();
				if (taxonNameType.trim().equals("scientificName") )
				{
					sqlBuf.append("SELECT acceptedsynonym, plantnamestatus, ");
					sqlBuf.append(" plantName, parentName, startDate, stopDate");
					sqlBuf.append(" from VEG_TAXA_SUMMARY where plantName like '%"+taxonName+"%'");
				}
				else if ( taxonNameType.trim().equals("commonName")  ) 
				{
					sqlBuf.append("SELECT acceptedsynonym, plantnamestatus, ");
					sqlBuf.append(" plantName, parentName, startDate, stopDate");
					sqlBuf.append(" from VEG_TAXA_SUMMARY where plantName like '%"+taxonName+"%'");
				}
				else 
				{
					System.out.println("TaxonomyQueryStore > unrecognized taxonNameType: "+taxonNameType);
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
					
					//bunch all of these data attributes into the return vector
					returnVector.addElement( consolidateTaxaSummaryInstance( 
						acceptedSynonym, 
						status, 
						concatenatedName, 
						commonName, 
						startDate, 
						stopDate) );
					
					//System.out.println("TaxonomyQueryStore > plantName: " + returnVector.toString()  );
				}
			
				//remember to close the connections etc..
			}
			catch (Exception e) 
			{
				System.out.println("failed " 
				+e.getMessage());
				e.printStackTrace();
			}
			System.out.println("TaxonomyQueryStore > returning results: " + returnVector.size()  );
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
		
	}
