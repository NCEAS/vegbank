/*
 *	'$RCSfile: HTMLUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-10 23:37:13 $'
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
 
package org.vegbank.common.utility;

import java.util.Collection;
import java.util.Iterator;

import org.vegbank.common.command.QueryReferences;
import org.vegbank.common.model.ReferenceSummary;

/**
 * @author farrell
 */

public class HTMLUtil
{
	
	public static String getReferenceLinkHTML(String id)
	{
		StringBuffer referenceLink = new StringBuffer();
		referenceLink.append("<a href='/DisplayReference?id=" + id + ">");
		referenceLink.append("'>View reference '" + id + "' </a>");
		return referenceLink.toString();
	}
	
	/**
	 * @return
	 */
	public static String getReferencesDropDown()
	{
		StringBuffer sb = new StringBuffer();

		Collection references = null;

		QueryReferences qr = new QueryReferences();
		try
		{
			references = qr.execute();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator refIt = references.iterator();

		//TODO: Expose the new Reference Contributor fields
		// Loop over all the references
		while (refIt.hasNext())
		{
			ReferenceSummary refSumm = (ReferenceSummary) refIt.next();
			sb.append(
				"<option value='"
					+ refSumm.getId()
					+ "'>"
					+ refSumm.getTitle()
					+ "</option>");
		}

		return sb.toString();
	}


}
