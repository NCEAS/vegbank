/**
 *  '$RCSfile: ConstraintMap.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-07-15 20:19:27 $'
 * '$Revision: 1.5 $'
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
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.vegbank.xmlresource.*;



/**
 * this class is a data structure class that represents the 
 * mapping between the constaint items in the constraing xml
 * and the 'get' functions in the 'PlotDataSource' class
 * 
 * the data structure is below:
 *
 *                       attributename
 *                     /  (String)
 *                   /                     methodName
 * map - mapInstance                     /  (String)
 * (Vec)     (Hash)   \            method 
 *                      \        / (Hash) \ methodParameters
 *                       mapVals              (String)
 *                        (Vec)  \          
 *                          |      \
 *                          |        database - - dbTable
 *                          |        (Hash)  \    (String)
 *                          |                  \ 
 *                          |                     dbAttribute
 *                          |                       (String)
 *                          |_____ rule
 *                                (String)
 *  
 * THIS IS THE MATCHING XML DOCUMENT 
 * <constraintMap>
 *  <mapInstance>
 *   <attributeName>geology</attributeName>
 *   <method>
 *    <methodName>getSurfGeo</methodName>
 *    <methodParams>plotname</methodParams>
 *   </method>
 *   <database>
 *    <dbTable>plot</dbTable>
 *    <dbAttribute>geology</dbAttribute>
 *   </database>
 *   <rule>exact</rule>
 *  </mapInstance>
 * </constraintMap>
 *
 *
 **/                               

 
public class ConstraintMap implements ConstraintMapInterface
{
	
  private XMLparse parser;
	private Document doc;
	//private String constraintMapDocument = "constraint-map.xml";

  protected Vector attributeNameVec;
  protected Vector map;
  protected Hashtable mapInstance;
  protected String attributeName;
  protected Vector mapValues;
  protected Hashtable method;
  protected String methodName;
  protected String methodParams;
  protected Hashtable database;
  protected String dbTable;
  protected String dbAttribute;
  protected String rule;
  
	/**
	 * when this class is constructed it populates a data 
   * structure that is used to store the info related to 
   * mapping the constraing items to the functions stored 
   * in the 'PlotDataSource' class
	 */
	 public ConstraintMap( String constraintMapDocument)
	 {
		 try
		 {
			 System.out.println("ConstraintMap > init");
       //init the inst variables
       map = new Vector();
       mapInstance = new Hashtable();
       mapValues = new Vector();
       method = new Hashtable();
       database = new Hashtable();
			 
			 // check that the file exists
			 File check = new File(constraintMapDocument);
			 if ( check.exists() == false)
			 {
				 throw new PlotValidationException("constraint map not found: " + constraintMapDocument);
			 }
			
			 
			 parser = new XMLparse();
			 doc = parser.getDocument(constraintMapDocument);
			 
       this.attributeNameVec  = parser.getValuesForPath(doc, "/constraintMap/mapInstance/attributeName");
			 System.out.println("ConstraintMap > attributeName: " + attributeNameVec.toString() ); 
       for (int i=0;i<attributeNameVec.size();i++) 
       {
        Node n = parser.get(doc, "mapInstance", i);
        Document mapinstance = parser.createDocFromNode(n, "mapInstance");
        
        // get all the attributes and construct the 'map' variable
        Vector v = parser.getValuesForPath(mapinstance, "//*/*/methodName");
        this.methodName = (String)v.elementAt(0);
        v = parser.getValuesForPath(mapinstance, "//*/*/methodParams");
        this.methodParams = (String)v.elementAt(0);
        v = parser.getValuesForPath(mapinstance, "//*/*/dbTable");
        this.dbTable = (String)v.elementAt(0);
        v = parser.getValuesForPath(mapinstance, "//*/*/dbAttribute");
        this.dbAttribute = (String)v.elementAt(0);
        v = parser.getValuesForPath(mapinstance, "//*/rule");
        this.rule = (String)v.elementAt(0);
        this.attributeName = (String)attributeNameVec.elementAt(i);
       
       // now add this instance to the 'map'
       this.addElementsToMap(attributeName, methodName, methodParams, dbTable,
       dbAttribute, rule);
        
       }
			 
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
   /**
    * method that returns the size of the constraint map -- basically 
    * the number of elements that are to be constrained 
    */
     public int getMapSize()
     {
       return(map.size() );
     }
     
     
    /**
     * method that returns the rule that governs the way the attribute 
     * is to be constrained by the constraint list
     * @param mapLevel -- the level in the map corresponding to the attribute 
     * of interest.
     */
     public String getConstraintRule(int mapLevel)
     {
      //place holder
      return("exact");
     }




   /**
    * method that adds each of the mapInstance elements to the map object
    * This method is called by the constructor for creating the data structure 
    * with the elements obtained from the xml document
    * @param attributeName
    * @param methodName
    * @param methodParams
    * @param dbTable
    * @param dbAttribute
    * @param rule
    */
    private void addElementsToMap(String attributeName, String methodName, 
    String methodParams, String dbTable, String dbAttribute, String rule)
    {
      try
      {
      /*
        System.out.println("attributeName: " +attributeName );
        System.out.println("methodName: " +  methodName);
        System.out.println("methodParams: " +  methodParams);
        System.out.println("dbTable: " + dbTable );
        System.out.println("dbAttribute: " +  dbAttribute);
        System.out.println("rule: " + rule );
      */
        method = new Hashtable();
        database = new Hashtable();
        mapValues = new Vector();
        mapInstance = new Hashtable();
        this.method.put("methodName", methodName);
        this.method.put("methodParams", methodParams);
        this.mapValues.addElement(method);
        this.database.put("dbTable", dbTable);
        this.database.put("dbAttribute", dbAttribute);
        this.mapValues.addElement(database);
        this.mapInstance.put(attributeName, mapValues);
        this.map.addElement(mapInstance);
		  }
      catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
    }

   /**
    * this method is to be called to get the methodParams using the integer 
    * number that corresponds to the level in the 'map' vector
    * @param mapLevel -- the level in the map corresponding to the attribute 
    * of interest.
    **/
    public String getDBAttribute(int mapLevel)
    {
      String rval = "";
      try
      {
        mapInstance = (Hashtable)map.elementAt(mapLevel);
				for (Enumeration e = mapInstance.keys() ; e.hasMoreElements() ;) 
				{
				  String cName = (String)e.nextElement();
				  Vector mapVals = (Vector)mapInstance.get(cName);
				  database = (Hashtable)mapVals.elementAt( 1 );
				  dbAttribute = (String)database.get("dbAttribute");
          rval = dbAttribute;
         }
      }
      catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
      return(rval);
    }
    
   /**
    * this method is to be called to get the methodParams using the integer 
    * number that corresponds to the level in the 'map' vector
    * @param mapLevel -- the level in the map corresponding to the attribute 
    * of interest.
    **/
    public String getDBTable(int mapLevel)
    {
      String rval = "";
      try
      {
        mapInstance = (Hashtable)map.elementAt(mapLevel);
				for (Enumeration e = mapInstance.keys() ; e.hasMoreElements() ;) 
				{
				  String cName = (String)e.nextElement();
				  Vector mapVals = (Vector)mapInstance.get(cName);
				  database = (Hashtable)mapVals.elementAt( 1 );
				  dbTable = (String)database.get("dbTable");
          rval = dbTable;
         }

      }
      catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
      return(rval);
    }
    
   /**
    * this method is to be called to get the methodParams using the integer 
    * number that corresponds to the level in the 'map' vector
    * @param mapLevel -- the level in the map corresponding to the attribute 
    * of interest.
    */
    public String getMethodParams(int mapLevel)
    {
      String rval = "";
      try
      {
        mapInstance = (Hashtable)map.elementAt(mapLevel);
				for (Enumeration e = mapInstance.keys() ; e.hasMoreElements() ;) 
				{
				  String cName = (String)e.nextElement();
				  Vector mapVals = (Vector)mapInstance.get(cName);
				  method = (Hashtable)mapVals.elementAt( 0 );
				  methodParams = (String)method.get("methodParams");
          rval = methodParams;
         }

      }
      catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
      return(rval);
    }
    
   /**
    * this method is to be called to get the methodName using the integer 
    * number that corresponds to the level in the 'map' vector
    * @param mapLevel -- the level in the map corresponding to the attribute 
    * of interest.
    **/
    public String getMethodName(int mapLevel)
    {
      String rval = "";
      try
      {
        mapInstance = (Hashtable)map.elementAt(mapLevel);
				for (Enumeration e = mapInstance.keys() ; e.hasMoreElements() ;) 
				{
				  String cName = (String)e.nextElement();
				
				  Vector mapVals = (Vector)mapInstance.get(cName);
				  method = (Hashtable)mapVals.elementAt( 0 );
				  methodName = (String)method.get("methodName");
          rval = methodName;
         }
      }
      catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
      return(rval);
    }
    

  /**
   * method for testing the mapping code
   */
   public static void main(String[] args)
   {
      ConstraintMap map = new ConstraintMap("constraint-map.xml"); 
      System.out.println("ConstraintMap > map size: "+ map.getMapSize() );
      System.out.println("ConstraintMap > rule: "+ map.getConstraintRule(0) );
      System.out.println("ConstraintMap > method name element 1: " + map.getMethodName(0) );
      System.out.println("ConstraintMap > method params element 1: " + map.getMethodParams(0) );
      System.out.println("ConstraintMap > table element 1: " + map.getDBTable(0) );
      System.out.println("ConstraintMap > attribute element 1: " + map.getDBAttribute(0) );
   }
}
