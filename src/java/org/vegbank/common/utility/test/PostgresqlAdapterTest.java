/**
 *  '$RCSfile: PostgresqlAdapterTest.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *  Purpose: To test the DBinsertPlotSource class by JUnit
 *  Authors: @@
 *  Release: @@
 *
 *  '$Author: anderson $'
 *  '$Date: 2003-12-10 19:37:40 $'
 *  '$Revision: 1.4 $'
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

import org.vegbank.common.dbAdapter.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit test for testing Step class processing
 */
public class PostgresqlAdapterTest extends TestCase
{
		
  /**
   * Constructor to build the test
   *
   * @param name the name of the test method
   */
  public PostgresqlAdapterTest(String name)
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
    return new TestSuite(PostgresqlAdapterTest.class);
  }
  /**
   * Run an initial test that always passes to check that the test
   * harness is working.
   */
  public void initialize()
  {
    assertEquals(1,1);
  }


  public void testgetSequenceColumnName() {
    PostgresqlAdapter pa = new PostgresqlAdapter();
    assertEquals("this_that_seq", pa.getSequenceColumnName("this", "that") );
    // 32 char 
    assertEquals("12345_6789ABCDEFGHIJ0123456_seq", pa.getSequenceColumnName("12345", "6789ABCDEFGHIJ01234567") );
    // 33 char 
    assertEquals("12345_6789ABCDEFGHIJ0123456_seq", pa.getSequenceColumnName("12345", "6789ABCDEFGHIJ012345678") );
    // many char 
    assertEquals("12345_6789ABCDEFGHIJ0123456_seq", pa.getSequenceColumnName("12345", "6789ABCDEFGHIJ0123456789ABCDEFGHIJK") );
		// pk and table name are too long
		assertEquals("here_is_a_ver_here_is_a_ver_seq", pa.getSequenceColumnName("here_is_a_very_long_table_name", "here_is_a_very_long_table_name_id") );
		// tablename is too long
		assertEquals("A23456789012345678901_A2345_seq", pa.getSequenceColumnName( "A2345678901234567890123456", "A2345") );
  }
  
	public void testGetSequenceColumnNameTest() {  
		PostgresqlAdapter pa = new PostgresqlAdapter();
		assertEquals("this_seq", pa.getSequenceColumnName("this") );
		// 32 char 
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ1_seq", pa.getSequenceColumnName("ABCDEFGHIJKLMNOPQRSTUVWXYZ1") );
		// 33 char 
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ1_seq", pa.getSequenceColumnName("ABCDEFGHIJKLMNOPQRSTUVWXYZ12") );
		// 40 char 
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ1_seq", pa.getSequenceColumnName("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") );
								
	}

}
