/*
 * Created on Dec 3, 2003
 *
 * '$RCSfile: RetrieveVBModelBean.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-27 19:10:32 $'
 *	'$Revision: 1.2 $'
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
package org.vegbank.common.command;

import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.plots.datasource.DBModelBeanReader;

/**
 * Get a <code>VBModelBean</code> tree.
 * 
 * @author Gabriel Farrell
 * @version '$Revision: 1.2 $' '$Date: 2004-02-27 19:10:32 $'
 */
public class RetrieveVBModelBean
{

	public VBModelBean execute(String accessionCode)
		throws Exception
	{
		VBModelBean bean = null;
		if ( ! Utility.isStringNullOrEmpty(accessionCode) )
		{
			DBModelBeanReader mbReader = new DBModelBeanReader();
			bean =  mbReader.getVBModelBean(accessionCode);
			mbReader.releaseConnection();
		}
		else
		{
			// I can't do anything else .. throw error
			throw new Exception("RetriveVBModelBeanTree: Recieved a unrecognized request");
		}
		return bean;
	}

}
