/**
 *  '$RCSfile: PlotValidator.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2003-01-08 16:37:39 $'
 * '$Revision: 1.4 $'
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

package edu.ucsb.nceas.vegbank.plotvalidation;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;



import PlotDataSource;
import edu.ucsb.nceas.vegbank.plotvalidation.PlotValidationReport;
import edu.ucsb.nceas.vegbank.plotvalidation.ValidationConstraint;
import edu.ucsb.nceas.vegbank.plotvalidation.ConstraintMap;
import edu.ucsb.nceas.vegbank.plotvalidation.PlotValidationException;


public class PlotValidator implements PlotValidationInterface
{
	PlotDataSource source;
	PlotValidationReport report;
	ValidationConstraint constraint;
	// this is the vector that contains the map between the xml and the 
	// methods in the 'PlotDataSource'
	private Vector constraintSourceMethodMap;
	
	// this attribute should rplace all the code that creates the map(s) within 
	// this class
	private ConstraintMap constraintMap;
	
	private static ResourceBundle validatorprops = ResourceBundle.getBundle("plotvalidation");
	private String constraintListFile;
	private String constraintMapFile;
	
	
	/**
	  * constructor method that is called for each plot that is to be 
	  * validated
	  *
	  * @param insource -- the PlotDataSource object
	  */
	  public  PlotValidator(PlotDataSource insource)
	  {
			try
			{
				System.out.println("PlotValidator > constructor called " );
				// get the properties from the prop file
				this.getProperties();
				//create a new instance of the PlotValidationReport
				report = new PlotValidationReport();
				//create an instance of the classes that store the xml data 
				constraint = new ValidationConstraint(constraintListFile);
				constraintMap = new ConstraintMap(constraintMapFile);
				
				
				// pass the reference 'PlotDataSource' input to the instance variable
				this.source = insource;
				// build and populate the map of the xml elements and the methods in the 
				// 'PlotDataSource'
				this.constraintSourceMethodMap = this.getXMLPlotDataSourceMap();
				//System.out.println("############### " + constraintSourceMethodMap.toString() );
			
			}
			catch (Exception e)
			{
					e.printStackTrace();
			}
		}
		
		/**
		  * this is a utility function that  reads the properties file and updates
			* the instance variables with their corresponding values 
			*/
			private void getProperties() 
			{
					try
					{
						//Enumeration keys = validatorprops.getKeys();
						constraintListFile = validatorprops.getString("constraintlist");
						constraintMapFile = validatorprops.getString("constraintmap");
						System.out.println("PlotValidator > constraint map: " + constraintMapFile);
						System.out.println("PlotValidator > constraint file: " + constraintListFile );
					}
					catch (Exception e)
					{
						throw new PlotValidationException(" error geting properties from properties file ", e );
					}
			}
	  
	  /**
	    * this is the method that is called by the database insertion 
	    * code to get whether the plot is valid or not
	    * @param plot -- the name of the plot in the plotdatasource
			* @return boolean -- true if the plot exists 
			*/
	  public boolean isPlotValid(String plot)
	  {
			boolean validFlag = false;
			System.out.println("PlotValidator > isPlotValid() called for: " + plot );
			
			
			// the number of rules to use to test the plot with 
			int constraintNumber = constraintMap.getMapSize();
			System.out.println("PlotValidator > number of constraints to map: " + constraintNumber);
			for (int i = 0; i < constraintMap.getMapSize() ; i++) 
			{
					String methodName = constraintMap.getMethodName(i);
					String methodParams = constraintMap.getMethodParams(i);
					String dbTable = constraintMap.getDBTable(i);
					String dbAttribute = constraintMap.getDBAttribute(i);
					
					// get the attribute from the plot data soure
					String val = this.getPlotDataSourceAttributeValue(methodName, plot);
					System.out.println("PlotValidator >: " + methodName +" val: " + val);
					
					//get the constaining vocabulary -- basically a list of the valid terms
					Vector constraints = constraint.getConstraints(dbTable, dbAttribute);
					//get the value from the data source 

					validFlag = this.isAttributeValid(val, constraints);
					// if this is false then the whole plot is false and a message should be 
					// reported to the PlotValidationReport class
					if (validFlag == false)
					{
						// this will log the messages
						report.setMessage(dbTable, dbTable, methodName, methodParams, val, constraints);
					}
			}
			
			
			
			
			return(validFlag);
	  }
	  
		
		/*
		 * this return the attribute value from the 'PlotDataSource' based on the 
		 * input methodName and the method input parameters 
		 * 
		 */
		 private String getPlotDataSourceAttributeValue(String method, String parameters)
		 {
			 	String result = null;
				//Class c = String.class;
				Class c = source.getClass();  
				Class[] parameterTypes = new Class[] {String.class};
				Method targetMethod;
				Object[] arguments = new Object[] {parameters};
				try {
					targetMethod = c.getMethod( method, parameterTypes);
					System.out.println("PlotValidator > mName: " + targetMethod.getName() );
					result = (String) targetMethod.invoke(source, arguments);
				} catch (NoSuchMethodException e) {
          System.out.println(e);
				} catch (IllegalAccessException e) {
          System.out.println(e);
				} catch (InvocationTargetException e) {
          System.out.println(e);
				}
      return result;
		 }
	  
		
		
		
		/* 
		 * method that creates the mapping between the constraint xml element names
		 * and the getMethod name
		 * 
		 * this method is in test stage and will eventually be populated via a properties file
		 * and there will be a class that represents the data structure that the output from 
		 * this method corresponds to.
		 */
		 private Vector getXMLPlotDataSourceMap()
		 {
			 Vector map = new Vector();
			 //this is the single map instance 
			 Hashtable mapInstance = new Hashtable();
			 Vector mapVals = new Vector();
			 // this hashtable stores the method paremetres
			 Hashtable method = new Hashtable();
			 method.put("methodName", "getRepresentativeness");
			 method.put("methodParameters", "plotName");
			 Hashtable database = new Hashtable();
			 database.put("dbTable", "observation");
			 database.put("dbAttribute", "representativeness"); 
			 mapVals.addElement(method);
			 mapVals.addElement(database);
			 mapInstance.put("representativeness", mapVals);
			 //put the methods into the map vector
			 map.addElement(mapInstance);
			 
			 mapInstance = new Hashtable();
			 mapVals = new Vector();
			 // this hashtable stores the method paremetres
			 method = new Hashtable();
			 method.put("methodName", "getSurfGeo");
			 method.put("methodParameters", "plotName");
			 database = new Hashtable();
			 database.put("dbTable", "plot");
			 database.put("dbAttribute", "geology");
			 mapVals.addElement(method);
			 mapVals.addElement(database);
			 mapInstance.put("geology", mapVals);
			 //put the methods into the map vector
			 map.addElement(mapInstance);
			 //
			 return(map);
		 }
		 
		 
		 
		
		/*
		 * this method will query all the attribute values from the 'PlotDataSource' 
		 * @deprecated -- this method should go away
		 * 
		 */
		 private void getPlotDataSourceAttributeValues()
		 {
			 String attributeName = "Representativeness";
			 String attributeValue;
			 // Name of class that holds the get methods 
			 Class c = PlotDataSource.class;   // Make class member ??
			 // create an instance of this class
			 //Object obj = c.newInstance();
			 //Class cParams = c.newInstance();

			 // The array of parametertypes and method name are required to find method
			 Method getValue;
			 String methodName = "get" + attributeName;
			 //Class[] parameterTypes = new Class[] {String.class};
			 
			 Method[] theMethods = c.getMethods();
			 
    	try
			{
				for (int i = 0; i < theMethods.length; i++) 
				{
         String methodString = theMethods[i].getName();
         //System.out.println("Name: " + methodString);
         String returnString = theMethods[i].getReturnType().getName();
         //System.out.println("   Return Type: " + returnString );
         Class[] parameterTypes = theMethods[i].getParameterTypes();
         //System.out.print("   Parameter Types:");
         for (int k = 0; k < parameterTypes.length; k ++) 
				 {
            String parameterString = parameterTypes[k].getName();
           // System.out.print(" " + parameterString);
         }
         //System.out.println();
      }
				//getValue = c.getMethod(methodName, parameterTypes );
				// Need to get parameters for the method
				//attributeValue = (String)getValue.invoke(obj, obj);
			}
			catch (Exception e)
			{
					e.printStackTrace();
			}
//			catch (NoSuchMethodException e) 
//			{
//				System.out.println(e);
//			}
//			catch (IllegalAccessException e) 
//			{
//				System.out.println(e);
//			}
//			catch (InvocationTargetException e) 
//			{
//				System.out.println(e);
//			}

			//return attributeValue;

		 }
	  
	  /**
	   *  method that does the comparison between the attribute value and
	   *  the constrainingVocaulary
	   *
	   */
	   private boolean isAttributeValid(String target, Vector constrainingVocab)
	   {
		boolean validFlag = false;
		//do the comparison -- if there are any matches then set the flag to positive
		System.out.println("PlotValidator > isAttributeValid() "+target+ "  " + constrainingVocab.toString() );
		 for (int i = 0; i < constrainingVocab.size(); i++)
		 {
			 String con = (String)constrainingVocab.elementAt(i);
			 if ( target.equalsIgnoreCase(con) )
			 {
				 validFlag = true;
			 }
		 }
		 return(validFlag);
	   }
	  
	  
	    
	   /**
	    * this is the method that is called by the database insertion 
	    * code to get the report 
	    */
	  public String getValidationReport()
	  {
		return(report.getReport());
	  }
	  
	  
	
}
