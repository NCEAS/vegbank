 /*
 *	'$RCSfile: PlotQueryAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-07-22 20:33:09 $'
 *	'$Revision: 1.20 $'
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.vegbank.common.model.*;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.DatabaseUtility;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 */

public class PlotQueryAction extends VegbankAction
{
	private static Log log = LogFactory.getLog(PlotQueryAction.class); 

	private static final String ANDVALUE = " AND ";
	private static final String ORVALUE = " OR ";
	private static final int NUM_TOP_TAXA =	5;
	private static String selectClause =	
		" SELECT DISTINCT(observation.accessioncode), observation.authorobscode, " +	
		" plot.latitude, plot.longitude, observation.observation_id ";

	
	// add sql to get the top taxa
	static {
		StringBuffer sb = new StringBuffer(512);
		for (int i=0; i < NUM_TOP_TAXA; i++) {
			sb.append(", (SELECT authorPlantName || ' (' || cover || ')' FROM taxonObservation as tob, ")
				.append(" taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id ")
				.append(" is null and cover is not null and authorPlantName is not null AND ")
				.append(" tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET ")
				.append(i)
				.append(") as topspp")
				.append(i+1);
		}
		selectClause += sb.toString();
	}

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		log.debug(" In action PlotQueryAction");
		ActionErrors errors = new ActionErrors();

		StringBuffer query = new StringBuffer(1024);
		query.append(selectClause)
				.append(" FROM plot, project, observation, covermethod, stratummethod ")	
				.append(" WHERE plot." + Plot.PKNAME + " = observation." + Observation.PLOT_ID)
				.append(" AND project." + Project.PKNAME + " = observation." + Observation.PROJECT_ID + " AND observation." + Observation.COVERMETHOD_ID + " = covermethod." + Covermethod.PKNAME )
				.append(" AND observation." + Observation.STRATUMMETHOD_ID + " = stratummethod." + Stratummethod.PKNAME);

		// Get the form
		PlotQueryForm pqForm = (PlotQueryForm) form;
		String conjunction = pqForm.getConjunction();
		
		query.append(" AND ( ");

		StringBuffer dynamicQuery = new StringBuffer(1024);
		
		// Countries
		dynamicQuery.append(DatabaseUtility.handleValueList(pqForm.getCountries(), "plot." + Plot.COUNTRY, conjunction));
		
		// States
		dynamicQuery.append(DatabaseUtility.handleValueList(pqForm.getState(), "plot." + Plot.STATEPROVINCE, conjunction));

		// Elevation
		//log.debug(  pqForm.getMaxElevation())+ " " + pqForm.getMinElevation() + " " +  pqForm.isAllowNullElevation() + " elevation" );
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxElevation(),
				pqForm.getMinElevation(),
				pqForm.isAllowNullElevation(),
				"plot." + Plot.ELEVATION,
				conjunction));

		// SlopeAspect
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeAspect(),
				pqForm.getMinSlopeAspect(),
				pqForm.isAllowNullSlopeAspect(),
				"plot." + Plot.SLOPEASPECT,
				conjunction));

		// SlopeGradient
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxSlopeGradient(),
				pqForm.getMinSlopeGradient(),
				pqForm.isAllowNullSlopeGradient(),
				"plot." + Plot.SLOPEGRADIENT,
				conjunction));

		// rockType
		dynamicQuery.append(DatabaseUtility.handleValueList(pqForm.getRockType(), "plot.rockType", conjunction));
		// surficalDeposits
		dynamicQuery.append(
			DatabaseUtility.handleValueList(
				pqForm.getSurficialDeposit(),
				"plot" + Plot.SURFICIALDEPOSITS,
				conjunction));
		// hydrologicregime
		dynamicQuery.append(
				DatabaseUtility.handleValueList(
				pqForm.getSurficialDeposit(),
				"observation." + Observation.HYDROLOGICREGIME,
				conjunction));
		// topoposition
		dynamicQuery.append(
				DatabaseUtility.handleValueList(pqForm.getTopoPosition(), "plot." + Plot.TOPOPOSITION, conjunction));
		// landForm
		dynamicQuery.append(DatabaseUtility.handleValueList(pqForm.getLandForm(), "plot." + Plot.LANDFORM, conjunction));

		// Date Observed
		// This need special handling because of the start/end points....
		dynamicQuery.append(
			this.handleMaxMinNullDateRange(
				pqForm.getMinObsStartDate(),
				pqForm.getMaxObsEndDate(),
				"observation." + Observation.OBSSTARTDATE,
				"observation." + Observation.OBSENDDATE,
				pqForm.isAllowNullObsDate(),
				" AND ")
		);

		// Date Entered
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxDateEntered(),
				pqForm.getMinDateEntered(),
				false,
				"plot." + Plot.DATEENTERED,
				conjunction));

		// Plot Area
		dynamicQuery.append(
			this.handleMaxMinNull(
				pqForm.getMaxPlotArea(),
				pqForm.getMinPlotArea(),
				pqForm.isAllowNullPlotArea(),
				"plot." + Plot.AREA,
				conjunction));

		// METHODS and PEOPLE
		dynamicQuery.append(
			DatabaseUtility.handleValueList(
				pqForm.getCoverMethodType(),
				"covermethod." + Covermethod.COVERTYPE,
				conjunction)); 
				
//		dynamicQuery.append(
//			this.handleSimpleCompare(
//				pqForm.getCoverMethodType(),
//				"covermethod.covertype",
//				pqForm.isAllowNullCoverMethodType(),
//				" like ", 
//				conjunction));
				
		dynamicQuery.append(
			DatabaseUtility.handleValueList(
				pqForm.getStratumMethodName(),
				"stratummethod." + Stratummethod.STRATUMMETHODNAME,
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
			DatabaseUtility.handleValueList(
				pqForm.getProjectName(),
				"project." + Project.PROJECTNAME,
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
			//log.debug("PLOT QUERY #1: " + query.toString());
			resultSets.add( da.issueSelect(query.toString() ) );		
			getPlantResultSets(pqForm, resultSets);
			getCommunitiesResultSets(pqForm, resultSets);
			
			// Save the Collection of Summaries into the session
			request.getSession().setAttribute("PlotsResults", getPlotSummaries(conjunction, resultSets));
		}
		catch (SQLException e1)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.database", e1.getMessage()));
			log.debug("PlotQueryAction: SQL ERROR ON QUERY: " + query.toString());
		}

		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			log.debug("PlotQueryAction: getting input fwd: " + mapping.getInputForward().toString());
			return (mapping.getInputForward());
		}

		log.debug("PlotQueryAction: GOOD: fwd to display results");
		return mapping.findForward("display_results");
	}

	private Collection getPlotSummaries(String conjunction, Vector resultSets)
		throws SQLException
	{
		log.debug("Conjunction is '" + conjunction + "'");
		
		// Collection for adding valid object created from current resultset
		Hashtable workspace = new Hashtable();
		// Collection for storing keys that are still valid ( only needed for Intersection)
		Vector validKeys = new Vector();
		// Used in Union to capture all of first resultset.
		boolean isFirstResultSet = true;
		
		//log.debug(">>> " + resultSets);
		Iterator results = resultSets.iterator();
		while ( results.hasNext() )
		{
			ResultSet rs = (ResultSet) results.next();
			log.debug("Processing " + rs.toString());
			
			// When doing a Intesection add to current collection all objects that existed
			// in oldWorkspace and current resultset.
			if ( conjunction.equals(ANDVALUE) ) 
			{
				validKeys.clear();
				//log.debug("111===> " + validKeys);
				Enumeration keysEnum = workspace.keys();
				while ( keysEnum.hasMoreElements() )
				{
					validKeys.add(keysEnum.nextElement());
				}
				//log.debug("222===> " + validKeys );
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
						//log.debug("#Adding " + plotsum + " has " + plotsum.getAccessionCode());
						workspace.put(plotsum.getAccessionCode(), plotsum);
					}
					else //Only add element if in oldWorkspace
					{
						
						//log.debug("3333===> " + validKeys);
						
						// Did this object exist in the previous results
						if ( validKeys.contains(plotsum.getAccessionCode()))
						{
							//log.debug("Adding " + plotsum.getAccessionCode());
							workspace.put(plotsum.getAccessionCode(), plotsum);
						}
						else
						{
							//log.debug("Not Adding " + plotsum.getAccessionCode());
							// Don't add this object
						}
					}
				}
				else // Concatonate without duplicates
				{
					if ( workspace.contains(plotsum.getAccessionCode()))
					{
						//log.debug("No need to ADD " + plotsum.getAccessionCode());
						// No need to add
					}
					else
					{
						// Add this new object
						workspace.put(plotsum.getAccessionCode(), plotsum);
						//log.debug("ADD " + plotsum.getAccessionCode());
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
		plotsum.setAccessionCode(rs.getString(1)); 
		plotsum.setAuthorObservationCode(rs.getString(2));
		plotsum.setLatitude(new Double(rs.getDouble(3)));
		plotsum.setLongitude(new Double(rs.getDouble(4)));
		plotsum.setPlotId(rs.getString(5));

		plotsum.setTopspp1(rs.getString(6));
		plotsum.setTopspp2(rs.getString(7));
		plotsum.setTopspp3(rs.getString(8));
		plotsum.setTopspp4(rs.getString(9));
		plotsum.setTopspp5(rs.getString(10));
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
				if (!plantNames[i].endsWith("%")) {
					plantNames[i] += "%";
				}
			
				StringBuffer plantQuery = new StringBuffer(1024);
				plantQuery.append( selectClause )
					.append(" FROM plot JOIN " )
					.append("	(observation JOIN  " )
					.append("		( " )
													// JOIN taxonobservation and taxonimportance to allow cover lookup
					.append("    ( SELECT taxonobservation." + Taxonobservation.PKNAME + ",  taxonobservation." + Taxonobservation.OBSERVATION_ID )
					.append("       , taxonimportance." + Taxonimportance.COVER )
					.append("       FROM taxonobservation, taxonimportance " )
					.append("        WHERE  taxonobservation." + Taxonobservation.PKNAME + " =  taxonimportance." + Taxonimportance.TAXONOBSERVATION_ID )
					.append("         AND taxonimportance." + Taxonimportance.STRATUM_ID + " IS NULL " )
					.append("     ) AS TOTI JOIN " )
					.append("     (taxoninterpretation JOIN ")
					                  // Get any names that match input string
					.append("			  ( SELECT " + Plantusage.PLANTCONCEPT_ID + " FROM plantusage WHERE" )
					.append(" 				 UPPER(" + Plantusage.PLANTNAME + ") LIKE '" + plantNames[i].toUpperCase() + "'" )
					.append("				 ) AS PU" )				
					.append("			  ON taxoninterpretation." +  Taxoninterpretation.PLANTCONCEPT_ID + " = PU." + Plantusage.PLANTCONCEPT_ID + " ) " )
					.append("			ON TOTI." + Taxonobservation.PKNAME + " = taxoninterpretation." + Taxoninterpretation.TAXONOBSERVATION_ID + " ) " )	
					.append("		ON observation." + Observation.PKNAME + " = TOTI." + Taxonobservation.OBSERVATION_ID + " ) " )
					.append("	ON plot." + Plot.PKNAME + " = observation." + Observation.PLOT_ID );
				
				plantQuery.append(" WHERE ( true ");
				
				StringBuffer plantQueryConditions = new StringBuffer(1024);
				
				plantQueryConditions.append(
					this.handleMaxMinNull(maxTaxonCover[i], minTaxonCover[i], true, "TOTI." +  Taxonimportance.COVER, " AND ")
				);
				
				// hack -- SQL need AND if  plantQueryConditions empty
				if ( Utility.isStringNullOrEmpty( plantQueryConditions.toString() ) )
 				{
 					plantQuery.append(" AND ");
 				}
				appendWhereClause(plantQuery, "", plantQueryConditions );
				
				// Have my query now run it!!
				DatabaseAccess da = new DatabaseAccess();
				log.debug("PLOT QUERY (plants): " + plantQuery.toString());
				ResultSet rs2 = da.issueSelect(plantQuery.toString());
				plantResultSets.add(rs2);
			}
			// I've got all my resultSets
		}
		log.debug("Number of records matching plants: " +  plantResultSets.size() );
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
				if (!commNames[i].endsWith("%")) {
					commNames[i] += "%";
				}
			
				StringBuffer communityQuery = new StringBuffer(1024);
				communityQuery.append( selectClause )
					.append(" FROM plot JOIN " )
					.append("	(observation JOIN  " )
					.append("		(commclass JOIN " )
					.append(" 		(comminterpretation JOIN")
          		// Get any names that match input string
					.append("			  ( SELECT " + Commusage.COMMCONCEPT_ID + " FROM commusage WHERE" )
					.append(" 				 UPPER(" + Commusage.COMMNAME + ") LIKE '" + commNames[i].toUpperCase() + "'" )
					.append("				 ) AS CU" )	 
					.append(" 			ON comminterpretation." + Comminterpretation.COMMCONCEPT_ID + " = CU." + Commusage.COMMCONCEPT_ID + " ) "	)
					.append("			ON commclass." + Commclass.PKNAME + " = comminterpretation. " + Comminterpretation.COMMCLASS_ID + " ) " )
					.append("		ON observation." + Observation.PKNAME + " = commclass." + Commclass.OBSERVATION_ID + ") " )
					.append("	ON plot." + Plot.PKNAME + " = observation." + Observation.PLOT_ID );
				
				communityQuery.append(" WHERE ( true ");
				
				StringBuffer communityQueryConditions = new StringBuffer(1024);
				
				communityQueryConditions.append(
						this.handleMaxMinNullDateRange(
						minCommStopDates[i],
						maxCommStartDates[i],
						"commclass." + Commclass.CLASSSTARTDATE,
						"commclass." + Commclass.CLASSSTOPDATE,
						true, 
						" AND ")
				);
				// hack -- SQL need AND if  communityQueryConditions empty
				if ( Utility.isStringNullOrEmpty( communityQueryConditions.toString() ) )
 				{
 					communityQuery.append(" AND ");
 				}
 			
				appendWhereClause(communityQuery, "", communityQueryConditions );
				
				// Have my query now run it!!
				DatabaseAccess da = new DatabaseAccess();
				ResultSet rs2 = da.issueSelect(communityQuery.toString());
				resultSets.add(rs2);
			}
			// I've got all my resultSets
		}
		log.debug("Number of records matching communities: " +  resultSets.size() );
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
		private String authorObservationCode = "";
		private String accessionCode = "";
		private Double latitude;
		private Double longitude;
		private String plotId = "";
		private String topspp1 = "";
		private String topspp2 = "";
		private String topspp3 = "";
		private String topspp4 = "";
		private String topspp5 = "";

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
		public Double getLatitude()
		{
			return latitude;
		}

		/**
		 * @return
		 */
		public Double getLongitude()
		{
			return longitude;
		}

		/**
		 * @return
		 */
		public String getAccessionCode()
		{
			return accessionCode;
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
		public void setLatitude(Double d)
		{
			latitude = d;
		}

		/**
		 * @param string
		 */
		public void setLongitude(Double d)
		{
			longitude = d;
		}

		/**
		 * @param string
		 */
		public void setAccessionCode(String string)
		{
			accessionCode = string;
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

		////////////////////////////////////////////////////
		// Top species
		////////////////////////////////////////////////////
		public String getTopspp1()
		{ return topspp1; }
		public void setTopspp1(String string)
		{ topspp1 = string; }

		public String getTopspp2()
		{ return topspp2; }
		public void setTopspp2(String string)
		{ topspp2 = string; }

		public String getTopspp3()
		{ return topspp3; }
		public void setTopspp3(String string)
		{ topspp3 = string; }

		public String getTopspp4()
		{ return topspp4; }
		public void setTopspp4(String string)
		{ topspp4 = string; }

		public String getTopspp5()
		{ return topspp5; }
		public void setTopspp5(String string)
		{ topspp5 = string; }

	}
	
}
