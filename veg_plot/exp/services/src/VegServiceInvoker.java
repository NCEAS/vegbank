/**
 *    '$RCSfile: VegServiceInvoker.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-04 00:37:11 $'
 *   '$Revision: 1.2 $'
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
import java.util.*;
import java.sql.*;
import electric.registry.Registry;
import electric.registry.RegistryException;
import examples.publish.IExchange;

public class VegServiceInvoker 
  {
  public static void main( String[] args )
    throws Exception
    {
		if (args.length < 1) 
		{
			System.out.println("USAGE: java VegServiceInvoker query-token(s) ");
		}
		else
		{
			// get all the query tokens
			String queryString = "";
			for (int i=0; i< args.length ; i++)
			{
				queryString = queryString.trim() + " " + (args[i].trim());
			}
			System.out.println(queryString);
			
    	// bind to web service whose WSDL is at the specified URL
    	String url = "http://localhost:8004/vegbank/exchange.wsdl";
    	ExchangeInterface exchange = (ExchangeInterface) Registry.bind( url, ExchangeInterface.class );

    	// invoke the web service as if it was a local java object
    	Vector v  = exchange.getPlotAccessionNumber(queryString);
   
	 		// print out the results
			for (int i=0; i< v.size() ; i++)
			{
				Hashtable h = (Hashtable)v.elementAt(i);
				System.out.println( h.toString()  );
			}
		}
    }
  }
