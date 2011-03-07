/*
 * '$RCSfile: UploadPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-10 00:01:51 $'
 *	'$Revision: 1.21 $'
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
import org.vegbank.common.model.WebUser;
import org.vegbank.common.utility.FileWrapper;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DateUtility;
import org.vegbank.common.utility.WebFileFetch;
import org.vegbank.common.utility.datafileexchange.DataFileDB;
import org.vegbank.dataload.XML.*;

/**
 * @author anderson
 *
 * Struts Action to upload plot from file
 */
public class UploadPlotAction extends VegbankAction
{
	private static Log log = LogFactory.getLog(UploadPlotAction.class);
	private static ResourceBundle rb = ResourceBundle.getBundle("vegbank");
	private String saveDir = "";


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

		int archiveType;
		boolean upload;
		boolean validate = true;
		boolean rectify = false;
		boolean dbLoad = false;
		String fileURL = null;
		FileWrapper file = null;
		String savedFileName = null;
		Collection files = new Vector();
		VegbankXMLUploadThread worker = null;
		HttpSession session = request.getSession();
		WebUser user = getUser(session);
		String userId = user.getUseridLong().toString();

		setSaveDir(user.getUploadDir(true));

		// put the email address in context
		request.setAttribute("email", user.getEmail());

		VegbankActionMapping vbMapping = (VegbankActionMapping)mapping;
		String param = vbMapping.getParameter();
		if (param != null && param.equals("form")) {
		    // just load the form
		    log.debug("forwarding to form...");
		    return mapping.findForward("form");
		}

		log.debug("processing XML upload");
		dbLoad = true;
		archiveType = theForm.getArchiveType();
		upload = theForm.isUpload();

		// Handle the uploaded file
		if (upload) {
			/////////////////////////////////////////////////
			// LOCAL UPLOAD
			/////////////////////////////////////////////////
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
			/////////////////////////////////////////////////
			// REMOTE DOWNLOAD
			/////////////////////////////////////////////////
			fileURL = theForm.getPlotFileURL();

			// fetch the file
			try {
				WebFileFetch fetcher = new WebFileFetch();

				log.debug("user's save dir: " + this.saveDir);
				fetcher.setSaveDir(this.saveDir);

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


		/////////////////////////////////////////////////
		// PREPARE THE FILE
		/////////////////////////////////////////////////
		String usersFileName = file.getFileName();
		String extension = this.getFileExtension(file.getFileName());
		if ( extension.equalsIgnoreCase("ZIP") ) {
				try {
					// Need to Unzip
					 files = ServletUtility.unZipToPath(file,getSaveDir() + "/tmp");

				} catch (Exception e) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
								"errors.action.failed",
								e.getMessage()));
					log.error("error unzipping given file: " + e.getMessage());
				}
		} else {
			try {
				log.debug("Adding file: " + file.getFileName());
				files.add( new FileWrapper(file) );
				file.destroy();

			} catch (Exception e) {
				errors.add(
					ActionErrors.GLOBAL_ERROR,
						new ActionError(
							"errors.action.failed",
								e.getMessage()));
				log.error("error adding given file: " + e.getMessage());
			}
		}

		if (files.size() == 1) {
			FileWrapper thisFile = (FileWrapper) files.iterator().next();
			extension = this.getFileExtension(thisFile.getFileName());

			if ( extension.equalsIgnoreCase("XML") ) {
				try {
					savedFileName = this.saveFile(thisFile);

				} catch (Exception e) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.action.failed", e.getMessage()));
					log.error("Problem saving uploaded file", e);
				}

			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.action.failed",
							"VegBank can only load an .xml file or a zipped .xml file" ));
				log.info("Attempt to load a file with extension " + extension + " failed");
			}
		} else {
			if (files.size() > 1) {
		    	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.action.failed",
					"VegBank can only handle one file at a time (even if zipped archive)"));
		    } else {
			    	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.action.failed",
					"VegBank failed to unzip your file correctly."));
		    }
			log.debug(files.size() + " where uploaded at once by user");
		}

		log.debug("saved file path: " + savedFileName);


		/////////////////////////////////////////////////
		// PARSE THE FILE
		/////////////////////////////////////////////////
		try {
			switch (archiveType) {
				case UploadPlotForm.VEGBANK_XML_FORMAT :
					log.info("VEGBANK_XML_FORMAT format uploaded: " + savedFileName);
					worker = this.uploadVegbankXML(request, savedFileName, validate, dbLoad, rectify, user);
					break;

				default:
					log.info("Unknown format uploaded");
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"Unknown format type uploaded"));
			}
		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.action.failed", e.getMessage()));
			log.error("Problem parsing XML", e);
		}


		// Report any errors we have discovered back to the original form
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.getInputForward());
		}


        /*
		// put the worker thread in the session
		if (worker != null) {
			log.debug("setting PW worker thread in session");
			HttpSession session = request.getSession();

			worker.setPlotFilePath(savedFileName);
			if (summarize) {
				worker.setForward("DisplayLoadSummary");
			} else {
				worker.setForward("DisplayLoadReport");
			}

			session.setAttribute("threadId",  worker.getThreadId());
			session.setAttribute("started",  new Boolean(true));
			session.setAttribute(worker.getThreadId(),  worker);
		} else {
			log.debug("no worker thread");
		}
        */

        /*
		if (summarize) {
			request.setAttribute("plotFilePath", savedFileName);
		}
        */

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.general", worker.getStatusMessages()));
		saveMessages(request, messages);

		//return mapping.findForward("PleaseWait");
		return mapping.findForward("summary");
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
			boolean validate, boolean dbLoad, boolean rectify, WebUser webUser) throws Exception
	{
		log.debug("============= SPAWNING LOADER THREAD ============= " + savedFileName);
		VegbankXMLUploadThread worker = new VegbankXMLUploadThread();
		worker.init(validate, rectify, dbLoad, savedFileName, webUser);
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

		try {
			// make the upload dirs if not there
			File udir = new File(fileNameAndPath);
			if (!udir.exists()) {
				udir.mkdirs();
			}
		} catch (Exception ex) {
			log.error("problem making upload dirs", ex);
		}

		fileNameAndPath += file.getFileName();

		//write the file to the file specified
		log.debug("file content-type: " + file.getContentType());
		InputStream stream;

		// 11/2/2004: ORIGINAL WAY
		stream = file.getInputStream();
		//

		//
		// 11/2/2004: Force the encoding to be UTF-16
		//
		//String fileContents = new String(file.getFileData());
		//stream = new ByteArrayInputStream(fileContents.getBytes("UTF-16"));

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


	/**
	 * Simply sets the save directory.
	 */
	private void setSaveDir(String s) {
		this.saveDir = s;
	}


	/**
	 * Simply returns the save directory as is.
	 */
	private String getSaveDir() {
		return this.saveDir;
	}
}
