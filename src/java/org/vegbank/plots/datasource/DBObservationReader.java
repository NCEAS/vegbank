/*
 *	'$RCSfile: DBObservationReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-10-24 19:26:40 $'
 *	'$Revision: 1.5 $'
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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plantname;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.Projectcontributor;
import org.vegbank.common.model.Stratum;
import org.vegbank.common.model.Taxonobservation;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Reads an Observation from the Vegbank Database and populates
 * a tree of beans with the data. The root of the tree is observation.
 * 
 * @author farrell
 */

public class DBObservationReader
{

	/**
	 * Uses to run SQL statements agains db
	 */
	//private DatabaseAccess da;
	private DBConnection con = null;
	private Vector ignoreObjects = new Vector();
	
	public DBObservationReader() throws SQLException
	{
		//da = new DatabaseAccess();
		con = DBConnectionPool.getDBConnection("Needed for reading observation");
	}
	
	public Observation getObservation(String key, int keyValue) throws Exception
	{
		Observation obs = new Observation();
		getObjectFromDB(obs, key, keyValue);
		getRelatedObjectsFromDB(key, keyValue, obs, ignoreObjects);
		
		// Need to fill out some more Objects 
		// Project 
		Project project = obs.getProjectobject();
		ignoreObjects.add("Observation");
		getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Project"), project.getProject_id(), project, ignoreObjects );
		
		// ProjectContributors
		Iterator projectContributors =project.getproject_projectcontributors().iterator();
		while ( projectContributors.hasNext() )
		{
			Projectcontributor projectContributor = (Projectcontributor)  projectContributors.next();
			Party party = (Party) projectContributor.getPartyobject();
			//System.out.println("###########################################################");
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Party"), party.getParty_id(), party, ignoreObjects );
			//System.out.println("###########################################################");
		}
		
		// TaxonObservations
		Iterator taxonObservations = obs.getobservation_taxonobservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			Taxonobservation taxonObservation = (Taxonobservation)  taxonObservations.next();
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("TaxonObservation"), taxonObservation.getTaxonobservation_id(), taxonObservation, ignoreObjects );
			
			Plantname plantName = taxonObservation.getPlantnameobject();
//			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("PlantName"), plantName.getPLANTNAME_ID(), plantName, ignoreObjects );
//			Iterator plantNames = plantName.getPLANTNAMEPlantUsages().iterator();
//			while ( plantNames.hasNext())
//			{
//				PlantUsage pu = (PlantUsage) plantNames.next();
//				PlantConcept pc = pu.getPLANTCONCEPT();
//				ignoreObjects.add("PlantConcept");
//				getRelatedObjectsFromDB(VBObjectUtils.getKeyName("PlantConcept"), pc.getPLANTCONCEPT_ID(), pc, ignoreObjects );	
//				ignoreObjects.remove("PlantConcept");
//			}
		}
		
		// Strata
		Iterator strata = obs.getobservation_stratums().iterator();
		while ( strata.hasNext())
		{
			Stratum stratum = (Stratum) strata.next();
			System.out.println("Ignore when searching through Stratum " + ignoreObjects);
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Stratum"), stratum.getStratum_id(), stratum, ignoreObjects );

		}
		
		// Plot
		Plot plot = obs.getPlotobject();
		System.out.println("Ignore when searching through Plot " + ignoreObjects);
		getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Plot"), plot.getPlot_id(), plot, ignoreObjects );
		
		return obs;
	}

	private void getRelatedObjectsFromDB(String key, int keyValue, Object object, Vector ignoreList)
	{
		// Get the one to many relationships
		Collection ListMethods = VBObjectUtils.getSetMethods(object, "java.util.List");
		//System.out.println("##### " + ListMethods);
		Iterator iter = ListMethods.iterator();
		
		OUTERLOOP: while ( iter.hasNext() )
		{
			Method method = (Method) iter.next();
			// Get the tableName
			String tableName = getTableName(object, method);	
			// Filter out tables called
			Iterator iterator = ignoreList.iterator();
			while ( iterator.hasNext()  )
			{
				if ( tableName.equalsIgnoreCase( (String) iterator.next() ) )
				{
					// Stop processing this one
					continue OUTERLOOP;
				}
			}
			
			try
			{
				//System.out.println("getObjectsFromDataBase(" + tableName + " , " + key + " , " + keyValue + ")");
				List objectsToAdd =  getObjectsFromDataBase(tableName, key, keyValue);
				Object[] parameters = {objectsToAdd};
				method.invoke(object, parameters);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handle the messy logic for getting the table name from the methodName
	 * e.g.
	 * 	setparty_addresss ==> Address
	 * 	setorganization_addresss ==> Address
	 * @param object
	 * @param method
	 * @return
	 */
	private String getTableName(Object object, Method method)
	{
		String tableName = VBObjectUtils.getFieldName( method.getName(), null );
		
		// Reconstitute the name of the table from the mess that is the methodName
		tableName = tableName.substring(0, tableName.length()-1); // remove the trailing 's'
		
		// Remove text before '_'
		tableName = 
			tableName.substring( tableName.indexOf('_')+1, tableName.length() );
		
		// Reconstitute the name of the table from the mess that is the methodName
		
		//System.out.println(VBObjectUtils.getUnQualifiedName( object.getClass().getName().toLowerCase()));
		//tableName = StringUtils.replaceOnce(tableName,VBObjectUtils.getUnQualifiedName( object.getClass().getName()).toLowerCase(), "");
		
		return tableName;
	}
	
	private List getObjectsFromDataBase(String table, String FKName, int FKValue) throws Exception
	{
		Vector retrivedObjects = new Vector();
		
		FKName = this.getFKNameInTable(FKName, table);
		
		// Get all the PK associated with this object
		Vector keys = new Vector();
		
		String PKName = table + "_ID";
		String SQLQuery = 
			"SELECT " + PKName + " FROM " + table + " WHERE " + FKName + " = " + FKValue;
		
		Statement selectStatement = con.createStatement();
		ResultSet rs = selectStatement.executeQuery(SQLQuery);
		
		while ( rs.next() )
		{
			keys.add( new Integer ( rs.getInt(1) ) );	
		}
		
		Iterator keysIterator = keys.iterator();
		while ( keysIterator.hasNext() )
		{
			int key = ( (Integer) keysIterator.next() ).intValue();
			Object newObject = Utility.createObject( VBObjectUtils.getFullyQualifiedName(table) );	
			this.getObjectFromDB(newObject, PKName, key);
			retrivedObjects.add(newObject);
		}
		//System.out.println("##### " + retrivedObjects);
		return retrivedObjects;
	}

	/**
	 * This is a complete hack to get around some errors I came accross.
	 * Sometimes the ForiegnKey Name is different from the Primary Key name
	 * in the table it refers to, this fixes this on a case by case basis.
	 * 
	 * @param FKName
	 * @param table
	 * @return
	 */
	private String getFKNameInTable(String FKName, String tableName)
	{
		if ( tableName.equalsIgnoreCase("observationsynonym") && FKName.equalsIgnoreCase("observation_id") )
		{
			FKName = "primaryobservation_id";
		}
		return FKName;
	}

	private void getObjectFromDB(Object object, String PKName, int PKValue)
	{
		// Place to store name values for populating object
		HashMap hmap = new HashMap();
		HashMap objectSetMethods = new HashMap();
		
		Method[] methods = object.getClass().getMethods();
		String className = VBObjectUtils.getUnQualifiedName(object.getClass().getName());
		//System.out.println(">>>> " + className + "," + object+ "," + object.getClass().getName());
		
		Vector fieldNames = new Vector();
		
		for (int i=0; i < methods.length ; i++ )
		{
			// Process method
			Method method = methods[i];
			String methodName = method.getName();
			String fieldName = VBObjectUtils.getFieldName(methodName, null);
			Class[] parameterTypes = method.getParameterTypes();
			
			if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
			{					
				// get the name of the field 
				fieldNames.add(fieldName);
			}
			else if ( VBObjectUtils.isGetMethod(method, "int") )
			{
				fieldNames.add(fieldName);
			}
			// Is this a set method for another vegbankObject
			else if (
				VBObjectUtils.isSetMethod(method)
					&& VBObjectUtils.existsInVegbankObjectModel(parameterTypes[0].getName() ))
			{
				// Store these for latter use fieldName
				Vector methodAndType = new Vector();
				methodAndType.add( parameterTypes[0].getName() );
				methodAndType.add( method );
				objectSetMethods.put( VBObjectUtils.getKeyName(fieldName), methodAndType);
				//System.out.println("Processing: " + methodAndType );
			}
			else
			{
				//System.out.println("Not processing: " + method.toString() );
			}
		}
		
		String sqlStatement = this.getSQLSelectStatement(fieldNames, className, PKName, PKValue);
		
		try
		{
			Statement selectStatement = con.createStatement();
			ResultSet rs = selectStatement.executeQuery(sqlStatement);
			int columnNumber = rs.getMetaData().getColumnCount();
			
			// Is there an row?
			if ( rs.next() )
			{
				ResultSetMetaData metaData = rs.getMetaData();
				
				// For each value add to the HashMap
				for ( int ii=0; ii < columnNumber; ii++ )
				{
					hmap.put(fieldNames.elementAt(ii), this.getColumnValue(rs, ii+1, metaData ));	
				}
				
				Iterator iter = objectSetMethods.keySet().iterator();
				while ( iter.hasNext() )
				{
					// Dereference data structure
					String fieldName =  (String) iter.next();
					Vector methodAndType = (Vector) objectSetMethods.get(fieldName);
					String fullyQualifiedClassName = (String) methodAndType.elementAt(0);
					Method method = (Method) methodAndType.elementAt(1);	
					
					// Need to get the corresponding PK name and value
					String value = (String) hmap.get(fieldName);
							
					// Need to create an object for each Set Object Method
					Object newObject = Utility.createObject(fullyQualifiedClassName);
					try
					{
						//	Fill out this object
						this.getObjectFromDB(newObject, fieldName, new Integer(value).intValue() );
						Object[] parameters =  {newObject} ;
						//	Add it using the set method
						method.invoke(object, parameters);
					}
					catch (java.lang.NumberFormatException e1)
					{
						// Not a real integer, assumming its a null.
						if (value != null)
						{
							System.out.println("Not an Integer: " + value);
							e1.printStackTrace();
						}
					}
				}
	
				
				Iterator it = hmap.keySet().iterator();
				while ( it.hasNext() )
				{
					String name = (String) it.next();
					String value = (String) hmap.get(name);
					//System.out.println("DBObservationReader > name: '" + name + "' value: '" + value + "'" );
					
					// Populate Object with value
					String property = getPropertyName(name);
					BeanUtils.copyProperty(object, property, value);
					//System.out.println(BeanUtils.getSimpleProperty(object, property) );
				}
			}
			else
			{
				// No observation found
				System.out.println("Did not find any results for " + PKName  + " = " + PKValue);
			}
	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param rs
	 * @param i
	 * @return
	 */
	private String getColumnValue(ResultSet rs, int i, ResultSetMetaData metaData) throws SQLException
	{
		String value = null;
		int SQLType = metaData.getColumnType(i);
		
		if ( SQLType == Types.BOOLEAN )
		{
			boolean booleanValue = rs.getBoolean(i);
			value = (new Boolean(booleanValue)).toString();
		}
		else if ( SQLType == Types.DATE )
		{
			// check for null
			Date dateValue = rs.getDate(i);
			if ( dateValue == null )
			{
				value = null;
			}
			else
			{
				value = dateValue.toString();
			}
		}
		else
		{
			value = rs.getString(i);
		}
		return value;
	}

	/**
	 * This is used to convert reflected fieldNames from the Vegbank Datamodel 
	 * to the corresponding property name for use with the apache BeanUtils class.
	 * 
	 * @param name
	 * @return
	 */
	private static String getPropertyName(String name)
	{
		String nametoCheck = name.substring(1); 
		for ( int i=0; i < nametoCheck.length(); i++ )
		{
			if ( Character.getType( nametoCheck.charAt(i) ) == Character.LOWERCASE_LETTER )
			{
				return name;
			}
		}
		return name.toUpperCase();
	}

	/**
	 * Construct a SQL statement from parameters.
	 * 
	 * @param fieldNames
	 * @param className
	 * @param key
	 * @param keyValue
	 * @return SQL Statement
	 */
	private String getSQLSelectStatement(
		Vector fieldNames,
		String className,
		String key,
		int keyValue)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		
		Iterator iter = fieldNames.iterator();
		while ( iter.hasNext() )
		{
			sb.append( iter.next() );
			// If there are more elements use a comma
			if (iter.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append( " FROM " + className + " WHERE " + key + " = " + keyValue);
		
		//System.out.println(">>>>> " + className +" :::: " + sb.toString());
		return sb.toString();
	}
	
}
