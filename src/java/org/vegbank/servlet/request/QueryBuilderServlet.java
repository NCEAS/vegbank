package org.vegbank.servlet.request;

/*
 *  '$RCSfile: QueryBuilderServlet.java,v $'
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-10-11 21:20:10 $'
 *  '$Revision: 1.4 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.GetURL;
import org.vegbank.common.utility.ServletUtility;


/**
 *
 * Servlet to interactively build the query with the client
 * The servlet stores the components of the nested query until
 * the user decides that it is time to issue the query at which
 * point the query is issued to the 'DataRequestServlet'
 *
 * <p>Valid parameters are:<br><br>
 *
 * REQUIRED PARAMETERS
 * @param queryParameterType -- {append, commit}
 * @param requestDataType -- {vegPlot, plantTaxon, vegCommunity}
 * @param requestDataFormatType -- includes: html, xml, text
 * @param clientType -- includes: browser, clientApplication
 * @param resultType -- {summary, full}
 * @param criteria -- the query criteria
 * @param operator -- the query operator
 * @param value -- the query value
 *
 *
 *
 *	'$Author: farrell $'
 *  '$Date: 2003-10-11 21:20:10 $'
 *  '$Revision: 1.4 $'
 *
 */

public class QueryBuilderServlet extends HttpServlet
{

	static ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private static final 	String DataRequestServletURL = rb.getString("requestparams.DataRequestServletURL");
	public DataRequestServlet drs = new DataRequestServlet();
	public ServletUtility su = new ServletUtility();
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
			String requestDataFormatType = null;
			String clientType = null;

			//the format that is requested by the browser is required -- get it
			if ( parameterHash(request).containsKey("requestDataFormatType") == true )
			{
				requestDataFormatType= ( parameterHash(request).get("requestDataFormatType").toString() );
			}
			//get the client type; depending on this parameter the client may get
			//slightly different responses
			if ( parameterHash(request).containsKey("clientType") == true )
			{
				clientType= ( parameterHash(request).get("clientType").toString() );
			}

			//make sure that the client passes the type of query
			//parameters to the servlet
			if ( parameterHash(request).containsKey("queryParameterType") == true )
			{
				//if the client requests to append , via queryParameterType
				//the the input query elements to the current standing query
				if ( parameterHash(request).get("queryParameterType").toString().equals("append") )
				{

					System.out.println( appendQueryAttributes( parameterHash(request)).toString() );
					System.out.println("QueryBuilderServlet > query vector: "+queryVector.toString() );
					//reprint the client html with the updated aggregate query
					// in the text area
					out.println( updateQueryClient(queryVector, requestDataFormatType, clientType ) );
				}
				//else run the query
				else if (parameterHash(request).get("queryParameterType").toString().equals("commit"))
				{
					System.out.println("QueryBuilderServlet >  issueing the query to the DataRequestServlet");
					//pass the query vector to the DataRequestServlet
					out.println( submitExtendedQuery(queryVector, requestDataFormatType, clientType) );
					//make a new occurence of the queryVector
					queryVector = new Vector();
				}
			}
			else
			{
				System.out.println("QueryBuilderServlet > didn't pass the query parameter type");
			}

		}
		catch( Exception e )
		{
			System.out.println("Exception QueryBuilderServlet > " + e.getMessage());
			e.printStackTrace();
		}
	}



 /**
  * method to pass the extended query to the DataRequestServlet
	* and get the results which are to be passed back to the client
	* browser
	*
	* @param queryVector -- the vector containg the query
	* @param requestDataFormatType -- html, xml, txt
	* @param clientType -- browser or app
  */
  private String submitExtendedQuery(
  	Vector queryVector,
		String requestDataFormatType,
		String clientType)
  {
    String htmlResults = "test";
    try
    {
      //create the parameter string to be passed to the DataRequestServlet --
			//this first part has the data request type stuff
      StringBuffer sb = new StringBuffer();
      sb.append("?clientType="+clientType+"&requestDataFormatType="
			+requestDataFormatType+"&requestDataType=vegPlot&queryType=extended&resultType=identity");
      for (int i=0; i<queryVector.size(); i++)
      {
          sb.append("&");
          //get each instance query
          Hashtable queryInstanceHash = (Hashtable)queryVector.elementAt(i);
          String criteria = queryInstanceHash.get("criteria").toString();
          String operator = queryInstanceHash.get("operator").toString();
          String value = queryInstanceHash.get("value").toString();

					// fix the value if the user uses the sql wildcard use the
					// escape charaters for the percent sign
					if ( value.startsWith("%") || value.indexOf("%") >= 1 )
					{
						value = value.replace('%', ' ').trim();
						value = "%25"+value+"%25";
					}

          sb.append("operator="+operator+"&criteria="+criteria+"&value="+value);
      }
      //connect to the DataRequestServlet
			//String uri = "http://dev.nceas.ucsb.edu/harris/servlet/DataRequestServlet"
			String uri = DataRequestServletURL+"/"+sb.toString().trim();
			System.out.println("QueryBuilderServlet > sending http: " + uri);
      int port=80;
      String requestType="POST";
      htmlResults = GetURL.requestURL(uri);


		}
    catch( Exception e )
    {
      System.out.println("Exception :  " + e.getMessage());
			e.printStackTrace();
    }
    return(htmlResults);
  }


 /**
	* method to re-stream the client html
	* with the textarea showing the aggregated
	* query updated
	*/
	private String updateQueryClient(Vector aggregateQuery,
	String requestDataFormatType,  String clientType)
	{
		String clientHtml = null;
		StringBuffer sb = new StringBuffer();
		clientHtml = (rb.getString("queryBuilderHtml"));
		try
		{
			//su.fileVectorizer("../../html/plotQueryBuilder.html");
			Vector vectoredFile = su.fileVectorizer(clientHtml);
			for (int i=0; i<vectoredFile.size(); i++)
			{
				//check for the line(s) for
				//updating
				if ( vectoredFile.elementAt(i).toString().indexOf("replaceAggregateQuery") >0 )
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
					sb.append( vectoredFile.elementAt(i)+"\n" );
				}
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception :  " + e.getMessage());
			e.printStackTrace();
		}
		return(sb.toString() );
	}


	/**
	 * method to stick the parameters from the client
	 * into a hashtable and then pass it back to the
	 * calling method
	 * @param request -- the http request
	 */
	private Hashtable parameterHash(HttpServletRequest request)
	{
		Hashtable params = new Hashtable();
		try
		{
			Enumeration anenum =request.getParameterNames();
			//System.out.println("QueryBuilderServlet > contacted");
 			while (anenum.hasMoreElements())
			{
				String name = (String) anenum.nextElement();
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
			System.out.println("Exception:  " + e.getMessage());
			e.printStackTrace();
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
				System.out.println("QueryBuilderServlet > did not find the correct params");
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception  " + e.getMessage());
		}
		return(queryVector);
	}

}
