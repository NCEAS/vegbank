/**	
 *  '$RCSfile: InterpretPlantForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-07-13 18:54:20 $'
 *	'$Revision: 1.3 $'
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

import java.sql.*;

import org.apache.struts.validator.ValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.PermComparison;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;

/**
 * @author anderson
 */

public class InterpretPlantForm extends ValidatorForm 
			implements java.io.Serializable
{
	private static Log log = LogFactory.getLog(InterpretPlantForm.class);

	// prepopulated fields
	private String tobsId;
	private String tobsAC;
	private String observation_id;
	private String plantName;
	private String taxonfit;
	private String interpretationtype;
	private String taxonconfidence;
	private String notes;
	private String pcAC;
	private Taxoninterpretation tint;


	/**
	 * Constructor
	 */
	public InterpretPlantForm() {
		super();
	}
	
	/**
	 * 
	 */
	public Taxoninterpretation getTaxonInterpretation() {
		if (tint == null) {
			log.debug("Creating new Taxoninterpretation");
			tint = new Taxoninterpretation();
		}

		tint.setTaxonfit(getTaxonfit());
		tint.setTaxonconfidence(getTaxonconfidence());
		tint.setInterpretationtype(getInterpretationtype());
		tint.setNotes(getNotes());
		return tint;
	}

	/**
	 *
	 */
	public void setTaxonInterpretation(Taxoninterpretation v) {
		tint = v;
	}

	/**
	 * 
	 */
	public String getTobsId() {
		return tobsId;
	}

	/**
	 *
	 */
	public void setTobsId(String s) {
		tobsId = s;
	}

	/**
	 * 
	 */
	public String getTobsAC() {
		return tobsAC;
	}

	/**
	 *
	 */
	public void setTobsAC(String s) {
		tobsAC = s;
	}

	/**
	 * 
	 */
	public String getObservation_id() {
		return observation_id;
	}

	/**
	 *
	 */
	public void setObservation_id(String s) {
		observation_id = s;
	}

	/**
	 * 
	 */
	public String getInterpretationtype() {
		return interpretationtype;
	}

	/**
	 *
	 */
	public void setInterpretationtype(String v) {
		interpretationtype = v;
	}

	/**
	 * 
	 */
	public String getTaxonconfidence() {
		return taxonconfidence;
	}

	/**
	 *
	 */
	public void setTaxonconfidence(String v) {
		taxonconfidence = v;
	}

	/**
	 * 
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 *
	 */
	public void setNotes(String v) {
		notes = v;
	}

	/**
	 * 
	 */
	public String getTaxonfit() {
		return taxonfit;
	}

	/**
	 *
	 */
	public void setTaxonfit(String v) {
		taxonfit = v;
	}

	/**
	 * 
	 */
	public String getPlantName() {
		if (plantName == null) {
			return null;
		} else {
			return plantName.toLowerCase();
		}
	}

	/**
	 *
	 */
	public void setPlantName(String v) {
		plantName = v;
	}

	/**
	 * 
	 */
	public String getPcAC() {
		return pcAC;
	}

	/**
	 *
	 */
	public void setPcAC(String v) {
		pcAC = v;
	}


}
