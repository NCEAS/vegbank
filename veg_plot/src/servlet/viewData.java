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

//call the utility class which will hold all the html
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

		//print the list of plots to the browser
		for (int ii=0;ii<u.outStringNum; ii++) {
			out.println(u.outString[ii]+"<br>");
		}
	}  //end try
	catch( Exception e ) {System.out.println("servlet failed in: viewData   "+e.getMessage());}
	

}

public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
} //end method




}
