import java.io.IOException;
import java.io.*;
import java.util.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;


/**
 * this class will convert legcy data 'packages' into the xml format
 * that is required for injestion into the plots database
 *
 * @Author John Harris
 * @Version May 2001
 */


public class LegacyDataWizard 
{

public String styleSheet="legacyDataDescription.xsl";
public String attributeFile="attFile.txt"; //the resuting transformed file
public Vector fileVector = new Vector();
public Hashtable attributeHash = new Hashtable();
public Hashtable constraintHash = new Hashtable();


/**
 * Main method that runs the Legacy Data Formatter
 * required as input are an xml file that defines the 
 * data strcture and an action
 */
public static void main(String[] args) 
{
	if (args.length != 1) 
	{
		System.out.println("Usage: java LegacyDataWizard  [XML] \n");
		System.exit(0);
	}
	else 
	{
		String inputXml=null;
		inputXml=args[0];
		//call the method to convert the data package to xml
		LegacyDataWizard ldw =new LegacyDataWizard();  
		ldw.transformDataPackage(inputXml);
	}
}

/**
 * method that steps thru the processes associated with transforming the 
 * data package to an xml file -- currently the functionality includes
 * parseing the xml file that describes the data package and then parsing
 * the elements out of the data package and placing them into the xml file
 * which can be used to load the database
 * @param inputXml
 */
public void transformDataPackage (String inputXml) 
{
	try 
	{
		System.out.println(inputXml);
	
		//call the method to parse the xml file into appropriate attributes and
		// store them in appropriate objects
		parseDataPackageXml(inputXml);
		
		//as a test that this works print to the system the object contents
		System.out.println("ATTRIBUTE HASH CONTENTS ON NEXT LINE" );
		for (Enumeration e = attributeHash.elements() ; e.hasMoreElements() ;) 
		{
         System.out.println(e.nextElement());
 		}
		System.out.println("\n");
		System.out.println("CONSTRAINED ATTRIBUTE HASH CONTENTS ON NEXT LINE" );
		for (Enumeration e = constraintHash.elements() ; e.hasMoreElements() ;) 
		{
         System.out.println(e.nextElement());
 		}
		System.out.println("\n");
		
		//run thru the qaqc methods
		//qualityChecker();
		
		//parse the elements from the ascii files in the correct
		//order and print out as xml file
		AsciiElementParser aep = new AsciiElementParser();
		aep.parsePackageElements(attributeHash, constraintHash);
		
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: LegacyDataWizard.transformDataPackage "
		+e.getMessage() );
		e.printStackTrace();
	}
}

/**
 * method that reads the xml file that reads the xml file 
 * that defines the contents and structure of the ascii data 
 * package and arranges a series of hash tables containing
 * details of the structure of the ascii package that can be 
 * used in the ascii element parser class to parse the data 
 * for composition into xml file(s)
 *
 * @param inputXml
 */	
private void parseDataPackageXml(String inputXml)
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
		//System.out.println(s);
		fileVector.addElement(s);
	}
	//parse all the attributes and constraints
	//from the xml file into a series of hash tables
	//which describe the structure and constraints 
	//inherent in a data package
	parseXmlDataFileElements();
}




/**
 * method that takes as input the xml file that describes the
 * data legacy ascii package structure and composition and 
 * constructs a number of hashtables that are used tp parse the 
 * ascii tables into xml files that can be used to insert or 
 * update the database.  Although a bit of a hack, (having a 
 * strcture that may not serve all purposes) the structure of 
 * these hash tables are as follows:
 *
 *>attribute hash: key=attributeName (as used in the database) 
 * followed by a string that is pipe delimeted and contains 
 * the following tokens in the following order:
 * 1] attributeName (as used in the DB)
 * 2] fileName (including path on local file system)
 * 3] column position (in the ascii table as delimeted by the delimeter)
 * 4] fileAttributeName (name of the attribute as used in the ascii file)
 * 5] fileKey (the key within the file that constrains the file -- 
 *			example: authorPlotCode may be the filekey for a file that 
 * 			contains site information including coordinates and slope 
 *			attributes )
 *	method should be broken
 * into many sub methods to get the thing working the correct way 
 */	
private void parseXmlDataFileElements()
{
	String fileName=null;
	String fileTheme=null;
	String attributeDelimeter=null;
	String constraintFileName=null;
	String constraintThemeName=null;
	String constraintAttributeName=null;
	String constraintCardnality=null;
	boolean constrained=false;
	int i=0;
	
	while ( i<fileVector.size() ) 
	{
		
		//look for a the beginning of a file description
		if (fileVector.elementAt(i).toString().startsWith("fileName") ) 
		{
			fileName=pipeStringTokenizer( (String)fileVector.elementAt(i),2);
			attributeDelimeter=pipeStringTokenizer( (String)fileVector.elementAt(i+1),2);
			fileTheme=pipeStringTokenizer( (String)fileVector.elementAt(i+2),2);
			
			//print the file related information
			System.out.println("fileName: "+fileName+"\nattributeDelimeter: "+
				attributeDelimeter+"\nfileTheme: "+fileTheme);
			
			//if the file has constraints deal with those before the attributes
			if (fileVector.elementAt(i+3).toString().startsWith("constraint.fileName"))
			{
				constrained=true;
				constraintFileName=pipeStringTokenizer((String)fileVector.elementAt(i+3),2);
				constraintThemeName=pipeStringTokenizer((String)fileVector.elementAt(i+4),2);
				constraintAttributeName=pipeStringTokenizer((String)fileVector.elementAt(i+5),2);
				constraintCardnality=pipeStringTokenizer((String)fileVector.elementAt(i+6),2);
				
				addConstraintToHash(
						fileName,
						constraintFileName,
						constraintThemeName,
						constraintAttributeName,
						constraintCardnality
				);
				
				int breaker = 0;  //turns to one when while loop gets past the attributes
				int ii=i+4; //current location in the vector
				while ( ii < fileVector.size() && breaker==0) 
				{
					//if get to another constrained file - break from this loop
					if ( fileVector.elementAt(ii).toString().startsWith("constraint.fileName") )
					{
						breaker = 1;
					}
					if ( fileVector.elementAt(ii).toString().startsWith("attributeName") )
					{
					//	System.out.println("CONSTRAINED FILE "+ fileVector.elementAt(ii).toString());
						//stick the attribute into the hash table
						addAttributeToHash( 
							pipeStringTokenizer((String)fileVector.elementAt(ii),3),
							fileName,
							pipeStringTokenizer((String)fileVector.elementAt(ii),4),
							pipeStringTokenizer((String)fileVector.elementAt(ii),2),
							constraintAttributeName,
							constrained
						);	
					}
					ii++;
				}
			}
			
			//else just deal with the attributes
			else if (fileVector.elementAt(i+3).toString().startsWith("attributeName"))
			{
				constrained=false;
				int ii=i+3; //current location in the vector
				while ( ii < fileVector.size() && 
				fileVector.elementAt(ii).toString().startsWith("attributeName") ) 
				{
					System.out.println("NON-CONFILE "+fileVector.elementAt(ii).toString());
					addAttributeToHash( 
							pipeStringTokenizer((String)fileVector.elementAt(ii),3),
							fileName,
							pipeStringTokenizer((String)fileVector.elementAt(ii),4),
							pipeStringTokenizer((String)fileVector.elementAt(ii),2),
							"fileKey",
							constrained
						);
					ii++;
				}
			}
		}
		i++;
	}
}


/**
 * method to add constraints to a the hashtable
 * which stores the information about the 
 * constraining relationships within the ascii
 * data package
 *
 * @param fileName - the name of the file that has the constraint
 * @param constrainingFileName
 * @param constrainingThemeName
 * @param constraingAttributeName
 * @param constrainingCadnality
 *
 */	
private void addConstraintToHash(String fileName, String constrainingFileName, 
	String constrainingThemeName, String constrainingAttributeName, 
	String constrainingCardnality)
{
	//check to see if the constraint already exists
	if ( constraintHash.get(constrainingAttributeName) != null  )
	{
		System.out.println("constraint key already exists");
		constrainingAttributeName=constraintKeyValue(constrainingAttributeName);
	}
	String keyName=constrainingAttributeName;
	String constraintInfo=fileName+"|"+constrainingFileName+"|"+constrainingThemeName+
	"|"+constrainingAttributeName+"|"+constrainingCardnality;

	constraintHash.put(keyName, constraintInfo);
}


/**
 * method to add an attribute entry
 * into the hashtable which stores info
 * about the attributes in a data package
 *
 * @param attributeName
 * @param fileName
 * @param columnPosition
 * @param fileAttributeName
 * @param fileKey
 * @param constrained - is this attribute constrained?
 *
 */	
private void addAttributeToHash(String attributeName, String fileName, 
	String columnPosition, String fileAttributeName, String fileKey, 
	boolean constrained)
{
//System.out.println("***"+constrained);
  //make a string to contain the relevant attribute information following the
	// order above outlijned in the params
	String attributeInfo=attributeName+"|"+fileName+"|"+columnPosition+"|"+
		fileAttributeName+"|"+fileKey;
		
	//make a string where the filename is concatenated to the attributeName
	//so that there is never duplication where an attributeName key ovewrites
	// another key
	String keyName=attributeName;
		
	//make a different key name if the attribute is in a constrained file
	if (constrained==true) 
	{
		System.out.println("existence of a key: "+attributeKeyValue(keyName+".constrained") );
		attributeHash.put(attributeKeyValue(keyName+".constrained"), attributeInfo);
		//attributeHash.put(keyName+".constrained", attributeInfo);
	}
	else 
	{
		attributeHash.put(keyName, attributeInfo);
	}
}




/**
 * method that returns the keyname with an
 * integer appended to it if the key already 
 * otherwise the base key is returned, and if 
 * base + appended integer exists te integer is
 * incremented by one and then appended
 */	
public String attributeKeyValue (String keyBase)
{
	System.out.println("looking for key base: "+keyBase);
	if (keyBase != null)
	{
		if ( (String)attributeHash.get(keyBase) != null )
		{
			//then there may be more with the same base and a number appended
			int i=1;
			while (i<10 )
			{
				if ( (String)attributeHash.get(keyBase+"."+i) != null )  
				{
					System.out.println("CYCLING");
				}
				else
				{
					return(keyBase+"."+i);
				}
				i++;
			}
			return(keyBase+"."+i);
		}
		else
		{
			return(keyBase);
		}
	}
	else
	{
		System.out.println("nullValue passed to: attributeKeyValue method");
		return(keyBase);
	}
}


/**
 * method that returns the keyname with an
 * integer appended to it if the key already 
 * otherwise the base key is returned, and if 
 * base + appended integer exists te integer is
 * incremented by one and then appended
 */	
public String constraintKeyValue (String keyBase)
{
	System.out.println("looking for key base: "+keyBase);
	if (keyBase != null)
	{
		if ( (String)constraintHash.get(keyBase) != null )
		{
			//then there may be more with the same base and a number appended
			int i=1;
			while (i<10 )
			{
				if ( (String)constraintHash.get(keyBase+"."+i) != null )  
				{
					System.out.println("CYCLING the constraint key value");
				}
				else
				{
					return(keyBase+"."+i);
				}
				i++;
			}
			return(keyBase+"."+i);
		}
		else
		{
			return(keyBase);
		}
	}
	else
	{
		System.out.println("nullValue passed to: constraintKeyValue method");
		return(keyBase);
	}
}




/**
 * method to tokenize elements from a pipe delimeted string
 */	
public String pipeStringTokenizer(String pipeString, int tokenPosition)
{
	//System.out.println("%%%%% "+pipeString+" "+tokenPosition);
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








/**
 * method that checks the quality of the xml file that relates the ascii
 * files.  The methos will check the cardinality and integrety constraints
 * as well as defining which portions of the database should be populated
 * ie highest level = project, lowest level = observations 
 */	
private void qualityChecker()
{
	String constraintKey=null;
	String constraintString=null;
	String constrainedFile=null;
	String constrainingFile=null;
	String constrainedAttriubute=null;
	String constrainingAttriubute=null;
	Vector constrainedValues= new Vector();
	Vector constrainingValues = new Vector();
	try {
		//grab the constraints out of the constraint hash
		Enumeration constraintList = constraintHash.keys();
		while (constraintList.hasMoreElements())
		{
			//the tokenized string containing the constraint tokens
  		constraintKey= (String)constraintList.nextElement();
			constraintString=(String)constraintHash.get(constraintKey);

			//grab the components of the constraint file
 			constrainedFile=pipeStringTokenizer(constraintString,1);
			constrainingFile=pipeStringTokenizer(constraintString,2);
			constrainedAttriubute=pipeStringTokenizer(constraintString,4);
			//for now the constrained and constraining attributes must have the same
			// values
			constrainingAttriubute=constrainedAttriubute;
		
			int constrainedColumn = Integer.parseInt( pipeStringTokenizer(
				(String)attributeHash.get(constrainedAttriubute+".constrained"),3) );
			int constrainingColumn = Integer.parseInt( pipeStringTokenizer(
				(String)attributeHash.get(constrainingAttriubute),3) );
			
			System.out.println("constrained file: "+constrainedFile);
			System.out.println("constrainting file: "+constrainingFile);
			System.out.println("constrained column: "+constrainedColumn);
			System.out.println("constraining column: "+constrainingColumn);
	
			//open the files
			BufferedReader constrainedFileIn = 
				new BufferedReader(new FileReader(constrainedFile), 8192);
			BufferedReader  constrainingFileIn= 
				new BufferedReader(new FileReader(constrainingFile), 8192);
		
			//read the columns into vectors
			String s=null;
			String s1=null;
			//read in the constrained values
			while ((s=constrainedFileIn.readLine()) != null) {
				constrainedValues.addElement(pipeStringTokenizer(s, constrainedColumn));
			}
			//read in the constraining values 
			while ((s1=constrainingFileIn.readLine()) != null) {
				constrainingValues.addElement(pipeStringTokenizer(s1, constrainingColumn));
			}
		
			//verify that the constrained file attribute exists in the constraininf file 
			for (int i=0; i<constrainedValues.size(); i++) {
				if (constrainingValues.contains(constrainedValues.elementAt(i))==true )
				{
					String test=null;
					//System.out.println(constrainedValues.elementAt(i));
				}
				else 
				{
					System.out.println("missing");
				}
			}
		
		
		
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: LegacyDataWizard.qualityChecker "
		+e.getMessage() );
		e.printStackTrace();
	}
	

}




}

