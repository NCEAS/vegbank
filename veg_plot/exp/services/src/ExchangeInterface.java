/**
 *    '$RCSfile: ExchangeInterface.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-04 00:37:11 $'
 *   '$Revision: 1.2 $'
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

public interface ExchangeInterface
  {
		/**
		 * method that takes a plant name and returns
		 * a summary of all the plots in vegbank that 
		 * contain that plant
		 *
		 * @param plantName -- the name of the plant 
		 * @return vector containing a hashtable -- with the following elements:
		 * 1] accessionNumber
		 * 2] latitude
		 * 3] longitude
		 * 4] state
		 */
		Vector getPlotAccessionNumber( String plantName );
  }
