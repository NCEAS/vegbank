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

//compose a very simple html page
out.println("<html>");
out.println("<body>");
out.println("<head>");
out.println("<title>" + title + "</title>");
out.println("</head>");
out.println("<body bgcolor=\"white\">");
out.println("<h3>" + title + "</h3>");
out.println("<p>");
out.println("<img alt=\"this is not the final image\" src=\"/harris/owlogo.jpg\" border=\"0\">");
out.println("<p>");
out.println(rb.getString("requestparams.params-in-req") + "<br>");
out.println("<br><i>");
out.println(rb.getString("requestparams.protoDescription")+"<br>");
out.println("<br></i>");
out.println("<A HREF=\"http://www.nceas.ucsb.edu/collab/2180/docs/diagrams/prototypeERD.pdf\"><B><FONT SIZE=\"-1\" FACE=\"arial\">Current Plots Data Model</FONT></B></A>");
out.println("<br></i>");
out.println("<P>");
out.print("<form action=\"");
out.print("plotQuery\" ");
out.println("method=POST>");

//set up the taxon specification
out.println("Enter taxon");  //set up text field for taxon name
out.println("<input type=text size=28 name=taxon>");
out.println("<input type=\"radio\" NAME=\"taxonOperation\" Value=\"required\" CHECKED> required");
out.println("<input type=\"radio\" NAME=\"taxonOperation\" Value=\"optional\" > optional");


out.println("<br>");
out.println("<br>");


//set up the community specification
out.println("Enter community name");  //set up text field for community name
out.println("<input type=text size=20 name=community>");
out.println("<input type=\"radio\" NAME=\"commOperation\" Value=\"required\" CHECKED> required");
out.println("<input type=\"radio\" NAME=\"commOperation\" Value=\"optional\" > optional");

out.println("<br><br> \n");
out.println("<input type=submit>");
out.println("</form>");
out.println("</body>");
out.println("</html>");


//determine the criteria for the query
int i=0;
String taxonName = request.getParameter("taxon");
String communityName = request.getParameter("community");
String taxonOperation=request.getParameter("taxonOperation");
String commOperation=request.getParameter("commOperation");

//broad check for entries
if (taxonName != null || communityName != null) {
	
	//for now just pass the taxonName to the composeQuery method
	//change this to include othe attributes later
	plotQuery g =new plotQuery();  
	g.composeQuery("taxonName", taxonName);
	
	//now using the xml query file created in composeQuery
	//issue the query using the plotAccess module
	plotQuery h =new plotQuery();  
	h.issueQuery();
	
	out.println(rb.getString("requestparams.taxonName"));
	out.println(" = " + taxonName + " "+taxonOperation+"<br>");
	out.println(rb.getString("requestparams.communityName"));
	out.println(" = " + communityName+ " "+commOperation+"<br>");

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
*  Method to compose and print out a query based
*  on the query options chosen by the user
*/
private void composeQuery (String queryElement, String elementString) {
try {
//set up the output query file called query.xml	using append mode 
PrintStream outFile  = new PrintStream(new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", false)); 

//print the query instructions in the xml document
outFile.println("<?xml version=\"1.0\"?> \n"+       
	"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
	"<dbQuery> \n"+
	"<query> \n"+
	"<queryElement>"+queryElement+"</queryElement> \n"+
	"<elementString>"+elementString+"</elementString> \n"+
	"</query> \n"+
	"</dbQuery>");
	
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

}





}
