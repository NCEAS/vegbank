/*
 *	'$RCSfile: PlotQueryAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-10-06 20:39:32 $'
 *	'$Revision: 1.11 $'
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
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
	private static final String ANDVALUE = " AND ";
	private static final String ORVALUE = " OR ";
	private static final String 	selectClause =	
		" SELECT DISTINCT(observation.obsaccessionnumber), observation.authorobscode, " +	"plot.latitude, plot.longitude, observation.observation_id";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println(" In action PlotQueryAction");
		ActionErrors errors = new ActionErrors();

		StringBuffer query = new StringBuffer(1024);
		query.append(selectClause)
				.append(" FROM plot, project, observation, covermethod, stratummethod ")	
				.append(" WHERE plot.plot_id = observation.plot_id ")
				.append(" AND project.project_id = observation.project_id AND observation.covermethod_id = covermethod.covermethod_id")
				.append(" AND observation.stratummethod_id = stratummethod.stratummethod_id");

		// Get the form
		PlotQueryForm pqForm = (PlotQueryForm) form;
		String conjunction = pqForm.getConjunction();
		
		query.append(" AND ( ");

		StringBuffer dynamicQuery = new StringBuffer(1024);
		
		// Countries
		dynamicQuery.append(this.handleValueList(pqForm.getCountries(), "plot.country", conjunction));
		
		// States
		dynamicQuery.append(this.handleValueList(pqForm.getState(), "plot.state", conjunction));

		// Elevation
		//System.out.println(  pqForm.getMaxElevation())+ " " + pqForm.getMinElevation() + " " +  pqForm.isAllowNullElevation() + " elevation" );
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxElevation(),
				pqForm.getMinElevation(),
				pqForm.isAllowNullElevation(),
				"plot.elevation",
				conjunction));

		// SlopeAspect
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeAspect(),
				pqForm.getMinSlopeAspect(),
				pqForm.isAllowNullSlopeAspect(),
				"plot.slopeaspect",
				conjunction));

		// SlopeGradient
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeGradient(),
				pqForm.getMinSlopeGradient(),
				pqForm.isAllowNullSlopeGradient(),
				"plot.slopegradient",
				conjunction));

		// rockType
		dynamicQuery.append(this.handleValueList(pqForm.getRockType(), "plot.rockType", conjunction));
		// surficalDeposits
		dynamicQuery.append(
			this.handleValueList(
				pqForm.getSurficialDeposit(),
				"plot.surficialdeposits",
				conjunction));
		// hydrologicregime
		dynamicQuery.append(
			this.handleValueList(
				pqForm.getSurficialDeposit(),
				"observation.hydrologicregime",
				conjunction));
		// topoposition
		dynamicQuery.append(
			this.handleValueList(pqForm.getTopoPosition(), "plot.topoposition", conjunction));
		// landForm
		dynamicQuery.append(this.handleValueList(pqForm.getLandForm(), "plot.landform", conjunction));

		// Date Observed
		// This need special handling because of the start/end points....
		dynamicQuery.append(
			this.handleMaxMinNullDateRange(
				pqForm.getMinObsStartDate(),
				pqForm.getMaxObsEndDate(),
				"observation.obsstartdate",
				"observation.obsenddate",
				pqForm.isAllowNullObsDate(),
				" AND ")
		);

		// Date Entered
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxDateEntered(),
				pqForm.getMinDateEntered(),
				false,
				"plot.dateentered",
				conjunction));

		// Plot Area
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxPlotArea(),
				pqForm.getMinPlotArea(),
				pqForm.isAllowNullPlotArea(),
				"plot.area",
				conjunction));

		// METHODS and PEOPLE
		dynamicQuery.append(
			this.handleValueList(
				pqForm.getCoverMethodType(),
				"covermethod.covertype",
				conjunction)); 
				
//		dynamicQuery.append(
//			this.handleSimpleCompare(
//				pqForm.getCoverMethodType(),
//				"covermethod.covertype",
//				pqForm.isAllowNullCoverMethodType(),
//				" like ", 
//				conjunction));
				
		dynamicQuery.append(
			this.handleValueList(
				pqForm.getStratumMethodName(),
				"stratummethod.stratummethodname",
				conjunction)); 
				
//		dynamicQuery.append(
//			this.handleSimpleCompare(
//				pqForm.getStratumMethodName(),
//				"stratummethod.stratummethodname",
//				pqForm.isAllowNullStratumMethodName(),
//			" like ", 
//			conjunction));

		// Need to traverse observation <- observationcontributor -> party ( multifield :O... )
		//query.apppend( this.handleSimpleEquals( pqForm.getObservationContributorName() 
		//					"") );
		dynamicQuery.append(
			this.handleValueList(
				pqForm.getProjectName(),
				"project.projectname",
				conjunction)); 
		// name is multifield
		//query.append( this.handleSimpleEquals( pqForm.getPlotSubmitterName(),
		//					) );
		
		appendWhereClause(query, conjunction, dynamicQuery);
		
		// FINISHED CONSTRUCTING QUERY
		
		try
		{
			Vector resultSets = new Vector();
			DatabaseAccess da = new DatabaseAccess();
			
			// Get all resultsets
			resultSets.add( da.issueSelect(query.toString() ) );		
			getPlantResultSets(pqForm, resultSets);
			getCommunitiesResultSets(pqForm, resultSets);
			
			// Save the Collection of Summaries into the request
			request.setAttribute("PlotsResults", getPlotSummaries(conjunction, resultSets));
		}
		catch (SQLException e1)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e1.getMessage()));
			System.out.println(query.toString());
		}

		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}

		return mapping.findForward("displayResults");
	}

	private Collection getPlotSummaries(String conjunction, Vector resultSets)
		throws SQLException
	{
		System.out.println("Conjunction is '" + conjunction + "'");
		
		// Collection for adding valid object created from current resultset
		Hashtable workspace = new Hashtable();
		// Collection for storing keys that are still valid ( only needed for Intersection)
		Vector validKeys = new Vector();
		// Used in Union to capture all of first resultset.
		boolean isFirstResultSet = true;
		
		//System.out.println(">>> " + resultSets);
		Iterator results = resultSets.iterator();
		while ( results.hasNext() )
		{
			ResultSet rs = (ResultSet) results.next();
			//System.out.println("Processing " + rs.toString());
			
			// When doing a Intesection add to current collection all objects that existed
			// in oldWorkspace and current resultset.
			if ( conjunction.equals(ANDVALUE) ) 
			{
				validKeys.clear();
				//System.out.println("111===> " + validKeys);
				Enumeration keysEnum = workspace.keys();
				while ( keysEnum.hasMoreElements() )
				{
					validKeys.add(keysEnum.nextElement());
				}
				//System.out.println("222===> " + validKeys );
				workspace.clear();
			}
			
			
			while (rs.next())
			{
				PlotSummary plotsum = new PlotSummary();
				populatePlotSummary(rs, plotsum);
				if ( conjunction.equals(ANDVALUE) ) // Do a Intesection of all resultset
				{
					if ( isFirstResultSet )	// if empty dump all into collection
					{
						//System.out.println("#Adding " + plotsum.getVegbankAccessionNumber());
						workspace.put(plotsum.getVegbankAccessionNumber(), plotsum);
					}
					else //Only add element if in oldWorkspace
					{
						
						//System.out.println("3333===> " + validKeys);
						
						// Did this object exist in the previous results
						if ( validKeys.contains(plotsum.getVegbankAccessionNumber()))
						{
							//System.out.println("Adding " + plotsum.getVegbankAccessionNumber());
							workspace.put(plotsum.getVegbankAccessionNumber(), plotsum);
						}
						else
						{
							//System.out.println("Not Adding " + plotsum.getVegbankAccessionNumber());
							// Don't add this object
						}
					}
				}
				else // Concatonate without duplicates
				{
					if ( workspace.contains(plotsum.getVegbankAccessionNumber()))
					{
						//System.out.println("No need to ADD " + plotsum.getVegbankAccessionNumber());
						// No need to add
					}
					else
					{
						// Add this new object
						workspace.put(plotsum.getVegbankAccessionNumber(), plotsum);
						//System.out.println("ADD " + plotsum.getVegbankAccessionNumber());
					}
				}
			}
			isFirstResultSet = false;
		}
		Collection col = (Collection) workspace.values();
		return col;
	}

	private void populatePlotSummary(ResultSet rs, PlotSummary plotsum)
		throws SQLException
	{
		plotsum.setVegbankAccessionNumber(rs.getString(1));
		plotsum.setAuthorObservationCode(rs.getString(2));
		plotsum.setLatitude(rs.getString(3));
		plotsum.setLongitude(rs.getString(4));
		plotsum.setPlotId(rs.getString(5));
	}

	private void appendWhereClause(
		StringBuffer query,
		String conjunction,
		StringBuffer dynamicQuery)
	{
		// User added nothing to query
		if ( dynamicQuery.toString().trim().equals("") )
		{
			query.append(" true ");
		}
		else if ( conjunction.trim().equals("AND") )
		{
			query.append(" true ");
		} 
		else if ( conjunction.trim().equals("OR")  )
		{
			query.append(" false ");
		}
		
		query.append(dynamicQuery.toString());
		query.append(" ) ");
	}
	
	private void getPlantResultSets(PlotQueryForm pqForm, Vector plantResultSets) throws SQLException
	{
		// VEGATATION
		String[] plantNames = pqForm.getPlantName();
		String[] minTaxonCover = pqForm.getMinTaxonCover();
		String[] maxTaxonCover = pqForm.getMaxTaxonCover();
		
		// Loop over all the plantnames entered
		for (int i=0; i<plantNames.length; i++)
		{
			// If no plantname given then forget it...
			if ( ! Utility.isStringNullOrEmpty(plantNames[i]))
			{
				StringBuffer plantQuery = new StringBuffer(1024);
				plantQuery.append( selectClause )
					.append(" FROM plot JOIN " )
					.append("	(observation JOIN  " )
					.append("		(taxonobservation JOIN " )
					.append("			( select codes.plantname_id from plantusage JOIN " )
					.append("				(select plantconcept_id, plantname_id from plantusage where classsystem = 'Code' )" )
					.append(" 			AS codes ON plantusage.plantconcept_id = codes.plantconcept_id " )
					.append(" 				where plantusage.plantname like '")
					.append( 					plantNames[i] )
					.append("					' ) " )
					.append("			AS FOO ON taxonobservation.plantname_id = FOO.plantname_id) " )
					.append("		ON observation.observation_id = taxonobservation.observation_id) " )
					.append("	ON plot.plot_id = observation.plot_id ");
			
				plantQuery.append(" WHERE ( true AND ");
				
				StringBuffer plantQueryConditions = new StringBuffer(1024);
				
				plantQueryConditions.append(
					this.handleMaxMinNull(maxTaxonCover[i], minTaxonCover[i], true, "taxonobservation.taxoncover", " AND")
				);
				
				appendWhereClause(plantQuery, "", plantQueryConditions );
				
				// Have my query now run it!!
				DatabaseAccess da = new DatabaseAccess();
				ResultSet rs2 = da.issueSelect(plantQuery.toString());
				plantResultSets.add(rs2);
			}
			// I've got all my resultSets
		}
		System.out.println("Number of records matching plants: " +  plantResultSets.size() );
	}


	private void getCommunitiesResultSets(PlotQueryForm pqForm, Vector resultSets) throws SQLException
	{
    // COMMUNITIES
		String[] commNames = pqForm.getCommName();
		String[] maxCommStartDates = pqForm.getMaxCommStartDate();
		String[] minCommStopDates = pqForm.getMinCommStopDate();
		
		for (int i=0; i<commNames.length; i++)
		{
			// If no communityname given then forget it...
			if ( ! Utility.isStringNullOrEmpty(commNames[i]))
			{
				StringBuffer communityQuery = new StringBuffer(1024);
				communityQuery.append( selectClause )
					.append(" FROM plot JOIN " )
					.append("	(observation JOIN  " )
					.append("		(commclass JOIN " )
					.append(" 		(comminterpretation JOIN")
					.append("				(select codes.commconcept_id from commusage JOIN " )
					.append("					(select commconcept_id, commname_id from commusage where classsystem = 'CEGL' )" )
					.append(" 				AS codes ON commusage.commconcept_id = codes.commconcept_id " )
					.append(" 					where commusage.commname like '")
					.append(						commNames[i]) 
					.append("							' ) " ) 
					.append(" 			AS FOO ON comminterpretation.commconcept_id = FOO.commconcept_id) "	)
					.append("			ON commclass.commclass_id = comminterpretation.commclass_id) " )
					.append("		ON observation.observation_id = commclass.observation_id) " )
					.append("	ON plot.plot_id = observation.plot_id ");
				
				communityQuery.append(" WHERE ( true AND ");
				
				StringBuffer communityQueryConditions = new StringBuffer(1024);
				
				communityQueryConditions.append(
						this.handleMaxMinNullDateRange(
						minCommStopDates[i],
						maxCommStartDates[i],
						"commclass.classstartdate",
						"commclass.classstopdate",
						true, 
						" AND ")
				);
 			
				appendWhereClause(communityQuery, "", communityQueryConditions );
				
				// Have my query now run it!!
				DatabaseAccess da = new DatabaseAccess();
				ResultSet rs2 = da.issueSelect(communityQuery.toString());
				resultSets.add(rs2);
			}
			// I've got all my resultSets
		}
		System.out.println("Number of records matching communities: " +  resultSets.size() );
	}



	private String handleSimpleCompare(
		String value,
		String fieldName,
		boolean allowNull,
		String operator,
		String conjunction)
	{
		StringBuffer sb = new StringBuffer(128);
		if (value == null || value.equals(""))
		{
			return "";
		}
		else
		{
			sb.append(conjunction).append(" ( ");
			sb.append(fieldName)
				.append(" ").append(operator).append(" '").append(value).append("' ");
			if (allowNull)
			{
				sb.append(" OR ").append(fieldName).append(" IS NULL ");
			}
			sb.append(" ) ");
		}
		return sb.toString();
	}

	/**
	 * Is the ANYVALUE present or is this a list of nulls
	 * which is functionally the same.
	 * 
	 * @param values
	 * @return
	 */
	private boolean isAnyValueAllowed(String[] values)
	{
		boolean allNulls = true;

		for (int i = 0; i < values.length; i++)
		{
			if ( values[i] != null )
			{
				allNulls = false;
				// Is ANYVALUE present
				if ( values[i].equals(ANYVALUE))
				{
					return true;
				}
			}
		}
		return allNulls;
	}

	/**
	 * Generated SQL for option boxs where many options can be selected
	 * 
	 * Special handling for values ANY, IS NULL and IS NOT NULL 
	 * Needs to handle a list of nulls 
	 * 
	 * @param values
	 * @param fieldName
	 * @return
	 */
	private String handleValueList(String[] values, String fieldName, String conjunction)
	{
		StringBuffer sb = new StringBuffer(1024);

		// Are there any constraints
		if (values != null && values.length != 0 && !isAnyValueAllowed(values) )
		{
			sb.append(conjunction)
				.append(" ( ").append(fieldName).append(" = '").append(values[0]).append("'");

			for (int i = 0; i < values.length; i++)
			{
				if ( values[i] == null)
				{
					// Do nothing
				}
				else if (values[i].trim().equals("IS NOT NULL")
					|| values[i].trim().equals("IS NULL"))
				{
					sb.append(" OR ").append(fieldName).append(" ").append(values[i]).append(" ");
				}
				else
				{
					sb.append(" OR ").append(fieldName).append(" ='").append(values[i]).append("'");
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
		StringBuffer sb = new StringBuffer(256);

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
				sb.append(" ").append(fieldName).append(" <= ").append(max).append(" ");

				if (!Utility.isStringNullOrEmpty(min))
				{
					sb.append(" AND ");
				}
			}

			if (!Utility.isStringNullOrEmpty(min))
			{
				sb.append(" ").append(fieldName).append(" >= ").append(min).append(" ");
			}
			sb.append(" ) ");

			if (nullsAllowed)
			{
				sb.append("  OR (").append(fieldName).append(" IS NULL )  )");
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
		StringBuffer sb = new StringBuffer(256);
	
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
				sb.append(" ( ").append(startDateFieldName).append(" >= '").append(minDate).append("' ");
				if ( allowNulls)
				{
					sb.append("  OR ").append(startDateFieldName).append(" IS NULL ");
				}
				sb.append(" ) ");
			}
	
			if (!Utility.isStringNullOrEmpty(maxDate))
			{
				sb.append(" AND ( ").append(endDateFieldName).append(" <= '").append(maxDate).append("' ");
				if ( allowNulls)
				{
					sb.append("  OR ").append(startDateFieldName).append(" IS NULL ");
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
