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
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import xmlresource.utils.transformXML;
import xmlresource.utils.XMLparse;

import databaseAccess.dbAccess;
import databaseAccess.CommunityQueryStore;
import databaseAccess.SqlFile;
import servlet.util.ServletUtility;
import DataSourceClient; //this is the rmi client for loading mdb files

/**
 * REQUIRED PARAMETERS
 * @param submitDataType -- 
 * 
 *
 *	'$Author: harris $'
 *  '$Date: 2002-04-04 00:08:29 $'
 *  '$Revision: 1.20 $'
 */


public class DataSubmitServlet extends HttpServlet 
{

	private String submitDataType = null;
	private String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/community-submit_valid.html";
	private String communityValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	private String commUpdateScript = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/update_community_summary.sql";
	
	
	ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private SqlFile sqlFile = new SqlFile(); 
	private ServletUtility su = new ServletUtility();
	private CommunityQueryStore qs;
	private VegCommunityLoader commLoader = new VegCommunityLoader();
	private XMLparse parser;
	
	private String rmiServer = "guest06.nceas.ucsb.edu";
	private int rmiServerPort = 1099;
	private DataSourceClient rmiClient;
	
	private String plotsArchiveFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/input.data";
	private String plotsArchiveType = "tncplots";
	
	private String plotSelectTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot-submit-select.html";
	private String plotSelectForm  = "/usr/local/devtools/jakarta-tomcat/webapps/forms/plot_select.html";
	
	private String browserType = "";
	
	/**
	 * constructor method
	 */
	public DataSubmitServlet()
	{
		System.out.println("init: DataSubmitServlet");
		//construct a new instance of the rmi client
		rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
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
			System.out.println("DataSubmitServlet > current user: " +  userName   );
			
			Hashtable params = new Hashtable();
			params = su.parameterHash(request);
			// there will be cases where there will be multiple parameters 
			// with the same name and they all need to be accessed so 
			// capture an ennumeration here
			Enumeration enum = request.getParameterNames();
			
			//get the browser type 
			this.browserType = su.getBrowserType(request); 
			
			System.out.println("DataSubmitServlet > IN PARAMETERS: "+params.toString() );
			submitDataType = (String)params.get("submitDataType");
			System.out.println("DataSubmitServlet > submit data type: "
			+ submitDataType);
			
			
			// FIGURE OUT WHAT TO DO WITH THE REQUEST
			if ( submitDataType.trim().equals("vegCommunity") )
			{
				StringBuffer sb = handleVegCommunitySubmittal(params, response);
				out.println( sb.toString() );
			}
			else if ( submitDataType.trim().equals("vegCommunityCorrelation")  )
			{
				submitDataType.trim().equals("vegCommunityCorrelation");
				StringBuffer sb = handleVegCommunityCorrelation(params, response);
				out.println( sb.toString() );
			}
			else if ( submitDataType.trim().equals("vegPlot")  )
			{
				//out.println("DataSubmitServlet > action unknown!");
				StringBuffer sb = handleVegPlotSubmittal(enum, params, request, response);
				out.println( sb.toString() );
			}
			else
			{
				out.println("DataSubmitServlet > action unknown!");
			}
			
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
		
	/**
	 * this method is used to handle the submittal of a vegplot into the 
	 * VegBank database using for the insertion of an access file the 
	 * rmi datasource client / server system to access the data on a 
	 * win nt machine
	 *
	 * @see DataSourceClient
	 * @param enum -- the input parameters to the parent servlet
	 * @param params -- a hashtable with the unique parameters sent to the servlet
	 * @param request -- http request
	 * @param response -- http reponse
	 * 
	 */
	 private StringBuffer handleVegPlotSubmittal(Enumeration enum,
	 Hashtable params, HttpServletRequest request, HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String action = (String)params.get("action");
			if ( action.equals("init") )
			{
				//check that the uer has valid priveleges to load a data 
				//file to the database and if so give the use a window 
				//to upload some data
				
				//make sure the correct parameters were passed and 
				//authenticate the user
				
				//redirect the user to the data selection form to either
				//upload or choose from a previosly uploaded file
				response.sendRedirect("/forms/plot-upload.html");
			}
			
			// if the action is upload that is basically ar referal 
			// from the data exchange client after the file has been 
			// upload
			else if ( action.equals("upload") )
			{
				//take the file that was just deposited via the data exchange servlet
				//and pass it onto the winnt machine
				rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
				boolean sendResults = rmiClient.putMDBFile(plotsArchiveFile, plotsArchiveType);
				System.out.println("DataSubmitServlet > RMI file send results: " + sendResults);
				
				//validate that the plot archive is real
				rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
				boolean fileValidityResults = rmiClient.isMDBFileValid();
				System.out.println("DataSubmitServlet > file validity at RMI server: " + fileValidityResults);
				
				// get the name of the plots and update the selection form and redircet
				// the browser there
				rmiClient = new DataSourceClient(rmiServer, ""+rmiServerPort);
				Vector plots = rmiClient.getPlotNames();
				// prepare the plots element
				StringBuffer sb2 = new StringBuffer();
				for (int i=0; i<plots.size(); i++) 
				{
					sb2.append("<option>" + (String)plots.elementAt(i) + "</option> \n");
					System.out.println("DataSubmitServlet > add plot: " + (String)plots.elementAt(i) );
				}
				//create the form
				su.filterTokenFile(plotSelectTemplate, plotSelectForm, "plots", sb2.toString() );
				// send the user there -- this is getting cached so print it to the user 
				// instead
				//response.sendRedirect("/forms/plot_select.html");
				String s = su.fileToString(plotSelectForm);
				sb.append( s );
				
			}
			// this is the actual submittal of one or many plots from the 
			// uploaded archive
			else if ( action.equals("submit") )
			{
				String receiptType  = (String)params.get("receiptType");
				System.out.println("DataSubmitServlet > requesting a receipt type: "+receiptType );
				while (enum.hasMoreElements()) 
				{
					String name = (String) enum.nextElement();
					if ( name.toUpperCase().equals("PLOTID") )
					{
						String values[] = request.getParameterValues(name);
						if (values != null) 
						{
							for (int i=0; i < values.length; i++) 
							{
								String thisPlot = values[i];
								System.out.println("DataSubmitServlet > requesting an rmi plot insert: '"+values[i]+"'" );
								//insert the plot over the rmi system
								String result = rmiClient.insertPlot(thisPlot);
								String receipt = getPlotInsertionReceipt(thisPlot, result, receiptType, i, values.length);
								sb.append( receipt );
								System.out.println("DataSubmitServlet > requesting a receipt: '"+i+"' out of: " + values.length );
							}
						}
					}
				}
				System.out.println("DataSubmitServlet > requesting an rmi insert ");
			}
			else
			{
				System.out.println("DataSubmitServlet > unknown plot submital task ");
			}
			
			
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}
		
		
	
	/**
	 * method that for a given plot will grab some of the salient stats for that 
	 * plot into an html table that can be used elsehere.  Also the loading results
	 * from the rmi client are passed into this method so that they can elso be 
	 * embedded into the table the table will look somethink like what is below for each 
	 * plot:
	 *
	 *  <table>
	 * 		<tr> <td> plot name </td> <td> $plotname </td>
	 * 		<tr> <td> state </td> <td> $state </td>
	 * 		<tr> <td> named place </td> <td> $named place </td>
	 * 		<tr> <td> community  </td> <td> $community </td>
	 *	</table> 
	 *
	 * @see DataSourceClient
	 * @param plot  -- the string name of the plot as it used in the users archive
	 * @param results -- the string results that have been returnd by the loader
	 *		which is the rmi client -- this is strictly an XML document
	 * @param receiptType -- 'minimal' or 'extensive'
	 * @param curPlotNumber -- the current number of the plots that to be inserted
	 * 	- if this is the first then the xml 1.0 header will be attached here
	 * 		this pllt should start at zero
	 * @param totalPlotNumber -- the total number of plots for which a receipt
	 *  will be requested for
	 */
	 private String getPlotInsertionReceipt(String plot, String results, 
	 String receiptType, int curPlotNumber, int totalPlotNumber)
	 {
	 		StringBuffer sb = new StringBuffer();
			System.out.println("DataSubmitServlet > currentPlot: " + curPlotNumber +" total number: "+ totalPlotNumber );
	 		try
			{
				if (receiptType.equals("extensive") ) 
				{
					if ( this.browserType.equals("msie") )
					{
						//if there is only one plot for which a receipt will be prepared
						if ( curPlotNumber == 0 && (totalPlotNumber == 1) )
						{
							sb.append("<?xml version=\"1.0\"?> \n");
							sb.append("<plotInsertionReceipt> \n");
							sb.append(results);
							sb.append("</plotInsertionReceipt>");
						}
						else if ( curPlotNumber == 0 && (totalPlotNumber > 1)  )
						{
							sb.append("<?xml version=\"1.0\"?> \n");
							sb.append("<plotInsertionReceipt> \n");
							sb.append(results);
						}
						else if ( (curPlotNumber+1) == (totalPlotNumber) )
						{
							System.out.println("DataSubmitServlet > closing the receipt tags");
							sb.append(results);
							sb.append("</plotInsertionReceipt>");
						}
						else
						{
							sb.append( results );
						}
					}
					else
					{
						//the loading results 
						// first transform the xml that is returned as the results string
						// into an html body
						transformXML trans  = new transformXML();
						String tr = trans.getTransformedFromString(results, "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/ascii-treeview.xsl");
						sb.append("<table> \n");
						sb.append(" <tr> \n");	
						sb.append(" 	<td> Loading Results </td> \n");	
						sb.append(" </tr> \n");	
						sb.append("</table> \n");
						sb.append(tr);
						sb.append("<br>");
					}
				}
				else if (receiptType.equals("minimal") ) 
				{
					String s = getMinimalInsertionReceipt(plot, results);
					sb.append( s );
				}
				else
				{
					System.out.println("DataSubmitServlet > unrecognized receipt type: " + receiptType );
				}
			}
			catch( Exception e ) 
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
	 }
	 
	 /**
	  * method that returns the most salient attributes of a plot that were
		* loaded to the database as an html table -- this method will be called when
		* the user request a 'minimal' receipt for the plots that they are attempting
		* to load to the vegbank
		* @param plot  -- the string name of the plot as it used in the users archive
	  * @param results -- the string results that have been returnd by the loader
	  *		which is the rmi client -- this is strictly an XML document
		*
		*/
		private String getMinimalInsertionReceipt(String plot, String results)
		{
			StringBuffer sb = new StringBuffer();
			try
			{
				parser = new XMLparse();	
				Document doc = parser.getDocumentFromString(results);
				Vector accessionNumber = parser.getValuesForPath(doc, "/plotInsertion/accessionNumber");
				Vector latitude = parser.getValuesForPath(doc, "/plotInsertion/latitude");
				Vector longitude = parser.getValuesForPath(doc, "/plotInsertion/longitude");
				Vector state =  parser.getValuesForPath(doc, "/plotInsertion/state");
				
			//	String plotName = rmiClient.getAuthorPlotCode(plot);
			//	String state = rmiClient.getState(plot);
			//	String community= rmiClient.getCommunityName(plot);
				
				//plot name
				sb.append("<table> \n");
			//	sb.append(" <tr bgcolor=\"#DFE5FA\"> \n");	
			//	sb.append(" 	<td> Plot Name: </td> \n");	
			//	sb.append(" 	<td> "+plotName+" </td> \n");
			//	sb.append(" </tr> \n");
				
				//accession number
				sb.append(" <tr bgcolor=\"#DFE5FA\"> \n");	
				sb.append(" 	<td> VegBank Identifier: </td> \n");	
				sb.append(" 	<td> "+accessionNumber.toString()+" </td> \n");
				sb.append(" </tr> \n");
				
				//the state
				sb.append(" <tr> \n");	
				sb.append(" 	<td> State </td> \n");	
				sb.append(" 	<td> "+state+" </td> \n");	
				sb.append(" </tr> \n");	
			
				//the locations
				sb.append(" <tr> \n");	
				sb.append(" 	<td> Latitude:  </td> \n");	
				sb.append(" 	<td> "+latitude+" </td> \n");	
				sb.append(" </tr> \n");	
				sb.append(" <tr> \n");	
				sb.append(" 	<td> Longitude:  </td> \n");	
				sb.append(" 	<td> "+longitude+" </td> \n");	
				sb.append(" </tr> \n");	
			
				//the community
			//	sb.append(" <tr> \n");	
			//	sb.append(" 	<td> Commmunity: </td> \n");	
			//	sb.append(" 	<td> "+community+" </td> \n");	
			//	sb.append(" </tr> \n");
				sb.append("</table> \n");
			}
			catch( Exception e ) 
			{
				System.out.println("Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return( sb.toString() );
		}
	
	
	/**
	 * this method is used to handle the coorrelation of communities
	 * 
	 */
	 private StringBuffer handleVegCommunityCorrelation(Hashtable params, 
	 HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String action = (String)params.get("action");
			if ( action.equals("init") )
			{
				sb.append("<html>");
				
				System.out.println("DataSubmitServlet > init vegCommunityCorrelation");
				String salutation = (String)params.get("salutation");;
				String firstName =  (String)params.get("firstName");
				String lastName =  (String)params.get("lastName");
				String emailAddress = (String)params.get("emailAddress");
				String orgName =  (String)params.get("orgName");
				String commName = (String)params.get("communityName");
				String correlationTaxon = (String)params.get("correlationTaxon");
				String correlationTaxonLevel = (String)params.get("correlationTaxonLevel");
				String status = "accepted";
				String nameRefAuthor = salutation+" "+firstName+" "+lastName;
				String nameRefTitle = "vegbank";
				sb.append("<b> communityName: " + commName + " </b> <br>" );
				//HERE NEED TO GET THE STATUS ID ASSOCIATED WITH THIS PLANT
				commLoader = new VegCommunityLoader();
				int statusId = commLoader.getStatusId(nameRefTitle, nameRefAuthor, 
				status, commName, salutation, firstName, lastName, emailAddress,
				orgName );
				
				//get a vector that contains hashtables with all the possible 
				//correlation ( name, recognizing party, system, status, level)
				qs = new CommunityQueryStore( );
				Vector correlationTargets = qs.getCorrelationTargets(correlationTaxon, 
				"natureserve", correlationTaxonLevel);
						
				for (int i=0; i<correlationTargets.size(); i++) 
				{
					//get the hashtables form the vector
					Hashtable hash = (Hashtable)correlationTargets.elementAt(i);
					String correlationName = (String)hash.get("commName");
					String recognizingParty  = (String)hash.get("recognizingParty");
					String level  = (String)hash.get("level");
					String conceptStatus  = (String)hash.get("conceptStatus");
					String commConceptId  = (String)hash.get("commConceptId");
					String usageId  = (String)hash.get("usageId");
						
					sb.append("<form action=\"http://vegbank.nceas.ucsb.edu/framework/servlet/DataSubmitServlet\" method=\"get\" >");
					sb.append(" <input type=hidden name=submitDataType value=vegCommunityCorrelation> ");
					sb.append(" <input type=hidden name=action value=submit> ");
					sb.append(" <input type=hidden name=statusId value="+statusId+"> ");
					sb.append(" <input type=hidden name=conceptId value="+commConceptId+"> ");
					
					sb.append(" <input type=hidden name=commName value=\""+commName+"\"> ");
					sb.append(" <input type=hidden name=correlationTargetName value="+correlationName+"> ");
					sb.append(" <input type=hidden name=correlationTargetLevel value="+level+"> ");
					
					sb.append("commName: " +correlationName + "<br> ");
					sb.append("party: " +recognizingParty + "<br> ");
					sb.append("level: " +level + "<br> ");
					sb.append("status: "+conceptStatus + "<br> ");
					sb.append("concept: "+commConceptId + "<br> ");
						
					sb.append("<select class=item name=correlation multiple size=3>");
					sb.append("<option selected>unknown");
					sb.append("<option>gt");
					sb.append("<option>lt");
					sb.append("<option>overlap");
					sb.append("</select>");
					sb.append("<br> <input type=\"submit\" VALUE=\"submit correlation\">");
					sb.append("<br>");
					
					sb.append("</form>");
				}
				sb.append("</html>");
			}
			else if ( action.equals("submit") )
			{
				//GET THE ATTRIBUTES TO LOAD TO THE DATABASE
				String conceptId = (String)params.get("conceptId");
				int concept = Integer.parseInt(conceptId);
				String statusId =  (String)params.get("statusId");
				int status = Integer.parseInt(statusId);
				String correlation =  (String)params.get("correlation");
				
				String communityName = (String)params.get("commName");
				String correlationTargetName = (String)params.get("correlationTargetName");
				String correlationTargetLevel = (String)params.get("correlationTargetLevel");
				
				String startDate = null;
				String stopDate = null;
				commLoader = new VegCommunityLoader();
				commLoader.insertCommunityCorrelation(status, concept, correlation
				, startDate, stopDate);
				//UPDATE THE DATABASE SUMMARY TABLE
				sqlFile.issueSqlFile(commUpdateScript);
				
				//assume that all went ok and return a recipt
				System.out.println("DataSubmitServlet > preparing results page");
				
				String resultPage = getCorrelationResultsPage(true, communityName, 
				correlationTargetName, correlationTargetLevel, correlation);
				
				sb.append( resultPage );
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}
	
	
	/**
	 * method to prepare the results page 
	 */
	 private String getCorrelationResultsPage(boolean result, String communityName, 
	 String correlationTargetName, String correlationTargetLevel, String correlation)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<b> CORRELATION SUBMITAL <b> \n");
		 sb.append("<br> \n");
		 sb.append("Community Name: " + communityName +" <br> \n");
		 sb.append("Correlation Target Name: " + correlationTargetName +" <br> \n");
		 sb.append("Correlation Target Level: " + correlationTargetLevel +" <br> \n");
		 sb.append("Convergence: " + correlation +" <br> \n");
		 sb.append("<a href=/forms/community-submit.html> Add a new community  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }
	 
	 
	/**
	 * this method handles the submital of community data to the 
	 * vegbank database.  The return from this method is a StrinBuffer 
	 * containg the response from the application and / or database in
	 * relation to the submittal proccess of a new community.  Here the 
	 * action may be either init ( which is the first stage in a two step 
	 * proccess), or submit ( which is the second stage ) where the data 
	 * are submitted to the database
	 * 
	 * @param params -- a hashtable storing the inpt parameters
	 * @return sb -- A string buffer with the response by the database
	 * and application about the success of the submittal to the database
	 */
	private StringBuffer handleVegCommunitySubmittal(Hashtable params, HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String action = (String)params.get("action");
			if ( action.equals("init") )
			{
				
				System.out.println("DataSubmitServlet > init vegCommunity");
				String salutation = (String)params.get("salutation");;
				String firstName =  (String)params.get("firstName");
				String lastName =  (String)params.get("lastName");
				String emailAddress =  (String)params.get("emailAddress");
				String orgName =  (String)params.get("orgName");
				String commName = (String)params.get("communityName");
				String dateEntered = "11-MAR-2002";
				String authors = salutation.trim()+" "+firstName+" "+lastName ;
				String title = "VegBank2002";
				String pubDate ="11-MAR-2002";
				String edition ="--";
				String seriesName ="--";
				String issueId ="--";
				String otherCitationDetails ="--";
				String pageNumber ="--";
				String isbn ="--";
				String issn ="--";
				String classSystem ="--";
				String commLevel ="--";
				String conceptStatus = "accepted";
				String nameStatus = "standard";
			
				//CREATE A COMMUNITY CODE
				String commCode = "";
				StringTokenizer t = new StringTokenizer(commName);
	  		while (t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					buf = buf.substring(0, 1);
					commCode = commCode+buf.toUpperCase();
				}
					
				//SEE IF THE NAME ALREADY EXISTS IN THE DATABASE
				qs = new CommunityQueryStore();
				Vector v = qs.getCommunityNames(commName);
				if ( v.size() < 1)
				{
					//assume that the name does not already exist in the db
					//so update the validation page and return it to the user
					String message = "You have entered a unique name; VegBank will be the reference";
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, classSystem, 
					message);
					//redirect the browser
					response.sendRedirect("/forms/valid.html");
				}
				else
				{
					String curName = v.elementAt(0).toString();
					qs = new CommunityQueryStore();
					Hashtable refHash = qs.getNameReference(curName);
					String nameRefTitle = (String)refHash.get("title");
					String nameRefAuthors = (String)refHash.get("authors");
					String nameRefPubDate = (String)refHash.get("pubDate");
					String nameRefEdition = (String)refHash.get("edition");
					String nameRefSeriesName = (String)refHash.get("seriesName");
					String nameRefIssueId = (String)refHash.get("issueId");
					String nameRefOtherCitationDetails = (String)refHash.get("otherCitationDetails");
					String nameRefPageNumber = (String)refHash.get("pageNumber");
					String nameRefISBN = (String)refHash.get("isbn");
					String nameRefISSN = (String)refHash.get("issn");
					String message =" This name already exists in the vegbank system and was <br>"
					+"has a refernce to: "+nameRefAuthors;
					
					sb.append("nameRefTitle: " + nameRefTitle);
					sb.append("nameRefAuthors: " + nameRefAuthors);
					sb.append("nameRefPubDate: " + nameRefPubDate);
					sb.append("nameRefPubEdition: " + nameRefEdition);
					sb.append("nameRefSeriesName: " + nameRefSeriesName);
					sb.append("nameRefIssueId: " + nameRefIssueId);
					sb.append("nameRefOtherCitationDetails: " + nameRefOtherCitationDetails);
					sb.append("nameRefPageNumber: " + nameRefPageNumber);
					sb.append("nameRefISBN: " + nameRefISBN);
					sb.append("nameRefISSN: " + nameRefISSN);
					System.out.println("DataSubmitServlet > " + sb.toString());
					
					
					//update the html page with the name refrence equal to the 
					//data retrieved from the database
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, nameRefAuthors, nameRefTitle, nameRefPubDate, nameRefEdition, 
					nameRefSeriesName, nameRefIssueId, nameRefOtherCitationDetails, 
					nameRefPageNumber, nameRefISBN, nameRefISSN, 
					authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, 
					classSystem, message);
					
					//redirect the browser
					response.sendRedirect("/forms/valid.html");
				}
				
			}
			//THIS WHERE THE ACTUAL SUBMITTAL OF THE NEW COMMUNITY TAKES PLACE
			else if ( action.equals("submit") )
			{
				
				String salutation = (String)params.get("salutation");
				String givenName = (String)params.get("firstName");
				String surName = (String)params.get("lastName");
				String middleName = "";
				String orgName = (String)params.get("orgName");
				String contactInstructions = (String)params.get("emailAddress");
				
				String conceptReferenceTitle = (String)params.get("conceptRefTitle");
				String conceptReferenceAuthor = (String)params.get("conceptRefAuthors");
				String conceptReferenceDate = "12-MAR-2002";

				String nameReferenceTitle = (String)params.get("nameRefTitle");
				String nameReferenceAuthor = (String)params.get("nameRefAuthors");
				String nameReferenceDate = "12-MAR-2002";
				
				String communityCode = (String)params.get("communityCode");
				String communityLevel = (String)params.get("communityLevel");
				String communityName = (String)params.get("communityName");
				String dateEntered = "12-MAR-2002";
				String parentCommunity = "";
				String partyName = "vegbank";
				String otherName = "";
				System.out.println("DataSubmitServlet > submit vegCommunity");
				
				//SUBMIT THE DATA TO THE DATABASE
				StringBuffer sbr = commLoader.insertGenericCommunity( salutation,  givenName, surName,
				middleName, orgName, contactInstructions,conceptReferenceTitle, 
				conceptReferenceAuthor, conceptReferenceDate, nameReferenceTitle,
				nameReferenceAuthor, nameReferenceDate, communityCode,communityLevel,
				communityName,  dateEntered, parentCommunity,  otherName  );
				
				String resultString = sbr.toString();
				
				System.out.println("DataSubmitServlet > submittal result: " + resultString);
				//IF SUCCESS THEN PREPARE AND RETURN A PAGE TO THE USER
				if ( resultString.toUpperCase().indexOf("TRUE") > 0 )
				{
					System.out.println("DataSubmitServlet > preparing results page");
					String resultPage = getSubmittalResultsPage(true, communityName, givenName, 
					surName, nameReferenceAuthor, conceptReferenceAuthor );
					sb.append( resultPage );
				}
				//UPDATE THE DATABASE SUMMARY TABLE
				sqlFile.issueSqlFile(commUpdateScript);
			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
	}
	
	
	/**
	 * method that returns a result page as a string based on a true or false
	 * 
	 * @param result -- true or false
	 * @param commName -- the name of the community
	 * @param givenName -- the given Name of the system user
	 * @param surName -- the surName of the system user
	 * @param nameReferenceAuthor -- the author of the name reference
	 * @param conceptReferenceAuthor -- the name of the concept reference auth.
	 */
	 private String getSubmittalResultsPage( boolean result, String commName, 
	 String givenName, String surName, String nameReferenceAuthor, 
	 String conceptReferenceAuthor)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html> \n");
		 sb.append("<b> SUCCESSFUL SUBMITAL <b> \n");
		 sb.append("<br> \n");
		 sb.append("VegBank User: " + givenName + " " + surName + " <br> \n");
		 sb.append("Community Name: " + commName +" <br> \n");
		 sb.append("Name Reference: " + nameReferenceAuthor +" <br> \n");
		 sb.append("Concept Reference: " + conceptReferenceAuthor +" <br> \n");
		 sb.append("<a href=/forms/community-correlate.html> correlate communities  </a> \n");
		 sb.append("");
		 sb.append("");
		 sb.append("");
		 sb.append("</html> \n");
		 return(sb.toString() );
	 }
	
	
	/**
	 * method that handles updating the validation form for the community
	 */
	 private boolean updateCommunityValidationPage(String salutation, 
	 String firstName, String lastName, String emailAddress, String orgName, 
	 String commName, String commCode, String commLevel, String conceptStatus, 
	 String nameStatus, String authors, String title, String pubDate, 
	 String edition,
	 String seriesName, String issueId, String otherCitationDetails, 
	 String pageNumber, String isbn, String issn, String classSystem, String message)
	 {
	 	boolean results = true;
		Hashtable replaceHash = new Hashtable();
		try
		{
			System.out.println("DataSubmitServlet > commName: " + commName);
			System.out.println("DataSubmitServlet > commLevel: " + commCode);
			System.out.println("DataSubmitServlet > commCode: " + commLevel);
			
			
			System.out.println("DataSubmitServlet > salutation: " + salutation);
			System.out.println("DataSubmitServlet > firstname: " + firstName);
			System.out.println("DataSubmitServlet > lastName: " + lastName );
			System.out.println("DataSubmitServlet > emailAddress: " + emailAddress);
			System.out.println("DataSubmitServlet > orgname: " + orgName);
			System.out.println("DataSubmitServlet > authors: " + authors);
			System.out.println("DataSubmitServlet > titlke: " + title);
			System.out.println("DataSubmitServlet > pubDate: " + pubDate);
			System.out.println("DataSubmitServlet > ");
			System.out.println("DataSubmitServlet > ");
			
			replaceHash.put("communityName", ""+commName);
			replaceHash.put("communityLevel", ""+commLevel);
			replaceHash.put("communityCode", ""+commCode);
			replaceHash.put("conceptStatus", ""+conceptStatus);
			replaceHash.put("nameStatus", ""+nameStatus);
			
			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", ""+salutation);
			replaceHash.put("firstName", ""+firstName);
			replaceHash.put("lastName", ""+lastName);
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("orgName" , ""+orgName);
			
			
			replaceHash.put("nameRefAuthors" , ""+authors);
			replaceHash.put("nameRefTitle" , ""+ title);
			replaceHash.put("nameRefPubDate" , ""+pubDate);
			replaceHash.put("nameRefEdition", ""+edition);
			replaceHash.put("nameRefSeriesName", ""+seriesName);
			replaceHash.put("nameRefIssueId", ""+ issueId);
			replaceHash.put("nameRefOtherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("nameRefPageNumber", ""+pageNumber );
			replaceHash.put("nameRefISBN", ""+ isbn );
			replaceHash.put("nameRefISSN", ""+issn);
			
			replaceHash.put("conceptRefAuthors" , ""+authors);
			replaceHash.put("conceptRefTitle" , ""+ title);
			replaceHash.put("conceptRefPubDate" , ""+pubDate);
			replaceHash.put("conceptRefEdition", ""+edition);
			replaceHash.put("conceptRefSeriesName", ""+seriesName);
			replaceHash.put("conceptRefIssueId", ""+issueId);
			replaceHash.put("conceptRefOtherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("conceptRefPageNumber", ""+pageNumber );
			replaceHash.put("conceptRefISBN", ""+isbn );
			replaceHash.put("conceptRefISSN", ""+issn);
			
			
			replaceHash.put("authors" , ""+authors);
			replaceHash.put("title" , ""+title);
			replaceHash.put("pubDate" , ""+pubDate);
			replaceHash.put("edition", ""+edition);
			replaceHash.put("seriesName", ""+seriesName);
			replaceHash.put("issueId", ""+issueId );
			replaceHash.put("otherCitationDetails", ""+otherCitationDetails);
			replaceHash.put("pageNumber", ""+pageNumber );
			replaceHash.put("isbn", ""+isbn );
			replaceHash.put("issn", ""+issn );
			replaceHash.put("classSystem", ""+classSystem );
			
			replaceHash.put("message", message );
			//replaceHash.put("", );
			//replaceHash.put("", );
			//replaceHash.put("", );
			su.filterTokenFile(communityValidationTemplate, communityValidationForm, replaceHash);
		}
		catch( Exception e)
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(results);
	 }
	 
	 
	 
	 private boolean updateCommunityValidationPage(String salutation, 
	 String firstName, String lastName, String emailAddress, String orgName, 
	 String commName, String commCode, String commLevel, String conceptStatus, 
	 String nameStatus, 
	 
	 String nameRefAuthors, String nameRefTitle, String nameRefPubDate, 
	 String nameRefEdition,
	 String nameRefSeriesName, String nameRefIssueId, String nameRefOtherCitationDetails, 
	 String nameRefPageNumber, String nameRefISBN, String nameRefISSN, 
	 
	 String conceptRefAuthors, String conceptRefTitle, String conceptRefPubDate, 
	 String conceptRefEdition,
	 String conceptRefSeriesName, String conceptRefIssueId, String conceptRefOtherCitationDetails, 
	 String conceptRefPageNumber, String conceptRefISBN, String conceptRefISSN, 
	 
	 String classSystem, String message)
	 {
	 	boolean results = true;
		Hashtable replaceHash = new Hashtable();
		try
		{
			System.out.println("DataSubmitServlet > commName: " + commName);
			System.out.println("DataSubmitServlet > commLevel: " + commCode);
			System.out.println("DataSubmitServlet > commCode: " + commLevel);
			
			
			System.out.println("DataSubmitServlet > salutation: " + salutation);
			System.out.println("DataSubmitServlet > firstname: " + firstName);
			System.out.println("DataSubmitServlet > lastName: " + lastName );
			System.out.println("DataSubmitServlet > emailAddress: " + emailAddress);
			System.out.println("DataSubmitServlet > orgname: " + orgName);
			
			System.out.println("DataSubmitServlet > nameRefAuthors: " + nameRefAuthors);
			System.out.println("DataSubmitServlet > nameRefTitle: " + nameRefTitle);
			System.out.println("DataSubmitServlet > nameRefPubDate: " + nameRefPubDate);
			
			System.out.println("DataSubmitServlet > conceptRefAuthors: " + conceptRefAuthors);
			System.out.println("DataSubmitServlet > conceptRefTitle: " + conceptRefTitle);
			System.out.println("DataSubmitServlet > conceptRefPubDate: " + conceptRefPubDate);
			
			
			replaceHash.put("communityName", ""+commName);
			replaceHash.put("communityLevel", ""+commLevel);
			replaceHash.put("communityCode", ""+commCode);
			replaceHash.put("conceptStatus", ""+conceptStatus);
			replaceHash.put("nameStatus", ""+nameStatus);
			
			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", ""+salutation);
			replaceHash.put("firstName", ""+firstName);
			replaceHash.put("lastName", ""+lastName);
			replaceHash.put("emailAddress", ""+emailAddress);
			replaceHash.put("orgName" , ""+orgName);
			
			replaceHash.put("nameRefAuthors" , ""+nameRefAuthors);
			replaceHash.put("nameRefTitle" , ""+ nameRefTitle);
			replaceHash.put("nameRefPubDate" , ""+nameRefPubDate);
			replaceHash.put("nameRefEdition", ""+nameRefEdition);
			replaceHash.put("nameRefSeriesName", ""+nameRefSeriesName);
			replaceHash.put("nameRefIssueId", ""+ nameRefIssueId);
			replaceHash.put("nameRefOtherCitationDetails", ""+nameRefOtherCitationDetails);
			replaceHash.put("nameRefPageNumber", ""+nameRefPageNumber );
			replaceHash.put("nameRefISBN", ""+nameRefISBN );
			replaceHash.put("nameRefISSN", ""+nameRefISSN);
			
			replaceHash.put("conceptRefAuthors" , ""+conceptRefAuthors);
			replaceHash.put("conceptRefTitle" , ""+ conceptRefTitle);
			replaceHash.put("conceptRefPubDate" , ""+conceptRefPubDate);
			replaceHash.put("conceptRefEdition", ""+conceptRefEdition);
			replaceHash.put("conceptRefSeriesName", ""+conceptRefSeriesName);
			replaceHash.put("conceptRefIssueId", ""+conceptRefIssueId);
			replaceHash.put("conceptRefOtherCitationDetails", ""+conceptRefOtherCitationDetails);
			replaceHash.put("conceptRefPageNumber", ""+conceptRefPageNumber );
			replaceHash.put("conceptRefISBN", ""+conceptRefISBN );
			replaceHash.put("conceptRefISSN", ""+conceptRefISSN);
			
			replaceHash.put("message", ""+message);
			
			replaceHash.put("classSystem", ""+classSystem );
			
			//replaceHash.put("", );
			//replaceHash.put("", );
			//replaceHash.put("", );
			//replaceHash.put("", );
			su.filterTokenFile(communityValidationTemplate, communityValidationForm, replaceHash);
		}
		catch( Exception e)
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(results);
	 }
	 
	 
	 


}
