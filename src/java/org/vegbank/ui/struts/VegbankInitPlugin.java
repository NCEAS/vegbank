/*
 * Created on Nov 26, 2003
 *
 * '$RCSfile: VegbankInitPlugin.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-17 02:53:20 $'
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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.Constants;
import org.vegbank.common.model.utility.Countrylist;
import org.vegbank.common.model.utility.Statelist;
import org.vegbank.common.model.utility.Regionlist;
import org.vegbank.common.model.utility.Certstatuslist;

/**
 * Set up beans in application scope for the vegbank webapp.
 * 
 * @author Gabriel
 **/
public class VegbankInitPlugin implements PlugIn
{
	private static Log log = LogFactory.getLog(VegbankInitPlugin.class);

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#destroy()
	 */
	public void destroy()
	{
		// Nothing to do yet.
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 */
	public void init(ActionServlet servlet, ModuleConfig config)
		throws ServletException
	{
		ServletContext sc = servlet.getServletContext();
		
		// Make the initialized beans available in application scope
		sc.setAttribute(Constants.STATELISTBEAN_KEY, new Statelist());
		sc.setAttribute(Constants.COUNTRYLISTBEAN_KEY, new Countrylist());
		sc.setAttribute(Constants.REGIONLISTBEAN_KEY, new Regionlist());
		sc.setAttribute(Constants.CERTSTATUSLISTBEAN_KEY, new Certstatuslist());
		log.info("Watching log4j.properties");
		PropertyConfigurator.configureAndWatch("log4j.properties", 20 * 1000);
	}

}
