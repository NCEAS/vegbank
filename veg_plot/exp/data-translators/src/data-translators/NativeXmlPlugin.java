import org.apache.xerces.parsers.DOMParser;
import org.apache.xalan.xpath.xml.FormatterToXML;
import org.apache.xalan.xpath.xml.TreeWalker;
import org.apache.xalan.*;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.*;


import  xmlresource.datatype.*;
import  xmlresource.utils.XMLparse;

/**
 * This is a plugin to implement the DataSourceInterface class, allowing 
 * Access to the data stored in the native VegBank XML structure.
 * 
 * 	'$Author: harris $'
 *	'$Date: 2002-08-30 19:12:02 $'
 *	'$Revision: 1.7 $'
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
	String plotArchiveFile = "input.xml";
	//the class that creates the xml cache -- a dir to store the atomic plots file
	NativeXmlCache cache;
	//the utility class for seraching the DOM
	NativeXmlPluginUtil util = new NativeXmlPluginUtil();
	
	//THESE VARIABLES ARE FOR TESTING -- REMOVE 
	private String stringBuf = null;
	private Vector vectorBuf = new Vector();
	private boolean booleanBuf = false;
	
  /**
   * constructor method -- pass in the name of the plot 
   * @param plotName -- the name of the plot
   */
  public NativeXmlPlugin(String plotName) throws FileNotFoundException
  {
		System.out.println("NativeXmlPlugin > instantiating class: " + plotName  );
		this.init(plotName);
	}
	/**
	 * second empty constructor
	 */
	 public NativeXmlPlugin() throws FileNotFoundException
	 {
		  
	 }
	 
	/**
	 * this is the method that is called to check and see that the XML document 
	 * associated with this plotname exists in the cache
	 * @param plotName -- the name of the plot
	 */ 
	public void init(String plotName )
	{
		try
		{
		if (  rootValue == null )
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
			try
			{
				parser.parse(in);
				fs.close();
			} 
			catch(Exception e1) 
			{
				System.out.println("Exception: "+e1.getMessage() );
				e1.printStackTrace();
			}
			doc = parser.getDocument();
			root = doc.getDocumentElement();
			rootValue = root.toString();
			System.out.println("NativeXmlPlugin > root element: " + root.toString() );
		}
		}
		catch(Exception e1) 
			{
				System.out.println("Exception: "+e1.getMessage() );
				e1.printStackTrace();
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
			new NativeXmlPlugin(plotName);
		}
		catch(Exception e1) 
		{
			System.out.println("Exception: "+e1.getMessage() );
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
		this.init(plotName);
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
		//this.init(plotName);
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
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "startDate") );
	}
	public String getProjectStopDate(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "stopDate") );
	}
	public String getProjectDescription(String plotName)
	{
		this.init(plotName);
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
			this.init(plotName);
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
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "authorE") );
	}
	public String getYCoord(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "authorN") );
	}
	public String getLatitude(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "latitude") );
	}
	public String getLongitude(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "longitude") );
	}
	public String getUTMZone(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "authorZone") );
	}
	public String getDatumType(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "authorDatum") );
	}
	public String getPlotShape(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "shape") );
	}
	public String getPlotArea(String plotName)	
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "area") );
	}
	
	public String getState(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "state") );
	}
	
	public String getTopoPosition(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "topoPosition") );
	}
	
	//must me a numeric value	
	public String getSlopeAspect(String plotName)
	{
		this.init(plotName);
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
		this.init(plotName);
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

	public String getSurfGeo(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "geology") );
	}
	public String getCountry(String plotName)
	{
		this.init(plotName);
		String s = null;
		String path = "/project/plot/country";
		Vector pathContent = parse.getValuesForPath(doc, path);
		if ( pathContent.size() == 1 )
		{
			s = (String)pathContent.elementAt(0);
		}
		return( s );
	}
	public String getStandSize(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "standSize") );
	}
	public String getAuthorLocation(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "authorLocation") );
	}
	public String getLandForm(String plotName)
	{
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "landform") );
	}
	
	// this has cannot be null and should be numeric
	public String getElevation(String plotName)
	{
		this.init(plotName);
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
		this.init(plotName);
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
		this.init(plotName);
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
		this.init(plotName);
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
		this.init(plotName);
		return( this.getPlotAttribute(plotName, "className") );
	}
	public String getCommunityCode(String plotName)
	{
		return(stringBuf);
	}
	public String getCommunityLevel(String plotName)
	{
		return(stringBuf);
	}
	public String getCommunityFramework(String plotName)
	{
		return(stringBuf);
	}
	public String getHydrologicRegime(String plotName)
	{
		return(stringBuf);
	}
	public String getSoilDrainage(String plotName) 
	{
		return(stringBuf);
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
		return(stringBuf);
	}
		//START
	/**
	*/
	public String getObsDateAccuracy(String plotName)
	{
		String s = "";
		return(s);
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
		String s = "";
		return(s);
	}

	/**
	 */
	public String getMethodNarrative(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getCoverDispersion(String plotName )
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public boolean getAutoTaxonCover(String plantName)
	{
		boolean s = true;
		return(s);
	}
	
	/**
	 */
	public String getStemObservationArea(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getStemSampleMethod(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getOriginalData(String plotName )
	{
		String s = "";
		return(s);
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
		String s = "";
		return(s);
	}
	
	//START
public String getPlotValidationLevel(String plotName)
{
	return("");
}

public String  getFloristicQuality(String plotName)
{
	return("");
}

public String  getBryophyteQuality(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	elementValue = parse.getNodeValue(doc, "bryophyteQuality");
	return(elementValue);
}

public String  getLichenQuality(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "lichenQuality");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: lichenQuality");
	}
	return(elementValue);
}

public String  getObservationNarrative(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "observationNarrative");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: observationNarrative");
	}
	return(elementValue);
}

public String  getHomogeneity(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "homogeneity");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: homogeneity");
	}
	return(elementValue);
}

public String  getPhenologicAspect(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "phenologicAspect");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: phenologicAspect");
	}
	return(elementValue);
}

public String  getRepresentativeness(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "representativeness");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: representativeness");
	}
	return(elementValue);
}

public String  getBasalArea(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "basalArea");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: basalArea");
	}
	return(elementValue);
}

public String  getSoilMoistureRegime(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "soilMoistureRegime");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: soilMoistureRegime");
	}
	return(elementValue);
}

public String  getWaterSalinity(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	elementValue = parse.getNodeValue(doc, "waterSalinity");
	return(elementValue);
}

public String  getShoreDistance(String plotName)
{
	return("");
}

public String  getOrganicDepth(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "organicDepth");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: organicDepth");
	}
	return(elementValue);
}

public String  getPercentBedRock(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentBedrock");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentBedRock");
	}
	return(elementValue);
}

public String  getPercentRockGravel(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentRockGravel");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentRockGravel");
	}
	return(elementValue);
}

public String  getPercentWood(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentWood");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentWood");
	}
	return(elementValue);
}
public String  getPercentLitter(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentLitter");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentLitter");
	}
	return(elementValue);
}

public String  getPercentBareSoil(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentBareSoil");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentBareSoil");
	}
	return(elementValue);
}

public String  getPercentWater(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentWater");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentWater");
	}
	return(elementValue);
}

public String  getPercentOther(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "percentOther");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: percentOther");
	}
	return(elementValue);
}

public String  getNameOther(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "nameOther");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: nameOther");
	}
	return(elementValue);
}

public String  getStandMaturity(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "standMaturity");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: standMaturity");
	}
	return(elementValue);
}


public String  getSuccessionalStatus(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "successionalStatus");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: successionalStatus");
	}
	return(elementValue);
}

public String  getTreeHt(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "treeHt");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: treeHt");
	}
	return(elementValue);
}

public String  getShrubHt(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "shrubHt");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: shrubHt");
	}
	return(elementValue);
}

public String  getNonvascularHt(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "nonvascularHt");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: nonvascularHt");
	}
	return(elementValue);
}

public String  getFloatingCover(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "floatingCover");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: floatingCover");
	}
	return(elementValue);
}

public String  getSubmergedCover(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "submergedCover");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: submergedCover");
	}
	return(elementValue);
}

public String  getDominantStratum(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "dominantStratum");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: dominantStratum");
	}
	return(elementValue);
}

public String  getGrowthform1Type(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform1Type");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform1Type");
	}
	return(elementValue);
}

public String  getGrowthform2Type(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform2Type");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform2Type");
	}
	return(elementValue);
}

public String  getGrowthform3Type(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform3Type");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform3Type");
	}
	return(elementValue);
}

public String  getGrowthform1Cover(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform1Cover");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform1Cover");
	}
	return(elementValue);
}

public String  getGrowthform2Cover(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform2Cover");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform2Cover");
	}
	return(elementValue);
}

public String  getGrowthform3Cover(String plotName)
{
	String elementValue = null;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "growthform3Cover");
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: growthform3Cover");
	}
	return(elementValue);
}

public boolean  getNotesPublic(String plotName)
{
	String elementValue = null;
	boolean b = false;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "notesPublic");
		System.out.println("NativeXmlPlugin >  " + elementValue);
		b = Boolean.getBoolean(elementValue);
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: notesPublic");
	}
	return(b);
}

public boolean  getNotesMgt(String plotName)
{
	String elementValue = null;
	boolean b = false;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "notesMgt");
		b = Boolean.getBoolean(elementValue);
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: notesMgt");
	}
	return(b);
}

public boolean  getRevisions(String plotName)
{
	String elementValue = null;
	boolean b = false;
	this.init(plotName);
	try
	{
		elementValue = parse.getNodeValue(doc, "revisions");
		b = Boolean.getBoolean(elementValue);
	}
	catch(Exception e )
	{
		System.out.println("Exception parsing: revisions");
	}
	return(b);
}
//END
//END


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
				
				if ( nodeName.equals("stratumComposition") )
				{
					if ((cn != null) && (cn.getNodeType() == 1))
					{
						NodeList children2 = cn.getChildNodes();
						//System.out.println("length: " + children2.getLength() );
						Node renegade = children2.item(3);
						String s = (renegade.getNodeName());
						//System.out.println("renegade name: " + s );
					
						s = renegade.getFirstChild().getNodeValue();
						//System.out.println("renegade val: " + s );
						strataVec.addElement( s );
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
		return(stringBuf);
	}
	public String getPlantTaxonCode(String plantName)
	{
		return(stringBuf);
	}
	public Vector getUniqueStrataNames(String plotName)
	{
		this.init(plotName);
		String path = "/project/plot/observation/stratum/stratumName";
		Vector pathContent = parse.getValuesForPath(doc, path);
		return(pathContent);
	}
	
	public String getStrataCover(String plotName, String stratumType)
	{
		this.init(plotName);
		String cover = null;
		Hashtable h = util.getStratum(doc, stratumType);
		cover = (String)h.get("stratumCover");
		return(cover);
	}
	
	public String getStrataBase(String plotName, String strataName)
	{
		this.init(plotName);
		String cover = null;
		Hashtable h = util.getStratum(doc, strataName);
		cover = (String)h.get("stratumBase");
		return(cover);
	}
	public String getStrataHeight(String plotName, String strataName)
	{
		this.init(plotName);
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
			NativeXmlPlugin nativesrc = new NativeXmlPlugin(plotName);
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
			System.out.println("Exception: " +e.getMessage() );
			e.printStackTrace();
		}
		return(returnVec);
	}
	
		/**
		* method that returns a hashtable containing the information for a given
		* strataType the keys include.  The keys in the hashtable are : <br> <br>
		* stratumType -- the name of the stratum which should be the same as the 
		* 	input parameter 'stratumType' <br>
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
				System.out.println("Exception: " +e.getMessage() );

				e.printStackTrace();
			}
			return(returnHash);
		}
		
	 }
}
