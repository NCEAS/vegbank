/**
 * class that was designed as a model for a structured data file, which is a 
 * file the has a very well structured header file that can be used to develop
 * a project XML file that is to be ingested into the plots database 
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:42 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;

import vegclient.framework.*;

public class StructuredDataFile 
{
	
	protected Vector headerVector = new Vector();
	protected Vector dataVector = new Vector();
	protected Hashtable headerHash = new Hashtable();
	//the vector in which to store the collumn attributes (called fields in
	// the header)
	protected Vector dbAttributesVec = new Vector();
	//a hashtable with the attribute name as the key and the position as the value
	protected Hashtable dbAttributesHash = new Hashtable();
	
	/**
	 * Creates a new instance of StructuredDataFile with the given file.
   * @param fileName -- the name of the structured data file.
	 */	
	
	public StructuredDataFile(String fileName)
	{
		//get the header of the file into a vector
		headerVector = getHeaderVector(fileName);
		//and then organize into a hashtable the elements in the header
		if (headerVector.size() < 1)
		{
			System.out.println("There is no header in the file:" + fileName);
		}
		else
		{
			headerHash = getHeaderHash(headerVector);
		}
		//get all the data from the file (beneath the vector)  into a vector
		dataVector = getDataVector(fileName);
		//validate that the number of attributes listed in the header matches the
		// number of columns
		if ( dbAttributesVec.size() > 0 )
		{
			if (validateCorrectAttributeNumber(dbAttributesVec, dataVector ) == false)
			{
				System.out.println(" mismatching attribute fields "+fileName);
			}
		}
		else
		{
			System.out.println(" no attributes in file name: "+fileName);
		}
		
		
	}


	/**
	 * method that returns true if the number of attributes in the header matches
	 * the number of columns in the data file
	 * @param dbAttributesVec
	 * @param dataVector 
	 */
		protected boolean validateCorrectAttributeNumber(Vector dbAttributesVec,
		Vector dataVector)
		{
			//get the first line form the data and count the columns
			String s = (String)dataVector.elementAt(0);
			StringTokenizer t = new StringTokenizer(s, ",");
			//the number of elements 
			int elementNum = 0;
			while ( t.hasMoreTokens() )
			{
				String buf = t.nextToken();
				elementNum++;
			}
			//see if there is a match
			if (elementNum == dbAttributesVec.size() )
			{
				return (true);
			}
			else
			{
				return(false);
			}
		}
	
	/**
	 * method that organizes the header elements in the header vector into the 
	 * hashtable structure which consists of the following keys and the associated
	 * values:
	 *	Type - the file type (Project, Site, Species etc.)
	 *	Version - the software version (1.0)
	 *	Format - the format (fixed, free)
	 * 
	 * @param headerVector -- the vector containing the header
	 * the elements in this vector look like:
	 * # Type: Site
	 * # Version: 1.0
	 * # Format: free
	 * # Field: 1 altValue fathoms
	 * # Field: 2 slopeAspect degrees
	 * # Field: 3 slopeGradient degrees
	 * # Field: 4 soilPH
	 * # Field: 5 state
	 * # Field: 6 surfGeo
	 * # Delimeter: space
	 * # End:
	 *
	 */
		private Hashtable getHeaderHash(Vector headerVector)
		{
			Hashtable ht = new Hashtable();
			for (int i=0; i<headerVector.size(); i++)
			{
				//handle those header elements that can only have a single instance in
				// the header like Type, Delimeter, Version etc.
				//TYPE
				if ( headerVector.elementAt(i).toString().indexOf("Type")>0 )
				{
					String element = getHeaderElement( headerVector.elementAt(i).toString() );
					ht.put("Type", element );
				}
				//DELIMTER
				if ( headerVector.elementAt(i).toString().indexOf("Delimeter")>0 )
				{
					String element = getHeaderElement( headerVector.elementAt(i).toString() );
					ht.put("Delimeter", element );
				}
				//VERSION
				if ( headerVector.elementAt(i).toString().indexOf("Version")>0 )
				{
					String element = getHeaderElement( headerVector.elementAt(i).toString() );
					ht.put("Version", element );
				}
				//FORMAT
				if ( headerVector.elementAt(i).toString().indexOf("Format")>0 )
				{
					String element = getHeaderElement( headerVector.elementAt(i).toString() );
					ht.put("Format", element );
				}
				
				//handle the dbattributes field next
				if ( headerVector.elementAt(i).toString().indexOf("Field")>0 )
				{
					String element = getHeaderAttributeName( headerVector.elementAt(i).toString() );
					int position = getHeaderAttributePosition(  headerVector.elementAt(i).toString() );
					
					//put the name and poition into a hashtable with the key ~ element
					
					dbAttributesHash.put(element, ""+position);
					dbAttributesVec.addElement( element );
				}
				
			}
			ht.put("dbAttributes", dbAttributesVec);
			return(ht);
		}
	
	
	
	
	 
	
	/**
	 * method that returns the header element value and can be used to obtain
	 * those element values including: Type, Version etc.
	 * @param headerString -- a line from the header that looks like:
	 * # Type: Site
	 * @return element -- a string that has all  the extraneous stuff parsed like:
	 * Site
	 */
	 private String getHeaderElement(String headerString)
	 {
		  String returnString = null;
		try 
		{ 
		 	StringTokenizer t = new StringTokenizer(headerString, ":");
		 	String buf = t.nextToken();
		 	returnString = t.nextToken().trim();
		}
		 catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
		 return(returnString);
	 }
	
	
	
	private int getHeaderAttributePosition(String headerString)
	 {
		int returnInt = 0;
			try 
		{ 
		
		 StringTokenizer t = new StringTokenizer(headerString, ":");
		 String buf = t.nextToken();
		 buf = t.nextToken().trim(); // the field number and rest of line
		 //get the text after the number and space
		 StringTokenizer t2 = new StringTokenizer(buf, " ");
		 buf = t2.nextToken().trim();
		 
		 //System.out.println("X "+buf);
		 returnInt = Integer.parseInt(buf);
		 
		}
		 catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
		
		return(returnInt);
	 }
	
	
	/**
	 *
	 * method that returns the dbAttribute name from the header
	 *
	 */
	private String getHeaderAttributeName(String headerString)
	 {
		  String returnString = null;
		try 
		{ 
		
		 StringTokenizer t = new StringTokenizer(headerString, ":");
		 String buf = t.nextToken();
		 buf = t.nextToken().trim(); // the field number and the rest of line
		 //get the text after the number and space
		 StringTokenizer t2 = new StringTokenizer(buf, " ");
		 buf = t2.nextToken().trim();
		 returnString  = t2.nextToken().trim(); 
		}
		 catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
		 return(returnString);
	 }
	
	
	/**
	 * method that returns a vector that is extracted from the 'dataVector'
	 * which is a many-many array based on the column number
	 */
	public Vector getColumnElements( int tokenPosition )
	{
		//System.out.println("X "+tokenPosition);
		Vector returnVec = new Vector();
		String token= null;
		try
		{
		for (int i=0; i<dataVector.size(); i++)
		{
			String s = (String)dataVector.elementAt(i);
			//System.out.println("X"+s);
			if (s != null) 
			{ 
				StringTokenizer t = new StringTokenizer(s, ",");
				int ii=1;
				while (ii<=tokenPosition) 
				{
					if ( t.hasMoreTokens() ) 
					{
			 			token=t.nextToken();
						ii++;
					}
					else 
					{
						token="nullToken";
						ii++;
					}
				}
			}
			returnVec.addElement(token );
		}
		}
		 catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
			return(returnVec);
	}


	
	/**
	 * method that returns a vector containing the header of a file
	 * @param fileName -- the name of the filename that is passed to the class
	 * the elements in this vector look like:
	 * # Type: Site
	 * # Version: 1.0
	 * # Format: free
	 * # Field: 1 altValue fathoms
	 * # Field: 2 slopeAspect degrees
	 * # Field: 3 slopeGradient degrees
	 * # Field: 4 soilPH
	 * # Field: 5 state
	 * # Field: 6 surfGeo
	 * # Delimeter: space
	 * # End:
	 */
	private Vector getHeaderVector(String fileName)
	{
		Vector localVector = new Vector();
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) 
			{
				if (s.startsWith("#") )
				{
					//System.out.println(s);	
					localVector.addElement(s);
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
		return(localVector);
	}
	
	/**
	 * method that stores all the data columns in a vector for the given file
	 *
	 */
	private Vector getDataVector(String fileName)
	{
		Vector localVector = new Vector();
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) 
			{
				if (! s.startsWith("#") )
				{
					//System.out.println(s);	
					localVector.addElement(s);
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
		return(localVector);
	}
	
	
	/**
	 * method that returns a string defining the type of data file
	 */
	 protected String getFileType()
	 {
		 String fileType = null;
		 fileType = (String)headerHash.get("Type");
		 return(fileType);
	 }
	 /**
	 * method that returns a string defining the delimeter of data file
	 */
	 protected String getDelimeter()
	 {
		 String delimeter = null;
		 delimeter = (String)headerHash.get("Delimeter");
		 return(delimeter);
	 }
	 /**
	 * method that returns a string defining the version of data file
	 */
	 protected String getVersion()
	 {
		 String version = null;
		 version = (String)headerHash.get("Version");
		 return(version);
	 }
	 /**
	 * method that returns a string defining the format of data file
	 */
	 protected String getFormat()
	 {
		 String format = null;
		 format = (String)headerHash.get("Format");
		 return(format);
	 }
	 
	 
	 
		
	/**
   * the main routine used to test the StructureDataFile class which 
	 * interacts with the vegclass database.
   * <p>
   * Usage: java StructureDataFile <filename ....>
   *
   * @param filename to be proccessed
   *
   */
	 
  static public void main(String[] args) 
	{
  	try 
		{
			//for now just allow the user to insert the plot
			if (args.length != 1) 
			{
				System.out.println("Usage: java StructuredDataFile [fileName]  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String fileName=args[0];

				StructuredDataFile structuredDataFile = new StructuredDataFile(fileName);
				System.out.println( "file type: "+structuredDataFile.getFileType());
				System.out.println( "Version: "+structuredDataFile.getVersion());
				System.out.println( "Delimiter: "+structuredDataFile.getDelimeter() ) ;
				System.out.println( "Format: "+structuredDataFile.getFormat() ) ;
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	


}
