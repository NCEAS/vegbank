import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

public class UpdateCoordinates 
{
	//the connection poling class
	static LocalDbConnectionBroker lb;
	public String tmp_utm_file = "utm_coord";
	public String tmp_ll_file = "ll_coord";
	
	// these connection pooling variables
	//are used in many of the methods within 
	//this class
	public Connection conn=null;
	public Statement query = null;
	public ResultSet results = null;
	
	public static void main(String[] args) 
	{
		String action = null;
		//test for correct usages
		if (args.length !=1) 
		{
			System.out.println("USAGE: java UpdateCoordinates <action> \n");
			System.out.println("	actions:	update -- will update the lat long in DB \n"+
				"	shapefile -- will make a points shape file including the origin points "
				+" for all the plots in the database");
			System.exit(0);
		}
		else
		{
			action =args[0].trim();
		}
		//if the action is 'update'
		if 	(action.equals("update"))
		{
			try 
			{
				UpdateCoordinates uc = new UpdateCoordinates();
				System.out.println( uc.convertUTM() );
			}
			catch( Exception e ) 
			{
				System.out.println(" callUtility "+e.getMessage());
				e.printStackTrace();
			}
		}
		//if the action is 'shapefile' -- dump to shape file
		else if (action.equals("shapefile") );
		{
			try 
			{
				UpdateCoordinates uc = new UpdateCoordinates();
				System.out.println( uc.dumpLocationsShapeFile() );
			}
			catch( Exception e ) 
			{
				System.out.println(" callUtility "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		
	}

	
	/**
	 * method to dump all the locations from the 
	 * plots database into and esri shape file 
	 * to be viewed in a GIS
	 */
	private String  dumpLocationsShapeFile()
	{	
			try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			conn = lb.manageLocalDbConnectionBroker("getConn");
			query = conn.createStatement ();
		}
		catch (Exception e) 
		{
			System.out.println("failed making database connection " + 
			e.getMessage());
			e.printStackTrace();
		}
		
		//get all the latitude and longitude coordinates
		Hashtable latLongResults = getDBLatLong ();
		
		Vector plotIdVec = (Vector)latLongResults.get("plotId");
		Vector latitudeVec =  (Vector)latLongResults.get("latitude");
		Vector longitudeVec =  (Vector)latLongResults.get("longitude");
		
		//print to file all the lat and longitude 
		//values in the database
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream("nvcLatLong.txt"));
			for (int i=0; i<plotIdVec.size(); i++)
			{
				//make sure that the results are not null
				if ( latitudeVec.elementAt(i) != null )
				{
					out.println( longitudeVec.elementAt(i)+" " + latitudeVec.elementAt(i) );
				}
			}
			//now convert this file to a points shape file
			String responseLine = null;
			Process listener;  
			DataInputStream nativeResponse;
			listener = Runtime.getRuntime().exec("./nvcToShape"
				+" nvcLatLong.txt");
			nativeResponse = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in4=new BufferedReader(new InputStreamReader(nativeResponse));
			
		}
		catch (Exception e) 
		{
			System.out.println("failed " + 
			e.getMessage());
			e.printStackTrace();
		}
			
		try 
		{
			query.close();
			conn.close();
			lb.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e) 
		{
			System.out.println("failed making database connection " + 
			e.getMessage());
			e.printStackTrace();
		}
		return("success in dumping shape file");
	}
	
	
	/**
	 * method to get thelatitude and longitudes stored
	 * as origins in the plots database
	 *
	 * @return latLongResults -- the hash table storing three
	 *		corresponding vectors 
	 *			1] the plotId
	 *			2] the latitude
	 *			3] the longitude
	 *
	 */
	private Hashtable getDBLatLong ()
	{
		//the hash table that stores the data
		//formatted as three vectors for the 
		//plot ids, xCoord, yCoord
		Hashtable latLongResults = new Hashtable();
		Vector plotIdVec = new Vector();
		Vector latitudeVec = new Vector();
		Vector longitudeVec = new Vector();
		try 
		{
			//create the query to get the databack from the database
			results=query.executeQuery("select plot_Id, plotOriginLat, plotOriginLong "
			+" from plot where plot_id > 100");
			while (results.next())
			{
				//make sure that a lat and long are actually
				//passed back from the database
				String plotId = results.getString(1);
				String latitude = results.getString(2);
				String longitude = results.getString(3);
				plotIdVec.addElement(plotId);
				latitudeVec.addElement(latitude);
				longitudeVec.addElement(longitude);
			}

			latLongResults.put("plotId", plotIdVec);
			latLongResults.put("latitude", latitudeVec);
			latLongResults.put("longitude", longitudeVec);
		}
		catch (Exception e) 
		{
			System.out.println("failed making transaction " + 
			e.getMessage());
			e.printStackTrace();
		}
		return(latLongResults);
	}
	
	/**
	 * method that converts all the database locations 
	 * from utm coordinates to latitude and longitude
	 */
	private String convertUTM ()
	{	
		//start the connection pooling
		try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			conn = lb.manageLocalDbConnectionBroker("getConn");
			query = conn.createStatement ();
		}
		catch (Exception e) 
		{
			System.out.println("failed making database connection " + 
			e.getMessage());
			e.printStackTrace();
		}

		//test the coord transform -- make fake coords
		String x = "616070";
		String y = "4897536";
		
		//grab the database elements with utm coordinates 
		//and stick into vectors
		Hashtable utmDBResults = getDBUtm("zone15");
		Vector plotIdVec = (Vector)utmDBResults.get("plotId");
		Vector xCoordVec =  (Vector)utmDBResults.get("xCoord");
		Vector yCoordVec =  (Vector)utmDBResults.get("yCoord");
		
		//update each row in the database
		for (int i=0; i<plotIdVec.size(); i++)
		{
			
//			System.out.println( xCoordVec.elementAt(i).toString() );
			//get the latitude / longitude back from the coordinate
			//transform method
			Hashtable geoCoordHash=convertUTMtoLatLong(xCoordVec.elementAt(i).toString(), 
				yCoordVec.elementAt(i).toString() );
			
			String curLat=geoCoordHash.get("latitude").toString();
			String curLong=geoCoordHash.get("longitude").toString();
		//convert the utm coords
			
			//put the lat long into the database
			putDBLatLong(plotIdVec.elementAt(i).toString(), 
				curLat, curLong) ;
		}
		
			try 
		{
			query.close();
			conn.close();
			lb.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e) 
		{
			System.out.println("failed making database connection " + 
			e.getMessage());
			e.printStackTrace();
		}
		
		//System.out.println( convertUTMtoLatLong(x, y).toString()) ;		
		//System.out.println( getDBUtm("zone15").toString() );
		return("success");
	}
	
	
	/**
	 * method to put the lat / long coordinates back into the 
	 * database
	 *
	 * @param plotId
	 * @param latitude
	 * @param longitude
	 * @return success
	 */
	 private String putDBLatLong (String plotId, String latitude, String longitude)
	{
		try 
		{
			//update the latitude then the longitude seperately 
			//for testing purposes
			query.executeUpdate("update plot "
				+"set plotoriginlat ="+latitude+" where plot_id like '"+plotId+"'");
				
			query.executeUpdate("update plot "
				+"set plotoriginlong ="+longitude+" where plot_id like '"+plotId+"'");
			
		}
		catch (Exception e) 
		{
			System.out.println("failed making transaction " + 
			e.getMessage());
			e.printStackTrace();
		}
		return("successfuly update the database");
	}
	
	/**
	 * method to get the utm coordinates from the database
	 *
	 * @return utmResults -- the hash table storing three
	 *		corresponding vectors 
	 *			1] the plotId
	 *			2] the xCoord
	 *			3] the yCoord
	 *
	 */
	private Hashtable getDBUtm (String utmZone)
	{
		//the hash table that stores the data
		//formatted as three vectors for the 
		//plot ids, xCoord, yCoord
		Hashtable utmResults = new Hashtable();
		Vector plotIdVec = new Vector();
		Vector xCoordVec = new Vector();
		Vector yCoordVec = new Vector();
		
	
		try 
		{
			//create the query to get the databack from the database
			results=query.executeQuery("select plot_Id, xCoord, yCoord from plot");
			while (results.next())
			{
				String plotId = results.getString(1);
				String xCoord = results.getString(2);
				String yCoord = results.getString(3);
				plotIdVec.addElement(plotId);
				xCoordVec.addElement(xCoord);
				yCoordVec.addElement(yCoord);
				//System.out.println(plotId+" "+xCoord+" "+yCoord);
				
			}
//			query.close();
//			conn.close();
//			lb.manageLocalDbConnectionBroker("destroy");
			utmResults.put("plotId", plotIdVec);
			utmResults.put("xCoord", xCoordVec);
			utmResults.put("yCoord", yCoordVec);
		
		}
		catch (Exception e) 
		{
			System.out.println("failed making transaction " + 
			e.getMessage());
			e.printStackTrace();
		}
		return(utmResults);
	}
	
	/**
	 * method which passes back a lat / long coordinate 
	 * set, using as input a set of utm coordinates
	 *
	 * @param xCoord - a string containing the x coordinate
	 * @param yCoord - a string containing the y coordinate
	 * @return hashtable containing the 
	 *		latitude:value
	 *		longitude:value
	 */
	private Hashtable convertUTMtoLatLong (String xCoord, String yCoord)
	{
		Hashtable llCoordinates = new Hashtable();
		String longitude = null;
		String latitude = null;
		try 
		{ 
			//write the coordinates to the file system
			PrintStream out = new PrintStream(new FileOutputStream(tmp_utm_file));
			out.println(xCoord+" "+yCoord);
			
			//set up for the external proccess using the grass library
			//to do the conversion
			String responseLine = null;
			Process listener;  
			DataInputStream nativeResponse;
			listener = Runtime.getRuntime().exec("./coord_transform.sh"+" "
				+tmp_utm_file+" "
				+tmp_ll_file);
			nativeResponse = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in4=new BufferedReader(new InputStreamReader(nativeResponse));
			
			while ((responseLine = in4.readLine()) !=null) 
			{
				//make sure that there is something on the 
				//line that is returned
				if (responseLine.length()>0)
				{
					//tokenize the input into two strings --
					//a lat and long
					StringTokenizer st=new StringTokenizer(responseLine, " \t");
					longitude = st.nextToken();
					latitude = st.nextToken();
					llCoordinates.put("latitude", latitude);
					llCoordinates.put("longitude", longitude);
  				System.out.println(responseLine.trim());
				}
  		}
		}
		catch ( Exception e ) 
		{
			System.out.println("failed in: LegacyDataFormatter.readTNCData "
			+e.getMessage());
		}
		return(llCoordinates);
	}



}

