/*
 *	'$RCSfile: CompositeRequestParamUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-12-07 02:53:45 $'
 *	'$Revision: 1.2 $'
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
 * @version $Revision: 1.2 $ $Date: 2004-12-07 02:53:45 $
 */

public class CompositeRequestParamUtil{

	private static final String LIST_KEY = ":LIST:";

	private static final Log log = LogFactory.getLog(RequestParameters.class);
	private ServletRequest request = null;
	private HashMap paramMap = null;


	public CompositeRequestParamUtil(ServletRequest r) {
		request = r;
		paramMap = new HashMap();
	}


	/**
	 * Given the top level parent name of a composite
	 * request param, this method makes a map of its
	 * children and puts it in the request.
	 */
	public void parseParent(String parent) {
		Enumeration theFields = request.getParameterNames();
		String fullKey;

		log.debug("parsing one parent into request: " + parent);
		while (theFields.hasMoreElements()) {
			fullKey = (String)theFields.nextElement();
			if (fullKey.startsWith(parent)) {
				try {
					// run the parser on the children
					addTopLevelParent(fullKey, parseKey(fullKey));

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
	/*
	public void parseAllParents() {
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
	 * Map m = parseKey("parent.child.grandchild");
	 * m = (Map)m.get("PARENT");
	 * m = (Map)m.get("CHILD");
	 * String s = (String)m.get("grandchild");
	 *
	 */
	private Map parseKey(String fullKey) {
		if (Utility.isStringNullOrEmpty(fullKey)) {
			return null;
		}

		Map m = new HashMap();
		int firstDot = fullKey.indexOf(".");
		if (firstDot == -1) {
			// this param has no children
			log.debug("non-composite param: " + fullKey);
			m.put(fullKey, getRequestParameter(fullKey));

		} else {
			// run the parser on the children
			String thisKey = fullKey.substring(0, firstDot);
			String childKey = fullKey.substring(firstDot+1)

			if (Utility.isNumeric(thisKey)) {
				m.put(thisKey, parseChildren(
							fullKey, childKey, new ArrayList()));
			} else {
				m.put(thisKey, parseChildren(
							fullKey, childKey, new HashMap()));
			}
		}

		return m;
	}


	/**
	 * Workhorse.  Siblings instance must be either a Map or List 
	 * when this method is called.
	 * @param fullKey the entire key from beginning to end
	 * @param childKey the part of the entire key which contains
	 *  the remaining portion after the current parent key.
	 * @param siblings an ArrayList or HashMap instance that contains 
	 *  the current child's siblings; i.e. values at the same lateral level
	 */
	private Object parseChildren(String fullKey, String childKey, Map siblings) {

		log.debug("PARSING " + childKey + " OF " + fullKey);
		int firstDot = childKey.indexOf(".");

		if (firstDot != -1 && childKey.length() > firstDot+1) {
			// there are more children
			String thisKey = childKey.substring(0, firstDot);
			childKey = childKey.substring(firstDot+1);

			// children always live in a Map at least
			insertValue(siblings, thisKey.toUpperCase(), 
					parseChildren(fullKey, childKey, new HashMap()));

			if (Utility.isNumeric(thisKey)) {
				// Need to make a numeric list too
				IndexedValue iv = new IndexedValue(thisKey, 

			} 


		} else {
			// no more dots
			// store the parameter value(s) with its siblings
			insertValue(siblings, thisKey, getRequestParameter(fullKey));
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
	 * Given a listOrMap, either puts a value using the Map key or
	 * adds a new IndexedValue instance at the end of the List.
	 */
	private	void insertValue(Object listOrMap, String key, Object value) {
		if (listOrMap instanceof ArrayList) {
			listOrMap.add( new IndexedValue(key, value) );

		} else if (listOrMap instanceof HashMap) {
			listOrMap.put(key, value);
		}
	}


	/**
	 * Top level parents are always HashMaps and always use
	 * an upper-case key.  Type of child is determined by first child key.
	 * If child key is numeric, type is ArrayList, else HashMap.
	 */
	private void addTopLevelParent(String fullKey, Object child) {
		int firstDot = fullKey.indexOf(".");
		if (firstDot == -1) {
			return;
		}

		String parentKey = fullKey.substring(0, firstDot).toUpperCase();
		String childKey = fullKey.substring(firstDot+1);

		Map parent = paramMap.get(parentKey);
		if (parent == null) { 
			parent = new HashMap();
		} 

		parent.put(childKey, child);

		paramMap.put(parentKey, parent);
		//request.setAttribute(parentKey, parent);
	}


	/**
	 *
	 */
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


	/**
	 *
	 */
	public List getList(String canon) {
		return (List)getByPath(canon, true);
	}


	/**
	 *
	 */
	public Map getMap(String canon) {
		return (Map)getByPath(canon, false);
	}


	/**
	 * Workhorse retrieval method.
	 */
	private Object getByPath(String canon, boolean returnList) {
		if (Utility.isStringNullOrEmpty(canon)) {
			return null;
		}

		int dotPos = canon.indexOf(".");
		if (dotPos == -1 || canon.length() < dotPos+2) {
			// must have children
			return null;
		}

		String parentKey = canon.substring(0, dotPos);
		Map m = (Map)paramMap.get(parentKey);
		if (m == null) {
			// padre mapa no existe
			log.debug("Could not find parent map named " + parentKey);
			return null;
		}

		// canon = xwhereParams.0.0
		String rhs = canon.substring(dotPos+1);

		// rhs = 0.0
		dotPos = rhs.indexOf(".");
		// dotPos = 1
		// length = 3

		while (dotPos != -1 && rhs.length() > dotPos+1) {
			// this child is not the last
			childKey = rhs.substring(0, dotPos);

			////////////////////////////////////////
			// Assume that all keys use a Map
			////////////////////////////////////////
			m = (Map)m.get(childKey);
			if (m == null) {
				return null;
			}

			rhs = rhs.substring(dotPos+1);
			dotPos = rhs.indexOf(".");
		}

		// found the right child
		if (returnList) {
			return (List)m.get(LIST_KEY);
		} else {
			return m;
		}
	}


	/**
	 *
	 */
	private class IndexedValue {
		public int index;
		public Object value;

		public IndexedValue(int i, Object o) {
			init(i, o);
		}

		public IndexedValue(String s, Object o) {
			try {
				init(Integer.parseInt(s), o);
			} catch (Exception ex) {
				// fiddly dee
			}
		}

		private init(int i, Object o) {
			index = i;
			value = o;
		}
	}

}
