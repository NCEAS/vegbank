/**
 * class that was designed as a model for a structured Project data file, which is a 
 * file the has a very well structured header file that can be used to develop
 * a project XML file that is to be ingested into the plots database 
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-11-01 17:39:00 $'
 * 	'$Revision: 1.1 $'
 */
//package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

//import vegclient.framework.*;

public class StructuredSpeciesFile extends StructuredDataFile
{
	
	public Vector authorNameIds = new Vector();
	//this stores the line numbers that have the plot of interest
	public Vector authorPlotCodeIndex = new Vector();
	//this is the current plot code
	public String authorPlotCode = null;
	//index that stores rge  cum strata cover
	public Vector cumStrataCover = new Vector();
		
	//variables  that can be refered to to see if the variable exists in the
	// package
	public boolean authorNameIdExist = false;
	public boolean authorPlotCodesExist = false;
	public boolean cumStrataCoverExist = false;
	
	
	
	/**
	 * constructor that matches the parent constructor
	 */
	public StructuredSpeciesFile(String fileName, String plotCode)
	{
		super(fileName);
		//make the authorPlotCode a global variable
		authorPlotCode = plotCode;
		//get the list of lines that have the plot codes associated with the current
		//relevant plot
		authorPlotCodeIndex = getAuthorPlotCodeIndex();
		authorNameIds = getAuthorNameId();
		cumStrataCover = getCumStrataCover();
		//System.out.println("author Name id: "+ authorNameIds.toString() );
	}
	
	/**
	 * method to return a list of plots in this site file
	 * @return vec -- a vector that has the row numbers (as intergers) 
	 *	that have the relavant plot code
	 */
	 public Vector getAuthorPlotCodeIndex()
	 {
		 Vector vec = new Vector();
		 Vector returnVec = new Vector();
		 int colNum =  Integer.parseInt(super.dbAttributesHash.get("authorPlotCode").toString() );
		 vec = super.getColumnElements( colNum );
		 //update the varible that can be refered to to see if this attribute exists
		 if ( vec.size() > 0)
		 {
			 authorPlotCodesExist=true;
			 //now reduce the size of the vector to include only the lines with the
		 	// relavant plot code
		 	for (int i=0; i<vec.size(); i++)
		 	{
			 if ( vec.elementAt(i).toString().trim().equals(authorPlotCode))
			 {
				 //System.out.println( authorPlotCode + " "+ vec.elementAt(i).toString().trim() );
			 	 //put into the return vector the level of the plot in the species files
				 returnVec.addElement(""+i);
				 }
			 }
		
		 }
		 return(returnVec);
	 }
		
		
	 
	 
	 
	/**
	 * method to return a list of species in this file that are associated with
	 * the relevant plot
	 */
	 public Vector getAuthorNameId()
	 {
		 Vector returnVec = new Vector();
		 Vector vec = new Vector();
		 System.out.println( super.dbAttributesHash.toString());
		 
		 //get the column number that the authorNameId populate
		 int colNum = Integer.parseInt(super.dbAttributesHash.get("authorNameId").toString() );
		 
		 vec = super.getColumnElements( colNum );
		 //update the varible that can be refered to to see if this attribute exists
		 if ( vec.size() > 0)
		 {
			 authorNameIdExist=true;
			 //reduce this vector to those values that are associated with this plot
			 // only
			 for (int i=0; i<authorPlotCodeIndex.size(); i++)
		 	 {
				 int validPlotElementRow = Integer.parseInt( authorPlotCodeIndex.elementAt(i).toString() ); 
				 returnVec.addElement( vec.elementAt(validPlotElementRow) ); 
			 } 
		 }
		 System.out.println();
		 return(returnVec);
	 }
	

 
	/**
	 * method to return a list of species in this file that are associated with
	 * the relevant plot
	 */
	 public Vector getCumStrataCover()
	 {
		 Vector returnVec = new Vector();
		 Vector vec = new Vector();
		 System.out.println( super.dbAttributesHash.toString());
		 
		 //get the column number that the authorNameId populate
		 int colNum = Integer.parseInt(super.dbAttributesHash.get("cumStrataCover").toString() );
		 
		 vec = super.getColumnElements( colNum );
		 //update the varible that can be refered to to see if this attribute exists
		 if ( vec.size() > 0)
		 {
			 cumStrataCoverExist=true;
			 //reduce this vector to those values that are associated with this plot
			 // only
			 for (int i=0; i<authorPlotCodeIndex.size(); i++)
		 	 {
				 int validPlotElementRow = Integer.parseInt( authorPlotCodeIndex.elementAt(i).toString() ); 
				 returnVec.addElement( vec.elementAt(validPlotElementRow) ); 
			 } 
		 }
		 System.out.println();
		 return(returnVec);
	 }
	
	
	 
	 
	/**
   * the main routine used to test the StructureProjectFile class which 
	 * interacts with the vegclass database.
   * <p>
   * Usage: java StructureProjectFile <filename ....>
   *
   * @param filename to be proccessed
   *
   */
	 
  static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length != 1) 
			{
				System.out.println("Usage: java StructuredSpeciesFile [fileName]  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String fileName=args[0];

				StructuredSpeciesFile structuredSpeciesFile = 
				new StructuredSpeciesFile(fileName, "041-07-0577");
				System.out.println( "file type: "+structuredSpeciesFile.getFileType());
			//	System.out.println( "Version: "+structuredProjectFile.getVersion());
			//	System.out.println( "Delimiter: "+structuredProjectFile.getDelimeter() ) ;
			//	System.out.println( "Format: "+structuredProjectFile.getFormat() ) ;
				//System.out.println( structuredProjectFile.dataVector.toString() );
			//	System.out.println( "Project Name: "+structuredProjectFile.getProjectNameList().toString() );
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
}
