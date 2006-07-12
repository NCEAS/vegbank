/*
 *	'$RCSfile: DBModelBeanReader.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-07-12 19:48:46 $'
 *	'$Revision: 1.23 $'
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.model.Covermethod;
import org.vegbank.common.model.Observation;
import org.vegbank.common.model.Observationsynonym;
import org.vegbank.common.model.Party;
import org.vegbank.common.model.Partymember;
import org.vegbank.common.model.Plantconcept;
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
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.VBObjectUtils;
import org.vegbank.common.utility.XMLUtil;
import org.vegbank.common.Constants;

/**
 * Reads an <code>VBModelBean</code> from the Vegbank Database and populates
 * a tree of beans with the data. The root of the tree is observation.
 * 
 * @author farrell
 */

public class DBModelBeanReader
{
	
	/**
	 * Commons Logging instance.
	 */
	protected static Log log = LogFactory.getLog(DBModelBeanReader.class);

	private DBConnection con = null;
	private boolean connectionCheckedOut = false;
	private Vector ignoreObjects = new Vector();
	private ModelBeanCache mbCache = null;

	
	public DBModelBeanReader() throws SQLException
	{
		con = DBConnectionPool.getInstance().getDBConnection("Needed for reading VBModelBean");
		con.setReadOnly(true);
		mbCache = ModelBeanCache.instance();
	}
	
	protected void finalize () throws Throwable
	{
		if ( this.connectionCheckedOut )
		{
			try
			{
				this.releaseConnection();
			}
			finally
			{
				super.finalize();
			}
		}
		connectionCheckedOut = true;
	}
	
	/**
	 * Release connection back into ConnectionPool
	 *
	 */
	public void releaseConnection() throws SQLException
	{
		// May be used for writing in future
		con.setReadOnly(false);
		DBConnectionPool.returnDBConnection(con);
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
				+ Constants.ACCESSIONCODENAME
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
					+ Constants.ACCESSIONCODENAME
					+ "' = "
					+ accessionCode);
		}

		rs.close();
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
		log.debug("Got request for: '" + accessionCode + "'");
		//TODO: Am I allowed to search cache --- revisions !!!
		// Search cache first
		VBModelBean bean = 
			(VBModelBean) mbCache.getBeanFromCache(accessionCode);
		if ( bean != null )
		{
			return bean;
		}		
		
		// Need to get from db
		HashMap parsedAC = Utility.parseAccessionCode(accessionCode);
		String entityCode = (String) parsedAC.get("ENTITYCODE");
		
		log.debug("Got an entity code of " + entityCode + " from " + accessionCode);
		
		// TODO: Get the entity name from AccessionGen & co when stable
		//AccessionGen ag = new AccessionGen();
		//String entityName = ag.getTableName(entityCode);
		
		// TODO: Depending upon entity go get it
		if ( entityCode.equalsIgnoreCase( "PC" ) )
		{
			bean = this.getPlantconceptBeanTree(accessionCode);
		}
		else if ( entityCode.equalsIgnoreCase("Ob") )
		{
			bean = this.getObservationBeanTree(accessionCode);
		}
		else if ( entityCode.equalsIgnoreCase("To") )
		{
			bean = this.getTaxonobservationBeanTree(accessionCode);
		}
		
		// Add to cache
		mbCache.addToCache(bean, accessionCode);
		
		return bean;
	}

	private Plantconcept getPlantconceptBeanTree(String accessionCode) throws Exception
	{	
		long pK = 0; 
		pK = this.getPKFromAccessionCode( "plantConcept", accessionCode, Plantconcept.PKNAME );
		return this.getPlantconceptBeanTree(pK);	
	}
	
	private Plantconcept getPlantconceptBeanTree(long pkValue) throws Exception
	{
		Plantconcept pc = new Plantconcept();
		getObjectFromDB(pc, Plantconcept.PKNAME, pkValue);
		getRelatedObjectsFromDB(Plantconcept.PKNAME, pkValue, pc);
		
		return pc;
	}
	
	private Observation getObservationBeanTree(String observationAccessionCode) throws Exception
	{			
		long pK = this.getPKFromAccessionCode( "observation", observationAccessionCode, Observation.PKNAME );
		Observation obs = this.getObservationBeanTree(pK);	
		return obs;
	}
	
	private Observation getObservationBeanTree(long observationId) throws Exception
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
    if(stratumMethod != null)
    {
      getRelatedObjectsFromDB(Stratummethod.PKNAME, stratumMethod.getStratummethod_id(), stratumMethod );
    }
		
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
			//log.debug(">> Ignore when searching through Stratum " + ignoreObjects);
			getRelatedObjectsFromDB(Stratum.PKNAME, stratum.getStratum_id(), stratum );
		}
		
		// Plot
		long plotId  = obs.getPlot_id();
		Plot plot = new Plot();
		getObjectFromDB(plot, Plot.PKNAME, plotId);
		getRelatedObjectsFromDB(Plot.PKNAME, plotId, plot);

		//		Add the Plot  to the  observation bean
		obs.setPlotobject(plot);

		return obs;
	}
	
	private Taxonobservation getTaxonobservationBeanTree(String accessionCode) throws Exception
	{	
		long pK = 0; 
		pK = this.getPKFromAccessionCode( "taxonObservation", accessionCode, Taxonobservation.PKNAME );
		return this.getTaxonobservationBeanTree(pK);	
	}
	
	private Taxonobservation getTaxonobservationBeanTree(long pkValue) throws Exception
	{
		Taxonobservation to = new Taxonobservation();
		getObjectFromDB(to, Taxonobservation.PKNAME, pkValue);
		//getRelatedObjectsFromDB(Taxonobservation.PKNAME, pkValue, to);
		
		return to;
	}
	
	// TODO: Reference, Party, Commconcept

	private void getRelatedObjectsFromDB(String key, long keyValue, VBModelBean bean)
	{
		// Get the one to many relationships
		Collection ListMethods = VBObjectUtils.getSetMethods(bean, "java.util.List");
		//log.debug("##### " + ListMethods);
		Iterator iter = ListMethods.iterator();
		
		OUTERLOOP: while ( iter.hasNext() )
		{
			Method method = (Method) iter.next();
			// Get the tableName
			String tableName = getTableName(bean, method);	
			
			// Filter out tables called
			//log.debug("----- " + ignoreObjects);
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
				Collection keysToUse = this.getFKNameInTable(key, tableName);
				//log.debug("getObjectsFromDataBase(" + tableName + " , " + keytoUse + " , " + keyValue + ")");
				Iterator it = keysToUse.iterator();
				while ( it.hasNext() )
				{
					String keyName = (String) it.next();
					List objectsToAdd =  getObjectsFromDataBase(tableName, keyName, keyValue);
					Object[] parameters = {objectsToAdd};
					method.invoke(bean, parameters);
				}
			}
			catch (Exception e)
			{
				log.error("Error > " + e.getMessage(), e);
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
		
		//log.debug(VBObjectUtils.getUnQualifiedName( object.getClass().getName().toLowerCase()));
		//tableName = StringUtils.replaceOnce(tableName,VBObjectUtils.getUnQualifiedName( object.getClass().getName()).toLowerCase(), "");
		
		return tableName;
	}
	
	private List getObjectsFromDataBase(String table, String FKName, long FKValue) throws Exception
	{
		Vector retrivedObjects = new Vector();
		
		// Get all the PK associated with this object
		Vector keys = new Vector();
		
		String PKName = Utility.getPKNameFromTableName( table );
		String SQLQuery = 
			"SELECT " + PKName + " FROM " + table + " WHERE " + FKName + " = " + FKValue;
		
		//log.debug("]]] "  + SQLQuery);
		
		Statement selectStatement = con.createStatement();	
		try
		{
			ResultSet rs = selectStatement.executeQuery(SQLQuery);
			while ( rs.next() )
			{
				keys.add( new Integer ( rs.getInt(1) ) );	
			}
			rs.close();
		} 
		catch (SQLException e)
		{
			log.error("SQL causing error: '" + SQLQuery + "'");
			throw e;
		}
		
		Iterator keysIterator = keys.iterator();
		while ( keysIterator.hasNext() )
		{
			long key = ( (Integer) keysIterator.next() ).intValue();
			VBModelBean newObject = (VBModelBean) Utility.createObject( VBObjectUtils.getFullyQualifiedName(table) );	
			this.getObjectFromDB(newObject, PKName, key);
			retrivedObjects.add(newObject);
		}
		//log.debug("##### " + retrivedObjects);
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
	private Collection getFKNameInTable(String PKNameInForeignTable, String tableName)
	{
		String result = PKNameInForeignTable;
		Vector keysToUse = new Vector();
		
		if ( tableName.equalsIgnoreCase("observationsynonym") && PKNameInForeignTable.equalsIgnoreCase(Observation.PKNAME) )
		{
			keysToUse.add(Observationsynonym.PRIMARYOBSERVATION_ID);
		}
		else if ( tableName.equalsIgnoreCase("plot") && PKNameInForeignTable.equals(Plot.PKNAME))
		{
			keysToUse.add(Plot.PARENT_ID);
		}
		else if ( tableName.equalsIgnoreCase("partymember") && PKNameInForeignTable.equals(Party.PKNAME) )
		{
			keysToUse.add(Partymember.CHILDPARTY_ID);
			keysToUse.add(Partymember.PARENTPARTY_ID);
		}
		else 
		{
			// Normally the FK in this table is the same as PK in the foreign table
			keysToUse.add(PKNameInForeignTable);
		}
		return keysToUse;
	}

	private void getObjectFromDB(VBModelBean bean, String PKName, long PKValue) throws SQLException
	{
		HashMap objectSetMethods = new HashMap();
		
		Method[] methods = bean.getClass().getMethods();
		String className = VBObjectUtils.getUnQualifiedName(bean.getClass().getName());
		//log.debug(">>>> " + className + "," + object+ "," + object.getClass().getName());
		
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
                if (fieldName.toLowerCase().equals("accessioncode")) {
                    // certain beans don't actually have ACs
                    String ac = bean.getAccessioncode();
                    if (ac != null && ac.equals(Constants.NO_AC)) {
                        log.debug("added accessioncode to field list..." + methodName);
                    } else {
                        // add the accession code
				        fieldNames.add(fieldName);
                    }

                } else {
                    // add the normal field
				    fieldNames.add(fieldName);
                }
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
				//log.debug("Processing: " + methodAndType );
			}
			else
			{
				//log.debug("Not processing: " + method.toString() );
			}
		}
		
		String sqlStatement = this.getSQLSelectStatement(fieldNames, className, PKName, PKValue);
		log.debug("SQL: " + sqlStatement);
		
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
					//log.debug("Checking inverted "  + propertyName);
					if ( ! bean.isInvertedRelationship( propertyName ) )
					{	
						// Need to create an object for each Set Object Method
						VBModelBean newObject = (VBModelBean) Utility.createObject(fullyQualifiedClassName);
						try
						{
							log.debug("DBModelBeanReader: Getting "  + propertyName + " for " + className);
							
							fieldName = Utility.getPKNameFromFKName(fieldName);
							
							//LogUtility.log("]]]" + hmap, LogUtility.DEBUG);
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
								log.error("Not an Integer: " + value, e1 );
							}
						}
					}
				}
	
				
				Iterator it = hmap.keySet().iterator();
				while ( it.hasNext() )
				{
					String name = (String) it.next();
					String value = (String) hmap.get(name);
					//log.debug("DBObservationReader > name: '" + name + "' value: '" + value + "'" );
					
					// Populate Object with value
					String property = getPropertyName(name);
					BeanUtils.copyProperty(bean, property, value);
					//log.debug(BeanUtils.getSimpleProperty(object, property) );
				}
			}
			else
			{
				// No observation found
				log.debug("Did not find any results for " + PKName  + " = " + PKValue);
			}
			rs.close();
	
		}
		catch (Exception e)
		{
			log.error("Error reading object from DB: " + e.getMessage(), e);
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
			//log.debug("boolean was " + value);
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
			//log.debug( metaData.getColumnName(i) + ": Just get as String for SQLTYPE: " + SQLType );
			//log.debug( "BOOLEAN is " + Types.BOOLEAN);
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
		
		//log.debug(">>>>> " + className +" :::: " + sb.toString());
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
		private static final File CACHE_DIR = new File(Utility.VB_HOME_DIR + "/cache");
		
		/**
		 * The maximum number of entries in the disk cache,
		 * can be made really large as a large bean is 200k,
		 * Keep an eye on typical size as data is loaded, however!
		 */
		private static final int MAX_DISK_CACHE_SIZE = 10000;

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
				log.debug("Creating Instance");
				instance = new ModelBeanCache();
			}
			return instance;
		}
		
		
		/**
		 * Register all the beans on disk to diskCache on startup
		 */
		private void registerDiskCachedBeans()
		{	
			if ( CACHE_DIR == null || ! CACHE_DIR.canRead())
			{
				log.error("Disk cache dir is absent, " + CACHE_DIR);
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
					log.debug("Reading all disk cached bean: " + cachedFiles.length+" files"); 
					for ( int i=0; i<cachedFiles.length ; i++ )
					{
						String  fileName = cachedFiles[i].getName();
						if ( fileName.startsWith( Utility.getAccessionPrefix() + ".") )
						{
							// Add all the names to diskCacheKeys
							diskCacheKeys.add( fileName );
						}
						else 
						{
							log.warn("ModelBeanCache: Not adding to Disk Cache: " + fileName);
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
				log.debug("Memory Cache Full: " + MAX_MEM_CACHE_SIZE + " files.");
				
				// Remove item from memory cache
				memoryCache.removeElementAt(0);
				log.debug("ModelBeanCache: Removed oldest bean from cache Memory Cache");
			}
			Vector newElement = new Vector ();
			newElement.add(accessionCode);
			newElement.add(bean);
			memoryCache.add( newElement );
			log.debug("Added to Memory Cache: " + accessionCode);

			
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
			if ( CACHE_DIR == null || !CACHE_DIR.canWrite() || !CACHE_DIR.canRead() )
			{
				log.error("Problem with Disk cache dir, not able to interact correctly with it, " + CACHE_DIR);
			}
			else
			{
				try
				{
					if ( diskCacheKeys.size() >=  MAX_DISK_CACHE_SIZE )
					{
						log.warn("Disk Cache Full: "+MAX_DISK_CACHE_SIZE+" files.");
							
						// Need to remove first Object from disk
						String fileNameToAxe = (String) diskCacheKeys.firstElement();
						File fileToAxe = new File(CACHE_DIR, fileNameToAxe);
						fileToAxe.delete();
						log.debug("Deleted from Disk Cache: " + fileNameToAxe);
					}
					saveToDisk(beanToSave, fileName);			
					log.debug("Added to Disk Cache: " + fileName);
				
					// Put the fileName is the diskCache
					diskCacheKeys.add(fileName);
				}
				catch (IOException e)
				{
					log.error("Failed to save Object to disk", e);
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
				log.debug( "accessionCode" + currentElement.elementAt(0) + " = " + currentElement.elementAt(1));
				if ( accessionCode.equalsIgnoreCase( (String) currentElement.elementAt(0) ) )
				{
					log.debug("Retrieved from memory Cache: " + accessionCode);
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
						log.debug("Retrieved from disk Cache: " + fileName);
						return bean;
					}
					catch (Exception e)
					{
						log.error("Failed to retrieve Object from disk", e);
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
