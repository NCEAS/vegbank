import java.io.IOException;
import java.io.*;
import java.util.*;

import LegacyDataWizard.*;

/**
 * this class will take as input objects that were developed by reading an xml 
 * that relates ascii files containing plots to one another and then will build
 * an xml file that is capable of being injested by the plots database. 
 *
 * @Author John Harris
 * @Version May 2001
 */

public class AsciiElementParser {

public Hashtable attributeHash=new Hashtable(); //passed from calling class
public Hashtable constraintHash=new Hashtable(); //passed from calling class

private static int currentPlotNum = 0; //current plot code row number
private String plotCodeFileName = null; //name of file containingsite.authplotcode
private int plotCodeColNum =0; //the column of the author plot code in that file
private boolean plotCodeConstrained; //true if the plotcode in constrained file

LegacyDataWizard ldw = new LegacyDataWizard();
//PlotDataMapper pdm = new PlotDataMapper();


/**
 * method to parse the elements out of the package 
 *
 */
public void parsePackageElements(Hashtable attHash, Hashtable constHash) 
{
	try 
	{
		//make the hash tables public
		attributeHash=attHash;
		constraintHash=constHash;
		
		//for each plot write an xml file
		
		for (int i=0; i<getPlotNum(); i++) 
		//for (int i=0; i<2; i++)
		{
			//the current plot code
			String currentPlot=getCurrentPlotCode();
			if (currentPlot != null)
			{
				Hashtable projectElements = parseProjectAttributes( currentPlot );
				Hashtable siteElements = parseSiteAttributes( currentPlot );
				Hashtable strataElements = parseStrataAttributes( currentPlot );
				Hashtable speciesElements = parseSpeciesAttributes( currentPlot );
				//System.out.println(strataElements.size()+" "+strataElements);
				//map the elements to the xml structue and then print
				mapElementsToXML( projectElements, siteElements, speciesElements, strataElements  );
			}
		}
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: AsciiElementParser.parsePackageElements "
		+e.getMessage() );
		e.printStackTrace();
	}
}



/**
 * method that returns the current authorPlotCode and updates the 
 * public static currentPlotNum
 */	
private void mapElementsToXML(Hashtable projectAttributes, 
	Hashtable siteAttributes, Hashtable speciesAttributes, 
	Hashtable strataAttributes )
{
	//because each time this method is called a new plot 
	//should be written the class should be initialized next
	PlotDataMapper pdm = new PlotDataMapper();
	
	//grab the authorPlotCode for use in writing the 
	// output xml file
	String authorPlotCode = null;
	
	//make sure that the plot code is there
	if (siteAttributes.get("authorPlotCode") != null)
	{
		authorPlotCode = siteAttributes.get("authorPlotCode").toString();
	//	System.out.println("PLOT CODE: "+authorPlotCode);
		pdm.plotElementMapper(
			siteAttributes.get("authorPlotCode").toString(), 
			"authorPlotCode",
			"site"
		);
	}
	
	//map the project level elements
	PlotElements pe = new PlotElements();
		pe.buildElementLists();
		
		//map the project elements
		for (int i=0; i <= pe.elementListSize("project"); i++) {
			String currentElement=pe.getProjectElement();
			if (currentElement != null )
			{
				//check to see that the element exists in the hash
				if (projectAttributes.get(currentElement) != null)
				{
					//pass to the plot data mapper constructor
					pdm.plotElementMapper(
						projectAttributes.get(currentElement).toString(), 
						currentElement,
						"project"
					);
				}
			}
		}
		
		//map the site elements
		for (int i=0; i <= pe.elementListSize("site"); i++) {
			String currentElement=pe.getSiteElement();
			if (currentElement != null )
			{
				//check to see that the element exists in the hash
				if (siteAttributes.get(currentElement) != null)
				{
					//pass to the plot data mapper constructor
					pdm.plotElementMapper(
						siteAttributes.get(currentElement).toString(), 
						currentElement,
						"site"
					);
				}
			}
		}
		
		
//		System.out.println("STRATA Hash Size: "+  strataAttributes.size() );
//		System.out.println("STRATA Hash values: "+  strataAttributes.toString() );
		//map the strata elements
		for (int i=0; i <= pe.elementListSize("strata"); i++) 
		//for (int i=0; i <= 3; i++)
		{
			//System.out.println("STRATA Element Size: "+  pe.elementListSize("strata") );
			String currentElement=pe.getStrataElement();
			//System.out.println("STRATA Element: "+  currentElement );
			if (currentElement != null )
			{
				//check to see that the element exists in the hash
				if (strataAttributes.get(currentElement) != null)
				{
//					System.out.println(">>>> STRATA INFO: "
//						+strataAttributes.get(currentElement).toString() +" list num: "+i);
					//pass to the plot data mapper constructor
					pdm.plotElementMapper(
						strataAttributes.get(currentElement).toString(), 
						currentElement,
						"observation"
					);
				}
			}
		}
		
		//map the species elements
	//	System.out.println("SPECIES Hash values: "+  speciesAttributes.toString() );
		for (int i=0; i <= pe.elementListSize("species"); i++) 
		{
			String currentElement=pe.getSpeciesElement();
			if (currentElement != null )
			{
//				System.out.println("CURRENT SPECIES ELEMENT: "+currentElement);
				//check to see that the element exists in the hash
				if (speciesAttributes.get(currentElement) != null)
				{
					//remember that there may be many vales for
					//any of these elements so take care when passing
					//to the method -- the object that should be parsed 
					//here is a vector
					Vector elementVector =(Vector)speciesAttributes.get(currentElement);
					for (int ii=0; ii < elementVector.size(); ii++)
					{
						//pass to the plot data mapper constructor
						pdm.plotElementMapper(
							//speciesAttributes.get(currentElement).toString(), 
							elementVector.elementAt(ii).toString(),
							currentElement,
							"species"
						);
						
						//fake the next 2 mappings to validate that the values are 
						//being mapped correctly : just map the increment into the 
						//mapping class as an integer that represents cover
						pdm.plotElementMapper(
							//speciesAttributes.get(currentElement).toString(), 
							""+ii,
							"stratum",
							"species"
						);
						
						pdm.plotElementMapper(
							//speciesAttributes.get(currentElement).toString(), 
							""+ii,
							"cover",
							"species"
						);
						
					}
				}
			}
		}
		
		//now that the above elements have been
		//passed into the plotDataMapper consolidate 
		//the varables before printing to file
		pdm.plotElementConsolidator(pdm.plotProjectParams, pdm.plotSiteParams, 
		pdm.plotObservationParams, pdm.plotCommunityParams,	pdm.plotSpeciesParams);
		
		//System.out.println("BEING PASSED TO THE WRITER: "+pdm.plotSpeciesParams.toString()+"\n");
	//	System.out.println("  ");
		//now print the xml file using the consolidated hash table made above
		PlotXmlWriter pxw = new PlotXmlWriter();
		pxw.writePlot(pdm.comprehensivePlot, "tester"+authorPlotCode.trim()+".xml");

}


/**
 * method that returns the current authorPlotCode and updates the 
 * public static currentPlotNum
 */	
private String getCurrentPlotCode()
{
	String currentPlotCode=null;
		
	//open the file and count the lines
	try {
		//System.out.println();
		BufferedReader in =	new BufferedReader
			(new FileReader(plotCodeFileName), 8192);
			
		//get the line with the plot code
		String s =null;
		int lineCount=0;
		while ((s=in.readLine()) != null && lineCount<currentPlotNum  ) 
		{
			currentPlotCode = ldw.pipeStringTokenizer(s, plotCodeColNum);
			lineCount++;
		}	
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: AsciiElementParser.getCurrentPlotCode "
		+e.getMessage() );
		e.printStackTrace();
	}
	
	//update the static counter
	currentPlotNum++; 
	return(currentPlotCode);
}




/**
 * method that returns the total number of plots in a package and updates the 
 * plotCodeFileName as well as the plotCodeColNum and whether that file is
 * constrained
 */	
public int getPlotNum()
{
	int total=0;
	String plotFileNameString = null;
	
	//take the plot code that is constrained by a parent file first
	if ( (String)attributeHash.get("authorPlotCode.constrained") != null)
	{
		plotFileNameString=(String)attributeHash.get("authorPlotCode.constrained");
		plotCodeConstrained = true;
	}
	else 
	{
		plotFileNameString=(String)attributeHash.get("authorPlotCode");
		plotCodeConstrained = false;
	}
	
	//update the public plotCodeFileName variable
	plotCodeFileName=ldw.pipeStringTokenizer(plotFileNameString,2);
	
	//update the column number of the code in the above file
	plotCodeColNum=Integer.parseInt(ldw.pipeStringTokenizer(plotFileNameString,3));
	
	//open the file and count the lines
	try 
	{
		BufferedReader in =	new BufferedReader(new FileReader(plotCodeFileName), 8192);			
		//count the lines
		String s =null;
		while ((s=in.readLine()) != null) 
		{
				total++;
		}	
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.getPlotNum "
		+e.getMessage() );
		e.printStackTrace();
	}
	return(total);
}



/**
 * method to parse the species related
 * data into a hash table.  The structure
 * of this data will be a single hash table 
 * containing many individual taxonObservations
 * in individual hash table where the key will
 * be an integer
 *
 */	
private Hashtable parseSpeciesAttributes(String authorPlotCode)
{
	System.out.println("species elements for plot: "+authorPlotCode);
	Hashtable speciesAttributesHash = new Hashtable();
	if (authorPlotCode == null) 
	{
		System.out.println("warning null plot code");
	}
	else 
	{
		PlotElements pe = new PlotElements();
		pe.buildElementLists();
		for (int i=0; i <= pe.elementListSize("species"); i++) 
		{
			String currentElement=pe.getSpeciesElement();
//			System.out.println(" FFFF element name: "+currentElement);
			
			//check to see that the element exists in the package as unconstrained
			if (elementExists(currentElement)==true )
			{
				System.out.println(" TAXA RELATED element exists somewhere");
			}
			//look for the element name as a constrained element
			//which it should be
			else if ( elementExists(currentElement+".constrained")==true )
			{
//				System.out.println("TAXA RELATED element (constrained) exists somewhere");
				//grab the vector that contains the, possibly, multiple
				//species names
				//"peet_species_entire_area.txt",
				//System.out.println("SEARCHING for PLOT: "+authorPlotCode);
				speciesAttributesHash.put(currentElement, 
						extractConstrainedElements(
						currentElement, 
						elementFileName(currentElement+".constrained"), 
						"authorPlotCode", 
						authorPlotCode) 
				);
			}
		}
	}
return(speciesAttributesHash);
}



/**
 * method to parse the data package and retrive the 
 * strata-related information in the form of a hashtable
 * 
 */	
private Hashtable parseStrataAttributes(String authorPlotCode)
{
	System.out.println("strata elements for plot: "+authorPlotCode);
	Hashtable strataAttributesHash = new Hashtable();
	
	strataAttributesHash.put("stratumName", "NVCS1");
	strataAttributesHash.put("stratumHeight", "12");
	strataAttributesHash.put("stratumCover", "78");
	
	
////	if (authorPlotCode == null) 
////	{
////		System.out.println("warning null plot code");
////	}
////	else 
////	{
////		PlotElements pe = new PlotElements();
////		pe.buildElementLists();
////		for (int i=0; i <= pe.elementListSize("strata"); i++) 
////		{
////			String currentElement=pe.getStrataElement();
////			System.out.println(" element name: "+currentElement);
			
			//check to see that the element exists in the package as unconstrained
////			if (elementExists(currentElement)==true )
////			{
////				System.out.println(" TAXA RELATED element exists somewhere");
////			}
			//look for the element name as a constrained element
			//which it should be
////			else if ( elementExists(currentElement+".constrained")==true )
////			{
////				System.out.println("TAXA RELATED element (constrained) exists somewhere");
////				speciesAttributesHash.put(currentElement, 
////						extractConstrainedElement(
////						currentElement, 
////						"peet_species.txt", 
////						"authorPlotCode", 
////						authorPlotCode) 
////				);
////			}
////		}

///	}
return(strataAttributesHash);
}








/**
 * method to parse the project related attributes 
 * from a data package.  This method is completely
 * screwed up and does not at all fit the model
 * for this application but it will remain until 
 * code is changed dramatically
 */	
private Hashtable parseProjectAttributes(String authorPlotCode)
{
System.out.println("project elements for plot: "+authorPlotCode);
Hashtable projectAttributesHash = new Hashtable();

	//if the authorPlotCode is null then warn user
	if (authorPlotCode == null) {
		System.out.println("warning null plot code");
	}
	else {
		PlotElements pe = new PlotElements();
		pe.buildElementLists();
		for (int i=0; i <= pe.elementListSize("project"); i++) {
			String currentElement=pe.getProjectElement();
			
			//check to see that the element exists in the package
			if (elementExists(currentElement)==true )
			{
			//	System.out.println("element exists somewhere");
			
				//check if the project element in file that constrains the plot code
			
					//if this is true then that file must have a col named 'authPlotCode'
					//thus grab the element using this -authCode- as the key
		//			System.out.println("element is in constraining file: "+currentElement);
					projectAttributesHash.put(currentElement, 
						extractElement(currentElement, 
							elementFileName(currentElement), 
							"authorPlotCode", 
							authorPlotCode)
						);
		}
	}
	}
	return(projectAttributesHash);
}



/**
 * method that returns true if the constraining element is in
 * a file that constrains the constrained element otherwise
 * false is returned
 */
private boolean elementInConstrainingFile(String constrainingElement, 
	String constrainedElement)
{

	boolean returnValue=false;
	//figure out the file that thet constrainingElement is in
//	String constrainingFile = elementFileName(constrainingElement);
//	String constrainedFile = elementFileName(constrainedElement);
	Vector constrainingFileVec=elementFileNameVec(constrainingElement);
	Vector constrainedFileVec=elementFileNameVec(constrainedElement);

	System.out.println("constrainingFileVec: "+constrainingFileVec);
	System.out.println("constrainedFileVec: "+constrainedFileVec);
	
	for (int i=0; i <constrainedFileVec.size(); i++) {
		for (int ii=0; ii <constrainingFileVec.size(); ii++) {
			if (fileConstrainedBy(constrainedFileVec.elementAt(i).toString(), 
				constrainingFileVec.elementAt(ii).toString() ) == true)
	 		{
				 returnValue=true;
	 		}
		}
	}
	return(returnValue);
}


/**
 * method that returns true if the constrainedFile is
 * really constrained by the constrainingFile
 */
private boolean fileConstrainedBy(String constrainedFile, 
	String constrainingFile)
{
	//cycle thru all the constraints
	Enumeration paramlist = constraintHash.keys();
	while (paramlist.hasMoreElements()) 
 	{
		String key=(String)paramlist.nextElement();
		String constraintString=(String)constraintHash.get(key);
		System.out.println("constraintString: "+constraintString);
		//get the constrained file from the string
		String constrainedFileString = ldw.pipeStringTokenizer(constraintString, 1);
		String constrainingFileString = ldw.pipeStringTokenizer(constraintString, 2);
		
		
		//if one of the file names is there then test for the constraint
		if ( constrainedFileString.equals(constrainedFile) && 
				constrainingFileString.equals(constrainingFile))
		{
//			System.out.println("THAT ELEMENT IS CONSTRIANED");
			return(true);
		}
		else 
		{
			return(false);
		}
	}
	return(false);
}



/**
 * method that returns the file name in a
 * string for which an element exists
 */
public String elementFileName(String elementName)
{
	//System.out.println("FAILING FINDING: "+elementName);
	String fileName = null;
	try 
	{
		//grab the file in which the element having
		//no appended interger exists in
		fileName = (
			ldw.pipeStringTokenizer(
			(String)attributeHash.get(
			elementName), 2)
		);
	}
	catch( Exception e ) 
	{
		System.out.println(" failed in: AsciiElementParser.elementFileName "
		+e.getMessage() );
		e.printStackTrace();
	}
	return(fileName);
}



/**
 * method that returns the file name in a
 * string for which an element exists
 *
 * this is on overloaded method of the above
 * method but that the hash is passed into this
 * method explicitly -- in the future the other
 * method should go away
 */
public String elementFileName(String elementName, Hashtable hash)
{
	//System.out.println("FAILING FINDING second method: "+elementName);
	String fileName = null;
	try 
	{
		//grab the file in which the element having
		//no appended interger exists in
		
		//first get the object from the hash
		//String line = hash.get("state").toString();
		//System.out.println(hash.keySet().toString());
		
		fileName = (
			ldw.pipeStringTokenizer(
			(String)hash.get(
			elementName), 2)
		);
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.elementFileName "
		+e.getMessage() );
		e.printStackTrace();
	}
	return(fileName);
}




/**
 * method that returns the file names in a
 * vector for which an element exists
 * this is an overloaded version of above
 */
private Vector elementFileNameVec(String elementName)
{
	String fileName = null;
	Vector fileNameVec=new Vector();
	//grab the file inchic the element having
	//no appended interger exists in
	fileName = (
			ldw.pipeStringTokenizer(
				(String)attributeHash.get(
					elementName), 2)
	);
	fileNameVec.addElement(fileName);
	
	//now get the filenames for the elements with 
	//appended integers
	int i=0;
	while (i<10)
	{
		fileName = (
			ldw.pipeStringTokenizer(
				(String)attributeHash.get(
					elementName+"."+i), 2)
		);
		if (fileName != null && fileName != "nullToken" )
		{
			fileNameVec.addElement(fileName);
		}
		i++;
	}
	System.out.println("77777"+ (String)fileNameVec.toString() );
	return(fileNameVec);
}






/**
 * method to parse the site related elements for a plot 
 * and return a hash table containing the element name and
 * element value.  At this point the method only works
 * with packages in which the plot site attributes exist 
 * in a single file constrained by a project file 
 */	
private Hashtable parseSiteAttributes(String authorPlotCode)
{
	System.out.println("site elements for plot: "+authorPlotCode);
	Hashtable siteAttributesHash = new Hashtable();

	//if the authorPlotCode is null then warn user
	if (authorPlotCode == null) {
		System.out.println("warning null plot code");
	}
	else {
		//add the plot - site related elements
		siteAttributesHash.put("authorPlotCode", authorPlotCode);
		
		//cycle thru the plot site elements
		PlotElements pe = new PlotElements();
		pe.buildElementLists();
		for (int i=0; i <= pe.elementListSize("site") ; i++) {
			//get the next element in the stack
			String currentElement=pe.getSiteElement();
			//if the element exits in then continue
			if ( elementExists(currentElement+".constrained")  == true ) 
			{
				//if the element is in the same file
//				System.out.println("SITE FILE: "+plotCodeFileName);
				if (elementExistsInFile(currentElement+".constrained",plotCodeFileName ) 
					== true)
				{
					//grab the element and stick into the hash table
					siteAttributesHash.put(currentElement, 
						extractConstrainedElement(currentElement, 
						plotCodeFileName, "authorPlotCode", authorPlotCode) 
						);
				}
			}
			else
			{
			//	System.out.println( "ELEMENT IS NOT THERE" );
			}
		}
	}
	return(siteAttributesHash);
}




/**
 * method that extracts the elements from a constrained file 
 * using as input the element name, associated file
 * and the associated key value and key name for 
 * that ascii file -- note that the elementName and the 
 * keyName should not have the .constrained appended to them
 * wehn they are passed into the method
 * 
 * this method is quite similar to that method named
 * 'extractConstrainedElement' but that this method 
 * retuns a vector containg the numerous data elements
 * instead of a string containing a single value
 */	
private Vector extractConstrainedElements (String elementName, 
	String elementFile, 
	String keyName, String keyValue )
{
//	System.out.println("elementName: "+elementName);
//	System.out.println("elementFile: "+elementFile);
//	System.out.println("keyName: "+keyName);
//	System.out.println("keyValue: "+keyValue);
	
	String elementValue=null;
	Vector elementValues = new Vector();
	//first select the attribute string
	String attributeString = (String)attributeHash.get(elementName+".constrained");
	
	//grab the column position of the element
	int elementColumn=Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				elementName+".constrained"), 3)
		);
//		System.out.println("elementCol: "+elementColumn);
	
	//grab the column position number for the key -- should become a method
	int keyColumn = Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				keyName+".constrained"), 3)
		);
//	System.out.println("keyColumn: "+keyColumn);
	//grab the row numbers for which the key value exits
	Vector rowNum = elementRowNumbers(keyValue, elementFile, keyColumn);
//	System.out.println("ROW NUMBERS CONTAINING ELEMENT: "+keyValue);
//	System.out.println("ROW NUMBERS CONTAINING VALUES: "+rowNum.toString());

	
	try 
	{
		BufferedReader in =	new BufferedReader(new FileReader(elementFile), 8192);
		String s=null;
		int cnt = 1;
		while ((s=in.readLine()) != null  ) 
		{
			//check to see if the line number is equal
			//to one within the range returned by the 
			//element row numbers above
			for (int i=0; i < rowNum.size(); i++)
			{
				int currentVecRowNumb = Integer.parseInt(rowNum.elementAt(i).toString().trim());
				if (cnt == currentVecRowNumb )
				{
					elementValue=ldw.pipeStringTokenizer(s, elementColumn);
			//		System.out.println("elementValue: "+elementValue+" should be at row: "+currentVecRowNumb);
					elementValues.addElement(elementValue);
				}
			}
			cnt++;
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.extractConstrainedElement "
		+e.getMessage() );
		e.printStackTrace();
	}
	
	//System.out.println(">>>> elementName: "+elementName+"  "+elementValues.toString()+"<<<<<");
	return(elementValues);
}











/**
 * method that extracts the element from a constrained file 
 * using as input the element name, associated file
 * and the associated key value and key name for 
 * that ascii file -- note that the elementName and the 
 * keyName should not have the .constrained appended to them
 * wehn they are passed into the method
 */	
private String extractConstrainedElement (String elementName, 
	String elementFile, 
	String keyName, String keyValue )
{
//	System.out.println("elementName: "+elementName);
//	System.out.println("elementFile: "+elementFile);
//	System.out.println("keyName: "+keyName);
//	System.out.println("keyValue: "+keyValue);
	
	String elementValue=null;
	//first select the attribute string
	String attributeString = (String)attributeHash.get(elementName+".constrained");
	
	//grab the column position of the element
	int elementColumn=Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				elementName+".constrained"), 3)
		);
//		System.out.println("elementCol: "+elementColumn);
	
	//grab the column position number for the key -- should become a method
	int keyColumn = Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				keyName+".constrained"), 3)
		);
//	System.out.println("keyColumn: "+keyColumn);
	
	//grab the row number on which the key value exits
	int rowNum = elementRowNumber(keyValue, elementFile, keyColumn);
//	System.out.println("ROW NUMBER: "+rowNum);
	
	try 
	{
		//System.out.println();
		BufferedReader in =	new BufferedReader(new FileReader(elementFile), 8192);
		String s=null;
		int cnt = 1;
		while ((s=in.readLine()) != null  && cnt <= rowNum  ) 
		{
			if (cnt == rowNum)
			{
				elementValue=ldw.pipeStringTokenizer(s, elementColumn);
//				System.out.println("elementValue"+elementValue);
			}
			cnt++;
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.extractConstrainedElement "
		+e.getMessage() );
		e.printStackTrace();
	}
	
//	System.out.println(">>>>"+elementValue+"<<<<<");
	return(elementValue);
}




/**
 * method that extracts the element from a constrained file 
 * using as input the element name, associated file
 * and the associated key value and key name for 
 * that ascii file -- note that the elementName and the 
 * keyName should not have the .constrained appended to them
 * wehn they are passed into the method
 */	
private String extractElement (String elementName, 
	String elementFile, 
	String keyName, String keyValue )
{
	String elementValue=null;
	//first select the attribute string
	String attributeString = (String)attributeHash.get(elementName);
	
	//grab the column position of the element
	int elementColumn=Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				elementName), 3)
		);
	
	//grab the column position number for the key -- should become a method
	int keyColumn = Integer.parseInt(
		ldw.pipeStringTokenizer(
			(String)attributeHash.get(
				keyName), 3)
		);
	
	//grab the row number on which the key value exits
	int rowNum = elementRowNumber(keyValue, elementFile, keyColumn);
//	System.out.println("ROW NUMBER: "+rowNum);
	
	try {
		//System.out.println();
		BufferedReader in =	new BufferedReader(new FileReader(elementFile), 8192);
		String s=null;
		int cnt = 1;
		while ((s=in.readLine()) != null  && cnt <= rowNum  ) 
		{
			if (cnt == rowNum)
			{
				elementValue=ldw.pipeStringTokenizer(s, elementColumn);
			}
			cnt++;
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.extractElement "
		+e.getMessage() );
		e.printStackTrace();
	}	
	return(elementValue);
}



/**
 * method that returns the row numbers of an element in an 
 * ascii file using as input the elementValue 
 * fileName, and the column number position of the element
 *
 * this method is very similar to the method 'elementRowNumber'
 * except that this method returns a vector containing the 
 * row numbers instead of a single value
 */	
private Vector elementRowNumbers (String elementValue, 
	String elementFile, 
	int colPosition )
{
	Vector rowNumbersContainElement = new Vector();
	int rowNum=1;
	try {
		//System.out.println();
		BufferedReader in =	new BufferedReader(
			new FileReader(elementFile), 8192);
		
		String s=null;
		while ((s=in.readLine()) != null ) 
		{
			if ( ldw.pipeStringTokenizer(s, colPosition).equals(elementValue) )
			{
//				System.out.println("element Name: "+elementValue+" row num: "+rowNum);
				String rowNumBuf = ""+rowNum;
				rowNumbersContainElement.addElement(rowNumBuf);
			}
			rowNum++;
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.elementRowNumber "
		+e.getMessage() );
		e.printStackTrace();
	}
	return(rowNumbersContainElement);
}



/**
 * method that returns the row number of an element in an 
 * ascii file using as input the elementValue 
 * fileName, and the column number position of the element
 */	
private int elementRowNumber (String elementValue, String elementFile, 
	int colPosition )
{
	int rowNum=1;
	try {
		//System.out.println();
		BufferedReader in =	new BufferedReader(
			new FileReader(elementFile), 8192);
		
		String s=null;
		while ((s=in.readLine()) != null ) 
		{
			if ( ldw.pipeStringTokenizer(s, colPosition).equals(elementValue) )
			{
				return(rowNum);
			}
			rowNum++;
		}
	}
	catch( Exception e ) {
		System.out.println(" failed in: AsciiElementParser.elementRowNumber "
		+e.getMessage() );
		e.printStackTrace();
	}
	return(rowNum);
}





/**
 * method that returns true if the element exists in the package or 
 * false if it is not in the package
 */	
private boolean elementExists (String elementName)
{
//	System.out.println("looking for element: "+elementName);
//	System.out.println("");
	if (elementName != null)
	{
		if ( (String)attributeHash.get(elementName) != null )
		{
			return(true);
		}
		else
		{
			return(false);
		}
	}
	else
	{
		System.out.println("nullValue passed to: elementExists method");
		return(false);
	}
}

/**
 * method that returns true if the element exists in the 
 * specified file
 */	
private boolean elementExistsInFile (String elementName, String fileName)
{
	if ( (String)attributeHash.get(elementName) != null )
	{
		String elementString=(String)attributeHash.get(elementName);
		if ( elementString.indexOf(fileName) > 0)
		{
			return(true);
		}
		else 
		{
			return(false);
		}
	}
	else
	{
		return(false);
	}
}


}
