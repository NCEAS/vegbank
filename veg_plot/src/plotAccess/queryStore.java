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
 *
 */

public class  queryStore
 {



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
 * @param conn - a database connection 
 */
public void getPlotSummary(String plotId[], int plotIdNum, Connection conn)
{

//this array will hold all the results before passing it back to the 
//calling class
String summaryResult[] = new String[10000];
int summaryResultNum=0;

//this is the number of uses that an input connection has had
int connectionUses=1;

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
	
/**	
* make a second query here to get the species specific information which will
* be appended onto the summary results line and ultimately tokenized in the 
* xmlWrter class - at some point this function may become its own method
*/
	action="select";
	statement="select AUTHORNAMEID from taxonObservation where OBS_ID in"+
		"(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in"+
		"(select plot_ID from plot where PLOT_ID ="+currentPlotId+"))";

		
	String returnFieldsB[]=new String[1];	
	returnFieldsB[0]="AUTHORNAMEID";
	int returnFieldLengthB=1;


	/*
	* Call the issueSelect method which will return an array with the return
	*/

	issueStatement k = new issueStatement();
	k.issueSelect(statement, action, returnFieldsB, returnFieldLengthB, conn);	

	//iterate the connection use counter
	connectionUses++;
	
	
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
	
}//end method
public String summaryOutput[] = new String[10000];  //the output from the issue sql can be mapped to this varaiable
public int summaryOutputNum; //the number of output rows from the issue sql is mapped to this varable
public int outConnectionUses; //number of connection uses



/**
* Method to query the database to get all the plotId's by using a single attribute
* currently only a taxon will work - soon to implement a community name -
* this method will be overloaded in methods below
* @param     queryElement  the value of the attribute used to query
* @param     queryElementType  the type of element used for querying the DB
* @param     conn  a database connection that was presumedly taken from the pool 
*/
public void getPlotId(String queryElement, String queryElementType, Connection conn)
{
String action="select";
String statement="select PLOT_ID from PLOT where PLOT_ID in"+
	"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
		"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%"+queryElement+"%'))";
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
