package vegbank.publish;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import electric.registry.Registry;
import electric.registry.RegistryException;
import examples.publish.IExchange;
import javax.servlet.*;
import javax.servlet.http.*;


public class ExchangeServlet extends HttpServlet 
{


 public void init(ServletConfig config) throws ServletException 
	{
		System.out.println("ExchangeServlet > init");
		
		// Startup the webservice
		try
		{
			Publish.main(null);
		}
		catch (Exception e)
		{
			System.out.println("Could not start Webservice");
			e.printStackTrace();
		}
		
		super.init(config);
  }
		
	/**
	 * this is the main get method
	 */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			
			PrintWriter out = response.getWriter();
			Hashtable h1 = this.parameterHash(request);
			String queryString = (String)h1.get("querytoken");
			System.out.println("ExchangeServlet > " + queryString);
			
    	// bind to web service whose WSDL is at the specified URL
    	String url = "http://localhost:8004/vegbank/exchange.wsdl";
			System.out.println("ExchangeServlet > binding to: " + url);
    	ExchangeInterface exchange = (ExchangeInterface) Registry.bind( url, ExchangeInterface.class );

    	// invoke the web service as if it was a local java object
    	Vector v  = exchange.getPlotAccessionNumber(queryString);
	 		this.printResults(v, out);
    }
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	/**
	 * method to print the attributes out as html to the browser
	 */
	 private void printResults(Vector v, PrintWriter out)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html>");
		 sb.append("<head>");
		 sb.append("<link href=\"http://numericsolutions.com/includes/default.css\" type=\"text/css\" rel=\"stylesheet\">");
		 sb.append("</head>");
		 sb.append("<body bgcolor=\"white\">");
		 
		 sb.append("Number of Unique Matches: " + v.size() +"<br> <br>" );
		 
		 sb.append("<table width=\"500\"> \n");
		 sb.append("<tr>");
		 sb.append("<span class=\"category\">");
		 sb.append("<td width=40> PLOT </td> <td width=20>LOCATION</td> <td width=20> STATE </td>");
		 sb.append("</span>");
		 sb.append("</tr>");
		 int row = 0;
		 for (int i=0; i< v.size() ; i++)
			{
				Hashtable h = (Hashtable)v.elementAt(i);
				String accessionNumber = (String)h.get("accessionNumber");
				// TOKENIZE THE  ACCESSION NUMBER TO GET THE PLOT ID
				StringTokenizer st = new StringTokenizer(accessionNumber, ".");
				String buf = st.nextToken();
				 buf = st.nextToken();
				String plotId = st.nextToken();
				//System.out.println("# " + plotId);
				String longitude = (String)h.get("longitude");
				String latitude = (String)h.get("latitude");
				String state = (String)h.get("state");
				//System.out.println( h.toString()  );
				if ( row == 0)
				{
					sb.append("<tr bgcolor=\"#C0D3E7\" >");
					// THE ACCESSION NUMBER AND THE COMPREHENSIVE VIEW 
					sb.append("<td> <span class=\"itemsmall\">  "+accessionNumber+"  ");
					sb.append(" <a href=\"/framework/servlet/DataRequestServlet?requestDataFormatType=html&clientType=browser&requestDataType=vegPlot&resultType=full&queryType=simple&plotId="+plotId+"\"><img alt=\"Comprehensive view\" src=\"/vegbank/images/comprehensive_sm.gif\" border=\"0\" align=\"left\"></a>");
					sb.append(" &nbsp;");
					sb.append(" <a href=\"/framework/servlet/DataRequestServlet?requestDataFormatType=html&clientType=browser&requestDataType=vegPlot&resultType=summary&queryType=simple&plotId="+plotId+"\"><img alt=\"Comprehensive view\" src=\"/vegbank/images/report_sm.gif\" border=\"0\" align=\"left\"></a>");
					sb.append(" </span> </td>");
					
					sb.append("<td> <span class=\"itemsmall\"> ");
					sb.append(" <a href=\"/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&longitude="+longitude+"&latitude="+latitude+"\">");
					sb.append(" <img alt=\"Location\" src=\"/vegbank/images/small_globe.gif\" border=\"0\" align=\"center\"> </a> </span> </td> ");
					sb.append("<td> <span class=\"itemsmall\"> "+state+"</span> </td> ");
					row = 1;
				}
				else
				{
					sb.append("<tr bgcolor=\"white\" >");
						// THE ACCESSION NUMBER AND THE COMPREHENSIVE VIEW 
					sb.append("<td> <span class=\"itemsmall\">  "+accessionNumber+"  ");
					sb.append(" <a href=\"/framework/servlet/DataRequestServlet?requestDataFormatType=html&clientType=browser&requestDataType=vegPlot&resultType=full&queryType=simple&plotId="+plotId+"\"><img alt=\"Comprehensive view\" src=\"/vegbank/images/comprehensive_sm.gif\" border=\"0\" align=\"left\"></a>");
					sb.append(" &nbsp;");
					sb.append(" <a href=\"/framework/servlet/DataRequestServlet?requestDataFormatType=html&clientType=browser&requestDataType=vegPlot&resultType=summary&queryType=simple&plotId="+plotId+"\"><img alt=\"Comprehensive view\" src=\"/vegbank/images/report_sm.gif\" border=\"0\" align=\"left\"></a>");
					sb.append(" </span> </td>");
					
					sb.append("<td> <span class=\"itemsmall\"> ");
					sb.append(" <a href=\"/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&longitude="+longitude+"&latitude="+latitude+"\">");
					sb.append(" <img alt=\"Location\" src=\"/vegbank/images/small_globe.gif\" border=\"0\" align=\"center\"> </a> </span> </td> ");
					sb.append("<td> <span class=\"itemsmall\"> "+state+"</span> </td> ");
					
					row = 0;
				}
				sb.append("</tr>");
			}
			sb.append("</table> \n");
			sb.append("</body>");
		 sb.append("</html>");
		 out.println(sb.toString() );
	 }
	
		
	/**
	 * method to stick the parameters from the client 
	 * into a hashtable and then pass it back to the 
	 * calling method
	 */
	public Hashtable parameterHash (HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
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
	
}
