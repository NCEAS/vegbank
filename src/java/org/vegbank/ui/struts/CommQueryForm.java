/*
 * '$RCSfile: CommQueryForm.java,v $' Authors: @author@ Release: @release@
 * 
 * '$Author: farrell $' '$Date: 2004-03-05 22:45:16 $' '$Revision: 1.1 $'
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.vegbank.ui.struts;

import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.model.Commstatus;
import org.vegbank.common.model.Commusage;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.LogUtility;


/**
 * @author Gabriel
 */

public class CommQueryForm extends ValidatorForm
{
	// Get the properties of the form
	private String commname;
	private String[] nameType= new String[20];
	private String[] taxonLevel = new String[20];
	private String nameExistsBeforeDate;
	private String accordingToParty;
	
	// Picklist for party .. use what exists in database.
	private Collection partyNameIds;
	private Collection commLevels;
	private Collection commClassSystems;


  /**
   * @return All valid values for CommLevel
   */
  public Collection getCommLevels()
  {
    if ( commLevels == null )
    {
			// Need to create this object
			Commstatus commStatus = new Commstatus();
			commLevels = commStatus.getCommlevelPickList();
    }
    return commLevels;
  }

	/**
	 * Gets relevant party data
	 * 
	 * @return void
	 */
	private Collection findPartyNameIdsInDB() {
		
		Vector partyIdNames = new Vector();
	
		StringBuffer partyQuery = new StringBuffer(512)
				.append("SELECT party_id, salutation, givenname, middlename, surname, organizationname  ")
				.append("FROM party WHERE party_id IN ( SELECT DISTINCT party_id FROM commstatus )");
	
		DatabaseUtility.getPartyValueLabelBeans(partyIdNames, partyQuery);
		return partyIdNames;
	}
	
	/**
	 * 
	 * @return Collection of <code>LabelValueBeans</code> containing partyId and 
	 * a readable lable.
	 */
	public Collection getPartyNameIds()
	{
		partyNameIds = this.findPartyNameIdsInDB();
		return partyNameIds;
	}
	
	/**
	 * @return Returns the acordingToParty.
	 */
	public String getAccordingToParty()
	{
		return accordingToParty;
	}
	/**
	 * @param acordingToParty The acordingToParty to set.
	 */
	public void setAccordingToParty(String accordingToParty)
	{
		this.accordingToParty = accordingToParty;
	}
	/**
	 * @return Returns the nameExistsBeforeDate.
	 */
	public String getNameExistsBeforeDate()
	{
		return nameExistsBeforeDate;
	}
	/**
	 * @param nameExistsBeforeDate The nameExistsBeforeDate to set.
	 */
	public void setNameExistsBeforeDate(String nameExistsBeforeDate)
	{
		this.nameExistsBeforeDate = nameExistsBeforeDate;
	}
	/**
	 * @return Returns the nameType.
	 */
	public String[] getNameType()
	{
		return nameType;
	}
	/**
	 * @param nameType The nameType to set.
	 */
	public void setNameType(String[] nameType)
	{
		this.nameType = nameType;
	}
	/**
	 * @return Returns the plantname.
	 */
	public String getCommname()
	{
		return commname;
	}
	/**
	 * @param plantname The plantname to set.
	 */
	public void setCommname(String commname)
	{
		this.commname = commname;
	}
	/**
	 * @return Returns the taxonLevel.
	 */
	public String[] getTaxonLevel()
	{
		return taxonLevel;
	}
	/**
	 * @param taxonLevel The taxonLevel to set.
	 */
	public void setTaxonLevel(String[] taxonLevel)
	{
		this.taxonLevel = taxonLevel;
	}
	/**
	 * @return Returns the commClassSystems.
	 */
	public Collection getCommClassSystems()
	{
    if ( this.commClassSystems == null )
    {
			// Using all the system defined values ignore 
    	// values added by users to db, ANY handles that!
			Commusage commUsage = new Commusage();
			commClassSystems = commUsage.getClasssystemOpenPickList();
    }
		return commClassSystems;
	}
}
