import java.io.IOException;
import java.io.*;
import java.util.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;
import java.sql.*;

//next classes are for the database connectivity
import LocalDbConnectionBroker.*;
import DbConnectionBroker.*;
import utility.*;
import issueStatement.*;

/**
 * 
 * this class is inteded to be used to load the itis data
 * from the itis xml format to the plan taxonomy database
 * that is used by the plots database for tracking names
 *
 * @Author John Harris
 * @Version April 2001
 */


public class USDAPlantsLoader2000 {

public String styleSheet="../lib/plantsToNVC.xsl";
public String attributeFile="attFile.txt";
public Vector fileVector = new Vector();
public Hashtable attributeHash = new Hashtable();
public Hashtable constraintHash = new Hashtable();

public String previousInstanceDate = "01-JAN-96";
public String startDate = "01-JAN-00";
public String stopDate = "01-JAN-50";
public String partyName = "USDA-PLANTS";
public String reference = "PLANTS96";

//constructor -- define as static the LocalDbConnectionBroker 
//so that methods called by this class can access the 'local' 
//pool of database connections
static LocalDbConnectionBroker lb;



/**
 * Main method to run the Legacy Data Wizard requires only the xml file that
 * defines the relationship between the files comprising the data package
 */
public static void main(String[] args) {
	if (args.length != 1) {
		System.out.println("Usage: java USDAPlantsLoader2000  [XML] \n");
		System.exit(0);
	}
	else {
		String inputXml=null;
		inputXml=args[0];
		//call the method to convert the data package to xml
		USDAPlantsLoader2000 il =new USDAPlantsLoader2000();  
		il.transformXmlData(inputXml);
	}
}

/**
 * method that steps thru the processes associated with 
 * transforming and loading the itis xml data
 * @param inputXml
 */
public void transformXmlData (String inputXml) 
{
	try {
		System.out.println(inputXml);
	
		//call the method to parse the xml file into appropriate attributes and
		// store them in appropriate objects
//		System.out.println(
//			elementParser(
//				transformToFile(inputXml)
//			).toString()
//		);
		
		//first initiate the connection pooling
		lb.manageLocalDbConnectionBroker("initiate");
		
		//update the 1996 data by ending the concepts and usages --
		//this will basically end the 
		updatePreviousDataInstance(previousInstanceDate, startDate);
		
		//load new plant instances -- EXCLUDING THOSE WITH SYNONOMYS
///		loadPlantInstances(
///			elementParser(
///				transformToFile(inputXml)
///			)
///		);

		//load the plant data when there is already related 
		//plant data that is loaded use this for loading the 
		//usda plants list from year 2000 after the 1996 data 
		//has already been loaded
		loadPlantInstancesUpdate(
			elementParser(
				transformToFile(inputXml)
			)
		);
		
		
		
		
		//purge the file vector
///		fileVector = new Vector();
		
		//load the plant INSTANCES WITH SYNONOMYS
///		loadPlantSynonym(
///			elementParser(
///				transformToFile(inputXml)
///			)
///		);
		
		//lastly destroy the connection pooling
		System.out.println(" attempting to destroy the connection pool");
		lb.manageLocalDbConnectionBroker("destroy");
		
	}
	catch( Exception e ) {
		System.out.println(" failed in: USDAPlantsLoader2000.transformXMLData "
		+e.getMessage() );
		e.printStackTrace();
	}
}


/**
 * method to transform the input xml file into a flat file that 
 * contains the relevant elements from that xml file included 
 * in the XSLT and then passes the contents of the file back
 * as a vector
 */	
private Vector transformToFile(String inputXml)
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

	//read the resulting file into the attribute vector
	BufferedReader in = new BufferedReader(new FileReader(attributeFile), 8192);
	String s=null;
	while ((s=in.readLine()) != null) 
	{
//		System.out.println(s);
		fileVector.addElement(s);
	}
	return(fileVector);
}



/**
 * method to load the plant synonym if 
 * it exists in the hash 
 */	
private void loadPlantSynonym(Hashtable plantsData)
{
	System.out.println("Size of plantsData hash: "+plantsData.size());
	for (int i=0; i<plantsData.size(); i++) {
		
		//if there is a synonymy associated with this
		//instance of a plant
		if (extractSinglePlantInstance(plantsData, i ).get("synonymousName").toString()
			!= "nullToken"
			)
		{
		//		System.out.println("there is a synonym associated with "+
		//			extractSinglePlantInstance(plantsData, i
		//			).get("synonymousName").toString()
		//		);
				//this will load the synonym that is required 
				loadSinglePlantSynonym( extractSinglePlantInstance(plantsData, i) );
		}
	}
}

/**
 * method that will update the plant instances from the 
 * preceeding plant instance load period. (ie, this method)
 * should be run if loading the 2000 plants and the desire is 
 * to update the 1996 plant instances.  Specifically the concept
 * date should be terminated, and the usage date should be 
 * terminated
 * 
 * @param previousInstanceDate -- the date that of the previous load 
 * instance
 * 
 * @param currentInstanceDate --  the date of the current instance
 *
 */

private void updatePreviousDataInstance(String previousInstanceDate,
	 String currentInstanceDate)
{
	//end the concepts having the corresponding end date
	endPlantConcept(previousInstanceDate, currentInstanceDate);
	//end too the plant usages
	endPlantUsage(previousInstanceDate, currentInstanceDate);
}


/**
 * method that ends the plant concepts having a start date
 * that corresponds to the input date
 */
private void endPlantConcept(String startDate, String endDate)
{
	System.out.println("ending concepts with a startdate: "+startDate);
	System.out.println(" assigning a stop date : "+endDate);
	
	//grab a connection from the pool that has already been created 
	Connection conn=null;
	Statement query=null;
	try
	{
		conn = lb.manageLocalDbConnectionBroker("getConn");
		query = conn.createStatement();
	}
	catch (Exception e) 
	{
		System.out.println("failed in the USDAPlantsLoader2000.endPlantConcept "
		+" getting a connection from the local pool"
		+ e.getMessage());
	}
	try 
	{
		query.executeUpdate("UPDATE PLANTCONCEPT set STOPDATE = '"+endDate+"' "
		+"WHERE STARTDATE ='"+startDate+"'");
		
		System.out.println( query.toString() );
		//clean up the connections and statements
		query.close();
	}
	catch (Exception e) 
	{
		System.out.println("failed in the USDAPlantsLoader2000.endPlantConcept "
		+ e.getMessage());
		e.printStackTrace();
	}
}


/**
 * method that ends the plant concepts having a start date
 * that corresponds to the input date
 */
private void endPlantUsage(String startDate, String endDate)
{
	//grab a connection from the pool that has already been created 
	Connection conn=null;
	Statement query=null;
	try
	{
		conn = lb.manageLocalDbConnectionBroker("getConn");
		query = conn.createStatement();
	}
	catch (Exception e) 
	{
		System.out.println("failed in the USDAPlantsLoader2000.endPlantConcept "
		+" getting a connection from the local pool"
		+ e.getMessage());
	}
	try 
	{
		query.executeUpdate("UPDATE PLANTUSAGE set STOPDATE = '"+endDate+"' "
		+"WHERE STARTDATE ='"+startDate+"'");
		
		System.out.println( query.toString() );
		//clean up the connections and statements
		query.close();
	}
	catch (Exception e) 
	{
		System.out.println("failed in the USDAPlantsLoader2000.endPlantConcept "
		+ e.getMessage());
		e.printStackTrace();
	}
}


/**
 * method to load an instance of a synonym into the 
 * plant taxa database -- the input single plant instance 
 * must contain information related to the synonomy, also
 * the both the plants (the plant that is the synonomy and
 * the plant that has the synonymous value) must already be 
 * in the database
 */	
private void loadSinglePlantSynonym(Hashtable singlePlantInstance)
{
	//veryify that the input hash table has a synonmyName element
	if (singlePlantInstance.get("synonymousName").toString() != null )
	{
		//System.out.println("from loadSinglePlantSynonym "+singlePlantInstance.get("synonymousName").toString());
		String concatenatedName
			= singlePlantInstance.get("concatenatedName").toString();
		String synonymousName
			= singlePlantInstance.get("synonymousName").toString();
		
		//check that the plant name exists
		if ( plantNameExists(concatenatedName) == true  && 
			plantNameExists(synonymousName) == true)
		{
			//the synonymous name is the name that the concatenated 
			//name is now known by
			int plantConceptId = plantConceptKey(synonymousName);
			System.out.println("KEY VAL for thet plant concept: "+plantConceptId);
		
			int plantNameId = plantNameKey(concatenatedName);
			
			//load the relevant data to the usage table
			loadPlantUsageInstance(concatenatedName, plantNameId, plantConceptId, 
			partyName, "SYNONYM", startDate, stopDate, reference);
	}
	else 
	{
		System.out.println("instance with synonomy does not exist");
	}
	}
}


/**
 * method that returns the primary key value
 * from the plant name table based on the 
 * input parameters
 */
private int plantNameKey(String plantName)
{
	int plantNameId = 0;
	String action="select";
	String statement="select plantName_id from plantname where plantname = '"
		+plantName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantname_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	if ( j.returnedValues.size() > 1 )
	{
		System.out.println("plantNameKey violation: there are more than one"
		+" keys returned");
	}
	//if there are no returned results
	else if ( j.returnedValues.size() == 0 )
	{
		System.out.println("no usage of "+plantName+" found in the palnt name table");
		return(0);
	}
	else 
	{
		//return the primary key value
		plantNameId = Integer.parseInt(
		j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	}
	//System.out.println("####"+ plantNameId );
	return(plantNameId);

}








/**
 * method that loads a new instance of a 
 * plant correlation  - name usage returns to 
 * the calling method the primary key for 
 * that concept - name usage
 */	
private int loadPlantCorrelationInstance(String concatenatedName, 
	int plantConceptStatusId, int plantConceptId, 
	String convergence, String startDate)
{
	String action="insert";
	String insertString="insert into plantCorrelation";
	String attributeString="plantName, plantConceptStatus_id, plantConcept_id, convergence, "
		+"startDate";	
	int inputValueNum=5;
	String inputValue[]=new String[5];
	inputValue[0]=""+concatenatedName;
	inputValue[1]=""+plantConceptStatusId;
	inputValue[2]=""+plantConceptId;
	inputValue[3]=""+convergence;
	inputValue[4]=""+startDate;
	
	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);
	String valueString = k.outValueString;

	issueStatement j = new issueStatement();
	j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);

	return(0);	
}







/**
 * method that returns the primary key value
 * from the plant concept key based on the 
 * input parameters
 */
private int plantConceptKey(String plantName)
{
	int plantConceptId = 0;
	String action="select";
	String statement="select plantconcept_id from plantconcept where plantname = '"
		+plantName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantConcept_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	if ( j.returnedValues.size() > 1 )
	{
		System.out.println("plantConceptKey violation: there are more than one"
		+" keys returned");
	}
	//if there are no returned results
	else if ( j.returnedValues.size() == 0 )
	{
		System.out.println("no usage of "+plantName+" found in the concept table");
		return(0);
	}
	else 
	{
		//return the primary key value
		plantConceptId = Integer.parseInt(
		j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	}
	//System.out.println("####"+ plantNameId );
	return(plantConceptId);

}


/**
 * method that returns the primary key value
 * from the plantUsage table where the plantName
 * field is equal to the input value
 */
private int plantUsageKey(String plantName)
{
	int plantUsageId = 0;
	String action="select";
	String statement="select plantusage_id from plantUsage where plantname = '"
		+plantName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantUsage_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	if ( j.returnedValues.size() > 1 )
	{
		System.out.println("plantUsageKey violation: there are more than one"
		+" keys returned");
	}
	//if there are no returned results
	else if ( j.returnedValues.size() == 0 )
	{
		System.out.println("no usage of "+plantName+" found");
		return(0);
	}
	else 
	{
		//return the primary key value
		plantUsageId = Integer.parseInt(
		j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	}
	//System.out.println("####"+ plantNameId );
	return(plantUsageId);
}







/**
 * method to load the plant instances, stored in a 
 * hashtable, to the database 
 */	
private void loadPlantInstances(Hashtable plantsData)
{
//System.out.println(plantsData.toString() );
	for (int i=0; i<plantsData.size(); i++) {
		loadSinglePlantInstance( 
			extractSinglePlantInstance(plantsData, i)
		);
	}
}

/**
 * method to load the plant instances, stored in a 
 * hashtable, to the database 
 */	
private void loadPlantInstancesUpdate(Hashtable plantsData)
{
//System.out.println(plantsData.toString() );
	for (int i=0; i<plantsData.size(); i++) {
		loadSinglePlantInstanceUpdate( 
			extractSinglePlantInstance(plantsData, i)
		);
	}
}




/**
 * method to extract a single plant instance
 * from the aggregate hashtable - returning a 
 * hashtable with keys like name, author orignDate
 * etc
 */	
private void loadSinglePlantInstance(Hashtable singlePlantInstance)
{
	int nameId = 0;
	int conceptId = 0;
	int statusId = 0;
	if ( singlePlantInstance.toString() != null )
	{
		String concatenatedName=singlePlantInstance.get("concatenatedName").toString();
		String tsnValue=singlePlantInstance.get("tsnValue").toString();
		String rank=singlePlantInstance.get("rank").toString();
		String initialDate=singlePlantInstance.get("initialDate").toString();
		String updateDate=singlePlantInstance.get("updateDate").toString();
		String parentName=singlePlantInstance.get("parentName").toString();
		String authorName=singlePlantInstance.get("authorName").toString();
		String itisUsage=singlePlantInstance.get("itisUsage").toString();
		String synonymousName=singlePlantInstance.get("synonymousName").toString();
		String publicationName=singlePlantInstance.get("publicationName").toString();
		String commonName=singlePlantInstance.get("commonName").toString();
		String familyName=singlePlantInstance.get("familyName").toString();
		String plantCode=singlePlantInstance.get("plantCode").toString();
		
		
		//if the plantname is not there then load it
		if (plantNameExists(concatenatedName)==false)
		{
			System.out.println(" Loading the first instance of a plant");
			nameId = loadPlantNameInstance(concatenatedName, commonName, plantCode);
		}
		//if the plant concept does not exist create an entry 
		// and create a status entry for that plant instance - if 
		//it is not a synonomy b/c if there is a synonomy then the 
		//concatenated value does not have a concept
		if (plantConceptExists(concatenatedName)==false)
		{
			if ( synonymousName.equals("nullToken") )
			{
				//update the concept -- get rid of the hard wired dates
				conceptId = loadPlantConceptInstance(concatenatedName, tsnValue, rank, 
				"PLANTS2000", "01-JAN-00");
				//update the status
				statusId = loadPlantStatusInstance(conceptId, itisUsage, "01-JAN-96", 
				"01-JAN-01", "PLANTS1996");
			}
		}
		//if no synonomies then add the accepted 
		//usage to the usage table -- for 1996
		if ( synonymousName.equals("nullToken") )
		{
			System.out.println("loading usage instance: \n plantNameId: "+nameId);
			System.out.println("for plant name: "+concatenatedName);
			if (nameId > 0)
			{
			loadPlantUsageInstance(concatenatedName, nameId, conceptId, "PLANTS", itisUsage, 
					startDate, stopDate, reference);
			}
			else 
			{
				System.out.println("FAILURE LOADING THE USAGE INSTANCE FOR A PLANT");
			}
		}
	}
}



/**
 * this method is used to load a single plant instance
 * to the plant database and then update any earlier 
 * related instances of the plant that exist in the 
 * database for loading the first instance of plant 
 * data do not use this method but instead use the 
 * method 'loadSinglePlantInstance' that will load the 
 * data for the first time
 */	
private void loadSinglePlantInstanceUpdate(Hashtable singlePlantInstance)
{
	int nameId = 0;
	int conceptId = 0;
	int statusId = 0;
	if ( singlePlantInstance.toString() != null )
	{
		String concatenatedName=singlePlantInstance.get("concatenatedName").toString();
		String tsnValue=singlePlantInstance.get("tsnValue").toString();
		String rank=singlePlantInstance.get("rank").toString();
		String initialDate=singlePlantInstance.get("initialDate").toString();
		String updateDate=singlePlantInstance.get("updateDate").toString();
		String parentName=singlePlantInstance.get("parentName").toString();
		String authorName=singlePlantInstance.get("authorName").toString();
		String itisUsage=singlePlantInstance.get("itisUsage").toString();
		String synonymousName=singlePlantInstance.get("synonymousName").toString();
		String publicationName=singlePlantInstance.get("publicationName").toString();
		String commonName=singlePlantInstance.get("commonName").toString();
		String familyName=singlePlantInstance.get("familyName").toString();
		String plantCode=singlePlantInstance.get("plantCode").toString();
		
		//if the plant name / concept pair is accepted then do 
		//the following which shall include:
		// 1] add name if not there
		// 2] add concept if not there 
		// 3] add the status
		// 4] update the status 
		// 5] add the correlation
		// 6] add the usage
		if ( synonymousName.equals("nullToken") )  //means that it is accepted
			{
				//if the plantname is not in the database then load it
				if (plantNameExists(concatenatedName)==false)
				{
					System.out.println("XXX Loading first instance of a plant: "+concatenatedName);
					nameId = loadPlantNameInstance(concatenatedName, commonName, plantCode);
					//assume that if the plantname is not there then
					//the plant concept is not there either -- this is 
					//a test -- and then add the status too for this 
					//accepted value
					if (plantConceptExists(concatenatedName)==false)
					{
						System.out.println("no concept either: " + concatenatedName);
						//add the new concept
						conceptId = loadPlantConceptInstance(concatenatedName, tsnValue, rank, 
						"PLANTS2000", "01-JAN-00");
						//update the status
						statusId = loadPlantStatusInstance(conceptId, itisUsage, "01-JAN-00", 
						"01-JAN-50", "PLANTS2000");
					}
					else 
					{
						System.out.println("this should never happen -- check the code");
					}
				}
				//if the plantName does already exist in the database 
				// - then update the concept status etc
				if (plantNameExists(concatenatedName)== true)
				{
					//if the plant was accepted in 96 then add a correlation 
					// to the correlation table
					if (plantConceptAccepted(concatenatedName, "USDA-PLANTS", "01-JAN-96") == true ) 
					{
						conceptId = plantConceptKey(concatenatedName);
						int conceptStatusId=plantConceptStatusKey(
							conceptId, "accepted", "PLANTS", "01-JAN-96");
						System.out.println("correlation> conceptID: "+conceptId+
							" conceptStatusId: "+conceptStatusId);
						loadPlantCorrelationInstance(concatenatedName, conceptId, 
							conceptStatusId, "unknown", "01-JAN-00");
						System.out.println("YYY accepted 1996 plant name already exists: "+concatenatedName);
					}
				}
				//lastly load the plant usage table with the instance
				loadPlantUsageInstance(
					concatenatedName, 
					plantNameKey(concatenatedName), 
					plantConceptKey(concatenatedName),
					"PLANTS", 
					itisUsage, 
					startDate, 
					stopDate, 
					reference);
			}
			
//----------------------------break the method here -----------------------
			
		//else if the plant name / concept pair is not accepted
		//then do roughly the same that is above
		if (  !(synonymousName.equals("nullToken")) )  //means that it is not accepted
			{
				System.out.println("THIS IS A SYNONYM");
				//if the plantname is not in the database then load it
				if (plantNameExists(concatenatedName)==false)
				{
					System.out.println("XXX Loading first instance of a plant (w/synonym): "+concatenatedName);
					nameId = loadPlantNameInstance(concatenatedName, commonName, plantCode);
				
				}
				if (plantNameExists(concatenatedName)== true)
				{
					//if the plant was accepted in 96 then add a correlation 
					// to the correlation table
					if (plantConceptAccepted(concatenatedName, "USDA-PLANTS", "01-JAN-96") == true ) 
					{
						conceptId = plantConceptKey(concatenatedName);
						int conceptStatusId=plantConceptStatusKey(
							conceptId, "accepted", "PLANTS", "01-JAN-96");
						System.out.println("correlation> conceptID: "+conceptId+
							" conceptStatusId: "+conceptStatusId);
						//here the correlation goes with the 
						//name and the synonym
						loadPlantCorrelationInstance(synonymousName, conceptId, 
							conceptStatusId, "unknown", "01-JAN-00");
						System.out.println("YYY accepted 1996 plant name already exists: "+concatenatedName);
					}
				//lastly load the plant usage table with the instance
				loadPlantUsageInstance(
					concatenatedName, 
					plantNameKey(concatenatedName), 
					plantConceptKey(synonymousName),
					"PLANTS", 
					"SYNONYM", 
					startDate, 
					stopDate, 
					reference);
			}
			}
	}
}








/**
 * method that takes as input a concatenated
 * name, a date and a party and returns a 
 * boolean true if the concept is an accepted 
 * status by that party for that year 
 * 
 * @param year - startDate ie 01-JAN-96
 */	
private boolean plantConceptAccepted(String concatenatedName, String Party, 
	String year)
{
	//determine first that the plant concept exits and then
	if (plantConceptExists(concatenatedName) == true )
	{
		int conceptId = plantConceptKey(concatenatedName);
		//pass the concept key to see if the 
		//this concept is accepted
		if (plantConceptStatusKey(conceptId, "accepted", "PLANTS", year) > 0)
		{
			System.out.println("plantconceptStatusID: "+ 
				plantConceptStatusKey(conceptId, "accepted", "PLANTS", year) 
			);
			return(true);
		}
		else 
		{
			return(false);
		}
	}
	else 
	{
		System.out.println("this should never happen -- plantConceptAccepted method");
		return(false);
	}
}




/**
 * method that returns a plant concept status id
 * using as input a conceptId, a status, and a 
 * party if no such id exists then the method 
 * returns a zero
 *
 * @param status -- either 'accepted' or some other lowercase string
 * @param party the name of the party that has yet to be defined
 * @param startDate -- the startdate for the status
 */	
private int plantConceptStatusKey(int conceptId, String status, String party, 
	String year)
{
	int plantConceptStatusId = 0;
	String action="select";
	String statement=" select plantconceptstatus_id, status, partyname from "
	+" plantConceptStatus where plantconcept_id = "+conceptId
	+" and status like '%' and startdate = '01-JAN-96'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantconceptstatus_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	//if multiple returned results
	if ( j.returnedValues.size() > 1 )
	{
		System.out.println("plantConceptStatusKey violation: there are more than one"
		+" keys returned");
	}
	//if there are no returned results
	else if ( j.returnedValues.size() == 0 )
	{
		System.out.println("no usage in plantConceptStatus table found ");
		return(0);
	}
	else if ( j.returnedValues.size() == 1 ) 
	{
		//return the primary key value
		//System.out.println(" fuk "+j.returnedValues.elementAt(0).toString().replace('|', ' ').trim());
		plantConceptStatusId = Integer.parseInt(
			j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	}
	return(plantConceptStatusId);
}




/**
 * method that loads a new instance of a 
 * plant usage - name usage returns to 
 * the calling method the primary key for 
 * that concept - name usage
 */	
private int loadPlantStatusInstance(int conceptId, String status, 
	String startDate, String endDate, String event)
{

	String action="insert";
	String insertString="insert into plantConceptStatus";
	String attributeString="plantConcept_id, status, startDate, stopDate, event";	
	int inputValueNum=5;
	String inputValue[]=new String[5];
	inputValue[0]=""+conceptId;
	inputValue[1]=""+status;
	inputValue[2]=""+startDate;
	inputValue[3]=""+stopDate;
	inputValue[4]=""+event;

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);
	String valueString = k.outValueString;

	issueStatement j = new issueStatement();
	j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);
	return(0);	
}









/**
 * method that returns true if an instance of a plant
 * concept exists that is identical to the input critreria
 */	
private boolean conceptNameUsageExists(int nameId, int conceptId)
{
	
	String action="select";
	String statement="select plantUsage_id from plantUsage where plantName_Id = "
		+nameId+" and plantconcept_id ="+conceptId;

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantUsage_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	//entireResult=j.outReturnFields[0];
	//entireSinglePlotOutput[0]=entireResult; //cheat here
	//entireSinglePlotOutputNum=1; //and here
	
	//if there are results retuened then a 
	//plant with the input name does exist
	//System.out.println("#### concept "+ j.returnedValues.size());
	if ( j.returnedValues.size() > 0 )
	{
		return(true);
	}
	else 
	{
		return(false);
	}
}




/**
 * method that loads a new instance of a 
 * plant concept - name usage returns to 
 * the calling method the primary key for 
 * that concept - name usage
 */	
private int loadPlantUsageInstance(String concatenatedName, int name_id, 
	int concept_id, String partyName, String usage, String startDate, 
	String stopDate, String reference)
{
	String action="insert";
	String insertString="insert into plantUsage";
	String attributeString="plantName, plantName_id, plantConcept_id, "+
		"partyName, partyUsageStatus, startDate, stopDate";	
	int inputValueNum=7;
	String inputValue[]=new String[7];
	inputValue[0]=""+concatenatedName;
	inputValue[1]=""+name_id;
	inputValue[2]=""+concept_id;
	inputValue[3]=""+partyName;
	inputValue[4]=""+usage;
	inputValue[5]=""+startDate;
	inputValue[6]=""+stopDate;

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);
	String valueString = k.outValueString;

	issueStatement j = new issueStatement();
	j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);
	return(0);	
}



/**
 * method that loads a new instance of a 
 * plant concept then returns to the calling
 * method the primary key for that concept
 */	
private int loadPlantConceptInstance(String concatenatedName, String classCode, 
	String classRank, String conceptRef, String startDate)
{
	String action="insert";
	String insertString="insert into plantConcept";
	String attributeString="plantName, classCode, classRank, conceptRef, startDate";
	int inputValueNum=5;
	String inputValue[]=new String[5];
	inputValue[0]=""+concatenatedName;
	inputValue[1]=""+classCode;
	inputValue[2]=""+classRank;
	inputValue[3]=""+conceptRef;
	inputValue[4]=""+startDate;
	

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);
	String valueString = k.outValueString;

	issueStatement j = new issueStatement();
	j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);
	
	
	//now query for the primary key value for this plantname
	action="select";
	String statement="select plantconcept_id from plantconcept where plantname = '"
		+concatenatedName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantconcept_id";

	j.issueSelect(statement, action, returnFields, returnFieldLength);
	
	int plantConceptId = Integer.parseInt(
		j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	System.out.println("#### plant concept id "+ plantConceptId );
	return(plantConceptId);
}





/**
 * method that returns true if an instance of a plant
 * concept exists that is identical to the input critreria
 */	
private boolean plantConceptExists(String concatenatedName)
{
	
	String action="select";
	String statement="select plantConcept_id from plantConcept where plantName = '"
		+concatenatedName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantconcept_id";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	//entireResult=j.outReturnFields[0];
	//entireSinglePlotOutput[0]=entireResult; //cheat here
	//entireSinglePlotOutputNum=1; //and here
	
	//if there are results retuened then a 
	//plant with the input name does exist
	//System.out.println("#### concept "+ j.returnedValues.size());
	if ( j.returnedValues.size() > 0 )
	{
		return(true);
	}
	else 
	{
		return(false);
	}
}






/**
 * method that loads a new instance of a 
 * plant name and then returns the primary
 * key value of that plant name to the calling
 * method
 */	
private int loadPlantNameInstance(String concatenatedName, String commonName, 
String plantCode)
{
	String action="insert";
	String insertString="insert into plantName";
	String attributeString="plantName, plantCommonName, plantSymbol";
	int inputValueNum=3;
	String inputValue[]=new String[3];
	inputValue[0]=""+concatenatedName;
	inputValue[1]=""+commonName;
	inputValue[2]=""+plantCode;

	//get the valueString from the method
	issueStatement k = new issueStatement();
	k.getValueString(inputValueNum);
	String valueString = k.outValueString;

	issueStatement j = new issueStatement();
	j.issueInsert(insertString, attributeString, valueString, inputValueNum, inputValue);
	
	
	//now query for the primary key value for this plantname
	action="select";
	String statement="select plantname_id from plantName where plantname = '"
		+concatenatedName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantname_id";

	// Issue the statement, and iterate the counter
	//issueStatement j1 = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	//entireResult=j.outReturnFields[0];
	//entireSinglePlotOutput[0]=entireResult; //cheat here
	//entireSinglePlotOutputNum=1; //and here
	
	//if there are results retuened then a 
	//plant with the input name does exist
	
	int plantNameId = Integer.parseInt(
		j.returnedValues.elementAt(0).toString().replace('|', ' ').trim()
		);
	//System.out.println("####"+ plantNameId );
	return(plantNameId);
}








/**
 * method that returns tru if an instance of a plant
 * name exists that is identical to the plantName 
 * input into the method
 */	
private boolean plantNameExists(String concatenatedName)
{
	
	String action="select";
	String statement="select plantName from plantName where plantname = '"
		+concatenatedName+"'";

	String returnFields[]=new String[1];
	int returnFieldLength=1;
	returnFields[0]="plantName";

	// Issue the statement, and iterate the counter
	issueStatement j = new issueStatement();
	j.issueSelect(statement, action, returnFields, returnFieldLength);

	//entireResult=j.outReturnFields[0];
	//entireSinglePlotOutput[0]=entireResult; //cheat here
	//entireSinglePlotOutputNum=1; //and here
	
	//if there are results retuened then a 
	//plant with the input name does exist
	//System.out.println("####"+ j.returnedValues.size());
	if ( j.returnedValues.size() > 0 )
	{
		return(true);
	}
	else 
	{
		return(false);
	}
}





/**
 * method to extract a single plant instance
 * from the aggregate hashtable - returning a 
 * hashtable with keys like name, author orignDate
 * etc
 */	
private Hashtable extractSinglePlantInstance(Hashtable plantsData, 
	int hashKeyNum)
{
	Hashtable singlePlantInstance = new Hashtable();
	if ( plantsData.get(""+hashKeyNum).toString() != null )
	{
		singlePlantInstance=(Hashtable)plantsData.get(""+hashKeyNum);
		//System.out.println(singlePlantInstance.toString() );
	}
	return(singlePlantInstance);
}


/**
 * method that parses the elements from a vector
 * containing the element names and element values
 * into a hashtable structure whose elements can easily
 * be extracted and then loaded to the database
 */	
private Hashtable elementParser(Vector fileVector)
{
	int hashKey=0;  //the keys in the hash table are incremental integers
	Hashtable plantHash = new Hashtable();
	for (int i=0; i<fileVector.size(); i++) {
		if ( fileVector.elementAt(i) != null )
		{
			if ( plantEntryStart(fileVector.elementAt(i).toString() ) == true )
			{
				//the key is a hack -- has to be a string
				plantHash.put(""+hashKey,  plantInstance(fileVector, i)  );
		//		System.out.println(fileVector.elementAt(i) );
				hashKey++;
			}
		}
	}
	System.out.println(" SIZE OF HASH (number of plant instances):"+hashKey);
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
			plantInstance.put("tsnValue", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("concatenatedName") )
		{
			plantInstance.put("concatenatedName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
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
			plantInstance.put("synonymousName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("publicationName") )
		{
			plantInstance.put("publicationName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
		}
		else if ( fileVector.elementAt(i).toString().startsWith("commonName") )
		{
			plantInstance.put("commonName", 
				pipeStringTokenizer(fileVector.elementAt(i).toString(), 2) );
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
	//System.out.println("%%%%% "+pipeString+" "+tokenPosition);
	String token="nullToken";
	if (pipeString != null) {
		StringTokenizer t = new StringTokenizer(pipeString.trim(), "|");

		int i=1;
		while (i<=tokenPosition) {
			if ( t.hasMoreTokens() )
			{
				token=t.nextToken();
				i++;
			}
			else 
			{
				token="nullToken";
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






}

