/*
 *	'$RCSfile: Xwhere.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-17 22:23:25 $'
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

package org.vegbank.ui.struts.taglib;


import java.util.*;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.text.Format;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.bean.WriteTag;

import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.CompositeRequestParamUtil;

/**
 * 
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.7 $ $Date: 2005-02-17 22:23:25 $
 */

public class Xwhere {

	private static final Log log = LogFactory.getLog(Xwhere.class);
	private static final String PARAM_DELIM = CompositeRequestParamUtil.PARAM_DELIM;

	private static ResourceBundle sqlResources = 
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");
	private ServletRequest request;
	private VegbankGetTag getTag;
	private String xwhereClause = null;
	private CompositeRequestParamUtil composUtil;

	public Xwhere(VegbankGetTag t, ServletRequest r) {
		getTag = t;
		request = r;
		xwhereClause = null;
		composUtil = new CompositeRequestParamUtil(request);
	}


	/**
	 * Builds xwhere clause if empty, then returns it.
	 */
	public String getXwhereClause() {
		if (Utility.isStringNullOrEmpty(this.xwhereClause)) {
			buildXwhereClause();
		}
		return this.xwhereClause;
	}

	/**
	 * Uses all available xwhere options to build
	 * the proper SQL where clause.
	 */
	private void buildXwhereClause() {

		// it all starts with xwhereParams values.
		composUtil.parseParent("xwhere");
		Object xwParams = getXwhereParamsObject();

		if (xwParams instanceof Map) {
			log.debug("xwp is a MAP");

			Iterator ids = ((Map)xwParams).keySet().iterator();
			while (ids.hasNext()) {

				String id = (String)ids.next();
				appendToClause(getXwhereKeySQL(id), 
						(Object[])((Map)xwParams).get(id), 
						getXwhereGlue(id),
						getXwhereSearch(id),
						getXwhereMatchAny(id));
			}

		} else if (xwParams instanceof Object[]) {
			log.debug("xwp is an ARRAY");
			// An array is used with the xwKey directly
			// It is one dimensional
			appendToClause(getXwhereKeySQL(), (Object[])xwParams, 
					getXwhereGlue(), getXwhereSearch(), getXwhereMatchAny());
		}

		if (Utility.isStringNullOrEmpty(this.xwhereClause) ||
				this.xwhereClause.equals("null")) {
			this.xwhereClause = "";
		} else {
			this.xwhereClause = "(" + this.xwhereClause + ")";
		}
	}

		/*
	public String getXwhereClauseOLD() {
		String xwKey = getTag.getXwhereKey();
		if (Utility.isStringNullOrEmpty(xwKey)) {
			return "";
		}

		String xwClause = sqlResources.getString(xwKey);
		String xwParams = getTag.getXwhereParams();
		String[] xwParamArray;

		if (!Utility.isStringNullOrEmpty(xwParams)) {
			xwParams = xwParams.replaceAll("%20", " ");
		}

		log.debug("xwhereClause: " + xwClause);
		log.debug("xwhereParams: " + xwParams);

		if (Utility.isStringNullOrEmpty(xwClause)) {
			return "";
		}

		boolean isSearch = getTag.getXwhereSearch();

		if (xwParams.indexOf(";") == -1) {
			// only one xwParam
			// format xwClause with xwParams as is
			xwParamArray = new String[1];

			if (isSearch) {
				xwParamArray[0] = getTag.makeStringSearchable(
						xwParams, xwClause, getTag.getXwhereGlue());
			} else {
				xwParamArray[0] = xwParams;
			}

		} else {
			// there are multiple xwParam values
			StringTokenizer stParams = new StringTokenizer(xwParams, ";");
			xwParamArray = new String[stParams.countTokens()];
			int j=0;
			while (stParams.hasMoreTokens()) {
				if (isSearch) {
					xwParamArray[j++] = getTag.makeStringSearchable(
							stParams.nextToken(), xwClause, getTag.getXwhereGlue());
				} else {
					xwParamArray[j++] = stParams.nextToken();
				}
			}
		}

		// expand the entire xwClause
		MessageFormat format = new MessageFormat(xwClause);
		StringBuffer sb = new StringBuffer(xwParamArray.length * 24);
		boolean first = true;
		String[] arr = new String[1];
		for (int i=0; i<xwParamArray.length; i++) {
			if (first) { first = false;
			} else { sb.append(" AND "); }

			if (isSearch) {
				sb.append(xwParamArray[i]);
			} else {
				arr[0] = xwParamArray[i];
				sb.append(format.format(arr));
			}
		}

		return sb.toString();
	}
	*/

	/**
	 * Gets an array of xwhereParam values, or an array of arrays.
	 * Allows for composite request params up to two levels deep.
	 * e.g. xwhereParams.0, xwhereParams.1, xwhereParams.2 or 
	 * xwhereParams.something.0, xwhereParams.something.1, etc. and
	 * xwhereParams.another.0, xwhereParams.something.1 etc.
	 *
	 * Note that the final param key must be an integer and order matters.
	 *
	 * If the non-composite xwhereParams is also set, that value is added
	 * to the end of the returned String[] array.
	 */
    private Object getXwhereParamsObject() {
		String normalXwhereParams = getTag.getXwhereParams();

		log.debug("about to parse composite xwhereParams. . .");
		boolean hasCompositeParams = (composUtil.getMap("XWHEREPARAMS") != null);

		Object[] arr;
		if (!hasCompositeParams) {
			arr = new String[1];
			arr[0] = normalXwhereParams;
			log.debug("returning normal xwhereParams: " + normalXwhereParams);
			return arr;

		} else {
			// look for any properties on the first level
			// e.g.  xwhereParams.0, xwhereParams.1
			log.debug("has composite xwhereParams");
			arr = composUtil.getObjectArray("xwhereParams");

			if (Utility.isArrayNullOrEmpty(arr)) {

				// try digging one level deeper
				// e.g.  xwhereParams.country.0, xwhereParams.country.1
				log.debug("nothing found at root; digging deeper...");
				Map m = composUtil.getMap("xwhereParams");
				if (m == null) { 
					log.debug("no composite mappings for 'xwhereParams'");
					return null; 
				}

				Map xwpMap = new HashMap();
				Iterator keys = m.keySet().iterator();
				log.debug("Got parent map, now getting arrays for each key...");
				while (keys.hasNext()) {

					String keyName = (String)keys.next();
					//log.debug("getting string array for xwhereParams" + PARAM_DELIM + keyName);
					//arr = composUtil.getObjectArray("xwhereParams" + PARAM_DELIM + keyName);
					arr = getXwhereParams(keyName);

					if (!Utility.isArrayNullOrEmpty(arr)) {
						log.debug("adding string array for key: " + keyName);
						xwpMap.put(keyName, arr);
					} else {
						log.debug("string array for " + keyName + " was empty");
					}
				}

				//log.debug("***** got map of xwhereParams: " + xwpMap.toString());
				return xwpMap;

			} else {
				// just return the first level array
				log.debug("using root level xwhereParams mapping");
				return arr;
			}
		}
	}

	/**
	 * Simple method for swapping params.
	 * @param xwKeySQL The SQL fragment that has placeholders {0}
	 * @param xwParams One dimensional array of strings.  If first 
	 *  index is empty, doSwap returns "".
	 */
	private String doSwap(String xwKeySQL, Object[] xwParams, 
			boolean xwSearch, boolean xwMatchAny) {

		// TODO: watch out for ; delimited strings
		// I think it was supposed to be for entering multiple
		// record IDs or something.  Not terribly important now.

		if (Utility.isStringNullOrEmpty(xwKeySQL)) {
			log.error("Can't swap into empty SQL template >> params: " + 
                    Utility.arrayToCommaSeparatedString(xwParams));
			return "";
		}

		String[] strXwParams = new String[xwParams.length];

		// first index might contain a String[]
		Object o = xwParams[0];

		// first param must not be empty
		if (o == null) { return ""; }

		if (o.getClass().isArray()) {
			String[] sarr = (String[])o;

			if (Utility.isStringNullOrEmpty(sarr[0])) { return ""; }

			StringBuffer sb = new StringBuffer(sarr.length*10);
			for (int j=0; j<sarr.length; j++) {
				// implode the array values
				if (j>0) { sb.append(","); }
				sb.append(DatabaseUtility.makeSQLSafe(sarr[j], !xwSearch));
			}
			strXwParams[0] = sb.toString();

		} else {
			if (Utility.isStringNullOrEmpty(o.toString())) { return ""; }
			strXwParams[0] = DatabaseUtility.makeSQLSafe(o.toString(), !xwSearch);
		}

		// make SQL safe
		for (int i=1; i<xwParams.length; i++) {
			strXwParams[i] = DatabaseUtility.removeSemicolons((String)xwParams[i]);
		}


		log.debug("swap params: " + Arrays.asList(strXwParams).toString());
		if (xwSearch) {
			// only the first string in index [0] is searchable
			log.debug("doing searchable swap");
			return doSwapSearchable(xwKeySQL, strXwParams, xwMatchAny);
		}

		return (new MessageFormat(xwKeySQL)).format(strXwParams);
	}


	/**
	 * Simple method for building the xwhereClause.
	 * @param xwKeySQL The SQL fragment that has placeholders {0}
	 * @param xwParams One dimensional array of strings
	 * @param xwGlue to paste this fragment to the extant clause
	 */
	private void appendToClause(String xwKeySQL, Object[] xwParams, 
			String xwGlue, boolean xwSearch, boolean xwMatchAny) {

		String swapped = doSwap(xwKeySQL, xwParams, xwSearch, xwMatchAny);
		if (!Utility.isStringNullOrEmpty(swapped)) {
			log.info("appending to xwClause: " + swapped);

			if (this.xwhereClause == null) { 
				this.xwhereClause = swapped;
			} else { 
				this.xwhereClause += " " + xwGlue + " " + swapped;
			}
		}
	}


	//////////////////////// xwhereKey /////////////////////////
	private String getXwhereKeySQL() {
		String s = getTag.getXwhereKey();
		if (!Utility.isStringNullOrEmpty(s)) {
			return sqlResources.getString(s);
		}
		return s;
	}

	private String getXwhereKeySQL(String parent) {
		return getXwhereValue("xwhereKey", parent, null, true);
	}
	 
	private String getXwhereKeySQL(String grandParent, String parent) {
		return getXwhereValue("xwhereKey", grandParent, parent, true);
	}


	//////////////////////// xwhereGlue /////////////////////////
	private String getXwhereGlue() {
		return getTag.getXwhereGlue();
	}

	private String getXwhereGlue(String parent) {
		return getXwhereValue("xwhereGlue", parent, null, false);
	}

	private String getXwhereGlue(String grandParent, String parent) {
		return getXwhereValue("xwhereGlue", grandParent, parent, false);
	}


	//////////////////////// xwhereParams /////////////////////////
	private Object[] getXwhereParams() {
		String[] xwParams = new String[1];
		xwParams[0] = getTag.getXwhereParams();
		return xwParams;
	}

	private Object[] getXwhereParams(String parent) {
		Object[] xwParams = null;
		try {
			xwParams = composUtil.getObjectArray("xwhereParams" + PARAM_DELIM + parent);
		} catch (Exception ex) {
			log.error("Problem getting xwhereParam for " + 
					parent + ": " + ex.getMessage());
		}
		return xwParams;

	}

	private Object[] getXwhereParams(String grandParent, String parent) {
		Object[] xwParams = null;
		try {
			xwParams = composUtil.getObjectArray("xwhereParams" + PARAM_DELIM + grandParent + PARAM_DELIM + parent);
		} catch (Exception ex) {
			log.error("Problem getting xwhereParam for " + grandParent +
					" -> " + parent + ": " + ex.getMessage());
		}
		return xwParams;
	}


	//////////////////////// xwhereSearch /////////////////////////
	private boolean getXwhereSearch() {
		return getTag.getXwhereSearch();
	}

	private boolean getXwhereSearch(String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereSearch", parent, null, false));
	}

	private boolean getXwhereSearch(String grandParent, String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereSearch", grandParent, parent, false));
	}



	//////////////////////// xwhereMatchAny /////////////////////////
	private boolean getXwhereMatchAny() {
		return getTag.getXwhereMatchAny();
	}

	private boolean getXwhereMatchAny(String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereMatchAny", parent, null, false));
	}

	private boolean getXwhereMatchAny(String grandParent, String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereMatchAny", grandParent, parent, false));
	}



	//////////////////////// workhorse /////////////////////////
	/**
	 * Workhorse.  Gets the xwhere value.
	 */
	private String getXwhereValue(String xwName, String grandParent, 
			String parent, boolean isProp) {
	
		String xwv = "";
		String path;
		try {
			if (!Utility.isStringNullOrEmpty(grandParent)) {
				if (!Utility.isStringNullOrEmpty(parent)) {
					// use both
					//xwv = (String)((Map)(composUtil.getMap(xwName).get(grandParent))).get(parent);
					path = xwName + PARAM_DELIM + grandParent;
					log.debug("getting " + path + PARAM_DELIM + parent.toLowerCase());
					xwv = (String)(composUtil.getMap(path)).get(parent.toLowerCase());

				} else {
					// use just the grandparent
					log.debug("getting " + xwName + PARAM_DELIM + grandParent.toLowerCase());
					xwv = (String)(composUtil.getMap(xwName).get(grandParent.toLowerCase()));
				}
			} 
			
		} catch (Exception ex) { 
			// fiddly dee 
		}

		if (Utility.isStringNullOrEmpty(xwv)) {
			// just use the simple value
			log.debug("Finding simple attribute: " + xwName);
			xwv = getTag.findAttribute(xwName);
		}

		if (isProp && !Utility.isStringNullOrEmpty(xwv)) { 
			return sqlResources.getString(xwv);
		} else { 
			return xwv; 
		}
	}

	/**
	 * Determines which xwhereParams to use:
	 * simple, parented or grandparented.
	 */
	/* ********************
	private Object getXwhereParamsObjectBAD() {
		// TODO: figure out a way to detect which type of xwps to use
		Map m;
		String[] tmp = getXwhereParamArray();
		if (Utility.isArrayNullOrEmpty(tmp)) {

			// try getting xwhereParams.0, etc.
			tmp = composUtil.getObjectArray("xwhereParams");
			if (Utility.isArrayNullOrEmpty(tmp)) {

				// try getting xwhereParam.identifier.0, etc.
				ArrayList l = new ArrayList();
				m = composUtil.getMap("xwhereParams");
				if (m == null) { return null; }
				Iterator keys = m.keySet().iterator();
				while (keys.hasNext()) {
					String keyName = (String)keys.next();
					tmp = composUtil.getObjectArray("xwhereParams" + PARAM_DELIM + keyName);
					if (Utility.isArrayNullOrEmpty(tmp)) {
						log.debug("Found 2nd level xwhereParams" + PARAM_DELIM + keyName);
						l.add(tmp);
					}
				}

			} else {
				log.debug("Found 1st level xwhereParams (e.g. xwhereParams.0)");
			}
		} else {
			log.debug("Found simple xwhereParams");
		}
	}
	******************* */

	/**
	 * Only the first index, [0], is searchable.
	 */
	private String doSwapSearchable(String tpl, Object[] params, boolean matchAny) {
		if (Utility.isStringNullOrEmpty(tpl)) {
			log.error("Can't (searchable) swap into empty template.");
			return "";
		}

		StringTokenizer stWords = new StringTokenizer((String)params[0], " ");
		int numWords = stWords.countTokens();

		MessageFormat format = new MessageFormat(tpl);
		//String[] arr = new String[1];

		// special handling for AND
		if (numWords > 1 && !matchAny) {
			StringBuffer sb = new StringBuffer(numWords * ((String)params[0]).length() * 2);

			// repeat the swapped tpl for each word in order to match all words
			boolean first = true;
			while (stWords.hasMoreTokens()) {
				if (first) { first = false;
				} else { sb.append(" AND "); }

				params[0] = DatabaseUtility.makeSQLSafe(stWords.nextToken(), true);
				log.debug("==-----formatting with: " + params[0]);
				sb.append(format.format((String[])params));
			}

			return sb.toString();

		} else {
			// matching any word (OR) is easy
			log.debug("Just one word or ANY match");
			params[0] = DatabaseUtility.makeSQLSafe(((String)params[0]).replace(' ', '|'), true);
			return format.format((String[])params);
		}
	}

	/**
	 *
	 */
	private String addSingleQuotes(String orig) {
		String str = "";
		if (orig.charAt(0) != '\'') {
			str = "'";
		}

		str += orig;

		if (orig.charAt(orig.length()-1) != '\'') {
			str += "'";
		}
		return str;
	}
}
