/*
 *	'$RCSfile: VegbankDenormTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-17 00:15:54 $'
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

import org.vegbank.common.Constants;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.common.utility.DenormUtility;

/**
 * Tag that selects the proper where clause key
 * to look up in SQLStore.properties.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.2 $ $Date: 2005-08-17 00:15:54 $
 */

public class VegbankDenormTag extends VegbankTag {

	public static final String AUTH_USR_ID = "usr_id";
	public static final String AUTH_CUSTOM = "custom";

	private static final Log log = LogFactory.getLog(VegbankDenormTag.class);
	private static ResourceBundle sqlStore =
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		try {
            String update = getUpdate();
            String denormtype = getDenormtype();
            String resultsString = Long.toString(DenormUtility.update(update, getDenormtype(), getWparamArray()));
            request.setAttribute(update+"-RESULTS",resultsString);

		} catch (Exception ex) {
			log.error("Error while running <vegbank:denorm> ", ex);
            request.setAttribute(update+"-RESULTS","-1");
		}


        return SKIP_BODY;
    }



    /**
     * Key in SQLStore.properties that defines SQL where clause.
     */
	protected String update;

    public String getUpdate() {
        return findAttribute("update", this.update);
    }

    public void setUpdate(String s) {
        this.update = s;
    }


    /**
     *  FROM vegbank:get
     */

    	protected String wparam = null;

	    public String getWparam() {
			String s = findAttribute("wparam", this.wparam);
		//	if (s != null) {
		//		s = s.toLowerCase();
		//	}

			return s.replace(';',' '); //no semicolons allowed.  could lead to security problem
    }

    public String[] getWparamArray() {
		String[] arr;

		// override if set explicitely
		String tmp = this.wparam;
		if (!Utility.isStringNullOrEmpty(tmp)) {
			arr = new String[1];
			//arr[0] = DatabaseUtility.makeSQLSafe(getWparam(), false);
			arr[0] = getWparam();
			log.debug(" has 1 WPARAM");
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

		return arr;
    }


    public void setWparam(String s) {
		this.wparam = s;
		//////////this.wparam = new String[1];
		//////////this.wparam[0] = s;
	}




    /**
     *  denormType is one strong of either: all, null, ac (null is default option)
     */
	protected String denormtype;

    public String getDenormtype() {
        return findAttribute("denormtype", this.denormtype);
    }

    public void setDenormtype(String s) {
        if (s == null) {
			s = "null"; // default
		}
        this.denormtype = s;
    }



}
