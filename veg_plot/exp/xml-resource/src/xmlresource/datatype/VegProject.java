/**
 * class to store a vegetation plot as a DOM
 * and to perform specific functions to the DOM
 * structure like add plot information and query
 * the nodes
 *
 * 
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-11-01 17:44:42 $'
 * 	'$Revision: 1.1 $'
 */
//package vegclient.framework;
package xmlresource.datatype;

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

import xmlresource.datatype.*;
import xmlresource.utils.XMLparse;
//import vegclient.framework.*;


public class VegProject
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
  public VegProject(String filename) throws FileNotFoundException
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
	
	
	public int getNumberOfPlots()
	{
		numberOfPlots = parse.getNumberOfNodes(doc, "plot");
		//System.out.println("number of plots: "+numberOfPlots);
		return(numberOfPlots);
	}
	
	/**
	 * method to return a vector containing the list of plotNames
	 *
	 * @return plotNames -- a vector of the individual plotnames under the 
	 * vegClass root document node
	 */
	public Vector getPlotNames()
	{
		Vector plotNames = new Vector();
		plotNames = parse.get(doc, "authorPlotCode");
		return(plotNames);
	}
	
	//method that returns the project name of this project
	public String getProjectName()
	{
		String returnString = parse.getNodeValue(doc, "projectName");
		return(returnString);
	}
	
	//method that returns the description of the project
	public String getProjectDescription()
	{
		String returnString = parse.getNodeValue(doc, "projectDescription");
		return(returnString);
	}
	
	
	// method that takes as input a node containg a plot and writes it to the file
	// that is also specified at input basically just a wrapper for the 'saveDom'
	// method in the XMLparse class
	public void savePlot(Node plotNode, String fileName)
	{
		parse.saveDOM(plotNode, fileName);
	}
	
	
	/**
	 * method to save a plot to a file based on a plotName
	 * @param plotName -- the name of the plot {authorPlotCode}
	 * @param filename -- the filename that the plot should be saved to
	 */
	 public void savePlot(String authorPlotCode, String fileName)
	 {
		 Node plot = getPlot(authorPlotCode);
		 savePlot(plot, fileName);
	 }
	
	//method to return a single plot from a project xml file which may contain
	// several plots
	public Node getPlot(String authorPlotCode)
	{
		//index number corresponding to the plot in the vector never should be less
		// than 0
		int index = -999;  
		Node returnNode = null;
		//get all the plot names and then get the one that matches the input
		Vector plotNames = getPlotNames();
		for (int i =0; i < plotNames.size(); i++)
		{
			if ( plotNames.elementAt(i).toString().trim().equals(authorPlotCode) )
			{
				//assign the index
				index = i;
			}
		}
		//if the index is unchanged, still -999, then print an error
		if (index == -999)
		{
			System.out.println("plot with authorPlotCode: "+authorPlotCode+"not found ERROR");
		}
		else
		{
			returnNode = parse.get(doc, "plot", index);
		}
		return(returnNode);
	}
	
}
