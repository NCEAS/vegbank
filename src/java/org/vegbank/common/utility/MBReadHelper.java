package org.vegbank.common.utility;

import java.util.Iterator;
import java.util.List;

import org.vegbank.common.model.Taxonimportance;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;

/*
 * '$RCSfile: MBReadHelper.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-25 19:27:01 $'
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
/**
 * @author farrell
 *
 * There are some rules for searching for information in the datamodel beans 
 * that are complex, this is an attempt to remove duplication with this task 
 * and therefore consistency of return results.
 * 
 */
public class MBReadHelper
{
	
	/**
	 * Convience method for looking up a plantName from taxonobservation bean.
	 * 
	 * Look for best case first then desend down to the less desirable locations. Wrap
	 * the unaccepted plantNames in *** *** to indicate that fact
	 * 
	 * @param to
	 * @return String -- plantName
	 */
	public static String getPlantName( Taxonobservation to)
	{
		// Get the current interpritation ... if none then return the author name for the plant
		String plantName = "";
		Iterator taxonInterpritations = to.gettaxonobservation_taxoninterpretations().iterator();
		while( taxonInterpritations.hasNext() )
		{
			Taxoninterpretation ti =  (Taxoninterpretation) taxonInterpritations.next();
			
			// Converting a String to boolean ... risky
			//LogUtility.log("Is this the current interpritation ?" + ti.getCurrentinterpretation() );
			if ( Utility.isTrue(ti.getCurrentinterpretation()) )
			{
				plantName = ti.getPlantconceptobject().getPlantnameobject().getPlantname();
				//LogUtility.log("The Plantname is  ?" +plantName );
			}
			
			if (  Utility.isStringNullOrEmpty(plantName) )
			{
				String authorName = to.getAuthorplantname();
				// No valid taxonInterpritation found use the authors name for the plant
				// *** to indicate on the ui that this is not accepted yet .
				plantName = "*** " + authorName + " ***";
			}

		}
		return plantName;
	}
	
	/**
	 * Convience method for looking up the total  cover from a 
	 * taxonobservation bean.
	 * 
	 * @param to
	 * @return String -- plantName
	 */
	public static String getTotalTaxonCover(Taxonobservation to)
	{
		String taxonCover = "";
		List taxonImportanceList = to.gettaxonobservation_taxonimportances();
		Iterator it = taxonImportanceList.iterator();
		while (it.hasNext())
		{
			Taxonimportance ti = (Taxonimportance) it.next();

			Object object = ti.getStratumobject();
			if (object == null)
			{
				// FIXME: Bussiness rule/ Usage Rule
				// This is the one we want, otherwise its just for a strata
				taxonCover = ti.getCover();
			}
		}
		return taxonCover;
	}


}
