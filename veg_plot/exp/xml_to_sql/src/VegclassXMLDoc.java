/**
 * 	This class handles the transformation of the base xml 
 *  documentation for the plots database into other data formats
 *  such as an xml format constent with the jakarta-torque ( a subset 
 *  of the Turbine project )
 *
 *     '$Author: harris $'
 *     '$Date: 2001-11-20 19:50:05 $'
 *     '$Revision: 1.1 $'
 *	
 *
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
//import org.apache.xalan.xpath.xml.FormatterToXML;
//import org.apache.xalan.xpath.xml.TreeWalker;
//import org.apache.xalan.*;
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

//import vegclient.framework.*;

public class VegclassXMLDoc 
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
	
	
	
	/**
	 * this second constructor is to convert an entire project using as input 
	 * a conf file that has the all the xml files, in the correct order,
	 * for the nvc or esa database
	 */
	public VegclassXMLDoc(String projectFile)
	{
	try
  {
		//the printWriter
		PrintWriter out = new PrintWriter(new FileWriter("test_fix.xml"));
		//the string buffer to store the xml
		StringBuffer sb = new StringBuffer();
		//this is to store the foreign key stuff that has to come last
		StringBuffer fksb = new StringBuffer();
		//header info
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?> \n");
		sb.append("<!DOCTYPE app-data SYSTEM \"http://jakarta.apache.org/turbine/dtd/database.dtd\"> \n");
		sb.append("	<app-data> \n");
		sb.append("		<database name=\"nvc\"> \n");		
		
		
		//cycle thru the vector containing the files
		Vector xmlFiles = databasePlotXMLPackage();
		for (int ii =0; ii < xmlFiles.size(); ii++)
		{
			String fileName = (String)xmlFiles.elementAt(ii);
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
			//System.out.println("Document element: "+ root.getNodeName() );
			System.out.println("table name: "+ getTableName() );
			//this will return the entire table
			sb.append( getTableXMLOutput(doc) );
		}
		sb.append("</database> \n");
		sb.append("</app-data> \n");
		//print the xml
		out.println(sb.toString() );
		out.close();
		
    } 
		catch(Exception e1) 
		{
			System.out.println("Exception: "+e1.getMessage() );
			e1.printStackTrace();
    }		
	}
	
		/**
	 * constructor
	 */
	public VegclassXMLDoc()
	{
		
	}
	
	
	
	/**
	 * constructor
	 */
	public VegclassXMLDoc(String inXml, String outXml)
	{
	try
  {
		//the printWriter
		PrintWriter out = new PrintWriter(new FileWriter("test_fix.xml"));
		//the string buffer to store the xml
		StringBuffer sb = new StringBuffer();
		//this is to store the foreign key stuff that has to come last
		StringBuffer fksb = new StringBuffer();
		//header info
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?> \n");
		sb.append("<!DOCTYPE app-data SYSTEM \"http://jakarta.apache.org/turbine/dtd/database.dtd\"> \n");
		sb.append("	<app-data> \n");
		sb.append("		<database name=\"nvc\"> \n");		
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
		System.out.println("table name: "+ getTableName() );
		//this will return the entire table
		sb.append( getTableXMLOutput(doc) );

		sb.append("</database> \n");
		sb.append("</app-data> \n");
		//print the xml
		out.println(sb.toString() );
		out.close();
		
    } 
		catch(Exception e1) 
		{
			System.out.println("Exception: "+e1.getMessage() );
			e1.printStackTrace();
    }		
	}
	
		/**
	 * method that returns a String represnting a complete table
	 */
	 public String getTableXMLOutput(Document doc)
	 {
		 StringBuffer sb = new StringBuffer();
		 StringBuffer fksb = new StringBuffer();
	try
  {
		root = doc.getDocumentElement();
		//the element node is the Plot_package
		//System.out.println("Document element: "+ root.getNodeName() );
		//System.out.println("table name: "+ getTableName() );
		sb.append("<table name=\""+getTableName()+"\"> \n");
		//System.out.println("col names: " + getCollumnNames().toString() );
		Vector collumnNames = getCollumnNames();
		
		
		for (int ii =0; ii < collumnNames.size(); ii++)
		{
			
			String currentCollumnString = (String)collumnNames.elementAt(ii);
			// start loading the collumn data to the stringbuffer
			sb.append("<column name=\""+currentCollumnString+"\"");
		
			Node currentCollumn = getCollumnData( currentCollumnString );
		
			NodeList nl = currentCollumn.getChildNodes();
			//System.out.println( "nodelListLength: "+ nl.getLength() );
		
			//look for all the relevant nodes like type, nulls, etc
			for (int i =0; i < nl.getLength(); i++)
			{
				//System.out.println( "node Names: "+ nl.item(i).getNodeName());
				Node checker = nl.item(i);
				//System.out.println("looking for date val: "+ checker.getNodeValue());
				if ( checker.getNodeName().equals("attType") &&  checker.getNodeName() != null)
				{
					sb.append(getAttributeType( checker.getFirstChild().getNodeValue().trim() ) );
					//sb.append(" type=\""+ checker.getFirstChild().getNodeValue().trim() +"\"");
					//System.out.println("raw val: "+ checker.getFirstChild().getNodeValue().trim() );
				}
				if ( checker.getNodeName().equals("attNulls") &&  checker.getNodeName() != null)
				{
					sb.append(getRequiredRule( checker.getFirstChild().getNodeValue().trim() ) );
					//sb.append(" required=\""+ checker.getFirstChild().getNodeValue().trim() +"\"");
					//System.out.println("raw val: "+ checker.getFirstChild().getNodeValue().trim() );
				}
				if ( checker.getNodeName().equals("attKey") &&  checker.getNodeName() != null)
				{
					if ( checker.getFirstChild().getNodeValue().trim().startsWith("P") )
						sb.append(getPrimaryKeyRule( checker.getFirstChild().getNodeValue().trim() ) );
					if ( checker.getFirstChild().getNodeValue().trim().equals("FK") )
						fksb.append(getForeignKeyRule( checker.getFirstChild().getNodeValue().trim(), currentCollumnString, currentCollumn ) );
					//sb.append(" key=\""+ checker.getFirstChild().getNodeValue().trim() +"\"");
					//System.out.println("raw val: "+ checker.getFirstChild().getNodeValue().trim() );
				}
			}
			//close the collumn node
			sb.append("/> \n");
		}
		//now print the forein key stuff before the footer
		sb.append(fksb.toString() );
		sb.append("</table> \n");
		//sb.append("</database> \n");
		//sb.append("</app-data> \n");
		//print the xml
		//out.println(sb.toString() );
		//out.close();
		
    } 
		catch(Exception e1) 
		{
			System.out.println("Exception: "+e1.getMessage() );
			e1.printStackTrace();
    }		
		 return(sb.toString() );
	 }
	
	
	/**
	 * method that handles transforming the vegplot component of the database
	 *
	 * @param databaseComponent -- string representing the database component can
	 * be: plot, comm-taxonomy, plain-taxonomy
	 */
	private void getTorqueDBDoc(String databaseComponent)
	{
		try
  	{
			//the printWriter
			PrintWriter out = new PrintWriter(new FileWriter("test_fix.xml"));
			//the string buffer to store the xml
			StringBuffer sb = new StringBuffer();
			//this is to store the foreign key stuff that has to come last
			StringBuffer fksb = new StringBuffer();
			//header info
			//cycle thru the vector containing the files
			Vector xmlFiles = new Vector();
			if (databaseComponent.equals("plot") )
				xmlFiles=	databasePlotXMLPackage();
			else if (databaseComponent.equals("comm-taxonomy") )
				xmlFiles=	databaseCommunityTaxonomyXMLPackage();
			else if (databaseComponent.equals("plant-taxonomy") )
				xmlFiles=	databasePlantTaxonomyXMLPackage();
			else
			{ 
				System.out.println(" unknown databaseComponent: " + databaseComponent);
				System.exit(0);
			}
			
			sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?> \n");
			sb.append("<!DOCTYPE app-data SYSTEM \"http://jakarta.apache.org/turbine/dtd/database.dtd\"> \n");
			sb.append("	<app-data> \n");
			sb.append("		<database name=\"nvc\"> \n");		
		
		
			
			for (int ii =0; ii < xmlFiles.size(); ii++)
			{
				String fileName = (String)xmlFiles.elementAt(ii);
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
				//System.out.println("Document element: "+ root.getNodeName() );
				System.out.println("table name: "+ getTableName() );
				//this will return the entire table
				sb.append( getTableXMLOutput(doc) );
			}
			sb.append("</database> \n");
			sb.append("</app-data> \n");
			//print the xml
			out.println(sb.toString() );
			out.close();
		
  	  } 
			catch(Exception e1) 
			{
				System.out.println("Exception: "+e1.getMessage() );
				e1.printStackTrace();
  	  }			
		}
	
	
	/**
	* method that contains all the data files that are used to compose the 
	* nvc database
	*/
	private Vector databasePlotXMLPackage()
	{
		Vector v = new Vector();
		v.addElement("./data/vegclass_doc/vegPlotRevision.xml");
		v.addElement("./data/vegclass_doc/vegPlotUserDefined.xml");
		v.addElement("./data/vegclass_doc/vegPlotDefinedValue.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotProject.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotParty.xml");
		v.addElement("./data/vegclass_doc/vegPlotAddress.xml");
		v.addElement("./data/vegclass_doc/vegPlotTelephone.xml");
		//v.addElement("./data/vegclass_doc/vegPlotEmail.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotAux_Soil.xml");
		v.addElement("./data/vegclass_doc/vegPlotCitation.xml");
		v.addElement("./data/vegclass_doc/vegPlotNamedPlace.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotCoverMethod.xml");
		v.addElement("./data/vegclass_doc/vegPlotCoverIndex.xml");
		v.addElement("./data/vegclass_doc/vegPlotStratumMethod.xml");
		v.addElement("./data/vegclass_doc/vegPlotSampleMethod.xml");
		v.addElement("./data/vegclass_doc/vegPlotPlot.xml");
		v.addElement("./data/vegclass_doc/vegPlotPlace.xml");
		v.addElement("./data/vegclass_doc/vegPlotObservation.xml");
		v.addElement("./data/vegclass_doc/vegPlotGraphic.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotStratumType.xml");
		v.addElement("./data/vegclass_doc/vegPlotStratumComposition.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotTaxonObservation.xml");
		v.addElement("./data/vegclass_doc/vegPlotTaxonInterpretation.xml");
		v.addElement("./data/vegclass_doc/vegPlotTreeStem.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotSoilObs.xml");
		v.addElement("./data/vegclass_doc/vegPlotAux_Geography.xml");
		v.addElement("./data/vegclass_doc/vegPlotAux_Role.xml");
		
		v.addElement("./data/vegclass_doc/vegPlotNoteLink.xml");
		v.addElement("./data/vegclass_doc/vegPlotNote.xml");
		v.addElement("./data/vegclass_doc/vegPlotProjectContributor.xml");
		v.addElement("./data/vegclass_doc/vegPlotObservationContributor.xml");
		//late additions
		v.addElement("./data/vegclass_doc/vegPlotDisturbanceObs.xml");
		v.addElement("./data/vegclass_doc/vegPlotStratum.xml");
		v.addElement("./data/vegclass_doc/vegPlotCommInterpretation.xml");
		v.addElement("./data/vegclass_doc/vegPlotCommClass.xml");
		v.addElement("./data/vegclass_doc/vegPlotClassContributor.xml");
		v.addElement("./data/vegclass_doc/vegPlotAux_usgsQuad.xml");
		
		
		return(v);
	}
	
	
	/**
	* method that contains all the data files that are used to compose the 
	* nvc database
	*/
	private Vector databaseCommunityTaxonomyXMLPackage()
	{
		Vector v = new Vector();
		v.addElement("./data/vegclass_doc/commTaxaCommUsage.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommStatus.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommReference.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommName.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommLineage.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommDescription.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommCorrelation.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommConcept.xml");
		v.addElement("./data/vegclass_doc/commTaxaCommParty.xml");
		return(v);
	}
	
		private Vector databasePlantTaxonomyXMLPackage()
	{
		Vector v = new Vector();
		v.addElement("./data/vegclass_doc/plantTaxaPlantUsage.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantStatus.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantReference.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantName.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantLineage.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantDescription.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantCorrelation.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantConcept.xml");
		v.addElement("./data/vegclass_doc/plantTaxaPlantParty.xml");
		
		return(v);
	}
	
	
	
	
	/**
	 * method to return a the key value for an attribute ie. primary key, foreign
	 * key
	 */
	public String getPrimaryKeyRule(String attKey)
	{
		if (attKey.startsWith("P"))
			return(" primaryKey=\"true\"  autoIncrement=\"true\" ");
		else
			return(" ");
	}
	
	/**
	 * method to return a the key value for an attribute ie. primary key, foreign
	 * key
	 */
	public String getForeignKeyRule(String attKey, String attName, Node node)
	{
		StringBuffer sb = new StringBuffer();
		String refTable = null; 
		String refAttribute = null; 
		if (attKey.startsWith("FK"))
		{
			
		//	System.out.println("getting the table name: ");
			NodeList nl = node.getChildNodes();
		//	System.out.println( "nodelListLength: "+ nl.getLength() );
		
			//look for all the relevant nodes like type, nulls, etc
			for (int i =0; i < nl.getLength(); i++)
			{
				//System.out.println( "node Names: "+ nl.item(i).getNodeName());
				Node checker = nl.item(i);
				//System.out.println("looking for date val: "+ checker.getNodeValue());
				if ( checker.getNodeName().equals("attReferences") &&  checker.getNodeName() != null)
				{
					//yokenize the val into table and attribute
						StringTokenizer tok = new StringTokenizer(checker.getFirstChild().getNodeValue().trim(), ".");
						String buf = tok.nextToken();
						refTable =  buf;
						if ( tok.hasMoreTokens() )
						{
							refAttribute=tok.nextToken();
						}
						else
						{
							System.out.println("warning  --- missing tokens: "+attName);
						}
					//System.out.println( "raw val: "+ checker.getFirstChild().getNodeValue().trim());
				}
			}
				sb.append("<foreign-key foreignTable=\""+refTable+"\"> \n");
				sb.append("<reference local=\""+attName+"\" foreign=\""+refAttribute+"\"/> \n");
				sb.append("</foreign-key> \n");
			return(sb.toString() );
		}
		
		else
			return(" ");
	}
	

	
	
	/**
	 * method to return a string detailing the requirement of a value in the 
	 * database 
	 */
	public String getRequiredRule(String attNulls)
	{
		if (attNulls.startsWith("No"))
			return(" required=\"true\" ");
		else if (attNulls.startsWith("Yes"))
			return(" required=\"false\" ");
		else
			return(" ");
	}
	
	
	
	/**
	 * method to return a string containing the database type of an attribute
	 */
	public String getAttributeType(String attType)
	{
		if (attType.startsWith("INT"))
			return(" type=\"INTEGER\" ");
		else if (attType.startsWith("VARCHAR"))
			return(" type=\"VARCHAR\" size=\"22\" ");
		else if (attType.startsWith("TEX"))
			return(" type=\"VARCHAR\" size=\"200\" ");
		else if (attType.startsWith("DAT"))
			return(" type=\"TIMESTAMP\"  ");
		else if (attType.startsWith("NUMER"))
			return(" type=\"FLOAT\"  "); 
		else
		{
			System.out.println("unknown type");
			return(" type=\"VARCHAR\" size=\"10\" ");
		}
	}
	
	/**
	 *method to return a vector containing the table name
	 */
	public String getTableName()
	{
		String  tableName = null;
		//make a vector b/c that is what the parser returns
		Vector tableNames = new Vector();
		tableNames  = parse.get(doc, "entityName");
		tableName = (String)tableNames.elementAt(0);
		//the entityname is like 'database.table' so parse the table
		StringTokenizer tok = new StringTokenizer(tableName, ".");
		String buf = tok.nextToken();
		if ( tok.hasMoreTokens() )
		{
			buf=tok.nextToken();
			return(buf);
		}
		else
		{
			return(null);
		}
	}
	
	/**
	 *method to return a vector containing the list of collumn Names
	 */
	public Vector getCollumnNames()
	{
		//make a vector b/c that is what the parser returns
		Vector collumnNames = new Vector();
		collumnNames = parse.get(doc, "attName");
		return(collumnNames);
	}
	
	
/**
	* method to return a single node representing the data about a 
	* collumn in the database
	*/
	public Node getCollumnData(String collumnName)
	{
		//index number corresponding to the plot in the vector never should be less
		// than 0
		int index = -999;  
		Node returnNode = null;
		//get all the plot names and then get the one that matches the input
		Vector collumnNames = getCollumnNames();
		try
		{
			//System.out.println("getPlot.plotnames number: " + collumnNames.size());
			for (int i =0; i < collumnNames.size(); i++)
			{
				String currentCollumn = collumnNames.elementAt(i).toString().trim();
				//System.out.println("currentCollumn: " + currentCollumn +" > "+ collumnName);
				if ( currentCollumn.equals(collumnName) )
				{
					//System.out.println("index val: " + i);
					//assign the index
					index = i;
				}
			}
			//if the index is unchanged, still -999, then print an error
			if (index == -999)
			{
				System.out.println("plot with authorPlotCode: "+collumnName+"not found ERROR");
			}
			else
			{
				returnNode = parse.get(doc, "attribute", index);
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
	 * main method for running the class
	 */
	static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length != 2) 
			{
				System.out.println("Usage: java class-name directory target \n"
				+" direcory: the directory where the vegclass documentation resides \n"
				+" target: the database to target <plot, commtaxonomy> \n");
				System.exit(0);
			}
			else
			{
				String directory=args[0];
				String target = args[1];
				
				//VegclassXMLDoc vxd = new VegclassXMLDoc(inXml,  outXml);				
				//VegclassXMLDoc vxd = new VegclassXMLDoc("project_test");
				VegclassXMLDoc vxd = new VegclassXMLDoc();
				if ( target.equals("plot") )
				{
					vxd.getTorqueDBDoc("plot");
				}
				else if ( target.equals("plant-taxonomy") )
				{
					System.out.println(" processing the plant taxonomy");
					vxd.getTorqueDBDoc("plant-taxonomy");
				}
				else if (target.equals("comm-taxonomy") )
				{
					System.out.println(" processing the community taxonomy");
					vxd.getTorqueDBDoc("comm-taxonomy");
				}
				else
				{
					System.out.println("unrecognized input: "+ target);
				}
				
				
			}
		}
		
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
}
