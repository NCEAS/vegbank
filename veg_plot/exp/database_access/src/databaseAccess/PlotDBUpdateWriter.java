package databaseAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.vegbank.common.utility.*;

import xmlresource.utils.transformXML;



/**
 * 
 *	Purpose: This class takes the traslated value of an xml file 
 * 	containing data that will be used to update the database and
 * 	puts the data into the veg plots database.  The translated 
 *	data input to this class must be from an xml document that
 *	conforms to the 'vegPlot2001cv.dtd' and has had been translated 
 *	by the 'zz.xsl' style sheet
 *			
 *  Copyright: 2000 Regents of the University of California and the
 *  	National Center for Ecological Analysis and Synthesis
 * 	Authors: John Harris
 *
 */



public class PlotDBUpdateWriter 
{
static LocalDbConnectionBroker lb;

/**
 * Main method used for testing -- will load an update xml
 * file to the database and report back to the user any errors
 */
public static void main(String[] args) 
{
	if (args.length != 1) 
	{
		System.out.println("Usage: PlotDBUpdateWriter.java   [XML] \n");
		System.exit(0);
	}
	else 
	{
		String inputXml=null;
		inputXml=args[0];
		String styleSheet = "transformUpdate.xsl";
		String attributeFile ="attFile.txt";
		try 
		{
		
			//call the method to translate the update xml file
			PlotDBUpdateWriter pdbuw = new PlotDBUpdateWriter();
			Vector fileVector = pdbuw.translateUpdateXml(inputXml, styleSheet, attributeFile);
		
			//call the to update the db
			pdbuw.updateDB(fileVector);
		}
		catch (Exception e) 
		{
			System.out.println("failed in main " + 
			e.getMessage());
			e.printStackTrace();
		}
	}
}


/**
 * Method that translates the update xml
 * document by the correponding xslt style sheet
 *
 * @param inputXml
 */	
private Vector translateUpdateXml(String inputXml, String styleSheet, 
	String attributeFile)

	throws java.io.IOException,
        java.net.MalformedURLException,
        org.xml.sax.SAXException
{
	Vector fileVector = new Vector();
	//obtain a interface to a new XSLTProcessor object.
	
	
	transformXML trans = new transformXML();
	FileWriter fw = new FileWriter(attributeFile);
	trans.getTransformed(inputXml, styleSheet, fw);
	fw.close();

	//read the resulting file into the attribute vector
	BufferedReader in = new BufferedReader(new FileReader(attributeFile), 8192);
	String s=null;
	while ((s=in.readLine()) != null) 
	{
		//System.out.println(s);
		fileVector.addElement(s);
	}
	//parse all the attributes and constraint
return(fileVector);
}



/**
 * Method that takes the translated update
 * xml file and updates the database with the 
 * values
 */	
private String  updateDB(Vector fileVector)
{
	Connection conn=null;
	Statement query = null;
	ResultSet results = null;
	
	//start the connection pooling
	try 
	{
		LocalDbConnectionBroker.manageLocalDbConnectionBroker("initiate");
		conn = LocalDbConnectionBroker.manageLocalDbConnectionBroker("getConn");
		query = conn.createStatement ();
	}
	catch (Exception e) 
	{
		System.out.println("failed making database connection " + 
		e.getMessage());
		e.printStackTrace();
	}
	
	
	int updateNum = getNumUpdateInstances(fileVector);
	for (int i=0; i<updateNum; i++)
	{
		Hashtable updateInstance = getCurrentUpdateInstance(i, fileVector);
		System.out.println(updateInstance.toString());
		String constrainingAttribute = updateInstance.get("constrainingAttribute").toString();
		String constrainingValue = updateInstance.get("constrainingValue").toString();
		String updateTable= updateInstance.get("updateTable").toString();
		String updateValue = updateInstance.get("updateValue").toString();
		String updateAttribute = updateInstance.get("updateAttribute").toString();
		
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE "+updateTable+" ");
		sb.append("SET "+updateAttribute+" = '"+updateValue+"' ");
		sb.append("WHERE "+constrainingAttribute+" = '"+constrainingValue+"'");
		
		System.out.println(sb.toString());
		try 
		{
			query.executeUpdate(sb.toString());
		}
		catch (Exception e) 
		{
			System.out.println("failed making transaction " + 
			e.getMessage());
			e.printStackTrace();
		}
		
	}

	return("test");
}



/**
 * method that return the number of attributes 
 * defined in the update xml file using as input 
 * the transformed xml file in a vector
 */
private Hashtable getCurrentUpdateInstance (int attributeNumber, Vector fileVector)
{
	System.out.println("attributeNumber "+attributeNumber);
	Hashtable updateValues = new Hashtable();
	int currentNumber=0;
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().equals("startUpdate"))
		{
		
			if (currentNumber==attributeNumber)
			{
				//make sure that the next line contains the attribute name
				if (fileVector.elementAt(i+1).toString().startsWith("constrainingAttribute"))
				{
					System.out.println("found the attribute");
					String constrainingAttribute = pipeStringTokenizer(
						fileVector.elementAt(i+1).toString(), 2);
					String constrainingValue = pipeStringTokenizer(
						fileVector.elementAt(i+2).toString(), 2);
					String updateTable= pipeStringTokenizer(
						fileVector.elementAt(i+3).toString(), 2);
					String updateAttribute= pipeStringTokenizer(
						fileVector.elementAt(i+4).toString(), 2);
					String updateValue= pipeStringTokenizer(
						fileVector.elementAt(i+5).toString(), 2);
					
					
					
					updateValues.put("constrainingAttribute", constrainingAttribute );
					updateValues.put("constrainingValue", constrainingValue);
					updateValues.put("updateTable", updateTable);
					updateValues.put("updateAttribute", updateAttribute);
					updateValues.put("updateValue", updateValue);
					
					System.out.println(updateValues.toString());
					
					return(updateValues);
				}
				else
				{
					System.out.println("error found in the transformed update xml");
				}
			}
				currentNumber++;
		}
	}
	return(updateValues);
}



/**
 * method that return the number of update instances
 *  in the translated update xml file using as input 
 * the transformed xml file in a vector
 */
private int getNumUpdateInstances (Vector fileVector)
{
	int num=0;
	for (int i=0; i<fileVector.size(); i++)
	{
		if (fileVector.elementAt(i).toString().equals("startUpdate"))
		{
			num++;
		}
	}
	return(num);
}



/**
 * method to tokenize elements from a pipe delimeted string
 */	
public String pipeStringTokenizer(String pipeString, int tokenPosition)
{
	//System.out.println("%%%%% "+pipeString+" "+tokenPosition);
	String token="nullToken";
	if (pipeString != null) 
	{ 
		StringTokenizer t = new StringTokenizer(pipeString.trim(), "|");
		int i=1;
		while (i<=tokenPosition) 
		{
			if ( t.hasMoreTokens() ) 
			{
	 			token=t.nextToken();
				i++;
			}
			else 
			{
				token="nullToken";
				i++;
			}
		}
		return(token);
	}
	else 
	{
		return(token);
	}
}



}

