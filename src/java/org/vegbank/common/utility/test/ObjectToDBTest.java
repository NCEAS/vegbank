/*
 *	'$RCSfile: ObjectToDBTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-04-16 17:37:44 $'
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

import java.util.Hashtable;
import java.util.Vector;

import org.vegbank.common.model.Community;

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
		String sqlString = o2db.getPreparedStatementString();
		System.out.println(sqlString);
		assertEquals("insert into Community (  ) values (  )", sqlString );
		
		String sqlString2 = o2db.getPreparedStatementString();
		System.out.println(sqlString2);
		assertEquals("insert into Community ( column1, column2 ) values ( ?,? )", sqlString2 );	
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
		o2db = new ObjectToDB(comm);
		super.setUp();
	}

}
