import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

import queryStore.*;  //class that stores all the queries


/**
 * This class will appear as a 'black box' class where inputs 
 * will be a transformed xml document string with some query/insert/update
 * attributes and possibly a second xml transformed xml document string 
 * with some criteria and SQL statements, which if some or all
 * of the criteria are 'met' by the first document the SQL
 * will be passed to another class to be issued to the database
 *
 * @version 11 Jan 2001
 * @author John Harris
 *
 */

public class  sqlMapper
{

private Hashtable queryElementHash = new Hashtable();
private Hashtable metaQueryHash = new Hashtable();





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

public void developPlotQuery(String[] transformedString, int transformedStringNum)
{

// First set up a pool of connections that can be passed to the queryStore class
//get the database parameters from the database.parameters file
 utility g =new utility(); 
 g.getDatabaseParameters("database", "query");
 Connection pconn=null; 
 DbConnectionBroker myBroker;

try {
 myBroker = new DbConnectionBroker(g.driverClass, g.connectionString,
	g.login,g.passwd,g.minConnections,g.maxConnections, g.logFile,1.0);
				  
// Get a DB connection from the Broker
 int thisConnection;
 pconn= myBroker.getConnection(); //grab one connection from pool
 thisConnection = myBroker.idOfConnection(pconn);

//This is a scheme to see how many times a database connection is used if 
//more than like 50 times check it back into the pool and grab a new one - the 
//exception thrown otherwise is exeeded max cursors
 int connectionUses=1;

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
 System.out.println("sqlMapper.developPlotQuery > \n resultType: "+ resultType +"\n "
	+"outFile: "+outFile+"\n plotId: "+plotId +"\n taxonName: "+taxonName 
	+"\n elevationMin: "+elevationMin+"\n elavationMax: "+elevationMax
	+"\n state: "+state+"\n multipleObs: "+multipleObs+"\n community: "+community);

	

// at the next line do a cardinality test
/** Check which elements are there -- plotId specific queries */
if (plotId != null && resultType != null && outFile != null) {
	
	/** Start a search for specific queries*/
	if ( resultType.equals("full") ) {
	
		System.out.println("sqlMapper.developPlotQuery - "
		+" calling queryStore.getEntireSinglePlot");
		queryStore b = new queryStore();
		b.getEntireSinglePlot(plotId, pconn);
		
		xmlWriter l = new xmlWriter();
		l.writePlotSummary(b.entireSinglePlotOutput, 
			b.entireSinglePlotOutputNum, outFile);
		
	
	}

}

/** Check which elements are there -- taxonName specific queries */
if (taxonName != null && resultType != null && outFile != null) {

	if ( resultType.equals("summary") ) { //retrieve the plotId's w taxon

		System.out.println("sqlMapper.developPlotQuery - "
		+" calling queryStore.getPlotId (taxonName)");
			queryStore j = new queryStore();
			j.getPlotId(taxonName, "taxonName", pconn);
			queryOutput=j.outPlotId;
			queryOutputNum=j.outPlotIdNum;

	}
}

/** Check which elements are there -- elevation specific queries */
if (elevationMin != null && elevationMax != null && resultType != null && outFile != null) {

	if ( resultType.equals("summary") ) { //retrieve the plotId's w	elevation

		System.out.println("sqlMapper.developPlotQuery - "
		+" calling queryStore.getPlotId (elevation option)");
		queryStore j = new queryStore();
		j.getPlotId(elevationMin,  elevationMax, "elevation", pconn);

		queryOutput=j.outPlotId;
		queryOutputNum=j.outPlotIdNum;

	}
}


//retrieve the summary info
///queryStore k = new queryStore();
///k.getPlotSummary(queryOutput, queryOutputNum, pconn);

// manage the database connections -- this doesn't really work here but keep it
// for later
///connectionUses=connectionUses+k.outConnectionUses;
System.out.println("number of uses of this connection: "+connectionUses);
if (connectionUses>12) {
	System.out.println("reseting the current connection");

	try {
		pconn.close(); 
		myBroker.freeConnection(pconn); 
		pconn=myBroker.getConnection();
		connectionUses=0;
	} catch (Exception e) {System.out.println("failed calling "
		+" dbConnect.makeConnection call" + e.getMessage());}

}


//write to a summary file -- this is an old method
///xmlWriter l = new xmlWriter();
///l.writePlotSummary(k.summaryOutput, k.summaryOutputNum, outFile);

//just test the new method
queryStore k1 = new queryStore();
k1.getPlotSummaryNew(queryOutput, queryOutputNum, pconn);

//write to a summary file - again this is a test
xmlWriter xw = new xmlWriter();
xw.writePlotSummary(k1.cumulativeSummaryResultHash, outFile+".xml");



// The connection is returned to the Broker
myBroker.freeConnection(pconn);
myBroker.destroy();
} //end try - for conn pooler
catch ( Exception e ){System.out.println("failed at: sqlMapper.developPlotQuery  "
	+e.getMessage());e.printStackTrace();}

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

// First set up a pool of connections that can be passed to the queryStore class
//get the database parameters from the database.parameters file
 utility g =new utility(); 
 g.getDatabaseParameters("database", "query");
 Connection pconn=null; 
 DbConnectionBroker myBroker;

try {
 myBroker = new DbConnectionBroker(g.driverClass, g.connectionString,
	g.login,g.passwd,g.minConnections,g.maxConnections, g.logFile,1.0);
				  
// Get a DB connection from the Broker
 int thisConnection;
 pconn= myBroker.getConnection(); //grab one connectionfrom pool
 thisConnection = myBroker.idOfConnection(pconn);

//This is a scheme to see how many times a database connection is used if 
//more than like 50 times check it back into the pool and grab a new one - the 
//exception thrown otherwise is exeeded max cursors
 int connectionUses=1;


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
	
	if ( resultType.equals("summary") ) { //retrieve the plotId's w taxon

		System.out.println("sqlMapper.developCompoundPlotQuery - "
		+" calling queryStore.getPlotId (compound)");
		queryStore j = new queryStore();
		j.getPlotId(taxonName, state, elevationMin, elevationMax,
		surfGeo, multipleObs, community, pconn);
		queryOutput=j.outPlotId;
		queryOutputNum=j.outPlotIdNum;

	}

}


//retrieve the summary info
queryStore k = new queryStore();
k.getPlotSummary(queryOutput, queryOutputNum, pconn);

// manage the database connections -- this doesn't really work here but keep it
// for later
connectionUses=connectionUses+k.outConnectionUses;
System.out.println("number of uses of this connection: "+connectionUses);
if (connectionUses>12) {
	System.out.println("reseting the current connection");

	try {
		pconn.close(); 
		myBroker.freeConnection(pconn); 
		pconn=myBroker.getConnection();
		connectionUses=0;
	} catch (Exception e) {System.out.println("failed calling "
		+" dbConnect.makeConnection call" + e.getMessage());}

}


//write to a summary file
xmlWriter l = new xmlWriter();
l.writePlotSummary(k.summaryOutput, k.summaryOutputNum, outFile);


// The connection is returned to the Broker
myBroker.freeConnection(pconn);
myBroker.destroy();
} //end try - for conn pooler
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


// First set up a pool of connections that can be passed to the queryStore class
//get the database parameters from the database.parameters file
 utility g =new utility(); 
 g.getDatabaseParameters("database", "query");
 Connection pconn=null; 
 DbConnectionBroker myBroker;

try {

myBroker = new DbConnectionBroker(g.driverClass, g.connectionString,
	g.login,g.passwd,g.minConnections,g.maxConnections, g.logFile,1.0);
				  
// Get a DB connection from the Broker
int thisConnection;
pconn=myBroker.getConnection(); //grab one connectionfrom pool

//add the connection counting stuff later

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
j.getCommunitySummary(communityName, communityLevel, pconn);
queryOutputNum=j.communitySummaryOutput.size(); //assign the number of returned values


//print the results by passing the summary vector to the writer class
xmlWriter l = new xmlWriter();
l.writeCommunitySummary(j.communitySummaryOutput, outFile);


//return the connection to the Broker
myBroker.freeConnection(pconn);
myBroker.destroy();

} //end try - for conn pooler
catch ( Exception e ){System.out.println("failed at: sqlMapper.developSimpleCommunityQuery  "
	+e.getMessage());e.printStackTrace();}



}












/**
 * General method to tokenize a string of type: col1|col2 into two seperate strings 
 * usually refered to as element and value.  In the future this method, being a 
 * utility should be put in a utility class
 *
 * @param combinedString - string
 * @param combinedStringNum - number of levels in the string
 *
 */
	
private void setAttributeAddress (String[] combinedString, int combinedStringNum) {
	
	
try {
int count=0;
for (int ii=0; ii<combinedStringNum; ii++) { 
	if (combinedString[ii].indexOf("|")>0 && combinedString[ii].trim() != null ) {  //make sure to tokenize an appropriate string
		//System.out.println(combinedString[ii]);
		StringTokenizer t = new StringTokenizer(combinedString[ii].trim(), "|");  //tokenize the string to get the requyired address
		
		if (t.hasMoreTokens())	{	//make sure that there are more tokens or else replace with the null value 
			element[count]=t.nextToken();
		}				//capture the required element
		else {element[count]="-999.25";}
				
				
		if (t.hasMoreTokens() )	{	//make sure that there are more tokens or else replace with the null value (-999)
			value[count]=t.nextToken();
		} 				//capture the attributeString
		else {value[count]="-999.25";}  //do the replacement
			
			
//		System.out.println(element[count]+" "+value[count]+" "+ii+" "+count);
			
		}//end if
		count++;
		elementNum=count;
}//end for
} //end try
catch ( Exception e ){System.out.println("failed at: sqlMapper.setAttributeAddress  "
	+e.getMessage());}
}  //end method



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
for (int i=0;i<pipeDelimitStringNum; i++) {
	
	if (value[i] != null) {
		
		if (element[i].equals("queryElement")) {
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



public String queryOutput[] = new String[10000];  //the output from the issue sql can be mapped to this varaiable
int queryOutputNum; //the number of output rows from the issue sql is mapped to this varable

public String element[] = new String[100];  //store the element  - like "queryElement" and "elementString"
public String value[] = new String[100];  //store the values associated with the element like "taxonName" and "Pinus"
public int elementNum=0;
		

} //end class
