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

/**
 * This is the class that manages the workflow manager
 * processes
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-08-21 23:13:30 $'
 * 	'$Revision: 1.2 $'
 */
public class ProjectManager extends JFrame 
{
	public StringBuffer errorMessage = new StringBuffer();
	public static boolean statusManagerRunning = false;
//	ProcessProgressDisplay pd = new ProcessProgressDisplay();
	
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

