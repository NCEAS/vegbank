/*
 * Created on Feb 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vegbank.ui.struts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.vegbank.common.model.Plantstatus;
import org.vegbank.common.model.Plantusage;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;

/**
 * @author Gabriel
 */

public class PlantQueryForm extends ActionForm
{
	// Get the properties of the form
	private String plantname;
	private String nameType;
	private String[] taxonLevel = new String[20];
	private String nameExistsBeforeDate;
	private String accordingToParty;
	
	// Picklist for party .. use what exists in database.
	private Collection partyNameIds;
	private Collection plantLevels;
	// FIXME: This is an open list -- not sure what to do here
	// for now hardcoded in the jsp PlantQuery.jsp
	private Collection plantClassSystems;


  /**
   * @return All valid values for plantLevel
   */
  public Collection getPlantLevels()
  {
    if ( plantLevels == null )
    {
			// Need to create this object
			Plantstatus plantStatus = new Plantstatus();
			plantLevels = plantStatus.getPlantlevelPickList();
    }
    return plantLevels;
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
				.append("FROM party WHERE party_id IN ( SELECT DISTINCT party_id FROM plantstatus )");
	
		try
		{
			DatabaseAccess da = new DatabaseAccess();
	
			// hit the DB, plot
			ResultSet rs = da.issueSelect(partyQuery.toString());		
	
			while (rs.next())
			{		
				String partyId  = rs.getString(1);
				String salutation = rs.getString(2);
				String givenName = rs.getString(3);
				String middleName = rs.getString(4);
				String surName = rs.getString(5);
				String organizationName = rs.getString(6);
				
				StringBuffer partyName = new StringBuffer();
				
				if ( Utility.isStringNullOrEmpty(surName) )
				{
					partyName.append(organizationName);
				}
				else
				{
					// Construct the party name 
					// e.g. 'Dr. James William Smith (NCEAS)'							
					if ( Utility.isStringNullOrEmpty(salutation) )
					{
						partyName.append(salutation + " ");
					}
					if ( Utility.isStringNullOrEmpty(givenName) )
					{
						partyName.append(givenName + " ");
					}
					if ( Utility.isStringNullOrEmpty(middleName) )
					{
						partyName.append(middleName + " ");
					}
					if ( Utility.isStringNullOrEmpty(surName) )
					{
						partyName.append(surName + " ");
					}
					if ( Utility.isStringNullOrEmpty(organizationName) )
					{
						partyName.append("(" + organizationName + ")");
					}
				}
				
				LabelValueBean partyNameId = new LabelValueBean(partyName.toString(), partyId);
				partyIdNames.add(partyNameId);
			}
		}
		catch (SQLException e1)
		{
			LogUtility.log("org.vegbank.ui.struts.PlantQueryForm:: " +
				"findPartyNameIdsInDB() ERROR: " + e1.getMessage(), e1);
		}
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
	public String getNameType()
	{
		return nameType;
	}
	/**
	 * @param nameType The nameType to set.
	 */
	public void setNameType(String nameType)
	{
		this.nameType = nameType;
	}
	/**
	 * @return Returns the plantname.
	 */
	public String getPlantname()
	{
		return plantname;
	}
	/**
	 * @param plantname The plantname to set.
	 */
	public void setPlantname(String plantname)
	{
		this.plantname = plantname;
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
}
