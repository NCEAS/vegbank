/**
 * This class is intended to use an ascii
 * data file to build an xml file that can be
 * used to update the plots database
 *
 * @Author @author@
 * @Version @release@
 *
 *     '$Author: harris $'
 *     '$Date: 2001-10-10 18:12:41 $'
 *     '$Revision: 1.1 $'
 */
 
package vegclient.framework;

import java.io.IOException;
import java.io.*;
import java.util.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;

import vegclient.framework.*;




public class LegacyDataUpdateWizard 
{

public String styleSheet="legacyUpdateDescription.xsl";
public String attributeFile="attFile.txt"; //the resuting transformed file
public Vector fileVector = new Vector();
//public Hashtable attributeHash = new Hashtable();
//public Hashtable constraintHash = new Hashtable();

LegacyDataWizard ldw = new LegacyDataWizard();



/**
 * Main method that to start the data
 * translation from the ascii file into
 * the reulting xml file
 */
public static void main(String[] args) 
{
	if (args.length != 1) 
	{
		System.out.println("Usage: java LegacyDataUpdateWizard  [XML] \n");
		System.exit(0);
	}
	else 
	{
		String inputXml=null;
		inputXml=args[0];
		//call the method to convert the data package to xml
		LegacyDataUpdateWizard lduw =new LegacyDataUpdateWizard();  
		lduw.transformUpdateDataPackage(inputXml);
	}
}


/**
 * method that steps thru the processes associated with transforming the 
 * data package to an xml file -- currently the functionality includes
 * parseing the xml file that describes the data package and then parsing
 * the elements out of the data package and placing them into the xml file
 * which can be used to load the database
 * @param inputXml
 */
public void transformUpdateDataPackage (String inputXml) 
{
	try 
	{
		System.out.println(inputXml);
	
		//call the method to parse the xml file into 
		//a hash table containg all the information needed
		//to create an xml document with the update instances
		
		Hashtable attributeHash=parseUpdateDataPackageXml(inputXml);
		
		
		//to write out the xml pass the method the 
		//hash table containing all the data in the 
		//update xml file
		writeUpdateXml(attributeHash);
		
		
		
		//as a test that this works print to the system the object contents
//		System.out.println("ATTRIBUTE HASH CONTENTS ON NEXT LINE" );
//		for (Enumeration e = attributeHash.elements() ; e.hasMoreElements() ;) 
//		{
//        System.out.println(e.nextElement());
//		}
//		System.out.println("\n");
		
		//run thru the qaqc methods
		//qualityChecker();
		
		//parse the elements from the ascii files in the correct
		//order and print out as xml file
	//	AsciiElementParser aep = new AsciiElementParser();
	//	aep.parsePackageElements(attributeHash, constraintHash);
		
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: LegacyDataWizard.transformDataPackage "
		+e.getMessage() );
		e.printStackTrace();
	}
}



/**
 * method that return the number of attributes 
 * defined in the update xml file using as input 
 * the transformed xml file in a vector
 */
private void writeUpdateXml (Hashtable attributeHash)
{
	//get and then drop the keys that are known and are always 
	//in the hash table	
	String fileTheme = attributeHash.get("fileTheme").toString();
	String attributeDelimeter = attributeHash.get("attributeDelimeter").toString();
	String fileName = attributeHash.get("fileName").toString();
	attributeHash.remove("fileTheme");
	attributeHash.remove("attributeDelimeter");
	attributeHash.remove("fileName");
	
	//now the rest of the attributes in the 
	//hash should be the attributes that require
	//writing to the xml file
	try 
	{
	PrintStream out = new PrintStream(new FileOutputStream("test.out"));
	//print the initial lines to the xml file
	out.println("<?xml version=\"1.0\"?>");
	out.println("<!DOCTYPE vegPlot SYSTEM \"vegPlot2001cv.dtd\">");
	out.println("<dbUpdate>");
	
	for (Enumeration e = attributeHash.keys() ; e.hasMoreElements() ;) 
	{
		String currentKey = e.nextElement().toString();
  	if (attributeIsUpdateValue(attributeHash, currentKey) == true)
		{
			System.out.println("UPDATE KEY > "+currentKey );
			String updateXml=	writeIndividualAttribute(attributeHash, currentKey, fileName);
			//write to the file
			out.println(updateXml);
			}
 		}
		out.println("</dbUpdate>");
	}
	catch ( Exception e1 ) 	
	{
			System.out.println(" failed in:  "
			+e1.getMessage() );
			e1.printStackTrace();
	}			
}


private String writeIndividualAttribute (Hashtable attributeHash, 
	String currentKey, String fileName)
{
	StringBuffer sb = new StringBuffer();
	//extract the hashtable containg the current attribute
	//and get the relevant objects
	Hashtable tmpHash = (Hashtable)attributeHash.get(currentKey);
	//System.out.println("TMPHASH "+tmpHash.toString());
	String plotDBattribute = tmpHash.get("plotDBattribute").toString();
	String attributePosition = tmpHash.get("attributePosition").toString();
	int attributePositionInt = Integer.parseInt(attributePosition.trim());
	String DBconstraint_tableName = tmpHash.get("DBconstraint_tableName").toString();
	String DBconstraint_attributeName = tmpHash.get("DBconstraint_attributeName").toString();
	String DBupdate_tableName = tmpHash.get("DBupdate_tableName").toString();
	String DBupdate_attributeName = tmpHash.get("DBupdate_attributeName").toString();
	
	//extract the same info about the constraining attribute
	//this should be made more flexible so that the incomming xml
	//file does not have to has the same name as the attribute name in the DB
	Hashtable constrainHash = (Hashtable)attributeHash.get(DBconstraint_attributeName);
	String constrainAttributePosition = constrainHash.get("attributePosition").toString();
	int constrainAttributePositionInt = Integer.parseInt(constrainAttributePosition.trim());
	
	System.out.println("plotDBAttribut: "+plotDBattribute);
	System.out.println("attributePos: "+attributePosition);
	System.out.println("constraintTable: "+DBconstraint_tableName);
	System.out.println("constraintAttribute: "+DBconstraint_attributeName);
	System.out.println("updateTabele: "+DBupdate_tableName);
	System.out.println("update attribute: "+DBupdate_attributeName);
	System.out.println("qonstraing att pos: "+constrainAttributePosition);
	
	//open the file to read the update elements
	try {
		BufferedReader in =	new BufferedReader(new FileReader(fileName), 8192);
		String s=null;
		while ((s=in.readLine()) != null  ) 
		{
			sb.append("<dbUpdateInstance> \n");
				sb.append("	<constrainingAttribute>"+ DBconstraint_attributeName +" </constrainingAttribute> \n");
				String constrainingValue=ldw.pipeStringTokenizer(s, constrainAttributePositionInt);
				sb.append("	<constrainingValue>"+ constrainingValue +" </constrainingValue> \n");
				String updateValue =ldw.pipeStringTokenizer(s, attributePositionInt);
				sb.append("	<updateTable>"+ DBupdate_tableName +" </updateTable> \n");
				sb.append("	<updateAttribute>"+ DBupdate_attributeName +" </updateAttribute> \n");
				sb.append("	<updateValue>"+ updateValue +" </updateValue> \n");
			sb.append("</dbUpdateInstance> \n");
		}
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in:  "
		+e.getMessage() );
		e.printStackTrace();
	}		
return(sb.toString());	
}





/**
 * given a hashtable that contains one or many hashtables and 
 * a key for one of these embedded hashtables will return true
 * if the hashtable (reflecting an attribute) is an attribute that 
 * is used for updating an attribute in the database
 */

private boolean attributeIsUpdateValue (Hashtable attributeHash, String currentKey)
{
	//make a temporary hashtable with corresponding to 
	// the key
	Hashtable tmpHash = new Hashtable();
	tmpHash = (Hashtable)attributeHash.get(currentKey);
	///System.out.println("BADD "+tmpHash.toString());
	if ( tmpHash.containsKey("DBupdate_tableName") )                      
	{
		return(true);
	}
	else 
	{
		return(false);
	}
}



/**
 * method that reads the xml file that reads the xml file 
 * that defines the contents and structure of the ascii data 
 * package and arranges a series of hash tables containing
 * details of the structure of the ascii package that can be 
 * used in the ascii element parser class to parse the data 
 * for composition into xml file(s)
 *
 * @param inputXml
 */	
private Hashtable parseUpdateDataPackageXml(String inputXml)
	throws java.io.IOException,
        java.net.MalformedURLException,
        org.xml.sax.SAXException
{
	//obtain a interface to a new XSLTProcessor object.
	XSLTProcessor processor = XSLTProcessorFactory.getProcessor();

	// Have the XSLTProcessor processor object transform inputXML  to
	// StringWriter, using the XSLT instructions found in "*.xsl".
	//print to a file
	processor.process(new XSLTInputSource(inputXml), 
		new XSLTInputSource(styleSheet),
		new XSLTResultTarget(attributeFile));

	//read the resulting file into the attribute vector
	BufferedReader in = new BufferedReader(new FileReader(attributeFile), 8192);
	String s=null;
	while ((s=in.readLine()) != null) 
	{
		//System.out.println(s);
		fileVector.addElement(s);
	}
	//parse all the attributes and constraints
	//from the xml file into a series of hash tables
	//which describe the structure and constraints 
	//inherent in a data package
	Hashtable parsedElements=	parseXmlUpdateElements(fileVector);
return(parsedElements);
}




/**
 * method that takes as input the xml file that describes the
 * data legacy ascii package structure and composition and 
 * constructs a number of hashtables that are used tp parse the 
 * ascii tables into xml files that can be used to insert or 
 * update the database.  Although a bit of a hack, (having a 
 * strcture that may not serve all purposes) the structure of 
 * these hash tables are as follows:
 *
 *>attribute hash: key=attributeName (as used in the database) 
 * followed by a string that is pipe delimeted and contains 
 * the following tokens in the following order:
 * 1] attributeName (as used in the DB)
 * 2] fileName (including path on local file system)
 * 3] column position (in the ascii table as delimeted by the delimeter)
 * 4] fileAttributeName (name of the attribute as used in the ascii file)
 * 5] fileKey (the key within the file that constrains the file -- 
 *			example: authorPlotCode may be the filekey for a file that 
 * 			contains site information including coordinates and slope 
 *			attributes )
 *	method should be broken
 * into many sub methods to get the thing working the correct way 
 */	
private Hashtable parseXmlUpdateElements(Vector fileVector)
{
	Hashtable attributeHash= new Hashtable();
	//first add the general information about the 
	//update file like delimeter type etc..
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().startsWith("fileName"))
		{
			attributeHash.put("fileName", 
				ldw.pipeStringTokenizer(fileVector.elementAt(i).toString(), 2));
		}
		if (fileVector.elementAt(i).toString().startsWith("attributeDelimeter"))
		{
			attributeHash.put("attributeDelimeter", 
				ldw.pipeStringTokenizer(fileVector.elementAt(i).toString(), 2));
		}
		if (fileVector.elementAt(i).toString().startsWith("fileTheme"))
		{
			attributeHash.put("fileTheme", 
				ldw.pipeStringTokenizer(fileVector.elementAt(i).toString(), 2));
		}
	}	
	
	//load each of the attributes defined in 
	//the input xml file into the attribute hash
	int i=0;
	while ( i<=getNumAttributes(fileVector) ) 
	{
		if ( getAttributeName(i, fileVector) != null )
		{
			//attributeHash.put(keyName+".constrained", attributeInfo);
			attributeHash.put(getAttributeName(i, fileVector), 
			getAttributeValues(i, fileVector) );
		}
		i++;
	}
	System.out.println(attributeHash.toString());
	return(attributeHash);
}


/**
 * method that return the number of attributes 
 * defined in the update xml file using as input 
 * the transformed xml file in a vector
 */
private int getNumAttributes (Vector fileVector)
{
	int num=0;
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().equals("startAttribute"))
		{
			num++;
		}
	}
	return(num);
}



/**
 * method that return the number of attributes 
 * defined in the update xml file using as input 
 * the transformed xml file in a vector
 */
private String getAttributeName (int attributeNumber, Vector fileVector)
{
	String attributeName =null;
	int currentNumber=0;
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().equals("startAttribute"))
		{
			currentNumber++;
			if (currentNumber==attributeNumber)
			{
				//make sure that the next line contains the attribute name
				if (fileVector.elementAt(i+1).toString().startsWith("attributeName"))
				{
					String buf = fileVector.elementAt(i+1).toString();
					//grab the second token from the 
					//string using the metod in the ldw
					attributeName = ldw.pipeStringTokenizer(buf, 2);
					//attributeName = fileVector.elementAt(i+1).toString();
					System.out.println("ATTRIBUTE NAME "+attributeName);
					return(attributeName);
				}
				else
				{
					System.out.println("error found in the transformed update xml");
				}
			}
		}
	}
	return(attributeName);
}




/**
 * method that return the number of attributes 
 * defined in the update xml file using as input 
 * the transformed xml file in a vector
 */
private Hashtable getAttributeValues (int attributeNumber, Vector fileVector)
{
	Hashtable attributeValues = new Hashtable();
	int currentNumber=0;
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().equals("startAttribute"))
		{
			currentNumber++;
			if (currentNumber==attributeNumber)
			{
				//make sure that the next line contains the attribute name
				if (fileVector.elementAt(i+1).toString().startsWith("attributeName"))
				{
					//String buf = fileVector.elementAt(i+2).toString();
					String plotDBattribute = ldw.pipeStringTokenizer(
						fileVector.elementAt(i+2).toString(), 2);
					String attributePosition = ldw.pipeStringTokenizer(
						fileVector.elementAt(i+3).toString(), 2);
					String DBconstraint_tableName= ldw.pipeStringTokenizer(
						fileVector.elementAt(i+4).toString(), 2);
					String DBconstraint_attributeName= ldw.pipeStringTokenizer(
						fileVector.elementAt(i+5).toString(), 2);
					String DBupdate_tableName= ldw.pipeStringTokenizer(
						fileVector.elementAt(i+6).toString(), 2);
					String DBupdate_attributeName= ldw.pipeStringTokenizer(
						fileVector.elementAt(i+7).toString(), 2);
					
					attributeValues.put("plotDBattribute", plotDBattribute );
					attributeValues.put("attributePosition", attributePosition );
					//only add the contraint info to the 
					//hash if it exists 
					if (! DBconstraint_tableName.trim().startsWith("null"))
					{
						attributeValues.put("DBconstraint_tableName", DBconstraint_tableName );
						attributeValues.put("DBconstraint_attributeName", DBconstraint_attributeName );
					}
					//only add the update stuff if it
					//exists
					if (! DBupdate_tableName.trim().startsWith("null"))
					{
						attributeValues.put("DBupdate_tableName", DBupdate_tableName );
						attributeValues.put("DBupdate_attributeName", DBupdate_attributeName );
					}
					return(attributeValues);
				}
				else
				{
					System.out.println("error found in the transformed update xml");
				}
			}
		}
	}
	return(attributeValues);
}



}

