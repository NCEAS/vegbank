package vegclient.framework;

/**
 * class to test the storage of a plot in an xml DOM
 * structure
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:43 $'
 * 	'$Revision: 1.1 $'
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import vegclient.framework.*;


 
public class TestPopulatePlot 
{
	public String plotFile = "VegProject.xml";
	public XMLparse parse = new XMLparse();
	
	public static void main (String args[]) 
	{
		TestPopulatePlot tpp = new TestPopulatePlot();
		try 
		{
			tpp.populatePlotSite();
		}
		catch (Exception e)
		{
			System.out.println("caught an exception");
		}
   }
	 
	 private void populatePlotSite()
	 {
		 try 
		{
			System.out.println("Parsing the project file: "+plotFile);
			//make a new instance of Project using a pre-existing project file that
			// contains a number of plots
			VegProject project = new VegProject(plotFile);
			//the number of plots that exist in this file
			System.out.println("Number of plots in project file: "+project.getNumberOfPlots() );
			//the names of the plots which exist in this project file
			System.out.println("Name of the plots:" + project.getPlotNames() );
			//get a plot from the project file that corresponds to one of the names
			// returned by the 'getPlotNames' method and 
			Node plotNode =	project.getPlot("ArtificialPlot2");
			//save this plot node to file
			//Plot plot = new Plot();
			project.savePlot(plotNode, "trash.xml");
			
			//seperate a single plot from the bunch
			System.out.println(" ");
			
			//make a new instance of a plot, using, as a basis, the plot that exists
			// in the file that was just written
			Plot plot = new Plot("trash.xml");
			System.out.println("Plot name: " + plot.getPlotName() );
			System.out.println("Parent plot name: " + plot.getParentPlotName() );
			System.out.println("Plot type: " + plot.getPlotType() );
			System.out.println("Surficial Geology: " + plot.getSurfGeo() );
			//the strata stuff
			System.out.println("Number of Strata: " + plot.getNumberOfStrata() );
			System.out.println("Names of Strata: " + plot.getStrataTypes() );
			System.out.println("Info for the 'tree' strata: " + plot.getStrata("tree"));
			//the species stuff
			System.out.println("Number of taxon observations: " + plot.getNumberOfTaxonObservations() );
			System.out.println("Name of the taxa recognized: " + plot.getObservedTaxaNames() );
			System.out.println("Details of the first taxon observation: " + plot.getTaxonObservation(1) );
			
			System.out.println("");
			
			
			
//	plot.putPlotSite("secondPlot");
//			plot.putPlotObservation("secondObservation");
		
			
			
			
			//now insert a plot into the database
			DB db = new DB("VegProject.xml");
			db.insertPlot("ArtificialPlot2");
			
		}
		catch (Exception e)
		{
			System.out.println("caught an exception");
		}
		 
	 }
	
}
