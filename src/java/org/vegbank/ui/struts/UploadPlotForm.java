/* *	
 * '$RCSfile: UploadPlotForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-10-05 02:13:01 $'
 *	'$Revision: 1.4 $'
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.Utility;

/**
 * @author farrell
 *
 * The ActionForm thats supports Plot Uploads 
 */
public class UploadPlotForm extends ValidatorForm 
{
	private static Log log = LogFactory.getLog(UploadPlotAction.class);

	// The formats accepted by vegbank
	public static final String LOCAL = "local";
	public static final String REMOTE = "remote";
	public static final int NATURESERVE_FORMAT = 0;
	public static final int VEGBANK_ACCESS_FORMAT = 1;
	public static final int VEGBANK_XML_FORMAT = 2;
	public static final String ERROR_PROPERTY_MAX_LENGTH_EXCEEDED = 
		"org.apache.struts.webapp.upload.MaxLengthExceeded";
	
	// Give a default
	private int archiveType = VEGBANK_XML_FORMAT;
	// Only allow false for now
	private final String updateArchivedPlot = "false";
	private FormFile plotFile = null; 
	private String plotFileURL = null; 
	
	// The following variables are for controlling uploader features
	// defaults are all true
	public boolean validate = true;
	public boolean rectify = true;
	public String dataFileLocation = "local";

	/**
	 * @return
	 */
	public int getArchiveType()
	{
		return archiveType;
	}

	/**
	 * @return
	 */
	public FormFile getPlotFile()
	{
		return plotFile;
	}

	/**
	 * @return
	 */
	public String getPlotFileURL()
	{
		return plotFileURL;
	}

	/**
	 * @return
	 */
	public String getUpdateArchivedPlot()
	{
		return updateArchivedPlot;
	}

	/**
	 * @param string
	 */
	public void setArchiveType(int format)
	{
		archiveType = format;
	}

	/**
	 * @param file
	 */
	public void setPlotFile(FormFile file)
	{
		plotFile = file;
	}

	/**
	 * @param string
	 */
	public void setPlotFileURL(String s)
	{
		plotFileURL = s;
	}

	/**
	 * @param b
	 */
	public void setUpdateArchivedPlot(String b)
	{
		//updateArchivedPlot = b;
	}


	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping am, HttpServletRequest req)
	{
		log.debug("validating...");

		ActionErrors errors = super.validate(am, req);
		if (errors == null) {
			errors = new ActionErrors();
		}
			
		if (isUpload()) {
			// check the upload file
			FormFile plotFile = this.getPlotFile();

			if (plotFile == null || plotFile.getFileSize() == 0)
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.required", "A valid plot datafile")
				);
			}
			
			// Check to see if the maximum file size has been exceeded!
			Boolean maxLengthExceeded = (Boolean)
					req.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
			if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue()))
			{
				String maxFileSize = am.getModuleConfig().getControllerConfig().getMaxFileSize();
				errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.maxLengthExceeded", maxFileSize ));
			}		

		} else {
			// make sure a URL was entered
			if (Utility.isStringNullOrEmpty(plotFileURL)) {
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError("errors.required", "A valid plot datafile URL")
				);
			}
		}

		return errors;
	}

	/**
	 * @return Returns the rectify.
	 */
	public boolean isRectify()
	{
		return rectify;
	}

	/**
	 * @param rectify The rectify to set.
	 */
	public void setRectify(Boolean rectify)
	{
		if ( rectify == null )
		{
			this.rectify = true;
		}
		this.rectify = rectify.booleanValue();
	}

	/**
	 * @return Returns the upload.
	 */
	public String getDataFileLocation()
	{
		log.debug("getting 'dataFileLocation': " + this.dataFileLocation);
		return dataFileLocation;
	}

	/**
	 * @return Returns the upload.
	 */
	public boolean isUpload()
	{
		if (this.dataFileLocation.equals(LOCAL)) { 
			log.debug("is local upload");
			return true;
		} else {
			log.debug("is remote download");
			return false;
		}
	}

	/**
	 * @param upload The upload to set.
	 */
	/*
	public void setUpload(String upload)
	{
		if ( Utility.isStringNullOrEmpty(upload) || upload != "true" )
		{
			this.upload = false;
		} else {
			this.upload = true;

		}
		log.debug("set 'upload' to " + this.upload);
	}
	*/

	/**
	 * @param dataFileLocation
	 */
	public void setDataFileLocation(String dfl)
	{
		if ( Utility.isStringNullOrEmpty(dfl) || !dfl.equals("remote") )
		{
			this.dataFileLocation = LOCAL;
		} else {
			this.dataFileLocation = REMOTE;
		}
		log.debug("set 'dataFileLocation' to " + this.dataFileLocation);
	}

	/**
	 * @return Returns the validate.
	 */
	public boolean isValidate()
	{
		return validate;
	}

	/**
	 * @param validate The validate to set.
	 */
	public void setValidate(Boolean validate)
	{
		if ( validate == null )
		{
			this.validate = true;
		}	
		this.validate = validate.booleanValue();
	}

}
