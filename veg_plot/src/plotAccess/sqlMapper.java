import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

 //import the class that stores all the queries
import queryStore.*; 


/**
 * This class will appear as a 'black box' class where inputs 
 * will be a transformed xml document string with some query/insert/update
 * attributes and possibly a second xml transformed xml document string 
 * with some criteria and SQL statements, which if some or all
 * of the criteria are 'met' by the first document the SQL
 * will be passed to another class to be issued to the database
 *
 * @version March 22, 2001
 * @author John Harris
 *
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
	issueStatement is = new issueStatement();
	//class that stores the 'canned' sql queries
	queryStore qs = new queryStore();
	
	
	
/**
 *
 * method to develope the sql query string required for the 
 * extended query -- using nested queries
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
			int queryInstanceNum = getExtendedQueryInstanceNumber( extendedQueryHash );
			String outFile = extendedQueryHash.get("outFile").toString().trim();
			//determine if there is a taxonomic component to the 
			//query and if create that query to be used as a sub-select
			int taxonComponentNum = taxonomicComponentExists(extendedQueryHash);
			if ( taxonComponentNum > 0)
			{
				System.out.println("TAXONOMIC LOOKUP! "+ taxonComponentNum);
			}
			
			//start the sql statement
			sb.append("select PLOT_ID from PLOTSITESUMMARY where ");
			
			for (int i=0;i<queryInstanceNum; i++) 
			{
				sb.append( getSingleExtendedQueryInstanceSQL(extendedQueryHash, i) );
				sb.append("\n");
				//print the 'and'
				if(i != ( queryInstanceNum-1) )
				{
					sb.append("and");
				}
				//end the statement
				if ( i == ( queryInstanceNum-1) )
				{
					sb.append(";");
				}
			}
			
			System.out.println("SQL QUERY: \n" + sb.toString() );
			
			//issue the statement to the database
			Vector plotIdVec = is.issuePureSQL(sb);
			System.out.println("number of plots found: "+ plotIdVec.size() );
			//update this public variable for 
			//the calling method
			queryOutputNum=plotIdVec.size();
			
			//retrieve the plot summary information, using as input the plotId numbers 
			//retrieved in the provios query 
			queryStore k1 = new queryStore();
			k1.getPlotSummaryNew(  plotIdVec  );

			//write to a summary information to the file 
			//that can be used by the application
			xmlWriter xw = new xmlWriter();
			xw.writePlotSummary(k1.cumulativeSummaryResultHash, outFile);
			
		}
				catch ( Exception e )
		{
			System.out.println("failed :   "
			+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
		* method that returns the number of taxa related componets
		* if there is a taxonomic component in the nested query
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
			System.out.println("Scanning criteria: "+ criteriaVec.elementAt(i).toString() );
		}
		return(taxonElementCount);
	}
	
	
	/**
	 * method to return the sql statement for a single
	 * instance of the extended query containing the 
	 * criteria, operator, and value
	 */
	private String getSingleExtendedQueryInstanceSQL(Hashtable extendedQueryHash, 
		int instanceNum)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			Vector criteriaVec = (Vector)extendedQueryHash.get("criteria");
			Vector operatorVec = (Vector)extendedQueryHash.get("operator");
			Vector valueVec = (Vector)extendedQueryHash.get("value");
			String criteria=criteriaVec.elementAt(instanceNum).toString();
			String operator=operatorVec.elementAt(instanceNum).toString();
			String value=valueVec.elementAt(instanceNum).toString();
			
			sb.append(" "+getDBAttribute(criteria)+" ");
			sb.append(" "+getSQLOperator(operator, getDBAttribute(criteria) )+" " );
			sb.append( getStringDelimeter( getSQLOperator(operator, getDBAttribute(criteria)), getDBAttribute(criteria) ) );
			sb.append(value);
			sb.append(getStringDelimeter( getSQLOperator(operator, getDBAttribute(criteria)),  getDBAttribute(criteria) ) );
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
	 * method to translate the criteria 
	 * to the correct database attribute name
	 *
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
			System.out.println("failed at:   "
			+e.getMessage());
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
				if (criteria.equals("state") )
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
				if ( criteria.equals("state") )
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
				return("like");
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
			// plot record or a 'summary' of the plot(s)
			
			if ( resultType.equals("full") )
			{
				if ( (String)queryElementHash.get("plotId") != null ) 
				{
		
					//assume that the user has passed the plotId
					//so retrieve the entire plot corresponding 
					// to that plot id
					String plotId = (String)queryElementHash.get("plotId");
					
					System.out.println("sqlMapper.developPlotQuery - "
					+" calling queryStore.getEntireSinglePlot");
				
					qs.getEntireSinglePlot(  plotId  );

					//write to a summary information to the file that can be used by the application
					xmlWriter xw = new xmlWriter();
					xw.writePlotSummary(qs.cumulativeSummaryResultHash, outFile);
				}
				else
				{
					System.out.println("didnt recognize the desired query");
				}
			}
			
			else if ( resultType.equals("summary") )
			{
				// this if targets those queries requesting summary information for plots using
				// a specific query element lime taxonName, communityName, state etc
				if ( resultType.equals("summary") && (queryElementHash.size() == 1) ) 
				{
		
					//grab the queryElementType from the hash
					String queryElementType	= (String)queryElementHash.keys().nextElement().toString();
					//grab the queryElementValue from the hash
					String queryElementValue = (String)queryElementHash.get(queryElementType);
		
					System.out.println("> the query element key: "+queryElementType);
					System.out.println("> the query element value: "+queryElementValue);
		
					System.out.println("sqlMapper.developPlotQuery - "
					+" calling queryStore.getPlotId (single query element)");
					queryStore j = new queryStore();
					j.getPlotId(queryElementValue, queryElementType);
					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				// this if targets thosequeries requesting summary information for plots having
				// some minimum and maximum value for a specific attribute like elevation or
				// latitude

				else if ( resultType.equals("summary") && (queryElementHash.size() == 2) ) 
				{

					//assume for now that the query elements being targetd here are
					//elevation and grab the results
					String elevationMin = (String)queryElementHash.get("elevationMin");
					String elevationMax = (String)queryElementHash.get("elevationMax");
		
		
					System.out.println("sqlMapper.developPlotQuery - "
					+" calling queryStore.getPlotId (elevation option)");
					queryStore j = new queryStore();
					j.getPlotId(elevationMin, elevationMax, "elevation");

					queryOutput=j.outPlotId;
					queryOutputNum=j.outPlotIdNum;
				}

				//retrieve the plot summary information, using as input 
				//the plotId numbers retrieved in the provios query 
				queryStore k1 = new queryStore();
				k1.getPlotSummaryNew(queryOutput, queryOutputNum);

				//write to a summary information to the file 
				//that can be used by the application
				xmlWriter xw = new xmlWriter();
				xw.writePlotSummary(k1.cumulativeSummaryResultHash, outFile);
			}
			else
			{
				System.out.println("didnt recognize the desired query");
			}

} //end try 
catch ( Exception e )
{
	System.out.println("failed at: sqlMapper.developPlotQuery  "
	+e.getMessage());
	e.printStackTrace();
}

} //end method






/**
 *
 * Method to map a query xml document containg more than one query element
 * to a query stored in the queryStore class
 *
 * @param transformedString string containg the query element type and 
 *		value (| delimeted )
 * @param transformedSringNum integer defining number of query elements
 */

public void developCompoundPlotQuery(String[] transformedString, int transformedStringNum)
{
try {

//get the query elements into a hash table
getQueryElementHash(transformedString, transformedStringNum);

/** Grab the meta elements - like filename & number of query elements */
 String resultType = (String)metaQueryHash.get("resultType");
 String outFile = (String)metaQueryHash.get("outFile");

/** Look for commonly used queries elements */
 String plotId = (String)queryElementHash.get("plotId");
 String taxonName = (String)queryElementHash.get("taxonName");
 String elevationMin = (String)queryElementHash.get("elevationMin");
 String elevationMax = (String)queryElementHash.get("elevationMax");
 String state = (String)queryElementHash.get("state");
 String surfGeo = (String)queryElementHash.get("surfGeo");
 String multipleObs = (String)queryElementHash.get("multipleObs");
 String community = (String)queryElementHash.get("community");
 
 
/** This is for debugging - and can be commented out later**/
 System.out.println("sqlMapper.developCompoundPlotQuery > \n resultType: "+ resultType +"\n "
	+"outFile: "+outFile+"\n plotId: "+plotId +"\n taxonName: "+taxonName 
	+"\n elevationMin: "+elevationMin+"\n elavationMax: "+elevationMax
	+"\n state: "+state+"\n multipleObs: "+multipleObs+"\n community: "+community);


// Check that all the appropriate query elements are there

if (taxonName != null && resultType != null && outFile != null) {
	
	if ( resultType.equals("summary") ) {

		System.out.println("sqlMapper.developCompoundPlotQuery - "
		+" calling queryStore.getPlotId (compound)");
		queryStore j = new queryStore();
		j.getPlotId(taxonName, state, elevationMin, elevationMax,
		surfGeo, community);
		queryOutput=j.outPlotId;
		queryOutputNum=j.outPlotIdNum;

	}

}


//retrieve the summary info
queryStore k = new queryStore();
k.getPlotSummaryNew(queryOutput, queryOutputNum);


//write to a summary file - that will be read by the requestor
xmlWriter xw = new xmlWriter();
xw.writePlotSummary(k.cumulativeSummaryResultHash, outFile);



} //end try 
catch ( Exception e ){System.out.println("failed at: sqlMapper.developPlotQuery  "
	+e.getMessage());e.printStackTrace();}

} //end method





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


//print the results by passing the summary vector to the writer class
xmlWriter l = new xmlWriter();
l.writeCommunitySummary(j.communitySummaryOutput, outFile);



} //end try
catch ( Exception e ){System.out.println("failed at: sqlMapper.developSimpleCommunityQuery  "
	+e.getMessage());e.printStackTrace();}



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
						element[count]="-999.25";
					}
				
					if (t.hasMoreTokens() )	
					{	
						//make sure that there are more tokens or else replace with the null value (-999)
						value[count]=t.nextToken();
					} 				
					//capture the attributeString
					else 
					{
						value[count]="-999.25";
					}  //do the replacement
				}
			count++;
			elementNum=count;
			}
		}//end for
			
	} //end try
	catch ( Exception e )
	{
		System.out.println("failed at: sqlMapper.setAttributeAddress  "
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
			System.out.println("*"+value[i]+" "+value[i+1]);
			queryElementHash.put(value[i],value[i+1]);
			elementTokenNum++;
		}
		metaQueryHash.put(element[i], value[i]);
	}
 }
 //embed the number of query elements stored in the hastable
 metaQueryHash.put("elementTokenNum", new Integer(elementTokenNum) );
} //end method



} //end class
