/*
 *	'$RCSfile: CompositeRequestParamUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005/05/05 20:22:31 $'
 *	'$Revision: 1.7 $'
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
 * @version $Revision: 1.7 $ $Date: 2005/05/05 20:22:31 $
 */

public class CompositeRequestParamUtil {

	public static final String CRP_PARAM_DELIM = "_";

	private static final Log log = LogFactory.getLog(CompositeRequestParamUtil.class);
	private ServletRequest request = null;
	private HashMap composParams = null;


	public CompositeRequestParamUtil(ServletRequest r) {
		request = r;
		composParams = new HashMap();
	}


	/**
	 * Given the top level parent name of a composite
	 * request param, this method makes a map of its
	 * children and puts it in the request.
	 */
	public boolean parseParent(String parent) {
		log.debug("parsing parents that start with: " + parent);

		boolean rv = false;
		Enumeration theFields = request.getParameterNames();
		String fullPath;

		while (theFields.hasMoreElements()) {
			fullPath = (String)theFields.nextElement();
			if (fullPath.startsWith(parent)) {
				try {
					// run the parser on the children
					rv = parseFromTop(fullPath);
					/*
					Map m = parseFromTop(fullPath);
					if (m != null) {
						rv = true;
						addTopLevelParent(fullPath, m);
					}
					*/

				} catch (Exception ex) {
					// oh well
					log.error("Problem while parsing param " + parent + " into map", ex);
				}
			}
		}
		return rv;
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
	/*
	public void parseAllParents() {
		int firstDot;
		String fullPath;
		Enumeration theFields = request.getParameterNames();

		while (theFields.hasMoreElements()) {
			try {
				fullPath = (String)theFields.nextElement();

				firstDot = fullPath.indexOf(CRP_PARAM_DELIM);
				if (firstDot == -1) {
					// this param has no children
					log.debug("skipping single parent: " + fullPath);

				} else {
					// run the parser on the children
				}

			} catch (Exception ex) {
				// oh well
				log.error("Problem while parsing params into map", ex);
			}
		}

	}
	*/


	/**
	 * Creates a mapping of parent.child.grandchild request
	 * parameters.  Children are accessed by calling get
	 * on a parent map using the parent's name in upper case.
	 * This will return either another Map, a String or
	 * a String[].
	 *
	 * Map m = parseFromTop("parent.child.grandchild");
	 * m = (Map)m.get("PARENT");
	 * m = (Map)m.get("CHILD");
	 * String s = (String)m.get("grandchild");
	 *
	 */
	private boolean parseFromTop(String fullPath) {
		if (Utility.isStringNullOrEmpty(fullPath)) {
			return false;
		}

		Map m;
		log.debug("Searching for delim " + CRP_PARAM_DELIM + " in " + fullPath);
		int firstDot = fullPath.indexOf(CRP_PARAM_DELIM);
		if (firstDot == -1) {
			// this param has no children
			log.debug("non-composite param: " + fullPath);
			//m.put(fullPath, getRequestParameter(fullPath));
			composParams.put(fullPath.toLowerCase(), getRequestParameter(fullPath));
			return false;

		} else {
			// run the parser on the children
			String thisKey = fullPath.substring(0, firstDot).toUpperCase();
			String childKey = fullPath.substring(firstDot+1);

			m = (Map)composParams.get(thisKey);
			if (m == null) {
				m = new HashMap();
			}

			//log.debug("putting (top-level) children with key: " + thisKey);
			composParams.put(thisKey,
					parseChildren(fullPath, childKey, m));
			log.info("Current top level mappings: " + composParams.toString() + "\n");

			return true;
		}
	}


	/**
	 * Workhorse.  Siblings is a Map of other properties that already
	 * exist at the same lattice as the given child.
	 *
	 * @param fullPath The entire key from beginning to end.
	 * @param childKey The part of the entire key which contains
	 *  the remaining portion after the current parent key.
	 * @param siblings A HashMap instance that contains
	 *  the current child's siblings; i.e. values at the same lateral level
	 *
	 * @return
	 */
	private Map parseChildren(String fullPath, String childKey, Map siblings) {

		log.debug("parseChildren: " + childKey);

		int firstDot = childKey.indexOf(CRP_PARAM_DELIM);
		Map childMap;

		if (firstDot != -1 && childKey.length() > firstDot+1) {
			// there are more children
			String thisKey = childKey.substring(0, firstDot).toUpperCase();
			childKey = childKey.substring(firstDot+1);
			log.debug("parsing children of " + thisKey + " from " + fullPath);

			childMap = (Map)siblings.get(thisKey);
			if (childMap == null) {
				// use the extant one if there
				childMap = new HashMap();
				//log.debug("This is the first parsed child");
			}

			log.debug("adding children to siblings of: " + thisKey);
			insertValue(siblings, thisKey.toUpperCase(),
					parseChildren(fullPath, childKey, childMap));

		} else {
			// no more dots
			// store the parameter value(s) with its siblings
			log.debug("NO MORE CHILDREN; adding one value to siblings of: " + childKey);
			insertValue(siblings, childKey.toLowerCase(), getRequestParameter(fullPath));
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

	/**
	 * Given a container, either puts a value using the Map key or
	 * adds a new IndexedValue instance at the end of the List.
	 */
	private	void insertValue(Map container, String key, Object value) {
		//log.debug("Inserting value with key: " + key + " :: VALUE IS " + value.toString());
		container.put(key, value);
	}


	/**
	 * Top level parents are always HashMaps and always use
	 * an upper-case key.  Type of child is determined by first child key.
	 * If child key is numeric, type is ArrayList, else HashMap.
	 */
	private void addTopLevelParent(String fullPath, Map newParentMap) {
		int dotPos = fullPath.indexOf(CRP_PARAM_DELIM);
		if (dotPos == -1) {
			log.debug("Not adding top level for: " + fullPath);
			return;
		}

		String parentKey = fullPath.substring(0, dotPos).toUpperCase();

		Map oldParentMap = (Map)composParams.get(parentKey);
		if (oldParentMap == null) {
			log.debug(parentKey + " did not exist in top level yet");
			oldParentMap = new HashMap();
		}

		/*
		String childKey = fullPath.substring(dotPos+1);
		dotPos = childKey.indexOf(CRP_PARAM_DELIM);
		if (dotPos != -1) {
			// this child is a parent, so make it upper case
			childKey = childKey.substring(0, dotPos).toUpperCase();
		}
		//log.debug("Adding child: " + childKey);
		//parentMap.put(childKey, child);
		*/

		log.debug("^^^^^^ Adding top level parent: " + parentKey);
		composParams.put(parentKey, newParentMap);

		log.debug("CURRENT TOP LEVEL MAPPINGS: " + composParams.toString() + "\n");

	}


	/**
	 * Builds a sorted array of values with numeric keys.
	 */
	public Object[] buildObjectArray(Map m) {
		if (m == null) { return null; }
		log.debug("buildObjectArray: " + m.toString());
		ArrayList l = new ArrayList();

		Arrays.sort(m.keySet().toArray());  // this doesnt seem to do anything (MTL 15-DEC-2010)
		log.debug("sorted? : " + m.keySet().toString());

		//Iterator keys = m.keySet().iterator();
                // new method for sorting keys.  this one works: MTL 15-DEC-2010
                Vector v = new Vector(m.keySet());
                Collections.sort(v);
                Iterator keys = v.iterator();

		while (keys.hasNext()) {
		    String keyName = (String)keys.next();
			log.debug("Testing numerocity of key: " + keyName);
			if (Utility.isNumeric(keyName)) {
				Object o = null;
				try {
					o = m.get(keyName);

					if (o instanceof String[]) {
						//l.add(implodeArray((String[])o, ",", "", true));
						l.add((String[])o);
					} else {
						l.add((String)o);
					}

				} catch (Exception ex) {
					// don't worry about non-string values
					log.debug("Could not use String value since type is: " +
							o.getClass().getName());
				}
			}
		}

		if (l.size() == 0) {
			return null;
		}

		//log.debug("about to return this array: " + l.toString());
		//return (String[])l.toArray(new String[1]);
		log.debug("Returning array: " + l.toString());
		return l.toArray();
	}

	/**
	 *
	 */
	/*
	public String[] getStringArray(String canon) {
		return buildStringArray(getMap(canon));
	}
	*/

	/**
	 *
	 */
	public Object[] getObjectArray(String canon) {
		return buildObjectArray(getMap(canon));
	}

		/*
	public String[] getStringArray(String canon) {
		List l = getList(canon);
		if (l == null) {
			return null;
		}

		Iterator it = l.iterator();
		int listSize = l.size();
		String[] arr = new String[listSize];

		while (it.hasNext()) {
			IndexedValue iv = (IndexedValue)it.next();
			if (iv.index < listSize && iv.index > -1) {
				try {
					arr[iv.index] = (String)iv.value;

				} catch (ClassCastException cce) {
					log.warn("Value at " + iv.index +
							" is not a String; while getting " + canon);
				}
			} else {
				log.error(iv.index +
						" is not an allowed index while getting " + canon);
			}
		}

		return arr;
	}
	*/


	/**
	 *
	 */
	public Map getMap(String canon) {
		log.debug("getMap: " + canon);
		if (Utility.isStringNullOrEmpty(canon)) {
			return null;
		}

		int dotPos = canon.indexOf(CRP_PARAM_DELIM);
		if (dotPos == -1 || canon.length() < dotPos+2) {
			//log.debug("getting top level map");
			return (Map)composParams.get(canon.toUpperCase());
		}

		String rhs, childKey, parentKey;
		parentKey = canon.substring(0, dotPos).toUpperCase();
		//log.debug("attempting to get parent map named: " + parentKey);
		Map m = (Map)composParams.get(parentKey);
		if (m == null) {
			// padre mapa no existe
			//log.debug("Could not find parent map named " + parentKey);
			return null;
		}

		// e.g. xwhereParams.commname.0
		rhs = canon;

		do {
			rhs = rhs.substring(dotPos+1);

			dotPos = rhs.indexOf(CRP_PARAM_DELIM);

			if (dotPos == -1) {
				childKey = rhs;
			} else {
				// this child is not the last
				childKey = rhs.substring(0, dotPos).toUpperCase();
			}

			try {
				m = (Map)m.get(childKey);
			} catch (ClassCastException cce) {
				Object o = m.get(childKey);
				log.error("Problem while getting child map named '" +
						childKey + "' from parent map: " + m.toString() +
						"\n\nCould not cast a " + o.getClass().getName() +
						" to a Map.  Its value was: " + o.toString() +
						"\nCheck the '" + childKey + "' parameter for misconfiguration.\n\n");
				m = null;
			}

			if (m == null) { return null; }

		} while (dotPos != -1 && rhs.length() > dotPos+1);

		return m;
	}


	/**
	 * Make a string out of array pieces.
	 */
	private String implodeArray(String[] sarr, String glue, String wrapper, boolean skipEmpties) {
		StringBuffer sb = new StringBuffer(sarr.length*10);
		boolean first = true;
		for (int i=0; i<sarr.length; i++) {

			if (skipEmpties && Utility.isStringNullOrEmpty(sarr[i])) {
				continue;
			}

			if (first) { first = false; }
			else { sb.append(glue); }

			sb.append(wrapper).append(sarr[i]).append(wrapper);
		}
		return sb.toString();
	}
}
