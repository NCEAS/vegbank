/**
 *  '$RCSfile: USDAPlantsLoader.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-03-20 20:03:04 $'
 * '$Revision: 1.22 $'
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTResultTarget;

import org.vegbank.common.utility.*;

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
	private String partyContractInstructions = "http://plants.usda.gov";
	public String reference = "PLANTS96";
	public String otherCitationDetails = "PLANTS1996";
	private Connection conn = null;
	private int connectionUses = 0;
	
	//this is the cache that stores all the previously
	//stored codes
	private Hashtable loadedPlantCodeHash = new Hashtable();
	
	private String dateEntered = "20-FEB-2002";
	
	
	
	//constructor method
	public USDAPlantsLoader()
	{
		conn = this.getConnection();
	}
	
	/**
	 * this is the main method to be called by other classes for loading 
	 * a package of plant data stored in an xml document
	 *
	 */
	public void loadPlantDataSet(String inputXml)
	{
		try 
 		{
			System.out.println("USDAPlantsLoader > infile: " + inputXml);
			
			System.out.println("USDAPlantsLoader > reading input file ");
			Vector v = this.transformToFile(inputXml);
			
			System.out.println("USDAPlantsLoader > parsing input file contents ");
			Hashtable plantsHash = this.elementParser(v);
			
			System.out.println("USDAPlantsLoader > loading data to db ");
			//load new plant instances -- EXCLUDING THOSE WITH SYNONOMYS
			loadPlantInstances(plantsHash);
		
			//load the plant INSTANCES WITH SYNONOMYS 
			//GOT RID OF THIS BECAUSE IT WILL BE DONE IN THE CORRELATION
			//TABLE 
			//	loadPlantSynonym(plantsHash);
			
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " +e.getMessage());
			e.printStackTrace();
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
			Class.forName("org.postgresql.Driver");
		//	c = DriverManager.getConnection("jdbc:postgresql://vegbank.nceas.ucsb.edu/plants_dev", "datauser", "");
	//		c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/plants_dev", "datauser", "");
			c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/tmp", "datauser", "");
			System.out.println("USDAPlantsLoader > connecting to: 127.0.0.1, tmp");
			//System.exit(0);
		}
		catch ( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
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
		///il.transformXmlData(inputXml);
		il.loadPlantDataSet(inputXml);
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
		in.close();
		return(fileVector);
	}



	/**
 	* method to load the plant synonym if 
 	* it exists in the hash 
 	*/	
	private void loadPlantSynonym(Hashtable plantsData)
	{
		System.out.println("USDAPlantsLoader > loading synonym ");
		System.out.println("USDAPlantsLoader > Size of plants hash: "+plantsData.size());
		for (int i=0; i<plantsData.size(); i++) 
		{
			//if there is a synonymy associated with this
			//instance of a plant
			if (extractSinglePlantInstance(plantsData, i ).get("synonymousName").toString() != "nullToken")
			{
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
		String sName = singlePlantInstance.get("synonymousName").toString();
		if (singlePlantInstance.get("synonymousName").toString() != null )
		{
			System.out.println("USDAPlantsLoader > loading a synonym: " + sName);
			String concatenatedName	= singlePlantInstance.get("concatenatedName").toString();
			String synonymousName	= singlePlantInstance.get("synonymousName").toString();
		
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
				 "SYNONYM", startDate, stopDate, reference);
			
				//update the plantConceptStatus table
				//with the status of "SYNONYM"
			}
			else 
			{
				System.out.println("USDAPlantsLoader > synonomy does not exist");
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
 * 
 * @param plantDescription -- the plantDescription
 */
	private int plantConceptKey(String plantDescription)
	{
		int plantConceptId = -999;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantconcept_id from plantconcept where plantDescription = '"
			+plantDescription+"'";
			
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
				System.out.println("USDAPlantsLoader > no  "+plantDescription+" found in the concept table");
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
			//System.exit(0);
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
		try
		{
			
			int plantNumber = plantsData.size();
			
			//for performance testing
			//int plantNumber = 1000;
			
			System.out.println("USDAPlantsLoader > number plant instances: " + plantNumber );
			
			//load the families first
			//first load the family associated with each of the plants, this
			//was written after most of the code was written and may need to be 
			//changed or the location may need to be changed
			for (int i=0; i<plantNumber; i++) 
			{
				//the hashtable containing the plant iformation for each plant
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				loadSingleFamilyInstance(plantInstanceHash);
			}
			
			//load the genus second
			plantNumber = plantsData.size();
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				if ( plantInstanceHash != null )
				{
					String rank=plantInstanceHash.get("rank").toString();
					if ( rank.equalsIgnoreCase("genus") )
					{
						this.loadSinglePlantInstance(plantInstanceHash, "Genus");
						//System.out.println("removing the genus from the hash");
						this.removeSinglePlantInstance(plantsData, i);
					}
				}
			}
			
			//load the species third
			plantNumber = plantsData.size();
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				if ( plantInstanceHash != null )
				{
					String rank=plantInstanceHash.get("rank").toString();
					if ( rank.equalsIgnoreCase("species") )
					{
						this.loadSinglePlantInstance(plantInstanceHash, "species");
						//System.out.println("removing the species from the hash");
						this.removeSinglePlantInstance(plantsData, i);
					}
				}
			}
			
			//load the subspecies third
			plantNumber = plantsData.size();
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				if ( plantInstanceHash != null )
				{
					String rank=plantInstanceHash.get("rank").toString();
					if ( rank.equalsIgnoreCase("subspecies") )
					{
						this.loadSinglePlantInstance(plantInstanceHash, "subspecies");
						//System.out.println("removing the subspecies from the hash");
						this.removeSinglePlantInstance(plantsData, i);
					}
				}
			}
			
			//load the varieties fourth
			plantNumber = plantsData.size();
			for (int i=0; i<plantNumber; i++) 
			{
				Hashtable plantInstanceHash = extractSinglePlantInstance(plantsData, i);
				if ( plantInstanceHash != null )
				{
					this.loadSinglePlantInstance(plantInstanceHash, "variety");
				}
			}
		}
		catch (Exception e)
	  {
		 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
		 e.printStackTrace();
		 //System.exit(0);
	  }
	}



 /**
 	* method to extract a single plant instance
 	* from the aggregate hashtable - returning a 
 	* hashtable with keys like name, author orignDate
 	* etc
 	*
 	* @param singlePlantInstance -- a hash table with all the attributes 
 	*	of a sigle plant instance
	* @param plantLevel -- the level of the plant in the usda heirarchy
 	* @return void
 	*
 	*/	
	private void loadSinglePlantInstance(Hashtable singlePlantInstance, String plantLevel)
	{
		try
		{
			int sciNameId = 0;
			int commonNameId = 0;
			int codeNameId = 0;
			int conceptId = 0;
			int statusId = 0;
			// IF THE CONNECTION HAS BEEN USED TOO MANY TIMES THEN CLOSE AND REOPEN
			this.connectionUses++;
			if ( this.connectionUses > 200 )
			{
				System.out.println("connection use: " + this.connectionUses);
				conn.close();
				conn = getConnection();
				this.connectionUses = 0;
			}
			
			if ( singlePlantInstance.toString() != null )
			{
				String concatenatedName=singlePlantInstance.get("concatenatedName").toString();
				String concatenatedLongName = singlePlantInstance.get("concatenatedLongName").toString();
				String tsnValue=singlePlantInstance.get("tsnValue").toString();
				String rank=singlePlantInstance.get("rank").toString();
				String initialDate=singlePlantInstance.get("initialDate").toString();
				String updateDate=singlePlantInstance.get("updateDate").toString();
				String parentName=singlePlantInstance.get("parentName").toString();
				String authorName=singlePlantInstance.get("authorName").toString();
				String plantConceptStatus=singlePlantInstance.get("itisUsage").toString();
				String synonymousName=singlePlantInstance.get("synonymousName").toString();
				String publicationName=singlePlantInstance.get("publicationName").toString();
				String commonName=singlePlantInstance.get("commonName").toString();
				String familyName=singlePlantInstance.get("familyName").toString();
				String plantCode=singlePlantInstance.get("plantCode").toString();
				//create a concept description based on all the names and the date
				String conceptDescription = plantCode+"|"+concatenatedName+"|"+commonName
				+"|"+plantConceptStatus+"PLANTS1996";
				
				//if the rank is the same as that specified as a input parameter 
				//to this method then try to load the plant
				if(rank.equalsIgnoreCase(plantLevel) )
				{
					//see if we already loaded this plant from the hash
					//table
					if (! this.loadedPlantCodeHash.contains(plantCode) )
					{
						//add the plant code to the cache of all the plants already 
						//loaded so that we do not try to load again
						this.loadedPlantCodeHash.put(plantCode, plantCode);
					
						//first load the reference table
						int refId = this.insertPlantReference(this.otherCitationDetails);
			
						//load the party table
						int partyId = this.insertPlantPartyInstance(this.partyName, this.partyContractInstructions);
						//System.out.println("USDAPlantsLoader > party id: " + partyId);
	
						//if the scientific plantname is not there then load it
						if (plantNameExists(concatenatedName)==false)
						{
							//sciNameId = loadPlantNameInstance(refId, concatenatedName, commonName, plantCode);
							sciNameId = loadPlantNameInstance(refId, concatenatedName, dateEntered);
						}
						//same with the common name 
						if (plantNameExists(commonName)==false)
						{
							//commonNameId = loadPlantNameInstance(refId, commonName, commonName, plantCode);
							commonNameId = loadPlantNameInstance(refId, commonName, dateEntered);
						}
						//same with the codes
						if (plantNameExists(plantCode)==false)
						{
							//codeNameId = loadPlantNameInstance(refId, plantCode, commonName, plantCode);
							codeNameId = loadPlantNameInstance(refId, plantCode, dateEntered);
						}
						//if the plant concept does not exist create an entry 
						// and create a status entry for that plant instance - if 
						//it is not a synonomy b/c if there is a synonomy then the 
						//concatenated value does not have a concept
						if (plantConceptExists(concatenatedName, tsnValue) == false)
						{
								//update the concept 
								conceptId = loadPlantConceptInstance(sciNameId, refId, concatenatedName, plantCode);
								//update the status add the plant parent here and also
								statusId = loadPlantStatusInstance(conceptId, plantConceptStatus, "30-JUN-96", 
								"01-JAN-01", "PLANTS96", partyId, parentName, rank);
								//first and always load the usage for the code no matter if it is standard or not
								loadPlantUsageInstanceNew(plantCode, codeNameId, conceptId, "STANDARD", 
								"30-JUN-96", "30-JUN-2001",  "CODE", partyId);
						}
						// IF THE CONCEPT DOES EXIST THEN SEND A WARNING AND THEN GET IT
						else
						{
							System.out.println("######## EXISTING CONCEPT ##########");
							// BELOW THE CONCATENATEDNAME IS THE PLANT DESCRIPTION
							conceptId =  getPlantConceptId(sciNameId, refId, concatenatedName, 
							plantCode, rank, concatenatedLongName);
						}
						//check to see that these plants are accepted or wait till the 
						//last loading step
						if ( synonymousName.equals("nullToken") )
						{
							//update the usage table with the scientific names and the 
							//commun names notice that if the nameid is zero (meaning that the 
							//name are not new names) then get the names and load
							if ( sciNameId > 0 )
							{
								loadPlantUsageInstanceNew(concatenatedName, sciNameId, conceptId, "STANDARD", 
								"30-JUN-96", "30-JUN-2001",  "SCIENTIFICNAME", partyId);
							}
							else
							{
								sciNameId  = this.getPlantNameId(concatenatedName);
								loadPlantUsageInstanceNew(concatenatedName, sciNameId, conceptId, "STANDARD", 
								"30-JUN-96", "30-JUN-2001",  "SCIENTIFICNAME", partyId);
							}
							//repeat this for the commonName
							if ( commonNameId > 0)
							{
								loadPlantUsageInstanceNew(commonName, commonNameId, conceptId, "STANDARD", 
								"30-JUN-96", "30-JUN-2001",  "COMMONNAME", partyId);
							}
							else 
							{
								commonNameId = this.getPlantNameId(commonName);
								loadPlantUsageInstanceNew(commonName, commonNameId, conceptId, "STANDARD", 
								"30-JUN-96", "30-JUN-2001",  "COMMONNAME", partyId);
							}
						}
						//else if not a standard and has a synonym
						else
						{
							System.out.println("USDAPLantsLoader > loading non standard plant: " + concatenatedName);
							//update the usage table with the scientific names and the 
							//commun names notice that if the nameid is zero (meaning that the 
							//name are not new names) then get the names and load
							if ( sciNameId > 0 )
							{
								loadPlantUsageInstanceNew(concatenatedName, sciNameId, conceptId, "NONSTANDARD", 
								"30-JUN-96", "30-JUN-1996",  "SCIENTIFICNAME", partyId, synonymousName);
							}
							else
							{
								sciNameId  = this.getPlantNameId(concatenatedName);
								loadPlantUsageInstanceNew(concatenatedName, sciNameId, conceptId, "NONSTANDARD", 
								"30-JUN-96", "30-JUN-1996",  "SCIENTIFICNAME", partyId, synonymousName);
							}
							//repeat this for the commonName
							if ( commonNameId > 0)
							{
								loadPlantUsageInstanceNew(commonName, commonNameId, conceptId, "NONSTANDARD", 
								"30-JUN-96", "30-JUN-1996",  "COMMONNAME", partyId, synonymousName);
							}
							else 
							{
								commonNameId = this.getPlantNameId(commonName);
								loadPlantUsageInstanceNew(commonName, commonNameId, conceptId, "NONSTANDARD", 
								"30-JUN-96", "30-JUN-1996",  "COMMONNAME", partyId, synonymousName);
							}
						}
					}
					else
					{
						System.out.println("USDAPlantsLoader > ALREADY LOADED: " + plantCode);
					}
				}
			}
		}
		catch( Exception e )
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("plant: " + singlePlantInstance.toString() );
			System.exit(0);
		}
	}

/**
 * method that returns the primary key value
 * from the plant concept key based on the 
 * input parameters
 * 
 * @param plantDescription -- the plantDescription
 * @param longNameId -- the long name of the plant
 * @param refId -- the reference ID for the concept
 * @param code -- the codeName for the plant
 * @param rank -- the heirarchical rank of the concept
 * @param longName -- the longName of the plantName
 * fix -- the name if the input plantDesc to taxonDes
 */
	private int getPlantConceptId(int longNameId, int refId, String plantDescription, 
	String code, String rank, String longName)
	{
		int plantConceptId = -999;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantconcept_id from plantconcept where plantDescription = '"
			+plantDescription+"'";
			
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
				System.out.println("USDAPlantsLoader > no  "+plantDescription+" found in the concept table");
			}
		}
		catch(Exception e)
		{
			//commit = false;
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
			//System.exit(0);
		}
		return(plantConceptId);
	}

/**
 * method to extract a single plant instance
 * from the aggregate hashtable - returning a 
 * hashtable with keys like name, author orignDate
 * etc
 */	
private void loadSingleFamilyInstance(Hashtable singlePlantInstance)
{
	int nameId = 0;
	int conceptId = 0;
	int statusId = 0;
	if ( singlePlantInstance.toString() != null )
	{
		String tsnValue=singlePlantInstance.get("tsnValue").toString();
		String plantConceptStatus= "accepted";
		String familyName=singlePlantInstance.get("familyName").toString();
		String rank = "family";
		String plantCode = "";
		String commonName = "";
		String parentName = "";
		
		
		//see if we already loaded this plant
		if (! this.loadedPlantCodeHash.contains(familyName) )
		{
			//System.out.println("USDAPlantsLoader > family not loaded: " + familyName);
			
			//add the plant code to the cache of all the plants already 
			//loaded so that we do not try to load again
			this.loadedPlantCodeHash.put(familyName, familyName);
		
			//first load the reference table
			int refId = this.insertPlantReference(this.otherCitationDetails);
		
			//load the party table
			int partyId = this.insertPlantPartyInstance(this.partyName, this.partyContractInstructions);

			//if the plantname is not there then load it
			if (plantNameExists(familyName)==false)
			{
				nameId = loadPlantNameInstance(refId, familyName, dateEntered);
			}

			//if the plant concept does not exist create an entry 
			// and create a status entry for that plant instance - if 
			//it is not a synonomy b/c if there is a synonomy then the 
			//concatenated value does not have a concept
			if (plantConceptExists(familyName, tsnValue)==false)
			{
					conceptId = loadPlantConceptInstance(nameId, refId, familyName, plantCode);
					//update the status
					statusId = loadPlantStatusInstance(conceptId, plantConceptStatus, 
					"30-JUN-96", "01-JAN-01", "PLANTS96", partyId, parentName, rank);
			}
			
			//load the plant usage 
			//load the relevant data to the usage table
			///fix the reference
			
			//loadPlantUsageInstance(familyName, nameId, conceptId, 
			//"STANDARD", "30-JUN-96", "01-JAN-01", "reference");
			
			loadPlantUsageInstanceNew(familyName, nameId, conceptId, "STANDARD", 
			"30-JUN-96", "30-JUN-2001",  "SCIENTIFIC NAME", partyId);
		}
	}
}

	

	
	/**
	 * method that loads a party occurence into the party table.  First the method
	 * determines if the party already exists and if not then it is loaded
	 * @param partyName -- the party name should always be, for this class:
	 * 'USDA-PLANTS'
	 * @param partyContactInstructions -- the url for the usda plants
	 * @return plantpartyid -- the primary key value for the plant party
	 */
	private int insertPlantPartyInstance(String partyName, String partyContractInstructions)
	{
	 int partyId = 0; 
	 StringBuffer sb = new StringBuffer();
	 try
	 {
		//first see if the reference already exists
		boolean partyExists = plantPartyExists(partyName);
		//System.out.println("USDAPlantsLoader > partyExists: " + partyExists); 
		
		if (partyExists == true)
		{
			partyId = getPlantPartyId(partyName);
			//throw new Exception("reference already exists");
		}
		else
		{
			//insert the strata values
			sb.append("INSERT into PLANTPARTY (organizationName, contactInstructions, email) "
			+" values(?,?,?)");
			PreparedStatement pstmt = conn.prepareStatement( sb.toString() );
			// Bind the values to the query and execute it
 			pstmt.setString(1, partyName);
			pstmt.setString(2, partyContractInstructions);
			pstmt.setString(3, "plants@plants.usda.gov");
			
			//execute the p statement
 			pstmt.execute();
 			// pstmt.close();
			
			//get the partyId
			sb = new StringBuffer();
			sb.append("SELECT plantParty_id from PLANTPARTY where organizationName"
			+" like '"+partyName+"'");
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			int cnt = 0;
			while ( rs.next() ) 
			{
				partyId = rs.getInt(1);
				cnt++;
			}
		}
	 }
		catch (Exception e)
	 {
		 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
		 e.printStackTrace();
		 System.out.println("sql: " + sb.toString() );
		 //System.exit(0);
	 }
	 return(partyId);
	}

	/** 
	 * method that returns the party nameId for an organization name
	 *
	 * @param partyName -- the name of the party
	 * @return plantpartyId -- the plantPartyId
	 * 
	 */
		private int getPlantPartyId(String partyName)
	  {
		 	int partyId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT plantparty_id from PLANTPARTY where organizationName"
				+" like '"+partyName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					partyId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(partyId);
	 }
	 
	
	/** 
	 * method that returns the plantnameId based on a plant name
	 *
	 * @param plantName -- the name of the plant
	 * @return plantnameId -- the plantnameId
	 * 
	 */
		private int getPlantNameId(String plantName)
	  {
		 	int plantNameId = 0; 
			try
			{
		 		StringBuffer sb = new StringBuffer();
				sb.append("SELECT plantname_id from PLANTNAME where plantname"
				+" like '"+plantName+"'");
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( sb.toString() );
				int cnt = 0;
				while ( rs.next() ) 
				{
					plantNameId = rs.getInt(1);
					cnt++;
				}
			}
			catch (Exception e)
		 {
			 System.out.println("USDAPlantsLoader > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(plantNameId);
	 }
	 
	 
	
	/** 
	  * method that returns true if the party already exists 
		*/
		private boolean plantPartyExists(String partyName)
	  {
			boolean exists = false;
		 try
		 {
		 	StringBuffer sb = new StringBuffer();
			//get the refId
			sb = new StringBuffer();
			sb.append("SELECT plantparty_id from PLANTPARTY where organizationName"
			+" like '"+partyName+"'");
			
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery( sb.toString() );
			
			int cnt = 0;
			while ( rs.next() ) 
			{
				cnt++;
			}
			if (cnt > 0)
			{
				//System.out.println("USDAPlantsLoader > matching party:  " + cnt  );
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
				//System.out.println("USDAPlantsLoader > matching reference:  " + cnt  );
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
	*
	* @param conceptId
	* @param status
	* @param startDate
	* @param endDate
	* @param event
	* @param plantPartyId
	* @param plantLevel
 	*/	
	private int loadPlantStatusInstance(int conceptId, String status, 
	String startDate, String endDate, String event, int plantPartyId, 
	String plantParent, String plantLevel)
	{
		
		try
		{
			int parentConceptId = 0;
			
			//System.out.println("USDAPlantsLoader > conceptId: " + conceptId);
			//System.out.println("USDAPlantsLoader > status: " + status);
			//System.out.println("USDAPlantsLoader > startDate: "+ startDate);
			//System.out.println("USDAPlantsLoader > endDate: " + endDate);
			//System.out.println("USDAPlantsLoader > event: " + event);
			//System.out.println("USDAPlantsLoader > plantPartyId: " + plantPartyId);
			//System.out.println("USDAPlantsLoader > plantParent: " + plantParent);
			
			//get the plant parentId
			if (plantParent !=null && plantParent.length() > 1 )
			{
				String s1 = "select plantConcept_id from plantConcept where plantDescription = '"
				+plantParent+"'";
				
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery( s1 );
				int cnt = 0;
				while ( rs.next() ) 
				{
					parentConceptId = rs.getInt(1);
					cnt++;
				}
				
				if (cnt > 1 )
				{
					System.out.println("USDAPlantsLoader > warning: to many parent concept id's");	
				}
				
				String s = "insert into PLANTSTATUS "
				+"(plantConcept_id, plantconceptstatus, startDate, stopDate, "
				+" plantPartyComments, plantParty_id, plantParentName, "
				+" plantParentConcept_id, plantlevel )  values(?,?,?,?,?,?,?,?,?) ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				//bind the values
				pstmt.setInt(1, conceptId);
				pstmt.setString(2, status);
				pstmt.setString(3, startDate);
				pstmt.setString(4, stopDate);
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
				pstmt.setInt(8, parentConceptId);
				pstmt.setString(9, plantLevel);
							
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
				
			}
			else
			{
			
				String s = "insert into PLANTSTATUS "
				+"(plantConcept_id, plantconceptstatus, startDate, stopDate, "
				+" plantPartyComments, plantParty_id, plantParentName, plantlevel)  "
				+ "values(?,?,?,?,?,?,?,?,?) ";
				PreparedStatement pstmt = conn.prepareStatement(s);
				//bind the values
				pstmt.setInt(1, conceptId);
				pstmt.setString(2, status);
				pstmt.setString(3, startDate);
				pstmt.setString(4, stopDate);
				pstmt.setString(5, event);
				pstmt.setInt(6, plantPartyId);
				pstmt.setString(7, plantParent);
				pstmt.setString(8, plantLevel);
			
				boolean results = true;
				results = pstmt.execute();
				pstmt.close();
			}
		
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
	int concept_id, String usage, String startDate, String stopDate, 
	String reference)
	{
		try
		{
			String s = "insert into PLANTUSAGE (plantName, plantName_id, plantConcept_id, "+
				" plantNameStatus, usageStart, usageStop)  values(?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setString(1, concatenatedName);
			pstmt.setInt(2, name_id);
			pstmt.setInt(3, concept_id);
			pstmt.setString(4, usage);
			pstmt.setString(5, startDate);
			pstmt.setString(6, stopDate);
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
 	* method that loads a new instance of a 
 	* plant concept - name usage returns to 
 	* the calling method the primary key for 
 	* that concept - name usage
	* 
	* @param concatenatedName
	* @param name_id
	* @param concept_id
	* @param plantNameStatus -- standard or non standard
	* @param usageStart
	* @param usageStop
	* @param classSystem -- common name or code or scientific name
 	*/	
	private int loadPlantUsageInstanceNew(String concatenatedName, int name_id, 
	int concept_id, String usage, String startDate, String stopDate, 
	String classSystem, int partyId)
	{
		String s = null;
		try
		{
			s = "insert into PLANTUSAGE (plantName, plantName_id, plantConcept_id, "+
				" plantNameStatus, usageStart, usageStop, classSystem, plantParty_id) "
				+" values(?,?,?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setString(1, concatenatedName);
			pstmt.setInt(2, name_id);
			pstmt.setInt(3, concept_id);
			pstmt.setString(4, usage);
			pstmt.setString(5, startDate);
			pstmt.setString(6, stopDate);
			pstmt.setString(7, classSystem);
			pstmt.setInt(8, partyId);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();	
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			System.out.println("sql: " + s);
			System.out.println("concatenatedName: " + concatenatedName );
			System.out.println("plantNameId: " + name_id );
			System.out.println("plantConceptId: " + concept_id );
			System.out.println("usage: " + usage );
			System.out.println("startDate" + startDate  );
			System.out.println("stopDate" + stopDate  );
			System.out.println("classSystem" + classSystem  );
			System.out.println("partyId" + partyId  );
			//System.out.println("acceptedSynonym " + acceptedSynonym  );
			
			e.printStackTrace();
			//System.exit(0);
		}
		return(0);	
	}
	
	/**
	 * this is an overloaded method of the above method
	 ** method that loads a new instance of a 
 	 * plant concept - name usage returns to 
 	 * the calling method the primary key for 
 	 * that concept - name usage
	 * 
	 * @param concatenatedName
	 * @param name_id
	 * @param concept_id
	 * @param plantNameStatus -- standard or non standard
	 * @param usageStart
	 *  @param usageStop
	 * @param classSystem -- common name or code or scientific name
	 * @param acceptedSynonym -- if the plant name is no longer standard
	 * 		then this refers to the synonym
	 */
	 private int loadPlantUsageInstanceNew(String concatenatedName, int name_id, 
	int concept_id, String usage, String startDate, String stopDate, 
	String classSystem, int partyId, String acceptedSynonym)
	{
		String s = null;
		try
		{
			s = "insert into PLANTUSAGE (plantName, plantName_id, plantConcept_id, "+
				" plantNameStatus, usageStart, usageStop, classSystem, plantParty_id, "
				+"acceptedSynonym) "
				+" values(?,?,?,?,?,?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setString(1, concatenatedName);
			pstmt.setInt(2, name_id);
			pstmt.setInt(3, concept_id);
			pstmt.setString(4, usage);
			pstmt.setString(5, startDate);
			pstmt.setString(6, stopDate);
			pstmt.setString(7, classSystem);
			pstmt.setInt(8, partyId);
			pstmt.setString(9, acceptedSynonym);
			boolean results = true;
			results = pstmt.execute();
			pstmt.close();	
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			System.out.println("sql: " + s);
			System.out.println("concatenatedName: " + concatenatedName );
			System.out.println("plantNameId: " + name_id );
			System.out.println("plantConceptId: " + concept_id );
			System.out.println("usage: " + usage );
			System.out.println("startDate" + startDate  );
			System.out.println("stopDate" + stopDate  );
			System.out.println("classSystem" + classSystem  );
			System.out.println("partyId" + partyId  );
			System.out.println("acceptedSynonym " + acceptedSynonym  );
			e.printStackTrace();
			//System.exit(0);
		}
		return(0);	
	}
		
		

	/**
 	* method that loads a new instance of a 
 	* plant concept then returns to the calling
 	* method the primary key for that concept
 	*/	
	private int loadPlantConceptInstance(int plantNameId, int plantReferenceId, 
	String plantDescription, String plantCode)
	{
		int plantConceptId = -999;
		try
		{
			String s = "insert into PLANTCONCEPT (plantname_id, plantDescription, "
			+" plantreference_id, plantCode ) values(?,?,?,?) ";
			PreparedStatement pstmt = conn.prepareStatement(s);
			//bind the values
			pstmt.setInt(1, plantNameId);
			pstmt.setString(2, plantDescription);
			pstmt.setInt(3, plantReferenceId);
			pstmt.setString(4, plantCode);
			pstmt.execute();
			pstmt.close();	
		
			//now get the associated primary key value
			String s1 = "select plantconcept_id from plantconcept where plantDescription = '"
			+plantDescription+"'";
			
			PreparedStatement pstmt1 = conn.prepareStatement(s1);
			ResultSet rs = pstmt1.executeQuery();
			int cnt = 0;
			while	( rs.next() )
			{
				cnt++;
				plantConceptId = rs.getInt(1);
				//System.out.println("USDAPlantsLoader >>> plantconcept_id: " + plantConceptId);
			}
			if (cnt > 1)
			throw new Exception("multiple plantconceptID's found");
			pstmt1.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() 
			+ "\n conceptId: " +  plantConceptId 
			+"\n plantDescription: " + plantDescription);
			e.printStackTrace();
			//System.exit(0);
		}
		return(plantConceptId);
	}






	/**
 	* method that returns true if an instance of a plant
 	* concept exists that is identical to the input critreria
	*
	* @param plantDescription -- the concatenated name
	* @param tsnValue -- the concept code
	* @return exists -- boolean 
 	*/	
	private boolean plantConceptExists(String plantDescription, String tsnValue)
	{
		boolean result = false;
		try
		{
			//now get the associated primary key value
			String s1 = " select plantConcept_id from plantConcept where plantDescription = '"
			+plantDescription+"'";
			
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
				// SEND A WARNING IF THE COUNT IS MORE THAN ONE
				if ( cnt > 1 )
				{
					System.out.println("USDAPlantsLoader > more than on matching concept");
				}
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
	*
	* @param refId -- the reference Id
	* @param plantName -- the plantname (w/o the author bit)
	* @param dateEntered -- the data thate the plant name is to be entered
	* @return plantNameId -- the plant name Id assocaited with this plant
	*
 	*/	
	private int loadPlantNameInstance(int refId, String plantName, String dateEntered)
	{
		int plantNameId = -999;
		try
		{
			
			
			// Get Primary key for table		
			plantNameId =  
				(int) Utility.dbAdapter.getNextUniqueID(conn, "PLANTNAME", "plantname_id");
				
			PreparedStatement pstmt = conn.prepareStatement(
				 "insert into PLANTNAME (plantreference_id, plantName,  dateEntered, "
				 + "plantname_id) values(?,?,?,?) " );

			//bind the values
			pstmt.setInt(1, refId);
			pstmt.setString(2, plantName);
			pstmt.setInt(4, plantNameId);
			pstmt.executeUpdate();
			pstmt.close();	
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
 	* method to remove an element from the hashtable that contains the plants
	* 
 	*/	
	private void removeSinglePlantInstance(Hashtable plantsData, int hashKeyNum)
	{
		try
		{
			plantsData.remove(""+hashKeyNum);
		}
		catch (Exception e)
		{
			System.out.println("Exception " + e.getMessage() );
			e.printStackTrace();
		}
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
		else if ( fileVector.elementAt(i).toString().startsWith("concatenatedLongName") )
		{
			plantInstance.put("concatenatedLongName", 
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

