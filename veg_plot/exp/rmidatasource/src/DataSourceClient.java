import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*; 
import java.rmi.*;


/** 
 * class that mimics the functionality of the PlotDataSource class except that 
 * this class actually makes an RMI connection to the PlotDataSource class
 * server running on a different machine.  This class is needed because there
 * are certain types of plot data sources (e.g., MS ACCESS) that run from a 
 * specific computing platform that we want to integrate with software running
 * elsewhere, also there is a need for the clients to access data stored on a
 * serever running the vegbank databases and this tool will allow for an
 * alternative to HTTP for accessing that data
 *
 */
public class DataSourceClient
{
	
	private String serverHost =  null;
	private String serverClass = "DataSourceServer";
	private String url = null;
	DataSourceServerInterface source;
	
	/**
	 * this constructor method makes a connection to the 
	 * data source server 
	 *
	 * @param hostName -- the resolveable name or IP of host running the 
	 * 		RMI server 
	 */
	public DataSourceClient(String hostName)
	{
		try
		{
			System.out.println("DataSourceCleint > connecting to server: " +hostName );
			this.serverHost = hostName;
			this.url = "//" + hostName + "/"+serverClass;
			//list the bound serveres to this host
			String[] remoteObjects = Naming.list(url);  
			for (int c = 0; c < remoteObjects.length; c++) 
			{    
				System.out.println("DataSourceCleint > names bound to: "
				+ serverHost +" - " 
				+ remoteObjects[c]  );
			}
			
			//look up the inplementation class
			this.source = (DataSourceServerInterface)Naming.lookup(url);
			
			//make a test
			//System.out.println("DataSourceCleint > " + source.getPlotNames() );
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
	/**
	 * method that returns the names of the plots stored in this data 
	 * source 
	 */
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		try
		{
			v =  source.getPlotNames( );
		}
			catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}

	/**
	 * method that returns the name of the project corresponding to 
	 * the input plot
	 */
	public String getProjectName(String plot)
	{
		try
		{
			return( "test project" );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
//#START EDITIONS
	/**
	 * method that retuns the description of a project that corresponds
	 * to the input plot
	 */
	public String getProjectDescription(String plot)
	{
		try
		{
			return( "test description" );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}

		/**
	 * method that retuns the start date of the project that corresponds
	 * to the input plot
	 */
	public String getProjectStartDate(String plot)
	{
		try
		{
			return( "01-JUN-1999" );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}

	/**
	 * method that retuns the stop date of the project that corresponds
	 * to the input plot
	 */
	public String getProjectStopDate(String plot)
	{
		try
		{
			return( "20-JUN-1999" );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}


	//retuns the input plot name
	public String getAuthorPlotCode(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getObservationCode(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getSamplingMethod(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getObservationStartDate(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	public String getObservationDateAccuracy(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getSoilDepth(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getPercentRock(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getPercentLitter(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public String getPercentWater(String plotName)
	{
		try
		{
			return( plotName );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		try
		{
			v = source.getUniqueStrataNames(plotName);
			System.out.println("DataSourceClient > strata: " + v.toString() );
			return( v );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(v);
	}
	
	
	public Vector getPlantNames(String plotName)
	{
		Vector v = new Vector();
		return(v);
	}
	
	
//#STOP EDDITIONS
	

	
	//returns the easting
	public String getXCoord(String plotName)
	{
		try
		{
			return( source.getXCoord(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the northing
	public String getYCoord(String plotName)
	{
		try
		{
			return( source.getYCoord(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	
	
		//returns the latitude
	String getLatitude(String plotName)
	{
		String s = null;
		try
		{
		s = source.getLatitude(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }

		return(s);
	}
	
	//returns the longitude
	String getLongitude(String plotName)
	{
		String s = null;
		try
		{
	s = source.getLongitude(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the geographic zone
	String getUTMZone(String plotName)
	{
		String s = null;
		try
		{
		s = source.getUTMZone(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the plot shape
	String getPlotShape(String plotName)
	{
		String s = null;
		try
		{
		s = source.getPlotShape(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the plot area
	String getPlotArea(String plotName)
	{
		String s = null;
		try
		{
		s = source.getPlotArea(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the state for the current plot
	String getCommunityName(String plotName)
	{
		String s = null;
		try
		{
		s = source.getCommunityName(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	

	//returns the state in which the plot exists
	String getState(String plotName)
	{
		String s = null;
		try
		{
		s = source.getState(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName)
	{
		String s = null;
		try
		{
		s = source.getHydrologicRegime(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the topo position
	String getTopoPosition(String plotName)
	{
		String s = null;
		try
		{
		s = source.getTopoPosition(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the slope aspect
	String getSlopeAspect(String plotName)
	{
		String s = null;
		try
		{
		s = source.getSlopeAspect(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns yje slope gradient
	String getSlopeGradient(String plotName)
	{
		String s = null;
		try
		{
		s = source.getSlopeGradient(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the surficial geology
	String getSurfGeo(String plotName)
	{
		String s = null;
		try
		{
		s = source.getSurfGeo(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//retuns the country
	String getCountry(String plotName)
	{
		String s = null;
		try
		{
		s = source.getCountry(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the size of the stand -- extensive etc..
	String getStandSize(String plotName)
	{
		String s = null;
		try
		{
	s = source.getStandSize(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the location as described by the author
	String getAuthorLocation(String plotName)
	{
		String s = null;
		try
		{
		s = source.getAuthorLocation(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the landForm
	String getLandForm(String plotName)
	{
		String s = null;
		try
		{
		s = source.getLandForm(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//retuns the elevation
	String getElevation(String plotName)
	{
		String s = null;
		try
		{
		s = source.getElevation(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//returns the elevation accuracy
	String getElevationAccuracy(String plotName)
	{
		String s = null;
		try
		{
		s = source.getElevationAccuracy(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//return the confidentiality reason -- not null
	String	getConfidentialityReason(String plotName)
	{
		String s = null;
		try
		{
		s = source.getConfidentialityReason(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	//return the confidentiality status -- not null 0-6
	String getConfidentialityStatus(String plotName)
	{
		String s = null;
		try
		{
		 s = source.getConfidentialityStatus(plotName);
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
	
	
	
	
	/**
	 * a test method that prints the salient data source attributes 
	 * @param plot -- the plot identifier
	 */
	 private void printDBVariables(String plot)
	 {
		 try
		 {
			 	System.out.println("DataSourceCleint > x: " + this.getXCoord(plot) );
				System.out.println("DataSourceCleint > y: " + this.getYCoord(plot) );
				
				System.out.println("DataSourceCleint > lat: " + this.getLatitude(plot) );
				System.out.println("DataSourceCleint > long: " + this.getLongitude(plot) );
				System.out.println("DataSourceCleint > authLoc: " + this.getAuthorLocation(plot) );
				
				//#START
				System.out.println("DataSourceCleint > community name : " + this.getCommunityName(plot) );
				System.out.println("DataSourceCleint > confidential reason: " + this.getConfidentialityReason(plot) );
				System.out.println("DataSourceCleint > confidentail status: " + this.getConfidentialityStatus(plot) );
				System.out.println("DataSourceCleint > country: " + this.getCountry(plot) );
				System.out.println("DataSourceCleint > elevation: " + this.getElevation(plot) );
				System.out.println("DataSourceCleint > elevation accuracy: " + this.getElevationAccuracy(plot) );
				System.out.println("DataSourceCleint > hydro regime: " + this.getHydrologicRegime(plot) );
				System.out.println("DataSourceCleint > land form: " + this.getLandForm(plot) );
				System.out.println("DataSourceCleint > plot area: " + this.getPlotArea(plot) );
				System.out.println("DataSourceCleint > plot shape: " + this.getPlotShape(plot) );
				System.out.println("DataSourceCleint > slope aspect: " + this.getSlopeAspect(plot) );
				System.out.println("DataSourceCleint > slope gradient: " + this.getSlopeGradient(plot) );
				System.out.println("DataSourceCleint > stand size: " + this.getStandSize(plot) );
				System.out.println("DataSourceCleint > state: " + this.getState(plot) );
				System.out.println("DataSourceCleint > surf geo: " + this.getSurfGeo(plot) );
				System.out.println("DataSourceCleint > topo position: " + this.getTopoPosition(plot) );
				System.out.println("DataSourceCleint > utm zone: " + this.getUTMZone(plot) );
		 		//#STOP
		 }
		 catch(Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	
	/**
	 * method to unbind from the server
	 */
		private void closeConnection()
		{
			try
			{
				//source = (DataSourceServerInterface)Naming.unbind(this.url);
				//source.unbind(this.url);
			}
			catch(Exception e)
			{
				System.out.println("Exception: " + e.getMessage() );
			}
		}
	 
	 
	/**
	 * main method for testing
	 */
	public static void main(String argv[]) 
	{
		//get a file from the server
		if(argv.length != 2) 
		{
			try
			{
				String hostServer =  argv[0];
				DataSourceClient client = new DataSourceClient(hostServer);
				System.out.println("DataSourceCleint > " + client.getPlotNames() );
				String plot ="VOYA.01";
				//call the method that prints the salient attributes
				client.printDBVariables(plot);
				client.closeConnection();
				
				
				
			}
			catch(Exception e) 
			{
				System.err.println("Exception: "+ e.getMessage());
				e.printStackTrace();
			}		
		}
		else
		{
			try 
			{
				String name = "//" + argv[1] + "/DataSourceServer";
				DataSourceServerInterface fi = (DataSourceServerInterface)Naming.lookup(name);
				byte[] filedata = fi.downloadFile(argv[0]);
				File file = new File(argv[0]);
				BufferedOutputStream output = new
				BufferedOutputStream(new FileOutputStream(file.getName()));
				output.write(filedata,0,filedata.length);
				output.flush();
				output.close();
			} 
			catch(Exception e) 
			{
				System.err.println("FileServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

}


