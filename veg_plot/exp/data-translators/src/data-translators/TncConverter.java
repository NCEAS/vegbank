/**
 * 
 *    Purpose: To convert tnc datasets to an xml file that can be loaded to the
 * 			veg plots database
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-11-01 17:39:00 $'
 * 	'$Revision: 1.1 $'
 *
 */
//package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

import xmlresource.utils.PlotDataMapper;
import xmlresource.utils.PlotXmlWriter;
//import vegclient.framework.*;

public class TncConverter 
{

public Vector errorLog = new Vector();

//the resource bundle to store the default information about TNC plots
//ResourceBundle rb = ResourceBundle.getBundle("tncPlots");
//vector to contain data from site file
Vector siteFileVector = new Vector(); 
//vector to contain data from species file
Vector speciesFileVector = new Vector();

//the vectors below store the species -related information gathered in the
// 'parseTNCData' method and the elements stored at a single postion will be
// related to those elements stored in the same psoition in the other two
// vectors
Vector taxonNameVector = new Vector(); //vector to contain series of taxon names
Vector taxonStrataVector = new Vector(); //vector store for corresp. srata
Vector taxonCoverVector = new Vector(); //vector store for corresp cover



String projectName = null; //get from resource bundle
String projectDescription = null; //get from resource bundle


String buf=null;
String subPlot=null;
String authorPlotCode=null;
String namedPlace=null; //stored in the res. bundle
String surDateReal=null; //date as stored in data set
String state=null; //stored in res. bundle
String authorObsCode=null; //madup value
/*subPlotParentCode:3*/ String subPlotParentCode=null;
/*airPhotoNum:3*/ String airPhotoNum=null;
/*polyCode:3*/ String polyCode=null;
/*provCommName:3*/ String provCommName=null;
/*classCommName:3*/ String classCommName=null;
/*tncElCode:3*/ String tncElCode=null;
/*tncEonumSuf:3*/ String tncEonumSuf=null;
/*locCode:3*/ String locCode=null;
/*subLoc:3*/ String subLoc=null;
/*quadName:3*/ String quadName=null;
/*quadCode:3*/ String quadCode=null;
/*coordSys:3*/ String coordSys=null;
/*gpsFile:3*/ String gpsFile=null;
/*gpsTech:3*/ String gpsTech=null;
/*fieldUtmX:3*/ String fieldUtmX=null;
/*fieldUtmY:3*/ String fieldUtmY=null;
/*corUtmX:3*/ String corUtmX=null;
/*corUtmY:3*/ String corUtmY=null;
/*utmZone:3*/ String utmZone=null;
/*fieldLat:3*/ String fieldLat=null;
/*fieldLong:3*/ String fieldLong=null;
/*corLat:3*/ String corLat=null;
/*corLong:3*/ String corLong=null;
/*surDate:3*/ String surDate=null;
/*surveyor:3*/ String surveyor=null;
/*plotDir:3*/ String plotDir=null;
/*Xdim:3*/ String Xdim=null;
/*Ydim:3*/ String Ydim=null;
/*plotShape:3*/ String plotShape=null;
/*photo:3*/ String photos=null;
/*rollNum:3*/ String rollNum=null;
/*frameNum:3*/ String frameNum=null;
/*perm:3*/ String perm=null;
/*represent:3*/ String represent=null;
/*elev:3*/ String elev=null;
/*elevUnits:3*/ String elevUnits=null;
/*slope:3*/ String slope=null;
			String aspect=null;
/*topoPos:3*/ String topoPos=null;
/*landForm:3*/ String landForm=null;
/*surfGeo:3*/ String surfGeo=null;
/*cowardinSys:3*/ String cowardinSys=null;
/*hydroRegime:3*/ String hydroRegime=null;
/*salinity:3*/ String salinity=null;
/*hydroEvidence:3*/ String hydroEvidence=null;
/*environComment:3*/ String environComment=null;
/*landscapeComment:3*/ String landscapeComment=null;
/*soilTaxon:3*/ String soilTaxon=null;
/*soilTex:3*/ String soilTex=null;
/*soilDepth:3*/ String soilDepth=null;
/*soilDepthUnit:3*/ String soilDepthUnit=null;
/*soilDrain:3*/ String soilDrain=null;
/*perBedRx:3*/ String perBedRx=null;
/*perLargeRx:3*/ String perLargeRx=null;
String perSmallRx=null;
String perSand=null;
String 	perLitter=null;
String 	perWood=null;
String perWater=null;
String 	perBareSoil=null;
String 	perOther1=null;
String 	perOther2=null;
String 	leafPhenology=null;
String 	leafType=null;
String 	physioClass=null;
String 	t1Ht=null;
String 	t1Cover=null;
String 	t2Ht=null;
String 	t2Cover=null;
String 	t3Ht=null;
String 	t3Cover=null;
String 	s1Ht=null;
String 	s1Cover=null;
String 	s2Ht=null;
String 	s2Cover=null;
String 	s3Ht=null;
String 	s3Cover=null;
String 	hHt=null;
String 	hCover=null;
String 	nHt=null;
String 	nCover=null;
String 	vHt=null;
String 	vCover=null;
String 	eHt=null;
String 	eCover=null;


	public PlotXmlWriter xmlwriter = new PlotXmlWriter();


	/**
	 * Main method for testing using a TNC site and species data set
	 *
	 */	
	public static void main(String[] args) 
	{

		/*print error meassge if zero arguments passed to app*/
		if (args.length < 2) 
		{
			System.out.println("Wrong number of arguments passed to TNCplot2xml\n");
			System.out.println("USAGE: java TncConverter <TNCplot.file> <TNCspecies.file> \n");
			System.out.println("The output from this will be a file called outPlotX.xml \n"+
			"where X corresponds to the plot number in the TNC file");
			System.exit(0);
		}
		else
		{
			String siteFile=args[0];
			String speciesFile=args[1];

			//pass the datfiles to the method that does the transformation process
			TncConverter lf = new TncConverter();
			lf.transformTNCData(siteFile, speciesFile);
		}
	}
	
	/**
	 * method that returns the number of unique plots in a tnc plots dataset
	 */
	 public int getNumberOfPlots(String siteFile)
	 {
		int numberOfPlots = 0;
		try 
		{ 
			BufferedReader inSite = new BufferedReader(new FileReader(siteFile));
			String s;
			while((s = inSite.readLine()) != null) 
			{	
				numberOfPlots++;
			}
		}
		catch ( Exception e ) 
		{
			System.out.println("failed in: "
			+e.getMessage());
		}
		return(numberOfPlots);
	 }	
	
	/**
	 * This method takes the input TNC files and transforms them to xml output
	 * consistent with the format that must be read into the database loader class
	 *
	 */
	public void transformTNCData(String siteFile, String speciesFile)
	{
		//this is the vector that stores all the plots as they exist in mapped
		// hashtables
		Vector projectVector = new Vector();
		
		//read the data files into memory -- vectors
		readTNCData(siteFile, speciesFile);
		
		

		//parse the data into their repective elements - one line at a time 
		//(meaning one plot at a time)
		for (int i=0; i<siteFileVector.size(); i++) 
		{
			//for (int i=0; i<1; i++) {
			parseTNCData(i);

			//pass the elements to the class that will map the elements into a hashtable
			//and print to a file and also retrieve the hashtable storing the plot
			// data
			Hashtable plot=mapTNCElements("./data/test"+i+".xml");
			projectVector.addElement(plot);
			//System.out.println(plot.toString());
		}
		//now print the project file
		xmlwriter.writeProjectFile( projectVector, "test_project.xml");
}




	/**
	 * Method to read the two TNC files into memory (vectors) that can be accessed
	 * by other methods in this class
	 *
	 */
	private void readTNCData (String siteFile, String speciesFile)
	{
		//read the data into vectors 
		try 
		{ 
			BufferedReader inSite = new BufferedReader(new FileReader(siteFile));
			BufferedReader inSpecies = new BufferedReader(new FileReader(speciesFile));
			String s;
			while((s = inSite.readLine()) != null) 
			{	
				siteFileVector.addElement(s);
				//vecElementCnt++;
			}

			while((s = inSpecies.readLine()) != null) 
			{	
				speciesFileVector.addElement(s);
				//vecElementCnt++;
			}
		}
		catch ( Exception e ) 
		{
			System.out.println("failed in: "
			+e.getMessage());
		}
}




	/**
	 * Method to parse the information stored in a TNC plots and species data file
	 * into database elements that can be passed to the mapping method
	 *
	 * @param i -- the level of the vector for which the parser should perform the
	 * parsing
	 *
	 */
	private void parseTNCData (int i)
	{
	

		//tokenize the elements into their respective hash tables -- this is done one
		// row at a time where the calling class dictates what level of the vector
		// should be parsed


		//remove all the species elements already stored in the rpecies-related vectors
		taxonNameVector.removeAllElements(); 
		taxonStrataVector.removeAllElements(); 
		taxonCoverVector.removeAllElements(); 


		try 
		{
			StringTokenizer t = new StringTokenizer(siteFileVector.elementAt(i).toString(), "|");
			authorPlotCode=t.nextToken().replace('"',' ').trim();
//try adding some info to the error log
errorLog.addElement(authorPlotCode);

//			System.out.println("authorPlotCode: "+authorPlotCode);
			buf=t.nextToken().replace('"',' ').trim();
			subPlot=buf;
			subPlotParentCode=t.nextToken().replace('"',' ').trim();
			buf=t.nextToken().replace('"',' ').trim();
			polyCode=t.nextToken().replace('"',' ').trim();
			provCommName=t.nextToken().replace('"',' ').trim();
			classCommName=t.nextToken().replace('"',' ').trim();
			tncElCode=t.nextToken().replace('"',' ').trim();
			tncEonumSuf=t.nextToken().replace('"',' ').trim();
			locCode=t.nextToken().replace('"',' ').trim();
			subLoc=t.nextToken().replace('"',' ').trim();
			quadName=t.nextToken().replace('"',' ').trim();
			quadCode=t.nextToken().replace('"',' ').trim();
			coordSys=t.nextToken().replace('"',' ').trim();
			gpsFile=t.nextToken().replace('"',' ').trim();
			gpsTech=t.nextToken().replace('"',' ').trim();
			fieldUtmX=t.nextToken().replace('"',' ').trim();
			fieldUtmY=t.nextToken().replace('"',' ').trim();
			corUtmX=t.nextToken().replace('"',' ').trim();
			corUtmY=t.nextToken().replace('"',' ').trim();
			utmZone=t.nextToken().replace('"',' ').trim();
			fieldLat=t.nextToken().replace('"',' ').trim();
			fieldLong=t.nextToken().replace('"',' ').trim();
			corLat=t.nextToken().replace('"',' ').trim();
			corLong=t.nextToken().replace('"',' ').trim();
			surDateReal=t.nextToken().replace(':','0').trim().replace(' ','#'); //this is real date
			surDate="24-AUG-2000";
		
			//make a fake authorObsCode
			authorObsCode= authorPlotCode+surDateReal.replace('/','_').trim();
		
			surveyor=t.nextToken().replace('"',' ').trim();
			plotDir=t.nextToken().replace('"',' ').trim();
			Xdim=t.nextToken().replace('"',' ').trim();
			Ydim=t.nextToken().replace('"',' ').trim();
			/*temporarily store the size as a string = Xdim this should be changed*/
			plotShape=t.nextToken().replace('"',' ').trim();
			photos=t.nextToken().replace('"',' ').trim();
			rollNum=t.nextToken().replace('"',' ').trim();
			frameNum=t.nextToken().replace('"',' ').trim();
			perm=t.nextToken().replace('"',' ').trim();
			represent=t.nextToken().replace('"',' ').trim();
			elev=t.nextToken().replace('"',' ').trim();
			elevUnits=t.nextToken().replace('"',' ').trim();
			slope=t.nextToken().replace('"',' ').trim();
			aspect=t.nextToken().replace('"',' ').trim();
			topoPos=t.nextToken().replace('"',' ').trim();
			landForm=t.nextToken().replace('"',' ').trim();
			surfGeo=t.nextToken().replace('"',' ').trim();
			cowardinSys=t.nextToken().replace('"',' ').trim();
			hydroRegime=t.nextToken().replace('"',' ').trim();
			salinity=t.nextToken().replace('"',' ').trim();
			hydroEvidence=t.nextToken().replace('"',' ').trim();
			environComment=t.nextToken().replace('"',' ').trim();
			landscapeComment=t.nextToken().replace('"',' ').trim();
			soilTaxon=t.nextToken().replace('"',' ').trim();
			soilTex=t.nextToken().replace('"',' ').trim();
			soilDepth=t.nextToken().replace('"',' ').trim();
			soilDepthUnit=t.nextToken().replace('"',' ').trim();
			soilDrain=t.nextToken().replace('"',' ').trim();
			perBedRx=t.nextToken().replace('"',' ').trim();
			perLargeRx=t.nextToken().replace('"',' ').trim();
		
			perSmallRx=t.nextToken().replace('"',' ').trim();
			perSand=t.nextToken().replace('"',' ').trim();
			perLitter=t.nextToken().replace('"',' ').trim();
			perWood=t.nextToken().replace('"',' ').trim();
			perWater=t.nextToken().replace('"',' ').trim();
			perBareSoil=t.nextToken().replace('"',' ').trim();
			perOther1=t.nextToken().replace('"',' ').trim();
			perOther2=t.nextToken().replace('"',' ').trim();
			leafPhenology=t.nextToken().replace('"',' ').trim();
			leafType=t.nextToken().replace('"',' ').trim();
			physioClass=t.nextToken().replace('"',' ').trim();
			t1Ht=t.nextToken().replace('"',' ').trim();
			t1Cover=t.nextToken().replace('"',' ').trim();
			t2Ht=t.nextToken().replace('"',' ').trim();
			t2Cover=t.nextToken().replace('"',' ').trim();
			t3Ht=t.nextToken().replace('"',' ').trim();
			t3Cover=t.nextToken().replace('"',' ').trim();
			s1Ht=t.nextToken().replace('"',' ').trim();
			s1Cover=t.nextToken().replace('"',' ').trim();
			s2Ht=t.nextToken().replace('"',' ').trim();
			s2Cover=t.nextToken().replace('"',' ').trim();
			s3Ht=t.nextToken().replace('"',' ').trim();
			s3Cover=t.nextToken().replace('"',' ').trim();
			hHt=t.nextToken().replace('"',' ').trim();
			hCover=t.nextToken().replace('"',' ').trim();
			nHt=t.nextToken().replace('"',' ').trim();
			nCover=t.nextToken().replace('"',' ').trim();
			vHt=t.nextToken().replace('"',' ').trim();
			vCover=t.nextToken().replace('"',' ').trim();
			eHt=t.nextToken().replace('"',' ').trim();
			eCover=t.nextToken().replace('"',' ').trim();
		
		
		//grab and parse the related species information
		for (int ii=0; ii<speciesFileVector.size(); ii++) 
		{
			if ( (speciesFileVector.elementAt(ii).toString().startsWith(authorPlotCode) ) ) 
			{
				//System.out.println( speciesFileVector.elementAt(ii).toString() );
				StringTokenizer speciesTok = new StringTokenizer(speciesFileVector.elementAt(ii).toString(), "|");
				
				String speciesBuf= speciesTok.nextToken();
				String speciesPlotCodeIndex = speciesBuf.trim();
				
				//this is there to make sure that we are looking at the correct
				// plot in the species list
				if (speciesPlotCodeIndex.equals(authorPlotCode.trim()) ) 
				{
				
///					System.out.println(speciesBuf);
				//}
				
					String strataBuf= null;
					String coverBuf=null;
					speciesBuf= speciesTok.nextToken();
					int bufCount=0;
		
					//this counts out to the position where the species is stored
					while (speciesTok.hasMoreTokens() && bufCount<4 ) {
						speciesBuf= speciesTok.nextToken();
						bufCount++;
//						taxonNameVector.addElement(speciesBuf); //species name
					} 
		
		
					//count out to the strata name and capture
					while (speciesTok.hasMoreTokens() && bufCount<12 ) {
						strataBuf= speciesTok.nextToken();
						bufCount++;
//						taxonNameVector.addElement(speciesBuf); //species name
					} 
		
					//count out to the strata real cover
					while (speciesTok.hasMoreTokens() && bufCount<15 ) {
						coverBuf= speciesTok.nextToken();
						bufCount++;
//						taxonNameVector.addElement(speciesBuf); //species name
					} 
				
					//add these values to the respective vector
					taxonNameVector.addElement(speciesBuf); //species name
					taxonStrataVector.addElement(strataBuf); //strata position
					taxonCoverVector.addElement(coverBuf); //coverage for taxon in that strata
			
			}
		}
	}	
	}
	catch ( Exception e ) 
	{
		System.out.println("failed in: LegacyDataFormatter.parseTNCData "
		+" -- the second try"+e.getMessage());  
		e.printStackTrace();
	}
}


	/**
	 * Method to map the elemts parsed from the tnc data file into hash tables
	 * stored in the 'PlotDataMapper' class
	 *
	 */
	private Hashtable mapTNCElements (String outFileName)
	{

		//get the defaults from the properties files
		//projectName=(rb.getString("requestparams.projectName"));
		//projectDescription=(rb.getString("requestparams.projectDescription"));

		//state=(rb.getString("requestparams.state"));
		//namedPlace=(rb.getString("requestparams.namedPlace"));

		projectName = "defaultProjectName";
		projectDescription = "defaultProjectDesc";
		state = "defaultState";
		namedPlace = "defaultNamedPlace";

		//pass these defaults to the element mapper -- project specific elements
		PlotDataMapper pdm = new PlotDataMapper();
		pdm.plotElementMapper(projectName, "projectName", "project");
		pdm.plotElementMapper(projectDescription, "projectDescription", "project");


		//pass the site specific variables
		pdm.plotElementMapper(authorPlotCode, "authorPlotCode", "site");
		pdm.plotElementMapper(plotShape, "shape", "site");
		//...
		pdm.plotElementMapper(slope, "slopeGradient", "site");
		pdm.plotElementMapper(aspect, "slopeAspect", "site");
		pdm.plotElementMapper(surfGeo, "surfGeo", "site");
		pdm.plotElementMapper(hydroRegime, "hydrologicRegime", "site");
		pdm.plotElementMapper(topoPos, "topoPosition", "site");
		pdm.plotElementMapper(soilDrain, "soilDrainage", "site");
		pdm.plotElementMapper(elev, "elevationValue", "site");
		pdm.plotElementMapper(state, "state", "site");
		pdm.plotElementMapper(corUtmX, "xCoord", "site");
		pdm.plotElementMapper(corUtmY, "yCoord", "site");
		pdm.plotElementMapper("zone"+utmZone, "coordType", "site");


		//pass the observational data
		pdm.plotElementMapper(soilTaxon, "soilType", "observation");
		pdm.plotElementMapper(perLargeRx, "percentRockGravel", "observation");
		pdm.plotElementMapper(soilDepth, "soilDepth", "observation");
		pdm.plotElementMapper(authorObsCode, "authorObsCode", "observation");

		pdm.plotElementMapper(perSmallRx, "perSmallRx", "observation");
		pdm.plotElementMapper(perSand, "perSand", "observation");
		pdm.plotElementMapper(perLitter, "perLitter", "observation");
		pdm.plotElementMapper(perWood, "perWood", "observation");
		pdm.plotElementMapper(perBareSoil, "perBareSoil", "observation");
		pdm.plotElementMapper(leafPhenology, "leafPhenology", "observation");
		pdm.plotElementMapper(leafType, "leafType", "observation");
		pdm.plotElementMapper(physioClass, "phisioClass", "observation");
		//strata info
		pdm.plotElementMapper("t1", "stratumName", "observation");
		pdm.plotElementMapper(t1Ht, "stratumHeight", "observation");
		pdm.plotElementMapper(t1Cover, "stratumCover", "observation");
		pdm.plotElementMapper("t2", "stratumName", "observation");
		pdm.plotElementMapper(t2Ht, "stratumHeight", "observation");
		pdm.plotElementMapper(t2Cover, "stratumCover", "observation");
		pdm.plotElementMapper("t3", "stratumName", "observation");
		pdm.plotElementMapper(t3Ht, "stratumHeight", "observation");
		pdm.plotElementMapper(t3Cover, "stratumCover", "observation");
		pdm.plotElementMapper("s1", "stratumName", "observation");
		pdm.plotElementMapper(s1Ht, "stratumHeight", "observation");
		pdm.plotElementMapper(s1Cover, "stratumCover", "observation");
		pdm.plotElementMapper("s2", "stratumName", "observation");
		pdm.plotElementMapper(s2Ht, "stratumHeight", "observation");
		pdm.plotElementMapper(s2Cover, "stratumCover", "observation");
		pdm.plotElementMapper("h", "stratumName", "observation");
		pdm.plotElementMapper(hHt, "stratumHeight", "observation");
		pdm.plotElementMapper(hCover, "stratumCover", "observation");
		pdm.plotElementMapper("n", "stratumName", "observation");
		pdm.plotElementMapper(nHt, "stratumHeight", "observation");
		pdm.plotElementMapper(nCover, "stratumCover", "observation");
		pdm.plotElementMapper("v", "stratumName", "observation");
		pdm.plotElementMapper(vHt, "stratumHeight", "observation");
		pdm.plotElementMapper(vCover, "stratumCover", "observation");
		pdm.plotElementMapper("e", "stratumName", "observation");
		pdm.plotElementMapper(eHt, "stratumHeight", "observation");
		pdm.plotElementMapper(eCover, "stratumCover", "observation");

		//community mappings
		pdm.plotElementMapper(classCommName, "communityName", "community");
		pdm.plotElementMapper(tncElCode, "CEGLCode", "community");


		//species mappings 
		for (int ii=0; ii<taxonNameVector.size(); ii++) 
		{
			pdm.plotElementMapper(taxonNameVector.elementAt(ii).toString(), "taxonName", "species");
			pdm.plotElementMapper(taxonStrataVector.elementAt(ii).toString(), "stratum", "species");
			pdm.plotElementMapper(taxonCoverVector.elementAt(ii).toString(), "cover", "species");
		}

		//now pass the categories to the consolidator -- to make a single plot
		pdm.plotElementConsolidator(pdm.plotProjectParams, pdm.plotSiteParams, 
		pdm.plotObservationParams, pdm.plotCommunityParams,	pdm.plotSpeciesParams);


		//now print the xml file using the consolidated hash table made above
		PlotXmlWriter pxw = new PlotXmlWriter();
		pxw.writePlot(pdm.comprehensivePlot, outFileName);
		
		//return the hashtable that represents the entire plot
		return(pdm.comprehensivePlot);
}


}
