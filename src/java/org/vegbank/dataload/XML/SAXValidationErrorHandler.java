/*	
 * '$RCSfile: SAXValidationErrorHandler.java,v $'
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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class SAXValidationErrorHandler implements ErrorHandler
{
	private LoadingErrors errors = null;
	private boolean valid = true;
	
	public SAXValidationErrorHandler(LoadingErrors errors)
	{
		this.errors = errors;
	}
	
	/**
	 * @return
	 */
	public boolean isValid()
	{
		return valid;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException arg0) throws SAXException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Error: '" + arg0.getMessage() + "'\n");
		sb.append("\tLine: " + arg0.getLineNumber() + "\n");
		sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
		errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
		valid = false;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException arg0) throws SAXException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Fatal Error: '" + arg0.getMessage() + "'\n");
		sb.append("\tLine: " + arg0.getLineNumber() + "\n");
		sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
		errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
		arg0.printStackTrace();
		valid = false;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException arg0) throws SAXException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Warning: '" + arg0.getMessage() + "'\n");
		sb.append("\tLine: " + arg0.getLineNumber() + "\n");
		sb.append("\tColumn : " + arg0.getColumnNumber() + "\n");
		errors.AddError(LoadingErrors.VALIDATIONERROR, sb.toString() );
	}
}