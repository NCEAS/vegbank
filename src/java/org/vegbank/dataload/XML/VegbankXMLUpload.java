package org.vegbank.dataload.XML;

/*
 * '$RCSfile: VegbankXMLUpload.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-01 01:54:42 $'
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
 * Read a Vegbank format XML file, generate a report, and 
 * create a SQL insert statements.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.vegbank.common.utility.LogUtility;
import org.vegbank.plots.datasource.NativeXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class VegbankXMLUpload 
{
	private XMLReader xr = null;
	private NativeXMLReader nr = null;
	
	
	private boolean validate = true;
	// TODO: use this variable as expected
	private boolean rectify = true;	
	private boolean load = true;
	
	// Errors reported
	private LoadingErrors errors = null;
		
 	public VegbankXMLUpload() throws Exception
  {
		xr = this.getXMLReader();
  }

	public VegbankXMLUpload(boolean validate, boolean rectify, boolean load) throws Exception
	{
		this.setLoad(load);
		this.setRectify(rectify);
		this.setValidate(validate);
		xr = this.getXMLReader();
	} 
	
	public XMLReader getXMLReader() throws Exception
	{
		this.errors = new LoadingErrors();
		nr = new NativeXMLReader(validate);
		XMLReader xr = nr.getXMLReader();
		return xr;
	}


	public void processXMLFile( String xmlFileName ) throws IOException, SAXException
	{
		LogUtility.log( "VegbankXMLUpload: Validation on: " + this.validate, LogUtility.INFO);
		LogUtility.log( "VegbankXMLUpload: Rectification on: " + this.rectify, LogUtility.INFO);
		LogUtility.log( "VegbankXMLUpload: Database Loading on: " + this.load, LogUtility.INFO);
		
		SAXValidationErrorHandler errorHandler = new SAXValidationErrorHandler(errors);
		SAX2DBContentHandler contentHandler = new SAX2DBContentHandler(errors, load);
		
		File xmlFile = new File(xmlFileName);
		
		if ( validate )
		{	
			xr.setErrorHandler( errorHandler );
			xr.parse( this.getInputSource(xmlFile) );
			LogUtility.log( "This file is valid: " + errorHandler.isValid(), LogUtility.INFO );
		}
		
		if ( errorHandler.isValid() )
		{
			xr.setContentHandler( contentHandler );
			xr.parse( this.getInputSource(xmlFile));
		}
		else 
		{
			LogUtility.log( "No attempt was made to rectify or load this dataset", LogUtility.ERROR);
			errors.AddError(LoadingErrors.DATABASELOADINGERROR, "No attempt was made to load this dataset into the database");
			errors.AddError(LoadingErrors.RECTIFICATIONERROR, "No attempt was made to rectify this dataset with the database");
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
			vbUpload.processXMLFile(fileName);
			
			System.out.println("REPORT:\n");
			System.out.println(vbUpload.getErrors().getSummaryMessage());
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("\tVALIDATION");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getValidationReport("\n"));
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("\tRECTIFICATION");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getRectificationReport("\n"));
			System.out.println("-----------------------------------------------------------------------");			
			System.out.println("\tDATABASE LOADING");
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(vbUpload.getErrors().getLoadReport("\n"));
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------");
		
			System.exit(0);
		}
	}

	/**
	 * @param b
	 */
	public void setLoad(boolean b)
	{
		load = b;
	}

	/**
	 * @param b
	 */
	public void setRectify(boolean b)
	{
		rectify = b;
	}

	/**
	 * @param b
	 */
	public void setValidate(boolean b) throws Exception
	{
		validate = b;
	}
}
