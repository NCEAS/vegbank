import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet to perform file downloading operations 
 *
 * <p>Valid parameters are:<br><br>
 * action=actionDownload -- initiates the file download process using the
 *		attributes listed below <br>
 * fileName -- the name that the user is applying to the downloaded file <br>
 * formatType -- the downloaded file format type (incl: xml, html, ascii) <br>
 * userNotes -- short note text that will be included in downloaded file header<br>
 * dataType -- type of plot information desired in the downloaded file - may
 * 		include entire plot, species list, or environmental data<br>
 * aggregationType -- describes how the downloaded data is to be clustered
 * 		- including gzipped single file, zipped multiple files, or both<br>
 * compression -- if no aggregation type is specifed then use this compression<br>
 * 
 *
 * <p>action=upload -- intitiates a file upload to the server<br>
 *
 *
 * @author John Harris
 */

public class fileDownload extends HttpServlet {

    ResourceBundle rb = ResourceBundle.getBundle("plotQuery");


/** Handle "POST" method requests from HTTP clients */
public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }
    
    
/** Handle "GET" method requests from HTTP clients */  
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {

/**
 * Before anything is done read the input parameters into a hashtable for
 * future use
 */

try {
response.setContentType("text/html");
PrintWriter out = response.getWriter();

Enumeration enum =request.getParameterNames();
Hashtable params = new Hashtable();
while (enum.hasMoreElements()) {
	String name = (String) enum.nextElement();
	String values[] = request.getParameterValues(name);
	if (values != null) {
		
		for (int i=0; i<values.length; i++) {
			out.println(name +" ("+ i + "): " +values[i]+"; <br>");
			params.put(name,values[i]);
		
		}
	}
}

//test print of the hash table
String n = (String)params.get("fileName");
     if (n != null) {
         out.println(n);
     }

//request the plots from the database
fileDownload a =new fileDownload();  
a.plotRequester("/jakarta-tomcat/webapps/examples/WEB-INF/lib/plotDownloadList", "entirePlot");

//transform the data to the appropriate data type 

//compress the file to the appropriate compression type

//redirect the user to the appropriate file
response.sendRedirect("/downloads/test.xml.gz");

/**


//test the method for compressing a file
out.println("compressing a file");
servletUtility g =new servletUtility();  
g.gzipCompress("/jakarta-tomcat/webapps/examples/WEB-INF/lib/summary.xml", 
"/jakarta-tomcat/webapps/ROOT/downloads/test.xml.gz");	

//sleep for a sec
//Thread.currentThread().sleep(1000);

//redirect the browser to the downloadable file
response.sendRedirect("/downloads/test.xml.gz");

*/
}//end try
catch( Exception e ) {System.out.println("servlet failed in: fileDownload.main "
	+" second try   "+e.getMessage());}
}//end method



/**
 *  Method that takes as input both the name of the file containing the database 
 *  plot identification numbers desired for downloading and the dataType, ie. 
 *  entire plot, species list, or environmental data and returns an xml file 
 *  containing that data which then must be transformed and compressed in 
 *  other methods
 *
 * @param plotList filename containing the list of the plots desired by the user
 * @param dataType the type of plot data desired including: species
 *	, environmental, or entire plot
 */

private void plotRequester (String plotList, String dataType) {
//pass the filename to the fileVectorizer method to make the file a vector
servletUtility a =new servletUtility();  
a.fileVectorizer(plotList);
System.out.println("number of vector elements: "+a.vecElementCnt);

//for each plotId request the plot from the plotQuery servlet
for (int i=0; i<a.vecElementCnt; i++) {
	System.out.println("fileDownload.plotRequstor > "+a.outVector.elementAt(i));
	//make the connection to the servlet here
}

//print the data file ?




}


/**
 *  Method that takes as input both the name of the xml file containing the plots
 *  desired for downloading and the formatType for the desired plots, ie. 
 *  html, xml, flat ascii
 *
 * @param plotFile filename containing the plots data desired by the user
 * @param formatType the format of the file desired by the user including
 *	gzipped single file, zipped, aggregate files, or both
 */

private void plotTransformer (String plotFile, String formatType) {

}



/**
 *  Method that takes as input both the name of the xml file containing the plots
 *  desired for downloading and the aggregationType for the desired plots, ie. 
 *  single gzip file, multiple zipped file, or both and prepares the file for 
 *  downloading
 *
 * @param plotFile filename containing the plots data desired by the user
 * @param aggregationType the format of the file desired by the user including
 *	gzipped single file, zipped, aggregate files, or both
 */

private void plotCompressor (String plotList, String aggregationType) {

}






}		
	
	
