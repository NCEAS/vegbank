package org.vegbank.databaseAccess;

/**
 *  '$RCSfile: xmlWriter.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: anderson $'
 *     '$Date: 2004-10-14 09:44:37 $'
 * '$Revision: 1.2 $'
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
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * This class will write out an xml file containing the results from
 * database queries.  These xml files will be consistent with either
 * vegPlot.dtd, or the community dtd file or the plant taxonomy dtd file
 *
 * @author @author@
 *
 */

public class  xmlWriter
{

	/**
 	 *  This method accepts a hashtable containg plot summary
 	 *  data and prints it to file in an xml format
   *
   */
	public String  writePlotSummary(Hashtable cumulativeSummaryResultHash)
	{
		String xmlResult = null;
		try
		{

			// Who knows what this is up to ????  Black box
			Hashtable multiPlotComprehensive =
				doHashMunging(cumulativeSummaryResultHash);

			//take the returned hash table and pass it to the xml writer class
			//put a try here b/c this class has been sensitive
			try
			{
				PlotXmlWriter pxw = new PlotXmlWriter();
				xmlResult = pxw.getMultiplePlotXMLString(multiPlotComprehensive);
			}
			catch (Exception e)
			{
				System.out.println("failed in xmlWriter.writePlotSummary "
				+"(using a hash table as input ) -- trying to write xml output" +
				e.getMessage());
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			System.out.println("failed in xmlWriter.writePlotSummary "
			+"(using a hash table as input )" +
			e.getMessage());
		}
		return xmlResult;
	}


	private Hashtable doHashMunging(Hashtable cumulativeSummaryResultHash)
	{
		Hashtable singlePlotSummary = new Hashtable();
		Hashtable multiPlotComprehensive = new Hashtable();


		//get the number of plots stored in hash
		System.out.println("xmlWriter > hash size: "+cumulativeSummaryResultHash.size());

		//write one plot at a time
		for (int i=0; i< cumulativeSummaryResultHash.size(); i++)
		{
			//get a single plot and stick into a temporary hash
			String plotRecord = "plot"+i;
			singlePlotSummary = (Hashtable)cumulativeSummaryResultHash.get(plotRecord);

			//pass this hash table containing a single plot to the method that will map the
			//elements that can be passed directly to the 'PlotXmlWriterClass'
			Hashtable singleFormatPlot = mapSummaryElements(singlePlotSummary);

			//take the single plot that is returned and put it in a hash with all the
			//other plots in the result set
			multiPlotComprehensive.put("plot"+i,singleFormatPlot);
		}
		return multiPlotComprehensive;
	}


	/**
 	 * Method to map the plot summary elements being passed
 	 * to this class in a hash table into the 'PlotDataMapper'
 	 * class which prepares the data for printing in the
 	 * 'PlotXmlWriter' class
 	 *
 	 * @param singlePlotSummary - is a hash table that contains all the summary
 	 * elements for a given plot
 	 */
	private Hashtable mapSummaryElements (Hashtable singlePlotSummary)
	{
		//pass the elements to the plotDatamapper class
		PlotDataMapper pdm = new PlotDataMapper();

		String plotId = "nulValue";
		String authorPlotCode = "nullValue";
		String plotShape = "nullValue";
		String slopeGradient = "nullValue";
		String slopeAspect = "nullValue";
		String surfGeo = "nullValue";
		String hydrologicRegime = "nullValue";
		String topoPosition = "nullValue";
		String soilDrainage = "nullValue";
		String elevationValue = "nullValue";
		String state = "nullValue";
		String xCoord = "nullValue";
		String yCoord = "nullValue";
		String coordType = "nullValue";
		String soilType = "nullValue";
		String percentSoil = "nullValue";
		String percentSand = "nullValue";
		String percentLitter = "nullValue";
		String percentWood = "nullValue";
		String soilDepth = "nullValue";
		String leafType = "nullValue";
		String currentCommunity = "nullValue";
		String physionomicClass = "nullValue";
		String authorObservationCode = "nullValue";

		try
		{
			//first get the elements  - then next map them using
			//the 'PlotDataMapper' class
			if (singlePlotSummary.containsKey("PLOT_ID"))
			{
				//	System.out.println("Does contain a plotId!");
				plotId = (String)singlePlotSummary.get("PLOT_ID");
			}
			if (singlePlotSummary.containsKey("AUTHORPLOTCODE"))
				authorPlotCode = (String)singlePlotSummary.get("AUTHORPLOTCODE");
			if (singlePlotSummary.containsKey("PLOTSHAPE"))
				plotShape = (String)singlePlotSummary.get("PLOTSHAPE");
			if (singlePlotSummary.containsKey("SLOPEGRADIENT"))
				slopeGradient = (String)singlePlotSummary.get("SLOPEGRADIENT");
			if (singlePlotSummary.containsKey("SLOPEASPECT"))
				slopeAspect = (String)singlePlotSummary.get("SLOPEASPECT");
			if (singlePlotSummary.containsKey("SURFGEO"))
				surfGeo = (String)singlePlotSummary.get("SURFGEO");
			if (singlePlotSummary.containsKey("HYDROLOGICREGIME"))
				hydrologicRegime = (String)singlePlotSummary.get("HYDROLOGICREGIME");
			if (singlePlotSummary.containsKey("SLOPEPOSITION"))
				topoPosition = (String)singlePlotSummary.get("SLOPEPOSITION");
			if (singlePlotSummary.containsKey("SOILDRAINAGE"))
				soilDrainage = (String)singlePlotSummary.get("SOILDRAINAGE");
			if (singlePlotSummary.containsKey("ALTVALUE"))
				elevationValue = (String)singlePlotSummary.get("ALTVALUE");
			if (singlePlotSummary.containsKey("STATE"))
				state = (String)singlePlotSummary.get("STATE");
			if (singlePlotSummary.containsKey("XCOORD"))
				xCoord = (String)singlePlotSummary.get("XCOORD");
			if (singlePlotSummary.containsKey("YCOORD"))
				yCoord = (String)singlePlotSummary.get("YCOORD");
			if (singlePlotSummary.containsKey("COORDTYPE"))
				coordType = (String)singlePlotSummary.get("COORDTYPE");
			if (singlePlotSummary.containsKey("SOILTYPE"))
				soilType = (String)singlePlotSummary.get("SOILTYPE");
			if (singlePlotSummary.containsKey("PERCENTSOIL"))
				percentSoil = (String)singlePlotSummary.get("PERCENTSOIL");
			if (singlePlotSummary.containsKey("PERCENTSAND"))
				percentSand = (String)singlePlotSummary.get("PERCENTSAND");
			if (singlePlotSummary.containsKey("PERCENTLITTER"))
				percentLitter = (String)singlePlotSummary.get("PERCENTLITTER");
			if (singlePlotSummary.containsKey("PERCENTWOOD"))
				percentWood = (String)singlePlotSummary.get("PERCENTWOOD");
			if (singlePlotSummary.containsKey("SOILDEPTH"))
				soilDepth = (String)singlePlotSummary.get("SOILDEPTH");
			if (singlePlotSummary.containsKey("LEAFTYPE"))
				leafType = (String)singlePlotSummary.get("LEAFTYPE");
			if (singlePlotSummary.containsKey("CURRENTCOMMUNITY"))
				currentCommunity = (String)singlePlotSummary.get("CURRENTCOMMUNITY");
			if (singlePlotSummary.containsKey("PHYSIONOMICCLASS"))
				physionomicClass = (String)singlePlotSummary.get("PHYSIONOMICCLASS");
			if (singlePlotSummary.containsKey("AUTHOROBSCODE"))
				authorObservationCode = (String)singlePlotSummary.get("AUTHOROBSCODE");
		}
		catch (Exception e)
		{
			System.out.println("failed in xmlWriter.mapSummaryElements "
			+" - extracting summary elements from hash table" + e.getMessage());
			e.printStackTrace();
		}

		try
		{
			//project info
			pdm.plotElementMapper("national veg plots database entry", "projectName", "project");
			//site info
			pdm.plotElementMapper(plotId, "plotId", "site");
			pdm.plotElementMapper(authorPlotCode, "authorPlotCode", "site");
			pdm.plotElementMapper(plotShape, "shape", "site");
			pdm.plotElementMapper(slopeGradient, "slopeGradient", "site");
			pdm.plotElementMapper(slopeAspect,"slopeAspect", "site");
			pdm.plotElementMapper(surfGeo,"surfGeo", "site");
			pdm.plotElementMapper(hydrologicRegime,"hydrologicRegime", "site");
			pdm.plotElementMapper(topoPosition,"topoPosition", "site");
			pdm.plotElementMapper(soilDrainage,"soilDrainage", "site");
			pdm.plotElementMapper(elevationValue,"elevationValue", "site");
			pdm.plotElementMapper(state,"state", "site");
			pdm.plotElementMapper(xCoord,"xCoord", "site");
			pdm.plotElementMapper(yCoord,"yCoord", "site");
			pdm.plotElementMapper(coordType,"coordType", "site");


			//observational info
			pdm.plotElementMapper(soilType, "soilType", "observation");
			pdm.plotElementMapper(soilDepth, "soilDepth", "observation");
			pdm.plotElementMapper(authorObservationCode, "authorObsCode", "observation");
			pdm.plotElementMapper(percentSand, "perSand", "observation");
			pdm.plotElementMapper(percentLitter, "perLitter", "observation");
			pdm.plotElementMapper(percentWood, "perWood", "observation");
			pdm.plotElementMapper(leafType, "leafType", "observation");
			pdm.plotElementMapper(physionomicClass, "phisioClass", "observation");

			//strata info
			pdm.plotElementMapper("t1", "stratumName", "observation");
			pdm.plotElementMapper("t1Ht", "stratumHeight", "observation");
			pdm.plotElementMapper("t1Cover", "stratumCover", "observation");

			//community info
			pdm.plotElementMapper(currentCommunity, "communityName", "community");
			pdm.plotElementMapper("tnc code", "CEGLCode", "community");

			//System.out.println(">>>state<<< "+ ((String)singlePlotSummary.get("STATE")) );
			////System.out.println(singlePlotSummary.toString() );
			//cycle thru the single plot and retrieve all the species info -- making the
			// number of iterations = 900 is a hack to keep going and will be fixed

			for (int i=0; i<900; i++)
			{
				String currentAuthorNameId = (String)singlePlotSummary.get("AUTHORNAMEID."+i);
				String currentStratum = (String)singlePlotSummary.get("STRATUMTYPE."+i);
				String currentPercentCover = (String)singlePlotSummary.get("PERCENTCOVER."+i);
				//make sure that no nulls are being passed
				if (currentAuthorNameId != null )
				{
					pdm.plotElementMapper(currentAuthorNameId, "taxonName", "species");
					pdm.plotElementMapper(currentStratum, "stratum", "species");
					pdm.plotElementMapper(currentPercentCover, "cover", "species");
				}
			}


			//now pass the categories to the consolidator -- to make a single plot
			pdm.plotElementConsolidator(pdm.plotProjectParams, pdm.plotSiteParams,
			pdm.plotObservationParams, pdm.plotCommunityParams,	pdm.plotSpeciesParams);
		}
		catch (Exception e)
		{
			System.out.println("failed in xmlWriter.mapSummaryElements "
			+" - passing elements to mapping class" + e.getMessage());
			e.printStackTrace();
		}
		return pdm.comprehensivePlot;
	}



	/**
 	 * method to print the results set from a plant taxonomy query to an
	 * xml file that can be easily transformed into an html or text file
	 *
	 * 	@param taxaResults -- a vector that contains the taxonomy results from the
	 *		query
	 *	@param outfile -- the path and file that the results will be printed to
	 *	@param taxonName -- the name used for the query
	 *	@param taxonNameType -- the type of name used for the query (eg. code, commonname, scientificname)
	 *	@param taxonNameLevel -- the level of the concept (eg, variety, species)
 	 */
	public void writePlantTaxonomySummary(
		Vector taxaResults,
		String outFile,
		String taxonName,
		String taxonNameType,
		String taxonNameLevel
		)
	{
		try
		{
			//set up the output query file called query.xml	using append mode
			PrintWriter out  = new PrintWriter(new FileOutputStream(outFile, false));

			String xmlString =
				getPlantTaxonomySummary(
					taxaResults,
					taxonName,
					taxonNameType,
					taxonNameLevel);

			out.println( xmlString );
		}
		catch (Exception e)
		{
			System.out.println("xmlWriter > Exception " +
			e.getMessage());
			e.printStackTrace();
		}

	}


	public String getPlantTaxonomySummary(
		Vector taxaResults,
		String taxonName,
		String taxonNameType,
		String taxonNameLevel)
	{
		StringBuffer sb = new StringBuffer();
		System.out.println("xmlWriter > number of plant instances: " + taxaResults.size() );

		// REPLACE THE WILDCARDS WITH THE APPROPRIATE TEXT IN THE QUERY TOKENS
		if ( taxonNameType.trim().equals("%") )
			taxonNameType = "ANY";
		if ( taxonNameLevel.trim().equals("%") )
			taxonNameLevel = "ALL";

		// WRITE THE HEADER
		sb.append("<plantTaxa> \n");
		// ADD THE QUERY ELEMENTS TO THE OUTPUT SO THAT THEY CAN BE USED
		// BY THE STYLESHEET
		sb.append("<query> \n");
		sb.append("	<taxonName>"+taxonName+"</taxonName> \n");
		sb.append(" <taxonNameType>"+taxonNameType+"</taxonNameType> \n");
		sb.append("	<taxonNameLevel>"+taxonNameLevel+"</taxonNameLevel> \n");
		sb.append("</query>");

		//iterate thru the results
		for (int i=0;i<taxaResults.size(); i++)
		{
			//the current hashtable used to store the plant instance attributes
			Hashtable currentTaxonHash = (Hashtable)taxaResults.elementAt(i);

			sb.append("<taxon> \n");
			sb.append("	<name> \n");

			this.hashTable2XML(sb, currentTaxonHash);

			sb.append("	</name> \n");
			sb.append("</taxon> \n");
		}
		sb.append("</plantTaxa> \n");

		return sb.toString();
	}

	private void hashTable2XML ( StringBuffer sb, Hashtable hash)
	{
		//System.out.println("------> " + hash.toString() );
		Enumeration anenum = hash.keys();

		while ( anenum.hasMoreElements() )
		{
			String key = (String) anenum.nextElement();
			// append <key>value</key> for each key value
			sb.append("<"+key+">" + hash.get(key) + "</"+key+">\n");
			//System.out.println("------> " + key +" and " + currentTaxonHash.get(key) );
		}
	}



	/**
 	 * Method to print the summary information returned about one or a group of
 	 * vegetation communities.  Currently this method is to take as input a vector
 	 * containing the information and the output file and then print such data as
 	 * the name, nvc level, abiCode, and parentName of the returned community(s)
 	 *
 	 * @param communitySummary - the vector that contains the information
 	 * @param outFile - the output file
 	 */
	public void writeCommunitySummary(Vector communitySummary, String outFile)
	{
		try
		{
			//set up the output query file called query.xml	using append mode
			PrintWriter out  = new PrintWriter(new FileOutputStream(outFile, false));

			String xmlString = getCommunitySummaryXMLString(communitySummary);

			//print to the output file
			out.println( xmlString );
		}
		catch (Exception e)
		{
			System.out.println("xmlWriter > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}


	public String getCommunitySummaryXMLString(Vector communitySummary)
	{

		StringBuffer printString = new StringBuffer();

		String commName="null";
		String dateEntered="null";
		String classLevel="null";
		String commDesc="null";
		String conceptOriginDate="null";
		String commConceptId = "null";
		String partyConceptStatus="null";

		//header
		printString.append("<?xml version=\"1.0\"?> \n");
		//printString.append("<!DOCTYPE vegPlot SYSTEM \"vegCommunity.dtd\">     \n");
		printString.append("<vegCommunity> \n");

		for (int i=0;i<communitySummary.size(); i++)
		{

			//tokenize each line of the vector
			StringTokenizer t = new StringTokenizer(communitySummary.elementAt(i).toString().trim(), "|");
			System.out.println("xmlWriter > number of community elements: " + t.countTokens() );

			commName=t.nextToken().trim();
			dateEntered=t.nextToken();
			classLevel=t.nextToken();
			commDesc =t.nextToken();
			conceptOriginDate =t.nextToken();
			commConceptId =t.nextToken();
			partyConceptStatus =t.nextToken();


			System.out.println("xmlWriter > commName: "+ commName);


			//temporary fix to trim the description to 100 chars
			if (commDesc.length() >100)
			{
				commDesc=commDesc.substring(1, 99);
			}

			printString.append("<community> \n");
			//printString.append("   <commSummaryId>"+commSummaryId+"</commSummaryId> \n");
			printString.append("   <commName>"+commName+"</commName> \n");
			printString.append("   <dateEntered>"+dateEntered+"</dateEntered> \n");
			printString.append("   <classLevel>"+classLevel+"</classLevel> \n");
			printString.append("   <commDesc>"+commDesc+"</commDesc> \n");
			printString.append("   <conceptOriginDate>"+conceptOriginDate+"</conceptOriginDate> \n");
			printString.append("   <commConceptId>"+commConceptId+"</commConceptId> \n");
			printString.append("   <partyConceptStatus>"+partyConceptStatus+"</partyConceptStatus> \n");
			//the parent info
			printString.append("   <parentComm> \n");

			printString.append("   </parentComm> \n");
			printString.append("</community> \n");
		}
		//footer
		printString.append("</vegCommunity>");

		return printString.toString();
	}
}
