/*
 *	'$RCSfile: VegbankAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-03-25 06:42:06 $'
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

package org.vegbank.ui.struts;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

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
	public WebUser getUser(HttpSession session) {
		try {
			return (new UserDatabaseAccess().getUser(
				(Long)session.getAttribute(Constants.USER_KEY)));
		} catch (Exception ex) {
			log.error("VegbankAction.getUser() error: returning null", ex);
			return null;
		}
	}

	/**
	 *
	 */
	public ResourceBundle getResourceBundle(String path) {
		return ResourceBundle.getBundle(path);
	}
		
}
