/**
 *  '$RCSfile: USDAPlantsLoader.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-02-19 21:13:16 $'
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

import java.io.IOException;
import java.io.*;
import java.util.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;
import java.lang.*;
import java.text.*;
import java.util.*;
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
 */


public class USDAPlantsLoader 
{

	public String styleSheet="../lib/plantsToNVC.xsl";
	public String attributeFile="attFile.txt";
	public Vector fileVector = new Vector();
	public Hashtable attributeHash = new Hashtable();
	public Hashtable constraintHash = new Hashtable();

	public String startDate = "01-JAN-96";
	public String stopDate = "01-JAN-22";
	public String partyName = "USDA-PLANTS";
	public String reference = "PLANTS96";
	public String otherCitationDetails = "PLANTS1996";

	//constructor -- define as static the LocalDbConnectionBroker 
	//so that methods called by this class can access the 'local' 
	//pool of database connections
	//static LocalDbConnectionBroker lb;

	private Connection conn = null;
	
	//constructor method
	public USDAPlantsLoader()
	{
		conn = this.getConnection();
	}
	
	
	
	/**
	* method that will return a database connection for use with the database
	*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/nvc", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("failed making db connection: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	

/**
 * Main method to run the Legacy Data Wizard requires only the xml file that
 * defines the relationship between the files comprising the data package
 */
public static void main(String[] args) 
{
	if (args.length != 1) 
	{
		System.out.println("Usage: java USDAPlantsLoader  [XML] \n");
		System.exit(0);
	}
	else 
	{
		String inputXml=null;
		inputXml=args[0];
		//call the method to convert the data package to xml
		USDAPlantsLoader il =new USDAPlantsLoader();  
		il.transformXmlData(inputXml);
	}
}

/**
 * method that steps thru the processes associated with 
 * transforming and loading the usda xml data
 *
 * @param inputXml
 */
public void transformXmlData (String inputXml) 
{
	try 
	{
		System.out.println("USDAPlantsLoader > infile: " + inputXml);
		
		//load new plant instances -- EXCLUDING THOSE WITH SYNONOMYS
		loadPlantInstances(elementParser(transformToFile(inputXml)));
		//purge the file vector
		fileVector = new Vector();
		
		//load the plant INSTANCES WITH SYNONOMYS
		loadPlantSynonym(elementParser(transformToFile(inputXml)));
		
	}
	catch( Exception e ) 
	{
		System.out.println("Exception:  "	+ e.getMessage() );
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
		System.out.println("USDAPlantsLaoder > loading synonym ");
		System.out.println("USDAPlantsLaoder > Size of plants hash: "+plantsData.size());
		for (int i=0; i<plantsData.size(); i++) 
		{
		
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
			System.out.println("USDAPlantsLaoder > ID plant concept: "+plantConceptId);
		
			int plantNameId = plantNameKey(concatenatedName);
			
			//load the relevant data to the usage table
			loadPlantUsageInstance(concatenatedName, plantNameId, plantConceptId, 
			partyName, "SYNONYM", startDate, stopDate, reference);
			
			//update the plantConceptStatus table
			//with the status of "SYNONYM"
		}
		else 
		{
			System.out.println("USDAPlantsLaoder > synonomy does not exist");
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
	int plantNameId = -999;
	try
	{
		//now get the associated primary key value
		String s1 = "select plantName_id from plantname where plantname = '"
		+plantName+"'";
			
		PreparedStatement pstmt1 = conn.prepareStatement(s1);
		ResultSet rs = pstmt1.executeQuery();
		int cnt = 0;
		while	( rs.next() )
		{	
			plantNameId = rs.getInt(1);
			cnt++;
		}
	}
	catch(Exception e)
	{
		System.out.println("Exception: " + e.getMessage() );
		e.printStackTrace();
	}
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
		try
		{
			String s = "insert into PLANTCORRELATION "
			+"(plantUsage1_id, plantUsage2_id, convergence)  values(?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, plantUsageId1);
			pstmt.setInt(2, plantUsageId2);
			pstmt.setString(3, convergence);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(0);	
	}







/**
 * method that returns the primary key value
 * from the plant concept key based on the 
 * input parameters
 */
private int plantConceptKey(String plantName)
{
	int plantConceptId = -999;
	try
	{
		//now get the associated primary key value
		String s1 = " select plantconcept_id from plantconcept where plantname = '"
		+plantName+"'";
			
		PreparedStatement pstmt1 = conn.prepareStatement(s1);
		ResultSet rs = pstmt1.executeQuery();
		int cnt = 0;
		while	( rs.next() )
		{	
			plantConceptId = rs.getInt(1);
			cnt++;
		}
		if ( cnt > 1 )
		{
			System.out.println("USDAPlantsLoader > plantConceptKey violation:  more than one"
			+" keys returned");
		}
		//if there are no returned results
		else if ( cnt == 0 )
		{
			System.out.println("USDAPlantsLoader > no  "+plantName+" found in the concept table");
		}
	}
	catch(Exception e)
	{
		System.out.println("Exception: " + e.getMessage() );
		e.printStackTrace();
	}
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
		System.out.println("USDAPlantsLoader > plantUsageKey violation: there are more than one"
		+" keys returned");
	}
	//if there are no returned results
	else if ( j.returnedValues.size() == 0 )
	{
		System.out.println("USDAPlantsLoader > no usage of "+plantName+" found");
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
	for (int i=0; i<plantsData.size(); i++) 
	{
		loadSinglePlantInstance(extractSinglePlantInstance(plantsData, i));
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
		
		//first load the reference table
		int refId = this.insertPlantReference(this.otherCitationDetails);


		//if the plantname is not there then load it
		if (plantNameExists(concatenatedName)==false)
		{
			System.out.println("USDAPlantsLoader > first instance of plant name: "
				+ concatenatedName );
			nameId = loadPlantNameInstance(refId, concatenatedName, commonName, plantCode);
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
				conceptId = loadPlantConceptInstance(nameId, refId, concatenatedName, 
				plantCode);
				//update the status
				statusId = loadPlantStatusInstance(conceptId, itisUsage, "01-JAN-96", 
				"01-JAN-01", "PLANTS96");
			}
		}
/*	
		//if no synonomies then add the accepted 
		//usage to the usage table
		if ( synonymousName.equals("nullToken") )
		{
			System.out.println("USDAPlantsLoader > loading usage instance plantNameId: " + nameId);
			System.out.println("USDAPlantsLoader > for plant name: "+ concatenatedName);
			if (nameId > 0)
			{
			loadPlantUsageInstance(concatenatedName, nameId, conceptId, "PLANTS", itisUsage, 
					startDate, stopDate, reference);
			}
			else 
			{
				System.out.println("USDAPlantsLaoder > FAILURE LOADING THE USAGE INSTANCE FOR A PLANT");
			}
		}
		
		*/
	}
}


 /** 
	 * method that inserts the reference data 
	 */
		private int getPlantReferenceId(String otherCitationDetails)
	  {
		 	int refId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT plantreference_id from PLANTREFERENCE where othercitationdetails"
				+" like '"+otherCitationDetails+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					refId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(refId);
	 }
	 
		
		 /** 
	  * method that inserts the reference data 
		*/
		private boolean plantReferenceExists(String otherCitationDetails)
	  {
			boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT plantreference_id from PLANTREFERENCE where othercitationdetails"
			+" like '"+otherCitationDetails+"'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				cnt++;
			}
			if (cnt > 0)
			{
				System.out.println("USDAPlantsLoader > matching reference:  " + cnt  );
				exists = true;
			}
			else
			{
				exists = false;
			}
		 }
			catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(exists);
	 }
	 
	 
	 
 /** 
  * method that inserts the reference data 
	*/
	private int insertPlantReference(String otherCitationDetails)
  {
	 int refId = 0; 
	 try
	 {
	 	StringBuffer sb = new StringBuffer();
		//first see if the reference already exists
		boolean refExists = plantReferenceExists(otherCitationDetails);
		//System.out.println("USDAPlantsLoader > refExists: " + refExists); 
		
		if (refExists == true)
		{
			refId = getPlantReferenceId(otherCitationDetails);
			//throw new Exception("reference already exists");
		}
		else
		{
			//insert the strata values
			sb.append("INSERT into PLANTREFERENCE (othercitationdetails) "
			+" values(?)");
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			// Bind the values to the query and execute it
 			pstmt.setString(1, otherCitationDetails);
			//execute the p statement
 			pstmt.execute();
 			// pstmt.close();
			
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT plantreference_id from PLANTREFERENCE where othercitationdetails"
			+" like '"+otherCitationDetails+"'");
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			int cnt = 0;
			while ( rs.next() ) 
			{
				refId = rs.getInt(1);
				cnt++;
			}
		}
	 }
		catch (Exception e)
	 {
		 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
		 e.printStackTrace();
	 }
	 return(refId);
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
		try
		{
			String s = "insert into PLANTSTATUS "
			+"(plantConcept_id, plantconceptstatus, startDate, stopDate, plantPartyComments)  values(?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, conceptId);
			pstmt.setString(2, status);
			pstmt.setString(3, startDate);
			pstmt.setString(4, stopDate);
			pstmt.setString(5, event);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	return(0);	
}



/**
 * method that returns true if an instance of a plant
 * concept exists that is identical to the input critreria
 */	
private boolean conceptNameUsageExists(int nameId, int conceptId)
{
	boolean result = false;
	try
	{
			//now get the associated primary key value
			String s1 = " select plantUsage_id from plantUsage where plantName_Id = "
			+nameId+" and plantconcept_id ="+conceptId;
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
			}
			//figure out how many 
			if (cnt > 0 )
			{
				result =true;
			}
			else 
			{
				result = false;
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(result);
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
		try
		{
			String s = "insert into PLANTUSAGE (plantName, plantName_id, plantConcept_id, "+
				"partyName, partyUsageStatus, startDate, stopDate)  values(?,?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setString(1, concatenatedName);
			pstmt.setInt(2, name_id);
			pstmt.setInt(3, concept_id);
			pstmt.setString(4, partyName);
			pstmt.setString(5, usage);
			pstmt.setString(6, startDate);
			pstmt.setString(7, stopDate);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();
			//System.out.println("USDAPlantsLoader >>>>>>>>>>>>> results: " + results);		
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(0);	
	}



	/**
 	* method that loads a new instance of a 
 	* plant concept then returns to the calling
 	* method the primary key for that concept
 	*/	
	private int loadPlantConceptInstance(int plantNameId, int plantReferenceId, 
	String plantName, String plantCode)
	{
		int plantConceptId = -999;
		try
		{
			String s = "insert into PLANTCONCEPT (plantname_id, plantName, "
			+" plantreference_id, plantCode ) values(?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, plantNameId);
			pstmt.setString(2, plantName);
			pstmt.setInt(3, plantReferenceId);
			pstmt.setString(4, plantCode);
			pstmt.execute();
			pstmt.close();	
		
			//now get the associated primary key value
			String s1 = "select plantconcept_id from plantconcept where plantname = '"
			+plantName+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantConceptId = rs.getInt(1);
				System.out.println("USDAPlantsLoader >>> plantconcept_id: " + plantConceptId);
			}
			if (cnt > 1)
			throw new Exception("multiple plantconceptID's found");
			pstmt1.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(plantConceptId);
}






	/**
 	* method that returns true if an instance of a plant
 	* concept exists that is identical to the input critreria
	*
	* @param concatenatedName -- the concatenated name
	* @param tsnValue -- the concept code
	* @return exists -- boolean 
 	*/	
	private boolean plantConceptExists(String concatenatedName, String tsnValue)
	{
		boolean result = false;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantConcept_id from plantConcept where plantName = '"
			+concatenatedName+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
			}
			//figure out how many 
			if (cnt > 0 )
			{
				result =true;
			}
			else 
			{
				result = false;
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(result);
	}


	/**
 	* method that loads a new instance of a 
 	* plant name and then returns the primary
 	* key value of that plant name to the calling
 	* method
 	*/	
	private int loadPlantNameInstance(int refId, String concatenatedName, 
	String commonName, String plantCode)
	{
		int plantNameId = -999;
		try
		{
			String s = "insert into PLANTNAME (plantreference_id, plantName, plantCommonName, plantSymbol) "
			+" values(?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, refId);
			pstmt.setString(2, concatenatedName);
			pstmt.setString(3, commonName);
			pstmt.setString(4, plantCode);
			pstmt.execute();
			pstmt.close();	
		
			//now get the associated primary key value
			String s1 = "select plantname_id from PLANTNAME where plantname = '"+ concatenatedName+ "' ";
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantNameId = rs.getInt(1);
				System.out.println("USDAPlantsLoader >>> plantname_id: " + plantNameId);
			}
			if (cnt > 1)
			throw new Exception("multiple plantnameID's found");
			pstmt1.close();
		}
		catch(Exception e )
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(plantNameId);
	}








	/**
 	* method that returns tru if an instance of a plant
 	* name exists that is identical to the plantName 
 	* input into the method
 	*/	
	private boolean plantNameExists(String concatenatedName)
	{
		boolean result = false;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantName from plantName where plantname = '"
			+concatenatedName+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
			}
			//figure out how many 
			if (cnt > 0 )
			{
				result =true;
			}
			else 
			{
				result = false;
			}
			pstmt1.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(result);
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
				//System.out.println("USDAPlantsLoader > trsnaleted line: "+ fileVector.elementAt(i) );
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
			//System.out.println("USDAPlantsLoader > tsnValue: " + tsnValue);
			plantInstance.put("tsnValue", tsnValue );
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
	String token="nullToken";
	if (pipeString != null) 
	{
		StringTokenizer t = new StringTokenizer(pipeString.trim(), "|");

		int i=1;
		while (i<=tokenPosition) 
		{
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

