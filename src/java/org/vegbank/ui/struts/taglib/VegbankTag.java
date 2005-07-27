/*
 *	'$RCSfile: VegbankTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-07-27 21:47:55 $'
 *	'$Revision: 1.9 $'
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
import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.struts.taglib.bean.WriteTag;
import org.vegbank.common.utility.Utility;


/**
 * Abstract base class tag.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.9 $ $Date: 2005-07-27 21:47:55 $
 */

public abstract class VegbankTag extends TagSupport {

	private static final Log log = LogFactory.getLog(VegbankTag.class);


	/**
	 *
	 */
    protected String findAttribute(String attribName, String attribValue) {

		if (Utility.isStringNullOrEmpty(attribValue)) {
			return findAttribute(attribName);

		} else {
			// search for an attribute with the name of the value
			String tmp = findAttribute(attribValue);
			if (Utility.isStringNullOrEmpty(tmp)) {
        		return attribValue;
			} else {
				// just return the original attribute's value
				return tmp;
			}
		}
    }


	/**
	 * Check the request for the given attribute.  If not found, 
     * then try to find the given attribName in other scopes.
     * @return value of given attribute, or empty string if not found
	 */
    protected String findAttribute(String attribName) {
        String attribValue;
		try {
			// look in the request
			//log.debug("Finding " + attribName + " with Request.getParameter()");
        	attribValue = pageContext.getRequest().getParameter(attribName);
			if (attribValue == null || attribValue.equals("")) {
				
				// find in other scopes
				//log.debug("Finding " + attribName + " with RequestUtils.lookup()");
                Object o = null;
                try {
                    o = RequestUtils.lookup(pageContext, attribName, null);
                    attribValue = (String)o;
                } catch (ClassCastException ccex) {
                    if (o == null) {
                        log.error("Value of attribute " + attribName + " cannot be a String");
                    } else {
                        log.error("Value of attribute " + attribName +
                                " cannot be a String; type is " + o.getClass().getName());
                    }
                }
			}

			if (attribValue == null) {
				attribValue = "";
			} 


		} catch (JspException jspex) {
			attribValue = "";
		}

		log.debug("findAttribute: " + attribName + " = " + attribValue);
		return attribValue;
    }


	/**
	 * Gets attribName from value in attribValue if attribValue[0] is set.
	 */
    protected String[] findAttributeArray(String attribName, String[] attribValue) {

		if (Utility.isArrayNullOrEmpty(attribValue)) {
			return findAttributeArray(attribName);

		} else {
			// search for an attribute with the name of the value
			String[] tmp = findAttributeArray(attribValue[0]);
			if (Utility.isArrayNullOrEmpty(tmp)) {
        		return attribValue;
			} else {
				// just return the original attribute's value
				return tmp;
			}
		}
    }


	/**
	 *
	 */
    protected String[] findAttributeArray(String attribName) {
        String[] attribValue;
		try {
			// look in the request
			//log.debug("Finding " + attribName + " with Request.getParameter()");
        	attribValue = pageContext.getRequest().getParameterValues(attribName);
			if (Utility.isArrayNullOrEmpty(attribValue)) {
				// find in other scopes
				//log.debug("Finding " + attribName + " with RequestUtils.lookup()");
				Object o = null;
                try {
                    o = RequestUtils.lookup(pageContext, attribName, null);
                    attribValue = (String[])o;
                } catch (ClassCastException ccex) {
                    if (o != null) {
                        if (o.getClass().getName().equals("java.lang.String")) {
                            attribValue = new String[1];
                            attribValue[0] = (String)o;
                        } else {
                            log.error("Value of attribute " + attribName +
                                    " cannot be a String[]; type is " + o.getClass().getName());
                        }
                    }
                }
			}

		} catch (JspException jspex) {
			attribValue = null;
		}

		/*
		if (attribValue == null) {
			attribValue = new String[1];
			attribValue[0] = "";
		} 
		*/

		//log.debug("findAttributeArray: " + attribName);
		return attribValue;
    }


	/**
	 * @param attrib String value of an attribute
	 * @param default int value to return if attrib is empty
	 */
	protected int convStringToInt(String attrib, int defVal) {
		if (Utility.isStringNullOrEmpty(attrib)) {
			return defVal;
		} else {
			return Integer.parseInt(attrib);
		}
	}


}
