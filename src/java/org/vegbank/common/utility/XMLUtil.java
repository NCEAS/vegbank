package org.vegbank.common.utility;

/*
 * '$RCSfile: XMLUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-07-11 19:26:56 $'
 *	'$Revision: 1.6 $'
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
 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.vegbank.common.model.VBModelBean;

/**
 * @author farrell
 *
 * A utility class for handling XML
 */
public class XMLUtil
{

	/**
	 * Takes a collection VegbankModelBeans and returns an XML String
	 * 
	 * @param Objects
	 * @return
	 */
	public static String getVBXML( Collection vegbankModelBeans)
	{
		System.out.println("here in getVBXML( Collection ) ");
		// Stringbuffer for constructing XML String
		StringBuffer sb = new StringBuffer();
		
		// TODO: Need a sort here to ensure schema order is followed
		Iterator iterator = vegbankModelBeans.iterator();
		while ( iterator.hasNext() )
		{
      //System.out.println("iterating through vegbankModelBeans");
			VBModelBean vbmb = (VBModelBean) iterator.next();
      //System.out.println("creating xml: " + vbmb.toXML());
			sb.append( vbmb.toXML() );
		}
		String entireXML = wrapInBoilerPlateXML(sb); 	
		return entireXML;
	}
	

	/**
	 * Convience method when only one VBModelBean to XMLify
	 * 
	 * @param vbmb
	 * @return
	 */
	public static String getVBXML( VBModelBean  vbmb)
	{
		System.out.println("here in getVBXML( VBModelBean ) ");
		Collection c = new ArrayList();
		c.add(vbmb);
		return getVBXML(c);
	}
	
	/**
	 * Wrap StringBuffer in boilerplate XML.
	 * 
	 * @param StringBuffer -- get wrapped in boilerplate 
	 */
	private  static String wrapInBoilerPlateXML( StringBuffer sb)
	{
		StringBuffer fullXML = new StringBuffer();
		
		// TODO: This varible need to be configurable
		String schemaWebLocation = "http://vegbank.org/vegdocs/xml/";
		
		fullXML.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		// TODO: Delaring schema to use here, may need to allow refernce to schema and no refernce
		fullXML.append("<VegBankPackage xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"" + schemaWebLocation + Utility.VEGBANK_SCHEMA_NAME + "\">\n" );
		 
		fullXML.append("\t<doc-VegBankVersion>" + Utility.VEGBANK_VERSION + "</doc-VegBankVersion>\n");
		fullXML.append("\t<doc-date>" + convertDateToXSdatetime(Utility.getNow()) + "</doc-date>\n");
		// TODO: Implement a getCurrentPartyFullName() method and/or pass in value 
		fullXML.append("\t<doc-author>" + "Gabriel Farrell" + "</doc-author>\n"); 
		fullXML.append("\t<doc-authorSoftware>Vegbank, version: " + Utility.VEGBANK_VERSION + "</doc-authorSoftware>\n");
		// TODO: Add relevant comments 
		fullXML.append("\t<doc-comments></doc-comments>\n");

		// Add the true content
		fullXML.append( sb.toString() );
		
		// Close up shop
		fullXML.append("</VegBankPackage>");

		// Overwrite the incomming sb with the fullXML sb
		return  fullXML.toString();
	}

	
	/**
	 * Convert Java Date object to a string that is a valid xml schema datatime datatype
	 * 
	 * @param Date -- the java date to convert
	 * @return String -- the date in valid xs datetime syntax
	 */
	public static String convertDateToXSdatetime(Date date)
	{
		String xsDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";
		SimpleDateFormat df = new SimpleDateFormat(xsDateTimePattern);
		//LogUtility.log("Utility: " + df.format(date));
		return df.format(date);
	}


}
