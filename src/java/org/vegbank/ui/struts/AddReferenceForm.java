/*
 * Created on May 2, 2003
 *
 *	'$RCSfile: AddReferenceForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-16 02:48:34 $'
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.command.QueryJournals;
import org.vegbank.common.model.Reference;
import org.vegbank.common.model.ReferenceContributor;

/**
 * @author farrell
 *
 * Struts ActionForm for the AddReference form.
 */
public class AddReferenceForm extends ValidatorForm
{
	
	private Reference  reference = new Reference();
	
	private Collection journals;
  private Collection roleTypes;
  
	private String[] system = new String[5];
	private String[] identifier = new String[5];;
	private String[] party = new String[10];
	private String[] roletype = new String[10];
		
	
	/**
	 * Validate the properties that have been set from this HTTP request,
	 * and return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no
	 * recorded error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request)
	{

		System.out.println("Validate got called");
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		return errors;

	}
	
	/**
	 * @return
	 */
	public Reference getReference()
	{
		return reference;
	}

	/**
	 * @param reference
	 */
	public void setReference(Reference reference)
	{
		this.reference = reference;
	}

	/**
	 * @return
	 */
	public String[] getIdentifier()
	{
		return identifier;
	}

	/**
	 * @return
	 */
	public String[] getParty()
	{
		return party;
	}

	/**
	 * @return
	 */
	public String[] getRoletype()
	{
		return roletype;
	}

	/**
	 * @return
	 */
	public String[] getSystem()
	{
		return system;
	}

	/**
	 * @param strings
	 */
	public void setIdentifier(String[] strings)
	{
		identifier = strings;
	}

	/**
	 * @param strings
	 */
	public void setParty(String[] strings)
	{
		party = strings;
	}

	/**
	 * @param strings
	 */
	public void setRoletype(String[] strings)
	{
		roletype = strings;
	}

	/**
	 * @param strings
	 */
	public void setSystem(String[] strings)
	{
		system = strings;
	}
	
	/**
	 * @return
	 */
	public Collection getJournals()
	{
		if ( journals == null )
		{
			// Need to create this object
			QueryJournals qr = new QueryJournals();
			try
			{
				journals = qr.execute();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return journals;
	}

	/**
	 * @return
	 */
	public Collection getRoleTypes()
	{
		if ( roleTypes == null )
		{
			// Need to create this object
      ReferenceContributor rc = new ReferenceContributor();
	    roleTypes = rc.getRoleTypePickList();	
		}
		return roleTypes;
	}

}
