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
 * 
 * Servlet to interactively build the query with the client
 *
 * <p>Valid parameters are:<br><br>
 * 
 *
 * 
 * @version @version@
 * @author John Harris
 * 
 */

public class QueryBuilderServlet extends HttpServlet 
{

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
public DataRequestServlet drs = new DataRequestServlet();
public servletUtility su = new servletUtility();
static Vector queryVector = new Vector();


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
			System.out.println("QueryBuilderServlet contacted");
			//make sure that the client passes the type of query 
			//parameters to the servlet
			if ( parameterHash(request).containsKey("queryParameterType") == true )
			{
				System.out.println("queryParameterType> "
					+parameterHash(request).get("queryParameterType").toString() 
				);
				//if the client requests to append , via queryParameterType 
				//the the input query elements to the current standing query
				if ( parameterHash(request).get("queryParameterType").toString().equals("append") )
				{
					System.out.println("appendingParameters to current query");
					out.println( appendQueryAttributes( parameterHash(request)) );
					System.out.println("query vector: "+queryVector.toString() );
					//reprint the client html with the updated aggregate query
					// in the text area
					out.println( updateQueryClient(queryVector) );
				}
				//else run the query
				else if (parameterHash(request).get("queryParameterType").toString().equals("commit"))
				{
					//make a new occurence of the queryVector
					queryVector = new Vector();
					System.out.println("issueing the query to the DataRequestServlet");
				}
			}
			else
			{
				System.out.println("didn't pass the parameter type");
			}
			out.println(parameterHash(request).toString());
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in: DataRequestServlet.main "
			+" first try - reading parameters "
			+e.getMessage());
		}
	}
	
		
	/**
	 * method to re-stream the client html
	 * with the textarea showing the aggregated
	 * query updated
	 */
	private String updateQueryClient (Vector aggregateQuery) 
	{
		String clientHtml = null;
		StringBuffer sb = new StringBuffer();
		try 
		{
			su.fileVectorizer("../../html/plotQueryBuilder.html");
			for (int i=0; i<su.outVector.size(); i++) 
			{
				//check for the line(s) for
				//updating
				if ( su.outVector.elementAt(i).toString().indexOf("replaceAggregateQuery") >0 )
				{
					sb.append( queryVector.toString() );
				}
				else
				{
					sb.append( su.outVector.elementAt(i) );
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed :  "
			+e.getMessage());
		}
		return(sb.toString() );
	}
	
	
	
	
	
	/**
	 * method to stick the parameters from the client 
	 * into a hashtable and then pass it back to the 
	 * calling method
	 */
	private Hashtable parameterHash (HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
			System.out.println("QueryBuilderServlet contacted");
 			while (enum.hasMoreElements()) 
			{
				String name = (String) enum.nextElement();
				String values[] = request.getParameterValues(name);
				if (values != null) 
				{
					for (int i=0; i<values.length; i++) 
					{
						params.put(name,values[i]);
					}
				}
 			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in:  "
			+" first try - reading parameters "
			+e.getMessage());
		}
		return(params);
	}
		
	/**
	 * method to build the aggregate of 
	 * queries that should be issued to the 
	 * DataRequestServlet
	 */
	private String appendQueryAttributes(Hashtable attributeParameters) 
	{
		String queryAggregate=null;
		try 
		{
			queryAggregate=attributeParameters.toString();
			queryVector.addElement( queryAggregate.toString() );
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in:  "
			+e.getMessage());
		}
		return(queryAggregate);
	}

}
