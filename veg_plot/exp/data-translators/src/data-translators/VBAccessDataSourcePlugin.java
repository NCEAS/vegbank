import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


/**
 * plugin to allow access to plot data stored in the VegBank - plots 
 * database 
 *
 *  Release: 
 *	
 *  '$Author: farrell $'
 *  '$Date: 2002-12-28 00:37:12 $'
 * 	'$Revision: 1.10 $'
 */
 
//public class VBAccessDataSourcePlugin
public class VBAccessDataSourcePlugin extends VegBankDataSourcePlugin implements PlotDataSourceInterface 
{
	private String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	private String dbUrl = "jdbc:odbc:vegbank_access";
	private String dbUser = "datauser";
	private Connection con = null;
	
	
	public VBAccessDataSourcePlugin()
	{
		super("msaccess");
		try 
		{
			con = super.con;
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex + "<BR>");
			ex.printStackTrace();
		}
	}


 	/** 
	 * method that returns the accession number associated with a plot id
	 * the input plot id is the unique identifier of the plot as used by 
	 * the RDBMS
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getAccessionValue(String plotId)
	{
		String s = "";
		return(s);
	}
	
	/** 
	 * method that returns the accession number for a given plot observation.  
	 * This method must be in this class eventhough it is not  relevant; because
	 * the TNC plots database does not use an accession number this method 
	 * will always return null
	 * @param plotId -- the RDBMS unique plot ID
	 */
	public String getObservationAccessionNumber(String plotId)
	{
		return("");
	}
	
  /**
	 * method to return the taxa code from a data source using as input
	 * the scientific plant name -- or the plant name that comes from
	 * the 'getPlantTaxaNames' method
	 *
	 * @param plantName -- the scientific plantName
	 * @see DBinsertPlotSource.insertTaxonObservation() -- calls this 
	 * 	method
	 */
	public String getPlantTaxonCode(String plantName)
	{
		String code = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append(" select PLANTCODE from PLANTCONCEPT where PLANTNAME like '"+plantName+"'");
			System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
			ResultSet rs = stmt.executeQuery( sb.toString() );	
			while (rs.next()) 
			{
				code = rs.getString(1);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(code);
	}
																																																																																																												
	
	
	/**
	 * method that returns a vector of PLOT ID'S (Plot table Primary keys) 
	 * from the target VegBank Database
	 */
	public Vector getPlotIDs()
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct PLOT_ID from PLOT");
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	
	/**
	 * method that returns a vector of plot codes which are the 
	 * VegBank Accession Numbers, for now it will be the authorPlotCode
	 * 
	 */
	public Vector getPlotCodes()
	{
		return super.getPlotCodes();
	}
 	

	/**
 	 * returns the soil drainage of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDrainage(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns the author's observation code
 	 * @param plotName -- the plot
 	 */
 	public String getAuthorObsCode(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns the start date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStartDate(String plotName)
 	{
		return super.getObsStartDate(plotName);
 	}
 	
 	/**
 	 * returns the stop date for the plot observation
 	 * @param plotName -- the plot
 	 */
 	public String getObsStopDate(String plotName)
 	{
		return super.getObsStopDate(plotName);	
 	}
 	
 	/**
 	 * returns the soil depth of a plot
 	 * @param plotName -- the plot
 	 */
 	public String getSoilDepth(String plotName) 
 	{
 		return super.getSoilDepth(plotName);
 	}
	
	//START
	/**
	*/
	public String getObsDateAccuracy(String plotName)
	{
 		return super.getObsDateAccuracy(plotName);
	}
	 
	/**
	*/
	public Hashtable getCoverMethod(String plotName)
	{
 		return super.getCoverMethod(plotName);
	}
	
	/**
	 */
	public Hashtable getStratumMethod(String plotName)
	{
 		return super.getStratumMethod(plotName);
	}
	
	/**
	 */
	public String getStemSizeLimit(String plotName)
	{
 		return super.getStemSizeLimit(plotName);
	}

	/**
	 */
	public String getMethodNarrative(String plotName)
	{
 		return super.getMethodNarrative(plotName);
	}
	
	/**
	 */
	public String getTaxonObservationArea(String plotName)
	{
 		return super.getTaxonObservationArea(plotName);
	}
	
	/**
	 */
	public String getCoverDispersion(String plotName )
	{
 		return super.getCoverDispersion(plotName);
	}
	
	/**
	 */
	public boolean getAutoTaxonCover(String plotName)
	{
 		return super.getAutoTaxonCover(plotName);
	}
	
	/**
	 */
	public String getStemObservationArea(String plotName)
	{
 		return super.getStemObservationArea(plotName);
	}
	
	/**
	 */
	public String getStemSampleMethod(String plotName)
	{
 		return super.getStemSampleMethod(plotName);
	}
	
	/**
	 */
	public String getOriginalData(String plotName )
	{
 		return super.getOriginalData(plotName);
	}
	
	/**
	 */
	public String getEffortLevel( String plotName )
	{
 		return super.getEffortLevel(plotName);
	}
	
	
	public String getPlotValidationLevel(String plotName)
	{
		return super.getPlotValidationLevel(plotName);
	}

public String  getFloristicQuality(String plotName)
{
	return super.getFloristicQuality(plotName);
}

public String  getBryophyteQuality(String plotName)
{
	return super.getBryophyteQuality(plotName);
}

public String  getLichenQuality(String plotName)
{
	return super.getLichenQuality(plotName);
}

public String  getObservationNarrative(String plotName)
{
	return super.getObservationNarrative(plotName);
}

public String  getHomogeneity(String plotName)
{
	return super.getHomogeneity(plotName);
}

public String  getPhenologicalAspect(String plotName)
{
	return super.getPhenologicalAspect(plotName);
}

public String  getRepresentativeness(String plotName)
{
	return super.getRepresentativeness(plotName);
}

public String  getBasalArea(String plotName)
{
	return super.getBasalArea(plotName);
}

public String  getSoilMoistureRegime(String plotName)
{
	return super.getSoilMoistureRegime(plotName);
}

public String  getWaterSalinity(String plotName)
{
	return super.getWaterSalinity(plotName);
}

public String  getShoreDistance(String plotName)
{
	return super.getShoreDistance(plotName);
}

public String  getOrganicDepth(String plotName)
{
	return super.getOrganicDepth(plotName);
}

public String  getPercentBedRock(String plotName)
{
	return super.getPercentBedRock(plotName);
}

public String  getPercentRockGravel(String plotName)
{
	return super.getPercentRockGravel(plotName);
}

public String  getPercentWood(String plotName)
{
	return super.getPercentWood(plotName);
}
public String  getPercentLitter(String plotName)
{
	return super.getPercentLitter(plotName);
}

public String  getPercentBareSoil(String plotName)
{
	return super.getPercentBareSoil(plotName);
}

public String  getPercentWater(String plotName)
{
	return super.getPercentWater(plotName);
}

public String  getPercentOther(String plotName)
{
	return super.getPercentOther(plotName);
}

public String  getNameOther(String plotName)
{
	return super.getNameOther(plotName);
}

public String  getStandMaturity(String plotName)
{
	return super.getStandMaturity(plotName);
}


public String  getSuccessionalStatus(String plotName)
{
	return super.getSuccessionalStatus(plotName);
}

public String  getTreeHt(String plotName)
{
	return super.getTreeHt(plotName);
}

public String  getShrubHt(String plotName)
{
	return super.getShrubHt(plotName);
}

public String  getNonvascularHt(String plotName)
{
	return super.getNonvascularHt(plotName);
}

public String  getFloatingCover(String plotName)
{
	return super.getFloatingCover(plotName);
}

public String  getSubmergedCover(String plotName)
{
	return super.getSubmergedCover(plotName);
}

public String  getDominantStratum(String plotName)
{
	return super.getDominantStratum(plotName);
}

public String  getGrowthform1Type(String plotName)
{
	return super.getGrowthform1Type(plotName);
}

public String  getGrowthform2Type(String plotName)
{
	return super.getGrowthform2Type(plotName);
}

public String  getGrowthform3Type(String plotName)
{
	return super.getGrowthform3Type(plotName);
}

public String  getGrowthform1Cover(String plotName)
{
	return super.getGrowthform1Cover(plotName);
}

public String  getGrowthform2Cover(String plotName)
{
	return super.getGrowthform2Cover(plotName);
}

public String  getGrowthform3Cover(String plotName)
{
	return super.getGrowthform3Cover(plotName);
}

public boolean  getNotesPublic(String plotName)
{
	return super.getNotesPublic(plotName);
}

public boolean  getNotesMgt(String plotName)
{
	return super.getNotesMgt(plotName);
}

public boolean  getRevisions(String plotName)
{
	return super.getRevisions(plotName);
}
//END
	
	

	/**
 	 * returns true if the plot is a permanent plot or false if not
 	 * @param plotName -- the plot
 	 */
 	 public boolean isPlotPermanent(String plotName)
 	 {
 	 	return super.isPlotPermanent(plotName);
 	 }
  
  /**
 	* returns the soil taxon for the plot -- this is the USDA class
 	* heirarchy (eg., Order, Group, Family, Series etc..)
 	* @param plotName -- the plot
 	*/
 	public String getSoilTaxon(String plotName)
 	{
 		return("");
 	}
 	
 	/**
 	 * returns how the soil taxon was determined (eg., field observation
 	 * mapping, other ...)
 	 * @param plotName -- the plot
 	 */
 	public String getSoilTaxonSource(String plotName)
 	{
 		return( super.getSoilTaxonSource(plotName) );
 	}


	/**
	 * this method returns the plotid's of all the plots stored 
	 * in the VegBank MDB file.  The name of this class seems
	 * strange because one would expect that the name would be 
	 * the name of the plot that the author used to describe the 
	 * plot, but this gives the 'unique' names from the perspective 
	 * of the database
	 */
	public Vector getPlotNames()
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct PLOT_ID from PLOT");
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	//returns the project name 
	public String getProjectName(String plotName)
	{ 
		return super.getProjectName(plotName); 
	}
	
	//returns the project description
	public Vector getProjectContributors(String plotName)
	{
		return super.getProjectContributors(plotName);
	}
	
	//retuns the person's salutation based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSalutation(String contributorWholeName)
	{
		return super.getProjectContributorSalutation(contributorWholeName);
	}
	//retuns the person's given based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorGivenName(String contributorWholeName)
	{
		return super.getProjectContributorGivenName(contributorWholeName);
	}
	//retuns the person's middle name based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorMiddleName(String contributorWholeName)
	{
		return super.getProjectContributorMiddleName(contributorWholeName);
	}
	//retuns the person's surName based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorSurName(String contributorWholeName)
	{
		return super.getProjectContributorSurName(contributorWholeName);
	}
	//retuns the name of an org. that a person is associated with based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorOrganizationName(String contributorWholeName)
	{
		return super.getProjectContributorOrganizationName(contributorWholeName);
	}
	
	//retuns the person's contactinstructions based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorContactInstructions(String contributorWholeName)
	{
		return super.getProjectContributorContactInstructions(contributorWholeName);
	}
	//retuns the person's phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorPhoneNumber(String contributorWholeName)
	{
		return super.getProjectContributorPhoneNumber(contributorWholeName);
	}
	//retuns the person's cellPhone based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCellPhoneNumber(String contributorWholeName)
	{
		return super.getProjectContributorCellPhoneNumber(contributorWholeName);
	}
	//retuns the person's fax phone number based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorFaxPhoneNumber(String contributorWholeName)
	{
		return super.getProjectContributorFaxPhoneNumber(contributorWholeName);
	}
	//retuns the party's position within an organization based on their full 
	// name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorOrgPosition(String contributorWholeName)
	{
		return super.getProjectContributorOrgPosition(contributorWholeName);
	}
	//retuns the person's email based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorEmailAddress(String contributorWholeName)
	{
		return super.getProjectContributorEmailAddress(contributorWholeName);
	}
	//retuns the person's address line based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorDeliveryPoint(String contributorWholeName)
	{
		return super.getProjectContributorDeliveryPoint(contributorWholeName);
	}
	//retuns the person's city based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCity(String contributorWholeName)
	{
		return super.getProjectContributorCity(contributorWholeName);
	}
	
	//returns the administrative area, or state that a party is from
	public String getProjectContributorAdministrativeArea(String contributorWholeName)
	{
		return super.getProjectContributorAdministrativeArea(contributorWholeName);
	}
	//retuns the zip code for a party
	public String getProjectContributorPostalCode(String contributorWholeName)
	{
		return super.getProjectContributorPostalCode(contributorWholeName);
	}
	//retuns the person's country based on their full name which is the
	//concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCountry(String contributorWholeName)
	{
			return super.getProjectContributorCountry(contributorWholeName);
	}
	//retuns a boolean 'true' if it is a party's current address based on their 
	//full name which is the concatenated givename and surname of the user like 'bob peet'
	public String getProjectContributorCurrentFlag(String contributorWholeName)
	{
		return super.getProjectContributorCurrentFlag(contributorWholeName);
	}
	//retuns the date that the address became current for a party based on 
	//their full name which is the concatenated givename and surname of the 
	//user like 'bob peet'
	public String getProjectContributorAddressStartDate(String contributorWholeName)
	{
		return super.getProjectContributorAddressStartDate(contributorWholeName);
	}
	
	//returns the start date for the project
	public String getProjectStartDate(String plotName )
	{
		return super.getProjectStartDate(plotName);
	}
	
	//returns the stop date for the project
	public String getProjectStopDate(String plotName )
	{
			return super.getProjectStopDate(plotName);
	}
	
	//returns the placeNames each name can be used to retrieve
	//other information about a place
	public Vector getPlaceNames(String plotName)
	{
		return super.getPlaceNames(plotName);
	}
	
	//retuns a description of a place based on a placeName
	public String getPlaceDescription(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("marina park by the sunken ship imitation");
		else if (placeName.startsWith("Ventura"))
			return("the ventura harbor on the north side");
		else return("");
	}
	//returns a placeCode based on a placeName
	public String getPlaceCode(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("MarinePrk");
		else if (placeName.startsWith("Ventura"))
			return("VenHbr");
		else return("");
	}
	//returns a place system based on a placeName
	public String getPlaceSystem(String placeName)
	{
		return("unknown system");
	}
	//returns the owner of a place based on the name of a place
	public String getPlaceOwner(String placeName)
	{
		if ( placeName.startsWith("Marina") )
			return("Ventura county");
		else if (placeName.startsWith("Ventura"))
			return("State of California");
		else return("");
	}
	
	
	//returns the project description
	public String getProjectDescription(String plotName )
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  projectdescription  "
			+" from PROJECT where PROJECT_ID = 0 "  );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
	//returns the plot code for the current plot
	public String getPlotCode(String plotName)
	{ 
		return super.getPlotCode(plotName);
	}
	
	//returns the easting 
	public String getXCoord(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authore "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the northing
	public String getYCoord(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authorn "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return latitude -- the validated latitude of that plot
	 */
	public String getLatitude(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  LATITUDE "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
	return(s);
	}
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return longitude -- the validated latitude of that plot
	 */
	public String getLongitude(String plotName)
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  LONGITUDE "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	
	//returns the geographic zone
	public String getUTMZone(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  authorzone "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	public String getDatumType(String plotName)
	{
		return(super.getDatumType(plotName) );
	}
	
	//returns the plot shape
	public String getPlotShape(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  shape "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the plot area -n m^2
	public String getPlotArea(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  area "
			+" from PLOT where PLOT_ID = " + plotName );
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the state for the current plot
	public String getState(String plotName)
	{ 
		String s = "NC";
		return(s);
	}
	
	//returns the state for the current plot
	public String getCommunityName(String plotName)
	{ return("ice-plant community"); }
	
	
	/**
	 * returns the community code for the named plot
	 */
	public String getCommunityCode(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 * returns the community level of the framework for the named plot
	 */
	public String getCommunityLevel(String plotName)
	{
		String s = "";
		return(s);
	}
	
	/**
	 * returns the community framework for the named plot
	 */
	public String getCommunityFramework(String plotName)
	{
		String s = "";
		return(s);
	}
	
	
	//retuns the topo position
	public String getTopoPosition(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  topoposition "
			+" from PLOT where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	//returns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{ 
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select  hydrologicregime "
			+" from observation where  PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				 s = rs.getString(1);
			}
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
		// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}
	
	/**
	 * method that retuns the latitude for an input plotid
	 * @param plotName -- the VegBank PLOTID
	 * @return geology -- the validated surface geology of that plot
	 */
	public String getSurfGeo(String plotName)
	{
		return(super.getSurfGeo(plotName) );
	}
	
	//returns the country
	public String getCountry(String plotName)
	{ 
		return super.getCountry(plotName); 
	}
	
	//returns the slope aspect
	public String getSlopeAspect(String plotName)
	{
		return super.getSlopeAspect(plotName);
	}
	
	//returns the slope gradient
	public String getSlopeGradient(String plotName)
	{
		return super.getSlopeGradient(plotName);
	}
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		return("extensive");
	}
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		return super.getAuthorLocation(plotName);
	}
	//returns the landForm
	public String getLandForm(String plotName)
	{
		return("aluvial fan");
	}
	//retuns the elevation
	public String getElevation(String plotName)
	{
		return super.getElevation(plotName);
	}
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		return super.getElevationAccuracy(plotName);
	}
	
	//return the confidentiality reason -- not null
	public String	getConfidentialityReason(String plotName)
	{
		return super.getConfidentialityReason(plotName);
	}
	
	//return the confidentiality status -- not null 
	public String getConfidentialityStatus(String plotName)
	{
		return super.getConfidentialityStatus(plotName);
	}
	
	/**
	 * method that retuns the unique names for a plotID
	 * @param plotName -- the VegBank PLOTID
	 * @return v -- a vector containg the unique strata for that plot
	 *
	 */
	public Vector getUniqueStrataNames(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			Vector observationIDs = this.getObservationIDs( plotName );
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct STRATUMNAME "
			+" from STRATUMTYPE " );
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	
	/**
	 * method that returns the observation ids that are children
	 * of a plot id
	 * @param plotName -- the VegBank plotId
	 * @return v -- a vector containg all the observtaions for that specific
	 *		plot id provided as input
	 */
	 private  Vector getObservationIDs(String plotName)
	{
		Vector v = new Vector();
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select OBSERVATION_ID from OBSERVATION "
			+"where PLOT_ID = " + plotName );
							
			while (rs.next()) 
			{
				String s = rs.getString(1);
				v.addElement(s);
			}
			
		}
		catch (SQLException ex) 
		{
			this.handleSQLException( ex );
		}
		catch (java.lang.Exception ex) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(v);
	}
	
	
	
	/**
	 * method that takes a plot code and populates the public varaibles with
	 * data related to that specific plot
	 *
	 * @param plotName -- the name of a plot for which to retrieve data for
	 */
	 public void getPlotData(String plotName)
	 {
		 //there is only one plot here  so this method should be blank
	 }
	 
	
		
		/**
		 * method that returns a vector with the unique plant 
		 * taxa names for a given plot -- this method is defined
		 * in the plugin interface
		 */
		 public Vector getPlantTaxaNames(String plotName)
		 {
				Vector v = new Vector();
				Statement stmt = null;
				try 
				{
					Vector observationIDs = this.getObservationIDs(plotName);
					stmt = con.createStatement();
					StringBuffer sb = new StringBuffer();
					sb.append("select plantname from plantname where plantname_id in ( ");
					sb.append("select distinct plantname_id from TAXONOBSERVATION where OBSERVATION_ID = ");
					sb.append((String)observationIDs.elementAt(0)+" )" );
					System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
					ResultSet rs = stmt.executeQuery( sb.toString() );	
					while (rs.next()) 
					{
						String s = rs.getString(1);
						v.addElement(s);
					}
				}
				catch (java.lang.Exception ex) 
				{   
					System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
					ex.printStackTrace();
				}
				return(v);
		 }
		 


	/**
	 * method that returns the strata in which the input plant
	 * exists within the input plot
	 * @param plantName -- the name of the plant
	 * @param plotName -- the name of the plot
	 * @return v -- a vector containing the names of the strata for
	 * 	which this plant was collected
	 */
	 public Vector getTaxaStrataExistence(String plantName, String plotName)
	 {
		 Vector v = new Vector();
		 	String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMNAME from STRATUMTYPE WHERE STRATUMTYPE_ID in ( ");
				sb.append("select STRATUMTYPE_ID FROM STRATUM WHERE OBSERVATION_ID in ( ");
				sb.append("select observation_id from OBSERVATION where PLOT_ID = " + plotName+" ) and STRATUM_ID in ");
				sb.append(" ( select STRATUM_ID from stratumcomposition where TAXONOBSERVATION_ID in ( ");
				sb.append(" select TAXONOBSERVATION_ID from TAXONOBSERVATION where PLANTNAME_ID = ( ");
				sb.append(" select PLANTNAME_ID from PLANTNAME where PLANTNAME LIKE '"+plantName+"' ) ) ) ) ");
				System.out.println("VBAceessDataSource > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );
				
				int cnt = 0;
				while (rs.next()) 
				{
					cnt++;
					s = rs.getString(1);
					v.addElement(s);
				}
				System.out.println("VBAceessDataSource > result set size: " + cnt);
				System.out.println("VBAceessDataSource > result set contents: " + v.toString());
				rs.close();
				stmt.close();
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				ex.printStackTrace();
			}
			return(v);
	 }
	 
	 
	 /**
	 * method to return the cover for a given strata for a given 
	 * plot
	 */
	 public String getStrataCover(String plotName, String strataName)
	 {
		 String s = null;
		 Statement stmt = null;
		 try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMCOVER from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
	 }
	 
	 /** 
	  * method the returns the base height of a strata based on the 
		* name of that starta and the plot for which that strata is included
		*/
		public String getStrataBase(String plotName, String strataName)
		{
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMBASE from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				//System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
						// IF THERE IS A NULL PASS A NUMERIC VALUE, BC IT HAS TO BE ONE
				if ( s == null )
				{
						s = "0.0";
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
		}
		
		/**
		 * method that returns the upper height of a starata based on 
		 * the name of the strata and the plot inwhich the strata exists
		 */
		public String getStrataHeight(String plotName, String strataName)
		{
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				StringBuffer sb = new StringBuffer();
				sb.append("select STRATUMHEIGHT from STRATUM where STRATUMTYPE_ID in (");
				sb.append(" select DISTINCT STRATUMTYPE_ID from STRATUMTYPE where STRATUMNAME LIKE '"+strataName+"') and OBSERVATION_ID = (");
				sb.append(" select observation_id from OBSERVATION where PLOT_ID = " + plotName+" )");
				//System.out.println("VBAccessDataSourcePlugin > query: " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );			
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
				// IF THERE IS A NULL PASS A NUMERIC VALUE, BC IT HAS TO BE ONE
				if ( s == null )
				{
						s = "0.0";
				}
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				ex.printStackTrace();
			}
			return(s);
		}
		
	 
	 /**
	 * method that returns the strata cover for a given plant, strata, and 
	 * plot.  Notice that the inputs into this method include the plantName, plotName
	 * (the plotid in this case) and the stratum(name).  The MS Access version allows
	 * a stratum with the same name to be used multiple times in the database which 
	 * causes a problem with this method.  The current fix is to edit the mdb file 
	 * and increment any stratum whose name has already been used; there should be 
	 * a method that can fix this so that the user does not have to do it by hand.
	 */
	 public String getTaxaStrataCover(String plantName, String plotName, String stratum)
	 {
		 	StringBuffer sb = new StringBuffer();
			String s = null;
			Statement stmt = null;
			try 
			{
				stmt = con.createStatement();
				System.out.println("VBAccessDataSourcePlugin > querying cover for: " + plantName+" "+plotName+" "+stratum );
				
				sb.append("select TAXONSTRATUMCOVER from STRATUMCOMPOSITION where TAXONOBSERVATION_ID in ( ");
				sb.append(" select TAXONOBSERVATION_ID from TAXONOBSERVATION where PLANTNAME_ID = ( ");
				sb.append(" select PLANTNAME_ID from PLANTNAME where PLANTNAME LIKE '"+plantName+"' ) )");
				sb.append(" and ");
				sb.append(" STRATUM_ID in ( select STRATUM_ID from STRATUM where STRATUMTYPE_ID = ( ");
				sb.append(" select STRATUMTYPE_ID from STRATUMTYPE WHERE STRATUMNAME like '"+stratum+"') and OBSERVATION_ID = ");
				sb.append(" (select observation_id from OBSERVATION where PLOT_ID = " + plotName+" ) )");
				//System.out.println("VBAccessDataSourcePlugin > query " + sb.toString() );
				ResultSet rs = stmt.executeQuery( sb.toString() );
				while (rs.next()) 
				{
					s = rs.getString(1);
				}
				rs.close();
				stmt.close();
				System.out.println("VBAccessDataSourcePlugin > getTaxaStrataCover result: " + s);
			}
			catch (java.lang.Exception ex) 
			{   
				System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
				System.out.println("sql: " + sb.toString() );
				ex.printStackTrace();
			}
			return(s);
	 }
	 
	/**
	 * method that retuns the cummulative cover accoss all strata for a given 
	 * plant taxa in a given plot
	 */
	public String getCummulativeStrataCover( String plantName, String plotName )
	{
		String s = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select TAXONCOVER from TAXONOBSERVATION where OBSERVATION_ID = ");
			sb.append(" (select OBSERVATION_ID from OBSERVATION where PLOT_ID = "+plotName+" ) ");
			sb.append(" and PLANTNAME_ID = ");
			sb.append(" ( select PLANTNAME_ID from PLANTNAME where PLANTNAME like '"+plantName+"') ");
			
			ResultSet rs = stmt.executeQuery( sb.toString() );
			while (rs.next()) 
			{
				 s = rs.getString(1);
				 //if we get null returned then make the value = '999.99'
				 if ( s == null )
				 {
					 s = "999.99";
				 }
			}
			stmt.close();
			//rs.close();
		}
		catch (java.lang.Exception ex) 
		{
			System.out.println("VBAccessDataSourcePlugin > Exception: " + ex );
			ex.printStackTrace();
		}
		return(s);
	}

	
	
	/**
	 * method for handling the sql exceptions thrown 
	 */
	private void handleSQLException(SQLException ex)
	{
		try
		{
			while (ex != null)
			{
				System.out.println ("VBAccessDataSourcePlugin > ErrorCode: " + ex.getErrorCode () );
				System.out.println ("VBAccessDataSourcePlugin > SQLState:  " + ex.getSQLState () );
				System.out.println ("VBAccessDataSourcePlugin > Message:   " + ex.getMessage () );
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception x) 
		{   
			// All other types of exceptions
			System.out.println("VBAccessDataSourcePlugin > Exception: " + x + "<BR>");
			x.printStackTrace();
		}
	}
	
	
	
	
	
/**
 * main method for testing --
 */
	public static void main(String[] args)
	{
		if (args.length == 1) 
		{
			String plotName = args[0];
		//	VBAccessDataSourcePlugin db = new VBAccessDataSourcePlugin("msaccess");
		//	System.out.println(" VBAccessDataSourcePlugin > getting info for: " + plotName );
		}
		else
		{
		//	VBAccessDataSourcePlugin db = new VBAccessDataSourcePlugin("msaccess");
		//	System.out.println( db.getPlotCodes().toString() );
		//	System.out.println( db.getPlotIDs().toString() );
		}
	}
	
	
}
