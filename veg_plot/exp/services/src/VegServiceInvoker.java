package vegbank.publish;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import electric.registry.Registry;
import electric.registry.RegistryException;
import examples.publish.IExchange;

public class VegServiceInvoker 
  {
  public static void main( String[] args )
    throws Exception
    {
		if (args.length < 1) 
		{
			System.out.println("USAGE: java VegServiceInvoker query-token(s) ");
		}
		else
		{
			// get all the query tokens
			String queryString = "";
			for (int i=0; i< args.length ; i++)
			{
				queryString = queryString.trim() + " " + (args[i].trim());
			}
			System.out.println(queryString);
			
    	// bind to web service whose WSDL is at the specified URL
    	String url = "http://localhost:8004/vegbank/exchange.wsdl";
    	ExchangeInterface exchange = (ExchangeInterface) Registry.bind( url, ExchangeInterface.class );

    	// invoke the web service as if it was a local java object
    	Vector v  = exchange.getPlotAccessionNumber(queryString);
   
	 		// print out the results
			for (int i=0; i< v.size() ; i++)
			{
				Hashtable h = (Hashtable)v.elementAt(i);
				System.out.println( h.toString()  );
			}
		}
    }
  }
