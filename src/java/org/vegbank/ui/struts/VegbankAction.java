/*
 *	'$RCSfile: VegbankAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-11 01:27:18 $'
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

package org.vegbank.ui.struts;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.UserDatabaseAccess;


/**
 * Provides some basic functionality for all Vegank Action classes.
 *
 * @author anderson
 */
public abstract class VegbankAction extends Action {

	private static Log log = LogFactory.getLog(VegbankAction.class); 

	/**
	 * @return the WebUser instance in given session or null
	 */
	public static WebUser getUser(HttpSession session) {
		try {
			return (new UserDatabaseAccess().getUser(
				(Long)session.getAttribute(Constants.USER_KEY)));
		} catch (Exception ex) {
			log.error("VegbankAction.getUser(session) error: returning null", ex);
			return null;
		}
	}

	/**
	 * Only for convenience.
	 * @return the WebUser instance with the given usr_id
	 */
	public WebUser getUser(long usrId) {
		try {
			return (new UserDatabaseAccess().getUser(usrId));
		} catch (Exception ex) {
			log.error("VegbankAction.getUser(long) error: returning null", ex);
			return null;
		}
	}

	/**
	 *
	 */
	public static ResourceBundle getResourceBundle(String path) {
		return ResourceBundle.getBundle(path);
	}
		
	/**
	 * Is the current user an administrator?
	 * @return true if current user is admin
	 */
	public boolean userIsAdmin(HttpSession session) {
		Boolean b = (Boolean)session.getAttribute("isAdmin");
		return (b != null && b.booleanValue() == true);
	}
		
	/**
	 * Helps with i18n. 
	 * Gets the value of the request parameter named 'cmd'.
	 * If 'cmd' is not specified then returns 'action'.
	 * The intended value of 'cmd' is a unique name of some
	 * action to process in the Action.
	 */
	public String getDispatchCommand(HttpServletRequest request) {
		String cmd = request.getParameter("cmd");
		if (cmd == null) {
			// no cmd, use action instead
			return request.getParameter("action");
		} else {
			// use cmd
			return cmd;
		}
	}
		
    /**
     * Helps report messages.  Puts 'vbmsg' in request, unlocalized.
     */
    protected void prepareMessage(String comment, ActionMessages messages, HttpServletRequest request) {
        messages.add(Globals.MESSAGE_KEY, new ActionMessage("errors.general", comment));
        messages.add("vbmsg", new ActionMessage(comment, false));
        saveMessages(request, messages);
    }
    
    /**
     * Helps report error messages.  Puts 'vberror' in request, unlocalized.
     */
    protected void prepareError(String comment, ActionErrors errors, HttpServletRequest request) {
        errors.add(Globals.ERROR_KEY, new ActionMessage("errors.general", comment));
        errors.add("vberror", new ActionMessage(comment, false));
        saveErrors(request, errors);
    }
}
