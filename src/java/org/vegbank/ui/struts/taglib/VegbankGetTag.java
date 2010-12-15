/*
 *	'$RCSfile: VegbankGetTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2005/06/01 09:41:12 $'
 *	'$Revision: 1.26 $'
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


import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.struts.taglib.bean.WriteTag;

import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.CompositeRequestParamUtil;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.QueryParameters;

/**
 * Tag that queries the DB and puts a map or bean in the
 * page context's servlet request object.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.26 $ $Date: 2005/06/01 09:41:12 $
 */

public class VegbankGetTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankGetTag.class);

	private static ResourceBundle sqlResources =
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

		//log.debug("Using <vegbank:get>");

		if (Utility.isStringNullOrEmpty(beanName)) {
			beanName = "map";
		}

		// Set up genericBean in the request scope with a list of Taxoninterpretation objects
		// execute() PARAMS:
		//   1: the HTTP request
		//   2: the SQL select key in SQLStore.properties
		//   3: the SQL where key in SQLStore.properties
		//   4: the name of the model bean to generate
		//   5: any SQL where parameters; can be an array too
		try {
			GenericCommand gc = new GenericCommand();

			log.debug("Setting GC's id, numItems, pageNumber, perPage and pager");
			gc.setId(getId());

			gc.setPageNumber(getPageNumber());
			gc.setNumItems(findAttribute("numItems"));
			gc.setPerPage(getPerPage());
			gc.setPager(getPager());


			log.debug("Setting up WHERE clause");
			setupWhereClause( (HttpServletRequest)pageContext.getRequest() );

			log.debug("Calling gc.execute()");
            gc.setQueryParameters(new QueryParameters(this));
			gc.execute(
					(HttpServletRequest)pageContext.getRequest(),
					getSelect(),
					getWhere(),
					getBeanName(),
					getOrderBy(),
					getWparamArray());

		} catch (Exception ex) {
			log.error("Problem running GenericCommand", ex);
		}

        // Continue processing this page
        return SKIP_BODY;
    }


    /**
     * Chooses the proper SQL where clause to use.
	 * Sets it in the request.
     */
    private void setupWhereClause(HttpServletRequest request) {

		try {
			//
			// If the URL does not contain 'where' then set it
			//

			String where = request.getParameter("where");
			////////String where = getWhere();

			if (Utility.isStringNullOrEmpty(where)) {
				String params = request.getParameter("params");
				if (Utility.isStringNullOrEmpty(params)) {
					// this doesn't need to happen if /get/VIEW/ENTITY is used
					// but it supports direct calls to the jsp
					params = request.getParameter("wparam");
				}

				log.debug("Checking numerocity of params: " + params);
				if (Utility.isNumericList(params)) {
					// use a PK
					where = getWhereNumeric();
					log.debug("got numeric where: " + where);

					if (Utility.isStringNullOrEmpty(where)) {
						// still null, set default numeric
						where = "where_" + request.getParameter("entity") + "_pk";
						log.debug("where was null so is now: " + where);
					}

				} else {
					// use an AC
					where = getWhereNonNumeric();

					if (Utility.isStringNullOrEmpty(where)) {
						// still null, set default non-numeric
						//where = "where_accessioncode";  // this one uses IN ('{0}')
						where = "where_ac";  // this one uses IN ({0})
						//log.debug("using default where clause since not specified");
					}
					log.debug("got non-numeric where: " + where);
				}

				request.setAttribute("where", where);
			}

		} catch (Exception ex) {
			log.error("Error while setting where clause key", ex);
		}
    }




    /**
     * Key in SQLStore.properties that defines SQL select clause.
     */
	protected String select;

    public String getSelect() {
        return findAttribute("select", this.select);
    }

    public void setSelect(String s) {
        this.select = s;
    }

    /**
     * Key in SQLStore.properties that defines SQL where clause.
     */
	protected String where;

    public String getWhere() {
        return findAttribute("where", this.where);
    }

    public void setWhere(String s) {
        this.where = s;
    }

    /**
     * Name of VBModelBean class, or "map" to use a HashMap.
     */
	protected String beanName;

	/**
	 * Returns pageNumber that has been set,
	 * or finds pageNumber attribute in any scope.
	 */
    public String getBeanName() {
        return findAttribute("beanName", this.beanName);
    }

    public void setBeanName(String s) {
        this.beanName = s;
    }

    /**
     *
     */
	protected String pageNumber;

	/**
     * If pager is enabled, find value for pageNumber.
     * Else, return explicitely set attribute value or 1.
     */
    public String getPageNumber() {
        if (getPager()) {
		    return findAttribute("pageNumber", this.pageNumber);
        } else {
            if (Utility.isStringNullOrEmpty(this.pageNumber)) {
                return "1";
            } else {
                return this.pageNumber;
            }
        }
    }

    public void setPageNumber(String s) {
        this.pageNumber = s;
    }

    /**
     *
     */
	protected String perPage;

    /**
     * If pager is enabled, find value for perPage.
     * Else, return explicitely set attribute value or default.
     */
    public String getPerPage() {
        if (getPager()) {
		    return findAttribute("perPage", this.perPage);
        } else {
            if (Utility.isStringNullOrEmpty(this.perPage)) {
                return Integer.toString(GenericCommand.DEFAULT_PER_PAGE);
            } else {
                return this.perPage;
            }
        }
    }

    public void setPerPage(String s) {
        this.perPage = s;
    }

    /**
     *
     */
	protected String wparam = null;

    public String getWparam() {
		String s = findAttribute("wparam", this.wparam);
		if (s != null) {
			s = s.toLowerCase();
		}

		return s;
    }

    public String[] getWparamArray() {
		String[] arr;

		// override if set explicitely
		String tmp = this.wparam;
		if (!Utility.isStringNullOrEmpty(tmp)) {
			// check for xwhere
			if (getXwhereEnable()) {
				arr = new String[2];
				//arr[0] = DatabaseUtility.makeSQLSafe(getWparam(), false);
				arr[0] = getWparam();
				arr[1] = getXwhereClause();
				log.debug(this.select + " has 1 WPARAM + XWHERE");
			} else {
				arr = new String[1];
				//arr[0] = DatabaseUtility.makeSQLSafe(getWparam(), false);
				arr[0] = getWparam();
				log.debug(this.select + " has 1 WPARAM");
			}

			return arr;
		}


		arr = findAttributeArray("wparam", null);
		if (arr != null) {
			for (int i=0; i<arr.length; i++) {
				///////////arr[i] = stripSingleQuotes(arr[i]);
				//arr[i] = DatabaseUtility.makeSQLSafe(arr[i], false);
				arr[i] = arr[i];
			}
		}

		if (getXwhereEnable()) {
			log.debug("xwhere enabled!");

			// add the xwhereClause to the end of the wparam array
			int i=0;
			String[] wpArr;
			if (arr == null) {
				log.debug("arr was null!");
				wpArr = new String[1];
			} else {
				// copy all wparams to wpArr
				log.debug("arr was NOT null!");
				wpArr = new String[arr.length+1];
				for (i=0; i<arr.length; i++) {
					wpArr[i] = arr[i];
					log.debug("  " + i + " is " + arr[i]);
				}
			}

			// add the xwhere clause in the last slot
			wpArr[i] = getXwhereClause();

			log.debug("added xwhere clause: " + i + " " + wpArr[i]);
			return wpArr;
		}

		log.debug("xwhere NOT enabled");
		return arr;
    }


    public void setWparam(String s) {
		this.wparam = s;
		//////////this.wparam = new String[1];
		//////////this.wparam[0] = s;
	}

    /**
     *
     */
	protected String id;

    public String getId() {
		return findAttribute("id", this.id);
    }

    public void setId(String s) {
        this.id = s;
    }

    /**
     *
     */
	protected boolean pager = false;

    public boolean getPager() {
		if (this.pager) {
            return true;
        }

        setPager(findAttribute("pager"));
        return this.pager;
    }

    public void setPager(String s) {
        this.pager = Utility.isStringTrue(s);
    }

    public void setPager(boolean b) {
        this.pager = b;
    }

    /**
     *
     */
	protected String whereNumeric;

    public String getWhereNumeric() {
		return findAttribute("whereNumeric", this.whereNumeric);
    }

    public void setWhereNumeric(String s) {
        this.whereNumeric = s;
    }

    /**
     *
     */
	protected String whereNonNumeric;

    public String getWhereNonNumeric() {
        return findAttribute("whereNonNumeric", this.whereNonNumeric);
    }

    public void setWhereNonNumeric(String s) {
        this.whereNonNumeric = s;
    }

    /**
     *
     */
	protected boolean xwhereSearch = false;

    public boolean getXwhereSearch() {
		log.debug("get xwhereSearch: " + xwhereSearch);
		if (xwhereSearch) {
			return true;
		}

        setXwhereSearch(findAttribute("xwhereSearch"));
		return this.xwhereSearch;
    }

    public void setXwhereSearch(String s) {
        this.xwhereSearch = Utility.isStringTrue(s);
    }

    public void setXwhereSearch(boolean b) {
        this.xwhereSearch = b;
    }

    /**
     *
     */
	protected boolean xwhereMatchAny = false;

    public boolean getXwhereMatchAny() {
		log.debug("get xwhereMatchAny: " + xwhereMatchAny);
	//	if (xwhereMatchAny) {
	//		return true;
	//	}

        setXwhereMatchAny(findAttribute("xwhereMatchAny"));
		return this.xwhereMatchAny;
    }

    public void setXwhereMatchAny(String s) {
        this.xwhereMatchAny = Utility.isStringTrue(s);
    }

    public void setXwhereMatchAny(boolean b) {
        this.xwhereMatchAny = b;
    }

    /**
     *
     */
	protected boolean xwhereMatchWholeWords = false;

    public boolean getXwhereMatchWholeWords() {
		log.debug("get xwhereMatchWholeWords: " + xwhereMatchWholeWords);
	//	if (xwhereMatchWholeWords) {
	//		return true;
	//	}

        setXwhereMatchWholeWords(findAttribute("xwhereMatchWholeWords"));
		return this.xwhereMatchWholeWords;
    }

    public void setXwhereMatchWholeWords(String s) {
        this.xwhereMatchWholeWords = Utility.isStringTrue(s);
    }

    public void setXwhereMatchWholeWords(boolean b) {
        this.xwhereMatchWholeWords = b;
    }

    /**
     *
     */
	protected boolean xwhereEnable = false;

    public boolean getXwhereEnable() {
		if (this.xwhereEnable) {
			return true;
		}

        setXwhereEnable(findAttribute("xwhereEnable"));
        return this.xwhereEnable;
    }

    public void setXwhereEnable(String s) {
		this.xwhereEnable = Utility.isStringTrue(s);
    }
    public void setXwhereEnable(boolean b) {
		this.xwhereEnable = b;
    }

    /**
     *
     */
	protected String xwhereKey;

    public String getXwhereKey() {
        return findAttribute("xwhereKey", this.xwhereKey);
    }

	/**
	 * Gets SQL, swaps in xwhereParams and handles xwhereSearch.
	 */
    public String getXwhereClause() {
		Xwhere xw = new Xwhere(this, pageContext.getRequest());
		return xw.getXwhereClause();
    }

    public void setXwhereKey(String s) {
        this.xwhereKey = s;
    }

    /**
     *
     */
	protected String xwhereParams;

	/**
	 *
	 */
    public String getXwhereParams() {
		return findAttribute("xwhereParams", this.xwhereParams);
    }

	/**
	 *
	 */
    public void setXwhereParams(String s) {
        this.xwhereParams = s;
    }


    /**
     * default: AND.
     */
	protected String xwhereGlue;

    public String getXwhereGlue() {
        String s = findAttribute("xwhereGlue", this.xwhereGlue);
		if (Utility.isStringNullOrEmpty(s)) {
			s = "AND";
		}
        return s;
    }

    public void setXwhereGlue(String s) {
        this.xwhereGlue = s;
    }

    /**
     *
     */
	protected String orderBy;

    public String getOrderBy() {
        if (getAllowOrderBy()) {
		    return findAttribute("orderBy", this.orderBy);
        } else {
            return null;
        }
    }

    public void setOrderBy(String s) {
        this.orderBy = s;
    }

    /**
     *  Does NOT search scopes.
     *
     */
	protected String save;

    public String getSave() {
        return this.save;
    }

    public void setSave(String s) {
        this.save = s;
    }

    /**
     *  Does NOT search scopes.
     */
	protected String load;

    public String getLoad() {
        return this.load;
    }

    public void setLoad(String s) {
        this.load = s;
    }

    /**
     * If true, put getQuery in request scope
     * as a String containing the full SQL used to
     * generate the results.
     */
	protected boolean showQuery = false;

    public boolean getShowQuery() {
		if (this.showQuery) {
			return true;
		}

        setShowQuery(findAttribute("showQuery"));
        return this.showQuery;
    }

    public void setShowQuery(String s) {
        setShowQuery(Utility.isStringTrue(s));
    }

    public void setShowQuery(boolean b) {
        this.showQuery = b;
    }

    /**
     *
     */
	protected boolean allowOrderBy = false;

    public boolean getAllowOrderBy() {
		if (this.allowOrderBy) {
			return true;
		}

        setAllowOrderBy(findAttribute("allowOrderBy"));
        return this.allowOrderBy;
    }

    public void setAllowOrderBy(String s) {
        setAllowOrderBy(Utility.isStringTrue(s));
    }

    public void setAllowOrderBy(boolean b) {
        this.allowOrderBy = b;
    }


	/* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

	/**
	 * @param
	 */
	/*
	private String addPosixRegex(String s, String regexOp) {
		StringBuffer sb = new StringBuffer(128);
		StringTokenizer st = new StringTokenizer(s, " ");
		String next;
		boolean first = true;
		boolean isAnd = regexOp.equals("");
		if (isAnd) {
			// final string will be
			// abc.*def.*ghi|ghi.*def.*abc
			// which isn't right
			regexOp = ".*";
		}

		while (st.hasMoreTokens()) {
			if (first) {
				first = false;
			} else {
				if (isAnd) {
					appendix.insert(regexOp);
				}
				sb.append(regexOp);
			}

			next = st.nextToken();
			if (isAnd) {
				appendix.insert(next);
			}
			sb.append(next);
		}

		if (isAnd) {
			sb.append("|").append(appendix);
		}
		return sb.toString();
	}
	*/

	/**
	 *
	 */
	private String stripSingleQuotes(String s) {
		if (!Utility.isStringNullOrEmpty(s) && s.length() > 2) {
			s = s.toLowerCase();
			int lastPos = s.length()-1;
			if (s.charAt(0) == '\'' && s.charAt(lastPos) == '\'') {
				log.debug("Removing 'single' quotes");
				s = s.substring(1, lastPos);
			}
		}
		return s;
	}

	/**
	 *
	 */

}
