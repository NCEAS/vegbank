 /*
 *	'$RCSfile: LoadPlantList.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-05-29 18:42:18 $'
 *	'$Revision: 1.6 $'
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
 
package org.vegbank.plants.datasource;

import java.io.FileReader;
import java.sql.Connection;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;

import org.vegbank.common.command.AddCorrelation;
import org.vegbank.common.model.*;
import org.vegbank.common.utility.Utility;
import org.vegbank.plants.datasink.*;

/**
 * @author farrell
 */

public class LoadPlantList
{

	public static void main(String[] args)
	{
		if (args.length != 1) 
		{
			System.out.println("Usage: java org.vegbank.datasource.LoadPlantList  [plantList] \n");
			System.exit(0);
		}
		else 
		{
			LoadPlantList pl = new LoadPlantList();
			Connection conn = pl.getConnection();
			
			String inputPlantList=null;
			inputPlantList=args[0];
			USDAPlantListReader plr;
			try
			{
				plr = new USDAPlantListReader(new FileReader(inputPlantList));
				AbstractList plantList = plr.getAllPlants();
				
				// Sort so the top level is loaded first 
				Collections.sort(plantList);
				Iterator i = plantList.iterator();
				
				int count = 1;
				long startAllTime = System.currentTimeMillis();
				while( i.hasNext() )
				{
					if (count%1000 == 0)
					{
						// Get a new connection if 1000, 2000, etc
						 conn.close();
						 conn = pl.getConnection();
					}
					long startTime = System.currentTimeMillis();
					System.out.println( "-------------------------------------------------------------" );
					System.out.println( "Starting plant insertion ....... " + count );
					DBPlantWriter dbw = new DBPlantWriter( (Plant) i.next(), conn);
					long stopTime = System.currentTimeMillis();
					long totalMilliSeconds =  stopTime - startTime ;
					System.out.println( "Finished took " + totalMilliSeconds +  " milliseconds  ");
					System.out.println( "-------------------------------------------------------------\n" );
					count++;
				}
				
				// Need to correlate the synoymns
				Iterator i2 = plantList.iterator();
				while( i2.hasNext() )
				{
					Plant plant = (Plant) i2.next();
					if ( ! Utility.isStringNullOrEmpty( plant.getSynonymName() ) )
					{
						System.out.println( "-------------------------------------------------------------\n" );
						// This plant needs to be correlated with an accepted plant
						AddCorrelation ac = new AddCorrelation();
						ac.execute(
							plant.getSynonymName(),
							plant.getScientificName(),
							plant.getStatusStartDate(),
							plant.getStatusStopDate(),
							"equal");
						System.out.println( "-------------------------------------------------------------\n" );
					}
				}
				
				long stopAllTime = System.currentTimeMillis();
				float totalAllMilliSeconds =  stopAllTime - startAllTime ;
				System.out.println(
					 "Finished took " + totalAllMilliSeconds/1000 +  " seconds to load " 
					 + plantList.size() + " plants"
				);
				
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		System.exit(0);
	}
	
	/**
	 * method that will return a database connection for use with the database
	 *
	 * @return conn -- an active connection
	 */
	private Connection getConnection()
	{
		System.out.println("Getting a new connection");
		Utility u = new Utility();
		Connection c = u.getConnection("vegbank", "localhost");
		return c;
	}
	
}
