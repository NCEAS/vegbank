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
				StringBuffer sb = handleVegCommunitySubmittal(params);
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
	 * relation to the submittal proccess of a new community
	 * 
	 * @param params -- a hashtable storing the inpt parameters
	 * @return sb -- A string buffer with the response by the database
	 * and application about the success of the submittal to the database
	 */
	private StringBuffer handleVegCommunitySubmittal(Hashtable params)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String conceptCode = "";
			String conceptLevel =  (String)params.get("communityLevel");
			String commName = (String)params.get("communityName");
			String dateEntered = "";
			String parentCommunity = "";
			String allianceTransName = "";
			String partyName = (String)params.get("partyName");
			sb = commLoader.insertGenericCommunity( conceptCode, conceptLevel, commName,
			dateEntered, parentCommunity, allianceTransName, partyName );
			
		}
		catch( Exception e ) 
		{
			System.out.println("Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		return(sb);
		
			
	}
	
	
	


}
