/*
 *	'$RCSfile: CompositeRequestParamUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-12-06 18:49:35 $'
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
import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tools for improving Java's boring handling of 
 * servlet request parameters.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2004-12-06 18:49:35 $
 */

public class CompositeRequestParamUtil{

	private static final Log log = LogFactory.getLog(RequestParameters.class);
	private ServletRequest request = null;


	public CompositeRequestParamUtil(ServletRequest r) {
		request = r;
	}


	/**
	 * Given the top level parent name of a composite
	 * request param, this method makes a map of its
	 * children and puts it in the request.
	 */
	public void parseIntoRequest(String parent) {
		Enumeration theFields = request.getParameterNames();
		String fullKey;
		int firstDot;

		log.debug("parsing one parent into request: " + parent);
		while (theFields.hasMoreElements()) {
			fullKey = (String)theFields.nextElement();
			if (fullKey.startsWith(parent)) {
				try {
					firstDot = fullKey.indexOf(".");
					if (firstDot != -1) {
						// run the parser on the children
						request.setAttribute(
								fullKey.substring(0, firstDot).toUpperCase(), 
								parseKeyIntoMap(fullKey));
					}
				} catch (Exception ex) {
					// oh well
					log.error("Problem while parsing param " + parent + " into map", ex);
				}
			}
		}
	}


	/**
	 * Translates request parameters that look like multi-dimensional
	 * array notation into fractal mappings, then sets those mappings
	 * back into the request as attributes.
	 *
	 * <input name="map_name.field1">
	 * <input name="map_name.field1.subfield1">
	 * <input name="map_name.field1.subfield2">
	 * <input name="another_map.some_field">
	 */
	public void parseAllIntoRequest() {
		int firstDot;
		String fullKey;
		Enumeration theFields = request.getParameterNames();

		while (theFields.hasMoreElements()) {
			try {
				fullKey = (String)theFields.nextElement();

				firstDot = fullKey.indexOf(".");
				if (firstDot == -1) {
					// this param has no children
					log.debug("skipping single parent: " + fullKey);

				} else {
					// run the parser on the children
					request.setAttribute(
							fullKey.substring(0, firstDot).toUpperCase(), 
							parseKeyIntoMap(fullKey));
				}

			} catch (Exception ex) {
				// oh well
				log.error("Problem while parsing params into map", ex);
			}
		}

	}


	/**
	 * Creates a mapping of parent.child.grandchild request
	 * parameters.  Children are accessed by calling get
	 * on a parent map using the parent's name in upper case.  
	 * This will return either another Map, a String or
	 * a String[].  
	 * 
	 * Map m = parseKeyIntoMap("parent.child.grandchild");
	 * m = (Map)m.get("PARENT");
	 * m = (Map)m.get("CHILD");
	 * String s = (String)m.get("grandchild");
	 *
	 */
	private Map parseKeyIntoMap(String fullKey) {
		if (Utility.isStringNullOrEmpty(fullKey)) {
			return null;
		}

		Map m = new HashMap();
		int firstDot = fullKey.indexOf(".");
		if (firstDot == -1) {
			// this param has no children
			log.debug("single parent: " + fullKey);
			m.put(fullKey, getRequestParameter(fullKey));

		} else {
			// run the parser on the children
			parseKeyIntoMap(fullKey, fullKey.substring(firstDot+1), m);
		}

		return m;
	}


	/**
	 * Workhorse.  Don't ever call this directly.
	 */
	private Map parseKeyIntoMap(String fullKey, String rhs, Map siblings) {
		log.debug("Parsing children of: " + rhs);
		int firstDot = rhs.indexOf(".");

		if (firstDot != -1 && rhs.length() > firstDot+1) {
			String lhs = rhs.substring(0, firstDot);
			rhs = rhs.substring(firstDot+1);
			siblings.put(lhs, parseKeyIntoMap(fullKey, rhs, new HashMap()));

		} else {
			// no more dots
			// store the parameter value(s) with its siblings
			siblings.put(rhs.toUpperCase(), getRequestParameter(fullKey));
		}

		return siblings;
	}


	/**
	 * Returns the single atomic String or String[] from the request.
	 * Only returns an array if there's more than one value for the param.
	 */
	public Object getRequestParameter(String key) {
		String[] paramValue = request.getParameterValues(key);
		if (!Utility.isArrayNullOrEmpty(paramValue)) {
			if (paramValue.length == 1) {
				return paramValue[0];
			} else {
				return paramValue;
			}
		} 

		return "";
	}

}
