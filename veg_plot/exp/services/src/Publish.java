/**
 *    '$RCSfile: Publish.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.3 $'
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

import electric.registry.Registry;
import electric.server.http.HTTP;

/**
 * Application to start the GLUE (www.themindelectric.com) server, making the
 * functions defined in the ExchangeInterface available, and to generate the
 * WSDL at: <br>
 * http://localhost:8004/vegbank/exchange.wsdl
 */
public class Publish
  {
  public static void main( String[] args )
    throws Exception
    {
      // start a web server on port 8004, accept messages via /glue
      HTTP.startup( "http://localhost:8004/vegbank" );

      // publish an instance of Exchange
      Registry.publish( "exchange", new Exchange() );
    }
  }
