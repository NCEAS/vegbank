/**
 * 
 *    Purpose: This class takes the traslated value of an xml file and
 * 			puts the data into the veg plots database.  The translated 
 *			data input to this class must be from an xml document that
 *			conforms to the 'vegPlot2001cv.dtd' and has had been translated 
 *			by the 'vegPlot2001DBTrans.xsl' style sheet
 *			
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 *
 */

import java.io.*;
import java.util.*;
import java.sql.*;
import java.lang.*;



public class PlotDBWriter 
{




public String dbAddress[] =new String[100000];  //array of database addresses
public String attributeData[] =new String[100000]; //array of repective values
public int dbAddressNum; //number of dbAddresses
public int outNextId;  //this is the value returned from teh getProjectId method	
public int outStrataId; //this is return value getStrataCampositionId method
public int outStrataCompositionId; //this is return getStrataCampositionId method
public String outTaxonObservationId=null;

int namedPlaceId=0;
int graphicId=0;

//plot specific site information
Hashtable plotSiteParams = new Hashtable();
//species specific site information
Hashtable plotSpeciesParams = new Hashtable();
//initialize the vector to store the multitude of taxon names, starta, and covers
// that may be passed to this method -- used in the plotElementMapper method
Vector taxonNameElements= new Vector();
Vector strataTypeElements = new Vector();
Vector coverAmountElements = new Vector();

//plot observation specific information
Hashtable plotObservationParams = new Hashtable();
Vector stratumNameElements = new Vector();  //recognized strata names
Vector stratumHeightElements = new Vector(); //recognized strata heights
Vector stratumCoverElements = new Vector(); //recognized strata cover vals


//project specific information
Hashtable plotProjectParams = new Hashtable();
//community specific information
Hashtable plotCommunityParams = new Hashtable();
// this is the hash that contains all the elements and will be passed to 
// the writer method

boolean dataValid = false;

//database attributes required to load a single plot

String projectId=null;  //projectId for current project
String plotId=null;  //plotId for current plot
String plotObservationId=null; //observationId for currentPlot.
public String outPlotObservationId=null;
String strataId=null;
String projectName=null;
String projectDescription=null;
String authorPlotCode=null;
String parentPlot=null;
String plotType=null;
String samplingMethod=null;
String coverScale=null;
String latitude =null;
String longitude=null;
String plotShape=null;
String plotSize=null;
String plotSizeAcc=null;
String altValue=null;
String surfGeo=null;
String state=null;
String hydrologicRegime=null;
String xCoord=null;
String yCoord=null;
String coordType=null;
String topoPosition=null;
String slopeGradient=null;
String slopeAspect=null;

//community info
String currentCommunity=null;
String CEGLCode=null;

String plotObservationCode=null;
String soilDepth=null;
String soilType=null;
String percentRockGravel=null;

String taxonObservationId=null;
String stratumType=null;
String stratumCover=null;
String percentCover=null;
String stratumHeight=null;
String authorNameId=null;
String originalAuthority=null;

String strataCompositionId=null;


/**
 * This method takes the input stream from the xslt transform and inserts the 
 * data into their respective cells, at this point the method is extremely long 
 * because there is one or a series of prepared statements for loading each 
 *  table. 
 *
 * @param transformedString -- array containg the database.address and value 
 *			in pipe delimited string
 * @param transformedStringNum -- the number of elements in the array
 * @param dataType -- this refers to the type of data that the user wants to
 * upload and vaible options may include: plot, community, coverIndices
 * speciesValues etc. - due to the suspicion that people may desire to load
 * multiple data components individually
 */	

public void insertPlot (String[] transformedString, int transformedStringNum,
			String dataType) 
{


//call the setAttributeAddress method to separate the database address from 
//their respective data
	
PlotDBWriter a = new PlotDBWriter();
a.setAttributeAddress(transformedString, transformedStringNum);
	
String address[]=a.dbAddress;  // the returned database address string
String dataString[]=a.attributeData;  // the returened data string
int addressNum=a.dbAddressNum; //the number of addresses and attributes	



//read thru the array and position the elements into respective hash tables
for (int ii=0; ii<addressNum; ii++) 

{
//	System.out.println(ii+" "+address[ii]);
	
	//project level information
	if ( address[ii] !=null && address[ii].startsWith("project") ) {
		//System.out.println(ii+" "+address[ii]);
		plotProjectParams.put(address[ii],dataString[ii]);
	}
	
	//plot level information
	else if ( address[ii] !=null && address[ii].startsWith("plot") && 
			address[ii].indexOf("Observation")<=0) {
			//System.out.println(ii+" plot "+address[ii]);
			plotSiteParams.put(address[ii],dataString[ii]);
	}
	
	//plot observation information
	else if ( address[ii] !=null && address[ii].indexOf("otObservation")>0 ) {
				
	
		//put the strataName vector into the hash
		if (address[ii].trim().equals("plotObservation.strata.stratumName")) 
		{
				stratumNameElements.addElement(dataString[ii]);
				plotObservationParams.put("plotObservation.strata.stratumName",
				stratumNameElements);
		}
	
		//put the strataHeight vector into the hash
		else if (address[ii].trim().equals("plotObservation.strata.stratumHeight")) 
		{
			stratumHeightElements.addElement(dataString[ii]);
			plotObservationParams.put("plotObservation.strata.stratumHeight",
			stratumHeightElements);
		}
		
		//put the strataCover vector into the hash
		else if (address[ii].trim().equals("plotObservation.strata.stratumCover")) {
			stratumCoverElements.addElement(dataString[ii]);
			plotObservationParams.put("plotObservation.strata.stratumCover",
				stratumCoverElements);
		}
	
	
		else {plotObservationParams.put(address[ii],dataString[ii]);}
	
	
	}
	
	
	else if ( address[ii] !=null && address[ii].trim().startsWith("taxonObservation") ) {
//		System.out.println(ii+" taxonObservation "+address[ii]);
		
		//put the authorNameId vector into the hash
		if (address[ii].trim().equals("taxonObservation.authNameId")) {
			taxonNameElements.addElement(dataString[ii]);
			plotSpeciesParams.put("taxonObservation.authNameId",taxonNameElements);
		}
		
		//put the strataType vector into the hash
		else if (address[ii].trim().equals("taxonObservation.strataComposition.strataType")) {
			strataTypeElements.addElement(dataString[ii]);
			plotSpeciesParams.put("taxonObservation.strataComposition.strataType",strataTypeElements);
		}
		
		//put the percent coverages vector into a hash
		else if (address[ii].trim().equals("taxonObservation.strataComposition.percentCover")) {
			coverAmountElements.addElement(dataString[ii]);
			plotSpeciesParams.put("taxonObservation.strataComposition.percentCover",coverAmountElements);
		}
		
	}
	
	
	
	else if ( address[ii] !=null && address[ii].startsWith("communityAssignment") ) {
		//System.out.println(ii+" "+address[ii]);
		plotCommunityParams.put(address[ii],dataString[ii]);
	}
	
	else {System.out.println("PlotDBWriter.insertPlot: unrecognized db address"
		+address[ii]);}
	
}

//validate that the data is extensive and cohesive enough to load to the
// database
	if (dataType.equals("entirePlot")) 
	{
		System.out.println("PlotDBWriter.insertPlot: validating entire plot");
		validatePlotData("entirePlot");
	
		//if the data is valid then upload to the database
		if (dataValid) 
		{
			System.out.println(" ** valid plot"); 
			insertToDB("entirePlot");
		}
	}

else {System.out.println("PlotDBWriter.insertPlot: unrecognized dataType");}

} //end method




/**
 * Method to validate that data passed to this class is extensive enough to load 
 * to the database
 */

private void validatePlotData (String dataType) {

//determine the type of data that the user wants to load to the database
if (dataType.equals("entirePlot")) {
//make checks to determine if all the required attributes are there



//get the required project fields
projectName=(String)plotProjectParams.get("project.projectName");
projectDescription=(String)plotProjectParams.get("project.projectDescription");

//get the plot site related fields
authorPlotCode=(String)plotSiteParams.get("plot.authorPlotCode");
parentPlot=(String)plotSiteParams.get("plot.parentPlot");
samplingMethod=(String)plotSiteParams.get("plot.samplingMethod");
coverScale=(String)plotSiteParams.get("plot.coverScale");
plotShape=(String)plotSiteParams.get("plot.plotShape");
plotSize=(String)plotSiteParams.get("plot.plotSize");
plotSizeAcc=(String)plotSiteParams.get("plot.plotSizeAcc");
altValue=(String)plotSiteParams.get("plot.elevationValue");
surfGeo=(String)plotSiteParams.get("plot.surfGeo");
state=(String)plotSiteParams.get("plot.state");
hydrologicRegime=(String)plotSiteParams.get("plot.hydrologicRegime");
xCoord=(String)plotSiteParams.get("plot.xCoord");
yCoord=(String)plotSiteParams.get("plot.yCoord");
coordType=(String)plotSiteParams.get("plot.coordType");
topoPosition=(String)plotSiteParams.get("plot.topoPosition");
slopeGradient=(String)plotSiteParams.get("plot.slopeGradient");
slopeAspect=(String)plotSiteParams.get("plot.slopeAspect");
plotShape=(String)plotSiteParams.get("plot.plotShape");



//get the observation related fields
plotObservationCode=(String)plotObservationParams.get("plotObservation.authorObsCode");
soilDepth=(String)plotObservationParams.get("plotObservation.soilDepth");
soilType=(String)plotObservationParams.get("plotObservation.soilType");
percentRockGravel=(String)plotObservationParams.get("plotObservation.percentRockGravel");

//get the strata information into a vector structure
stratumNameElements=(Vector)plotObservationParams.get("plotObservation.strata.stratumName");
stratumHeightElements=(Vector)plotObservationParams.get("plotObservation.strata.stratumHeight");
stratumCoverElements=(Vector)plotObservationParams.get("plotObservation.strata.stratumCover");



//get the community related fields
currentCommunity =(String)plotCommunityParams.get("communityAssignment.classAssociation");
CEGLCode=(String)plotCommunityParams.get("communityAssignment.CEGLCode");

//get the species/taxon related fields
taxonNameElements=(Vector)plotSpeciesParams.get("taxonObservation.authNameId");
strataTypeElements=(Vector)plotSpeciesParams.get("taxonObservation.strataComposition.strataType");
coverAmountElements=(Vector)plotSpeciesParams.get("taxonObservation.strataComposition.percentCover");
//taxonNameElements.addElement(elementValue);
//System.out.println("XXXX"+taxonNameElements.toString());

//for now just assume everything is ok -- and return true
dataValid=true;
}


}


/**
 * Method that does the actual upload of the data to the database.
 */

private void insertToDB (String dataType) {

//start and manage the database connections
Connection conn=null; 
	
//get the database parameters from the database.parameters file
utility g2 =new utility(); 
g2.getDatabaseParameters("database", "insert");

DbConnectionBroker myBroker;
try {
//initialize the connection broker
myBroker = new DbConnectionBroker(g2.driverClass,g2.connectionString, 
	g2.login, g2.passwd, g2.minConnections, g2.maxConnections, g2.logFile,1.0);

//Get a DB connection from the Broker
int thisConnection;
conn= myBroker.getConnection();
int connectionUses=0;

//detrimine the datatype to load
if (dataType.equals("entirePlot")) {
//load the project data
putProject(conn);

//load the site data
putPlot(conn);

//load the observation data
putPlotObservation(conn);

//cycle thru the strata that are recognized in this observation
//although at times there may be no strata in the xml file

/*
System.out.println("strata "+stratumNameElements.isEmpty());
System.out.println("Number of strata types included in this file: "
	+stratumNameElements.size());
for (int i=0; i<stratumNameElements.size(); i++) 
{
	stratumType=(String)stratumNameElements.elementAt(i);
	stratumHeight=(String)stratumHeightElements.elementAt(i);
	stratumCover=(String)stratumCoverElements.elementAt(i);
	//load these strata to the database
	putStrata(conn);
}
*/

//get a new connection
try {
conn.close(); //close it - it is no good anymore
myBroker.freeConnection(conn); 
conn=myBroker.getConnection();
connectionUses=0;
} catch (Exception e) {System.out.println("failed calling "
+" dbConnect.makeConnection call" + e.getMessage());}



//load the species data to the database -- notice that the stratum names here
// are different than above where it is associated with a height and 'total'
// cover
for (int i=0; i<taxonNameElements.size(); i++) {

	//populate the relevant attributes
	authorNameId=(String)taxonNameElements.elementAt(i);
	stratumType=(String)strataTypeElements.elementAt(i);
	percentCover=(String)coverAmountElements.elementAt(i);
	
///	System.out.println("authorNameId: "+authorNameId);
///	System.out.println("stratumType: "+stratumType);
///	System.out.println("percentCover: "+percentCover);
	
	
	//insert to the taxonObservation table
	putTaxonObservation(conn);

	//insert to the stratum composition table
	putStrataComposition(conn);

//	System.out.println("connecteion use: "+connectionUses);
	connectionUses++;
	if (connectionUses==98) {
	//take care of connections here -- temporary fix
	try {
		conn.close(); //close it - it is no good anymore
		myBroker.freeConnection(conn); 
		conn=myBroker.getConnection();
		connectionUses=0;
	} catch (Exception e) {System.out.println("failed calling "
		+" dbConnect.makeConnection call" 
		+ e.getMessage());}
	}


}


}

myBroker.freeConnection(conn);
myBroker.destroy();	
}
catch (IOException e5)  {System.out.println("connection pooling failed: "
	+e5.getMessage());
	System.exit(0);}

}




/**
 *  Method that takes as input the attributes in the project table and insert/
 *  update the table after perfroming a check to determine if there is redundancy
 *  in the table
 */

private void putProject (Connection conn) {
//int projectId=0;
try {
		
//check to see if the project already exists  by making a query
//and sending it to the issue statement class

	String action="select";
	String statement="select PROJECT_ID from PROJECT where PROJECTNAME like '%"+
		projectName+"%'";
	String returnFields[]=new String[1];	
	returnFields[0]="PROJECT_ID";
	int returnFieldLength=1;


	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength, conn);
	
	
	//if redundancies	
	if (j.outReturnFieldsNum>1) {
		System.out.println("WARNING: corrpted database multiple projects with identical name");
		System.out.println("  Number of redundancies: "+j.outReturnFieldsNum);
		
		//return the first project id that matches to caller
		outProjectId=j.outReturnFields[0].replace('|', ' ').trim();;
		
		//make this value the current project id
		projectId=outProjectId;
		
	}
		
	//if single match found
	if (j.outReturnFieldsNum==1) {
		
		outProjectId=j.outReturnFields[0].replace('|', ' ').trim();  //return to caller
		System.out.println("Matching project found, ID number: "+outProjectId);
		
		//make this value the current project id
		projectId = outProjectId;
		
	
	}
	
	//if zero matches found then create a new project_id number

	if (j.outReturnFieldsNum==0) {
		System.out.println("No Matching projects found - creating a new one \n"+
		"Project ID number: ");
		
		//grab the next project ID number
		PlotDBWriter g =new PlotDBWriter();  
		g.getNextId("test", "project", conn);
		projectId=""+g.outNextId;  //assign the projectID
		outProjectId=""+projectId;
		outProjectId=outProjectId.replace('|', ' ').trim();
		
		//make this value the current project id
		projectId = outProjectId;
		
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
	catch (Exception e) {System.out.println("failed in PlotDBWriter.putProject: " + 
	e.getMessage());}


}  //end method
public String outProjectId=null;





/**
 *  Method that takes as input the attributes in the plot table and insert/
 *  update the table after perfroming a check to determine if there is redundancy
 *  in the table
 */


private void putPlot ( Connection conn) {

try {

//grab the next value in the plot table
PlotDBWriter g =new PlotDBWriter();  
g.getNextId("test", "plot", conn);

//make this value the current plot id
plotId = ""+g.outNextId;


//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO PLOT";
String attributeString="PLOT_ID, PROJECT_ID, AUTHORPLOTCODE, PARENTPLOT, PLOTTYPE, "+
"SAMPLINGMETHOD, COVERSCALE, PLOTORIGINLAT, PLOTORIGINLONG, PLOTSHAPE, "
+"PLOTSIZE, PLOTSIZEACC, ALTVALUE, SURFGEO, STATE, CURRENTCOMMUNITY, "
+"HYDROLOGICREGIME, XCOORD, YCOORD, COORDTYPE, SLOPEPOSITION, SLOPEGRADIENT, "
+"SLOPEASPECT";

int inputValueNum=23;
String inputValue[]=new String[23];	
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
	System.out.println(" elevation: "+altValue);
inputValue[13]=surfGeo;
	System.out.println(" surfGeo: "+surfGeo);	
inputValue[14]=state;
	System.out.println(" state: "+state);
inputValue[15]=currentCommunity;
	System.out.println(" currentCommunity: "+currentCommunity);
//
inputValue[16]=hydrologicRegime;
	System.out.println(" hydologicRegime: "+currentCommunity);
inputValue[17]=xCoord;
	System.out.println(" xCoord: "+ xCoord);
inputValue[18]=yCoord;
	System.out.println(" xCoord: "+ yCoord);
inputValue[19]=coordType;
	System.out.println(" coordType: "+ coordType );
inputValue[20]=topoPosition;
	System.out.println(" topoPosition: "+ topoPosition);
inputValue[21]=slopeGradient;
	System.out.println(" slopeGradient: "+ slopeGradient);
inputValue[22]=slopeAspect;
	System.out.println(" slopeAspect: "+ slopeAspect );

//get the valueString from the method
issueStatement k = new issueStatement();
k.getValueString(inputValueNum);	
String valueString = k.outValueString;
//issue the above statement
issueStatement l = new issueStatement();
l.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue, conn);	

}
catch (Exception e) {System.out.println("failed in PlotDBWriter.putPlot " + 
	e.getMessage());}
	
} //end method
public String outPlotId=null;

	

/**
 *  Method that takes as input the attributes in the plot observation table and 
 * insert/update the table after perfroming a check to determine if there is 
 * redundancy in the table
 */

private void putPlotObservation (Connection conn) {

try {

//grab the next value in the plotObservation table
PlotDBWriter g =new PlotDBWriter(); 
g.getNextId("test", "plotObservation", conn);
//make this value the current plot id
plotObservationId=""+g.outNextId;  
//outPlotObservationId=""+plotObservationId; //pass to public
		
//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO PLOTOBSERVATION";
String attributeString="OBS_ID, PARENTPLOT, AUTHOROBSCODE";
int inputValueNum=3;
String inputValue[]=new String[3];	
inputValue[0]=plotObservationId;
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
catch (Exception e) {System.out.println("failed in PlotDBWriter.putPlotObservation " + 
	e.getMessage());}	
} //end method





/**
 *  Method that takes as input the attributes in the strata related tables and 
 * insert/update the table with the input data
 *
 */
private void putStrata (Connection conn) 
{
try {

//grab the next value in the strata table
PlotDBWriter g =new PlotDBWriter();  
g.getNextId("test", "strata", conn);
				
strataId=""+g.outNextId;  //grab the returned value
		
//pass the required arguements to the isssue SQl class
String insertString="INSERT INTO STRATA";
String attributeString="STRATA_ID, OBS_ID, STRATUMTYPE, STRATUMCOVER, "
	+"STRATUMHEIGHT";
int inputValueNum=5;
String inputValue[]=new String[5];	
inputValue[0]=strataId;
inputValue[1]=plotObservationId;
inputValue[2]=stratumType.toUpperCase();
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
catch (Exception e) {System.out.println("failed in PlotDBWriter.putStrata " + 
	e.getMessage());}
}  //end method



/**
 *  Method that takes as input the attributes from the 
 *  taxonObservation tables and inserts/updates the 
 * 	table with the input data
 *
 */

private void putTaxonObservation (Connection conn) 
 {

 try 
 {
	 
	 
	 
  //grab the next value in the strata table
  PlotDBWriter g =new PlotDBWriter();  
  g.getNextId("test", "taxonObservation", conn);
				
  taxonObservationId=""+g.outNextId;  //grab the returned value
	
  //pass the required arguements to the isssue SQl class
  String insertString="INSERT INTO TAXONOBSERVATION";
  String attributeString="TAXONOBSERVATION_ID, OBS_ID, AUTHORNAMEID, "
  	+"ORIGINALAUTHORITY";
  int inputValueNum=4;
  String inputValue[]=new String[4];	
  inputValue[0]=taxonObservationId;
  inputValue[1]=plotObservationId;
  inputValue[2]=authorNameId;
  inputValue[3]=originalAuthority;

  //get the valueString from the method
  issueStatement k = new issueStatement();
  k.getValueString(inputValueNum);	
  String valueString = k.outValueString;
	
	System.out.println("LOADING TAXONOBSERVATION: ");
	System.out.println("TAXONOBSERVATION_ID: "+taxonObservationId);
	System.out.println("PLOTOBSERVATIONID: "+plotObservationId);
	System.out.println("AUTHORNAMEID: "+authorNameId);
	System.out.println("ORIGIONAL AUTHORITY: "+originalAuthority);	
	System.out.println("\n");
	
	
  //issue the above statement
  issueStatement l = new issueStatement();
  l.issueInsert(insertString, attributeString, valueString, inputValueNum, 
	inputValue, conn);	
 }
	catch (Exception e) 
	{
		System.out.println("failed in PlotDBWriter.putTaxonObservation "
	+e.getMessage());
	}
}  //end method



/**
 *  Method that takes as input the attributes from the strataComposition tables and 
 * inserts/updates the table with the input data
 */
private void putStrataComposition (Connection conn) 
{
	try 
	{

	//System.out.println("This Taxon in strataComposition: "+strataCompositionId);
	//get the strataId value associated with this specific taxon
	getStrataComposition(plotObservationId, stratumType, conn);	
	
	String insertString="INSERT INTO STRATACOMPOSITION";
	String attributeString="TAXONOBSERVATION_ID, STRATA_ID, "
	+"CHEATSTRATUMTYPE, PERCENTCOVER";
	int inputValueNum=4;
	String inputValue[]=new String[4];	
	//inputValue[0]=strataCompositionId;
	inputValue[0]=taxonObservationId;
	inputValue[1]=strataId;
	inputValue[2]=stratumType;
	inputValue[3]=percentCover;

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);	
	String valueString = k.outValueString;

	issueStatement l = new issueStatement();
	l.issueInsert(insertString, attributeString, valueString, inputValueNum, 
	inputValue, conn);	

	}
	catch (Exception e) 
	{
		System.out.println("failed in PlotDBWriter.putStrataComposition " + 
		e.getMessage());
	}
}  //end method



	
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
						
		}//end if
		count++;
		dbAddressNum=count;
			
	}//end for

} //end try
catch ( Exception e ){System.out.println("failed at: PlotDBWriter.setAttributeAddress  "+e.getMessage());}
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
} catch (Exception e)  {System.out.println("failed at PlotDBWriter.getNextId "
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
catch (Exception e) {System.out.println("failed in PlotDBWriter.getNextId trying "
	+"to query the next id value in a table data: " + e.getMessage());}



} //end method



/**
 *  Method that returns a strataId value that correspnds to a use of a taxon
 * 	within a specific strata for a given plotObservation 
 *
 * @param obseravtionId - the taxonObservation value for a given recognition of 
 *	a taxon 
 * @param strataType - the type of strata in which the taxon is found
 *
 */
	private void getStrataComposition (String plotObservationId, String stratumType, 
		Connection pconn) 
	{
		try 
		{	
	
		//because the TNC data sometimes uses upercase and other times doesn't
		//make the query using uppercases

		//System.out.println("plotObservationId: "+plotObservationId+" stType: "+stratumType);

		String action="select";
		String statement="SELECT STRATA_ID from STRATA where OBS_ID = "
		+plotObservationId.toUpperCase()+" and stratumType like '%"
		+stratumType.toUpperCase()+"%'";

		int returnFieldLength=1;
		String returnFields[]=new String[1];	
		returnFields[0]="STRATA_ID";	


		issueStatement j = new issueStatement();
		j.issueSelect(statement, action, returnFields, returnFieldLength, pconn);	

		//return the strataId
		strataId=j.outReturnFields[0].replace('|',' ').trim();
		//System.out.println("strataID "+strataId);

	}	 //end try
	catch (Exception e) 
	{
		System.out.println("failed in: "
		+"PlotDBWriter.getStrataComposition " 
		+ e.getMessage());
	}	
}//end method



}

