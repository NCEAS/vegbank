/**
 * This is the class that manages the workflow manager
 * processes.  This is a non gui class.
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:42 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


import vegclient.framework.*;



public class ProjectManager  
{
	public StringBuffer errorMessage = new StringBuffer();
	public static boolean statusManagerRunning = false;
	public String tncDataPackageTemplate = "template_tncDataPackage.xml";
//	ProcessProgressDisplay pd = new ProcessProgressDisplay();
	TncConverter tncConverter = new  TncConverter();
	
  /**
   * Print debugging messages based on severity level, where severity level 1
   * are the most critical and higher numbers are more trivial messages.
   * Messages with severity 1 to 4 will result in an error dialog box for the
   * user to inspect.  Those with severity 5-9 result in a warning dialog
   * box for the user to inspect.  Those with severity greater than 9 are
   * printed only to standard error.
   * Setting the debug_level to 0 in the configuration file turns all messages
   * off.
   *
   * @param severity the severity of the debug message
   * @param message the message to log
   */
  public static void debug(int severity, String message)
  {
//    if (debug) {
//      if (debug_level > 0 && severity <= debug_level) {
        // Show a dialog for severe errors
//        if (severity < 5) {
					JOptionPane.showMessageDialog(null, message, "Error!",
                                        JOptionPane.ERROR_MESSAGE);
																				
     //     JOptionPane.showMessageDialog(null, message, "Error!");
//        } else if (severity < 10) {
//          JOptionPane.showMessageDialog(null, message, "Warning!",
 //                                       JOptionPane.WARNING_MESSAGE);
//        }

        // Everything gets printed to standard error
        System.err.println(message);
 //     }
 //   }
  } 
	
	
		
	/**
   * the main method for testing
	 * 
   */
  static public void main(String[] args) 
	{
  	try 
		{
			ProjectManager manager = new ProjectManager();
			//manager.postTransformationOptions();
			manager.postLoacalDBLoadOption();
		 
		}
		
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	 /**
  	* method that shows an option pane for edititing, viewing or loading 
		* a dataset to the database
    */
  public static void postTransformationOptions()
  {
		JFrame frame = new JFrame();
		Object[] options = { "view data summary", "edit data", "load data" };
    
		//JOptionPane.showMessageDialog(null, "Data Transformed to ESA Plot Format", "Error!",
    //                                JOptionPane.QUESTION_MESSAGE);
    int n = JOptionPane.showOptionDialog( frame, "Data Transformed to ESA Plot Format", "Post transform options",  
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1] );
		
		if (n == 0)
		{
			new StyleSheetInterface().show();
			System.out.println(" view summary data");
		}
		//edit the data
		else if (n==1)
		{
			debug(0, "go to main menu and choose: project file editor");
			System.out.println(" edit data ");
			
		}
		//launch the loader
		else if (n==2)
		{
			System.out.println(" load data ");
			new Loader().show();
		}
		else
		{
			System.err.println("loader Option" + n);
 		}
  } 
	
	
	
	 /**
  	* method that shows an option pane for creating the snap shot of the
		* database for querying:
		*
    */
  public static void postLoacalDBLoadOption()
  {
		JFrame frame = new JFrame();
		Object[] options = { "Foralize Database Transaction" };
    
		//JOptionPane.showMessageDialog(null, "Data Transformed to ESA Plot Format", "Error!",
    //                                JOptionPane.QUESTION_MESSAGE);
    int n = JOptionPane.showOptionDialog( frame, "Data Loaded to ESA Plot Format", "Post transform options",  
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0] );
		
		if (n == 0)
		{
			sql2db sql = new sql2db("makePlotSummaryTables_postgres.sql");
			sql.insertStatement();
			System.out.println("Created Database Snapshot");
		}
		else
		{
			System.err.println("unknown Option" + n);
 		}
  } 
	
	
	
	
	
	
	/**
	 * this method is the method that initiates the process of converting a
	 * TNC plot data set, consisting of a site and species file into the xml 
	 * document to be parsed into the local and remote database
	 *
	 * @param projectFile -- the file containing the project information for the
	 * tnc plots
	 * @param siteFile -- the file containing the site information for the
	 * tnc
	 * @param speciesFile -- the file containing the species information
	 * @param deleimeter -- the delimeter
	 */
	 public void tranformTNCDataSet(String projectFile, String siteFile, 
	 String speciesFile, String delimeter)
	 {
		 try
		 {
		 	System.out.println("transforming the tnc data set");
			 //print out the tncDataPackage xml file replacing the filename information
			 // with the file name information passed to this method
/* BELOW IS OLD CODE BUT THOUGHT TO LEAVE IT FOR AMUSEMENT
			 Vector fileVector = fileVectorizer(tncDataPackageTemplate);
			 //replace the site and species file names in the package file
			Vector vec = replaceElement(fileVector, "projectFile", projectFile);
			Vector vec1 = replaceElement(vec, "siteFile", siteFile);
			Vector vec2 = replaceElement(vec1, "speciesFile", speciesFile ) ;
	 		//print the vector to the file system
			printVector( vec2, "tmp.xml" );
*/			
			//open the HtmlViewer and pronounce the number of plots in the data set
			 HtmlViewer hv = new  HtmlViewer(); 
       hv.show();
       hv.showData(" ");
			 //get the number of plots into the screen
			 hv.showData( "number of plots in dataset: "+tncConverter.getNumberOfPlots(siteFile) );
			 
				//transform the dataset
				
				tncConverter.transformTNCData( siteFile, speciesFile);
				//spool the errors into the html viwer
				for (int i=0; i<tncConverter.errorLog.size(); i++) 
				{
					//just print the last 50 lines because  there is a problem with the
					// viewer class
					if (i > (tncConverter.errorLog.size()-100) )
					{
						hv.addData( tncConverter.errorLog.elementAt(i).toString() );
					}
				}
		 }
			catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
			e.printStackTrace();
		}
	 }
	
	
	/**
	 * method that takes a vector and a file name and writes it to the file System
	 * @param vec -- the name of the input vector
	 * @param fileName -- the name of the file that the vector should be written
	 * to
	 */
	 public void printVector(Vector vec, String fileName)
	 {
		 try
		 {
			 PrintWriter out = new PrintWriter(new FileWriter(fileName));
			 for (int i=0; i<vec.size(); i++) 
			 {
				 //System.out.println( vec.elementAt(i) );
					out.println(vec.elementAt(i).toString() );
			 }
			 out.close(); 
		 }
			 catch (Exception e) 
		{
			System.out.println("failed: " + 
			e.getMessage());
		}
		 
	 }
	 
	/**
	 * method that replaces an element in a vector.  An element is denoted with
	 * the '@' sign buffering either side of a string thus if the element to be
	 * replaced in the vector is 'fileName' the string '@fileName@' should exist
	 * somewhere in the vector
	 * @param vec -- the vector containing the tags
	 * @param elementName - the name of the element minus the '@' buffering the
	 * string (ie. 'fileName' not '@filename@')
	 * @param replaceString -- the string that should replace the element
	 */
	 public Vector replaceElement(Vector vec, String elementName, String replaceString)
	 {
		 Vector returnVec = new Vector();
		 for (int i=0; i<vec.size(); i++) 
		 {
			 if (vec.elementAt(i).toString().indexOf(elementName)>0 )
			 {
				 //capture this line for changing
				 String elementLine = vec.elementAt(i).toString();
				 if (elementLine.indexOf("@")>0)
				 {
					 StringTokenizer t = new StringTokenizer(elementLine, "@");
					 //the first bit 
					 String firstToken = t.nextToken();
					 //the string to be replaced
					 String replacePortion = t.nextToken();
					 //the last bit
					 String lastToken = t.nextToken();
					 //replace the element in the vector
					 vec.set(i, firstToken+replaceString+lastToken);
					 
					 System.out.println( vec.elementAt(i) );
				 }
			 }
		 }
		 return(vec);
	 }
	 
	

/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then makes the vector and number of vector elements access
 *  to the public 
 *
 * @param fileName name of the file that whose contents should be written to a vector
 */
public Vector fileVectorizer (String fileName) 
{
		Vector localVector = new Vector();
	try 
	{
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String s;
		while((s = in.readLine()) != null) 
		{
			//System.out.println(s);	
			localVector.addElement(s);
		}
	}
	catch (Exception e) 
	{
		System.out.println("failed: " + 
		e.getMessage());
	}
	return(localVector);
}


	/**
	 * this method runs the interface system based on the 
	 * xml file that configures the process
	 */
	public void initiateProgressManager()
	{
		//start the process status display
		System.out.println("status maneger running: "+statusManagerRunning);
		if (statusManagerRunning == false)
		{
			//call the method that displays the appropriate tools on the 
			//progress manager
			new ProcessProgressDisplay().show();
			statusManagerRunning = true;
		}
		else
		{
			//this is a test -- make the describe project status show yellow
			//ProcessProgressDisplay.projectInitializePanel.remove( ProcessProgressDisplay.red_label );
			ProcessProgressDisplay.projectDescriptionStatus.setText("initializing");
			System.out.println("process status monitor already running");
		}
	}
	
	
	/**
	 * method that takes as input a hash table that contains the 
	 * status of the paremeters in the project initialization 
	 * GUI and determines if all the required selections have been 
	 * made if not the public variable errorMessage is updated and
	 * a boolean false is returned
	 *
	 */
	public boolean initQAQC(Hashtable params)
	{ 
		System.out.println("'"+params.get("projectName").toString().trim()+"'");
		if (params.get("projectName").toString().trim() == null 
			|| params.get("projectName").toString().trim().length() < 1)
		{
			errorMessage.append("no project name identified");
			return(false);
		}
		else
		{
			return(true);
		}
	}


	/**
	 * method to print the xml file that will be used thruout the 
	 * workflow process using the configuration settings selected
	 * by the user on the process inititation GUI
	 * 
	 * @param params -- a hash table containing the elements required for the
	 * creation of the workflow 'process' xml file
	 */
	public void  initiateProcessXML(Hashtable params)
	{
		 try
     {
      PrintWriter out = new PrintWriter(new FileWriter("process.xml"));
     	out.println("<?xml version=\"1.0\"?>");
			out.println("<workflow>");
			//just print the hashtable as an xml file -- a hack for now
			
     	for (Enumeration e = params.keys() ; e.hasMoreElements() ;) 
			{
				StringBuffer sb = new StringBuffer();
				String currentTag = e.nextElement().toString();
				sb.append( getStartTag( currentTag) );
				sb.append( params.get(currentTag) );
				sb.append( getEndTag(currentTag) );
         out.println( sb.toString() );
				 //System.out.println( sb.toString() );
     	}
			out.println("</workflow>");
			out.close();
		 }
     catch(Exception e)
     {
			 System.out.println("failed: "+e.getMessage() );
		 }
	}

	
	
	
	
	/**
	 * method that initiates the project xml file using as input a
	 * hashtable containing the basic information for a project
	 * sush as the name and description of the project
	 *
	 * @param params -- a hash table with the top-level basic information for a
	 *  project including projectName, and projectDescription
	 *
	 */
	public void  initiateProjectXML(Hashtable params)
	{
		 try
     {
      PrintWriter out = new PrintWriter(new FileWriter("project.xml"));
     	out.println("<?xml version=\"1.0\"?>");
				out.println("<vegPlot>");
			out.println("<project>");
			//just print the hashtable as an xml file -- a hack for now
			
     	for (Enumeration e = params.keys() ; e.hasMoreElements() ;) 
			{
				StringBuffer sb = new StringBuffer();
				String currentTag = e.nextElement().toString();
				sb.append( getStartTag( currentTag) );
				sb.append( params.get(currentTag) );
				sb.append( getEndTag(currentTag) );
         out.println( sb.toString() );
				 //System.out.println( sb.toString() );
     	}		
			// now print the veg project node
			out.println("</project>");
			out.close();
		 }
     catch(Exception e)
     {
			 System.out.println("failed: "+e.getMessage() );
		 }
	}
	
	
	
	
	
	/**
	 * method that returns the start tag for a tagname
	 * given as input the tag name
	 * --should probably be in the XMLparse class
	 *
	 */
	public String getStartTag(String tagName)
	{
		return("<"+tagName+">");
	}
	
	/**
	 * method that returns the end tag for a tagname
	 * given as input the tag name
	 * --should probably be in the XMLparse class
	 *
	 */
	public String getEndTag(String tagName)
	{
		return("</"+tagName+">");
	}
	
}

