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
 *
 * taxonName - name of a taxon occuring in a plot <br>
 * communityName - name of a vegetaion community <br>
 * minElevation - minimum elavation in an elevation range <br>
 * maxElevation - maximum elevation in an elevation range <br>
 * state - state in which a plot exists <br>
 * surfGeo - surface geology type <br>
 * multipleObs - multiple observations <br>
 * compoundQuery - descriptor of the query type, which may include simple
 * 	- where only one criteria is used or compound where multiple criteria
 * 	may be used the flag for siple is '0' and the flag for compound is '1'<br>
 * plotId - database plot identification number <br> 
 * resultType - type of results expected by the user may include summary, where
 * 	a summary of the plot is returned and full where all plot attributes 
 *	are returned 
 * requestDataType 
 * resultFormatType - mak be either xml or html depending on the client tools<br>
 * 
 * @version Feb. 09 2001
 * @author John Harris
 * 
 */

public class DataRequestServlet extends HttpServlet 
{

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
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

// community related attributes
// commmunity name already defined above
private String communityLevel = null;
public servletUtility su = new servletUtility();
public dbAccess dba =new dbAccess(); 

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
			//enumeration is needed for those
			//cases where there are multiple values 
			// for a given parameter
			Enumeration enum =request.getParameterNames();
			Hashtable params = new Hashtable();
			params = su.parameterHash(request);
 
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
 			clientLog=(rb.getString("requestparams.clientLog"));
			servletDir = rb.getString("requestparams.servletDir");

 			remoteHost=request.getRemoteHost();
			System.out.println("accessed by: "+remoteHost);
			
			//log the use of the servlet by the client
			updateClentLog (clientLog, remoteHost);

			//return to the browser a summary of the request being made of the servlet this
			//method also adds some tags that are required many browsers
			returnQueryElemenySummary (out, params, response);
			
			//figure out what of data request type the client is requesting?
			if ( requestDataType.trim().equals("vegPlot") )
			{
				System.out.println("determining query type: " + determineQueryType(params) );
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
				System.out.println( " query on the plant taxonomy database \n");
				handlePlantTaxonQuery( params, out, requestDataType );
			}
			else if ( requestDataType.trim().equals("vegCommunity") )
			{
				System.out.println( " query on the vegetation community database \n"
					+" not yet implemented ");
				handleVegCommunityQuery( params, out, requestDataType);
			}
			else 
			{
				System.out.println( " unknown 'requestDataType' parameter "
					+" must be: vegPlot or plantTaxon or vegCommunity ");
				
			}
			
			
		}//end try
		catch( Exception e ) 
		{
			System.out.println("** failed in: DataRequestServlet.main "
			+" first try - reading parameters "
			+e.getMessage());
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
			System.out.println("cannot process query: no type specified");
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
			//validate that there are the correct type and number
			//of parameters being passed to thie method
			if ( (param.containsKey("criteria") == false ) 
				|| ( param.containsKey("operator")== false )
				|| ( param.containsKey("value") == false ))
			{
				System.out.println("did not get the correct parameters to handleExtendedQuery");	
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
				//the number of plots in the result set\
				out.println("Number of results returned: "+queryOutputNum+"<br><br>");
				
				
				if (queryOutputNum>=1) 
				{
					System.out.println("result sets: "+queryOutputNum);
 					servletUtility l =new servletUtility();  
 					l.getViewOption(requestDataType);
 					out.println(l.outString);
				}
				else 
				{ 
					out.println("<br> <b> Please try another query </b> <br>"); 
					out.println("<a href = \"/examples/servlet/pageDirector?pageType=DataRequestServlet\">"
					+"return to query page</a><b>&#183;</b>"); //put in rb
				}
				
				
			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in: DataRequestServlet.main "
			+" first try - reading parameters "
			+e.getMessage());
		}
	}


/**
 * Handles simple queries which are those queries where there is only one 
 * queryElemnt such as taxon or community name this is in contrast to a compound
 * query which has multiple query elements and is handled by the
 * 'handleCompoundQuery' method
 *
 * @param params - the Hashtable of parameters that should be included in the
 * 	query
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
		
		//attempt to recognize the request as a query for communities 
		if (requestDataType.trim().equals("community")) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
			+ "community information - not requesting plot info");
			composeCommunityQuery(params);
			issueQuery("simpleCommunityQuery");
			out.println("Number of communities returned: "+queryOutputNum+"<br><br>");
		}
		// Cheat here - to recognise the single plot query to return entire plot */
		else if (plotId != null && resultType.equals("full") ) 
		{
			String outFile=servletDir+"atomicResult";
			out.println("<br>DataRequestServlet.handleSimpleQuery - returning a full data set "
				+"for plot: "+plotId+" <br>");
			composeSinglePlotQuery(plotId, resultType, outFile);
			issueQuery("simpleQuery");
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
			System.out.println("result sets: "+queryOutputNum);
 			servletUtility l =new servletUtility();  
 			l.getViewOption(requestDataType);
 			out.println(l.outString);
		}
		else 
		{ 
			out.println("<br> <b> Please try another query </b> <br>"); 
			out.println("<a href = \"/examples/servlet/pageDirector?pageType=DataRequestServlet\">"
			+"return to query page</a><b>&#183;</b>"); //put in rb
		}
	}

	
	/**
	 * method to handle queries that are meant to be issued against 
	 * the plantTaxonomy database
	 */
	private void handlePlantTaxonQuery(Hashtable params, PrintWriter out, 
	String requestDataType)
	{
		if (requestDataType.trim().equals("plantTaxon")) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
			+ "plant taxonomy information - not requesting plot info");
			composePlantTaxonomyQuery(params);
			issueQuery("simplePlantTaxonomyQuery");
			out.println("Number of taxa returned: "+queryOutputNum+"<br><br>");
			
			servletUtility l =new servletUtility();  
 			//the requestDataType 'plantTaxon' specifies the style sheet
			l.getViewOption(requestDataType); 
 			out.println(l.outString);
		}
	}
	
	/**
	 * method to handle queries that are meant to be issued against 
	 * the vegetation community database
	 */
	private void handleVegCommunityQuery( Hashtable params, PrintWriter out, 
	String requestDataType)
	{
		if (requestDataType.trim().equals("vegCommunity")) 
		{  
			out.println("<br>DataRequestServlet.handleSimpleQuery - requesting "
			+ "community information - not requesting plot info");
			composeCommunityQuery(params);
			issueQuery("simpleCommunityQuery");
			out.println("Number of communities returned: "+queryOutputNum+"<br><br>");
			
			servletUtility l =new servletUtility();  
 			//the requestDataType 'plantTaxon' specifies the style sheet
			l.getViewOption(requestDataType); 
 			out.println(l.outString);
			
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
 		servletUtility l =new servletUtility();  
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

			System.out.println("printing from DataRequestServlet.composeCommunityQuery: "+
			"communityName: "+communityName);

 			//print the query instructions in the xml document
 			queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
 				"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
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
			System.out.println("** failed in DataRequestServlet.composeCommunityQuery " 
			+ e.getMessage());
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
		

			System.out.println("printing from DataRequestServlet.composePlantTaxonomyQuery: "+
			"taxonName: "+taxonName);

 			//print the query instructions in the xml document
 			queryOutFile.println("<?xml version=\"1.0\"?> \n"+       
 				"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
 				"	<dbQuery> \n"+
 				"		<query> \n"+
 				"			<queryElement>taxonName</queryElement> \n"+
 				"			<elementString>"+taxonName+"</elementString> \n"+
 				"		</query> \n"+
				"		<query> \n"+
 				"			<queryElement>taxonNameType</queryElement> \n"+
 				"			<elementString>"+taxonNameType+"</elementString> \n"+
 				"		</query> \n"+
 				"		<requestDataType>"+requestDataType+"</requestDataType> \n"+
 				"		<resultType>"+resultType+"</resultType> \n"+
 				"		<outFile>"+servletDir+"summary.xml</outFile> \n"+
 				"</dbQuery>"
 			);
 		}
		catch (Exception e) 
		{
			System.out.println(" failed : " 
			+ e.getMessage());
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
private void updateClentLog (String clientLog, String remoteHost) 
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
		System.out.println("failed in DataRequestServlet.updateClientLog " 
		+"trying to do client book keeping: " 
		+ e.getMessage());
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
			servletUtility k =new servletUtility();  
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
			System.out.println("failed in "
			+"DataRequestServlet.returnQueryElementSummary" 
			+ e.getMessage());
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
				"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
				"<dbQuery> \n"+
				"<query> \n"+
				"<queryElement>plotId</queryElement> \n"+
				"<elementString>"+plotId+"</elementString> \n"+
				"</query> \n"+
				"<resultType>"+resultType+"</resultType> \n"+
				"<outFile>"+outFile+"</outFile> \n"+
				"</dbQuery>"
			);
		}
		catch (Exception e) 
		{
			System.out.println("failed in DataRequestServlet.composeSinglePlotQuery" + 
			e.getMessage());
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
				System.out.println("cannot compose extended query");
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
			System.out.println("failed in DataRequestServlet.composeQuery: " + 
			e.getMessage());
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
				"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
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
			System.out.println("failed in DataRequestServlet.composeQuery: " + 
			e.getMessage());
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
				"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
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
			System.out.println("failed in DataRequestServlet.composeQuery:" + 
			e.getMessage());
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
			"<!DOCTYPE vegPlot SYSTEM \"plotQuery.dtd\"> \n"+     
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
			System.out.println("failed in DataRequestServlet.composeQuery: " + 
			e.getMessage());
		}
	}



/**
 *  Method to use dbAccess to issue the query to the database from the xml file
 *  created in the DataRequestServlet 
 *
 * @param queryType - the type of query to be sent through the dataAccess module
 * including simpleQuery (one attribute) and compoundQuery (multiple attributes) 
 */
	private void issueQuery (String queryType) 
	{
		//the database access module
		//dbAccess g =new dbAccess(); 
		if (queryType.equals("simpleQuery")) 
		{ 
			//call the plot access module
			dba.accessDatabase(servletDir+"query.xml", servletDir+"querySpec.xsl", "query");	
			//dba.accessDatabase("query.xml", "querySpec.xsl", "query");	
//			dba.accessDatabase("/jakarta-tomcat/webapps/examples/WEB-INF/lib/query.xml", "/jakarta-tomcat/webapps/examples/WEB-INF/lib/querySpec.xsl", "query");
			
			//grab the result set from the dbAccess class
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
			System.out.println("DataRequestServlet could not handle query type: "
				+queryType);
		}
	}
	
}
