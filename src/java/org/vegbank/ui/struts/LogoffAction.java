package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.LogUtility;

/*
 * '$RCSfile: LogoffAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-07 06:45:37 $'
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
 
/**
 * Implementation of <strong>Action</strong> that processes a
 * user logoff.
 * 
 * @author farrell
 * @version $Revision: 1.2 $
 */
public class LogoffAction extends VegbankAction
{
	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param actionForm The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 *
	 * @exception Exception if business logic throws an exception
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{

		// Extract attributes we will need
		HttpSession session = request.getSession();
		WebUser user = null;
		try {
			user = getUser(session);
		} catch(Exception ex) {
		}

		// Process this user logoff
		if (user != null)
		{
			LogUtility.log(
				"LogoffAction: User '"
					+ user.getUsername()
					+ "' logged off in session "
					+ session.getId());
		}
		else
		{
			LogUtility.log(
				"LogoffAction: User logged off in session " + session.getId());
		}
		session.removeAttribute(Constants.USER_KEY);
		session.invalidate();

		// Forward control to the specified success URI
		return (mapping.findForward("success"));
	}

}
