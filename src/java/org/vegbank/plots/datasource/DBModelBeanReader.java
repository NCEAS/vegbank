/*
 *	'$RCSfile: DBModelBeanReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-01-18 20:47:47 $'
 *	'$Revision: 1.14 $'
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vegbank.common.model.*;
import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DBConnection;
import org.vegbank.common.utility.DBConnectionPool;
import org.vegbank.common.utility.LogUtility;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;
import org.vegbank.common.utility.XMLUtil;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Reads an <code>VBModelBean</code> from the Vegbank Database and populates
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
	private ModelBeanCache mbCache = null;

	
	public DBModelBeanReader() throws SQLException
	{
		con = DBConnectionPool.getInstance().getDBConnection("Needed for reading observation");
		mbCache = ModelBeanCache.instance();
	}
	
	/**
	 * convienice method for looking up a tables PK from its accessionCode 
	 * field
	 * 
	 * @param entityName
	 * @param accessionCode
	 * @param pKName
	 * @return long -- PK of the row found
	 * @throws Exception
	 */
	private long getPKFromAccessionCode(
		String entityName,
		String accessionCode,
		String pKName)
		throws Exception
	{
		long pk = 0;

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
			pk = rs.getLong(1);
		}
		else
		{
			throw new Exception(
				"Could not find a row in '"
					+ entityName
					+ "' with '"
					+ accessionCodeName
					+ "' = "
					+ accessionCode);
		}

		return pk;
	}
	

	/**
	 * Uses accessionCode to unmarshall the corresponding model bean 
	 * from the database.
	 *   
	 * @param accessionCode
	 * @return VBModelBean
	 */ 
	public VBModelBean getVBModelBean(String accessionCode) throws Exception
	{
		VBModelBean result = null;
		HashMap parsedAC = Utility.parseAccessionCode(accessionCode);
		String entityCode = (String) parsedAC.get("ENTITYCODE");
		
		LogUtility.log("Got an entity code of " + entityCode + " from " + accessionCode);
		
		// TODO: Get the entity name from AccessionGen & co when stable
		//AccessionGen ag = new AccessionGen();
		//String entityName = ag.getTableName(entityCode);
		
		// TODO: Depending upon entity go get it
		if ( entityCode.equalsIgnoreCase( "PC" ) )
		{
			result = this.getPlantconceptBeanTree(accessionCode);
		}
		else if ( entityCode.equalsIgnoreCase("Ob") )
		{
			result = this.getPlotObservationBeanTree(accessionCode);
		}
		return result;
	}

	public Plantconcept getPlantconceptBeanTree(String accessionCode) throws Exception
	{	
		long pK = 0; 
		pK = this.getPKFromAccessionCode( "plantConcept", accessionCode, Plantconcept.PKNAME );
		return this.getPlantconceptBeanTree(pK);	
	}
	
	public Plantconcept getPlantconceptBeanTree(long pkValue) throws Exception
	{
		Plantconcept pc = new Plantconcept();
		getObjectFromDB(pc, Plantconcept.PKNAME, pkValue);
		getRelatedObjectsFromDB(Plantconcept.PKNAME, pkValue, pc);
		
		return pc;
	}
	
	public Plot getPlotObservationBeanTree(String observationAccessionCode) throws Exception
	{	
		//TODO: Am I allowed to search cache --- revisions !!!
		// Search cache first
		Plot plot = 
			(Plot) mbCache.getBeanFromCache(observationAccessionCode);
		if ( plot != null )
		{
			return plot;
		}
		
		long pK = this.getPKFromAccessionCode( "observation", observationAccessionCode, Observation.PKNAME );
		plot = this.getPlotObservationBeanTree(pK);	
		
		// Add to cache
		mbCache.addToCache(plot, observationAccessionCode);
		
		return plot;
	}
	
	public Plot getPlotObservationBeanTree(long observationId) throws Exception
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
		long plotId  = obs.getPlot_id();
		Plot plot = new Plot();
		getObjectFromDB(plot, Plot.PKNAME, plotId);
		getRelatedObjectsFromDB(Plot.PKNAME, plotId, plot);

		// Add the observation to the Plot bean
		plot.addplot_observation( obs );
		return plot;
	}
	
	// TODO: Reference, Party, Commconcept
	
		
	public VBModelBean LgetVBModelBean(String BeanName, long pkValue) throws Exception
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

	private void getRelatedObjectsFromDB(String key, long keyValue, VBModelBean bean)
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
				LogUtility.log("BDModelBeanReader: Error > " + e.getMessage(), e);
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
		String tableName = VBObjectUtils.getFieldName( method.getName(), null, null );
		
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
	
	private List getObjectsFromDataBase(String table, String FKName, long FKValue) throws Exception
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
			long key = ( (Integer) keysIterator.next() ).intValue();
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

	private void getObjectFromDB(VBModelBean bean, String PKName, long PKValue)
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
			String fieldName = VBObjectUtils.getFieldName(methodName, null, null);
			Class[] parameterTypes = method.getParameterTypes();
			
			if ( VBObjectUtils.isGetMethod(method, "java.lang.String") )
			{					
				// get the name of the field 
				fieldNames.add(fieldName);
			}
			else if ( VBObjectUtils.isGetMethod(method, "long") )
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
					String propertyName = VBObjectUtils.getFieldName(method.getName(), "object", null);
					
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
							
							fieldName = Utility.getPKNameFromFKName(fieldName);
							
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
								LogUtility.log("DBModelBeanReader: Not an Integer: " + value, e1 );
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
				LogUtility.log("DBModelBeanReader: Did not find any results for " + PKName  + " = " + PKValue);
			}
	
		}
		catch (Exception e)
		{
			LogUtility.log("DBModelBeanReader: Error reading object from DB: " + e.getMessage(), e);
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
		long keyValue)
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
		/**
		 * Vector of vectors to cache Objects into memory, each element of the memoryCache
		 * has a vector that contains an accessionCode (elementAt(0)) and the Object (elementAt(1))
		 */
		private static Vector memoryCache = new Vector();
		
		/**
		 * Vector to store fileNames of the serialized Objects on the disk
		 */
		private static Vector diskCacheKeys = new Vector();
		
		/**
		 * The maximum number of entries in the memory cache
		 */
		private static final int MAX_MEM_CACHE_SIZE = 2;
		
		/**
		 * The directory to  cache into.
		 * FIXME: Make a configurable property
		 */
		private static final File CACHE_DIR = new File("/usr/vegbank/cache");
		
		/**
		 * The maximum number of entries in the disk cache,
		 * can be made really large as a large bean is 200k,
		 * Keep an eye on typical size as data is loaded, however!
		 */
		private static final int MAX_DISK_CACHE_SIZE = 3000;

		/**
		 * A handle to the unique ModelBeanCache instance.
		 */
		static private ModelBeanCache instance = null;
 
		/**
		 * Private constructor can only be called by this class, i.e Singleton
		 */
		private ModelBeanCache() {
			registerDiskCachedBeans();
		}
		
		/**
		 * @return The unique instance of this class.
		 */
		static public ModelBeanCache instance()
		{
			if (null == instance)
			{
				LogUtility.log("ModelBeanCache: Creating Instance");
				instance = new ModelBeanCache();
			}
			return instance;
		}
		
		
		/**
		 * Register all the beans on disk to diskCache on startup
		 */
		private void registerDiskCachedBeans()
		{	
			if ( CACHE_DIR == null )
			{
				LogUtility.log("ModelBeanCache: Disk cache dir is absent, " + CACHE_DIR);
			}
			else
			{
				File[]  cachedFiles = CACHE_DIR.listFiles();
				if ( cachedFiles == null )
				{
					// Nothing to read
				}
				else
				{
					for ( int i=0; i<cachedFiles.length ; i++ )
					{
						String  fileName = cachedFiles[i].getName();
						if ( fileName.startsWith( Utility.getAccessionPrefix() + ".") )
						{
							LogUtility.log("ModelBeanCache: Added to Disk Cache: " + fileName);
							// Add all the names to diskCacheKeys
							diskCacheKeys.add( fileName );
						}
						else 
						{
							LogUtility.log("ModelBeanCache: Not adding to Disk Cache: " + fileName);
						}
					}
				}
			}
		}
		
		/**
		 * Add a new accessionCode bean pair to cache
		 * 
		 * @param bean
		 * @param accessionCode
		 */
		public synchronized  void  addToCache( VBModelBean bean, String accessionCode)
		{
			if ( memoryCache.size() >= MAX_MEM_CACHE_SIZE )
			{
				LogUtility.log(
					"ModelBeanCache: Memory Cache Full: "
						+ MAX_DISK_CACHE_SIZE
						+ " files.");
				
				// Remove item from memory cache
				memoryCache.removeElementAt(0);
				LogUtility.log("ModelBeanCache: Removed oldest bean from cache Memory Cache");
			}
			Vector newElement = new Vector ();
			newElement.add(accessionCode);
			newElement.add(bean);
			memoryCache.add( newElement );
			LogUtility.log("ModelBeanCache: Added to Memory Cache: " + accessionCode);
			
			// Also add to diskCache
			addToDiskCache(accessionCode, bean);
		}
		
		/**
		 * Add a bean to the disk cache, does some sanity checks and tests removes
		 * oldest bean if Cache is full. <br/>
		 * Allows the caching to persist between JVM lifetimes.
		 * 
		 * @param fileName -- the name to save the bean as.
		 * @param bean -- the bean to cache on disk
		 * 
		 */
		private void addToDiskCache(String fileName, VBModelBean beanToSave)
		{
			if ( CACHE_DIR == null )
			{
				LogUtility.log("ModelBeanCache: Disk cache dir is absent, " + CACHE_DIR);
			}
			else
			{
				try
				{
					if ( diskCacheKeys.size() >=  MAX_DISK_CACHE_SIZE )
					{
						LogUtility.log(
							"ModelBeanCache: Disk Cache Full: "
								+ MAX_DISK_CACHE_SIZE
								+ " files.");
							
						// Need to remove first Object from disk
						String fileNameToAxe = (String) diskCacheKeys.firstElement();
						File fileToAxe = new File(CACHE_DIR, fileNameToAxe);
						fileToAxe.delete();
						LogUtility.log("ModelBeanCache: Deleted from Disk Cache: " + fileNameToAxe);
					}
					saveToDisk(beanToSave, fileName);			
					LogUtility.log("ModelBeanCache: Added to Disk Cache: " + fileName);
				
					// Put the fileName is the diskCache
					diskCacheKeys.add(fileName);
				}
				catch (IOException e)
				{
					LogUtility.log("ModelBeanCache: Failed to save Object to disk", e);
				}
			}
		}

		/**
		 * Search for a bean that has a key of accessionCode
		 * 
		 * @param accessionCode
		 * @return VBModelBean -- The bean found or null if none found
		 */
		public VBModelBean getBeanFromCache( String accessionCode )
		{
			// Search memoryCache first
			for ( int i=0 ; i < memoryCache.size() ; i++)
			{
				Vector currentElement = (Vector) memoryCache.elementAt(i);
				LogUtility.log( "ModelBeanCache: accessionCode" + currentElement.elementAt(0) + " = " + currentElement.elementAt(1));
				if ( accessionCode.equalsIgnoreCase( (String) currentElement.elementAt(0) ) )
				{
					LogUtility.log("ModelBeanCache: Retrived from memory Cache: " + accessionCode);
					return (VBModelBean) currentElement.elementAt(1);
				}
			}
			
			// Search the diskCache
			for ( int i=0 ; i < diskCacheKeys.size() ; i++)
			{
				String fileName = (String) diskCacheKeys.elementAt(i);
				VBModelBean bean;
				if ( accessionCode.equalsIgnoreCase( fileName ) )
				{
					try
					{
						bean = (VBModelBean) readBeanFromDisk(fileName);
						LogUtility.log("ModelBeanCache: Retrived from disk Cache: " + fileName);
						return bean;
					}
					catch (Exception e)
					{
						LogUtility.log("ModelBeanCache: Failed to retrieve Object from disk", e);
					}
				}
			}
			return null;
		}
		
		/**
		 * Serialize an <code>VBModelBean<code> to the disk.
		 * 
		 * @param bean <code>VBModelBean<code> to serialize
		 * @param fileName Filename stored to
		 * @throws IOException
		 */
		private static void saveToDisk( VBModelBean bean, String fileName ) throws IOException
		{
			//Write to disk with FileOutputStream
			FileOutputStream fileOut = new FileOutputStream(new File( CACHE_DIR, fileName));

			// Write object with ObjectOutputStream
			ObjectOutputStream objectOut = new ObjectOutputStream (fileOut);

			// Write object out to disk
			objectOut.writeObject ( bean );
		}
		
		/**
		 * Retrieves a <code>VBModelBean<code> that had been serialized to the
		 * disk.
		 * 
		 * @param fileName	filename the bean was serialized to.
		 * @return bean	the restored bean
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private static VBModelBean readBeanFromDisk( String fileName ) throws IOException, ClassNotFoundException
		{
			// Read from disk using FileInputStream
			FileInputStream fileIn = new  FileInputStream( new File( CACHE_DIR, fileName) );

			// Read object using ObjectInputStream
			ObjectInputStream beanIn = new ObjectInputStream (fileIn);

			// Read an object
			VBModelBean bean = (VBModelBean) beanIn.readObject();
			
			return bean;
		}
		
	}
	
}
