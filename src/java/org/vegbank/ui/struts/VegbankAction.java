/*
 *	'$RCSfile: VegbankAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-02-28 11:22:01 $'
 *	'$Revision: 1.3 $'
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
import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.UserDatabaseAccess;


/**
 * Provides some basic functionality for all Vegank Action classes.
 *
 * @author anderson
 */
public abstract class VegbankAction extends Action {

	/**
	 * @return the WebUser instance in given session or null
	 */
	public WebUser getUser(HttpSession session) {
		try {
			return (new UserDatabaseAccess().getUser(
				(Long)session.getAttribute(Constants.USER_KEY)));
		} catch (Exception ex) {
			LogUtility.log("VegbankAction.getUser() error: returning null", ex);
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
