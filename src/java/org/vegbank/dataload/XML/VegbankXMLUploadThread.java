package org.vegbank.dataload.XML;

/*
 * '$RCSfile: VegbankXMLUploadThread.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-07-28 07:42:37 $'
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.PleaseWaitThread;
import org.vegbank.plots.datasource.NativeXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author anderson
 *
 * Runs the XML uploader in its own thread.
 * 
 */
public class VegbankXMLUploadThread extends PleaseWaitThread 
{
	private static Log log = LogFactory.getLog(VegbankXMLUploadThread.class);

	// XML REFRESH rate:  10 sec per 1MB
	private static final int SEC_PER_DELAY_XML = 10;
	private static final int BYTES_PER_DELAY_XML = 1048576;

	// ZIP REFRESH rate:  45 sec per 1MB
	private static final int BYTES_PER_DELAY_ZIP = 1048576;
	
	private ActionMessages messages;
	private VegbankXMLUpload xmlUpload;
	private String xmlFileName;
	private String fwdName;
	private boolean isDone;
	private int delayLength = 0;

	/**
	 * <p>Set all the availible options using this constructor</code>
	 * 
	 * @param validate
	 * @param rectify
	 * @param load
	 * @param xmlFileName
	 * @throws Exception
	 */
	public void init(boolean validate, boolean rectify, boolean load, String xmlFileName) throws Exception
	{
		xmlUpload = new VegbankXMLUpload(validate, rectify, load);
		xmlUpload.setLoad(load);
		xmlUpload.setRectify(rectify);
		xmlUpload.setValidate(validate);
		xmlUpload.getXMLReader();  // sets private xr in parent

		// set up the refresh rate, in # seconds delay
		/*
		int fileSize = xmlUpload.getUploadFileSize();
		int fileType = xmlUpload.getUploadFileType();

		if (fileType == VegbankXMLUpload.TYPE_XML) {
			delayLength = (SEC_PER_DELAY_XML / BYTES_PER_DELAY_XML) * fileSize;
		} else if (fileType == VegbankXMLUpload.TYPE_ZIP) {
			delayLength = (SEC_PER_DELAY_ZIP / BYTES_PER_DELAY_ZIP) * fileSize;
		}
		*/
		delayLength = 10;
		log.debug("Delay: " + delayLength);


		// used only in Thread
		this.xmlFileName = xmlFileName;
		this.fwdName = "vberror";
		this.isDone = false;
		this.messages = new ActionMessages();
	} 

	/* ========================================================== */
	/* PleaseWaitThread implementation */
	/* ========================================================== */
	/**
	 * 
	 */
	public void run() {
		messages.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage("errors.general",
					"Loading XML..."));

		try {
			xmlUpload.processXMLFile(this.xmlFileName, this);

		} catch (Exception ex) {
			messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.general",
						"ERROR WHILE LOADING: " + ex.toString()));
		}

		isDone = true;
		fwdName = "DisplayLoadReport";

		messages.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage("errors.general",
					"XML import complete."));
	}

	/**
	 * @return ActionMessages
	 */
	public ActionMessages getStatusMessages() {
		return messages;
	}

	/**
	 * @return boolean
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * @return String name of forward 
	 */
	public String getForward() {
		return fwdName;
	}

	/**
	 * Prepares the request.
	 */
	public void prepareRequest(HttpServletRequest request) {
		request.getSession().setAttribute("ErrorsReport",  xmlUpload.getErrors());
		request.getSession().setAttribute("delayLength",  String.valueOf(delayLength));
	}

}
