/*
 *	'$RCSfile: VegbankTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-08-27 23:26:32 $'
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


/**
 * Abstract base class tag.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2004-08-27 23:26:32 $
 */

public abstract class VegbankTag extends TagSupport {

	private static final Log log = LogFactory.getLog(VegbankTag.class);


    protected Object findAttribute(String attribName) {
        Object attribValue;
		try {
			// look in the request
        	attribValue = pageContext.getRequest().getParameter(attribName);
			if (attribValue == null) {
				
				// find in other scopes
				attribValue = (String)RequestUtils.lookup(pageContext, attribName, null);
			}

			if (attribValue == null) {
				// just set it to the given value
				attribValue = "";

			} else {
				attribValue = (String)attribValue;
			}


		} catch (JspException jspex) {
			attribValue = "";
		}

		log.debug("findAttribute: " + attribName + " = " + attribValue);
		return attribValue;
    }



}
