/* *	'$RCSfile: PlotQueryForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-08-28 00:15:42 $'
 *	'$Revision: 1.6 $'
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
import java.util.Iterator;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.struts.action.ActionForm;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.model.Covermethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.Stratummethod;
import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author farrell
 */

public class PlotQueryForm extends ActionForm
{
  // PickLists to populate the form with
  private Collection rockTypes;
  private Collection surficialDeposits;
  private Collection hydrologicRegimes;
  private Collection topoPositions;
  
  // DB constructed picklists
	private Collection projectNames;
	private Collection coverMethodNames;
	private Collection stratumMethodNames;

  // State and Country
	private String[] countries = new String[20];
  private String[] state = new String[20];
  

	
  // Lat Long
  private String nlat;
  private String slat;
  private String wlong;
  private String elong;
	
  // Plot attributes
  private String minElevation;
  private String maxElevation;
  private String curMinElevation;
  private String curMaxElevation;
  private boolean allowNullElevation;
  private String minSlopeAspect;
  private String maxSlopeAspect;
  private String curMinSlopeAspect;
  private String curMaxSlopeAspect;
  private boolean allowNullSlopeAspect;
  private String minSlopeGradient;
  private String maxSlopeGradient;
  private String curMinSlopeGradient;
  private String curMaxSlopeGradient;
  private boolean allowNullSlopeGradient;

  private String[] rockType = new String[20];
  private String[] surficialDeposit = new String[20];
  private String[] hydrologicRegime = new String[20];
  private String[] topoPosition = new String[20];
  private String[] landForm = new String[20];
	
  // Sampling Methods
  private String maxObsEndDate;
  private String minObsStartDate;
  private boolean allowNullObsDate;
  private String minDateEntered;
  private String maxDateEntered;
  private String minPlotArea;
  private String maxPlotArea;
  private boolean allowNullPlotArea;

  private String[] coverMethodType = new String[20];;
  //private boolean allowNullCoverMethodType;
  private String[] stratumMethodName = new String[20];
 	// private boolean allowNullStratumMethodName;
	private String[] projectName = new String[20];
  private String observationContributorName;
  private boolean allowNullObservationContributorName;
  private String plotSubmitterName;
	
  // Vegatation
  private String[] plantName = new String[5];
  private String[] minTaxonCover = new String[5];
  private String[] maxTaxonCover = new String[5];

	
  // Richness
  private String minCountTaxonObservation;
  private String maxCountTaxonObservation;	
	
  // Strata
  private String minTreeCover;
  private String maxTreeCover;
  private String minTreeHt;
  private String maxTreeHt;	
  private boolean allowNullsTreeCover;
	
  private String minShrubCover;
  private String maxShrubCover;
  private String minShrubHt;
  private String maxShrubHt;	
  private boolean allowNullsShrubCover;
	
  private String minFieldCover;
  private String maxFieldCover;
  private String minFieldHt;
  private String maxFieldHt;	
  private boolean allowNullsFieldCover;
	
  private String minNonVascularCover;
  private String maxNonVascularCover;
  private String minNonVascularHt;
  private String maxNonVascularHt;	
  private boolean allowNullsNonVascularCover;
	
  private String minFloatingCover;
  private String maxFloatingCover;	
  private boolean allowNullsFloatingCover;
	
  private String minSubMergedCover;
  private String maxSubMergedCover;
  private String minSubMergedHt;
  private String maxSubMergedHt;	
  private boolean allowNullsSubMergedCover;	
	
  // GrowthForms
  private String[] dominantStratum;
	
  private String[] growthForm1;
  private String minGrowthForm1Cover;
  private String maxGrowthForm1Cover;
  private boolean allowNullsGrowthForm1Cover;
	
  private String[] growthForm2;
  private String minGrowthForm2Cover;
  private String maxGrowthForm2Cover;
  private boolean allowNullsGrowthForm2Cover;
		
  private String[] growthForm3;
  private String minGrowthForm3Cover;
  private String maxGrowthForm3Cover;
  private boolean allowNullsGrowthForm3Cover;
	
  // Communites
  private String[] commName = new String[4];
  private String[] maxCommStartDate = new String[4];
  private String[] minCommStopDate = new String[4];
  private String[] classContributorName = new String[4];
  
  // Conjuction to use
  private String conjunction = " AND ";

	
  /**
   * Constructor
   */
  public PlotQueryForm()
  {
  	super();
	loadDataConstraints();
  }

  /**
   * @return
   */
  public String[] getState()
  {
    return state;
  }

  /**
   * @param strings
   */
  public void setState(String[] strings)
  {
    state = strings;
  }

  /**
   * @return
   */
  public boolean isAllowNullElevation()
  {
    return allowNullElevation;
  }

  /**
   * @return
   */
  public boolean isAllowNullSlopeAspect()
  {
    return allowNullSlopeAspect;
  }

  /**
   * @return
   */
  public boolean isAllowNullSlopeGradient()
  {
    return allowNullSlopeGradient;
  }

  /**
   * @return
   */
  public String getCurMaxElevation()
  {
    return curMaxElevation;
  }

  /**
   * @return
   */
  public String getCurMaxSlopeAspect()
  {
    return curMaxSlopeAspect;
  }

  /**
   * @return
   */
  public String getCurMaxSlopeGradient()
  {
    return curMaxSlopeGradient;
  }

  /**
   * @return
   */
  public String getCurMinElevation()
  {
    return curMinElevation;
  }

  /**
   * @return
   */
  public String getCurMinSlopeAspect()
  {
    return curMinSlopeAspect;
  }

  /**
   * @return
   */
  public String getCurMinSlopeGradient()
  {
    return curMinSlopeGradient;
  }


  /**
   * @return
   */
  public String getMaxElevation()
  {
    return maxElevation;
  }

  /**
   * @return
   */
  public String getMaxSlopeAspect()
  {
    return maxSlopeAspect;
  }

  /**
   * @return
   */
  public String getMaxSlopeGradient()
  {
    return maxSlopeGradient;
  }

  /**
   * @return
   */
  public String getMinElevation()
  {
    return minElevation;
  }

  /**
   * @return
   */
  public String getMinSlopeAspect()
  {
    return minSlopeAspect;
  }

  /**
   * @return
   */
  public String getMinSlopeGradient()
  {
    return minSlopeGradient;
  }

  /**
   * @param b
   */
  public void setAllowNullElevation(boolean b)
  {
    allowNullElevation = b;
  }

  /**
   * @param b
   */
  public void setAllowNullSlopeAspect(boolean b)
  {
    allowNullSlopeAspect = b;
  }

  /**
   * @param b
   */
  public void setAllowNullSlopeGradient(boolean b)
  {
    allowNullSlopeGradient = b;
  }

  /**
   * @param string
   */
  public void setCurMaxElevation(String string)
  {
    curMaxElevation = string;
  }

  /**
   * @param string
   */
  public void setCurMaxSlopeAspect(String string)
  {
    curMaxSlopeAspect = string;
  }

  /**
   * @param string
   */
  public void setCurMaxSlopeGradient(String string)
  {
    curMaxSlopeGradient = string;
  }

  /**
   * @param string
   */
  public void setCurMinElevation(String string)
  {
    curMinElevation = string;
  }

  /**
   * @param string
   */
  public void setCurMinSlopeAspect(String string)
  {
    curMinSlopeAspect = string;
  }

  /**
   * @param string
   */
  public void setCurMinSlopeGradient(String string)
  {
    curMinSlopeGradient = string;
  }

  /**
   * @param string
   */
  public void setMaxElevation(String string)
  {
    maxElevation = string;
  }

  /**
   * @param string
   */
  public void setMaxSlopeAspect(String string)
  {
    maxSlopeAspect = string;
  }

  /**
   * @param string
   */
  public void setMaxSlopeGradient(String string)
  {
    maxSlopeGradient = string;
  }

  /**
   * @param string
   */
  public void setMinElevation(String string)
  {
    minElevation = string;
  }

  /**
   * @param string
   */
  public void setMinSlopeAspect(String string)
  {
    minSlopeAspect = string;
  }

  /**
   * @param string
   */
  public void setMinSlopeGradient(String string)
  {
    minSlopeGradient = string;
  }

  /**
   * @return
   */
  public String[] getHydrologicRegime()
  {
    return hydrologicRegime;
  }

  /**
   * @return
   */
  public String[] getLandForm()
  {
    return landForm;
  }

  /**
   * @return
   */
  public String[] getRockType()
  {
    return rockType;
  }

  /**
   * @return
   */
  public String[] getSurficialDeposit()
  {
    return surficialDeposit;
  }

  /**
   * @return
   */
  public String[] getTopoPosition()
  {
    return topoPosition;
  }

  /**
   * @param strings
   */
  public void setHydrologicRegime(String[] strings)
  {
    hydrologicRegime = strings;
  }

  /**
   * @param strings
   */
  public void setLandForm(String[] strings)
  {
    landForm = strings;
  }

  /**
   * @param strings
   */
  public void setRockType(String[] strings)
  {
    rockType = strings;
  }

  /**
   * @param strings
   */
  public void setSurficialDeposit(String[] strings)
  {
    surficialDeposit = strings;
  }

  /**
   * @param strings
   */
  public void setTopoPosition(String[] strings)
  {
    topoPosition = strings;
  }
	
  /**
   * @return
   */
  public Collection getRockTypes()
  {
    if ( rockTypes == null )
      {
				// Need to create this object
				Plot plot = new Plot();
				rockTypes = plot.getRocktypePickList();
      }
    return rockTypes;
  }

  /**
   * @return
   */
  public Collection getHydrologicRegimes()
  {
    if ( hydrologicRegimes == null )
      {
	// Need to create this object
	Observation obs = new Observation();
	hydrologicRegimes = obs.getHydrologicregimePickList();
      }
    return hydrologicRegimes;
  }

  /**
   * @return
   */
  public Collection getSurficialDeposits()
  {
    if ( surficialDeposits == null )
      {
				// Need to create this object
				Plot plot = new Plot();
				surficialDeposits = plot.getSurficialdepositsPickList();
      }
    return surficialDeposits;
  }

  /**
   * @return
   */
  public Collection getTopoPositions()
  {
    if ( topoPositions == null )
      {
				// Need to create this object
				Plot plot = new Plot();
				topoPositions = plot.getTopopositionPickList();
      }
    return topoPositions;
  }

  /**
   * @return
   */
  public boolean isAllowNullObsDate()
  {
    return allowNullObsDate;
  }

  /**
   * @return
   */
  public boolean isAllowNullPlotArea()
  {
    return allowNullPlotArea;
  }

  /**
   * @return
   */
  public String getMaxDateEntered()
  {
    return maxDateEntered;
  }

  /**
   * @return
   */
  public String getMaxObsEndDate()
  {
    return maxObsEndDate;
  }

  /**
   * @return
   */
  public String getMaxPlotArea()
  {
    return maxPlotArea;
  }

  /**
   * @return
   */
  public String getMinDateEntered()
  {
    return minDateEntered;
  }

  /**
   * @return
   */
  public String getMinObsStartDate()
  {
    return minObsStartDate;
  }

  /**
   * @return
   */
  public String getMinPlotArea()
  {
    return minPlotArea;
  }

  /**
   * @param b
   */
  public void setAllowNullObsDate(boolean b)
  {
    allowNullObsDate = b;
  }

  /**
   * @param b
   */
  public void setAllowNullPlotArea(boolean b)
  {
    allowNullPlotArea = b;
  }

  /**
   * @param string
   */
  public void setMaxDateEntered(String string)
  {
    maxDateEntered = string;
  }

  /**
   * @param string
   */
  public void setMaxObsEndDate(String string)
  {
    maxObsEndDate = string;
  }

  /**
   * @param string
   */
  public void setMaxPlotArea(String string)
  {
    maxPlotArea = string;
  }

  /**
   * @param string
   */
  public void setMinDateEntered(String string)
  {
    minDateEntered = string;
  }

  /**
   * @param string
   */
  public void setMinObsStartDate(String string)
  {
    minObsStartDate = string;
  }

  /**
   * @param string
   */
  public void setMinPlotArea(String string)
  {
    minPlotArea = string;
  }
  
	/**
		 * Gets the value of coverMethodType
		 *
		 * @return the value of coverMethodType
		 */
		public String[] getCoverMethodType() {
			return this.coverMethodType;
		}

		/**
		 * Sets the value of coverMethodType
		 *
		 * @param argCoverMethodType Value to assign to this.coverMethodType
		 */
		public void setCoverMethodType(String[] argCoverMethodType){
			this.coverMethodType = argCoverMethodType;
		}

//		/**
//		 * Gets the value of allowNullCoverMethodType
//		 *
//		 * @return the value of allowNullCoverMethodType
//		 */
//		public boolean isAllowNullCoverMethodType() {
//			return this.allowNullCoverMethodType;
//		}

//		/**
//		 * Sets the value of allowNullCoverMethodType
//		 *
//		 * @param argAllowNullCoverMethodType Value to assign to this.allowNullCoverMethodType
//		 */
//		public void setAllowNullCoverMethodType(boolean argAllowNullCoverMethodType){
//			this.allowNullCoverMethodType = argAllowNullCoverMethodType;
//		}

		/**
		 * Gets the value of stratumMethodName
		 *
		 * @return the value of stratumMethodName
		 */
		public String[] getStratumMethodName() {
			return this.stratumMethodName;
		}

		/**
		 * Sets the value of stratumMethodName
		 *
		 * @param argStratumMethodName Value to assign to this.stratumMethodName
		 */
		public void setStratumMethodName(String[] argStratumMethodName){
			this.stratumMethodName = argStratumMethodName;
		}

//		/**
//		 * Gets the value of allowNullStratumMethodName
//		 *
//		 * @return the value of allowNullStratumMethodName
//		 */
//		public boolean isAllowNullStratumMethodName() {
//			return this.allowNullStratumMethodName;
//		}
//
//		/**
//		 * Sets the value of allowNullStratumMethodName
//		 *
//		 * @param argAllowNullStratumMethodName Value to assign to this.allowNullStratumMethodName
//		 */
//		public void setAllowNullStratumMethodName(boolean argAllowNullStratumMethodName){
//			this.allowNullStratumMethodName = argAllowNullStratumMethodName;
//		}

		/**
		 * Gets the value of observationContributorName
		 *
		 * @return the value of observationContributorName
		 */
		public String getObservationContributorName() {
			return this.observationContributorName;
		}

		/**
		 * Sets the value of observationContributorName
		 *
		 * @param argObservationContributorName Value to assign to this.observationContributorName
		 */
		public void setObservationContributorName(String argObservationContributorName){
			this.observationContributorName = argObservationContributorName;
		}

		/**
		 * Gets the value of allowNullObservationContributorName
		 *
		 * @return the value of allowNullObservationContributorName
		 */
		public boolean isAllowNullObservationContributorName() {
			return this.allowNullObservationContributorName;
		}

		/**
		 * Sets the value of allowNullObservationContributorName
		 *
		 * @param argAllowNullObservationContributorName Value to assign to this.allowNullObservationContributorName
		 */
		public void setAllowNullObservationContributorName(boolean argAllowNullObservationContributorName){
			this.allowNullObservationContributorName = argAllowNullObservationContributorName;
		}

		/**
		 * Gets the value of projectName
		 *
		 * @return the value of projectName
		 */
		public String[] getProjectName() {
			return this.projectName;
		}

		/**
		 * Sets the value of projectName
		 *
		 * @param argProjectName Value to assign to this.projectName
		 */
		public void setProjectName(String[] strings){
			this.projectName = strings;
		}

		/**
		 * Gets the value of plotSubmitterName
		 *
		 * @return the value of plotSubmitterName
		 */
		public String getPlotSubmitterName() {
			return this.plotSubmitterName;
		}


	/**
	 * @return
	 */
	public String[] getMaxTaxonCover()
	{
		return maxTaxonCover;
	}


	/**
	 * @return
	 */
	public String[] getMinTaxonCover()
	{
		return minTaxonCover;
	}

	/**
	 * @return
	 */
	public String[] getPlantName()
	{
		return plantName;
	}



	/**
	 * @param strings
	 */
	public void setMaxTaxonCover(String[] strings)
	{
		maxTaxonCover = strings;
	}


	/**
	 * @param strings
	 */
	public void setMinTaxonCover(String[] strings)
	{
		minTaxonCover = strings;
	}

	/**
	 * @param strings
	 */
	public void setPlantName(String[] strings)
	{
		plantName = strings;
	}

	/**
	 * @return
	 */
	public String[] getClassContributorName()
	{
		return classContributorName;
	}

	/**
	 * @return
	 */
	public String[] getCommName()
	{
		return commName;
	}

	/**
	 * @return
	 */
	public String[] getMaxCommStartDate()
	{
		return maxCommStartDate;
	}

	/**
	 * @return
	 */
	public String[] getMinCommStopDate()
	{
		return minCommStopDate;
	}

	/**
	 * @param strings
	 */
	public void setClassContributorName(String[] strings)
	{
		classContributorName = strings;
	}

	/**
	 * @param strings
	 */
	public void setCommName(String[] strings)
	{
		commName = strings;
	}

	/**
	 * @param strings
	 */
	public void setMaxCommStartDate(String[] strings)
	{
		maxCommStartDate = strings;
	}

	/**
	 * @param strings
	 */
	public void setMinCommStopDate(String[] strings)
	{
		minCommStopDate = strings;
	}

	/**
	 * @return
	 */
	public String getConjunction()
	{
		return conjunction;
	}

	/**
	 * @param string
	 */
	public void setConjunction(String string)
	{
		conjunction = string;
	}

	/**
	 * @return
	 */
	public String[] getCountries()
	{
		return countries;
	}

	/**
	 * @param strings
	 */
	public void setCountries(String[] strings)
	{
		countries = strings;
	}

 	// Add the picklists here......
 	
 	
	/**
	 * @return
	 */
	public Collection getProjectNames()
	{
		if ( projectNames == null )
		{
			projectNames = new Vector();
			String selectClauseKey = "project";
			String whereClauseKey = null;
			String beanName = "Project";
			String[] whereParams = null;
			// Need to create this object
			GenericCommand  gc = new GenericCommand();
			try
			{
				Collection projects = gc.execute(selectClauseKey, whereClauseKey, beanName, whereParams );
				Iterator projectsIter = projects.iterator();
				while ( projectsIter.hasNext() )
				{
					Project project = (Project) projectsIter.next();
					projectNames.add( project.getProjectname() );
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return projectNames;
	}

	/**
	 * @return
	 */
	public Collection getCoverMethodNames()
	{
		if ( this.coverMethodNames == null )
		{
			coverMethodNames = new Vector();
			String selectClauseKey = "covermethod";
			String whereClauseKey = null;
			String beanName = "Covermethod";
			String[] whereParams = null;
			// Need to create this object
			GenericCommand  gc = new GenericCommand();
			try
			{
				Collection cms = gc.execute(selectClauseKey, whereClauseKey, beanName, whereParams );
				Iterator cmIter = cms.iterator();
				while ( cmIter.hasNext() )
				{
					Covermethod cm = (Covermethod) cmIter.next();
					coverMethodNames.add( cm.getCovertype() );
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return coverMethodNames;
	}

	/**
	 * @return
	 */
	public Collection getStratumMethodNames()
	{
		if ( projectNames == null )
		{
			stratumMethodNames = new Vector();
			String selectClauseKey = "stratummethod";
			String whereClauseKey = null;
			String beanName = "Stratummethod";
			String[] whereParams = null;
			// Need to create this object
			GenericCommand  gc = new GenericCommand();
			try
			{
				Collection stratummethods = gc.execute(selectClauseKey, whereClauseKey, beanName, whereParams );
				Iterator stratummethodsIter = stratummethods.iterator();
				while ( stratummethodsIter.hasNext() )
				{
					Stratummethod sm = (Stratummethod) stratummethodsIter.next();
					stratumMethodNames.add( sm.getStratummethodname() );
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stratumMethodNames;
	}

	/**
	 * Gets relevant plot data 
	 *
	 * @return void
	 */
	private void loadDataConstraints() {
		StringBuffer query = new StringBuffer(1024)
				.append("SELECT MIN(elevation) as curMinElevation, ")
				.append("MAX(elevation) as curMaxElevation, ")
				.append("MIN(slopeAspect) as curMinSlopeAspect, ")
				.append("MAX(slopeAspect) as curMaxSlopeAspect, ")
				.append("MIN(slopeGradient) as curMinSlopeGradient, ")
				.append("MAX(slopeGradient) as curMaxSlopeGradient ")
				.append("FROM plot");
		try
		{
			DatabaseAccess da = new DatabaseAccess();
			// hit the DB
			ResultSet rs = da.issueSelect(query.toString());		

			System.out.println("Processing " + rs.toString());
			
			if (rs.next())
			{
				curMinElevation = Integer.toString( (Double.valueOf(rs.getString(1))).intValue() );
				curMaxElevation = Integer.toString( (Double.valueOf(rs.getString(2))).intValue() );
				curMinSlopeAspect = rs.getString(3);
				curMaxSlopeAspect = rs.getString(4);
				curMinSlopeGradient = rs.getString(5);
				curMaxSlopeGradient = rs.getString(6);
			}
		}
		catch (SQLException e1)
		{
			System.out.println("org.vegbank.ui.struts.PlotQueryForm:: " +
				"loadDataConstraints() ERROR: " + e1.getMessage());
		}

	}


}
