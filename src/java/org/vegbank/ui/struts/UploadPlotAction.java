/*
 * '$RCSfile: UploadPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-11-01 20:37:52 $'
 *	'$Revision: 1.14 $'
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

import java.io.*;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Vector; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.FileWrapper;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.WebFileFetch;
import org.vegbank.common.utility.datafileexchange.DataFileDB;
import org.vegbank.dataload.XML.*;

/**
 * @author farrell
 *
 * Struts Action to upload plot from file
 */
public class UploadPlotAction extends VegbankAction
{
	private static Log log = LogFactory.getLog(UploadPlotAction.class);

	private String saveDir = "";
	private String uploadPath = Utility.VB_DATA_DIR;
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
		log.debug(" In UploadPlotAction ");
		ActionErrors errors = new ActionErrors();
		LoadingErrors loadingErrors = null;

		UploadPlotForm theForm = (UploadPlotForm) form;

		int archiveType = theForm.getArchiveType();
		// Always false for now
		String updateArchivedPlot = theForm.getUpdateArchivedPlot();
		boolean validate = theForm.isValidate();
		boolean rectify = theForm.isRectify();

		log.debug("ACTION.execute(): calling isUpload");
		boolean upload = theForm.isUpload();
		String savedFileName = null;
		VegbankXMLUploadThread worker = null;
		Collection files = new Vector();

		FileWrapper file = null;
		String fileURL = null;

		setSaveDir( getUser(request.getSession()).getUseridLong().toString() );

		if (upload) {
			try {
				file = new FileWrapper(theForm.getPlotFile());
			} catch (Exception ex) {
				errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
							"error uploading local file. " + ex.getMessage()));
				log.error("error uploading local file. " + ex.getMessage());
				saveErrors(request, errors);
				return (mapping.getInputForward());
			}
		} else {
			fileURL = theForm.getPlotFileURL();

			// fetch the file
			try {
				WebFileFetch fetcher = new WebFileFetch();

				String saveDir = getSaveDir();

				log.debug("user's save dir: " + saveDir);
				fetcher.setSaveDir(saveDir);

				file = new FileWrapper( 
						fetcher.saveRemoteFile(fileURL, null, "GET"));

			} catch (Exception ex) {
				errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
							"error downloading remote file. " + ex.getMessage()));
				log.error("error downloading remote file. " + ex.getMessage());
				saveErrors(request, errors);
				return (mapping.getInputForward());
			}

		}
		
		
		String usersFileName = file.getFileName();
		String extension = this.getFileExtension(file.getFileName());
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
					log.error("error unzipping given file: " + e.getMessage());
				}
		}
		else
		{
			try
			{
				log.debug("Adding file: " + file.getFileName());
				files.add( new FileWrapper(file) );
			} 
			catch (Exception e) 
			{
				errors.add(
					ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
								e.getMessage()));
				log.error("error adding given file: " + e.getMessage());
			}
		}

		log.debug("Got " + files.size() + " files ");
		if ( files.size() != 1 )
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"errors.action.failed", "Vegbank can only handle one file at a time ( even if zipped archive )"));
			log.debug("Added error: " + files.size() + " where uploaded at once by user");	
		}
		else
		{
			FileWrapper thisFile = (FileWrapper) files.iterator().next();
			
			String thisExtension = this.getFileExtension(  thisFile.getFileName());
			if ( thisExtension.equalsIgnoreCase("XML") )
			{
				try 
				{
					savedFileName = this.saveFile(thisFile);
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
							log.info("UploadPlotAction: NATURESERVE_FORMAT format uploaded");
							break;
				
						case UploadPlotForm.VEGBANK_ACCESS_FORMAT :
							log.info("UploadPlotAction: VEGBANK_ACCESS_FORMAT format uploaded");
							break;
				
						case UploadPlotForm.VEGBANK_XML_FORMAT :
							log.info("UploadPlotAction: VEGBANK_XML_FORMAT format uploaded");
							worker = this.uploadVegbankXML(request, savedFileName, validate, upload, rectify);
							break;
				
						default :
							log.warn("UploadPlotAction: Unknown format uploaded");
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"Unknown format type uploaded"));
					}
				} catch (Exception e)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.action.failed", e.getMessage()));
					log.debug("Added error: " + e.getMessage(), e);
				}
			}
			else 
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.action.failed", "Vegbank can only upload a *.xml file or a zipped *.xml file" ));
				log.warn("Added error: Attempt to load a file with extension  " + thisExtension + " failed");
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


		// put the worker thread in the session
		if (worker != null) {
			log.debug("setting thread in session");
			HttpSession session = request.getSession();
			session.setAttribute("threadId",  worker.getThreadId());
			session.setAttribute("started",  new Boolean(true));
			session.setAttribute(worker.getThreadId(),  worker);
		} else {
			log.debug("no worker thread");
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.general", worker.getStatusMessages()));
		saveMessages(request, messages);
		return mapping.findForward("PleaseWait");
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String getFileExtension(String string)
	{
		String extension = string.substring(string.lastIndexOf('.') + 1);
		log.debug("Got extension: " + extension + " from: " + string);
		return extension;
	}

	/**
	 * @param savedFileName
	 * @return worker thread 
	 */
	private VegbankXMLUploadThread uploadVegbankXML(HttpServletRequest request, String savedFileName, 
			boolean validate, boolean upload, boolean rectify) throws Exception
	{
		//System.out.println(validate + " , " +  upload + " , " + rectify);
		log.debug("============= SPAWNING LOADER THREAD =============");
		VegbankXMLUploadThread worker = new VegbankXMLUploadThread();
		worker.init(validate, rectify, upload, savedFileName);
		worker.start();
		return worker;
	}

	/**
	 * Just save the freaking file.
	 * 
	 * @param FormFile -- the uploaded file
	 * @return String -- the fileName used for saving
	 */
	private String saveFile(FileWrapper file) throws Exception {
		
		String fileNameAndPath = getSaveDir();
		fileNameAndPath += file.getFileName();

		//write the file to the file specified
		InputStream stream = file.getInputStream();
		OutputStream bos = new FileOutputStream(fileNameAndPath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		log.debug("UploadPlotAction: The file has been written to \"" + fileNameAndPath + "\"");

		//close the stream
		stream.close();

		return fileNameAndPath;
	}

	/**
	 * Save file to filesystem using a unique name aquired from the the framework 
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
		String fileNameAndPath = getSaveDir() + Integer.toString(fileName);

		//write the file to the file specified
		OutputStream bos = new FileOutputStream(fileNameAndPath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
		{
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		log.debug("UploadPlotAction: The file has been written to \"" + fileNameAndPath + "\"");

		//close the stream
		stream.close();

		//register the document with the database
		/*
		filedb.registerDocument(
			fileName,
			file.getFileSize(),  
			file.getFileName(),
			uploadPath);
		*/
			
		return fileNameAndPath;
	}


	private void setSaveDir(String userDirName) {
		saveDir = uploadPath;

		if (!saveDir.endsWith(File.separator)) {
			 saveDir += File.separator;
		}

		saveDir += userDirName;

		if (!saveDir.endsWith(File.separator)) {
			 saveDir += File.separator;
		}
		saveDir += File.separator;
	}


	private String getSaveDir() {
		return this.saveDir;
	}
}
