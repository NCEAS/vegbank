/*
 *	'$RCSfile: VegbankResubmitFormTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2005-05-24 17:42:33 $'
 *	'$Revision: 1.4 $'
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
import javax.servlet.jsp.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;

import org.owasp.encoder.Encode;

import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;

/**
 * this inserts into the current page a form called resubmitForm that takes all of the parameters as hidden inputs with values
 * and sets the submit action as the current URI
 * this allows us to link to the same form, changing one parameter by using this form rather than a <a href="">
 *
 * @author Michael Lee
 * @version $Revision: 1.4 $ $Date: 2005-05-24 17:42:33 $
 */

public class VegbankResubmitFormTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankResubmitFormTag.class);


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		StringBuffer outputHTML = new StringBuffer(6028);

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Map urlParams;

		try {
            if (getAbsolute()) {
                // url
                outputHTML.append("<form method='post' name='resubmitForm' action='" + request.getRequestURL() + "' class='hidden'>");
            } else {
                // uri
                outputHTML.append("<form method='post' name='resubmitForm' action='" + request.getRequestURI() + "' class='hidden'>");
            }
            //not really just URL Params, but also params posted in body of request.
			urlParams = ServletUtility.parameterHash(request);
            boolean placeholderwritten = false; // trying to avoid duplication of the placeholder element.
            Iterator kit = urlParams.keySet().iterator();
            String str;
            while (kit.hasNext()) {

                String key = (String)kit.next();
                if ( key.equals("placeholder") ) {
					placeholderwritten = true; // dont need to add a new one in this form if already written
				}
                Object paramValue = urlParams.get(key);

                if (paramValue instanceof String) {
                    //is not an array, just a simple value
                    str = (String)paramValue;
                    outputHTML.append("<textarea name='")
                            .append(key).append("'>")
                            .append(Encode.forHtml(str))
                            .append("</textarea>");

                } else if (paramValue instanceof String[]) {
                    //is part of array of values
                    String[] paramArr = request.getParameterValues(key);

                    for (int i=0; i<paramArr.length; i++) {
                        str = (String)paramArr[i];
                        outputHTML.append("<textarea name='")
                                .append(key).append("'>")
                                .append(str)
                                .append("</textarea>");
                    }

                }

            }
            if ( placeholderwritten == false ) {
				// placeholder is not part of URL, so is now no part of this form, add it.
              outputHTML.append("<input name='placeholder' type='hidden' />") ;
		    }
            outputHTML.append("</form>") ;

			pageContext.getOut().println(outputHTML.toString());


		} catch (Exception ex) {
			log.error("Error while writing form to change params via method='post'", ex);
		}

        return SKIP_BODY;
    }

  /**
     *
     */
	protected boolean absolute = false;

    public boolean getAbsolute() {
		if (this.absolute) {
			return true;
		}

        setAbsolute(findAttribute("absolute"));
        return this.absolute;
    }

    public void setAbsolute(String s) {
        setAbsolute(Utility.isStringTrue(s));
    }

    public void setAbsolute(boolean b) {
        this.absolute = b;
    }


}
