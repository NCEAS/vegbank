/**
 *  '$RCSfile: DataFileServer.java,v $'
 *    Purpose: A Class that implements a socket server for data files
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Chad Berkley
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-02-17 00:14:09 $'
 * '$Revision: 1.1 $'
 */

//package edu.ucsb.nceas.metacat;

import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

public class DataFileServer extends Thread 
{
//  MetaCatUtil util = new MetaCatUtil();
  static String filedir = "";
  String user = null;
  String sess_id = null;
  int port;
  static String httpstring = "http://dev.nceas.ucsb.edu:8090/metacat/upload/";
  protected Socket s;
  
  DataFileServer (int port, String user, String sess_id) 
  {
    this.sess_id = sess_id;
    this.user = user;
    this.port = port;
//    filedir = util.getOption("datafilepath");
	filedir = "/jakarta-tomcat/webapps/ROOT/uploads/"; 
  }
 
 
 /**
  * Main method for testing
  */  
public static void main(String[] args)
  {
    System.out.println("Starting DataStreamTest");
	int newPort=8081;
	String newUser="harris";
	String newSess_id="abc";
	DataFileServer dfs = new DataFileServer(newPort, newUser, newSess_id);
//DataFileServer dfs = new DataFileServer(Port, newUser, newSess_id);
      dfs.start();
	
	//run();
    
   }	
   
   
   
  /**
   * returns true if the port specified is not in use.  false otherwise
   */
  public static boolean portIsAvailable(int port)
  {
    Socket s;
    String host = "localhost";
    try
    {
      s = new Socket(host, port);
      s.close();
      //we could create a socket on this port so the port is not available
      return false;
    }
    catch(UnknownHostException u)
    {
      //it better know localhost!
    }
    catch(IOException i)
    {
      //an ioexception is thrown if the port is not in use
      //System.out.println("port not in use");
      return true;
    }
    return true;
  }




  /**
   * This method is invoked when this class is broken off into a new thread
   */
  public void run () 
  {
    ServerSocket server = null;
    Socket client = null;
    try
    {
      System.out.println("DataFileServer.run > Starting on port " + port);
      System.out.println("DataFileServer.run > Waiting for sess_id: " + sess_id);
      server = new ServerSocket((new Integer(port)).intValue());
      System.out.println("DataFileServer.run > Waiting");
      client = server.accept();
      System.out.println("DataFileServer.run > Accepted from " + client.getInetAddress());
    
      InputStream in = client.getInputStream();
      OutputStreamWriter osw = new OutputStreamWriter(client.getOutputStream());
      System.out.println("DataFileServer.run > output stream received");
      // first read to get the file name
      byte[] str = new byte[1024];
      int val = -1;
      int i = 0;
      //get the filename
      while (val!=0) 
      {
        //System.out.println("i: " + i);
        val = in.read();
        str[i] = (byte)val;
        i++;
      }
      String filename = (new String(str,0, i-1)).trim();
      System.out.println("DataFileServer.run > filename: " + filename);
      
      val = -1;
      i = 0;
      str = new byte[1024];
      //get the session id
      while (val!=0) 
      {
        val = in.read();
        str[i] = (byte)val;
        i++;
      }
      String session_id = (new String(str,0, i-1)).trim();
      System.out.println("DataFileServer.run > session_id: " + session_id);
      
      if(!sess_id.equals(session_id))
      {//this is an unauthorized connection to this port
        System.out.println("DataFileServer.run > unauthorized request");
        return;
      }
      else
      {
        System.out.println("DataFileServer.run > User authenticated on port "
			+ port);
      }
      
      String restext=null;
      File outfile = new File(filedir + filename);
      System.out.println("DataFileServer.run > outfile: " + filedir + filename);
      boolean nameInUse = outfile.exists();
      int filenametemp = 1;
      while(nameInUse)
      {
        filename += filenametemp;
        outfile = new File (filedir + filename);
        nameInUse = outfile.exists();
        filenametemp++;
      }
       
      try
      {
//        AccessionNumber anum = new AccessionNumber();
//        String accnum = anum.generate(null, "INSERT");
        //osw.write(restext,0,restext.length());
        //osw.flush();
        FileOutputStream out = new FileOutputStream(outfile);
        byte[] buf = new byte[1024];
        int cnt = 0;
        while (cnt!=-1) 
        {
          cnt = in.read(buf, 0, 1024);
          if (cnt!=-1) 
          {
            out.write(buf, 0, cnt);
          }
        }
        out.flush();
        out.close();
//		put the new document info into the DB
//      updateDB(accnum, filename, user);
      }
      catch(Exception e)
      {
        System.out.println("error in DataFileServer.run(): " + e.getMessage());
        e.printStackTrace(System.out);
      }
    } 
    catch (IOException ex) 
    {
      ex.printStackTrace();
    } 
    finally 
    {
      try
      {
        client.close();
        System.out.println("DataFileServer.run > client socket closed");
        server.close();
        System.out.println("DataFileServer.run > server socket closed");
      } 
      catch (IOException ex) 
      {
        ex.printStackTrace();
      }
      System.out.println("DataFileServer.run > done");
    }
  }
  
}
