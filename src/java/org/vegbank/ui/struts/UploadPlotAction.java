package org.vegbank.ui.struts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.datafileexchange.DataFileDB;
import org.vegbank.plots.datasource.VegbankXMLUpload;
import org.vegbank.plots.datasource.VegbankXMLUpload.LoadingErrors;
import org.vegbank.plots.rmi.DataSourceClient;

/*
 * '$RCSfile: UploadPlotAction.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-10 23:37:14 $'
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
		System.out.println(" In UploadPlotAction ");
		ActionErrors errors = new ActionErrors();
		LoadingErrors loadingErrors = null;

		UploadPlotForm theForm = (UploadPlotForm) form;

		int archiveType = theForm.getArchiveType();
		// Always false for now
		String updateArchivedPlot = theForm.getUpdateArchivedPlot();
		FormFile file = theForm.getPlotFile();
		String savedFileName = null;
		
		try 
		{
			savedFileName = this.saveAndRegisterFile(file);
		}
		catch (Exception e) {
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError(
					"errors.database",
					e.getMessage()));
		}
		//destroy the temporary file created
		file.destroy();
		
		try
		{	
			// Each format is handled differently
			switch ( archiveType )
			{
				case UploadPlotForm.NATURESERVE_FORMAT:
					System.out.println("UploadPlotAction: NATURESERVE_FORMAT format uploaded");
					this.getPlotNamesFromPlotFile(savedFileName, true, "tnc", request);
					break;
					
				case UploadPlotForm.VEGBANK_ACCESS_FORMAT:
					System.out.println("UploadPlotAction: VEGBANK_ACCESS_FORMAT format uploaded");
					break;
					
				case UploadPlotForm.VEGBANK_XML_FORMAT:
					System.out.println("UploadPlotAction: VEGBANK_XML_FORMAT format uploaded");
					loadingErrors = this.uploadVegbankXML(savedFileName);
					break;
					
				default:
					System.out.println("UploadPlotAction: Unknown format uploaded");
					errors.add( ActionErrors.GLOBAL_ERROR,
						new ActionError("Unkown format type uploaded"));
			}
		} 
		catch ( RMIServerBusyException e)
		{
			errors.add(
				ActionErrors.GLOBAL_ERROR,
				new ActionError(
				"errors.database",
				"RMIServer Busy, please try upload latter")
			);
		}


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
	 * @param savedFileName
	 */
	private LoadingErrors uploadVegbankXML(String savedFileName)
	{
		VegbankXMLUpload xmlUpload = new VegbankXMLUpload();
		xmlUpload.uploadFromXMLFile(new File(savedFileName));
		return xmlUpload.getErrors();
	}

	/**
	 * Save file to filesystem using a unique name aquired from the the framework 
	 * database.
	 * 
	 * @param FormFile -- the uploaded file
	 * @return String -- the fileName used for saving
	 */
	private String saveAndRegisterFile(FormFile file)
		throws Exception
	{
		DataFileDB filedb = new DataFileDB();
		int fileName = filedb.getNewAccessionId();
		String fileNameAndPath = originalDataStore + "/" + new Integer(fileName).toString();
		//retrieve the file data
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream stream = file.getInputStream();

		//write the file to the file specified
		OutputStream bos = new FileOutputStream(fileNameAndPath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
		{
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		System.out.println(
			"UploadPlotAction: The file has been written to \"" + fileNameAndPath + "\"");

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
	
	private Vector getPlotNamesFromPlotFile(
		String fileName,
		boolean useRMI,
		String format,
		HttpServletRequest request) throws RMIServerBusyException
	{
		Vector plots = new Vector();

		if (useRMI)
		{
			// Need to use the RMIServer to read a mdb file 
			DataSourceClient rmiClient =
				new DataSourceClient(rmiServer, "" + rmiServerPort);

			// Is the RMI Server free 
			String location = rmiClient.getFileUploadLocation();

			if (location == null)
			{
				throw new RMIServerBusyException();
			}
			else
			{
				boolean sendResults =
					rmiClient.putMDBFile(
						fileName,
						format,
						location);

				System.out.println(
					"PlotUploadAction > RMI file send results: "
						+ sendResults);

				//validate that the plot archive is real
				boolean fileValidityResults =
					rmiClient.isMDBFileValid(location);
				System.out.println(
					"PlotUploadAction > file validity at RMI server: "
						+ fileValidityResults);

				// get the name of the plots
				System.out.println(
					"PlotUploadAction > instantiating an rmi client on: "
						+ rmiServer);
				rmiClient = new DataSourceClient(rmiServer, "" + rmiServerPort);
				plots = rmiClient.getPlotNames(format);

				request.getSession().setAttribute(
					"rmiFileUploadLocation",
					location);
			}
		}
		//		  else 
		//		  {
		//			  // TODO: Remove hard coding                    
		//			  PlotDataSource pds = new PlotDataSource("VegbankXMLPlugin");
		//			  plots = pds.getPlotNames();
		//		  }
		//			
		System.out.println(
			"DataSubmitServlet > number of plots in archive: " + plots.size());
		return plots;
	}
	
	public class RMIServerBusyException extends Exception
	{
		
	}

}
