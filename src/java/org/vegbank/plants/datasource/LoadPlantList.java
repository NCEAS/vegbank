 /*
 *	'$RCSfile: LoadPlantList.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-01 23:11:25 $'
 *	'$Revision: 1.7 $'
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
import java.sql.SQLException;
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
		if (args.length < 1) 
		{
			System.out.println(
				"Usage: java org.vegbank.datasource.LoadPlantList  [plantList] [first plantCode] [correlate]\n");
			System.exit(0);
		}
		else 
		{
			LoadPlantList pl = new LoadPlantList();
			
			Connection conn = pl.getConnection();
			
			String inputPlantList=null;
			inputPlantList=args[0];
			
			String startLoadingAfterPlantCode = null;
			if ( args.length > 1 ) 
			{
				startLoadingAfterPlantCode = args[1];
			}
			boolean startLoading = ( startLoadingAfterPlantCode == null || startLoadingAfterPlantCode.equals("") );
			
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
				
				if ( args[2] == null || ! args[2].equals("correlate") ) 
				{
					loadPlants(
						pl,
						conn,
						startLoadingAfterPlantCode,
						startLoading,
						i,
						count);
				}
				
				correlatePlants(plantList);
				
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

	private static void loadPlants(
		LoadPlantList pl,
		Connection conn,
		String startLoadingAfterPlantCode,
		boolean startLoading,
		Iterator i,
		int count)
		throws SQLException
	{
		while( i.hasNext() )
		{
			Plant plant = (Plant) i.next();
			// Hack to allow starting latter down the list 
			if ( ! startLoading )
			{
				if ( startLoadingAfterPlantCode.equals(plant.getCode()) )
				{
					startLoading = true;
					System.out.println("FOUND PLANTCODE = '" + plant.getCode() + "' about to start loading.");
					continue;
				}
				else
				{
					continue;
				}
			}
			
			if (count%1000 == 0)
			{
				// Get a new connection if 1000, 2000, etc
				 conn.close();
				 conn = pl.getConnection();
			}
			long startTime = System.currentTimeMillis();
			System.out.println( "-------------------------------------------------------------" );
			System.out.println( "Starting plant insertion ....... " + count );
			DBPlantWriter dbw = new DBPlantWriter(plant, conn);
			long stopTime = System.currentTimeMillis();
			long totalMilliSeconds =  stopTime - startTime ;
			System.out.println( "Finished took " + totalMilliSeconds +  " milliseconds  ");
			System.out.println( "-------------------------------------------------------------\n" );
			count++;
		}
	}

	private static void correlatePlants(AbstractList plantList) throws Exception
	{
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
					"undetermined");
				System.out.println( "-------------------------------------------------------------\n" );
			}
		}
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
