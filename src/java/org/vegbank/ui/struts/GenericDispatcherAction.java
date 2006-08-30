/*
 *	'$RCSfile: GenericDispatcherAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-08-30 23:02:03 $'
 *	'$Revision: 1.19 $'
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

/**
 * Allows the request to specific the command to run and the jsp page to dispatch too.
 * 
 * @author farrell
 */


import java.lang.reflect.Method;
import java.util.*;
import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.vegbank.common.Constants;
import org.vegbank.common.command.GenericCommandStatic;
import org.vegbank.common.command.RetrieveVBModelBean;
import org.vegbank.common.model.VBModelBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.VBObjectUtils;

public class GenericDispatcherAction extends Action
{
	private static Log log = LogFactory.getLog(GenericDispatcherAction.class);

	private static final String GENERIC_CMD = "GenericCommand";
	private static final String DISPATCH = "Dispatch";
	private static final String FORWARD = "Forward";
	private static final String RETRIEVE_BEAN = "RetrieveVBModelBean";
	private static final String commandLocation = "org.vegbank.common.command.";
	private static final String jspLocation = "/forms/";
	private static final String ROOT_ENTITY = "rootEntity";  
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		ActionErrors errors = new ActionErrors();

		// get form params
		DynaActionForm dynaForm = (DynaActionForm) form; 
		String accessionCode = (String)dynaForm.get("accessionCode");
		String fwdURL = null;

		// get request params
		String command = request.getParameter("command");
		String selectKey = request.getParameter("SQL");
		String whereKey = request.getParameter("WHERE");
		String beanName = request.getParameter("BeanName");
		String jsp = request.getParameter("jsp");
		String[] wparam = request.getParameterValues("wparam");


		//Map urlParams = new HashMap();
		Map urlParams = ServletUtility.parameterHash(request);
		urlParams.remove("command");
		
		try
		{
			log.debug( "GD: command == " + command );
			if (command == null) {
				command = "";
				errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.general", "param 'command' not specified"));


			} else if ( command.equalsIgnoreCase(DISPATCH)) {
				///////////////////////////////////////////////////////////////
				// Handle special URL /get/view/entity/params
				///////////////////////////////////////////////////////////////
				
				// view: literal value { detail | summary | dd | bean | xml | *} 
				String view = request.getParameter("view").toLowerCase();

				if (view.equals("std")) {
					// for now just try to get detail view
					view = "detail";
				}

				// entity: { VBModelBean name | SQLStore select key }
				String entity = request.getParameter("entity");
				
				// params: comma-separated PKs or ACs
				String params = request.getParameter("params");

				log.debug("Dispatching /get\n\tview: " + view +
						"\n\tentity: " + entity + 
						"\n\tparams: " + params);




				if (view.equalsIgnoreCase("jsp")) {
					//////////////////////////////////////////
					// VIEW: jsp
					//////////////////////////////////////////
					command = GENERIC_CMD;

					selectKey = entity;
					whereKey = buildWhereKey(entity, params);
					beanName = Utility.capitalize(entity);
					//wparam = buildWparamArray(params);

					if (!jsp.endsWith("ViewData.jsp")) {
						jsp += "ViewData.jsp";
					} else if (!jsp.endsWith(".jsp")) {
						jsp += ".jsp";
					}
					request.setAttribute("jsp", jsp);

					//log.debug("SQL, BeanName, WHERE, wparam, jsp: " +
					//		entity + ", " + beanName + ", " + whereKey + ", (" + params + "), " + jsp);


				} else if (view.equals("dd")) {
					//////////////////////////////////////////
					// VIEW: dd
					//////////////////////////////////////////
					command = FORWARD;
					fwdURL = "/dbdictionary/dd~table~" + entity;
					if (Utility.isStringNullOrEmpty(params)) {
						fwdURL += "~type~tableview.html";
					} else {
						// add a field
						fwdURL += "~field~" + params + "~type~fieldview.html";
					}

				} else if (view.equals("xml")) {
					//////////////////////////////////////////
					// VIEW: xml
					//////////////////////////////////////////
					//command = "GetXML";
					// make a custom VegbankCommand maybe

				} else if (view.equals("bean")) {
					//////////////////////////////////////////
					// VIEW: bean
					//////////////////////////////////////////
					command = RETRIEVE_BEAN;

				//} else if (view.equals("detail") || view.equals("summary")) {
				} else {
					//////////////////////////////////////////
					// VIEWS: detail or summary or catch-all
					//////////////////////////////////////////
					command = FORWARD;
					wparam = buildWparamArray(params);
                    if (Utility.isArrayNullOrEmpty(wparam)) {
                        if ( view.equals("detail") ) {
                            log.debug("no wparam on detail view, so using summary view"); 
                            view = "summary";
                        }
                    }

					fwdURL = "/views/" + entity + "_" + view + ".jsp";

					// see if file exists
					File viewFile = new File(Utility.WEBAPP_DIR + fwdURL);

					if (!viewFile.exists()) {
						// no view, try the other one
						if (view.equals("detail")) {
							fwdURL = "/views/" + entity + "_summary.jsp";
						} else {
							fwdURL = "/views/" + entity + "_detail.jsp";
						}

						viewFile = new File(Utility.WEBAPP_DIR + fwdURL);
						if (!viewFile.exists()) {
							// still no view
							errors.add(ActionErrors.GLOBAL_ERROR,
								new ActionError("errors.general", "No views for " + entity));
						}
					}
					


					if (!Utility.isStringNullOrEmpty(params)) {
						if (params.indexOf(Utility.PARAM_DELIM) == -1) {
							// not an aggregate
							// add the delimited list as one wparam
							String wparamList = "";

							boolean first = true;
							for (int i=0; i<wparam.length; i++) {
								if (first) {
									first = false;
								} else {
									wparamList += ",";
								}

								wparamList += wparam[i];
							}
							urlParams.put("wparam", wparamList);
							log.debug("PMAPMAPMA: wparam is being set to: " + wparamList);

						} else {
							// add the whole array
							urlParams.put("wparam", wparam);
						}


						/*
						fwdURL += "?wparam=";
						boolean first = true;
						for (int i=0; i<wparam.length; i++) {
							if (!first) {
								fwdURL += ",";
							}
							fwdURL += wparam[i];
						}
						*/

						/*
						//
						// Add a wparam for each param
						//
						fwdURL += "?";
						boolean first = true;
						for (int i=0; i<wparam.length; i++) {
							if (!first) {
								fwdURL += "&";
							}
							fwdURL += "wparam=" + wparam[i];
						}
						*/
					}


				} 

			}



			if ( command.equalsIgnoreCase(GENERIC_CMD)) {
				log.debug( "GD: executing new gen cmd" );
		        String orderBy = request.getParameter("orderBy");
				GenericCommandStatic.execute(request, selectKey, whereKey, beanName, orderBy, wparam);
				log.debug( "GD: done executing GC" );


			} else if ( command.equalsIgnoreCase(RETRIEVE_BEAN)) {
				log.debug( "GD: executing new RetriveVBModelBean cmd" );
				VBModelBean bean = new RetrieveVBModelBean().execute(accessionCode);
				String rootEntity = VBObjectUtils.getUnQualifiedName( bean.getClass().getName() );
				log.debug("Putting genericBean in request " + bean);
				request.setAttribute("genericBean",  bean);
				log.debug( "GD: done executing RetriveVBModelBean" );
				
				//Testing out
				HttpSession session = request.getSession();

				// get more request params
				String expandEntity = request.getParameter("expandEntity");
				String contractEntity = request.getParameter("contractEntity");
				
				ExpandStatusStore ess = 
					(ExpandStatusStore) session.getAttribute("DisplayProps");
				if ( ess == null )
				{	
					ess = new ExpandStatusStore();
				}
				
				if ( expandEntity != null )
				{
					log.debug("Expand: " + expandEntity);
					ess.setNodeExpanded(expandEntity);
				}
				if ( contractEntity != null )
				{
					log.debug("Expand: " + contractEntity);
					ess.setNodeContracted(contractEntity);
				}
				
				// put in  session
				session.setAttribute("DisplayProps", ess);
				log.debug("Setting rootEntity = " + rootEntity);
				request.setAttribute(ROOT_ENTITY,rootEntity);
				request.setAttribute(Constants.ACCESSIONCODENAME,accessionCode);


			} else if ( command.equalsIgnoreCase(FORWARD)) {
				if (Utility.isStringNullOrEmpty(fwdURL)) {
					fwdURL = request.getParameter("fwd");
				}

				fwdURL += ServletUtility.buildQueryString(urlParams);
				log.debug("Forwarding to " + fwdURL);
				return new ActionForward(fwdURL, true);


			} else {
				Object commandObject = Utility.createObject(commandLocation + command);
				Class commandClass = commandObject.getClass();
				Class[] parameterTypes = null;
				//	{ Class.forName(" javax.servlet.http.HttpServletRequest"),  
				//		Class.forName("javax.servlet.http.HttpServletResponse"), };
				Method commandExecute = commandClass.getMethod("execute",parameterTypes);
				Object[] arguments = null;//{ request, response };
				Collection collection = (Collection) commandExecute.invoke(commandObject, arguments);
				
				request.setAttribute("genericBean",  collection);
			}
			
			// Forward to a jsp
			log.debug( "GD: fwd to" + jspLocation + jsp );
			RequestDispatcher dispatcher = request.getRequestDispatcher(jspLocation + jsp);
			log.debug( "GD: got dispatcher");
			log.debug( "rootEntity = " + request.getAttribute(ROOT_ENTITY) );
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.resource.not.found", e.getMessage()));
			log.debug( e.getMessage(),e );
		}
		
		// Report any errors we have discovered to failure page
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.findForward("failure"));
		}
		return null;	
	}
	

	/**
	 *
	 */
	private String buildWhereKey(String entity, String delimitedParams) {
		if (Utility.isStringNullOrEmpty(delimitedParams)) {
			return entity;
		}

		if (Utility.isNumericList(delimitedParams)) {
			return "where_" + entity + "_pk";
		} else {
			return "where_accessioncode";
		}

	}


	/**
	 * Simply splits the given params into an array using
     * Utility.PARAM_DELIM, if it exists in the string.
	 * @return a List of values
	 */
	private String[] buildWparamArray(String delimitedParams) {
		if (Utility.isStringNullOrEmpty(delimitedParams)) {
			return null;
		}

        String[] arr = delimitedParams.split(Utility.PARAM_DELIM);

        for (int j=0; j<arr.length; j++) {
            arr[j] = arr[j].toLowerCase();
        }

        /*
		//StringTokenizer st = new StringTokenizer(delimitedParams, Utility.PARAM_DELIM);
		int numTokens = st.countTokens();
		String[] arr = new String[numTokens];
		String param;
		int i=0;
		while (st.hasMoreTokens()) {
			param = st.nextToken(); 
			arr[i++] = param.toLowerCase();
		}
        */

		/*
		if (numTokens > 1) {
			while (st.hasMoreTokens()) {
				param = st.nextToken(); 
				//arr[i] = "'" + param.toLowerCase() + "'";
				arr[i] = param.toLowerCase();
				i++;
			}
		} else {
			arr[0] = delimitedParams.toLowerCase();
		}
		*/

		return arr;
	}

}
