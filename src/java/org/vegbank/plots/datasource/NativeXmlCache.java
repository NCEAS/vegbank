package org.vegbank.plots.datasource;

/**
 * '$RCSfile: NativeXmlCache.java,v $'
 *
 * Purpose: 
 *
 * '$Author: farrell $'
 * '$Date: 2003-08-21 21:16:45 $'
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import org.vegbank.xmlresource.XMLparse;

/**
 * 
 * 	'$Author: farrell $'
 *	'$Date: 2003-08-21 21:16:45 $'
 *	'$Revision: 1.3 $'
 */

public class NativeXmlCache
{
	// root node of the in-memory DOM structure
	private Node root;
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
	//the name of the directory where the plots xml docs are cached
	String cacheDir = "plot_cache";

	/**
	* constructor method
	* String passed to the constructor is the plot archive xml document 
	* which is parsed into an instance Document variable, at which point 
	* the 'createXMLCache' mathod could be called
	*
	* @param filename name of XML file
	*/
	public NativeXmlCache(String filename) throws FileNotFoundException
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
		catch (Exception e1)
		{
			System.out.println(
				"NativeXmlCache > Exception: " + e1.getMessage());
		}
		doc = parser.getDocument();
		root = doc.getDocumentElement();
	}

	/**
	 * second constructor method
	 */
	public NativeXmlCache()
	{
	}

	/**
	 * method to determine the number of plots stored in the plot archive
	 *
	 * @return numberOfPlots -- the number of plots in the archive file
	 */
	public int getNumberOfPlots()
	{
		numberOfPlots = parse.getNumberOfNodes(doc, "plot");
		return (numberOfPlots);
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
		return (plotNames);
	}

	/**
	 * method that takes as input a node containg a plot and writes it to the file
	 * that is also specified at input basically just a wrapper for the 'saveDom'
	 * method in the XMLparse class
	 * 
	 * @param fileName -- the name of the file that the plot xml doc will be written to
	 * @param plotNode -- the xml node containing the plot
	 */
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

	/**
	 * method to return a single plot from a project xml file which may contain
	 * several plots
	 * @param authorPlotCode
	 * @return node -- the xml plot associated with the input parameter
	 */
	public Node getPlot(String authorPlotCode)
	{
		//index number corresponding to the plot in the vector never should be less
		// than 0
		int index = -999;
		Node returnNode = null;
		//get all the plot names and then get the one that matches the input
		Vector plotNames = getPlotNames();
		for (int i = 0; i < plotNames.size(); i++)
		{
			if (plotNames
				.elementAt(i)
				.toString()
				.trim()
				.equals(authorPlotCode))
			{
				//assign the index
				index = i;
			}
		}
		//if the index is unchanged, still -999, then print an error
		if (index == -999)
		{
			System.out.println(
				"plot with authorPlotCode: "
					+ authorPlotCode
					+ "not found ERROR");
		}
		else
		{
			returnNode = parse.get(doc, "project", index);
		}
		return (returnNode);
	}

	/**
	 * method that creates a cache of atomized plot xml files.  Specifically a
	 * plot archive file that contains multiple plot elements will be used to 
	 * create a directory 'this.cacheDir' in which each plot, after being separated 
	 * from the archive file will be written as 'authorPlotCode.xml'
	 */
	public void createXMLCache()
	{
		try
		{
			System.out.println("NativeXmlCache > creating cache");
			//create the directory
			File f = new File(cacheDir);
			System.out.println("NativeXmlCache > readable: " + f.canRead());
			System.out.println("NativeXmlCache > writeable: " + f.canWrite());
			System.out.println("NativeXmlCache > make dir: " + f.mkdir());

			numberOfPlots = this.getNumberOfPlots();
			System.out.println(
				"NativeXmlCache > number of plots in archive: "
					+ numberOfPlots);
			Vector v = this.getPlotNames();

			for (int i = 0; i < numberOfPlots; i++)
			{
				String plot = (String) v.elementAt(i);
				System.out.println(
					"NativeXmlCache > writing: " + plot + ".xml");
				this.savePlot(plot, cacheDir + "/" + plot + ".xml");
			}
		}
		catch (Exception e)
		{
			System.out.println("NativeXmlCache > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method that will destroy the cache, including delete the files and
	 * the directory
	 */
	public void destroyXMLCache()
	{
		try
		{
			System.out.println("NativeXmlCache > destroying cache");
			File f = new File(cacheDir);
			//before the cache directory can be deleted the files must 
			//be removed
			String s[] = f.list();
			for (int i = 0; i < s.length; i++)
			{
				System.out.println("NativeXmlCache > deleting file: " + s[i]);
				File delFile = new File(cacheDir + "/" + s[i]);
				delFile.delete();
			}
			//now delete the directory
			System.out.println(
				"NativeXmlCache > deleted directory: " + f.delete());
		}
		catch (Exception e)
		{
			System.out.println("NativeXmlCache > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method that returns the directory where the xml cache is to be 
		* created
		*/
	public String getCacheDir()
	{
		return this.cacheDir;
	}

	/**
	 * method that returns a 'true' if a plot exits in a cache
	 * @param the name of the plot that is to be checked
	 */
	public boolean plotExistsInCache(String plot)
	{
		System.out.println(
			"NativeXmlCache > looking for plot file assocated with: " + plot);
		boolean dirExists = this.plotCacheDirExists();
		if (dirExists == false)
		{
			return (false);
		}
		else
		{
			File f = new File(cacheDir + "/" + plot + ".xml");
			if (f.exists())
			{
				return (true);
			}
			else
			{
				return (false);
			}
		}
	}

	/**
	 * method that returns 'true' if the cache dir exists
	 */
	public boolean plotCacheDirExists()
	{
		File f = new File(cacheDir);
		return (f.exists());
	}

	/**
	* main method for testing
	*/
	public static void main(String[] args)
	{
		try
		{
			String filename = "out.xml";
			NativeXmlCache project = new NativeXmlCache(filename);
			project.createXMLCache();
			//project.destroyXMLCache();
		}
		catch (Exception e)
		{
			System.out.println("NativeXmlCache > Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
