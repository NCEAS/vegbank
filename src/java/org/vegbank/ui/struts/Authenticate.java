/*
 *	'$RCSfile: Authenticate.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-31 01:31:13 $'
 *	'$Revision: 1.7 $'
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.PermComparison;


/**
 * @author farrell
 *
 * Impliements autentication by checking for session with user object.
 * 
 * TODO: Replace with a more sensible method of authentication.
 */
public class Authenticate implements  Authentication
{

	/* (non-Javadoc)
	 * @see org.vegbank.ui.struts.Authentication#check(javax.servlet.http.HttpServletRequest)
	 */
	public boolean checkAuth(HttpServletRequest request) 
	{	
		boolean valid = false;
		
		// Get logged on status from session / user object
		HttpSession session = request.getSession();
		
		if ((session != null)
			&& (session.getAttribute(Constants.USER_KEY) != null))
		{
			valid = true;
		}
		LogUtility.log("Authenticate: User logged on: "+ valid);
		return valid;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.ui.struts.Authentication#checkReqRoles(javax.servlet.http.HttpServletRequest, int)
	 */
	public boolean checkReqRoles(HttpServletRequest request, String reqRoles)
	{
		boolean result = false;
		
		// Get the certification level of the user
		WebUser user = 
			(WebUser) request.getSession().getAttribute(Constants.USER_KEY);
			
		int userPerms = user.getPermissiontype();
		LogUtility.log(
			"Authenticate: Comparing user's roles;  "
				+ userPerms
				+ " with the required roles; "
				+ reqRoles);
			
		if (reqRoles == null) {
			LogUtility.log("Authenticate: assuming Action should be " +
					"configured with min role (reg)");
			reqRoles = "reg";
		}
		
		return PermComparison.matchesAll(reqRoles, userPerms);
	}

}
