package org.vegbank.databaseAccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import org.vegbank.xmlresource.transformXML;

import org.vegbank.common.utility.*;
import org.vegbank.plots.datasource.PlotXmlWriterV2;
/**
 * Takes an xml file containg either query attributes
 * or plot data and depending on the document type, either
 * insert, update or query the database - see the diagram 
 * "plotAccessArchitecture.vsd" to better understand the 
 * relation of this class to database access module.
 *
 * 1] For database queries:
 * queryXML should be the xml document containing the query attributes
 * queryXSL is the XSLT sheet for transforming the queryXML into attributeType and 
 * attributeString action is, at this point, verify - to transform the document, 
 * and query which will formulate the query and pass it to the database
 * 
 * 2] for database insertion:
 * dataXML should be the xml document containing plot and/or plant
 * data.  dataXSL is the XSLT sheet for transforming the dataXML file into 
 * databaseAddress and dataValue and the current usable action is: verify
 * (same as above) and insert which will insert the plot data to the 
 * database
 *
 * 3] for database update:
 * at this point there is no update capabilities, although it will
 * allow the user to update the database, based on a plot xml 
 * document containing only partial data from a plot 
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-10-27 20:27:55 $'
 *  '$Revision: 1.8 $'
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

public class dbAccess 
{

	public String queryOutput[] = new String[10000]; //the output from query
	public int queryOutputNum; //the number of output rows from the query

	/**
	 * constructor method
		*/
	public dbAccess() {
		System.out.println("dbAccess > init");
	}
	
	/**
	 * Take a plot id number used in vegbank and a
		* filename to return all the plot data associated with that plot 
		* as an xml string 
		*
		* @param plotId -- the VegBank plotId
		*/
	public String getSingleVegBankPlotXMLString(String plotId) {
		String xmlResult = null;
		try {
			System.out.println("dbAccess > printing single plot");
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegbankOMPlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			xmlResult = writer.getSinglePlotXMLString(plotId);
		} catch (Exception e) {
			System.out.println("dbAccess > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		//System.out.println("The Plot String " + xmlResult);
		return xmlResult;
	}

	/**
	 * this method will take a plot id number used in vegbank and a
		* filename to write all the plot data associated with that plot 
		* into an xml document -- this method is much newer than the
		* org.vegbank.databaseAccess method, and should be used when the explicit 
		* desire is to write a single plot to an xml doc
		*
		* @param plotIdVec -- a vector contaning a number of the VegBank plotId's
		* @param outFile -- the fileName to which to write the data
		*/
	public boolean writeMultipleVegBankPlot(Vector plotIdVec, String outFile) {
		try {
			System.out.println("dbAccess > printing multiple plots");
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegbankOMPlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			writer.writeMultiplePlot(plotIdVec, outFile);
		} catch (Exception e) {
			System.out.println("dbAccess > Exception: " + e.getMessage());
			e.printStackTrace();
			return (false);
		}
		return (true);
	}

	/**
	 *  Take a plot id number used in vegbank and a
		* filename to return all the plot data associated with that plot 
		* into as an xml string
		*
		* @param plotIdVec -- a vector contaning a number of the VegBank plotId's
		*/
	public String getMultipleVegBankPlotXMLString(Vector plotIdVec) {
	  String xmlResult = null;
    try {
			System.out.println("dbAccess > return multiple plots");
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegbankOMPlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			xmlResult = writer.getMultiplePlotXMLString(plotIdVec);
		} catch (Exception e) {
			System.out.println("dbAccess > Exception: " + e.getMessage());
			e.printStackTrace();
			return xmlResult;
		}
		return xmlResult;
	}


	/**
	 * method to get the identification file for a collection of plots
	 * this is different than the similar summary-writer method that writes
	 * a summary of the plots, and also takes a bit longer
	 *
	 *	@param plotIdVec -- a vector contaning a number of the VegBank plotId's
	 *
	 */
	public String  getMultipleVegBankPlotIdentifcationXMLString(Vector plotIdVec)
	{
		String xmlResult = null;
		try 
		{
			System.out.println("dbAccess > printing multiple plots");
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegbankOMPlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			xmlResult = writer.getMultiplePlotIdentificationXMLString(plotIdVec);
		} catch (Exception e) {
			System.out.println("dbAccess > Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return xmlResult;
	}


	/**
	 * method to write the identification file for a collection of plots
	 * this is different than the similar summary-writer method that writes
	 * a summary of the plots, and also takes a bit longer
	 *
	 *	@param plotIdVec -- a vector contaning a number of the VegBank plotId's
	 * 	@param outFile -- the fileName to which to write the data
	 *
	 */
	public boolean writeMultipleVegBankPlotIdentifcation(
		Vector plotIdVec,
		String outFile) {
		try {
			System.out.println("dbAccess > printing multiple plots");
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegbankOMPlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			writer.writeMultiplePlotIdentifcation(plotIdVec, outFile);
		} catch (Exception e) {
			System.out.println("dbAccess > Exception: " + e.getMessage());
			e.printStackTrace();
			return (false);
		}
		return (true);
	}
}