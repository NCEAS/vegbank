import java.io.*;
import java.util.*;
import java.sql.*;



/**
* This class will insert a plot into the database the string passed to this 
* class is the transformed (via XSLT) XML data document and the string has 
* both a database address and the data which wil have to be converted to
*  the correct type and then bound to one of the prepared statements
*/

public class plotWriter {
	
/**
* This method takes the input stream from the xslt transform and inserts the 
* data into their respective cells, at this point the method is extremely long 
* because there is one or a series of prepared statements for loading each 
* table.  Currently working on a method in the issueSQL class that will 
* replace all the prepared statements.
*/	

public void insertPlot (String[] transformedString, int transformedStringNum) {


/**
*  The ordering of events is critical in this method and will be in the following 
*  order:  validate if project exists, update project, validate if plot exists, 
*  update plots
*/
	
//call the setAttributeAddress method to separate the database address from the data
	
plotWriter a = new plotWriter();
a.setAttributeAddress(transformedString, transformedStringNum);
	
String address[]=a.dbAddress;  // the returned database address string
String dataString[]=a.attributeData;  // the returened data string
int addressNum=a.dbAddressNum; //the number of addresses and attributes in the returned set
	
	
//Define the variable that will be reused in this method	
String projectId=null;  //this is the global variable b/c it will be needed for inserting the plots etc.
String plotId=null;  //this is the global variable b/c it will be needed for inserting the plot observations etc.
String plotObservationId=null; //this is the global variable b/c it will be needed for inserting taxon observations etc.
int strataId=0; //this is the global variable b/c it will be needed for inserting taxon observations etc.
int taxonObservationId=0;
int namedPlaceId=0;
int graphicId=0;


/**
* Grab a pool of database connections that can be accessed from any where in
* the remainder of the method
*/
//replace this with the connection pooling option
Connection conn=null;  //this is a connection opend from within the dbConnect class
Connection pconn=null; //this is a connection that is opened from within the pool
Connection pconn1=null; //this is a connection that is opened from within the pool
Statement query = null;
ResultSet results = null;
	
//get the database parameters from the database.parameters file
utility g2 =new utility(); 
g2.getDatabaseParameters("database", "insert");

		
System.out.println(g2.driverClass+" "
	+"driverClass: "+g2.driverClass+" \n"
	+"connectionString: "+g2.connectionString+" \n"
	+"login: "+g2.login+" \n"
	+"password: "+g2.passwd+" \n"
	+"minConnection: "+g2.minConnections+" \n"
	+"maxConnections: "+g2.maxConnections+" \n"
	+"pooling logFile: "+g2.logFile);


DbConnectionBroker myBroker;
try {

myBroker = new DbConnectionBroker(g2.driverClass,
                                  g2.connectionString,
                                  g2.login,g2.passwd,
				  g2.minConnections,g2.maxConnections,
                                  g2.logFile,1.0);


//myBroker = new DbConnectionBroker("oracle.jdbc.driver.OracleDriver",
//                                  "jdbc:oracle:thin:@dev.nceas.ucsb.edu:1521:exp",
//                                  "harris","use4dev",8,20,
//                                  "DCB_Example1.log",1.0);

				  
//Get a DB connection from the Broker
int thisConnection;
pconn= myBroker.getConnection();
pconn1=myBroker.getConnection();
thisConnection = myBroker.idOfConnection(conn);

//This is an attempt to devise a scheme to see how many times
//a database connection is used if more than like 50 times 
//check it back into the pool and grab a new one - the exception
//thrown otherwise is exeeded max cursors
int connectionUses=0;

for (int ii=0; ii<addressNum; ii++) 
{
		
//insert project data
	if (address[ii] != null && address[ii].startsWith("project.projectName")) {
	System.out.println("XMLfile PROJECTNAME: "+dataString[ii]);	
		
	plotWriter g =new plotWriter();  
	g.putProject(dataString[ii], dataString[ii], pconn);
	projectId=g.outProjectId;
	//increment the connection uses
	connectionUses++;
	}//end if


//insert the plot data
	if (address[ii] != null && address[ii].startsWith("plot.authorPlotCode")) {
	System.out.println("XMLfile AUTHORPLOTNAME: "+dataString[ii]);	
	String authorPlotCode=dataString[ii];
	String parentPlot=dataString[ii+1];
	String plotType =dataString[ii+2];
	String samplingMethod =dataString[ii+3];
	String coverScale=dataString[ii+4];
	String latitude=dataString[ii+5];
	String longitude=dataString[ii+6];
	String plotShape=dataString[ii+7];
	String plotSize=dataString[ii+8];
	String plotSizeAcc=dataString[ii+9];
	String altValue=dataString[ii+10];
	plotWriter g =new plotWriter();  
	g.putPlot(projectId, authorPlotCode, parentPlot, plotType, samplingMethod, coverScale, latitude,
	longitude, plotShape, plotSize, plotSizeAcc, altValue, pconn);
	plotId=g.outPlotId;

	//increment the connection uses - couple times for its entire use
	connectionUses= connectionUses+4;
		
	}//end if

	
//insert the plotObservationData
	if (address[ii] != null && address[ii].startsWith("plotObservation.previousObs")) {
	System.out.println("XMLfile OBSERVATIONCODE: "+dataString[ii+1]);	
	String previousObservation=dataString[ii+1];
	String plotObservationCode=dataString[ii+1];	
	plotWriter g =new plotWriter();  
	g.putPlotObservation(plotId, previousObservation, plotObservationCode, pconn);		
	plotObservationId=g.outPlotObservationId;
	
	//increment the connection uses - couple times for its entire use
	connectionUses= connectionUses+4;
	
	}//end if

		
//insert the strata
	if (address[ii] != null && address[ii].startsWith("strata.stratumType")) {
	System.out.println("XMLfile STRATUMTYPE: "+dataString[ii]);	
	String stratumType=dataString[ii];
	String stratumCover=dataString[ii+1];
	String stratumHeight=dataString[ii+2];
	
	plotWriter g =new plotWriter();  
	g.putStrata(plotObservationId, stratumType, stratumCover, stratumHeight, pconn);				
	
	//increment the connection uses - couple times for its entire use
	connectionUses= connectionUses+4;
	}//end if
			
		
//insert the taxonObservation
	if (address[ii] != null && address[ii].startsWith("taxonObservation.authNameId")) {
	System.out.println("XMLfile TAXONNAME: "+dataString[ii]);
	String authorNameId=dataString[ii];
	String originalAuthority=dataString[ii+1];
	String stratumType=dataString[ii+2];
	String percentCover=dataString[ii+3];
	
	plotWriter g =new plotWriter();  
	g.putTaxonObservation(plotObservationId, authorNameId, originalAuthority, 
		pconn);
	String taxonObservation=g.outTaxonObservationId;

	
	//insert the strataComposition too  -- from the same if statement
	
	//get the strataId and strataCompositionId needed for this insertion
	//by calling the method below

	
	int buf=Float.valueOf(plotObservationId).intValue();  //convert the string to and int
	plotWriter gs=new plotWriter();  
	gs.getStrataComposition(buf,  stratumType, pconn);
			
	int returnStrataId=gs.outStrataId;
	int strataCompositionId=gs.outStrataCompositionId;

	plotWriter h =new plotWriter();  
	h.putStrataComposition(returnStrataId, strataCompositionId, taxonObservation, 
		stratumType, percentCover, pconn);
	
	//(int strataId, int strataCompositionId, String taxonObservationId, 
	//String stratumType, String percentCover) {
	
	
	//increment the connection uses - couple times for its entire use
	//check to see if more than 50 then check it in -- this is the failure
	//that I have been getting: using connection 49 times
	//java.sql.SQLException: ORA-01000: maximum open cursors exceeded
	connectionUses= connectionUses+2;
	System.out.println("number of times the connection has been used: "
	+connectionUses);
	
	if (connectionUses>70) {
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
		
	
	}//end if


//insert the named place information	
	if (address[ii] != null && address[ii].startsWith("namedPlace.placeName")) {
		
	System.out.println("XMLfile placeName: "+dataString[ii]);	
	String placeName=dataString[ii];
	String placeDesc=dataString[ii+1];
	
	plotWriter g =new plotWriter();  
	g.putNamedPlace(placeName, placeDesc, pconn1);	
	

}//end if

/**
//insert the grahical information		
		
		
		if (address[ii] != null && address[ii].startsWith("graphic.graphicName")) {
			
			plotWriter g =new plotWriter();  
			g.getNextId(dataString[ii], "graphic");
			
			graphicId=g.outNextId;  //grab the nextValue in the graphic table
			
			//pass the required arguements to the isssue SQl class
			String insertString="INSERT INTO GRAPHIC";
			String attributeString="GRAPHIC_ID, PLOT_ID, BROWSEN, BROWSED, BROWSET";
			int inputValueNum=5;
			String inputValue[]=new String[5];	
			inputValue[0]=""+graphicId;
			inputValue[1]=""+plotId;
			inputValue[2]=dataString[ii];
			inputValue[3]=dataString[ii+1];
			inputValue[4]=dataString[ii+2];

			//get the valueString from the method
			issueStatement k = new issueStatement();
			k.getValueString(inputValueNum);	
			String valueString = k.outValueString;
			
				issueStatement j = new issueStatement();
				j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);	
	

		
		}//end if
		
*/		
		
		
		
		
	//remember to close the connection here
	}  //end for
	
// The connection is returned to the Broker
//myBroker.freeConnection(conn);
myBroker.freeConnection(pconn);
myBroker.freeConnection(pconn1);
myBroker.destroy();	
}//end try -- connection pooling
catch (IOException e5)  {System.out.println("connection pooling failed: "+e5.getMessage());}

} //end method
	
	
	
	
	
	
	

/**
*  Method that takes as input the attributes in the project table and insert/
* update the table after perfroming a check to determine if there is redundancy
* in the table
*/

private void putProject (String projectName, String projectDescription, Connection conn) {
int projectId=0;
try {
		
//check to see if the project already exists  by making a query
//and sending it to the issue statement class
		
	String action="select";
	String statement="select PROJECT_ID from PROJECT where PROJECTNAME like '%"+projectName+"%'";
	String returnFields[]=new String[1];	
	returnFields[0]="PROJECT_ID";
	int returnFieldLength=1;

	/**
	* Call the issueSelect method which will return an array with the return
	* values containing the project_id number associated with this
	* plot file if the project_id is null then iterate the
	* project_id number if there are more than a single project_id
	* numbers then the database is corrupt and send a message
	* relaying this
	*/

	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength, conn);
	
	
	//if redundancies	
	if (j.outReturnFieldsNum>1) {
		System.out.println("WARNING: corrpted database multiple projects with identical name");
		System.out.println("  Number of redundancies: "+j.outReturnFieldsNum);
		
		//return the first project id that matches to caller
		outProjectId=j.outReturnFields[0].replace('|', ' ').trim();;
	}
		
	//if single match found
	if (j.outReturnFieldsNum==1) {
		
		outProjectId=j.outReturnFields[0].replace('|', ' ').trim();  //return to caller
		System.out.println("Matching project found, ID number: "+j.outReturnFields[0]);
	
	}
	
	//if zero matches found then create a new project_id number

	if (j.outReturnFieldsNum==0) {
		System.out.println("No Matching projects found - creating a new one \n"+
		"Project ID number: ");
		
		//grab the next project ID number
		plotWriter g =new plotWriter();  
		g.getNextId("test", "project", conn);
		projectId=g.outNextId;  //assign the projectID
		outProjectId=""+projectId;
		outProjectId=outProjectId.replace('|', ' ').trim();
		
		//pass the required arguements to the isssue SQl class
		String insertString="INSERT INTO PROJECT";
		String attributeString="PROJECT_ID, PROJECTNAME, DESCRIPTION";
		int inputValueNum=3;
		String inputValue[]=new String[3];	
		inputValue[0]=""+projectId;
		inputValue[1]=projectName;
		inputValue[2]=projectDescription;

		//get the valueString from the method
		issueStatement k = new issueStatement();
		k.getValueString(inputValueNum);	
		String valueString = k.outValueString;
		//issue the above statement
		issueStatement l = new issueStatement();
		l.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue, conn);
	
	}
			
	} //end try
	catch (Exception e) {System.out.println("failed in plotWriter.putProject: " + 
	e.getMessage());}


}  //end method
public String outProjectId=null;



/**
*  Method that takes as input the attributes in the plot table and insert/
* update the table after perfroming a check to determine if there is redundancy
* in the table
*/


private void putPlot (String projectId, String authorPlotCode, String parentPlot, 
String plotType, String samplingMethod, String coverScale, String latitude, 
String longitude, String plotShape, String plotSize, String plotSizeAcc, String altValue, 
Connection conn) {
int plotId=0;
try {


//add here a redundancy checker
	
//grab the next value in the plot table
plotWriter g =new plotWriter();  
g.getNextId("test", "plot", conn);
			
plotId=g.outNextId;  //grab the returned value		
outPlotId=""+plotId; //pass to public
System.out.println("PlotID returned: "+plotId);  //this is check the return values 
		
//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO PLOT";
String attributeString="PLOT_ID, PROJECT_ID, AUTHORPLOTCODE, PARENTPLOT, PLOTTYPE, "+
"SAMPLINGMETHOD, COVERSCALE, PLOTORIGINLAT, PLOTORIGINLONG, PLOTSHAPE, PLOTSIZE, PLOTSIZEACC, ALTVALUE";
int inputValueNum=13;
String inputValue[]=new String[13];	
inputValue[0]=""+plotId;
	System.out.println(" plotId: "+plotId);
inputValue[1]=projectId;
	System.out.println(" projectId: "+projectId);
inputValue[2]=authorPlotCode;
	System.out.println(" authorPlotCode: "+authorPlotCode);
inputValue[3]=parentPlot;
	System.out.println(" parentPlot: "+parentPlot);
inputValue[4]=plotType;
	System.out.println(" plotType: "+plotType);
inputValue[5]=samplingMethod;
	System.out.println(" samplingMethod: "+samplingMethod);
inputValue[6]=coverScale;
	System.out.println(" coverScale: "+coverScale);
inputValue[7]=latitude;
	System.out.println(" latitude: "+latitude);
inputValue[8]=longitude;
	System.out.println(" plotShape: "+plotShape);
inputValue[9]=plotShape;
	System.out.println(" plotSize: "+plotSize);
inputValue[10]=plotSize;
	System.out.println(" plot size accuracy: "+plotSizeAcc);
inputValue[11]=plotSizeAcc;
	System.out.println(" elevation: "+altValue);
inputValue[12]=altValue;
//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue, conn);	

}
catch (Exception e) {System.out.println("failed in plotWriter.putPlot " + 
	e.getMessage());}
	
} //end method
public String outPlotId=null;

	

/**
*  Method that takes as input the attributes in the plot observation table and 
* insert/update the table after perfroming a check to determine if there is 
* redundancy in the table
*/

private void putPlotObservation (String plotId, String previousObservation, 
String plotObservationCode, Connection conn) {
int plotObservationId=0;
try {

//grab the next value in the plotObservation table
plotWriter g =new plotWriter(); 

g.getNextId("test", "plotObservation", conn);				
plotObservationId=g.outNextId;  //grab the returned value
outPlotObservationId=""+plotObservationId; //pass to public
		
//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO PLOTOBSERVATION";
String attributeString="OBS_ID, PARENTPLOT, AUTHOROBSCODE";
int inputValueNum=3;
String inputValue[]=new String[3];	
inputValue[0]=""+plotObservationId;
inputValue[1]=plotId;
inputValue[2]=plotObservationCode;

//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue,
	conn);	
	
}
catch (Exception e) {System.out.println("failed in plotWriter.putPlotObservation " + 
	e.getMessage());}	
} //end method
public String outPlotObservationId=null;



/**
*  Method that takes as input the attributes in the strata related tables and 
* insert/update the table with the input data
*/
private void putStrata (String plotObservationId, String stratumType, String 
stratumCover, String stratumHeight, Connection conn) {
int strataId=0;
try {

//grab the next value in the strata table
plotWriter g =new plotWriter();  
g.getNextId("test", "strata", conn);
				
strataId=g.outNextId;  //grab the returned value
		
//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO STRATA";
String attributeString="STRATA_ID, OBS_ID, STRATUMTYPE, STRATUMCOVER, STRATUMHEIGHT";
int inputValueNum=5;
String inputValue[]=new String[5];	
inputValue[0]=""+strataId;
inputValue[1]=plotObservationId;
inputValue[2]=stratumType;
inputValue[3]=stratumCover;
inputValue[4]=stratumHeight;

//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, 
	inputValue, conn);	
				
}
catch (Exception e) {System.out.println("failed in plotWriter.putStrata " + 
	e.getMessage());}
}  //end method



/**
*  Method that takes as input the attributes from the taxonObservation tables and 
* inserts/updates the table with the input data
*/

private void putTaxonObservation (String plotObservationId, String authorNameId, 
	String originalAuthority, Connection conn) {
int taxonObservationId=0;
try {
	//grab the next value in the strata table
	plotWriter g =new plotWriter();  
	g.getNextId("test", "taxonObservation", conn);
				
	taxonObservationId=g.outNextId;  //grab the returned value
	outTaxonObservationId=""+taxonObservationId;

	//taxonobservation_id, OBS_ID, authorNameId, originalAuthority) " +
	
	//pass the required arguements to the isssue SQl class
	String insertString="INSERT INTO TAXONOBSERVATION";
	String attributeString="TAXONOBSERVATION_ID, OBS_ID, AUTHORNAMEID, ORIGINALAUTHORITY";
	int inputValueNum=4;
	String inputValue[]=new String[4];	
	inputValue[0]=""+taxonObservationId;
	inputValue[1]=plotObservationId;
	inputValue[2]=authorNameId;
	inputValue[3]=originalAuthority;

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);	
	String valueString = k.outValueString;
	//issue the above statement
	issueStatement l = new issueStatement();
	l.issueInsert(insertString, attributeString, valueString, inputValueNum, 
		inputValue, conn);	
				

}
catch (Exception e) {System.out.println("failed in plotWriter.putTaxonObservation " + 
	e.getMessage());}
}  //end method
public String outTaxonObservationId=null;


/**
*  Method that takes as input the attributes from the strataComposition tables and 
* inserts/updates the table with the input data
*/
private void putStrataComposition (int strataId, int strataCompositionId, 
	String taxonObservationId, String stratumType, String percentCover, Connection conn) {

try {
String insertString="INSERT INTO STRATACOMPOSITION";
String attributeString="STRATACOMPOSITION_ID, TAXONOBSERVATION_ID, STRATA_ID, CHEATSTRATUMTYPE, PERCENTCOVER";
int inputValueNum=5;
String inputValue[]=new String[5];	
inputValue[0]=""+strataCompositionId;
inputValue[1]=taxonObservationId;
inputValue[2]=""+strataId;
inputValue[3]=stratumType;
inputValue[4]=percentCover;

//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue, conn);	

}
catch (Exception e) {System.out.println("failed in plotWriter.putStrataComposition " + 
	e.getMessage());}
}  //end method


/**
*  Method that takes as input the attributes associated with the namedPlace table 
* inserts/updates the table with the input data
*/
private void putNamedPlace (String placeName, String placeDesc, Connection conn) {
int namedPlaceId=0;
try {

//grab the next value in the namedPlace table
plotWriter g =new plotWriter();  
g.getNextId("test", "namedPlace", conn);
				
namedPlaceId=g.outNextId;  //grab the returned value
System.out.println("******** "+namedPlaceId);


String insertString="INSERT INTO NAMEDPLACE";
String attributeString="namedplace_id, placeName, placeDesc";
int inputValueNum=3;
String inputValue[]=new String[3];	
inputValue[0]=""+namedPlaceId;
inputValue[1]=placeName;
inputValue[2]=""+placeDesc;

//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, 
	inputValue, conn);	
	
}
catch (Exception e) {System.out.println("failed in plotWriter.putNamedPlace " + 
	e.getMessage());}
} //end method




/**
*  Method that takes as input the attributes associated with the graphic table 
* inserts/updates the table with the input data
*/
private void putGraphic () {
try {


	
}
catch (Exception e) {System.out.println("failed in plotWriter.putNamedPlace " + 
	e.getMessage());}
} //end method




	
/**
* general method to tokenize a string of type: col1|col2 into two seperate 
* strings called dbAddress and attributeData.  There is a bug here: there are 
* some strings included in the output that have nulls and have to be stripped 
* in the above method - I can not figure out what is the problem
*/
	
private void setAttributeAddress (String[] combinedString, int combinedStringNum) {
	
	
	try {
		int count=0;
		for (int ii=0; ii<combinedStringNum; ii++) { 
	
			if (combinedString[ii].indexOf("|")>0 && combinedString[ii].trim() != null ) {  //make sure to tokenize an appropriate string
			//System.out.println(combinedString[ii]);
			StringTokenizer t = new StringTokenizer(combinedString[ii].trim(), "|");  //tokenize the string to get the requyired address
			//count++;
				if (t.hasMoreTokens())	{	//make sure that there are more tokens or else replace with the null value 
					dbAddress[count]=t.nextToken();
				}				//capture the required address
				else {dbAddress[count]="-999.25";}
				
				
				if (t.hasMoreTokens() )	{	//make sure that there are more tokens or else replace with the null value (-999)
					attributeData[count]=t.nextToken();
				} 				//capture the attributeString
				else {attributeData[count]="-999.25";}  //do the replacement
			
			
			//System.out.println(dbAddress[count]+" "+attributeData[count]+" "+ii+" "+count);
			
			}//end if
			count++;
	dbAddressNum=count;
			
		}//end for

	
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.setAttributeAddress  "+e.getMessage());}
	
	}  //end method
	
	
/**
* method to return the next integer for an index from a table
* in future versions this should be lumped with a switch that 
* will warn if there is a repetative value
*/
private void getNextId (String projectName, String tableName) {
	
	try {
	//Define the variable that will be reused in this method
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	//get a database connection
	try {
	dbConnect m = new dbConnect();
	m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
	}//end try 
	catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection call" + e.getMessage());}



	//this is a skeleton, the method just returns the next id value but in a future 
	//revision will look to see if the project exists and will return some warning
	
	try {	
		//results =query.executeQuery("SELECT project_id from  project where projectName like '%"+projectName+"%'");
		
		ResultSet lastValue=query.executeQuery("SELECT count(*) from "+tableName+"");
			
			int lastNum=0;
                        while (lastValue.next()) {
				lastNum = lastValue.getInt(1);
				}
                        lastNum++;
                        outNextId=lastNum;
			
			conn.close();
			query.close();
			

	} //end try
	catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to query the next id value in a table data: " + e.getMessage());}
	
		
		
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.getProjectId  "+e.getMessage());}
}  //end method




/**
*  This is and overloaded version of the above method where a connection may be
*  passed directly into the method inorder to figure out the number of rows in
*  a table - use this one if possible because it does not have the potential to
*  fail from the inability to get a db connection for the connection comes from
*  the pool being managed, presumedly, in the calling class
*/
private void getNextId (String projectName, String tableName, Connection pconn) {

//Connection conn=null;
Statement query = null;
ResultSet results = null;


/**
* transfer the database connection
*/
try {
	//conn=pconn;
	query = pconn.createStatement ();;	
} catch (Exception e)  {System.out.println("failed at plotWriter.getNextId "
	+"transfering db connections"
	+ e.getMessage());e.printStackTrace();}
	
try {	
//results =query.executeQuery("SELECT project_id from  project where projectName like '%"+projectName+"%'");
	ResultSet lastValue=query.executeQuery("SELECT count(*) from "+tableName+"");
			
		int lastNum=0;
                        while (lastValue.next()) {
				lastNum = lastValue.getInt(1);
				}
		lastNum++;
                outNextId=lastNum;
		/*don't close this connection because it is intended for re-use*/	
		//pconn.close();
		query.close();
		lastValue.close();
			

} //end try
catch (Exception e) {System.out.println("failed in plotWriter.getNextId trying "
	+"to query the next id value in a table data: " + e.getMessage());}



} //end method




/**
* a method to get the required information for the strataComposition table which 
* is an intersection between that strata and the taxonObservation. 1] take as 
* input the observationId, and stratumType to gain 2] the output containing 
* strataCompositionId, strataId - this table should be denormalized to include 
* stratum type
*/

		
private void getStrataComposition (int observationId, String stratumType ) {
	
	
	try {
	//Define the variable that will be reused in this method
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	//get a database connection
	
	try {
	dbConnect m = new dbConnect();
	m.makeConnection(conn, query);
	conn=m.outConn;
	query=m.outStmt;	
	}//end try 
	catch (Exception e) {System.out.println("failed calling dbConnect.makeConnection call" + e.getMessage());}

	//query the starta table to get the strata_id
	try {	
		int strataId=0;
		System.out.println("passed values: "+observationId+" "+stratumType);
		ResultSet strataIdResult=query.executeQuery("SELECT strata_id from strata where OBS_ID = "+observationId+
		"and stratumType like '%"+stratumType+"%'");
			
			
                        while (strataIdResult.next()) {
				strataId = strataIdResult.getInt(1);
				}
                        
			System.out.println("returned startaId: "+strataId);		
			outStrataId=strataId;  
			
			//now get the last index/integer in the strataComposition table
			
			plotWriter g =new plotWriter();  
			g.getNextId("strataComposition", "strataComposition");
	
			
			
			outStrataCompositionId=g.outNextId;  //grab the returned value
			System.out.println("strataCompositionId: "+outStrataCompositionId);
		
			
			//conn.close();
			query.close();
			

	} //end try
	catch (Exception e) {System.out.println("failed in plotWriter.insertPlot trying to query the next id value in a table data: " + e.getMessage());}
		
	} //end try
	catch ( Exception e ){System.out.println("failed at: plotWriter.getProjectId  "+e.getMessage());}
		
}//end method	
	
	
	
/**
* a method to get the required information for the strataComposition table which 
* is an intersection between that strata and the taxonObservation. 1] take as 
* input the observationId, and stratumType to gain 2] the output containing 
* strataCompositionId, strataId - this table should be denormalized to include 
* stratum type -- this is an overloaded version of the above method
*/


private void getStrataComposition (int observationId, String stratumType, 
	Connection pconn) {
	
try {
//Define the variable that will be reused in this method
Connection conn=null;
Statement query = null;
ResultSet results = null;
	
/**
* transfer the database connection
*/
try {
	conn=pconn;
	query = conn.createStatement ();	
} catch (Exception e)  {System.out.println("failed at plotWriter.getStrataComposition "
	+"transfering db connections"
	+ e.getMessage());e.printStackTrace();}

//query the starta table to get the strata_id
try {	
	int strataId=0;
	System.out.println("passed values: "+observationId+" "+stratumType);
	ResultSet strataIdResult=query.executeQuery("SELECT strata_id from strata where OBS_ID = "+observationId+
	"and stratumType like '%"+stratumType+"%'");
			
			
	while (strataIdResult.next()) {
		strataId = strataIdResult.getInt(1);
	}
                        
	System.out.println("returned startaId: "+strataId);		
	outStrataId=strataId;  
			
	//now get the last index/integer in the strataComposition table
		
	plotWriter g =new plotWriter();  
	g.getNextId("strataComposition", "strataComposition", conn);
	
			
			
	outStrataCompositionId=g.outNextId;  //grab the returned value
	System.out.println("strataCompositionId: "+outStrataCompositionId);
		
	/*dont close the connection, it is needed elsewhere*/	
	//conn.close();
	query.close();
			

} //end try
catch (Exception e) {System.out.println("failed in plotWriter.getStrataComposition " + e.getMessage());}
		
} //end try
catch ( Exception e ){System.out.println("failed at: plotWriter.getStrataComposition  "+e.getMessage());}
		
}//end method
	
	
	
	
public String dbAddress[] =new String[100000];
public String attributeData[] =new String[100000];
public int dbAddressNum;

public int outNextId;  // this is the value returned from teh getProjectId method
	
public int outStrataId; //this is the return value from the getStrataCampositionId method
public int outStrataCompositionId; //this is the return value from the getStrataCampositionId method
	
	
}

