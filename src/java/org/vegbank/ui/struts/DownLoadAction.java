package org.vegbank.ui.struts;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.ResourceBundle;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.model.Observation;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.XMLUtil;
import org.vegbank.plots.datasink.ASCIIReportsHelper;
import org.vegbank.plots.datasource.DBModelBeanReader;
import org.vegbank.xmlresource.transformXML;

import com.Ostermiller.util.LineEnds;

/*
 * '$RCSfile: DownLoadAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-06-04 17:00:31 $'
 *	'$Revision: 1.9 $'
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
	private static Log log = LogFactory.getLog(DownLoadAction.class);

	// Supported FormatTypes
	private static final String XML_FORMAT_TYPE = "xml";
	private static final String FLAT_FORMAT_TYPE = "flat";
	private static final String VEGBRANCH_FORMAT_TYPE = "vegbranch";
	
	// Supported dataTypes
	private static final String ALL_DATA_TYPE = "all";
	private static final String SPECIES_DATA_TYPE = "species";
	private static final String ENVIRONMENTAL_DATA_TYPE = "environmental";
	
	// Response content Types
	private static final String ZIP_CONTENT_TYPE = "application/x-zip";
	private static final String DOWNLOAD_CONTENT_TYPE = "application/x-zip";

	// Resource paths
	private static ResourceBundle res = ResourceBundle.getBundle("vegbank");
	private static final String VEGBRANCH_XSL_PATH = res.getString("vegbank2vegbranch_xsl");

	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		log.debug(" In DownLoadAction ");
		ActionErrors errors = new ActionErrors();
		String fwd = null; // Do not go forward if successfull, download file
		
		// Get the form
		DynaActionForm thisForm = (DynaActionForm) form;
		
		String dataType = (String ) thisForm.get("dataType");
		String formatType = (String) thisForm.get("formatType");
		String[] plotsToDownLoad = (String[]) thisForm.get("plotsToDownLoad");
		
		log.debug("dataType = " + dataType + ", formatType = " + formatType +", plotsToDownLoad = " + plotsToDownLoad);
		
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
					//this.initResponseForFileDownLoad( response, "VegbankDownload.xml", DOWNLOAD_CONTENT_TYPE);
					//this.sendFileToBrowser(XML, response);

					/////////////////
					// ZIP the XML doc
					/////////////////
					this.initResponseForFileDownLoad(response, "VegbankDownload.zip", ZIP_CONTENT_TYPE);
					
					Hashtable nameContent = new Hashtable();
					nameContent.put("plotObservations.xml", XML);
					OutputStream responseOutputStream = response.getOutputStream();
					responseOutputStream.flush();
					
					ServletUtility.zipFiles( nameContent, responseOutputStream, LineEnds.STYLE_DOS );
						
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
					String environmentalData = null;
					String speciesData = null;
					
					// Get plotObservation Collection
					Collection plotObservations = this.getPlotObservations(plotsToDownLoad);

					if (dataType.equalsIgnoreCase(ENVIRONMENTAL_DATA_TYPE)
						|| dataType.equalsIgnoreCase(ALL_DATA_TYPE))
					{
						environmentalData = 
							ASCIIReportsHelper.getEnvironmentalData(plotObservations);

					}
					if (dataType.equalsIgnoreCase(SPECIES_DATA_TYPE)
						|| dataType.equalsIgnoreCase(ALL_DATA_TYPE))
					{
						speciesData =
							ASCIIReportsHelper.getSpeciesData(plotObservations);
					}

					// Place generated file in ZIP archive
					if ( dataType.equalsIgnoreCase(ALL_DATA_TYPE) )
					{
						this.initResponseForFileDownLoad(response, "VegbankDownload.zip", ZIP_CONTENT_TYPE);
						
						Hashtable nameContent = new Hashtable();
						nameContent.put("environmentalData.txt",environmentalData);
						nameContent.put("speciesData.txt", speciesData);
						OutputStream responseOutputStream = response.getOutputStream();
						responseOutputStream.flush();
						
						// TODO: Get the OS of user if possible and return a native file	
						// For now use DOS style, cause those idiots would freak with anything else ;)					
						ServletUtility.zipFiles( nameContent, responseOutputStream, LineEnds.STYLE_DOS );
						
					}
					// Just downLoad the generated file
					else 
					{
						this.initResponseForFileDownLoad( response, "VegbankDownload.txt", DOWNLOAD_CONTENT_TYPE);
						if ( environmentalData != null )
						{
							this.sendFileToBrowser( environmentalData.toString() , response);
						}
						else if ( speciesData != null ) 
						{
							this.sendFileToBrowser( speciesData.toString() , response);
						}
					}
				}
			}
			else if ( formatType.equalsIgnoreCase( VEGBRANCH_FORMAT_TYPE ) )
			{
				// Check that all data is requested
				if ( dataType.equalsIgnoreCase( ALL_DATA_TYPE ) )
				{
					// Store the returned ModelBean Trees
					Collection plotObservations = this.getPlotObservations(plotsToDownLoad);
			
					// wrap in XML
					String xml = XMLUtil.getVBXML(plotObservations);

					java.io.File f = new java.io.File(VEGBRANCH_XSL_PATH);
					log.debug("XSL file: " + f.getAbsolutePath());
					if (f.exists()) {
						log.debug("XSL file exists");
					}
					
					// Use XSLT to transform the XML
					transformXML transformer = new transformXML();
					String xmlToImport = transformer.getTransformedFromString(xml, VEGBRANCH_XSL_PATH);

					log.debug("Transformed XML; VegBranch CSV:\n" + xmlToImport);

					/////////////////
					// ZIP the CSV doc
					/////////////////
					this.initResponseForFileDownLoad(response, "VegBranchImport.zip", ZIP_CONTENT_TYPE);
					
					Hashtable nameContent = new Hashtable();
					nameContent.put("VegBranchImport.csv", xmlToImport);
					OutputStream responseOutputStream = response.getOutputStream();
					responseOutputStream.flush();
					
					ServletUtility.zipFiles( nameContent, responseOutputStream, LineEnds.STYLE_DOS );
					/////////////////
						
				}
				else
				{
					// Invalid Request --- xml formatType only supports dataType all
					errors.add(
						Globals.ERROR_KEY,
						new ActionMessage(
							"errors.action.failed",
							"Download '"
								+ VEGBRANCH_FORMAT_TYPE
								+ "' formatType only supports - dataType = '"
								+ ALL_DATA_TYPE
								+ "', '"
								+ dataType
								+ "' not supported"));
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
			log.debug("DownLoadAction: " + e.getMessage(), e);
			e.printStackTrace();
			errors.add(
				Globals.ERROR_KEY,
				new ActionMessage("errors.action.failed", e.getMessage() ));
		}
		
		if ( ! errors.isEmpty() )
		{
			saveErrors(request, errors);
			// Need to put plotsToDownLoad into the request 
			//TODO: use the form bean instead 
			request.setAttribute("plotsToDownLoad", plotsToDownLoad);
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
			log.debug("DownLoadAction : DownLoading " + plotsToDownLoad[i]);
			Observation observation = (Observation) dbmbReader.getVBModelBean( plotsToDownLoad[i]  );
			plotObsersevations.add(observation);
		}
		dbmbReader.releaseConnection();
		return plotObsersevations;
	}
	
	private void initResponseForFileDownLoad(HttpServletResponse response, String downloadFileName , String contentType) 
	{
		response.setContentType(contentType);
		response.setHeader("Content-Disposition","attachment; filename="+downloadFileName+";"); 
	}
	
	private void sendFileToBrowser( String fileContents, HttpServletResponse response ) throws IOException
	{
		//log.debug(fileContents);
		response.getWriter().write(fileContents);
	}
}
