package servlet.plugin.spatial;

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

import servlet.util.ServletUtility;
import org.w3c.dom.Document;
import xmlresource.utils.XMLparse;


/**
 * Servlet to
 *
 * <p>Valid parameters are:<br><br>
 * 
 * REQUIRED PARAMETERS
 * @param queryType -- includes: simple, compound and extended
 
 * 
 * 
 * ADDITIONAL PARAMETERS
 * @param taxonName - name of a taxon occuring in a plot <br>
 *
 * 
 * 
 */

public class SpatialViewerServlet extends HttpServlet 
{
	//required classes
	ServletUtility util = new ServletUtility();
	
	
	/**
	 * constructor method
	 */
	public SpatialViewerServlet()
	{
		System.out.println("SpatialViewerServlet loaded...");
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
			//enumeration is needed for those
			//cases where there are multiple values 
			// for a given parameter
				Enumeration enum =request.getParameterNames();
				Hashtable params = new Hashtable();
				params = util.parameterHash(request);
			
				out.println("SpatialViewerServlet: IN PARAMETERS: " + params.toString() + "\n" );
			
			}
		catch( Exception e ) 
		{
			System.out.println("** failed in: DataRequestServlet.main "
			+" first try - reading parameters "
			+e.getMessage());
		}
	}
	
	 
}
