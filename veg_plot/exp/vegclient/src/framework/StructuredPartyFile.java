/**
 * class that was designed as a model for a structured Project data file, which is a 
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

public class StructuredPartyFile extends StructuredDataFile
{
	
	
	
	/**
	 * constructor that matches the parent constructor
	 */
	public StructuredPartyFile(String fileName)
	{
		super(fileName);
	}
	
	
	/**
	 * method to return a list of surNames in this file
	 */
	 public Vector getSurNameList()
	 {
		 Vector vec = new Vector();
		 try
		 {	 
			 int colNum =  Integer.parseInt(super.dbAttributesHash.get("surName").toString() );
			 vec = super.getColumnElements( colNum );
			}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
			} 
			 return(vec);
	 }
	 
	 /**
	 * method to return a list of givenNames in this file
	 */
	 public Vector getGivenNameList()
	 {
		 Vector vec = new Vector();
		 try
		 {
			 int colNum =  Integer.parseInt(super.dbAttributesHash.get("givenName").toString() );
			 vec= super.getColumnElements( colNum );
		 	}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
			}
		 return(vec);
	 }
	 
	  
	 /**
	 * method to return a list of givenNames in this file
	 */
	 public Vector getOrgNameList()
	 {
		 Vector vec = new Vector();
		 try
		 {
			 int colNum =  Integer.parseInt(super.dbAttributesHash.get("orgName").toString() );
			 vec= super.getColumnElements( colNum );
		 	}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
			}
		 return(vec);
	 }
	 
	 
	  /**
	 * method to return a list of givenNames in this file
	 */
	 public Vector getEmailAddressList()
	 {
		 Vector vec = new Vector();
		 try
		 {
			 int colNum =  Integer.parseInt(super.dbAttributesHash.get("emailAddress").toString() );
			 vec= super.getColumnElements( colNum );
		 	}
			catch (Exception e)
			{
				System.out.println("Caught Exception: "+e.getMessage() ); 
				e.printStackTrace();
			}
		 return(vec);
	 }
	
	
	
	/**
   * the main routine used to test the StructureProjectFile class which 
	 * interacts with the vegclass database.
   * <p>
   * Usage: java StructureProjectFile <filename ....>
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
				System.out.println("Usage: java StructuredPartyFile [fileName]  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String fileName=args[0];

				StructuredPartyFile StructuredPartyFile = new StructuredPartyFile(fileName);
				System.out.println( "file type: "+StructuredPartyFile.getFileType());
				System.out.println( "Version: "+StructuredPartyFile.getVersion());
				System.out.println( "Delimiter: "+StructuredPartyFile.getDelimeter() ) ;
				System.out.println( "Format: "+StructuredPartyFile.getFormat() ) ;
				//System.out.println( StructuredPartyFile.dataVector.toString() );
			//	System.out.println( "orgAddress: "+StructuredPartyFile.getOrgNameList().toString() );
			//	System.out.println( "surName: "
			//	+StructuredPartyFile.dbAttributesHash.toString() );
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
}
