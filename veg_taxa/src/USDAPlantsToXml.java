/**
 *  '$RCSfile: USDAPlantsToXml.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2002-07-01 20:52:59 $'
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


/**
 * 
 * this class is intended to transform the USDA 'plants' 
 * list into an xml file that can be loaded directly to 
 * the concept-based-taxonomy database that acts as an
 * auxiliary database to the plots database
 *
 */


public class USDAPlantsToXml 
{


public Vector fileVector = new Vector();
public Hashtable attributeHash = new Hashtable();


/**
 * Main method to run the the class
 * and transform the plants list into
 * the xml file
 *
 */
public static void main(String[] args) 
{
	if (args.length != 1) 
	{
		System.out.println("Usage: java USDAPlantsToXml  [plantList] \n");
		System.exit(0);
	}
	else 
	{
		String inputPlantList=null;
		inputPlantList=args[0];
		//call the method to convert the data package to xml
		USDAPlantsToXml px =new USDAPlantsToXml();  
		px.transformPlantList(inputPlantList);
	}
}



/**
 * method that transforms the input file 
 * into the structure needed to be output 
 * as an xml document
 */
public void transformPlantList(String inputPlantList) 
{
	try 
	{
		System.out.println("USDAPlantsToXml > reading the input file: " + inputPlantList);
		Vector v = fileToVector(inputPlantList);
		
		System.out.println("USDAPlantsToXml > parsing the input");
		Hashtable h = parsePlantElements(v);
		
		System.out.println("USDAPlantsToXml > writing the output");
		this.outputXml(h);
		
	}
	catch( Exception e ) {
		System.out.println(" failed in: USDAPlantsToXml.transformPlantList "
		+e.getMessage() );
		e.printStackTrace();
	}
}

/**
 * method that parses the elements from 
 * the plant lis and returns a hashtable
 * that contains all the plant instances
 */
private Hashtable parsePlantElements(Vector fileVector)
{
	Hashtable plantInstances = new Hashtable();
	
	for (int i=0; i<fileVector.size(); i++) 
	{
		//put each plant instance hash into the plantInstances hash
		plantInstances.put(""+i, (Hashtable)parseSinglePlantInstance(fileVector.elementAt(i).toString()) 
		);
		//System.out.println( fileVector.elementAt(i).toString() );
	}
	return(plantInstances);
}


/**
 * method that returns a hash table that contains the 
 * elements of a single plant instance which, in the 
 * case of the usda plants list, equates to a single 
 * line from the file
 */

private Hashtable parseSinglePlantInstance(String fileVectorLine)
{
	Hashtable singlePlantInstance = new Hashtable();
	if (fileVectorLine != null)
	{
		if (acceptedPlantInstance(fileVectorLine) == true )
		{
			//System.out.println(" accepted usage :");
			singlePlantInstance.put("plantCode", plantCode(fileVectorLine) );
			singlePlantInstance.put("concatenatedName", plantConcatenatedName(fileVectorLine) );
			singlePlantInstance.put("familyName", plantFamilyName(fileVectorLine) );
			singlePlantInstance.put("commonName", plantCommonName(fileVectorLine) );
			singlePlantInstance.put("acceptence", "accepted" );
		}
		else 
		{
			//System.out.println("non accepted usage ");
			singlePlantInstance.put("plantCode", plantCode(fileVectorLine) );
			singlePlantInstance.put("concatenatedName", plantConcatenatedName(fileVectorLine) );
			singlePlantInstance.put("familyName", plantFamilyName(fileVectorLine) );
			singlePlantInstance.put("synonymName", plantSynonymName(fileVectorLine) );
			singlePlantInstance.put("acceptence", "not accepted" );
		}
	}
	return(singlePlantInstance);
}



/**
 * method that takes as input the hashtable containing the 
 * plant instances and prints out an xml document - that 
 * corresponds to some dtd
 */
private void outputXml(Hashtable plantInstances)
{
	try 
	{
		Hashtable singlePlantInstance = new Hashtable();
		StringBuffer outData = new StringBuffer();
		PrintStream out = new PrintStream(new FileOutputStream("outfile.xml"));
		//print the header
		outData.append(xmlHeader()).toString();
	
		//print each instance of a plant taxon
		for (int i=0; i<plantInstances.size(); i++) 
		{
			//grab the corresponding plant instance 			
			singlePlantInstance = (Hashtable)plantInstances.get(""+i);
			//determine if accepted
			if (singlePlantInstance.get("acceptence") == "accepted")
			{
				outData.append(xmlAcceptedInstance(singlePlantInstance, i)).toString();
				//System.out.println(singlePlantInstance);
			}
			else 
			{
				outData.append(xmlNotAcceptedInstance(singlePlantInstance, i)).toString();
			}
		}
		//print the footer
		out.println(
			outData.append(xmlFooter()).toString()
		);
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: USDAPlantsToXml.outputXml "
		+e.getMessage() );
		e.printStackTrace();
	}
}



/**
 * method that returns the atomized taxon name 
 * components based on the long cancateneted string 
 * containg authors etc
 * 
 * @param concatenatedName -- the long concatenated name
 * @param rank -- the reank (eg., genus, species, family, variety ...)
 */
private String xmlAtomicName(String  concatenatedName, String rank)
{
	StringBuffer atomicElements = new StringBuffer(0);
	if (concatenatedName != null)
	{
		atomicElements.append("<Unit_Name1></Unit_Name1> \n");
		//Genus name
		atomicElements.append("    <Unit_Name2>"+
			spaceStringTokenizer(concatenatedName, 1)
		+"</Unit_Name2> \n");
		//species name
		atomicElements.append("    <Unit_Name3>"+
			spaceStringTokenizer(concatenatedName, 2)
		+"</Unit_Name3> \n");
		//if not a subspecies or variety - then a species !
		if (concatenatedName.indexOf("var.") < 0 && concatenatedName.indexOf("ssp.") < 0)
		{
			//make a concatenated name
			if ( rank.trim().indexOf("genus") > 0 )
			{
				//System.out.println(">> GENUS" );
				String s = spaceStringTokenizer(concatenatedName, 1);
				atomicElements.append("    <concatenatedName>"+s+"</concatenatedName> \n");
			}
			else
			{
				atomicElements.append("    <concatenatedName>"+
				spaceStringTokenizer(concatenatedName, 1)+" "+spaceStringTokenizer(concatenatedName, 2)
				+"</concatenatedName> \n");
			}
			//add the parent name
			atomicElements.append("    <parentName>"+
				spaceStringTokenizer(concatenatedName, 1)
			+"</parentName> \n");
			
			//grab the author information
			atomicElements.append("    <authorName>");
			for (int i=3; i<10; i++)
			{
				
				if ( ! spaceStringTokenizer(concatenatedName, i).equals("nullToken"))
				{
					//System.out.println(">> "+spaceStringTokenizer(concatenatedName, i) );
					//make sure more than one character
					if (spaceStringTokenizer(concatenatedName, i).length() > 0)
					{
						atomicElements.append( spaceStringTokenizer(concatenatedName, i)+" " );
					}
					else 
					{
						//System.out.println(">> "+spaceStringTokenizer(concatenatedName, i) );
						atomicElements.append("");
					}
				}
			}
			atomicElements.append("    </authorName> \n");
		}
		//else is a variety
		else if ( concatenatedName.indexOf("var.") > 0 )
		{
			//System.out.println("VARIETY");
			atomicElements.append( xmlAtomicVarietyName(concatenatedName) );
		}
		// ELSE IF IT IS A SUBSPECIES
		else if ( concatenatedName.indexOf("ssp.") > 0 )
		{
			//System.out.println("SUBSPECIES: " + concatenatedName);
			atomicElements.append( xmlAtomicSubspeciesName(concatenatedName) );
		}
	}
	return(atomicElements.toString() );
}

/**
 * method that returns the atomic name properties etc for a
 * plant instance that is a variety
 * @param concatenatedName -- the name to parse into the sci name w/o the
 *	author bit
 */
private String xmlAtomicSubspeciesName(String  concatenatedName )
{
	StringBuffer atomicElements = new StringBuffer(0);
	if (concatenatedName != null)
	{
		String sspToken = null;
		StringBuffer authorToken=new StringBuffer();
		
		//grab the variety token
		for (int i=3; i<30; i++)
		{
			if ( spaceStringTokenizer(concatenatedName, i).equals("ssp.") )
			{
				sspToken =spaceStringTokenizer(concatenatedName, i+1);
			}
		}
		
		//varietyName name
		atomicElements.append("    <Unit_Name4>"+
			sspToken
		+"</Unit_Name4> \n");
		
		//make a concatenated for the variety name
		atomicElements.append("    <concatenatedName>"+
			spaceStringTokenizer(concatenatedName, 1)+" "+
			spaceStringTokenizer(concatenatedName, 2)+" "+
			"ssp. "+sspToken+
		"</concatenatedName> \n");
			
		//add the parent name
		atomicElements.append("    <parentName>"+
			spaceStringTokenizer(concatenatedName, 1)+" "+
			spaceStringTokenizer(concatenatedName, 2)
		+"</parentName> \n");
			
			//grab the author info
			for (int i=3; i<30; i++)
			{
			if ( spaceStringTokenizer(concatenatedName, i).equals("ssp.") )
			{
				break;
			}
			else 
			{
				authorToken.append(spaceStringTokenizer(concatenatedName, i) );
			}
		}
		atomicElements.append("    <authorName>"+
			authorToken.toString()
		+"</authorName> \n");		
	}
	return(atomicElements.toString() );
}
 


/**
 * method that returns the atomic name properties etc for a
 * plant instance that is a variety
 * @param concatenatedName -- the name to parse into the sci name w/o the
 *	author bit
 */
private String xmlAtomicVarietyName(String  concatenatedName )
{
	StringBuffer atomicElements = new StringBuffer(0);
	if (concatenatedName != null)
	{
		String varietyToken = null;
		StringBuffer authorToken=new StringBuffer();
		
		//grab the variety token
		for (int i=3; i<30; i++)
		{
			if ( spaceStringTokenizer(concatenatedName, i).equals("var.") )
			{
				varietyToken =spaceStringTokenizer(concatenatedName, i+1);
			}
		}
		
		//varietyName name
		atomicElements.append("    <Unit_Name4>"+
			varietyToken
		+"</Unit_Name4> \n");
		
		//make a concatenated for the variety name
		atomicElements.append("    <concatenatedName>"+
			spaceStringTokenizer(concatenatedName, 1)+" "+
			spaceStringTokenizer(concatenatedName, 2)+" "+
			"var. "+varietyToken+
		"</concatenatedName> \n");
			
		//add the parent name
		atomicElements.append("    <parentName>"+
			spaceStringTokenizer(concatenatedName, 1)+" "+
			spaceStringTokenizer(concatenatedName, 2)
		+"</parentName> \n");
			
			//grab the author info
			for (int i=3; i<30; i++)
			{
			if ( spaceStringTokenizer(concatenatedName, i).equals("var.") )
			{
				break;
			}
			else 
			{
				authorToken.append(spaceStringTokenizer(concatenatedName, i) );
			}
		}
		atomicElements.append("    <authorName>"+
			authorToken.toString()
		+"</authorName> \n");
		
	}
	return(atomicElements.toString() );
}
 

/**
 * method that returns the main components of
 * information about a plant taxon in xml format
 * for an non-accepted plant taxon instance
 */
private String xmlRank(String  concatenatedName, String plantCode )
{
	StringBuffer rank = new StringBuffer(0);
	if (concatenatedName != null)
	{
		if ( concatenatedName.indexOf("var.") > 0)
		{
			rank.append("<classLevel>variety</classLevel> \n");
		}
		else if ( concatenatedName.indexOf("ssp.") > 0 )
		{
			rank.append("<classLevel>subspecies</classLevel> \n");
		}
		else 
		{
			//if the second token has a period at the end then it is a Genus
			//System.out.println("ESCOND TOKEN: "+spaceStringTokenizer(concatenatedName, 2) );
			//if (spaceStringTokenizer(concatenatedName, 2).endsWith("."))
			if ( rankIsGenus(concatenatedName, plantCode) == true )
			{
				rank.append("<classLevel>genus</classLevel> \n");
			}
			//serach for hybrids
			else if (spaceStringTokenizer(concatenatedName, 2).startsWith("x"))
			{
				rank.append("<classLevel>Hybrid</classLevel> \n");
			}
			//else must be a species
			else 
			{
				return("<classLevel>species</classLevel> \n");
			}
		}
		return(rank.toString() );
	}
	else 
	{
		return("<!-- rank should be here --> \n");
	}
}

	/**
	 * method that returns true if the rank of the concatenated name 
	 * is type genus based on the rules outlined by R. Peet on 20020220
	 *
	 * @param concatenatedName -- the long usda name
	 * @return boolean -- true if it is a genus
	 *
	 */
 private boolean rankIsGenus(String concatenatedName, String plantCode)
 {
		//if there are only two tokens then it is a genus
		if( getNumberOfNameTokens(concatenatedName) == 2 )
		{
			//System.out.println("USDAPlantsToXml > two tokens: " + concatenatedName);
			return(true);
		}

		//the following is a genus: ZYGOP2
		else if (plantCode.length() >= 5)
		{
			//System.out.println("USDAPlantsToXml > genus code: " + plantCode );
			//String s = plantCode.substring(4,5);
			//System.out.println("USDAPlantsToXml > 5th char: " + s );
			char c = plantCode.charAt(4);
			//System.out.println("USDAPlantsToXml > 5th char: " + c );
			if ( java.lang.Character.isDigit(c) == false )
			{
				//System.out.println("USDAPlantsToXml > is alpha: ");
				return(true);
			}
			else
			{
				return(false);
			}
		}	
		
		//if the second token starts with a capital then a genus
		else if (secondTokenStartsWithCapital(concatenatedName) == true )
	 	{
			System.out.println("USDAPlantsToXml > second token cap: " + concatenatedName );
			return(true);
	 	}
		
	 	else
	 	{
			return(false);
	 	}
	}
	
	/**
	 * utility method that returns true if the second token of 
	 * a string, in this case the concatenated name starts
	 * with a capital letter -- meaning that the plant is type 
	 * genus
	 *
	 * @param string --  the character string
	 * @return boolean 
	 */
	 private boolean secondTokenStartsWithCapital(String string)
	 {
		boolean result = false;
		try
		{
			//make sure that the first char does not start with a 
			// 'X'
			if ( ! string.startsWith("X") )
			{
				String s = spaceStringTokenizer(string, 2);
				char c = s.charAt(0);
				if ( java.lang.Character.isLowerCase(c) == false )
				{
					result = true;
				}
				else
				{
					result = false;
				}
			}
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		
		return(result);
	 }
 
	/**
	 * method that returns the number of tokens (char strings separated by 
	 * a space, it is intended to segregate the genus becase there are 
	 * only two tokens
	 *
	 * @param string --  the character string
	 * @return tokens -- the number of tokens in the string (follow the above
	 * 	def.)
	 */
	 private int getNumberOfNameTokens(String s)
	 {
		int tokens = 0;
		
		try
		{
			StringTokenizer t = new StringTokenizer(s.trim(), " ");
			tokens = t.countTokens();
		}
		catch( Exception e ) 
		{
			System.out.println(" Exception:  " + e.getMessage() );
			e.printStackTrace();
		}
		
		return(tokens);
	 }

/**
 * method that returns the main components of
 * information about a plant taxon in xml format
 * for an non-accepted plant taxon instance
 */
private String xmlNotAcceptedInstance(Hashtable singlePlantInstance, int instanceValue )
{
	StringBuffer plantInstance = new StringBuffer();
	String concatenatedName = (String)singlePlantInstance.get("concatenatedName");
	String plantCode = (String)singlePlantInstance.get("plantCode");
	String commonName = (String)singlePlantInstance.get("commonName");
	String familyName = (String)singlePlantInstance.get("familyName");
	String status = (String)singlePlantInstance.get("acceptence");
	String rank = xmlRank(concatenatedName, plantCode);
	String atomicNames = xmlAtomicName(concatenatedName, rank);
	
	
	//parent node open
	plantInstance.append("  <taxon> \n");
	
	
	//a unique value -- that is basically the year and 
	//the row number of the plants list
	plantInstance.append("  <taxonUnit>plants"+instanceValue+"</taxonUnit> \n");
	
	//append the concatenated name 
	plantInstance.append("    <concatenatedLongName>"+
		singlePlantInstance.get("concatenatedName")
	+"</concatenatedLongName> \n");
	
	//append the atomic names
	plantInstance.append("    "+atomicNames);
	
	//append the symbol
	plantInstance.append("    <plantCode>"+
		singlePlantInstance.get("plantCode")
	+"</plantCode> \n");
	
	//append the commonName
	plantInstance.append("    <synonymName>"+
		singlePlantInstance.get("synonymName")
	+"</synonymName> \n");
	
	//append the family
	plantInstance.append("    <familyName>"+
		singlePlantInstance.get("familyName")
	+"</familyName> \n");
	
	//append the status
	plantInstance.append("    <status>"+
		singlePlantInstance.get("acceptence")
	+"</status> \n");
	
	//append the rank
	plantInstance.append("    "+rank);
	
	//close the parent
	plantInstance.append("  </taxon> \n");
		
	return(plantInstance.toString() );
}


/**
 * method that returns the main components of
 * information about a plant taxon in xml format
 * for an accepted plant taxon instance
 */
private String xmlAcceptedInstance(Hashtable singlePlantInstance, int instanceValue )
{
	StringBuffer plantInstance = new StringBuffer();
	String concatenatedName = (String)singlePlantInstance.get("concatenatedName");
	String plantCode = (String)singlePlantInstance.get("plantCode");
	String commonName = (String)singlePlantInstance.get("commonName");
	String familyName = (String)singlePlantInstance.get("familyName");
	String status = (String)singlePlantInstance.get("acceptence");
	String rank = xmlRank(concatenatedName, plantCode);
	String atomicNames = xmlAtomicName(concatenatedName, rank);
	
	if( rank.indexOf("genus") > 0)
	{
		//System.out.println("USDAPlantsToXml > concatName: " + concatenatedName );
		//System.out.println("USDAPlantsToXml > rank: " + rank );
		//System.out.println("USDAPlantsToXml > atomicNames: " + atomicNames +" \n");
	}
	
	//parent node open
	plantInstance.append("  <taxon> \n");
	
	//a unique value -- that is basically the year and 
	//the row number of the plants list
	plantInstance.append("  <taxonUnit>plants"+instanceValue+"</taxonUnit> \n");
	
	//append the concatenated name 
	plantInstance.append("    <concatenatedLongName>"+concatenatedName+"</concatenatedLongName> \n");
	
	//append the atomic names
	plantInstance.append("    "+atomicNames);
	
	//append the symbol
	plantInstance.append("    <plantCode>"+plantCode+"</plantCode> \n");
	
	//append the commonName
	plantInstance.append("    <commonName>"+commonName+"</commonName> \n");
	
	//append the family
	plantInstance.append("    <familyName>"+familyName+"</familyName> \n");
	
	//append the status
	plantInstance.append("    <status>"+status+"</status> \n");
	
	//append the rank
	plantInstance.append("    "+rank);
	
	//close the parent
	plantInstance.append("  </taxon> \n");
		
	return(plantInstance.toString() );
}

/**
 * method that returns the footer for the 
 * xml file that contains the plant instances
 */
private String xmlFooter()
{
	StringBuffer header = new StringBuffer();
	header.append("</plantTaxa> \n");
	return(header.toString() );
}


/**
 * method that returns the header for the 
 * xml file that contains the plant instances
 */
private String xmlHeader()
{
	StringBuffer header = new StringBuffer();
	header.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?> \n");
	header.append("<plantTaxa> \n");
	return(header.toString() );
}

/**
 * method that returns the plant family name 
 * from a single plant instance using as input
 * a line from the USDA plants list
 */
private String plantSynonymName(String fileVectorLine)
{
	String synonymName = null;
	if (fileVectorLine != null)
	{
		synonymName = commaStringTokenizer(fileVectorLine, 3);
		return(synonymName.replace('=', ' ').trim() );
	}
	else 
	{
		return("nullSynonymName");
	}
}

/**
 * method that returns the plant family name 
 * from a single plant instance using as input
 * a line from the USDA plants list
 */
private String plantCommonName(String fileVectorLine)
{
	String commonName = null;
	if (fileVectorLine != null)
	{
		commonName = commaStringTokenizer(fileVectorLine, 3);
		return(commonName);
	}
	else 
	{
		return("nullCommonName");
	}
}



/**
 * method that returns the plant family name 
 * from a single plant instance using as input
 * a line from the USDA plants list
 */
private String plantFamilyName(String fileVectorLine)
{
	String familyName = null;
	if (fileVectorLine != null)
	{
		familyName = commaStringTokenizer(fileVectorLine, 4);
		
		//if there is an equals sign in this token then get the fifth token
		if( familyName.trim().startsWith("=") )
		{
			familyName = commaStringTokenizer(fileVectorLine, 5);
		}
			
		return(familyName);
	}
	else 
	{
		return("nullFamilyName");
	}
}




/**
 * method that returns the plantCode 
 * from a single plant instance using as input
 * a line from the USDA plants list
 */
private String plantCode(String fileVectorLine)
{
	String plantCode = null;
	if (fileVectorLine != null)
	{
		plantCode = commaStringTokenizer(fileVectorLine, 1);
		return(plantCode);
	}
	else 
	{
		return("nullPlantCode");
	}

}


/**
 * method that returns the concatenated name 
 * from a single plant instance using as input
 * a line from the USDA plants list
 */
private String plantConcatenatedName(String fileVectorLine)
{
	String concatenatedName = null;
	if (fileVectorLine != null)
	{
		concatenatedName = commaStringTokenizer(fileVectorLine, 2);
		return(concatenatedName);
	}
	else 
	{
		return("nullConcatenatedName");
	}

}



 /**
 	* method that returns true is the plant
 	* instance on a line is an accepted usage
 	* by the USDA
 	*/
	private boolean acceptedPlantInstance(String fileVectorLine)
	{
		//the equals sign in the plant instance 
		//means the plant is not accepted and that
		//the synonym on the right of the equals
		//sign is the accepted element
		if (fileVectorLine.indexOf("=") > 0)
		{
			return(false);
		}
		else 
		{
			return(true);
		}
	}







/**
 * method to read the plant list file into memory and return the 
 * object as a vector that can then be parsed into a hash table 
 * structure that stores all the plant instances
 *
 */	
private Vector fileToVector(String inputPlantList)
	throws java.io.IOException,
        java.net.MalformedURLException,
        org.xml.sax.SAXException
{
	//read the resulting file into the attribute vector
	BufferedReader in = new BufferedReader(new FileReader(inputPlantList), 8192);
	String s=null;
	while ((s=in.readLine()) != null) 
	{
		//System.out.println(s);
		fileVector.addElement(s);
	}
	return(fileVector);
}


/**
 * method to tokenize elements from a comma delimeted string
 */
public String commaStringTokenizer(String pipeString, int tokenPosition)
{
	String token="nullToken";
	if (pipeString != null) 
	{
		StringTokenizer t = new StringTokenizer(pipeString.trim(), "\",\"");
		int i=1;
		while (i<=tokenPosition) 
		{
			if ( t.hasMoreTokens() )
			{
				token=t.nextToken().replace('"', ' ').trim();
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
 * method to tokenize elements from a space delimeted string
 */
public String spaceStringTokenizer(String pipeString, int tokenPosition)
{
	String token="nullToken";
	if (pipeString != null) 
	{
		StringTokenizer t = new StringTokenizer(pipeString.trim(), " ");
		int i=1;
		while (i<=tokenPosition) 
		{
			if ( t.hasMoreTokens() )
			{
				token=t.nextToken().replace('"', ' ').trim();
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
