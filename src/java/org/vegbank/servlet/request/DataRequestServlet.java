package org.vegbank.servlet.request;

/*
 *  '$RCSfile: DataRequestServlet.java,v $'
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-10-27 19:49:02 $'
 *  '$Revision: 1.15 $'
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.ServletUtility;

import org.vegbank.xmlresource.transformXML;


/**
 * Servlet to compose XML documents containg query information and then send the 
 * information to the dataAccess module, where that XML document will be
 * transformed and analyzed - a process resulting in a query being issued to the
 * plots database
 *
 * <p>Valid parameters are:<br><br>
 * 
 * REQUIRED PARAMETERS
 * @param queryType -- includes: simple, compound and extended
 * @param requestDataType -- includes: vegPlot, plantTaxon, vegCommunity
 * @param requestDataFormatType -- includes: html, xml, text
 * @param clientType -- includes: browser, clientApplication
 * @param resultType -- includes: summary, full
 * 
 * 
 * ADDITIONAL PARAMETERS
 * @param taxonName - name of a taxon occuring in a plot <br>
 * @param communityName - name of a vegetaion community <br>
 * @param minElevation - minimum elavation in an elevation range <br>
 * @param maxElevation - maximum elevation in an elevation range <br>
 * @param state - state in which a plot exists <br>
 * @param surfGeo - surface geology type <br>
 * @param multipleObs - multiple observations <br>
 * @param compoundQuery - descriptor of the query type, which may include simple
 * @param - where only one criteria is used or compound where multiple criteria
 * 		may be used the flag for siple is '0' and the flag for compound is '1'<br>
 * @param plotId - database plot identification number <br> 
 * @param resultFormatType - mak be either xml or html depending on the client tools<br>
 * 
 *	'$Author: farrell $'
 *  '$Date: 2003-10-27 19:49:02 $'
 *  '$Revision: 1.15 $'
 * 
 */

public class DataRequestServlet extends HttpServlet 
{

	static ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private  ServletUtility su = new ServletUtility();
	private transformXML transformer = new transformXML();
	
	private static final String GENERICFORM = rb.getString("genericform");
	private static final String DEFAULT_PLOT_STYLESHEET = rb.getString("defaultplotidentitystylesheet");
	private static final Boolean QUERYCACHING = new Boolean ( (String) rb.getObject("querycaching") );
	private static final String SERVLET_DIR = rb.getString("requestparams.servletDir");
	
	// TODO: This need to taken out of here !!
	private static final String summaryViewStyleSheet = SERVLET_DIR + "transformPlotSummary.xsl";
	private static final String comprehensiveViewStyleSheet = SERVLET_DIR + "transformFullPlot.xsl";
	private static final String returnXMLStyleSheet = SERVLET_DIR + "copy.xsl";
	private static final String communityQueryPage = "/vegbank/forms/community-query.html";
	
	
	/**
	 * constructor method
	 */
	public DataRequestServlet()
	{
		try
		{
			System.out.println("init: DataRequestServlet");
			//show some of the instance variablies
			System.out.println("init: DataRequestServlet > user query caching: " + QUERYCACHING);
			System.out.println("init: DataRequestServlet > servlet dir: " + SERVLET_DIR);
			System.out.println("init: DataRequestServlet > default style sheet: " + DEFAULT_PLOT_STYLESHEET);
			System.out.println("init: DataRequestServlet > generic html form: " + GENERICFORM);
		}
		catch (Exception e)
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the servlet by creating appropriate database connections
	 */
	public void init( ServletConfig config ) throws ServletException {
		try {
			super.init( config );
			System.out.println("DataRequestServlet Initialize");


			//initial DBConnection pool
			DBConnectionPool.getInstance();

		} catch ( ServletException ex ) {
			throw ex;
		} catch (SQLException e) {
			System.out.println("Error in DataRequestServlet.init: "+e.getMessage());
		}
	}

	/**
	 * Close all db connections from the pool
	 */
	public void destroy() {
			// Close all db connection
			System.out.println("Destroying DataRequestServlet");
			DBConnectionPool.release();
	}


	
	

	/** Handle "POST" method requests from HTTP clients */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
 	 throws IOException, ServletException 	
		{
			System.out.println("DataRequestServlet > GET");
			doPost(request, response);
		}


	/** Handle "POST" method requests from HTTP clients */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException  
		{
			System.out.println("DataRequestServlet > POST");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			try 
			{
				//handle the cookies -- the cookies should start with the user name
				//which will be used to register the query and results documents 
				//with the dataexcahnge servlet
				String userName =  this.su.getCookieValue(request);
				System.out.println("DataRequstServlet > current user: " +  userName   );
				
				//enumeration is needed for those
				//cases where there are multiple values 
				// for a given parameter
				Enumeration enum =request.getParameterNames();
				Hashtable params = new Hashtable();
				params = su.parameterHash(request);
			
				System.out.println("DataRequestServlet > IN PARAMETERS: "+params.toString() );
 
 				String resultType = null;
 				//how much data is being requested -- summary set, full data set etc
 				if ( (String)params.get("resultType") != null )
		 		{
 					resultType = (String)params.get("resultType");
 				}
 			//what type of data is being requested -- ie. plot, plantTaxon, vegCommunity
 			String requestDataType = null;
 			if ( (String)params.get("requestDataType") != null )
 			{
 				requestDataType = (String)params.get("requestDataType");
 			}
 			//get the variables privately held in the properties file
 			String clientLog= (rb.getString("requestparams.clientLog"));
 			String remoteHost=request.getRemoteHost();
			System.out.println("DataRequstServlet > accessed by: "+remoteHost);
			
			// PLOT QUERY
			if ( requestDataType.trim().equals("vegPlot") )
			{
				System.out.println("DataRequstServlet > determining query type: " + determineQueryType(params) );
				if ( determineQueryType(params).equals("simple") )
				{
					handleSimpleQuery(params, out, response, resultType, userName);
				}
				else if ( determineQueryType(params).equals("extended") )
				{
					handleExtendedQuery(enum, params, out, response, request, resultType, userName);
				}
			}
			//UNKNOWN QUERY
			else 
			{
				System.out.println("DataRequstServlet > unknown 'requestDataType' parameter "
					+" must be: vegPlot or plantTaxon or vegCommunity ");
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception :  "+ e.getMessage());
			e.printStackTrace();
		}
	}




/**
 * method to determine the type of query that should be handled
 * by the servlet -- types include:
 * 1] simple -- the result sets are summarized and returned to client
 * 2] compound -- same as simple but that there are multiple query elements
 * 3] extended -- the user gets the option to view or reduce selection
 */
	private String determineQueryType (Hashtable params) 
	{
		String queryType = null;
		if ( params.containsKey("queryType") == true )
		{
			queryType = (String)params.get("queryType");
		}
		else
		{
			System.out.println("DataRequstServlet > cannot process query: no type specified");
		}
		return(queryType);
	}

	
	/**
	 * method to handle extended queries hich are those queries 
	 * whose parameters are equal to criteria operator and value
	 * and are being requested by a more complex client than
	 * the simple html form known as plot query --
	 * @param enum -- an enumeration of all the paramters passed to the servlet
	 * @param param -- a hashtable with all the parameters passed to the servlet
	 * @param out --  the printwriter back to the client
	 * @param response -- the http response object
	 * @param request -- the http request object
	 */	 
	private void handleExtendedQuery(
		Enumeration enum,
		Hashtable param,
		PrintWriter out,
		HttpServletResponse response,
		HttpServletRequest request,
		String resultType,
		String userName)
	{
		Hashtable extendedParamsHash = new Hashtable();
		try
		{
			//figure out what type of client that is accessing the servlet
			//the results that are returned depend on this parameter
			String clientType = param.get("clientType").toString();
			String requestDataType  = param.get("requestDataType").toString();
			System.out.println("DataRequestServlet > requestDataType for extended query: " + requestDataType);
			
			//validate that there are the correct type and number
			//of parameters being passed to thie method
			if ( (param.containsKey("criteria") == false ) 
				|| ( param.containsKey("operator")== false )
				|| ( param.containsKey("value") == false ))
			{
				System.out.println("DataRequstServlet > did not get the correct parameters to handleExtendedQuery");	
			}
			//else compose and issue the query to the database access module
			else
			{
//				System.out.println("ELEMENTS TO BE PROCESSED: "+enum.toString() );
				while (enum.hasMoreElements()) 
				{
					//process the criteria data to make a hashtbale
					// with keys that equal 'criteria', 'operator'
					// and 'value'
					String name = (String)enum.nextElement();
//					System.out.println("ELEMENT : "+name  );
					if ( name.equals("criteria")  
						|| name.equals("operator") 
						|| name.equals("value"))
					{
						Vector  extendedValsVector = new Vector();
						String values[] = request.getParameterValues(name);
						for (int i=0; i<values.length; i++) 
						{
							extendedValsVector.addElement( ( values[i]) );
						}
						extendedParamsHash.put( name, extendedValsVector );
					}
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  "	+e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	/**
	 * this method is to be used for passing the results from the 
	 * servlet to the client that has accessed the client.  Depending 
	 * on the client type and the data format type that the client has 
	 * requested, either text, html, or xml will be passed back to the 
	 * client
	 * 
	 * @param out -- PrintWriter back to the client
	 * @param clientType -- the type of client {browser, 'clientApplication' or 'app' where
	 * app and clientApplication are synonomous}
	 * @param requestDataFormatType -- the data format type that the client has 
	 *		requested {tsxt, html, xml}
	 * @param response -- the servlet response to the client 
	 * @param params -- the parameters passed to the servlet
	 */
	private void handleQueryResultsResponse(
		String clientType,
		String requestDataType,
		PrintWriter out,
		HttpServletResponse response,
		Hashtable params,
		String resultType,
		QueryResult qr)
	{
		try
		{
			System.out.println(
				"DataRequestServlet > result sets: "
					+ qr.getResultsTotal()
					+ " to: "
					+ clientType);
			//pass back the summary of parameters passed to the servlet
			//returnQueryElementSummary(out, params, response);
			if (requestDataType.equals("vegPlot"))
			{
				// IF THE USER WANTS THE IDENTITY PASS THIS TO THEM
				// FROM HERE THEY SHOULD BE ABLE TO SEE MORE
				if (resultType.equalsIgnoreCase("identity"))
				{
					Thread.sleep(1000);
					out.println(this.getPlotIdentityResults( qr.getXMLString() ));
				}
				// THE USER WANTS TO SEE THE COMPREHENSIVE VIEW 
				// OF A PLOT
				else
					if (resultType.equalsIgnoreCase("full"))
					{
						out.println(this.getPlotView(comprehensiveViewStyleSheet,  qr.getXMLString() ));
					}
				// THE USER WANTS TO SEE THE SUMMARY VIEW OF A PLOT
				else
					if (resultType.equalsIgnoreCase("summary"))
					{
						out.println(this.getPlotView(summaryViewStyleSheet,  qr.getXMLString() ));
					}
					else if  ( resultType.equalsIgnoreCase("rawXML") )
					{
						out.println(this.getPlotView(returnXMLStyleSheet,  qr.getXMLString() ));
					}
					else
					{
						out.println(this.getResultsSetOptions());
					}
			}
			else
			{
				System.out.println(
					"DataRequstServlet >  requestDataType: " + requestDataType);
				System.out.println("DataRequstServlet > handling " + requestDataType  +" result set " );

				// IF THEY ARE REQUESTING THE VEG COMMUNITY RETURN THE RESULT SET
				if (requestDataType.equalsIgnoreCase("vegCommunity"))
				{
					String results = this.getCommunityResults( qr.getXMLString() );
					out.println(results);
				}
				// ELSE IF LOOKING FOR THE PLANT TAXA RESULTS SET
				else
					if (requestDataType.equalsIgnoreCase("plantTaxon"))
					{
						String results = this.getPlantTaxaResults(  qr.getXMLString() );
						out.println(results);
					}
					else
					{
						//this method below has been deprecated and there should be a mthod
						//written in this class to handle this
						String result = this.getViewOption(requestDataType);
						out.println(result);
					}
			}
		}
		catch (Exception e)
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to store html code that can be accessed based on the requests
	 * made by the user that will allow the user to access the summary viewer class
	 * called 'viewData' and depending on the input parameter passed will show the 
	 * user the summary of the selected plots, veg vommunities, or plant taxonmies.
	 *
	 * @param summaryViewType -- the type of summary to view, can include vegPlot, 
	 *	vegCommunity, or plantTaxa
	 * 
	 *
	 */
	public String getViewOption(String summaryViewType) 
	{
		System.out.println("ServletUtility > accessing the getViewOptionMethod");
		ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
		StringBuffer responseBuf=null; //use this string buffer instead
		String response=
		" "
		//+"<html> \n"
		//+"<body> \n"
		//+"<head> \n"
		+"<form action=\"/vegbank/servlet/viewData\" method=\"GET\"> \n"
		+"<input type=\"hidden\" name=\"resultType\" value=\"summary\" > \n"
		+"<input type=\"hidden\" name=\"summaryViewType\" value=\""+summaryViewType+"\"> \n"
		+"<input type=\"submit\" name=\"submitButton\" value=\"view data\" > \n"
		+"</form> \n"
		//+"</body> \n"
		+"</html> \n";
		return response;	
	}
	
	/**
	 * method that returns an html form that contains the plant taxa results
	 * by transforming the result-sets xml document with the appropriate stylesheet
	 * @see handleQueryResultsResponse -- the method that calls this one
	 */
	 private String getPlantTaxaResults(String xmlString)
	 {
		 StringBuffer sb = new StringBuffer();
		try
		{
			String styleSheet = SERVLET_DIR + "showPlantTaxaSummary.xsl";

			System.out.println(
				"DataRequestServlet > stylesheet name: '" + styleSheet + "'");

			sb.append( transformer.getTransformedFromString(xmlString, styleSheet) );
		}
		catch (Exception e)
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return( sb.toString() );
	 }
	
	
	
	/**
	 * method that returns an html form that contains the veg community results
	 * by transforming the result-sets xml document with the appropriate stylesheet
	 * @see handleQueryResultsResponse -- the method that calls this one
	 */
	 private String getCommunityResults( String xmlResult )
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 String styleSheet=SERVLET_DIR+"showCommunitySummary.xsl";

			 //System.out.println("DataRequestServlet > xml document: '" + xmlResult +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			
				sb.append( transformer.getTransformedFromString( xmlResult, styleSheet ) );
		 }
		 catch( Exception e ) 
		 {
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		return( sb.toString() );
	 }
	 

	 
	/**
	 *  Shows a view of the plot
	 * 
	 * @param  String  - the full filename of the stylesheet to use
	 */
	private String getPlotView(String styleSheet, String xmlResult)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			System.out.println("DataRequestServlet > getPlotView ");

			//System.out.println("DataRequestServlet > xml document: '" + xmlResult +"'" );
			//System.out.println("DataRequestServlet > stylesheet name: '"+styleSheet +"'");

			sb.append(transformer.getTransformedFromString(xmlResult, styleSheet));
		}
		catch (Exception e)
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return (sb.toString());
	}

	 
	/**
	 * returns the identity page containing all the plots that 
	 * were in the result set ( or a range of plots )
	 */
	 private String getPlotIdentityResults( String xmlResult)
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {

			 String styleSheet = DEFAULT_PLOT_STYLESHEET;
			 //System.out.println("DataRequestServlet > xml document: '" + xmlResult +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			 
			sb.append( transformer.getTransformedFromString( xmlResult, styleSheet ) );
		 }
		 catch( Exception e ) 
		 {
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		return( sb.toString() );
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
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(v);
	}

	
	/**
	 * method that presents the number of results returned from the VegBank 
	 * application layer and the options that the user has to interact 
	 * with those data.  These options should include: view plot id's 
	 * view plot summaries, view plot locations.  There should also be a message 
	 * that tells the user what is going to happen depending on the selection
	 * criteria and also provide the user with some tips -- this method
	 * was written to replace the ServletUtility getViewOption method that
	 * was deprecated 20020415
	 *
	 */
	private String getResultsSetOptions()
	{
		StringBuffer su = new StringBuffer();
		try
		{
			su.append("<html> \n");
			su.append("<head> \n");
			su.append("<title> \n");
			su.append("VEGBANK PLOT SELECTION WIZARD \n");
			su.append("</title> \n");
			su.append("<link REL=STYLESHEET HREF=\"/vegbank/includes/default.css\" TYPE=\"text/css\"> \n");
			su.append(" </head>\n");
			su.append("<table border=\"0\" width=\"85%\" bgcolor=\"#DFE5FA\"> \n");
			su.append("<tr> \n");
			su.append("	<td  align=left valign=top  width=\"5%\" > \n");
			su.append("	<font face=\"Helvetica,Arial,Verdana\" size=\"3\" color=\"23238E\"> \n");
			su.append("	<b>Options:</b> \n");
			su.append("	</td> \n");
			su.append("	</font> \n");
			su.append("</tr> \n");
	
			su.append("<tr> \n");
			su.append("	<td align=\"center\" width=\"5%\"> \n");
			su.append("		<img src=\"/vegbank/images/icon_cat31.gif\" alt=\"exclamation\" width=\"15\" height=\"15\" > \n");
			su.append("	</td> \n");
			su.append("	<td class=\"item\"> \n");
			su.append("	Please choose from the selections below.  The options to <br>\n");
			su.append("	view plot identification tags, or to view the summaries of the <br> \n");
			su.append("	plots will allow you to download the plot data in a variety of formats \n");
			su.append("	</td> \n");
			su.append("</tr> \n");
			su.append("</table> \n");
			
			su.append("<br>");
			su.append("<br>");
				
			su.append("<form action=\"/vegbank/servlet/viewData\" method=\"GET\"> \n");
			// the options to view summaries or identifiers -- either way the user
			// can get to the download option
			su.append("<input type=\"radio\" name=\"resultType\" value=\"identity\" checked> View Plot Id's \n");
			su.append("<br>");
			su.append("<input type=\"radio\" name=\"resultType\" value=\"summary\"> View Plot Summaries \n");
			su.append("<br>");
			su.append("<input type=\"hidden\" name=\"summaryViewType\" value=\"vegPlot\"> \n");
			su.append("<br>");
			su.append("<br>");
			su.append("<input type=\"submit\" name=\"submitButton\" value=\"Continue\" > \n");
			su.append("</form> \n");
			su.append("</html> \n");
		}
		catch( Exception e ) 
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(su.toString() );
	}
	
	
/**
 * Handles simple queries which are those queries where there is only one 
 * queryElemnt such as taxon or community name this is in contrast to a compound
 * query which has multiple query elements and is handled by the
 * 'handleCompoundQuery' method
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 		query
 * @param out - the output stream to the client
 * @param response - the response object linked to the client 
 */
	private void handleSimpleQuery(
		Hashtable params,
		PrintWriter out,
		HttpServletResponse response,
		String resultType,
		String userName)
	{

		QueryResult qr = new QueryResult();

		try
		{
			// Get all possible parameters from the hash 	
			String taxonName = (String) params.get("taxon");
			String communityName = (String) params.get("community");
			String taxonOperation = (String) params.get("taxonOperation");
			String commOperation = (String) params.get("commOperation");
			String minElevation = (String) params.get("minElevation");
			String maxElevation = (String) params.get("maxElevation");
			String state = (String) params.get("state");
			String surfGeo = (String) params.get("surfGeo");
			String multipleObs = (String) params.get("multipleObs");
			String compoundQuery = (String) params.get("compoundQuery");
			String plotId = (String) params.get("plotId");
			// may be 'full', 'summary', or 'identity'
			//String resultType = (String)params.get("resultType");
			String requestDataType = (String) params.get("requestDataType");
			//the servlet directory
			String clientType = params.get("clientType").toString();
			String requestDataFormatType =
				params.get("requestDataFormatType").toString();

	
			// Cheat here - to recognize the single plot query to return entire plot
			//20020117 testing the new plot writer and data translation 
			//modules so cheating here first
			if ( plotId != null && ( resultType.equals("full") || resultType.equals("summary") || resultType.equals("rawXML") ) )
			{
				System.out.println("About to run plot query");
				new FullPlotQuery().execute(qr, plotId);
			}
			else
			{
				System.out.println("DataRequestServlet > This request is not understood");
			}
			// if there are results returned to the servlet from the database in the form 
			// of a file returned then grab the summary viewer then let the user know
			if (qr.getResultsTotal() >= 1)
			{
				handleQueryResultsResponse(
					clientType,
					requestDataType,
					out,
					response,
					params,
					resultType,
					qr);
			}
			else
			{
				String results = this.getEmptyResultSetMessage(params);
				out.println(results);
			}
		}
		catch (Exception e)
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/** 
	 * This method retuns an html form containing a message that the 
	 * issued query did not return any results 
	 * @param params -- the parameters passed to the servlet
	 * @return contents -- the contents of the empty result set page
	 *
	 */
	 private String getEmptyResultSetMessage(Hashtable params)
	 {
		 StringBuffer sb = new StringBuffer();
			StringWriter output = new StringWriter();
		 try
		 {
			 // GET THE DATA REQUEST TYPE PARAMETER
			 String requestDataType  = params.get("requestDataType").toString();
			 Hashtable replaceHash = new Hashtable();
			 sb.append("<b> Sorry, no results found for ");
			 sb.append(requestDataType);
			 sb.append(" query: <br> <br> </b>\n ");
			 
			 
			 // GET THE REST OF THE PARAMETERS
			 Enumeration e = params.keys();
			 
			 while (e.hasMoreElements()) 
			 {
				String name = (String)e.nextElement();
				System.out.println("getting : " + name );
				String value = (String)params.get(name);
				sb.append(name+ ":   "+value+" <br>  \n");
			 }
			
			 replaceHash.put("messages", sb.toString() );
			 
			 su.filterTokenFile(GENERICFORM, output , replaceHash);
			 Thread.sleep(800);
		 }
		 catch(Exception e)
		 {
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		 return( output.toString() );
	 }
	 
/**
 * This method takes the hashtable that stores the input parameters passed to
 * the servlet -- which in this case refer to the community that the user wants
 * data about and builds the query xml file that is passed then to the database
 * access module
 *
 * @param params -- all the params passed to the servlet that contains
 * attributes used for, in this care, query the community database
 * 
 */
	private String composeCommunityQuery (Hashtable params, String resultType, String requestDataType) 
	{
		StringBuffer output = new StringBuffer();
 		try 
		{
			
			//grab the relevent query attributes out of the hash
			String communityName = (String)params.get("communityName");
			String communityLevel = (String)params.get("communityLevel");

			System.out.println("DataRequstServlet > printing from DataRequestServlet.composeCommunityQuery: "+
			"communityName: "+communityName);

			output.append("<?xml version=\"1.0\"?> \n"+       
 				"<!DOCTYPE dbQuery> \n"+     
 				"<dbQuery> \n"+
 				"<query> \n"+
 				"<queryElement>communityName</queryElement> \n"+
 				"<elementString>"+communityName+"</elementString> \n"+
 				"</query> \n"+
 				"<query> \n"+
 				"<queryElement>communityLevel</queryElement> \n"+
 				"<elementString>"+communityLevel+"</elementString> \n"+
 				"</query> \n"+
 				"<requestDataType>"+requestDataType+"</requestDataType> \n"+
 				"<resultType>"+resultType+"</resultType> \n"+
 				"<outFile>"+SERVLET_DIR+"summary.xml</outFile> \n"+
 				"</dbQuery>"
 			);
 		}
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: "+ e.getMessage());
			e.printStackTrace();
		}
		return output.toString();
	}





/**
 * This method takes the hashtable that stores the input parameters passed to
 * the servlet -- which in this case refer to the community that the user wants
 * data about and builds the query xml file that is passed then to the database
 * access module
 *
 * @param params -- all the params passed to the servlet that contains
 * attributes used for, in this care, query the community database
 * 
 */
	private String composePlantTaxonomyQuery (Hashtable params, String resultType, String requestDataType) 
	{
		StringBuffer query = new StringBuffer();
 		try 
		{
			System.out.println("DataRequstServlet > printing from composePlantTaxonomyQuery: "+
			" hash: " + params.toString() );
			
			//grab the relevent query attributes out of the hash
			String taxonName = (String)params.get("taxonName");
			
			// Special handling here
			String taxonNameType = (String)params.get("taxonNameType");
			String taxonLevel = (String)params.get("taxonLevel");
			
			// Check for nulls .... Query does't work with nulls for these values 
			//	because the null gets converted into a string bellow and the query
			// uses this "null" for its search 
			//	FIXME: do this in a better way ... hack
			if (taxonNameType == null)
			{
				taxonNameType = "";
			}
			if (taxonLevel == null )
			{
				taxonLevel = "";
			}
			
			String party = (String)params.get("party");
			String startDate = (String)params.get("startDate");
			String stopDate = (String)params.get("stopDate");
			String targetDate = (String)params.get("targetDate");
	
			
			System.out.println(
				"DataRequstServlet > printing composePlantTaxonomyQuery: "+
				"taxonName: "+taxonName +
				" taxonNameType: " + taxonNameType +
				" taxonLevel: " + taxonLevel
			);
			
			
			
			query.append("<?xml version=\"1.0\"?> \n");       
			query.append(	"<!DOCTYPE dbQuery> \n");
			query.append(	"	<dbQuery> \n");
			query.append(	"		<query> \n");
			query.append(	"			<queryElement>taxonName</queryElement> \n");
			query.append(	"			<elementString>"+taxonName+"</elementString> \n");
			query.append(	"		</query> \n");
			query.append(	"		<query> \n");
			query.append(	"			<queryElement>taxonNameType</queryElement> \n");
			query.append(	"			<elementString>"+taxonNameType+"</elementString> \n");
			query.append(	"		</query> \n");
			query.append(	"		<query> \n");
			query.append(	"			<queryElement>taxonLevel</queryElement> \n");
			query.append(	"			<elementString>"+taxonLevel+"</elementString> \n");
			query.append(	"		</query> \n");
			query.append(	"		<query> \n");
			query.append(	"			<queryElement>party</queryElement> \n");
			query.append(	"			<elementString>"+party+"</elementString> \n");
			query.append(	"		</query> \n");

			
			// if the target date was passed instead of the start and stop compose a 
			// slightly different query 
			if ( targetDate != null )
			{
				query.append(	"		<query> \n");
				query.append(	"			<queryElement>targetDate</queryElement> \n");
				query.append(	"			<elementString>"+targetDate+"</elementString> \n");
				query.append(	"		</query> \n");
				

			}
			else
			{
				//print the query instructions in the xml document
				query.append(	"		<query> \n");
				query.append(	"			<queryElement>startDate</queryElement> \n");
				query.append(	"			<elementString>"+startDate+"</elementString> \n");
				query.append(	"		</query> \n");
				query.append(	"		<query> \n");
				query.append(	"			<queryElement>stopDate</queryElement> \n");
				query.append(	"			<elementString>"+stopDate+"</elementString> \n");
				query.append(	"		</query> \n");				
			}
			
			query.append(	"		<requestDataType>"+requestDataType+"</requestDataType> \n");
			query.append(	"		<resultType>"+resultType+"</resultType> \n");
			query.append(	"		<outFile>"+SERVLET_DIR+"summary.xml</outFile> \n");
			query.append(	"</dbQuery>");
 		}
		catch (Exception e) 
		{
			System.out.println("Exception : " +  e.getMessage());
			e.printStackTrace();
		}
		return query.toString();
	}




/**
 * logs the use of this servlet, the date and the remote host of the client in 
 * log file 
 *
 * @param clientLog - the name of the client log file
 * @param remoteHost - the name of the remote host using this client
 * 
 */
private void updateClientLog (String clientLog, String remoteHost) 
{ 
 try 
 {
 		PrintStream clientLogger = new PrintStream(new FileOutputStream(clientLog, true));
 		SimpleDateFormat formatter = new SimpleDateFormat ("yy-MM-dd HH:mm:ss");
 		java.util.Date localtime = new java.util.Date();
 		String dateString = formatter.format(localtime);
 		//Date date = new Date();
		clientLogger.println(remoteHost+", "+dateString);
	}
	catch (Exception e) 
	{
		System.out.println("DataRequestServlet Exception: " 	+ e.getMessage());
		e.printStackTrace();
	}
}

/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml
 *
 * @param plotId - internal database plot identification number
 * @param resultType - the type of data expected from the query full/summary
 */
	private String composeSinglePlotQuery (String plotId, String resultType, String outFile) 
	{
		StringBuffer query = new StringBuffer();
		try 
		{

			//print the query instructions in the xml document
			query.append("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery> \n"+     
				"<dbQuery> \n"+
				"<query> \n"+
				"	<queryElement>plotId</queryElement> \n"+
				"	<elementString>"+plotId+"</elementString> \n"+
				"</query> \n"+
				"<resultType>"+resultType+"</resultType> \n"+
				"<outFile>"+outFile+"</outFile> \n"+
				"</dbQuery>"
			);
		}
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return query.toString();
	}

	
/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform a query on a vegetation database this 
 * 	particular method is for extended queries, those queries comprised of
 * 	a series of criteria, values and operators;
 *
 * @param
 * @param
 */
	private String composeExtendedQuery ( Hashtable extendedParamsHash ) 
	{
		try 
		{
			//set up the output query file called query.xml	using append mode to build  
			PrintStream outFile  = new PrintStream(new FileOutputStream(
			SERVLET_DIR+"query.xml", false)); 
			
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\"?> \n");
			sb.append("<dbQuery> \n");
			sb.append(" <extendedQuery> \n");
			//we know that the following is true
			if (extendedParamsHash.containsKey("criteria")==true)
			{
				//then must have all these
				Vector criteriaVec = (Vector)extendedParamsHash.get("criteria");
				Vector operatorVec = (Vector)extendedParamsHash.get("operator");
				Vector valueVec = (Vector)extendedParamsHash.get("value");
				for (int i=0; i<criteriaVec.size(); i++) 
				{
					sb.append("  <queryTriple> \n");
					sb.append("  	<queryCriteria>"+criteriaVec.elementAt(i)+"</queryCriteria> \n");
					sb.append("  	<queryOperator>"+operatorVec.elementAt(i)+"</queryOperator> \n");
					sb.append("  	<queryValue>"+valueVec.elementAt(i)+"</queryValue> \n");
					sb.append("  </queryTriple> \n");
				}
			}
			else 
			{
				//print the error found
				System.out.println("DataRequstServlet > cannot compose extended query");
			}
			sb.append("</extendedQuery>"+"\n");
			sb.append("<requestDataType>identity</requestDataType> \n");
			sb.append("<resultType>identity</resultType> \n");
			sb.append("<outFile>"+SERVLET_DIR+"identity.xml</outFile> \n");
			sb.append("</dbQuery>"+"\n");
			outFile.println( sb.toString() );
		}
		
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return("composed extended query");
	}



/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml
 *
 * @param queryElement - name of the element being used to query the database, 
 *	such as taxonName, plotId etc.
 * @param elementString - value of the queryElement
 * @param resultType -- the result type of desired may be: full, identity, or summary
 */
	private String composeQuery(String queryElement, String elementString, String resultType) 
	{
		StringBuffer query = new StringBuffer();
		try 
		{

			//print the query instructions in the xml document
			query.append("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery> \n"+     
				"<dbQuery> \n"+
				"<query> \n"+
				"<queryElement>"+queryElement+"</queryElement> \n"+
				"<elementString>"+elementString+"</elementString> \n"+
				"</query> \n"+
				"<resultType>"+resultType+"</resultType> \n"+
				"<outFile>"+SERVLET_DIR+"summary.xml</outFile> \n"+
				"</dbQuery>"
			);
		}
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: " + 	e.getMessage());
			e.printStackTrace();
		}
		return query.toString();
	}




/**
 * Method to compose and print to a file an xml document that can be passed to
 * the dbAccess class to perform the query and print the results to a file
 * that is currently hard-wired as summary.xml.  Thinking of queries where
 * there a minimum and maximum value like elevation or tree stem diamtere
 *
 * @param minElement
 * @param minValue - value of the queryElement
 * @param maxElement
 * @param maxValue - value of the queryElement
 * @param resultType -- the result type of desired may be: full, identity, or summary
 */
	private String composeQuery (
		String minElement, 
		String minValue, 
		String maxElement, 
		String maxValue, 
		String resultType) 
	{
		StringBuffer query = new StringBuffer();
		try 
		{

			//print the query instructions in the xml document
			query.append("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery> \n"+     
				"<dbQuery> \n"+
				"<query> \n"+
				"<queryElement>"+minElement+"</queryElement> \n"+
				"<elementString>"+minValue+"</elementString> \n"+
				"</query> \n"+
				"<query> \n"+
				"<queryElement>"+maxElement+"</queryElement> \n"+
				"<elementString>"+maxValue+"</elementString> \n"+
				"</query> \n"+
				"<resultType>"+resultType+"</resultType> \n"+
				"<outFile>"+SERVLET_DIR+"summary.xml</outFile> \n"+
				"</dbQuery>"
			);

		}
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return query.toString();
	}



/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml  - an overloaded version of the 
 *  above.
 *
 * @param taxonName - taxon name
 * @param communityName - community name
 * @param taxonOperation - taxon operation
 * @param commOperation - community operation
 * @param minElevation - minimum elevation
 * @param maxElevation - maximum elevation
 * @param state - state
 * @param surfGeo - surface geology type
 * @param multipleObs - multiple Observations
 * @param compoundQuery - compound Query
 */
	private String composeQuery(
		String taxonName,
		String communityName,
		String taxonOperation,
		String commOperation,
		String minElevation,
		String maxElevation,
		String state,
		String surfGeo,
		String multipleObs,
		String compoundQuery,
		String resultType)
	{
		StringBuffer query = new StringBuffer();
		try 
		{
			// SET THE RESULT TYPE TO IDENTITY SO THAT THE RESULTS ARE RETURNED
			// QUICKLY, THIS CAN BE IDENTITY OR SUMMARY
			resultType = "identity";
			
			//print the query instructions in the xml document
 			query.append("<?xml version=\"1.0\"?> \n"+       
			"<!DOCTYPE dbQuery> \n"+     
			"<dbQuery> \n"+
			"<query> \n"+
			"<queryElement>taxonName</queryElement> \n"+
			"<elementString>"+taxonName+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>state</queryElement> \n"+
			"<elementString>"+state+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>elevationMin</queryElement> \n"+
			"<elementString>"+minElevation+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>elevationMax</queryElement> \n"+
			"<elementString>"+maxElevation+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>surfGeo</queryElement> \n"+
			"<elementString>"+surfGeo+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>multipleObs</queryElement> \n"+
			"<elementString>"+multipleObs+"</elementString> \n"+
			"</query> \n"+
			"<query> \n"+
			"<queryElement>community</queryElement> \n"+
			"<elementString>"+communityName+"</elementString> \n"+
			"</query> \n"+
			"<resultType>"+resultType+"</resultType> \n"+
			"<outFile>"+SERVLET_DIR+"summary.xml</outFile> \n"+
			"</dbQuery>"
		);
		}
		catch (Exception e) 
		{
			System.out.println("DataRequestServlet Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return query.toString();
	}
}
