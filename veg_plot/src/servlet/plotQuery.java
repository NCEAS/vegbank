import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


/**
 * Servlet to compose XML documents containg query information and then send the 
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
 *	are returned <br>
 * 
 * @version 11 Jan 2001
 * @author John Harris
 * 
 */

public class plotQuery extends HttpServlet {

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

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


/** Handle "POST" method requests from HTTP clients */
public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException 	
{
        doGet(request, response);
}


/** Handle "GET" method requests from HTTP clients */ 
public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException  {

Enumeration enum =request.getParameterNames();
Hashtable params = new Hashtable();

response.setContentType("text/html");
PrintWriter out = response.getWriter();


/**
 * The first try block is for reading the parameters into a hash table and then 
 * assigning the parameters to their respective variables
 */
try {

 while (enum.hasMoreElements()) {
	String name = (String) enum.nextElement();
	String values[] = request.getParameterValues(name);
	if (values != null) {
		
		for (int i=0; i<values.length; i++) {
			//out.println(name +" ("+ i + "): " +values[i]+"; <br>");
			params.put(name,values[i]);
		
		}
	}
 }

//get the variables that are needed to start the processes
 if ( (String)params.get("compoundQuery") != null )
 {
 	compoundQuery = (String)params.get("compoundQuery");
 	compoundQuery.trim();
 }
 
 if ( (String)params.get("resultType") != null )
 {
 	resultType = (String)params.get("resultType");
 }
 
//get the variables privately held in the properties file
 clientLog=(rb.getString("requestparams.clientLog"));

//get the client-related variables
 remoteHost=request.getRemoteHost();

}//end try
catch( Exception e ) {System.out.println("** failed in: plotQuery.main "
	+" first try - reading parameters "+e.getMessage());}



/**
 * The second try block authenticates the user, logs the use of the servlet and
 * develops/issues the query(s)
 */
try {

//test out the method below
 returnQueryElemenySummary (out, params, response);

//authenticate the client user - skip for now

//log the use of the servlet by the client
 updateClentLog (clientLog, remoteHost);

//figure out how to handle the query
 if (compoundQuery != null && compoundQuery.equals("1")) //a compound query
 {
	handleCompoundQuery (params, out, response);
//	System.out.println("*compond query: "+compoundQuery);
 }

 else 
 {
	handleSimpleQuery (params, out, response);
 }	

}
catch( Exception e ) {System.out.println("** failed in: plotQuery.main "
	+" first try - anaylyzing the input parameters "+e.getMessage());}

}  //end method




/**
 * Handles simple queries which are those queries where there is only one 
 * queryElemnt such as taxon or community name this is in contrast to a compound
 * query which has multiple query elements and is handled by another metod
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query
 * @param out - the output stream to the client
 * @param response - the response object linked to the client 
 */
private void handleSimpleQuery (Hashtable params, PrintWriter out, 
	HttpServletResponse response) {
		
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


/** Cheat here - to recognise the single plot query to return entire plot */
 if (plotId != null && resultType.equals("full") ) {
	String outFile="/jakarta-tomcat/webapps/examples/WEB-INF/lib/atomicResult";
	out.println("<br>plotQuery.handleSimpleQuery - returning a full data set "
		+"for plot: "+plotId+" <br>");
	composeSinglePlotQuery(plotId, resultType, outFile);
	issueQuery("simpleQuery");
 }

 
 if ( taxonName != null  && taxonName.length()>0 ) {  
	 out.println("<br>plotQuery.handleSimpleQuery - returning a summary data set "
		+"containing plots with taxonName: "+taxonName+" <br>");
	composeQuery("taxonName", taxonName);
 	issueQuery("simpleQuery");
	
	out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }
 
 if ( minElevation != null  && minElevation.length()>0 ) {  
	 out.println("<br>plotQuery.handleSimpleQuery - returning a summary data set "
		+"containing plots with a minElevation of: "+minElevation+" <br>");
	composeQuery("elevationMin", minElevation, "elevationMax", maxElevation);
 	issueQuery("simpleQuery");
	
	out.println("Number of results returned: "+queryOutputNum+"<br><br>");
 }
 
 
 
if (queryOutputNum>=1) {
	//allow the user to access the results
 	servletUtility l =new servletUtility();  
 	l.getViewOption("vegPlot");
 	out.println(l.outString);
}

else { 
	out.println("<br> <b> Please try another query </b> <br>"); 
	out.println("<a href = \"/examples/servlet/pageDirector?pageType=plotQuery\">"
		+"return to query page</a><b>&#183;</b>"); //put in rb

}

//}
}




/**
 * Handles compound queries where there are more than one query elements
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query
 * @param out - the output stream to the client
 * @param response - the response object linked to the client 
 */
private void handleCompoundQuery (Hashtable params, PrintWriter out, 
	HttpServletResponse response) {
/* Get all possible parameters from the hash */	
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
 
 out.println("<br>plotQuery.handleCompoundQuery - returning a summary data set ");
		
 composeQuery(taxonName, communityName, taxonOperation, commOperation, minElevation,
	maxElevation, state, surfGeo, multipleObs, compoundQuery);
 issueQuery("compoundQuery");
 
 out.println("Number of results returned: "+queryOutputNum);
 
//allow the user to access the summary results
 servletUtility l =new servletUtility();  
 l.getViewOption("vegPlot");
 out.println(l.outString);

}






/**
 * logs the use of this servlet, the date and the remote host of the client in 
 * log file 
 *
 * @param clientLog - the name of the client log file
 * @param remoteHost - the name of the remote host using this client
 * 
 */
private void updateClentLog (String clientLog, String remoteHost) {
 
 try {
 PrintStream clientLogger = new PrintStream(new FileOutputStream(clientLog, true));
 SimpleDateFormat formatter = new SimpleDateFormat ("yy-MM-dd HH:mm:ss");
 java.util.Date localtime = new java.util.Date();
 String dateString = formatter.format(localtime);
 //Date date = new Date();
 clientLogger.println(remoteHost+", "+dateString);
}
catch (Exception e) {System.out.println("** failed in plotQuery.updateClientLog " 
	+"trying to do client book keeping: " + e.getMessage());}
	
}



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
private void returnQueryElemenySummary (PrintWriter out, Hashtable params, 
                 HttpServletResponse response) {
try {
	
 servletUtility k =new servletUtility();  
 k.htmlStore();
 out.println(k.outString); 
 
 Enumeration paramlist = params.keys();
 
 //set up a table in which to print the query elements to the browser
 out.println("<table summary=queryElements border=0 width=60%>"
 	+"<tr bgcolor=DFE5FA> <td>Query Element</td> <td>Element Value</td>");

 while (paramlist.hasMoreElements()) 
      {
        String queryElement = (String)paramlist.nextElement();
        String elementValue  = (String)params.get(queryElement);
	out.println("<tr bgcolor=dddddd> <td><b> " + queryElement +" </b></td> "
		+" <td><i> "+ elementValue +" </i></td> </tr>");
      }

//close the table
out.println("</table>");

}
catch (Exception e) {System.out.println("failed in "
	+"plotQuery.returnQueryElementSummary" + e.getMessage());}
} //end method





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

PrintStream queryOutFile = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 

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
catch (Exception e) {System.out.println("failed in plotQuery.composeSinglePlotQuery" + 
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
PrintStream outFile  = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 

//print the query instructions in the xml document
outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>"+queryElement+"</queryElement> \n"+
	"<elementString>"+elementString+"</elementString> \n"+
	"</query> \n"+
	"<resultType>summary</resultType> \n"+
	"<outFile>/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml</outFile> \n"+
	"</dbQuery>"
);
	
}
catch (Exception e) {System.out.println("failed in plotQuery.composeQuery" + 
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
PrintStream outFile  = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 


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
catch (Exception e) {System.out.println("failed in plotQuery.composeQuery" + 
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
 PrintStream outFile  = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 


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
catch (Exception e) {System.out.println("failed in plotQuery.composeQuery" + 
	e.getMessage());}
} //end method



/**
 *  Method to use dbAccess to issue the query to the database from the xml file
 *  created in the plotQuery 
 *
 * @param queryType - the type of query to be sent through the dataAccess module
 * including simpleQuery (one attribute) and compoundQuery (multiple attributes) 
 */
private void issueQuery (String queryType) {

	
if (queryType.equals("simpleQuery")) { 

	//call the plot access module
	dbAccess g =new dbAccess();  
	g.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", 
	"/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "query");	

	//grab the result set from the dbAccess class
	queryOutput=g.queryOutput;
	queryOutputNum=g.queryOutputNum;

}

if (queryType.equals("compoundQuery")) { 

	//call the plot access module
	dbAccess g =new dbAccess();  
	g.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", 
	"/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "compoundQuery");	

	//grab the result set from the dbAccess class
	queryOutput=g.queryOutput;
	queryOutputNum=g.queryOutputNum;

}



}

public String queryOutput[] = new String[10000];  //the output from query
int queryOutputNum; //the number of output rows from the query


}
