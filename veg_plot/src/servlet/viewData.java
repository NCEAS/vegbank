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
 * This class allows the user to view the summary of the resultset produced by
 * the queries run by the plotQuery class, and soon this class will have
 * multiple methods for viewing and analyzing plots in different ways - this
 * will happen after the NCEAS interface design meeting in jan 2001
 *
 * @version 11 Jan 2001
 * @author John Harris
 *
 */
public class viewData extends HttpServlet {

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

/** Handle "POST" method requests from HTTP clients */
public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
}


/** Handle "GET" method requests from HTTP clients */
public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException  {

response.setContentType("text/html");
PrintWriter out = response.getWriter();

/**
* Determine if the user wants to download the data or just to view the summary
* table  - in the future these options will increase
*/

/**
* Check to see if the user wants to download the data and if so do below - which
* is to write a list of the selected plots (checked check-boxes) to the local
* (lib) directory and then call the fileDownload servlet to seperate the desied
* plots form the undesired plots and then transform the data into the format
* specified by the form (download.html) calling the fileDownload servlet
*
*/
String downLoadAction= request.getParameter("downLoadAction");
if (downLoadAction != null) {

try {
//print out to file the names of the plots and their plotId's for use by the 
//fileDownload servlet
 PrintStream outFile  = new PrintStream(
	new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/plotDownloadList", false));

//use this function to figure out the type and number of inputs
//it is a temporary function - comment out later
Enumeration enum =request.getParameterNames();
while (enum.hasMoreElements()) {
	String name = (String) enum.nextElement();
	String values[] = request.getParameterValues(name);
	if (values != null) {
		for (int i=0; i<values.length; i++) {
			if (name.equals("plotName")) {
			outFile.println(values[i]);	
			out.println(name +" ("+ i + "): "
			+values[i]+"; <br>");
			
			}
		
		}
	}
}

//now call the download page
 response.sendRedirect("/examples/servlet/pageDirector?pageType=download");	
}
catch( Exception e ) {System.out.println("servlet failed in: viewData.main"
	+e.getMessage());}
}

/**
*  If the download page is not requested then request then show the summary
*  of all the search results
*/
else {

viewResultsSummary(response, out, "fileName");


} //end else

}



/**
 *  Method to allow the client to view summary results from a plot query
 *
 * @param response - the response object linked to the client
 * @param out - the output stream to the client
 * @param fileName - name of the xml file for translation/viewing
 * 
 */

private void viewResultsSummary (HttpServletResponse response, 
	PrintWriter out, String fileName) {

try {

//	servletUtility k =new servletUtility();  
//	k.getViewMethod();
//	out.println(k.outString);
		
	
	/**
	* call the method to transform the data xml document and pass back a string writer  
	*/
	transformXML m = new transformXML();
	m.getTransformed("/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml", 
	"/jakarta-tomcat/webapps/examples/WEB-INF/lib/showSummary.xsl");

	StringWriter transformedData=m.outTransformedData;  //the stringwriter containg all the transformed data

	/**
	* pass the String writer to the utility class to convert the StringWriter to an array
	*/

	utility u =new utility();
	u.convertStringWriter(transformedData);

	String transformedString[]=u.outString;  // the string from the utility.convertStringWriter
	int transformedStringNum=u.outStringNum; // the number of vertical elements contained in the string array

	//print the list of plots to the browser as a summary and then
	//read the further requests such as expanded species, or download
	for (int ii=0;ii<u.outStringNum; ii++) {
		out.println(u.outString[ii]+"");
	}
	
		
		
}  //end try
catch( Exception e ) {System.out.println("servlet failed in: viewData   "+e.getMessage());}

}



}
