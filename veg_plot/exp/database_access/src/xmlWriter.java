package databaseAccess;

/**
 *  '$RCSfile: xmlWriter.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-02-28 15:21:16 $'
 * '$Revision: 1.3 $'
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

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

import databaseAccess.*;


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

	private StringBuffer printString = new StringBuffer(); //this is the print string


	/**
 	 *  This method accepts a hashtable containg plot summary
 	 *  data and prints it to file in an xml format
   *
   */
	public void writePlotSummary(Hashtable cumulativeSummaryResultHash, 
		String outFile)
	{
		try 
		{
	
			//set up the output query file called query.xml	using append mode 
			PrintStream out  = new PrintStream(new FileOutputStream(outFile, false));
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

			//take the returned hash table and pass it to the xml writer class
			//put a try here b/c this class has been sensitive
			try 
			{
				PlotXmlWriter pxw = new PlotXmlWriter();
				pxw.writeMultiplePlot(multiPlotComprehensive, outFile);
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
 	 */
	public void writePlantTaxonomySummary(Vector taxaResults, String outFile)
	{
		try 
		{
			//set up the output query file called query.xml	using append mode 
			PrintStream out  = new PrintStream(new FileOutputStream(outFile, false));
			StringBuffer sb = new StringBuffer();
			System.out.println("xmlWriter > number of plant instances: " + taxaResults.size() ); 
			/**
			 * the attributes that are in this hashtable: 
			 		returnHash.put("plantNameId", ""+plantNameId);
					returnHash.put("plantConceptId", ""+plantConceptId);
			 		returnHash.put("plantName", plantName);
					returnHash.put("status", status);
					returnHash.put("classSystem", classSystem);
			 		returnHash.put("plantLevel", plantLevel);
					returnHash.put("parentName", parentName);
					returnHash.put("acceptedSynonym", acceptedSynonym);
					returnHash.put("startDate", startDate);
					returnHash.put("stopDate", stopDate);
			 */
			//the header stuff
			sb.append("<plantTaxa> \n");
			//iterate thru the results
			for (int i=0;i<taxaResults.size(); i++) 
			{
				//the current hashtable used to store the plant instance attributes
				Hashtable currentTaxonHash = (Hashtable)taxaResults.elementAt(i);
				
				String plantNameId = (String)currentTaxonHash.get("plantNameId");
				String plantConceptId = (String)currentTaxonHash.get("plantConceptId");
			 	String plantName = (String)currentTaxonHash.get("plantName");
				String status = (String)currentTaxonHash.get("status");
			 	String classSystem = (String)currentTaxonHash.get("classSystem");
			 	String plantLevel = (String)currentTaxonHash.get("plantLevel");
			 	String parentName = (String)currentTaxonHash.get("parentName");
			 	String acceptedSynonym = (String)currentTaxonHash.get("acceptedSynonym");
			 	String startDate = (String)currentTaxonHash.get("startDate");
			 	String stopDate = (String)currentTaxonHash.get("stopDate");
				String plantDescription = (String)currentTaxonHash.get("plantDescription");
				
				sb.append("<taxon> \n");
				sb.append("	<name> \n");
				sb.append("		<plantNameId>"+plantNameId+"</plantNameId> \n");
				sb.append("		<plantConceptId>"+plantConceptId+"</plantConceptId> \n");
				sb.append("		<plantName>"+plantName+"</plantName> \n");
				sb.append("		<status>"+status+"</status> \n");
				sb.append("		<classSystem>"+classSystem+"</classSystem> \n");
				sb.append("		<plantLevel>"+plantLevel+"</plantLevel> \n");
				sb.append("		<parentName>"+parentName+"</parentName> \n");
				sb.append("		<acceptedSynonym>"+acceptedSynonym+"</acceptedSynonym> \n");
				sb.append("		<startDate>"+startDate+"</startDate> \n");
				sb.append("		<stopDate>"+stopDate+"</stopDate> \n");
				sb.append("		<plantDescription>"+plantDescription+"</plantDescription> \n");
				sb.append("	</name> \n");
				sb.append("</taxon> \n");

			}
			sb.append("</plantTaxa> \n");
			out.println(sb.toString() );
		}
		catch (Exception e) 
		{
			System.out.println("Exception " + 
			e.getMessage());
			e.printStackTrace();
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
			PrintStream out  = new PrintStream(new FileOutputStream(outFile, false));

			String commName="null";
			String commDesc="null";
			String abiCode="null";
			String parentCommName="null";
			String dateEntered="null";
			String classCode="null";
			String classLevel="null";
			String conceptOriginDate="null";
			String conceptUpdateDate="null";
			String parentAbiCode="null";
			String recognizingParty="null";
			String partyConceptStatus="null";
			String parentCommDescription="null";
			String commSummaryId="null";
			String nullValue = "-999.25";

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
				abiCode=t.nextToken();
				commDesc=t.nextToken();
				parentCommName=t.nextToken();
				dateEntered=t.nextToken();
				classCode=t.nextToken();
				classLevel=t.nextToken();
				conceptOriginDate=t.nextToken();
				conceptUpdateDate=t.nextToken();
				parentAbiCode=t.nextToken();
				recognizingParty=t.nextToken();
				partyConceptStatus=t.nextToken();
				parentCommDescription=t.nextToken();
				commSummaryId=t.nextToken();
			
				System.out.println("xmlWriter > commName: "+ commName);
				System.out.println("xmlWriter > abiCode: "+ abiCode);
				
				
				//temporary fix to trim the description to 100 chars
				if (commDesc.length() >100) 
				{
					commDesc=commDesc.substring(1, 99);
				}

				printString.append("<community> \n");
				printString.append("   <commSummaryId>"+commSummaryId+"</commSummaryId> \n");
				printString.append("   <commName>"+commName+"</commName> \n");
				printString.append("   <commDesc>"+commDesc+"</commDesc> \n");
				printString.append("   <abiCode>"+abiCode+"</abiCode> \n");
				printString.append("   <classCode>"+classCode+"</classCode> \n");
				printString.append("   <classLevel>"+classLevel+"</classLevel> \n");
				printString.append("   <originDate>"+conceptOriginDate+"</originDate> \n");
				printString.append("   <updateDate>"+conceptUpdateDate+"</updateDate> \n");
				printString.append("   <recognizingParty>"+recognizingParty+"</recognizingParty> \n");
				printString.append("   <partyConceptStatus>"+partyConceptStatus+"</partyConceptStatus> \n");

				printString.append("   <parentComm> \n");
				printString.append("     <commName>"+parentCommName+"</commName> \n");
				printString.append("     <abiCode>"+parentAbiCode+"</abiCode> \n");
				printString.append("     <commDesc>"+parentCommDescription+"</commDesc> \n");
				printString.append("   </parentComm> \n");
				printString.append("</community> \n");
			}
			//footer
			printString.append("</vegCommunity>");
			//print to the output file
			out.println( printString.toString() );
		}
		catch (Exception e) 
		{
			 System.out.println("xmlWriter > Exception: " + 
			e.getMessage());
			e.printStackTrace();
		}
	}



 /**
 *  This is an old method that was used to print plot summary information and
 * used as input an array of Strings that were tokenized into variables an then
 * printed out.  The newer method that uses the same class as the data
 * formatting/loading classes is above and uses a hash table that is passed to
 * it instead of the array with strings.
 */

public void writePlotSummary(String plotSummary[], int plotSummaryNum, String outFile)
{
try {
String nullValue="nullValue";
	
//set up the output query file called query.xml	using append mode 
PrintStream out  = new PrintStream(new FileOutputStream(outFile, false)); 	
	
//print the header stuff
out.println("<?xml version=\"1.0\"?>       ");
out.println("<!DOCTYPE vegPlot SYSTEM \"vegPlot.dtd\">     ");
out.println("<vegPlot>");
	
//tokenize the incomming string array into respective elements
//make the token map and put into cvs
for (int i=0;i<=plotSummaryNum; i++) {	
	 StringTokenizer t = new StringTokenizer(plotSummary[i], "|");
	 String plotId=t.nextToken().trim();
	 String authorPlotCode=t.nextToken();
	 String project_id=t.nextToken();
	 String surfGeo=t.nextToken();
	 String plotType=t.nextToken();
	 String origLat=t.nextToken();
	 String origLong=t.nextToken();
	 
	String plotShape=t.nextToken();
	String plotSize=t.nextToken();
	String plotSizeAcc=t.nextToken();
	String altValue=t.nextToken();
	String altPosAcc=t.nextToken();
	String slopeAspect=t.nextToken();
	String slopeGradient=t.nextToken();
	String slopePosition=t.nextToken();
	String hydrologicRegime=t.nextToken();
	String soilDrainage=t.nextToken();
	String state=t.nextToken();
	String currentCommunity=t.nextToken();
	
	 
	 //the remaining elements are species so grab the species names and 
	 //stick into a array
	 String buf=null;
	 String speciesArray[]=new String[100000];
	 int speciesArrayNum=0;
	 while (t.hasMoreTokens()) {
		buf=t.nextToken();
		//System.out.println("writer "+buf);
		speciesArray[speciesArrayNum]=buf;
		speciesArrayNum++;
	 }
	 
//print to elements in the correct order
out.println("<project>");
out.println("	<projectName>"+authorPlotCode+"</projectName>");
out.println("	<projectDescription>TNC PROJECT:</projectDescription>");
out.println("	<projectContributor>");
out.println("		<role>"+nullValue+"</role>");
out.println("		<party>");
out.println("			<salutation>"+nullValue+" </salutation>");
out.println("			<givenName>"+nullValue+"  </givenName>");
out.println("			<surName>"+nullValue+" </surName>");
out.println("			<organizationName>"+nullValue+" </organizationName>");
out.println("			<positionName>"+nullValue+" </positionName>");
out.println("			<hoursOfService>"+nullValue+" </hoursOfService>");
out.println("			<contactInstructions>"+nullValue+" </contactInstructions>");
out.println("			<onlineResource>        ");
out.println("				<linkage>"+nullValue+"</linkage>       ");
out.println("				<protocol>"+nullValue+"</protocol>       ");
out.println("				<name>"+nullValue+"</name>       ");
out.println("				<applicationProfile>"+nullValue+"</applicationProfile>       ");
out.println("				<description>"+nullValue+"</description>       ");
out.println("			</onlineResource>        ");
out.println("			<telephone>        ");
out.println("				<phoneNumber>nullValue</phoneNumber>        ");
out.println("				<phoneType>"+nullValue+"</phoneType>       ");
out.println("			</telephone>        ");
out.println("			<email>        ");
out.println("				<emailAddress>"+nullValue+"</emailAddress>       ");
out.println("			</email>        ");
out.println("			<address>        ");
out.println("				<deliveryPoint>"+nullValue+"</deliveryPoint>       ");
out.println("				<city>"+nullValue+"</city>       ");
out.println("				<administrativeArea>"+nullValue+"</administrativeArea>       ");
out.println("				<postalCode>"+nullValue+"</postalCode>       ");
out.println("				<country>"+nullValue+"</country>       ");
out.println("				<currentFlag>"+nullValue+"</currentFlag>       ");
out.println("			</address>        ");
out.println("		</party>");
out.println("	</projectContributor>        ");
out.println("	<plot>        ");
out.println("		<plotId>"+ plotId +"</plotId>       ");
out.println("		<authorPlotCode>"+ authorPlotCode+"</authorPlotCode>       ");
out.println("		<parentPlot>"+nullValue+"</parentPlot>       ");
out.println("		<plotType>"+plotType+"</plotType>       ");
out.println("		<samplingMethod>"+nullValue+"</samplingMethod>       ");
out.println("		<coverScale>"+nullValue+"</coverScale>       ");
out.println("		<plotOriginLat>"+origLat+" </plotOriginLat>       ");
out.println("		<plotOriginLong>"+origLong+"</plotOriginLong>       ");
out.println("		<plotShape>"+plotShape+" </plotShape>       ");
out.println("		<plotSize>"+plotSize+"</plotSize>       ");
out.println("		<plotSizeAcc>"+plotSizeAcc+"</plotSizeAcc>       ");
out.println("		<altValue>"+altValue+" </altValue>       ");
out.println("		<altPosAcc>"+altPosAcc+"</altPosAcc>       ");
out.println("		<slopeAspect>"+slopeAspect+"</slopeAspect>       ");
out.println("		<slopeGradient>"+slopeGradient+"</slopeGradient>       ");
out.println("		<slopePosition>"+slopePosition+" </slopePosition>       ");
out.println("		<hydrologicRegime>"+hydrologicRegime+" </hydrologicRegime>       ");
out.println("		<soilDrainage>"+soilDrainage+" </soilDrainage>       ");
out.println("		<surfGeo>"+surfGeo+" </surfGeo>       ");
out.println("		<state>"+state+" </state>       ");
out.println("		<currentCommunity>"+currentCommunity+" </currentCommunity>       ");


out.println("		<plotObservation>        ");
out.println("			<previousPlot>"+nullValue+"</previousPlot>       ");
out.println("			<plotStartDate>"+nullValue+" </plotStartDate>       ");
out.println("			<plotStopDate>"+nullValue+"</plotStopDate>       ");
out.println("			<dateAccuracy>"+nullValue+"</dateAccuracy>       ");
out.println("			<effortLevel>"+nullValue+"</effortLevel>       ");

out.println("			<strata>        ");
out.println("				<stratumType>"+nullValue+"</stratumType>       ");	
out.println("				<stratumCover>"+nullValue+"</stratumCover>       ");
out.println("				<stratumHeight>"+nullValue+"</stratumHeight>       ");
out.println("			</strata>        ");




/*this is where to print out the list of different species found in the resultset
* for now just print the unique ones*/
utility m = new utility();
m.getUniqueArray(speciesArray, speciesArrayNum);

//print the unique species types
for (int h=0; h<m.outArrayNum; h++) {
if (h>5) {}
else 
{	
	out.println("                   <taxonObservations>        ");
	out.println("                           <authNameId>"+m.outArray[h]+"</authNameId>       ");
	out.println("                           <originalAuthority>"+nullValue+"</originalAuthority>       ");
	out.println("                           <strataComposition>        ");
	out.println("                                   <strataType>"+nullValue+"</strataType>       ");
	out.println("                                   <percentCover>"+nullValue+"</percentCover>       ");
	out.println("                           </strataComposition>        ");
	out.println("                           <interptretation>        ");
	out.println("                                   <circum_id>"+nullValue+"</circum_id>       ");
	out.println("                                   <role>"+nullValue+"</role>       ");
	out.println("                                   <date>"+nullValue+"</date>       ");
	out.println("                                   <notes>"+nullValue+"</notes>       ");
	out.println("                           </interptretation>        ");
	out.println("                   </taxonObservations>        ");
}
}



out.println("			<communityType>        ");
out.println("				<classAssociation>"+nullValue+"</classAssociation>       ");
out.println("				<classQuality>"+nullValue+"</classQuality>       ");
out.println("				<startDate>"+nullValue+"</startDate>       ");
out.println("				<stopDate>"+nullValue+"</stopDate>       ");
out.println("			</communityType>        ");

out.println("			<citation>        ");
out.println("				<title>"+nullValue+"</title>       ");
out.println("				<altTitle>"+nullValue+"</altTitle>       ");
out.println("				<pubDate>"+nullValue+"</pubDate>       ");
out.println("				<edition>"+nullValue+"</edition>       ");
out.println("				<editionDate>"+nullValue+"</editionDate>       ");
out.println("				<seriesName>"+nullValue+"</seriesName>       ");
out.println("				<issueIdentification>"+nullValue+"</issueIdentification>       ");
out.println("				<otherCredentials>"+nullValue+"</otherCredentials>       ");
out.println("				<page>"+nullValue+"</page>       ");
out.println("				<isbn>"+nullValue+"</isbn>       ");
out.println("				<issn>"+nullValue+"</issn>       ");

out.println("				<citationContributor>        ");
out.println("				</citationContributor>        ");

out.println("			</citation>        ");
out.println("		</plotObservation>        ");

out.println("		<namedPlace>        ");
out.println("			<placeName>"+nullValue+"</placeName>       ");
out.println("			<placeDescription>"+nullValue+"</placeDescription>       ");
out.println("			<gazeteerRef>"+nullValue+"</gazeteerRef>       ");
out.println("		</namedPlace>        ");

out.println("		<stand>        ");
out.println("			<standSize>"+nullValue+"</standSize>       ");
out.println("			<standDescription>"+nullValue+"</standDescription>       ");
out.println("			<standName>"+nullValue+"</standName>       ");
out.println("		</stand>        ");

out.println("		<graphic>        ");
out.println("			<browseName>"+nullValue+"</browseName>       ");
out.println("			<browseDescription>"+nullValue+"</browseDescription>       ");
out.println("			<browseType>"+nullValue+"</browseType>       ");
out.println("			<browseData>"+nullValue+"</browseData>       ");
out.println("		</graphic>        ");

out.println("		<plotContributor>        ");
out.println("			<role>"+nullValue+"</role>       ");
out.println("			<party>"+nullValue+"</party>       ");
out.println("		</plotContributor>        ");
out.println("	</plot>");
out.println("</project>");


}
//print the end of the file
out.println("</vegPlot>");
	
	
}
catch (Exception e) {System.out.println("failed in xmlWriter.writePlotSummary" + 
	e.getMessage());}

}	
	
}
