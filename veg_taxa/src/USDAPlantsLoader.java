import java.io.IOException;
import java.io.*;
import java.util.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;

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


public class USDAPlantsLoader {

public String styleSheet="plantsToNVC.xsl";
public String attributeFile="attFile.txt";
public Vector fileVector = new Vector();
public Hashtable attributeHash = new Hashtable();
public Hashtable constraintHash = new Hashtable();

public String startDate = "01-JAN-96";
public String stopDate = "01-JAN-22";
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
		System.out.println("Usage: java USDAPlantsLoader  [XML] \n");
		System.exit(0);
	}
	else {
		String inputXml=null;
		inputXml=args[0];
		//call the method to convert the data package to xml
		USDAPlantsLoader il =new USDAPlantsLoader();  
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
		
		//load new plant instances -- EXCLUDING THOSE WITH SYNONOMYS
		loadPlantInstances(
			elementParser(
				transformToFile(inputXml)
			)
		);
		//purge the file vector
		fileVector = new Vector();
		
		//load the plant INSTANCES WITH SYNONOMYS
		loadPlantSynonym(
			elementParser(
				transformToFile(inputXml)
			)
		);
		
		//lastly destroy the connection pooling
		lb.manageLocalDbConnectionBroker("destroy");
		
	}
	catch( Exception e ) {
		System.out.println(" failed in: USDAPlantsLoader.transformXMLData "
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
			
			//update the plantConceptStatus table
			//with the status of "SYNONYM"
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
 * plant concept - name usage returns to 
 * the calling method the primary key for 
 * that concept - name usage
 */	
private int loadPlantCorrelationInstance(int plantUsageId1, 
	int plantUsageId2, String convergence)
{
	String action="insert";
	String insertString="insert into plantCorrelation";
	String attributeString="plantUsage1_id, plantUsage2_id, convergence";	
	int inputValueNum=3;
	String inputValue[]=new String[3];
	inputValue[0]=""+plantUsageId1;
	inputValue[1]=""+plantUsageId2;
	inputValue[2]=""+convergence;
	
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
			System.out.println("VV>> Loading the first instance of a plant");
			nameId = loadPlantNameInstance(concatenatedName, commonName, plantCode);
		}
		//if the plant concept does not exist create an entry 
		// and create a status entry for that plant instance - if 
		//it is not a synonomy b/c if there is a synonomy then the 
		//concatenated value does not have a concept
		if (plantConceptExists(concatenatedName, tsnValue)==false)
		{
			if ( synonymousName.equals("nullToken") )
			{
				//upadte the concept
				conceptId = loadPlantConceptInstance(concatenatedName, tsnValue, rank, 
				"PLANTS96", startDate);
				//update the status
				statusId = loadPlantStatusInstance(conceptId, itisUsage, "01-JAN-96", 
				"01-JAN-01", "PLANTS96");
			}
		}
		//if no synonomies then add the accepted 
		//usage to the usage table
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
private boolean plantConceptExists(String concatenatedName, String tsnValue)
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

