/*
 *	'$RCSfile: ObjectToDBTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-12-10 19:37:40 $'
 *	'$Revision: 1.7 $'
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
 
package org.vegbank.common.utility.test; 

import org.vegbank.common.model.*;
import org.vegbank.common.utility.*;
import junit.framework.TestCase;

/**
 * @author farrell
 */

public class ObjectToDBTest extends TestCase
{
	
	// Need an object to test with 
	private Community comm = null;
	
	private Party party =null;
	private Address address = null;
	private VBModelBeanToDB o2db;
	
	/**
	 * Constructor for ObjectToXMLTest.
	 * @param arg0
	 */
	public ObjectToDBTest(String arg0)
	{
		super(arg0);
	}

	public void testObjectToDB()
	{

	}
	
	public void testWrite()
	{
		try
		{
			o2db.insert(party);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Had an exception thrown");
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		party = new Party();
		party.setEmail("test@test.com");
		party.setGivenname("Gabriel");
		party.setSurname("Farrell");	
		
		address = new Address();
		address.setCity("London");
		address.setCountry("UK");
		
		party.addparty_address(address);
		
		o2db = new VBModelBeanToDB();
		super.setUp();
	}

}
