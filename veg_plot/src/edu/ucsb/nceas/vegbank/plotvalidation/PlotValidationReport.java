/**
 *  '$RCSfile: PlotValidationReport.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2003-01-10 21:43:12 $'
 * '$Revision: 1.2 $'
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

package edu.ucsb.nceas.vegbank.plotvalidation;
import java.io.*;
import java.lang.*;
import java.util.*;


public class PlotValidationReport
{
	// this is the buffer that contains the report
	private  StringBuffer report = new StringBuffer();
	
	// constructor
	public PlotValidationReport()
	{
		// create a new report 
		report = new StringBuffer();
		// start the root element
		this.report.append("\n<plotValidationReport>\n");
	}
	
	/**
	 * method called by the validator to set a message in the report
	 * @param dbTable -- the table that the attribute would be put into
	 * @param dbAttribute -- the attribute where the attribute should be stored
	 * @param methodName -- the name of the method that was used to look up the attribute
	 * @param methodParams -- the parameter(s) that are passed to the method
	 * @param  target -- this is the value used in the 'PlotDataSource'
	 * @param  constraints -- the vector containing the allowable element values
	 *
	 */
	public void setMessage(String dbTable, String dbAttribute, String methodName, 
	String methodParams, String target, Vector constraints)
	{
		System.out.println("PlotValidationReport > setMessage called");
		this.report.append("\n <failedValidationAttribute>\n");
		this.report.append("  <dbTable>"+dbTable+"</dbTable>\n");
		this.report.append("  <dbAttribute>"+dbAttribute+"</dbAttribute>\n");
		this.report.append("  <methodName>"+methodName+"</methodName>\n");
		this.report.append("  <methodParams>"+methodParams+"</methodParams>\n");
		this.report.append("  <failedTarget>" +target+ "</failedTarget>\n");
		this.report.append("  <constraints>\n");
		 for (int i = 0; i < constraints.size(); i++)
		 {
			 this.report.append("   <constraint>" + (String)constraints.elementAt(i) + "</constraint>\n");
		 }
		this.report.append("  </constraints> \n");
		this.report.append(" </failedValidationAttribute> \n");
	}
	
	/**
	 * method to retrieve the  xml report
	 */
	 public String getReport()
	 {
		 // end the root element 
		 	this.report.append("</plotValidationReport>\n");
		 return(report.toString() );
	 }
}