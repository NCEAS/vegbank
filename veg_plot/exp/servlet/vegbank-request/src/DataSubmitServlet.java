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



public class DataSubmitServlet extends HttpServlet 
{

	private String submitDataType = null;
	private String communityValidationTemplate = "/usr/local/devtools/jakarta-tomcat/webapps/forms/community-submit_valid.html";
	private String communityValidationForm = "/usr/local/devtools/jakarta-tomcat/webapps/forms/valid.html";
	
	
	ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private ServletUtility su = new ServletUtility();
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
			
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
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
				
				//if action is init then do the logic and display the user a second form
				
				//assume that the name does not already exist in the db
				//so update the validation page and return it to the user
				updateCommunityValidationPage(salutation, firstName, lastName, 
				emailAddress, orgName, commName, commCode, commLevel, conceptStatus, 
				nameStatus, authors, title, pubDate, edition, seriesName,
				issueId, otherCitationDetails, pageNumber, isbn, issn, classSystem);
				
				response.sendRedirect("/forms/valid.html");
				
			}
			else
			{
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
			
			replaceHash.put("communityName", commName);
			replaceHash.put("communityLevel", commLevel);
			replaceHash.put("communityCode", commCode);
			replaceHash.put("conceptStatus", conceptStatus);
			replaceHash.put("nameStatus", nameStatus);
			
			replaceHash.put("level", "unknown");
			replaceHash.put("salutation", salutation);
			replaceHash.put("firstName", firstName);
			replaceHash.put("lastName", lastName);
			replaceHash.put("emailAddress", emailAddress);
			replaceHash.put("orgName" , orgName);
			
			replaceHash.put("authors" , authors);
			replaceHash.put("title" , title);
			replaceHash.put("pubDate" , pubDate);
			replaceHash.put("edition", edition);
			replaceHash.put("seriesName", seriesName);
			replaceHash.put("issueId", issueId );
			replaceHash.put("otherCitationDetails", otherCitationDetails);
			replaceHash.put("pageNumber", pageNumber );
			replaceHash.put("isbn", isbn );
			replaceHash.put("issn", issn );
			replaceHash.put("classSystem", classSystem );
			
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
