import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataSourceServerInterface extends Remote 
{
   public byte[] downloadFile(String fileName) throws
   RemoteException;
	 
	//this method retrives all the attributes for a gine plotname
  void getPlotData(String plotName)
	throws Exception;
	
	//method that returns all the plot names, as a vector, stored in a given
	//source
	public Vector getPlotNames()
	throws Exception;
	
	//returns the project name 
	public String getProjectName(String plotName)
	throws Exception;
	
	//returns the project name 
	String getProjectDescription(String plotName)
	throws Exception;
	
	//returns the easting
	String getXCoord(String plotName)
	throws Exception;

	//returns the northing
	String getYCoord(String plotName)
	throws Exception;

	//returns the latitude
	String getLatitude(String plotName)
	throws Exception;

	//returns the longitude
	String getLongitude(String plotName)
	throws Exception;	
	
	//returns the geographic zone
	String getUTMZone(String plotName)
	throws Exception;

	//returns the plot shape
	String getPlotShape(String plotName)
	throws Exception;

	//returns the plot area
	String getPlotArea(String plotName)
	throws Exception;

	//returns the state for the current plot
	String getCommunityName(String plotName)
	throws Exception;

	//returns the state in which the plot exists
	String getState(String plotName)
	throws Exception;

	//retuns the hydrologic regime
	String getHydrologicRegime(String plotName)
	throws Exception;

	//returns the topo position
	String getTopoPosition(String plotName)
	throws Exception;

	//returns the slope aspect
	String getSlopeAspect(String plotName)
	throws Exception;

	//returns yje slope gradient
	String getSlopeGradient(String plotName)
	throws Exception;

	//returns the surficial geology
	String getSurfGeo(String plotName)
	throws Exception;

	//retuns the country
	String getCountry(String plotName)
	throws Exception;

	//returns the size of the stand -- extensive etc..
	String getStandSize(String plotName)
	throws Exception;

	//returns the location as described by the author
	String getAuthorLocation(String plotName)
	throws Exception;

	//returns the landForm
	String getLandForm(String plotName)
	throws Exception;

	//retuns the elevation
	String getElevation(String plotName)
	throws Exception;

	//returns the elevation accuracy
	String getElevationAccuracy(String plotName)
	throws Exception;

	//return the confidentiality reason -- not null
	String	getConfidentialityReason(String plotName)
	throws Exception;

	//return the confidentiality status -- not null 0-6
	String getConfidentialityStatus(String plotName)
	throws Exception;
	
}
