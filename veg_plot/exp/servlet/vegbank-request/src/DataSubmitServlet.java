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
import databaseAccess.CommunityQueryStore;
import servlet.util.ServletUtility;



public class DataSubmitServlet extends HttpServlet 
{

	private String submitDataType = null;
	private String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/community-submit_valid.html";
	private String communityValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	
	
	ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private ServletUtility su = new ServletUtility();
	private CommunityQueryStore qs;
	private VegCommunityLoader commLoader = new VegCommunityLoader();
	
	/**
	 * constructor method
	 */
	public DataSubmitServlet()
	{
		System.out.println("init: DataSubmitServlet");
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
			Hashtable params = new Hashtable();
			params = su.parameterHash(request);
			
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
			
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * this method is used to handle the coorelation of communoities
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
				String emailAddress =  (String)params.get("emailAddress");
				String orgName =  (String)params.get("orgName");
				String commName = (String)params.get("communityName");
				String correlationTaxon = (String)params.get("correlationTaxon");
				
				sb.append("<b> communityName: " + commName + " </b> <br>" );
				
				System.out.println("DataSubmitServlet > correlation taxon:" + correlationTaxon);
				qs = new CommunityQueryStore();
				Vector v = qs.getCommunityNames(correlationTaxon);
				if ( v.size() > 0)
				{
					
					for (int i=0; i<v.size(); i++) 
					{
						String correlationName = v.elementAt(i).toString() ;
						System.out.println( correlationName );
						//sb.append( "<br> correlationName: "+ correlationName +"<br>");
						
						qs = new CommunityQueryStore();
						//get a vector that contains hashtables with all the possible 
						//correlation ( name, recognizing party, system, status, level)
						Vector correlationTagets = qs.getCorrelationTargets(correlationTaxon);
						//get the hashtables form the vector
						Hashtable hash = (Hashtable)correlationTagets.elementAt(i);
						correlationName = (String)hash.get("commName");
						String recognizingParty  = (String)hash.get("recognizingParty");
						String level  = (String)hash.get("level");
						String conceptStatus  = (String)hash.get("conceptStatus");
						
						sb.append("<form action=\"http://vegbank.nceas.ucsb.edu/framework/servlet/DataSubmitServlet\" method=\"get\" >");
						sb.append(" <input type=hidden name=submitDataType value=vegCommunityCorrelation> ");
						sb.append(" <input type=hidden name=action value=submit> ");
						sb.append("commName: " +correlationName + "<br> ");
						sb.append("party: " +recognizingParty + "<br> ");
						sb.append("level: " +level + "<br> ");
						sb.append("status: "+conceptStatus + "<br> ");
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
					
					
				}
				
				sb.append("</html>");
			}
			else if ( action.equals("submit") )
			{
				
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
			
				
				String commCode = "";
				StringTokenizer t = new StringTokenizer(commName);
	  		while (t.hasMoreTokens() )
				{
					String buf = t.nextToken();
					buf = buf.substring(0, 1);
					commCode = commCode+buf.toUpperCase();
				}
					
				//see if the name already existis
				qs = new CommunityQueryStore();
				Vector v = qs.getCommunityNames(commName);
				if ( v.size() < 1)
				{
					//if action is init then do the logic and display the user a second form
				
					//assume that the name does not already exist in the db
					//so update the validation page and return it to the user
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, classSystem);
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
					
					sb.append("not done");
					sb.append("found: " +v.size()+ " attributes");
					
					
					//update the html page with the name refrence equal to the 
					//data retrieved from the database
					updateCommunityValidationPage(salutation, firstName, lastName, 
					emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
					nameStatus, nameRefAuthors, nameRefTitle, nameRefPubDate, nameRefEdition, 
					nameRefSeriesName, nameRefIssueId, nameRefOtherCitationDetails, 
					nameRefPageNumber, nameRefISBN, nameRefISSN, 
					
					authors, title, pubDate, edition, seriesName,
					issueId, otherCitationDetails, pageNumber, isbn, issn, 
					
					
					classSystem);
					//redirect the browser
					response.sendRedirect("/forms/valid.html");
				}
				
			}
			else if ( action.equals("submit") )
			{
				sb.append("starting the correlation: ");
				System.out.println("DataSubmitServlet > submit vegCommunity");
				//sb = commLoader.insertGenericCommunity( conceptCode, conceptLevel, commName,
				//dateEntered, parentCommunity, allianceTransName, partyName );
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
	 * method that handles updating the validation form for the community
	 */
	 private boolean updateCommunityValidationPage(String salutation, 
	 String firstName, String lastName, String emailAddress, String orgName, 
	 String commName, String commCode, String commLevel, String conceptStatus, 
	 String nameStatus, String authors, String title, String pubDate, 
	 String edition,
	 String seriesName, String issueId, String otherCitationDetails, 
	 String pageNumber, String isbn, String issn, String classSystem)
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
	 
	 String classSystem)
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
