/*
 *	'$RCSfile: RequestParameters.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-11-24 19:29:32 $'
 *	'$Revision: 1.1 $'
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

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tools for improving Java's boring handling of 
 * servlet request parameters.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2004-11-24 19:29:32 $
 */

public class RequestParameters {

	private static final Log log = LogFactory.getLog(RequestParameters.class);

	/**
	 * Translates request parameters that look like multi-dimensional
	 * array notation into fractal mappings, then sets those mappings
	 * back into the request as attributes.
	 *
	 * <input name="map_name[field1]">
	 * <input name="map_name[field1][subfield1]">
	 * <input name="map_name[field1][subfield2]">
	 * <input name="another_map[some_field]">
	 */
	private void parseFormFieldMaps(HttpServletRequest request) {
		int lpos, rpos;
		String fullFieldName, mapKeyFieldName, tempMapName;
		Map formFieldMap = new HashMap();
		Map tempMap;
		Enumeration theFields = request.getParameterNames();

		while (theFields.hasMoreElements()) {
			try {
				fullFieldName = (String)theFields.nextElement();
				Map m = buildMapFromArray(fullFieldName, 
						request.getParameter(fullFieldName));



				lpos = fullFieldName.indexOf('[');
				if (lpos > 0) {
					rpos = fullFieldName.indexOf(']', lpos);
					if (rpos != -1) {
						// extract field name into a map
						tempMapName = fullFieldName.substring(0, lpos);
						mapKeyFieldName = fullFieldName.substring(lpos+1, rpos);

						tempMap = (Map)formFieldMap.get(tempMapName);
						if (tempMap == null) {
							tempMap = new HashMap();
						}
						tempMap.put(mapKeyFieldName, request.getParameter(fullFieldName));

						// this is final goal right here
						formFieldMap.put(tempMapName, tempMap);
					}
				}
			} catch (Exception ex) {
				// oh well
			}
		}

		//((Map)formFieldMap.get("numbers")).get("one")
	}

	/**
	 * Given a string in multi-dimensional array notation,
	 * build a map of maps.
	 */
	private Map buildMapFromArray(String array, String value) {
		return new HashMap();

	}

}
