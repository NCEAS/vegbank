/*
 * Created on Mar 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.vegbank.common.utility.CommandLineTools;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.cli.*;
import org.vegbank.common.model.Observation;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.plots.datasource.DBModelBeanReader;
/**
 * @author Gabriel
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadBeansToCache 
{

	public static void main(String[] args) 
	{
		LoadBeansToCache lbtc = new LoadBeansToCache();
		//create the command line parser
		CommandLineParser parser = new PosixParser();

		//		create the Options
		Options options = new Options();
		options.addOption( "a", "all", false, "Load every Observation in db in modelbean cache" );
		options.addOption( "l", "list", false, "Load the following list of AccessionCodes" );


		try 
		{
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
	
			// validate that block-size has been set
			if( line.hasOption( "all" ) || line.hasOption('a' ) )
			{
				// print the value of block-size
				System.out.println( "Got load all" );
				lbtc.LoadAllObservationsIntoCache();
			}
			else if ( line.hasOption("list") || line.hasOption('l') )
			{
				System.out.println( "Got load list" );
				System.out.println( "going to load" + line.getArgList() );
				List accessionCodes = line.getArgList();
				lbtc.FetchMBsByAC(accessionCodes);
			}
			else
			{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "LoadBeansToCache", options );
			}
		}
		catch( Exception exp ) {
			System.out.println( "Unexpected exception:" + exp.getMessage() );
			System.exit(0);
		}
		System.exit(0);
	}

	/**
	 * Get the AccessionCodes from all the Observations in the db, extract there
	 * the data into modelbeans and save into the cache.
	 */
	private void LoadAllObservationsIntoCache() throws Exception 
	{
		Vector accessionCodes = new Vector();
		DatabaseAccess da = new DatabaseAccess();
		String SQL = "SELECT " + Observation.ACCESSIONCODE + " FROM Observation";
		ResultSet rs = da.issueSelect(SQL);
		
		while (rs.next())
		{
			String accessionCode = rs.getString(1);
			if ( ! Utility.isStringNullOrEmpty(accessionCode) )
			{
				// Add to the list
				accessionCodes.add(accessionCode);
			}
		}
		
		// Now retrive all these AC's from db thus populating the cache
		this.FetchMBsByAC(accessionCodes);
	}

	/**
	 * @param accessionCodes
	 */
	private void FetchMBsByAC(Collection codes) throws Exception 
	{
		DBModelBeanReader dbreader = new DBModelBeanReader();
		
		System.out.println("Loading " + codes.size() + " ModelBeans to cache ... please wait");
		// Setup Progress status bar
		StatusBarUtil sb = new StatusBarUtil();
		sb.setupStatusBar(codes.size());
		
		Iterator codeIt = codes.iterator();
		while ( codeIt.hasNext() )
		{
			String accessionCode = (String) codeIt.next();
			//System.out.println("Bout to load " + accessionCode);
			dbreader.getVBModelBean(accessionCode);
			sb.updateStatusBar();
		}
		sb.completeStatusBar(codes.size());
	}
}
