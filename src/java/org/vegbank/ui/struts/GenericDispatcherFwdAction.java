/*
 *	'$RCSfile: GenericDispatcherFwdAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-06-29 06:59:38 $'
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
 
package org.vegbank.ui.struts;

/**
 * Loads the GenericDispatcher URL from the properties file.
 * 
 * @author anderson
 */


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.vegbank.common.utility.Utility;


public class GenericDispatcherFwdAction extends VegbankAction
{
	private static Log log = LogFactory.getLog(GenericDispatcherFwdAction.class);
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		ActionErrors errors = new ActionErrors();

		try {
			String prop = request.getParameter("prop");
			String param = request.getParameter("param");
			String url2get = null;

			if (Utility.isStringNullOrEmpty(prop)) {
				// required
				errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.general", "parameter 'prop' is required"));
			} else {
				if (!prop.startsWith("url2get_")) {
					prop = "url2get_" + prop;
				}
				log.debug("prop: " + prop);

				try {
					log.debug("Loading url2get_data.properties");
					ResourceBundle rb = getResourceBundle("url2get_data");
					url2get = rb.getString(prop);

					if (!Utility.isStringNullOrEmpty(param)) {
						url2get += param;
					}

					log.debug("url2get: " + url2get);
					
					// Forward to the GenericDispatcher
					RequestDispatcher dispatcher = request.getRequestDispatcher(url2get);
					dispatcher.forward(request, response);

				} catch (Exception pe) {
					errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.resource.not.found", pe.getMessage()));
				}
			}


		} catch (Exception ex) {
			log.error("Problem forwarding to GD", ex);
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.general", ex.getMessage()));
		}

		// Report any errors we have discovered to failure page
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.findForward("vberror");
		}
		return null;
	}
	
}
