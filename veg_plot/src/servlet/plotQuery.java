import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;



/**
* This class provides an html page where a user can
* select from a series of criteria to develop a query
* that can be used to query the national plots database
*/
public class plotQuery extends HttpServlet {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException  {

response.setContentType("text/html");
PrintWriter out = response.getWriter();
String title =("plotQuery - Interface to the Plots Database");


//call the utility class which write the html telling the user that s/he
//is using the servlet to query the database
servletUtility k =new servletUtility();  
k.htmlStore();
out.println(k.outString);


//determine the criteria for the query - these parameters must map to the
//utlity class
int i=0;
String taxonName = request.getParameter("taxon");
String communityName = request.getParameter("community");
String taxonOperation=request.getParameter("taxonOperation");
String commOperation=request.getParameter("commOperation");

//print to the servlet page the query parameters
	out.println(rb.getString("requestparams.taxonName"));
	out.println(" " + taxonName +"<br>");
	out.println(rb.getString("requestparams.communityName"));
	out.println(" " + communityName + "<br><br>");
	
	
//broad check for entries
if (taxonName != null || communityName != null) {
	
	//for now just pass the taxonName to the composeQuery method
	//change this to include othe attributes later
	plotQuery g =new plotQuery();  
	g.composeQuery("taxonName", taxonName);
	
	
	//now using the xml query file created in composeQuery
	//method, issue the query using the plotAccess module
	plotQuery h =new plotQuery();  
	h.issueQuery();


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
*  Method to use dbAccess to issue the query to the database from the xml file
*  created in the plotQuery.composeQuery method 
*/
private void issueQuery () {

//call the plot access module
dbAccess g =new dbAccess();  
g.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", 
"/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "query");	

//grab the result set from the dbAccess class
queryOutput=g.queryOutput;
queryOutputNum=g.queryOutputNum;

}

public String queryOutput[] = new String[10000];  //the output from query
int queryOutputNum; //the number of output rows from the query


}
