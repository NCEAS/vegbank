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
* This is the main servlet class used to query the plots database.  Currently
* the servlet is accessed through a web page (called plotQuery.html) and can query 
* the database by using any of the following attributes:
* 	taxonName
*	elevation
*
* @author John H. Harris
* 
*/

public class plotQuery extends HttpServlet {
	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException  {

response.setContentType("text/html");
PrintWriter out = response.getWriter();


//call the utility class to write the html to the browser allowing for the
//user to build queries for the database
servletUtility k =new servletUtility();  
k.htmlStore();
out.println(k.outString);

//at some point we will want to do access control but for the time being
//just store the client's Ip address
String remoteHost=request.getRemoteHost();

try {
PrintStream clientLog = new PrintStream(new FileOutputStream(
	rb.getString("requestparams.clientLog"), true));
Date date = new Date();

clientLog.println(remoteHost+", "+date);

}
catch (Exception e) {System.out.println("failed in plotQuery.main trying to "
	+"access client log" + 
	e.getMessage());}

//determine grab the attributes as input from the web-page or URL
int i=0;
String taxonName = request.getParameter("taxon");
String communityName = request.getParameter("community");
String taxonOperation=request.getParameter("taxonOperation");
String commOperation=request.getParameter("commOperation");
String minElevation=request.getParameter("minElevation");
String maxElevation=request.getParameter("maxElevation");
String state = request.getParameter("state");
String surfGeo = request.getParameter("surfGeo");
String multipleObs = request.getParameter("multipleObs");
String compoundQuery = request.getParameter("compoundQuery");

//print to the browser the results from the query
	out.println(rb.getString("requestparams.taxonName"));
	out.println(" " + taxonName +"<br>");
	out.println(rb.getString("requestparams.communityName"));
	out.println(" " + communityName + "<br>");
	out.println(rb.getString("requestparams.minElevation"));
	out.println(" " + minElevation +"<br>");
	out.println(rb.getString("requestparams.maxElevation"));
	out.println(" " + maxElevation + "<br>");
	out.println(rb.getString("requestparams.state"));
	out.println(" " + state +"<br>");
	out.println(rb.getString("requestparams.surfGeo"));
	out.println(" " + surfGeo + "<br>");
	out.println(rb.getString("requestparams.multipleObs"));
	out.println(" " + multipleObs +"<br>");
	out.println(rb.getString("requestparams.compoundQuery"));
	out.println(" " + compoundQuery + "<br><br><br>");
	//and the name of the accessing machine
	out.println("database client: " + remoteHost + "<br><br><br>");
	
	
	
	
//broad check for attributes for single attribute queries
if ( (taxonName != null && compoundQuery == null) ) {
	
	if (taxonName != null) {
		plotQuery g =new plotQuery();  
		g.composeQuery("taxonName", taxonName);
	}
	
/* put this back in soon
	if ((minElevation != null) && (minElevation.indexOf("0")>=0) ) {
		String elevationRange [] = new String[2];
		elevationRange[0]=minElevation;
		elevationRange[1]=maxElevation;

		plotQuery g =new plotQuery();  
		g.composeQuery("elevation", elevationRange);
		
	}
*/	
	
	//now using the xml query file created in composeQuery
	//method, issue the query using the plotAccess module
	plotQuery h =new plotQuery();  
	h.issueQuery("simpleQuery");


	//pass to the the browser the number of results sets which includes
	//just plot identifiers
	out.println("<B> Number of plots in the Result set: "+h.queryOutputNum
		+"</B><br>");


	//if there are query results
	if (h.queryOutputNum>0) {
	
	//call the utility class which will allow the user 
	//to view the result set in various ways
	servletUtility l =new servletUtility();  
	l.getViewOption();
	out.println(l.outString);
	}//end if

} //end if


//broad check for attributes for compound attribute queries
if (compoundQuery != null ) {
out.println("Initiating compound query");

plotQuery m =new plotQuery();  
m.composeQuery(taxonName, communityName, taxonOperation, commOperation, minElevation,
	maxElevation, state, surfGeo, multipleObs, compoundQuery);

plotQuery n =new plotQuery();  
n.issueQuery("compoundQuery");

//pass to the the browser the number of results sets which includes
	//just plot identifiers
out.println("<B> Number of plots in the Result set: "+n.queryOutputNum
		+"</B><br>");


//if there are query results
if (n.queryOutputNum>0) {
	
//call the utility class which will allow the user 
//to view the result set in various ways
servletUtility o =new servletUtility();  
o.getViewOption();
out.println(o.outString);
}
	
} //end if

	
else {
            out.println(rb.getString("requestparams.no-params"));
} //end else
}  //end method


public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
} //end method





/**
*  Method to compose and print to a file an xml document that can be passed to
*  the dbAccess class to perform the query and print the results to a file
*  that is currently hard-wired as summary.xml
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
*  Method to compose and print to a file an xml document that can be passed to
*  the dbAccess class to perform the query and print the results to a file
*  that is currently hard-wired as summary.xml  - an overloaded version of the 
*  above.
*/

private void composeQuery (String queryElement, String[] elementString) {
try {
//set up the output query file called query.xml	using append mode to build  
PrintStream outFile  = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 

if (queryElement.equals("elevation")) {
//print the query instructions in the xml document
outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>elevationMin</queryElement> \n"+
	"<elementString>"+elementString[0]+"</elementString> \n"+
	"</query> \n"+
	"<query> \n"+
	"<queryElement>elevationMax</queryElement> \n"+
	"<elementString>"+elementString[1]+"</elementString> \n"+
	"</query> \n"+
	"<resultType>summary</resultType> \n"+
	"<outFile>/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml</outFile> \n"+
	"</dbQuery>"
);
}
	
}
catch (Exception e) {System.out.println("failed in plotQuery.composeQuery" + 
	e.getMessage());}
} //end method



/**
*  Method to compose and print to a file an xml document that can be passed to
*  the dbAccess class to perform the query and print the results to a file
*  that is currently hard-wired as summary.xml  - an overloaded version of the 
*  above.
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
