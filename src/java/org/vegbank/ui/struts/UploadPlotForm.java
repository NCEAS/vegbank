/* *	
 * '$RCSfile: UploadPlotForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-03 03:49:47 $'
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
package org.vegbank.ui.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.upload.MultipartRequestHandler;

/**
 * @author farrell
 *
 * The ActionForm thats supports Plot Uploads 
 */
public class UploadPlotForm extends ValidatorForm 
{
	// The formats accepted by vegbank
	public static final int NATURESERVE_FORMAT = 0;
	public static final int VEGBANK_ACCESS_FORMAT = 1;
	public static final int VEGBANK_XML_FORMAT = 2;
	
	// Give a default
	private int archieveType = VEGBANK_XML_FORMAT;
	// Only allow false for now
	private final String updateArchivedPlot = "false";
	private FormFile plotFile = null; 
	public static final String ERROR_PROPERTY_MAX_LENGTH_EXCEEDED = "org.apache.struts.webapp.upload.MaxLengthExceeded";
	
	

	/**
	 * @return
	 */
	public int getArchiveType()
	{
		return archieveType;
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
	public String getUpdateArchivedPlot()
	{
		return updateArchivedPlot;
	}

	/**
	 * @param string
	 */
	public void setArchiveType(int format)
	{
		archieveType = format;
	}

	/**
	 * @param file
	 */
	public void setPlotFile(FormFile file)
	{
		plotFile = file;
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
		System.out.println("About to validate the form");
		ActionErrors errors = super.validate(am, req);
		if ( errors == null)
			errors = new ActionErrors();
			
		FormFile plotFile = this.getPlotFile();

		if ( plotFile== null 
			|| plotFile.getFileSize() == 0 )
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError("errors.required", "A Plot datafile")
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
		return errors;
	}

}
