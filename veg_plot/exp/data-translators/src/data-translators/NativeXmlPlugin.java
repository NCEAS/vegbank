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
 * 	'$Author: harris $'
 *	'$Date: 2002-05-20 20:00:41 $'
 *	'$Revision: 1.1 $'
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
	 //init 
	//public void init(String plotName ) throws FileNotFoundException
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
	public Vector getPlotNames()
	{
		return(vectorBuf);
	}
		
	/**
	 **
	 ** THE PROJECT - RELATED DATA
	 **
	 **/
	public String getProjectName(String plotName)
	{
		return(stringBuf);
	}
	public Vector getProjectContributors(String plotName)
	{
		return(vectorBuf);
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
		return(stringBuf);
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
		return(stringBuf);
	}
	public String getProjectStopDate(String plotName)
	{
		return(stringBuf);
	}
	public String getProjectDescription(String plotName)
	{
		return(stringBuf);
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
	
	public String getSlopeAspect(String plotName)
	{
		return( this.getPlotAttribute(plotName, "slopeAspect") );
	}
	
	public String getSlopeGradient(String plotName)
	{
		return( this.getPlotAttribute(plotName, "slopeGradient") );
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
		return(stringBuf);
	}
	public String getLandForm(String plotName)
	{
		return( this.getPlotAttribute(plotName, "landform") );
	}
	public String getElevation(String plotName)
	{
		return( this.getPlotAttribute(plotName, "elevation") );
	}
	public String getElevationAccuracy(String plotName)
	{
		return( this.getPlotAttribute(plotName, "elevationAccuracy") );
	}
	public String	getConfidentialityReason(String plotName)
	{
		return(stringBuf);
	}
	public String getConfidentialityStatus(String plotName)
	{
		return(stringBuf);
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
		return(stringBuf);
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
		return(vectorBuf);
	}
	public String getCummulativeStrataCover( String plantName, String plotCode)
	{
		return(stringBuf);
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
				if (index == -999)
				{
					System.out.println("strataType: "+stratumType+" not found ERROR");
				}
				else
				{
					type = parse.getNodeValue(doc, "stratumName", index);
					cover = parse.getNodeValue(doc, "stratumCover", index);
					base = parse.getNodeValue(doc, "stratumBase", index);
					height = parse.getNodeValue(doc, "stratumHeight", index);
					
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
