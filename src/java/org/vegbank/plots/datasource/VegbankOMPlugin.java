/*
 *	'$RCSfile: VegbankOMPlugin.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-01 23:11:24 $'
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
 
package org.vegbank.plots.datasource;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.vegbank.common.model.*;

/**
 * @author farrell
 */

public class VegbankOMPlugin implements PlotDataSourceInterface
{

	private Observation observation;
	private Plot plot;
	private Project project;
	private CoverMethod coverMethod;
	private StratumMethod stratumMethod;
	private SoilTaxon soilTaxon;
	private CommClass commClass;
	
	private Hashtable projectContibs = new Hashtable();
	private Hashtable taxonObs = new Hashtable();
	private HashMap stratumComposition = new HashMap();	
	private HashMap strataNames = new HashMap();	
	private HashMap placeNames = new HashMap();	
	
	
  public VegbankOMPlugin()
  {
  }

  public VegbankOMPlugin(int plotId)
	{
	  init(plotId);	
	}

  private void  init(int observation_id)
  {
  	// Get All Observations related to this plot
  	
  			
    DBObservationReader ob = new DBObservationReader();
		try
		{
			// TODO:  Get a single observation_id for this plot
			observation = ob.getObservation("observation_id", observation_id);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plot = observation.getPLOT();
		project = observation.getPROJECT();
		coverMethod = observation.getCOVERMETHOD();
		stratumMethod = observation.getSTRATUMMETHOD();
		soilTaxon = observation.getSOILTAXON();
		
		// Just get the first one as thats all the interface supports
		Iterator commClasses = observation.getOBSERVATIONCommClasss().iterator();
		if ( commClasses.hasNext() )
		{
			commClass = (CommClass)commClasses.next();
		}
		else
		{
			// Create an empty commClass
			commClass = new CommClass();
		}

  }

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getAccessionValue(java.lang.String)
	 */
	public String getAccessionValue(String plotId)
	{
		return plot.getAccession_number();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getAuthorLocation(java.lang.String)
	 */
	public String getAuthorLocation(String plotName)
	{
		return plot.getAuthorLocation();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getAuthorObsCode(java.lang.String)
	 */
	public String getAuthorObsCode(String plotName)
	{
		return observation.getAuthorObsCode();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getAutoTaxonCover(java.lang.String)
	 */
	public boolean getAutoTaxonCover(String plantName)
	{
		return new Boolean(observation.getAutoTaxonCover()).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getAzimuth(java.lang.String)
	 */
	public String getAzimuth(String plotName)
	{
		return plot.getAzimuth();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getBasalArea(java.lang.String)
	 */
	public String getBasalArea(String plotName)
	{
		return observation.getBasalArea();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getBryophyteQuality(java.lang.String)
	 */
	public String getBryophyteQuality(String plotName)
	{
		return observation.getBryophyteQuality();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getClassNotes(java.lang.String)
	 */
	public String getClassNotes(String plotName)
	{
		return commClass.getClassNotes();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityCode(java.lang.String)
	 */
	public String getCommunityCode(String plotName)
	{
		return commClass.getCommCode();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityExpertSystem(java.lang.String)
	 */
	public String getCommunityExpertSystem(String plotName)
	{
		return commClass.getExpertSystem();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityFramework(java.lang.String)
	 */
	public String getCommunityFramework(String plotName)
	{
		return commClass.getCommFramework();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityInspection(java.lang.String)
	 */
	public String getCommunityInspection(String plotName)
	{
		return commClass.getInspection();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityLevel(java.lang.String)
	 */
	public String getCommunityLevel(String plotName)
	{
		return commClass.getCommLevel();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityMultiVariateAnalysis(java.lang.String)
	 */
	public String getCommunityMultiVariateAnalysis(String plotName)
	{
		return commClass.getMultivariateAnalysis();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityName(java.lang.String)
	 */
	public String getCommunityName(String plotName)
	{
		return commClass.getCommName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityPublication(java.lang.String)
	 */
	public String getCommunityPublication(String plotName)
	{
		// TODO: Not sure what is wanted here
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityStartDate(java.lang.String)
	 */
	public String getCommunityStartDate(String plotName)
	{
		return commClass.getClassStartDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityStopDate(java.lang.String)
	 */
	public String getCommunityStopDate(String plotName)
	{
		return commClass.getClassStopDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCommunityTableAnalysis(java.lang.String)
	 */
	public String getCommunityTableAnalysis(String plotName)
	{
		return commClass.getTableAnalysis();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getConfidentialityReason(java.lang.String)
	 */
	public String getConfidentialityReason(String plotName)
	{
		return plot.getConfidentialityReason();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getConfidentialityStatus(java.lang.String)
	 */
	public String getConfidentialityStatus(String plotName)
	{
		return plot.getConfidentialityStatus();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCountry(java.lang.String)
	 */
	public String getCountry(String plotName)
	{
		return plot.getCountry();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCoverDispersion(java.lang.String)
	 */
	public String getCoverDispersion(String plotName)
	{
		return observation.getCoverDispersion();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCoverMethod(java.lang.String)
	 */
	public Hashtable getCoverMethod(String plotName)
	{
		// TODO: Implement ( if needed )
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCoverMethodName(java.lang.String)
	 */
	public String getCoverMethodName(String plotName)
	{
		return coverMethod.getCoverType();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getCummulativeStrataCover(java.lang.String, java.lang.String)
	 */
	public String getCummulativeStrataCover(String plantName, String plotCode)
	{
		// TODO: Implement ( not sure what this is about )
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getDatumType(java.lang.String)
	 */
	public String getDatumType(String plotName)
	{
		return plot.getAuthorDatum();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getDominantStratum(java.lang.String)
	 */
	public String getDominantStratum(String plotName)
	{
		return observation.getDominantStratum();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getDSGPoly(java.lang.String)
	 */
	public String getDSGPoly(String plotName)
	{
		return plot.getDsgpoly();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getEffortLevel(java.lang.String)
	 */
	public String getEffortLevel(String plotName)
	{
		return observation.getEffortLevel();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getElevation(java.lang.String)
	 */
	public String getElevation(String plotName)
	{
		return plot.getElevation();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getElevationAccuracy(java.lang.String)
	 */
	public String getElevationAccuracy(String plotName)
	{
		return plot.getElevationAccuracy();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getFieldCover(java.lang.String)
	 */
	public String getFieldCover(String plotName)
	{
		return observation.getFieldCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getFieldHt(java.lang.String)
	 */
	public String getFieldHt(String plotName)
	{
		return observation.getFieldHt();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getFloatingCover(java.lang.String)
	 */
	public String getFloatingCover(String plotName)
	{
		return observation.getFloatingCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getFloristicQuality(java.lang.String)
	 */
	public String getFloristicQuality(String plotName)
	{
		return observation.getFloristicQuality();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform1Cover(java.lang.String)
	 */
	public String getGrowthform1Cover(String plotName)
	{
		return observation.getGrowthform1Cover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform1Type(java.lang.String)
	 */
	public String getGrowthform1Type(String plotName)
	{
		return observation.getGrowthform1Type();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform2Cover(java.lang.String)
	 */
	public String getGrowthform2Cover(String plotName)
	{
		return observation.getGrowthform2Cover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform2Type(java.lang.String)
	 */
	public String getGrowthform2Type(String plotName)
	{
		return observation.getGrowthform2Type();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform3Cover(java.lang.String)
	 */
	public String getGrowthform3Cover(String plotName)
	{
		return observation.getGrowthform3Cover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getGrowthform3Type(java.lang.String)
	 */
	public String getGrowthform3Type(String plotName)
	{
		return observation.getGrowthform3Type();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getHomogeneity(java.lang.String)
	 */
	public String getHomogeneity(String plotName)
	{
		return observation.getHomogeneity();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getHydrologicRegime(java.lang.String)
	 */
	public String getHydrologicRegime(String plotName)
	{
		return observation.getHydrologicRegime();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLandForm(java.lang.String)
	 */
	public String getLandForm(String plotName)
	{
		return plot.getLandform();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLandscapeNarrative(java.lang.String)
	 */
	public String getLandscapeNarrative(String plotName)
	{
		return observation.getLandscapeNarrative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLatitude(java.lang.String)
	 */
	public String getLatitude(String plotName)
	{
		return plot.getLatitude();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLayoutNarrative(java.lang.String)
	 */
	public String getLayoutNarrative(String plotName)
	{
		return plot.getLayoutNarative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLichenQuality(java.lang.String)
	 */
	public String getLichenQuality(String plotName)
	{
		return observation.getLichenQuality();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLocationNarrative(java.lang.String)
	 */
	public String getLocationNarrative(String plotName)
	{
		return plot.getLocationNarrative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getLongitude(java.lang.String)
	 */
	public String getLongitude(String plotName)
	{
		return plot.getLongitude();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getMethodNarrative(java.lang.String)
	 */
	public String getMethodNarrative(String plotName)
	{
		return observation.getMethodNarrative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getNameOther(java.lang.String)
	 */
	public String getNameOther(String plotName)
	{
		return observation.getNameOther();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getNonvascularCover(java.lang.String)
	 */
	public String getNonvascularCover(String plotName)
	{
		return observation.getNonvascularCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getNonvascularHt(java.lang.String)
	 */
	public String getNonvascularHt(String plotName)
	{
		return observation.getNonvascularHt();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getNotesMgt(java.lang.String)
	 */
	public boolean getNotesMgt(String plotName)
	{
		return new Boolean( observation.getNotesMgt() ).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getNotesPublic(java.lang.String)
	 */
	public boolean getNotesPublic(String plotName)
	{
		return new Boolean( observation.getNotesPublic() ).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObsDateAccuracy(java.lang.String)
	 */
	public String getObsDateAccuracy(String plotName)
	{
		return observation.getObservationNarrative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationAccessionNumber(java.lang.String)
	 */
	public String getObservationAccessionNumber(String plotId)
	{
		return observation.getObsaccessionnumber();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorAddressStartDate(java.lang.String)
	 */
	public String getObservationContributorAddressStartDate(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorAdministrativeArea(java.lang.String)
	 */
	public String getObservationContributorAdministrativeArea(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorCellPhoneNumber(java.lang.String)
	 */
	public String getObservationContributorCellPhoneNumber(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorCity(java.lang.String)
	 */
	public String getObservationContributorCity(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorContactInstructions(java.lang.String)
	 */
	public String getObservationContributorContactInstructions(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorCountry(java.lang.String)
	 */
	public String getObservationContributorCountry(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorCurrentFlag(java.lang.String)
	 */
	public String getObservationContributorCurrentFlag(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorDeliveryPoint(java.lang.String)
	 */
	public String getObservationContributorDeliveryPoint(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorEmailAddress(java.lang.String)
	 */
	public String getObservationContributorEmailAddress(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorFaxPhoneNumber(java.lang.String)
	 */
	public String getObservationContributorFaxPhoneNumber(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorGivenName(java.lang.String)
	 */
	public String getObservationContributorGivenName(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorMiddleName(java.lang.String)
	 */
	public String getObservationContributorMiddleName(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorOrganizationName(java.lang.String)
	 */
	public String getObservationContributorOrganizationName(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorOrgPosition(java.lang.String)
	 */
	public String getObservationContributorOrgPosition(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorPhoneNumber(java.lang.String)
	 */
	public String getObservationContributorPhoneNumber(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorPostalCode(java.lang.String)
	 */
	public String getObservationContributorPostalCode(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorRole(java.lang.String)
	 */
	public String getObservationContributorRole(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributors(java.lang.String)
	 */
	public Vector getObservationContributors(String plotName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorSalutation(java.lang.String)
	 */
	public String getObservationContributorSalutation(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationContributorSurName(java.lang.String)
	 */
	public String getObservationContributorSurName(String contributorWholeName)
	{
		// TODO: Hook up properly
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObservationNarrative(java.lang.String)
	 */
	public String getObservationNarrative(String plotName)
	{
		return observation.getObservationNarrative();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObsStartDate(java.lang.String)
	 */
	public String getObsStartDate(String plotName)
	{
		return observation.getObsStartDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getObsStopDate(java.lang.String)
	 */
	public String getObsStopDate(String plotName)
	{
		return observation.getObsStartDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getOrganicDepth(java.lang.String)
	 */
	public String getOrganicDepth(String plotName)
	{
		return observation.getOrganicDepth();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getOriginalData(java.lang.String)
	 */
	public String getOriginalData(String plotName)
	{
		return observation.getOriginalData();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentBareSoil(java.lang.String)
	 */
	public String getPercentBareSoil(String plotName)
	{
		return observation.getPercentBareSoil();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentBedRock(java.lang.String)
	 */
	public String getPercentBedRock(String plotName)
	{
		return observation.getPercentBedRock();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentLitter(java.lang.String)
	 */
	public String getPercentLitter(String plotName)
	{
		return observation.getPercentLitter();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentOther(java.lang.String)
	 */
	public String getPercentOther(String plotName)
	{
		return observation.getPercentOther();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentRockGravel(java.lang.String)
	 */
	public String getPercentRockGravel(String plotName)
	{
		return observation.getPercentRockGravel();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentWater(java.lang.String)
	 */
	public String getPercentWater(String plotName)
	{
		return observation.getPercentWater();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPercentWood(java.lang.String)
	 */
	public String getPercentWood(String plotName)
	{
		return observation.getPercentWood();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPhenologicalAspect(java.lang.String)
	 */
	public String getPhenologicalAspect(String plotName)
	{
		return observation.getPhenologicAspect();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlaceCode(java.lang.String)
	 */
	public String getPlaceCode(String placeName)
	{
		NamedPlace namedPlace = (NamedPlace) placeNames.get(placeName);
		return namedPlace.getPlaceCode();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlaceDescription(java.lang.String)
	 */
	public String getPlaceDescription(String placeName)
	{
		NamedPlace namedPlace = (NamedPlace) placeNames.get(placeName);
		return namedPlace.getPlaceDescription();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlaceNames(java.lang.String)
	 */
	public Vector getPlaceNames(String plotName)
	{
		Vector placeNameList = new Vector();
		Iterator places = plot.getPLOTPlaces().iterator();
		while ( places.hasNext() )
		{
			Place place = (Place) places.next();
			NamedPlace namedPlace = (NamedPlace) place.getNAMEDPLACE();
			String key = namedPlace.getPlaceName();

			placeNames.put(key, place);
			placeNameList.add(key);
		}
		
		return placeNameList;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlaceOwner(java.lang.String)
	 */
	public String getPlaceOwner(String placeName)
	{
		NamedPlace namedPlace = (NamedPlace) placeNames.get(placeName);
		return namedPlace.getOwner();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlaceSystem(java.lang.String)
	 */
	public String getPlaceSystem(String placeName)
	{
		NamedPlace namedPlace = (NamedPlace) placeNames.get(placeName);
		return namedPlace.getPlaceSystem();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlantTaxaNames(java.lang.String)
	 */
	public Vector getPlantTaxaNames(String plotName)
	{
		Vector nameList = new Vector();
		// TODO: Need to go through the plantusage, plantconcept and plantusage to get the Sci name
		Iterator taxonObservations = observation.getOBSERVATIONTaxonObservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			TaxonObservation to = (TaxonObservation) taxonObservations.next();
			String key = "";
			//PlantName plantName = (PlantName) to.getPLANTNAME();
			Iterator taxonInterpritations = to.getTAXONOBSERVATIONTaxonInterpretations().iterator();
			if ( taxonInterpritations.hasNext() )
			{
				TaxonInterpretation ti =  (TaxonInterpretation) taxonInterpritations.next();
				key = ti.getPLANTCONCEPT().getPlantname();
			}
			taxonObs.put(key, to);
			nameList.add(key);
		}
		return nameList;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlantTaxonCode(java.lang.String)
	 */
	public String getPlantTaxonCode(String plantName)
	{
		TaxonObservation to = (TaxonObservation) taxonObs.get(plantName);
		return to.getPLANTNAME().getPlantName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlantTaxonCover(java.lang.String, java.lang.String)
	 */
	public String getPlantTaxonCover(String plantName, String plotName)
	{
		TaxonObservation to = (TaxonObservation) taxonObs.get(plantName);
		return to.getTaxonCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotArea(java.lang.String)
	 */
	public String getPlotArea(String plotName)
	{
		return plot.getArea();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotCode(java.lang.String)
	 */
	public String getPlotCode(String plotName)
	{
		return plot.getAuthorPlotCode();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotData(java.lang.String)
	 */
	public void getPlotData(String plotName) throws Exception
	{
		// TODO: Not sure what to do with this???
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotNames()
	 */
	public Vector getPlotNames()
	{
		// TODO: I think this is a useless method here?
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotShape(java.lang.String)
	 */
	public String getPlotShape(String plotName)
	{
		return plot.getShape();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getPlotValidationLevel(java.lang.String)
	 */
	public String getPlotValidationLevel(String plotName)
	{
		return observation.getPlotValidationLevel();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorAddressStartDate(java.lang.String)
	 */
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
		String addressStartDate = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			addressStartDate = ( (Address) party.getpartyAddresss().get(0)).getAddressStartDate();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return addressStartDate;
	}
	

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorAdministrativeArea(java.lang.String)
	 */
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		String administrativeArea = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			administrativeArea = ( (Address) party.getpartyAddresss().get(0)).getAdministrativeArea();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return administrativeArea;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorCellPhoneNumber(java.lang.String)
	 */
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getSalutation();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorCity(java.lang.String)
	 */
	public String getProjectContributorCity(String contributorWholeName)
	{
		String city = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			city = ( (Address) party.getpartyAddresss().get(0)).getCity();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return city;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorContactInstructions(java.lang.String)
	 */
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getContactInstructions();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorCountry(java.lang.String)
	 */
	public String getProjectContributorCountry(String contributorWholeName)
	{
		String country = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			country = ( (Address) party.getpartyAddresss().get(0)).getCountry();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return country;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorCurrentFlag(java.lang.String)
	 */
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		String currentFlag = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			currentFlag = ( (Address) party.getpartyAddresss().get(0)).getCurrentFlag();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return currentFlag;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorDeliveryPoint(java.lang.String)
	 */
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		String deliveryPoint = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			deliveryPoint = ( (Address) party.getpartyAddresss().get(0)).getDeliveryPoint();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return deliveryPoint;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorEmailAddress(java.lang.String)
	 */
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getEmail();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorFaxPhoneNumber(java.lang.String)
	 */
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		List telephones = party.getPARTYTelephones();
		Iterator telIter = telephones.iterator();
		while ( telIter.hasNext() )
		{
			Telephone tel =  (Telephone)telIter.next();
			if ( tel.getPhoneType().equalsIgnoreCase("Fax") )
			{
				return tel.getPhoneNumber();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorGivenName(java.lang.String)
	 */
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getGivenName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorMiddleName(java.lang.String)
	 */
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getMiddleName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorOrganizationName(java.lang.String)
	 */
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getOrganizationName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorOrgPosition(java.lang.String)
	 */
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
		// TODO: Find out whats need here
		Party party = (Party) projectContibs.get(contributorWholeName);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorPhoneNumber(java.lang.String)
	 */
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		List telephones = party.getPARTYTelephones();
		Iterator telIter = telephones.iterator();
		while ( telIter.hasNext() )
		{
			Telephone tel =  (Telephone)telIter.next();
			if ( tel.getPhoneType().equalsIgnoreCase("Work") )
			{
				return tel.getPhoneNumber();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorPostalCode(java.lang.String)
	 */
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		String postalCode = null;
		Party party = (Party) projectContibs.get(contributorWholeName);
		try
		{
			postalCode = ( (Address) party.getpartyAddresss().get(0)).getPostalCode();
		}
		catch (RuntimeException e)
		{
			return null;
		}
		return postalCode;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributors(java.lang.String)
	 */
	public Vector getProjectContributors(String plotName)
	{
		Vector nameList = new Vector();
		List projectContributors = project.getPROJECTProjectContributors();
		Iterator pcIterator = projectContributors.iterator();
		while ( pcIterator.hasNext() )
		{
			ProjectContributor  pc = (ProjectContributor) pcIterator.next();
			Party party = pc.getPARTY();
			String key = party.getGivenName() + " " + party.getSurName();
			projectContibs.put(key, party);
			nameList.add(key);
		}
		
		return nameList;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorSalutation(java.lang.String)
	 */
	public String getProjectContributorSalutation(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getSalutation();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectContributorSurName(java.lang.String)
	 */
	public String getProjectContributorSurName(String contributorWholeName)
	{
		Party party = (Party) projectContibs.get(contributorWholeName);
		return party.getSurName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectDescription(java.lang.String)
	 */
	public String getProjectDescription(String plotName)
	{
		return project.getProjectDescription();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectName(java.lang.String)
	 */
	public String getProjectName(String plotName)
	{
    if ( project == null )
    {
      this.init(new Integer(plotName).intValue() );
    }
		return project.getProjectName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectStartDate(java.lang.String)
	 */
	public String getProjectStartDate(String plotName)
	{
		return project.getStartDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getProjectStopDate(java.lang.String)
	 */
	public String getProjectStopDate(String plotName)
	{
		return project.getStopDate();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getRepresentativeness(java.lang.String)
	 */
	public String getRepresentativeness(String plotName)
	{
		return observation.getRepresentativeness();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getRevisions(java.lang.String)
	 */
	public boolean getRevisions(String plotName)
	{
		return new Boolean( observation.getRevisions() ).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getRockType(java.lang.String)
	 */
	public String getRockType(String plotName)
	{
		return plot.getRockType();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getShoreDistance(java.lang.String)
	 */
	public String getShoreDistance(String plotName)
	{
		return observation.getShoreDistance();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getShrubCover(java.lang.String)
	 */
	public String getShrubCover(String plotName)
	{
		return observation.getShrubCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getShrubHt(java.lang.String)
	 */
	public String getShrubHt(String plotName)
	{
		return observation.getShrubHt();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSlopeAspect(java.lang.String)
	 */
	public String getSlopeAspect(String plotName)
	{
		return plot.getSlopeAspect();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSlopeGradient(java.lang.String)
	 */
	public String getSlopeGradient(String plotName)
	{
		return plot.getSlopeGradient();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSoilDepth(java.lang.String)
	 */
	public String getSoilDepth(String plotName)
	{
		return observation.getSoilDepth();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSoilDrainage(java.lang.String)
	 */
	public String getSoilDrainage(String plotName)
	{
		return observation.getSoilDrainage();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSoilMoistureRegime(java.lang.String)
	 */
	public String getSoilMoistureRegime(String plotName)
	{
		return observation.getSoilMoistureRegime();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSoilTaxon(java.lang.String)
	 */
	public String getSoilTaxon(String plotName)
	{
		return soilTaxon.getSoilName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSoilTaxonSource(java.lang.String)
	 */
	public String getSoilTaxonSource(String plotName)
	{
		return observation.getSoilTaxonSrc();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStandMaturity(java.lang.String)
	 */
	public String getStandMaturity(String plotName)
	{
		return observation.getStandMaturity();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStandSize(java.lang.String)
	 */
	public String getStandSize(String plotName)
	{
		return plot.getStandSize();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getState(java.lang.String)
	 */
	public String getState(String plotName)
	{
		return plot.getState();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStemObservationArea(java.lang.String)
	 */
	public String getStemObservationArea(String plotName)
	{
		return observation.getStemObservationArea();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStemSampleMethod(java.lang.String)
	 */
	public String getStemSampleMethod(String plotName)
	{
		return observation.getStemSampleMethod();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStemSizeLimit(java.lang.String)
	 */
	public String getStemSizeLimit(String plotName)
	{
		return observation.getStemSizeLimit();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStrataBase(java.lang.String, java.lang.String)
	 */
	public String getStrataBase(String plotName, String stratumName)
	{
		Stratum stratum = (Stratum) strataNames.get(stratumName);
		return stratum.getStratumBase();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStrataCover(java.lang.String, java.lang.String)
	 */
	public String getStrataCover(String plotName, String stratumName)
	{
		Stratum stratum = (Stratum) strataNames.get(stratumName);
		return stratum.getStratumCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStrataHeight(java.lang.String, java.lang.String)
	 */
	public String getStrataHeight(String plotName, String stratumName)
	{
		Stratum stratum = (Stratum) strataNames.get(stratumName);
		return stratum.getStratumHeight();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStratumMethod(java.lang.String)
	 */
	public Hashtable getStratumMethod(String plotName)
	{
		// TODO: Should this be here? returning null for now
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getStratumMethodName(java.lang.String)
	 */
	public String getStratumMethodName(String plotName)
	{
		return stratumMethod.getStratumMethodName();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSubmergedCover(java.lang.String)
	 */
	public String getSubmergedCover(String plotName)
	{
		return observation.getSubmergedCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSubmergedHt(java.lang.String)
	 */
	public String getSubmergedHt(String plotName)
	{
		return observation.getSubmergedHt();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSuccessionalStatus(java.lang.String)
	 */
	public String getSuccessionalStatus(String plotName)
	{
		return observation.getSuccessionalStatus();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSurfGeo(java.lang.String)
	 */
	public String getSurfGeo(String plotName)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getSurficialDeposits(java.lang.String)
	 */
	public String getSurficialDeposits(String plotName)
	{
		return plot.getSurficialDeposits();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTaxaStrataCover(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getTaxaStrataCover(
		String plantName,
		String plotCode,
		String stratumName)
	{
		StratumComposition sc = (StratumComposition)  stratumComposition.get(stratumName);
		return sc.getTaxonStratumCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTaxaStrataExistence(java.lang.String, java.lang.String)
	 */
	public Vector getTaxaStrataExistence(String plantName, String plotName)
	{
		Vector strataList = new Vector();
		TaxonObservation to = (TaxonObservation) taxonObs.get(plantName);
		List scList = to.getTAXONOBSERVATIONStratumCompositions();
		Iterator startumCompsitions = scList.iterator();
		while ( startumCompsitions.hasNext() )
		{
			StratumComposition sc = (StratumComposition) startumCompsitions.next();
			String stratumName = sc.getSTRATUM().getStratumName();
			strataList.add(stratumName);
			stratumComposition.put(stratumName, sc);
		}
		return strataList;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTaxonObservationArea(java.lang.String)
	 */
	public String getTaxonObservationArea(String plotName)
	{
		return observation.getTaxonObservationArea();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTopoPosition(java.lang.String)
	 */
	public String getTopoPosition(String plotName)
	{
		return plot.getTopoPosition();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTreeCover(java.lang.String)
	 */
	public String getTreeCover(String plotName)
	{
		return observation.getTreeCover();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getTreeHt(java.lang.String)
	 */
	public String getTreeHt(String plotName)
	{
		return observation.getTreeHt();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getUniqueStrataNames(java.lang.String)
	 */
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector uniqueStrataNames = new Vector();
		Iterator strata = observation.getOBSERVATIONStratums().iterator();
		while ( strata.hasNext())
		{
			Stratum stratum = (Stratum) strata.next();
			String statumName = stratum.getStratumName();
			uniqueStrataNames.add(statumName);
			strataNames.put(statumName, stratum);
		}
		return uniqueStrataNames;
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getUTMZone(java.lang.String)
	 */
	public String getUTMZone(String plotName)
	{
		// TODO: Confirm
		return plot.getAuthorZone();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getWaterDepth(java.lang.String)
	 */
	public String getWaterDepth(String plotName)
	{
		return observation.getWaterDepth();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getWaterSalinity(java.lang.String)
	 */
	public String getWaterSalinity(String plotName)
	{
		return observation.getWaterSalinity();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getXCoord(java.lang.String)
	 */
	public String getXCoord(String plotName)
	{
		return plot.getAuthorE();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#getYCoord(java.lang.String)
	 */
	public String getYCoord(String plotName)
	{
		return plot.getAuthorN();
	}

	/* (non-Javadoc)
	 * @see org.vegbank.plots.datasource.PlotDataSourceInterface#isPlotPermanent(java.lang.String)
	 */
	public boolean isPlotPermanent(String plotName)
	{
		return new Boolean( plot.getPermanence() ).booleanValue();
	}

}
