/*
 *	'$RCSfile: VegbankTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-10 00:01:08 $'
 *	'$Revision: 1.10 $'
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
 * @version $Revision: 1.10 $ $Date: 2005-08-10 00:01:08 $
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

        // look in the request
        attribValue = pageContext.getRequest().getParameter(attribName);
        if (Utility.isStringNullOrEmpty(attribValue)) {
            attribValue = lookupAttribute(attribName);
        }

        if (attribValue == null) {
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
        // look in the request
        //log.debug("Finding " + attribName + " with Request.getParameter()");
        attribValue = pageContext.getRequest().getParameterValues(attribName);
        if (Utility.isArrayNullOrEmpty(attribValue)) {
            attribValue = lookupAttributeArray(attribName);
        }

		return attribValue;
    }


    /**
     * Check other scopes for this attribute as an array.
     */
    protected String[] lookupAttributeArray(String attribName) {
        String[] attribValue = null;
        Object o = null;

        // find in other scopes
        //log.debug("Finding " + attribName + " with RequestUtils.lookup()");
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
		} catch (JspException jspex) {
			attribValue = null;
        }

        return attribValue;
    }

    /**
     * Check other scopes for this attribute as a String.
     */
    protected String lookupAttribute(String attribName) {
        String attribValue = null;
        Object o = null;

        // find in other scopes
        //log.debug("Finding " + attribName + " with RequestUtils.lookup()");
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
		} catch (JspException jspex) {
			attribValue = "";
        }

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
