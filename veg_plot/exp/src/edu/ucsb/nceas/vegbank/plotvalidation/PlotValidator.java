/**
 *  '$RCSfile: PlotValidator.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-12-13 22:25:15 $'
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

import PlotDataSource;
import edu.ucsb.nceas.vegbank.plotvalidation.PlotValidationReport;
import edu.ucsb.nceas.vegbank.plotvalidation.ValidationConstraint;


public class PlotValidator implements PlotValidationInterface
{
	PlotDataSource source;
	PlotValidationReport report;
	ValidationConstraint constraint;
	
	/**
	  * constructor method that is called for each plot that is to be 
	  * validated
	  *
	  * @param insource -- the PlotDataSource object
	  */
	  public  PlotValidator(PlotDataSource insource)
	  {
		  System.out.println("PlotValidator > constructor called " );
		  //create a new instance of the PlotValidationReport
		  report = new PlotValidationReport();
		  //create an instance of the constaint class
		  constraint = new ValidationConstraint();
		  this.source = insource;
	  }
	  
	  /**
	    * this is the method that is called by the database insertion 
	    * code to get whether the plot is valid or not
	    */
	  public boolean isPlotValid(String plot)
	  {
		boolean validFlag = false;
		System.out.println("PlotValidator > isPlotValid() called for: " + plot );
		
		//do the logic here -- as a test just do the geology
		String table = "observation";
		String attribute = "representativeness";
		
		//get the constaining vocabulary
		Vector constraints = constraint.getConstraints(table, attribute);
		//get the value from the data source 
		String target = this.source.getRepresentativeness(plot);
		
		validFlag = this.isAttributeValid(target, constraints);
		// if this is false then the whole plot is false and a message should be 
		// reported to the PlotValidationReport class
		if (validFlag == false)
		{
			// this will log the messages
			report.setMessage(target, constraints);
		}
		
		return(validFlag);
	  }
	  
	  
	  
	  /**
	   *  method that does the comparison between the attribute value and
	   *  the constrainingVocaulary
	   *
	   */
	   private boolean isAttributeValid(String target, Vector constrainingVocab)
	   {
		boolean validFlag = false;
		//do the comparison -- if there are any matches then set the flag to positive
		System.out.println("PlotValidator > isAttributeValid() "+target+ "  " + constrainingVocab.toString() );
		 for (int i = 0; i < constrainingVocab.size(); i++)
		 {
			 String con = (String)constrainingVocab.elementAt(i);
			 if ( target.equalsIgnoreCase(con) )
			 {
				 validFlag = true;
			 }
		 }
		 return(validFlag);
	   }
	  
	  
	    
	   /**
	    * this is the method that is called by the database insertion 
	    * code to get the report 
	    */
	  public String getValidationReport()
	  {
		return(report.getReport());
	  }
	  
	  
	
}