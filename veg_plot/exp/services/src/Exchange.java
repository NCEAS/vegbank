/**
 *    '$RCSfile: Exchange.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-04 00:37:11 $'
 *   '$Revision: 1.4 $'
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
public class Exchange implements ExchangeInterface
{
  
	public Vector getPlotAccessionNumber( String plantName )
	{
		System.out.println("Exchange > getting vegbank plot accession numbers");
		// TRIM OFF ANY WHITE SPACE
		plantName = plantName.trim();
		int resultNum = 0;
		Vector v = new Vector();
		StringBuffer sb = new StringBuffer();
		try
		{
			System.out.println("Exchange > query token: " + plantName);
			Connection conn = this.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = null;
			//create and issue the query --
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("SELECT accession_number, plotoriginlat, plotoriginlong, state ");
			sqlBuf.append(" from PLOTSITESUMMARY where plot_id in ");
			sqlBuf.append(" ( select plot_id from plotspeciessum where ");
			sqlBuf.append(" upper(authorNameId) like ");
			sqlBuf.append(" '%"+plantName.toUpperCase()+"%')");
			sqlBuf.append(" or upper(accession_number) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(surfgeo) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(plottype) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(plotarea) like '%"+plantName.toUpperCase()+"%'");
			// sqlBuf.append(" or upper(slopeaspect) like %"+plantName.toUpperCase()+"%'");
			// sqlBuf.append(" or upper(slopegradient) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(hydrologicregime) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(soildrainage) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(currentcommunity) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(effortlevel) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(hardcopylocation) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(soiltype) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(leaftype) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(physionomicclass) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(authorplotcode) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(state) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(country) like '%"+plantName.toUpperCase()+"%'");
			sqlBuf.append(" or upper(authorobscode) like '%"+plantName.toUpperCase()+"%'");
			System.out.println("Exchange > sql: " + sqlBuf.toString());
			results = query.executeQuery( sqlBuf.toString() );
			//retrieve the results
			while (results.next()) 
			{
				resultNum++;
				Hashtable h = new Hashtable();
				String accessionNumber = results.getString(1);
				String latitude = results.getString(2);
				String longitude = results.getString(3);
				String state = results.getString(4);
				h.put("accessionNumber", accessionNumber);
				h.put("latitude", ""+latitude);
				h.put("longitude", ""+longitude);
				h.put("state", ""+state);
				v.addElement(h);
			}
			//remember to close the connections etc..
	 	}
		catch (Exception e) 
		{
			System.out.println("failed " +e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Exchange > returning " +resultNum+ " results");
		return(v);
	}
	
		
	/**
	* method that will return a database connection for use with the database
	*
	* @return conn -- an active connection
	*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");

			String driverString = "jdbc:postgresql://vegbank.nceas.ucsb.edu/vegbank";
			System.out.println("Exchange > connecting to: " + driverString   );
			c = DriverManager.getConnection(driverString, "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	
}
