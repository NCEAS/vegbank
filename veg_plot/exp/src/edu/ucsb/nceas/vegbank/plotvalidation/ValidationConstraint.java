/**
 *  '$RCSfile: ValidationConstraint.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-12-13 17:59:08 $'
 * '$Revision: 1.1 $'
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

package edu.ucsb.nceas.vegbank.plotvalidation;
import java.io.*;
import java.lang.*;
import java.util.*;


public class ValidationConstraint
{
	
	/**
	 *  method that takes the table name and attribute name and returns the 
	 *  vector containing the list of available elements
	 * @param table -- the name of the table
	 * @param attribute -- the name of the attribute
	 */
	 public Vector getConstraints(String table, String attribute)
	 {
		 Vector v = new Vector();
		 v.addElement("constraint");
		 return(v);
	 }
}
