/*
 *	'$RCSfile: DBObservationReader.java,v $'
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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Collection;
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
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Reads an Observation from the Vegbank Database.
 * 
 * @author farrell
 */

public class DBObservationReader
{

	/**
	 * Uses to run SQL statements agains db
	 */
	private DatabaseAccess da;
	Vector ignoreObjects = new Vector();
	
	DBObservationReader()
	{
		da = new DatabaseAccess();
	}
	
	public Observation getObservation(String key, int keyValue) throws Exception
	{
		Observation obs = new Observation();
		getObjectFromDB(obs, key, keyValue);
		getRelatedObjectsFromDB(key, keyValue, obs, ignoreObjects);
		
		// Need to fill out some more Objects 
		// Project 
		Project project = obs.getProject();
		ignoreObjects.add("Observation");
		getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Project"), project.getProject_id(), project, ignoreObjects );
		
		// ProjectContributors
		Iterator projectContributors =project.getprojectprojectcontributors().iterator();
		while ( projectContributors.hasNext() )
		{
			Projectcontributor projectContributor = (Projectcontributor)  projectContributors.next();
			Party party = (Party) projectContributor.getParty();
			//System.out.println("###########################################################");
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Party"), party.getParty_id(), party, ignoreObjects );
			//System.out.println("###########################################################");
		}
		
		// TaxonObservations
		Iterator taxonObservations = obs.getobservationtaxonobservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			Taxonobservation taxonObservation = (Taxonobservation)  taxonObservations.next();
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("TaxonObservation"), taxonObservation.getTaxonobservation_id(), taxonObservation, ignoreObjects );
			
			Plantname plantName = taxonObservation.getPlantname();
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
		Iterator strata = obs.getobservationstratums().iterator();
		while ( strata.hasNext())
		{
			Stratum stratum = (Stratum) strata.next();
			getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Stratum"), stratum.getStratum_id(), stratum, ignoreObjects );

		}
		
		// Plot
		Plot plot = obs.getPlot();
		getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Plot"), plot.getPlot_id(), plot, ignoreObjects );
		
		
		//Project project = obs.getPROJECT();
		//ignoreObjects.add("Observation");
		//getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Project"), project.getPROJECT_ID(), project, ignoreObjects );
		
		
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
	 * 	setpartyAddresss ==> Address
	 * 	setORGANIZATIONAddresss ==> Address
	 * @param object
	 * @param method
	 * @return
	 */
	private String getTableName(Object object, Method method)
	{
		String tableName = VBObjectUtils.getFieldName( method.getName(), null );
		//System.out.println(">>>>>>>>>>>" + tableName);
		
		// Reconstitute the name of the table from the mess that is the methodName
		tableName = tableName.substring(0, tableName.length()-1); // remove the trailing 's'
		
		// Start count at 1 to avoid potential problems with first character and remove
		for ( int i=1; i < tableName.length(); i++)
		{
			if (  Character.getType(tableName.charAt(i)) == Character.LOWERCASE_LETTER )
			{
				//System.out.println(" ### " + tableName.charAt(i) + " from " + tableName);
				tableName = tableName.substring(i-1);
				break;
			}
		}
		// Reconstitute the name of the table from the mess that is the methodName
		
		System.out.println(VBObjectUtils.getUnQualifiedName( object.getClass().getName().toLowerCase()));
		tableName = StringUtils.replaceOnce(tableName,VBObjectUtils.getUnQualifiedName( object.getClass().getName()).toLowerCase(), "");
		
		return tableName;
	}
	
	private List getObjectsFromDataBase(String table, String FKName, int FKValue) throws Exception
	{
		Vector retrivedObjects = new Vector();
		
		// Get all the PK associated with this object
		Vector keys = new Vector();
		
		String PKName = table + "_ID";
		String SQLQuery = 
			"SELECT " + PKName + " FROM " + table + " WHERE " + FKName + " = " + FKValue;
			
		ResultSet rs = da.issueSelect(SQLQuery);
		
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

	private void getObjectFromDB(Object object, String PKName, int PKValue)
	{
		// Place to store name values for populating object
		HashMap hmap = new HashMap();
		HashMap objectSetMethods = new HashMap();
		
		Method[] methods = object.getClass().getMethods();
		String className = VBObjectUtils.getUnQualifiedName(object.getClass().getName());
		
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
			}
		}
		
		String sqlStatement = this.getSQLSelectStatement(fieldNames, className, PKName, PKValue);
		
		try
		{
			ResultSet rs = da.issueSelect(sqlStatement);
			int columnNumber = rs.getMetaData().getColumnCount();
			
			// Is there an observation?
			if ( rs.next() )
			{
				// For each value add to the HashMap
				for ( int ii=0; ii < columnNumber; ii++ )
				{
					hmap.put(fieldNames.elementAt(ii), rs.getString(ii+1) );	
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
		return sb.toString();
	}
	
}
