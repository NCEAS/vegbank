package org.vegbank.servlet.request;

/*
 *  '$RCSfile: viewData.java,v $'
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-05-07 01:37:27 $'
 *  '$Revision: 1.3 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.servlet.util.GetURL;
import xmlresource.utils.transformXML;



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

	private static ResourceBundle  rb = ResourceBundle.getBundle("plotQuery");

	//private String downLoadAction=null;
	//private String summaryViewType=null;
	//private String resultType=null;
	private static final String servletDir=  rb.getString("requestparams.servletDir");
	private static final String servletPath = rb.getString("servlet-path");
	
	
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		//grab all the input parameters
		String downLoadAction=request.getParameter("downLoadAction");
		String summaryViewType=request.getParameter("summaryViewType");
		String resultType=request.getParameter("resultType");
		String userEmail = this.getCookieValue(request);
		
		//print the input to the system
		System.out.println("ViewData > downLoadAction: " + downLoadAction);
		System.out.println("ViewData > summaryViewType: " + summaryViewType );
		System.out.println("ViewData > user: " + userEmail);
		System.out.println("ViewData > resultType: " + resultType );

		//handle a download request
		if (downLoadAction != null)
		{
			this.initPlotDownload(request);
 			response.sendRedirect("/forms/resultset-download.html");	
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
				
				StringWriter transformedData = new StringWriter();
				m.getTransformed(xmlDoc, styleSheet, transformedData);
				
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
		try 
		{
			if (summaryViewType.equals("vegPlot")) 
			{
				xmlDoc = servletDir+"test-summary.xml";
				styleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/transformMultiPlotIdentity.xsl";
				System.out.println("ViewData > xml document: '" + xmlDoc +"'" );
				System.out.println("ViewData > stylesheet name: '" + styleSheet +"'" );
				
				StringWriter transformedData = new StringWriter();
				m.getTransformed(xmlDoc, styleSheet, transformedData);

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
				String uri = "/vegbank/servlet/dataexchange"+sb.toString().trim();
				System.out.println("ViewData > sent to servlet: " + uri);
      	int port=80;
      	String requestType="POST";
      	htmlResults = GetURL.requestURL(uri);
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
