/*
 *	'$RCSfile: UserListAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-06-09 17:43:51 $'
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

package org.vegbank.ui.struts.admin;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.ui.struts.CertificationForm;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.common.utility.UserDatabaseAccess;


/**
 * ADMIN
 * UserListAction
 * return a list of all users to the system
 * 
 * @author berkley
 */
public class UserListAction extends VegbankAction {

	private static Log log = LogFactory.getLog(UserListAction.class); 

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {



		log.debug("In action UserListAction");
		ActionErrors errors = new ActionErrors();

		UserDatabaseAccess uda = new UserDatabaseAccess(); 


		// List all applications
		try {
			log.debug("UserListAction: getting all users");
			/*List allApps = uda.getAllCertificationAppHeaders(
					request.getParameter("sortby"));*/
      List allUsersList = uda.getFullUserList();

			// NOTE: Action must be configured scope=session
			//   in order to pass List (of Beans) to JSP 
			request.setAttribute("usrList", allUsersList);

			log.debug("Leaving UserListAction");
			return mapping.findForward("view");
		
		} catch (Exception ex) {
			log.error("ERROR UserListAction: FAILURE: " + ex.toString());
			errors.add(Globals.ERROR_KEY, new ActionMessage(
						"errors.general", ex.toString()));
			saveErrors(request, errors);
			return mapping.findForward("failure");
		}

	}

}
