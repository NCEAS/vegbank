/*
 *	'$RCSfile: CommunityUsage.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-03-20 19:34:15 $'
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

/**
 * @author farrell
 */

public class CommunityUsage
{

	private String name = null;
	private String nameStatus = null;
	private String classSystem = null;	
	private String startDate = null;
	private String stopDate = null;
	

	/**
	 * @return String
	 */
	public String getClassSystem()
	{
		return classSystem;
	}

	/**
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return String
	 */
	public String getNameStatus()
	{
		return nameStatus;
	}

	/**
	 * @return String
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @return String
	 */
	public String getStopDate()
	{
		return stopDate;
	}

	/**
	 * Sets the classSystem.
	 * @param classSystem The classSystem to set
	 */
	public void setClassSystem(String classSystem)
	{
		this.classSystem = classSystem;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the nameStatus.
	 * @param nameStatus The nameStatus to set
	 */
	public void setNameStatus(String nameStatus)
	{
		this.nameStatus = nameStatus;
	}

	/**
	 * Sets the startDate.
	 * @param startDate The startDate to set
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Sets the stopDate.
	 * @param stopDate The stopDate to set
	 */
	public void setStopDate(String stopDate)
	{
		this.stopDate = stopDate;
	}

}

