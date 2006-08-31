/*
 *	'$RCSfile: Xwhere.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-08-31 05:22:27 $'
 *	'$Revision: 1.13 $'
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
 * @version $Revision: 1.13 $ $Date: 2006-08-31 05:22:27 $
 */

public class Xwhere {

	private static final Log log = LogFactory.getLog(Xwhere.class);
	private static final String CRP_PARAM_DELIM = CompositeRequestParamUtil.CRP_PARAM_DELIM;

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
						getXwhereMatchAny(id),
						getXwhereMatchWholeWords(id));
			}

		} else if (xwParams instanceof Object[]) {
			log.debug("xwp is an ARRAY");
			// An array is used with the xwKey directly
			// It is one dimensional
			appendToClause(getXwhereKeySQL(), (Object[])xwParams,
					getXwhereGlue(), getXwhereSearch(), getXwhereMatchAny(), getXwhereMatchWholeWords() );
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
					//log.debug("getting string array for xwhereParams" + CRP_PARAM_DELIM + keyName);
					//arr = composUtil.getObjectArray("xwhereParams" + CRP_PARAM_DELIM + keyName);
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
			boolean xwSearch, boolean xwMatchAny, boolean xwMatchWholeWords) {

		// TODO: watch out for ; delimited strings
		// I think it was supposed to be for entering multiple
		// record IDs or something.  Not terribly important now.

		if (Utility.isStringNullOrEmpty(xwKeySQL)) {
			//log.error("Can't swap into empty SQL template >> params: " +
             //       Utility.arrayToCommaSeparatedString(xwParams));
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
			return doSwapSearchable(xwKeySQL, strXwParams, xwMatchAny, xwMatchWholeWords);
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
			String xwGlue, boolean xwSearch, boolean xwMatchAny, boolean xwMatchWholeWords) {

		String swapped = doSwap(xwKeySQL, xwParams, xwSearch, xwMatchAny, xwMatchWholeWords);
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
			xwParams = composUtil.getObjectArray("xwhereParams" + CRP_PARAM_DELIM + parent);
		} catch (Exception ex) {
			log.error("Problem getting xwhereParam for " +
					parent + ": " + ex.getMessage());
		}
		return xwParams;

	}

	private Object[] getXwhereParams(String grandParent, String parent) {
		Object[] xwParams = null;
		try {
			xwParams = composUtil.getObjectArray("xwhereParams" + CRP_PARAM_DELIM + grandParent + CRP_PARAM_DELIM + parent);
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


	//////////////////////// xwhereMatchWholeWords /////////////////////////
	private boolean getXwhereMatchWholeWords() {
		return getTag.getXwhereMatchWholeWords();
	}

	private boolean getXwhereMatchWholeWords(String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereMatchWholeWords", parent, null, false));
	}

	private boolean getXwhereMatchWholeWords(String grandParent, String parent) {
		return Utility.isStringTrue(
				getXwhereValue("xwhereMatchWholeWords", grandParent, parent, false));
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
					path = xwName + CRP_PARAM_DELIM + grandParent;
					log.debug("getting " + path + CRP_PARAM_DELIM + parent.toLowerCase());
					xwv = (String)(composUtil.getMap(path)).get(parent.toLowerCase());

				} else {
					// use just the grandparent
					log.debug("getting " + xwName + CRP_PARAM_DELIM + grandParent.toLowerCase());
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
					tmp = composUtil.getObjectArray("xwhereParams" + CRP_PARAM_DELIM + keyName);
					if (Utility.isArrayNullOrEmpty(tmp)) {
						log.debug("Found 2nd level xwhereParams" + CRP_PARAM_DELIM + keyName);
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
	private String doSwapSearchable(String tpl, Object[] params, boolean matchAny, boolean matchWholeWords) {
		if (Utility.isStringNullOrEmpty(tpl)) {
			log.error("Can't (searchable) swap into empty template.");
			return "";
		}

        //this is where the multiple words get split into different clauses:
        // idea here is to parse based on whitespace and quotes, but once you start parsing by quotes,
        // ignore whitespace until you hit another quote.  nextToken(string) is used to put a new delimiter in to flip them.

         String currentDelims = fWHITESPACE_AND_QUOTES;
		 StringTokenizer stWords = new StringTokenizer((String)params[0], currentDelims, true);

	    int numWords = stWords.countTokens();

		MessageFormat format = new MessageFormat(tpl);
		//String[] arr = new String[1];

        //MTL : easier to do AND and OR using the same loop, instead of replacing " " with "|".  Hope performance is ok.

		//if (numWords > 1 && !matchAny) {
			StringBuffer sb = new StringBuffer(2 + numWords * ((String)params[0]).length() * 2);
            sb.append("("); //start opening paren, needed in OR statements
			boolean first = true;
			String token = null;
			while (stWords.hasMoreTokens()) {
				token = stWords.nextToken(currentDelims);
				if ( !isDoubleQuote(token) ) {
				  if (textHasContent(token)) {
				      // if it doesn't have content, don't worry about it
	   				  // repeat the swapped tpl for each word in order to match all words

					  if (first) { first = false;
					  } else {
						    //MTL : must do AND and OR the same way for sanity's sake.  Hope performance is ok.
						    // this could be handled by string defined before this loop.
						    if ( matchAny )  {
										sb.append(" OR ");
									} else {
										sb.append(" AND ");
		                     }
				      }
                      // if token starts with - then search for things without it!
                      if (token.startsWith("-")) {
                          log.debug("attempting a negative search with " + token);
                          sb.append(" NOT ");
                          // remove - from token
                          token = token.substring(1);
                      }
				      if ( matchWholeWords ) {
					    //only match whole words, by inserting beginning and ending of word reg. expressions,
					    // e.g. select count(1) from keywords where keywords ~* '[[:<:]]quercus[[:>:]]';
					    params[0] = DatabaseUtility.makeSQLSafe("[[:<:]]" + token + "[[:>:]]", true);
					  } else {
					    params[0] = DatabaseUtility.makeSQLSafe(token, true);
				      }
					  log.debug("==-----formatting with: " + params[0]);
					  sb.append(format.format((String[])params));
			      }
				} else {
				  // flip delimiters, b/c delim is quote
				  currentDelims = flipDelimiters(currentDelims);
                }

			}
            sb.append(")"); //close starting paren, which is needed in OR statements
			return  sb.toString() ;

		//} else {
			// matching any word (OR) is easy
		//	log.debug("Just one word or ANY match");
		//	params[0] = DatabaseUtility.makeSQLSafe(((String)params[0]).replace(' ', '|'), true);
		//	return format.format((String[])params);
		//}
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

/*****************************************************************************************************
* Adapted the following from (as well as bit above about flipping delimiters):
* http://www.javapractices.com/Topic87.cjp
* Thanks.  MTL 31-May-2005
*****************************************************************************************************/

  /// PRIVATE /////
  private String fSearchText;
  private static final String fDOUBLE_QUOTE = "\"";

  //the parser flips between these two sets of delimiters
  private static final String fWHITESPACE_AND_QUOTES = " \t\r\n\"";
  private static final String fQUOTES_ONLY ="\"";

  /**
  * Use to determine if a particular word entered in the
  * search box should be discarded from the search.
  */

  private boolean textHasContent(String aText) {
    return (aText != null) && (!aText.trim().equals(""));
  }

  private boolean addNonTrivialWordToResult( String aToken ){
    return  ( textHasContent(aToken) ) ;
  }

  private boolean isDoubleQuote( String aToken ){
    return aToken.equals(fDOUBLE_QUOTE);
  }

  private String flipDelimiters( String aCurrentDelims ) {
    String result = null;
    if ( aCurrentDelims.equals(fWHITESPACE_AND_QUOTES) ) {
      result = fQUOTES_ONLY;
    }
    else {
      result = fWHITESPACE_AND_QUOTES;
    }
    return result;
  }

  /*****************************************************************************************************/
  /*           END of 31-May-2005 MTL adaptation from http://www.javapractices.com/Topic87.cjp         */
  /*****************************************************************************************************/



}
