/*
 *	'$RCSfile: VegbankPagerTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-09-01 03:01:40 $'
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


import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;
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
import org.vegbank.common.utility.ServletUtility;

/**
 * Tag that builds a page selector in HTML.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.2 $ $Date: 2004-09-01 03:01:40 $
 */

public class VegbankPagerTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankPagerTag.class);


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

		// read perPage and pageNumber from the request

		int perPage;
		int numItems;

		try {
			String tmp = findAttribute("numItems");
			log.debug("numItems: " + tmp);
			if (Utility.isStringNullOrEmpty(tmp)) {
				// no deal if no numItems given
				return SKIP_BODY;
			} else {
				numItems = Integer.parseInt(tmp);
			} 

			tmp = findAttribute("perPage");
			log.debug("perPage: " + tmp);
			if (Utility.isStringNullOrEmpty(tmp)) {
				// no deal if no perPage given
				return SKIP_BODY;
			} else {
				perPage = Integer.parseInt(tmp);
			} 

			int numPages = numItems / perPage;
			int pageNumber;

			tmp = findAttribute("pageNumber");
			log.debug("pageNumber: " + tmp);
			if (Utility.isStringNullOrEmpty(tmp)) {
				pageNumber = 1;
			} else {
				pageNumber = Integer.parseInt(tmp);
			}

			String pagerHTML = "";
			String queryString;

			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

			Map urlParams;
			urlParams = ServletUtility.parameterHash(request);

			urlParams.put("numItems", Integer.toString(numItems));

			if (pageNumber > 1) {
				urlParams.put("pageNumber", Integer.toString(pageNumber - 1));
				queryString = ServletUtility.buildQueryString(urlParams);
				pagerHTML += "&laquo; <a href='" + request.getRequestURI() + queryString + "'>previous</a> ";
			}

			for (int i=1; i<=numPages; i++) {

				if (pageNumber == i) {
					pagerHTML += " " + i + " ";

				} else {
					urlParams.put("pageNumber", Integer.toString(i));
					queryString = ServletUtility.buildQueryString(urlParams);
					pagerHTML += "<a href='" + request.getRequestURI() + queryString + "'>" + i + "</a>";
				}

				if (i < numPages) {
					pagerHTML += " | ";
				}
			}

			if (pageNumber < numPages) {
				urlParams.put("pageNumber", Integer.toString(pageNumber + 1));
				queryString = ServletUtility.buildQueryString(urlParams);
				pagerHTML += " <a href='" + request.getRequestURI() + queryString + "'>next</a> &raquo;";
			}

			pageContext.getOut().println("<strong>" + pagerHTML + "</strong>");
		} catch (Exception ex) {
			log.error("Error while creating pager", ex);
		}

		
        return SKIP_BODY;
    }



    /**
     * 
     */
	protected String numItems;

    public String getNumItems() {
		return findAttribute("numItems", this.numItems);
    }

    public void setNumItems(String s) {
		this.numItems = s;
	}


    /**
     * 
     */
	protected String pageNumber;

	/**
	 * Returns pageNumber that has been set,
	 * or finds pageNumber attribute in any scope.
	 */
    public String getPageNumber() {
        return findAttribute("pageNumber", this.pageNumber);
    }

    public void setPageNumber(String s) {
        this.pageNumber = s;
    }

    /**
     * 
     */
	protected String perPage;

    public String getPerPage() {
		return findAttribute("perPage", this.perPage);
    }

    public void setPerPage(String s) {
        this.perPage = s;
    }

}
