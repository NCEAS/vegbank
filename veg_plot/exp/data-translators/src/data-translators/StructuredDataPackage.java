/**
 * class that was designed as a model for a structured data file, which is a 
 * file the has a very well structured header file that can be used to develop
 * a project XML file that is to be ingested into the plots database 
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-11-01 17:39:00 $'
 * 	'$Revision: 1.1 $'
 */
//package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

//import vegclient.framework.*;
import xmlresource.utils.PlotDataMapper;
import xmlresource.utils.PlotXmlWriter;

public class StructuredDataPackage
{
	
	//the vector that contains the files passed to the class
	Vector fileVector = new Vector();
	//a hash table the maps the fileName to a type
	Hashtable fileTypeHash = new Hashtable();
	
	private String projectFileName = null;
	private String siteFileName = null;
	private String speciesFileName = null;
	
	
	
	//the xml printer class
	PlotXmlWriter xmlWriter = new PlotXmlWriter();
	
	
	
	public StructuredDataPackage(Vector fileVector)
	{
		
		try
		{
		//System.out.println(fileVector.toString() );
		//get the file typles
		fileTypeHash = getFileTypes( fileVector );
		
		//THE PROJECT DATA
		if ( getProjectFileName( fileVector ) != null )
		{
			projectFileName = getProjectFileName( fileVector );
		}
		else
		{
			System.out.println("No Project File Found");
		}
		
		//THE SITE DATA
		if ( getSiteFileName( fileVector ) != null )
		{
			siteFileName = getSiteFileName( fileVector );
		}
		else
		{
			System.out.println("No Site File Found");
		}
		
		//THE SPECIES DATA
		if ( getSpeciesFileName( fileVector ) != null )
		{
			speciesFileName = getSpeciesFileName( fileVector );
		}
		else
		{
			System.out.println("No Species File Found");
		}
		
			
		//wrap the hashtable into a vector for now -- eventually write anothe method
		//in the plot xml writer class
		Vector projectVector = new Vector();
		
		//mapp the data
		StructuredProjectFile project = new StructuredProjectFile( projectFileName);
		StructuredSiteFile site = new StructuredSiteFile( siteFileName);
		
		
		for (int i=0; i<site.authorPlotCodes.size(); i++)
		{
			//the attribute mapping class
			PlotDataMapper dataMapper = new PlotDataMapper();
			//PROJECT DATA
			dataMapper.plotElementMapper( project.projectName, "projectName", "project" );
    	dataMapper.plotElementMapper( project.projectDescription, "projectDescription", "project" );
			
			//SITE DATA
			dataMapper.plotElementMapper( site.authorPlotCodes.elementAt(i).toString(), "authorPlotCode", "site" );
			
			if ( site.elevationValuesExist == true )
			{
				dataMapper.plotElementMapper( site.elevationValues.elementAt(i).toString(), "elevationValue", "site" );
			}
			
			if ( site.stateValuesExist == true )
			{
				dataMapper.plotElementMapper( site.stateValues.elementAt(i).toString(), "state", "site" );
			}
			
			if ( site.origLatValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.origLatValues.elementAt(i).toString(), "origLat", "site" );
			}
			
			if ( site.origLongValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.origLongValues.elementAt(i).toString(), "origLong", "site" );
			}
			
			if ( site.plotSizeValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.plotSizeValues.elementAt(i).toString(), "plotSize", "site" );
			}
			
			if ( site.placeNameValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.placeNameValues.elementAt(i).toString(), "placeName", "site" );
			}
			
			if ( site.countryValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.countryValues.elementAt(i).toString(), "country", "site" );
			}
			
			//below here is the observation data
			if ( site.treeStemAreaValuesExist == true )
			{
				dataMapper.plotElementMapper( 
					site.treeStemAreaValues.elementAt(i).toString(), "treeStemArea", "observation" );
			}
			
			//Cheat by Genereating a authorPlotCode 
			String obsCode = "1"+site.authorPlotCodes.elementAt(i).toString()
			+project.projectName;
			dataMapper.plotElementMapper( obsCode, "authorObsCode", "observation" );
			
			//Get the species info
			StructuredSpeciesFile species = new StructuredSpeciesFile( speciesFileName, 
			 site.authorPlotCodes.elementAt(i).toString());
			 
			 for (int ii=0; ii<species.authorNameIds.size(); ii++)
			 {
				 dataMapper.plotElementMapper( 
				 	species.authorNameIds.elementAt(ii).toString(), "taxonName", "species" );
					
					dataMapper.plotElementMapper( 
				 	species.cumStrataCover.elementAt(ii).toString(), "cumStrataCover", "species" );
			 }
			
		
			//consolidate the subcategories into a consolidated hashtable
			dataMapper.plotElementConsolidator
			(
		 	 dataMapper.plotProjectParams, 
		 	 dataMapper.plotSiteParams, 
		 	 dataMapper.plotObservationParams, 
		 	 dataMapper.plotCommunityParams,	
		 	 dataMapper.plotSpeciesParams
			);
			
			//add to the vector
			projectVector.addElement(dataMapper.comprehensivePlot);
		}
	
		
		//print the thing
		xmlWriter.writeProjectFile(projectVector, "peet_project.xml");
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * method that returns the project fileName for the proect file in the package
	 */
	 private String getProjectFileName(Vector fileVector)
	 {
		String returnString = null;
		for (int i=0; i<fileVector.size(); i++)
		{
		 String s = fileVector.elementAt(i).toString().trim();
		 //System.out.println(s);
		 if ( fileTypeHash.get(s).toString().equals("Project") )
			{
				returnString = s;
			}
		}
		//System.out.println(returnString);
		return(returnString);
	 }
	
	
	/**
	 * method that returns the project fileName for the proect file in the package
	 */
	 private String getSiteFileName(Vector fileVector)
	 {
		String returnString = null;
		for (int i=0; i<fileVector.size(); i++)
		{
		 String s = fileVector.elementAt(i).toString().trim();
		 //System.out.println(s);
		 if ( fileTypeHash.get(s).toString().equals("Site") )
			{
				returnString = s;
			}
		}
		//System.out.println(returnString);
		return(returnString);
	 }
	
	
	
	/**
	 * method that returns the species fileName for the proect file in the package
	 */
	 private String getSpeciesFileName(Vector fileVector)
	 {
		String returnString = null;
		for (int i=0; i<fileVector.size(); i++)
		{
		 String s = fileVector.elementAt(i).toString().trim();
		 //System.out.println(s);
		 if ( fileTypeHash.get(s).toString().equals("TaxonObs") )
			{
				returnString = s;
			}
		}
		//System.out.println(returnString);
		return(returnString);
	 }
	
	
	/**
	 * method that returns a has table with fileName and type
	 */
	private Hashtable getFileTypes( Vector fileVector )
	{
		Hashtable returnHash = new Hashtable();
		for (int i=0; i<fileVector.size(); i++)
		{
			StructuredDataFile dataFile = new	StructuredDataFile(fileVector.elementAt(i).toString());
			returnHash.put(fileVector.elementAt(i).toString(), dataFile.getFileType() );
		}
		return(returnHash);
		
	}
	
	
	
	
	/**
   * the main routine used to test the StructureDataPackage class which 
	 * interacts with the vegclass database.
   * <p>
   * Usage: java StructureDataFile <filename ....>
   *
   * @param filename to be proccessed
   *
   */
	 
  static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length < 1) 
			{
				System.out.println("Usage: java StructuredDataFile [fileName] [fileName] ...  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				Vector vec = new Vector();
				int fileNumber = args.length;
				for (int i=0; i<fileNumber; i++)
				{
					vec.addElement( args[i]);
				}
				//the constructor
				StructuredDataPackage dataPackage = new StructuredDataPackage(vec);
				
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
}
