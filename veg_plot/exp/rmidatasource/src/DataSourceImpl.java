import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;


//the data translator classes
import PlotDataSource;
//the plot loader
import databaseAccess.DBinsertPlotSource;

public class DataSourceImpl extends UnicastRemoteObject
  implements DataSourceServerInterface 
	{
		private String name;
	 	private PlotDataSource source;
		private String mdbFile = "";
	 
	 	//loader stuff
	 	private DBinsertPlotSource dbinsert;
	 	private String loaderPlugin = "TNCPlotsDB";
	 
	 // properties file
	 	private ResourceBundle rb;
	 
	 //constructor
   public DataSourceImpl(String sourcePluginClass) 
	 throws RemoteException
	 {
		 super();
		 try
		 {
		 	System.out.println("DataSourceImpl > plugin to be used: " + sourcePluginClass);
		 	source = new PlotDataSource(sourcePluginClass);
		 	System.out.println("DataSourceImpl > number of plots:  " + source.getPlotNames().size() );
			
			rb = ResourceBundle.getBundle("rmidatasource");
			mdbFile = rb.getString("access_file");
			
		 }
		 catch (Exception e)
		 {
		 	System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
     }
   }
	
	
	/**
 	 * this method handles the loading of a plot into the database
	 */	
	 public String insertPlot(String plot, String emailAddress)
	 {
		 String s = null;
		 try
		 {
		 		//set the debugging level to 2 -- the highest on the loader
				int debugLevel = 2;
			 	//the plugin and the source are the same in this case
				 String plugin = "";
			 	//construct an instance of the plot loader
				dbinsert = new DBinsertPlotSource(loaderPlugin, plot);
				s = dbinsert.insertPlot(plot, debugLevel, emailAddress);
		 }
		 catch (Exception e)
		 {
		 	System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
     }
		 return(s);
	 }
	
	/**
	 * method used for uploading the access mdb file of a given type
	 *
	 * @param  fileName -- the name of the uploaded file
	 * @param  fileType -- the type of the uploaded file
	 * @param  buffer -- the file contents
	 *
	 */
	 public boolean getMDBFile(String fileName, String fileType, byte buffer[] )
	 {
		 try
		 {
				System.out.println("DataSourceImpl > uploading: " + fileName);
				byte[] filedata = buffer;
				//make the file that is defined as the instance variable
				System.out.println("DataSourceImpl > writing to: " + mdbFile);
				File file = new File(mdbFile);
				BufferedOutputStream output = new
				BufferedOutputStream(new FileOutputStream(file ));
				output.write(filedata,0,filedata.length);
				output.flush();
				output.close();
		 }
		 catch (Exception e)
		 {
		 	System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
			return(false);
     }
		 return(true);
	 }
	 
	 
	 /**
	  * 	method that examines the mdbFile stored in the location described by 
		* 	'mdbFile' instance variable to verify that it is indeed an ms access 
		* 	file
		*/
	 public boolean isMDBFileValid()
	 {
		 System.out.println("DataSourceImpl > validating: " + mdbFile);
		 
		 //for now just check that there are vlid plot names in the file
		 Vector v = getPlotNames();
		 System.out.println("DataSourceImpl > found : " + v.size() + " plots");
		 //if there are no plots then return false
		 if (v.size() < 1 )
		 {
			 return(false);
		 }
		 else
		 {
		 	return(true);
		 }
	 }
	
	//#START EDITIONS

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
			System.out.println("DataSourceImpl > number of strata: " + v.size() );
		}
		catch (Exception e)
		{
			System.out.println( "Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
	
	
	public Vector getPlantNames(String plotName)
	{
		Vector v = new Vector();
		v = source.getPlantNames(plotName);
		System.out.println("DataSourceImpl > plant names: " + v.toString() );
		return(v);
	}
	
	
//#STOP EDDITIONS
	

	
	
	
	// see the interface for method descriptions
	public String getProjectDescription(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	/**
	 * simple method for transfering files back and forth 
	 * between the client and the server
   */
	 public byte[] downloadFile(String fileName)
	 {
      try 
			{
         File file = new File(fileName);
         byte buffer[] = new byte[(int)file.length()];
         BufferedInputStream input = new
      	 BufferedInputStream(new FileInputStream(fileName));
         input.read(buffer,0,buffer.length);
         input.close();
         return(buffer);
      } 
			catch(Exception e)
			{
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(null);
      }
   }
	 
	/**
	 * method that retuns all the plots stored in a 
	 * data source
	 */
  public Vector getPlotNames()
	{
		Vector v = new Vector();
		try
		{
			// re-instantiate this class
			source = new PlotDataSource(this.loaderPlugin);
			
			System.out.println("DataSourceImpl > get plot names contacted");
			v = source.getPlotNames();
			System.out.println("DataSourceImpl > plot names: " + v.toString() );
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
		
		
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
	public String getLatitude(String plotName)
	{
		try
		{
			return( source.getLatitude(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the longitude
	public String getLongitude(String plotName)
	{
		try
		{
			return( source.getLongitude(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the geographic zone
	public String getUTMZone(String plotName)
	{
		try
		{
			return( source.getUTMZone(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the plot shape
	public String getPlotShape(String plotName)
	{
		try
		{
			return( source.getPlotShape(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	//returns the plot area
	public String getPlotArea(String plotName)
	{
		try
		{
			return( source.getPlotArea(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the state for the current plot
	public String getCommunityName(String plotName)
	{
		try
		{
			return( source.getCommunityName(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	//returns the state in which the plot exists
	public String getState(String plotName)
	{
		try
		{
			return( source.getState(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{
		try
		{
			return( source.getHydrologicRegime(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the topo position
	public String getTopoPosition(String plotName)
	{
		try
		{
			return( source.getTopoPosition(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the slope aspect
	public String getSlopeAspect(String plotName)
	{
		try
		{
			return( source.getSlopeAspect(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns yje slope gradient
	public String getSlopeGradient(String plotName)
	{
		try
		{
			return( source.getSlopeGradient(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the surficial geology
	public String getSurfGeo(String plotName)
	{
		try
		{
			return( source.getSurfGeo(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the country
	public String getCountry(String plotName)
	{
		try
		{
			return( source.getCountry(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		try
		{
			return( source.getStandSize(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		try
		{
			return( source.getAuthorLocation(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the landForm
	public String getLandForm(String plotName)
	{
		try
		{
			return( source.getLandForm(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the elevation
	public String getElevation(String plotName)
	{
		try
		{
			return( source.getElevation(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		try
		{
			return( source.getElevationAccuracy(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//return the confidentiality reason -- not null
	public String getConfidentialityReason(String plotName)
	{
		try
		{
			return( source.getConfidentialityReason(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//return the confidentiality status -- not null 0-6
	public String getConfidentialityStatus(String plotName)
	{
		try
		{
			return( source.getConfidentialityStatus(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}	
		
	 
	 //this method retrives all the attributes for a gine plotname
  public void getPlotData(String plotName)
	{
		try
		{
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
		
	//returns the project name 
	public String getProjectName(String plotName)
	{
		String s = null;
		try
		{
			s = 	source.projectName;
			System.out.println("DataSourceImpl > proj name contacted");
			this.getPlotNames();
		}
		catch(Exception e) 
		{
     	System.err.println("Exception: "+ e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
}


