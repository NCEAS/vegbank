/*
 *	'$RCSfile: NativeXMLReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-19 17:39:03 $'
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
 
package org.vegbank.plots.datasource;

/**
 * @author farrell
 */

import java.io.File;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.vegbank.common.utility.Utility;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class NativeXMLReader 
{	
	private XMLReader reader = null;
	private boolean validate = false;
	public NativeXMLReader( boolean validating ) throws Exception
	{
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			reader = saxParser.getXMLReader();
			try 
			{
				reader.setFeature(
					"http://xml.org/sax/features/namespaces",
					true);
				reader.setFeature(
					"http://xml.org/sax/features/namespace-prefixes",
					true);
					
				this.setValidate(validating);
			}
			catch (SAXException e) 
			{
				System.err.println("could not set parser feature");
				throw e;
			}
		} 
		catch (FactoryConfigurationError e) 
		{
			// unable to get a document builder factory
		} 
		catch (ParserConfigurationException e)
		{
			// parser was unable to be configured
		}
		catch (SAXException e) 
		{
			// parsing error
		} 
	}



	public boolean validate( InputSource pInputSource, DefaultHandler dh )
  	{
	 	return parse(pInputSource, dh);
  	}



  	public boolean parse(InputSource pInputSource, DefaultHandler dh)
  	{
	  	boolean mblnValue = false;
	  	try
		{
			reader.setContentHandler(dh);
		 	reader.parse(pInputSource);
		   	mblnValue = true;
		   	return mblnValue;
		}
		catch ( Exception  e  )
		{
			System.out.println( "Could not Parse file " );
			e.printStackTrace();
		}

		return mblnValue;
	}
	
	public boolean parse(InputSource pInputSource)
	{
		boolean mblnValue = false;
		try
		{
			reader.parse(pInputSource);
			mblnValue = true;
			return mblnValue;
		}
		catch ( Exception  e  )
		{
			System.out.println( "Could not Parse file " );
			e.printStackTrace();
		}

		return mblnValue;
	}
	
	
	/**
	 * @return
	 */
	public boolean isValidate()
	{
		return validate;
	}

	/**
	 * @param b
	 */
	public void setValidate(boolean b) throws Exception
	{
		try 
		{
			reader.setFeature(
				"http://xml.org/sax/features/validation",
				b);
			reader.setFeature(
				"http://apache.org/xml/features/validation/schema",
				b);
				
			// Sanity check -- can the schema be found
			File schema = new File(Utility.VEGBANK_XML_SCHEMA);
			if ( ! schema.exists() || ! schema.canRead() )
			{
				// Cannot find or work with this file
				throw new Exception("Cannot find the schema file at '" + Utility.VEGBANK_XML_SCHEMA + "'.");
			}
			else
			{
				System.out.println("NativeXMLReader > Found the schema file: '" + Utility.VEGBANK_XML_SCHEMA + "'.");
			}
			
			reader.setProperty(
				"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
				Utility.VEGBANK_XML_SCHEMA);	
		}
		catch (SAXException e) 
		{
			System.err.println("could not set parser feature");
			throw e;
		}
	}
	
	public void setContentHandler( ContentHandler ch)
	{
		reader.setContentHandler(ch);
	}



	/**
	 * @return XMLReader
	 */
	public XMLReader getXMLReader()
	{
		// TODO Auto-generated method stub
		return reader;
	}


}
