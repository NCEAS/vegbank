/*
 *	'$RCSfile: VegbankGetTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-08-13 01:04:50 $'
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

/**
 * Tag that queries the DB and puts a map or bean in the 
 * page context's servlet request object.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2004-08-13 01:04:50 $
 */

public class VegbankGetTag extends TagSupport {

	private static final Log log = LogFactory.getLog(VegbankGetTag.class);


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
			GenericCommand.execute(
					(HttpServletRequest)pageContext.getRequest(), 
					select, 
					where, 
					beanName, 
					wparam);

		} catch (Exception ex) { 
			log.error("Problem running GenericCommand", ex);
		}
		
        // Continue processing this page
        return (SKIP_BODY);
    }

    /**
     * Key in SQLStore.properties that defines SQL select clause.
     */
	protected String select;

    public String getSelect() {
        return (this.select);
    }

    public void setSelect(String s) {
        this.select = s;
    }

    /**
     * Key in SQLStore.properties that defines SQL where clause.
     */
	protected String where;

    public String getWhere() {
        return (this.where);
    }

    public void setWhere(String s) {
        this.where = s;
    }

    /**
     * Name of VBModelBean class, or "map" to use a HashMap.
     */
	protected String beanName;

    public String getBeanName() {
        return (this.beanName);
    }

    public void setBeanName(String s) {
        this.beanName = s;
    }

    /**
     * Key in SQLStore.properties that defines SQL select clause.
     */
	protected String wparam;

    public String getWparam() {
        return (this.wparam);
    }

    public void setWparam(String s) {
        Object param ;
		try {
			// look in the request
        	param = pageContext.getRequest().getParameter(s);
			if (param == null) {
				
				// find in other scopes
				param = (String)RequestUtils.lookup(pageContext, s, null);
			}

			if (param == null) {
				// just set it to the given value
				this.wparam = s;

			} else {
				this.wparam = (String)param;
			}


		} catch (JspException jspex) {
			this.wparam = s;
		}
    }



}
