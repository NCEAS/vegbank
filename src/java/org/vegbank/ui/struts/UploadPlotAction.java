package org.vegbank.ui.struts;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.vegbank.common.utility.FileWrapper;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.datafileexchange.DataFileDB;
import org.vegbank.plots.datasource.VegbankXMLUpload;
import org.vegbank.plots.datasource.VegbankXMLUpload.LoadingErrors;


/*
 * '$RCSfile: UploadPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-27 19:13:52 $'
 *	'$Revision: 1.8 $'
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
 * Struts Action to upload plot from file
 */
public class UploadPlotAction extends Action
{
	private String originalDataStore = "/usr/vegbank/originalDataStore";
	private ServletUtility util = new ServletUtility();
	
	// ResourceBundle properties
	private static ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private static final String rmiServer= rb.getString("rmiserver");
	private static final int rmiServerPort = 1099;
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		LogUtility.log(" In UploadPlotAction ", LogUtility.TRACE);
		ActionErrors errors = new ActionErrors();
		LoadingErrors loadingErrors = null;

		UploadPlotForm theForm = (UploadPlotForm) form;

		int archiveType = theForm.getArchiveType();
		// Always false for now
		String updateArchivedPlot = theForm.getUpdateArchivedPlot();
		boolean validate = theForm.isValidate();
		boolean rectify = theForm.isRectify();
		boolean upload = theForm.isUpload();
		
		FormFile file = theForm.getPlotFile();
		String usersFileName = file.getFileName();
		// Check if has zip extension
		String savedFileName = null;
		
		Collection files = new Vector();
		
		String extension = this.getFileExtension(  file.getFileName());
		if ( extension.equalsIgnoreCase("ZIP") )
		{
				try
				{
					// Need to Unzip
					 files = ServletUtility.unZip(file);
				} 
				catch (Exception e)
				{
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
								"errors.action.failed",
								e.getMessage()));
					LogUtility.log("Added error: " + e.getMessage(), LogUtility.ERROR);
				}
		}
		else
		{
			try
			{
				files.add( new FileWrapper(file) );
			} 
			catch (Exception e) 
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
								e.getMessage()));
				LogUtility.log("Added error: " + e.getMessage(), LogUtility.ERROR);
			}
		}

		LogUtility.log("Got " + files.size() + " files ");
		if ( files.size() > 1 )
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"errors.action.failed", "Vegbank can only handle one file at a time ( even if zipped archive )"));
			LogUtility.log("Added error: " + files.size() + " where uploaded at once by user");	
		}
		else
		{
			FileWrapper thisFile = (FileWrapper) files.iterator().next();
			
			String thisExtension = this.getFileExtension(  thisFile.getFileName());
			if ( thisExtension.equalsIgnoreCase("XML") )
			{
				try 
				{
					savedFileName = this.saveAndRegisterFile(thisFile);
				}
				catch (Exception e) {
					errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
							e.getMessage()));
				}
				
				try
				{	
					// Each format is handled differently
					switch (archiveType)
					{
						case UploadPlotForm.NATURESERVE_FORMAT :
							LogUtility.log("UploadPlotAction: NATURESERVE_FORMAT format uploaded", LogUtility.INFO);
							break;
				
						case UploadPlotForm.VEGBANK_ACCESS_FORMAT :
							LogUtility.log("UploadPlotAction: VEGBANK_ACCESS_FORMAT format uploaded", LogUtility.INFO);
							break;
				
						case UploadPlotForm.VEGBANK_XML_FORMAT :
							LogUtility.log("UploadPlotAction: VEGBANK_XML_FORMAT format uploaded", LogUtility.INFO);
							loadingErrors = this.uploadVegbankXML(savedFileName, validate,
									upload, rectify);
							break;
				
						default :
							LogUtility.log("UploadPlotAction: Unknown format uploaded", LogUtility.WARN);
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"Unknown format type uploaded"));
					}
				} catch (Exception e)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.action.failed", e.getMessage()));
					LogUtility.log("Added error: " + e.getMessage(), e);
				}
			}
			else 
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.action.failed", "Vegbank can only upload a *.xml file or a zipped *.xml file" ));
				LogUtility.log("Added error: Attempt to load a file with extension  " + thisExtension + " failed", LogUtility.WARN);
			}
		}
		//destroy the temporary file created
		file.destroy();


		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}

		request.setAttribute("ErrorsReport",  loadingErrors);
		return mapping.findForward("DisplayLoadReport");
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String getFileExtension(String string)
	{
		String extension = string.substring(string.lastIndexOf('.') + 1);
		LogUtility.log("Got extension: " + extension + " from: " + string, LogUtility.INFO);
		return extension;
	}

	/**
	 * @param savedFileName
	 */
	private LoadingErrors uploadVegbankXML(String savedFileName, boolean validate, boolean upload, boolean rectify) throws Exception
	{
		//System.out.println(validate + " , " +  upload + " , " + rectify);
		VegbankXMLUpload xmlUpload = new VegbankXMLUpload(validate, rectify, upload);
		xmlUpload.processXMLFile(savedFileName);
		return xmlUpload.getErrors();
	}

	/**
	 * Save file to filesystem using a unique name aquired from the the framework 
	 * database.
	 * 
	 * @param FormFile -- the uploaded file
	 * @return String -- the fileName used for saving
	 */
	private String saveAndRegisterFile(FileWrapper file)
		throws Exception
	{
		// Need to get a InputStream
		InputStream stream = file.getInputStream();
		
		DataFileDB filedb = new DataFileDB();
		int fileName = filedb.getNewAccessionId();
		String fileNameAndPath = originalDataStore + "/" + new Integer(fileName).toString();

		//write the file to the file specified
		OutputStream bos = new FileOutputStream(fileNameAndPath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
		{
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		LogUtility.log(
			"UploadPlotAction: The file has been written to \"" + fileNameAndPath + "\"", LogUtility.INFO);

		//close the stream
		stream.close();

		//register the document with the database
		filedb.registerDocument(
			fileName,
			file.getFileSize(),  
			file.getFileName(),
			originalDataStore);
			
		return fileNameAndPath;
	}
}
