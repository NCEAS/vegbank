/**
 * This class acts as a store for sql queries which are used 
 * depending on the number and type of query elements passed
 * to the sqlMapper class.  Ultimately these queries are issued 
 * to the database though the issue select method
 *
 * @author 
 * @version 
 *
 *
 *
 *  '$Author: farrell $'
 *  '$Date: 2003-05-07 01:41:35 $'
 *  '$Revision: 1.4 $'
 *
 *
 */

package databaseAccess;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class queryStore
{
	public String summaryOutput[] = new String[10000]; 
	public int summaryOutputNum; 
	//hash table to store the cummulative summary results for all the plots
	public Hashtable cumulativeSummaryResultHash = new Hashtable();
	public String entireSinglePlotOutput[] = new String[10000];  //should always = 1
	public int entireSinglePlotOutputNum; 
	public String outPlotId[] = new String[30000];  //the output plotIds
	public int outPlotIdNum; //the number of plotId's




/**
 * Method using as input a single plot database id number 
 * to retieve all the attributes for that plot all fields 
 * are made publicly accessible and will, ultimately be 
 * passed to the xml writer class for file output
 *
 * @param plotId - database plot id number
 */

 public void getEntireSinglePlot(String plotId)
 {
	//because the 'getPlotSummary' method has become
	//pretty comprehensive just use that for now making
	// the method pretty much an overloaded method
	if (plotId != null)
	{
		System.out.println(  "getEntireSinglePlot accessed ");
		//soon make this method accept a vector
		Vector plotIdVec = new Vector();
		plotIdVec.addElement(plotId);
		
		String plotIdArray[] = new String[plotIdVec.size()];
		for (int i=0; i<plotIdVec.size(); i++) 
		{
			plotIdArray[i]=plotIdVec.elementAt(i).toString().trim();	
		}
		
		//get the summary info
		getPlotSummaryNew(plotIdArray,  plotIdVec.size() );
	}
	else
	{
		System.out.println("null value found where there should have been a plotId");
	}
 }


 
 /**
  * method to overload the method below allowing for an
	* input of a vector
	*/
	public void getPlotSummaryNew(Vector plotIdVec)
	{
		//make the array the same size as the 
		//vector to store the vector
		String plotId[] = new String[plotIdVec.size()];
		for (int i=0; i<plotIdVec.size(); i++) 
		{
			plotId[i]=plotIdVec.elementAt(i).toString().trim();	
		}
		//pass this array onto the 
		//overloaded method
		getPlotSummaryNew(plotId,  plotIdVec.size() );
	}





/**
 * Method to query the database by using a plot id number to obtain a summary of 
 * that plot including 1] author plot code, 2] surfaceGeology
 * The input to the method is a string array containing plot id numbers and
 * an integer representing the number of elements in the array
 *
 * @param plotId - an array of plotId numbers
 * @param plotIdNum - the number of elements in the above array
 */
	public void getPlotSummaryNew(String plotId[], int plotIdNum)
	{

		//get the database management parameter settings
		////g.getDatabaseParameters("database", "query");
		java.util.Date startDate = new java.util.Date();
		int startSeconds = startDate.getSeconds();
		System.out.println("queryStore > StartQuery: "+startDate );
		try 
		{

			//an array to store the summary results
			String summaryResult[] = new String[30000];
			int summaryResultNum=0;

			//iterate through the plot id numbers
			for (int i=0;i<plotIdNum; i++) 
			{
				//hash table to store the results of each individual plot
				Hashtable summaryResultHash = new Hashtable();

				//Because often the output of issueStatement is used as input here,
				//and the output values are tokenized by pipes replace all the pipes below
				String currentPlotId=plotId[i].replace('|',' ').trim();

				//this code should be replaced by a method that checks to see that the plotId is
				//a valid one

				if (currentPlotId.equals("nullValue") || currentPlotId == null ) 
				{
					currentPlotId="0";  //change this to a more resonable value
				}


				String action="select";
				String statement="select PLOT_ID, PROJECT_ID, PLOTTYPE, SAMPLINGMETHOD, "
					+" COVERSCALE, PLOTORIGINLAT,  PLOTORIGINLONG, PLOTSHAPE, PLOTAREA, "
					+" ALTVALUE, SLOPEASPECT, SLOPEGRADIENT, SLOPEPOSITION, HYDROLOGICREGIME, "
					+" SOILDRAINAGE, CURRENTCOMMUNITY, XCOORD, YCOORD, COORDTYPE, OBSSTARTDATE, "
					+" OBSSTOPDATE, EFFORTLEVEL, HARDCOPYLOCATION, SOILTYPE, SOILDEPTH, "
					+" PERCENTROCKGRAVEL, PERCENTSOIL, PERCENTLITTER, PERCENTWOOD, PERCENTWATER, "
					+" PERCENTSAND, PERCENTCLAY, PERCENTORGANIC, LEAFTYPE, PHYSIONOMICCLASS, "
					+" AUTHORPLOTCODE, SURFGEO, STATE, PARENTPLOT, AUTHOROBSCODE"
					+" from PLOTSITESUMMARY where PLOT_ID = "+currentPlotId;
		
				String returnFields[]=new String[40];
				int returnFieldLength=40;
	
				returnFields[0]="PLOT_ID";
				returnFields[1]="PROJECT_ID";
				returnFields[2]="PLOTTYPE";
				returnFields[3]="SAMPLINGMETHOD";
				returnFields[4]="COVERSCALE";
				returnFields[5]="PLOTORIGINLAT";
				returnFields[6]="PLOTORIGINLONG";
				returnFields[7]="PLOTSHAPE";
				returnFields[8]="PLOTAREA";
				returnFields[9]="ALTVALUE";
				returnFields[10]="SLOPEASPECT";
				returnFields[11]="SLOPEGRADIENT";
				returnFields[12]="SLOPEPOSITION";
				returnFields[13]="HYDROLOGICREGIME";
				returnFields[14]="SOILDRAINAGE";
				returnFields[15]="CURRENTCOMMUNITY";
				returnFields[16]="XCOORD";
				returnFields[17]="YCOORD";
				returnFields[18]="COORDTYPE";
				returnFields[19]="OBSSTARTDATE";
				returnFields[20]="OBSSTOPDATE";
				returnFields[21]="EFFORTLEVEL";
				returnFields[22]="HARDCOPYLOCATION";
				returnFields[23]="SOILTYPE";
				returnFields[24]="SOILDEPTH";
				returnFields[25]="PERCENTROCKGRAVEL";
				returnFields[26]="PERCENTSOIL";
				returnFields[27]="PERCENTLITTER";
				returnFields[28]="PERCENTWOOD";
				returnFields[29]="PERCENTWATER";
				returnFields[30]="PERCENTSAND";
				returnFields[31]="PERCENTCLAY";
				returnFields[32]="PERCENTORGANIC";
				returnFields[33]="LEAFTYPE";
				returnFields[34]="PHYSIONOMICCLASS";
				returnFields[35]="AUTHORPLOTCODE";
				returnFields[36]="SURFGEO";
				returnFields[37]="STATE";
				returnFields[38]="PARENTPLOT";
				returnFields[39]="AUTHOROBSCODE";

				//execute the selection request
				issueStatement j = new issueStatement();
				j.issueSelect(statement, returnFields, summaryResultHash);	
			
				
	
			//take the results from the issueSelect and put into the array
			//note that the j.outReturnFieldsNum should always be = 1 in this case
			//because we are querying by a plot ID number which should be unique
			for (int ii=0;ii<j.outReturnFieldsNum; ii++) 
			{
				summaryResult[i]=j.outReturnFields[ii];
			}
	
	
			// make a second query here to get the species specific information 
			action="select";
			statement="select AUTHORNAMEID, AUTHORPLOTCODE, STRATUMTYPE, PERCENTCOVER" 
				+" from PLOTSPECIESSUM where PLOT_ID = "+currentPlotId;

			int returnSpeciesFieldLength=4;
			String returnSpeciesFields[]=new String[4];	
	
			returnSpeciesFields[0]="AUTHORNAMEID";
			returnSpeciesFields[1]="AUTHORPLOTCODE";
			returnSpeciesFields[2]="STRATUMTYPE";
			returnSpeciesFields[3]="PERCENTCOVER";
	
			//issue the select statement
			issueStatement k = new issueStatement();
			k.issueSelect(statement, returnSpeciesFields, summaryResultHash);
	

	
			// add the individual plots (represented as their own hash) and include in the
			// cumulative hash table
			cumulativeSummaryResultHash.put("plot"+i,k.outResultHash);
			summaryResultNum=i;
			
			//System.out.println("printing the selection: "+ cumulativeSummaryResultHash.toString() );
			
		} //end for
		summaryOutput=summaryResult;
		summaryOutputNum=summaryResultNum;

		//print the end date
		java.util.Date stopDate = new java.util.Date();
		int stopSeconds = stopDate.getSeconds();
		System.out.println("queryStore > Stop Query: "+stopDate );
		System.out.println("queryStore > elapsed time (secs) : "+ (stopSeconds - startSeconds) );
		
	}
	catch (Exception e) 
	{
		System.out.println("failed in querySrore.getPlotSummaryNew"
		+" " + e.getMessage()); e.printStackTrace();
	}	
}//end method



/**
 * Method to query the database by using a plot id number to obtain a summary of 
 * that plot including 1] author plot code, 2] surfaceGeology
 * The input to the method is a string array containing plot id numbers and
 * an integer representing the number of elements in the array
 *
 * @param plotId - an array of plotId numbers
 * @param plotIdNum - the number of elements in the above array
 * @param conn - a database connection -- get rid of this and put the conn
 * management here in this class
 * @deprecated
 */
public void getPlotSummary(String plotId[], int plotIdNum)
{
	try 
	{
		//this array will hold all the results before passing it back to the 
		//calling class
		String summaryResult[] = new String[10000];
		int summaryResultNum=0;

		//iterate through the plot id numbers
		for (int i=0;i<plotIdNum; i++) 
		{

			//Because often the output of issueStatement is used as input here,
			//and the output values are tokenized by pipes replace all the pipes below
			String currentPlotId=plotId[i].replace('|',' ').trim();

			String action="select";
			String statement="select PLOT_ID, AUTHORPLOTCODE, project_id, "+
			"surfGeo,  PLOTTYPE, PLOTORIGINLAT, PLOTORIGINLONG, PLOTSHAPE, "
			+" PLOTSIZE, PLOTSIZEACC, ALTVALUE, ALTPOSACC, SLOPEASPECT, SLOPEGRADIENT, "
			+" SLOPEPOSITION, HYDROLOGICREGIME, SOILDRAINAGE, STATE, CURRENTCOMMUNITY "
			+" from PLOT where PLOT_ID = "+currentPlotId;
			String returnFields[]=new String[19];	
			returnFields[0]="PLOT_ID";	
			returnFields[1]="AUTHORPLOTCODE";
			returnFields[2]="project_id";
			returnFields[3]="surfGeo";
			returnFields[4]="PLOTTYPE";
			returnFields[5]="PLOTORIGINLAT";
			returnFields[6]="PLOTORIGINLONG";
			returnFields[7]="PLOTSHAPE";
			returnFields[8]="PLOTSIZE";
			returnFields[9]="PLOTSIZEACC";
			returnFields[10]="ALTVALUE";
			returnFields[11]="ALTPOSACC";
			returnFields[12]="SLOPEASPECT";
			returnFields[13]="SLOPEGRADIENT";
			returnFields[14]="SLOPEPOSITION";
			returnFields[15]="HYDROLOGICREGIME";
			returnFields[16]="SOILDRAINAGE";
			returnFields[17]="STATE";
			returnFields[18]="CURRENTCOMMUNITY";
			int returnFieldLength=19;
			issueStatement j = new issueStatement();
			j.issueSelect(statement, returnFields, returnFieldLength);	
			
			for (int ii=0;ii<j.outReturnFieldsNum; ii++) 
			{
				summaryResult[i]=j.outReturnFields[ii];
			}
			// make a second query here to get the species specific information which will
			// be appended onto the summary results line and ultimately tokenized in the 
			// xmlWrter class - at some point this function may become its own method

			action="select";
			statement="select AUTHORNAMEID from taxonObservation where OBS_ID in"+
			"(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in"+
			"(select plot_ID from plot where PLOT_ID ="+currentPlotId+"))";
			String returnFieldsB[]=new String[1];	
			returnFieldsB[0]="AUTHORNAMEID";
			int returnFieldLengthB=1;
	
			//issue the select statement
			issueStatement k = new issueStatement();
			k.issueSelect(statement, returnFieldsB, returnFieldLengthB);	
	
			//take the results from this query and append to the summary line
			//which will ultimately be passed back to the xmlWriter to be tokenized
			//and writen to xml - againd there should only be one line returned 
			for (int ii=0;ii<k.outReturnFieldsNum; ii++) 
			{
				summaryResult[i]=summaryResult[i]+k.outReturnFields[ii];
			}
			summaryResultNum=i;
		}
		summaryOutput=summaryResult;
		summaryOutputNum=summaryResultNum;
	} //end try
	catch (Exception e) 
	{
		System.out.println("failed in queryStore.getPlotSummary"+" " + e.getMessage());
	}		
}




/**
 * Method to query the database to get all the plotId's by using a single 
 * attribute including taxonName, surfGeo, communityName, etc.  This method is
 * linked to the 'handleSimpleQuery' method in the DataRequestServlet
 * 
 * @param     queryElement  the value of the attribute used to query
 * @param     queryElementType  the type of element used for querying the DB
 *  types may include taxonname, state, surfgeo, currentcommunity, plotid
 */
public void getPlotId(String queryElement, String queryElementType)
{
	String queryTableName = null;  //name of the table for which the query to be made
	//translate the query attributeName into the table name and determine the table
	if ( queryElementType.equalsIgnoreCase("taxonName") ) 
	{
		queryElementType = "AUTHORNAMEID";
		queryTableName = "PLOTSPECIESSUM";
	}

	else if ( queryElementType.equalsIgnoreCase("state") ) 
	{
		queryElementType = "STATE";
		queryTableName = "PLOTSITESUMMARY";
	}

	else if ( queryElementType.equalsIgnoreCase("surfGeo") ) 
	{
		queryElementType = "SURFGEO";
		queryTableName = "PLOTSITESUMMARY";
	}

	else if ( queryElementType.equalsIgnoreCase("communityName") ) 
	{
		queryElementType = "CURRENTCOMMUNITY";
		queryTableName = "PLOTSITESUMMARY";
	}
	// PLOTID ( THE PK VALUE )
	else if (queryElementType.equalsIgnoreCase("plotid"))
	{
		queryElementType = "PLOTID";
		queryTableName = "PLOTSITESUMMARY";
	}
	
	// ELSE DONT RECOGNIZE THE ELEMENT
	else 
	{	
		System.out.println("plotQuery.getPlotId: unrecognized queryElementType: "
		+queryElementType); 
	}

	// this select statement will use the summary tables for the plots database where
	// most of the database attributes are denormalized to two tables:
	// 'plotSiteSummary' and 'plotSpeciesSum'
	String action="select";
	StringBuffer sb = new StringBuffer();
	// HANDLE THE PLOTID'S A LITTLE DIFFERENTLY BECAUSE THEY ARE INTEGERS
	if (queryElementType.equals("PLOTID") )
	{
		sb.append("select DISTINCT PLOT_ID from "+queryTableName+" where ");
		sb.append("PLOT_ID = " + queryElement);
	}
	// ALSO HANDLE THE PLANT TAXA QUERY DIFFERENTLY -- THERE MAY BE MULTIPLE 
	else if ( queryElementType.equalsIgnoreCase("AUTHORNAMEID") )
	{
		String taxonName = queryElement;
		//sb.append("select  distinct(PLOTSITESUMMARY.PLOT_ID)  in ");
			// DEPENDING ON THE NUMBER OF TAXA COMPOSE A DIFFERENT SUB-QUERY
			if ( taxonName.indexOf(",") >0 )
			{
				StringTokenizer st = new StringTokenizer(taxonName, ",");
				int num = st.countTokens();
				sb.append(" (SELECT DISTINCT PLOT_ID from PLOTSPECIESSUM where  PLOT_ID in ( ");
				for (int i=0;i<num; i++) 
				{
					String buf = st.nextToken().trim();
					if ( i == (num-1) )
					{
						sb.append(" select distinct( PLOT_ID ) from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+buf.toUpperCase()+"%' ) )");
					}
					else
					{
						sb.append(" select PLOT_ID from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+buf.toUpperCase()+"%' ");
						sb.append(" intersect ");
					}
				}
			}
			// ELSE IF JUST A SINGLE PLANT PASSED
			else
			{
				sb.append(" select distinct( PLOT_ID ) from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+queryElement.toUpperCase()+"%'");
			}
	}
	// ALSO HANDLE THE COMMUNITIES A LITTLE DIFFERENTLY BECAUSE THEY CAN 
	// BE EITHER A CODE OR A NAME
	else if ( queryElementType.equalsIgnoreCase("CURRENTCOMMUNITY") )
	{
		sb.append("select DISTINCT PLOT_ID from  PLOTSITESUMMARY  where  PLOT_ID in ");
		sb.append( "(select PLOT_ID from  PLOTSITESUMMARY where upper(CURRENTCOMMUNITY) like '%");
		sb.append(queryElement.toUpperCase()+"%'  ");
		sb.append(" union ");
		sb.append( " select PLOT_ID from  PLOTSITESUMMARY where upper(CURRENTCOMMUNITYCODE) like '%");
		sb.append(queryElement.toUpperCase()+"%' ) ");
	}
	else
	{
		sb.append("select DISTINCT PLOT_ID from "+queryTableName+" where ");
		sb.append(" UPPER("+queryElementType +") like '%"+queryElement.toUpperCase()+"%'" );
	}
	System.out.println("queryStore > select statement: " + sb.toString() );
	String returnFields[]=new String[1];	
	returnFields[0]="PLOT_ID";
	int returnFieldLength=1;
	// ISSUE THE STATEMENT TO THE DATABASE
	issueStatement j = new issueStatement();
	j.issueSelect(sb.toString(), returnFields, returnFieldLength);	

	//grab the returned result set and transfer to a public array
	//ultimately these results are passed to the calling class -- if for some reason
	//the value was null then a string: 'nullValue' is returned
	outPlotId=j.outReturnFields;
	outPlotIdNum=j.outReturnFieldsNum;
	System.out.println("queryStore > result set size: " + outPlotIdNum );
}




/**
 * Method to query the database to get all the plotId's by using a two attributes
 * currently only a min/max elevation will work - soon to implement a community name -
 * this method will be overloaded in methods below
 * @param     queryElement  the value of the attribute used to query
 * @param     queryElement2  the value of the attribute used to query
 * @param     queryElementType  the type of element used for querying the DB
 * @param     conn  a database connection that was presumedly taken from the pool 
 */
	public void getPlotId(String queryElement, String queryElement2, 
	String queryElementType)
	{
		try
		{
			String action="select";
			String statement="select PLOT_ID from PLOT where ELEVATION >= "+queryElement+
			" and ELEVATION <= "+queryElement2;
			String returnFields[]=new String[1];	
			returnFields[0]="PLOT_ID";
			int returnFieldLength=1;
			
			System.out.println("queryStore > " + statement );

			issueStatement j = new issueStatement();
			j.issueSelect(statement, returnFields, returnFieldLength);	


			//grab the returned result set and transfer to a public array
			//ultimately these results are passed to the calling class
			outPlotId=j.outReturnFields;
			outPlotIdNum=j.outReturnFieldsNum;
		}
		catch (Exception e)
		{
			System.out.println("queryStore > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}


/**
 * Method to query the database to get all the plotId's using as input the 
 * following query elements this method is intended for the 'multiple element
 * queries' 
 * 
 * @param taxonName
 * @param state
 * @param elevationMin
 * @param elevationMax
 * @param surfGeo
 * @param multipleObs
 * @param community
 *
 */
	public void getPlotId(String taxonName, String state, String elevationMin, 
	String elevationMax, String surfGeo, String community)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			String action="select";
			System.out.println("queryStore >  taxonName: "+ taxonName);
			System.out.println("queryStore >  state: "+ state );
			System.out.println("queryStore >  elevationMin: "+ elevationMin );
			System.out.println("queryStore >  elevationMax: "+ elevationMax );
			System.out.println("queryStore >  surfGeo: "+ surfGeo );
			System.out.println("queryStore >  community: "+ community);
			// CREATE THE QUERY
			sb.append(" select DISTINCT PLOTSITESUMMARY.PLOT_ID from PLOTSITESUMMARY ");
			sb.append(" WHERE ");
			sb.append(" ALTVALUE <="+elevationMax );
			sb.append(" and ");
			sb.append(" ALTVALUE >="+elevationMin );
			sb.append(" and ");
			sb.append(" SURFGEO like '%" + surfGeo + "%' ");
			sb.append(" and ");
			sb.append(" STATE like '%"+state+"%' ");
			sb.append(" and ");
			sb.append(" PLOTSITESUMMARY.PLOT_ID in ");
			// DEPENDING ON THE NUMBER OF TAXA COMPOSE A DIFFERENT SUB-QUERY
			if ( taxonName.indexOf(",") >0 )
			{
				StringTokenizer st = new StringTokenizer(taxonName, ",");
				int num = st.countTokens();
				sb.append(" (SELECT DISTINCT PLOT_ID from PLOTSPECIESSUM where  PLOT_ID in ( ");
				for (int i=0;i<num; i++) 
				{
					String buf = st.nextToken().trim();
					if ( i == (num-1) )
					{
						sb.append(" select PLOT_ID from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+buf.toUpperCase()+"%' ) )");
					}
					else
					{
						sb.append(" select PLOT_ID from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+buf.toUpperCase()+"%' ");
						sb.append(" intersect ");
					}
				}
				
			}
			else
			{
				sb.append(" (SELECT DISTINCT PLOT_ID from PLOTSPECIESSUM where upper(AUTHORNAMEID) like '%"+taxonName.toUpperCase()+"%') ");
			}
			String statement = sb.toString();

			String returnFields[]=new String[1];	
			returnFields[0]="PLOT_ID";
			int returnFieldLength=1;
		
			System.out.println("queryStore statement: > " + statement );
		
			issueStatement j = new issueStatement();
			j.issueSelect(statement, returnFields, returnFieldLength);	

			//grab the returned result set and transfer to a public array
			//ultimately these results are passed to the calling class
			outPlotId=j.outReturnFields;
			outPlotIdNum=j.outReturnFieldsNum;
			System.out.println("queryStore resultset size: > " + outPlotIdNum  );
			
		}
		catch(Exception e)
		{
			System.out.println("queryStore > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}


}
