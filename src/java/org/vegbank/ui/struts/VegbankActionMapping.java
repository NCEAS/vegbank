package org.vegbank.ui.struts;

import org.apache.struts.action.ActionMapping;

/*
 * '$RCSfile: VegbankActionMapping.java,v $'
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
 * <p>
 * Implementation of enhanced <code>ActionMapping</code>.
 * It defines the following custom properties:
 * <ul>
 * <li><b>authClass</b> - The name of the authentication class which checks if the
 *     user is allowed to execute the action.</li>
 * <li><b>minCertLevel</b> - The minimum Certification Level is checked to see 
 *     if the user is allowed to execute the action.</li>
 * </ul>
 * </p>
 * 
 * @author farrell
 * @version $Revision: 1.1 $
 */
public class VegbankActionMapping extends ActionMapping
{

	protected String authClass;
	protected int minCertLevel = 0;
	
	/**
	 * Constructor
	 */
	public VegbankActionMapping()
	{
		super();
	}

	/**
	 * @return
	 */
	public String getAuthClass()
	{
		return authClass;
	}

	/**
	 * @return
	 */
	public int getMinCertLevel()
	{
		return minCertLevel;
	}

	/**
	 * @param string
	 */
	public void setAuthClass(String string)
	{
		authClass = string;
	}

	/**
	 * @param i
	 */
	public void setMinCertLevel(int i)
	{
		minCertLevel = i;
	}

}
