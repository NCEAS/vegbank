package databaseAccess;

/**
 *  '$RCSfile: sqlMapper.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-07-26 01:26:21 $'
 * '$Revision: 1.14 $'
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

 //import the databaseAccess package
import databaseAccess.*;


/**
 * This class will appear as a 'black box' class where inputs 
 * will be a transformed xml document string with some query/insert/update
 * attributes and possibly a second xml transformed xml document string 
 * with some criteria and SQL statements, which if some or all
 * of the criteria are 'met' by the first document the SQL
 * will be passed to another class to be issued to the database
 */

public class  sqlMapper
{

	//the output from the query or group of queries run in this class	
	public String queryOutput[] = new String[10000];  
	//the number of output rows from the issue sql is mapped to this varable
	int queryOutputNum; 
	//store the element  - like "queryElement" and "elementString"
	public String element[] = new String[100];
	//store the values associated with the element like "taxonName" and "Pinus"
	public String value[] = new String[100]; 
	public int elementNum=0;
		
		
	private Hashtable queryElementHash = new Hashtable();
	private Hashtable metaQueryHash = new Hashtable();
	
	//the class that handles the sql requests
	private issueStatement is = new issueStatement();
	//class that stores the 'canned' sql queries
	private queryStore qs = new queryStore();
	
	
	
/**
 *
 * method to develop the sql query string required for the 
 * extended query -- using nested query html form. 
 *
 * @param transformedString string containg the query element type and 
 *		value (| delimeted )
 * @param transformedSringNum integer defining number of query elements
 */

	public void developExtendedPlotQuery(String[] transformedString, int transformedStringNum)
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			Hashtable extendedQueryHash=getQueryTripleHash(transformedString, 
				transformedStringNum);
			
			//make a completely separate hashtable for use with the 
			//taxonomic queries for the is something weird when the
			//same hash is used for creating both sql statements
			Hashtable extendedTaxonQueryHash = getQueryTripleHash(transformedString, 
				transformedStringNum);
			
			System.out.println("sqlMapper > developExtendedPlotQuery");
			System.out.println("sqlMapper > query elements: " + extendedQueryHash.toString() );
			String outFile = extendedQueryHash.get("outFile").toString().trim();
			String taxonSql = null;
			String nonTaxonSql = null;
			
			
			//determine if there is a taxonomic component to the 
			//query and if create that query to be used as a sub-select
			int taxonComponentNum = taxonomicComponentExists(extendedTaxonQueryHash);
			if ( taxonComponentNum > 0)
			{
				System.out.println("sqlMapper > performing taxonomic query ");
				//create the taxonomic sql query 
				taxonSql = getExtendedTaxonomicQuery(extendedTaxonQueryHash) ;
				
				//fix this so that you can pass a string to the method
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append(taxonSql);
				//issue the statement to the database - to return the 
				//plot id's of those plots which have the taxon
				Vector taxonPlotIdVec = is.issuePureSQL(sqlBuf);
				System.out.println("sqlMapper > number of plots with taxon: "+ taxonPlotIdVec.size() );
				
				//get the sql query for the attributes 
				//that are not related to the taxonomy
				nonTaxonSql = getExtendedNonTaxonomicQuery(extendedQueryHash, taxonPlotIdVec) ;
			}
			else 
			{
				//make an empty vector to pass the the below method -- in time write a 
				//overloaded method so that this does not have to be done
				Vector plotIdVec = new Vector();
				nonTaxonSql = getExtendedNonTaxonomicQuery(extendedQueryHash, plotIdVec);
			}
			
			issueStatement is = new issueStatement();
			
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append(nonTaxonSql);
			
			System.out.println("sqlMapper > new sql query: " + sqlBuf.toString() );
			
			//issue the statement to the database
			Vector plotIdVec = new Vector();
			plotIdVec = is.issuePureSQL(sqlBuf);
			System.out.println("sqlMapper > number of plots found: "+ plotIdVec.size() );
			//update this public variable for 
			//the calling method
			queryOutputNum=plotIdVec.size();
			
			//retrieve the plot summary information, using as input the plotId numbers 
			//retrieved in the provios query 
		/// REMOVED THIS CODE -- TO SLOW JHH 20020410
		//  queryStore k1 = new queryStore();
		//  k1.getPlotSummaryNew(  plotIdVec  );
			
			//instantiate a new instance of the dbAccess class -- probably
			//should be done above
			String testFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test-summary.xml";
			System.out.println("sqlMapper > writing the plots using new class - test file: "+testFile );
			dbAccess dbaccess = new dbAccess();
			dbaccess.writeMultipleVegBankPlot(plotIdVec, testFile);

			//write to a summary information to the file 
			//that can be used by the application
				/// REMOVED THIS CODE -- TO SLOW JHH 20020410
	//		xmlWriter xw = new xmlWriter();
	//		xw.writePlotSummary(k1.cumulativeSummaryResultHash, outFile);
			
		}
		catch ( Exception e )
		{
			System.out.println("failed :   "
			+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * method that will create a taxonomic query from the 
	 * hashtable of extended query elements -- not yet totally
	 * figured out how this should work
	 * 
	 * @param extendedQueryHash - the hashtables containg the 
	 *		query elemets etc 
	 */
	private String getExtendedTaxonomicQuery( Hashtable extendedTaxonQueryHash)
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
			int taxonComponentNum = taxonomicComponentExists(extendedTaxonQueryHash);
			if ( taxonComponentNum > 0)
			{
				System.out.println("sqlMapper > doing "+ taxonComponentNum +" taxonomic queries");
				Hashtable isolatedTaxonQueryHash = isolateTaxonQuery(extendedTaxonQueryHash);
				//start the sql statement
				sb.append("select DISTINCT(PLOT_ID) from PLOTSPECIESSUM where ");
			
				for (int i=0;i<taxonComponentNum; i++) 
				{
					sb.append( getSingleExtendedQueryInstanceSQL(isolatedTaxonQueryHash, i) );
					sb.append("\n");
					//print the 'and'
					if(i != ( taxonComponentNum-1) )
					{
						sb.append("and");
					}
					//end the statement
					if ( i == ( taxonComponentNum-1) )
					{
						sb.append(";");
					}
				}
				System.out.println("sqlMapper > full taxonomic SQL query: "+sb.toString() );
			}
		}
		catch ( Exception e )
		{
			System.out.println("failed :   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(sb.toString() );
	}
	
		/**
	 * method that will create a taxonomic query from the 
	 * hashtable of extended query elements -- not yet totally
	 * figured out how this should work
	 * 
	 * @param extendedQueryHash - the hashtables containg the 
	 *		query elemets etc 
	 * @param plotIdvec - this is a vector which contains a number
	 * 	of plotId numbers that can be used to further constrain the 
	 *	query developed in this method 
	 */
	private String getExtendedNonTaxonomicQuery( Hashtable extendedQueryHash, 
	Vector plotIdVec)
	{
		StringBuffer sb = new StringBuffer();
		try 
		{
				Hashtable isolatedNonTaxonQueryHash = isolateNonTaxonQuery(extendedQueryHash);
				//start the sql statement
				sb.append("select PLOT_ID from PLOTSITESUMMARY where ");
				int queryInstanceNum = getExtendedQueryInstanceNumber( isolatedNonTaxonQueryHash );
				for (int i=0;i<queryInstanceNum; i++) 
				{
					sb.append( getSingleExtendedQueryInstanceSQL(isolatedNonTaxonQueryHash, i) );
					sb.append("\n");
					//print the 'and'
					if(i != ( queryInstanceNum-1) )
					{
						sb.append("and");
					}
				}
				//now add the plotId's if there are any in the 
				//passed in vector
				if (plotIdVec.size() > 0)
				{
					sb.append("AND PLOT_ID IN ( " );
					System.out.println("sqlMapper > futher constraining the site-related sql");
					for (int j=0;j<plotIdVec.size(); j++)
					{
						sb.append(plotIdVec.elementAt(j) );
						if (j == (plotIdVec.size() -1))
						{
							sb.append(");");
						}
						else
						{
							sb.append(", ");
						}
					}
				}
				else
				{
					sb.append(";");
				}
		}
		catch ( Exception e )
		{
			System.out.println("failed :   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(sb.toString() );
	}
	
	
		
	/**
		*
		*
		*/
	private Hashtable isolateNonTaxonQuery(Hashtable extendedQueryHash)
	{
		//extract the vectors from the hashtable 
		Vector criteriaVector = (Vector)extendedQueryHash.get("criteria");
		Vector operatorVector = (Vector)extendedQueryHash.get("operator");
		Vector valueVector = (Vector)extendedQueryHash.get("value");
		//these are temporary vec's 
		Vector  criteriaVecBuf = new Vector();
		Vector  operatorVecBuf = new Vector();
		Vector  valueVecBuf = new Vector();
		
		
		
		int origVectorSize = criteriaVector.size();
		for (int i=0;i<origVectorSize; i++) 
		{
			//remove all elements that are not related to a taxonomic query
			if ( ! criteriaVector.elementAt(i).toString().trim().equals("plantTaxon") )
			{
				criteriaVecBuf.addElement( criteriaVector.elementAt(i).toString().trim() );
				operatorVecBuf.addElement( operatorVector.elementAt(i).toString().trim() );
				valueVecBuf.addElement( valueVector.elementAt(i).toString().trim() );
			}
		}
		//add the vectors back to the hashtable
		extendedQueryHash.put("criteria", criteriaVecBuf);
		extendedQueryHash.put("operator", operatorVecBuf);
		extendedQueryHash.put("value", valueVecBuf);
			
		System.out.println("ISOATED SITE HASH: "+extendedQueryHash.toString() );
		return(extendedQueryHash);
	}
	
	
	
	/**
		* method to extract only the taxon queries form the 
		* hashtable containing all the query elements and return
		* a hashtable that mimics the structure of the input 
		* hashtable except that it will only contain the 
		* taxonomic related query elements
		*
		*/
	private Hashtable isolateTaxonQuery(Hashtable extendedQueryHash)
	{
		Hashtable extendedTaxonomicQueryHash = extendedQueryHash;
		//extract the vectors from the hashtable 
		Vector criteriaVector = (Vector)extendedTaxonomicQueryHash.get("criteria");
		Vector operatorVector = (Vector)extendedTaxonomicQueryHash.get("operator");
		Vector valueVector = (Vector)extendedTaxonomicQueryHash.get("value");
		//these are temporary vec's 
		Vector  criteriaVecBuf = new Vector();
		Vector  operatorVecBuf = new Vector();
		Vector  valueVecBuf = new Vector();
		
		System.out.println("sqlMapper > creating a plant-taxonomic-only hashtable " );
		System.out.println("sqlMapper > orig hash: "+extendedTaxonomicQueryHash.toString() );
		
		int origVectorSize = criteriaVector.size();
		for (int i=0;i<origVectorSize; i++) 
		{
			//remove all elements that are not related to a taxonomic query
			if ( criteriaVector.elementAt(i).toString().trim().equals("plantTaxon") )
			{
				criteriaVecBuf.addElement( criteriaVector.elementAt(i).toString().trim() );
				operatorVecBuf.addElement( operatorVector.elementAt(i).toString().trim() );
				valueVecBuf.addElement( valueVector.elementAt(i).toString().trim() );
			}
		}
		//add the vectors back to the hashtable
		extendedTaxonomicQueryHash.put("criteria", criteriaVecBuf);
		extendedTaxonomicQueryHash.put("operator", operatorVecBuf);
		extendedTaxonomicQueryHash.put("value", valueVecBuf);
			
		System.out.println("sqlMapper > output hash: "+extendedTaxonomicQueryHash.toString() );
		return(extendedTaxonomicQueryHash);
	}
	
	
	
	
	
	/**
		* method that returns the number of taxa related componets
		* if there is a taxonomic component in the nested query 
		* @param extendedQueryHash -- the hashtable containg the query elements
		*
		*/
	private int taxonomicComponentExists(Hashtable extendedQueryHash)
	{	
		int taxonElementCount = 0;
		
		//int queryInstanceNum = getExtendedQueryInstanceNumber( extendedQueryHash );
		Vector criteriaVec = (Vector)extendedQueryHash.get("criteria");
		
		for (int i=0;i<criteriaVec.size(); i++) 
		{
			if (criteriaVec.elementAt(i).toString().trim().equals("plantTaxon") )
			{
				taxonElementCount++;
			}
			//System.out.println("Scanning criteria: "+ criteriaVec.elementAt(i).toString() );
		}
		return(taxonElementCount);
	}
	
	
	/**
	 * method to return the sql statement for a single
	 * instance of the extended query containing the 
	 * criteria, operator, and value
	 * @param extendedQueryHash -- the hashtable with the critira, values and 
	 * 		operators related to the queries
	 * @param instanceNum -- the query number for which to compose the sql query
	 *		for
	 * @return a part sql query for this instance -- like 'authorNameId like Abies bifolia'
	 */
	private String getSingleExtendedQueryInstanceSQL(Hashtable extendedQueryHash, 
		int instanceNum)
	{
		StringBuffer sb = new StringBuffer();
		Vector criteriaVec = (Vector)extendedQueryHash.get("criteria");
		Vector operatorVec = (Vector)extendedQueryHash.get("operator");
		Vector valueVec = (Vector)extendedQueryHash.get("value");
				
		//make sure that the request is not for a instance number
		//not contained within the hashtabable
		System.out.println("sqlMapper > queryInstanceNumber: "+instanceNum
		+"  criteriaVectorSize: "+ criteriaVec.size() );
		if (instanceNum > criteriaVec.size() )
		{
			System.out.println("sqlMapper > THIS REQUEST SHOULD NEVER HAPPEN!");
		}
		else
		{
			try
			{
				String criteria=criteriaVec.elementAt(instanceNum).toString();
				String operator=operatorVec.elementAt(instanceNum).toString();
				String value=valueVec.elementAt(instanceNum).toString();
			
				sb.append(" "+getDBAttribute(criteria)+" ");
				sb.append(" "+getSQLOperator(operator, getDBAttribute(criteria) )+" " );
				sb.append( getStringDelimeter( getSQLOperator(operator, getDBAttribute(criteria)), getDBAttribute(criteria) ) );
				sb.append( getSQLValue(value, getSQLOperator(operator, getDBAttribute(criteria) ) ) );
				sb.append(getStringDelimeter( getSQLOperator(operator, getDBAttribute(criteria)),  getDBAttribute(criteria) ) );
			}
			catch ( Exception e )
			{
				System.out.println("Exception " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("sqlMapper > composed query component: " + sb.toString() );
		return(sb.toString());
	}
	
	
	/**
	 * method to return the criteria values used for querying the database
	 * a value string may be like:
	 *	'elevation > value' -- where value is a single string 
	 * or
	 *	'authorNameId in (value)' -- where the value should be a comma separated
	 * 	string
	 */ 
	private String getSQLValue(String valueString, String operator)
	{
		try
		{
			if ( operator.trim().equals("in") )
			{
				return("("+valueString+")");
			}
			else
			{
				return(valueString);
			}
		}
			catch ( Exception e )
		{
			System.out.println("Exception: " +e.getMessage());
			e.printStackTrace();
		}
		return(valueString);
	}
	
	
	
	
	/**
	 * method to translate the criteria to the correct database attribute name
	 * for instance the term plantTaxon would become authorNameId
	 * @param the criteria -- like plantTaxon, elevation etc..
	 */
	private String getDBAttribute(String criteria)
	{
		try
		{
			if ( criteria.trim().equals("elevation") )
			{
				return("altValue");
			}
			else if (criteria.trim().equals("plantTaxon"))
			{
				return("authorNameId");
			}
			else 
			{
				return(criteria);
			}
		}
			catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(criteria);
	}
		
		
	/**
	 * method to translate the string delimeters 
	 * used in the sql
	 */
	private String getStringDelimeter(String operator, String criteria)
	{
		try
		{
			if (operator.equals("like") )
			{
				return("'");
			}
			if (operator.equals("equals") )
			{
				if (criteria.equals("altValue") )
				{
					return(" ");
				}
			}
			if (operator.equals("=") )
			{
				if (criteria.equals("state") ||  criteria.equals("authorNameId") )
				{
					return("'");
				}
			}
		}
		catch ( Exception e )
		{
			System.out.println("failed at:   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(" ");
	}
	
	
	
	/**
	 * method to translate the boolean operators into 
	 * true SQL operators like : eq --> equals
	 */
	private String getSQLOperator( String operator, String criteria)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			if ( operator.equals("eq") )
			{
				if ( criteria.equals("state") || criteria.equals("authorNameId") )
				{
					return("like");
				}
				else
				{
					return("=");
				}
			}
			else if ( operator.equals("gt") )
			{
				return(">");
			}
			else if ( operator.equals("lt") )
			{
				return("<");
			}
			else if ( operator.equals("gt") )
			{
				return(">");
			}
			else if ( operator.equals("contains") )
			{
				return("in");
			}
			else
			{
				return(operator);
			}
		}
		catch ( Exception e )
		{
			System.out.println("failed at:   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(sb.toString());
	}
	
	
	
	/**
	 * method that returns the number of query instances 
	 * in an extended query by using as input a hashtable 
	 * that contains the keys: criteria, operator and value
	 * each with containg the associated values stuck
	 * in a vector
	 */
	
	private int getExtendedQueryInstanceNumber(Hashtable transformedString)
	{
		int vecSize = 0;
		try
		{
			//make sure that the hashtable has the correct
			//keys -- or at least one of them
			if ( transformedString.containsKey("criteria") )
			{
				Vector queryCriteriaVec = (Vector)transformedString.get("criteria");
				vecSize = queryCriteriaVec.size();
			}
			else 
			{
				System.out.println("cannot find the correct keys "+transformedString.toString() );
			}
		}
		catch ( Exception e )
		{
			System.out.println("failed at:   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(vecSize);
	}
	
	
	
	/**
	 * method that takes as input a string array
	 * that is the transformed extended query and
	 * contains the 'queryCriteria', 'queryOperator' 
	 * and 'queryValue' and passes back a hashtable
	 * containing keys: criteria, operator and value
	 * each with containg the associated values stuck
	 * in a vector
	 */
	
	private Hashtable getQueryTripleHash(String[] transformedString, 
		int transformedStringNum)
	{
		
		Hashtable queryTripleHash = new Hashtable();
		Vector queryCriteria = new Vector();
		Vector queryOperator = new Vector();
		Vector queryValue = new Vector();
		try 
		{
			sqlMapper sm = new sqlMapper();
			sm.setAttributeAddress(transformedString, transformedStringNum);	
			String element[]=sm.element;
			String value[]=sm.value;
			for (int i=0;i<element.length; i++) 
			{
				if (value[i] != null)
				//if (element[i].trim() != null)
				{
					if ( element[i].trim().equals("queryCriteria") )
					{
						queryCriteria.addElement( value[i].trim() );
					}
					else if ( element[i].trim().equals("queryOperator") )
					{
						queryOperator.addElement( value[i].trim() );
					}
					else if ( element[i].trim().equals("queryValue") )
					{
						queryValue.addElement( value[i].trim() );
					}
					//now put in the hash some of the meta- data 
					//related to the query
					else
					{
						queryTripleHash.put(element[i].trim(), value[i].trim() );
						System.out.println("getQueryTripleHash query meta data:"
							+value[i].trim());
					}
				}
			}
			//add the vectors to the hashtable
			queryTripleHash.put("criteria", queryCriteria );
			queryTripleHash.put("operator", queryOperator );
			queryTripleHash.put("value", queryValue );
		}
		catch ( Exception e )
		{
			System.out.println("failed at:   "
			+e.getMessage());
			e.printStackTrace();
		}
		return(queryTripleHash);
	}



/**
 *
 * This method will take as input a single  queryElement (like taxonName, 
 * namedPlace, communityType etc), and depending on the type of this single
 * element will map the input to one or a series of queries in the queryStore
 * class. The output will from the query(s) will be passed directly to the 
 * the xml writer which will write the plot info out (as directed in the query 
 * xml document to a file
 *
 * @param transformedString string containg the query element type and 
 *		value (| delimeted )
 * @param transformedSringNum integer defining number of query elements
 */

	public void developPlotQuery(String[] transformedString, 
		int transformedStringNum)
	{
		try 
		{
			//get the query elements into a hash table
			getQueryElementHash(transformedString, transformedStringNum);

			// Grab the meta elements - like filename & number of query elements
 			String resultType = (String)metaQueryHash.get("resultType");
 			String outFile = (String)metaQueryHash.get("outFile");

			// there are two over-arching criteria for
			// developing the queries -- either the user
			// wants to develop a query to get the entire
			// plot record a 'summary', or a 'identity' of the plot(s)
			if ( resultType.equals("full") )
			{
				if ( (String)queryElementHash.get("plotId") != null ) 
				{
		
					// assume that the user has passed the plotId
					// so retrieve the entire plot corresponding 
					// to that plot id
					String plotId = (String)queryElementHash.get("plotId");
					System.out.println("sqlMapper > issuing a query through the query store");
					qs.getEntireSinglePlot(plotId);

					//write to a summary information to the file that can be used by the application
					xmlWriter xw = new xmlWriter();
					xw.writePlotSummary(qs.cumulativeSummaryResultHash, outFile);
				}
				else
				{
					System.out.println("sqlMapper > didn't recognize the desired query");
				}
			}
			
			// ELSE IF THE SUMMARIES ARE DESIRED
			else if ( resultType.equals("summary") )
			{
				System.out.println("sqlMapper > compiling a summary results file");
				// this if targets those queries requesting summary information for plots using
				// a specific query element lime taxonName, communityName, state etc
				if ( resultType.equals("summary") && (queryElementHash.size() == 1) ) 
				{
					// grab the queryElementType from the hash
					String queryElementType	= (String)queryElementHash.keys().nextElement().toString();
					// grab the queryElementValue from the hash
					String queryElementValue = (String)queryElementHash.get(queryElementType);
					System.out.println("sqlMapper > the query element key: "+queryElementType);
					System.out.println("sqlMapper > the query element value: "+queryElementValue);
					System.out.println("sqlMapper > developPlotQuery calling queryStore.getPlotId(single query element)");
					queryStore j = new queryStore();
					j.getPlotId(queryElementValue, queryElementType);
					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				// this block targets the queries that use an elevation range
				else if ( resultType.equals("summary") && (queryElementHash.size() == 2) ) 
				{
					//assume for now that the query elements being targetd here are
					//elevation and grab the results
					String elevationMin = (String)queryElementHash.get("elevationMin");
					String elevationMax = (String)queryElementHash.get("elevationMax");
		
					System.out.println("sqlMapper > developPlotQuery issuing an elevation query");
					queryStore j = new queryStore();
					j.getPlotId(elevationMin, elevationMax, "elevation");
					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				//write the summary information to the file 
				//that can be used by the application -- write two
				//xml files using the two xml writers for testing the 
				//performance
				System.out.println("sqlMapper > writing the plots to xml file");
				Vector plotIdVec = getVector(queryOutput);
				System.out.println("sqlMapper > number of plots: " + plotIdVec.size() );
				
				// instantiate a new instance of the dbAccess class 
				// -- probably should be done above
				String testFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test-summary.xml";
				System.out.println("sqlMapper > writing the plots using new class - test file: " + testFile );
				dbAccess dbaccess = new dbAccess();
				dbaccess.writeMultipleVegBankPlot(plotIdVec, testFile);
			}
			// ELSE GET THE IDENTITIES FOR THE GIVEN PLOT(S)
			else if ( resultType.equalsIgnoreCase("identity") )
			{
				System.out.println("sqlMapper > compiling an identity results file");
				// this if targets those queries requesting summary information for plots using
				// a specific query element lime taxonName, communityName, state etc
				if ( resultType.equals("identity") && (queryElementHash.size() == 1) ) 
				{
					// grab the queryElementType from the hash
					String queryElementType	= (String)queryElementHash.keys().nextElement().toString();
					// grab the queryElementValue from the hash
					String queryElementValue = (String)queryElementHash.get(queryElementType);
					System.out.println("sqlMapper > the query element key: "+queryElementType);
					System.out.println("sqlMapper > the query element value: "+queryElementValue);
					System.out.println("sqlMapper > developPlotQuery calling queryStore.getPlotId(single query element)");
					queryStore j = new queryStore();
					j.getPlotId(queryElementValue, queryElementType);
					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				// this block targets the queries that use an elevation range
				else if ( resultType.equals("identity") && (queryElementHash.size() == 2) ) 
				{
					//assume for now that the query elements being targetd here are
					//elevation and grab the results
					String elevationMin = (String)queryElementHash.get("elevationMin");
					String elevationMax = (String)queryElementHash.get("elevationMax");
		
					System.out.println("sqlMapper > developPlotQuery issuing an elevation query");
					queryStore j = new queryStore();
					j.getPlotId(elevationMin, elevationMax, "elevation");
					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				//write the summary information to the file 
				//that can be used by the application -- write two
				//xml files using the two xml writers for testing the 
				//performance
				System.out.println("sqlMapper > writing the plots to xml file");
				Vector plotIdVec = getVector(queryOutput);
				System.out.println("sqlMapper > number of plots: " + plotIdVec.size() );
				
				// instantiate a new instance of the dbAccess class 
				// -- probably should be done above
				String testFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/identity.xml";
				System.out.println("sqlMapper > writing the plots using new class - test file: " + testFile );
				dbAccess dbaccess = new dbAccess();
				dbaccess.writeMultipleVegBankPlotIdentifcation(plotIdVec, testFile);
			}
			// ELSE DONT RECOGNIZE WHAT TO DO
			else
			{
				System.out.println("sqlMapper > didn't recognize the desired query");
			}
		} 
		catch ( Exception e )
		{
			System.out.println("Exception:  "
			+e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * method that converts a string array into 
	 * a vector, the reason that this is a private 
	 * method is that the pipe delimeter from the 
	 * string will also be removed in this method
	 *
	 * @param inString -- the string to be converted to the vector
	 * @return v -- the vector
	 */
	 private Vector getVector(String inString[])
	 {
		 Vector v = new Vector();
		 try
		 {
				for (int i=0;i<inString.length; i++) 
				{
					if ( inString[i] != null )
					{
						//remove the pipe delimeter -- this is why this 
						//method is unique to this class
						String s = inString[i].replace('|', ' ').trim();
						v.addElement(s);
					}
				}		
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(v);
	 }




/**
 *
 * Method to map a query xml document containg more than one query element
 * to a query stored in the queryStore class.  The elements in the array
 * can be:
 * 	plotID
 * 	taxonName -- can have multiple if seperated by commas
 *	elevationMin
 * 	elevationMax
 * 	state
 * 	surfGeo 
 * 	multipleObs
 *  community
 *
 * @param transformedString string containg the query element type and 
 *		value (| delimeted )
 * @param transformedSringNum integer defining number of query elements
 */

public void developCompoundPlotQuery(String[] transformedString, int transformedStringNum)
{
	try 
	{
		//get the query elements into a hash table
		getQueryElementHash(transformedString, transformedStringNum);

		// Grab the meta elements - like filename & number of query elements 
		String resultType = (String)metaQueryHash.get("resultType");
 		String outFile = (String)metaQueryHash.get("outFile");

		// Look for commonly used queries elements
 		String plotId = (String)queryElementHash.get("plotId");
	 	String taxonName = (String)queryElementHash.get("taxonName");
 		String elevationMin = (String)queryElementHash.get("elevationMin");
 		String elevationMax = (String)queryElementHash.get("elevationMax");
 		String state = (String)queryElementHash.get("state");
 		String surfGeo = (String)queryElementHash.get("surfGeo");
 		String multipleObs = (String)queryElementHash.get("multipleObs");
 		String community = (String)queryElementHash.get("community");
 
 
		// This is for debugging - and can be commented out later
 		System.out.println("sqlMapper > developCompoundPlotQuery");
		System.out.println("sqlMapper > resultType: " + resultType);
		System.out.println("sqlMapper > plotId: " + plotId);
		System.out.println("sqlMapper > taxonName: " + taxonName);
		System.out.println("sqlMapper > elevationMin: " + elevationMin);
		System.out.println("sqlMapper > elavationMax: " + elevationMax);
		System.out.println("sqlMapper > state: " + state);
		System.out.println("sqlMapper > multipleObs: " + multipleObs);
		System.out.println("sqlMapper > community: " + community);
		
		
		// Check that all the appropriate query elements are there
		if (taxonName != null && resultType != null && outFile != null) 
		{
			System.out.println("sqlMapper.developCompoundPlotQuery  calling queryStore.getPlotId(compound)");
			queryStore j = new queryStore();
			j.getPlotId(taxonName, state, elevationMin, elevationMax,surfGeo, community);
			queryOutput=j.outPlotId;
			queryOutputNum=j.outPlotIdNum;
		}
		
		
		// GET THE PLOTID VECTOR
		Vector plotIdVec = getVector(queryOutput);
		System.out.println("sqlMapper > number of plots returned: " + plotIdVec.size() );
		
		// INSTANTIATE AN INSTANCE OF THE DATABASE ACCESS CLASS
		dbAccess dbaccess = new dbAccess();
		
		if (resultType.equals("summary") )
		{
			String testFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test-summary.xml";
			System.out.println("sqlMapper > writing the plots using new class - test file: "+testFile );
			dbaccess.writeMultipleVegBankPlot(plotIdVec, testFile);
		}
		else if (resultType.equals("identity") )
		{
			// IF THE THE RESULT TYPE IS AN IDENTITY THEN PRODUCE THAT
			String testFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/identity.xml";
			System.out.println("sqlMapper > writing the plots using new class - test file: " + testFile );
			dbaccess.writeMultipleVegBankPlotIdentifcation(plotIdVec, testFile);
		}
		
	}
	catch ( Exception e )
	{
		System.out.println("Exception: sqlMapper.developPlotQuery " +e.getMessage());
		e.printStackTrace();
	}
} 


	/**
	 * method to develope and issue the approporiate sql query 
	 * to be issued against the plant taxonomy database
	 *
	 * @param transformedString -
	 */
	public void developSimplePlantTaxonomyQuery(String[] transformedString, 
	int transformedStringNum)
	{
		try 
		{
			
			System.out.println("sqlMapper > developSimplePlantTaxonomyQuery ");
			//get the query elements into a hash table
			getQueryElementHash(transformedString, transformedStringNum);

			//get the needed elements
			String resultType = (String)metaQueryHash.get("resultType");
			String outFile = (String)metaQueryHash.get("outFile");
			
			//get the taxonName entered by the user
			System.out.println("sqlMapper > query elements: " + queryElementHash.toString() );
			String taxonName = (String)queryElementHash.get("taxonName");
			String taxonNameType = (String)queryElementHash.get("taxonNameType");
			String taxonLevel = (String)queryElementHash.get("taxonLevel");
			
			//test the new class for doing all of what is currently in this method
			TaxonomyQueryStore tqs = new TaxonomyQueryStore();
			Vector taxaResults = tqs.getPlantTaxonSummary(taxonName, taxonNameType, taxonLevel);
			//Vector taxaResults = tqs.getPlantTaxonSummary(taxonName, taxonNameType);
			queryOutputNum=taxaResults.size();
			
			//print the results by passing the summary vector to the xml writer class
			System.out.println("sqlMapper > writing the plant results as xml ");
			xmlWriter l = new xmlWriter();
			l.writePlantTaxonomySummary(taxaResults, outFile);
		}
		catch ( Exception e )
		{	
			System.out.println("Exception :  "
			+e.getMessage());
			e.printStackTrace();
		}
	}




	/**
 	*
 	* Method to map a query xml document, containing a 'requestDataType' of
 	* community to either a SQL query stored in the 'CommunityQueryStore' class
 	* or to a method in that class that will generate a SQL query 
 	*
 	* @param transformedString string containg the query element type and 
 	*		value (| delimeted )
 	* @param transformedSringNum integer defining number of query elements
 	*/
	public void developSimpleCommunityQuery(String[] transformedString, 
	int transformedStringNum)
	{
		try 
		{
			//get the query elements into a hash table
			getQueryElementHash(transformedString, transformedStringNum);

			//get the needed elements
			String resultType = (String)metaQueryHash.get("resultType");
			String outFile = (String)metaQueryHash.get("outFile");

			String communityName = (String)queryElementHash.get("communityName");
			String communityLevel = (String)queryElementHash.get("communityLevel");
			Integer queryElementNum = (Integer)metaQueryHash.get("elementTokenNum");

			//This is for debugging - and can be commented out later**/
 			System.out.println("sqlMapper.developSimpleCommunityQuery > \n"
 			+" resultType: "+ resultType +"\n "
			+"outFile: "+outFile+"\n communityName: "+communityName
			+"\n communityLevel: "	+communityLevel+"\n queryElementNum: "+queryElementNum);
	
			//call the method in the CommunityQueryStore class to develop the query
			CommunityQueryStore j = new CommunityQueryStore();
			j.getCommunitySummary(communityName, communityLevel);
			queryOutputNum=j.communitySummaryOutput.size(); //assign the number of returned values

			//print the results by passing the summary vector to the xml writer class
			xmlWriter l = new xmlWriter();
			l.writeCommunitySummary(j.communitySummaryOutput, outFile);

		}
		catch ( Exception e )
		{	
			System.out.println("failed at: sqlMapper.developSimpleCommunityQuery  "
			+e.getMessage());e.printStackTrace();
		}
	}












/**
 * General method to tokenize a string of type: col1|col2 
 * into two seperate string arrays  usually refered to as 
 * element and value.  In the future this method, being a 
 * utility should be put in a utility class
 *
 * @param combinedString - string
 * @param combinedStringNum - number of levels in the string
 *
 */
	private void setAttributeAddress (String[] combinedString, int combinedStringNum) 
	{	
		try 
		{
			int count=0;
			for (int ii=0; ii<combinedStringNum; ii++) 
			{ 
				//System.out.println(" set attribute address : "+ combinedString[ii] +" "+ ii);
				if ( combinedString[ii] != null )
				{
					if ( combinedString[ii].indexOf("|") >0   ) 
					{
						//System.out.println(combinedString[ii]);
						StringTokenizer t = new StringTokenizer(combinedString[ii].trim(), "|");  //tokenize the string to get the requyired address
		
						if ( t.hasMoreTokens() )	
						{	//make sure that there are more tokens or else replace with the null value 
							element[count]=t.nextToken();
						}			
						//capture the required element
						else 
						{
							element[count]=" ";
						}
				
						if (t.hasMoreTokens() )	
						{	
							//make sure that there are more tokens or else replace with the null value (-999)
							value[count]=t.nextToken();
						} 				
						//capture the attributeString
						else 
						{
							value[count]=" ";
						}  //do the replacement
					}
				count++;
				elementNum=count;
				}
			}//end for
			
		} //end try
		catch ( Exception e )
		{
			System.out.println("Exception: sqlMapper.setAttributeAddress  "
			+e.getMessage());
			e.printStackTrace();
		}
} 



	/**
 	 * Method to make two hastables: 1] contains the query elements and respective 
 	 * values 2] contains metadata related to the query elements such as output file
 	 * names and data resultType -- uses as input an array of containing a pipe
 	 * separted query element and string
 	 *
 	 * @param  pipeDelimitString - the input pipe delimited string
 	 * @param  pipeDelimitStringNum - the number of elements in the array
 	 *
 	 */

	public void getQueryElementHash(String[] pipeDelimitString, 
	int pipeDelimitStringNum)
	{
		//System.out.println("test from getQueryElementHash");
		//pass the inputString to the tokenizer method below
		sqlMapper a =new sqlMapper();
		a.setAttributeAddress(pipeDelimitString, pipeDelimitStringNum);	
		String element[]=a.element;  // the returned array of query elements
		String value[]=a.value;  // the returned array of query element values
		int elementNum=a.elementNum; //the number of elements returned

		int elementTokenNum = 0; //number of query elements
		for (int i=0;i<pipeDelimitStringNum; i++) 
		{	
			if (value[i] != null) 
			{	
				if (element[i].equals("queryElement")) 
				{
					//System.out.println("*"+value[i]+" "+value[i+1]);
					queryElementHash.put(value[i],value[i+1]);
					elementTokenNum++;
				}
				metaQueryHash.put(element[i], value[i]);
			}
 		}
 		//embed the number of query elements stored in the hastable
 		metaQueryHash.put("elementTokenNum", new Integer(elementTokenNum) );
	}



} //end class
