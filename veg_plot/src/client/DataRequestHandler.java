//package client;

import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;



/**
 * This application was developed directly from the DataRequestServlet to handle
 * requests from in this case the DataRequestClient (used by the Gui Interface
 * classes) and in the case of the symetrical servlet by a browser.  This
 * application will take a request from DataRequestClient (format it as an xml
 * file) and pass the query to the database access module to query the local
 * embedded database.
 * 
 * Application to compose XML documents containg query information and then send the 
 * information to the dataAccess module, where that XML document will be
 * transformed and analyzed - a process resulting in a query being issued to the
 * plots database
 *
 * <p>Valid parameters are:<br><br>
 * 
 * taxonName - name of a taxon occuring in a plot <br>
 * communityName - name of a vegetaion community <br>
 * minElevation - minimum elavation in an elevation range <br>
 * maxElevation - maximum elevation in an elevation range <br>
 * state - state in which a plot exists <br>
 * surfGeo - surface geology type <br>
 * multipleObs - multiple observations <br>
 * compoundQuery - descriptor of the query type, which may include simple
 * 	- where only one criteria is used or compound where multiple criteria
 * 	may be used the flag for siple is '0' and the flag for compound is '1'<br>
 * plotId - database plot identification number <br> 
 * resultType - type of results expected by the user may include summary, where
 * 	a summary of the plot is returned and full where all plot attributes 
 *	are returned 
 * requestDataType 
 * resultFormatType - mak be either xml or html depending on the client tools<br>
 * 
 * @version March 22, 2001
 * @author John Harris
 * 
 */

public class DataRequestHandler {

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

//the string buffer below is the results that have been requested by the
// DataRequestClient in the form of a string buffer
public StringBuffer results = new StringBuffer();


public String queryOutput[] = new String[10000];  //the output from query
int queryOutputNum; //the number of output rows from the query


private String taxonName = null;
private String communityName = null;
private String taxonOperation = null;
private String commOperation = null;
private String minElevation = null;
private String maxElevation = null;
private String state = null;
private String surfGeo = null;
private String multipleObs = null;
private String compoundQuery = null;
private String plotId = null;
private String resultType = null;
private String clientLog = null;
private String remoteHost = null;
private String requestDataType = null;
//community related attributes
//commmunity name already defined above
private String communityLevel = null;

/** Handle "POST" method requests from HTTP clients */
///public void doPost(HttpServletRequest request,
///                      HttpServletResponse response)
///        throws IOException, ServletException 	
///{
///        doGet(request, response);
///}


/**
 *  This method is modeled after the 'doGet' method in the servlet but instead
 *  of accepting as input a servletRequest accept a hastable that contains the 
 *  name value pair and instead of reponding using a response use a stringBuffer
 *  containing the html or xml or whatever
 *
 * @param request -- hash table to store the request information
 * @param response -- the databse response from the database access module 
 * 
 */
public void doGet(Hashtable request, StringBuffer response)
//      throws IOException   
{

////Enumeration enum =request.getParameterNames();

Hashtable params = new Hashtable();
params = request; //transfer the hash table

///response.setContentType("text/html");
///PrintWriter out = response.getWriter();


/**
 * The first try block is for reading the parameters into a hash table and then 
 * assigning the parameters to their respective variables
 */
try {

// while (enum.hasMoreElements()) {
//	String name = (String) enum.nextElement();
//	String values[] = request.getParameterValues(name);
//	if (values != null) {
		
//		for (int i=0; i<values.length; i++) {
//			//out.println(name +" ("+ i + "): " +values[i]+"; <br>");
//			params.put(name,values[i]);
		
//		}
//	}
// }

//is this request being made using multiple query elements
 if ( (String)params.get("compoundQuery") != null )
 {
 	compoundQuery = (String)params.get("compoundQuery");
 	compoundQuery.trim();
 }
 
 //how much data is being requested -- summary set, full data set etc
 if ( (String)params.get("resultType") != null )
 {
 	resultType = (String)params.get("resultType");
 }
 
 //what type of data is being requested -- ie. plot, plant, community
 if ( (String)params.get("requestDataType") != null )
 {
 	requestDataType = (String)params.get("requestDataType");
 }
 
//get the variables privately held in the properties file
// clientLog=(rb.getString("requestparams.clientLog"));

//get the client-related variables
/// remoteHost=request.getRemoteHost();

}//end try
catch( Exception e ) {System.out.println("** failed in: DataRequestServlet.main "
	+" first try - reading parameters "+e.getMessage());}




// The second try block authenticates the user, logs the use of the servlet and
// develops/issues the query(s)
try {

//return to the browser a summary of the request being made of the servlet this
//method also adds some tags that are required many browsers
///returnQueryElemenySummary (out, params, response);

//authenticate the client user - skip for now

//log the use of the servlet by the client
/// updateClentLog (clientLog, remoteHost);


//figure out how to handle the query -- either handle as a sinple query or as a 
//compound query
 if (compoundQuery != null && compoundQuery.equals("1")) //a compound query
 {
	handleCompoundQuery (params);
//	System.out.println("*compond query: "+compoundQuery);
 }

 //else is not a coumpund query
 else 
 {
	System.out.println("params used for a simple query: "+params.toString());
	handleSimpleQuery (params);
 }	
 
 //provide the client with the results as a string buffer
 readResults("summary.xml");
 
 
 

}
catch( Exception e ) {System.out.println("** failed in: DataRequestServlet.main "
	+" first try - anaylyzing the input parameters "+e.getMessage());}
}  //end method















/**
 * Handles simple queries which are those queries where there is only one 
 * queryElemnt such as taxon or community name this is in contrast to a compound
 * query which has multiple query elements and is handled by the
 * 'handleCompoundQuery' method
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query
 */
 
private void handleSimpleQuery (Hashtable params) {
		
/** Get all possible parameters from the hash */	
 taxonName = (String)params.get("taxon");
 communityName = (String)params.get("community");
 taxonOperation = (String)params.get("taxonOperation");
 commOperation = (String)params.get("commOperation");
 minElevation = (String)params.get("minElevation");
 maxElevation = (String)params.get("maxElevation");
 state = (String)params.get("state");
 surfGeo = (String)params.get("surfGeo");
 multipleObs = (String)params.get("multipleObs");
 compoundQuery = (String)params.get("compoundQuery");
 plotId = (String)params.get("plotId");
 resultType = (String)params.get("resultType");
 requestDataType = (String)params.get("requestDataType");
 
//attempt to recognize the request as a query for communities 
if (requestDataType.trim().equals("community")) {  
	
///	out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
///	+ "community information ");
	composeCommunityQuery(params);
	issueQuery("simpleCommunityQuery");
	
	System.out.println("Number of communities returned: "+queryOutputNum+"<br><br>");

}


/** Cheat here - to recognise the single plot query to return entire plot */
 if (plotId != null && resultType.equals("full") ) {
	String outFile="/jakarta-tomcat/webapps/examples/WEB-INF/lib/atomicResult";
///	out.println("<br>DataRequestServlet.handleSimpleQuery - returning a full data set "
///		+"for plot: "+plotId+" <br>");
	composeSinglePlotQuery(plotId, resultType, outFile);
	issueQuery("simpleQuery");
	
 }

 
 // this is where the query element checking is done for the vegetation plots
 // the way that this is structured currently the user is not forced to choose a
 //single query element

//look for a taxonName
 if ( taxonName != null  && taxonName.length()>0 ) {  
	System.out.println("DataRequestHandler.handleSimpleQuery - returning a summary data set "
	+"containing plots with taxonName: "+taxonName+" <br>");
	composeQuery("taxonName", taxonName);
 	issueQuery("simpleQuery");
	
	System.out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }
 
 //look for elevation
 if ( minElevation != null  && minElevation.length()>0 ) {  
	System.out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
	+"containing plots with a minElevation of: "+minElevation+" <br>");
	composeQuery("elevationMin", minElevation, "elevationMax", maxElevation);
 	issueQuery("simpleQuery");
	
	System.out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }
 
 //look for state
 if ( state != null  && state.length()>0 ) {  
	 System.out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
	+"containing plots with a state equal to: "+state+" <br>");
	composeQuery("state", state);
 	issueQuery("simpleQuery");
	
	System.out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }

//look for the community name
if ( communityName != null  && communityName.length()>0 ) {  
	System.out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
		+"containing plots with a communityName equal to: "+communityName+" <br>");
	composeQuery("communityName", communityName);
 	issueQuery("simpleQuery");
	System.out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }


//look for the surface geology option
if ( surfGeo != null  && surfGeo.length()>0 ) {  
	 System.out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
	+"containing plots with a surface geology like: "+surfGeo+" <br>");
	composeQuery("surfGeo", surfGeo);
 	issueQuery("simpleQuery");
	System.out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }






//if there are results returned to the servlet from the database in the form 
//of a file returned then grab the summary viewer then let the user know

if (queryOutputNum>=1) {
/// 	servletUtility l =new servletUtility();  
///	l.getViewOption(requestDataType);
/// 	out.println(l.outString);
}




else { 
///	out.println("<br> <b> Please try another query </b> <br>"); 
///	out.println("<a href = \"/examples/servlet/pageDirector?pageType=DataRequestServlet\">"
///		+"return to query page</a><b>&#183;</b>"); //put in rb

}

}




/**
 * Handles compound queries where there are more than one query elements
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query 
 */
private void handleCompoundQuery (Hashtable params) {
//Get all possible parameters from the hash
 taxonName = (String)params.get("taxon");
 communityName = (String)params.get("community");
 taxonOperation = (String)params.get("taxonOperation");
 commOperation = (String)params.get("commOperation");
 minElevation = (String)params.get("minElevation");
 maxElevation = (String)params.get("maxElevation");
 state = (String)params.get("state");
 surfGeo = (String)params.get("surfGeo");
 multipleObs = (String)params.get("multipleObs");
 compoundQuery = (String)params.get("compoundQuery");
 plotId = (String)params.get("plotId");
 resultType = (String)params.get("resultType");
 
/// out.println("<br>DataRequestServlet.handleCompoundQuery - returning a summary data set ");
		
 composeQuery(taxonName, communityName, taxonOperation, commOperation, minElevation,
	maxElevation, state, surfGeo, multipleObs, compoundQuery);
 issueQuery("compoundQuery");
 
/// out.println("Number of results returned: "+queryOutputNum);
 
//allow the user to access the summary results
/// servletUtility l =new servletUtility();  
/// l.getViewOption("vegPlot");  //change the attribute being passed to requestDataType
/// out.println(l.outString);

}




/**
 * This method takes the hashtable that stores the input parameters passed to
 * the servlet -- which in this case refer to the community that the user wants
 * data about and builds the query xml file that is passed then to the database
 * access module
 *
 * @param params -- all the params passed to the servlet that contains
 * attributes used for, in this care, query the community database
 * 
 */
private void composeCommunityQuery (Hashtable params) {
 
 try {
 PrintStream queryOutFile = new PrintStream(
 	new FileOutputStream("commQuery.xml", false)); 

//grab the relevent query attributes out of the hash
communityName = (String)params.get("communityName");
communityLevel = (String)params.get("communityLevel");

System.out.println("printing from DataRequestServlet.composeCommunityQuery: "+
	"communityName: "+communityName);

 //print the query instructions in the xml document
 queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
 "<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
 "<dbQuery> \n"+
 "<query> \n"+
 "<queryElement>communityName</queryElement> \n"+
 "<elementString>"+communityName+"</elementString> \n"+
 "</query> \n"+
 "<query> \n"+
 "<queryElement>communityLevel</queryElement> \n"+
 "<elementString>"+communityLevel+"</elementString> \n"+
 "</query> \n"+
 "<requestDataType>"+requestDataType+"</requestDataType> \n"+
 "<resultType>"+resultType+"</resultType> \n"+
 "<outFile>summary.xml</outFile> \n"+
 "</dbQuery>"
 );

 
 }
catch (Exception e) {System.out.println("** failed in DataRequestServlet.composeCommunityQuery " 
	+"trying to do client book keeping: " + e.getMessage());}
	
}







/**
 * logs the use of this servlet, the date and the remote host of the client in 
 * log file 
 *
 * @param clientLog - the name of the client log file
 * @param remoteHost - the name of the remote host using this client
 * 
 */
///private void updateClentLog (String clientLog, String remoteHost) {
 
/// try {
/// PrintStream clientLogger = new PrintStream(new FileOutputStream(clientLog, true));
/// SimpleDateFormat formatter = new SimpleDateFormat ("yy-MM-dd HH:mm:ss");
/// java.util.Date localtime = new java.util.Date();
/// String dateString = formatter.format(localtime);
/// //Date date = new Date();
/// clientLogger.println(remoteHost+", "+dateString);
///}
///catch (Exception e) {System.out.println("** failed in DataRequestServlet.updateClientLog " 
///	+"trying to do client book keeping: " + e.getMessage());}
	
///}



/**
 * Write to the browser the query elements and their respective strings as well
 * as the some other information like the last date the user accessed the servlet 
 * the authentication status of the user
 * 
 * @param out - the output stream to the client
 * @param params - the Hashtable of parameters that should be included in the
 * 	response
 * @param response - the response object linked to the client 
 */
///private void returnQueryElemenySummary (PrintWriter out, Hashtable params, 
///                 HttpServletResponse response) {
///try {
	
//this utility class is to store reused html code
/// servletUtility k =new servletUtility();  
/// k.htmlStore();
/// out.println(k.outString); 
 
/// Enumeration paramlist = params.keys();
 
 //set up a table in which to print the query elements to the browser
/// out.println("<table summary=queryElements border=0 width=60%>"
/// 	+"<tr bgcolor=DFE5FA> <td>Query Element</td> <td>Element Value</td>");

/// while (paramlist.hasMoreElements()) 
///      {
///        String queryElement = (String)paramlist.nextElement();
///        String elementValue  = (String)params.get(queryElement);
		
///		//print the query elements to the browser
///		out.println("<tr bgcolor=dddddd> <td><b> " + queryElement +" </b></td> "
///		+" <td><i> "+ elementValue +" </i></td> </tr>");
      
///	  }

//close the table
///out.println("</table>");
//next line is for debuging
///out.println("---end of DataRequestServlet.returnQueryElemenySummary----<br></br>");

///}
///catch (Exception e) {System.out.println("failed in "
///	+"DataRequestServlet.returnQueryElementSummary" + e.getMessage());}
///} //end method





/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml
 *
 * @param plotId - internal database plot identification number
 * @param resultType - the type of data expected from the query full/summary
 */
private void composeSinglePlotQuery (String plotId, String resultType, String outFile) {
try {

PrintStream queryOutFile = new PrintStream(new FileOutputStream("query.xml", false)); 

//print the query instructions in the xml document
queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
"<dbQuery> \n"+
"<query> \n"+
"<queryElement>plotId</queryElement> \n"+
"<elementString>"+plotId+"</elementString> \n"+
"</query> \n"+
"<resultType>"+resultType+"</resultType> \n"+
"<outFile>"+outFile+"</outFile> \n"+
"</dbQuery>"
);

}
catch (Exception e) {System.out.println("failed in DataRequestServlet.composeSinglePlotQuery" + 
	e.getMessage());}
}


/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml
 *
 * @param queryElement - name of the element being used to query the database, 
 *	such as taxonName, plotId etc.
 * @param elementString - value of the queryElement
 */
private void composeQuery (String queryElement, String elementString) {
try {
//set up the output query file called query.xml	using append mode to build  
PrintStream outFile  = new PrintStream(new FileOutputStream("query.xml", false)); 

//print the query instructions in the xml document
outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>"+queryElement+"</queryElement> \n"+
	"<elementString>"+elementString+"</elementString> \n"+
	"</query> \n"+
	"<resultType>summary</resultType> \n"+
	"<outFile>summary.xml</outFile> \n"+
	"</dbQuery>"
);
	
}
catch (Exception e) {System.out.println("failed in DataRequestServlet.composeQuery" + 
	e.getMessage());}
} //end method




/**
 * Method to compose and print to a file an xml document that can be passed to
 * the dbAccess class to perform the query and print the results to a file
 * that is currently hard-wired as summary.xml.  Thinking of queries where
 * there a minimum and maximum value like elevation or tree stem diamtere
 *
 * @param minElement
 * @param minValue - value of the queryElement
 * @param maxElement
 * @param maxValue - value of the queryElement
 */

private void composeQuery (String minElement, String minValue, String maxElement, String maxValue) {
try {
//set up the output query file called query.xml	using append mode to build  
PrintStream outFile  = new PrintStream(new FileOutputStream("query.xml", false)); 


//print the query instructions in the xml document
outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>"+minElement+"</queryElement> \n"+
	"<elementString>"+minValue+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>"+maxElement+"</queryElement> \n"+
	"<elementString>"+maxValue+"</elementString> \n"+
	"</query> \n"+
	"<resultType>summary</resultType> \n"+
	"<outFile>/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml</outFile> \n"+
	"</dbQuery>"
);

	
}
catch (Exception e) {System.out.println("failed in DataRequestServlet.composeQuery" + 
	e.getMessage());}
} //end method



/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml  - an overloaded version of the 
 *  above.
 *
 * @param taxonName - taxon name
 * @param communityName - community name
 * @param taxonOperation - taxon operation
 * @param commOperation - community operation
 * @param minElevation - minimum elevation
 * @param maxElevation - maximum elevation
 * @param state - state
 * @param surfGeo - surface geology type
 * @param multipleObs - multiple Observations
 * @param compoundQuery - compound Query
 */

private void composeQuery (String taxonName, String communityName, String taxonOperation, 
String commOperation, String minElevation, String maxElevation, String state, 
String surfGeo, String multipleObs, String compoundQuery) {
try {
//set up the output query file called query.xml	using append mode to build  
 PrintStream outFile  = new PrintStream(new FileOutputStream("query.xml", false)); 


//print the query instructions in the xml document
 outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>taxonName</queryElement> \n"+
	"<elementString>"+taxonName+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>state</queryElement> \n"+
	"<elementString>"+state+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>elevationMin</queryElement> \n"+
	"<elementString>"+minElevation+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>elevationMax</queryElement> \n"+
	"<elementString>"+maxElevation+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>surfGeo</queryElement> \n"+
	"<elementString>"+surfGeo+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>multipleObs</queryElement> \n"+
	"<elementString>"+multipleObs+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>community</queryElement> \n"+
	"<elementString>"+communityName+"</elementString> \n"+
	"</query> \n"+
	"<resultType>summary</resultType> \n"+
	"<outFile>/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml</outFile> \n"+
	"</dbQuery>"
);

	
}
catch (Exception e) {System.out.println("failed in DataRequestServlet.composeQuery" + 
	e.getMessage());}
} //end method



/**
 *  Method to use dbAccess to issue the query to the database from the xml file
 *  created in the DataRequestServlet 
 *
 * @param queryType - the type of query to be sent through the dataAccess module
 * including simpleQuery (one attribute) and compoundQuery (multiple attributes) 
 */
private void issueQuery (String queryType) {

if (queryType.equals("simpleQuery")) { 

	//call the plot access module
	dbAccess g =new dbAccess();  
	g.accessDatabase("query.xml", "querySpec.xsl", "query");

}

if (queryType.equals("compoundQuery")) { 

	//call the plot access module
//	dbAccess g =new dbAccess();  
//	g.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", 
//	"/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "compoundQuery");	

	//grab the result set from the dbAccess class
//	queryOutput=g.queryOutput;
//	queryOutputNum=g.queryOutputNum;

}


if (queryType.equals("simpleCommunityQuery")) { 

	//call the plot access module
//	dbAccess g =new dbAccess();  
//	g.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/commQuery.xml", 
//	"/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "simpleCommunityQuery");	

	//grab the result set from the dbAccess class
//	queryOutput=g.queryOutput;
//	queryOutputNum=g.queryOutputNum;

}

}

/**
 *  This method reads into a string buffer the output file that was written by
 * the database access module.  This string buffer is intended to be used by the
 * data request client class this method maybe should go into the utility calss
 *
 * @parm outFile -- the path and filename of the output file that contains the
 * data retrieved by the query handled in this class
 *
 */
private void readResults(String outFile) {

 try {
	//read the results
	BufferedReader in = new BufferedReader(new FileReader(outFile));
	String s;
	while((s = in.readLine()) != null) {
		results.append(s);
	}
 } 
 catch( Exception e ) {System.out.println("** failed in: DataRequestHandler.readResults "
	+" "+e.getMessage());}




}



}
