/**
 * This class was written to simply load plots data directly to an 
 * instance of the plots database.  The plots data should be stored 
 * in two columnar-ascii tables where the first is a table containing
 * 'site' data and the second table should contain 'plant taxonomy data'
 * this is not a replacement for the legacy data loader which loads the 
 * database from any number of files -- and that, as an interum step, 
 * creates xml documents conforming to the plots standard dtd.
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:41 $'
 * 	'$Revision: 1.1 $'
 */
package vegclient.framework;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.net.*;

import vegclient.framework.*;

public class AsciiToPlotDB
{
	ResourceBundle rb = ResourceBundle.getBundle("dataload");
	static LocalDbConnectionBroker lb;


	/**
 	* main method to start the class
 	*/
	public static void main(String[] args) 
	{
		if (args.length != 1) 
		{
			System.out.println("Usage: java xxx load");
			System.exit(0);
		}
		else 
		{
			AsciiToPlotDB apdb = new AsciiToPlotDB();
			apdb.uploadPlotsData();
			
		}
	}
	
	
	/**
	 * This method may be thought of as the real main implementation
	 * method and which controls the use of the other methods in the
	 * class.  Basically the method gets the attributes needed to load 
	 * the data from the properties file 
	 */ 
	private void uploadPlotsData()
	{
		//initiate the database connections
		lb.manageLocalDbConnectionBroker("initiate");
		
		//get the hashtable containg the properties from the properties file
		Hashtable asciiDataAttributes = getAsciiDataAttributes();

		//read both the site and species files into a vector
		Vector siteFileVector=fileVectorizer( 
			(String)asciiDataAttributes.get("plotSiteDataFile") );
		Vector speciesFileVector=fileVectorizer( 
			(String)asciiDataAttributes.get("plotSpeciesDataFile") );
		
		//first update the project-level attributes
		int projectId = putProjectAttributes("PEL");
		
		
		
		//for each line in the plot-site file extract the elements
		//int a hash and load the data to the appropriate tables
		for (int i=0; i<siteFileVector.size(); i++) 
		{
			//System.out.println( siteFileVector.elementAt( i )) ;
			//get the site attributes for this line
			Hashtable siteAttributes = getSiteAttributes(siteFileVector.elementAt(i).toString()
			, asciiDataAttributes);
			//add some of the static attributes stored in the properties file etc..
			siteAttributes.put("projectId", ""+projectId);
			
			Hashtable speciesAttributes = getSpeciesAttributes(speciesFileVector
			, asciiDataAttributes, siteAttributes.get("authorPlotCode").toString() );	
		
			//update the plot variables
			Hashtable siteDBResults =	putPlotSiteAttributes( siteAttributes );
			putPlotTaxonAttributes( speciesAttributes, siteDBResults );
		}
	}
	
	
	private Hashtable getSpeciesAttributes(Vector speciesFileVector, 
		Hashtable asciiDataAttributes, String authorPlotCode)
	{
		Hashtable speciesAttributes = new Hashtable();
		
		//grab those rows form the file vector that pertain to this
		//plot.  below is a hack but will work for the time being
		Vector currentPlotSpeciesVector = new Vector();
		for (int i=0; i<speciesFileVector.size(); i++) 
		{
			if (speciesFileVector.elementAt(i).toString().startsWith(authorPlotCode) ||
				speciesFileVector.elementAt(i).toString().indexOf(authorPlotCode)>0)
			{
				currentPlotSpeciesVector.add( speciesFileVector.elementAt(i) );
				//System.out.println( speciesFileVector.elementAt(i));
			}
		}
		//speciesAttributes.put("currentPlotSpeciesVector", currentPlotSpeciesVector);
		speciesAttributes.put("authorPlotCode", authorPlotCode);
		
		Vector taxonName = new Vector();
		Vector cumStrataCover = new Vector();
		
		String taxonNamePosition = asciiDataAttributes.get("taxonNamePosition").toString();
		String cumStrataCoverPosition = asciiDataAttributes.get("cumStrataCoverPosition").toString();
		
		taxonName = extractVectorElements(currentPlotSpeciesVector,taxonNamePosition);
		cumStrataCover = extractVectorElements(currentPlotSpeciesVector,cumStrataCoverPosition);
		speciesAttributes.put("taxonName", taxonName);
		speciesAttributes.put("cumStrataCover", cumStrataCover);
		
		return(speciesAttributes);
	}
	
	
	/** 
	 * This is a utility method that grabs the site attributes into 
	 * a hashtable that are then to be stuck into the database
	 *
	 * @param siteAsciiFileLine -- a sinle line for the site ascii file
	 *	that has the data which is to be loaded to the database
	 * @param asciiDataAttribues -- these are all the attributes from the 
	 *	properties file stored as a hashtable
	 */
	private Hashtable getSiteAttributes(String siteAsciiFileLine, Hashtable asciiDataAttributes)
	{
		Hashtable siteAttributes = new Hashtable();
		try 
		{
			//in the future use the 'getPropertyFileAttributes' method 
			// to get all the attributes needed, but for now just do it
			// by hand
			String authorPlotCode = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("authorPlotCodeSiteFilePosition").toString()));
			String slopeAspect = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("slopeAspectPosition").toString()));
			String slopeGradient = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("slopeGradientPosition").toString()));
			String plotComments = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("plotCommentsPosition").toString()));
			String soilDepth = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("soilDepthPosition").toString()));
			String soilType = extractLineElement(siteAsciiFileLine,
				(asciiDataAttributes.get("soilTypePosition").toString()));
				
			siteAttributes.put("authorPlotCode", authorPlotCode);
			siteAttributes.put("slopeAspect", slopeAspect);
			siteAttributes.put("slopeGradient", slopeGradient);
			siteAttributes.put("plotComments", plotComments);
			siteAttributes.put("soilDepth", soilDepth);
			siteAttributes.put("soilType", soilType);
			
			//now stick in the attributes which have a static value for all the plots
			String state = rb.getString("static.state");
			String surfGeo = rb.getString("static.surfGeo");
			String namedPlace = rb.getString("static.namedPlace");
			String plotType = rb.getString("static.plotType");
			
			//update the attribute hash
			siteAttributes.put("state", state);
			siteAttributes.put("surfGeo", surfGeo);
			siteAttributes.put("namedPlace", namedPlace);
			siteAttributes.put("plotType", plotType);
			
		}
		catch( Exception e ) 
		{
			System.out.println(" failed in: "
			+e.getMessage() );
			e.printStackTrace();
		}
		return(siteAttributes);
	}
	
	
	/**
	 *
	 */ 
	private Vector extractVectorElements(Vector containingVector , String tokenLinePosition)
	{
		int attributePositionInt = Integer.parseInt(tokenLinePosition.trim());
		Vector returnVector = new Vector();
		
		//parse each of the elements
		for (int i=0; i<containingVector.size(); i++) 
		{
			String token = pipeStringTokenizer(containingVector.elementAt(i).toString() , attributePositionInt);
			returnVector.add(token);
			//System.out.println(token);
		}
		return(returnVector);
	}
	
	
	/**
	 *
	 */ 
	private String extractLineElement( String asciiTextLine, String tokenLinePosition)
	{
		int attributePositionInt = Integer.parseInt(tokenLinePosition.trim());
		String token = pipeStringTokenizer( asciiTextLine, attributePositionInt);
		return(token);
	}
	
	
	/**
 	* method to tokenize elements from a pipe delimeted string
 	*/	
	public String pipeStringTokenizer(String pipeString, int tokenPosition)
	{
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
	 * This is a utility method that grabs the parameters from the 
	 * properties file which are required for the program to run
	 */
	private Hashtable getAsciiDataAttributes()
	{
		Hashtable asciiDataAttributes = new Hashtable();
		try 
		{
			//get the parameters from the properties file
			for (int i=0; i<getPropertiesFileAttributes().size(); i++) 
			{
				//add the attribute from the properties file to the hashtable
			asciiDataAttributes.put(getPropertiesFileAttributes().elementAt(i).toString()
			, rb.getString( getPropertiesFileAttributes().elementAt(i).toString() ));
 			}
		}
		catch( Exception e ) 
		{
			System.out.println(" failed in: "
			+e.getMessage() );
			e.printStackTrace();
		}
		return(asciiDataAttributes);
	}
	
	
	/**
	 * this method stores the attributes that must be stored in the 
	 * properties file.  The method is to be used for the display
	 * of the usage at start up as well as at other times where there 
	 * is some need for structure / oraganization of the properties in 
	 * the properties file.
	 *
	 * @results propertiesFileAttributes -- the properties that must be 
	 *		in the 'loaddata' properties file.
	 */
	
	private Vector getPropertiesFileAttributes()
	{
		Vector propertiesFileAttributes = new Vector();
		//populate the vector
		propertiesFileAttributes.add("plotSiteDataFile");
		propertiesFileAttributes.add("plotSpeciesDataFile");
		propertiesFileAttributes.add("plotSiteDataFileDelimeter");
		propertiesFileAttributes.add("plotSpeciesDataFileDelimeter");
		propertiesFileAttributes.add("authorPlotCodeSiteFilePosition");
		propertiesFileAttributes.add("authorPlotCodeSpeciesFilePosition");
		//site specific attributes
		propertiesFileAttributes.add("slopeAspectPosition");
		propertiesFileAttributes.add("slopeGradientPosition");
		propertiesFileAttributes.add("plotCommentsPosition");
		propertiesFileAttributes.add("soilTypePosition");
		propertiesFileAttributes.add("soilDepthPosition");
		
		//species specific attributes
		propertiesFileAttributes.add("taxonNamePosition");
		propertiesFileAttributes.add("cumStrataCoverPosition");
		
		//return the properties to the method
		return(propertiesFileAttributes);
	}
	
	
	
/**
 *  Method that takes as input a name of a file and writes the file contents 
 *  to a vector and then returns the vector 
 *
 * @param fileName name of the file that whose contents should be 
 * written to a vector
 */
	public Vector fileVectorizer (String fileName) 
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
 
 
 
 
	private Hashtable putPlotSiteAttributes(Hashtable plotSiteAttributes)
	{
		//this hash table is returned to the clalling method 
		//with the attributes that are needed to load the other 
		//tables -- ie. the plant taxonomy -related tables
		Hashtable siteDBResults = new Hashtable();
		try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
			Statement query = conn.createStatement ();
			
			
			int projectId =  Integer.parseInt( plotSiteAttributes.get("projectId").toString() );
			String authorPlotCode = plotSiteAttributes.get("authorPlotCode").toString();
			String slopeAspect = plotSiteAttributes.get("slopeAspect").toString();
			String slopeGradient = plotSiteAttributes.get("slopeGradient").toString();
			String plotComments = plotSiteAttributes.get("plotComments").toString();
			String soilType = plotSiteAttributes.get("soilType").toString();
			String soilDepth = plotSiteAttributes.get("soilDepth").toString();
			String state = plotSiteAttributes.get("state").toString();
			String namedPlace = plotSiteAttributes.get("namedPlace").toString();
			String plotType = plotSiteAttributes.get("plotType").toString();
			String surfGeo = plotSiteAttributes.get("surfGeo").toString();
			
			//UPDATE THE PLOT TABLE
			//get the next tuple value
			int plotId = getNextTupleValue("PLOT", conn);
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT into PLOT (plot_id, project_id, authorPlotCode, ");
			sb.append(" slopeAspect, slopeGradient, state, namedPlace, plotType, surfGeo)");
			sb.append("VALUES ( "+plotId+", "+projectId);
			sb.append(", '"+authorPlotCode+"', '"+slopeAspect+"', '"+slopeGradient+"', ");
			sb.append(" '"+state+"', '"+namedPlace+"', '"+plotType+"', '"+surfGeo+"')");
			System.out.println("PLOT SITE ISSUED: "+ sb.toString() );
			//issue the query
			query.executeUpdate(sb.toString());
			
			//UPDATE THE PLOT OBSERVATION TABLE
			int plotObservationId = getNextTupleValue("plotObservation", conn);
			sb = new StringBuffer();
			sb.append("INSERT into PLOTOBSERVATION (obs_id, parentPlot, authorObsCode, ");
			sb.append("soilType, soilDepth )");
			sb.append("VALUES ( "+plotObservationId+", "+plotId+", '");
			sb.append(authorPlotCode+"', '"+soilType+"', '"+soilDepth+"')");
			System.out.println("PLOT OBSERVATION ISSUED: "+ sb.toString() );
			//issue the query
			query.executeUpdate(sb.toString());
			lb.manageLocalDbConnectionBroker("destroy");
			
			//add the important attributes to the hashtable that is to be returned
			//with the results needed to go further
			siteDBResults.put("plotId", ""+plotId);
			siteDBResults.put("plotObservationId", ""+plotObservationId);
		}
		catch (Exception e) 
		{
			System.out.println("failed " 
			+e.getMessage());
		}
		return(siteDBResults);
	}
	
	
	
	
	private boolean putPlotTaxonAttributes(Hashtable plotTaxonAttributes, 
	Hashtable siteDBResults )
	{
		try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
			Statement query = conn.createStatement ();
			
			
			int plotObservationId =  Integer.parseInt( siteDBResults.get("plotObservationId").toString() );
			Vector taxonNameVec = (Vector)plotTaxonAttributes.get("taxonName");
			Vector cumStrataCoverVec = (Vector)plotTaxonAttributes.get("cumStrataCover");
			
			
			//load all the taxon elements in the vector
			for (int i=0; i<taxonNameVec.size(); i++) 
			{
				int taxonObservationId = getNextTupleValue("taxonObservation", conn);
				String taxonName = taxonNameVec.elementAt(i).toString();
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT into  TAXONOBSERVATION(TAXONOBSERVATION_ID, OBS_ID, AUTHORNAMEID)");
				sb.append("VALUES ( "+taxonObservationId+", "+plotObservationId+", '"+taxonName+"')");
				System.out.println("TAXON OBSERVATION ISSUED: "+ sb.toString() );
				//issue the query
				query.executeUpdate(sb.toString());
				
				//update the startacomposition table
				sb = new StringBuffer();
				sb.append("INSERT into  STRATACOMPOSITION( TAXONOBSERVATION_ID, STRATA_ID, CHEATSTRATUMTYPE, PERCENTCOVER)");
				sb.append("VALUES ( "+taxonObservationId+",  1, 'tree', 12)");
				System.out.println("STRATACOMP  ISSUED: "+ sb.toString() );
				//issue the query
				query.executeUpdate(sb.toString());
			}
		}
		catch (Exception e) 
		{
			System.out.println("failed " 
			+e.getMessage());
		}
		return(false);
	}
	
	
	
	
	/**
	 * method to verify that the project information is there 
	 * and if not will create a project, else the project_id 
	 * will be returned
	 */
	private int putProjectAttributes(String projectName)
	{
		int storedProjectId = -999;
		try 
		{
			lb.manageLocalDbConnectionBroker("initiate");
			System.out.println("Grabbing a DB connection from the local pool");
			//get the connections etc
			Connection conn = lb.manageLocalDbConnectionBroker("getConn");
			Statement query = conn.createStatement ();
			ResultSet results= null;
			
			//get the next touple value
			int plotId = getNextTupleValue("PLOT", conn);
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT projectName, project_id from PROJECT ");
			sb.append("WHERE projectName like '"+projectName+"'");
			
			//issue the query
			results = query.executeQuery(sb.toString());
			
			//get the results
			while (results.next()) 
			{
				if (results.getString(1) != null) 
				{
					String projectNameResult = results.getString(1); 
					storedProjectId = results.getInt(2); 
				}
			}
			
			//if the 'storedProjectId' is not updated then update the database
			if (storedProjectId == -999)
			{
				System.out.println("project does not exist");
			}
			else
			{
				System.out.println("project already stored in the database");
			}
			
			
			//destroy the connection
			lb.manageLocalDbConnectionBroker("destroy");
		}
		catch (Exception e) 
		{
			System.out.println("failed " 
			+e.getMessage());
		}
		return(storedProjectId);
	}
	
	
	
	
	/**
	 * method to get the next row (tuple) level in a table
	 * for use in updating the primary key
	 *
	 */
	private int getNextTupleValue(String tableName, Connection pconn) 
	{
		int lastNum=0;
		Statement query = null;
		ResultSet results = null;
		try 
		{
			query = pconn.createStatement ();;	
		} 
		catch (Exception e)  
		{
			System.out.println("failed at: "
			+"transfering db connections"
			+ e.getMessage());
			e.printStackTrace();
		}
		try 
		{	
			ResultSet lastValue=query.executeQuery("SELECT count(*) from "+tableName+"");
      while (lastValue.next()) 
			{
				lastNum = lastValue.getInt(1);
			}
		lastNum++;
		lastValue.close();
		} //end try
		catch (Exception e) 
		{
			System.out.println("failed in PlotDBWriter.getNextId trying "
			+"to query the next id value in a table data: " + e.getMessage());
		}
		return(lastNum);
	} 

	
	
	
}
