package org.vegbank.plots.datasource;

/**
 * '$RCSfile: NativeXmlPlugin.java,v $'
 *
 * Purpose: 
 *
 * '$Author: farrell $'
 * '$Date: 2003-08-21 21:16:45 $'
 * '$Revision: 1.6 $'
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


import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import org.vegbank.xmlresource.XMLparse;

/**
 * This is a plugin to implement the DataSourceInterface class, allowing 
 * Access to the data stored in the native VegBank XML structure.
 * 
 *	'$Author: farrell $'
 *	'$Date: 2003-08-21 21:16:45 $'
 *	'$Revision: 1.6 $'
 *
 */
public class NativeXmlPlugin implements PlotDataSourceInterface
//public class NativeXmlPlugin
{
	//root node of the in-memory DOM structure
	private Node root;
	//the current value of the root node
	private String rootValue = null;
	// Document node of the in-memory DOM structure
  private Document doc;
  // XML file name in string form
  private String fileName;
  // Print writer (output)
  private PrintWriter out;
	// access to the parser utility
	private XMLparse parse = new XMLparse();
	//the number of plots in this project file
	int numberOfPlots = 0;
	// this is the xml data source -- the file that contains the plots
	private String plotArchiveFile = "input.xml";
	//the class that creates the xml cache -- a dir to store the atomic plots file
	NativeXmlCache cache;
	//the utility class for seraching the DOM
	NativeXmlPluginUtil util = new NativeXmlPluginUtil();
	
	//THESE VARIABLES ARE FOR TESTING -- REMOVE 
	private String stringBuf = null;
	private Vector vectorBuf = new Vector();
	private boolean booleanBuf = false;
	
	/**
	 * empty constructor
	 */
	 public NativeXmlPlugin() throws FileNotFoundException
	 {
		System.out.println("NativeXmlPlugin > instantiating class with no plotName" );		  
	 }
	 
	/**
	 * this is the method that is called to check and see that the XML document 
	 * associated with this plotname exists in the cache
	 * @param plotName -- the name of the plot
	 * @param plotArchiveFile -- the name of the xml file to use
	 */ 
//	public void init(String plotName, String plotArchiveFile )
//	{
//		this.plotArchiveFile = plotArchiveFile;
//		init(plotName);
//	}

	/**
	 * this is the method that is called to check and see that the XML document 
	 * associated with this plotname exists in the cache
	 * @param plotName -- the name of the plot
	 */ 
	public void init(String plotName)	
	{
		if (  rootValue == null )
		{
			try
			{
				System.out.println("NativeXmlPlugin > init: " + plotName  );
				//parse  = new XMLparse();
				//first make sure that the file does exist in the cache
				//the cache dir
				cache = new NativeXmlCache();
				String cacheDir = cache.getCacheDir();
				this.fileName = cacheDir+"/"+plotName+".xml";
				if ( cache.plotCacheDirExists() == false || cache.plotExistsInCache(plotName) == false )
				{
					cache = new NativeXmlCache(plotArchiveFile);
					cache.createXMLCache();
				}
				DOMParser parser = new DOMParser();
				File plotFile = new File(fileName);
				InputSource in;
				FileInputStream fs;
				fs = new FileInputStream(fileName);
				in = new InputSource(fs);
				parser.parse(in);
				fs.close();
				
				doc = parser.getDocument();
				root = doc.getDocumentElement();
				rootValue = root.toString();
				System.out.println("NativeXmlPlugin > root element: " + root.toString() );
			} 
			catch(Exception e1) 
			{
				System.out.println("NativeXmlPlugin > Exception: "+e1.getMessage() );
				e1.printStackTrace();
			}
		}
	}
	
	
	//START
	
	//this method must be called before any of the either methods to build
	//the DOM document
	public void getPlotData(String plotName)
	{
		try
		{
			System.out.println("NativeXmlPluging > re-instantiating class with plot: " + plotName);
			NativeXmlPlugin nxp = new NativeXmlPlugin();
			nxp.init(plotName);
			
		}
		catch(Exception e1) 
		{
			System.out.println("NativeXmlPlugin > Exception: "+e1.getMessage() );
			e1.printStackTrace();
    }
	}
	//returns all the names of the plots stored in the archive file
	public Vector getPlotNames()
	{
		Vector v = parse.get(plotArchiveFile, "authorPlotCode");
		return(v);
	}
		
	/**
	 **
	 ** THE PROJECT - RELATED DATA
	 **
	 **/
	public String getProjectName(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "projectName") );
	}
	public Vector getProjectContributors(String plotName)
	{
		String path = "/project/projectContributor/wholeName";
		Vector pathContent = parse.getValuesForPath(doc, path);
		return(pathContent);
	}
	public String getProjectContributorSalutation(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorSurName(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorCity(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		return(stringBuf);
	}
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		return(stringBuf);
	}
  
	public String getProjectContributorCountry(String contributorWholeName)
	{
		String s = null;
		String path = "/project/projectContributor/address/country";
		Vector pathContent = parse.getValuesForPath(doc, path);
		if ( pathContent.size() == 1 )
		{
			s = (String)pathContent.elementAt(0);
		}
		return( s );
	}
  
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		return(stringBuf);
	}
  
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
		return(stringBuf);
	}
  
	public String getProjectStartDate(String plotName)
	{
		return( this.getPlotAttribute(plotName, "startDate") );
	}
  
	public String getProjectStopDate(String plotName)
	{
		return( this.getPlotAttribute(plotName, "stopDate") );
	}
  
	public String getProjectDescription(String plotName)
	{
		return( this.getPlotAttribute(plotName, "projectDescription") );
	}
		
	/**
	 **
	 ** THE PLOT - RELATED DATA
	 **
	 **/
	 
	/**
	* This method takes as input the plot attribute name of an attribute
	* that may only have a single occurence in a plot (eg., latitude) and 
	* returns the element value.  The method was written to minimize the amount 
	* of code that had to be written for each of the methods that are used to 
	* implement the interface
	* @param element -- the attribute name that is to be returned from the 
	* 	xml document
	* @param plotName -- the name of the plot for which the attribute should be 
	*		returned
	* @return elementValue -- the value of the query attribute
	*/
	private String getPlotAttribute(String plotName, String elementName)
	{
		String elementValue = null;
		elementValue = parse.getNodeValue(doc, elementName);
		return(elementValue);
	}
		
	public String getPlotCode(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorPlotCode") );
	}
	public String getAccessionValue(String plotName)
	{
		return( this.getPlotAttribute(plotName, "plotAccessionNumber") );
	}
	public String getObservationAccessionNumber(String plotName)
	{
		return( this.getPlotAttribute(plotName, "plotAccessionNumber") );
	}
	
	public Vector getObservationContributors( String plotName )
	{
		return vectorBuf;
	}
	
	public String getObservationContributorSalutation(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorGivenName(String contributorWholeName) 
	{
		return null;
	}
	
	public String getObservationContributorMiddleName(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorSurName(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorOrganizationName(String contributorWholeName){
		return null;
	}
	
	public String getObservationContributorContactInstructions(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCellPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorFaxPhoneNumber(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorOrgPosition(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorEmailAddress(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorDeliveryPoint(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCity(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorAdministrativeArea(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorPostalCode(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCountry(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorCurrentFlag(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorAddressStartDate(String contributorWholeName)
	{
		return null;
	}
	
	public String getObservationContributorRole(String contributorWholeName)
	{
		return null;
	}	
	
	public Vector getPlaceNames(String plotName)
	{
		return(vectorBuf);
	}
	public String getPlaceDescription(String placeName)
	{
		return(stringBuf);
	}
	public String getPlaceCode(String placeName)
	{
		return(stringBuf);
	}
	public String getPlaceSystem(String placeName)
	{
		return(stringBuf);
	}
	public String getPlaceOwner(String placeName)
	{
		return(stringBuf);
	}
	public String getXCoord(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorE") );
	}
	public String getYCoord(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorN") );
	}
	public String getLatitude(String plotName)
	{
		return( this.getPlotAttribute(plotName, "latitude") );
	}
	
	public String getLongitude(String plotName)
	{
		return( this.getPlotAttribute(plotName, "longitude") );
	}
	
	public String getUTMZone(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorZone") );
	}
	
	public String getDatumType(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorDatum") );
	}
	
	public String getPlotShape(String plotName)
	{
		return( this.getPlotAttribute(plotName, "shape") );
	}
	
	public String getPlotArea(String plotName)	
	{
		return( this.getPlotAttribute(plotName, "area") );
	}
	
	public String getState(String plotName)
	{
		return( this.getPlotAttribute(plotName, "state") );
	}
	
	public String getTopoPosition(String plotName)
	{
		return( this.getPlotAttribute(plotName, "topoPosition") );
	}
	
	//must me a numeric value	
	public String getSlopeAspect(String plotName)
	{
		String s = this.getPlotAttribute(plotName, "slopeAspect");
		if ( (s != null) && (! s.trim().equals("null")) )
		{
			return( s.trim() );
		}
		else
		{
			return("");
		}
	}
	
	//must be numeric
	public String getSlopeGradient(String plotName)
	{
		String s = this.getPlotAttribute(plotName, "slopeGradient");
		if ( (s != null) && (! s.trim().equals("null")) )
		{
			return( s.trim() );
		}
		else
		{
			return("");
		}
	}
	
	public String getAzimuth(String plotName) 
	{
		return this.getPlotAttribute(plotName, "azimuth");
	}	
 
	public String getDSGPoly(String plotName)
	{
		return this.getPlotAttribute(plotName, "dsgPoly");

	}
	
	public String getLocationNarrative(String plotName)
	{
		return this.getPlotAttribute(plotName, "locationNarrative");
	}
	
	public String getLayoutNarrative( String plotName )
	{
		// NOTE: Thris is misspelled in the XML
		return this.getPlotAttribute(plotName, "layoutNarative");
	}
	
	public String getSurfGeo(String plotName)
	{
		return( this.getPlotAttribute(plotName, "geology") );
	}

	public String getCountry(String plotName)
	{
		return( this.getPlotAttribute(plotName, "country") );
	}
	
	public String getStandSize(String plotName)
	{
		return( this.getPlotAttribute(plotName, "standSize") );
	}
	
	public String getAuthorLocation(String plotName)
	{
		return( this.getPlotAttribute(plotName, "authorLocation") );
	}
	
	public String getLandForm(String plotName)
	{
		return( this.getPlotAttribute(plotName, "landform") );
	}
	
	// this has cannot be null and should be numeric
	public String getElevation(String plotName)
	{
		String s = "";
		s =  this.getPlotAttribute(plotName, "elevation");
		if ( s != null )
		{
			return( s.trim() );
		}
		else
		{
			return( "");
		}
	}
	
	// this has cannot be null and should be numeric
	public String getElevationAccuracy(String plotName)
	{
		String s = "";
		s =  this.getPlotAttribute(plotName, "elevationAccuracy");
		if ( s != null )
		{
		  return( s.trim() );
		}
		else
		{
			return( "");
		}
	}

	
	//this method must return a not-null value
	public String	getConfidentialityReason(String plotName)
	{
		String s = this.getPlotAttribute(plotName, "confidentialityReason");
		if ( s != null)
		{
			return(s);
		}
		else
		{
			return("");
		}
	}
	
	//this method must return a not-null value
	public String getConfidentialityStatus(String plotName)
	{
		String s = this.getPlotAttribute(plotName, "confidentialityStatus");
		if ( s != null)
		{
			return(s);
		}
		else
		{
			return("");
		}
	}
	
	public boolean isPlotPermanent(String plotName)
	{
		return(booleanBuf);
	}
	
	public String getSoilTaxon(String plotName)
	{
		return(stringBuf);
	}
	
	public String getSoilTaxonSource(String plotName)
	{
		return(stringBuf);
	}
	/**
	 **
	 ** THE COMMUNITY - RELATED DATA
	 **
	 **/
	public String getCommunityName(String plotName)
	{
		return( this.getPlotAttribute(plotName, "className") );
	}
	
  public String getCommunityCode(String plotName)
	{
		return( this.getPlotAttribute(plotName, "classCode") );
	}
	
  public String getCommunityLevel(String plotName)
	{
    return(stringBuf);
		//return( this.getPlotAttribute(plotName, "") );
	}
	
  public String getCommunityFramework(String plotName)
	{
    return(stringBuf);
		//return( this.getPlotAttribute(plotName, "") );
	}
  
  public String getClassNotes(String plotName)
	{
		return( this.getPlotAttribute(plotName, "classNotes") );
	}
  
  public String getHydrologicRegime(String plotName)
	{
		return( this.getPlotAttribute(plotName, "hydrologicRegime") );
	}
	public String getSoilDrainage(String plotName) 
	{
		return( this.getPlotAttribute(plotName, "soilDrainage") );
	}
	public String getAuthorObsCode(String plotName) 
	{
		return( this.getPlotAttribute(plotName, "authorObsCode") );
	}
	public String getObsStartDate(String plotName)
	{
		return( this.getPlotAttribute(plotName, "obsStartDate") );
	}
	public String getObsStopDate(String plotName)
	{
		return( this.getPlotAttribute(plotName, "obsEndDate") );
	}
	public String getSoilDepth(String plotName)
	{
    // FIXME: This had a null in the test data so I'm ignoring it 
    // just to get things working  ---  should be dealt with by the 
    // validation layer ... coming soon (TM)
		//return( this.getPlotAttribute(plotName, "soilDepth") );
   return ""; 
	}
	
  /**
	*/
	public String getObsDateAccuracy(String plotName)
	{
		return( this.getPlotAttribute(plotName, "dateAccuracy") );
	}
	 
	/**
	*/
	public Hashtable getCoverMethod(String plotName)
	{
		Hashtable  s = new Hashtable();
		return(s);
	}
	
	/**
	 */
	public Hashtable getStratumMethod(String plotName)
	{
		Hashtable s = new Hashtable();
		return(s);
	}
	
	/**
	 */
	public String getStemSizeLimit(String plotName)
	{
		return( this.getPlotAttribute(plotName, "stemSizeLimit") );
	}

	/**
	 */
	public String getMethodNarrative(String plotName)
	{
		return( this.getPlotAttribute(plotName, "methodNarrative") );
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
		return( this.getPlotAttribute(plotName, "taxonObservationArea") );
	}
	
	/**
	 */
	public String getCoverDispersion(String plotName )
	{
		return( this.getPlotAttribute(plotName, "coverDispersion") );
	}
	
  /**
  */
	public boolean getAutoTaxonCover(String plotName)
	{
		String value = this.getPlotAttribute(plotName, "autoTaxonCover");
    return convertStringToBoolean(value);
	}
	
	/**
	 */
	public String getStemObservationArea(String plotName)
	{
		return( this.getPlotAttribute(plotName, "stemObservationArea") );
	}
	
	/**
	 */
	public String getStemSampleMethod(String plotName)
	{
		return( this.getPlotAttribute(plotName, "stemSampleMethod") );
	}
	
	/**
	 */
	public String getOriginalData(String plotName )
	{
		return( this.getPlotAttribute(plotName, "originalData") );
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
		return( this.getPlotAttribute(plotName, "effortLevel") );
	}
	
	//START
  public String getPlotValidationLevel(String plotName)
  {
		return( this.getPlotAttribute(plotName, "plotValidationLevel") );
  }

  public String  getFloristicQuality(String plotName)
  {
		return( this.getPlotAttribute(plotName, "floristicQuality") );
  }

  public String  getBryophyteQuality(String plotName)
  {
		return( this.getPlotAttribute(plotName, "bryophyteQuality") );
  }

  public String  getLichenQuality(String plotName)
  {
	  String elementValue = null;
	  try
	  {
		  elementValue = parse.getNodeValue(doc, "lichenQuality");
	  }
	  catch(Exception e )
	  {
		  System.out.println("NativeXmlPlugin > Exception parsing: lichenQuality");
	  }
	  return(elementValue);
  }

  public String  getObservationNarrative(String plotName)
  {
	  String elementValue = null;
	  try
	  {
		  elementValue = parse.getNodeValue(doc, "observationNarrative");
	  }
	  catch(Exception e )
	  {
		  System.out.println("NativeXmlPlugin > Exception parsing: observationNarrative");
	  }
	  return(elementValue);
  }

  public String  getHomogeneity(String plotName)
  {
	  String elementValue = null;
	  try
	  {
		  elementValue = parse.getNodeValue(doc, "homogeneity");
	  }
	  catch(Exception e )
	  {
		  System.out.println("NativeXmlPlugin > Exception parsing: homogeneity");
	  }
	  return(elementValue);
  }

  public String  getRepresentativeness(String plotName)
  {
	  String elementValue = null;
	  try
	  {
		  elementValue = parse.getNodeValue(doc, "representativeness");
	  }
	  catch(Exception e )
	  {
		  System.out.println("NativeXmlPlugin > Exception parsing: representativeness");
	  }
	  return(elementValue);
  }

	public String  getBasalArea(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "basalArea");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: basalArea");
		}
		return(elementValue);
	}
	
	public String  getSoilMoistureRegime(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "soilMoistureRegime");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: soilMoistureRegime");
		}
		return(elementValue);
	}
	
	public String  getWaterSalinity(String plotName)
	{
		String elementValue = null;
		elementValue = parse.getNodeValue(doc, "waterSalinity");
		return(elementValue);
	}
	
	public String  getShoreDistance(String plotName)
	{
		return( this.getPlotAttribute(plotName, "shoreDistance") );
	}
	
	public String  getOrganicDepth(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "organicDepth");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: organicDepth");
		}
		return(elementValue);
	}
	
	public String  getPercentBedRock(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentBedrock");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentBedRock");
		}
		return(elementValue);
	}
	
	public String  getPercentRockGravel(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentRockGravel");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentRockGravel");
		}
		return(elementValue);
	}
	
	public String  getPercentWood(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentWood");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentWood");
		}
		return(elementValue);
	}
	public String  getPercentLitter(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentLitter");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentLitter");
		}
		return(elementValue);
	}
	
	public String  getPercentBareSoil(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentBareSoil");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentBareSoil");
		}
		return(elementValue);
	}
	
	public String  getPercentWater(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentWater");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentWater");
		}
		return(elementValue);
	}
	
	public String  getPercentOther(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "percentOther");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: percentOther");
		}
		return(elementValue);
	}
	
	public String  getNameOther(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "nameOther");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: nameOther");
		}
		return(elementValue);
	}
	
	public String  getStandMaturity(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "standMaturity");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: standMaturity");
		}
		return(elementValue);
	}
	
	
	public String  getLandscapeNarrative(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "landscapeNarrative");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: landscapeNarrative");
		}
		return(elementValue);
	}
	
	public String  getPhenologicalAspect(String plotName)
	{
		String elementValue = null;
		try
		{
			elementValue = parse.getNodeValue(doc, "phenologicalAspect");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: phenologicalAspect");
		}
		return(elementValue);
	}
	
	public String  getWaterDepth(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "waterDepth");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: waterDepth");
		}
		return(elementValue);
	}
	
	public String  getFieldHt(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "fieldHt");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: fieldHt");
		}
		return(elementValue);
	}
	
	public String  getSubmergedHt(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "submergedHt");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: submergedHt");
		}
		return(elementValue);
	}
	
	public String  getTreeCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "treeCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: treeCover");
		}
		return(elementValue);
	}
	
	public String  getShrubCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "shrubCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: shrubCover");
		}
		return(elementValue);
	}
	
	public String  getFieldCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "fieldCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: fieldCover");
		}
		return(elementValue);
	}
	
	public String  getNonvascularCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "nonvascularCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: nonvascularCover");
		}
		return(elementValue);
	}
	
	
	public String  getSuccessionalStatus(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "successionalStatus");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: successionalStatus");
		}
		return(elementValue);
	}
	
	public String  getTreeHt(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "treeHt");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: treeHt");
		}
		return(elementValue);
	}
	
	public String  getShrubHt(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "shrubHt");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: shrubHt");
		}
		return(elementValue);
	}
	
	public String  getNonvascularHt(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "nonvascularHt");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: nonvascularHt");
		}
		return(elementValue);
	}
	
	public String  getFloatingCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "floatingCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: floatingCover");
		}
		return(elementValue);
	}
	
	public String  getSubmergedCover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "submergedCover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: submergedCover");
		}
		return(elementValue);
	}
	
	public String  getDominantStratum(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "dominantStratum");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: dominantStratum");
		}
		return(elementValue);
	}
	
	public String  getGrowthform1Type(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform1type");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform1type");
		}
		return(elementValue);
	}
	
	public String  getGrowthform2Type(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform2type");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform2type");
		}
		return(elementValue);
	}
	
	public String  getGrowthform3Type(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform3type");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform3type");
		}
		return(elementValue);
	}
	
	public String  getGrowthform1Cover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform1cover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform1cover");
		}
		return(elementValue);
	}
	
	public String  getGrowthform2Cover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform2cover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform2cover");
		}
		return(elementValue);
	}
	
	public String  getGrowthform3Cover(String plotName)
	{
		String elementValue = null;
		
		try
		{
			elementValue = parse.getNodeValue(doc, "growthform3cover");
		}
		catch(Exception e )
		{
			System.out.println("NativeXmlPlugin > Exception parsing: growthform3cover");
		}
		return(elementValue);
	}
	
	public boolean  getNotesPublic(String plotName)
	{
	  String elementValue = this.getPlotAttribute(plotName, "notesPublic");
		System.out.println("NativeXmlPlugin >  notesPublic: " + elementValue);
		return convertStringToBoolean(elementValue);
	}
	
	public boolean  getNotesMgt(String plotName)
	{
		String elementValue = this.getPlotAttribute(plotName, "notesMgt");
		System.out.println("NativeXmlPlugin >  notesMgt: " + elementValue);
    return convertStringToBoolean(elementValue);
	}
	
	public boolean  getRevisions(String plotName)
	{
		String elementValue = this.getPlotAttribute(plotName, "revisions");
		System.out.println("NativeXmlPlugin >  revisions: " + elementValue);
    return convertStringToBoolean(elementValue);
	}


	/**
	 **
	 ** THE TAXONOMY - RELATED DATA
	 **
	 **/
	public Vector getPlantTaxaNames( String plotName)
	{
		Vector returnVec = new Vector();
		returnVec = parse.get(doc, "authorNameId");
		Vector v = new Vector();
		//get and return the unique values
		for (int i =0; i < returnVec.size(); i++)
		{
			String val = (String)returnVec.elementAt(i);
			if (! v.contains(val) )
			v.addElement(val);
		}
		return(v);
	}
	
	public Vector getTaxaStrataExistence(String plantName, String plotName)
	{
    // TODO: Use xpath to get info!!
		// first figure out the number corresponding to the specific 
		// taxonObservation block containing this plantName
		Vector nameVec = new Vector();
		nameVec = parse.get(doc, "authorNameId");
		//System.out.println("NativeXmlPlugin > nameVec size: " + nameVec.size() );
		int target = nameVec.indexOf(plantName);
		//System.out.println("NativeXmlPlugin > target: " + target );
		
		 // this is the taxonObs node of interest
		Node tObs = parse.get(doc, "taxonObservation", target);
		
		// blank vector 
		Vector strataVec = new Vector();
		NodeList children = tObs.getChildNodes();
    if (children != null)
    {
			int len = children.getLength();
      for (int i = 0; i < len; i++)
      {

				Node cn = children.item(i);
				String nodeName = cn.getNodeName();
				//System.out.println("NativeXMLPlugin > node name: " + nodeName+" " + cn.getNodeType() );
				
				if ( nodeName.equals("stratumComposition") )
				{
					NodeList children2 = cn.getChildNodes();
					if (children2 != null)
					{
						int len2 = children2.getLength();
						for (int ii = 0; ii < len2; ii++)
						{				
							Node n = children2.item(ii);
							if ( n.getNodeName().equals("stratumName") )
							{
								// Assumming that firstChild is the text node
								strataVec.addElement( n.getFirstChild().getNodeValue() );
							}
						}
					}
				}
      }
    }
		return(strataVec);
	}
	
	
	
	public String getCummulativeStrataCover( String plantName, String plotCode)
	{
		String s = null;
		// first figure out the number corresponding to the specific 
		// taxonObservation block containing this plantName
		Vector nameVec = new Vector();
		nameVec = parse.get(doc, "authorNameId");
		//System.out.println("NativeXmlPlugin > nameVec size: " + nameVec.size() );
		int target = nameVec.indexOf(plantName);
		//System.out.println("NativeXmlPlugin > target: " + target );
		
		 // this is the taxonObs node of interest
		Node tObs = parse.get(doc, "taxonObservation", target);
		// blank vector 
		Vector strataVec = new Vector();
		NodeList children = tObs.getChildNodes();
    if (children != null)
    {
			int len = children.getLength();
      for (int i = 0; i < len; i++)
      {
				//System.out.println( children.item(i) );
				//Node cn = children.item(i).getFirstChild();
				Node cn = children.item(i);
				String nodeName = cn.getNodeName();
				//System.out.println("node name: " + nodeName+" " + cn.getNodeType() );
				
				if ( nodeName.equals("taxonCover") )
				{
					if ((cn != null) && (cn.getNodeType() == 1))
					{
						s = (cn.getFirstChild().getNodeValue());
						//System.out.println("value: " + s );
					}
				}
      }
    }
		return(s);
	}
	
  public String getTaxaStrataCover(String plantName, String plotCode, String stratumName)
	{
		String taxonStrataCover = null;
		String path2taxonStrataCover = 
      "/project/plot/observation/taxonObservation[./authorNameId='"+plantName+"']"
      +"/stratumComposition[./stratumName = '"+stratumName+"']/taxonStratumCover";
    Vector taxonStrataCovers = parse.getValuesForPath(doc, path2taxonStrataCover);
    return (String) taxonStrataCovers.firstElement(); 
	}
	
  public String getPlantTaxonCode(String plantName)
	{
		String taxonCode = null;
		String path2taxonCode = 
      "/project/plot/observation/taxonObservation[./authorNameId='"+plantName+"']"
      +"/authorCodeId";
    Vector taxonCodes = parse.getValuesForPath(doc, path2taxonCode);
    // TODO: I only expect one value returned should be assertive here
    return (String) taxonCodes.firstElement(); 
  }
  
  public String getPlantTaxonCover(String plantName, String plotName)
	{
		String taxonCover = null;
		String path2taxonCover = 
      "/project/plot/observation/taxonObservation[./authorNameId='"+plantName+"']"
      +"/taxonCover";
    Vector taxonCovers = parse.getValuesForPath(doc, path2taxonCover);
    // TODO: I only expect one value returned should be assertive here
    return (String) taxonCovers.firstElement(); 
	}
  
	public Vector getUniqueStrataNames(String plotName)
	{
		 //FIXME: The doc value needs to be made availible
		String path = "/project/plot/observation/stratum/stratumName";
		Vector pathContent = parse.getValuesForPath(doc, path);
		return(pathContent);
	}
	
	public String getStrataCover(String plotName, String stratumType)
	{
		
		String cover = null;
		Hashtable h = util.getStratum(doc, stratumType);
		cover = (String)h.get("stratumCover");
		return(cover);
	}
	
	public String getStrataBase(String plotName, String strataName)
	{
		
		String cover = null;
		Hashtable h = util.getStratum(doc, strataName);
		cover = (String)h.get("stratumBase");
		return(cover);
	}
	public String getStrataHeight(String plotName, String strataName)
	{
		
		String cover = null;
		Hashtable h = util.getStratum(doc, strataName);
		cover = (String)h.get("stratumHeight");
		return(cover);
	}
	//END
	
	/**
	* main method for testing
	*/
	public static void main(String[] args)
	{
		try
		{
			String plotName = "YOSE.98K116";
			NativeXmlPlugin nativesrc = new NativeXmlPlugin();
			nativesrc.init(plotName);
			System.out.println("slope: " + nativesrc.getSlopeAspect(plotName) );
			System.out.println("plots: " + nativesrc.getPlotNames() );
		}
		catch (Exception e)
		{
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * this is a utility class that can be used by the plugin
	 */
	public class NativeXmlPluginUtil
	{
	
	private XMLparse parse = new XMLparse();
	//the number of plots in this project file

	
	public void putPlotSite(String authorPlotCode)
	{
		System.out.println("adding plotSite to DOM structure");
		parse.addNewElement(doc, "project", 0, "plot", "plot2");
		//now print to file 
		parse.save(root, "out.xml");
	}
	
	//method that returns a vector containing the names of the various starta
	//recognized in the plot
	public Vector getStrataTypes(Document doc)
	{
		Vector returnVec = new Vector();
		try
		{
			returnVec = parse.get(doc, "stratumName");
		}
		catch(Exception e) 
		{
			System.out.println("NativeXmlPlugin > Exception: " +e.getMessage() );
			e.printStackTrace();
		}
		return(returnVec);
	}

   /*
		* stratumCover -- the percent cover of vegetation in that stratum <br>
		* stratumBase -- the height of the base of the stratum in meters <br>
		* strataumHeight -- the height of the top of stratum in meters <br> <br>
		* @param doc -- the DOM document corresponding to a plot 
		* @param stratumType -- a string representation of the stratum type
		* 
		*/
    public Hashtable getStratum(Document doc, String stratumType)
		{
			String cover = null;
			String type = null;
			String base = null;
			String height = null;
			int index = -999;
			Hashtable returnHash = new Hashtable();
			String path = "/project/plot/observation/stratum/stratumName";
			Vector strataTypes = parse.getValuesForPath(doc, path);
			try 
			{
				//System.out.println("NativeXmlPlugin > trashy: " + doc.getLength() );
				for (int i =0; i < strataTypes.size(); i++)
				{
					if ( strataTypes.elementAt(i).toString().trim().equals(stratumType) )
					{
						//assign the index
						index = i;
					}
				}
				//if the index is unchanged, still -999, then print an error
				if (index == -999 )
				{
					System.out.println("strataType: "+stratumType+" not found ERROR");
				}
				else
				{
					System.out.println("name: " + type +" cover: "+ cover+" base: "+ base+" height: "+height);
					type = parse.getNodeValue(doc, "stratumName", index);
					cover = parse.getNodeValue(doc, "stratumCover", index);
					base = parse.getNodeValue(doc, "stratumBase", index);
					height = parse.getNodeValue(doc, "stratumHeight", index);
					System.out.println("name: " + type +" cover: "+ cover+" base: "+ base+" height: "+height);
					
					//stick these elements into the hashtable to be returned
					returnHash.put("stratumType", type);
					if ( cover != null)
					{
						returnHash.put("stratumCover", cover);
					}
					if ( height != null)
					{
						returnHash.put("stratumHeight", height);
					}
					if ( height != null)
					{
						returnHash.put("stratumBase", base);
					}
				}
			}
			catch(Exception e) 
			{
				System.out.println("NativeXmlPlugin > Exception: " +e.getMessage() );

				e.printStackTrace();
			}
			return(returnHash);
    }
  }


  private boolean convertStringToBoolean ( String s ) 
  {
    // FIXME: This is logically flawed .... should be able to return nothing
    // rather than forcing a boolean on us. Nothing should be returned when 
    // nothing in datasource or a value we don't have a match for.
    // Must be a library that does this?

    boolean retVal;
    // Need to turn this string into a boolean.
    if (s == null) 
    {
      retVal = false;
    }
    else if ( s.equalsIgnoreCase("false") )
    {
      retVal = false;
    }
    else if ( s.equalsIgnoreCase("true") )
    {
      retVal = true;
    }
    else 
    {
      retVal = false;
    }
    return retVal;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityExpertSystem(java.lang.String)
	 */
	public String getCommunityExpertSystem(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityInspection(java.lang.String)
	 */
	public String getCommunityInspection(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityMultiVariateAnalysis(java.lang.String)
	 */
	public String getCommunityMultiVariateAnalysis(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityPublication(java.lang.String)
	 */
	public String getCommunityPublication(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityStartDate(java.lang.String)
	 */
	public String getCommunityStartDate(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityStopDate(java.lang.String)
	 */
	public String getCommunityStopDate(String plotName)
	{
		return null;
	}

	/**
	 * @see PlotDataSourceInterface#getCommunityTableAnalysis(java.lang.String)
	 */
	public String getCommunityTableAnalysis(String plotName)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getRockType(java.lang.String)
	 */
	public String getRockType(String plotName)
	{
		return this.getPlotAttribute(plotName, "rockType") ;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSurficialDeposits(java.lang.String)
	 */
	public String getSurficialDeposits(String plotName)
	{
		return this.getPlotAttribute(plotName, "surficialDeposits") ;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStratumMethodName(java.lang.String)
	 */
	public String getStratumMethodName(String plotName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCoverMethodName(java.lang.String)
	 */
	public String getCoverMethodName(String plotName)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
