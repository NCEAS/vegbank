/*
 *	'$RCSfile: PlantTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-12-10 19:37:40 $'
 *	'$Revision: 1.3 $'
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

import java.util.Collections;
import java.util.Vector;

import org.vegbank.common.Constants;
import org.vegbank.common.model.*;

import junit.framework.TestCase;

/**
 * @author farrell
 */

public class PlantTest extends TestCase implements Constants
{

	/**
	 * Constructor for PlantTest.
	 * @param arg0
	 */
	public PlantTest(String arg0)
	{
		super(arg0);
	}

	public void testCompareTo()
	{
		Plant plantA = new Plant();
		Plant plantB = new Plant();
		Plant plantC = new Plant();
		plantA.setClassLevel(PLANT_CLASS_HYBRID);
		plantB.setClassLevel(PLANT_CLASS_GENUS);
		plantC.setClassLevel(PLANT_CLASS_SPECIES);
		Vector list = new Vector();
		list.add(plantA);
		list.add(plantB);
		list.add(plantC);
		Collections.sort(list);
		
		Plant plant0 = (Plant) list.elementAt(0);
		Plant plant1 = (Plant) list.elementAt(1);
		Plant plant2 = (Plant) list.elementAt(2);
		assertEquals( PLANT_CLASS_GENUS, plant0.getClassLevel() );	
		assertEquals( PLANT_CLASS_SPECIES, plant1.getClassLevel() );
		assertEquals( PLANT_CLASS_HYBRID, plant2.getClassLevel() );	
		
	}

}
