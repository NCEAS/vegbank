package org.vegbank.plots.datasink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vegbank.common.model.*;
import org.vegbank.common.utility.MBReadHelper;

/*
 * '$RCSfile: ASCIIReportsHelper.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-13 22:36:35 $'
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
 * Helper for creating ASCII reports from data model beans.
 */
public class ASCIIReportsHelper
{

	/**
	 * Create an environmental data report from a single plot model 
	 * beans.
	 * 
	 * @param plotObservations
	 * @return String -- The ASCII report
	 */
	public static String getEnvironmentalData ( Plot plotObservation )
	{
		Collection plotObservations = new ArrayList();
		plotObservations.add(plotObservation);
		return ASCIIReportsHelper.getEnvironmentalData( plotObservations );
	}

	/**
	 * Create an environmental data report from a collection of plot model 
	 * beans.
	 * 
	 * @param plotObservations
	 * @return String -- The ASCII report
	 */	
	public static String getEnvironmentalData( Collection plotObservations)
	{
		// Do enviromental data
		StringBuffer environmentalData = new StringBuffer();

		// Header line
		environmentalData.append(
			"PLOT ID, ACCESSION_NUMBER, COMMUNITY NAME, COLLECTION DATE, LATITUDE, LONGITUDE, ELEVATION, SLOPE ASPECT, SLOPE GRADIENT\n");

		// process plot observations
		Iterator pos = plotObservations.iterator();
		while (pos.hasNext())
		{
			Plot plot = (Plot) pos.next();

		// Get the values of interest
		long plotObservationId = 0;
		String commName = null;
		String accNumber = null;
		String collectionDate = null;
		String latitude = plot.getLatitude();
		String longitude = plot.getLongitude();
		String elevatation = plot.getElevation();
		String slopeAspect = plot.getSlopeaspect();
		String slopeGradient = plot.getSlopegradient();
						
		List observations = plot.getplot_observations();
		if ( ! observations.isEmpty() )
		{
			//Get the observation ( should just be one ) 
			Observation obs = (Observation) observations.get(0);
							
			plotObservationId = obs.getObservation_id();
			accNumber = obs.getObsaccessionnumber();
			collectionDate = obs.getObsenddate();
							
			// Get commName
			List commclasses = obs.getobservation_commclasss();
			if ( ! commclasses.isEmpty() )
			{
				// Just get the first one FIXME: Can be more than one
				Commclass commclass = (Commclass) commclasses.get(0);
				commName = commclass.getCommname();
			}
		}

			// Put them in the String
			environmentalData.append(
				""
					+ plotObservationId
					+ ","
					+ accNumber
					+ ","
					+ commName
					+ ","
					+ collectionDate
					+ ","
					+ latitude
					+ ","
					+ longitude
					+ ","
					+ elevatation
					+ ","
					+ slopeAspect
					+ ","
					+ slopeGradient
					+"\n");
		}
		return environmentalData.toString();
	}
	
	/**
	 * Create a species data report from a single plot model 
	 * beans.
	 * 
	 * @param plotObservations
	 * @return String -- The ASCII report
	 */
	public static String getSpeciesData ( Plot plotObservation )
	{
		Collection plotObservations = new ArrayList();
		plotObservations.add(plotObservation);
		return ASCIIReportsHelper.getSpeciesData( plotObservations );
	}
	
	/**
	 * Create a species data report from a collection of plot model 
	 * beans.
	 * 
	 * @param plotObservations
	 * @return String -- The ASCII report
	 */
	public static String getSpeciesData ( Collection plotObservations )
	{
		// Do species
		StringBuffer speciesData = new StringBuffer();

		// Header line
		speciesData.append(
			"PLOT ID, PLANT NAME, STRATUMCOVER, STRATUMNAME, TAXONCOVER\n");

		// process plot observations
		Iterator pos = plotObservations.iterator();
		while (pos.hasNext())
		{
			Plot plot = (Plot) pos.next();

			// Get the values of interest
			long plotObservationId = 0;
			String plantName = null;
			String stratumCover = null;
			String stratumName = null;
			String taxonCover = null;

			List observations = plot.getplot_observations();
			if ( ! observations.isEmpty() )
			{
				//Get the observation ( should just be one ) 
				Observation obs = (Observation) observations.get(0);
								
				plotObservationId = obs.getObservation_id();
								
				// Get taxonObservations
				List taxonobservations = obs.getobservation_taxonobservations();
				Iterator taxonIter = taxonobservations.iterator();
				while ( taxonIter.hasNext() )
				{
					Taxonobservation taxonObservation = (Taxonobservation) taxonIter.next();

					plantName = MBReadHelper.getPlantName(taxonObservation);												
									
					taxonCover = taxonObservation.getTaxoncover();
									
					//Get stratumComposition
					List stratumCompositions = taxonObservation.gettaxonobservation_stratumcompositions();
					Iterator stratumCompIter = stratumCompositions.iterator();
					while ( stratumCompIter.hasNext() )
					{
						Stratumcomposition stratumComposition = (Stratumcomposition) stratumCompIter.next();
						stratumCover = stratumComposition.getTaxonstratumcover();
									
						// Get the stratum name
						Stratum stratum = stratumComposition.getStratumobject();
						if ( stratum != null )
						{
							Stratumtype stratumType = stratum.getStratumtypeobject();
							if ( stratumType != null )
							{
								stratumName = stratumType.getStratumname();
							}
						}
						
						// Put them in the StringBuffer
						speciesData.append(
							""
								+ plotObservationId
								+ ","
								+ plantName
								+ ","
								+ stratumCover
								+ ","
								+ stratumName
								+ ","
								+ taxonCover
								+"\n");
					}
				}
			}
		}
		return speciesData.toString();
	}

}
