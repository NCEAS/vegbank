import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;



/**
 * This class acts as a store for sql queries which are used depending on the 
 * number and type of query elements passed to the sqlMapper class.  Ultimately
 * these queries are issued to the database though the issue select method
 *
 * @author John Harris
 * @version March 22, 2001
 *
 */

public class  queryStore
 {

public String summaryOutput[] = new String[10000]; 
public int summaryOutputNum; 
public int outConnectionUses;

//hash table to store the cummulative summary results for all the plots
public Hashtable cumulativeSummaryResultHash = new Hashtable();



utility g =new utility(); 
//g.getDatabaseParameters("database", "query");
Connection pconn=null; 
DbConnectionBroker myBroker;



/**
 * Method using as input a single plot database id number to retieve all the 
 * attributes for that plot all fields are made publicly accessible and will,
 * ultimately be passed to the xml writer class for file output
 *
 * @param plotId - database plot id number
 * @param conn - database connection
 *
 */
public void getEntireSinglePlot(String plotId, Connection conn)
{
//at this point the query is the same as the get summary query - wait till
//closure on data model before adjusting

String entireResult=null;
int connectionUses=1;

String action="select";
String statement="select PLOT_ID, AUTHORPLOTCODE, project_id, "+
	"surfGeo,  PLOTTYPE, PLOTORIGINLAT, PLOTORIGINLONG, PLOTSHAPE, "
	+" PLOTSIZE, PLOTSIZEACC, ALTVALUE, ALTPOSACC, SLOPEASPECT, SLOPEGRADIENT, "
	+" SLOPEPOSITION, HYDROLOGICREGIME, SOILDRAINAGE, STATE, CURRENTCOMMUNITY "
	+" from PLOT where PLOT_ID = "+plotId;
		
String returnFields[]=new String[19];	
returnFields[0]="PLOT_ID";	
returnFields[1]="AUTHORPLOTCODE";
returnFields[2]="project_id";
returnFields[3]="surfGeo";
returnFields[4]="PLOTTYPE";
returnFields[5]="PLOTORIGINLAT";
returnFields[6]="PLOTORIGINLONG";
returnFields[7]="PLOTSHAPE";
returnFields[8]="PLOTSIZE";
returnFields[9]="PLOTSIZEACC";
returnFields[10]="ALTVALUE";
returnFields[11]="ALTPOSACC";
returnFields[12]="SLOPEASPECT";
returnFields[13]="SLOPEGRADIENT";
returnFields[14]="SLOPEPOSITION";
returnFields[15]="HYDROLOGICREGIME";
returnFields[16]="SOILDRAINAGE";
returnFields[17]="STATE";
returnFields[18]="CURRENTCOMMUNITY";
int returnFieldLength=19;

/** Issue the statement, and iterate the counter */
issueStatement j = new issueStatement();
j.issueSelect(statement, action, returnFields, returnFieldLength, conn);	
connectionUses++;
	
entireResult=j.outReturnFields[0];
//System.out.println(entireResult);
entireSinglePlotOutput[0]=entireResult; //cheat here
entireSinglePlotOutputNum=1; //and here



/**	
 * make a second query here to get the species specific information which will
 * be appended onto the summary results line and ultimately tokenized in the 
 * xmlWrter class - at some point this function may become its own method
 */

action="select";
statement="select AUTHORNAMEID from taxonObservation where OBS_ID in"+
	"(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in"+
	"(select plot_ID from plot where PLOT_ID ="+plotId+"))";

		
String returnFieldsB[]=new String[1];	
returnFieldsB[0]="AUTHORNAMEID";
int returnFieldLengthB=1;

/** Issue the statement, and iterate the counter */
issueStatement k = new issueStatement();
k.issueSelect(statement, action, returnFieldsB, returnFieldLengthB, conn);	
connectionUses++;
	
	
//take the results from this query and append to the summary line
//which will ultimately be passed back to the xmlWriter to be tokenized
//and writen to xml - againd there should only be one line returned 


entireResult=entireResult+k.outReturnFields[0];
entireSinglePlotOutput[0]=entireResult; //cheat here
entireSinglePlotOutputNum=1; //and here

}//end method
public String entireSinglePlotOutput[] = new String[10000];  //should always = 1
public int entireSinglePlotOutputNum; 






/**
 * Method to query the database by using a plot id number to obtain a summary of 
 * that plot including 1] author plot code, 2] surfaceGeology
 * The input to the method is a string array containing plot id numbers and
 * an integer representing the number of elements in the array
 *
 * @param plotId - an array of plotId numbers
 * @param plotIdNum - the number of elements in the above array
 * @param conn - a database connection -- get rid of this and put the conn
 * management here in this class
 */
public void getPlotSummaryNew(String plotId[], int plotIdNum, Connection conn)
{

//get the database management parameter settings
g.getDatabaseParameters("database", "query");

try {

myBroker = new DbConnectionBroker(g.driverClass, g.connectionString,
	g.login,g.passwd,g.minConnections,g.maxConnections, g.logFile+"QueryStore",1.0);
conn=myBroker.getConnection(); //grab one connection from pool

//this is the number of uses that an input connection has had
int connectionUses=1;


//this array will hold all the results before passing it back to the 
//calling class
String summaryResult[] = new String[10000];
int summaryResultNum=0;

//iterate through the plot id numbers
for (int i=0;i<plotIdNum; i++) {

//hash table to store the results of each individual plot
Hashtable summaryResultHash = new Hashtable();

//Because often the output of issueStatement is used as input here,
//and the output values are tokenized by pipes replace all the pipes below
String currentPlotId=plotId[i].replace('|',' ').trim();

//this code should be replaced by a method that checks to see that the plotId is
//a valid one

if (currentPlotId.equals("nullValue") || currentPlotId == null ) {
	currentPlotId="0";  //change this to a more resonable value
}


String action="select";
String statement="select PLOT_ID, PROJECT_ID, PLOTTYPE, SAMPLINGMETHOD, "
	+" COVERSCALE, PLOTORIGINLAT,  PLOTORIGINLONG, PLOTSHAPE, PLOTAREA, "
	+" ALTVALUE, SLOPEASPECT, SLOPEGRADIENT, SLOPEPOSITION, HYDROLOGICREGIME, "
	+" SOILDRAINAGE, CURRENTCOMMUNITY, XCOORD, YCOORD, COORDTYPE, OBSSTARTDATE, "
	+" OBSSTOPDATE, EFFORTLEVEL, HARDCOPYLOCATION, SOILTYPE, SOILDEPTH, "
	+" PERCENTROCKGRAVEL, PERCENTSOIL, PERCENTLITTER, PERCENTWOOD, PERCENTWATER, "
	+" PERCENTSAND, PERCENTCLAY, PERCENTORGANIC, LEAFTYPE, PHYSIONOMICCLASS, "
	+" AUTHORPLOTCODE, SURFGEO, STATE, PARENTPLOT, AUTHOROBSCODE"
	+" from PLOTSITESUMMARY where PLOT_ID = "+currentPlotId;
		
	String returnFields[]=new String[40];
	int returnFieldLength=40;
	
	returnFields[0]="PLOT_ID";
	returnFields[1]="PROJECT_ID";
	returnFields[2]="PLOTTYPE";
	returnFields[3]="SAMPLINGMETHOD";
	returnFields[4]="COVERSCALE";
	returnFields[5]="PLOTORIGINLAT";
	returnFields[6]="PLOTORIGINLONG";
	returnFields[7]="PLOTSHAPE";
	returnFields[8]="PLOTAREA";
	returnFields[9]="ALTVALUE";
	returnFields[10]="SLOPEASPECT";
	returnFields[11]="SLOPEGRADIENT";
	returnFields[12]="SLOPEPOSITION";
	returnFields[13]="HYDROLOGICREGIME";
	returnFields[14]="SOILDRAINAGE";
	returnFields[15]="CURRENTCOMMUNITY";
	returnFields[16]="XCOORD";
	returnFields[17]="YCOORD";
	returnFields[18]="COORDTYPE";
	returnFields[19]="OBSSTARTDATE";
	returnFields[20]="OBSSTOPDATE";
	returnFields[21]="EFFORTLEVEL";
	returnFields[22]="HARDCOPYLOCATION";
	returnFields[23]="SOILTYPE";
	returnFields[24]="SOILDEPTH";
	returnFields[25]="PERCENTROCKGRAVEL";
	returnFields[26]="PERCENTSOIL";
	returnFields[27]="PERCENTLITTER";
	returnFields[28]="PERCENTWOOD";
	returnFields[29]="PERCENTWATER";
	returnFields[30]="PERCENTSAND";
	returnFields[31]="PERCENTCLAY";
	returnFields[32]="PERCENTORGANIC";
	returnFields[33]="LEAFTYPE";
	returnFields[34]="PHYSIONOMICCLASS";
	returnFields[35]="AUTHORPLOTCODE";
	returnFields[36]="SURFGEO";
	returnFields[37]="STATE";
	returnFields[38]="PARENTPLOT";
	returnFields[39]="AUTHOROBSCODE";

	
	/*
	* Call the issueSelect method which will return an array with the return
	*/

	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength, 
	summaryResultHash, conn);	
	
	//iterate the connection use counter
	connectionUses++;
	
	//take the results from the issueSelect and put into the array
	//note that the j.outReturnFieldsNum should always be = 1 in this case
	//because we are querying by a plot ID number which should be unique
	for (int ii=0;ii<j.outReturnFieldsNum; ii++) {
		summaryResult[i]=j.outReturnFields[ii];
	}
	
///System.out.println(">>>: "+j.outResultHash.toString());
	
// make a second query here to get the species specific information 

action="select";
statement="select AUTHORNAMEID, AUTHORPLOTCODE, STRATUMTYPE, PERCENTCOVER" 
	+" from PLOTSPECIESSUM where PLOT_ID = "+currentPlotId;

	int returnSpeciesFieldLength=4;
	String returnSpeciesFields[]=new String[4];	
	
	returnSpeciesFields[0]="AUTHORNAMEID";
	returnSpeciesFields[1]="AUTHORPLOTCODE";
	returnSpeciesFields[2]="STRATUMTYPE";
	returnSpeciesFields[3]="PERCENTCOVER";
	
	//issue the select statement
	issueStatement k = new issueStatement();
	k.issueSelect(statement, action, returnSpeciesFields, returnSpeciesFieldLength, 
	summaryResultHash, conn);
	
//	System.out.println("<<<K: "+k.outResultHash.toString());

//iterate the connection use counter
	connectionUses++;
	
	//if the connection has been used more than some arbitrary times renew conn
	if (connectionUses > 90) {
		
		System.out.println("queryStore.getPlotSummaryNew - connection uses:"
		+connectionUses);
		
		conn.close(); 
		myBroker.freeConnection(conn); 
		conn=myBroker.getConnection();
		connectionUses=0;
	}
	
	
	//take the results from this query and append to the summary line
	//which will ultimately be passed back to the xmlWriter to be tokenized
	//and writen to xml - againd there should only be one line returned 
///	for (int ii=0;ii<k.outReturnFieldsNum; ii++) {
///		summaryResult[i]=summaryResult[i]+k.outReturnFields[ii];
///	}

// add the individual plots (represented as their own hash) and include in the
// cumulative hash table
cumulativeSummaryResultHash.put("plot"+i,k.outResultHash);
	
summaryResultNum=i;
} //end for

//at the end add to the hash the number of plots
//System.out.println("???????> "+cumulativeSummaryResultHash.toString());


summaryOutput=summaryResult;
summaryOutputNum=summaryResultNum;
outConnectionUses=connectionUses;

} //end try
catch (Exception e) {System.out.println("failed in querySrore.getPlotSummaryNew"
		+" " + e.getMessage()); e.printStackTrace();}
		
}//end method

















/**
 * Method to query the database by using a plot id number to obtain a summary of 
 * that plot including 1] author plot code, 2] surfaceGeology
 * The input to the method is a string array containing plot id numbers and
 * an integer representing the number of elements in the array
 *
 * @param plotId - an array of plotId numbers
 * @param plotIdNum - the number of elements in the above array
 * @param conn - a database connection -- get rid of this and put the conn
 * management here in this class
 */
public void getPlotSummary(String plotId[], int plotIdNum, Connection conn)
{

//get the database management parameter settings
g.getDatabaseParameters("database", "query");

try {
myBroker = new DbConnectionBroker(g.driverClass, g.connectionString,
	g.login,g.passwd,g.minConnections,g.maxConnections, g.logFile+"QueryStore",1.0);
conn=myBroker.getConnection(); //grab one connection from pool

//this is the number of uses that an input connection has had
int connectionUses=1;


//this array will hold all the results before passing it back to the 
//calling class
String summaryResult[] = new String[10000];
int summaryResultNum=0;

//iterate through the plot id numbers
for (int i=0;i<plotIdNum; i++) {

//Because often the output of issueStatement is used as input here,
//and the output values are tokenized by pipes replace all the pipes below
String currentPlotId=plotId[i].replace('|',' ').trim();

	String action="select";
	String statement="select PLOT_ID, AUTHORPLOTCODE, project_id, "+
		"surfGeo,  PLOTTYPE, PLOTORIGINLAT, PLOTORIGINLONG, PLOTSHAPE, "
		+" PLOTSIZE, PLOTSIZEACC, ALTVALUE, ALTPOSACC, SLOPEASPECT, SLOPEGRADIENT, "
		+" SLOPEPOSITION, HYDROLOGICREGIME, SOILDRAINAGE, STATE, CURRENTCOMMUNITY "
		+" from PLOT where PLOT_ID = "+currentPlotId;
		
	String returnFields[]=new String[19];	
	returnFields[0]="PLOT_ID";	
	returnFields[1]="AUTHORPLOTCODE";
	returnFields[2]="project_id";
	returnFields[3]="surfGeo";
	returnFields[4]="PLOTTYPE";
	returnFields[5]="PLOTORIGINLAT";
	returnFields[6]="PLOTORIGINLONG";
	
	returnFields[7]="PLOTSHAPE";
	returnFields[8]="PLOTSIZE";
	returnFields[9]="PLOTSIZEACC";
	returnFields[10]="ALTVALUE";
	returnFields[11]="ALTPOSACC";
	returnFields[12]="SLOPEASPECT";
	returnFields[13]="SLOPEGRADIENT";
	returnFields[14]="SLOPEPOSITION";
	returnFields[15]="HYDROLOGICREGIME";
	returnFields[16]="SOILDRAINAGE";
	returnFields[17]="STATE";
	returnFields[18]="CURRENTCOMMUNITY";
	
	int returnFieldLength=19;

	/*
	* Call the issueSelect method which will return an array with the return
	*/

	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength, conn);	
	
	//iterate the connection use counter
	connectionUses++;
	
	//take the results from the issueSelect and put into the array
	//note that the j.outReturnFieldsNum should always be = 1 in this case
	//because we are querying by a plot ID number which should be unique
	for (int ii=0;ii<j.outReturnFieldsNum; ii++) {
		summaryResult[i]=j.outReturnFields[ii];
	}
	
	
// make a second query here to get the species specific information which will
// be appended onto the summary results line and ultimately tokenized in the 
// xmlWrter class - at some point this function may become its own method

	action="select";
	statement="select AUTHORNAMEID from taxonObservation where OBS_ID in"+
		"(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in"+
		"(select plot_ID from plot where PLOT_ID ="+currentPlotId+"))";

		
	String returnFieldsB[]=new String[1];	
	returnFieldsB[0]="AUTHORNAMEID";
	int returnFieldLengthB=1;
	
	//issue the select statement
	issueStatement k = new issueStatement();
	k.issueSelect(statement, action, returnFieldsB, returnFieldLengthB, conn);	

	//iterate the connection use counter
	connectionUses++;
	System.out.println("queryStore.getPlotSummary - connection uses:"
		+connectionUses);
	//if the connection has been used more than some arbitrary times renew conn
	if (connectionUses > 90) {
		conn.close(); 
		myBroker.freeConnection(conn); 
		conn=myBroker.getConnection();
		connectionUses=0;
	}
	
	
	//take the results from this query and append to the summary line
	//which will ultimately be passed back to the xmlWriter to be tokenized
	//and writen to xml - againd there should only be one line returned 
	for (int ii=0;ii<k.outReturnFieldsNum; ii++) {
		summaryResult[i]=summaryResult[i]+k.outReturnFields[ii];
	}
	
summaryResultNum=i;
} //end for

summaryOutput=summaryResult;
summaryOutputNum=summaryResultNum;
outConnectionUses=connectionUses;

} //end try
catch (Exception e) {System.out.println("failed in querySrore.getPlotSummary"
		+" " + e.getMessage());}
		
}//end method








/**
 * Method to query the database to get all the plotId's by using a single 
 * attribute including taxonName, surfGeo, communityName, etc.  This method is
 * linked to the 'handleSimpleQuery' method in the DataRequestServlet
 * @param     queryElement  the value of the attribute used to query
 * @param     queryElementType  the type of element used for querying the DB
 * @param     conn  a database connection that was presumedly taken from the pool 
 */
public void getPlotId(String queryElement, String queryElementType, Connection conn)
{

String queryTableName = null;  //name of the table for which the query to be made

//translate the query attributeName into the table name and determine the table
if ( queryElementType.equals("taxonName") ) {
	queryElementType = "AUTHORNAMEID";
	queryTableName = "PLOTSPECIESSUM";
}

else if ( queryElementType.equals("state") ) {
	queryElementType = "STATE";
	queryTableName = "PLOTSITESUMMARY";
}

else if ( queryElementType.equals("surfGeo") ) {
	queryElementType = "SURFGEO";
	queryTableName = "PLOTSITESUMMARY";
}

else if ( queryElementType.equals("communityName") ) {
	queryElementType = "CURRENTCOMMUNITY";
	queryTableName = "PLOTSITESUMMARY";
} 

else {System.out.println("plotQuery.getPlotId: unrecognized queryElementType: "
	+queryElementType); }

//this select statement will use the summary tables for the plots database where
//most of the database attributes are denormalized to two tables:
// 'plotSiteSummary' and 'plotSpeciesSum'
String action="select";
String statement="select PLOT_ID from "+queryTableName+" where "+queryElementType
+" like '%"+queryElement+"%'";


String returnFields[]=new String[1];	
returnFields[0]="PLOT_ID";
int returnFieldLength=1;

issueStatement j = new issueStatement();
j.issueSelect(statement, action, returnFields, returnFieldLength, conn);	

//grab the returned result set and transfer to a public array
//ultimately these results are passed to the calling class -- if for some reason
//the value was null then a string: 'nullValue' is returned
outPlotId=j.outReturnFields;
outPlotIdNum=j.outReturnFieldsNum;
	
} //end method




/**
 * Method to query the database to get all the plotId's by using a two attributes
 * currently only a min/max elevation will work - soon to implement a community name -
 * this method will be overloaded in methods below
 * @param     queryElement  the value of the attribute used to query
 * @param     queryElement2  the value of the attribute used to query
 * @param     queryElementType  the type of element used for querying the DB
 * @param     conn  a database connection that was presumedly taken from the pool 
 */
public void getPlotId(String queryElement, String queryElement2, 
	String queryElementType, Connection conn)
{
String action="select";
String statement="select PLOT_ID from PLOT where ALTVALUE >= "+queryElement+
	" and ALTVALUE <= "+queryElement2;
String returnFields[]=new String[1];	
returnFields[0]="PLOT_ID";
int returnFieldLength=1;


issueStatement j = new issueStatement();
j.issueSelect(statement, action, returnFields, returnFieldLength, conn);	


//grab the returned result set and transfer to a public array
//ultimately these results are passed to the calling class
outPlotId=j.outReturnFields;
outPlotIdNum=j.outReturnFieldsNum;
	
} //end method


/**
 * Method to query the database to get all the plotId's usinf as input the 
 * following query elements
 * 
 * @param taxonName
 * @param state
 * @param elevationMin
 * @param elevationMax
 * @param surfGeo
 * @param multipleObs
 * @param community
 *
 */

public void getPlotId(String taxonName, String state, String elevationMin, 
	String elevationMax, String surfGeo, String multipleObs, String community, 
	Connection conn)
{


System.out.println("queryStore.getPlotId - queryElements > \n"
	+" taxonName: "+taxonName+" \n state: "+state+" \n elevationMin: "
	+elevationMin+" \n elevationMax: "+elevationMax+" \n surfGeo: "+surfGeo
	+" \n multipleObs: "+multipleObs+" \n community: "+community);


String action="select";
String statement="select PLOT.PLOT_ID from PLOT where PLOT.ALTVALUE >="+elevationMin+
" and PLOT.ALTVALUE <="+elevationMax+" and PLOT.SURFGEO like '%"+surfGeo+"%' and "
+"PLOT.STATE like '%"+state+"%' and PLOT.CURRENTCOMMUNITY like '%"+community+"%' and PLOT_ID in"+
	"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
		"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%"+taxonName+"%'))";
String returnFields[]=new String[1];	
returnFields[0]="PLOT_ID";
int returnFieldLength=1;



issueStatement j = new issueStatement();
j.issueSelect(statement, action, returnFields, returnFieldLength, conn);	


//grab the returned result set and transfer to a public array
//ultimately these results are passed to the calling class
outPlotId=j.outReturnFields;
outPlotIdNum=j.outReturnFieldsNum;
	
} //end method

public String outPlotId[] = new String[10000];  //the output plotIds
public int outPlotIdNum; //the number of plotId's



}
