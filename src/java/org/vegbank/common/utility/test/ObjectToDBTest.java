/*
 *	'$RCSfile: ObjectToDBTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:13 $'
 *	'$Revision: 1.4 $'
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
import org.vegbank.common.model.Plantparty;

import junit.framework.TestCase;

/**
 * @author farrell
 */

public class ObjectToDBTest extends TestCase
{
	
	// Need an object to test with 
	private Community comm = null;
	
	private ObjectToDB o2db;
	
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
	
	public void testGetPreparedStatementString()
	{
		try
		{
			String sqlString = o2db.getPreparedStatementString();
			System.out.println(sqlString);
			assertEquals("insert into Plantparty ( givenname, surname, email, Plantparty_id ) values ( ?,?,?, ? )", sqlString );	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Had an exception thrown");
		}
	}
	
	public void testWrite()
	{
		try
		{
			o2db.insert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Had an exception thrown");
		}
	}
	
	public void testIsObjectInDatabase()
	{
		try
		{
			o2db.isObjectInDatabase(null);
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
		Plantparty pp = new Plantparty();
		pp.setEmail("test@test.com");
		pp.setGivenname("Gabriel");
		pp.setSurname("Farrell");	
		o2db = new ObjectToDB(pp);
		super.setUp();
	}

}
