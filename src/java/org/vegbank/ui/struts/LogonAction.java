package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.model.Userdataset;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DatasetUtility;
import org.vegbank.common.utility.UserDatabaseAccess;

/*
 * '$RCSfile: LogonAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-08-08 22:38:20 $'
 *	'$Revision: 1.16 $'
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
 * Struts <code>Action</code> to handle logging in to Vegbank.
 * 
 * @author farrell
 */
public class LogonAction extends VegbankAction 
{
	private static Log log = LogFactory.getLog(LogonAction.class);

	
	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 *
	 * @exception Exception if business logic throws an exception
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)	{
			
	/*
		log.debug("LogonAction: calling other execute()");
		return execute(mapping, (DynaActionForm)form, request, response);
	}

	public ActionForward execute(
			ActionMapping mapping,
			DynaActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)	{
	*/
				
		log.debug("LogonAction: begin");

		DynaActionForm dform = (DynaActionForm)form;

		// Validate the request parameters specified by the user
		ActionErrors errors = new ActionErrors();
		String username =
			(String)dform.get("username");
//			(String) PropertyUtils.getSimpleProperty(dform, "username");
		String password =
			(String)dform.get("password");
//			(String) PropertyUtils.getSimpleProperty(dform, "password");
		
		log.debug("LogonAction: authenticating user: '"+ username); 
	
		WebUser user = null;
		UserDatabaseAccess uda = new UserDatabaseAccess();
    
		try
		{
			// Attempt to get user from database;
			username = username.trim();
			if ( username.equalsIgnoreCase(Constants.GUEST_USER_KEY)) {
				user = this.getGuestUser();
				
			} else {
				log.debug("LogonAction: getting user " + username);
				user = uda.getUser(username);			
			}
      
      System.out.println("user.password: " + user.getPassword().trim());
      System.out.println("digested provided password: " + uda.getDigest(password.trim(), user.getEmail().trim()));
			
			if ( user == null ) {
				errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.user.not.found", username));

			} else if ( user.isGuest() ) {
				// No need to check password

			} else if  ( ! user.getPassword().trim().equals(uda.getDigest(password.trim(), user.getEmail().trim())) ) {
				// wrong password
				errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.password.mismatch"));
			} 

			// Report any errors we have discovered back to the original form
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.getInputForward());
			}

			if (!user.isGuest()) {
				// this is a valid, authenticated user
				uda.updateTicketCount(user.getUsername());
			}

		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.action.failed", e.getMessage() ));
			log.error("LogonAction: problem: ", e);
			saveErrors(request, errors);
			return mapping.findForward("vberror");
		}
		

		// Save the logged-in user's ID in session
		HttpSession session = request.getSession();
		Long usrId = user.getUseridLong();
		session.setAttribute(Constants.USER_KEY, usrId);
		session.setMaxInactiveInterval(-1);  // never timeout

		log.debug("LogonAction: User '"+ user.getUsername() + 
				"' authenticated in session " + session.getId());

        //
        // DATACART
        // If session has datacart, bump old dc and set ownership of current.
        //
        try {
            DatasetUtility dsu = new DatasetUtility(); 
            Long dsId = dsu.getDatacartId(session);

            long numItems = 0;
            if (dsId != null) {
                numItems = dsu.countItems(dsId);
            }
            log.debug("anon datacart size: " + numItems);

            if (dsId == null || numItems == 0) {
                // get user's old datacart if has one
                Userdataset datacart = dsu.getDatacartByUser(usrId);
                if (datacart != null) {
                    log.debug("anon datacart was empty so using old one");
                    dsId = new Long(datacart.getUserdataset_id());
                    session.setAttribute(Utility.DATACART_KEY, dsId);
                    session.setAttribute(Utility.DATACART_COUNT_KEY, new Long(dsu.countItems(dsId)));
                }

            } else {
                // bump old cart 
                // ds_id already in session
                log.debug("claiming datacart for user");
                dsu.bumpDatacart(usrId.longValue());
                dsu.setOwner(usrId.longValue(), dsId.longValue());
                dsu.setDatacart(usrId.longValue(), dsId.longValue());
            }

        } catch (Exception ex) {
            log.error("Problem while setting datacart for logged in user", ex);
        }

		// Remove the obsolete form bean
		if (mapping.getAttribute() != null) {
			
			if ("request".equals(mapping.getScope())) {
				request.removeAttribute(mapping.getAttribute());
				
			} else {
				session.removeAttribute(mapping.getAttribute());
			}
		}

		// if admin...
		Boolean isAdmin = new Boolean(
				PermComparison.matchesOne(user.getPermissiontype(), "dba"));
		log.debug("Logged in user is admin?: " + isAdmin.booleanValue());
		session.setAttribute("isAdmin", isAdmin);

		String postLoginFwd = (String)session.getAttribute("postLoginFwd");
		if (!Utility.isStringNullOrEmpty(postLoginFwd)) {
			try { 
				log.debug("Post login redir to: " + postLoginFwd);
				response.sendRedirect(postLoginFwd);
				return null;
				//return mapping.findForward(postLoginFwd);
			} catch (java.io.IOException ioe) {
				errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.action.failed", ioe.getMessage() ));
				log.error("problem redirecting to " + postLoginFwd + " -- ", ioe);
				saveErrors(request, errors);
				return mapping.findForward("vberror");
			}
		}

		// Forward control to the specified success URI
		return mapping.findForward("success");
	}
	
	/**
	 * @return
	 */
	private WebUser getGuestUser()
	{
		// Set up the guest user
		// TODO: This can be made more efficient, 
		// make a singleton by subclassing webuser?
		WebUser guestUser = new WebUser();
		guestUser.setUserid(0);
		guestUser.setPermissiontype(0);
		guestUser.setEmail(Constants.GUEST_USER_KEY);
		guestUser.setUsername(Constants.GUEST_USER_KEY);
		guestUser.setGivenname("Guest");
		guestUser.setSurname("User");
		guestUser.isGuest(true);
		return guestUser;
	}


}
