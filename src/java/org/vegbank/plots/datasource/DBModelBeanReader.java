/*
 *	'$RCSfile: DBModelBeanReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-11-05 18:45:30 $'
 *	'$Revision: 1.7 $'
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
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.vegbank.common.model.Covermethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Observationsynonym;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Plantconcept;
import org.vegbank.common.model.Plantname;
import org.vegbank.common.model.Plot;
import org.vegbank.common.model.Project;
import org.vegbank.common.model.Projectcontributor;
import org.vegbank.common.model.Stratum;
import org.vegbank.common.model.Stratummethod;
import org.vegbank.common.model.Taxoninterpretation;
import org.vegbank.common.model.Taxonobservation;
import org.vegbank.common.model.VBModelBean;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;
import org.vegbank.common.utility.XMLUtil;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Reads an Observation from the Vegbank Database and populates
 * a tree of beans with the data. The root of the tree is observation.
 * 
 * @author farrell
 */

public class DBModelBeanReader
{

	/**
	 * Uses to run SQL statements agains db
	 */
	private DBConnection con = null;
	private Vector ignoreObjects = new Vector();

	
	public DBModelBeanReader() throws SQLException
	{
		con = DBConnectionPool.getDBConnection("Needed for reading observation");
	}
	
	/**
	 * 
	 * @param key
	 * @param keyValue
	 * @return
	 * @throws Exception
	 * 
	 * @deprecated
	 */
	public Observation LgetObservation(String key, int keyValue) throws Exception
	{
		Observation obs = new Observation();
		getObjectFromDB(obs, key, keyValue);
		getRelatedObjectsFromDB(key, keyValue, obs);
		
		// Need to fill out some more Objects 
		// Project 
		Project project = obs.getProjectobject();
		ignoreObjects.add("Observation");
		getRelatedObjectsFromDB(Project.PKNAME, project.getProject_id(), project );
		
		// ProjectContributors
		Iterator projectContributors =project.getproject_projectcontributors().iterator();
		while ( projectContributors.hasNext() )
		{
			Projectcontributor projectContributor = (Projectcontributor)  projectContributors.next();
			Party party = (Party) projectContributor.getPartyobject();
			//System.out.println("###########################################################");
			getRelatedObjectsFromDB(Party.PKNAME, party.getParty_id(), party );
			//System.out.println("###########################################################");
		}
		
		// TaxonObservations
		Iterator taxonObservations = obs.getobservation_taxonobservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			Taxonobservation taxonObservation = (Taxonobservation)  taxonObservations.next();
			getRelatedObjectsFromDB(Taxonobservation.PKNAME, taxonObservation.getTaxonobservation_id(), taxonObservation );
			
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
			//System.out.println(">> Ignore when searching through Stratum " + ignoreObjects);
			getRelatedObjectsFromDB(Stratum.PKNAME, stratum.getStratum_id(), stratum );

		}
		
		// Plot
		//Plot plot = obs.getPlotobject();
		//System.out.println("Ignore when searching through Plot " + ignoreObjects);
		//getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Plot"), plot.getPlot_id(), plot, ignoreObjects );
		
		return obs;
	}
	
	/**
	 * convienice method for looking up a tables PK from its accessionCode 
	 * field
	 * 
	 * @param entityName
	 * @param accessionCode
	 * @param pKName
	 * @return int -- PK of the row found
	 * @throws Exception
	 */
	private int getPKFromAccessionCode(
		String entityName,
		String accessionCode,
		String pKName)
		throws Exception
	{
		int pk = 0;

		// Get the PK name for this table
		String fieldName = Utility.getPKNameFromTableName(entityName);

		// Get the accessionCode name for this table
		String accessionCodeName = 
			Utility.getAccessionCodeAttributeName(entityName);

		if ( fieldName == null )
		{
			throw new Exception(entityName + " has no accessionCode or similar  field");
		}

		// Find PK for accessionCode 
		String query =
			" select "
				+ fieldName
				+ " from "
				+ entityName
				+ " where "
				+ accessionCodeName
				+ "= '"
				+ accessionCode
				+ "'";

		Statement queryAccessionCode = con.createStatement();
		ResultSet rs = queryAccessionCode.executeQuery(query);

		if (rs.next())
		{
			pk = rs.getInt(1);
		}
		else
		{
			throw new Exception(
				"Could not find a row in '"
					+ entityName
					+ "' with '"
					+ fieldName
					+ "' = "
					+ accessionCode);
		}

		return pk;
	}
	
	public Plantconcept getPlantconceptBeanTree(String accessionCode) throws Exception
	{	
		int pK = 0; 
		this.getPKFromAccessionCode( "plantConcept", accessionCode, Plantconcept.PKNAME );
		return this.getPlantconceptBeanTree(pK);	
	}
	
	public Plantconcept getPlantconceptBeanTree(int pkValue) throws Exception
	{
		Plantconcept pc = new Plantconcept();
		getObjectFromDB(pc, Plantconcept.PKNAME, pkValue);
		getRelatedObjectsFromDB(Plantconcept.PKNAME, pkValue, pc);
		
		return pc;
	}
	
	public Plot getPlotObservationBeanTree(String observationAccessionCode) throws Exception
	{	
		// Search cache first
		Plot plot = 
			(Plot) ModelBeanCache.getBeanFromCache(observationAccessionCode);
		if ( plot != null )
		{
			return plot;
		}
		
		int pK = this.getPKFromAccessionCode( "observation", observationAccessionCode, Observation.PKNAME );
		plot = this.getPlotObservationBeanTree(pK);	
		
		// Add to cache
		ModelBeanCache.addToCache(plot, observationAccessionCode);
		
		return plot;
	}
	
	public Plot getPlotObservationBeanTree(int observationId) throws Exception
	{
		// Get the observation
		Observation obs = new Observation();
		getObjectFromDB(obs, Observation.PKNAME, observationId);
		getRelatedObjectsFromDB(Observation.PKNAME, observationId, obs);
		ignoreObjects.add("Observation");	
		
		// Need to fill out some more Objects 
		
		// Project 
		Project project = obs.getProjectobject();
		getRelatedObjectsFromDB(Project.PKNAME, project.getProject_id(), project );
		
		// CoverMethod
		Covermethod coverMethod = obs.getCovermethodobject();
		getRelatedObjectsFromDB(Covermethod.PKNAME, coverMethod.getCovermethod_id(), coverMethod );
		
		// StratumMethod
		Stratummethod stratumMethod = obs.getStratummethodobject();
		getRelatedObjectsFromDB(Stratummethod.PKNAME, stratumMethod.getStratummethod_id(), stratumMethod );
		
		// ProjectContributors
		Iterator projectContributors =project.getproject_projectcontributors().iterator();
		while ( projectContributors.hasNext() )
		{
			Projectcontributor projectContributor = (Projectcontributor)  projectContributors.next();
			Party party = (Party) projectContributor.getPartyobject();
			getRelatedObjectsFromDB(Party.PKNAME, party.getParty_id(), party );
		}
		
		// TaxonObservations
		Iterator taxonObservations = obs.getobservation_taxonobservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			Taxonobservation taxonObservation = (Taxonobservation)  taxonObservations.next();
			getRelatedObjectsFromDB(Taxonobservation.PKNAME, taxonObservation.getTaxonobservation_id(), taxonObservation );
			
			//TaxonInterpretations
			Iterator taxonInterpretations =taxonObservation.gettaxonobservation_taxoninterpretations().iterator();
			while ( taxonInterpretations.hasNext() )
			{
				Taxoninterpretation taxonInterpretation = (Taxoninterpretation)  taxonInterpretations.next();
				getRelatedObjectsFromDB(Taxoninterpretation.PKNAME, taxonInterpretation.getTaxoninterpretation_id(), taxonInterpretation );
			
				Plantconcept pc = taxonInterpretation.getPlantconceptobject();
				getRelatedObjectsFromDB(Plantconcept.PKNAME, taxonInterpretation.getPlantconcept_id() , pc );
		
			}
			
		}
		
		// Strata
		Iterator strata = obs.getobservation_stratums().iterator();
		while ( strata.hasNext())
		{
			Stratum stratum = (Stratum) strata.next();
			//System.out.println(">> Ignore when searching through Stratum " + ignoreObjects);
			getRelatedObjectsFromDB(Stratum.PKNAME, stratum.getStratum_id(), stratum );
		}
		
		// Plot
		int plotId  = obs.getPlot_id();
		Plot plot = new Plot();
		getObjectFromDB(plot, Plot.PKNAME, plotId);
		getRelatedObjectsFromDB(Plot.PKNAME, plotId, plot);

		// Add the observation to the Plot bean
		plot.addplot_observation( obs );
		return plot;
	}
	
	// TODO: Reference, Party, Commconcept
	
		
	public VBModelBean getVBModelBean(String BeanName, int pkValue) throws Exception
	{
		Observation obs = new Observation();
		getObjectFromDB(obs, Observation.PKNAME, pkValue);
		getRelatedObjectsFromDB(Observation.PKNAME, pkValue, obs);
		
		// Need to fill out some more Objects 
		// Project 
		Project project = obs.getProjectobject();
		ignoreObjects.add("Observation");
		getRelatedObjectsFromDB(Project.PKNAME, project.getProject_id(), project );
		
		// ProjectContributors
		Iterator projectContributors =project.getproject_projectcontributors().iterator();
		while ( projectContributors.hasNext() )
		{
			Projectcontributor projectContributor = (Projectcontributor)  projectContributors.next();
			Party party = (Party) projectContributor.getPartyobject();
			getRelatedObjectsFromDB(Party.PKNAME, party.getParty_id(), party );
		}
		
		// TaxonObservations
		Iterator taxonObservations = obs.getobservation_taxonobservations().iterator();
		while ( taxonObservations.hasNext() )
		{
			Taxonobservation taxonObservation = (Taxonobservation)  taxonObservations.next();
			getRelatedObjectsFromDB(Taxonobservation.PKNAME, taxonObservation.getTaxonobservation_id(), taxonObservation );
			
			Plantname plantName = taxonObservation.getPlantnameobject();
		}
		
		// Strata
		Iterator strata = obs.getobservation_stratums().iterator();
		while ( strata.hasNext())
		{
			Stratum stratum = (Stratum) strata.next();
			//System.out.println(">> Ignore when searching through Stratum " + ignoreObjects);
			getRelatedObjectsFromDB(Stratum.PKNAME, stratum.getStratum_id(), stratum );

		}
		
		// Plot
		//Plot plot = obs.getPlotobject();
		//System.out.println("Ignore when searching through Plot " + ignoreObjects);
		//getRelatedObjectsFromDB(VBObjectUtils.getKeyName("Plot"), plot.getPlot_id(), plot, ignoreObjects );
		
		return obs;
	}

	private void getRelatedObjectsFromDB(String key, int keyValue, VBModelBean bean)
	{
		// Get the one to many relationships
		Collection ListMethods = VBObjectUtils.getSetMethods(bean, "java.util.List");
		//System.out.println("##### " + ListMethods);
		Iterator iter = ListMethods.iterator();
		
		OUTERLOOP: while ( iter.hasNext() )
		{
			Method method = (Method) iter.next();
			// Get the tableName
			String tableName = getTableName(bean, method);	
			
			// Filter out tables called
			//System.out.println("----- " + ignoreObjects);
			Iterator iterator = ignoreObjects.iterator();
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
				String keytoUse = this.getFKNameInTable(key, tableName);
				//System.out.println("getObjectsFromDataBase(" + tableName + " , " + keytoUse + " , " + keyValue + ")");
				List objectsToAdd =  getObjectsFromDataBase(tableName, keytoUse, keyValue);
				Object[] parameters = {objectsToAdd};
				method.invoke(bean, parameters);
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
		
		String PKName = Utility.getPKNameFromTableName( table );
		String SQLQuery = 
			"SELECT " + PKName + " FROM " + table + " WHERE " + FKName + " = " + FKValue;
		
		//System.out.println("]]] "  + SQLQuery);
		
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
			VBModelBean newObject = (VBModelBean) Utility.createObject( VBObjectUtils.getFullyQualifiedName(table) );	
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
		String result = FKName;
		
		if ( tableName.equalsIgnoreCase("observationsynonym") && FKName.equalsIgnoreCase(Observation.PKNAME) )
		{
			result = Observationsynonym.PRIMARYOBSERVATION_ID;
		}
		
		if ( tableName.equalsIgnoreCase("plot") && FKName.equals(Plot.PKNAME))
		{
			result = Plot.PARENT_ID;
		}
		//System.out.println(tableName + " X " + FKName + "  = ???? " + result);
		return result;
	}

	private void getObjectFromDB(VBModelBean bean, String PKName, int PKValue)
	{
		HashMap objectSetMethods = new HashMap();
		
		Method[] methods = bean.getClass().getMethods();
		String className = VBObjectUtils.getUnQualifiedName(bean.getClass().getName());
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
		LogUtility.log("BDModelBeanReader: " + sqlStatement);
		
		try
		{
			Statement selectStatement = con.createStatement();
			ResultSet rs = selectStatement.executeQuery(sqlStatement);
			int columnNumber = rs.getMetaData().getColumnCount();
			
			// Is there an row?
			if ( rs.next() )
			{
				ResultSetMetaData metaData = rs.getMetaData();
				
				// Place to store name values for populating object
				HashMap hmap = new HashMap();
				
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
					String propertyName = VBObjectUtils.getFieldName(method.getName(), "object");
					
					// Need to get the corresponding PK name and value
					String value = (String) hmap.get(fieldName);
					
					// DO NOT GET OBJECTS WITH INVERTED RELATIONSHIP
					//System.out.println("Checking inverted "  + propertyName);
					if ( ! bean.isInvertedRelationship( propertyName ) )
					{	
						// Need to create an object for each Set Object Method
						VBModelBean newObject = (VBModelBean) Utility.createObject(fullyQualifiedClassName);
						try
						{
							LogUtility.log("DBModelBeanReader: Getting "  + propertyName + " for " + className);
							
							// new method -- maybe
							// Don't know what i'm doing here -- tring to solve a bug
							// maybe need special handling depending on PK search or FK search ?	
							if ( fieldName.equalsIgnoreCase(Party.OWNER_ID))
							{
								fieldName = Party.PKNAME;
							}
							// New Method end -- maybe
							
							//LogUtility.log("]]]" + hmap);
							//	Fill out this object  --- RECURSIVE carefull
							//LogUtility.log("this.getObjectFromDB( " + newObject + "," + fieldName+ ", " +  new Integer(value).intValue() + ")" );
							this.getObjectFromDB(newObject, fieldName, new Integer(value).intValue() );
							Object[] parameters =  {newObject} ;
							//	Add it using the set method
							method.invoke(bean, parameters);
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
				}
	
				
				Iterator it = hmap.keySet().iterator();
				while ( it.hasNext() )
				{
					String name = (String) it.next();
					String value = (String) hmap.get(name);
					//System.out.println("DBObservationReader > name: '" + name + "' value: '" + value + "'" );
					
					// Populate Object with value
					String property = getPropertyName(name);
					BeanUtils.copyProperty(bean, property, value);
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
		
		// Postgres claims it bool is a bit so ....
		if (SQLType == Types.BOOLEAN || SQLType == Types.BIT)
		{
			// if null it automatically defaults to false
			boolean booleanValue = rs.getBoolean(i);
			
			// Was a null read from the database last 
			if ( rs.wasNull() )
			{
				// set to null;
				value = null;
			}
			else
			{
				value = ""+booleanValue;
			}
			//System.out.println("boolean was " + value);
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
				value = XMLUtil.convertDateToXSdatetime(dateValue);
			}
		}
		else
		{
			//System.out.println( metaData.getColumnName(i) + ": Just get as String for SQLTYPE: " + SQLType );
			//System.out.println( "BOOLEAN is " + Types.BOOLEAN);
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
	
	/**
	 * ModelBeanCache implements a small, simple cache for model beans using
	 * a Vector of Vectors  and a key of the accessionCode
	 * 
	 * Is a FIFO cache, first entry gets axed when size limit reached.
	 *  
	 * @author farrell
	 *
	 */
	public static class ModelBeanCache
	{
		private static Vector cache = new Vector();
		private static final int MAX_CACHE_SIZE = 5;
		
		/**
		 * Add a new accessionCode bean pair to cache
		 * 
		 * @param bean
		 * @param accessionCode
		 */
		public static synchronized  void  addToCache( VBModelBean bean, String accessionCode)
		{
			if ( cache.size() >= MAX_CACHE_SIZE )
			{
				cache.removeElementAt(0);
			}
			Vector newElement = new Vector ();
			newElement.add(accessionCode);
			newElement.add(bean);
			cache.add( newElement );
		}
		
		/**
		 * Search for a bean that has a key of accessionCode
		 * @param accessionCode
		 * @return VBModelBean -- The bean found or null if none found
		 */
		public static VBModelBean getBeanFromCache( String accessionCode )
		{
			for ( int i=0 ; i < cache.size() ; i++)
			{
				Vector currentElement = (Vector) cache.elementAt(i);
				if ( accessionCode.equalsIgnoreCase( (String) currentElement.elementAt(0) ) )
				{
					return (VBModelBean) currentElement.elementAt(1);
				}
			}
			return null;
		}
	}
	
}
