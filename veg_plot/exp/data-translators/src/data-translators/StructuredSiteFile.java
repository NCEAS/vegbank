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

public class StructuredSiteFile extends StructuredDataFile
{
	
	public Vector authorPlotCodes = new Vector();
	public Vector elevationValues = new Vector();
	public Vector stateValues = new Vector();
	public Vector origLatValues = new Vector();
	public Vector origLongValues = new Vector();
	public Vector plotSizeValues = new Vector();
	public Vector placeNameValues = new Vector();
	public Vector countryValues = new Vector();
	public Vector treeStemAreaValues = new Vector();
	
	//variables  that can be refered to to see if the variable exists in the
	// package
	public boolean authorPlotCodesExist = false;
	public boolean elevationValuesExist = false;
	public boolean stateValuesExist = false;
	public boolean origLatValuesExist = false;
	public boolean origLongValuesExist = false;
	public boolean plotSizeValuesExist = false;
	public boolean placeNameValuesExist = false;
	public boolean countryValuesExist = false;
	public boolean treeStemAreaValuesExist = false;
	
	
	/**
	 * constructor that matches the parent constructor
	 */
	public StructuredSiteFile(String fileName)
	{
		super(fileName);
		authorPlotCodes = getAuthorPlotCodes();
		elevationValues = getElevationValues();
		stateValues = getStateValues();
		origLatValues = getOrigLatValues();
		origLongValues = getOrigLongValues();
		plotSizeValues = getPlotSizeValues();
		placeNameValues = getPlaceNameValues(); //may be multiple
		countryValues = getCountryValues();
		treeStemAreaValues = getTreeStemAreaValues();
		
		//System.out.println("plot codes: "+ authorPlotCodes.toString() );
	}
	
	
	/**
	 * method to return a list of plots in this site file
	 */
	 public Vector getAuthorPlotCodes()
	 {
		 Vector vec = new Vector();
		 int colNum =  Integer.parseInt(super.dbAttributesHash.get("authorPlotCode").toString() );
		 vec = super.getColumnElements( colNum );
		 //update the varible that can be refered to to see if this attribute exists
		 if ( vec.size() > 0)
		 {
			 authorPlotCodesExist=true;
		 }
		 return(vec);
	 }

	 
	/**
	 * method to return a list of elevations in this site file
	 */
	 public Vector getElevationValues()
	 {
		 Vector vec = new Vector();
		 if ( super. dbAttributesHash.containsKey("elevationValue") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("elevationValue").toString() );
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 elevationValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	  
	/**
	 * method to return a list of projects in this projectFile
	 */
	 public Vector getStateValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("state") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("state").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 stateValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	 
	   
	/**
	 * method to return a list of latitudes in this projectFile
	 */
	 public Vector getOrigLatValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("origLat") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("origLat").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 origLatValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	 /**
	 * method to return a list of longitudes in this projectFile
	 */
	 public Vector getOrigLongValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("origLong") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("origLong").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 origLongValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	 /**
	 * method to return a list of plotSize (area) in this projectFile
	 */
	 public Vector getPlotSizeValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("plotSize") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("plotSize").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 plotSizeValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	/**
	 * method to return a list of placeName in this projectFile
	 */
	 public Vector getPlaceNameValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("placeName") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("placeName").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 placeNameValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	 /**
	 * method to return a list of country in this projectFile
	 */
	 public Vector getCountryValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("country") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("country").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 countryValuesExist = true;
		 	}
		}
		 return(vec);
	 }
	 
	 
	  
	 /**
	 * method to return a list of country in this projectFile
	 */
	 public Vector getTreeStemAreaValues()
	 {
		 Vector vec = new Vector();
		 //System.out.println( super. dbAttributesHash.toString() );
		 if ( super. dbAttributesHash.containsKey("treeStemArea") )
		 {
		 	int colNum =  Integer.parseInt(super.dbAttributesHash.get("treeStemArea").toString() );
			//System.out.println("state col: " + colNum);
		 	vec = super.getColumnElements( colNum );
			 //update the varible that can be refered to to see if this attribute exists
		 	if ( vec.size() > 0)
		 	{
				 treeStemAreaValuesExist = true;
		 	}
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
				System.out.println("Usage: java StructuredSiteFile [fileName]  \n"
				+" ");
				System.exit(0);
			}
			else
			{
				String fileName=args[0];

			//	StructuredSiteFile structuredProjectFile = new StructuredSiteFile(fileName);
			//	System.out.println( "file type: "+structuredProjectFile.getFileType());
			//	System.out.println( "Version: "+structuredProjectFile.getVersion());
			//	System.out.println( "Delimiter: "+structuredProjectFile.getDelimeter() ) ;
			//	System.out.println( "Format: "+structuredProjectFile.getFormat() ) ;
				//System.out.println( structuredProjectFile.dataVector.toString() );
			//	System.out.println( "Project Name: "+structuredProjectFile.getProjectNameList().toString() );
			}
		 
		}
		catch (Exception e)
		{
			System.out.println("Caught Exception: "+e.getMessage() ); 
			e.printStackTrace();
		}
	}
	
	
	
}
