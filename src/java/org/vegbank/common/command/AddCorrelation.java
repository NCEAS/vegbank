/*
 *	'$RCSfile: AddCorrelation.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-11 21:24:38 $'
 *	'$Revision: 1.2 $'
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
 
package org.vegbank.common.command;

/**
 * @author farrell
 */

public class AddCorrelation
{	
		private Query query = new Query();
		
		public void execute(
			String synoymnName,
			String unacceptedName,
			String startDate,
			String stopDate,
			String convergence) throws Exception
		{
			System.out.println("AddCorrelation > " + unacceptedName + " with " + synoymnName);
			
			int statusId;
			try
			{
				//Get Primary key of concept using synonymnName
				//int conceptId = query.getPlantConceptId(synoymnName);	
				// Get PK of status using unacceptedName
				statusId = query.getPlantStatusId(unacceptedName);

				//Write into the PlantCorrelation table -- need statusId, conceptId, correlation string, dates .
				query.insertPlantCorrelation(synoymnName, statusId, convergence, startDate, stopDate );
			}
			catch (Exception e)
			{
				System.out.println("Cannot find '" + unacceptedName + "' in database. SKIPPING");
				e.printStackTrace();
			}
		}

}
