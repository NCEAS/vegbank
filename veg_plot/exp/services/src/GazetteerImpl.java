/**
 *    '$RCSfile: GazetteerImpl.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.1 $'
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

import java.util.Hashtable;
import java.util.StringTokenizer;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
/**
 * Class that implements the GazetteerInterface, containing all the logic for
 * Place Name lookups and the other relevant gazetteer-spatial functions
 * defined in the aforementioned interface.  To support the functions in this
 * class, access has to be available to an JDBC-Compliant RDBMS containing a
 * gazetter represented as a single table following the convetions: <br>
 * <br>
 * gazateer_pk      | integer <br>               
 * statealphacode   | character varying(50)  <br> 
 * featurename      | character varying(50)  <br>
 * featuretype      | character varying(50)  <br>
 * countyname       | character varying(50)  <br>
 * statenumbercode  | character varying(50)  <br>
 * countynumbercode | character varying(50)  <br>
 * primarylatitude  | numeric(30,6)          <br>
 * primarylongitude | numeric(30,6)          <br>
 * elevation        | numeric(30,6)          <br>
 * population       | numeric(30,6)          <br>
 */

public class GazetteerImpl implements GazetteerInterface {

  // the attributes of the 'PLACE' 
  private Hashtable PLACE;
  private int  PLACE_gazateer_pk;              
  private String  PLACE_statealphacode;  
  private String  PLACE_featurename;
  private String  PLACE_featuretype; 
  private String  PLACE_countyname;
  private String  PLACE_statenumbercode;  
  private String  PLACE_countynumbercode;
  private double  PLACE_primarylatitude;        
  private double  PLACE_primarylongitude;          
  private double  PLACE_elevation;  
  private double  PLACE_population;
  
  private int GAZETTER_PK; //primary-key for place in the gazetter db
  private Connection conn;
  private String GAZETTEER_DB_CONN_STRING="jdbc:postgresql://localhost/gazateer";
  private Statement query;
	private ResultSet results;

  private String GAZETTEER_REFERENCE_LOCATIONS="gazateer_reference_locations.txt";

  /**
   * method that takes as input the x,y (long, latitude) location and returns
   * the nearest place to that location
   * @see GazetteerInterface for further description
   */
  public Hashtable lookupNearestPlace(double longitude, double latitude ) {
    System.out.println("GazetteerImpl > lookupNearestPlace called");
    // implementing code goes here
    GAZETTER_PK = calculateNearestPlace(longitude, latitude);
    PLACE = getPlace(GAZETTER_PK);
    return PLACE;
  }

  /**
   * method that returns the place object based on a primary-key value
   * corresponding to the place in the gazateer database
   */
   private Hashtable getPlace(int gazetteer_pk) {
    System.out.println("GazetteerImpl > getPlace called");
    try {
		  StringBuffer sqlb = new StringBuffer();
      conn = this.getConnection(GAZETTEER_DB_CONN_STRING);
      query = conn.createStatement();
      results = null;
      sqlb.append("SELECT ");
      sqlb.append("gazateer_pk ,");
      sqlb.append("statealphacode ,");
      sqlb.append("featurename ,");
      sqlb.append("featuretype ,");
      sqlb.append("countyname ,");
      sqlb.append("statenumbercode ,");
      sqlb.append("countynumbercode ,");
      sqlb.append("primarylatitude ,");
      sqlb.append("primarylongitude ,");
      sqlb.append("elevation ,");
      sqlb.append("population");
      sqlb.append(" from gazateer where gazateer_pk =  "+gazetteer_pk);
      //System.out.println(sqlb.toString());
      results = query.executeQuery( sqlb.toString() );
      PLACE = constructPlaceObject(results);
      //System.out.println("place: " +PLACE);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return PLACE;
   }

  /**
   * method to construct the place hashtable object
   */
   private Hashtable constructPlaceObject(ResultSet results)
   {
    System.out.println("GazetteerImpl > constructPlaceObject called");
    PLACE = new Hashtable();
		try {
      while (results.next()) 
			{
        PLACE_gazateer_pk = results.getInt(1);
        PLACE_statealphacode = results.getString(2);  
        PLACE_featurename = results.getString(3);
        PLACE_featuretype = results.getString(4); 
        PLACE_countyname = results.getString(5);
        PLACE_statenumbercode = results.getString(6);
        PLACE_countynumbercode = results.getString(7);
        PLACE_primarylatitude = results.getDouble(8);        
        PLACE_primarylongitude = results.getDouble(9);          
        PLACE_elevation = results.getDouble(10);  
        PLACE_population = results.getDouble(11);

        //set the attributes into the hashtable
        PLACE.put("gazateer_pk", ""+PLACE_gazateer_pk);
        PLACE.put("statealphacode", ""+PLACE_statealphacode);
        PLACE.put("featurename", ""+PLACE_featurename );
        PLACE.put("featuretype",""+PLACE_featuretype );
        PLACE.put("countyname",""+PLACE_countyname );
        PLACE.put("statenumbercode",""+PLACE_statenumbercode );
        PLACE.put("countynumbercode", ""+PLACE_countynumbercode );
        PLACE.put("primarylatitude", ""+PLACE_primarylatitude);
        PLACE.put("primarylongitude", ""+PLACE_primarylongitude);
        PLACE.put("elevation", ""+PLACE_elevation );
        PLACE.put("population", ""+PLACE_population);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return PLACE;
   }
   
  /**
   * method that calculates the nearest location based on the data stored in the
   * gazetteer database reference locations and returns the primary key value
   * for the place name that is closest
   * @param longitude -- the longitudinal geocoordinate for a location
   * @param latitude -- the latitudinal geocoordinate for a location
   * @return GAZETTER_PK -- the primary key value for the corresponding place in
   * the gazetteer database.
   */
   private int calculateNearestPlace(double longitude, double latitude ) {
    System.out.println("GazetteerImpl > calculateNearestPlace called for: "
    + longitude + " " + latitude);
    int minimum_dist_id = 0; // the id associated with the minimum id
    double minimum_dist = 10e20; //the current minimum distance
    int count=0;
    try {
      BufferedReader in = new BufferedReader(new FileReader(GAZETTEER_REFERENCE_LOCATIONS));
      String line;
      while((line = in.readLine()) != null) 
      {
        StringTokenizer t = new StringTokenizer(line.trim(), " \r\n\t");
        // the file should be id, longitude, latitude like:
        // 140775      -113.986940       33.339720
        String id = t.nextToken();
        double ref_long = (new Double(t.nextToken())).doubleValue();
        double ref_lat = (new Double(t.nextToken())).doubleValue();
        //make the first record the minimum distance 
        double dist = calculateDistance(longitude, ref_long, latitude, ref_lat);
        if ( count == 0 ) {
          minimum_dist = dist;
          minimum_dist_id=Integer.parseInt(id);
        }
        // else check to see if the current distance is less
        else {
          if (dist < minimum_dist) {
            minimum_dist = dist;
            minimum_dist_id=Integer.parseInt(id);
          }
        }
        //System.out.println(id + " " + ref_lat +" "+ref_long+" "+dist);
        count++;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    GAZETTER_PK = minimum_dist_id;
    return GAZETTER_PK;
   }

  /**
   * method that returns the distance between two sets of x,y locations using
   * the Pythagorean theorem
   * @param x -- the double value for the x location
   * @param ref_x -- the double value for the reference x location
   * @param y -- the double value for the y location
   * @param ref_y -- the double value for the reference y location
   * @return distance -- the distance between the two in cartesian coordinates
   */
   private double calculateDistance(double x, double ref_x, double y, double ref_y) {
    double distance =0;
    //System.out.println("x: " + x +"ref_x: " + ref_x);
    //System.out.println("y: " + y +"ref_y: " + ref_y);
    try {
      distance = Math.sqrt( ( (Math.pow((x-ref_x), 2)) + (Math.pow((y-ref_y), 2)) ) );
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return distance;
   }
 
 /**
	* method that will return a database connection for use with the database
  * @param driverStrig -- the JDBC connection string like: 
  *   jdbc:postgresql://vegbank.nceas.ucsb.edu/vegbank
	* @return conn -- an active connection
	*/
	private Connection getConnection(String driverString)
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//String driverString = "jdbc:postgresql://vegbank.nceas.ucsb.edu/vegbank";
			System.out.println("GazetteerImpl > connecting to: " + driverString   );
			c = DriverManager.getConnection(driverString, "harris", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}

  /**
   * method used to generate the GAZETTEER_REFERENCE_LOCATIONS which is a
   * file to store the locations for all the points in the gazetteer database
   * @see -- the 'main' method of this class which is the method that calls this
   * one 
   */
   public void generateGazetteerRefLocations()
   {
    System.out.println("GazetteerImpl > generating a file with the reference \n"
    +" locations of all the points in the gazetteer database ");
    //open the printwriter to: GAZETTEER_REFERENCE_LOCATIONS
    PrintWriter out;
    try {
      out = new PrintWriter(new FileWriter(GAZETTEER_REFERENCE_LOCATIONS));
      try {
		    StringBuffer sqlb = new StringBuffer();
        conn = this.getConnection(GAZETTEER_DB_CONN_STRING);
        query = conn.createStatement();
        results = null;
        sqlb.append("SELECT ");
        sqlb.append("gazateer_pk ,");
        sqlb.append("primarylatitude ,");
        sqlb.append("primarylongitude ");
        sqlb.append(" from gazateer  ");
  //      sqlb.append(" from gazateer where gazateer_pk < 1200000 ");
        results = query.executeQuery( sqlb.toString() );
        while (results.next()) 
			  {
          int gazateer_pk = results.getInt(1);
          double lat = results.getDouble(2);
          double lon = results.getDouble(3);
          //write to the file 
          out.println(gazateer_pk+" "+lon+" "+lat);
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
   }

  /**
   * main method used to generate the GAZETTEER_REFERENCE_LOCATIONS which is a
   * file to store the locations for all the points in the gazetteer database
   */
  public static void main(String[] args) 
  {
    //assume that we should generate the gazetteer reference locations file
    GazetteerImpl gaz = new GazetteerImpl();
    gaz.generateGazetteerRefLocations();
  } 
   
}
