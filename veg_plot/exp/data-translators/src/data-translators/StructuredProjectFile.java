/**
 * class that was designed as a model for a structured Project data file, which is a 
 * file the has a very well structured header file that can be used to develop
 * a project XML file that is to be ingested into the plots database 
 *
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: farrell $'
 *  '$Date: 2003-01-14 01:12:39 $'
 * 	'$Revision: 1.2 $'
 */
//package vegclient.framework;

import java.util.Vector;

//import vegclient.framework.*;

public class StructuredProjectFile extends StructuredDataFile
{
	
	public String projectName = null;
	public String projectDescription = null;
	
	/**
	 * constructor that matches the parent constructor
	 */
	public StructuredProjectFile(String fileName)
	{
		super(fileName);
		projectName = getProjectName();
		projectDescription = getProjectDescription();
		//System.out.println("projectname: "+ projectName);
	}
	
	
	/**
	 * method to return a list of projects in this projectFile
	 */
	 public Vector getProjectNameList()
	 {
		 Vector projectList = new Vector();
		 int colNum =  Integer.parseInt(super.dbAttributesHash.get("projectName").toString() );
		 projectList= super.getColumnElements( colNum );
		 return(projectList);
	 }
	
	/**
	 * method to return a projectName from the project data package
	 */
	 public String getProjectName()
	 {
		 String projectName = null;
		 int colNum =  Integer.parseInt(super.dbAttributesHash.get("projectName").toString() );
		 projectName= super.getColumnElements( colNum ).elementAt(0).toString() ;
		 return(projectName);
	 }
	
	/**
	 * method to return a projectName from the project data package
	 */
	 public String getProjectDescription()
	 {
		 String projectDescription = null;
		 int colNum =  Integer.parseInt(super.dbAttributesHash.get("projectDescription").toString() );
		 projectDescription = super.getColumnElements( colNum ).elementAt(0).toString() ;
		 return(projectDescription);
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
				System.out.println("Usage: java StructuredProjectFile [fileName]  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String fileName=args[0];

				StructuredProjectFile structuredProjectFile = new StructuredProjectFile(fileName);
				System.out.println( "file type: "+structuredProjectFile.getFileType());
				System.out.println( "Version: "+structuredProjectFile.getVersion());
				System.out.println( "Delimiter: "+structuredProjectFile.getDelimeter() ) ;
				System.out.println( "Format: "+structuredProjectFile.getFormat() ) ;
				//System.out.println( structuredProjectFile.dataVector.toString() );
				System.out.println( "Project Name: "+structuredProjectFile.getProjectNameList().toString() );
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
}
