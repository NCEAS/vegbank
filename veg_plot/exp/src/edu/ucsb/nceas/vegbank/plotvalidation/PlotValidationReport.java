/**
 *  '$RCSfile: PlotValidationReport.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-12-13 17:59:08 $'
 * '$Revision: 1.1 $'
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
	private static StringBuffer report = new StringBuffer();
	
	
	/**
	 * method called by the validator to set a message in the report
	 */
	public void setMessage(String target, Vector constraints)
	{
		System.out.println("PlotValidationReport > setMessage called");
		this.report.append("\n");
		this.report.append("<failedTarget>" +target+ "</failedTarget>\n");
		this.report.append("<constraints>\n");
		 for (int i = 0; i < constraints.size(); i++)
		 {
			 this.report.append("<constraint>" + (String)constraints.elementAt(i) + "</constraint>\n");
		 }
		this.report.append("</constraints> \n");
	}
	
	/**
	 * method to retrieve the report
	 */
	 public String getReport()
	 {
		 return(report.toString() );
	 }
}