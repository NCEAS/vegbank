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

//this is the new xalan
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;


import xmlresource.utils.transformXML;
import servlet.util.GetURL;



/**
 * This class allows the user to view the summary of the resultset produced by
 * the queries run by the query servlet(s).  These summaries are currently
 * produced by an xsl transform of the results set which exists as an xml file. 
 * These xsl sheets are currently static but in the near future the end user
 * will be able to manipulate the sheets to view the data the he/she desires.
 *
 * <p>Valid parameters are:<br><br>
 * 
 * summaryViewType - type of data to summarize, can include community, 
 	vegPlot, plantTaxa <br>
 * resultType - type of summary result - this is not really currently used <br>
 * downLoadAction - if this is not null then the user wants to download a file
 *
 * @version 12 Feb 2001
 * @author John Harris
 *
 */
public class viewData extends HttpServlet 
{

	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

	private String downLoadAction=null;
	private String summaryViewType=null;
	private String resultType=null;
	private String servletDir= null; //like: /opt/jakarta/harris/servlet
	private String servletPath=null; //like: /harris/servlet
	private String userEmail = null;
	
	
	//access the method to transfor the xml document and retrieve 
	//the string writer
	private transformXML m = new transformXML();
	private GetURL gurl = new GetURL();
	
	
	//constructor
	public viewData()
	{
		try
		{
			System.out.println("init: viewData" );
			servletDir = rb.getString("requestparams.servletDir");
			servletPath = rb.getString("servlet-path");
			System.out.println("ViewData > servlet dir: " + servletDir );
			System.out.println("ViewData > servlet path: " + servletPath );
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	

	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException 
	{
        doGet(request, response);
	}


	/** Handle "GET" method requests from HTTP clients */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException  
	{
		servletDir = rb.getString("requestparams.servletDir");
		servletPath = rb.getString("servlet-path");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		//grab all the input parameters
		downLoadAction=request.getParameter("downLoadAction");
		summaryViewType=request.getParameter("summaryViewType");
		resultType=request.getParameter("resultType");
		this.userEmail = this.getCookieValue(request);
		
		//print the input to the system
		System.out.println("ViewData > downLoadAction: " + downLoadAction);
		System.out.println("ViewData > summaryViewType: " + summaryViewType );
		System.out.println("ViewData > user: " + this.userEmail);
		System.out.println("ViewData > resultType: " + resultType );

		//handle a download request
		if (downLoadAction != null)
		{
			this.initPlotDownload(request);
 			response.sendRedirect("http://vegbank.nceas.ucsb.edu/forms/resultset-download.html");	
		}
		
		else if (resultType.equals("identity") )
		{
			viewResultsIdentity(response, out, "fileName", summaryViewType.trim());
		}
		else 
		{
			viewResultsSummary(response, out, "fileName", summaryViewType.trim());
		}
	}
	
	/**
	 * method to handle the request for a download which is basiaclly to
	 * generate a file locally that has the plot id's that the user 
	 * wants to capture in the download
	 */
	 private void initPlotDownload(HttpServletRequest request)
	 {
		 try
		 {
			 PrintStream outFile  = new PrintStream(
			 new FileOutputStream(servletDir+"plotDownloadList", false));
		
				//use this function to figure out the type and number of inputs
				//it is a temporary function - comment out later
				Enumeration enum =request.getParameterNames();
				while (enum.hasMoreElements()) 
				{
					String name = (String) enum.nextElement();
					String values[] = request.getParameterValues(name);
					if (values != null) 
					{
						for (int i=0; i<values.length; i++) 
						{
							if (name.equals("plotName")) 
							{
								System.out.println("viewData > name: " + values[i]);
								outFile.println(values[i]);	
							}
						}
					}
				}
				outFile.close();
		 }
			catch( Exception e ) 
			{
				System.out.println("Exception: " + e.getMessage());
			}
	 }
	
	
	/**
	 * method that returns the cookie value associated with the 
	 * current browser
	 */
	private String getCookieValue(HttpServletRequest req)
	{
		
		//get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
		//determine if the requested page should be shown
    if (cookies.length > 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
				System.out.println("ViewData > cleint passing the cookie: "+cookieName+" value: "
					+cookieValue);
			}
  	}
		return(cookieValue);
	}


/**
 *  Method to print to screen the results set returned by the query servlet in
 * summary format using an xsl style sheet
 *
 * @param response - the response object linked to the client
 * @param out - the output stream to the client
 * @param fileName - name of the xml file for translation/viewing
 * @param summaryViewType - the type of summary that should be displayed to the
 * browser including: vegPlot, community, plantTaxa
 * 
 */
	private void viewResultsSummary(HttpServletResponse response, 
	PrintWriter out, String fileName, String summaryViewType) 
	{

		String styleSheet=null; //the stylesheet to use
		String xmlDoc = null;
		//get the servlet directory
	//	ResourceBundle rbun = ResourceBundle.getBundle("plotQuery");
		servletDir = rb.getString("requestparams.servletDir");
		try 
		{
			//determine the style sheet to use for the xsl transform
			if (summaryViewType.equals("vegCommunity")) 
			{
  			styleSheet=servletDir+"showCommunitySummary.xsl";
				xmlDoc = servletDir+"summary.xml";
			}
			else if (summaryViewType.equals("plantTaxon")) 
			{
  			styleSheet=servletDir+"showPlantTaxaSummary.xsl";
				xmlDoc = servletDir+"summary.xml";
			}
			//GET THE USER'S DEFAULT STYLE SHEET
			else if (summaryViewType.equals("vegPlot")) 
			{
				xmlDoc = servletDir+"test-summary.xml";
				System.out.println("ViewData > retrieving the users stylesheet -");
				//JHH TURND THIS FUNCTION OFF 20020315
				//String stylefile = getUserDefaultStyle("user").trim();
				String stylefile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/transformMultiPlotSummary.xsl";
				System.out.println("ViewData > xml document: '" + xmlDoc +"'" );
				System.out.println("ViewData > stylesheet name: '" + stylefile +"'" );
				
				if ( stylefile == null  ||  stylefile.equals("null")   )
				{
					System.out.println("ViewData > no default style for this user");
					styleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test.xsl";
				}
				
				else
				{
					styleSheet = stylefile;
				}
			}
			//let the user know that there is a problem with the request
			else 
			{
				out.println("ViewData.viewResultsSummary: unknown request for xsl: '"
					+summaryViewType+"'" );
			}
			
				System.out.println("ViewData > used old transformer");
				// access the method to transfor the xml document and 
				// retrieve the string writer
				String teststyle = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/test.xsl";
				
				m.getTransformed(xmlDoc, styleSheet);
				StringWriter transformedData = m.outTransformedData;
				Vector contents = this.convertStringWriter(transformedData);

				//##/test the new method in the xmlTransformer class
///			transformXML transformer = new transformXML();
///			String test = transformer.getTransformedNoErrors(servletDir+"summary.xml", styleSheet); 
				for (int ii=0;ii< contents.size() ; ii++) 
				{
					out.println( (String)contents.elementAt(ii) );
				}
		} 
		catch( Exception e ) 
		{
			System.out.println("Exception: ViewData.viewResultsSummary: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
/**
 *  Method to print to screen the identity information realetd to a plot
 * this consists of the location and the name and the accession number
 *
 * @param response - the response object linked to the client
 * @param out - the output stream to the client
 * @param fileName - name of the xml file for translation/viewing
 * @param summaryViewType - the type of summary that should be displayed to the
 * browser including: vegPlot, community, plantTaxa
 * 
 */
	private void viewResultsIdentity(HttpServletResponse response, 
	PrintWriter out, String fileName, String summaryViewType) 
	{

		String styleSheet=null; //the stylesheet to use
		String xmlDoc = null;
		servletDir = rb.getString("requestparams.servletDir");
		try 
		{
			if (summaryViewType.equals("vegPlot")) 
			{
				xmlDoc = servletDir+"test-summary.xml";
				styleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/transformMultiPlotIdentity.xsl";
				System.out.println("ViewData > xml document: '" + xmlDoc +"'" );
				System.out.println("ViewData > stylesheet name: '" + styleSheet +"'" );
				
				m.getTransformed(xmlDoc, styleSheet);
				StringWriter transformedData = m.outTransformedData;
				Vector contents = this.convertStringWriter(transformedData);
				for (int ii=0;ii< contents.size() ; ii++) 
				{
					out.println( (String)contents.elementAt(ii) );
				}
				
			}
			//let the user know that there is a problem with the request
			else 
			{
				out.println("ViewData.viewResultsSummary: unknown request for xsl: '"
					+summaryViewType+"'" );
			}
		} 
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * method to request the users default style sheet from the 
	 * profile database
	 */
	 private String getUserDefaultStyle(String userName)
	 {
			String htmlResults = null;
    	try
    	{
      	//create the parameter string to be passed to the DataRequestServlet -- 
				//this first part has the data request type stuff
      	StringBuffer sb = new StringBuffer();
      	sb.append("?action=userdefaultstyle&username="+userName);
			
      	//connect to the dataExchaneServlet
				String uri = "http://vegbank.nceas.ucsb.edu/framework/servlet/dataexchange"+sb.toString().trim();
				System.out.println("ViewData > sent to servlet: " + uri);
      	int port=80;
      	String requestType="POST";
      	htmlResults = gurl.requestURL(uri);
    	}
    	catch( Exception e )
    	{
     	 System.out.println("Exception:  " + e.getMessage());
			 e.printStackTrace();
    	}
			
    	return(htmlResults);
	 }
	
	
	
	/**
	 *  this method will take, as input a StringWriter object and 
	 *  return a Vector containing the contents of the StringWriter
	 * @param inputStringWriter -- the input StringWriter (in this cas probably
	 * from an xslt transform
	 * @return v -- a vector with each line segment as an element
	 */
	public Vector convertStringWriter(StringWriter inputStringWriter)
	{
		Vector v = new Vector();
		try 
		{
			// a string inwhich to convert the String Writer to
			String transformedString=null;  
			//do the conversion to the string
			transformedString  = inputStringWriter.toString().trim();  
			//the buffered reader
			BufferedReader br = new BufferedReader(new StringReader(transformedString)); //speed up the string parsing with a buffered reader

			//read each line
			String line; // temporary string to contain the lines from the transformedData 
			int lineCnt=0; //running line counter
			while ((line = br.readLine()) !=null ) 
			{
				v.addElement( line.trim() );
				lineCnt++;  //increment the line
			}

		} 
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(v);
	}

	}
