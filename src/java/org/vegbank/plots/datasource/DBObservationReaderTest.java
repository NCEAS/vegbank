/*
 *	'$RCSfile: DBObservationReaderTest.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-01 23:11:24 $'
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
 
package org.vegbank.plots.datasource;

import java.util.List;

import org.vegbank.common.model.Address;
import org.vegbank.common.model.CommClass;
import org.vegbank.common.model.CoverMethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.ObservationContributor;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.ProjectContributor;
import org.vegbank.common.model.TaxonObservation;

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
		Plot plot = obs.getPLOT();
		Project project = obs.getPROJECT();
		CoverMethod coverMethod = obs.getCOVERMETHOD();
		List commClasses = obs.getOBSERVATIONCommClasss();
		CommClass commclass 	=	(CommClass) commClasses.get(0);
		List projectContributors = project.getPROJECTProjectContributors();
		Party projectContributorParty = (Party)( (ProjectContributor) projectContributors.get(0)).getPARTY();
		TaxonObservation to = (TaxonObservation) obs.getOBSERVATIONTaxonObservations().get(0);
		
		//Address projectContributorAddress = (Address) projectContributorParty.getpartyAddresss().get(0);
		
		//System.out.println( ( (ObservationContributor) obs.getOBSERVATIONObservationContributors().get(0)) + "<-----");
		
		assertEquals("VB.9", obs.getObsaccessionnumber());
		assertEquals("BADL.103", plot.getAuthorPlotCode());
		assertEquals("BADL Vegetation Mapping Project -- NATURESERVE", project.getProjectName());
		assertEquals("replace this", coverMethod.getCoverType());
		assertEquals("Fraxinus pennsylvanica - Ulmus americana / Prunus virginiana Woodland", commclass.getCommName());
		assertEquals("Drake", projectContributorParty.getSurName());
		assertEquals("AMCA6", to.getPLANTNAME().getPlantName());
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
