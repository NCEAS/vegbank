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
* This class allows the user to navigate the resultset produced by
* using the plotQuery class
*/
public class viewData extends HttpServlet {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

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

// 1. get the other input from the form, like the names of the plots that are
// 2. needed and pass to the xml transform utility to get correct dataset	
// 3. pass the download dataset to the compression utility
// 4. pass the download-related attributes like filename etc to the function that 
// 5. creates the html-page for download

	
try {
//print out to file the names of the plots and their plotId's for use by the 
//fileDownload servlet
PrintStream outFile  = new PrintStream(
	new FileOutputStream("/jakarta-tomcat/webapps/examples/WEB-INF/lib/plotDownloadList", false));

Enumeration enum =request.getParameterNames();
while (enum.hasMoreElements()) {
	String name = (String) enum.nextElement();
	String values[] = request.getParameterValues(name);
	if (values != null) {
		for (int i=0; i<values.length; i++) {
			
			if (name.equals("plotName")) {
				outFile.println(values[i]);
			}
			out.println(name +" ("+ i + "): " +values[i]+"; <br></br>");
			
		}
	}
}

//now call the download page
response.sendRedirect("/examples/servlet/pageDirector?pageType=download");

	
out.println("<a href=\"/downloads/test.zip\">DownloadFile From Here</a>");
//response.sendRedirect("/downloads/test.zip");


	
	}  //end try
	
catch( Exception e ) {System.out.println("servlet failed in: viewData.main"
	+e.getMessage());}

	
}

/**
*  If the download page is not requested then request then show the summary
*  of all the search results
*/
else {
//call the utility class which holds the introduction html as a string that can
//be returned to the browser
	servletUtility k =new servletUtility();  
	k.getViewMethod();
	out.println(k.outString);
		
	String formatType = request.getParameter("formatType");
	//out.println(formatType);
	
	//no matter how the user wants the plots displayed just
	//transform the result set to the screen in a simple fashion
	
	
	try {
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

	/**
	* This try block retrieves data back from the summary page of the
	* result set from the plotQuery data and determines which plots should be
	* downloaded
	*/
		
	try {
				
		//use this function to figure out the type and number of inputs
		//it is a temporary function - comment out later
		Enumeration enum =request.getParameterNames();
		while (enum.hasMoreElements()) {
			String name = (String) enum.nextElement();
			String values[] = request.getParameterValues(name);
			if (values != null) {
				for (int i=0; i<values.length; i++) {
					out.println(name +" ("+ i + "): "
					+values[i]+"; ");
				}
			}
		}
	/*	
	String buttonAction= request.getParameter("formatType");
	String downLoadPlot = request.getParameter("myform");
	
	String listInput = request.getParameter("list");  //plot numbers
	out.println("buttonAction: " +buttonAction+"<br>");
	out.println("downLoadPlot: " +downLoadPlot+"<br>");
	out.println("listInput: " +listInput+"<br>");
	*/
	
	
	
	
	}  //end try
	catch( Exception e ) {System.out.println("servlet failed in: viewData.main second try   "+e.getMessage());}

} //end else

}

public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
} //end method




}
