/*
 * '$RCSfile: QueryHelper.java,v $' 
 *  Authors: @author@ 
 *  Release: @release@
 * 
 * '$Author: anderson $' 
 * '$Date: 2005-01-24 18:29:33 $' 
 * '$Revision: 1.6 $'
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

package org.vegbank.ui.struts.helpers;

import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.ui.struts.CommQueryForm;
import org.vegbank.ui.struts.PlantQueryForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Gabriel
 * 
 * Some helpers for Query Generation.
 *  
 */
public class QueryHelper
{
	private static Log log = LogFactory.getLog(QueryHelper.class);

	/**
	 * Get a query for communities constructed from form.
	 * 
	 * @param CommQueryForm
	 * @return StringBuffer containing SQL query.
	 */
	public static StringBuffer getCommQuerySQL(CommQueryForm commForm)
	{
		// Get the properties of the form
		String commname = commForm.getCommname();
		String[] nameType = commForm.getNameType();
		String[] taxonLevel = commForm.getTaxonLevel();
		String nameExistsBeforeDate = commForm.getNameExistsBeforeDate();
		String acordingToParty = commForm.getAccordingToParty();

		return getCommQuerySQL(commname, taxonLevel, nameExistsBeforeDate,
				acordingToParty, nameType);
	}

	/**
	 * Get a query for communities constructed for parameters.
	 * 
	 * @param commname
	 * @param taxonLevel
	 * @param nameExistsBeforeDate
	 * @param acordingToParty
	 * @return
	 */
	private static StringBuffer getCommQuerySQL(String commname,
			String[] taxonLevel, String nameExistsBeforeDate, String acordingToParty, String[] nameType)
	{

		// Get sci name, code and common name for this plant
		String sharedGetNameType = " ( SELECT commname.commname FROM commusage NATURAL JOIN commname where commusage.commconcept_id = commconcept.commconcept_id AND commusage.classsystem =";
		String selectCodeName = sharedGetNameType + "'CEGL' ) AS Code";
		String selectCommonName = sharedGetNameType
				+ "'English Common' ) AS \"English Common\"";
		String selectSciName = sharedGetNameType + "'NVC' ) AS \"  Scientific\"";

		// Query Construct
		StringBuffer query = new StringBuffer();
		query
				.append(" SELECT DISTINCT(commconcept.accessioncode), "
						+ selectCodeName
						+ ","
						+ selectCommonName
						+ ","
						+ selectSciName
						+ " FROM "
						+ "  (commconcept "
						+ "    JOIN "
						+ "     ( commname JOIN commusage ON commname.commname_id = commusage.commname_id ) "
						+ "    ON commusage.commconcept_id = commconcept.commconcept_id "
						+ "  ) " + "  JOIN commstatus "
						+ "    ON commstatus.commconcept_id = commconcept.commconcept_id "
						+ " WHERE UPPER(commname.commname) like '" + commname.toUpperCase()
						+ "'");

		// AND classsytem is
		query.append(DatabaseUtility.handleValueList(nameType,
				"commusage.classsystem", " AND "));
		
		// AND taxonLevel is
		query.append(DatabaseUtility.handleValueList(taxonLevel,
				"commstatus.commlevel", " AND "));

		// AND date is
		String stopdateField = "commstatus.stopdate";
		String startdateField = "commstatus.startdate";
		if (Utility.isStringNullOrEmpty(nameExistsBeforeDate))
		{
			// Use todays date
			nameExistsBeforeDate = Utility.dbAdapter.getDateTimeFunction();
		}
		query.append(isDateInRange(nameExistsBeforeDate, startdateField,
				stopdateField));

		// Wire up the Party
		if (!Utility.isStringNullOrEmpty(acordingToParty))
			query.append(" AND commstatus.party_id = " + acordingToParty);

		return query;
	}

	/**
	 * @param nameExistsBeforeDate
	 * @return
	 */
	private static String isDateInRange(String nameExistsBeforeDate,
			String startdateField, String stopDateField)
	{
		return " AND '" + nameExistsBeforeDate + "' >= " + startdateField + " AND "
				+ "( " + stopDateField + " <= '" + nameExistsBeforeDate + "' OR "
				+ stopDateField + " IS NULL)";
	}

	/**
	 * Get a query for plant constructed from form.
	 * 
	 * @param PlantQueryForm
	 * @return StringBuffer containing SQL query
	 */
	public static StringBuffer getPlantQuerySQL(PlantQueryForm form)
	{
		// Get the properties of the form
		return getPlantQuerySQL(
				form.getPlantname(),
				form.getTaxonLevel(),
				form.getNameExistsBeforeDate(),
				form.getAccordingToParty(),
				form.getNameType() );
	}

	/**
	 * Get a query constructed for the parameters passed in.
	 * 
	 * @param plantname
	 * @param taxonLevel
	 * @param nameExistsBeforeDate
	 * @param acordingToParty
	 * @param nameType
	 * @return StringBuffer containing SQL query
	 */
	private static StringBuffer getPlantQuerySQL(String plantname,
			String[] taxonLevel, String nameExistsBeforeDate, String acordingToParty,
			String[] nameType)
	{
		// Get sci name, code and common name for this plant
		String sharedGetNameType = " ( SELECT plantname.plantname FROM plantusage NATURAL JOIN plantname where plantusage.plantconcept_id = plantconcept.plantconcept_id AND plantusage.classsystem =";
		String selectCodeName = sharedGetNameType + "'Code' ) AS Code";
		String selectCommonName = sharedGetNameType
				+ "'English Common' ) AS \"English Common Name\"";
		String selectSciName = sharedGetNameType
				+ "'Scientific' ) AS \"Scientific Name\"";

		if (Utility.AUTO_APPEND_WILDCARD && !Utility.isStringNullOrEmpty(plantname) &&
				!plantname.endsWith("%")) {
			plantname += "%";
		}

		// Query Construct
		StringBuffer query = new StringBuffer();
		query
				.append(" SELECT DISTINCT(plantconcept.accessioncode) , "
						+ selectCodeName
						+ ","
						+ selectCommonName
						+ ","
						+ selectSciName
						+ " FROM "
						+ "  (plantconcept "
						+ "    JOIN "
						+ "     ( plantname JOIN plantusage ON plantname.plantname_id = plantusage.plantname_id ) "
						+ "    ON plantusage.plantconcept_id = plantconcept.plantconcept_id "
						+ "  ) "
						+ "  JOIN plantstatus "
						+ "    ON plantstatus.plantconcept_id = plantconcept.plantconcept_id "
						+ " WHERE UPPER(plantname.plantname) like '"
						+ plantname.toUpperCase() + "'");

		// AND classsytem is
		query.append(DatabaseUtility.handleValueList(nameType,
				"plantusage.classsystem", " AND "));

		// AND taxonLevel is
		query.append(DatabaseUtility.handleValueList(taxonLevel,
				"plantstatus.plantlevel", " AND "));
		
		// AND date is
		String stopdateField = "plantstatus.stopdate";
		String startdateField = "plantstatus.startdate";
		if (Utility.isStringNullOrEmpty(nameExistsBeforeDate))
		{
			// Use todays date
			nameExistsBeforeDate = Utility.dbAdapter.getDateTimeFunction();
		}
		query.append(isDateInRange(nameExistsBeforeDate, startdateField,
				stopdateField));

		// Wire up the Party
		if (!Utility.isStringNullOrEmpty(acordingToParty))
			query.append(" AND plantstatus.party_id = " + acordingToParty);

		log.debug("generated plant query sql: " + query);
		return query;
	}

}
