package org.vegbank.dataload.XML;

/*
 * '$RCSfile: VegbankXMLUpload.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-11-16 01:21:31 $'
 *	'$Revision: 1.5 $'
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
import org.vegbank.common.utility.ConditionalContentHandlerController;
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
	
	// Errors reported
	private LoadingErrors errors = null;
	
	// AccessionCodes of loaded root entities
	private List accessionCodes = null;

	
 	/**
 	 * <p>Use the defaults when contructing this object</p>
 	 * @throws Exception
 	 */
 	public VegbankXMLUpload() throws Exception
  {
		xr = this.getXMLReader();
  }

	/**
	 * <p>Set all the availible options using this constructor</code>
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
			ConditionalContentHandlerController controller) throws IOException, SAXException
	{
		log.info("processing XML file");
		log.info("Validation on: " + this.validate);
		log.info("Rectification on: " + this.rectify);
		log.info("Database Loading on: " + this.load);
		
		errorHandler = new SAXValidationErrorHandler(errors);
		contentHandler = new SAX2DBContentHandler(errors, accessionCodes, load);
		contentHandler.setController(controller);
		log.debug("Set ConditionalContentHandlerController");

		xmlFile = new File(xmlFileName);
		
		if ( validate )
		{	
			xr.setErrorHandler( errorHandler );
			xr.parse( this.getInputSource(xmlFile) );
		}
		
		if ( errorHandler.isValid() )
		{
			log.info( xmlFileName + " is a valid vegbank XML package");

			//if ( load ) // Only load if told to
			//{
				log.debug("Loading XML to DB");
				xr.setContentHandler( contentHandler );
				xr.parse( this.getInputSource(xmlFile));	
			//} 

			if (!load) {
				contentHandler.generateSummary(null, 0);
			}
		}
		else 
		{
			log.debug( "Invalid file.  No attempt was made to rectify or load this dataset");
		}
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
				fileName = args[i];
			}
			else
			{
				System.out.println("You entered and invalid option");
			}
		}
		
		if ( fileName == null)
		{
			System.out.println("Please enter XMLFile to process");
		}
		else
		{
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
