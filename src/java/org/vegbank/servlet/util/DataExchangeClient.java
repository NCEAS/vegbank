package org.vegbank.servlet.util;

/*
 *  '$RCSfile: DataExchangeClient.java,v $'
 *    Purpose: A test driver for the DataFileServer class
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *  Authors: @author@
 *  Release: @release@
 *	
 *
 * 	'$Author: farrell $'
 *  	'$Date: 2003-04-16 00:12:48 $'
 * 	'$Revision: 1.2 $'
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
 


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;




public class DataExchangeClient
{
 //constructor
 public DataExchangeClient()
  {
 
  }
  
  /**
   * sends a file to the data file server socket -- that should be opened
   */
  public static String SendFile(String filename, String host, int port, 
                                String cookie) 
  {
    Socket echoSocket = null;
    OutputStream out = null;
    InputStream in = null;
    
		String res = "return";
    
    System.out.println("DataExchangeClient.SendFile > host: " + host 
					+ "\n port: " + port 
					+ "\n filename: " + filename 
					+ "\n cookie: " + cookie);
					
    try 
    {
      //while(DataFileServer.portIsAvailable(port)) 
      //{//loop until the port is there
      //}
      echoSocket = new Socket(host, port);
      out = echoSocket.getOutputStream();
      in = echoSocket.getInputStream();
    } 
    catch (UnknownHostException e) 
    {
      System.err.println("DataExchangeClient > Don't know about host: " + host);
      System.out.println("DataExchangeClient > error: " + e.getMessage());
      e.printStackTrace(System.out);
      System.exit(1);
    } 
    catch (IOException e) 
    {
      System.err.println("Couldn't get I/O for "
                         + "the connection to: "+host);
      System.out.println("DataExchangeClient > error: " + e.getMessage());
      e.printStackTrace(System.out);
      System.exit(1);
    }
	  
    try
    {  
      File file = new File(filename);
      DataOutputStream dsout = new DataOutputStream(out);
      FileInputStream fs = new FileInputStream(file);
      // first convert the filename to a byte array
      String fn = file.getName();
      byte[] fname = fn.getBytes();
      // now write the string bytes followed by a '0'
      for (int i=0;i<fname.length;i++) 
      {
        dsout.write(fname[i]);    
      }
      dsout.write(0);  // marks end of name info
      
      //write the session id to the stream
      byte[] cook = cookie.getBytes();
      for(int i=0; i<cook.length; i++)
      {
        dsout.write(cook[i]);
      }
      dsout.write(0);
      
      // now read response from server
      InputStreamReader isr = new InputStreamReader(in);
      BufferedReader rin = new BufferedReader(isr);

      // now send the file data
      byte[] buf = new byte[1024];
      int cnt = 0;
      int i = 0;
      while (cnt!=-1) 
      {
        cnt = fs.read(buf);
        //System.out.println("i = "+ i +" Bytes read = " + cnt);
        if (cnt!=-1) 
        {
          dsout.write(buf, 0, cnt);
        }
        i++;
      }
      fs.close();
      dsout.flush();
      dsout.close();
	  }
	  catch (Exception w) 
    {
      System.out.println("DataExchangeClient > error in DataStream: " + w.getMessage());
    }

	  return res;
	}
  
  
  
  /**
  *
  * Main method for testing the application
  *
  */
	public static void main(String[] args)
  {
	 String temp = null;
     String servlet = "/framework/servlet/dataexchange";
     String protocol = "http://";
     String host = "vegbank.nceas.ucsb.edu";
     String server = protocol + host + servlet;
     String filename = "test.dat";
		 String userName = "user";
	 	// String parameterString = "?action=uploadFile&user=harris02@hotmail.com&exchangeType=upload&submitter=harris&"
	  //	+"password=jasmine&file=test.dat";
	 

		DataExchangeClient.uploadFile(servlet, protocol, host, server, filename, userName, "testfile");
  }





 /**
  *  This is the method that negotiates the upload process which 
	*  consistes of figuring out the port number to open for the 
	* 'sendFile' method
	*
	* @param servlet -- the servlet name 
	* @param protocol -- http
  * @param host -- the name of the host or the ip address
  * @param server
  * @param fileName -- the name of the file to be sent
	* @param userName -- the name of the user that owns the file
	* @param fileType -- the type of file to be specified in the database
	*
  */
		public static void uploadFile(
			String servlet,
			String protocol,
			String host,
			String server,
			String filename,
			String userName,
			String fileType)
		{
   	 System.out.println("DataExchangeClient > Begining the DataExchangeClient.uploadFile negotiations" );
    	try
   	  {
       String temp = null;
      
			 String u1str = server 
			 + "?action=uploadFile&username="+userName
			 +"&exchangeType=upload&submitter="+userName+"&"
	  	 +"password=jasmine&file="+filename+"&"
			 +"filetype="+fileType;
			 
       System.out.println("DataExchangeClient > server string: " + u1str);
       
			 URL u1 = new URL(u1str);
       HttpMessage msg = new HttpMessage(u1);
 			 InputStream in =   msg.sendPostMessage();

       InputStreamReader isr = new InputStreamReader(in);
       char c;
       int i = isr.read();
       String temp2 = "";
       while(i != -1)
       { 
			 	 //get the server response to the getdataport request
         c = (char)i;
         temp2 += c;
       	 i = isr.read();
       }
		
			System.out.println("DataExchangeClient > initial server output string: " + temp2 );
	  	//the input looks like 'port|xxxx|cookie|xxx' tokenize the interger on the pipe
	  	StringTokenizer t = new StringTokenizer(temp2, "|");
	  	String buf = t.nextToken();
	  	buf = t.nextToken().trim();
      int port = (new Integer(buf)).intValue();
	  	//now pick up the cookie info
	  	buf = t.nextToken().trim();
	  	String cookie = t.nextToken().trim();
	  
	  	System.out.println("DataExchangeClient > connecting to server on port: "	+port);
      //this is a hack to give the servlet enough time to start
      //the socket thread.  Sometimes it doesn't work
      Thread.sleep(500);
      SendFile(filename, host, port, cookie);
    }
    catch(Exception e)
    {
      System.out.println("Exception: " + e.getMessage());
      e.printStackTrace(System.out);
    }
    
    
  }
}
