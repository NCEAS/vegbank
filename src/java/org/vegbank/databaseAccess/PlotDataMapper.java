/**
 * 
 *    Purpose: This class is used to transform data into an xml document that
 * 			can be loaded into the plots database.  Specifically other classes
 * 			can use this class to store a plot in a series of hashtables that 
 *		can then be passed to the PlotXmlWriter class for out put of an xml
 *		document.
 *    Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: John Harris
 *
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
package org.vegbank.databaseAccess;

import java.util.Hashtable;
import java.util.Vector;


public class PlotDataMapper 
{


	//initialize the vector to store the multitude of taxon names, starta, and covers
	// that may be passed to this method -- used in the plotElementMapper method
	Vector taxonNameElements= new Vector();
	Vector cumStrataCoverElements= new Vector();
	Vector strataTypeElements = new Vector();
	Vector coverAmountElements = new Vector();

	Vector stratumNameElements = new Vector();  //recognized strata names
	Vector stratumHeightElements = new Vector(); //recognized strata heights
	Vector stratumCoverElements = new Vector(); //recognized strata cover vals

	//project specific information
	Hashtable plotProjectParams = new Hashtable();
	//plot specific site information
	Hashtable plotSiteParams = new Hashtable();
	//plot observation specific information
	Hashtable plotObservationParams = new Hashtable();
	//species specific site information
	Hashtable plotSpeciesParams = new Hashtable();
	//community specific information
	Hashtable plotCommunityParams = new Hashtable();

	// this is the hash that contains all the elements and will be passed to 
	// the writer method
	Hashtable comprehensivePlot = new Hashtable();


	/**
	 * make constructor that will populate the hashtable that stores a series of
	 * plots with the term null so that the plotXmlWriter class that uses this
	 * hashtable does not blow up when it hits a null
	 */
	 public  PlotDataMapper()
	 {
		 //System.out.println("constructed: PlotDataMapper");
		 //fill in the plot site attributes
		 PlotElements elements = new PlotElements();
		 elements.buildElementLists();  //weired method
		 int siteElementNum = elements.elementListSize("site");
		 //System.out.println(" "+ siteElementNum);
		 
		 //fill the site hashtable
		 for (int i=0; i<siteElementNum; i++)
		 {
//			 plotSiteParams.put(elements.getSiteElement().toString(), "null");
			 //System.out.println( elements.getSiteElement() );
		 }
		 
	 }
	 

	/**
	 * This method puts data attributes into a specific hastable depending on the 
	 * database category and attribute type assignded to the element.  There are a 
	 * series of hashtables that that can be loaded in this method.  These 
	 * hashtables are consistent with the dtd used to define a plot; having 4 main 
	 * categories: 'project', 'site', 'observation', 'species' and 'community' 
	 * related data. -- maybe add a has called indicies
	 * These hash tables are used to store the plot data and then are passed to 
	 * the 'consolidator' method where they are then put into a single 
	 * 'comprehensive' hastable that can then be passed to the 'PlotXmlWriter' 
	 * class for printing to a file
	 *
	 * @param
	 * @param
	 *
	 * make comments about about the multitude of species-oriented data that may
	 * come in and how it is handled
	 *
	 */

	public void plotElementMapper (String elementValue, String elementName, 
	String elementCategory )
	{

	//store project specific data
	if ( elementCategory.equals("project") ) 
	 {
		// System.out.println(elementName+" "+elementValue);
		plotProjectParams.put(elementName,elementValue);
	// 	System.out.println(plotProjectParams.toString());
	 }


	//store site specific data
	else if ( elementCategory.equals("site") ) 
	 {
		plotSiteParams.put(elementName,elementValue);
	 }


	//store plot observational data -- which may include strata information
	else if ( elementCategory.equals("observation") ) 
	 {
		//put vectors with the strata info into the hash tables
		if (elementName.equals("stratumName"))
		{
			stratumNameElements.addElement(elementValue);
			plotObservationParams.put("stratumName",stratumNameElements);
		}
		else if (elementName.equals("stratumHeight")) 
		{
			elementValue=elementValue.replace('<','l');
			stratumHeightElements.addElement(elementValue);
//		System.out.println(elementValue);
			plotObservationParams.put("stratumHeight",stratumHeightElements);
		}
		else if (elementName.equals("stratumCover")) 
		{
			elementValue=elementValue.replace('<','l');
			stratumCoverElements.addElement(elementValue);
			plotObservationParams.put("stratumCover",stratumCoverElements);
		}
		else 
		{
			plotObservationParams.put(elementName,elementValue);
 		}
	}

	//store community related data
	else if ( elementCategory.equals("community") ) 
	 {
		plotCommunityParams.put(elementName,elementValue);
	 }

	//store species related data 
	else if ( elementCategory.equals("species") ) 
	 {
		//store  the incomming species data in a temporary vector
		//System.out.println(elementValue);
		if (elementName.equals("taxonName")) 
		{
			//System.out.println(elementValue);
			taxonNameElements.addElement(elementValue);
			plotSpeciesParams.put(elementName,taxonNameElements);
		}
			else if (elementName.equals("cumStrataCover")) 
		{
			//System.out.println(elementValue);
			cumStrataCoverElements.addElement(elementValue);
			plotSpeciesParams.put(elementName, cumStrataCoverElements);
		}
		
		else if (elementName.equals("stratum")) 
		{
			//System.out.println(elementValue);
			strataTypeElements.addElement(elementValue);
			plotSpeciesParams.put(elementName,strataTypeElements);
		}
		else if (elementName.equals("cover")) 
		{
			//System.out.println(elementValue);
			coverAmountElements.addElement(elementValue);
			plotSpeciesParams.put(elementName,coverAmountElements);
		}
		
	}
	else 
	{
		System.out.println("unrecognized veg plot category -- "
		+"PlotDataMapper.plotElementMapper: "+elementCategory);
	}
}





	/**
	 * This method will take all the elements of a single plot and put them into a
	 * single hashtbale that can be passed to the method that writes the plot
	 * information to file
	 * 
	 * @param plotSiteElements - a hashtable contining the site elements of a plot
	 * with a cardinality of one
	 *
	 */
	public void plotElementConsolidator (Hashtable plotProjectParams, 
		Hashtable plotSiteParams, Hashtable plotObservationData, Hashtable	
		plotCommunityParams, Hashtable plotSpeciesParams)
	{
		//System.out.println("test: "+plotProjectParams.toString());
		//combine the plot categories into a single hash table
		comprehensivePlot.put("siteParameters",plotSiteParams);
		comprehensivePlot.put("communityParameters",plotCommunityParams);
		comprehensivePlot.put("speciesParameters",plotSpeciesParams);
		comprehensivePlot.put("projectParameters",plotProjectParams);
		comprehensivePlot.put("observationParameters",plotObservationParams);
	}

}
