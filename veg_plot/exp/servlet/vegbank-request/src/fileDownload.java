import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import servlet.util.ServletUtility;
import servlet.util.GetURL;
import servlet.util.HttpMessage;
import java.net.*;
import xmlresource.utils.transformXML;

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

public class fileDownload extends HttpServlet 
{

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
	private String DataRequestServletURL=null;
	private int debugLevel=1;
	private ServletUtility sutil; 
	private GetURL gurl;
	private transformXML transformer; 
	
	private String htmlStyleSheet =  "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/ascii-treeview.xsl";
	
	//below are the stylesheets for the flat ascii file tarnsformation process 
	private String asciiSitesStyleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/plotsites-flatascii.xsl";
	private String asciiSpeciesStyleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/plotspecies-flatascii.xsl";
	private String condensedAsciiSpeciesStyleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/plotspecies-condensedascii.xsl";
	
	
	/**
	 * constructor method -- just to show status on startup
	 */
	 public fileDownload()
	 {
	 	try
		{
		 System.out.println("init: fileDownload");
		 this.DataRequestServletURL= (rb.getString("requestparams.DataRequestServletURL"));
		 System.out.println("fileDownload init > datarequest url: " +  this.DataRequestServletURL);
		 sutil = new ServletUtility();
		 gurl = new GetURL();
		 transformer = new transformXML();
		}
		 catch (Exception e )
		 {
		 	System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }

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
		try 
		{
			Enumeration enum =request.getParameterNames();
			Hashtable params = new Hashtable();
			params = sutil.parameterHash(request);
		
			fileName = (String)params.get("fileName");
			fileFormatType = (String)params.get("formatType");
			aggregationType = (String)params.get("aggregationType");
			userNotes = (String)params.get("userNotes");
			dataType = (String)params.get("dataType");

			//get the variables privately held in the properties file
			downloadPath=(rb.getString("requestparams.downloadPath"));
			plotRequestList=(rb.getString("requestparams.plotRequestList"));
			atomicResultSet = (rb.getString("requestparams.atomicResultSet"));
			cummulativeResultSet= (rb.getString("requestparams.cummulativeResultSet"));
			//DataRequestServletURL= (rb.getString("requestparams.DataRequestServletURL"));
		
			//this is the path and filename of the download file
			//and should be passed to the file copy method the downLoad
			//path should be absolute
			fileNamePath = downloadPath+"/"+fileName.trim();
			System.out.println("fileDownload > fileNamePath: "+ fileNamePath);
			System.out.println("fileDownload > dataType: "+ dataType);
			System.out.println("fileDownload > userNotes: "+ userNotes);
			System.out.println("fileDownload > aggregationType: " + aggregationType);
			System.out.println("fileDownload > fileFormatType: "+ fileFormatType);
			System.out.println("fileDownload > fileName: "+ fileName);
		
		}//end try
		catch( Exception e ) 
		{
			System.out.println("servlet failed in: fileDownload.main "
			+" first try - reading parameters "
			+e.getMessage());
		}
		
		try 
		{
			String downloadFile = "";
			
			//request the plots from the database  
			dataRequester(plotRequestList, atomicResultSet, cummulativeResultSet, 
			"entirePlot");
			// REGISTER THE FILENAMES FOR THE REQUESTED TYPE
			if ( fileFormatType.equals("html") )
			{
				downloadFile = "download.html";
			}
			else if ( fileFormatType.equals("flat") || fileFormatType.equals("condensed") )
			{
				downloadFile = "download.txt";
			}
			else if ( fileFormatType.equals("xml") )
			{
				downloadFile = "download.xml";
			}
			else
			{
				System.out.println("fileDownload > unknown fileFormatType ");
				downloadFile = downloadPath+"/download.xml";
			}
			//transform the file into the appropriate type
			this.dataTransformer(cummulativeResultSet, fileFormatType);
			
			// if the user requested compression then do it otherwise just pass
			// them the file
			if ( aggregationType.equals("compress") )
			{
				//compress the file to the appropriate compression type
				dataCompressor(cummulativeResultSet, downloadPath+downloadFile+".gz", "gzip");
				//redirect the user to the appropriate file 
				response.sendRedirect("/downloads/"+downloadFile+".gz");
			}
			else
			{
				System.out.println("fileDownload > copying file: " + cummulativeResultSet +" to: "+downloadPath+downloadFile);
				sutil.fileCopy(cummulativeResultSet, downloadPath+downloadFile);
				//redirect the user to the appropriate file 
				response.sendRedirect("/downloads/"+downloadFile);
			}
		}
		catch(Exception e) 
		{
			System.out.println("Exception  " + e.getMessage() );
		}
	}





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

	private void dataRequester(String plotList, String atomicResultSet, 
	String cummulativeResultSet, String dataType) 
	{
		try
		{		
			//pass the filename to the fileVectorizer method to make the file a vector
			Vector fileVec = sutil.fileToVector(plotList);
			sutil.flushFile(atomicResultSet);
			//flush a different way
			(new File(cummulativeResultSet)).delete();

	
			// this is the stringbuffer that will store the plot id numbers
			// and will be then transformed into a comma-separated string before
			// being passed to the data request servlet
			StringBuffer plotBuf = new StringBuffer();
			for (int i=0; i< fileVec.size(); i++) 
			{
				if (fileVec.elementAt(i) != null )
				{
					String plotId = fileVec.elementAt(i).toString().trim();
					
					if ( i == 0)
					{
						plotBuf.append(plotId);
					}
					else
					{
						plotBuf.append(","+plotId);
					}
				}
				else
				{
					System.out.println("FileDownloadServlet > encountered a null");
				}
			}
				//THIS USES THE REQUESTURL METHOD
	//			String protocol = "http://";
	 // 		String host = "vegbank.nceas.ucsb.edu";
				String s = null;		
		//		String servlet = "/framework/servlet/DataRequestServlet";
				//try a different method
			 	StringBuffer urlBuf = new StringBuffer();
			// 	urlBuf.append(protocol+host+servlet+"/?");
			 	urlBuf.append(DataRequestServletURL+"/?");
				urlBuf.append("requestDataType=vegPlot&");
			 	urlBuf.append("resultType=full&");
			 	urlBuf.append("queryType=simple&");
			 	urlBuf.append("requestDataFormatType=xml&");
			 	urlBuf.append("clientType=app&");
			 	urlBuf.append("plotId="+plotBuf.toString());
			 
			 String  u1str = urlBuf.toString();
       System.out.println("fileDownload > server string: " + u1str);
       
			 URL u1 = new URL(u1str);
       HttpMessage msg = new HttpMessage(u1);
 			 InputStream in = msg.sendPostMessage();

       InputStreamReader isr = new InputStreamReader(in);
       char c;
       int i = isr.read();
       String temp2 = "";
       while(i != -1)
       { 
         c = (char)i;
         temp2 += c;
       	 i = isr.read();
       }
			System.out.println("fileDownload > retreiving the DataRequestServlet response" );
			// Concatenate the resulting file so that it isnt overwritten  
			sutil.fileCopy(atomicResultSet, cummulativeResultSet, "concat");
		}
		catch( Exception e)
		{
				System.out.println("Exception: " + e.getMessage() );
				e.printStackTrace();
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
	private void dataTransformer (String plotFile, String formatType) 
	{
		String workFile = "/tmp/filedownload-work.txt";
		String workFile2 = "/tmp/filedownload-work-species.txt";
		try
		{
			// the user wants to transform to a flat file -- which is actually two files 
			// one with the 
			if ( formatType.equals("flat") )
			{
				System.out.println("fileDownload > transforming data to flat-ascii -- sites");
				transformer.transformXMLDocumentToFile(plotFile, asciiSitesStyleSheet, workFile);
				System.out.println("fileDownload > transforming data to flat-ascii -- species");
				transformer.transformXMLDocumentToFile(plotFile, asciiSpeciesStyleSheet, workFile2);
				//copy the work file to the target file
				sutil.fileCopy(workFile, cummulativeResultSet, "append");
				sutil.fileCopy(workFile2, cummulativeResultSet, "concat");
			}
			else if ( formatType.equals("html") ) 
			{
				System.out.println("fileDownload > transforming data to html");
				transformer.transformXMLDocumentToFile(plotFile,htmlStyleSheet,workFile);
				//copy the work file to the target file
				sutil.fileCopy(workFile, cummulativeResultSet, "append");
			}
			// THIS IS FOR THE CONDENSED ASCII FORMAT -- THE FORMAT THAT IS TO BE 
			// USED BY THE ANALYTICAL TOOLS 
			else if ( formatType.equals("condensed") ) 
			{
				System.out.println("fileDownload > transforming data to condensed-ascii -- sites");
				transformer.transformXMLDocumentToFile(plotFile, asciiSitesStyleSheet, workFile);
				System.out.println("fileDownload > transforming data to condensed-ascii -- species");
				transformer.transformXMLDocumentToFile(plotFile, condensedAsciiSpeciesStyleSheet, workFile2);
				//copy the work file to the target file
				sutil.fileCopy(workFile, cummulativeResultSet, "append");
				sutil.fileCopy(workFile2, cummulativeResultSet, "concat");
			}
			
			else
			{
				System.out.println("fileDownload > no transformation");
			}
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
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
	String aggregationType) 
	{
		ServletUtility a =new ServletUtility();  
		a.gzipCompress(inFile, outFile);	
	}

}		
	
	
