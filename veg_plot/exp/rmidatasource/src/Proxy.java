/**
* Proxy.java
*
* These set of classes were written to teach myself JAVA socket
* communications.
*
* copyright 1998 Paul Pearce
* @author Paul Pearce
* @version July 4 1998 v12
* paul@pfad.demon.co.uk
* http://www.pfad.demon.co.uk
*
* Usage of this source code is covered in the
* Proxyreadme.txt file
*
*
* JHH - got rid of line 399 b/c it was failing while running on jdk 1.3
*
*/

import java.io.* ;
import java.util.* ;
import java.net.* ;
import java.io.* ;

// import Format ;

/**
* Proxy reads the config. file specified on the command line and
* starts the proxySocket thread.
*
* run this with :-
*
* java Proxy proxy.ini
*
*/

class Proxy {
   protected static int iConnCount = 0 ;
   protected static Vector vConn = new Vector() ;
   protected static String LogName ;
   protected static Logger Log ;

   public static void main(String args[]) {
      ProxySocket Ps ;

      // read the command line and get arguments

      try {
          args[0].length() ;
      } catch(ArrayIndexOutOfBoundsException e) {
         System.err.println("Error: Specify ini file to read") ;
         System.exit(1) ;
      }

      try {
         // get the parameters
         ReadParamFile(args[0]) ;
      } catch(IOException e) {
         System.err.println("Problem reading parameters") ;
         System.exit(2) ;
      } catch(Error e) {
         System.err.println("Problem reading parameters") ;
         System.exit(3) ;
      }

      // open the log file
      Log = new Logger(LogName) ;

      // multiple connections are in vConn at this stage

      for(int i = 0 ; i < iConnCount ; i++) {
         StringTokenizer Param = new StringTokenizer((String)vConn.elementAt(i)) ;
         if(Param.countTokens() < 5) {
            System.err.println(vConn.elementAt(i) + " is invalid") ;
            continue ;
         }

         // create a Proxy socket object

         // System.out.println(vConn.elementAt(i)) ;

         Ps = new ProxySocket(Integer.parseInt(Param.nextToken()),
                              Param.nextToken(),
                              Integer.parseInt(Param.nextToken()),
                              Log,
                              Integer.parseInt(Param.nextToken()),
                              Integer.parseInt(Param.nextToken()),
                              "Ps" + i) ;

         // set the Proxy object running
         Ps.start() ;
         Ps = null ;
         Param = null ;
      }
   }

   static void ReadParamFile(String FileName) throws IOException {
      Properties props = new Properties() ;
      File f = null ;
      InputStream is ;
      String p ;

      f = new File(FileName) ;

      try {
         is = new BufferedInputStream(new FileInputStream(f)) ;
      } catch(FileNotFoundException e) {
        System.out.println("Error: File " + FileName + " not found") ;
        throw e ;
      }

      // load the properties from the file
      props.load(is) ;

      // close the file
      is.close() ;

      // now get the values from the file

      p = props.getProperty("Conn" + iConnCount) ;
      while(p != null) {
         iConnCount++ ;
         vConn.addElement(p) ;
         p = props.getProperty("Conn" + iConnCount) ;
      }

      if(iConnCount < 1) {
         System.err.println("Error: ini file has no 'Conn0='") ;
         throw new Error() ;
      }
      p = props.getProperty("LogFile") ;
      if(p == null) {
         System.err.println("Error: ini file has no 'LogFile='") ;
         throw new Error() ;
      }
      else {
         LogName = p ;
      }
   }
}
/**
* ProxySocket listens for a connection on the specified TCP port, then makes
* a connection to another IpAddress and port number. It then starts two
* threads to handle bidirectional traffic on the two sockets. It then loops
* back to waiting for more connections.
*
*/

class ProxySocket extends Thread {
   private int iListenPortNumber ;
   private InetAddress IpAddr ;
   private int iDestPortNumber ;
   private String sDestIpAddress ;
   protected Socket soInbound, soOutbound ;
   private SocketIoBase sOutbound, sInbound ;
   private ServerSocket soServer ;
   protected int iLogging ;
   private Logger Log ;
   private int iConn, iConnCount, iDataCapture ;
   private String MyName ;
   private FileOutputStream dcRw = null ;

   public ProxySocket(
              int iListenPortNumber,
              String sDestIpAddress,
              int iDestPortNumber,
              Logger Log,
              int iLogging,
              int iDataCapture,
              String MyName) {

      this.iListenPortNumber = iListenPortNumber ;
      this.Log = Log ;
      this.iLogging = iLogging ;
      this.iDataCapture = iDataCapture ;
      try {
         this.IpAddr = InetAddress.getByName(sDestIpAddress) ;
      } catch (UnknownHostException e) {}

      this.iDestPortNumber = iDestPortNumber ;
      this.sDestIpAddress = sDestIpAddress ;
      this.MyName  = MyName ;
   }

   public void run() {
      int iEcho = 0 ;

      this.setName(MyName) ;

      // create a listen socket

      if(iLogging > 0) 
			{
         Log.It("Creating a listen socket", iLogging) ;
      }

      try 
			{
         soServer = new ServerSocket(iListenPortNumber) ;
      } 
			catch(IOException e) 
			{
				System.out.println("Proxy > Exception: " + e.getMessage() );
			}

      iConn = 0 ;
      iConnCount = 0 ;

      while(true) 
			{
         // listen & accept an incoming connection
         try 
				 {
            if(iLogging > 0) 
						{
               Log.It("Listening on port " + iListenPortNumber + " ...", iLogging) ;
            }

            soInbound = soServer.accept() ;

            if(iLogging > 0) 
						{
               Log.It("Accepted a connection from " +
                      soInbound.getInetAddress() +
                      ":" +
                      soInbound.getPort(),
                      iLogging) ;
            }
         } 
				 catch(IOException e) 
				 {
             Log.It("Error on accept " + e, iLogging) ;
             continue ;
         }

         if(sDestIpAddress.startsWith("ECHO")) 
				 {
           iEcho = 1 ;
         }
         else 
				 {
           iEcho = 0 ;
         }

         if(iLogging > 0) {
           Log.It("Connecting to " +
                  sDestIpAddress +
                  ":" + iDestPortNumber,
                   iLogging) ;
         }

         if(iEcho > 0) 
				 {
           // loopback the two sockets
           soOutbound = soInbound ;
         }
         else 
				 {
            // make a socket connection to destination
            try
						{
               soOutbound = new Socket(IpAddr, iDestPortNumber) ;
            } 
						catch(IOException e) 
						{
              Log.It("Error on connect " + e, iLogging) ;
              try 
							{
                 soInbound.close() ;
              } 
							catch(IOException ee) 
							{
								System.out.println("Proxy > Exception: " + ee.getMessage() );
							}

              soInbound = null ;
              continue ;
            }
         }

         // pass startup parameters to socket handling classes
         if(iLogging > 0) 
				 {
            Log.It("Setting up socket handler class data", iLogging) ;
         }

         // create two new standalone socket handler threads

         sOutbound = new SocketIoBase() ;
         sInbound  = new SocketIoBase() ;

         iConn++ ;
         iConnCount++ ;

         if(iLogging > 0) 
				 {
            Log.It("Current proxy connections: " + iConn, iLogging) ;
            Log.It("Total   proxy connections: " + iConnCount, iLogging) ;
         }

         if(iDataCapture > 0) 
				 {
            try 
						{
               dcRw  = new FileOutputStream(MyName +  "_" +
                                            iConnCount +
                                            ".cap") ;
            } 
						catch(IOException e) 
						{
              System.out.println("Proxy > Exception: " + e.getMessage() );
							Log.It("Problem opening data capture files:" + e, iLogging) ;
              iDataCapture = 0 ;
            }
         }
				 
         // setup sockets and logging for the socket handler threads
         sOutbound.Setup(this,
                         soInbound,
                         soOutbound,
                         MyName + "OrI" + iConnCount,
                         Log,
                         iLogging,
                         iDataCapture,
                         dcRw,
                         "OrI>") ;

         sInbound.Setup(this,
                         soOutbound,
                         soInbound,
                         MyName + "NeW" + iConnCount,
                         Log,
                         iLogging,
                         iDataCapture,
                         dcRw,
                         "NeW>") ;

         // pass peer thread to each other
         sOutbound.SetPeer(sInbound) ;
         sInbound.SetPeer(sOutbound) ;


         // start them up and running
         if(iLogging > 0) {
            Log.It("Starting up socket handlers", iLogging) ;
         }
         sOutbound.start() ;
         sInbound.start() ;

         // free my sockets and go back to the accept

         soOutbound = null ;
         soInbound  = null ;
         dcRw = null ;
      }
   }

   synchronized void ConnectionLost() {
      // socket handler thread that lost the socket will call this method
      // to update current connection counts.
      iConn-- ;
   }
}

/**
* SocketIoBase simply blocks on reading from one socket, then simply passed the
* data to the other socket.
*
*/

class SocketIoBase extends Thread {
   private InputStream  Sinput ;
   private OutputStream Soutput ;
   private ProxySocket Parent ;
   protected int iLogging = 0 ;
   private SocketIoBase Peer ;
   private Socket s1, s2 ;
   private int iTerminate = 0 ;
   private int iDataCapture ;
   private Logger Log ;
   private String MyName, sSentinal ;
   private byte[] InBuffer1024 = new byte[1024] ;
   private byte[] OutBuffer = new byte[16384] ;
   private byte[] Sentinal = new byte[4] ;
   private FileOutputStream dcRw ;
   private PrintStream ps ;
   private int iHaveRead, iToWrite, iWritten ;
   private static long lLastIo  = System.currentTimeMillis() ;
   private StringBuffer sbHex ;
   private StringBuffer sbVal ;

   public void SetPeer(SocketIoBase Peer) {
      this.Peer = Peer ;
   }

   public synchronized void CleanUp() throws IOException {
      iTerminate = 1 ;

      try {
         s1.close() ;
         s2.close() ;
         Sinput.close() ;
         Soutput.close() ;
         if(iDataCapture > 0) {
            dcRw.flush() ;
            dcRw.close() ;
         }
      } catch(IOException e) {
         if(iLogging > 0) {
            Log.It("Cleanup had problem " + e, iLogging) ;
         }
         throw e ;
      }
   }

   public void Setup(ProxySocket Parent,
                     Socket sNew1,
                     Socket sNew2,
                     String MyName,
                     Logger Log,
                     int iLogging,
                     int iDataCapture,
                     FileOutputStream dcRw,
                     String sSentinal) {
      this.Parent = Parent ;
      this.s1 = sNew1 ;
      this.s2 = sNew2 ;
      this.MyName = MyName ;
      this.Log = Log ;
      this.iLogging = iLogging ;
      this.iDataCapture = iDataCapture ;
      this.dcRw = dcRw ;
      //this.ps = new PrintStream(dcRw) ;
      this.sSentinal = sSentinal ;
      try {
         Sinput  = new BufferedInputStream(s1.getInputStream()) ;
         Soutput = new BufferedOutputStream(s2.getOutputStream()) ;
      } catch(IOException e) {}
      sbHex = new StringBuffer() ;
      sbVal = new StringBuffer() ;
   }

   public void run() 
	 {
      this.setName(MyName) ;

      Sentinal = sSentinal.getBytes() ;

      // read from Sinput

      if(iLogging > 0) 
			{
         Log.It("Starting up", iLogging) ;
      }

      iHaveRead = -1 ;

      try {
         iHaveRead = Sinput.read(InBuffer1024, 0, 1024) ;
      } catch(IOException e) {
        Log.It("Error on Input " + e, iLogging) ;
        iHaveRead = -1 ;
      }
      if(iLogging > 0) {
         Log.It("Read " + iHaveRead + " bytes", iLogging ) ;
      }

      while(iHaveRead != -1) {
         // call process line
         if(iDataCapture > 0) {
            try {
               synchronized(Parent) {
                  DumpBuffer(iDataCapture) ;
               }
            }catch(IOException e) {
               Log.It("Problem writing data capture file" + e, iLogging) ;
               iDataCapture = 0 ;
            }
         }
         iToWrite = ProcData(iHaveRead) ;

         // write to Soutput
         if(iLogging > 0) {
            Log.It("Writing " + iToWrite + " bytes", iLogging ) ;
         }
         try {
            Soutput.write(OutBuffer, 0, iToWrite) ;
            Soutput.flush() ;
         } catch(IOException e) {
           Log.It("Error on Output " + e, iLogging) ;
           iHaveRead = -1 ;
           break ;
         }

         // be kind to everyone else
         yield() ;

         try {
            iHaveRead = Sinput.read(InBuffer1024, 0, 1024) ;
         } catch(IOException e) {
           Log.It("Error on Input " + e, iLogging) ;
           iHaveRead = -1 ;
           break ;
         }
         if(iLogging > 0) {
            Log.It("Read " + iHaveRead + " bytes", iLogging ) ;
         }
      }

      if(iLogging > 0) {
         Log.It("Lost the socket", iLogging ) ;
      }

      if(iTerminate == 0) {
         try {
            if(iLogging > 0) {
               Log.It("CleanUp Peer", iLogging ) ;
            }
            Peer.CleanUp() ;
         } catch(IOException e) {
           Log.It("Problem with Cleanup Peer " + e, iLogging) ;
         }

         if(iLogging > 0) {
            Log.It("Telling Parent", iLogging ) ;
         }
         Parent.ConnectionLost() ;

         if(iLogging > 0) {
            Log.It("CleanUp me", iLogging ) ;
         }
         try {
            CleanUp() ;
         } catch(IOException e) {
           Log.It("Problem with Cleanup me " + e, iLogging) ;
         }
      }
      if(iLogging > 0) {
         Log.It("... and bye", iLogging ) ;
      }
   }


   // mod. the input Buffer, and write results to the output Buffer
   // return the length of the output Buffer

   private int ProcData(int iReadd) {

      if(iLogging > 0) {
         Log.It("In ProcData, data length " + iReadd, iLogging) ;
      }

      // simply copy input to output here
      System.arraycopy(InBuffer1024, 0, OutBuffer, 0, iReadd) ;

      return(iReadd) ;
   }

   void DumpBuffer(int iType) throws IOException {
      long lTmp ;
      int i = 0 ;
      int l = 0 ;
      int iIndex, iLoops, iLeftOver ;
      String sHexBuf = new String() ;
      String sAscBuf = new String() ;

      if(iType == 1) {
         dcRw.write(Sentinal, 0, 4) ;
         dcRw.write(InBuffer1024, 0, iHaveRead) ;
         dcRw.flush() ;
         return ;
      }

      lTmp = System.currentTimeMillis() - lLastIo ;

      Date today = new Date() ;
      ps.println("S  " +
                  sSentinal +
                  " " +
                  today.getYear() +
                  "/" +
                  (today.getMonth() + 1) +
                  "/" +
                  today.getDate() +
                  " " +
                  today.getHours() +
                  ":" +
                  today.getMinutes() +
                  ":" +
                  today.getSeconds() +
                  "  " +
                  iHaveRead +
                  "b " +
                  lTmp +
                  "mS") ;

      sbHex.setLength(0) ;
      sbVal.setLength(0) ;

      // create two string buffers containing printable representations of
      // the byte array. The spacer values are so the lineup of the
      // characters match when printed.

      sHexBuf = bytesToHex(InBuffer1024,  iHaveRead, 1) ;
      sAscBuf = bytesToChar(InBuffer1024, iHaveRead, 2) ;

      // tut tut. I hardcoded values for speed from here onwards

      iLoops = iHaveRead / 16 ;
      iLeftOver = iHaveRead % 16 ;

      sbHex.append("X  ") ;
      sbVal.append("C  ") ;

      i = 0 ;
      l = 0 ;
      iIndex = 0 ;

      for(; i < iLoops ; i++, iIndex += 48) {
         sbHex.append(sHexBuf.substring(iIndex, iIndex + 48)) ;
         sbVal.append(sAscBuf.substring(iIndex, iIndex + 48)) ;

         l += 16 ;

         sbVal.append("  " + (l - 1)) ;
         ps.println(sbHex) ;
         ps.println(sbVal) ;
         ps.println("_") ;
         sbHex.setLength(0) ;

         sbVal.setLength(0) ;
         sbHex.append("X  ") ;
         sbVal.append("C  ") ;
      }

      if(iLeftOver > 0) {
         sbHex.append(sHexBuf.substring(iIndex,
                                        iIndex + (iLeftOver * 3))) ;
         sbVal.append(sAscBuf.substring(iIndex,
                                        iIndex + (iLeftOver * 3))) ;
         l += iLeftOver ;

         sbVal.append("  " + (l - 1)) ;
         ps.println(sbHex) ;
         ps.println(sbVal) ;
      }
      ps.println("#  ======") ;
      ps.flush() ;
      lLastIo = System.currentTimeMillis() ;
   }

   public static final String bytesToHex(byte[] a, int iLength, int iSpacer)
   {
      StringBuffer s = new StringBuffer() ;

      for(int i = 0 ; i < iLength ; i++) {
         s.append(Character.forDigit((a[i] >> 4) & 0xf, 16)) ;
         s.append(Character.forDigit(a[i] & 0xf, 16)) ;
         if(iSpacer > 0) {
            if(iSpacer > 1) {
               for(int j = 0 ; j < iSpacer ; j++) {
                  s.append(' ') ;
               }
            }
            else {
               s.append(' ') ;
            }
         }
      }
      return(s.toString()) ;
   }

   public static final String bytesToChar(byte[] a, int iLength, int iSpacer)
   {
      StringBuffer s = new StringBuffer() ;

      for(int i = 0 ; i < iLength ; i++) {
         if((char)a[i] < ' ' || (char)a[i] > 127) {
            s.append(".") ;
         }
         else {
            s.append((char)a[i]) ;
         }
         if(iSpacer > 0) {
            if(iSpacer > 1) {
               for(int j = 0 ; j < iSpacer ; j++) {
                  s.append(' ') ;
               }
            }
            else {
               s.append(' ') ;
            }
         }
      }
      return(s.toString()) ;
   }

   public static final String byteToHex(int a)
   {

      StringBuffer s = new StringBuffer() ;

      s.append(Character.forDigit((a >> 4) & 0xf, 16)) ;
      s.append(Character.forDigit(a & 0xf, 16)) ;

      return(s.toString()) ;
   }
}

/**
* Logger writes a message to the log file and optionally to stderr
*/

class Logger {
   static PrintStream log = null ;
   static String LogFile ;

   public Logger(String LogFile) {
      this.LogFile = LogFile ;
   }

   public synchronized void It(String sLine, int iLevel) {
      if(log == null) {
         try {
            log = new PrintStream(
                      new BufferedOutputStream(
                          new AppendFileOutputStream(LogFile))) ;
         } catch(IOException e) {}
         // write separator for start of new log entries
         log.println("#####################################################") ;
      }
      Date today = new Date() ;
      log.println(today.getYear() +
                  "/" +
                  (today.getMonth() + 1) +
                  "/" +
                  today.getDate() +
                  " " +
                  today.getHours() +
                  ":" +
                  today.getMinutes() +
                  ":" +
                  today.getSeconds() +
                  " " +
                  Thread.currentThread().getName() +
                  "," +
                  sLine) ;
      log.flush() ;

      if(iLevel > 1) {
         System.err.println(
                  today.getYear() +
                  "/" +
                  (today.getMonth() + 1) +
                  "/" +
                  today.getDate() +
                  " " +
                  today.getHours() +
                  ":" +
                  today.getMinutes() +
                  ":" +
                  today.getSeconds() +
                  " " +
                  Thread.currentThread().getName() +
                  "," +
                  sLine) ;
      }
   }
}

/**
* AppendFileOutputStream allows Logger to append to an existing file
*
* Original code copied directly from :-
*
* JAVA Network Programming
* Merlin & Conrad Huges/Michael Shoffner/Maria Winslow
* ISBN 1-884777-23-1
* Chapter 5
* Page 68
*
*/

class AppendFileOutputStream extends FileOutputStream {
   protected FileInputStream file ;

   AppendFileOutputStream(String FileName) throws IOException {
      super((new RandomAccessFile(FileName, "rw")).getFD()) ;
      file = new FileInputStream(getFD()) ;
      int n = file.available() ;
      do {
         n -= file.skip(n) ;
      } while(n > 0) ;
   }
}

