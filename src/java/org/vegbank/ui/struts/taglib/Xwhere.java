/*
 *	'$RCSfile: Xwhere.java,v $'
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
import org.vegbank.common.utility.CompositeRequestParamUtil;

/**
 * 
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.2 $ $Date: 2004-12-07 02:53:45 $
 */

public class Xwhere {

	private static final Log log = LogFactory.getLog(Xwhere.class);

	private static ResourceBundle sqlResources = 
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");
	private ServletRequest request;
	private VegbankGetTag getTag;


	public Xwhere(VegbankGetTag t, ServletRequest r) {
		getTag = t;
		request = r;
	}


	public String getXwhereClause() {
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
				xwParamArray[0] = getTag.makeStringSearchable(xwParams, xwClause, getTag.getXwhereGlue());
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
					xwParamArray[j++] = getTag.makeStringSearchable(stParams.nextToken(), xwClause, getTag.getXwhereGlue());
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
				xwParamArray[0] = getTag.makeStringSearchable(xwParams, xwClause, getTag.getXwhereGlue());
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
					xwParamArray[j++] = getTag.makeStringSearchable(stParams.nextToken(), xwClause, getTag.getXwhereGlue());
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

}


