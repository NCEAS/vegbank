/**
 *  '$RCSfile: ValidationConstraint.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-12-13 22:25:15 $'
 * '$Revision: 1.2 $'
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

package edu.ucsb.nceas.vegbank.plotvalidation;
import java.io.*;
import java.lang.*;
import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.*;                         // DOM interface
import org.apache.xerces.parsers.DOMParser;   // Parser (to DOM)
import org.xml.sax.InputSource;

import xmlresource.utils.XMLparse; 



public class ValidationConstraint
{
	
	private XMLparse parser;
	private Document doc;
	private String constraintDocument = "constraints.xml";
	//this is the map of the list names and their position in the doc.
	private Hashtable listMap = new Hashtable();  
	
	/**
	 * constructor method to read the xml constraints file etc...
	 */
	 public ValidationConstraint()
	 {
		 try
		 {
			 parser = new XMLparse();
			 doc =parser.getDocument(constraintDocument);
			 
			 //build the map of the table names, attribute names and the list number
			 Vector tNames  = parser.getValuesForPath(doc, "/*/*/TableName");
			 Vector aNames = parser.getValuesForPath(doc, "/*/*/FieldName");
			 
			 if ( tNames.size() == aNames.size() )
			 {
				 for (int i=0;i<tNames.size();i++) 
				 {
					 String table = ((String)tNames.elementAt(i)).toUpperCase();
					 String attribute = ((String)aNames.elementAt(i)).toUpperCase();
					 String key = table+"|"+attribute;
					 System.out.println(table+"|"+attribute);
					 listMap.put(key, ""+i);
				 }
			 }
			else
			{
				System.out.println("ValidationConstraint > constraint document does not have equal attribute / table elements");
			}
			
			 
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	
	/**
	 *  method that takes the table name and attribute name and returns the 
	 *  vector containing the list of available elements
	 * @param table -- the name of the table
	 * @param attribute -- the name of the attribute
	 */
	 public Vector getConstraints(String table, String attribute)
	 {
		 Vector contents = new Vector();
		 try
		 {
			 // get the position of the 'list' element in the xml document
			 String key = table.toUpperCase()+"|"+attribute.toUpperCase();
			 String listPosition = (String)listMap.get(key);
			 int pos = Integer.parseInt(listPosition);
			 
			 //get the appropriate node and then create a new document with it
			 Node n = parser.get(doc, "list", pos);
			 Document d = parser.createDocFromNode(n, "rootElement");
			 
			 //check that the document is correctly built
			 NodeList nl = d.getElementsByTagName("values");
			 System.out.println("ValidationConstraint > values length: " + nl.getLength() );
			 /*
			 <list>
				<sourceTableName>aux_plot_geology</sourceTableName>
				<TableName>plot</TableName>
				<FieldName>geology</FieldName>
				<tableDataType>varchar (50)</tableDataType>
				<record>
				 <values>6</values>
				 <valueDescription>Full embargo on data</valueDescription>
				 <sortOrd>7</sortOrd>
				</record>
			 */
			 
			 contents = parser.getValuesForPath(d, "/*/*/*/values");
			 //System.out.println( contents.toString() );
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 return(contents);
	 }
}
