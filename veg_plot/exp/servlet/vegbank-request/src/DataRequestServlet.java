import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import xmlresource.utils.transformXML;
import databaseAccess.dbAccess;
import servlet.util.ServletUtility;


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
 *  '$Date: 2003-01-28 19:27:57 $'
 *  '$Revision: 1.28 $'
 * 
 */

public class DataRequestServlet extends HttpServlet 
{

	ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	public ServletUtility suy = new ServletUtility();

	public String queryOutput[] = new String[10000];  //the output from query
	public int queryOutputNum; //the number of output rows from the query

	private String taxonName = null;
	private String communityName = null;
	private String taxonOperation = null;
	private String commOperation = null;
	private String minElevation = null;
	private String maxElevation = null;
	private String state = null;
	private String surfGeo = null;
	private String multipleObs = null;
	private String compoundQuery = null;
	private String plotId = null;
	private String resultType = null;
	private String clientLog = null;
	private String remoteHost = null;
	private String requestDataType = null;
	private String servletDir = null; // the absolute path to the servlet
	private String userName = null; // this is the user of the servlet
	private String clientType = null;

	// community related attributes
	// commmunity name already defined above
	private String communityLevel = null;
	private  ServletUtility su = new ServletUtility();
	public dbAccess dba =new dbAccess();
	
	private transformXML transformer = new transformXML();
	private String genericForm =""; // this is the vb generic form to attache messages to
	private boolean queryCaching; // true if queries should be cached for the user
	
	// TODO: This need to taken out of here !!
	private static final String servletLib = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/";
	private static final String summaryViewStyleSheet = servletLib + "TransformPlot_Summary.xsl";
	private static final String comprehensiveViewStyleSheet = servletLib + "transformFullPlot.xsl";
	private String defaultPlotIdentityStyleSheet = "";
	private static final String communityQueryPage = "/forms/community-query.html";
	
	/**
	 * constructor method
	 */
	public DataRequestServlet()
	{
		try
		{
			System.out.println("init: DataRequestServlet");
			//show some of the instance variables
			servletDir = rb.getString("requestparams.servletDir");
			defaultPlotIdentityStyleSheet = rb.getString("defaultplotidentitystylesheet");
			this.genericForm = rb.getString("genericform");
			String s = rb.getString("querycaching");
			if ( s.equals("true") )
				this.queryCaching = true;
			else
				this.queryCaching = false;
			System.out.println("init: DataRequestServlet > user query caching: " + queryCaching);
			System.out.println("init: DataRequestServlet > servlet dir: " + servletDir);
			System.out.println("init: DataRequestServlet > default style sheet: " + this.defaultPlotIdentityStyleSheet);
			System.out.println("init: DataRequestServlet > generic html form: " + this.genericForm);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	

	/** Handle "POST" method requests from HTTP clients */
	public void doGet(HttpServletRequest request,
		HttpServletResponse response)
 	 throws IOException, ServletException 	
		{
			System.out.println("DataRequestServlet > GET");
			doPost(request, response);
		}


	/** Handle "POST" method requests from HTTP clients */ 
	public void doPost(HttpServletRequest request, 
		HttpServletResponse response)
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
				userName = getCookieValue(request);
				System.out.println("DataRequstServlet > current user: " +  userName   );
				
				//enumeration is needed for those
				//cases where there are multiple values 
				// for a given parameter
				Enumeration enum =request.getParameterNames();
				Hashtable params = new Hashtable();
				params = su.parameterHash(request);
			
				System.out.println("DataRequestServlet > IN PARAMETERS: "+params.toString() );
 
 				//how much data is being requested -- summary set, full data set etc
 				if ( (String)params.get("resultType") != null )
		 		{
 					resultType = (String)params.get("resultType");
 				}
 			//what type of data is being requested -- ie. plot, plantTaxon, vegCommunity
 			if ( (String)params.get("requestDataType") != null )
 			{
 				requestDataType = (String)params.get("requestDataType");
 			}
 			//get the variables privately held in the properties file
 			clientLog= (rb.getString("requestparams.clientLog"));
			servletDir = rb.getString("requestparams.servletDir");
 			remoteHost=request.getRemoteHost();
			System.out.println("DataRequstServlet > accessed by: "+remoteHost);
			
			// PLOT QUERY
			if ( requestDataType.trim().equals("vegPlot") )
			{
				System.out.println("DataRequstServlet > determining query type: " + determineQueryType(params) );
				if ( determineQueryType(params).equals("simple") )
				{
					handleSimpleQuery(params, out, response);
				}
				else if ( determineQueryType(params).equals("compound") )
				{
					handleCompoundQuery(params, out, response);
				}
				else if ( determineQueryType(params).equals("extended") )
				{
					handleExtendedQuery(enum, params, out, response, request);
				}
			}
			// PLANT TAXONOMY QUERY
			else if ( requestDataType.trim().equals("plantTaxon") )
			{
				System.out.println( "DataRequstServlet > query on the plant taxonomy database \n");
				handlePlantTaxonQuery( params, out, requestDataType, response );
			}
			// VEG COMMUNITY QUERY
			else if ( requestDataType.trim().equals("vegCommunity") )
			{
				System.out.println("DataRequstServlet > query on the vegetation community database \n"
					+" not yet implemented ");
				handleVegCommunityQuery( params, out, requestDataType, response);
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
	private void handleExtendedQuery(Enumeration enum,Hashtable param, PrintWriter out,	
	HttpServletResponse response, HttpServletRequest request) 
	{
		Hashtable extendedParamsHash = new Hashtable();
		try
		{
			//figure out what type of client that is accessing the servlet
			//the results that are returned depend on this parameter
			this.clientType = param.get("clientType").toString();
			String requestDataFormatType  = param.get("requestDataFormatType").toString();
			System.out.println("DataRequestServlet > resultType for extended query: " + requestDataFormatType);
			
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
				//compose the query
				composeExtendedQuery( extendedParamsHash ) ;
				//issue the extended query
				issueQuery("extendedQuery");
				
				
				//if the query had results respond accordingly to various clients
				if (queryOutputNum>=1)
				{
					handleQueryResultsResponse(clientType, requestDataFormatType, out, 
						response, param, resultType);
				}
				else 
				{ 
					String results = this.getEmptyResultSetMessage(param);
					out.println( results );
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
	private void handleQueryResultsResponse(String clientType, 
		String requestDataFormatType, PrintWriter out, 
		HttpServletResponse response, Hashtable params, String resultType)
	{
		try 
		{
			System.out.println("DataRequstServlet > result sets: "+queryOutputNum+" to: "+clientType);
			// THIS STATEMENT IS CARRIED OUT IF THE CLIENT IS AN APPLICATION
			if ( clientType.trim().equals("clientApplication")  || clientType.trim().equals("app") )
			{
				//does the client want xml or html?
				if ( requestDataFormatType.trim().equals("xml") )
				{
					System.out.println("DataRequestServlet > sending xml ");
					// GET THE FILE INTO A VECTOR AND SENT IT BACK
					//suy.fileVectorizer(servletDir+"summary.xml");
					Vector resultsVector = suy.fileToVector(servletDir+"summary.xml");
					System.out.println("DataRequestServlet > xml line num " + resultsVector.size() );
					for (int i=0; i<resultsVector.size(); i++) 
					{
						String s = (String)resultsVector.elementAt(i);
						//System.out.println("## " + s );
						out.println( s );
					}
					System.out.println("DataRequestServlet > done passing the xml " );
					out.close();
					
				}
				//assume that client wants html 
				else
				{
					System.out.println("DataRequestServlet > sending element summary ");
					//pass back the summary of parameters passed to the servlet
					returnQueryElementSummary(out, params, response);
					//the number of plots in the result set\
					out.println("Number of results returned: "+queryOutputNum+"<br><br>");
					//send to the client the html summary page
					//+"<form action=\"http://"+rb.getString("server")+""+rb.getString("servlet-path")+"viewData\" method=\"GET\"> \n"
					if (this.requestDataType.equals("vegPlot") )
					{
						response.sendRedirect("/harris/servlet/viewData?resultType=summary&summaryViewType=vegPlot");
					}
					else
					{
						System.out.println("DataRequstServlet > handeling non-plot result set ");
					}
				}
			}
			// the browser
			else
			{
				//pass back the summary of parameters passed to the servlet
				//returnQueryElementSummary(out, params, response);
				if (this.requestDataType.equals("vegPlot") )
				{
					// IF THE USER WANTS THE IDENTITY PASS THIS TO THEM
					// FROM HERE THEY SHOULD BE ABLE TO SEE MORE
					if ( resultType.equalsIgnoreCase("identity") )
					{
						Thread.sleep(1000);
						out.println( this.getPlotIdentityResults() );
					}
					// THE USER WANTS TO SEE THE COMPREHENSIVE VIEW 
					// OF A PLOT
					else if ( resultType.equalsIgnoreCase("full") )
					{
						out.println( this.getPlotView(comprehensiveViewStyleSheet) );
					}
					// THE USER WANTS TO SEE THE SUMMARY VIEW OF A PLOT
					else if ( resultType.equalsIgnoreCase("summary") )
					{
						out.println( this.getPlotView(summaryViewStyleSheet) );
					}
					else
					{
						out.println( this.getResultsSetOptions() );
					}
				}
				else
				{
					System.out.println("DataRequstServlet >  requestDataType: " + this.requestDataType);
					System.out.println("DataRequstServlet > handling non-plot result set ");
					
					// IF THEY ARE REQUESTING THE VEG COMMUNITY RETURN THE RESULT SET
					if ( requestDataType.equalsIgnoreCase("vegCommunity") )
					{
						String results = this.getCommunityResults();
						out.println( results );
					}
					// ELSE IF LOOKING FOR THE PLANT TAXA RESULTS SET
					else if ( requestDataType.equalsIgnoreCase("plantTaxon") )
					{
						String results = this.getPlantTaxaResults();
						out.println( results );
					}
					else
					{
						//this method below has been deprecated and there should be a mthod
						//written in this class to handle this
						su.getViewOption(this.requestDataType);
						out.println(su.outString);
					}
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * method that returns an html form that contains the plant taxa results
	 * by transforming the result-sets xml document with the appropriate stylesheet
	 * @see handleQueryResultsResponse -- the method that calls this one
	 */
	 private String getPlantTaxaResults()
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 String styleSheet=servletDir+"showPlantTaxaSummary.xsl";
			 String xmlDoc = servletDir+"summary.xml";
			 System.out.println("DataRequestServlet > xml document: '" + xmlDoc +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			 transformer.getTransformed(xmlDoc, styleSheet);
			 StringWriter transformedData = transformer.outTransformedData;
			 Vector contents = this.convertStringWriter(transformedData);
			 for (int ii=0;ii< contents.size() ; ii++) 
			 {
					String buf =  (String)contents.elementAt(ii) ;
					sb.append( buf + "\n");
			 }
		 }
		 catch( Exception e ) 
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		return( sb.toString() );
	 }
	
	
	
	/**
	 * method that returns an html form that contains the veg community results
	 * by transforming the result-sets xml document with the appropriate stylesheet
	 * @see handleQueryResultsResponse -- the method that calls this one
	 */
	 private String getCommunityResults()
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 String styleSheet=servletDir+"showCommunitySummary.xsl";
			 String xmlDoc = servletDir+"summary.xml";
			 System.out.println("DataRequestServlet > xml document: '" + xmlDoc +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			 transformer.getTransformed(xmlDoc, styleSheet);
			 StringWriter transformedData = transformer.outTransformedData;
			 Vector contents = this.convertStringWriter(transformedData);
			 for (int ii=0;ii< contents.size() ; ii++) 
			 {
					String buf =  (String)contents.elementAt(ii) ;
					sb.append( buf + "\n");
			 }
		 }
		 catch( Exception e ) 
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		return( sb.toString() );
	 }
	 
	 
	
	
	/**
	 * shows a full view of a plot by transforming the 
	 * xml document containing the plot by the stylesheet to display 
	 * the full plot view
	 * 
	 * @deprecated  TODO: get rid of.
	 */
	 private String getPlotFullView()
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 System.out.println("DataRequestServlet > getPlotFullView ");
			 String xmlDoc = servletDir + "atomicResult";
			 String styleSheet = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/transformFullPlot.xsl";
			 System.out.println("DataRequestServlet > xml document: '" + xmlDoc +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			 transformer.getTransformed(xmlDoc, styleSheet);
			 StringWriter transformedData = transformer.outTransformedData;
			 Vector contents = this.convertStringWriter(transformedData);
			 for (int ii=0;ii< contents.size(); ii++) 
			 {
					String buf =  (String)contents.elementAt(ii) ;
					sb.append( buf + "\n");
			 }
		 }
		 catch( Exception e ) 
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		return( sb.toString() );
	 }
	 
	 /**
	  *  Shows a view of the plot
	  * 
	  * @param  String  - the full filename of the stylesheet to use
	  */
	 private String getPlotView(String styleSheet)
	 {
		StringBuffer sb = new StringBuffer();
				 try
				 {
					 System.out.println("DataRequestServlet > getPlotView ");
					 String xmlDoc = servletDir + "atomicResult";
					 System.out.println("DataRequestServlet > xml document: '" + xmlDoc +"'" );
					 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
					 transformer.getTransformed(xmlDoc, styleSheet);
					 StringWriter transformedData = transformer.outTransformedData;
					 Vector contents = this.convertStringWriter(transformedData);
					 for (int ii=0;ii< contents.size(); ii++) 
					 {
							String buf =  (String)contents.elementAt(ii) ;
							sb.append( buf + "\n");
					 }
				 }
				 catch( Exception e ) 
				 {
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
				 }
				return( sb.toString() );
	 }

	 
	/**
	 * returns the identity page containing all the plots that 
	 * were in the result set ( or a range of plots )
	 */
	 private String getPlotIdentityResults()
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 String xmlDoc = servletDir + "identity.xml";
			 String styleSheet = this.defaultPlotIdentityStyleSheet;
			 System.out.println("DataRequestServlet > xml document: '" + xmlDoc +"'" );
			 System.out.println("DataRequestServlet > stylesheet name: '" + styleSheet +"'" );
			 transformer.getTransformed(xmlDoc, styleSheet);
			 StringWriter transformedData = transformer.outTransformedData;
			 Vector contents = this.convertStringWriter(transformedData);
			 for (int ii=0;ii< contents.size() ; ii++) 
			 {
					String buf =  (String)contents.elementAt(ii) ;
					sb.append( buf + "\n");
			 }
		 }
		 catch( Exception e ) 
		 {
			System.out.println("Exception: " + e.getMessage());
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
			System.out.println("Exception: " + e.getMessage());
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
				
			su.append("<form action=\"/framework/servlet/viewData\" method=\"GET\"> \n");
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
			System.out.println("Exception: " + e.getMessage());
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
	private void handleSimpleQuery(Hashtable params, PrintWriter out, 
	HttpServletResponse response) 
	{
		try
		{
		// Get all possible parameters from the hash 	
 		taxonName = (String)params.get("taxon");
 		communityName = (String)params.get("community");
 		taxonOperation = (String)params.get("taxonOperation");
 		commOperation = (String)params.get("commOperation");
 		minElevation = (String)params.get("minElevation");
 		maxElevation = (String)params.get("maxElevation");
 		state = (String)params.get("state");
 		surfGeo = (String)params.get("surfGeo");
 		multipleObs = (String)params.get("multipleObs");
 		compoundQuery = (String)params.get("compoundQuery");
 		plotId = (String)params.get("plotId");
		// may be 'full', 'summary', or 'identity'
 		resultType = (String)params.get("resultType");
 		requestDataType = (String)params.get("requestDataType");
		//the servlet directory
		servletDir = rb.getString("requestparams.servletDir");
		this.clientType = params.get("clientType").toString();
		String requestDataFormatType  = params.get("requestDataFormatType").toString();
		
		//attempt to recognize the request as a query for communities 
		if (requestDataType.trim().equals("community")) 
		{  
		//	out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
		//	+ "community information - not requesting plot info");
			composeCommunityQuery(params);
			issueQuery("simpleCommunityQuery");
		//	out.println("Number of communities returned: "+queryOutputNum+"<br><br>");
		}
		// Cheat here - to recognize the single plot query to return entire plot
		//20020117 testing the new plot writer and data translation 
		//modules so cheating here first
		else if (plotId != null && resultType.equals("full") ) 
		{
			System.out.println("DataRequestServlet > requesting full data set for plot: " + plotId );
			String outFile=servletDir+"atomicResult";
			
			//check to see how many plots are being requested -- if there are 
			//commas then there are multiple
			if ( plotId.indexOf(",") > 0 )
			{
				System.out.println("DataRequestServlet plot collection: " + plotId);
				Vector vec = new Vector();
				StringTokenizer tok = new StringTokenizer(plotId, ",");
				while ( tok.hasMoreTokens() )
				{
					String buf = tok.nextToken();
					vec.addElement(buf);
				}
				dba.writeMultipleVegBankPlot(vec, outFile);
				queryOutputNum = vec.size();
				System.out.println("DataRequestServlet > done writing "+ vec.size() +" plots to: " + outFile );
			}
			else
			{
				dba.writeSingleVegBankPlot(plotId, outFile);
				queryOutputNum = 1;
			}
		}
		// this is where the query element checking is done for the vegetation plots
		// the way that this is structured currently the user is not forced to choose a
		//single query element
		//look for a taxonName
		else if ( taxonName != null  && taxonName.length()>0 ) 
		{
			//out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			//+"containing plots with taxonName: "+taxonName+" <br>");
			composeQuery("taxonName", taxonName, resultType);
			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
 		//look for elevation
		else if ( minElevation != null  && minElevation.length() >0 ) 
		{  
			//out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			//+"containing plots with a minElevation of: "+minElevation+" <br>");
			composeQuery("elevationMin", minElevation, "elevationMax", maxElevation, resultType);
 			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for state
		else if ( state != null  && state.length()>0 ) 
		{  
			//out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			//+"containing plots with a state equal to: "+state+" <br>");
			composeQuery("state", state, resultType);
			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for the community name
		else if ( communityName != null  && communityName.length()>0 ) 
		{  
			//out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			//+"containing plots with a communityName equal to: "+communityName+" <br>");
			composeQuery("communityName", communityName, resultType);
 			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for the surface geology option
		else if ( surfGeo != null  && surfGeo.length()>0 ) 
		{  
			//out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			//+"containing plots with a surface geology like: "+surfGeo+" <br>");
			composeQuery("surfGeo", surfGeo, resultType);
			issueQuery("simpleQuery");
		}
		// if there are results returned to the servlet from the database in the form 
		// of a file returned then grab the summary viewer then let the user know
		if (queryOutputNum>=1) 
		{
			handleQueryResultsResponse(clientType, requestDataFormatType, out, 
			response, params, resultType);
		}
		else 
		{ 
			String results = this.getEmptyResultSetMessage(params);
			out.println( results );
		}
	}
	catch( Exception e)
	{
		System.out.println("Exception: " + e.getMessage() );
	}
	}

	
	/**
	 * method to handle queries that are meant to be issued against 
	 * the plantTaxonomy database
	 *
	 * @param params -- the prameters that are passed to the method
	 * @param out -- the printwriter back to the client
	 * @param requestDataType -- the data type requested by the client
	 *  	used to check that the query was diercted to the correct method
	 *		{plantTaxon}
	 * @param response - the response object linked to the client 
	 * 
	 */
	private void handlePlantTaxonQuery(Hashtable params, PrintWriter out, 
	String requestDataType, HttpServletResponse response)
	{
		try
		{
			//get the parameters needed for retuning the results
			String clientType = params.get("clientType").toString();
			String requestDataFormatType  = params.get("requestDataFormatType").toString();
			if (requestDataType.trim().equals("plantTaxon")) 
			{  
			//	out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
			//	+ "plant taxonomy information - not requesting plot info");
				composePlantTaxonomyQuery(params);
				issueQuery("simplePlantTaxonomyQuery");
				//out.println("Number of taxa returned: "+queryOutputNum+"<br><br>");
				//use the method that handles the response	
				if (queryOutputNum>=1) 
				{
					handleQueryResultsResponse(clientType, requestDataFormatType, out, 
					response, params, resultType);
				}
				else 
				{ 
					if ( ! clientType.equals("clientApplication") )
					{
						String results = this.getEmptyResultSetMessage(params);
						out.println( results );
					}
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
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
		 String contents ="";
		 String errors = "/tmp/errors.html";
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 // GET THE DATA REQUEST TYPE PARAMETER
			 String requestDataType  = params.get("requestDataType").toString();
			 Hashtable replaceHash = new Hashtable();
			 sb.append("<b> Sorry "+userName+" no results found for ");
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
			 su.filterTokenFile(genericForm, errors , replaceHash);
			 Thread.sleep(800);
			 contents = su.fileToString(errors);
		 }
		 catch(Exception e)
		 {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		 }
		 return(contents);
	 }
	
	
	/**
	 * method to handle queries that are meant to be issued against 
	 * the vegetation community database
	 *
	 *	@param params -- the prameters that are passed to the method
	 * 	@param out -- the printwriter back to the client
	 * 	@param requestDataType -- the data type requested by the client
	 *  	used to check that the query was diercted to the correct method
	 *		{plantTaxon}
	 * 	@param response - the response object linked to the client 
	 */
	private void handleVegCommunityQuery( Hashtable params, PrintWriter out, 
	String requestDataType, HttpServletResponse response)
	{
		try
		{
			// get the parameters needed for retuning the results
			String clientType = params.get("clientType").toString();
			String requestDataFormatType  = params.get("requestDataFormatType").toString();
			if (requestDataType.trim().equals("vegCommunity")) 
			{  
				System.out.println("DataRequestServlet > DataRequestServlet.handleSimpleQuery - requesting "
				+ "community information - not requesting plot info");
				composeCommunityQuery(params);
				issueQuery("simpleCommunityQuery");
				System.out.println("DataRequestServlet > Number of communities returned: "+queryOutputNum+"<br><br>");
				
				// use the method that handles the response if there are any results 
				// from the DB otherwise just return either a request for another
				// query or nothing
				if (queryOutputNum>=1) 
				{
					handleQueryResultsResponse(clientType, requestDataFormatType, out, 
					response, params, resultType);
				}
				else 
				{ 
					if ( ! clientType.equals("clientApplication") )
					{
						String results = this.getEmptyResultSetMessage(params);
						out.println( results );
					}
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	

/**
 * Handles compound queries where there are more than one query elements
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query
 * @param out - the output stream to the client
 * @param response - the response object linked to the client 
 */
	private void handleCompoundQuery(Hashtable params, PrintWriter out, 
		HttpServletResponse response) 
	{
		// Get all possible parameters from the hash
 		taxonName = (String)params.get("taxon");
 		communityName = (String)params.get("community");
 		taxonOperation = (String)params.get("taxonOperation");
 		commOperation = (String)params.get("commOperation");
 		minElevation = (String)params.get("minElevation");
 		maxElevation = (String)params.get("maxElevation");
 		state = (String)params.get("state");
 		surfGeo = (String)params.get("surfGeo");
 		multipleObs = (String)params.get("multipleObs");
 		compoundQuery = (String)params.get("compoundQuery");
 		plotId = (String)params.get("plotId");
 		resultType = (String)params.get("resultType");
		clientType = (String)params.get("clientType");
		// COMPOSE THE QUERY
 		this.composeQuery(taxonName, communityName, taxonOperation, commOperation, minElevation,
			maxElevation, state, surfGeo, multipleObs, compoundQuery);
 		
		// ISSUE THE QUERY
		this.issueQuery("compoundQuery");
		
		// if there are results returned to the servlet from the database in the form 
		// of a file returned then grab the summary viewer then let the user know
		if (queryOutputNum>=1) 
		{
			handleQueryResultsResponse(clientType, resultType, out, 
			response, params, resultType);
		}
 		// IF NO RESULTS THEN PROMPT THE USER FOR ANOTHER QUERY	
		else 
		{ 
			String results = this.getEmptyResultSetMessage(params);
			out.println( results );
		}
		
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
	private void composeCommunityQuery (Hashtable params) 
	{
 		try 
		{
 			PrintStream queryOutFile = new PrintStream(
 			new FileOutputStream(servletDir+"commQuery.xml", false)); 
			
			//grab the relevent query attributes out of the hash
			communityName = (String)params.get("communityName");
			communityLevel = (String)params.get("communityLevel");

			System.out.println("DataRequstServlet > printing from DataRequestServlet.composeCommunityQuery: "+
			"communityName: "+communityName);

 			//print the query instructions in the xml document
 			queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
 				"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
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
 				"<outFile>"+servletDir+"summary.xml</outFile> \n"+
 				"</dbQuery>"
 			);
 		}
		catch (Exception e) 
		{
			System.out.println("Exception: "+ e.getMessage());
			e.printStackTrace();
		}
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
	private void composePlantTaxonomyQuery (Hashtable params) 
	{
		StringBuffer query = new StringBuffer();
 		try 
		{
			System.out.println("DataRequstServlet > printing from composePlantTaxonomyQuery: "+
			" hash: " + params.toString() );
			
 			PrintStream queryOutFile = new PrintStream(
 			new FileOutputStream(servletDir+"taxonQuery.xml", false)); 
			
			//grab the relevent query attributes out of the hash
			String taxonName = (String)params.get("taxonName");
			String taxonNameType = (String)params.get("taxonNameType");
			String taxonLevel = (String)params.get("taxonLevel");
			String party = (String)params.get("party");
			String startDate = (String)params.get("startDate");
			String stopDate = (String)params.get("stopDate");
			String targetDate = (String)params.get("targetDate");
		

			System.out.println("DataRequstServlet > printing composePlantTaxonomyQuery: "+
			"taxonName: "+taxonName);
			
			
			// if the target date was passed instead of the start and stop compose a 
			// slightly different query 
			if ( targetDate != null )
			{
				query.append("<?xml version=\"1.0\"?> \n");       
 				query.append(	"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n");
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
				query.append(	"		<query> \n");
				query.append(	"			<queryElement>targetDate</queryElement> \n");
				query.append(	"			<elementString>"+targetDate+"</elementString> \n");
				query.append(	"		</query> \n");
				
				query.append(	"		<requestDataType>"+requestDataType+"</requestDataType> \n");
				query.append(	"		<resultType>"+resultType+"</resultType> \n");
				query.append(	"		<outFile>"+servletDir+"summary.xml</outFile> \n");
				query.append(	"</dbQuery>");
				queryOutFile.println( query.toString() );
			}
			else
			{
				//print the query instructions in the xml document
				queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
 					"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
					"	<dbQuery> \n"+
					"		<query> \n"+
					"			<queryElement>taxonName</queryElement> \n"+
					"			<elementString>"+taxonName+"</elementString> \n"+
					"		</query> \n"+
					"		<query> \n"+
					"			<queryElement>taxonNameType</queryElement> \n"+
					"			<elementString>"+taxonNameType+"</elementString> \n"+
					"		</query> \n"+
					"		<query> \n"+
					"			<queryElement>taxonLevel</queryElement> \n"+
					"			<elementString>"+taxonLevel+"</elementString> \n"+
					"		</query> \n"+
					"		<query> \n"+
					"			<queryElement>party</queryElement> \n"+
					"			<elementString>"+party+"</elementString> \n"+
					"		</query> \n"+
					"		<query> \n"+
					"			<queryElement>startDate</queryElement> \n"+
					"			<elementString>"+startDate+"</elementString> \n"+
					"		</query> \n"+
					"		<query> \n"+
					"			<queryElement>stopDate</queryElement> \n"+
					"			<elementString>"+stopDate+"</elementString> \n"+
					"		</query> \n"+
					"		<requestDataType>"+requestDataType+"</requestDataType> \n"+
					"		<resultType>"+resultType+"</resultType> \n"+
					"		<outFile>"+servletDir+"summary.xml</outFile> \n"+
					"</dbQuery>"
					);
			}
			queryOutFile.close();
 		}
		catch (Exception e) 
		{
			System.out.println("Exception : " +  e.getMessage());
			e.printStackTrace();
		}
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
		System.out.println("Exception: " 	+ e.getMessage());
		e.printStackTrace();
	}
}



/**
 * Write to the browser the query elements and their respective strings as well
 * as the some other information like the last date the user accessed the servlet 
 * the authentication status of the user
 * 
 * @param out - the output stream to the client
 * @param params - the Hashtable of parameters that should be included in the
 * 	response
 * @param response - the response object linked to the client 
 */
	public void returnQueryElementSummary (PrintWriter out, Hashtable params, 
  	HttpServletResponse response) 
	{
		try 
		{
			//this utility class is to store reused html code
			ServletUtility k =new ServletUtility();  
 			k.htmlStore();
 			out.println(k.outString); 
 
	 		Enumeration paramlist = params.keys();
 
 			//set up a table in which to print the query elements to the browser
 			out.println("<table summary=queryElements border=0 width=60%>"
 			+"<tr bgcolor=DFE5FA> <td>Query Element</td> <td>Element Value</td>");

 			while (paramlist.hasMoreElements()) 
 			{
				String queryElement = (String)paramlist.nextElement();
   			String elementValue  = (String)params.get(queryElement);
				//System.out.println("paramter passed to DataRequestServlet: "+queryElement);
				//print the query elements to the browser
				out.println("<tr bgcolor=dddddd> <td><b> " + queryElement +" </b></td> "
				+" <td><i> "+ elementValue +" </i></td> </tr>");
			}

			//close the table
			out.println("</table>");
			//next line is for debuging
			out.println("---end of DataRequestServlet.returnQueryElementSummary----<br></br>");
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	} //end method





/**
 *  Method to compose and print to a file an xml document that can be passed to
 *  the dbAccess class to perform the query and print the results to a file
 *  that is currently hard-wired as summary.xml
 *
 * @param plotId - internal database plot identification number
 * @param resultType - the type of data expected from the query full/summary
 */
	private void composeSinglePlotQuery (String plotId, 
		String resultType, String outFile) 
	{
		try 
		{
			String servletDir = rb.getString("requestparams.servletDir");
			PrintStream queryOutFile = new PrintStream(new FileOutputStream(
			servletDir+"query.xml", false)); 

			//print the query instructions in the xml document
			queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
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
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
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
			servletDir+"query.xml", false)); 
			
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
			sb.append("<outFile>"+servletDir+"identity.xml</outFile> \n");
			sb.append("</dbQuery>"+"\n");
			outFile.println( sb.toString() );
		}
		
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
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
	private void composeQuery(String queryElement, String elementString, String resultType) 
	{
		try 
		{
			//set up the output query file called query.xml	using append mode to build  
			PrintStream outFile  = new PrintStream(new FileOutputStream(
			servletDir+"query.xml", false)); 

			//print the query instructions in the xml document
			outFile.println("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
				"<dbQuery> \n"+
				"<query> \n"+
				"<queryElement>"+queryElement+"</queryElement> \n"+
				"<elementString>"+elementString+"</elementString> \n"+
				"</query> \n"+
				"<resultType>"+resultType+"</resultType> \n"+
				"<outFile>"+servletDir+"summary.xml</outFile> \n"+
				"</dbQuery>"
			);
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + 	e.getMessage());
			e.printStackTrace();
		}
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
	private void composeQuery (String minElement, String minValue, 
		String maxElement, String maxValue, String resultType) 
	{
		try 
		{
			//set up the output query file called query.xml	using append mode to build  
			PrintStream outFile  = new PrintStream(new FileOutputStream(
			servletDir+"query.xml", false)); 


			//print the query instructions in the xml document
			outFile.println("<?xml version=\"1.0\"?> \n"+       
				"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
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
				"<outFile>"+servletDir+"summary.xml</outFile> \n"+
				"</dbQuery>"
			);

		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
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
	private void composeQuery (String taxonName, String communityName, 
		String taxonOperation, String commOperation, 
		String minElevation, String maxElevation, String state, 
		String surfGeo, String multipleObs, String compoundQuery) 
	{
		try 
		{
			//set up the output query file called query.xml	using append mode to build  
 			PrintStream outFile  = new PrintStream(new FileOutputStream(servletDir+"query.xml", false)); 
			// SET THE RESULT TYPE TO IDENTITY SO THAT THE RESULTS ARE RETURNED
			// QUICKLY, THIS CAN BE IDENTITY OR SUMMARY
			resultType = "identity";
			
			//print the query instructions in the xml document
 			outFile.println("<?xml version=\"1.0\"?> \n"+       
			"<!DOCTYPE dbQuery SYSTEM \"plotQuery.dtd\"> \n"+     
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
			"<outFile>"+servletDir+"summary.xml</outFile> \n"+
			"</dbQuery>"
		);
		}
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}



/**
 *  Method to use dbAccess to issue the query to the database from the xml file
 *  created in the DataRequestServlet 
 *
 * @param queryType - the type of query to be sent through the dataAccess module
 * including simpleQuery (one attribute) and compoundQuery (multiple attributes) 
 */
	private void issueQuery(String queryType) 
	{
		System.out.println("DataRequestServlet > QUERY TYPE  " + queryType);
		if (queryType.equals("simpleQuery")) 
		{ 
			// IF IT IS A BROWSER QUERY THEN REGISTER THE DOCUMENT -- IF 
			// CACHING IS TURNED ON
			if (this.clientType.equals("browser") )
			{
				if (this.queryCaching == true )
				{
					System.out.println("DataRequestServlet > Caching the query doc");
					registerQueryDocument();
				}
				else
				{
					System.out.println("DataRequestServlet > Not caching the query doc");
				}
			}
			//else if it is an application (i.e., another servlet ) test the
			//new xml writer
			else if ( this.clientType.equals("app") )
			{
				
			}
			//call the plot access module
			dba.accessDatabase(servletDir+"query.xml", servletDir+"querySpec.xsl", "query");
			queryOutput=dba.queryOutput;
			queryOutputNum=dba.queryOutputNum;
		}
		else if (queryType.equals("compoundQuery")) 
		{
			//call the plot access module
			dba.accessDatabase(servletDir+"query.xml", 
			servletDir+"querySpec.xsl", "compoundQuery");	
			//grab the result set from the dbAccess class
			queryOutput=dba.queryOutput;
			queryOutputNum=dba.queryOutputNum;
		}
		else if (queryType.equals("extendedQuery")) 
		{
			//call the plot access module
			dba.accessDatabase(servletDir+"query.xml", 
			servletDir+"querySpec.xsl", "extendedQuery");	
			//grab the result set from the dbAccess class
			queryOutput=dba.queryOutput;
			queryOutputNum=dba.queryOutputNum;
		}
		else if (queryType.equals("simpleCommunityQuery")) 
		{ 
			//call the plot access module
			dba.accessDatabase(servletDir+"commQuery.xml", 
			servletDir+"querySpec.xsl", "simpleCommunityQuery");	
			//grab the result set from the dbAccess class
			queryOutput=dba.queryOutput;
			queryOutputNum=dba.queryOutputNum;
		}
			else if (queryType.equals("simplePlantTaxonomyQuery")) 
		{ 
			//call the plot access module
			dba.accessDatabase(servletDir+"taxonQuery.xml", 
			servletDir+"querySpec.xsl", "simplePlantTaxonomyQuery");	
			//grab the result set from the dbAccess class
			queryOutput=dba.queryOutput;
			queryOutputNum=dba.queryOutputNum;
		}
		
		else
		{
			System.out.println("DataRequestServlet >  could not handle query type: "
				+queryType);
		}
	}
	
	/**
	 * method that returns the name and value of a valid cookie
	 * REMOVE THIS METHOD -- THE SAME ONE EXISTS IN THE 'SERVLETUTILITY CLASS'
	 */
	 private String getCookieValue( HttpServletRequest req)
	 {
		String s = null;
	 //get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
	//	System.out.println("cookie dump: " + cookies.toString() );
	//	Cookie cook = cookies[0];
	//	String name =cook.getName();
	//	System.out.println("cookie name > : " + name );
		
		//determine if the requested page should be shown
    if (cookies.length >= 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				System.out.println("DataRequestServlet > cookie name: " + cookieName);
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
				s = cookieValue.trim(); 
			}
  	} 
		else 
		{
			System.out.println("DataRequestServlet > No valid cookies found");
		}
	return(s);
	}
	
	
		
	/**
	 * method that registers a query document with the datafile database
	 * this method should only be called from the 'issueQury' method and only
	 * if the 'queryCaching' attribute in the properties file is true
	 * @see composeQuery -- the method that calls this one
	 */
	 private void registerQueryDocument()
	 {
		 System.out.println("DataRequestServlet > registering the query document ###");
		 suy.uploadFileDataExcahgeServlet(servletDir+"query.xml", userName);
	 }
}
