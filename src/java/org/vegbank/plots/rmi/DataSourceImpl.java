package org.vegbank.plots.rmi;

import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.plots.datasource.PlotDataSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import databaseAccess.DBinsertPlotSource;
import edu.ucsb.nceas.vegbank.plotvalidation.PlotValidator;

/** 
 *  Class that handles the upload of a plots data set to the VegBank database
 * 	The functionality related to this task includes <br>
 * 	1] allowing the client to upload a data file (via and RMI connection)
 * 	to the server so that it is cached on the server file system. <br> <br>
 *  2] allowing for the client to request that one or many of the plots
 *	stored in the uploaded file to be loaded to the database.  The file 
 *  types that are supported here are: 1. tnc ms access file, vegbank ms 
 *	access file, and the vegbank native xml document <br> <br>
 *	3] utilities allowing the client to request plot attributes associated 
 *	with a give plot <br> <br>
 *	The class was written so that ms access files could be accessed and loaded 
 * 	from a wintel box, and the class is connected to via an rmi client that 
 *	runs an any hardware/software that will jun java.
 *	
 * <br> <br>
 *  '$Author: farrell $'
 *  '$Date: 2003-05-16 03:33:34 $'
 * 	'$Revision: 1.4 $'
 *
 */

public class DataSourceImpl extends UnicastRemoteObject
  implements DataSourceServerInterface 
	{

		private String name;
	 	private PlotDataSource source;
		//private String mdbFile = ""; //the file that the uploaded access files are written to
	  private String xmlFile = ""; //the file that the uploaded xml files are written to
	  
	  private static Set availableLocations = new HashSet();
		private static Hashtable unAvailableLocations = new Hashtable();
	 
	 	//loader stuff -- default to the native xml because it can be run on any
    //architecture and OS
	 	private DBinsertPlotSource dbinsert;
	 	private String loaderPlugin = "NativeXmlPlugin";
	 
	 // properties file
	 	private ResourceBundle rb;
   //plot validator 
   private PlotValidator validator;
	 
	 //constructor
   public DataSourceImpl(String sourcePluginClass) 
	 throws RemoteException
	 {
		 super();
		 try
		 {
				System.out.println("DataSourceImpl >  starting the rmi server:  " );
				
				// test the vegbank systrem
				System.out.println("DataSourceImpl >  testing the VegBank services:  " );
				DatabaseUtility util = new DatabaseUtility();
				boolean success = util.testVegBankConnections();
				System.out.println("DataSourceImpl >  VegBank services passed:  " + success );
				
				rb = ResourceBundle.getBundle("rmidatasource");
				String mdbFile = rb.getString("access_file");
				
				// Add to availibleLocations if it is not there already
				availableLocations.add(mdbFile);
				
				System.out.println("DataSourceImpl > ms database file:  " + mdbFile );
				xmlFile = rb.getString("xml_file");
				System.out.println("DataSourceImpl > xml file:  " + xmlFile );
				System.out.println("DataSourceImpl > plugin to be used: " + sourcePluginClass);
				// This is the dataSource to use just for initialization test!!
				source = new PlotDataSource(sourcePluginClass);
				System.out.println("DataSourceImpl > number of plots:  " + source.getPlotNames().size() );
        // instantiate the plot validator 
        validator = new PlotValidator(source);
			
		 }
		 catch (Exception e)
		 {
		 	System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
     }
   }
	
	
	/**
 	 *
	 * this method handles the loading of a plot into the database <br>
	 * <br>
	 * @deprecated -- use the other method with the same name for this 
	 * 	method assumes that the plots data set 'fileType' corresponds
	 * 	to the 'tnc' plots data type which is a relect of the early 
	 * design of this server/cleint system
	 *
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
				loaderPlugin = "TNCPlotsDB";
				 
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
	 * This method handles the loading of a plot into the VegBank database.
	 * Currently the type of plots database files that can be used to load to the 
	 * database are the: <br>
	 * TNC plots MDB File <br>
	 * VegBank MDB File <br>
	 * VegBank Native XML Document <br> <br>
	 * But there is the ongoing suspicion that other data sources may be used 
	 * in the future like the TurboVeg 
	 * <br>
	 * @param plot -- the name of the plot which is to be loaded 
	 * @param fileType -- the type of the file that is cached on the server
	 * this may inclued: 'tnc', 'vbaccess', and 'nativexml' only
	 * @param emailAddress -- the email address of the person responsible 
	 * for the loading of the plot data.
	 *
	 */	
    public String insertPlot(String plot, String fileType, String emailAddress)
	  {
		  String s = null;
		  try
		  {
		    //set the debugging level to 2 -- the highest on the loader
        int debugLevel = 2;
		 	  setPluginClassName(fileType);	

      	//if the plugin is not null then try loading the data
		    if ( loaderPlugin != null )
		    {
			    //construct an instance of the plot loader
			    dbinsert = new DBinsertPlotSource(loaderPlugin, plot);
			    s = dbinsert.insertPlot(plot, debugLevel, emailAddress);
		    }
		  }
		  catch (Exception e)
		  {
		  	System.out.println("Exception: "+e.getMessage());
        e.printStackTrace();
      }
		  return(s);
	  }
	
	
	
	/**
	 * method to accept from the client a file containing 
	 * that corresponds to a specific fileType that may 
	 * include: <br> <br>
	 * nativexml -- the VegBank native xml document <br>
	 * tnc -- the tnc ms access plots file <br>
	 * vbaccess -- the VegBank ms access file <br>
	 * 
	 * Eventhough the method name is put MDBFile this method is 
	 * to be used to upload any / all data files <br>
	 *
	 * @param fileName -- the path and name of the file to 
	 *		upload to the server 
	 * @param fileType -- the type of file (see above description)
	 * @param  buffer -- the file contents
	 *
	 */
	 public boolean getMDBFile(String fileName, String fileType, byte buffer[], String location)
	 {
		 try
		 {
			 	String serverFile = null;
				System.out.println("DataSourceImpl > uploading file named: " + fileName
				+ " file type: " + fileType);
				// figure out where to put the file based on its type. if it is
				// an ms access file put it in a place so that it is accessed via odbc
				if ( fileType.trim().equals("nativexml") )
				{
					serverFile = xmlFile;
				}
				else
				{
					serverFile = location;
				}
				byte[] filedata = buffer;
				//make the file that is defined as the instance variable
				System.out.println("DataSourceImpl > writing to: " + serverFile);
				File file = new File(serverFile);
				BufferedOutputStream output = new
				BufferedOutputStream(new FileOutputStream(file ));
				output.write(filedata,0,filedata.length);
				System.out.println("DataSourceImpl > file size: " + filedata.length);
				output.flush();
				output.close();
				//sleep for debug purposes
				Thread.sleep(2000);
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
   * this metod uses the  plotvalidator to test that the input plot is valid.
   * @param plotName -- the name of the plot to test for validity 
   * @return valid  -- true if the plot is valid, else false 
   */
   public boolean isPlotValid(String plot)
   {
    boolean result = true;
    try
    {
			validator = new PlotValidator(this.source);
      result  = validator.isPlotValid(plot);
      //result = validator.getValidationReport();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return(result);
   }
	 
	 /**
	  * this method returns the validation report for the plot
		* @return -- returns the xml receipt for the plot validation report 
		*/
		public String getValidationReport()
		{
			String report = null;
			try
			{
				//result  = validator.isPlotValid(plot);
				report = validator.getValidationReport();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return(report);
		}
  
	 /**
	  * 	method that examines the mdbFile stored in the location described by 
		* 	'mdbFile' instance variable to verify that it is indeed an ms access 
		* 	file.
		*/
	 public boolean isMDBFileValid(String location)
	 {
		 System.out.println("DataSourceImpl > validating: " + location);
		 
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
	 * @param fileName -- the name if the file that as used at the 
	 *  client
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
	 * @deprecated -- this method assumes that the tnc plots loader 
	 *	plugin is to be used 
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
	
	/**
	 * method that retuns all the plots stored in a 
	 * data source
	 *
	 * @param fileType -- the type of file that the plots are 
	 * stored in, that dictates the plugin that can be used.
	 * this may inclued: 'tnc', 'vbaccess', and 'nativexml' only
	 */
  public Vector getPlotNames(String fileType)
	{
		System.out.println("DataSourceImpl > get plot names contacted");
		Vector v = new Vector();
		try
		{
			this.setPluginClassName(fileType);			
			if ( loaderPlugin != null )
			{
				// re-instantiate this class
				source = new PlotDataSource(this.loaderPlugin);
				v = source.getPlotNames();
				System.out.println("DataSourceImpl > plot names: " + v.toString() );
			}
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
	
	public void setPluginClassName (String fileType) 
	{
    //set the plugin to null 
		loaderPlugin = null;
		System.out.println("DataSourceImpl > plot file type: " + fileType);
		if ( fileType.toUpperCase().equals("TNC") )
		{
			System.out.println("DataSourceImpl > using datasource plugin: TNCPlotsDB");
			loaderPlugin = "TNCPlotsDB";
		}
		else if (fileType.toUpperCase().equals("VBACCESS") )
		{
			System.out.println("DataSourceImpl > using datasource plugin: VBAccessDataSourcePlugin");
			loaderPlugin = "VBAccessDataSourcePlugin";
		}
		else if (fileType.toUpperCase().equals("NATIVEXML") )
		{
			System.out.println("DataSourceImpl > using datasource plugin: NativeXmlPlugin");
			loaderPlugin = "NativeXmlPlugin";
		}
		else
		{
			System.out.println("DataSourceImpl > cannot determine datasrc plugin type");
		}
  	this.source = new PlotDataSource(loaderPlugin);
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


	/* (non-Javadoc)
	 * @see org.vegbank.plots.rmi.DataSourceServerInterface#getFileUploadLocation()
	 */
	public String getFileUploadLocation()
	{
		String locationString = null;
		Iterator i = availableLocations.iterator();
		if ( i.hasNext() )
		{
			System.out.println("DataSourceImpl > Getting a location");
			Object location = i.next();
			availableLocations.remove(location);
			unAvailableLocations.put(location, new Long(System.currentTimeMillis()));
			locationString = (String) location;
		}
		else
		{
			locationString = findExpiredLocation();
		}
		return locationString;
	}


	private String findExpiredLocation()
	{
		String locationString = null;
		// Check the locations in use for older than 10 minutes ( 10 * 60 *  1000 )
		long TIME_OUT = 600000;  // in milliseconds
		System.out.println("DataSourceImpl > Check each unAvailableLocation");
		Enumeration keys = unAvailableLocations.keys();
		while (keys.hasMoreElements() )
		{
			Object location = keys.nextElement();
			Long startTime = (Long) unAvailableLocations.get(location);
			long timeUsed = System.currentTimeMillis() - startTime.longValue();
			System.out.println("DataSourceImpl > " + location + "has beeb busy for" 
					+ timeUsed + "milliseconds" );
			
			
			// If the location is older than arbritary time limit  
			if ( timeUsed > TIME_OUT)
			{
				// take over this location 
				System.out.println("DataSourceImpl > Taking over " + location);
				unAvailableLocations.put(location, new Long(System.currentTimeMillis()));
				locationString = (String) location;
			}
			
		}
		return locationString;
	}
	
	public void releaseFileUploadLocation(String location)
	{
		availableLocations.add(location);
	}
	
}


