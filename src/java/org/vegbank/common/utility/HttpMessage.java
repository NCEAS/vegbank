package org.vegbank.common.utility;
/**
 *        Name: HttpMessage.java
 *     Purpose: Used for Java applet/application communication
 *              with servlet. Based on code given in the book
 *              "Java Servlet Programming" by Hunter & crawford
 *   Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *     Authors: Dan Higgins modified for metacat by Chad Berkley
 *     Version: '$Id: HttpMessage.java,v 1.3 2003-11-12 22:27:31 farrell Exp $'
 *
 *     '$Author: farrell $'
 *     '$Date: 2003-11-12 22:27:31 $'
 *     '$Revision: 1.3 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;


public class HttpMessage {
    public String contype;
    URL servlet = null;
    String argString = null;
    
		//constructor
		public HttpMessage(URL servlet) 
		{
			System.out.println("HttpMessage > constructed");
    	this.servlet = servlet;
    }
    
    /**
     * Performs a GET request to the previously given servlet
     * with no query string
    */
    public InputStream sendGetMessage() throws IOException {
        return sendGetMessage(null);
    }
    
    /**
     * Performs a GET request to the previously given servlet
     *Builds a query string from the supplied Properties list.
     */
    public InputStream sendGetMessage(Properties args) throws IOException {
        argString = ""; //default
        
        if (args != null) {
            argString = "?" + toEncodedString(args);
        }
        URL url = new URL(servlet.toExternalForm() + argString);
        
        // turn off caching
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        contype = con.getContentType();
        
        return con.getInputStream();
    }

    /**
     * Returns the urls argument strings
     */
    public String getArgString() {
        String argString1 = argString;
        if (!argString1.startsWith("?")) {
            argString1 = "?"+argString1;}
        return argString1;
    }
    
    /**
     * Converts a Properties list to a URL-encoded query string
     */
    private String toEncodedString(Properties args) 
    {
        StringBuffer buf = new StringBuffer();
        Enumeration names = args.propertyNames();
        while (names.hasMoreElements()) 
        {
            String name = (String)names.nextElement();
            String value = args.getProperty(name);
            try
			{
				buf.append(URLEncoder.encode(name, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				System.out.println("Unexpected error with encoding");
				e.printStackTrace();
			}
            if (names.hasMoreElements()) 
            {
            	buf.append("&");
            } 
        }
        return buf.toString();
    }
}

