/*
 *	'$RCSfile: ReferenceSummary.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-06-30 20:08:17 $'
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
 
package org.vegbank.common.model;

import java.io.Serializable;

import org.vegbank.common.command.QueryReferences;

/**
 * @author farrell
 */

/**
 * @author farrell
 *
 * JavaBean to hold the reference summary.
 */
public class ReferenceSummary  implements Serializable
{
	private QueryReferences references;
	private String title;
	private String id;
	private String referenceType;
	private String shortname;

	public ReferenceSummary(QueryReferences references)
	{
		this.references = references;
	}
	
	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param key
	 */
	public void setId(String key)
	{
		id = key;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string)
	{
		title = string;
	}

	/**
	 * @return
	 */
	public String getReferenceType()
	{
		return referenceType;
	}

	/**
	 * @return
	 */
	public String getShortname()
	{
		return shortname;
	}

	/**
	 * @param string
	 */
	public void setReferenceType(String string)
	{
		referenceType = string;
	}

	/**
	 * @param string
	 */
	public void setShortname(String string)
	{
		shortname = string;
	}

}