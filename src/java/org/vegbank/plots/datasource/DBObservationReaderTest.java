/*
 *	'$RCSfile: DBObservationReaderTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:13 $'
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
 
package org.vegbank.plots.datasource;

import java.util.List;

import org.vegbank.common.model.Address;
import org.vegbank.common.model.Commclass;
import org.vegbank.common.model.Covermethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Observationcontributor;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.Projectcontributor;
import org.vegbank.common.model.Taxonobservation;

import junit.framework.TestCase;

/**
 * @author farrell
 */

public class DBObservationReaderTest extends TestCase
{
	private DBObservationReader obsReader;
	
	/**
	 * Constructor for DBObservationReaderTest.
	 * @param arg0
	 */
	public DBObservationReaderTest(String arg0)
	{
		super(arg0);
	}
	

	
	public void testGetObservation()
	{
		Observation obs = null;
		try
		{
			obs = obsReader.getObservation("observation_id", 93);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Plot plot = obs.getPlot();
		Project project = obs.getProject();
		Covermethod coverMethod = obs.getCovermethod();
		List commClasses = obs.getobservationcommclasss();
		Commclass commclass 	=	(Commclass) commClasses.get(0);
		List projectContributors = project.getprojectprojectcontributors();
		Party projectContributorParty = (Party)( (Projectcontributor) projectContributors.get(0)).getParty();
		Taxonobservation to = (Taxonobservation) obs.getobservationtaxonobservations().get(0);
		
		//Address projectContributorAddress = (Address) projectContributorParty.getpartyAddresss().get(0);
		
		//System.out.println( ( (ObservationContributor) obs.getOBSERVATIONObservationContributors().get(0)) + "<-----");
		
		assertEquals("VB.9", obs.getObsaccessionnumber());
		assertEquals("BADL.103", plot.getAuthorplotcode());
		assertEquals("BADL Vegetation Mapping Project -- NATURESERVE", project.getProjectname());
		assertEquals("replace this", coverMethod.getCovertype());
		assertEquals("Fraxinus pennsylvanica - Ulmus americana / Prunus virginiana Woodland", commclass.getCommname());
		assertEquals("Drake", projectContributorParty.getSurname());
		assertEquals("AMCA6", to.getPlantname().getPlantname());
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		obsReader = new DBObservationReader();
		super.setUp();
	}

}
