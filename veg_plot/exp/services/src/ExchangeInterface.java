package vegbank.publish;
import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;


public interface ExchangeInterface
  {
		/**
		 * method that takes a plant name and returns
		 * a summary of all the plots in vegbank that 
		 * contain that plant
		 *
		 * @param plantName -- the name of the plant 
		 * @return vector containing a hashtable -- with the following elements:
		 * 1] accessionNumber
		 * 2] latitude
		 * 3] longitude
		 * 4] state
		 */
		Vector getPlotAccessionNumber( String plantName );
  }