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
 * @version Feb. 09 2001
 * @author John Harris
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
	public ServletUtility su = new ServletUtility();
	public dbAccess dba =new dbAccess();
	
	
	/**
	 * constructor method
	 */
	public DataRequestServlet()
	{
		System.out.println("init: DataRequestServlet");
		//sho some of the instance variables
		servletDir = rb.getString("requestparams.servletDir");
		System.out.println("init: DataRequestServlet > servlet dir: " + servletDir);	
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
			//log the use of the servlet by the client
			
////			updateClientLog(clientLog, remoteHost);
		
			//return to the browser a summary of the request being made of the servlet this
			//method also adds some tags that are required many browsers
//			returnQueryElemenySummary (out, params, response);
			
			//figure out what of data request type the client is requesting?
			if ( requestDataType.trim().equals("vegPlot") )
			{
				System.out.println("DataRequstServlet > determining query type: " + determineQueryType(params) );
				if ( determineQueryType(params).equals("simple") )
				{
					handleSimpleQuery (params, out, response);
				}
				else if ( determineQueryType(params).equals("compound") )
				{
					handleCompoundQuery (params, out, response);
				}
				else if ( determineQueryType(params).equals("extended") )
				{
				handleExtendedQuery(enum, params, out, response, request);
				}
			}
			else if ( requestDataType.trim().equals("plantTaxon") )
			{
				System.out.println( "DataRequstServlet > query on the plant taxonomy database \n");
				handlePlantTaxonQuery( params, out, requestDataType, response );
			}
			else if ( requestDataType.trim().equals("vegCommunity") )
			{
				System.out.println("DataRequstServlet > query on the vegetation community database \n"
					+" not yet implemented ");
				handleVegCommunityQuery( params, out, requestDataType, response);
			}
			else 
			{
				System.out.println("DataRequstServlet > unknown 'requestDataType' parameter "
					+" must be: vegPlot or plantTaxon or vegCommunity ");
				
			}
			
			
		}//end try
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
	 * @param
	 * @param
	 */	 
	private void handleExtendedQuery(Enumeration enum,
	Hashtable param, PrintWriter out,	HttpServletResponse response, 
	HttpServletRequest request) 
	{
		Hashtable extendedParamsHash = new Hashtable();
		try
		{
			//figure out what type of client that is accessing the servlet
			//the results that are returned depend on this parameter
			this.clientType = param.get("clientType").toString();
			String requestDataFormatType  = param.get("requestDataFormatType").toString();
			
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
						response, param);
				}
				else 
				{ 
					out.println("<br> <b> Please try another query </b> <br>"); 
					out.println("<a href = \"http://vegbank.nceas.ucsb.edu/forms/plot-query.html\">"
					+"return to query page</a><b>&#183;</b>");
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
	 * @param clientType -- the type of client {browser, clientApplication}
	 * @param requestDataFormatType -- the data format type that the client has 
	 *		requested {tsxt, html, xml}
	 * @param response -- the servlet response to the client 
	 * @param params -- the parameters passed to the servlet
	 */
	private void handleQueryResultsResponse(String clientType, 
		String requestDataFormatType, PrintWriter out, 
		HttpServletResponse response, Hashtable params)
	{
		try 
		{
			System.out.println("DataRequstServlet > result sets: "+queryOutputNum+" to: "+clientType);
			if ( clientType.trim().equals("clientApplication") )
			{
				//does the client want xml or html?
				if ( requestDataFormatType.trim().equals("xml") )
				{
					//out.println("sending xml");
					suy.fileVectorizer(servletDir+"summary.xml");
					Vector resultsVector = suy.outVector;
					for (int i=0; i<resultsVector.size(); i++) 
					{
						out.println( resultsVector.elementAt(i) );
					}
					//suy.fileVectorizer(servletDir+"summary.xml");
					//out.println(suy.outVector);
				}
				else //assume that client wants html 
				{
					//pass back the summary of parameters passed to the servlet
					returnQueryElemenySummary (out, params, response);
					//the number of plots in the result set\
					out.println("Number of results returned: "+queryOutputNum+"<br><br>");
					//send to the client the html summary page
					//+"<form action=\"http://"+rb.getString("server")+""+rb.getString("servlet-path")+"viewData\" method=\"GET\"> \n"
					response.sendRedirect("/harris/servlet/viewData?resultType=summary&summaryViewType=vegPlot");
				}
			}
			else  //the browser
			{
				//pass back the summary of parameters passed to the servlet
				returnQueryElemenySummary(out, params, response);
				//the number of plots in the result set\
				out.println("Number of results returned: "+queryOutputNum+"<br><br>");
				//this passes the browser an html form to view the summary data
 				//ServletUtility l =new ServletUtility();  
 				su.getViewOption(requestDataType);
 				out.println(su.outString);
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
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
	private void handleSimpleQuery (Hashtable params, PrintWriter out, 
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
 		requestDataType = (String)params.get("requestDataType");
		//the servlet directory
		servletDir = rb.getString("requestparams.servletDir");
		this.clientType = params.get("clientType").toString();
		String requestDataFormatType  = params.get("requestDataFormatType").toString();
		
		//attempt to recognize the request as a query for communities 
		if (requestDataType.trim().equals("community")) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
			+ "community information - not requesting plot info");
			composeCommunityQuery(params);
			issueQuery("simpleCommunityQuery");
			out.println("Number of communities returned: "+queryOutputNum+"<br><br>");
		}
		// Cheat here - to recognise the single plot query to return entire plot
		//20020117 testing the new plot writer and data translation 
		//modules so cheating here first
		else if (plotId != null && resultType.equals("full") ) 
		{
			String outFile=servletDir+"atomicResult";
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a full data set "
				+"for plot: "+plotId+" <br>");
//			composeSinglePlotQuery(plotId, resultType, outFile);
//			issueQuery("simpleQuery");
			 dba.writeSingleVegBankPlot(plotId, outFile);
		}
		// this is where the query element checking is done for the vegetation plots
		// the way that this is structured currently the user is not forced to choose a
		//single query element
		//look for a taxonName
		else if ( taxonName != null  && taxonName.length()>0 ) 
		{
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			+"containing plots with taxonName: "+taxonName+" <br>");
			composeQuery("taxonName", taxonName);
			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
 		//look for elevation
		else if ( minElevation != null  && minElevation.length()>0 ) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			+"containing plots with a minElevation of: "+minElevation+" <br>");
			composeQuery("elevationMin", minElevation, "elevationMax", maxElevation);
 			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for state
		else if ( state != null  && state.length()>0 ) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			+"containing plots with a state equal to: "+state+" <br>");
			composeQuery("state", state);
			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for the community name
		else if ( communityName != null  && communityName.length()>0 ) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			+"containing plots with a communityName equal to: "+communityName+" <br>");
			composeQuery("communityName", communityName);
 			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//look for the surface geology option
		else if ( surfGeo != null  && surfGeo.length()>0 ) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a summary data set "
			+"containing plots with a surface geology like: "+surfGeo+" <br>");
			composeQuery("surfGeo", surfGeo);
			issueQuery("simpleQuery");
			out.println("Number of results returned: "+queryOutputNum+"<br><br>");
		}
		//if there are results returned to the servlet from the database in the form 
		//of a file returned then grab the summary viewer then let the user know
		if (queryOutputNum>=1) 
		{
			handleQueryResultsResponse(clientType, requestDataFormatType, out, 
			response, params);
		}
		else 
		{ 
			out.println("<br> <b> Please try another query </b> <br>"); 
			out.println("<a href = \"http://vegbank.nceas.ucsb.edu/forms/plot-query.html\">"
			+"return to query page</a><b>&#183;</b>"); //put in rb
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
	String requestDataType,  HttpServletResponse response)
	{
		try
		{
			//get the parameters needed for retuning the results
			String clientType = params.get("clientType").toString();
			String requestDataFormatType  = params.get("requestDataFormatType").toString();
			if (requestDataType.trim().equals("plantTaxon")) 
			{  
				out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
				+ "plant taxonomy information - not requesting plot info");
				composePlantTaxonomyQuery(params);
				issueQuery("simplePlantTaxonomyQuery");
				out.println("Number of taxa returned: "+queryOutputNum+"<br><br>");
				//use the method that handles the response	
				if (queryOutputNum>=1) 
				{
					handleQueryResultsResponse(clientType, requestDataFormatType, out, 
					response, params);
				}
				else 
				{ 
					out.println("<br> <b> Please try another query </b> <br>"); 
					out.println("<a href = \"/harris/servlet/pageDirector?pageType=plantQuery\">"
					+"return to query page</a><b>&#183;</b>"); //put in rb
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
			//get the parameters needed for retuning the results
			String clientType = params.get("clientType").toString();
			String requestDataFormatType  = params.get("requestDataFormatType").toString();
			if (requestDataType.trim().equals("vegCommunity")) 
			{  
				out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
				+ "community information - not requesting plot info");
				composeCommunityQuery(params);
				issueQuery("simpleCommunityQuery");
				out.println("Number of communities returned: "+queryOutputNum+"<br><br>");
	

		
//				servletUtility l =new servletUtility();  
 				//the requestDataType 'plantTaxon' specifies the style sheet
//				l.getViewOption(requestDataType); 
// 				out.println(l.outString);
				
				//use the method that handles the response	
				if (queryOutputNum>=1) 
				{
					handleQueryResultsResponse(clientType, requestDataFormatType, out, 
					response, params);
				}
				else 
				{ 
					out.println("<br> <b> Please try another query </b> <br>"); 
					out.println("<a href = \"/harris/servlet/pageDirector?pageType=communityQuery\">"
					+"return to query page</a><b>&#183;</b>"); //put in rb
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
	private void handleCompoundQuery (Hashtable params, PrintWriter out, 
		HttpServletResponse response) 
	{
		/* Get all possible parameters from the hash */	
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
 
 		out.println("<br>DataRequestServlet.handleCompoundQuery - returning a summary data set ");
		
 		composeQuery(taxonName, communityName, taxonOperation, commOperation, minElevation,
			maxElevation, state, surfGeo, multipleObs, compoundQuery);
 		issueQuery("compoundQuery");
 
 		out.println("Number of results returned: "+queryOutputNum);
 
		//allow the user to access the summary results
 		ServletUtility l =new ServletUtility();  
 		l.getViewOption("vegPlot");  //change the attribute being passed to requestDataType
 		out.println(l.outString);
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
 		try 
		{
 			PrintStream queryOutFile = new PrintStream(
 			new FileOutputStream(servletDir+"taxonQuery.xml", false)); 
			
			//grab the relevent query attributes out of the hash
			String taxonName = (String)params.get("taxonName");
			String taxonNameType = (String)params.get("taxonNameType");
			String taxonLevel = (String)params.get("taxonLevel");
		

			System.out.println("DataRequstServlet > printing from DataRequestServlet.composePlantTaxonomyQuery: "+
			"taxonName: "+taxonName);

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
 				"		<requestDataType>"+requestDataType+"</requestDataType> \n"+
 				"		<resultType>"+resultType+"</resultType> \n"+
 				"		<outFile>"+servletDir+"summary.xml</outFile> \n"+
 				"</dbQuery>"
 			);
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
	public void returnQueryElemenySummary (PrintWriter out, Hashtable params, 
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
			out.println("---end of DataRequestServlet.returnQueryElemenySummary----<br></br>");
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
			sb.append("<requestDataType>summary</requestDataType> \n");
			sb.append("<resultType>summary</resultType> \n");
			sb.append("<outFile>"+servletDir+"summary.xml</outFile> \n");
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
 */
	private void composeQuery (String queryElement, String elementString) 
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
				"<resultType>summary</resultType> \n"+
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
 */

	private void composeQuery (String minElement, String minValue, 
		String maxElement, String maxValue) 
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
				"<resultType>summary</resultType> \n"+
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
 			PrintStream outFile  = new PrintStream(
			new FileOutputStream(servletDir+"query.xml", false)); 

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
			"<resultType>summary</resultType> \n"+
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
			//if it is a browser query then register the document
			if (this.clientType.equals("browser") )
			{
				System.out.println("DataRequestServlet > registering the doc. -- browser client");
				registerQueryDocument();
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
	 *
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
	 */
	 private void registerQueryDocument()
	 {
		 System.out.println("DataRequestServlet > registering the query document ###");
		 suy.uploadFileDataExcahgeServlet(servletDir+"query.xml", userName);
		 
	 }
}
