package servlet.util;
/**
 *        Name: HttpMessage.java
 *     Purpose: Used for Java applet/application communication
 *              with servlet. Based on code given in the book
 *              "Java Servlet Programming" by Hunter & crawford
 *   Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *     Authors: Dan Higgins modified for metacat by Chad Berkley
 *     Version: '$Id: HttpMessage.java,v 1.3 2002-04-11 23:53:25 harris Exp $'
 *
 *     '$Author: harris $'
 *     '$Date: 2002-04-11 23:53:25 $'
 *     '$Revision: 1.3 $'
 */


import java.io.*;
import java.net.*;
import java.util.*;

import servlet.util.*;


public class HttpMessage {
    public String contype;
    URL servlet = null;
    String argString = null;
    static String cookie = null;
    
		//constructor
		public HttpMessage(URL servlet) 
		{
			System.out.println("HttpMessage > constructed");
    	this.servlet = servlet;
    }
    
    /**
     * return the cookie that this message object contains
     */
    public String getCookie()
    {
      return cookie;
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
     * Performs a POST request to the previously given servlet
     * with no query string
     */
    public InputStream sendPostMessage() throws IOException {
        return sendPostMessage(null);
    }
    
    /**
     * Builds post data from the supplied properties list
     */
    public InputStream sendPostMessage(Properties args) throws IOException {
        argString = ""; //default
        if (args != null) {
            argString = toEncodedString(args); 
        }
        URLConnection con = servlet.openConnection();
        if (cookie!=null) {
            int k = cookie.indexOf(";");
            if (k>0) {
                cookie = cookie.substring(0, k);
            }
            //System.out.println("Cookie = " + cookie);
            con.setRequestProperty("Cookie", cookie); 
            con.setRequestProperty("User-Agent", "MORPHO");  // add 10/26/00 by DFH so Metacat can determine where request come from
        }
        //prepare for both input and output
        con.setDoInput(true);
        con.setDoOutput(true);
        //turn off caching
        con.setUseCaches(false);
        
        //work around a Netscape bug
        //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //Write the arguments as post data
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(argString);
        out.flush();
        contype = con.getContentType();
        String temp = con.getHeaderField("Set-Cookie");
        if (temp!=null) {
            cookie = temp;
        }
        //System.out.println(cookie);
        out.close();
        
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
    private String toEncodedString(Properties args) {
        StringBuffer buf = new StringBuffer();
        Enumeration names = args.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String)names.nextElement();
            String value = args.getProperty(name);
            buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
            if (names.hasMoreElements()) buf.append("&");
        }
        return buf.toString();
    }
}

