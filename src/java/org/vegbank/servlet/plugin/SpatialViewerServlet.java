package org.vegbank.servlet.plugin;

/**
 * '$RCSfile: SpatialViewerServlet.java,v $'
 *
 * Purpose:
 *
 * '$Author: farrell $'
 * '$Date: 2003-10-11 21:20:10 $'
 * '$Revision: 1.3 $'
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vegbank.common.utility.ServletUtility;


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
				Enumeration anenum =request.getParameterNames();
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
