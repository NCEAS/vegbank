package org.vegbank.common.utility.test; 
/*
 * '$RCSfile: ServletUtilityTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-01-18 20:47:47 $'
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
 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import org.vegbank.common.utility.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author farrell
 *
 * A unit test for the SerletUtility class 
 */
public class ServletUtilityTest extends TestCase
{

	/**
	 * Constructor to build the test
	 *
	 * @param name the name of the test method
	 */
	public ServletUtilityTest(String name)
	{
	  super(name);
	}

	/**
	 * Establish a testing framework by initializing appropriate objects
	 */
	public void setUp()
	{
    
	}

	/**
	 * Release any objects after tests are complete
	 */
	public void tearDown()
	{
	}
	
	/**
	 * Create a suite of tests to be run together
	 */
	public static Test suite()
	{
	  TestSuite suite = new TestSuite();
	  //suite.addTest(new DBinsertPlotSourceTest("initialize"));
	  return new TestSuite(ServletUtilityTest.class);
	}
  
	/**
	 * Run an initial test that always passes to check that the test
	 * harness is working.
	 */
	public void initialize()
	{
	  assertEquals(1,1);
	}
	
	/**
	 * Test the setZippedFile(Hashtable nameContent, OutputStream outStream) 
	 * method
	 */
	public void testSetZippedFile()
	{
		Hashtable nameContent = new Hashtable();
		nameContent.put("fileA", "The contents of File A");
		nameContent.put("fileB", "The contents of File B");
		
		//ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try
		{
			File file = new File("test.zip");
			FileOutputStream outStream = new FileOutputStream(file);
			
			ServletUtility.zipFiles(nameContent, outStream, 0);
			//InputStream is = new ByteArrayInputStream( outStream.toByteArray());
			

			//String result = Utility.getStringFromInputStream(is);
			//System.out.println(result);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
