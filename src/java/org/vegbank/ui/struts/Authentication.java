package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;

/*
 * '$RCSfile: Authentication.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-12 22:22:18 $'
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

/**
 * Interface that must be implemented by classes which provide authentication checks,
 * that are used in the action mapping's property "authClass" and "minCertLevel".
 * Please note that only a single instance of each authentication class is instanciated.
 * Therefore, you have to be very careful when using data members. Normally only the
 * single method <code>checkAuth</code> is provided, which does not work on any object's
 * variables.
 * 
 * Also has check cert level method.
 * 
 * 
 *
 * @author Farrell
 * @version $Revision: 1.1 $
 */
                                                                                                                                                            
public interface Authentication
{
	/**
	 * Check whether the user is authenticated to access the action that is protected
	 * with instances of the implementing class.
	 * @param request the request object
	 * @return true, if the user passed this check, false otherwise
	 */
	public boolean checkAuth(HttpServletRequest request);
   
	/**
	 * Check whether the user has a certification level higher or equal to the minimum 
	 * certification level required.
	 * @param minCertLevel level required
	 * @param request the request object
	 * @return true, if the user passed this check, false otherwise
	 */     
	public boolean checkCertLevel(HttpServletRequest request, int minCertLevel);
}

