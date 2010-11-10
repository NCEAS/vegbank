/*
 * '$RCSfile: LoadingErrors.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-09-02 21:15:15 $'
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
package org.vegbank.dataload.XML;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author farrell
 *
 * A Holdall for loading errors. Used for creating reports of a loading for end users.
 */
public class LoadingErrors
{
	private static Log log = LogFactory.getLog(LoadingErrors.class);

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
	public void  addError( int TYPE, String message)
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
				log.error("LoadingErrors: Invalid Error type");
		}
	}

	public StringBuffer getHTMLReport()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<table size=\"100%\">");
		// Title
		sb.append( "<tr><td>" + getSummaryMessage(null) + "</td></tr>");

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

	public String getSummaryMessage( String identifier)
	{
		String message = null;
		String defaultIdentifier = "Dataset";

		if ( identifier != null )
			defaultIdentifier = identifier;


		// general message  -- loaded or not
		if ( hasErrors )
		{
			if ( this.validationErrors.isEmpty() && this.databaseLoadingErrors.isEmpty() )
			{
				// no problem
				if ( this.retificationErrors.isEmpty() )
				{
					// no problem at all
					message = defaultIdentifier + " Loaded into the database with no problems";
				}
				else
				{
					// Retification error
					message = defaultIdentifier + " Loaded into the database with rectification issues, this dataset will not become visible to all users until you fix these issues.";
				}
			}
			else
			{
				// There were errors
				message = defaultIdentifier + " had errors.";
			}
		}
		else
		{
			message = "No problems encountered with " + defaultIdentifier;
		}
		return message;
	}

	private String getHeader( String title)
	{
		return "<tr bgcolor=\""+ bgColor +"\"><td>" + title + "</td></tr>";
	}

	/**
	 * @param separtor separator to use between errors, e.g. "/n", &gt;br&lt;
	 * @return All the loading errors found
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
			Enumeration anenum = this.databaseLoadingErrors.elements();
			while ( anenum.hasMoreElements() )
			{
				sb.append( anenum.nextElement() +separtor);
			}
		}
		return sb;
	}

	/**
	 * @param separtor separator to use between errors, e.g. "/n", &gt;br&lt;
	 * @return All the rectification errors found
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
	 * @param separtor separator to use between errors, e.g. "/n", &gt;br&lt;
	 * @return All the validation errors found
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
			Enumeration anenum = this.validationErrors.elements();
			while ( anenum.hasMoreElements() )
			{
				sb.append( anenum.nextElement() +separtor);
			}
		}
		return sb;
	}

	/**
	 *
	 * @param identifier
	 * @return A textual report of the processing
	 */
	public String getTextReport( String identifier )
	{
		StringBuffer sb = new StringBuffer();

		sb.append("-----------------------------------------------------------------------\n");
		sb.append("REPORT:\n");
		sb.append(this.getSummaryMessage(identifier) + "\n");

		if ( ! validationErrors.isEmpty() )
		{
			sb.append("-----------------------------------------------------------------------\n");
			sb.append("\tVALIDATION\n");
			sb.append("-----------------------------------------------------------------------\n");
			sb.append(this.getValidationReport("\n"));
		}

		if ( ! retificationErrors.isEmpty() )
		{
			sb.append("-----------------------------------------------------------------------\n");
			sb.append("\tRECTIFICATION\n");
			sb.append("-----------------------------------------------------------------------\n");
			sb.append(this.getRectificationReport("\n"));
		}

		if ( ! databaseLoadingErrors.isEmpty() )
		{
			sb.append("-----------------------------------------------------------------------\n");
			sb.append("\tDATABASE LOADING\n");
			sb.append("-----------------------------------------------------------------------\n");
			sb.append(this.getLoadReport("\n"));
		}

		sb.append("-----------------------------------------------------------------------\n");
		sb.append("-----------------------------------------------------------------------");

		return sb.toString();
	}


	/**
	 * @return Did processing encounter any errors?
	 */
	public boolean hasErrors()
	{
		return hasErrors;
	}
}
