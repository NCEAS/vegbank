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
 *	attributes listed below <br>
 * debugLevel -- if this value is one then debugging/processing info is written
 *	to the client browser <br>
 * fileName -- the name that the user is applying to the downloaded file <br>
 * formatType -- the downloaded file format type (incl: xml, html, ascii) <br>
 * userNotes -- short note text that will be included in downloaded file header<br>
 * dataType -- type of plot information desired in the downloaded file - may
 * 	include entire plot, species list, or environmental data<br>
 * aggregationType -- describes how the downloaded data is to be clustered
 * 	- including gzipped single file, zipped multiple files, or both<br>
 * compression -- if no aggregation type is specifed then use this compression<br>
 * fileNamePath -- the downloadPath plus fileName <br>
 * plotRequestList -- the list of plotId numbers that the user is requesting
 * 	data for <br>
 * atomicResultSet -- the file that the results for each individual plot is
 * 	stored in before being concatenated or zipped into the download file <br>
 * cummulativeResultsSet -- the file that the results for the entire list of
 * 	plots are contained within before being compressed/zipped <br>
 *
 * <p>action=upload -- intitiates a file upload to the server<br>
 *
 * @author John Harris
 *
 */

public class fileDownload extends HttpServlet {

ResourceBundle rb = ResourceBundle.getBundle("fileDownload");

private String fileName = null;
private String downloadPath=null;
private String fileNamePath = null;
private String fileFormatType = null;
private String userNotes = null;
private String aggregationType = null;
private String dataType = null;
private String plotRequestList = null;
private String atomicResultSet = null;
private String cummulativeResultSet= null;

int debugLevel=0;




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

response.setContentType("text/html");
PrintWriter out = response.getWriter();


/**
 * The first try block is for reading the parameters into a hash table and then 
 * assigning the parameters to their respective variables
 */
try {
Enumeration enum =request.getParameterNames();
Hashtable params = new Hashtable();

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

//get the variables from the form
String buf = (String)params.get("debugLevel");
debugLevel = 0;
if (buf != null)
	debugLevel =  Integer.parseInt(buf);	
fileName = (String)params.get("fileName");
fileNamePath = downloadPath+fileName.trim();
fileFormatType = (String)params.get("formatType");
aggregationType = (String)params.get("aggregationType");
userNotes = (String)params.get("userNotes");
dataType = (String)params.get("dataType");

//get the variables privately held in the properties file
downloadPath=(rb.getString("requestparams.downloadPath"));
plotRequestList=(rb.getString("requestparams.plotRequestList"));
atomicResultSet = (rb.getString("requestparams.atomicResultSet"));
cummulativeResultSet= (rb.getString("requestparams.cummulativeResultSet"));


}//end try
catch( Exception e ) {System.out.println("servlet failed in: fileDownload.main "
	+" first try - reading parameters "+e.getMessage());}



/**
 * In the second try the parameters read in the first try are analyzed and
 * actions are taken reflecting this information
 */
try {

if (debugLevel !=1) {
	//request the plots from the database  
	dataRequester(plotRequestList, atomicResultSet, cummulativeResultSet, "entirePlot");

	//transform the data to the appropriate data type 

	//compress the file to the appropriate compression type
	dataCompressor(cummulativeResultSet, fileNamePath, "gzip"); 

	//redirect the user to the appropriate file - if the debugger is off == 0
	response.sendRedirect("/downloads/"+fileName);
}

else {
	out.println("Servlet: fileDownload > <br>");
	out.println("fileName: " + fileName +"<br>"
		+"debugLevel: " + debugLevel +"<br>"
		+"downloadPath: " + downloadPath +"<br>"
		+"fileNamePath: " + fileNamePath +"<br>"
		+"fileFormatType: " + fileFormatType +"<br>"
		+"aggregationType: " + aggregationType +"<br>"
		+"userNotes: " + userNotes +"<br>"
		+"debugLevel: " + debugLevel +"<br>"
		+"plotRequestList: " + plotRequestList +"<br>"
		+"aggregationType: " + aggregationType +"<br>"
		+"dataType: " + dataType +"<br>"
		+"atomicResultSet " + atomicResultSet +"<br>"
		+"cummulativeResultSet " + cummulativeResultSet +"<br>"
		);
}

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
 * @param dataType the type of plot data desired including: species,
 *	environmental, or entire plot
 */

private void dataRequester (String plotList, String atomicResultSet, 
	String cummulativeResultSet, String dataType) {

//pass the filename to the fileVectorizer method to make the file a vector
servletUtility a =new servletUtility();  
a.fileVectorizer(plotList);

/** Flush the temporary download files */
servletUtility c =new servletUtility();  
c.flushFile(atomicResultSet);
//flush a different way
(new File(cummulativeResultSet)).delete();


//for each plotId request the plot from the plotQuery servlet
for (int i=0; i<a.vecElementCnt; i++) {
	System.out.println("fileDownload.plotRequstor - plotId > "
		+a.outVector.elementAt(i));

	String plotId = a.outVector.elementAt(i).toString().trim();

	/** Make the connection to the wellQuery servlet here */
	String uri = "http://127.0.0.1/examples/servlet/"
		+"plotQuery?plotId="+plotId+"&"+"resultType=full";
	int port=8080;
	String requestType="POST";

	GetURL b =new GetURL();  
	b.getPost(uri, port, requestType);

	/** Concatenate the resulting file so that it isnt overwritten */
	servletUtility d =new servletUtility();  
	d.fileCopy(atomicResultSet, cummulativeResultSet, "concat");

}
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

private void dataTransformer (String plotFile, String formatType) {

}





/**
 *  Method that takes as input both the name of the xml file containing the plots
 *  desired for downloading and the aggregationType for the desired plots, ie. 
 *  single gzip file, multiple zipped file, or both and prepares the file for 
 *  downloading
 *
 * @param inFile filename of the file to be compressed
 * @param outFile output file name of the compressed file
 * @param aggregationType the format of the file desired by the user including
 *	gzipped single file, zipped, aggregate files, or both
 */

private void dataCompressor (String inFile, String outFile, 
	String aggregationType) {

servletUtility a =new servletUtility();  
a.gzipCompress(inFile, outFile);	

}





}		
	
	
