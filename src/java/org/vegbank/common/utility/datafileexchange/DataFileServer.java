package org.vegbank.common.utility.datafileexchange;

/**
 *  '$RCSfile: DataFileServer.java,v $'
 *    Purpose: A Class that implements a socket server for data files
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Chad Berkley
 *    Release: @release@
 *
 *   '$Author: farrell $'
 *     '$Date: 2003-11-12 22:53:51 $'
 * '$Revision: 1.3 $'
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.vegbank.common.utility.ServletUtility;

	public class DataFileServer extends Thread 
	{
		private ServletUtility util = new ServletUtility();
		//this class needs the file database classes 
		private DataFileDB filedb = new DataFileDB();
		ResourceBundle rb = ResourceBundle.getBundle("veg_servlet");
		private String uploadDir = "/usr/local/devtools/jakarta-tomcat/webapps/uploads/";
  	static String filedir = "";
		private String fileType = null;
  	String user = null;
  	String sess_id = null;
  	int port;
  	static String httpstring = "http://dev.nceas.ucsb.edu:8090/metacat/upload/";
  	protected Socket s;
  	
		//constructor
  	DataFileServer(int port, String user, String sess_id, String filetype) 
  	{
    	this.sess_id = sess_id;
    	this.user = user;
    	this.port = port;
			filedir = uploadDir;
			this.fileType = filetype;
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
		DataFileServer dfs = new DataFileServer(newPort, newUser, newSess_id, "knknown");
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
  public void run() 
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
      
      if(! sess_id.equals(session_id))
      {
				//this is an unauthorized connection to this port
        System.out.println("DataFileServer.run > unauthorized request");
        System.out.println("DataFileServer.run > sessid: '"+ sess_id+"'");
				System.out.println("DataFileServer.run > session_id (required): '"+ session_id+"'");
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
			
				//put the new document info into the DB
				//now register the document with the database
				boolean registered = registerUserDocument( user, filename, this.fileType);
				System.out.println( "DataFileServer >  registration results: " + registered);
      
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
	
	
	/**
	 * method to handle the registration of a document to a specified user
	 */
	 private boolean registerUserDocument(String username, String filename, 
	 	String fileType)
	 {
		try
		{
			System.out.println("DataFileServer > REGISTERED USER: " + username);
			 //figure out a way for determinin file size
			 int fileSize = 0;
			 
			 //register the document with the database
			int accessionId = filedb.getNewAccessionId();
			//register and make sure that we get back an
			//accession number 
			filedb.registerDocument(accessionId, fileSize, filename, uploadDir );
					
			//copy the file to a file with the name of the accesion number
			util.fileCopy(uploadDir+filename, uploadDir+accessionId);
			//delete the file
			util.flushFile(uploadDir+filename);
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(true);
	 }
	
}
