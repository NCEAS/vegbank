package org.vegbank.ui.struts;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.vegbank.common.model.Commclass;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Stratumcomposition;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.XMLUtil;
import org.vegbank.plots.datasource.DBModelBeanReader;

/*
 * '$RCSfile: DownLoadAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-03 05:06:37 $'
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
/**
 * @author farrell
 *
 * Struts action to download file(s) based on id
 */
public class DownLoadAction extends Action
{
	// Supported FormatTypes
	private static final String XML_FORMAT_TYPE = "xml";
	private static final String FLAT_FORMAT_TYPE = "flat";
	
	// Supported dataTypes
	private static final String ALL_DATA_TYPE = "all";
	private static final String SPECIES_DATA_TYPE = "species";
	private static final String ENVIRONMENTAL_DATA_TYPE = "environmental";
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log(" In DownLoadAction ");
		ActionErrors errors = new ActionErrors();
		String fwd = null; // Do not go forward if successfull, download file
		
		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		
		String dataType = (String ) thisForm.get("dataType");
		String formatType = (String) thisForm.get("formatType");
		// Get the plots form the session
		String[] plotsToDownLoad = (String[]) request.getSession().getAttribute("plotsToDownLoad");
		
		LogUtility.log("dataType = " + dataType + ", formatType = " + formatType +", plotsToDownLoad = " + plotsToDownLoad);
		
		try
		{
			// Handle the format type
			if ( formatType.equalsIgnoreCase( XML_FORMAT_TYPE) )
			{
				// Check that all data is requested
				if ( dataType.equalsIgnoreCase( ALL_DATA_TYPE ) )
				{					
					// Store the returned ModelBean Trees
					Collection plotObservations = this.getPlotObservations(plotsToDownLoad);
			
					// wrap in XML
					String XML = XMLUtil.getVBXML(plotObservations);
					
					// Place into file for download
					this.initResponseForFileDownLoad( response, "VegbankDownload.xml");
					this.sendFileToBrowser(XML, response);
				}
				else
				{
					// Invalid Request --- xml formatType only supports dataType all
					errors.add(
						Globals.ERROR_KEY,
						new ActionMessage(
							"errors.action.failed",
							"Download '"
								+ XML_FORMAT_TYPE
								+ "' formatType only supports - dataType = '"
								+ ALL_DATA_TYPE
								+ "', '"
								+ dataType
								+ "' not supported"));
				}
			}
			else if ( formatType.equalsIgnoreCase( FLAT_FORMAT_TYPE ) )
			{
				if ( ! dataType.equalsIgnoreCase( ALL_DATA_TYPE)  && ! dataType.equalsIgnoreCase( ENVIRONMENTAL_DATA_TYPE) && ! dataType.equalsIgnoreCase( SPECIES_DATA_TYPE))
				{
					// Invalid Request --- xml formatType only supports dataType all
					errors.add(
						Globals.ERROR_KEY,
						new ActionMessage(
							"errors.action.failed",
							"Download '"
								+ FLAT_FORMAT_TYPE
								+ "' formatType does not support, dataType = '"
								+ dataType + "'"));
				}
				// Good to go
				else 
				{			
					StringBuffer environmentalData = null;
					StringBuffer speciesData = null;
					
					// Get plotObservation Collection
					Collection plotObservations = this.getPlotObservations(plotsToDownLoad);

					if (dataType.equalsIgnoreCase(ENVIRONMENTAL_DATA_TYPE)
						|| dataType.equalsIgnoreCase(ALL_DATA_TYPE))
					{
						// Do enviromental data
						environmentalData = new StringBuffer();

						// Header line
						environmentalData.append(
							"PLOT ID, ACCESSION_NUMBER, COMMUNITY NAME, COLLECTION DATE, LATITUDE, LONGITUDE, ELEVATION, SLOPE ASPECT, SLOPE GRADIENT\n");

						// process plot observations
						Iterator pos = plotObservations.iterator();
						while (pos.hasNext())
						{
							Plot plot = (Plot) pos.next();

						// Get the values of interest
						int plotObservationId = 0;
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

					}
					if (dataType.equalsIgnoreCase(SPECIES_DATA_TYPE)
						|| dataType.equalsIgnoreCase(ALL_DATA_TYPE))
					{
						// Do species
						speciesData = new StringBuffer();

						// Header line
						speciesData.append(
							"PLOT ID, PLANT NAME, STRATUMCOVER, STRATUMNAME, TAXONCOVER\n");

						// process plot observations
						Iterator pos = plotObservations.iterator();
						while (pos.hasNext())
						{
							Plot plot = (Plot) pos.next();

							// Get the values of interest
							int plotObservationId = 0;
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

									plantName = taxonObservation.getAuthorplantname();
									
									if ( Utility.isStringNullOrEmpty(plantName) )
									{
										List taxonInterpretations = taxonObservation.gettaxonobservation_taxoninterpretations();
										Iterator taxonInterpIter = taxonInterpretations.iterator();
										while( taxonInterpIter.hasNext() )
										{
											Taxoninterpretation ti =  (Taxoninterpretation) taxonInterpIter.next();
											// Converting a String to boolean ... risky
											System.out.println("Is this the current interpritation ?" + ti.getCurrentinterpretation() );
											if ( Utility.isTrue(ti.getCurrentinterpretation()) )
											{
												plantName = ti.getPlantconceptobject().getPlantnameobject().getPlantname();
												System.out.println("The Plantname is  ?" +plantName );
											}
											if ( Utility.isStringNullOrEmpty(plantName) )
											{
												// No valid taxonInterpritation found use the authors name for the plant
												// *** to indicate on the ui that this is not accepted yet .
												plantName =  taxonObservation.getCheatplantname() ;
											}
										}
									}								
									
									taxonCover = taxonObservation.getTaxoncover();
									
									//Get stratumComposition
									List stratumCompositions = taxonObservation.gettaxonobservation_stratumcompositions();
									Iterator stratumCompIter = stratumCompositions.iterator();
									while ( stratumCompIter.hasNext() )
									{
										Stratumcomposition stratumComposition = (Stratumcomposition) stratumCompIter.next();
										stratumCover = stratumComposition.getTaxonstratumcover();
									
										// FIXME: Null pointer City
										String statumName = stratumComposition.getStratumobject().getStratumtypeobject().getStratumname();

										// Put them in the String
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
					}

					// Place generated file in ZIP archive
					if ( dataType.equalsIgnoreCase(ALL_DATA_TYPE) )
					{
						this.initResponseForFileDownLoad(response, "VegbankDownload.zip");
						
						Hashtable nameContent = new Hashtable();
						nameContent.put("environmentalData.txt",environmentalData);
						nameContent.put("speciesData.txt", speciesData);
						OutputStream responseOutputStream = response.getOutputStream();
						ServletUtility.putZippedFileInOutputStream( nameContent, responseOutputStream );
						
					}
					// Just downLoad the generated file
					else 
					{
						this.initResponseForFileDownLoad( response, "VegbankDownload.txt");
						if ( environmentalData != null )
						{
							this.sendFileToBrowser(environmentalData.toString() , response);
						}
						else if ( speciesData != null ) 
						{
							this.sendFileToBrowser(speciesData.toString() , response);
						}
					}
				}
			}
			else
			{
				// Invalid Request
				errors.add(
					Globals.ERROR_KEY,
					new ActionMessage("errors.action.failed", "formatType = '" + formatType +  "' not supported"));
			}
		}
		catch (Exception e)
		{
			LogUtility.log("DownLoadAction: " + e.getMessage(), e);
			e.printStackTrace();
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage("errors.action.failed", e.getMessage() ));
		}
		
		if ( ! errors.isEmpty() )
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());	
		}
		
		return mapping.findForward(fwd);
	}
	
	/**
	 * @param plotsToDownLoad
	 * @return
	 */
	private Collection getPlotObservations(String[] plotsToDownLoad) throws NumberFormatException, Exception
	{
		Collection plotObsersevations = new ArrayList();
		DBModelBeanReader dbmbReader = new DBModelBeanReader();
		
		// Get the plots
		for ( int i = 0; i < plotsToDownLoad.length ; i++ )
		{
			LogUtility.log("DownLoadAction : DownLoading " + plotsToDownLoad[i]);
			Plot plot = dbmbReader.getPlotObservationBeanTree( new Integer(plotsToDownLoad[i]).intValue()  );
			plotObsersevations.add(plot);
		}
		return plotObsersevations;
	}
	
	private void initResponseForFileDownLoad(HttpServletResponse response, String downloadFileName ) 
	{
		response.setContentType("application/stream");
		response.setHeader("Content-Disposition","attachment; filename="+downloadFileName+";"); 
	}
	
	private void sendFileToBrowser( String fileContents, HttpServletResponse response ) throws IOException
	{
		response.getWriter().print(fileContents);
	}
}
