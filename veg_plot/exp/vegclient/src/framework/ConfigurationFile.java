/**
 * This class is designed to store configuration information in
 * an XML file. The concept is similar to that of a Properties
 * file except that using the XML format allows for a hierarchy
 * of properties and repeated properties.
 * 
 * All 'keys' are element names, while values are always stored
 * as XML text nodes. The XML file is parsed and stored in 
 * memory as a DOM object. 
 * 
 * Note that nodes are specified by node tags rather than paths
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:41 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

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
import vegclient.framework.*;


public class ConfigurationFile
{
	
	public String workingDirectory = null;
	public String OSType = null;
	public String RDBMSType = null;
	public String DBManagerPlugin = null;
	public String servletLocation = null;
	
	public String configFile = "./lib/config.xml";
	
	//utility classes 
	XMLparse parse = new XMLparse();
	
	public ConfigurationFile()
	{
		
		RDBMSType = parse.getNodeValue(configFile, "RDBMSType");
		OSType = parse.getNodeValue(configFile, "OSType");
		DBManagerPlugin = parse.getNodeValue(configFile, "DBManagerPlugin");
		workingDirectory = parse.getNodeValue(configFile, "workingDirectory");
		servletLocation = parse.getNodeValue( configFile, "servletLocation");
	}
	
	
	/**
	 * method that replaces the name of the database lugin in the xml file
	 */
	 public void replacePlugin( String pluginName)
	 {
		 System.out.println(" replacing the database plugin with: " + pluginName);
	 		parse.replaceElement(configFile,  "DBManagerPlugin", pluginName
			, "./lib/config.xml");
	 }
	 
	 
	 /**
	 * method that replaces the name of the working directory in the 
	 * configuration xml file
	 */
	 public void replaceWorkingDirectory( String dir)
	 {
		 System.out.println(" replacing the working directory with: " + dir);
	 		parse.replaceElement(configFile,  "workingDirectory", dir
			, "./lib/config.xml");
	 }
	 
				
		
	/**
	 * main method for testing 
	 */
	public static void main(String[] args) 
	{
		ConfigurationFile config  = new ConfigurationFile();
			
			try 
			{
				System.out.println("RDBMS TYPE: "+ config.RDBMSType);
				System.out.println("OS TYPE: "+ config.OSType);
				System.out.println("WORKING DIRECTORY: " + config.workingDirectory);	
			}
			catch (Exception e) 
			{
				System.out.println("Exception: " 
				+ e.getMessage());
			}
		}
		
	
	
	
}
