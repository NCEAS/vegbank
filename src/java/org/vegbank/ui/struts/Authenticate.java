/*
 *	'$RCSfile: Authenticate.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-12 22:22:18 $'
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vegbank.common.Constants;
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.LogUtility;


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
		HttpSession session = 
			 request.getSession();
		
		if ((session != null)
			&& (session.getAttribute(Constants.USER_KEY) != null))
		{
			valid = true;
		}
		LogUtility.log("Authenticate: User logged on: "+ valid);
		return valid;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.ui.struts.Authentication#checkCertLevel(javax.servlet.http.HttpServletRequest, int)
	 */
	public boolean checkCertLevel(HttpServletRequest request, int minCertLevel)
	{
		boolean result = false;
		
		// Get the certification level of the user
		WebUser user = 
			(WebUser) request.getSession().getAttribute(Constants.USER_KEY);
			
		int certificationLevel = user.getCertificationLevel();
		LogUtility.log(
			"Authenticate: Comparing users cert level;  "
				+ certificationLevel
				+ " with the minimum level of; "
				+ minCertLevel);
		if (minCertLevel <= certificationLevel)
		{
			result = true;
		}
		return result;
	}

}
