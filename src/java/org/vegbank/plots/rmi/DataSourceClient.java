package org.vegbank.plots.rmi;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Vector;

import org.vegbank.xmlresource.transformXML;


/** 
 * Class that mimics the functionality of the PlotDataSource class except that 
 * this class actually makes an RMI connection to the PlotDataSource class
 * server running on a different machine.  This class is needed because there
 * are certain types of plot data sources (e.g., MS ACCESS) that run from a 
 * specific computing platform that we want to integrate with software running
 * elsewhere, also there is a need for the clients to access data stored on a
 * server running the vegbank databases and this tool will allow for an
 * alternative to HTTP for accessing that data.  This RMI client is used by the 
 * VegBank java servlet system in order to pass an access file to the server that
 * runs on a wintel box and then to insert a plot(s) to the VegBank database vi 
 * the RMI Server
 * 	
 *	<br> <br>
 *  Authors: @author@
 *  Release: @release@
 *	
 *  <br> <br>
 *  '$Author: farrell $'
 *  '$Date: 2003-10-27 19:49:02 $'
 *  '$Revision: 1.6 $'
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

public class DataSourceClient
{
	
	private String serverHost =  null;
	private String port = null;
	private String serverClass = "DataSourceServer";
	private String url = null;
	DataSourceServerInterface source;
	private transformXML transformer  = new transformXML(); 
	
	/**
	 * this constructor method makes a connection to the 
	 * data source server 
	 *
	 * @param hostName -- the resolveable name or IP of host running the 
	 * 		RMI server 
	 */
	public DataSourceClient(String hostName, String port)
	{
		try
		{
			System.out.println("DataSourceClient > connecting to server: " +hostName );
			System.out.println("DataSourceClient > on port: " +port );
			this.serverHost = hostName;
			this.url = "//" + hostName + ":"+port+"/"+serverClass;
			
			//list the bound serveres to this host
			String[] remoteObjects = Naming.list(url);  
			for (int c = 0; c < remoteObjects.length; c++) 
			{    
				System.out.println("DataSourceClient > names bound to: "
				+ serverHost +" - " 
				+ remoteObjects[c]  );
			}
			
			//look up the inplementation class
			this.source = (DataSourceServerInterface)Naming.lookup(url);
			
			//make a test
			//System.out.println("DataSourceClient > plots:" + source.getPlotNames() );
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
	 * @deprecated -- this method assumes that the calling class is to
	 * access a tnc plots mdb type database which is a bad assumption
	 */
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		try
		{
			v = source.getPlotNames();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
	
	
	/**
	 * method that returns the names of the plots stored in this data 
	 * source 
	 * @param fileType -- the type of file that the plots are 
	 * stored in, that dictates the plugin that can be used.
	 * this may inclued: 'tnc', 'vbaccess', and 'nativexml' only
	 */
	public Vector getPlotNames(String fileType)
	{
		Vector v = new Vector();
		try
		{
			System.out.println("DataSourceClient > requesting plot names for file type: " + fileType);
			v = source.getPlotNames(fileType);
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
		try
		{
			return( source.getPlantNames(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(v);
	}
	
	
//#STOP EDDITIONS
	
	/**
	 * method that returns the state from which the plot was collected from 
	 * the rmi server based on a plot
	 *
	 * @param plot -- string representation of a plot
	 */
	 public String getState(String plotName)
	 {
	 	String s = null;
		try
		{
			s= source.getState(plotName) ;
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
	 	return(s);
	 }
	
	/**
	 * method that returns community that a plot falls in
	 * the rmi server based on a plot
	 *
	 * @param plot -- string representation of a plot
	 */
	 public String getCommunityName(String plotName)
	 {
	 	String s = null;
		try
		{
			s=  source.getCommunityName(plotName) ;
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
	 	return(s);
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
	

	
	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName)
	{
		String s = null;
		try
		{
			s = source.getHydrologicRegime(plotName);
			System.out.println("DataSourceClient > hydro regime: " + s);
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
			 	System.out.println("DataSourceClient > x: " + this.getXCoord(plot) );
				System.out.println("DataSourceClient > y: " + this.getYCoord(plot) );
				
				System.out.println("DataSourceClient > lat: " + this.getLatitude(plot) );
				System.out.println("DataSourceClient > long: " + this.getLongitude(plot) );
				System.out.println("DataSourceClient > authLoc: " + this.getAuthorLocation(plot) );
				
				//#START
				System.out.println("DataSourceClient > community name : " + this.getCommunityName(plot) );
				System.out.println("DataSourceClient > confidential reason: " + this.getConfidentialityReason(plot) );
				System.out.println("DataSourceClient > confidentail status: " + this.getConfidentialityStatus(plot) );
				System.out.println("DataSourceClient > country: " + this.getCountry(plot) );
				System.out.println("DataSourceClient > elevation: " + this.getElevation(plot) );
				System.out.println("DataSourceClient > elevation accuracy: " + this.getElevationAccuracy(plot) );
				System.out.println("DataSourceClient > hydro regime: " + this.getHydrologicRegime(plot) );
				System.out.println("DataSourceClient > land form: " + this.getLandForm(plot) );
				System.out.println("DataSourceClient > plot area: " + this.getPlotArea(plot) );
				System.out.println("DataSourceClient > plot shape: " + this.getPlotShape(plot) );
				System.out.println("DataSourceClient > slope aspect: " + this.getSlopeAspect(plot) );
				System.out.println("DataSourceClient > slope gradient: " + this.getSlopeGradient(plot) );
				System.out.println("DataSourceClient > stand size: " + this.getStandSize(plot) );
				System.out.println("DataSourceClient > state: " + this.getState(plot) );
				System.out.println("DataSourceClient > surf geo: " + this.getSurfGeo(plot) );
				System.out.println("DataSourceClient > topo position: " + this.getTopoPosition(plot) );
				System.out.println("DataSourceClient > utm zone: " + this.getUTMZone(plot) );
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
	  * method to load to the server a file containing 
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
		*/
		public boolean putMDBFile(String fileName, String fileType, String location)
		{
			boolean results = true;
			try 
			{
				boolean lock = true;//= source.getMDBFileLock();
				if (lock)
				{
					System.out.println("DataSourceClient > loading file name: " + fileName +" file type: " + fileType);
					File file = new File(fileName);
					byte buffer[] = new byte[(int)file.length()];
					BufferedInputStream input = new
					BufferedInputStream(new FileInputStream(fileName));
					input.read(buffer,0,buffer.length);
					input.close();
					results = source.getMDBFile(fileName, fileType, buffer, location);
				}
				else
				{
					System.out.println("");
				}

      } 
			catch(Exception e)
			{
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
				 return(results);
      }
			return(results);
		}
		
	  
	 /**
	  * 	method that examines the mdbFile stored in the location described by 
		* 	'mdbFile' instance variable to verify that it is indeed an ms access 
		* 	file
		*/
	 public boolean isMDBFileValid(String location)
	 {
		 boolean results = true;
		 try
		 {
      results = source.isMDBFileValid(location);
		 }
		 catch(Exception e)
     {
      System.out.println("FileImpl: "+e.getMessage());
      e.printStackTrace();
		  results = false;
     }
		 return(results);
	 }

	
	/**
	 * method that will insert a plot on the windows machine based on the name
	 * of that plot 
	 * 
	 * @param plot -- the plot that should be loaded 
	 * @param fileType -- the type of plot data set that the file, cached at the 
	 * 	server, represents the possible types may include 'tnc', 'vbaccess', 'nativexml'
	 * @param emailAddress -- the emailAddress of the person submitting the plots
	 */
	 public String insertPlot(String plot, String fileType, String emailAddress)
	 {
		 String s = null;
		 try
			{
				s= source.insertPlot(plot, fileType, emailAddress);
			}
			catch(Exception e) 
			{
				System.err.println("Exception: "+ e.getMessage());
				e.printStackTrace();
			}
			return(s);
	 }
	 
	 public void setPluginClassName (String pluginClassName) 
	 {
	 		try
	 		{
	 			source.setPluginClassName(pluginClassName);
	 		}
	 		catch (Exception e)
	 		{
	 			System.out.println("DataSourceClient > Exception " + e.getMessage() );
	 			e.printStackTrace();
	 		}
	 }
	 
	 /**
	  * Returns the location to upload the file. This acts as a lock, if no 
	  * location availible then returns null
	  */
	 public String getFileUploadLocation()
	 {

	 	String location = null;
		try
		{
			location = source.getFileUploadLocation();
		}
		catch (Exception e)
		{
			System.out.println("DataSourceClient > Exception " + e.getMessage() );
			e.printStackTrace();
		}
	 	return location;
	 }

	 
	/**
	 *	main method for testing.  The arguments that can be passed into this are 
	 *	the following:
	 *  1] host server name like: raptor.nceas.ucsb.edu
	 *	2] file -- the file to be uploaded to the server for loading to the DB
	 *	3] filetype -- the type of file which dicatates the plot data source plugin
	 *		used by the server.  Options are 'tnc', 'vbaccess' and 'nativexml'
	 *	4] plotname -- the name of the plot to be loaded by the server.  If the 
	 *		argument is equal to '-all' then all will be loaded
	 *
	 */
	public static void main(String argv[]) 
	{
		//get a file from the server
		System.out.println("DataSourceClient > argument length: " + argv.length );
		if (argv.length >= 2)
		{
			System.out.println("DataSourceClient >  posting a file to the server " );
			try 
			{
				String hostServer =  argv[0];
				String file = argv[1];
				//this is the type of file and can be like: TNC, VBAccess, NativeXML etc...
				String fileType = argv[2];
				System.out.println("DataSourceClient > file type: " + fileType );
				
				DataSourceClient client = new DataSourceClient(hostServer, "1099");
				String location = client.getFileUploadLocation();
				System.out.println("DataSourceClient > sending file: " + file );
				boolean sendResults = client.putMDBFile(file, fileType, location);
				System.out.println("DataSourceClient > transmittal results: " + sendResults );
				boolean fileValidityResults = client.isMDBFileValid(location);
				System.out.println("DataSourceClient > access file validity: " + fileValidityResults );
				Vector v = client.getPlotNames(fileType);
				System.out.println("DataSourceClient > found : " + v.size() +" plots in archive " );
				String testPlot = "";
				
				//this is the email address of the user who is responsible for laoding the data 
				String email = "test@test.com";
				
				if (argv.length == 4)
				{
					testPlot = argv[3];
					//if the all flag is passed
					if ( testPlot.equals("-all") )
					{
						StringBuffer sb = new StringBuffer();
						sb.append("<archiveInsertion>");
						String insertResults = "";
						System.out.println("DataSourceClient > inserting all the plots in the archive " );
						for (int c = 0; c < v.size(); c++)
						{
							String curPlot = v.elementAt(c).toString();
							insertResults = client.insertPlot( curPlot, fileType, email );
							sb.append(insertResults);
						}
						sb.append("</archiveInsertion>");
						
						//transform the xml using a generic xslt sheet
						transformXML trans  = new transformXML(); 
						String tr = trans.getTransformedFromString( sb.toString() , "./lib/ascii-treeview.xsl");
						System.out.println("DataSourceClient > insertion results html: \n" + tr );
					}
					//just load the single plot identified on the command line
					else
					{
						// set the plugin to use 
						client.setPluginClassName(fileType);
						
						System.out.println("DataSourceClient > inserting the plot: " + testPlot );
						String insertResults = client.insertPlot(testPlot, fileType, email);
						//transform and display the results to the user
						transformXML trans  = new transformXML(); 
						String tr = trans.getTransformedFromString(insertResults, "./lib/ascii-treeview.xsl");
						System.out.println("DataSourceClient > insertion results html: \n" + tr );
					}
				}
				client.releaseFileUploadLocation(location);
			} 
			catch(Exception e) 
			{
				System.err.println("FileServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}


	/**
	 * @param location
	 */
	public void releaseFileUploadLocation(String location)
	{
		try
		{
			source.releaseFileUploadLocation(location);
		}
		catch (Exception e)
		{
			System.out.println("DataSourceClient > Exception " + e.getMessage() );
			e.printStackTrace();
		}
	}

}


