/*
 *	'$RCSfile: XMLToObjectTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-10 00:33:27 $'
 *	'$Revision: 1.2 $'
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
 
package org.vegbank.common.utility;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractList;

import javax.xml.parsers.ParserConfigurationException;

import org.vegbank.common.model.PlantConcept;
import org.vegbank.common.model.PlantName;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * @author farrell
 */

public class XMLToObjectTest extends TestCase
{
	XMLToObject x2o;
	PlantConcept pc;
	String fileContents;
	
	/**
	 * Constructor for XMLToObjectTest.
	 * @param arg0
	 */
	public XMLToObjectTest(String arg0)
	{
		super(arg0);
	}
	
	public void setUp()  throws ParserConfigurationException, SAXException, IOException
	{
		URL fileURL = getClass().getResource("TestXMLToObject.xml");
		String fileName = fileURL.getFile();
		fileContents =  Utility.getURLContent( fileURL );
		
		x2o = new XMLToObject(fileName);
		AbstractList generatedObjects = x2o.getGeneratedObjects();
		pc = (PlantConcept) generatedObjects.get(0);
	}
	
	public void  testExistsInObjectModel()
	{
		boolean shouldBeFalse = VegBankObjectWriter.existsInVegbankObjectModel("this_is_not_a_classname");
		assertEquals("No class by this name", false, shouldBeFalse);
		
		boolean shouldBeTrue = VegBankObjectWriter.existsInVegbankObjectModel("org.vegbank.common.model.PlantUsage");
		assertEquals("The PlantUsage Object should exist", true, shouldBeTrue);
		
		boolean shouldBeFalse2 = VegBankObjectWriter.existsInVegbankObjectModel("org.vegbank.common.model.pLaNtUsAge");
		assertEquals("The pLaNtUsAge Object should not exist", false, shouldBeFalse2);
	}
	
	public void testGetGeneratedObjects()
	{

		PlantName pn = pc.getPLANTNAME();
		
		assertEquals(pc.getPlantDescription(), "this is a description");
		assertEquals(pn.getPlantName(), "this is my name");
	}
	
	public void testXML2XML()
	{
		ObjectToXML o2x = new ObjectToXML(pc);
		assertEquals("The input and output xml should be the same", o2x.getXML() , fileContents );
	}
	
	public void testXMLToObject() throws ParserConfigurationException, SAXException, IOException
	{
		try
		{
			XMLToObject x2o = new XMLToObject("filename");
		}
		catch (IOException e)
		{
			// No Problem 
		}
	}
	

}
