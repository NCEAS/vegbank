/*
             * DBConnectionBroker
             *
             * Copyright (C) 1999 Marc A. Mnich <marc@javaexchange.com>
             * 
             * This library is free software; you can redistribute it and/or
             * modify it under the terms of the GNU Lesser General Public
             * License as published by the Free Software Foundation; either
             * version 2.1 of the License, or (at your option) any later version.
             *
             * This library is distributed in the hope that it will be useful,
             * but WITHOUT ANY WARRANTY; without even the implied warranty of
             * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
             * Lesser General Public License for more details.
             *
             * You should have received a copy of the GNU Lesser General Public
             * License along with this library; if not, write to the Free Software
             * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
             *
             * See the COPYING file located in the top-level-directory of
             * the archive of this library for complete text of license.
             */

            /**
             * DbConnectionBroker.
             * @version 1.0.11 12/7/99
             * @author Marc A. Mnich
             */
            import java.io.*;
            import java.sql.*;
            import java.text.*;
            import java.util.Date;


            /**
             * DbConnectionBroker
             * A servlet-based broker for database connections.
             * Creates and manages a pool of database connections.
             * @version 1.0.11 12/7/99
             * @author Marc A. Mnich
             */
            public class DbConnectionBroker implements Runnable {
                private Thread runner;

                private Connection[] connPool;
                private int[] connStatus;

                private long[] connLockTime, connCreateDate;
                private String[] connID;
                private String dbDriver, dbServer, dbLogin, dbPassword, logFileString;
                private int currConnections, connLast, minConns, maxConns, maxConnMSec;

                //available: set to false on destroy, checked by getConnection()
                private boolean available=true;

                private PrintWriter log;
                private SQLWarning currSQLWarning;
                private String pid;

                /**
                 * Creates a new Connection Broker<br>
                 * dbDriver:        JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'<br>
                 * dbServer:        JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'<br>
                 * dbLogin:         Database login name.  e.g. 'Scott'<br>
                 * dbPassword:      Database password.    e.g. 'Tiger'<br>
                 * minConns:        Minimum number of connections to start with.<br>
                 * maxConns:        Maximum number of connections in dynamic pool.<br>
                 * logFileString:   Absolute path name for log file. e.g. 'c:/temp/mylog.log' <br>
                 * maxConnTime:     Time in days between connection resets. (Reset does a basic cleanup)<br>
                 */
                public DbConnectionBroker(String dbDriver, String dbServer, String dbLogin,
                    String dbPassword, int minConns, int maxConns,
                        String logFileString, double maxConnTime) throws IOException
                {

                    connPool = new Connection[maxConns];
                    connStatus = new int[maxConns];
                    connLockTime = new long[maxConns];
                    connCreateDate = new long[maxConns];
                    connID = new String[maxConns];
                    currConnections = minConns;
                    this.maxConns = maxConns;
                    this.dbDriver = dbDriver;
                    this.dbServer = dbServer;
                    this.dbLogin = dbLogin;
                    this.dbPassword = dbPassword;
                    this.logFileString = logFileString;
                    maxConnMSec = (int)(maxConnTime * 86400000.0);  //86400 sec/day
                    if(maxConnMSec < 30000) {  // Recycle no less than 30 seconds.
                        maxConnMSec = 30000;
                    }

                    try {
                    log = new PrintWriter(new FileOutputStream(logFileString),true);

                    // Can't open the requested file. Open the default file.
                    } catch (IOException e1) {
                    try {
                    log = new PrintWriter(new FileOutputStream("DCB_" +
                               System.currentTimeMillis() + ".log"),
                               true);

                    } catch (IOException e2) {
                    throw new IOException("Can't open any log file");
                    }

                    }



                // Write the pid file (used to clean up dead/broken connection)
                SimpleDateFormat formatter
                    = new SimpleDateFormat ("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
                Date nowc = new Date();
                pid = formatter.format(nowc);

                BufferedWriter pidout = new BufferedWriter(new
                                                FileWriter(logFileString + "pid"));
                pidout.write(pid);
                pidout.close();

                    log.println("Starting DbConnectionBroker Version 1.0.11:");
                    log.println("dbDriver = " + dbDriver);
                    log.println("dbServer = " + dbServer);
                    log.println("dbLogin = " + dbLogin);
                    log.println("log file = " + logFileString);
                    log.println("minconnections = " + minConns);
                    log.println("maxconnections = " + maxConns);
                    log.println("Total refresh interval = " + maxConnTime + " days");
                    log.println("-----------------------------------------");


                    // Initialize the pool of connections with the mininum connections:
                    // Problems creating connections may be caused during reboot when the
                    //    servlet is started before the database is ready.  Handle this
                    //    by waiting and trying again.  The loop allows 5 minutes for
                    //    db reboot.
                    boolean connectionsSucceeded=false;
                    int dbLoop=20;

                    try {
                        for(int i=1; i < dbLoop; i++) {
                            try {
                                for(int j=0; j < currConnections; j++) {
                                    createConn(j);
                                }
                                connectionsSucceeded=true;
                                break;
                            } catch (SQLException e){
                                log.println("--->Attempt (" + String.valueOf(i) +
                                            " of " + String.valueOf(dbLoop) +
                                            ") failed to create new connections set at startup: ");
                                log.println("    " + e);
                                log.println("    Will try again in 15 seconds...");
                                try { Thread.sleep(15000); }
                                catch(InterruptedException e1) {}
                            }
                        }
                        if(!connectionsSucceeded) { // All attempts at connecting to db exhausted
                            log.println("\r\nAll attempts at connecting to Database exhausted");
                            throw new IOException();
                        }
                    } catch (Exception e) {
                        throw new IOException();
                    }

                    // Fire up the background housekeeping thread

                    runner = new Thread(this);
                    runner.start();

                }//End DbConnectionBroker()
                private void createConn(int i)

                    throws SQLException {

                    Date now = new Date();

                    try {
                        Class.forName (dbDriver);
System.out.println("> "+dbServer);
dbServer = dbServer.trim();
//dbServer="jdbc:idb:/home/computer/harris/compare/vegclass/veg_plot/src/build/bin/sample.prp";
System.out.println("> "+dbServer);
												connPool[i] = DriverManager.getConnection
                                      (dbServer,dbLogin,dbPassword);

                        connStatus[i]=0;
                        connID[i]=connPool[i].toString();
                        connLockTime[i]=0;
                        connCreateDate[i] =  now.getTime();
                    }
                    catch (ClassNotFoundException e2)
                    {}

                    log.println(now.toString() + "  Opening connection " + String.valueOf(i) +
                                " " + connPool[i].toString() + ":");
                }
                /**
                 * Less safe shutdown.  Uses default timeout value.
                 * This method simply calls the <code>destroy()</code> method
                 * with a <code>millis</code>
                 * value of 10000 (10 seconds) and ignores <code>SQLException</code>
                 * thrown by that method.
                 * @see     #destroy(int)
                 */
                public void destroy() {
                    try {
                        destroy(10000);
                    }
                    catch(SQLException e) {}
                }
                /**
                 * Shuts down the housekeeping thread and closes all connections
                 * in the pool. Call this method from the destroy() method of the servlet.
                 */

                /**
                 * Multi-phase shutdown.  having following sequence:
                 * <OL>
                 * <LI><code>getConnection()</code> will refuse to return connections.
                 * <LI>The housekeeping thread is shut down.<br>
                 *    Up to the time of <code>millis</code> milliseconds after shutdown of
                 *    the housekeeping thread, <code>freeConnection()</code> can still be
                 *    called to return used connections.
                 * <LI>After <code>millis</code> milliseconds after the shutdown of the
                 *    housekeeping thread, all connections in the pool are closed.
                 * <LI>If any connections were in use while being closed then a
                 *    <code>SQLException</code> is thrown.
                 * <LI>The log is closed.
                 * </OL><br>
                 * Call this method from a servlet destroy() method.
                 *
                 * @param      millis   the time to wait in milliseconds.
                 * @exception  SQLException if connections were in use after
                 * <code>millis</code>.
                 */
                public void destroy(int millis) throws SQLException {

                    // Checking for invalid negative arguments is not necessary,
                    // Thread.join() does this already in runner.join().

                    // Stop issuing connections
                    available=false;

                    // Shut down the background housekeeping thread
                    runner.interrupt();

                    // Wait until the housekeeping thread has died.
                    try { runner.join(millis); }
                    catch(InterruptedException e){} // ignore

                    // The housekeeping thread could still be running
                    // (e.g. if millis is too small). This case is ignored.
                    // At worst, this method will throw an exception with the
                // clear indication that the timeout was too short.

                    long startTime=System.currentTimeMillis();

                    // Wait for freeConnection() to return any connections
                    // that are still used at this time.
                    int useCount;
                    while((useCount=getUseCount())>0 && System.currentTimeMillis() - startTime <=  millis) {
                        try { Thread.sleep(500); }
                        catch(InterruptedException e) {} // ignore
                    }

                    // Close all connections, whether safe or not
                    for(int i=0; i < currConnections; i++) {
                        try
                        {
                            connPool[i].close();
                        }
                        catch (SQLException e1)
                        {
                            log.println("Cannot close connections on Destroy");
                        }
                    }

                    if(useCount > 0) {
                        //bt-test successful
                        String msg="Unsafe shutdown: Had to close "+useCount+
                    " active DB connections after "+millis+"ms";
                        log.println(msg);
                        // Close all open files
                        log.close();
                        // Throwing following Exception is essential because servlet authors
                        // are likely to have their own error logging requirements.
                        throw new SQLException(msg);
                    }

                    // Close all open files
                    log.close();

                }//End destroy()
                /**
                 * Frees a connection.  Replaces connection back into the main pool for
                 * reuse.
                 */
                public String freeConnection(Connection conn) {
                    String res="";

                    int thisconn = idOfConnection(conn);
                    if(thisconn >= 0) {
                        connStatus[thisconn]=0;
                        res = "freed " + conn.toString();
                        //log.println("Freed connection " + String.valueOf(thisconn) +
                        //            " normal exit: ");
                    } else {
                        log.println("----> Could not free connection!!!");
                    }

                    return res;

                }
                /**
                 * Returns the age of a connection -- the time since it was handed out to
                 * an application.
                 */
                public long getAge(Connection conn) { // Returns the age of the connection in millisec.
                    int thisconn = idOfConnection(conn);
                    return System.currentTimeMillis() - connLockTime[thisconn];
                }
                /**
                 * This method hands out the connections in round-robin order.
                 * This prevents a faulty connection from locking
                 * up an application entirely.  A browser 'refresh' will
                 * get the next connection while the faulty
                 * connection is cleaned up by the housekeeping thread.
                 *
                 * If the min number of threads are ever exhausted, new
                 * threads are added up the the max thread count.
                 * Finally, if all threads are in use, this method waits
                 * 2 seconds and tries again, up to ten times.  After that, it
                 * returns a null.
                 */
                public Connection getConnection() {

                    Connection conn=null;

                    if(available){
                        boolean gotOne = false;

                        for(int outerloop=1; outerloop<=10; outerloop++) {

                            try  {
                                int loop=0;
                                int roundRobin = connLast + 1;
                                if(roundRobin >= currConnections) roundRobin=0;

                                do {
                                     synchronized(connStatus) {
                                        if((connStatus[roundRobin] < 1) &&
                               (! connPool[roundRobin].isClosed())) {
                                                conn = connPool[roundRobin];
                                                connStatus[roundRobin]=1;
                                                connLockTime[roundRobin] =
                                System.currentTimeMillis();
                                                connLast = roundRobin;
                                                gotOne = true;
                                                break;
                                        } else {
                                            loop++;
                                            roundRobin++;
                                            if(roundRobin >= currConnections) roundRobin=0;
                                        }
                                     }
                                }
                                while((gotOne==false)&&(loop < currConnections));

                            }
                            catch (SQLException e1)
                            {}

                            if(gotOne) {
                                break;
                            } else {
                                synchronized(this) {  // Add new connections to the pool
                                    if(currConnections < maxConns) {

                                        try
                                        {
                                            createConn(currConnections);
                            currConnections++;
                                        }
                                        catch(SQLException e)
                                        {
                                            log.println("Unable to create new connection: " + e);
                                        }

                                    }
                                }

                                try { Thread.sleep(2000); }
                                catch(InterruptedException e) {}
                                log.println("-----> Connections Exhausted!  Will wait and try " +
                                            "again in loop " +
                                            String.valueOf(outerloop));
                            }

                        } // End of try 10 times loop

                    } else {
                        log.println("Unsuccessful getConnection() request during destroy()");
                    } // End if(available)

                    return conn;

                }
                /**
                 * Returns the number of connections in the dynamic pool.
                 */
                public int getSize() {
                    return currConnections;
                }//End getSize()
                /**
                 * Returns the number of connections in use.
                 */
                // This method could be reduced to return a counter that is
                // maintained by all methods that update connStatus.
                // However, it is more efficient to do it this way because:
                // Updating the counter would put an additional burden on the most
                // frequently used methods; in comparison, this method is
                // rarely used (although essential).
                public int getUseCount() {
                    int useCount=0;
                    synchronized(connStatus) {
                        for(int i=0; i < currConnections; i++) {
                            if(connStatus[i] > 0) { // In use
                                useCount++;
                            }
                        }
                    }
                    return useCount;
                }//End getUseCount()
                /**
                 * Returns the local JDBC ID for a connection.
                 */
                public int idOfConnection(Connection conn) {
                    int match;
                    String tag;

                    try {
                        tag = conn.toString();
                    }
                    catch (NullPointerException e1) {
                        tag = "none";
                    }

                    match=-1;

                    for(int i=0; i< currConnections; i++) {
                        if(connID[i].equals(tag)) {
                            match = i;
                            break;
                        }
                    }
                    return match;
                }
                /**
                 * Housekeeping thread.  Runs in the background with low CPU overhead.
                 * Connections are checked for warnings and closure and are periodically
                 * restarted.
                 * This thread is a catchall for corrupted
                 * connections and prevents the buildup of open cursors. (Open cursors
                 * result when the application fails to close a Statement).
                 * This method acts as fault tolerance for bad connection/statement programming.
                 */
                public void run()
                {
                    boolean forever = true;
                    Statement stmt=null;
                    String currCatalog=null;
                
                    while(forever) {

                    // Make sure the log file is the one this instance opened
                    // If not, clean it up!
                    try {
                    BufferedReader in = new BufferedReader(new
                                   FileReader(logFileString + "pid"));
                    String curr_pid = in.readLine();
                    if(curr_pid.equals(pid)) {
                        //log.println("They match = " + curr_pid);
                    } else {
                        //log.println("No match = " + curr_pid);
                        log.close();
                    
                        // Close all connections silently - they are definitely dead.
                        for(int i=0; i < currConnections; i++) {
                        try
                            {
                            connPool[i].close();
                            }
                        catch (SQLException e1) {} // ignore
                        }
                        // Returning from the run() method kills the thread
                        return;
                    }
                    
                    in.close();
                    
                    } catch (IOException e1) {
                    log.println("Can't read the file for pid info: " +
                            logFileString + "pid");
                    }
                
                
                    // Get any Warnings on connections and print to event file
                    for(int i=0; i < currConnections; i++) {
                    try {
                        currSQLWarning = connPool[i].getWarnings();
                        if(currSQLWarning != null) {
                        log.println("Warnings on connection " +
                                String.valueOf(i) + " " + currSQLWarning);
                        connPool[i].clearWarnings();
                        }
                    } catch(SQLException e) {
                        log.println("Cannot access Warnings: " + e);
                    }
                    
                    }

                    for(int i=0; i < currConnections; i++) { // Do for each connection
                    long age = System.currentTimeMillis() - connCreateDate[i];
                    
                    
                    synchronized(connStatus) {
                        if(connStatus[i] > 0) { // In use, catch it next time!
                        continue;
                        }
                        connStatus[i] = 2; // Take offline (2 indicates housekeeping lock)
                    }
                    
                    try {  // Test the connection with createStatement call
                        if(age > maxConnMSec) {  // Force a reset at the max conn time
                        throw new SQLException();
                        }
                    
                        stmt = connPool[i].createStatement();
                        connStatus[i] = 0;  // Connection is O.K.
                        //log.println("Connection confirmed for conn = " +
                        //             String.valueOf(i));
                    
                        // Some DBs return an object even if DB is shut down
                        if(connPool[i].isClosed()) {
                        throw new SQLException();
                        }

                    
                        // Connection has a problem, restart it
                    } catch(SQLException e) {
                        try {
                        log.println(new Date().toString() +
                                " ***** Recycling connection " +
                                String.valueOf(i) + ":");
                        
                        connPool[i].close();
                        createConn(i);
                        
                        } catch(SQLException e1) {
                        log.println("Failed: " + e1);
                        connStatus[i] = 0;  // Can't open, try again next time
                        }
                    } finally {
                        try{if(stmt != null) {stmt.close();}} catch(SQLException e1){};
                    }
                    
                    }
                
                    try { Thread.sleep(20000); }  // Wait 20 seconds for next cycle
                
                    catch(InterruptedException e) {
                    // Returning from the run method sets the internal
                    // flag referenced by Thread.isAlive() to false.
                    // This is required because we don't use stop() to
                    // shutdown this thread.
                    return;
                    }
                
                    }

                } // End run
            }// End class
