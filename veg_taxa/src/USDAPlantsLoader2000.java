import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTResultTarget;


/**
  * class that loads the 2002 usda plants data into the 
	* vegbank plants database which already contains the 
	* 1996 plants data set 
	*/
public class USDAPlantsLoader2000 
{
	
	private String styleSheet="../xmlfiles/plantsToNVC.xsl";
	private String attributeFile="attFile.txt";
	private Vector fileVector = new Vector();
	private Vector missingPlantNames = new Vector();
	private Hashtable missingNamesCache = new Hashtable(); // a cache so that a given name is not repeated
	private String authors ="USDA, NRCS";
	private String title = "The Plants Database";
	private String pubdate ="30-JUN-2002";
	private String edition  = "Version 3.5";
	private String othercitationdetails = "(http://plants.usda.gov) National Plant Data Center, Baton Rouge, LA 70874-4490 USA.";
	private int refId = 0;
	// this stores the data that are to be loaded to the concept table
	private Vector conceptTableValues = new Vector();
	private String dateEntered = "20-AUG-2002";
	private Vector statusTableValues = new Vector();
	private Vector usageTableValues = new Vector();
	private String organization = "USDA-NRCS-PLANTS-2002";
	private  int partyId = 1;
	private String email = "plants@plants.usda.gov";
	private String contactInstructions = "http://plants.usda.gov";
	private String year = "2002"; // this is the year that the list was from
	private static final String connectionString = "jdbc:postgresql://127.0.0.1/plants_dev";
	private static final String databaseUser = "datauser";
	private static final String jdbcDriver = "org.postgresql.Driver";
	/**
	 * This is where it happens -- the data are loaded from this 
	 * method
	 */
	public void loadPlantDataSet(String inputXml)
	{
		try 
 		{
			System.out.println("USDAPlantsLoader2000 > infile: " + inputXml);
			System.out.println("USDAPlantsLoader2000 > reading input xml  file ");
			this.transformToFile(inputXml);
			System.out.println("USDAPlantsLoader2000 > reading attribute file ");
			Vector v = this.getAttributeFile();
			
			System.out.println("USDAPlantsLoader2000 > parsing input file contents ");
			Hashtable plantsHash = this.elementParser(v);
			
			System.out.println("USDAPlantsLoader2000 > loading data to db ");
			////load new plant instances -- EXCLUDING THOSE WITH SYNONOMYS
			loadPlantInstances(plantsHash);
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	
 /**
 	* method to load the plant instances, stored in a 
 	* hashtable, to the database 
 	*/	
	private void loadPlantInstances(Hashtable plantsData)
	{
		try
		{
			int plantNumber = plantsData.size();
			System.out.println("USDAPlantsLoader2000 > number plant instances: " + plantNumber );
			Connection conn = this.getConnection();
			
			//remove any known temporary database objects 
			this.removeTempDBObjects();
			
			// create the indicies on the plant name table
			this.createPlantNameIndex();
			//insert the reference and party info 
			this.insertPartyReference();
			

			
			// create the missing plants and the concept data vector
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				String plantCode=plantInstanceHash.get("plantCode").toString();
				String concatenatedName= plantInstanceHash.get("concatenatedName").toString();
				String concatenatedLongName = plantInstanceHash.get("concatenatedLongName").toString();
				String rank= plantInstanceHash.get("rank").toString();
				String parentName=plantInstanceHash.get("parentName").toString();
				String plantConceptStatus=plantInstanceHash.get("itisUsage").toString();
				String synonymousName=plantInstanceHash.get("synonymousName").toString();
				String familyName=plantInstanceHash.get("familyName").toString();
				String commonName=plantInstanceHash.get("commonName").toString();
				
				// now get the associated primary key value for the sciname
				String s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+concatenatedName+ "' ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				ResultSet rs = pstmt.executeQuery();
				// if the plant is missing then add it to the list of missing names
				if ( rs.first()  == false )
				{
					System.out.println("USDAPlantsLoader2000 >>> not found: " + concatenatedName);
					// add the names to those that are missing
					if ( missingNamesCache.containsKey(concatenatedName) == false )
					{
						this.missingPlantNames.add(concatenatedName+"|"+concatenatedLongName+"|scientific");
						this.missingNamesCache.put(concatenatedName, concatenatedLongName);
					}
				}

				pstmt.close();
				
				//do the same for the code 
				s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+plantCode+ "' ";
				pstmt = conn.prepareStatement(s);
				rs = pstmt.executeQuery();
				// if the plant is missing then add it to the list of missing names
				if ( rs.first()  == false )
				{
					System.out.println("USDAPlantsLoader2000 >>> not found: " + plantCode);
					// add the names to those that are missing
					if ( missingNamesCache.containsKey(plantCode) == false )
					{
							this.missingPlantNames.add(plantCode+"|"+concatenatedLongName+"|code");
							this.missingNamesCache.put(plantCode, concatenatedLongName);
					}
				}
				
				//do the same for the commonname -- but only if there is one
				if (commonName.length() > 0 )
				{
					s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+commonName+ "' ";
					pstmt = conn.prepareStatement(s);
					rs = pstmt.executeQuery();
					// if the plant is missing then add it to the list of missing names
					if ( rs.first()  == false )
					{
						System.out.println("USDAPlantsLoader2000 >>> not found: " + commonName);
						// add the names to those that are missing
						if ( missingNamesCache.containsKey(commonName) == false )
					{
						this.missingPlantNames.add(commonName+"|"+concatenatedLongName+"|common");
						this.missingNamesCache.put(commonName, concatenatedLongName);
					}
					}
				}
				
			}
			
			// now insert the missing plant names 
			System.out.println("USDAPlantsLoader2000 > inserting missing plant names : " + this.missingPlantNames.size()  );
			for (int i=0; i<missingPlantNames.size(); i++) 
			{
				StringTokenizer st = new StringTokenizer((String)missingPlantNames.elementAt(i), "|");
				String name = st.nextToken();
				// FIXME: Should be add elsewhere
				String longName = st.nextToken();
				String nameType = st.nextToken(); // this is for the second step
				
				PreparedStatement pstmt = conn.prepareStatement(
					"insert into PLANTNAME (PLANTREFERENCE_ID, PLANTNAME, "
					+ " DATEENTERED ) values(?,?,?) " );
					
				pstmt.setInt(1, this.refId);
				pstmt.setString(2, name);
				pstmt.setString(3, dateEntered);
				pstmt.execute();
				pstmt.close();
			}
			
			// now that all the new plant names have been inserted construct the vector
			// which contains the plantconcept data
			// create the missing plants and the concept data vector
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				String plantCode=plantInstanceHash.get("plantCode").toString();
				String concatenatedName= plantInstanceHash.get("concatenatedName").toString();
				String concatenatedLongName = plantInstanceHash.get("concatenatedLongName").toString();
				String rank= plantInstanceHash.get("rank").toString();
				String parentName=plantInstanceHash.get("parentName").toString();
				String plantConceptStatus=plantInstanceHash.get("itisUsage").toString();
				String synonymousName=plantInstanceHash.get("synonymousName").toString();
				String familyName=plantInstanceHash.get("familyName").toString();
				String commonName=plantInstanceHash.get("commonName").toString();
				
				// now get the associated primary key value for the sciname
				String s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+concatenatedName+ "' ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				ResultSet rs = pstmt.executeQuery();
				// if the plant is missing then add it to the list of missing names
				if ( rs.first()  == false )
				{
					System.out.println("USDAPlantsLoader2000 >>> not found should not happen ever : " + concatenatedName);
					// add the names to those that are missing
					//this.missingPlantNames.add(concatenatedName+"|"+concatenatedLongName+"|scientific");
				}
				// if it is there set the plantname id in the concept table which should only include the sci name
				else
				{
					rs.first();
					int plantId = rs.getInt(1);
					conceptTableValues.add(plantId+"|"+concatenatedName+"|"+plantCode+"|"+rank+"|"+parentName+"|"+plantConceptStatus);
				}
				pstmt.close();
			}
			
			
			// now insert the concept information
			System.out.println("USDAPlantsLoader2000 > inserting new concepts : " + this.conceptTableValues.size()  );
			for (int i=0; i<this.conceptTableValues.size(); i++) 
			{
				StringTokenizer st = new StringTokenizer((String)conceptTableValues.elementAt(i), "|");
				String plantId = st.nextToken();
				String concatName = st.nextToken();
				String code = st.nextToken();

				
				StringBuffer sb = new StringBuffer();
				sb.append(" insert into PLANTCONCEPT (PLANTNAME_ID, PLANTREFERENCE_ID, PLANTNAME, ");
				sb.append(" PLANTDESCRIPTION,  PLANTCODE) ");
				sb.append(" values (?,?,?,?,?)");
				PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
				pstmt.setString(1, plantId);
				pstmt.setInt(2, this.refId);
				pstmt.setString(3, concatName);
				pstmt.setString(4, concatName);
				pstmt.setString(5, code);
				
				pstmt.execute();
				pstmt.close();	
			}
			
			//create the indicies for query perf in concept
			this.createPlantConceptIndex();
			
			//create the vector with the status values 
			System.out.println("USDAPlantsLoader2000 > creating status load values: " + this.conceptTableValues.size()  );
			for (int i=0; i<this.conceptTableValues.size(); i++) 
			{
				StringTokenizer st = new StringTokenizer((String)conceptTableValues.elementAt(i), "|");
				String plantId = st.nextToken();
				String concatName = st.nextToken();
				String code = st.nextToken();
				String level = st.nextToken();
				String parent = st.nextToken();
				String status = st.nextToken();
				
				StringBuffer sb = new StringBuffer();
				sb.append("select PLANTCONCEPT_ID from PLANTCONCEPT where ");
				sb.append(" PLANTNAME_ID =  " + plantId+ " and ");
				sb.append(" PLANTREFERENCE_ID = " + this.refId );
				
				PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
				ResultSet rs = pstmt.executeQuery();
				rs.first();
				int conceptId = rs.getInt(1);
				this.statusTableValues.add( conceptId+"|"+status+"|"+parent+"|"+level);
				pstmt.close();	
			}
			
			
			//insert the status data
			System.out.println("USDAPlantsLoader2000 > inserting status values: " + this.statusTableValues.size()  );
			for (int i=0; i<this.conceptTableValues.size(); i++)
			{
				StringTokenizer st = new StringTokenizer((String)statusTableValues.elementAt(i), "|");
				String conceptId = st.nextToken();
				String status = st.nextToken();
				String parentname = st.nextToken();
				String level = st.nextToken();
				
				StringBuffer sb = new StringBuffer();
				sb.append(" insert into PLANTSTATUS (PLANTCONCEPT_ID, PLANTREFERENCE_ID, PLANTPARTY_ID, ");
				sb.append(" PLANTCONCEPTSTATUS, PLANTPARENTNAME, startdate, PLANTLEVEL) ");
				sb.append(" values (?,?,?,?,?,?,?)");
				PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
				pstmt.setString(1, conceptId);
				pstmt.setInt(2, this.refId);
				pstmt.setString(3, "1");
				pstmt.setString(4, status);
				pstmt.setString(5, parentname);
				pstmt.setString(6, this.dateEntered);
				pstmt.setString(7, level);
				pstmt.execute();
				pstmt.close();	
			}

			//generate the usage vector 
			System.out.println("USDAPlantsLoader2000 > creating usage values: " + this.conceptTableValues.size()  );
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				String plantCode=plantInstanceHash.get("plantCode").toString();
				String concatenatedName= plantInstanceHash.get("concatenatedName").toString();
				String concatenatedLongName = plantInstanceHash.get("concatenatedLongName").toString();
				String rank= plantInstanceHash.get("rank").toString();
				String parentName=plantInstanceHash.get("parentName").toString();
				String plantConceptStatus=plantInstanceHash.get("itisUsage").toString();
				String synonymousName=plantInstanceHash.get("synonymousName").toString();
				String familyName=plantInstanceHash.get("familyName").toString();
				String commonName=plantInstanceHash.get("commonName").toString();
				
				// now get the associated primary key value for the sciname
				String s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+concatenatedName+ "' ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				ResultSet rs = pstmt.executeQuery();
				rs.first();
				int plantNameId = rs.getInt(1); 
				pstmt.close();
				rs.close();
				
				// get the concept for the plant -- using the sci name 
				StringBuffer sb = new StringBuffer();
				sb.append("select PLANTCONCEPT_ID from PLANTCONCEPT where ");
				sb.append(" PLANTNAME_ID =  " + plantNameId+ " and ");
				sb.append(" PLANTREFERENCE_ID = " + this.refId );
				
				pstmt = conn.prepareStatement(sb.toString() );
				rs = pstmt.executeQuery();
				int conceptId = 0;
				if ( rs.first() == true )
				{
					conceptId = rs.getInt(1);
				}
				
				if ( conceptId != 0) 
				{
				
					this.usageTableValues.add(plantNameId+"|"+concatenatedName+"|"+conceptId+"|"+synonymousName+"|"+plantConceptStatus+"|SCIENTIFIC NAME");
				
					//now get the id's for the codes and common names
					s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+plantCode+ "' ";
					pstmt = conn.prepareStatement(s);
					rs = pstmt.executeQuery();
					rs.first();
					plantNameId = rs.getInt(1);
					pstmt.close();
					rs.close();
					this.usageTableValues.add(plantNameId+"|"+plantCode+"|"+conceptId+"|"+synonymousName+"|"+plantConceptStatus+"|CODE");
				
					//now get the id's for the common names
					if ( commonName.length() > 2 )
					{
						s = "select PLANTNAME_ID from PLANTNAME where PLANTNAME = '"+commonName+ "' ";
						pstmt = conn.prepareStatement(s);
						rs = pstmt.executeQuery();
						rs.first();
						plantNameId = rs.getInt(1);
						pstmt.close();
						rs.close();
						this.usageTableValues.add(plantNameId+"|"+commonName+"|"+conceptId+"|"+synonymousName+"|"+plantConceptStatus+"|COMMON NAME");
					}
				}
			}
			
			// insert the data to the usage table
			System.out.println("USDAPlantsLoader2000 > inserting usage values: " + this.usageTableValues.size()  );
			for (int i=0; i<this.usageTableValues.size(); i++)
			{
				String line = (String)usageTableValues.elementAt(i);
				StringTokenizer st = new StringTokenizer(line, "|");
				//System.out.println("USDAPlantsLoader2000 > line: " + line); 
				
				String nameId = st.nextToken();
				String name = st.nextToken();
				String conceptId = st.nextToken();
				String synonym = st.nextToken();
				String status = st.nextToken();
				String classSystem = st.nextToken();
				
				StringBuffer sb = new StringBuffer();
				sb.append(" insert into PLANTUSAGE (PLANTNAME_ID, PLANTCONCEPT_ID, PLANTNAME, PLANTPARTY_ID, ");
				sb.append(" USAGESTART, acceptedsynonym, plantnamestatus, classsystem ) ");
				sb.append(" values (?,?,?,?,?,?,?,?)");
				PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
				pstmt.setString(1, nameId);
				pstmt.setString(2, conceptId);
				pstmt.setString(3, name);
				pstmt.setInt(4, this.partyId);
				pstmt.setString(5, this.dateEntered);
				pstmt.setString(6, synonym );
				pstmt.setString(7, status );
				pstmt.setString(8, classSystem );
				pstmt.execute();
				pstmt.close();	
			}
			
			//create the temporary correlation table
			this.createTempCorrelationTable();
			
			//create indicies related to correlation performance
			this.createCorrelationPerfIndicies();
			
			// insert into the temp the correlation table
			System.out.println("USDAPlantsLoader2000 > inserting correlations: "  );
			System.out.println("USDAPlantsLoader2000 > inserting to temp-correlation table " );
			for (int i=0; i<this.usageTableValues.size(); i++)
			{
				String line = (String)usageTableValues.elementAt(i);
				StringTokenizer st = new StringTokenizer(line, "|");
				
				
				String nameId = st.nextToken();
				String name = st.nextToken();
				String conceptId = st.nextToken();
				String synonym = st.nextToken();
				String status = st.nextToken();
				String classSystem = st.nextToken();
				
				 
				// only if they are scientific names and are not standard 
				if ( status.startsWith("not")  &&  classSystem.toUpperCase().startsWith("SCI")  )
				{
						StringBuffer sb = new StringBuffer();
						sb.append(" insert into TEMP_CORRELATION (PAST_PLANTCONCEPT_ID, PLANTCONVERGENCE, CORRELATIONSTART,  ");
						sb.append("  SYNONYM ) ");
						sb.append(" values (?,?,?,?)");
						PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
						pstmt.setString(1, conceptId);
						pstmt.setString(2, "UNKNOWN");
						pstmt.setString(3, this.dateEntered);
						//pstmt.setInt(4, statusId);
						pstmt.setString(4, synonym);
						pstmt.execute();
						pstmt.close();
				}
			}
			//update the values in this table 
			this.updateTempCorrelationTable();
			// migrate the values from the tempoarary table 
			System.out.println("migrating correlation data");
			this.migrateCorrelationData();
			
			
			// update the parent data in the concept table -- this can be left out 
			System.out.println("updating parent concepts");
///			this.updateParentConcept(plantsData);
			this.updateParentConcept();
			
			
			// if the year is 2002 then update the 1996 data that is already 
			// in the database 
			System.out.println("updating the existing 1996 data ");
			if ( this.year.equalsIgnoreCase("2002") )
			{
				this.update1996Data();
			}
			
			
			//remove the temp correlation table 
			//this.dropTempCorrelationTable();
			// remove the plantname index 
			this.dropPlantNameIndex();
			// drop the index 
			this.dropPlantConceptIndex();
		}
		catch (Exception e)
	  {
		 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
		 e.printStackTrace();
		// System.exit(0);
	  }
	}
	
	/**
	 * method that removes the known temporary objects 
	 */
	 private void removeTempDBObjects()
	 {
		 try
		 {
			 	System.out.println("USDAPlantsLoader2000 > removing any known db temporary objects ");
			 	Connection conn = this.getConnection();
			 	StringBuffer sb = new StringBuffer();
				PreparedStatement pstmt = null;
			 
			 	System.out.println(" USDAPlantsLoader2000 > dropping the temp_correlation table ");
			 	if ( ! this.databaseEntityExists("temp_correlation", "table"))
			 	{
			 		sb.append("drop table temp_correlation");
			 		pstmt = conn.prepareStatement(sb.toString());
			 		pstmt.execute();
			 		pstmt.close();
			 	}
			 
			 	System.out.println("USDAPlantsLoader2000 > dropping the temp_parentconcepts table ");
			 	sb = new StringBuffer();
				if ( ! this.databaseEntityExists("temp_parentconcepts", "table"))
				{
			 		sb.append("drop table temp_parentconcepts");
			 		pstmt = conn.prepareStatement(sb.toString());
			 		pstmt.execute();
			 		pstmt.close();
				}
				
			 	System.out.println("USDAPlantsLoader2000 > dropping some indicies tmp_concept_dual ");
			 	sb = new StringBuffer();
			 	if ( ! this.databaseEntityExists("tmp_concept_dual", "index"))
			 	{
			 		sb.append("drop index tmp_concept_dual ");
			 		pstmt = conn.prepareStatement(sb.toString());
			 		pstmt.execute();
			 		pstmt.close();
			 		conn.close();
			 	}
		 
		 }
		 catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	
	/**
	 * method that updates the 1996 data that are already stored in the 
	 * vegbank system.  These updates include:
	 *
	 */
	 private void update1996Data()
	 {
		 try
		 {
			 System.out.println("USDAPlantsLoader2000 > updating the 1996 data");
	 
			 System.out.println("updating the correlations ");
			 Connection conn = this.getConnection();
			 StringBuffer sb = new StringBuffer();
			 //-- first update the 1996 correlations 
			 sb.append("update PLANTCORRELATION set correlationstop = '10-JUN-2002'");
			 PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			 pstmt.execute();
			 pstmt.close();
			 
			 String tableName = "cross_year_correlations";
				if ( this.tableExists(tableName) )
				{
					System.out.println("USDAPlantsLoader2000 > Table "+tableName+" already exists");					
				}
				else
				{
					System.out.println("creating a table ");					
					//create the table
					sb = new StringBuffer();
					sb.append("create table cross_year_correlations");
					sb.append("(");
					sb.append("PLANTSTATUS_ID integer,");
					sb.append("PAST_PLANTCONCEPT_ID integer,");
					sb.append("PLANTCONCEPT_ID integer,");
					sb.append("plantCode varchar (20) ");
					sb.append(")");
					pstmt = conn.prepareStatement(sb.toString());
					pstmt.execute();
					pstmt.close();
				}
			 
			 	System.out.println("inserting the base 1996 - 2002 correlation data ");
			 	//insert the base data 
			 	sb = new StringBuffer();
			 	sb.append("insert into cross_year_correlations ( PLANTSTATUS_ID,     PAST_PLANTCONCEPT_ID) "); 
			 	sb.append("select  PLANTSTATUS_ID, PLANTCONCEPT_ID from plantstatus where plantreference_id = 1");
			 	pstmt = conn.prepareStatement(sb.toString());
			 	pstmt.execute();
			 	pstmt.close();
			 
			 	System.out.println("updating the correlation data ");
			 	//update the plantcodes
			 	sb = new StringBuffer();
			 	sb.append("update  cross_year_correlations set plantCode = ");
			 	sb.append("(select plantcode from plantconcept where plantconcept.plantconcept_id = cross_year_correlations.PAST_PLANTCONCEPT_ID)");
			 	pstmt = conn.prepareStatement(sb.toString());
			 	pstmt.execute();
			 	pstmt.close();
			 
			 	/////////////////////////
			 	System.out.println("making some performance indexes");
			 	//create the indicies
			 	sb = new StringBuffer();
			 	String entityType = "index";
			 	
				String indexName = "tmp_code_concept";			
				if ( !databaseEntityExists(indexName, entityType ) )
					sb.append("create index " + indexName + " on plantconcept (plantCode);");

				indexName = "tmp_ref_concept";			
				if ( !databaseEntityExists(indexName, entityType ) )
					sb.append("create index " + indexName + " on plantconcept(plantreference_id);");
				
				indexName = "tmp_dual_concept";			
				if ( !databaseEntityExists(indexName, entityType ) )
					sb.append("create index " + indexName + " on plantconcept(plantCode,plantreference_id );");

				indexName = "tmp_code_crsscorre";			
				if ( !databaseEntityExists(indexName, entityType ) )
					sb.append("create index " + indexName + " on cross_year_correlations (plantCode);");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.execute();
				pstmt.close();
				//////////////////////////////
				 
				 System.out.println("updating the concept id's for the new correlations ");
				 //update the plantconcept
				 sb = new StringBuffer();
				 sb.append("update cross_year_correlations set PLANTCONCEPT_ID = ");
				 sb.append("(select max(plantconcept_id) from plantconcept where plantconcept.plantcode = cross_year_correlations.plantCode and plantconcept.plantreference_id = 2)");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 System.out.println("deleting any nulls ");
				 //delete any nulls
				 sb = new StringBuffer();
				 sb.append("delete from cross_year_correlations where plantconcept_id = null");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 System.out.println("migrating the data ");
				 //migrate the correlation data 
				 sb = new StringBuffer();
				 sb.append("insert into PLANTCORRELATION (PLANTSTATUS_ID, PLANTCONCEPT_ID, PLANTCONVERGENCE, CORRELATIONSTART)");
				 sb.append("select PLANTSTATUS_ID, PLANTCONCEPT_ID, 'UNKNOWN', '10-JUN-2002'");
				 sb.append("from cross_year_correlations where plantstatus_id > 0 and plantconcept_id > 0");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 System.out.println("setting the plantstatus index on the reference ");
					if ( !databaseEntityExists("tmp_status_dualb", "index" ) )
					{
						String s = " create index tmp_status_dualb on plantstatus (  plantreference_id, plantstatus_id ); ";
					 	pstmt = conn.prepareStatement(s);
					 	pstmt.execute();
					 	pstmt.close();
					}
				 
				 System.out.println("setting the plantstatus stop date to 2002 ");
				 sb = new StringBuffer();
				 sb.append(" update plantstatus set stopdate = '20-JUN-2002' where plantreference_id = 1 and plantstatus_id > 0 ; ");
				 //sb.append("update plantstatus set plantpartycomments  = 'supperceeded by 2002 data' where plantreference_id != "+this.refId+";");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 conn.close();
			 
		 }
		 catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
	 }
	 
	
	/**
	  * method to update the parent concept attributes 
		*/
		 private void updateParentConcept()
		 {
			 try
			 {
				 Connection conn = this.getConnection();
				 StringBuffer sb = new StringBuffer();
				 sb.append("create table temp_parentconcepts (");
				 sb.append("child_id integer,");
				 sb.append("childname varchar(230),");
				 sb.append("parent_id  integer,");
				 sb.append("parentname varchar(230),");
				 sb.append("plantreference_id integer");
				 sb.append(");");
				 PreparedStatement pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 // insert the base information and then send the updates
				 sb = new StringBuffer();
				 sb.append("insert into TEMP_PARENTCONCEPTS (CHILD_ID, PARENTNAME, PARENT_ID, plantreference_id) ");
				 sb.append("select PLANTSTATUS.PLANTCONCEPT_ID, PLANTSTATUS.PLANTPARENTNAME, PLANTCONCEPT.PLANTCONCEPT_ID, PLANTCONCEPT.plantreference_id where ");
				 sb.append("PLANTSTATUS.PLANTPARENTNAME = PLANTCONCEPT.PLANTNAME and PLANTSTATUS.PLANTREFERENCE_ID = "+this.refId+"  ; ");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 // get the child name 
				 sb = new StringBuffer();
				 sb.append("update temp_parentconcepts set childname = ( ");
				 sb.append("select plantname from plantconcept where plantconcept.plantconcept_id = temp_parentconcepts.child_id); ");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 // delete some of the entries -- also delete the concepts that are not relevant to the 
				 // current reference
				 sb = new StringBuffer();
				 sb.append("delete from  temp_parentconcepts where parent_id  =  child_id; ");
				 sb.append("delete from  temp_parentconcepts where plantreference_id  != "+this.refId+"; ");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 // now migrate the values
				 sb = new StringBuffer();
				 sb.append(" CREATE INDEX tmp_child ON  temp_parentconcepts (child_id );");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
				 
				 sb = new StringBuffer();
				 sb.append(" update plantstatus set plantparent = ");
				 sb.append(" ( select max(parent_id) from temp_parentconcepts where temp_parentconcepts.child_id = plantconcept.plantconcept_id ); ");
				 pstmt = conn.prepareStatement(sb.toString());
				 pstmt.execute();
				 pstmt.close();
			 }
			 catch (Exception e)
			 {
				 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
				 e.printStackTrace();
				 // System.exit(0);
			 }
		 }
		 
		 
	/**
	 * method to update the values in the temp correlation table
	 */
	private void updateTempCorrelationTable()
	{
		///Vector statusIdVec = new Vector();
		///Vector conceptIdVec = new Vector();
		///Vector targetConceptIdVec = new Vector();
		 try
		 {
			 	Connection conn = this.getConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE  temp_correlation  SET plantstatus_id  = ");
				sb.append(" ( select max(plantstatus_id) from plantstatus where ");
				sb.append(" plantstatus.plantconcept_id = temp_correlation.past_plantconcept_id ");
				sb.append(" and plantstatus.plantreference_id = "+this.refId+");");
				PreparedStatement pstmt = conn.prepareStatement(sb.toString());
				pstmt.execute();
				pstmt.close();
				
				sb = new StringBuffer();
				sb.append(" UPDATE  temp_correlation  SET plantconcept_id  = ");
				sb.append(" ( select max(plantconcept_id)  from plantconcept  where ");
				sb.append(" plantconcept.plantname = temp_correlation.synonym and plantconcept.plantreference_id = "+this.refId+"); ");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.execute();
				pstmt.close();
				conn.close();
		 }
		 	catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private void createCorrelationPerfIndicies()
	{
		Connection conn = this.getConnection();
		String entityType = "index";
		String indexName = "";	
		StringBuffer sb = new StringBuffer();
		
		try
		{
			indexName = "TMP_CONCEPT_DUAL";
			if ( !databaseEntityExists(indexName, entityType) )
				sb.append( "create index " + indexName + " on plantconcept (PLANTNAME, PLANTREFERENCE_ID);" );

			indexName = "TMP_STATUS_DUAL";
			if ( !databaseEntityExists(indexName, entityType) )			
				sb.append( "create index " + indexName + " on plantstatus (PLANTCONCEPT_ID, PLANTCONCEPTSTATUS);" );

			// This is NOT currently used
			//indexName = "TMP_CORRCON";
			//if ( !databaseEntityExists(indexName, entityType) )			
			//	String s2 = " create index " + indexName = " on temp_correlation (PAST_PLANTCONCEPT_ID)";

			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.execute();
			pstmt.close();
			conn.close();
		}
		catch (Exception e)
	  {
		 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
		 e.printStackTrace();
	  }
	}
	
	
	/**
	 * method that migrates the data from the temporary table into 
	 * the correlation table
	 */
	 private void migrateCorrelationData()
	 {
		try
		{
			 Connection conn = this.getConnection();
			 StringBuffer sb = new StringBuffer();
			 
			 //create an index
			 sb.append("create index bad on temp_correlation  (PLANTSTATUS_ID, PLANTCONCEPT_ID, PLANTCONVERGENCE, CORRELATIONSTART, CORRELATIONSTOP)");
			 PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
			 pstmt.execute();
			 pstmt.close();
			 
			 sb = new StringBuffer();
			 sb.append("insert into  PLANTCORRELATION (PLANTSTATUS_ID, PLANTCONCEPT_ID, PLANTCONVERGENCE, CORRELATIONSTART, CORRELATIONSTOP)  ");
			 sb.append(" select PLANTSTATUS_ID, PLANTCONCEPT_ID, PLANTCONVERGENCE, CORRELATIONSTART, CORRELATIONSTOP ");
			 sb.append(" from temp_correlation where plantstatus_id > 0 and plantconcept_id > 0");
			
			 pstmt = conn.prepareStatement(sb.toString() );
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
			 
		}
		catch (Exception e)
	  {
		 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
		 e.printStackTrace();
		 System.exit(0);
	  }
	 }
	
	/**
	 * method that creates an index in the plantname party -- 
	 * specifically on the plantname attribute for qury perfomance
	 */
	 private void createTempCorrelationTable()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 StringBuffer sb = new StringBuffer();
			 
			 sb.append("create table TEMP_CORRELATION ( ");
			 sb.append("PLANTSTATUS_ID integer,");
			 	sb.append("PAST_PLANTCONCEPT_ID integer,");
			 sb.append("PLANTCONCEPT_ID integer,");
			 sb.append("plantConvergence varchar (20),");
			 sb.append("correlationStart timestamp,");
			 sb.append("correlationStop timestamp, ");
			 sb.append("synonym varchar (200) ");
			 sb.append(")");
			 
			 PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	 
	
	
	/**
   * method that removes the temporary correlation 
	 * table
	 */
	 private void dropTempCorrelationTable()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 StringBuffer sb = new StringBuffer();
			 sb.append("drop table TEMP_CORRELATION ");
			 PreparedStatement pstmt = conn.prepareStatement(sb.toString() );
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	 
	 
	
	/**
	 * method that creates an index in the plantname party -- 
	 * specifically on the plantname attribute for qury perfomance
	 */
	 private void createPlantConceptIndex()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 String s = "create index TMP_CONCEPT_IDX on PLANTCONCEPT (plantreference_id, plantname_id) ";
			 PreparedStatement pstmt = conn.prepareStatement(s);
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	 
	
	/**
	 * method that drops the index in the plantname party -- 
	 * specifically on the plantname attribute for qury perfomance
	 */
	 private void dropPlantConceptIndex()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 String s = "drop index TMP_CONCEPT_IDX ";
			 PreparedStatement pstmt = conn.prepareStatement(s);
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	
	/**
	 * method that creates an index in the plantname party -- 
	 * specifically on the plantname attribute for qury perfomance
	 */
	 private void createPlantNameIndex()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 String s = "create index TMP_NAME_IDX on PLANTNAME (PLANTNAME) ";
			 PreparedStatement pstmt = conn.prepareStatement(s);
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	 
	
	
	/**
	 * method that drops the index in the plantname party -- 
	 * specifically on the plantname attribute for qury perfomance
	 */
	 private void dropPlantNameIndex()
	 {
		 try
		 {
			 Connection conn = this.getConnection();
			 String s = "drop index TMP_NAME_IDX ";
			 PreparedStatement pstmt = conn.prepareStatement(s);
			 pstmt.execute();
			 pstmt.close();
			 conn.close();
		 }
		 catch (Exception e)
		 {
			System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			e.printStackTrace();
		 }
	 }
	
	
	
	/**
	 * method that inserts the data into the plant party and 
	 * reference tables
	 */
	 private void insertPartyReference()
	 {
		 StringBuffer sb = new StringBuffer();
		 try
		 {
			 Connection conn = this.getConnection();
			 sb.append("select PLANTREFERENCE_ID from PLANTREFERENCE where ");
			 sb.append( "AUTHORS like '"+this.authors+"'");
			 sb.append(" and EDITION like '"+this.edition+"'");
			 PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			 ResultSet rs = pstmt.executeQuery();
			 // check to see that we got one row at least 
			 if ( rs.first()  == true )
			 {
				System.out.println("USDAPlantsLoader2000 > reference exists ");
				refId = rs.getInt(1);
			 }
			 else
			 {
				 StringBuffer sb1 = new StringBuffer();
				 sb1.append("INSERT into PLANTREFERENCE (OTHERCITATIONDETAILS, AUTHORS, TITLE, ");
				 sb1.append(" PUBDATE, EDITION) ");
				 sb1.append(" values(?,?,?,?,?)");
				 
				 pstmt = conn.prepareStatement( sb1.toString() );
				 pstmt.setString(1, this.othercitationdetails);
				 pstmt.setString(2, this.authors);
				 pstmt.setString(3, this.title);
				 pstmt.setString(4, this.pubdate);
				 pstmt.setString(5, this.edition);
				 pstmt.execute();
				 pstmt.close();
				 //now get the refid 
				 pstmt = conn.prepareStatement(sb.toString());
				 rs = pstmt.executeQuery();
				 rs.first();
				 refId = rs.getInt(1);
				 
			 }
			 
			 // get the party information 
			 sb = new StringBuffer();
			 conn = this.getConnection();
			 sb.append("select PLANTPARTY_ID from PLANTPARTY where ");
			 sb.append( "ORGANIZATIONNAME like '"+this.organization+"'");
			 pstmt = conn.prepareStatement(sb.toString());
			 rs = pstmt.executeQuery();
			 // check to see that we got one row at least 
			 if ( rs.first()  == true )
			 {
				System.out.println("USDAPlantsLoader2000 > party exists ");
				refId = rs.getInt(1);
			 }
			 // else insert it and get it
			 else
			 {
				 StringBuffer  sb1 = new StringBuffer();
				 sb1.append("INSERT into PLANTPARTY (ORGANIZATIONNAME, EMAIL, CONTACTINSTRUCTIONS) ");
				 sb1.append(" values(?,?,?)");
				 
				 pstmt = conn.prepareStatement( sb1.toString() );
				 pstmt.setString(1, this.organization);
				  pstmt.setString(2, this.email);
					 pstmt.setString(3, this.contactInstructions);
				 pstmt.execute();
				 pstmt.close();
				 //now get the refid 
				 pstmt = conn.prepareStatement(sb.toString());
				 rs = pstmt.executeQuery();
				 rs.first();
				 this.partyId = rs.getInt(1);
				 
			 }
			 
			 
		 }
		 catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader2000 > Exception: " + e.getMessage() );
			 e.printStackTrace();
			 System.exit(0);
		 }
	 }
	


	/**
	* method that will return a database connection for use with the database
	*
	* @return conn -- an active connection
	*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName(jdbcDriver);
			c = DriverManager.getConnection(connectionString, databaseUser, "");	
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
	
	/**
 	* method to extract a single plant instance from the 
 	* aggregate hashtable - returning a hashtable with keys 
 	* like name, author orignDate etc
 	*/	
	private Hashtable extractSinglePlantInstance(Hashtable plantsData, int hashKeyNum)
	{
		Hashtable singlePlantInstance = new Hashtable();
		try
		{
			if ( plantsData.containsKey(""+hashKeyNum) )
			{
				singlePlantInstance=(Hashtable)plantsData.get(""+hashKeyNum);
				//System.out.println(singlePlantInstance.toString() );
			}
			// if it is not there then return a null
			else
			{
				singlePlantInstance = null;
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception " + e.getMessage() );
			System.out.println("on key: " + hashKeyNum);
			e.printStackTrace();
		}
		return(singlePlantInstance);
	}

	
/**
 * method that returns true if the input line from 
 * the vector is the first line of a plant entry
 */
public boolean plantEntryStart (String vectorLine)
{
	if (vectorLine != null && vectorLine.startsWith("tsnValue") )
	{
		return(true);
	}
	else
	{
		return(false);
	}
}
	
/**
 * method to tokenize elements from a pipe delimeted string
 */
public String pipeStringTokenizer(String pipeString, int tokenPosition)
{
	String token="";
	try
	{
		if (pipeString != null) 
		{
			StringTokenizer t = new StringTokenizer(pipeString.trim(), "|");
			int i=1;
			while ( i<=tokenPosition ) 
			{
					if ( t.hasMoreTokens() )
					{
						token=t.nextToken();
						i++;
					}
					else 
					{
						token="";
						i++;
					}
			}
			return(token);
		}
		else
		{
			return(token);
		}
	}
	catch (Exception e)
	{
		System.out.println("Exception parsing: " + e.getMessage() );
		System.out.println("Exception parsing: " + pipeString);
	}
	return(token);
}
	
 /**
 	* method that parses the elements from a vector
 	* containing the element names and element values
 	* into a hashtable structure whose elements can easily
 	* be extracted and then loaded to the database
 	*
 	* @param fileVector -- the file after being translated via the syle sheer
 	* @return plantHash the hashtable containg the plant attributes 
 	*/	
	private Hashtable elementParser(Vector fileVector)
	{
		int hashKey=0;  //the keys in the hash table are incremental integers
		Hashtable plantHash = new Hashtable();
		for (int i=0; i<fileVector.size(); i++) 
		{
			if ( fileVector.elementAt(i) != null)
			{
				if ( plantEntryStart(fileVector.elementAt(i).toString() ) == true )
				{
					//the key is a hack -- has to be a string
					plantHash.put(""+hashKey,  plantInstance(fileVector, i)  );
					//System.out.println("USDAPlantsLoader2000 > trsnaleted line: "+ fileVector.elementAt(i) );
					hashKey++;
				}
			}
		}
		//System.out.println(" SIZE OF HASH (number of plant instances):"+hashKey);
		return(plantHash);
	}


/**
 * method that returns a hashtable that contains the 
 * information, stored in a hashtable, for an instance
 * of a plant entry including the plantname, tsn val,
 * publication information etc; using as input a vector
 * containing the plant data for an entire aggregate of 
 * plants and an integer refering to the level that the 
 * information for that instance of the plant starts on
 */
public Hashtable plantInstance(Vector fileVector, int startLevel)
{
	Hashtable plantInstance = new Hashtable();
	for (int i=startLevel; i<fileVector.size(); i++) 
	{
		//if the first level then it is the tsn value
		if (i==startLevel) 
		{
			String tsnValue = pipeStringTokenizer(fileVector.elementAt(i).toString(), 2);
			//System.out.println("USDAPlantsLoader2000 > tsnValue: " + tsnValue);
			plantInstance.put("tsnValue", tsnValue );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("concatenatedName") )
		{
			plantInstance.put("concatenatedName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("concatenatedLongName") )
		{
			// this is the name with the author, which may have some pretty weird names 
			// so handle appropriately
			String s = pipeStringTokenizer(fileVector.elementAt(i).toString(), 2);
			plantInstance.put("concatenatedLongName", s );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("rank") )
		{
			plantInstance.put("rank", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("initialDate") )
		{
			plantInstance.put("initialDate", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("updateDate") )
		{
			plantInstance.put("updateDate", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("parentName") )
		{
			plantInstance.put("parentName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("authorName") )
		{
			plantInstance.put("authorName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("itisUsage") )
		{
			plantInstance.put("itisUsage", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("synonymousName") )
		{
			String p = pipeStringTokenizer(fileVector.elementAt(i).toString(), 2);
			if ( p.length() < 2)
			{
				p = " ";
			}
			plantInstance.put("synonymousName", p);
		}
		else if ( fileVector.elementAt(i).toString().startsWith("publicationName") )
		{
			plantInstance.put("publicationName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("commonName") )
		{
			String c = pipeStringTokenizer(fileVector.elementAt(i).toString(), 2);
			c = c.replace('\'', ' ');
			plantInstance.put("commonName", c );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("plantCode") )
		{
			plantInstance.put("plantCode", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("familyName") )
		{
			plantInstance.put("familyName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		
		
		//if hit the next instance break from the loop
		else if (fileVector.elementAt(i).toString().startsWith("tsnValue"))
		{
			break;
		}
	//	System.out.println(i);
	}
	return(plantInstance);
}


	/**
 	* method to transform the input xml file into a flat file that 
 	* contains the relevant elements from that xml file included 
 	* in the XSLT and then passes the contents of the file back
 	* as a vector
 	*/	
	private void transformToFile(String inputXml)
	throws java.io.IOException,
        java.net.MalformedURLException,
        org.xml.sax.SAXException
	{
		//obtain a interface to a new XSLTProcessor object.
		XSLTProcessor processor = XSLTProcessorFactory.getProcessor();

		// Have the XSLTProcessor processor object transform inputXML  to
		// StringWriter, using the XSLT instructions found in "*.xsl".
		//print to a file
		processor.process(new XSLTInputSource(inputXml), 
		new XSLTInputSource(styleSheet),
		new XSLTResultTarget(attributeFile));
	}
	
	/**
	 * method that reads the post-transformed data set
	 */
	 private Vector getAttributeFile()
	 {
		 try
		 {
			 	BufferedReader in = new BufferedReader(new FileReader(attributeFile), 8192);
				String s=null;
				while ((s=in.readLine()) != null) 
				{
					fileVector.addElement(s);
				}
				in.close();
		 	}
			catch( Exception e ) 
			{
				System.out.println(" Exception:  " + e.getMessage() );
				e.printStackTrace();
			}
			return(fileVector);
	 }
	
/**
 * method that sets the instance varibales to correspond to the 
 * plant list from the year entered to the main method -- this should
 * be called before the loading is to have commenced
 * @param year -- the year of the usda plants list
 */
 private void setPlantList(String year)
 {
	 try
	 {
		 // if the year is 1996 then set the parameters for that year 
		 // otherwise leave them the way that they are
		 if ( year.equalsIgnoreCase("1996") )
		 {
			 this.title = "The Plants Database 1996";
			 this.pubdate = "30-JUN-1996";
			 this.edition = "Plants 1996";
			 this.dateEntered = "30-JUN-1996";
			 this.organization = "USDA-NRCS-PLANTS-1996";
			 this.year = "1996";
		 }
	 }
	 catch( Exception e )
	 {
		 System.out.println(" Exception:  " + e.getMessage() );
		 e.printStackTrace();
	 }
 }
	
	
	/*
	 * Finds out if a table exits in the database ...
	 * This method is specific to postgres because it looks into the postgres
	 * table calles pg_tables. Similar tables exist in other databases so this
	 * method could be grown. Ideally, I would like to see it dissappear.
	 */
	private boolean tableExists(String tableName)
	{
		boolean retVal = false;
		Connection conn = this.getConnection();
		String s = "select tablename from pg_tables where tablename =  '" + tableName +"'" ;
		
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(s);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first() )
			{
				//System.out.println("---->" + rs.getString("tableName"));
				retVal = true;
			}
			else 
			{
				//System.out.println("----> Nothing here!!!");
				retVal = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retVal;
	}
	
	/**
	 * Find out if an entity exists in the database ... Specific to postgres
	 * because it looks into the postgres specific tables to check the existence
	 * of the entity of that name. Similar tables exist in other databases so this
	 * method could be grown. Ideally, I would like to see it dissappear.
	 * 
	 * @param entityName --  
	 * @param entityType -- like indexs, tables etc.
	 *  
	 * */
	private boolean databaseEntityExists(String entityName, String entityType)
	{
		boolean retVal = false;
		String tableToSearch = null;
		String fieldName = null;
		entityName = entityName.toLowerCase();
		
		// Lookup fieldName and tableToSearch given an specfic entityType 
		if ( entityType == "index")
		{
			tableToSearch = "pg_indexes";
			fieldName = "indexname";
		}
		else if (entityType == "table") 
		{
			tableToSearch = "pg_tables";
			fieldName = "tablename";
		}
		else
		{
			System.out.println("USDAPlantsLoader2000 > Invalid entityType given" + entityType);
			return false; // seems like a resonable default here
		}
		
		// Generate the sql query
		String s = "select " + fieldName + " from " + tableToSearch + " where " + fieldName +  " = '" + entityName +"'" ;
		System.out.println("--->" +s);
		
		Connection conn = this.getConnection();
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(s);
			ResultSet rs = pstmt.executeQuery();
			
			// Check if any results returned, results => entity exists ... I hope ;)
			if (rs.first() )
			{
				System.out.println("---->" + rs.getString( fieldName ));
				retVal = true;
			}
			else 
			{
				System.out.println("----> Nothing here!!!");
				retVal = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retVal;
	}
	
/**
 * Main method to run the Legacy Data Wizard requires only the xml file that
 * defines the relationship between the files comprising the data package
 */
public static void main(String[] args) 
{
	if (args.length != 2) 
	{
		System.out.println("Usage: java USDAPlantsLoader2000 [XML-file] [year] \n");
		System.exit(0);
	}
	
	else 
	{
		String inputXml=null;
		inputXml = args[0];
		String year = args[1];
		//call the method to convert the data package to xml
		USDAPlantsLoader2000 il =new USDAPlantsLoader2000();
		// set the instance varaibales to the values associated 
		// with the plants list from that year
		il.setPlantList(year);
		il.loadPlantDataSet(inputXml);
	}
}

}

