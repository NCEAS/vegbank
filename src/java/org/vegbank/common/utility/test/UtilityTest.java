/**
 *  '$RCSfile: UtilityTest.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *  Purpose: To test the DBinsertPlotSource class by JUnit
 *  Authors: @@
 *  Release: @@
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-02-27 01:05:12 $'
 *  '$Revision: 1.1 $'
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit test for testing Step class processing
 */
public class UtilityTest extends TestCase
{
		
  /**
   * Constructor to build the test
   *
   * @param name the name of the test method
   */
  public UtilityTest(String name)
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
    return new TestSuite(UtilityTest.class);
  }
  /**
   * Run an initial test that always passes to check that the test
   * harness is working.
   */
  public void initialize()
  {
    assertEquals(1,1);
  }


  public void testEscapeCharacter() {
  	
  	String inputA = "this and ' $that";
		String expectedResultA = "this and \\' \\$that";
		String resultA = Utility.escapeCharacters(inputA); 
		assertEquals(expectedResultA, resultA);
				
		String inputB = "aa'aa^^";
		String expectedResultB = "aa\\'aa\\^\\^";
		String resultB = Utility.escapeCharacters(inputB);
		assertEquals(expectedResultB, resultB);   
				
		String inputC = "$aaa'aa'aa''a^";
		String expectedResultC = "\\$aaa\\'aa\\'aa\\'\\'a\\^";
		String resultC = Utility.escapeCharacters(inputC);
		assertEquals(expectedResultC, resultC);  
		
		String inputD = null;
		String expectedResultD = null;
		String resultD = Utility.escapeCharacters(inputD);
		assertEquals(expectedResultD, resultD);  

  }

}
