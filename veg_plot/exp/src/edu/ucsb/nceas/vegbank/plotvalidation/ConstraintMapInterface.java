/**
 *  '$RCSfile: ConstraintMapInterface.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-12-23 16:21:35 $'
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

import edu.ucsb.nceas.vegbank.plotvalidation.PlotValidationReport;

public interface ConstraintMapInterface
{
  /**
   * returns the size (ie., the number of elements in the map.
   * @return -- size the integer size of the map
   */
  public int getMapSize();
  /**
   * returns the rule that governs the way the attribute is to be 
   * constrained by the constraing vocabulary
   * @param mapLevel -- the level in the map corresponding to the attribute 
   * of interest.
   * 
   */
  public String getConstraintRule(int mapLevel);
  /**
   * returns the attribute name corresponding to the datasource 
   * element -- this is the database attribute
   * @param mapLevel -- the level in the map corresponding to the attribute 
   * of interest.
   */
  public String getDBAttribute(int mapLevel);
  /**
   * returns the table that the attribute is stored in in the 
   * database 
   * @param mapLevel -- the level in the map corresponding to the attribute 
   * of interest.
   */
  public String getDBTable(int mapLevel);
  /**
   * returns the names of the method input parameteres to be 
   * used to access the method in the 'PlotDataSource' 
   * @param mapLevel -- the level in the map corresponding to the attribute 
   * of interest.
   */
  public String getMethodParams(int mapLevel);
  /**
   * method that returns the name of the method that should be called 
   * from the 'PlotDataSource' class to access the corresponding plot
   * attribute
   * @param mapLevel -- the level in the map corresponding to the attribute 
   * of interest.
   */
  public String getMethodName(int mapLevel);
}
