package org.vegbank.ui.struts;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import org.vegbank.ui.struts.Authentication;

/*
 * '$RCSfile: RequestProcessor.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-11-16 01:19:58 $'
 *	'$Revision: 1.8 $'
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
 * Extends the the struts <code>RequestProcessor</code> to Vegbank specific 
 * things, such as authentication and checking roles and certification level.
 * 
 * @author farrell
 *
 */
public class RequestProcessor extends org.apache.struts.action.RequestProcessor
{
	
	/**
	 * The name used to find an ActionForward instance if an authentication violation occurs
	 */
	public static final String AUTHENTICATION_VIOLATION_FORWARD = "AuthenticationException";

	/**
	 * The name used to find an ActionForward instance if an certification violation occurs
	 */
	public static final String CERTIFICATION_VIOLATION_FORWARD = "CertificationException";

	
	
	/**
	 * The set of Authentication instances that have been created and initialized,
	 * keyed by the fully qualified Java class name of the Authentication class.
	 */
	protected Hashtable authClasses = new Hashtable();

	
	/**
	 * <p>Process the roles of a user before .</p>
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * 
	 * @return boolean true to continue processing 
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a processing exception occurs
	 */
	public boolean processRoles(
		HttpServletRequest request,
		HttpServletResponse response,
		ActionMapping mapping)
		throws IOException, ServletException
	{
		VegbankActionMapping vbMapping = (VegbankActionMapping) mapping;
		
		// acquire the specified authClass if there is one defined and do the checks
		String authClassName = vbMapping.getAuthClass();
		if (null != authClassName) {
			Authentication authClass = processAuthClassCreate(response, authClassName);
			if (null == authClass) {
					return true;
			}

			try {
				if (!authClass.checkAuth(request)) {
					ActionForward forward = mapping.findForward(AUTHENTICATION_VIOLATION_FORWARD);
					setPostLoginFwd(request);
					super.processForwardConfig(request, response, forward);
					return false;
				}
					
				String reqRoles = vbMapping.getReqRoles();
				if (!authClass.checkReqRoles(request, reqRoles)) {
					ActionForward forward = mapping.findForward(CERTIFICATION_VIOLATION_FORWARD);
					setPostLoginFwd(request);
					super.processForwardConfig(request, response, forward);
					return false;
				}
			} catch (Exception ex) { 
				throw new ServletException("User probably needs to be logged in.");
			}
		}

		//request.getSession().removeAttribute("postLoginFwd");

		return true;
	}

	/**
	 * Sets the postLoginFwd session attribute.
	 */
	private void setPostLoginFwd(HttpServletRequest request) throws Exception {
		if (request.getSession() == null) {
			log.debug("sPLF: session is null!");
		}

		request.getSession().setAttribute("postLoginFwd", 
				request.getRequestURL().toString() + "?" + request.getQueryString());

	}


	/**
	 * Return an <code>Authentication</code> instance that will be used to
	 * check whether the requested action is allowed to be executed with the
	 * current request.
	 *
	 * @param response The servlet response we are creating
	 * @param authClass the name of the Authentication class to return
	 *
	 * @exception IOException if an input/output error occurs
	 */
	protected Authentication processAuthClassCreate(
		HttpServletResponse response,
		String className)
		throws IOException
	{
		Authentication instance = null;
		synchronized (authClasses)
		{
			// return any existing instance of this class
			instance = (Authentication) authClasses.get(className);
			if (instance != null)
			{
				return (instance);
			}

			// create and return a new Authentication instance
			try
			{
				instance =
					(Authentication) RequestUtils.applicationInstance(
						className);
				authClasses.put(className, instance);
			}
			catch (Throwable t)
			{
				response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error instantiating " + className);
				return (null);
			}
		}

		return (instance);
	}

	/**
	 * If this action is protected by an authClass, do the necessary checks.
	 * Return <code>true</code> to continue normal processing, or <code>false</code>
	 * if the check failed.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param mapping The mapping we are using
	 * @param authClass The Authentication instance to use for the checks
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	protected boolean checkAuth(
		HttpServletRequest request,
		HttpServletResponse response,
		ActionMapping mapping,
		Authentication authClass)
		throws IOException, ServletException
	{
		return authClass.checkAuth(request);
	}
                                                                                                                                                            

}
