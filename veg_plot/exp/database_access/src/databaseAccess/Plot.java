package databaseAccess;

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

import databaseAccess.*;

/**
 * class to store a vegetation plot as a DOM
 * and to perform specific functions to the DOM
 * structure like add plot information and query
 * the nodes
 *
 */
public class Plot
{

  /**
   * root node of the in-memory DOM structure
   */
  private Node root;

  /**
   * Document node of the in-memory DOM structure
   */
  private Document doc;

  /**
   * XML file name in string form
   */
  private String fileName;

  /**
   * Print writer (output)
   */
  private PrintWriter out;
	
	/**
	 * access to the parser utility
	 */
	private XMLparse parse = new XMLparse();
	
	//the number of plots in this project file
	int numberOfPlots = 0;
	
  /**
   * String passed to the creator is the XML config file name
   * 
   * @param filename name of XML file
   */
  public Plot(String filename) throws FileNotFoundException
  {
    this.fileName = filename;

    DOMParser parser = new DOMParser();
    File plotFile = new File(filename);
    InputSource in;
    FileInputStream fs;
    fs = new FileInputStream(filename);
    in = new InputSource(fs);

    try
    {
      parser.parse(in);
      fs.close();
    } 
		catch(Exception e1) 
		{
			System.out.println("Exception: "+e1.getMessage() );
    }
    doc = parser.getDocument();
    root = doc.getDocumentElement();
	}
	
	//method that returns the observation start data
	public String getStartDate()
	{
		String returnString = parse.getNodeValue(doc, "plotStartDate");
		return(returnString);
	}
	
	//method that returns the observation finish data
	public String getStopDate()
	{
		String returnString = parse.getNodeValue(doc, "plotStopDate");
		return(returnString);
	}
	
	//method that returns the plotObservation value -- this should be expanded to
	// return multiple values for a plot
	public String getPlotObservationCode()
	{
		String returnString = parse.getNodeValue(doc, "authorObsCode");
		return(returnString);
	}
	
	
	//method to return the slopeAspect of a plot
	public String getSlopeAspect()
	{
		String returnString = parse.getNodeValue(doc, "slopeAspect");
		return(returnString);
	}
	
	//method to return the slopeGradient of a plot
	public String getSlopeGradient()
	{
		String returnString = parse.getNodeValue(doc, "slopeGradient");
		return(returnString);
	}
	
	//method to return the slope position of a plot
	public String getSlopePosition()
	{
		String returnString = parse.getNodeValue(doc, "slopePosition");
		return(returnString);
	}
	
	//method to return the hydrologic regime of a plot
	public String getHydrologicRegime()
	{
		String returnString = parse.getNodeValue(doc, "hydrologicRegime");
		return(returnString);
	}
	
	//method to return the soil drainage of a plot
	public String getSoilDrainage()
	{
		String returnString = parse.getNodeValue(doc, "soilDrainage");
		return(returnString);
	}
	
	//method to return the shape of a plot
	public String getPlotShape()
	{
		String returnString = parse.getNodeValue(doc, "plotShape");
		//System.out.println("plot shape: " + returnString);
		return(returnString);
	}
	
	
	//method to return the altitude value of a plot
	public String getAltValue()
	{
		String returnString = parse.getNodeValue(doc, "altValue");
		return(returnString);
	}
	
	//method that returns the latitude of the plot origin
	public String getOriginLatitude()
	{
		String returnString = parse.getNodeValue(doc, "origLat");
		return(returnString);
	}
	
	//method that returns the latitude of the plot origin
	public String getOriginLongitude()
	{
		String returnString = parse.getNodeValue(doc, "origLong");
		return(returnString);
	}
	
	//method that returns the latitude of the plot origin
	public String getCoverScale()
	{
		String returnString = parse.getNodeValue(doc, "coverScale");
		return(returnString);
	}
	
	//method that returns the latitude of the plot origin
	public String getPlotSize()
	{
		String returnString = parse.getNodeValue(doc, "plotSize");
		return(returnString);
	}
	
	
	//method that returns the samplingmethod used in the plot collection
	public String getSamplingMethod()
	{
		String returnString = parse.getNodeValue(doc, "samplingMethod");
		return(returnString);
	}
	
	//method that returns the place name assigned to a plot
	public String getPlaceName()
	{
		String returnString = parse.getNodeValue(doc, "placeName");
		return(returnString);
	}
	
	
	//method that returns the plot name
	public String getPlotName()
	{
		String plotName = parse.getNodeValue(doc, "authorPlotCode");
		return(plotName);
	}
	
	//method that returns the plot name of the parent plot
	public String getParentPlotName()
	{
		String parentPlot = parse.getNodeValue(doc, "parentPlot");
		return(parentPlot);
	}
	
	//method to return the type of plot
	public String getPlotType()
	{
		String returnString = parse.getNodeValue(doc, "plotType");
		return(returnString);
	}
	
	//method to return the type of plot
	public String getSurfGeo()
	{
		String returnString = parse.getNodeValue(doc, "surfGeo");
		return(returnString);
	}
	
	
	//method that returns the number of strata in this given plot file
	public int getNumberOfStrata()
	{
		int returnInt = 0;
		returnInt = parse.getNumberOfNodes(doc, "strata");
		//System.out.println("number of plots: "+numberOfPlots);
		return(returnInt);
	}
	
	//method that returns a vector containing the names of the various starta
	//recognized in the plot
	public Vector getStrataTypes()
	{
		Vector returnVec = new Vector();
		returnVec = parse.get(doc, "strataName");
		//System.out.println("number of plots: "+numberOfPlots);
		return(returnVec);
	}
	
	
	
	//method that returns a hashtable containing the information for a given
	//strataType the keys include:
	//stratumType, stratumCover, strataumHeight
	public Hashtable getStrata(String strataType)
	{
		int index = -999;
		Hashtable returnHash = new Hashtable();
		Vector strataTypes = getStrataTypes();
		for (int i =0; i < strataTypes.size(); i++)
		{
			if ( strataTypes.elementAt(i).toString().trim().equals(strataType) )
			{
				//assign the index
				index = i;
			}
		}
		//if the index is unchanged, still -999, then print an error
		if (index == -999)
		{
			System.out.println("strataType: "+strataType+" not found ERROR");
		}
		else
		{
			String type = parse.getNodeValue(doc, "strataName", index);
			String cover = parse.getNodeValue(doc, "strataCover", index);
			String height = parse.getNodeValue(doc, "strataHeight", index);
			//stick these elements into the hashtable to be returned
			returnHash.put("stratumType", type);
			returnHash.put("stratumCover", cover);
			returnHash.put("stratumHeight", height);
			//returnNode = parse.get(doc, "plot", index);
		}
		return(returnHash);
	}
	
	
	//method that returns the number of taxonObservations made in a plot
	public int getNumberOfTaxonObservations()
	{
		int returnInt = 0;
		returnInt = parse.getNumberOfNodes(doc, "taxonObservation");
		//System.out.println("number of plots: "+numberOfPlots);
		return(returnInt);
	}
	
	//method that returns the names of the taxa observed
	public Vector getObservedTaxaNames()
	{
		Vector returnVec = new Vector();
		returnVec = parse.get(doc, "authNameId");
		//System.out.println("number of plots: "+numberOfPlots);
		return(returnVec);
	}
	
	
	//method that returns a hashtable containing the information related to a
	// taxon observation
	public Hashtable getTaxonObservation(int observationNumber)
	{
		Hashtable returnHash = new Hashtable();
	
		String authorNameId = parse.getNodeValue(doc, "authNameId", observationNumber);
		String strataType = parse.getNodeValue(doc, "strataType", observationNumber);
		String percentCover = parse.getNodeValue(doc, "percentCover", observationNumber);
			
		//stick these elements into the hashtable to be returned
		returnHash.put("authorNameId", authorNameId);
		returnHash.put("strataType", strataType);
		returnHash.put("percentCover", percentCover);
		//returnNode = parse.get(doc, "plot", index);
		
		return(returnHash);
	}
	
	
	
	
	//method that takes as input a node containg a plot and writes it to the file
	// that is also specified at input basically just a wrapper for the 'saveDom'
	// method in the XMLparse class
	public void savePlot(Node plotNode, String fileName)
	{
		parse.saveDOM(plotNode, fileName);
	}
	
	
	
	public void putPlotSite(String authorPlotCode)
	{
		System.out.println("adding plotSite to DOM structure");
		parse.addNewElement(doc, "project", 0, "plot", "plot2");
		//now print to file 
		parse.save(root, "out.xml");
	}
	
	
	public void putPlotObservation(String authorObsCode)
	{
		System.out.println("adding observation to DOM structure");
		parse.addNewElement(doc, "plot", 1, "observation", "observation2");
		//now print to file 
		parse.save(root, "out.xml");
	}
	
	
	
	
}
