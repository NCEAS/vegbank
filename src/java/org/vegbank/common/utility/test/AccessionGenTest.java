/*
 * Created on Feb 16, 2004
 *
 * '$RCSfile: AccessionGenTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-16 20:19:03 $'
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
package org.vegbank.common.utility.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;

import junit.framework.TestCase;

/**
 * Unit test for AccessionGen methods
 * 
 * @author Gabriel Farrell
 * @version '$Revision: 1.2 $' '$Date: 2005-02-16 20:19:03 $'
 */
public class AccessionGenTest extends TestCase
{
	AccessionGen ag = null;
	HashMap tablesKeys = null;
	Connection conn = null; 
	
	protected void setUp()
	{
		try
		{
			//	Get Connection
			DBConnection dbConn = DBConnectionPool.getInstance().getDBConnection("Need connection for testing");
			dbConn.setAutoCommit(true);
			
			ag = new AccessionGen( dbConn, "TEST" );

			// Load some fake data for testing
			Statement statement = conn.createStatement();

			String AddProject = 
				"INSERT INTO project (stopDate,            projectName,PROJECT_ID) VALUES "
				+ "                  ('2003-01-01T00:00:00','TEST PROJECT',900000000)";
			String AddPlot =
				"INSERT INTO plot (authorplotcode,   notesMgt,notesPublic,confidentialityStatus,PLOT_ID) VALUES "
				+ "               ('TEST PLOT','false','false',     '0',                  900000000 )";
			String AddObservation1 = 
				"INSERT INTO observation (authorobscode,PROJECT_ID,PLOT_ID,OBSERVATION_ID)"
				+ "VALUES          			 ('TEST OBS1'  ,900000000,900000000,900000000)";
			String AddObservation2 = 
				"INSERT INTO observation (authorobscode,PROJECT_ID,PLOT_ID,OBSERVATION_ID)"
				+ "VALUES          			 ('TEST OBS2'  ,900000000,900000000,900000001)";
			String AddObservation3 = 
				"INSERT INTO observation (authorobscode,PROJECT_ID,PLOT_ID,OBSERVATION_ID)"
				+ "VALUES          			 ('TEST OBS3'  ,900000000,900000000,900000002)";
		
			// Load test data into db
			statement.execute(AddProject);
			statement.execute(AddPlot);
			statement.execute(AddObservation1);
			statement.execute(AddObservation2);
			statement.execute(AddObservation3);
				
			// Contruct TableKeys HashMap
			tablesKeys = new HashMap();
			Vector projectKeys = new Vector();
			projectKeys.add(new Long(900000000));
			Vector plotKeys = new Vector();
			plotKeys.add(new Long(900000000));
			Vector obsKeys = new Vector();
			obsKeys.add(new Long(900000000));
			obsKeys.add(new Long(900000001));
			obsKeys.add(new Long(900000002));
			// Deal non extant value
			obsKeys.add(new Long(999999999));
			
	 
			// Should be case insensitive
			tablesKeys.put("PrOjeCt", projectKeys );
			tablesKeys.put("pLot", plotKeys );
			tablesKeys.put("obsERVAtion", obsKeys );
			
			// Try to add garbage
			tablesKeys.put("GarBageTable", obsKeys );
			
			// Try with table without AccessionCode
			tablesKeys.put("plantname", obsKeys );			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail("Exeception thrown: '" + e.getMessage() + "'");
		}
	}
	
	public void testUpdateNewRows()
	{		
		assertNotNull(ag);
		try
		{
			ag.updateSpecificRows(  tablesKeys );

			// Now Confirm that the accession codes are as expected.
			Statement statement = conn.createStatement();
			
			String getObsAccessionCode = 
				"SELECT accessioncode FROM observation WHERE observation_id = 900000002";

			String getPlotAccessionCode = 
				"SELECT accessioncode FROM plot WHERE plot_id = 900000000";
			
			String observationAC = null;
			String plotAC = null;
			
			ResultSet rs = statement.executeQuery(getObsAccessionCode);
			while ( rs.next() )
			{
				observationAC = rs.getString(1);
			}
			
			ResultSet rs1 = statement.executeQuery(getPlotAccessionCode);
			while ( rs1.next() )
			{
				plotAC = rs1.getString(1);
			}
			
			// Compare expected with actual
			assertEquals("TEST.Ob.900000002.TESTOBS3", observationAC);
			assertEquals("TEST.Pl.900000000.TESTPLOT", plotAC);
		}
		catch (SQLException e)
		{
			fail("Exeception thrown: '" + e.getMessage() + "'");
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		Statement statement = conn.createStatement();

		String deleteObservations =
			"DELETE FROM observation WHERE plot_id >= 900000000";
		String deletePlot =
			"DELETE FROM project WHERE project_id = 900000000";
		String deleteProject = 
			"DELETE FROM plot WHERE plot_id = 900000000";

		statement.execute(deleteObservations);
		statement.execute(deletePlot);
		statement.execute(deleteProject);
		
		conn.close();
		
		super.tearDown();
	}

}
