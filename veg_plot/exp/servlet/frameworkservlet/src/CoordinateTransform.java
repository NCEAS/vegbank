package servlet.plugin.spatial;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class CoordinateTransform implements ServletPluginInterface
{
	
	private String inType = null; //the input coord type
	private String outType = null;  //the desired output type
	private String xCoord = null;
	private String yCoord = null;
	private String zone = null; // the utm input zone
	
	public CoordinateTransform(  ) 
	{
		System.out.println( "CoordinateTransform  > accessed");
		System.out.println("CoordinateTransform  >  init in directory: "
		+ System.getProperty("user.dir"));
	}
	
	
	/** 
	* this method is designed to be called from the 
	* framework java servlet which will pass an 'action' and a hashtable
	* containing the other parameters that were passed to the servlet.  
	* all the plugins are accedd from the framework servlet through this 
	* method
	
	* this specific servlet plugin will require a an action that that equals
	* "coordinateTransform"
	*
	* and the parameters have to have the following elements:
	* intype -- incomming data coordinate type
	* outtype -- the outgoing data coordinate type
	* x -- the x coordinate
	* y -- the y coordinate
	* zone -- the utm zone
	* 
	*/
	public StringBuffer servletRequestHandler(String action, Hashtable params)
	{
		StringBuffer sb = new StringBuffer();
		System.out.println("CoordinateTransform  >  Action: " + action);
		if (! action.trim().equals("coordinateTransform") )
		{
			sb.append( getRequiredParameters() );
		}
		else
		{
			//System.out.println( "Parameters: " + params.toString() );
	  	try 
			{
			
				inType = (String)params.get("intype");
				outType = (String)params.get("outtype");
				xCoord = (String)params.get("x");
				yCoord = (String)params.get("y");
				zone = (String)params.get("zone");
			
				System.out.println("CoordinateTransform  > intype: " + inType);
				System.out.println("CoordinateTransform  > outtype: " + outType);
				System.out.println("CoordinateTransform  > xCoord: " + xCoord);
				System.out.println("CoordinateTransform  > yCoord: " + yCoord);
				System.out.println("CoordinateTransform  > zone: " + zone);
			
				//if any of these are null let the requestor know the parameters 
			
			}
			catch (Exception e) 
			{
				System.out.println( "Exception: " + e.getMessage() );
			}
			
		
			//get the geocoordinates back from the servlet
			Hashtable geoCoords = convertUTMtoLatLong( xCoord, yCoord, zone);
			sb.append( (String)geoCoords.get("latitude") +" \n");
			sb.append( (String)geoCoords.get("longitude") );
		}
		
		return(sb);
	}
	
	
	/**
	 * method that returns the required input parameters into this class
	 */
	 private String getRequiredParameters()
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("<html>");
		 sb.append("<br>required parameters for plugin: CoordinateTransform <br>");
		 sb.append("<br> action = coordinateTransform<br>");
		 sb.append("<br> intype = the input coordinate type {utm} <br>");
		 sb.append("<br> outtype = the output coordinate type {geocoordinates}<br>");
		 sb.append("<br> x = the x coordinate <br>");
		 sb.append("<br> y = the y coordinate <br>");
		 sb.append("<br> zone = the utm zone <br>");
		 sb.append("<br>  <br>");
		 sb.append("</html>");
		 return(sb.toString());
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
	private Hashtable convertUTMtoLatLong (String xCoord, String yCoordn, String zone)
	{
		Hashtable llCoordinates = new Hashtable();
		String longitude = null;
		String latitude = null;
		try 
		{ 
			//write the coordinates to the file system
      System.out.println("CoordinateTransform  > writing input coordinates: " +xCoord+" "+yCoord+" to: tmp_utm_file");
			PrintStream out = new PrintStream(new FileOutputStream("tmp_utm_file"));
			out.println(xCoord+" "+yCoord);
			
			//set up for the external proccess using the grass library
			//to do the conversion
			String responseLine = null;
			Process listener;  
			DataInputStream nativeResponse;
			listener = Runtime.getRuntime().exec("/usr/local/devtools/jakarta-tomcat/webapps/vegbank/WEB-INF/lib/coord_transform.sh tmp_utm_file tmp_ll_file "+zone);
			nativeResponse = new DataInputStream(new BufferedInputStream(listener.getInputStream()));
			BufferedReader in4=new BufferedReader(new InputStreamReader(nativeResponse));
			
			while ((responseLine = in4.readLine()) !=null) 
			{
				System.out.println("CoordinateTransform  >  converting");
				//make sure that there is something on the 
				//line that is returned
				if (responseLine.length()>0)
				{
          System.out.println("CoordinateTransform  > grass shell script response: " + responseLine);
					//tokenize the input into two strings --
					//a lat and long
					StringTokenizer st=new StringTokenizer(responseLine);
					longitude = st.nextToken();
					latitude = st.nextToken();
					llCoordinates.put("latitude", latitude);
					llCoordinates.put("longitude", longitude);
  				System.out.println("CoordinateTransform  >  response: " + responseLine.trim());
				}
  		}
		}
		catch ( Exception e ) 
		{
			System.out.println("Exception " + e.getMessage());
      e.printStackTrace();
		}
		return(llCoordinates);
	}
	
	
	/**
	 * main method for testing 
	 */
	public static void main(String[] args) 
	{
	}

}
