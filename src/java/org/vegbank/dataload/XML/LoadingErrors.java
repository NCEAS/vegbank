/*	
 * '$RCSfile: LoadingErrors.java,v $'
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
package org.vegbank.dataload.XML;

import java.util.Enumeration;
import java.util.Vector;

import org.vegbank.common.utility.LogUtility;


/**
 * 
 * @author farrell
 *
 * A Holdall for loading errors. Used for creating reports of a loading for end users.
 */
public class LoadingErrors
{
	private Vector retificationErrors = new Vector();
	private Vector validationErrors = new Vector();
	private Vector databaseLoadingErrors = new Vector();
	private String bgColor = "#CCCC99"; // a tan color
	
	private boolean hasErrors = false;
	
	public static final int RECTIFICATIONERROR = 0;
	public static final int VALIDATIONERROR = 1;		
	public static final int DATABASELOADINGERROR = 2;
			
	/**
	 * Add an error message of a type, the types are public availible static ints provided 
	 * by this class
	 * 
	 * @param TYPE
	 * @param message
	 */
	public void  AddError( int TYPE, String message)
	{
		hasErrors = true;
		
		switch(TYPE)
		{
			case RECTIFICATIONERROR:
				retificationErrors.add(message);
				break;
			case VALIDATIONERROR:
				validationErrors.add(message);
				break;
			case DATABASELOADINGERROR:	
				databaseLoadingErrors.add(message);
				break;
			default:
				LogUtility.log("LoadingErrors: Invalid Error type", LogUtility.ERROR);
		}
	}
	
	public StringBuffer getHTMLReport()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<table size=\"100%\">");
		// Title
		sb.append( "<tr><td>" + getSummaryMessage() + "</td></tr>");
		
		// some formating
		//sb.append();
		// The subparts
		sb.append( getHeader("Validation Results:") );
		sb.append( "<tr><td>" + this.getValidationReport("<br/>") + "</td></tr>");
		sb.append( getHeader("Retification Results:") );
		sb.append( "<tr><td>" + this.getRectificationReport("<br/>") + "</td></tr>");
		sb.append( getHeader("Database Load Results:") );
		sb.append( "<tr><td>" + this.getLoadReport("<br/>") + "</td></tr>");
		sb.append("</table>");
		return sb;
	}

	public String getSummaryMessage()
	{
		String message = null; 
		// general message  -- loaded or not
		if ( this.validationErrors.isEmpty() && this.databaseLoadingErrors.isEmpty() )
		{
			// no problem
			if ( this.retificationErrors.isEmpty() )
			{
				// no problem at all
				message = "Dataset Loaded into the database with no problems";
			}
			else
			{
				// Retification error
				message = "Dataset Loaded into the database with rectification issues, this dataset will not become visible to all users until you fix these issues.";
			}
		}
		else 
		{
			// Plot failed to load
			message = "Dataset failed to load, see errors below";
		}
		return message;
	}
	
	private String getHeader( String title)
	{
		return "<tr bgcolor=\""+ bgColor +"\"><td>" + title + "</td></tr>";
	}
	
	/**
	 * @return
	 */
	public StringBuffer getLoadReport(String separtor)
	{
		StringBuffer sb = new StringBuffer();
		if (this.databaseLoadingErrors.size() == 0)
		{
			// no errors found
			sb.append("No errors loading this dataset into the database.");
		}
		else
		{
			Enumeration databaseLoadingErrors = this.databaseLoadingErrors.elements();
			while ( databaseLoadingErrors.hasMoreElements() )
			{
				sb.append( databaseLoadingErrors.nextElement() +separtor);
			}
		}
		return sb;
	}

	/**
	 * @return
	 */
	public StringBuffer getRectificationReport(String separtor)
	{
		StringBuffer sb = new StringBuffer();
		if (this.retificationErrors.size() == 0)
		{
			// no errors found
			sb.append("No errors rectifing this dataset.");
		}
		else
		{
			Enumeration rectificationErrors = this.retificationErrors.elements();
			while ( rectificationErrors.hasMoreElements() )
			{
				sb.append( rectificationErrors.nextElement() +separtor);
			}
		}
		return sb;
	}

	/**
	 * @return
	 */
	public StringBuffer getValidationReport(String separtor)
	{
		StringBuffer sb = new StringBuffer();
		if (this.validationErrors.size() == 0)
		{
			// no errors found
			sb.append("No errors validating this dataset.");
		}
		else
		{
			Enumeration validationErrors = this.validationErrors.elements();
			while ( validationErrors.hasMoreElements() )
			{
				sb.append( validationErrors.nextElement() +separtor);
			}
		}
		return sb;
	}
}