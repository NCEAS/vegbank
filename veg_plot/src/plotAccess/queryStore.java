import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;



/**
* This class stores the sql code that will be issued through the 
* issueStatement class as directed by the sqlMapper class
*/


public class  queryStore
{


/**
* Method to query the database by using a plot id number to obtain a summary of 
* that plot including 1] author plot code, 2] surfaceGeology
* The input to the method is a string array containing plot id numbers and
* an integer representing the number of elements in the array
*/
public void getPlotSummary(String plotId[], int plotIdNum)
{

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
		"surfGeo,  PLOTTYPE, PLOTORIGINLAT, PLOTORIGINLONG  from PLOT "+
		"where PLOT_ID = "+currentPlotId;
		
	String returnFields[]=new String[7];	
	returnFields[0]="PLOT_ID";	
	returnFields[1]="AUTHORPLOTCODE";
	returnFields[2]="project_id";
	returnFields[3]="surfGeo";
	returnFields[4]="PLOTTYPE";
	returnFields[5]="PLOTORIGINLAT";
	returnFields[6]="PLOTORIGINLONG";
	int returnFieldLength=7;


	/*
	* Call the issueSelect method which will return an array with the return
	*/

	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);	

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

/*		
select AUTHORNAMEID, ORIGINALAUTHORITY from taxonObservation where OBS_ID in
	(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in 
		(select plot_ID from plot where PLOT_ID = 2));
*/		
	String returnFieldsB[]=new String[1];	
	returnFieldsB[0]="AUTHORNAMEID";
	int returnFieldLengthB=1;


	/*
	* Call the issueSelect method which will return an array with the return
	*/

	issueStatement k = new issueStatement();
	k.issueSelect(statement, action, returnFieldsB, returnFieldLengthB);	

	//take the results from this query and append to the summary line
	//which will ultimatell be passed back to the xmlWriter to be tokenized
	//and writen to xml - againd there should only be one line returned 
	for (int ii=0;ii<k.outReturnFieldsNum; ii++) {
	summaryResult[i]=summaryResult[i]+k.outReturnFields[ii];
	}
	



summaryResultNum=i;
} //end for

summaryOutput=summaryResult;
summaryOutputNum=summaryResultNum;
	

	
}//end method
public String summaryOutput[] = new String[10000];  //the output from the issue sql can be mapped to this varaiable
public int summaryOutputNum; //the number of output rows from the issue sql is mapped to this varable




/**
* Method to query the database to get all the plotId's 
* for the plots containing a taxon that matches the input 
*/
public void getPlotId(String queryElement, String queryElementType)
{
String action="select";
String statement="select PLOT_ID from PLOT where PLOT_ID in"+
	"(select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in"+
		"(select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%"+queryElement+"%'))";
String returnFields[]=new String[1];	
returnFields[0]="PLOT_ID";
int returnFieldLength=1;


/**
* Call the issueSelect method which will return an arry with the return
* values
*/

issueStatement j = new issueStatement();
j.issueSelect(statement, action, returnFields, returnFieldLength);	


//grab the returned result set and transfer to a public array
//ultimately these results are passed to the calling class
outPlotId=j.outReturnFields;
outPlotIdNum=j.outReturnFieldsNum;
	
} //end method

public String outPlotId[] = new String[10000];  //the output plotIds
public int outPlotIdNum; //the number of plotId's



/**
* Method to query the database to retrieve all the plot attributes
* by using a plotID
*/
public void getPlot(int plotId)
{

	
} //end method

}
