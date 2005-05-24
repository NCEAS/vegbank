/*
 *	'$RCSfile: VegbankChangeParamTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2005-05-24 18:07:12 $'
 *	'$Revision: 1.5 $'
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

import org.vegbank.common.Constants;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;

/**
 * Tag that replaces one request parameter value with another
 * and returns an absolute or relative URL back to the current
 * address with the new parameter value.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.5 $ $Date: 2005-05-24 18:07:12 $
 */

public class VegbankChangeParamTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankChangeParamTag.class);


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		StringBuffer newLinkHTML = new StringBuffer(128);

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Map urlParams;

		try {
            if (getAbsolute()) {
                // url
                newLinkHTML.append(request.getRequestURL()).append("?");
            } else {
                // uri
                newLinkHTML.append(request.getRequestURI()).append("?");
            }

			urlParams = ServletUtility.parameterHash(request);


            String n = getParamName();
            String v = getParamValue();
            urlParams.put(n, v);

            boolean first = true;
            Iterator kit = urlParams.keySet().iterator();
            while (kit.hasNext()) {

                String key = (String)kit.next();
                Object paramValue = urlParams.get(key);


                if (paramValue instanceof String) {
                    if (first) { first = false;
                    } else { newLinkHTML.append("&"); }

                    newLinkHTML.append(key).append("=")
                        .append(java.net.URLEncoder.encode((String)paramValue, "UTF-8"));

                } else if (paramValue instanceof String[]) {
                    String[] paramArr = request.getParameterValues(key);

                    for (int i=0; i<paramArr.length; i++) {
                        if (first) { first = false;
                        } else { newLinkHTML.append("&"); }

                        newLinkHTML.append(key).append("=")
                            .append(java.net.URLEncoder.encode(paramArr[i], "UTF-8"));
                    }
                }

            }

            boolean usePostParams = getPostParams() ;
            // a way to DEBUG without logging:
            // newLinkHTML.append("&lengthOfThisURLBeforeThisParam=").append(newLinkHTML.length());
            if ( newLinkHTML.length() > Constants.MAX_URL_LENGTH ) {
               // then we should post these params as the URL is too long
               usePostParams = true ;
		    }
            if ( usePostParams ) {
				//use javascript to post form : LIMITATION- cannot post value (or named parameter) that contains a quotation mark!
				// LIMITATION #2: cannot wrap this tag in double quotes, must be single quotes.
				pageContext.getOut().println("javascript:postNewParam('" + n + "','" + v + "')");
			} else {
				// use href with params
				pageContext.getOut().println(newLinkHTML.toString());
			}

		} catch (Exception ex) {
			log.error("Error while changing params", ex);
		}

        return SKIP_BODY;
    }


    /**
     *
     */
	protected String paramName;

    public String getParamName() {
		return this.paramName;
    }

    public void setParamName(String s) {
        this.paramName = s;
    }

    /**
     *
     */
	protected String paramValue;

    public String getParamValue() {
		return findAttribute("paramValue", this.paramValue);
    }

    public void setParamValue(String s) {
        this.paramValue = s;
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

	protected boolean postParams = false;

    public boolean getPostParams() {
		if (this.postParams) {
			return true;
		}
        setPostParams(findAttribute("postParams"));
        return this.postParams;
    }

    public void setPostParams(String s) {
        setPostParams(Utility.isStringTrue(s));
    }

    public void setPostParams(boolean b) {
        this.postParams = b;
    }

}
