/*
 *	'$RCSfile: AddCoverMethodForm.java,v $'
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
import org.vegbank.common.model.Covermethod;

/**
 * @author farrell
 */

public class AddCoverMethodForm extends ValidatorForm
{
	
	private Covermethod coverMethod = new Covermethod();
	
	private Collection references;
	
	// CoverIndex values
	private String[] coverCode = new String[20];
	private String[] coverPercent = new String[20];
	private String[] lowerLimit = new String[20];
	private String[] upperLimit = new String[20];
	private String[] indexDescription = new String[20];

	/**
	 * @return
	 */
	public Covermethod getCoverMethod()
	{
		return coverMethod;
	}

	/**
	 * @param method
	 */
	public void setCoverMethod(Covermethod method)
	{
		coverMethod = method;
	}

	/**
	 * @return
	 */
	public String[] getCoverCode()
	{
		return coverCode;
	}

	/**
	 * @return
	 */
	public String[] getCoverPercent()
	{
		return coverPercent;
	}

	/**
	 * @return
	 */
	public String[] getIndexDescription()
	{
		return indexDescription;
	}

	/**
	 * @return
	 */
	public String[] getLowerLimit()
	{
		return lowerLimit;
	}

	/**
	 * @return
	 */
	public String[] getUpperLimit()
	{
		return upperLimit;
	}

	/**
	 * @param strings
	 */
	public void setCoverCode(String[] strings)
	{
		coverCode = strings;
	}

	/**
	 * @param strings
	 */
	public void setCoverPercent(String[] strings)
	{
		coverPercent = strings;
	}

	/**
	 * @param strings
	 */
	public void setIndexDescription(String[] strings)
	{
		indexDescription = strings;
	}

	/**
	 * @param strings
	 */
	public void setLowerLimit(String[] strings)
	{
		lowerLimit = strings;
	}

	/**
	 * @param strings
	 */
	public void setUpperLimit(String[] strings)
	{
		upperLimit = strings;
	}

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

}
