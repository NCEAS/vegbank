 /**
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-12-06 02:07:58 $'
 * 	'$Revision: 1.1 $'
 */

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

/**
 * this class represents an 'exetrnal' data source -- a data source which
 * is not the vegbank databases or the vegbank native xml type document
 * at the inception of this class exteranl sources include: tnc plots, 
 * turbo veg plots and the structured data set.  In the future this class
 * will also contain local 'vegbank' data sources
 */
public class PlotDataSource 
{
	private Object pluginObj = null;
	
	//BELOW ARE THE PLOT VARIABLES WHICH CAN BE ACCESSED
	public String projectName = null;
	public String projectDescription = null;
	public String projectStartDate = null;
	public String projectStopDate = null;
	public Vector projectContributors = new Vector() ;
	
	public String latitude = null;
	public String longitude = null;
	public String datum = null;
	public String state = null;
	public String communityName = null;
	public String plotShape = null;
	public String plotId = null;
	public String plotCode = null;
	public String plotArea = null;
	public String surfGeo = null;
	public String elevation = null;
	public String elevationAccuracy = null;
	public String landForm = null;
	public String authorLocation = null;
	public String standSize = null;
	public String topoPostion = null;
	public String country = null;
	public String utmZone = null;
	public String slopeAspect = null;
	public String slopeGradient = null;
	public String hydrologicRegime = null;
	public String xCoord = null;
	public String yCoord = null;
	public String topoPosition = null;
	public String soilDepth = null;
	public String confidentialityReason = null;
	public String confidentialityStatus = null;
	public Vector uniqueStrataNames = new Vector();
	public int uniquePlantTaxaNumber = 0;
	public Vector plantTaxaNames = new Vector();
	
	
	//constructor method
	public PlotDataSource()
	{
		try
		{ 
		
		}
		catch(Exception e)
		{
			System.out.println("Error getting plugin: " + e.getMessage());
		}
	}
	
	
	//constructor method
	public PlotDataSource(String pluginClass )
	{
		try
		{ 
			//create a generic object of the type specified 
			pluginObj = createObject(pluginClass);
		}
		catch(Exception e)
		{
			System.out.println("Error getting plugin: " + e.getMessage());
		}
	}
	
	
	/**
	 * method that populates this class with all the data representing a 
	 * plot using the plugin defined in the constructor method
	 */
	public boolean getPlot( String plotName )
	{
		try
		{
	  	//((PlotDataSourceInterface)pluginObj).getPlotData(plotName);
			
			projectName = ((PlotDataSourceInterface)pluginObj).getProjectName(plotName);
			projectDescription = ((PlotDataSourceInterface)pluginObj).getProjectDescription(plotName);
	    projectContributors = ((PlotDataSourceInterface)pluginObj).getProjectContributors(plotName);
			projectStartDate = ((PlotDataSourceInterface)pluginObj).getProjectStartDate(plotName);
			projectStopDate = ((PlotDataSourceInterface)pluginObj).getProjectStopDate(plotName);
			plotCode = ((PlotDataSourceInterface)pluginObj).getPlotCode(plotName);
			state = ((PlotDataSourceInterface)pluginObj).getState(plotName);
			plotShape =  ((PlotDataSourceInterface)pluginObj).getPlotShape(plotName);
			slopeGradient = ((PlotDataSourceInterface)pluginObj).getSlopeGradient(plotName);
			slopeAspect = ((PlotDataSourceInterface)pluginObj).getSlopeAspect(plotName);
			surfGeo = ((PlotDataSourceInterface)pluginObj).getSurfGeo(plotName);
			hydrologicRegime = ((PlotDataSourceInterface)pluginObj).getHydrologicRegime(plotName);
			topoPosition = ((PlotDataSourceInterface)pluginObj).getTopoPosition(plotName);
			plotArea = ((PlotDataSourceInterface)pluginObj).getPlotArea(plotName);
			country = ((PlotDataSourceInterface)pluginObj).getCountry(plotName);
			xCoord = ((PlotDataSourceInterface)pluginObj).getXCoord(plotName);
			yCoord = ((PlotDataSourceInterface)pluginObj).getYCoord(plotName);
			utmZone = ((PlotDataSourceInterface)pluginObj).getUTMZone(plotName);
			 standSize = ((PlotDataSourceInterface)pluginObj).getStandSize(plotName);
			 
			latitude = ((PlotDataSourceInterface)pluginObj).getLatitude(plotName);
			longitude = ((PlotDataSourceInterface)pluginObj).getLongitude(plotName);
			
			confidentialityReason = ((PlotDataSourceInterface)pluginObj).getConfidentialityReason(plotName);
			confidentialityStatus = ((PlotDataSourceInterface)pluginObj).getConfidentialityStatus(plotName);
			authorLocation
			= ((PlotDataSourceInterface)pluginObj).getAuthorLocation(plotName);
			landForm = ((PlotDataSourceInterface)pluginObj).getlandForm(plotName);
			elevation = ((PlotDataSourceInterface)pluginObj).getElevation(plotName);
			elevationAccuracy
			= ((PlotDataSourceInterface)pluginObj).getElevationAccuracy(plotName);
			
			uniqueStrataNames = ((PlotDataSourceInterface)pluginObj).getUniqueStrataNames(plotName);
			plantTaxaNames = ((PlotDataSourceInterface)pluginObj).getPlantTaxaNames(plotCode);
			uniquePlantTaxaNumber =  plantTaxaNames.size();
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
		return(true);
	}
	
	/**
	 * method that returns a vector containing the names of the 
	 * strata in which the input plant taxa name exists within the 
	 * input plot
	 */
	public Vector getTaxaStrataExistence(String plantName, String plotCode)
	{
		Vector v = new Vector();
		v =  ((PlotDataSourceInterface)pluginObj).getTaxaStrataExistence( plantName, plotCode );
		return(v);
	}
	
	/**
	 * method that returns the coverage of a specific class in a specific
	 * strata
	 */
	public String getTaxaStrataCover(String plantName, String plotCode, String stratumName)
	{
		String s = ((PlotDataSourceInterface)pluginObj).getTaxaStrataCover(
		plantName, plotCode, stratumName);
		//String s = "bad";	
		return(s);
	}
	
	
	 /**
   * creates an object of a type className.  this is used for instantiating
   * plugins.
   */
  private static Object createObject(String className) 
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
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 String s = ((PlotDataSourceInterface)pluginObj).getStrataCover(
		  plotName, strataName);	
			return(s);
	 }
	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		public String getStrataBase(String plotName, String strataName)
		{
			String s = ((PlotDataSourceInterface)pluginObj).getStrataBase(plotName, 
			strataName);
			return(s);
		}
		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		public String getStrataHeight(String plotName, String strataName)
		{
			String s = ((PlotDataSourceInterface)pluginObj).getStrataHeight(
		  plotName, strataName);	
			return(s);
		}
	
	/**
	 * method that prints the variables for a given plot to the 
	 * sys out
	 */
	private void printDBVariables(String plotName)
	{
		System.out.println(" getting info for: " + plotName );
		try
		{
			//define the plugin
			PlotDataSource source = new PlotDataSource("TestPlotSourcePlugin");
			//get all the variables for the given plot
			source.getPlot(plotName);
			
			System.out.println(" \n ---------------------project info------------------------");
			System.out.println("project name: " + source.projectName );
			System.out.println("project description: " + source.projectDescription );
			System.out.println("project contributors: " + source.projectContributors.toString() );
			System.out.println("project start date: " + source.projectStartDate );
			System.out.println("project stop date: " + source.projectStopDate );
			
			System.out.println(" \n ----------------------plot info-----------------------------");
			System.out.println("authorplotcode: " + source.plotCode );
			System.out.println("latitude: " + source.latitude );
			System.out.println("longitude: " + source.longitude );
			System.out.println("x coord: " + source.xCoord );
			System.out.println("y coord: " + source.yCoord );
			System.out.println("utm zone: " + source.utmZone );
			System.out.println("datum: " + source.datum );
			System.out.println("plot shape: " + source.plotShape );
			System.out.println("geology: " + source.surfGeo );
			System.out.println("topo position: " + source.topoPosition );
			System.out.println("hydrologic regime: " + source.hydrologicRegime );
			System.out.println("plot area(m): " + source.plotArea );
			System.out.println("elevation(m): " + source.elevation );
			System.out.println("elevation accuracy (%): " + source.elevationAccuracy );
			System.out.println("landform : " + source.landForm );
			System.out.println("author location: " + source.authorLocation);
			System.out.println("stand size: " + source.standSize );
			
			
			System.out.println(" \n ----------------------strata info-----------------------------");
			System.out.println("unique strata names: " + source.uniqueStrataNames.toString() );
			//get the height, base cover etc for  each of the strata
			for (int i=0; i<source.uniqueStrataNames.size(); i++)
			{
				String curstrata = source.uniqueStrataNames.elementAt(i).toString();
				String cover = getStrataCover(plotCode, curstrata);
				String base = getStrataBase(plotCode, curstrata);
				String height = getStrataHeight(plotCode, curstrata);
				System.out.println("strata: " + curstrata+" cover: "+cover+" base: "+base+" height: "+ height);
			}
			
			System.out.println(" \n ----------------------plant taxa info-----------------------------");
			System.out.println("unique plant names: " + source.plantTaxaNames.toString() );
			
			//get the coverages for the plants for each strata
			for (int i=0; i<source.plantTaxaNames.size(); i++)
			{
				String name = source.plantTaxaNames.elementAt(i).toString();
				Vector strata = getTaxaStrataExistence(name, plotCode);
				System.out.println("name:  " + name  );
				for (int ii=0; ii<strata.size(); ii++)
				{
					String curStrata = strata.elementAt(ii).toString();
					System.out.println(" starta: " + curStrata );
					System.out.println(" cover: " + getTaxaStrataCover(name, 
						curStrata, plotCode ));
				}
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
		}
	}
	
	
	
/**
 * main method for testing --
 */
	public static void main(String[] args)
	{
		//use the plugin with the test data
		PlotDataSource source = new PlotDataSource("TestPlotSourcePlugin");
		source.printDBVariables("test-plot");
	}
	
}
	
