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
 *	'$Author: harris $'
 *  '$Date: 2001-06-21 21:23:04 $'
 *  '$Revision: 1.7 $'
 * 
 */

public class QueryBuilderServlet extends HttpServlet 
{

ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
public DataRequestServlet drs = new DataRequestServlet();
public servletUtility su = new servletUtility();
public GetURL gurl = new GetURL();
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
					out.println( appendQueryAttributes( parameterHash(request)).toString() );
					System.out.println("query vector: "+queryVector.toString() );
					//reprint the client html with the updated aggregate query
					// in the text area
					out.println( updateQueryClient(queryVector) );
				}
				//else run the query
				else if (parameterHash(request).get("queryParameterType").toString().equals("commit"))
				{
					System.out.println("issueing the query to the DataRequestServlet");
					//pass the query vector to the DataRequestServlet
					out.println( submitExtendedQuery(queryVector) );
					//make a new occurence of the queryVector
					queryVector = new Vector();
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
	 * method to pass the extended query to the DataRequestServlet
	 * using the GetUrl Class 
	 *
	 */
	private String submitExtendedQuery (Vector queryVector) 
	{
		String htmlResults = "test";
		try 
		{
			//put these into the properties file
			String servlet = "/harris/servlet/DataRequestServlet";
			String protocol = "http://";
	  	String host = "dev.nceas.ucsb.edu";
		
			//set the query into the properties
			Properties parameters = new Properties();
			parameters.setProperty("requestDataType", "vegPlot");
			parameters.setProperty("queryType", "extended");
			parameters.setProperty("resultType", "summary");
			for (int i=0; i<queryVector.size(); i++) 
			{
				Hashtable queryInstanceHash = (Hashtable)queryVector.elementAt(i);
				parameters.setProperty("criteria", queryInstanceHash.get("criteria").toString() );
				parameters.setProperty("operator", queryInstanceHash.get("operator").toString());
				parameters.setProperty("value", queryInstanceHash.get("value").toString());
			}
		
			
			//request the data from the servlet and print the results to the system	
			htmlResults = gurl.requestURL(servlet, protocol, host, parameters);
			//System.out.println(htmlResults);
		
		}
		catch( Exception e ) 
		{
			System.out.println("** failed :  "
			+e.getMessage());
		}
		return(htmlResults);
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
					//add the aggregated query to the stringbuffer 
					//instead of the taged line in the vector
					for (int ii=0; ii<queryVector.size(); ii++)
					{
						sb.append( queryVector.elementAt(ii).toString() +"\n");
					}
				}
				else
				{
					sb.append( su.outVector.elementAt(i)+"\n" );
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
	 * DataRequestServlet the structure of the 
	 * returned vector is: a vector that contains 
	 * a hash table for each query instance in the aggregate
	 * the instanse hash table will contain the criteria, 
	 * operator and the value
	 */
	private Vector appendQueryAttributes(Hashtable attributeParameters) 
	{
		Hashtable queryInstance = new Hashtable();
		try 
		{
			//make sure that the correct parameters are passed 
			//into this method
			if (attributeParameters.containsKey("criteria") == true)
			{
				String criteriaValue=attributeParameters.get("criteria").toString();
				String operator = attributeParameters.get("operator").toString();
				String value = attributeParameters.get("value").toString();
				//add these elements to the instance hash
				queryInstance.put("criteria", criteriaValue);
				queryInstance.put("operator", operator);
				queryInstance.put("value", value);
				//add the instance hash to the queryVector
				//that is a static variable in this class
				queryVector.addElement( queryInstance );
			}
			else 
			{
				System.out.println("did not find the correcte parameters");
			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in:  "
			+e.getMessage());
		}
		return(queryVector);
	}

}
