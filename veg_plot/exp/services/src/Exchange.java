/**
 *    '$RCSfile: Exchange.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.5 $'
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

/**
 * Class that implements the ExchangeInterface, and contains the functions which
 * are available via the web services application server.
 */
public class Exchange implements ExchangeInterface
{

  private String VEGBANK_DB_CONN_STRING; //the jdbc conn string for vegbank
  private String GAZETTEER_PLUGIN; //class that implements gazateer interface
  private Connection conn;
  private Statement query;
	private ResultSet results;
  private ResourceBundle resources;
  
  /**
   * constructor method
   */
  public Exchange() {
    System.out.println("Exchange > constructor called");
    try {
    resources = ResourceBundle.getBundle("vegbankservice");
    VEGBANK_DB_CONN_STRING = resources.getString("VEGBANK_DB_CONN_STRING");
    GAZETTEER_PLUGIN = resources.getString("GAZETTEER_PLUGIN");
    System.out.println(VEGBANK_DB_CONN_STRING);
		//test the connection to the database
    conn = this.getConnection(VEGBANK_DB_CONN_STRING);
    //System.out.println(conn.getCatalog());
    conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
       
  /**
   * method that takes as input the geocoodinates of a location and returns
   * the nearest place registered in a gazetteer
   * @param latitude -- the double value for a latitude 
   * @param longitude -- the double value for a longitude
   * @return place -- a hashtable representation of a place object, for
   * details on the keys stored in the place object see the
   * GazetteerInterface class.
   */
   public Hashtable getNearestPlaceForLocation(double latitude, double longitude) {
    Hashtable place = new Hashtable();
    //use the 3rd party gazateer to get the location info
    //create a generic object of the type specified in the config file.
    try { 
      Object pluginObj = createObject(GAZETTEER_PLUGIN);
      place = ((GazetteerInterface)pluginObj).lookupNearestPlace(longitude,  latitude);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return place; 
   }

  /**
   * method that takes as input a plot accession number and returns the
   * nearest place registered in a gazateer.  The implimentation of this 
   * method consists of two parts:
   * 1] get the location (long. and lat) from the vegbank system 
   * 2] use a 3rd party webservice gazateer to get the nearest named place to
   * the location identified in 1.
   * @param plotId -- the plot accession number like: VB.1110
   * @return place -- a hashtable representation of a place containing the
   * following elements:
   * 1] placename 
   * 2] state
   * 3] latitude 
   * 4] longitude
   */
   public Hashtable getNearestPlaceForPlot(String plotId)
   {
    double latitude = 0;
    double longitude = 0;
    String state;
		StringBuffer sqlb = new StringBuffer();
		System.out.println("Exchange > getNearestPlaceForPlot called");
    Hashtable place = new Hashtable();
    //get the longitudes and latitudes from vegbank for the plotId
     try {
      conn = this.getConnection(VEGBANK_DB_CONN_STRING);
      query = conn.createStatement();
      results = null;
      sqlb.append("SELECT latitude, longitude, state FROM ");
      sqlb.append("PLOT where PLOT_ID = ");
      sqlb.append(" (SELECT PLOT_ID from OBSERVATION where OBSERVATION_ID = "+plotId+")" );
      results = query.executeQuery( sqlb.toString() );
		  while (results.next()) 
			{
				latitude = results.getDouble(1);
				longitude = results.getDouble(2);
        // THIS IS A HACK TO MAKE SURE THAT THE LONGITUDE IS CORRECT, MEANING
        // THAT IN THE USA, WHICH ALL POINTS ARE TO BE CONSIDERED, ARE *NEGATIVE
        if (longitude > 0) {
          longitude = longitude*(-1);
        }
				state = results.getString(3);
        System.out.println(latitude + " "  + longitude + " " + state);
      }
     }
     catch (Exception e) {
      e.printStackTrace();
     }
     //use the 3rd party gazateer to get the location info
     //create a generic object of the type specified in the config file.
     try { 
      Object pluginObj = createObject(GAZETTEER_PLUGIN);
      place = ((GazetteerInterface)pluginObj).lookupNearestPlace(longitude,  latitude);
     }
     catch (Exception e) {
      e.printStackTrace();
     }
    return place;
   }
   
  /**
   * creates an object of a type className. this is used for instantiating
   * plugins
   * @param className -- the fully qualified name of the class to instantiate
   * @return object -- the instantiated class
   */
  public static Object createObject(String className) 
        throws InstantiationException, 
        IllegalAccessException,
        ClassNotFoundException
  {
    Object object = null;
    try 
    {
        Class classDefinition = Class.forName(className);
        object = classDefinition.newInstance();
    } 
    catch (InstantiationException e) 
    {
        throw new InstantiationException("Error instantiating plugin: " + e);
    } 
    catch (IllegalAccessException e) 
    {
        throw new IllegalAccessException("Error accessing plugin: " + e);
    } 
    catch (ClassNotFoundException e) 
    {
        throw new ClassNotFoundException("Plugin " + className + " not " +
                                         "found: " + e);
    }
    return object;
  } 
  
  
  /**
	 * method that takes a plant name and returns
	 * a summary of all the plots in vegbank that 
	 * contain that plant
	 *
   * @param plantName -- the name of the plant 
	 * @return vector containing a hashtable -- with the following elements:
	 * 1] accessionNumber
	 * 2] latitude
	 * 3] longitude
	 * 4] state
	 */
  public Vector getPlotAccessionNumber( String plantName )
	{
		System.out.println("Exchange > getPlotAccessionNumber called ");
		System.out.println("Exchange > getting vegbank plot accession numbers");
		// TRIM OFF ANY WHITE SPACE
		plantName = plantName.trim();
		int resultNum = 0;
		Vector v = new Vector();
		StringBuffer sb = new StringBuffer();
		try
		{
			System.out.println("Exchange > query token: " + plantName);
			conn = this.getConnection(VEGBANK_DB_CONN_STRING);
			query = conn.createStatement();
			results = null;
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
	* @return conn -- an active connection
	*/
	private Connection getConnection(String driverString)
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//String driverString = "jdbc:postgresql://vegbank.nceas.ucsb.edu/vegbank";
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
