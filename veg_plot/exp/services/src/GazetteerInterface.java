
/**
 *    '$RCSfile: GazetteerInterface.java,v $'
 *    Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *   '$Date: 2003-08-10 22:44:50 $'
 *   '$Revision: 1.1 $'
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

import java.util.Hashtable;

/**
 * Interface describing the functionality of the Gazetteer-related needs
 * implicit in the ecological functions described in the ExchangeInterface.
 * @see ExchangeInterface
 */
public interface GazetteerInterface {
  
  /**
   * method that takes as input the x,y (long, latitude) location and returns
   * the nearest place to that location
   * @param longitude -- the longitudinal geocoordinate for a location
   * @param latitude -- the latitudinal geocoordinate for a location
   * @return place -- a hashtable structure with the following name-value
   * pairs:<br><br>
   * gazateer_pk      | integer <br>               
   * statealphacode   | character varying(50)  <br> 
   * featurename      | character varying(50)  <br>
   * featuretype      | character varying(50)  <br>
   * countyname       | character varying(50)  <br>
   * statenumbercode  | character varying(50)  <br>
   * countynumbercode | character varying(50)  <br>
   * primarylatitude  | numeric(30,6)          <br>
   * primarylongitude | numeric(30,6)          <br>
   * elevation        | numeric(30,6)          <br>
   * population       | numeric(30,6)          <br>
   */
  public Hashtable lookupNearestPlace(double longitude, double latitude );
  
}
