import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

/**
* This class will appear as a 'black box' class where inputs 
* will be a transformed xml document string with some query/insert/update
* attributes and possibly a second xml transformed xml document string 
* with some criteria and SQL statements, which if some or all
* of the criteria are 'met' by the first document the SQL
* will be passed to another class to be issued to the database
*
*  This current version just takes the first query xml document
* and determines the sql code which is passed to the issueSQL
* class to be issued to the database 
*
* desire is to have a relatively universal query engine
* where the user can make an xml document that specifies 
* the development of a sql query in this class
*/

public class  sqlMapper
{

/**
*
* This method will take as input a single  queryElement (like taxonName, 
* namedPlace, communityType etc), and depending on the type of this single
* element will map the input to one or a series of queries in the queryStore
* class. The output will from the query(s) will be passed directly to the 
* the xml writer which will write the plot info out (as directed in the query 
* xml document to a file
*
* @param 	transformedString string containg the query element type and 
*		value (| delimeted )
* @param 	transformedSringNum integer defining number of query elements
*/


//accept the transformed xml document as a string and number of rows in the array
public void developPlotQuery(String[] transformedString, int transformedStringNum)
{
/**
* First set up a pool of connections that can be passed to the queryStore class
*/
//get the database parameters from the database.parameters file
utility g =new utility(); 
g.getDatabaseParameters("database", "query");

	
Connection pconn=null; //this is a connection that is taken from within the pool
DbConnectionBroker myBroker;
try {
myBroker = new DbConnectionBroker(g.driverClass,
                                  g.connectionString,
                                  g.login,g.passwd,
				  g.minConnections,g.maxConnections,
                                  g.logFile,1.0);

				  
// Get a DB connection from the Broker
int thisConnection;
pconn= myBroker.getConnection(); //grab one connectionfrom pool
thisConnection = myBroker.idOfConnection(pconn);

//This is an attempt to devise a scheme to see how many times
//a database connection is used if more than like 50 times 
//check it back into the pool and grab a new one - the exception
//thrown otherwise is exeeded max cursors
int connectionUses=1;


//pass the inputString to the tokenizer method below
sqlMapper a =new sqlMapper();
a.setAttributeAddress(transformedString, transformedStringNum);	
	
//grab the results
String element[]=a.element;  // the returned database address string
String value[]=a.value;  // the returened data string
int elementNum=a.elementNum; //the number of addresses and attributes in the returned set



//check to see the type of the query attributes passed 
//and depending, assign a sql query to the query
for (int i=0;i<transformedStringNum; i++) {
	
	if (element[i] != null && element[i].startsWith("queryElement")) {
		
		if (value[i] != null && value[i].startsWith("taxonName")) { 
			//if this and the above are true then trigger sql stmt below
			//call the queryStore method associated with this request
			//to get the plot id numbers having the associated taxon
			queryStore j = new queryStore();
			j.getPlotId(value[i+1], "taxonName", pconn);
			queryOutput=j.outPlotId;
			queryOutputNum=j.outPlotIdNum;
		}  //end if
		
		if (value[i] != null && value[i].startsWith("elevationMin")) {
			//require that an elevation restriction have both min max
			queryStore l = new queryStore();
			System.out.println("passing the following "+value[i+1]+
				" "+value[i+3]);
			l.getPlotId(value[i+1],  value[i+3], "elevation", pconn);
			queryOutput=l.outPlotId;
			queryOutputNum=l.outPlotIdNum;
			
		}
		
		
	} //end if

	
	/*determine the type of output requested by the calling class*/
	if (element[i] != null && element[i].startsWith("resultType")) {
		//if summary - call the method to return the summary elements
		//using as input the plotId numbers returned in the if clause above
		if (value[i] != null && value[i].startsWith("summary")) {
			System.out.println("outputing the results set as: "+value[i]);
			queryStore k = new queryStore();
			k.getPlotSummary(queryOutput, queryOutputNum, pconn);
			
			
			
			/**
			* in the near future will want to look here to check
			* the number of connection uses to determine if need
			* to close and reopen
			*/
			connectionUses=connectionUses+k.outConnectionUses;
			System.out.println("number of uses of this connection: "
				+connectionUses);
			if (connectionUses>12) {
				System.out.println("reseting the current connection");
				try {
				pconn.close(); //close it - it is no good anymore
				myBroker.freeConnection(pconn); //not sure if this should be 
							//commented b/c not sure of the 
							//meaning of 'recycle' in the
							//broker
				pconn=myBroker.getConnection();
				connectionUses=0;
				} catch (Exception e) {System.out.println("failed calling "
				+" dbConnect.makeConnection call" + e.getMessage());}
			}
			
			//pass the returned summary results to the xmlWriter
			//to write the xml file whis is identified in the xml
			String outFile=value[i+1];
			xmlWriter l = new xmlWriter();
			l.writePlotSummary(k.summaryOutput, k.summaryOutputNum, outFile);
			
			
		} //end if
	} //end if
		
	
} //end for

// The connection is returned to the Broker
myBroker.freeConnection(pconn);
myBroker.destroy();
} //end try - for conn pooler
catch ( Exception e ){System.out.println("failed at: sqlMapper.developPlotQuery  "
	+e.getMessage());}
	

} //end method



/**
* This is an overloaded method of the above for mapping compound queries
* input includes the transformed query string from the xml query document 
* and the number of elements as well as the number of query element types 
* being passed to the method.  Output includes 
*
* @param  transformedString string containg the query element type and 
*		value (| delimeted )
* @param  transformedSringNum integer defining number of query elements
* @param  elementTypeNum the number of different element types
*
*/


//accept the transformed xml document as a string and number of rows in the array
public void developPlotQuery(String[] transformedString, int transformedStringNum,
	int elementTypeNum)
{

//grab the elements and put into an array mapping to the following:
//	1] taxonName, 2] state, 3] eleveationMin, 4] elevationMax, 5] surfGeo
//	6] multipleObs, 7]community


//pass the array to the corresponding getPlotId


//grab the summary data and send to xml writer

} //end method







/**
* General method to tokenize a string of type: col1|col2 into two seperate strings 
* usually refered to as element and value.  In the future this method, being a 
* utility should be put in a utility class
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
			
			
		System.out.println(element[count]+" "+value[count]+" "+ii+" "+count);
			
		}//end if
		count++;
		elementNum=count;
}//end for
} //end try
catch ( Exception e ){System.out.println("failed at: sqlMapper.setAttributeAddress  "+e.getMessage());}
}  //end method






public String queryOutput[] = new String[10000];  //the output from the issue sql can be mapped to this varaiable
int queryOutputNum; //the number of output rows from the issue sql is mapped to this varable

public String element[] = new String[100];  //store the element  - like "queryElement" and "elementString"
public String value[] = new String[100];  //store the values associated with the element like "taxonName" and "Pinus"
public int elementNum=0;
		

} //end class
