/*
 *	'$RCSfile: AddStratumMethodForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:13 $'
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
 
package org.vegbank.ui.struts;

import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.command.QueryReferences;
import org.vegbank.common.model.Stratummethod;

/**
 * @author farrell
 */

public class AddStratumMethodForm extends ValidatorForm
{
	
	private Stratummethod stratumMethod = new Stratummethod();
	
	private Collection references;
	
	// CoverIndex values
	private String[] stratumName = new String[20];
	private String[] stratumIndex = new String[20];
	private String[] stratumDescription = new String[20];



	/**
	 * @return
	 */
	public Collection getReferences()
	{
		if ( references == null )
		{
			// Need to create this object
			QueryReferences qr = new QueryReferences();
			try
			{
				references = qr.execute();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return references;
	}

	/**
	 * @param set
	 */
	public void setReferences(Collection col)
	{
		references = col;
	}

	/**
	 * @return
	 */
	public Stratummethod getStratumMethod()
	{
		return stratumMethod;
	}

	/**
	 * @param method
	 */
	public void setStratumMethod(Stratummethod method)
	{
		stratumMethod = method;
	}

	/**
	 * @return
	 */
	public String[] getStratumDescription()
	{
		return stratumDescription;
	}

	/**
	 * @return
	 */
	public String[] getStratumIndex()
	{
		return stratumIndex;
	}

	/**
	 * @return
	 */
	public String[] getStratumName()
	{
		return stratumName;
	}

	/**
	 * @param strings
	 */
	public void setStratumDescription(String[] strings)
	{
		stratumDescription = strings;
	}

	/**
	 * @param strings
	 */
	public void setStratumIndex(String[] strings)
	{
		stratumIndex = strings;
	}

	/**
	 * @param strings
	 */
	public void setStratumName(String[] strings)
	{
		stratumName = strings;
	}

}
