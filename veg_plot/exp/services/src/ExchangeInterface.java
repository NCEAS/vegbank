/**
 *    '$RCSfile: ExchangeInterface.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.3 $'
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 */
package vegbank.publish;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

/**
 * Interface which defines all the methods which should be exposed via the web
 * services application server (GLUE -- http://themindelectric.com/).  These
 * functions are relevant to the ecological community.
 */
public interface ExchangeInterface {
		 /**
		  * method that takes a plant name and returns
		  * a summary of all the plots in vegbank that 
		  * contain that plant
		  * @param plantName -- the name of the plant 
		  * @return vector containing a hashtable -- with the following elements:
		  * 1] accessionNumber
		  * 2] latitude
		  * 3] longitude
		  * 4] state
		  */
      Vector getPlotAccessionNumber( String plantName );

     /**
      * method that takes as input a plot accession number and returns the
      * nearest place registered in a gazateer 
      * @param plotId -- the plot accession number
      * @return place -- a hashtable representation of a place object, for
      * details on the keys stored in the place object see the
      * GazetteerInterface class.
      */
      Hashtable getNearestPlaceForPlot(String plotId);

     /**
      * method that takes as input the geocoodinates of a location and returns
      * the nearest place registered in a gazetteer
      * @param latitude -- the double value for a latitude 
      * @param longitude -- the double value for a longitude
      * @return place -- a hashtable representation of a place object, for
      * details on the keys stored in the place object see the
      * GazetteerInterface class.
      */
      Hashtable getNearestPlaceForLocation(double latitude, double longitude);
}
