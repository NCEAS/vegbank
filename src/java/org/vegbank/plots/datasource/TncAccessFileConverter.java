package org.vegbank.plots.datasource;

/**
 * 
 *	Purpose: To convert tnc datasets to an xml file that can be loaded 
 *						to the veg plots database -- this class works specifically 
 *						with the microsoft Access file that has to be 
 *	Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: farrell $'
 *  '$Date: 2003-08-21 21:16:45 $'
 * 	'$Revision: 1.3 $'
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

import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.xmlresource.*;


public class TncAccessFileConverter 
{
	private PlotXmlWriter xmlwriter = new PlotXmlWriter();
	//instatntiate the tnc plots database class that is responsible
	//for querying the mdb file from an odbc src
	private TNCPlotsDB db = new TNCPlotsDB();
	
	public TncAccessFileConverter(String odbcSource)
	{
		//get the names of the plots
		Vector plotVec = db.getPlotCodes();
		//a vec to store all the plots, each as a hashtable
		Vector packageVector = new Vector();
		
		//process each plot
		for (int i=0; i<plotVec.size(); i++)
		{
			db.getPlotData(plotVec.elementAt(i).toString());
			//map all the elements for this plot and will print each individual
			//plot as a plot file, as well as returns the plot as a hashtable
			packageVector.addElement( this.mapTNCElements() );
		}
		//now print the package plot file -- will contain all the plots
		xmlwriter.writeProjectFile( packageVector, "test_project.xml");
	}


	
	/**
	 * Method to map the elemts parsed from the tnc data file into hash tables
	 * stored in the 'PlotDataMapper' class
	 *
	 */
	private Hashtable mapTNCElements()
	{
		//pass these defaults to the element mapper -- project specific elements
		PlotDataMapper pdm = new PlotDataMapper();
		pdm.plotElementMapper(db.projectName, "projectName", "project");
		pdm.plotElementMapper(db.projectDescription, "projectDescription", "project");


		//pass the site specific variables
		pdm.plotElementMapper(db.plotCode, "authorPlotCode", "site");
		pdm.plotElementMapper(db.plotShape, "shape", "site");
		//...
		pdm.plotElementMapper(db.slope, "slopeGradient", "site");
		pdm.plotElementMapper(db.aspect, "slopeAspect", "site");
		pdm.plotElementMapper(db.surfGeo, "surfGeo", "site");
		pdm.plotElementMapper(db.elevation, "elevationValue", "site");
		pdm.plotElementMapper(db.state, "state", "site");
		pdm.plotElementMapper(db.xCoord, "xCoord", "site");
		pdm.plotElementMapper(db.yCoord, "yCoord", "site");
		pdm.plotElementMapper(db.utmZone, "coordType", "site");
		pdm.plotElementMapper(db.placeName, "placeName", "site");


		//pass the observational data
		pdm.plotElementMapper(db.soilDepth, "soilDepth", "observation");
		pdm.plotElementMapper(db.authorObsCode, "authorObsCode", "observation");
		
		
		//System.out.println("cover vals: " + db.t1Height + " " + db.t1Cover);
		//strata info -- these cannot be null
		pdm.plotElementMapper("t1", "stratumName", "observation");
		pdm.plotElementMapper( db.t1Height , "stratumHeight", "observation");
		pdm.plotElementMapper( db.t1Cover , "stratumCover", "observation");
		//System.out.println( "t1 cover: "+ db.t1Cover);
		pdm.plotElementMapper( "t2" , "stratumName", "observation");
		pdm.plotElementMapper( db.t2Height, "stratumHeight", "observation");
		pdm.plotElementMapper( db.t2Cover, "stratumCover", "observation");
		pdm.plotElementMapper( "t3", "stratumName", "observation");
		pdm.plotElementMapper( db.t3Height, "stratumHeight", "observation");
		pdm.plotElementMapper( db.t3Cover , "stratumCover", "observation");
//		pdm.plotElementMapper("s1", "stratumName", "observation");
//		pdm.plotElementMapper("s1Ht", "stratumHeight", "observation");
//		pdm.plotElementMapper("s1Cover", "stratumCover", "observation");
//		pdm.plotElementMapper("s2", "stratumName", "observation");
//		pdm.plotElementMapper("s2Ht", "stratumHeight", "observation");
//		pdm.plotElementMapper("s2Cover", "stratumCover", "observation");
//		pdm.plotElementMapper("h", "stratumName", "observation");
//		pdm.plotElementMapper("hHt", "stratumHeight", "observation");
//		pdm.plotElementMapper("hCover", "stratumCover", "observation");
//		pdm.plotElementMapper("n", "stratumName", "observation");
//		pdm.plotElementMapper("nHt", "stratumHeight", "observation");
//		pdm.plotElementMapper("nCover", "stratumCover", "observation");
//		pdm.plotElementMapper("v", "stratumName", "observation");
//		pdm.plotElementMapper("vHt", "stratumHeight", "observation");
//		pdm.plotElementMapper("vCover", "stratumCover", "observation");
//		pdm.plotElementMapper("e", "stratumName", "observation");
//		pdm.plotElementMapper("eHt", "stratumHeight", "observation");
//		pdm.plotElementMapper("eCover", "stratumCover", "observation");

		//community mappings
		pdm.plotElementMapper(db.communityName, "communityName", "community");
		pdm.plotElementMapper(db.communityCode, "CEGLCode", "community");


		//species mappings 
		for (int ii=0; ii<db.scientificNames.size(); ii++) 
		{
			pdm.plotElementMapper(db.scientificNames.elementAt(ii).toString(), "taxonName", "species");
			pdm.plotElementMapper("fake strata", "stratum", "species");
			pdm.plotElementMapper("fake-cover", "cover", "species");
		}

		//now pass the categories to the consolidator -- to make a single plot
		pdm.plotElementConsolidator(pdm.plotProjectParams, 
			pdm.plotSiteParams, 
			pdm.plotObservationParams, 
			pdm.plotCommunityParams,	
			pdm.plotSpeciesParams);


		//now print the xml file using the consolidated hash table made above
		PlotXmlWriter pxw = new PlotXmlWriter();
		pxw.writePlot(pdm.comprehensivePlot, "out");
		
		//return the hashtable that represents the entire plot
		return(pdm.comprehensivePlot);
}


	/**
	 * Main method for testing using a TNC access ODBC source
	 */	
	public static void main(String[] args) 
	{
		if (args.length != 1)
		{
			System.out.println("Wrong number of arguments \n");
			System.out.println("USAGE: java TncAccessFileConverter <outfile> \n");
			System.exit(0);
		}
		else
		{
			String outFile = args[0];
			TncAccessFileConverter l = new TncAccessFileConverter( outFile );
			//lf.transformTNCData(siteFile, speciesFile);
		}
	}
	
	
}
