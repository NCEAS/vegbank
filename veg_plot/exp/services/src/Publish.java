package vegbank.publish;

import electric.registry.Registry;
import electric.server.http.HTTP;

public class Publish
  {
  public static void main( String[] args )
    throws Exception
    {
    // start a web server on port 8004, accept messages via /glue
    HTTP.startup( "http://localhost:8004/vegbank" );

    // publish an instance of Exchange
    Registry.publish( "exchange", new Exchange() );
    }
  }