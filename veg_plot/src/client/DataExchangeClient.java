/**
 *  '$RCSfile: DataExchangeClient.java,v $'
 *    Purpose: A test driver for the DataFileServer class
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Chad Berkley
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-02-22 21:24:41 $'
 * '$Revision: 1.1 $'
 */

package client;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import java.net.*;
//import edu.ucsb.nceas.metacat.*;
//import javax.servlet.http.*;


public class DataExchangeClient{
 public DataExchangeClient()
  {
  
  }
  
  /**
   * sends a file to the data file server socket
   */
  public static String SendFile(String filename, String host, int port, 
                                String cookie) 
  {
    Socket echoSocket = null;
    OutputStream out = null;
    InputStream in = null;
    
		String res = "return";
    
    System.out.println("host: " + host + " port: " + port + " filename: " +
                       filename + " cookie: " + cookie);
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
      System.err.println("Don't know about host: " + host);
      System.out.println("error: " + e.getMessage());
      e.printStackTrace(System.out);
      System.exit(1);
    } 
    catch (IOException e) 
    {
      System.err.println("Couldn't get I/O for "
                         + "the connection to: "+host);
      System.out.println("error: " + e.getMessage());
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
        System.out.println("i = "+ i +" Bytes read = " + cnt);
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
      System.out.println("error in DataStreamTest: " + w.getMessage());
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
     String servlet = ":8080/examples/servlet/DataExchangeServlet";
     String protocol = "http://";
     String host = "127.0.0.1";
     String server = protocol + host + servlet;
     String filename = "../test.dat";
	 String parameterString = "?exchangeType=upload&submitter=harris&"
	  	+"password=jasmine&file=../test.dat";
	 
	DataExchangeClient dec = new DataExchangeClient();
	dec.uploadFile(servlet, protocol, host, server, filename);
  }





 /**
  *  This is the method that negotiates the upload process and uploads the
  * datfile
  *
  *
  *
  */
	  
public static void uploadFile (String servlet, String protocol, String host, 
String server, String filename) {
    System.out.println("Starting DataStreamTest");
    try
    {
      String temp = null;
//      String servlet = ":8080/examples/servlet/DataExchangeServlet";
//      String protocol = "http://";
//      String host = "beta.nceas.ucsb.edu";
//      String server = protocol + host + servlet;
//      String filename = "../test.dat";
      //login url
      String u1str = server + "?exchangeType=upload&submitter=harris&"
	  	+"password=jasmine&file=../test.dat";
      System.out.println("u1: " + u1str);
      URL u1 = new URL(u1str);
      HttpMessage msg = new HttpMessage(u1);
 InputStream in =   msg.sendPostMessage();
//      String cookie = msg.getCookie();
      ////get the port to send the data to
//      System.out.println("u2: " + server + "?action=getdataport");
//      URL u2 = new URL(server + "?action=getdataport");
//      HttpMessage msg2 = new HttpMessage(u2);
//      InputStream in = msg2.sendPostMessage();

//	  InputStream in = msg.sendPostMessage();
      InputStreamReader isr = new InputStreamReader(in);
      char c;
      int i = isr.read();
      String temp2 = "";
      while(i != -1)
      { //get the server response to the getdataport request
        c = (char)i;
        temp2 += c;
       i = isr.read();
      }
	  //the input looks like 'port|xxxx|cookie|xxx' tokenize the interger on the pipe
	  StringTokenizer t = new StringTokenizer(temp2, "|");
	  String buf = t.nextToken();
	  buf = t.nextToken().trim();
      int port = (new Integer(buf)).intValue();
	  //now pick up the cookie info
	  buf = t.nextToken().trim();
	  String cookie = t.nextToken().trim();
	  
	  System.out.println("DataStreamTest.main connceting to server on port: "
	  	+port);
      //this is a hack to give the servlet enough time to start
      //the socket thread.  Sometimes it doesn't work
      Thread.sleep(1000);
      
      SendFile(filename, host, port, cookie);
    }
    catch(Exception e)
    {
      System.out.println("error in main: " + e.getMessage());
      e.printStackTrace(System.out);
    }
    
    
  }
}
