/**
 *    '$RCSfile: ExchangeServlet.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris, Gabe Farrell
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.6 $'
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 */
package vegbank.publish;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.*;

import electric.registry.Registry;
import electric.registry.RegistryException;
import examples.publish.IExchange;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;


/**
 * Class to be used as a test harness for the vegbank web services subsystem
 * which allows a user to invoke the public methods available in as vegbank
 * registered webservices via a web browser.  This class is intended to
 * illustrate the use of publicly available web services at vegbank.org so that
 * interested parties can better understand how to integrate vegbank data into
 * their applications.  This source code is derived from similar examples at:
 *
 * http://www.themindelectric.com <br>
 * http://arcweb.esri.com/arcwebonline <br>
 *
 * <p>Valid parameters are:<br>
 * querytoken=querystring -- querytoken is a single querystring for which a
 * query is issued accross many of the attributes stored in the vegbank system
 * analogous to the "google-like" query for a matching keyword in web pages
 *
 */
public class ExchangeServlet extends HttpServlet 
{
  private String GAZATEER_SERVLET; //this is the url to a gazateer
  private String STYLESHEET; //url of the css stylesheet
  private String IMAGE_URL; // the location to the image directory
  private String VEGBANK_SERVICE_URL; // the url of the vegbank wsdl
  private ResourceBundle resources;
  
  public void init(ServletConfig config) throws ServletException 
  {
    System.out.println("ExchangeServlet > init");
    
    resources = ResourceBundle.getBundle("vegbankservice");
    GAZATEER_SERVLET = resources.getString("GAZATEER_SERVLET");
    STYLESHEET =resources.getString("STYLESHEET");
    IMAGE_URL = resources.getString("IMAGE_URL");
    VEGBANK_SERVICE_URL = resources.getString("VEGBANK_SERVICE_URL");
    
    System.out.println("ExchangeServlet > GAZATEER_SERVLET: " + GAZATEER_SERVLET);
    System.out.println("ExchangeServlet > STYLESHEET: " + STYLESHEET);
    System.out.println("ExchangeServlet > IMAGE_URL: " + IMAGE_URL);
    System.out.println("ExchangeServlet > VEGBANK_SERVICE_URL: " + VEGBANK_SERVICE_URL);

    super.init(config);
  }
		
	/**
	 * handles the doGet method request from the HTTP client
   * the API supported by this servlet is as follows:
   *
   * querytoken -- a string representation of a key-word for querying vegbank,
   * whereby the plot accession numbers are returned
   *
   * lookupnearestplotlocation -- a plot accession id which will be used to look
   * up the nearest location from a gazateer database
   * 
   * action=lookupnearestlocation -- returns the nearest place for the
   * corresponding parameters:
   *  latitude -- the latitude in geocoordinates
   *  longiude -- the longitude in geocoordinates
   *
   *  @param - request the HttpServletRequest object 
   *  @param - response the HttpServletResponse object
	 */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{		
			PrintWriter out = response.getWriter();
			Hashtable h = this.parameterHash(request);
			String queryString = (String)h.get("querytoken");
			String lookupNearestPlotLocation = (String)h.get("lookupnearestplotlocation");
			String action = (String)h.get("action");
      if (lookupNearestPlotLocation == null && queryString == null && action ==
      null) {
        this.handleEmptyRequest(out);
      }
      else
      {
        // bind to web service whose WSDL is at the specified URL
			  System.out.println("ExchangeServlet > binding to: " + VEGBANK_SERVICE_URL);
    	  ExchangeInterface exchange = (ExchangeInterface)Registry.bind(VEGBANK_SERVICE_URL, ExchangeInterface.class );
        
        if ( queryString != null ) {
			    System.out.println("ExchangeServlet > query string: " + queryString);
    	    // invoke the web service as if it was a local java object
    	    Vector v  = exchange.getPlotAccessionNumber(queryString);
			    System.out.println("ExchangeServlet > service return vector size: " + v.size() );
	 		    this.printQueryResults(v, out); 
        }
        else if ( lookupNearestPlotLocation  != null ) {
          System.out.println("ExchangeServlet > lookupNearestPlotLocation: " + lookupNearestPlotLocation);
          Hashtable nearestPlace = exchange.getNearestPlaceForPlot(lookupNearestPlotLocation);
          this.printPlaceQueryResults( nearestPlace, out);
          //System.out.println("ExchangeServlet > place: " + nearestPlace);
        }
        else if ( action != null ) {
          System.out.println("ExchangeServlet > action: " + action);
          double lat = (new Double((String)h.get("latitude"))).doubleValue();
          double lon = (new Double((String)h.get("longitude"))).doubleValue();
          Hashtable nearestPlace =exchange.getNearestPlaceForLocation(lat, lon);        
          this.printPlaceQueryResults( nearestPlace, out);
        }
        else {
          this.handleEmptyRequest(out);
        }
      }
    }
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
      e.printStackTrace();
		}
	}

  /**
   * method that prints out to the browser an html fragment containing
   * information corresponding to a 'place' returned from the gazetteer-related
   * services 
   * @param place -- the hashtable representation of a place -- see the
   * GazetterInterface class
   * @param out -- the printwriter object
   */
   private void printPlaceQueryResults(Hashtable place, PrintWriter out) {
    out.println("<html>");
    out.println("<br>" + place + "</br>");
    out.println("</html>");
   }

	
  /**
   * method that is used to handle the case where the request object does not
   * correspond to the correct api defined in the header for the doGet method
   * @param out -- the printwriter object
   */
   private void handleEmptyRequest(PrintWriter out) {
    out.println("<html>");
    out.println("query object does not have the correct parameters");
    out.println("</html>");
   }

  /**
   * method to print the attributes returned from the web service as an html
   * page fragment.
   * @param v -- the vector containing the resultant plots
   * @param out -- the PrintWriter object to write to the browser
	 */
  private void printQueryResults(Vector v, PrintWriter out) {
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
    for (int i=0; i< v.size() ; i++) {
      Hashtable h = (Hashtable)v.elementAt(i);
      String accessionNumber = (String)h.get("accessionNumber");
      System.out.println("ExchangeServlet > processing results: " + accessionNumber);
      String plotId = accessionNumber;
      String longitude = (String)h.get("longitude");
      String latitude = (String)h.get("latitude");
      String state = (String)h.get("state");
      sb.append("<tr bgcolor=\"#C0D3E7\" >");
      sb.append("<td> <span class=\"itemsmall\">  "+accessionNumber+"  ");
      sb.append(" </span> </td>");
		  sb.append("<td> <span class=\"itemsmall\"> ");
      sb.append(" <a href=\""+GAZATEER_SERVLET+"?action=mapsinglecoordinate&longitude="+longitude+"&latitude="+latitude+"\">");
      sb.append(" <img alt=\"Location\" src=\""+IMAGE_URL+"small_globe.gif\" border=\"0\" align=\"center\"> </a> </span> </td> ");
      sb.append("<td> <span class=\"itemsmall\"> "+state+"</span> </td> ");
			sb.append("</tr>");
      }
    sb.append("</table> \n");
		sb.append("</body>");
    sb.append("</html>");
		out.println(sb.toString() );
    out.close();
	 }
	
		
	/**
	 * method to stick the parameters from the client 
	 * into a hashtable and then pass it back to the 
	 * calling method
   * @param request -- the http request object passed to the servlet
   * @returns -- a hashtable with the name value pairs in the request
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
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
		}
		return(params);
	}
	
}
