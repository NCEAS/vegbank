/*
 *	'$RCSfile: VegbankWhereTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-09-24 01:39:38 $'
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
import org.vegbank.common.utility.ServletUtility;

/**
 * Tag that selects the proper where clause key
 * to look up in SQLStore.properties.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2004-09-24 01:39:38 $
 */

public class VegbankWhereTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankWhereTag.class);


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		try {
			//
			// If the URL does not contain 'where' then set it
			//
			
			String where = request.getParameter("where");
			if (Utility.isStringNullOrEmpty(where)) {
				String params = request.getParameter("params");
				if (Utility.isNumericList(params)) {
					// use a PK
					where = getNumeric();
					log.debug("got numeric where: " + where);

					if (Utility.isStringNullOrEmpty(where)) {
						// still null, set default numeric
						where = "where_" + request.getParameter("entity") + "_pk";
						log.debug("was null so is now: " + where);
					}

				} else {
					// use an AC
					where = getNonNumeric();
					log.debug("got non-numeric where: " + where);

					if (Utility.isStringNullOrEmpty(where)) {
						// still null, set default non-numeric
						where = "where_ac";
						log.debug("was null so is now: " + where);
					}
				}

				request.setAttribute("where", where);

			}

		} catch (Exception ex) {
			log.error("Error while setting where clause key", ex);
		}

		
        return SKIP_BODY;
    }


    /**
     * 
     */
	protected String numeric;

    public String getNumeric() {
		return findAttribute("numeric", this.numeric);
    }

    public void setNumeric(String s) {
        this.numeric = s;
    }

    /**
     * 
     */
	protected String nonNumeric;

    public String getNonNumeric() {
        return findAttribute("nonNumeric", this.nonNumeric);
    }

    public void setNonNumeric(String s) {
        this.nonNumeric = s;
    }


}
