package databaseAccess;

import java.io.IOException;
import java.io.*;
import java.util.*;

import databaseAccess.*;

//this is currently in the datatranslator package
//the package name should be changed
import PlotDataSource;

//this is currently in the datatranslator package but
//will be moved
import PlotXmlWriterV2;


 

/**
 * This class will take an xml file containg either query attributes
 * or plot data and depending on the document type, either
 * insert, update or query the database - see the diagram 
 * "plotAccessArchitecture.vsd" to better understand the 
 * relation of this class to database access module.
 *
 * 1] For database queries:
 * queryXML should be the xml document containing the query attributes
 * queryXSL is the XSLT sheet for transforming the queryXML into attributeType and 
 * attributeString action is, at this point, verify - to transform the document, 
 * and query which will formulate the query and pass it to the database
 * 
 * 2] for database insertion:
 * dataXML should be the xml document containing plot and/or plant
 * data.  dataXSL is the XSLT sheet for transforming the dataXML file into 
 * databaseAddress and dataValue and the current usable action is: verify
 * (same as above) and insert which will insert the plot data to the 
 * database
 *
 * 3] for database update:
 * at this point there is no update capabilities, although it will
 * allow the user to update the database, based on a plot xml 
 * document containing only partial data from a plot 
 *
 *  '$Author: harris $'
 *  '$Date: 2002-01-18 00:21:49 $'
 * 	'$Revision: 1.2 $'
 */


public class dbAccess 
{

//constructor -- define as static the LocalDbConnectionBroker so that methods
// called by this class can access the 'local' pool of database connections
static LocalDbConnectionBroker lb;


public String queryOutput[] = new String[10000];  //the output from query
public int queryOutputNum; //the number of output rows from the query

 /**
  * constructor method
	*/
	public dbAccess()
	{
		System.out.println("init: databaseAccess.dbAccess ");
	}

 /**
  * this method will take a plot id number used in vegbank and a
	* filename to write all the plot data associated with that plot 
	* into an xml document -- this method is much newer than the
	* databaseAccess method, and should be used when the explicit 
	* desire is to write a single plot to an xml doc
	*/
	public boolean writeSingleVegBankPlot(String plotId, String outFile)
	{
		try
		{
			//convert the input integer to a string so that 
			//it can be passed to the xml writer
//			String plot = ""+plotId;
			//this class allows access to the vegbank databases
			//so the plugin will always be the same 
			String pluginClass = "VegBankDataSourcePlugin";
			PlotXmlWriterV2 writer = new PlotXmlWriterV2(pluginClass);
			writer.writeSinglePlot(plotId, outFile);
			
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
			return(false);
		}
		return(true);
	}


/**
 *  Public interface for running the plotAccess module, this is how the
 * interfaces and servlets will load and query the database
 * input: xml file, may be datafile or query file, xsl sheet to
 * transform the document and action, either insert or query
 * @param inputXml - input xml file
 * @param inputXsl - input xsl transform sheet
 * @param action - database action
 *
 */
public void accessDatabase(String inputXml, String inputXSL, String action) 
{
	try 
	{

		//first initiate the local database pooling class so that connections may be
		// used by the classes that are going to be subsequently called
		lb.manageLocalDbConnectionBroker("initiate");

		//call the method to transform the data xml document and pass back a string writer  
		transformXML m = new transformXML();
		m.getTransformed(inputXml, inputXSL);
		//the stringwriter containg all the transformed data
		StringWriter transformedData=m.outTransformedData;  
		//pass to the utility class to convert the StringWriter to an array
		utility u =new utility();
		u.convertStringWriter(transformedData);
		String transformedString[]=u.outString; 
		int transformedStringNum=u.outStringNum;
		
		//query action
		if (action.equals("extendedQuery")) 
		{
			for (int ii=0; ii<transformedStringNum; ii++) 
			{
				System.out.println(transformedString[ii]);	
			}
			
			 //pass the array to the sql mapping class - single attribute query
			sqlMapper w =new sqlMapper();
			w.developExtendedPlotQuery(transformedString, transformedStringNum);
			//grab the results from the sqlMapper class
			queryOutput=w.queryOutput;
			queryOutputNum=w.queryOutputNum;
		}
		
		//query action
		else if (action.equals("query")) 
		{
			// pass the array to the sql mapping class - single attribute query
			sqlMapper w =new sqlMapper();
			w.developPlotQuery(transformedString, transformedStringNum);
			//grab the results from the sqlMapper class
			queryOutput=w.queryOutput;
			queryOutputNum=w.queryOutputNum;
		}
		//compound query action
		else if (action.equals("compoundQuery")) 
		{
			//pass the array to the sql mapping class - compound queries
			sqlMapper w =new sqlMapper();
			w.developCompoundPlotQuery(transformedString, transformedStringNum);
			//grab the results from the sqlMapper class
			queryOutput=w.queryOutput;
			queryOutputNum=w.queryOutputNum;
		}
		//insert action -- to insert a plot to the last database
		else if (action.equals("insert")) 
		{
			//pass the array to the plot writer to be inserted into the database
			plotWriter w =new plotWriter();
			w.insertPlot(transformedString, transformedStringNum);
		}
		//insertPlot action -- this is to insert a plot to the most recent DB
		else if (action.equals("insertPlot")) 	
		{
			//pass the array to the plot writer to be inserted into the database
			PlotDBWriter w =new PlotDBWriter();
			w.insertPlot(transformedString, transformedStringNum, "entirePlot");
		}
		//verify action
		else if (action.equals("verify")) 
		{
			for (int ii=0; ii<transformedStringNum; ii++) 
			{
				System.out.println(transformedString[ii]);	
			}
		}
		//simple community query action
		else if (action.equals("simpleCommunityQuery")) 
		{
			{
				sqlMapper w =new sqlMapper();
				w.developSimpleCommunityQuery(transformedString, 
				transformedStringNum);
				//grab the results from the sqlMapper class
				queryOutput=w.queryOutput;
				queryOutputNum=w.queryOutputNum;
			}
		}
		
		else if (action.equals("simplePlantTaxonomyQuery")) 
		{
				sqlMapper w =new sqlMapper();
				w.developSimplePlantTaxonomyQuery(transformedString, 
				transformedStringNum);
				//grab the results from the sqlMapper class
				queryOutput=w.queryOutput;
				queryOutputNum=w.queryOutputNum;
		}
		
		else 
		{
			System.out.println("dbAccess.accessDatabase: unrecognized action: "
			+action);
		}

		//shut down the connection pooling
		lb.manageLocalDbConnectionBroker("destroy");
	} //end try
	catch( Exception e ) 
	{
		System.out.println(" failed in: dbAccess.accessDatabase "
		+e.getMessage() );
		e.printStackTrace();
	}
}



/**
 * main method -- really for testing the db
 * access module
 *
 */
public static void main(String[] args) 
{
	if (args.length != 3) 
	{
		System.out.println("Usage: java dbAccess  [XML] [XSL] [action] \n"
			+"version: Feb 2001 \n \n"
			+"actions:  query, compoundQuery, extendedQuery, insert, insertPlot "
			+"verify, simpleCommunityQuery");
			System.out.println("descriptions of input parameters to come soon!");
			System.exit(0);
	}
	//input xml file for loading to the database
	String inputXml=args[0];
	String inputXSL=args[1];
	String action=args[2];

	//call the public method

	dbAccess g =new dbAccess();  
	g.accessDatabase(inputXml, inputXSL, action);
}



}


