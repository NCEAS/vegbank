package servlet.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import servlet.util.ServletUtility;
import xmlresource.utils.XMLparse;


/**
 * Servlet to
 *
 * <p>Valid parameters are:<br><br>
 * 
 *
 * 
 * 
 */

public class FrameworkServlet extends HttpServlet 
{
	
	//required classes
	ServletUtility util = new ServletUtility();
	XMLparse parse = new XMLparse();
	
	//class variables
	private Document doc;
	private Hashtable pluginNameClass = new Hashtable();
	private Object pluginObj = null;
	
	
	/**
	 * constructor method
	 */
	public FrameworkServlet()
	{
		System.out.println("init: FrameworkServlet");
		//the xml config document
		System.out.println("FrameworkServlet > reading xml config file");
		doc = parse.getDocument("frameworkservlet-config.xml");
		//method to initialize the plugin names and corresponding classes
		//--not sure if this really belongs here -- might want to write this
		//data to the browser
		getPluginNames();
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
			
				System.out.println("FrameworkServlet > IN PARAMETERS: " + params.toString() + "\n" );
				
				//figure out the action
				String action = getAction( params );
				if ( getPluginActionViable(action) == true)
				{	 
					System.out.println("FrameworkServlet > action requested: " + action  + " \n");
					//now from the action get the approprite plugin
					String pluginClassName = getPluginClass(action);
					if ( pluginClassName == null || pluginClassName.equals("null") )
					{
						System.out.println("FrameworkServlet > no plugin available ");
					}
					else
					{
					 try
    			 { 
							//String className = "servlet.plugin.spatial.CoordinateTransform";
							//create a generic object of the type specified in the config file.
      				pluginObj = util.createObject(pluginClassName);
							StringBuffer sb =((ServletPluginInterface)pluginObj).servletRequestHandler(action,  params);
							//System.out.println("FrameworkServlet > plugin results: "+ sb.toString() );
							out.println( sb.toString() );
    			 }
    			catch(Exception e)
    			{
    			  System.out.println("Error getting plugin: " + e.getMessage());
						e.printStackTrace();
    			}
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
	 * method that takes, as input the parameter 'action' and returns the
	 * class name for the plugin
	 */
	 private String getPluginClass(String actionParameter)
	 {
		 String s = null;
		 try
		 {		 
		 	if ( actionParameter.equals("coordinateTransform") )
		 	{
		 		s="servlet.plugin.spatial.CoordinateTransform";
		 	}
		 
			//this will initiate the loading
		 	else if ( actionParameter.equals("initPlotLoad") )
		 	{
		 		s="servlet.plugin.db.PlotDBInsert";
		 	}
		 
		 	//this will actually load the plots
			 else if ( actionParameter.equals("loadTNCData") )
		 	{
		 		s="servlet.plugin.db.PlotDBInsert";
		 	}
			//this will look up the names of the plants in the database
			 else if ( actionParameter.equals("plantlookup") )
		 	{
		 		s="servlet.plugin.PlantNameList";
		 	}
			else
			{
				s = null;
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
		 }
		 return(s);
	 }
	 
	
	
	/**
	 * method that returns the desired action requested by the user
	 *
	 * @param params - hashtable with all the servlet parameters
	 *
	 */
	 private String getAction(Hashtable params)
	 {
		 String s = null;
		 s=(String)params.get("action");
		 return(s);
	 }

	 
	/**
	 * method that returns if the action requeste of this servlet has a 
	 * corresponding plugin
	 *
	 * @param action - the action requested by the client
	 *
	 */
	 private boolean getPluginActionViable(String action)
	 {
		 return(true);
	 }
	 
	 /**
	  * method that will update the class level variables related
		* to the pluginNames and related classes from the framework
		* servlet configuration xml file
		*/
	 private void getPluginNames()
	 {
		 try
		 {
			 String name = parse.getNodeValue(doc, "pluginName");
			 String className = parse.getNodeValue(doc, "pluginClass");
			 System.out.println( name +" "+ className);
		 }
		 catch (Exception e)
		 {
			 System.out.println( "Exception: " + e.getMessage() );
		 }
		 
	 }
	 
}
