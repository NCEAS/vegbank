/*
 *	'$RCSfile: ObjectToXMLTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:34:17 $'
 *	'$Revision: 1.1 $'
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

import org.vegbank.common.model.Community;

import junit.framework.TestCase;

/**
 * @author farrell
 */

public class ObjectToXMLTest extends TestCase
{

	// Need an object to test with 
	private Community comm = null;
	/**
	 * Constructor for ObjectToXMLTest.
	 * @param arg0
	 */
	public ObjectToXMLTest(String arg0)
	{
		super(arg0);
	}
	
	

	public void testObjectToXML()
	{
		ObjectToXML o2x = new ObjectToXML(comm);
		String xmlString = o2x.getXML();
		System.out.println(xmlString);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		comm = new Community();
		comm.setClassLevel("Alliance");
		comm.setCode("COOOODE");
		comm.setName("testname");
		comm.setDescription("this is a fake description");	
		comm.setDateEntered("today");	
		comm.setParentCode("I'm a bastard");	
		comm.setCommonName("You don' want to know");	
		super.setUp();
	}

}
