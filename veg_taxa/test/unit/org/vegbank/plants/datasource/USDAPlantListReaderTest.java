/**
 *  '$RCSfile: USDAPlantListReaderTest.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *  Purpose: To test the DBinsertPlotSource class by JUnit
 *  Authors: @@
 *  Release: @@
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-03-07 22:22:43 $'
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

package org.vegbank.plants.datasource;

import java.io.StringReader;
import java.util.AbstractList;

import org.vegbank.common.Constants;
import org.vegbank.common.model.Plant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * A JUnit test for testing Step class processing
 */
public class USDAPlantListReaderTest extends TestCase implements Constants
{

	/**
	 * Constructor to build the test
	 *
	 * @param name the name of the test method
	 */
	public USDAPlantListReaderTest(String name)
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
		return new TestSuite(USDAPlantListReaderTest.class);
	}
	/**
	 * Run an initial test that always passes to check that the test
	 * harness is working.
	 */
	public void initialize()
	{
		assertEquals(1, 1);
	}

	public void testReadSinglePlantSpecies()
	{
		StringReader sr =
		new StringReader("\"ANMI3\",\"Antennaria microphylla Rydb. \",\"littleleaf pussytoes\",\"Asteraceae\"");
		PlantListReader plr = new USDAPlantListReader(sr);
		AbstractList plantList = plr.getAllPlants();
		// Get the first plant
		Plant plant = (Plant) plantList.get(0);
		assertEquals("Antennaria microphylla Rydb.",plant.getScientificName());
		assertEquals("Rydb.", plant.getAuthor());
		assertEquals("Antennaria microphylla", plant.getScientificNameNoAuthors());
		assertEquals("ANMI3", plant.getCode());
		assertEquals(PLANT_CLASS_SPECIES, plant.getClassLevel());
		assertEquals("Antennaria", plant.getParentName() );
		// Need more asserts
	}

	public void testReadSinglePlantVariety()
	{
		StringReader sr =
					new StringReader(
						"\"ABLAA\",\"Abies lasiocarpa (Hook.) Nutt. var. arizonica (Merriam) Lemmon \",\"corkbark fir\",\"Pinaceae\"");
		PlantListReader plr = new USDAPlantListReader(sr);
		AbstractList plantList = plr.getAllPlants();
		// Get the first plant
		Plant plant = (Plant) plantList.get(0);
		assertEquals(
			"Abies lasiocarpa (Hook.) Nutt. var. arizonica (Merriam) Lemmon",
			plant.getScientificName());
		assertEquals("(Merriam) Lemmon", plant.getAuthor());
		assertEquals("Abies lasiocarpa var. arizonica", plant.getScientificNameNoAuthors());
		assertEquals("ABLAA", plant.getCode());
		assertEquals(PLANT_CLASS_VARIETY, plant.getClassLevel());
		assertEquals("Abies lasiocarpa", plant.getParentName() );
	}
	
	public void testReadSinglePlantSubSpecies()
	{
		StringReader sr =
					new StringReader(
						"\"ABNAC2\",\"Abronia nana S. Wats. ssp. covillei (Heimerl) Munz \",\"=Abronia nana var. covillei\",\"Nyctaginaceae\""
						);
		PlantListReader plr = new USDAPlantListReader(sr);
		AbstractList plantList = plr.getAllPlants();
		// Get the first plant
		Plant plant = (Plant) plantList.get(0); 
		assertEquals(
			"Abronia nana S. Wats. ssp. covillei (Heimerl) Munz",
			plant.getScientificName());
		assertEquals("(Heimerl) Munz", plant.getAuthor());
		assertEquals("Abronia nana ssp. covillei", plant.getScientificNameNoAuthors());
		assertEquals("ABNAC2", plant.getCode());
		assertEquals(PLANT_CLASS_SUBSPECIES, plant.getClassLevel());
		assertEquals("Abronia nana", plant.getParentName() );
	}
	
	public void testReadSinglePlantGenus()
	{
			StringReader sr =
						new StringReader(
							"\"ISOCA\",\"Isocarpha R. Br. \",\"pearlhead\",\"Asteraceae\""
						);
			PlantListReader plr = new USDAPlantListReader(sr);
			AbstractList plantList = plr.getAllPlants();
			// Get the first plant
			Plant plant = (Plant) plantList.get(0); 
			//System.out.println(plant.getScientificName() + "\n" + plant.getClassLevel() );
			assertEquals("Isocarpha R. Br.",plant.getScientificName());
			assertEquals("Br.", plant.getAuthor());
			assertEquals("Isocarpha R.", plant.getScientificNameNoAuthors());
			assertEquals("ISOCA", plant.getCode());
			assertEquals(PLANT_CLASS_GENUS, plant.getClassLevel());
			assertEquals("", plant.getParentName() );
		}
	
	public void testReadSinglePlantHybrid()
	{
			StringReader sr =
						new StringReader(
							"\"ADTR3\",\"Adiantum ×tracyi C.C. Hall ex W.H. Wagner \",\",\"Pteridaceae\""
						);
			PlantListReader plr = new USDAPlantListReader(sr);
			AbstractList plantList = plr.getAllPlants();
			// Get the first plant
			Plant plant = (Plant) plantList.get(0); 
			//System.out.println(plant.getScientificName() + "\n" + plant.getClassLevel() );
			assertEquals("Adiantum ×tracyi C.C. Hall ex W.H. Wagner",plant.getScientificName());
			assertEquals("C.C. Hall ex W.H. Wagner", plant.getAuthor());
			assertEquals("Adiantum ×tracyi", plant.getScientificNameNoAuthors());
			assertEquals("ADTR3", plant.getCode());
			assertEquals(PLANT_CLASS_HYBRID, plant.getClassLevel());
			assertEquals("Adiantum", plant.getParentName() );
		}
	

	public void testLoadEmptyString()
	{
		StringReader sr = new StringReader("");
		PlantListReader plr = new USDAPlantListReader(sr);
		AbstractList plantList = plr.getAllPlants();
	}
	
}
