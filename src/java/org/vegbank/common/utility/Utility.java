package org.vegbank.common.utility;

/**
 * '$RCSfile: Utility.java,v $'
 * '$Author: farrell $'
 * '$Date: 2003-02-03 21:46:13 $'
 * '$Revision: 1.1 $'
 * 
 */ 


public class Utility
{
	
	
	public static String escapeCharacters(String s)
	{
		String origString = s;
		// List of characters to escape
		char[] specialChar = {'\'', '$', '^'};
		
		for (int i = 0; i < specialChar.length ; i++)
		{
			char currentChar = specialChar[i];
			//System.out.println("----->" + currentChar);
			
			if ( s.indexOf ( currentChar) != -1 ) 
			{
				StringBuffer hold = new StringBuffer();
				char c;
				for ( int ii = 0; ii < s.length(); ii++ ) 
				{
					if ( (c=s.charAt(ii)) == currentChar  )
						hold.append ("\\" + currentChar );
					else
						hold.append(c);
				}
				s = hold.toString();
			}
		}
		//System.out.println("---->" + origString + " VS. " + s );
		return s;
	}

}
