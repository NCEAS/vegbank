/*
 *	'$RCSfile: Authenticate.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-10 23:37:14 $'
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

import javax.servlet.http.HttpServletRequest;

import org.vegbank.common.utility.ServletUtility;

import com.livinglogic.struts.workflow.Authentication;

/**
 * @author farrell
 *
 * Impliements autentication by checking that the vegbank cookie for a a non 
 * null username. 
 * 
 * TODO: Replace with a more sensible method of authentication.
 */
public class Authenticate implements Authentication {

	/* (non-Javadoc)
	 * @see com.livinglogic.struts.workflow.Authentication#check(javax.servlet.http.HttpServletRequest)
	 */
	public boolean check(HttpServletRequest request) {	
		
		boolean result = false;
		// get the name and username
		String cookieName = (new ServletUtility()).getCookieValue(request);
		String  emailAddress = request.getParameter("emailAddress");
		
		System.out.println("Authenticate: cookieName: " +  cookieName);
		
		if ( cookieName == null || cookieName.equals(""))
		{
			result = false;
		}
		else 
		{
			result = true;
		}
		
		return result;
	}

}
