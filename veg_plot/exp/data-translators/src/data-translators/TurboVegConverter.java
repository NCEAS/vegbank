/**
 * 
 *
 *		 '$Author: harris $'
 *     '$Date: 2002-04-17 01:58:46 $'
 *     '$Revision: 1.3 $'
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

import xmlresource.utils.transformXML;
import xmlresource.utils.XMLparse;
//import vegclient.framework.*;

public class TurboVegConverter 
{
	
	 /**
   * root node of the in-memory DOM structure
   */
  private Node root;

	/**
   * root node of the in-memory new DOM structure -- to be output
   */
  private Node newProject;
	
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
	
	//the hashtable that can be used to look up the value
	//for a species number
	private Hashtable speciesLookUpHash = new Hashtable();
	
	/**
	 * this is a second constrctor
	 */
	public TurboVegConverter()
	{
		
	}
	
	
	/**
	 * constructor
	 */
	public TurboVegConverter(String inXml, String outXml)
	{
		try
    {
			
			//the printWriter
			PrintWriter out = new PrintWriter(new FileWriter("test_fix.xml"));
			String fileName = inXml;
		
			DOMParser parser = new DOMParser();
    	File plotFile = new File(fileName);
    	InputSource in;
    	FileInputStream fs;
    	fs = new FileInputStream(fileName);
    	in = new InputSource(fs);
		
			parser.parse(in);
    	fs.close();
			
			doc = parser.getDocument();
    	//get the root node
			root = doc.getDocumentElement();
			//the element node is the Plot_package
			System.out.println("Document element: "+ root.getNodeName() );
			//import that into the new document
			Node newProject = doc.importNode(root, false);
			NodeList nl = newProject.getChildNodes();
			//System.out.println("Number of nodes in new project node: "+ nl.getLength() );
			//test print this node
			//parse.print(out, newProject); 
		
		
			//now do the conversions
			Vector plotNames = getPlotNames();
			System.out.println( plotNames.toString()  );
			
			speciesLookUpHash = getSpeciesList();

			//fix one plot at a time
			Node fixedPlot = null;
			System.out.println("number of plots: "+ plotNames.size());
			for (int i =0; i < plotNames.size() ; i++)
			{
				//System.out.println("processing plot: " + plotNames.elementAt(i).toString() );
				Node curPlot = getPlot( plotNames.elementAt(i).toString() );
				
				//System.out.println("current plot being proccessed:" + curPlot.toString() );
				fixedPlot = convertDate(curPlot);
				
				//this is mearly a test
				Node buf = updateSpeciesName(fixedPlot);
				
				//append to the newProject node
				newProject.appendChild( buf );
			}
			//print the project -- which is still a turboveg project file
			parse.print(out, newProject); 

			//lastly use the style sheet to transform the above document
			transformXML transformer = new transformXML();
			transformer.transformXMLDocumentToFile( "test_fix.xml", 
				"transformTurboVeg.xsl", outXml );
    	} 
			catch(Exception e1) 
			{
				System.out.println("Exception: "+e1.getMessage() );
				e1.printStackTrace();
    	}
		}
	
	/** 
	 * method that returns is a file is a valid turboVeg export file
	 * @param fileName -- a string that is the name of a turboveg file
	 */
	public boolean isValidTurboVegFile(String fileName)
	{
		System.out.println("validating turboveg export file: "+fileName);
		return(true);
	}

	
	
	/**
	*
	* method that adds a node with the species name into the species data node
	*
	* @param node -- the node containing the releve
	*/
	
	private Node updateSpeciesName(Node node)
	{
		
		NodeList nl = node.getChildNodes();
		//System.out.println( "nodelListLength: "+ nl.getLength() );
		
		for (int i =0; i < nl.getLength(); i++)
		{
		//	System.out.println( "node Names: "+ nl.item(i).getNodeName());
			Node checker = nl.item(i);
			//System.out.println("name: "+ checker.getNodeName());
			if ( checker.getNodeName().equals("Species_data") &&  checker.getNodeName() != null)
			{
				NodeList snl = checker.getChildNodes();
				for (int ii =0; ii < snl.getLength(); ii++)
				{
					if ( snl.item(ii).getNodeName().trim().equals("species_nr") )
					{
						//this is the species number which will be translated
						String speciesNumber
						= snl.item(ii).getFirstChild().getNodeValue().trim();
						
						Node newElem = doc.createElement("species_name");
						//this is the correct name retrieved from the lookup hash
						String correctName = (String)speciesLookUpHash.get(speciesNumber); 
						Node newText = doc.createTextNode(correctName);
        		//add text to element
        		newElem.appendChild(newText);
						checker.appendChild(newElem);
					}
				}
			}		
		}		
		return( node) ;
	}
	
	
	
	/**
	*
	* method that returns the plot node with  the correct  date.  The date that
	* turboveg uses is like '19970102' which means that the 1997 is the year the
	* 01 is the month and the 02 is the day
	*
	* @param node -- the node containing the releve
	*/
	
	private Node convertDate(Node node)
	{
		
		NodeList nl = node.getChildNodes();
		System.out.println( "nodelListLength: "+ nl.getLength() );
		
		for (int i =0; i < nl.getLength(); i++)
		{
		//	System.out.println( "node Names: "+ nl.item(i).getNodeName());
			Node checker = nl.item(i);
			//System.out.println("looking for date val: "+ checker.getNodeValue());
			if ( checker.getNodeName().equals("date") &&  checker.getNodeName() != null)
			{
				System.out.println("raw val: "+ checker.getFirstChild().getNodeValue().trim() );
				if ((checker.hasChildNodes()) ) 
				{
					String badDate = checker.getFirstChild().getNodeValue().trim();
					//get the value into the string
					
					String year = "2222";
					String month = null;
					String day = null;
					if ( badDate.length() <= 4 )
					{
							System.out.println("really terrible date: "+badDate);
							month = "01";
							year = "2222";
							day = "01";
					}
					else
					{
						if (  badDate.length() > 4 )
						{
							year = badDate.substring(0, 4);
							month = badDate.substring(4, 6);
							//this is hypersonicsql - specific code
							month = adjustMonthHsql( month );
							//this is the ideal situation for dates
							if ( badDate.length() == 8)
							{
								day = badDate.substring(6, 8);
							}
							else
							{
								//assign the day to be 01
								day = "01";
							}
						}
						else
						{
							System.out.println("really terrible date: "+badDate);
							month = "01";
							year = "2222";
							day = "01";
						}
					}
					//System.out.println("bad date value: "+  badDate +" year: "+ year+
					//	" month: "+month+" day: "+day+" good date: " + day+"-"+month+"-"+year);
					checker.getFirstChild().setNodeValue(day+"-"+month+"-"+year);
					String valString = checker.getFirstChild().getNodeValue();
					System.out.println("desired value: "+ valString );	
				}
				else 
				{  
					System.out.println("THIS SHOULD NOT HAPPEN -- THERE SHOULD BE A CHILD"); 
				}
			}		
		}		
		return(node) ;
	}
	
	
		
	
	
	
	/**
	 * method to adjust the month
	 */
	 private String adjustMonth(String m)
	 {
		 String month = "JAN";
		 if (m.trim().equals("02") ) month="FEB";
		 if (m.trim().equals("03") ) month="MAR";
		 if (m.trim().equals("04") ) month="APR";
		 if (m.trim().equals("05") ) month="MAY";
		 if (m.trim().equals("06") ) month="JUN";
		 if (m.trim().equals("07") ) month="JUL";
		 if (m.trim().equals("08") ) month="AUG";
		 if (m.trim().equals("09") ) month="SEP";
		 if (m.trim().equals("10") ) month="OCT";
		 if (m.trim().equals("11") ) month="NOV";
		 if (m.trim().equals("12") ) month="DEC";
		 return(month);
	 }
	
		/**
	 * method to adjust the month -- this method is specifically for the 
	 * 'hypersonic' sql database
	 */
	 private String adjustMonthHsql(String m)
	 {
		 String month = "01";
		 if (m.trim().equals("02") ) month="02";
		 if (m.trim().equals("03") ) month="03";
		 if (m.trim().equals("04") ) month="04";
		 if (m.trim().equals("05") ) month="05";
		 if (m.trim().equals("06") ) month="06";
		 if (m.trim().equals("07") ) month="07";
		 if (m.trim().equals("08") ) month="08";
		 if (m.trim().equals("09") ) month="09";
		 if (m.trim().equals("10") ) month="10";
		 if (m.trim().equals("11") ) month="11";
		 if (m.trim().equals("12") ) month="12";
		 return(month);
		 
	 }
	
		
 /**
	* method to return a vector containing the list of plotNames
	* for the tvexport file
	*/
	public Vector getPlotNames()
	{
		Vector plotNames = new Vector();
		plotNames = parse.get(doc, "releve_nr");
		//System.out.println("releve nos : "+ plotNames.toString() );
		return(plotNames);
	}
	
	
	
	/**
	 * method that gets all the plant_lists from the xml document which is a
	 * series of elements that have a species code and the corresponding species
	 * name
	 */
	 private Hashtable getSpeciesList()
	 {
		 Hashtable speciesHash = new Hashtable();
			Vector speciesNr = new Vector();
			Vector speciesNames = new Vector();
			speciesNr = parse.get(doc, "species_list_nr");
			speciesNames = parse.get(doc, "species_list_name");
			if ( speciesNr.size() != speciesNames.size() )
			{
				System.out.println("species names and numeric values not the same length - exiting");
				System.exit(0);
			}
			else
			{
				for (int i =0; i < speciesNr.size(); i++)
				{
					
					speciesHash.put( speciesNr.elementAt(i) ,  speciesNames.elementAt(i));
					
				}
			}
			//System.out.println("releve nos : "+ plotNames.toString() );
			return(speciesHash);
	 }
	
	
	
	/**
	 * removes the quoates that Stephan left in the xml
	 */
	private Vector removeQuotes(Vector vec)
	{
		Vector returnVec = new Vector();
		for (int i =0; i < vec.size(); i++)
		{
			//System.out.println( vec.elementAt(i).toString().replace('"', ' ') );
			if ( vec.elementAt(i).toString().trim().startsWith("<?") )
			{
				System.out.println( "printing xml header: "+ vec.elementAt(i).toString() );	
				returnVec.addElement( vec.elementAt(i).toString());
			}
			else if ( vec.elementAt(i).toString().trim().startsWith("<!") )
			{
				System.out.println( "printing xml header: "+ vec.elementAt(i).toString() );
				returnVec.addElement( vec.elementAt(i).toString());
			}
			else
			{
				returnVec.addElement( vec.elementAt(i).toString().replace('"', ' ') );
			}
		}
		return (returnVec);
		
	}
	
	
	/**method to return a single plot from a project xml file which may contain
	* several plots
	*/
	public Node getPlot(String releve_nr)
	{
		//index number corresponding to the plot in the vector never should be less
		// than 0
		int index = -999;  
		Node returnNode = null;
		//get all the plot names and then get the one that matches the input
		Vector plotNames = getPlotNames();
		try
		{
			System.out.println("getPlot.plotnames number: " + plotNames.size());
			for (int i =0; i < plotNames.size(); i++)
			{
				String currentPlot = plotNames.elementAt(i).toString().trim();
				if ( currentPlot.equals(releve_nr) )
				{
					System.out.println("currentPlot: " + currentPlot +" ~ "+ releve_nr);
					System.out.println("index val: " + i);
					//assign the index
					index = i;
				}
			}
			//if the index is unchanged, still -999, then print an error
			if (index == -999)
			{
				System.out.println("plot with authorPlotCode: "+releve_nr
					+" not found ERROR" );
			}
			else
			{
				returnNode = parse.get(doc, "Plot", index);
			}
		}
		  catch(Exception e)
     {
			 System.out.println("failed in:" + 
			e.getMessage());
			e.printStackTrace();
			 
     }
		return(returnNode);
	}
	
	
	
	/**
	 * will write a file from the elements in a vector
	 */
	private void printFile(Vector vec, String outFile)
	{
	  try
     {
      PrintWriter out = new PrintWriter(new FileWriter(outFile));
			for (int i =0; i < vec.size(); i++)
			{
				out.println( vec.elementAt(i).toString().trim() );	
			}
			out.close(); 
     		
		 }
     catch(Exception e)
     {
			 System.out.println("failed in:" + 
			e.getMessage());
			e.printStackTrace();
			 
     }
		}	 
		
	
/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then returns the vector 
 *
 * @param fileName name of the file that whose contents should be 
 * written to a vector
 */
	public Vector fileVectorizer (String fileName) 
	{
		Vector fileVector = new Vector();
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) 
			{
				//System.out.println(s);	
				fileVector.addElement(s);
			}
		}
		catch (Exception e) 
		{
			System.out.println("failed in:" + 
			e.getMessage());
			e.printStackTrace();
		}
		return(fileVector);
	} 
 
 
 
	
	/**
   * the main routine used to test and run the application that 
	 * 
   */
  static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length != 1) 
			{
				System.out.println("Usage:java code infile \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String inXml=args[0];
				String outXml = "processed_turboveg.xml";
				TurboVegConverter tvc = new TurboVegConverter(inXml,  outXml);				
			}
		 
		}
		
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	

}

