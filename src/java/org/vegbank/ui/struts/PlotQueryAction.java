/*
 *	'$RCSfile: PlotQueryAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-29 00:24:54 $'
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

package org.vegbank.ui.struts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class PlotQueryAction extends Action
{

	private static final String ANYVALUE = "ANY";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action PlotQueryAction");
		ActionErrors errors = new ActionErrors();

		StringBuffer query = new StringBuffer();
		query.append(
			" SELECT DISTINCT(observation.obsaccessionnumber), observation.authorobscode, "
				+ "plot.latitude, plot.longitude, plot.plot_id"
				+ " FROM plot, project, observation, taxonobservation, covermethod, stratummethod, "
				
				// Allow searching for Commusage for commname
				+ " (commConcept INNER JOIN (commClass INNER JOIN commInterpretation ON"
				+ " commClass.COMMCLASS_ID = commInterpretation.COMMCLASS_ID) ON "
				+ " commConcept.COMMCONCEPT_ID = commInterpretation.COMMCONCEPT_ID) " 
				+	"INNER JOIN commUsage ON commConcept.COMMCONCEPT_ID = commUsage.COMMCONCEPT_ID "
				// End Joining Observation to commUsage
				
				+ " WHERE plot.plot_id = observation.plot_id AND taxonobservation.observation_id = observation.observation_id "
				+ " AND project.project_id = observation.project_id AND observation.covermethod_id = covermethod.covermethod_id"
				+ " AND observation.stratummethod_id = stratummethod.stratummethod_id"
				+ " AND taxonobservation.observation_id = observation.observation_id"
				+ " AND commclass.observation_id = observation.observation_id"
		);

		// Get the form
		PlotQueryForm pqForm = (PlotQueryForm) form;
		String conjunction = pqForm.getConjunction();

		// States
		query.append(this.handleValueList(pqForm.getState(), "plot.state", conjunction));

		// Elevation
		//System.out.println(  pqForm.getMaxElevation() + " " + pqForm.getMinElevation() + " " +  pqForm.isAllowNullElevation() + " elevation" );
		query.append(
			this.handleMaxMinNull(
				pqForm.getMaxElevation(),
				pqForm.getMinElevation(),
				pqForm.isAllowNullElevation(),
				"plot.elevation",
				conjunction));

		// SlopeAspect
		query.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeAspect(),
				pqForm.getMinSlopeAspect(),
				pqForm.isAllowNullSlopeAspect(),
				"plot.slopeaspect",
				conjunction));

		// SlopeGradient
		query.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeGradient(),
				pqForm.getMinSlopeGradient(),
				pqForm.isAllowNullSlopeGradient(),
				"plot.slopegradient",
				conjunction));

		// rockType
		query.append(this.handleValueList(pqForm.getRockType(), "plot.rockType", conjunction));
		// surficalDeposits
		query.append(
			this.handleValueList(
				pqForm.getSurficialDeposit(),
				"plot.surficialdeposits",
				conjunction));
		// hydrologicregime
		query.append(
			this.handleValueList(
				pqForm.getSurficialDeposit(),
				"observation.hydrologicregime",
		conjunction));
		// topoposition
		query.append(
			this.handleValueList(pqForm.getTopoPosition(), "plot.topoposition", conjunction));
		// landForm
		query.append(this.handleValueList(pqForm.getLandForm(), "plot.landform", conjunction));

		// Date Observed
		// This need special handling because of the start/end points....
		query.append(
			this.handleMaxMinNullDateRange(
				pqForm.getMinObsStartDate(),
				pqForm.getMaxObsEndDate(),
				"observation.obsstartdate",
				"observation.obsenddate",
				pqForm.isAllowNullObsDate(),
				" AND ")
		);

		// Date Entered
		query.append(
			this.handleMaxMinNull(
				pqForm.getMaxDateEntered(),
				pqForm.getMinDateEntered(),
				false,
				"plot.dateentered",
				conjunction));

		// Plot Area
		query.append(
			this.handleMaxMinNull(
				pqForm.getMaxPlotArea(),
				pqForm.getMinPlotArea(),
				pqForm.isAllowNullPlotArea(),
				"plot.area",
				conjunction));

		// METHODS and PEOPLE
		query.append(
			this.handleSimpleCompare(
				pqForm.getCoverMethodType(),
				"covermethod.covertype",
				pqForm.isAllowNullCoverMethodType(),
				" like ", 
				conjunction));
		query.append(
			this.handleSimpleCompare(
				pqForm.getStratumMethodName(),
				"stratummethod.stratummethodname",
				pqForm.isAllowNullStratumMethodName(),
			" like ", 
			conjunction));

		// Need to traverse observation <- observationcontributor -> party ( multifield :O... )
		//query.apppend( this.handleSimpleEquals( pqForm.getObservationContributorName() 
		//					"") );
		query.append(
			this.handleSimpleCompare(
				pqForm.getProjectName(),
				"project.projectname",
				false,
				" like ", 
				conjunction));
		// name is multifield
		//query.append( this.handleSimpleEquals( pqForm.getPlotSubmitterName(),
		//					) );

		// VEGATATION
		String[] plantNames = pqForm.getPlantName();
		String[] minTaxonCover = pqForm.getMinTaxonCover();
		String[] maxTaxonCover = pqForm.getMaxTaxonCover();
		String[] minTaxonBasalArea = pqForm.getMinTaxonBasalArea();
		String[] maxTaxonBasalArea = pqForm.getMaxTaxonBasalArea();

		for (int i=0; i<plantNames.length; i++)
		{
			// If no plantname given then forget it...
			if ( ! Utility.isStringNullOrEmpty(plantNames[i]))
			{
				query.append(conjunction + " ( ");
				query.append( " plantname.plantname like '" + plantNames[i] + "'"
				+ " and plantname.plantname_id = taxonobservation.plantname_id");
				
				query.append(
					this.handleMaxMinNull(maxTaxonCover[i], minTaxonCover[i], true, "taxonobservation.taxoncover", " AND")
				);
				query.append(
					this.handleMaxMinNull(maxTaxonBasalArea[i], minTaxonBasalArea[i], true, "taxonobservation.taxonbasalarea", " AND")
				);

				query.append(" ) ");
			}
		}
		
		// COMMUNITIES
		String[] commNames = pqForm.getCommName();
		String[] maxCommStartDates = pqForm.getMaxCommStartDate();
		String[] minCommStopDates = pqForm.getMinCommStopDate();

		for (int i=0; i<commNames.length; i++)
		{
			// If no plantname given then forget it...
			if ( ! Utility.isStringNullOrEmpty(commNames[i]))
			{
				
				query.append(conjunction + " ( ");
				query.append( " commusage.commname like '" + commNames[i] + "'");
				
				query.append(
					this.handleMaxMinNullDateRange(
						minCommStopDates[i],
						maxCommStartDates[i],
						"commclass.classstartdate",
						"commclass.classstopdate",
						true, 
						" AND ")
				);
				
				query.append(" ) ");
			}
		}

		// FINISHED CONSTRUCTING QUERY

		// try to run this (potentially) monster query
		try
		{
			DatabaseAccess da = new DatabaseAccess();
			ResultSet rs = da.issueSelect(query.toString());

			Collection col = new Vector();
			while (rs.next())
			{
				PlotSummary plotsum = new PlotSummary();
				plotsum.setVegbankAccessionNumber(rs.getString(1));
				plotsum.setAuthorObservationCode(rs.getString(2));
				plotsum.setLatitude(rs.getString(3));
				plotsum.setLongitude(rs.getString(4));
				plotsum.setPlotId(rs.getString(5));
				col.add(plotsum);
			}
			request.setAttribute("PlotsResults", col);
		}
		catch (SQLException e1)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e1.getMessage() ,query.toString()));
			System.out.println(query.toString());
		}

		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}

		return mapping.findForward("DisplayResults");
	}

	private String handleSimpleCompare(
		String value,
		String fieldName,
		boolean allowNull,
		String operator,
		String conjunction)
	{
		StringBuffer sb = new StringBuffer();
		if (value == null || value.equals(""))
		{
			return "";
		}
		else
		{
			sb.append(conjunction + " ( ");
			sb.append(fieldName + " " + operator +" '" + value + "' ");
			if (allowNull)
			{
				sb.append(" OR " + fieldName + " IS NULL ");
			}
			sb.append(" ) ");
		}
		return sb.toString();
	}

	private boolean isAnyValueAllowed(String[] values)
	{
		for (int i = 0; i < values.length; i++)
		{
			//System.out.println("-->" + values + " " + values[i]);
			if (!(values[i] == null) && values[i].equals(ANYVALUE))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Generated SQL for option boxs where many options can be selected
	 * 
	 * Special handling for values ANY, IS NULL and IS NOT NULL 
	 * 
	 * @param values
	 * @param fieldName
	 * @return
	 */
	private String handleValueList(String[] values, String fieldName, String conjunction)
	{
		StringBuffer sb = new StringBuffer();

		if (values != null && values.length > 0 && !isAnyValueAllowed(values))
		{
			sb.append(
				conjunction + " ( " + fieldName + " = '" + values[0] + "'");
			for (int i = 0; i < values.length; i++)
			{
				if (values[i].trim().equals("IS NOT NULL")
					|| values[i].trim().equals("IS NULL"))
				{
					sb.append(" OR " + fieldName + " " + values[i] + " ");
				}
				else
				{
					sb.append(" OR " + fieldName + " ='" + values[i] + "'");
				}
			}
			sb.append(" ) ");
		}
		return sb.toString();
	}

	/**
	 * Generate the sql for max, min comparisons 
	 * 
	 * @param max
	 * @param min
	 * @param nullsAllowed
	 * @param fieldName
	 * @return
	 */
	private String handleMaxMinNull(
		String max,
		String min,
		boolean nullsAllowed,
		String fieldName,
		String conjunctionToUse)
	{
		StringBuffer sb = new StringBuffer();

		if (Utility.isStringNullOrEmpty(max) && Utility.isStringNullOrEmpty(min))
		{
			// Both empty nothing to be done
		}
		else
		{

			sb.append(conjunctionToUse);

			if (nullsAllowed)
			{
				sb.append(" ( ");
			}

			sb.append(" ( ");
			if (!Utility.isStringNullOrEmpty(max))
			{
				sb.append(" " + fieldName + " < " + max + " ");

				if (!Utility.isStringNullOrEmpty(min))
				{
					sb.append(" AND ");
				}
			}

			if (!Utility.isStringNullOrEmpty(min))
			{
				sb.append(" " + fieldName + " >" + min + " ");
			}
			sb.append(" ) ");

			if (nullsAllowed)
			{
				sb.append("  OR (" + fieldName + " IS NULL )  )");
			}
		}
		return sb.toString();
	}

	/**
	 * Generate the sql for max, min date range comparisons 
	 * 
	 * @param maxDate
	 * @param minDate
	 * @param endDateFieldName
	 * @param endDateFieldName
	 * @param allowNulls
	 * @param conjunctionToUse
	 * @return String -- SQL fragment
	 */
	private String handleMaxMinNullDateRange(
		String minDate,
		String maxDate,
		String startDateFieldName,
		String endDateFieldName,
		boolean allowNulls,
		String conjunctionToUse)
	{
		StringBuffer sb = new StringBuffer();
	
		if (Utility.isStringNullOrEmpty(maxDate) && Utility.isStringNullOrEmpty(minDate))
		{
			// Both empty nothing to be done
		}
		else
		{
	
			sb.append(conjunctionToUse);
			sb.append(" ( ");
	
			if (!Utility.isStringNullOrEmpty(minDate))
			{
				sb.append(" ( ");
				sb.append(" " + startDateFieldName + " >= '" + minDate + "' ");
				if ( allowNulls)
				{
					sb.append("  OR " + startDateFieldName + " IS NULL ");
				}
				sb.append(" ) ");
			}
	
			if (!Utility.isStringNullOrEmpty(maxDate))
			{
				sb.append(" AND ");
				sb.append(" ( ");
				sb.append(" " + endDateFieldName + " <= '" + maxDate + "' ");
				if ( allowNulls)
				{
					sb.append("  OR " + startDateFieldName + " IS NULL ");
				}
				sb.append(" ) ");
			}
			
			sb.append(" ) ");
		}
		return sb.toString();
	}

	/**
	 * @author farrell
	 *
	 * JavaBean to hold the plot summary.
	 */
	public class PlotSummary
	{
		private String authorObservationCode;
		private String vegbankAccessionNumber;
		private String latitude;
		private String longitude;
		private String plotId;

		public PlotSummary()
		{
		}

		/**
		 * @return
		 */
		public String getAuthorObservationCode()
		{
			return authorObservationCode;
		}

		/**
		 * @return
		 */
		public String getLatitude()
		{
			return latitude;
		}

		/**
		 * @return
		 */
		public String getLongitude()
		{
			return longitude;
		}

		/**
		 * @return
		 */
		public String getVegbankAccessionNumber()
		{
			return vegbankAccessionNumber;
		}

		/**
		 * @param string
		 */
		public void setAuthorObservationCode(String string)
		{
			authorObservationCode = string;
		}

		/**
		 * @param string
		 */
		public void setLatitude(String string)
		{
			latitude = string;
		}

		/**
		 * @param string
		 */
		public void setLongitude(String string)
		{
			longitude = string;
		}

		/**
		 * @param string
		 */
		public void setVegbankAccessionNumber(String string)
		{
			vegbankAccessionNumber = string;
		}

		/**
		 * @return
		 */
		public String getPlotId()
		{
			return plotId;
		}

		/**
		 * @param string
		 */
		public void setPlotId(String string)
		{
			plotId = string;
		}

	}
	
}
