package org.vegbank.databaseAccess;

/**
 * 
 *    Purpose: To write plot data as xml documents that can then be loaded into
 * 				the plots database 
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 *
 *		 '$Author: farrell $'
 *     '$Date: 2003-08-21 21:16:44 $'
 *     '$Revision: 1.2 $'
 *
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


import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

public class PlotXmlWriter 
{


//plot specific site information
Hashtable plotSiteParams = new Hashtable();
//species specific site information
Hashtable plotSpeciesParams = new Hashtable();
//initialize the vector to store the multitude of taxon names, starta, and covers
// that may be passed to this method -- used in the plotElementMapper method
Vector taxonNameElements= new Vector();

Vector strataTypeElements = new Vector();
Vector coverAmountElements = new Vector();

//plot observation specific information
Hashtable plotObservationParams = new Hashtable();
Vector stratumNameElements = new Vector();  //recognized strata names
Vector cumStrataCoverElements = new Vector();
Vector stratumHeightElements = new Vector(); //recognized strata heights
Vector stratumCoverElements = new Vector(); //recognized strata cover vals



//project specific information
Hashtable plotProjectParams = new Hashtable();
//community specific information
Hashtable plotCommunityParams = new Hashtable();
// this is the hash that contains all the elements and will be passed to 
// the writer method
Hashtable comprehensivePlot = new Hashtable();

//the output string buffer
StringBuffer output = new StringBuffer(); 

//the universal null value for the plots database
String nullValue = "-999.25";






/**
 * This method will write a comprensive plot data file, which means a data file 
 * contains all of the data required for an an upload of a 'new' plot to the 
 * database that 
 *
 * @param comprehensivePlot - a hash table that stores all the data required to
 * write a full plot and which has a structure identical to:
 *
 *
 */
public void writePlot (Hashtable comprehensivePlot, String outFile)
{
	//the first try will update the outputFile in memmory and the second will
	// print to the file system
	try 
	{
		//decompose the comprehensive plot data hastable	
		dataDecomposer(comprehensivePlot);
				
		//prepare the header
		writeHeader();

		//write the project related data -- the remaining children nodes will be
		// automatically written
		writeProjectData(plotProjectParams);

		//write the footer
		writeFooter();
	}
	catch (Exception ex) 
	{
		System.out.println("PlotXmlWriter.writePlot "
		+"Error passing arguments "+ex+" "+ex.getMessage() 
		); 
		ex.printStackTrace();
	}
	//now print to the file system
	try 
	{
		PrintStream out = new PrintStream(new FileOutputStream(outFile, false));
		out.println(output.toString());
	}
	catch (Exception ex) 
	{
		System.out.println("Error printing the xml file "+ex);
		System.exit(1);
	}
}


 /**
  * method that takes as input a vector that contains all the plots in a project
	*	stored as hashtables that are the mapped plots and a project file name and
	* writes the information to a single project file
	*
	* @param projectVector the vector that stores all the plots in a project where
	*	each plot has been mapped into a hashtable
	* @param filename -- the name of the file that should be written
	*
	*/
	
	public void writeProjectFile (Vector projectVector, String fileName)
	{
		try 
		{
			//prepare the header
			writeHeader();	
			for (int i=0; i< projectVector.size(); i++)
			//for (int i=0; i<3; i++) 
			{
				//System.out.println("plot"+i);
				Hashtable singlePlot = (Hashtable)projectVector.elementAt(i);
				//decompose the comprehensive plot data hastable this will update the
				// parameter hashtables in this class
				dataDecomposer(singlePlot);
				
				//using the information from the first plot write the project level
			// information
			if (i==0)
			{
				startProject(plotProjectParams);
			}
			//write the plots for this plot and the remainder plots
			writeSiteData(plotSiteParams);

			//write the project related data -- the remaining children nodes will be
			// automatically written
			//	writeProjectData(plotProjectParams);
			}
			//end the project tags
			endProject();
			//write the footer
			writeFooter();
		}
		catch (Exception ex) 
		{
			System.out.println("PlotXmlWriter > Exception: "
			+ex.getMessage() ); 
			ex.printStackTrace();
		}
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream(fileName));
			out.println(output.toString());
		}
		catch (Exception ex) 
		{
			System.out.println("Error printing the xml file "+ex);
			System.exit(1);
		}
	}



/**
 * This method will write a comprensive plot data file, which means a data file 
 * contains all of the data required for an an upload of a 'new' plot to the 
 * database that 
 *
 * @param multiPlotComprehensive - a hash table that stores moultiple
 * comprehensive plots
 *
 */
 
public void writeMultiplePlot(Hashtable multiPlotComprehensive, String outFile)
{
	getMultiplePlotXMLString(multiPlotComprehensive);

	try 
	{
		PrintStream out = new PrintStream(new FileOutputStream(outFile));
		out.println(output.toString());
	}
	catch (Exception ex) 
	{
		System.out.println("Error printing the xml file "+ex);
		System.exit(1);
	}
}


public String getMultiplePlotXMLString(Hashtable multiPlotComprehensive)
{
	try 
	{
		//prepare the header
		writeHeader();
	
		//figure out how many plots
		System.out.println("PlotXmlWriter.writeMultiplePlot: hash size: "
		+multiPlotComprehensive.size());
	
		//write all the plots
		for (int i=0; i< multiPlotComprehensive.size(); i++) 
		{
			String plotRecord = "plot"+i;
			Hashtable singlePlotSummary = (Hashtable)multiPlotComprehensive.get(plotRecord);
	
			//decompose the comprehensive plot data hastable
			dataDecomposer(singlePlotSummary);
	
			//write the project related data -- the remaining children nodes will be
			// automatically written
			writeProjectData(singlePlotSummary);
		}
		//write the footer
		writeFooter();
	}
	catch (Exception ex) 
	{
		System.out.println("PlotXmlWriter > Exception: "
		+ex.getMessage() ); 
		ex.printStackTrace();
	}
  System.out.println("Her I am ----->" + output);
	return output.toString();
}






/**
 * Method to decompose the 'comprehensivePlot' into data to be written to
 * specific nodes of the xml document
 *
 */
private void dataDecomposer(Hashtable comprehensivePlot)
{
try {
	//grab from the hash the project data
	plotProjectParams =(Hashtable)comprehensivePlot.get("projectParameters");
	//System.out.println( plotProjectParams.toString() );	
	
	//grab the hash that has all the plot site specific parameters
	plotSiteParams = (Hashtable)comprehensivePlot.get("siteParameters");
//	System.out.println("plotSiteParameters -- being decomposed "+plotSiteParams.toString());

	//grab the hash that has all the observational data
	plotObservationParams = (Hashtable)comprehensivePlot.get("observationParameters");
	if ( plotObservationParams.get("stratumName") != null)
	{
		stratumNameElements = (Vector)plotObservationParams.get("stratumName"); 
	}
	if ( plotObservationParams.get("stratumHeight") != null)
	{
		stratumHeightElements=(Vector)plotObservationParams.get("stratumHeight"); 
	}
	if ( plotObservationParams.get("stratumName") != null)
	{
		stratumCoverElements=(Vector)plotObservationParams.get("stratumName"); 
	}

	//the species specific attributes -- into vectors
	if ( comprehensivePlot.get("speciesParameters") != null )
	{
		plotSpeciesParams = (Hashtable)comprehensivePlot.get("speciesParameters");
		
		if ( plotSpeciesParams.get("taxonName") != null )
		{
			taxonNameElements = (Vector)plotSpeciesParams.get("taxonName");
		}
		if  ( plotSpeciesParams.get("cumStrataCover") != null )
		{
			cumStrataCoverElements=(Vector)plotSpeciesParams.get("cumStrataCover");
			System.out.println(" cum strata cover: " + plotSpeciesParams.get("cumStrataCover").toString());
		}
		if  ( plotSpeciesParams.get("stratum") != null )
		{
			strataTypeElements=(Vector)plotSpeciesParams.get("stratum");
		}
		if  ( plotSpeciesParams.get("cover") != null )
		{
			coverAmountElements=(Vector)plotSpeciesParams.get("cover");
		}
	}
	
	
	if  ( plotSpeciesParams.get("communityParameters") != null )
	{
		plotCommunityParams = (Hashtable)comprehensivePlot.get("communityParameters");
	}
	}
	catch (Exception e) 
	{
		System.out.println("failed in PlotXmlWriter.dataDecomposer "
		+" " + e.getMessage() );
	}
}





	/**
	 * Method to write a plot xml header
	 */
	private void writeHeader()
	{
		output.append("<?xml version=\"1.0\"?> \n"
		+"<!DOCTYPE vegPlot SYSTEM \"vegPlot2001cv.dtd\"> \n"
		+"<vegPlot> \n");
	}


	/**
	 * Method to write a plot xml footer
	 */
	private void writeFooter ()
	{
		output.append(" \n"
		+"</vegPlot> \n");
	}

	/**
	 * method that appends to the 'output' StringBuffer the 
	 * project information that is to be related to a series 
	 * of plots that will be stored in a single xml doculemt this
	 * document type is to be used to store a vegetation project 
	 * prior to upload to a database
	 *
	 */
	 private void startProject( Hashtable plotProjectParams)
	 {
		 //System.out.println( plotProjectParams.toString() );
		output.append(" \n"
		+"<project> \n"
		+"	<projectName>"+plotProjectParams.get("projectName")+"</projectName> \n"
		+"	<projectDescription>"+plotProjectParams.get("projectDescription")+"</projectDescription> \n"
		+"	<projectContributor>"+plotProjectParams.get("projectContributor")+"</projectContributor> \n"
		+"\n"
		); 
	 }
	
	/**
	 * method to end the project tags -- which should be called after writing the
	 * plot information but before writing the footer
	 */
	 private void endProject( )
	 {
		 output.append("	</project> \n"
		+"\n"
		);
	 }
	
	
	/**
	 * Method to write a plot xml header
	 */
	private void writeProjectData (Hashtable plotProjectParams)
	{
		//System.out.println( plotProjectParams.toString() );
		output.append(" \n"
		+"<project> \n"
		+"	<projectName>"+plotProjectParams.get("projectName")+"</projectName> \n"
		+"	<projectDescription>"+plotProjectParams.get("projectDescription")+"</projectDescription> \n"
		+"	<projectContributor>"+plotProjectParams.get("projectContributor")+"</projectContributor> \n"
		+"\n"
		);

		//print the site data here
		writeSiteData(plotSiteParams);

		//rememeber to end the project
		output.append("	</project> \n"
		+"\n"
		);
	}



	/**
	 * Method to write a plot site data into the string buffer called output
	 */
	private void writeSiteData (Hashtable plotSiteParams)
	{	
	
	//because at times data commining into this method
	//will not be from the database, but instead will 
	//beform legacy data there will not always be a 
	//'plotId'
	try
	{
		if ( plotSiteParams.containsKey("plotId") )
		{
			output.append(" <plot> \n");
			output.append(" 	<plotId>"+plotSiteParams.get("plotId")+"</plotId>  \n");
		}
		else
		{
			output.append(" <plot> \n");
		}
	
		output.append("<authorPlotCode>"+plotSiteParams.get("authorPlotCode").toString().replace('.','_')+"</authorPlotCode> \n"
			+"		<plotShape>"+plotSiteParams.get("topoShape")+"</plotShape> \n"
			+"		<slopeGradient>"+plotSiteParams.get("slopeGradient")+" </slopeGradient> \n"
			+"		<slopeAspect>"+plotSiteParams.get("slopeAspect")+" </slopeAspect> \n"
			+"		<surfGeo>"+plotSiteParams.get("surfGeo")+" </surfGeo> \n"
			+"		<hydrologicRegime>"+plotSiteParams.get("hydrologicRegime")+" </hydrologicRegime> \n"
			+"		<topoPosition>"+plotSiteParams.get("topoPosition")+"</topoPosition> \n"
			+"		<soilDrainage>"+plotSiteParams.get("soilDrainage")+" </soilDrainage> \n"
			+"		<elevationValue>"+plotSiteParams.get("elevationValue")+" </elevationValue> \n"
			+"		<origLat>"+plotSiteParams.get("origLat")+"</origLat> \n"
			+"		<origLong>"+plotSiteParams.get("origLong")+"</origLong> \n"
			+"		<plotSize>"+plotSiteParams.get("plotSize")+"</plotSize> \n"
			+"		<country>"+plotSiteParams.get("country")+"</country> \n"
			+"		<state>"+plotSiteParams.get("state")+"</state> \n"
			+"		<xCoord>"+plotSiteParams.get("xCoord")+"</xCoord> \n"
			+"		<yCoord>"+plotSiteParams.get("yCoord")+"</yCoord> \n"
			+"		<coordType>"+plotSiteParams.get("coordType")+"</coordType> \n"
			+"\n"
		);

	//print the observation data here
	writeObservationData(plotObservationParams);

	//end the plot
	output.append("	</plot> \n"
	+"\n"
	);
	}
	
	catch (Exception e) 
	{
		System.out.println("Caught Exception:  "
		+" " + e.getMessage()   );
		e.printStackTrace();
	}
	
	}

	/**
	 * Method to write a plot site data
	 */
	private void writeObservationData (Hashtable plotObservationParams)
	{
	try 
	{
		output.append("	<plotObservation> \n"
		+"			<soilDepth>"+plotObservationParams.get("soilDepth")+"</soilDepth> \n"
		+"			<soilType>"+plotObservationParams.get("soilType")+"</soilType> \n"
		+"			<percentRockGravel>"+plotObservationParams.get("percentRockGravel")+" </percentRockGravel> \n"
		+"			<authorObsCode>"+plotObservationParams.get("authorObsCode")+"</authorObsCode> \n"
		+"			<treeStemArea>"+plotObservationParams.get("treeStemArea")+"</treeStemArea> \n"
		+"			<perSmallRx>"+plotObservationParams.get("perSmallRx")+"</perSmallRx> \n"
		+"			<perSand>"+plotObservationParams.get("perSand")+"</perSand> \n"
		+"			<perLitter>"+plotObservationParams.get("perLitter")+"</perLitter> \n"
		+"			<perWood>"+plotObservationParams.get("perWood")+"</perWood> \n"
		+"			<perBareSoil>"+plotObservationParams.get("perBareSoil")+"</perBareSoil> \n"
		+"			<leafPhenology>"+plotObservationParams.get("leafPhenology")+"</leafPhenology> \n"
		+"			<leafType>"+plotObservationParams.get("leafType")+"</leafType> \n"
		+"			<physioClass>"+plotObservationParams.get("physioClass")+"</physioClass> \n"
		+"\n"
		+"<!-- strata data next-->"
	);

		//write the strata info
//		System.out.println("writing strata objects: number of elements: "+stratumNameElements.size() );
		writeStrataData();

		//write the community information
		writeCommunityData();

		//print the species info -- this is where they may be null values
		writeSpeciesData(taxonNameElements,  strataTypeElements,  coverAmountElements);

		output.append(
			"		</plotObservation> \n"
			+"\n"
		);
		}
		catch (Exception e) 
		{
			System.out.println("PlotXmlWriter > Exception: " + e.getMessage()   );
			e.printStackTrace();
		}
	}



/**
 * Method to write strata related data
 */
private void writeStrataData()
{
	try
	{
		output.append(" \n <!-- stratum elements should go here --> ");
	
		if (stratumNameElements.size() > 0)
		{
			for (int i=0; i<stratumNameElements.size(); i++) 
			{
 					output.append(" \n"
 						+"			<strata> \n"
 						+"				<strataName>"+(String)stratumNameElements.elementAt(i)+"</strataName> \n"
 						+"				<strataHeight>"+stratumHeightElements.elementAt(i)+"</strataHeight> \n"
 						+"				<strataCover>"+stratumCoverElements.elementAt(i)+"</strataCover> \n"
 						+"			</strata> \n"
					);
			}
		}
		else 
		{
			output.append(" \n <!-- stratum elements should go here; but none--> \n");
		}
	}
	catch (Exception e) 
	{
		System.out.println("PlotXmlWriter > Exception: " + e.getMessage()   );
		e.printStackTrace();
	}
}


/**
 * Method to write species related data
 */
private void writeSpeciesData (
	Vector taxonNameElements, 
	Vector strataTypeElements, 
	Vector coverAmountElements)
{
	try
	{
		if (taxonNameElements.isEmpty() == true )
		{
			output.append(" \n <!-- species should go here -->");
		}
		else 
		{
		//This is set up this way to rectify the failures that was having
		System.out.println("PlotXmlWriter > number of taxon elements: " + taxonNameElements.size());
		for (int i=0; i<taxonNameElements.size(); i++)
		{
			String strataType = null;
			String percentCover =null;
			String cumStrataCover =null;
			//check to see if these elements exist
////			if ( strataTypeElements.size() == taxonNameElements.size()  ) 
////				strataType = strataTypeElements.elementAt(i).toString();
////			else 
////				strataType = "fake";
////			if ( coverAmountElements.size() == taxonNameElements.size()  ) 
////				percentCover = coverAmountElements.elementAt(i).toString();
////			else
////				percentCover = "0";	
////			if ( cumStrataCoverElements.size() == taxonNameElements.size()  ) 
////				cumStrataCover = cumStrataCoverElements.elementAt(i).toString();
////			else
				cumStrataCover = "0";
			
			//clean the taxon string which is prone to having
			//sensetive characters.
			String taxon = (taxonNameElements.elementAt(i).toString()).replace('\'', ' ');
			output.append(" \n"
			+"			<taxonObservation> \n"
			+"				<authNameId>"+taxon.replace('&', '_')+"</authNameId> \n"
			//+"				<originalAuthority>"+nullValue+"</originalAuthority> \n"
			+"				<cumStrataCoverage>"+cumStrataCover+"</cumStrataCoverage> \n"
////			+"				<strataComposition> \n"
			//think about how the rest of these attributes shoul be handled
////			+"					<strataType>"+strataType+"</strataType> \n"
////			+"					<percentCover>"+percentCover+"</percentCover> \n"
////			+"				</strataComposition> \n"
			+"			</taxonObservation> \n"
			);
		}
		}
	}
	catch (Exception e )
	{
		System.out.println("PlotXmlWriter > Exception: " + e.getMessage() );
		e.printStackTrace();
	}
}



/**
 * Method to write community related data
 */
private void writeCommunityData ()
{
	try
	{
	output.append(" \n"
	+"			<communityAssignment> \n"
	+"				<communityName>"+plotCommunityParams.get("communityName")+"</communityName> \n"
	+"				<CEGLCode>"+plotCommunityParams.get("CEGLCode")+"</CEGLCode> \n"
	+"			</communityAssignment> \n"
	);
	}
	catch (Exception e) 
	{
			System.out.println("PlotXmlWriter > Exception: " + e.getMessage()   );
			e.printStackTrace();
	}

}






/**
 * Method to write party related data
 */
	private void writePartyData (Hashtable plotSiteParams)
	{
		output.append("<vegPlot>");
		output.append("<project>");
		output.append("<projectName>"+plotSiteParams.get("authorCode")+"</projectName>");
		output.append("<projectDescription> TNC PROJECT: </projectDescription>");
		output.append("  <projectContributor>");
		output.append("		<role>"+nullValue+"</role>");
		output.append("		<party>");
		output.append("			<salutation>"+nullValue+"</salutation>");
		output.append("			<givenName>"+nullValue+"</givenName>");
		output.append("			<surName>"+nullValue+" </surName>");
		output.append("			<organizationName>"+nullValue+"</organizationName>");
		output.append("			<positionName>"+nullValue+"</positionName>");
		output.append("			<hoursOfService>"+nullValue+"</hoursOfService>");
		output.append("			<contactInstructions>"+nullValue+" </contactInstructions>");
		output.append("			<onlineResource>");
		output.append("				<linkage>"+nullValue+"</linkage>");
		output.append("				<protocol>"+nullValue+"</protocol>");
		output.append("				<name>"+nullValue+"</name>");
		output.append("				<applicationProfile>"+nullValue+"</applicationProfile>");
		output.append("				<description>"+nullValue+"</description>");
		output.append("			</onlineResource>");
	}



}
