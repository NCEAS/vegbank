/**
 * This class provides a number of methods that can be used to 
 * load a Ternary Search Tree
 * 
 *    Authors: @author@
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-10-22 23:45:00 $'
 * '$Revision: 1.1 $'
 */
package vegclient.ternarysearch;


import java.io.*;
import java.util.*;

import vegclient.ternarysearch.*;
import vegclient.framework.*;

public class TSTLoader 
{
	
	
	/**
	 * method that gets an array of data for loading into the tst
	 * structure -- this works with the dictionary loader class
	 *
	 */
	public static String[] getArray(String string) {
		int numWords = 0;
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == ' ') numWords++;
		}
		numWords++;  // the last word
		String[] returnArray = new String[numWords];
		int returnArrayIndex = 0;
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == ' ') {
				returnArray[returnArrayIndex] = buffer.toString();
				returnArrayIndex++;
				buffer = new StringBuffer();
			} else {
				buffer.append(string.charAt(i));
			}
		}
		returnArray[returnArrayIndex] = buffer.toString();
		return returnArray;
	}
	
	
	/**
	 * method written by jhh to build a tst from an xml document
	 *
	 * 
	 */
	public static TernarySearchTree getXMLToTST(String fileName, String keystring, String objectstring) 
	{
		TernarySearchTree tst = new TernarySearchTree();
		//String[] currentStringArray;
		Vector vec = new Vector();
//		vec.addElement("dog");
//		vec.addElement("cat");
		String document = fileName; 
		String key = keystring;
		String object = objectstring;
		
		
//		XMLparse parse = new XMLparse();
//		vec = parse.get(document, key);

		XMLSAXparse parse = new XMLSAXparse();
		//vec = parse.getSingleElement(document, "concatenatedLongName");
		vec = parse.getElementPair(document, key, object);
		
		Vector keyVec = (Vector)vec.elementAt(0);
		Vector objectVec = (Vector)vec.elementAt(1);
		
		System.out.println("keys: " + keyVec.size() );
		System.out.println("objects: " + objectVec.size() );
		
		String definition = "Definition of the word ";
		
		//currentStringArray = getArray(DictionaryLoader0.dictionaryString);
		int count = 0;
		for(int i = 0; i < objectVec.size()-2; i++)
		{
			count = i;
			tst.put( (String)keyVec.elementAt(count), (String)objectVec.elementAt(count));
		}
		return tst;
	}
	
	/**
	 * method that loads collumnar data into a tst
	 */
	 public static TernarySearchTree getTableToTST(String tableName, 
	 String delimiter, int keyPosition, int objectPosition)
	{
		System.out.println("key Position: " + keyPosition);
		TernarySearchTree tst = new TernarySearchTree();
		Vector vec = fileVectorizer( tableName );
		int count = 0;
		//cycle thru the entire vector
		for(int i = 0; i < vec.size(); i++)
		{
			count = i;
			StringTokenizer t = new StringTokenizer(vec.elementAt(count).toString(), delimiter);
			StringTokenizer t2 = new StringTokenizer(vec.elementAt(count).toString(), delimiter);
			
			String buf = null;
			String buf2 = null;
			
			//count out to the column of interest for the key
			for ( int ii = 0; ii <= keyPosition; ii++ )
			{
				if (ii == keyPosition)
				{
					
					if ( t.hasMoreTokens() )
					{
						buf = t.nextToken();
						//System.out.println( buf );
					}
				}
				else
				{
					//System.out.println(" token does not exist ");
					buf = t.nextToken();
				}
			}
			
			//count out to the column of interest for the object
			for ( int ii = 0; ii <= objectPosition; ii++ )
			{
				if (ii == objectPosition)
				{
					
					if ( t2.hasMoreTokens() )
					{
						buf2 = t2.nextToken();
						//System.out.println( buf );
					}
				}
				else
				{
					//System.out.println(" token does not exist ");
					buf2 = t2.nextToken();
				}
			}
			
			//String buf = t.nextToken();
			//String buf2 = t.nextToken();
			tst.put( buf, buf2);
		}
		return(tst);
	}
	
	
	/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then returns the vector 
 *
 * @param fileName name of the file that whose contents should be 
 * written to a vector
 */
	public static Vector fileVectorizer (String fileName) 
	{
		Vector fileVector = new Vector();
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) 
			{
				//System.out.println(s);	
				fileVector.addElement(s);
			}
		}
		catch (Exception e) 
		{
			System.out.println("failed in:" + 
			e.getMessage());
			e.printStackTrace();
		}
		return(fileVector);
	} 

	
	/**
	 * method the returns an empty tst which can be 
	 * populated by some subsequent loading session
	 *
	 */
	public static TernarySearchTree getEmptyTST()
	{
		TernarySearchTree tst = new TernarySearchTree();
		return tst;
	}
	
	
	
	public static void main(String[] args) 
	{
			String testString = "hello goodbye";
			String[] testArray = getArray(testString);
			for(int i = 0; i < testArray.length; i++) System.out.println(testArray[i] + ".");
			
	//		TernarySearchTree tst = getTST();
	//		System.out.println(tst.numNodes());
	//		System.out.println(tst.numDataNodes());
	//		System.out.println(tst.get("ad"));
//			System.out.println(tst.get("zymurgies"));
//			System.out.println(tst.get("ajh"));
			
	
	}
	
	
	
	
}
