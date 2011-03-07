package org.vegbank.dataload.XML;

/*
 * '$RCSfile: VegbankXMLUpload.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-06-02 21:15:15 $'
 *	'$Revision: 1.10 $'
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
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.DataloadLog;
import org.vegbank.common.utility.ConditionalContentHandlerController;
import org.vegbank.common.model.WebUser;
import org.vegbank.plots.datasource.NativeXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author farrell
 *
 * Reads a Vegbank format XML file and
 * Validate and/or Retify and/or Load into the database. 
 * 
 * A report is generated capturing the results of each action called for.
 * 
 */
public class VegbankXMLUpload 
{
	private static Log log = LogFactory.getLog(VegbankXMLUpload.class);

	private XMLReader xr = null;
	private NativeXMLReader nr = null;
	
	private SAXValidationErrorHandler errorHandler = null;
	private SAX2DBContentHandler contentHandler = null;
	private File xmlFile = null;
	
	private boolean validate = true;
	// TODO: use this variable as expected
	private boolean rectify = true;	
	private boolean load = true;
	private WebUser webUser = null;
	
	// Errors reported
	private LoadingErrors errors = null;
	
	// AccessionCodes of loaded root entities
	private List accessionCodes = null;
  private org.vegbank.common.utility.Timer timer;

	
 	/**
 	 * <p>Use the defaults when contructing this object</p>
 	 * @throws Exception
 	 */
 	public VegbankXMLUpload() throws Exception
  {
		xr = this.getXMLReader();
  }

	/**
	 * <p>Set all the available options using this constructor</code>
	 * 
	 * @param validate
	 * @param rectify
	 * @param load
	 * @throws Exception
	 */
	public VegbankXMLUpload(boolean validate, boolean rectify, boolean load) throws Exception
	{
		this.setLoad(load);
		this.setRectify(rectify);
		this.setValidate(validate);
		xr = this.getXMLReader();
	} 
	
	/**
	 * @return
	 * @throws Exception
	 */
	public XMLReader getXMLReader() throws Exception
	{
		this.errors = new LoadingErrors();
		this.accessionCodes = new Vector();
		nr = new NativeXMLReader(validate);
		xr = nr.getXMLReader();
		return xr;
	}


	/**
	 * <p>Set the XML file</p>
	 * 
	 * @param xmlFileName
	 * @throws IOException
	 * @throws SAXException
	 */
	public void processXMLFile( String xmlFileName, 
			ConditionalContentHandlerController controller)
            throws IOException, SAXException
	{
    timer = new org.vegbank.common.utility.Timer("Time to process xml file");
		log.info("processing XML file");
		log.info("Validation on: " + this.validate);
		log.info("Rectification on: " + this.rectify);
		log.info("Database Loading on: " + this.load);

        String xmlFileNameNoPath = xmlFileName.substring(xmlFileName.lastIndexOf(File.separator)+1);
        String xmlLoadDir = xmlFileName.substring(0, xmlFileName.lastIndexOf(File.separator));
		log.info("Creating dataload log in: " + xmlLoadDir);
        DataloadLog dlog = new DataloadLog(xmlLoadDir);
        dlog.setUserEmail(webUser.getEmail());

		errorHandler = new SAXValidationErrorHandler(errors);
		contentHandler = new SAX2DBContentHandler(errors, accessionCodes, load, 
                xmlFileNameNoPath, webUser.getUseridLong(), dlog);
		contentHandler.setController(controller);
		log.debug("set ConditionalContentHandlerController");

		log.debug("Loading file " + xmlFileName);
		xmlFile = new File(xmlFileName);
		
		if ( validate )
		{	
			xr.setErrorHandler( errorHandler );
		    log.debug("about to parse XML");
		     try {    
                	xr.parse( this.getInputSource(xmlFile) );
                     }  catch(Exception vbex) {
                        log.error("FATAL: problem parsing xml: " + xmlFile + ": " + vbex.getMessage());
                      }
		    log.debug("done parsing XML");
		}
		
		log.debug("checking if valid...");
		if ( errorHandler.isValid() ) {
			log.info( xmlFileName + " is a valid vegbank XML package");

			//if ( load ) // Only load if told to
			//{
				log.debug("Loading XML to DB");
				xr.setContentHandler( contentHandler );
				xr.parse( this.getInputSource(xmlFile));	
			//} 

// TODO: give these to contentHandler
        //dlog.addTag("fileName", xxx);
        //dlog.addTag("timestamp", xxx);
        //dlog.addTag("username", xxx);

			if (!load) {
				contentHandler.generateSummary(null, 0);
			}
		} else {
			log.debug( "Invalid XML file.  No attempt was made to rectify or load this dataset");
      log.debug(errorHandler.getLoadingErrors().getTextReport("\n"));
		}

		log.debug("Done processing XML file");
    timer.stop();
	}
	
	private InputSource getInputSource( File pFile) throws FileNotFoundException
	{
		FileInputStream mFileInputStream = new FileInputStream( pFile );
		return new InputSource( mFileInputStream );
	}

	/**
	 * Returns the errors that were encountered upon loading
	 * 
	 * @return
	 */
	public LoadingErrors getErrors()
	{
		return errors;
	}
	
	/**
	 * Provide a command line interface 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void  main (String[] args) throws Exception
	{
		String fileName = null;
		String userEmail = null;
		VegbankXMLUpload vbUpload = new VegbankXMLUpload();
		
		System.out.println("Vegbank XML Parser version 1.0.2");
		System.out.println("Usage: java org.vegbank.plots.datasource  [-L] [-V] [-R] XMLFile");
		System.out.println("\t -V Turn off validation");
		System.out.println("\t -L Turn off loading");	
		System.out.println("\t -R Turn off rectification");	
		System.out.println("\t All are on by default");
		System.out.println("-----------------------------------------------------------------------");
		
		int argNumber = args.length;
		for ( int i=0 ; i<argNumber; i++)
		{
			if (args[i].equals("-L"))
			{
				vbUpload.setLoad(false);
			}
			else if (args[i].equals("-V"))
			{
				vbUpload.setValidate(false);
			}
			else if (args[i].equals("-R"))
			{
				vbUpload.setRectify(false);
			}
			else if (!args[i].startsWith("-"))
			{
                if (fileName == null) {
				    fileName = args[i];
                    /*
                } else {
                    userEmail = args[i];
                    */
                }
			}
			else
			{
				System.out.println("You entered and invalid option");
			}
		}
		
		if ( fileName == null) {
			System.out.println("Please enter XMLFile to process");
		} else if (userEmail == null) {
			System.out.println("Please enter a user's email address");
		} else {
			vbUpload.processXMLFile(fileName, null);
			
			vbUpload.getErrors().getTextReport(null);
		
			System.exit(0);
		}
	}

	/**
	 * <p>
	 * Sets Loading into database on and off
	 * </p>
	 * 
	 * @param b
	 */
	public void setLoad(boolean b)
	{
		load = b;
	}

	/**
	 * <p>
	 * Sets the user doing the loading.
	 * </p>
	 * 
	 * @param w
	 */
	public void setUser(WebUser w)
	{
		webUser = w;
	}

	/**
	 * <p>
	 * Sets Rectification on and off
	 * </p>
	 * 
	 * @param b
	 */
	public void setRectify(boolean b)
	{
		rectify = b;
	}

	/**
	 * <p>
	 * Sets validation on and off
	 * </p>
	 * 
	 * @param b
	 * @throws Exception
	 */
	public void setValidate(boolean b) throws Exception
	{
		validate = b;
	}
	
	/**
	 * @return AccessionCodes of the root entities loaded into the database
	 */
	public List getAccessionCodesLoaded()
	{
		List topLevelAccessionCodes = new Vector();
		// Need to filter out non top level elements		
		Iterator iter = accessionCodes.iterator();
		while (iter.hasNext())
		{
			String ac = (String) iter.next();
			HashMap parsedAC = Utility.parseAccessionCode(ac);
			String entityCode = (String) parsedAC.get("ENTITYCODE");
			if ( Utility.isRootEntity(entityCode))
			{
				topLevelAccessionCodes.add(ac);
			}

		}
		
		return topLevelAccessionCodes;
	}

	/**
	 *
	 */
	public Map getSummary() {
		return contentHandler.getSummary();
	}

}
