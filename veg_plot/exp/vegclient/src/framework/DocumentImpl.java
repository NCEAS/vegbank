/**
 *  '$RCSfile: DocumentImpl.java,v $'
 *    Purpose: A Class that represents an XML document
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Matt Jones
 *    Release: @release@
 *
 *   '$Author: harris $'
 *     '$Date: 2001-10-10 18:12:41 $'
 * '$Revision: 1.1 $'
 */

package edu.ucsb.nceas.metacat;

import java.sql.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.InputStreamReader;

import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Enumeration;

import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.net.URL;

import vegclient.framework.*;

/**
 * A class that represents an XML document. It can be created with a simple
 * document identifier from a database connection.  It also will write an
 * XML text document to a database connection using SAX.
 */
public class DocumentImpl {

  static final int ALL = 1;
  static final int WRITE = 2;
  static final int READ = 4;

  private Connection conn = null;
  private String docid = null;
  private String docname = null;
  private String doctype = null;
  private String doctitle = null;
  private String createdate = null;
  private String updatedate = null;
  private String system_id = null;
  private String userowner = null;
  private String userupdated = null;
  private int rev;
  private int serverlocation;
  private int publicaccess; 
  private long rootnodeid;
  private ElementNode rootNode = null;
  private TreeSet nodeRecordList = null;

  /**
   * Constructor, creates document from database connection, used 
   * for reading the document
   *
   * @param conn the database connection from which to read the document
   * @param docid the identifier of the document to be created
   */
  public DocumentImpl(Connection conn, String docid) throws McdbException 
  {
    try { 
      this.conn = conn;
      this.docid = docid;
      
      DocumentIdentifier id = new DocumentIdentifier(docid);
      
  
      // Look up the document information
      getDocumentInfo(docid);
      
      // Download all of the document nodes using a single SQL query
      // The sort order of the records is determined by the NodeComparator
      // class, and needs to represent a depth-first traversal for the
      // toXml() method to work properly
      nodeRecordList = getNodeRecordList(rootnodeid);
  
    } catch (McdbException ex) {
      throw ex;
    } catch (Throwable t) {
      throw new McdbException("Error reading document " + docid + ".");
    }
  }

  /** 
   * Construct a new document instance, writing the contents to the database.
   * This method is called from DBSAXHandler because we need to know the
   * root element name for documents without a DOCTYPE before creating it.
   *
   * @param conn the JDBC Connection to which all information is written
   * @param rootnodeid - sequence id of the root node in the document
   * @param docname - the name of DTD, i.e. the name immediately following 
   *        the DOCTYPE keyword ( should be the root element name ) or
   *        the root element name if no DOCTYPE declaration provided
   *        (Oracle's and IBM parsers are not aware if it is not the 
   *        root element name)
   * @param doctype - Public ID of the DTD, i.e. the name immediately 
   *                  following the PUBLIC keyword in DOCTYPE declaration or
   *                  the docname if no Public ID provided or
   *                  null if no DOCTYPE declaration provided
   *
   */
  public DocumentImpl(Connection conn, long rootnodeid, String docname, 
                      String doctype, String docid, String action, String user,
                      int serverCode)
                      throws SQLException, Exception
  {
    this.conn = conn;
    this.rootnodeid = rootnodeid;
    this.docname = docname;
    this.doctype = doctype;
    this.docid = docid;
    writeDocumentToDB(action, user, serverCode);
  }
  
  public DocumentImpl(Connection conn, long rootnodeid, String docname, 
                      String doctype, String docid, String action, String user)
                      throws SQLException, Exception
  {
    this.conn = conn;
    this.rootnodeid = rootnodeid;
    this.docname = docname;
    this.doctype = doctype;
    this.docid = docid;
    writeDocumentToDB(action, user);
  }

  /**
   * get the document name
   */
  public String getDocname() {
    return docname;
  }

  /**
   * get the document type (which is the PublicID)
   */
  public String getDoctype() {
    return doctype;
  }

  /**
   * get the system identifier
   */
  public String getSystemID() {
    return system_id;
  }

  /**
   * get the root node identifier
   */
  public long getRootNodeID() {
    return rootnodeid;
  }
  
  /**
   * get the creation date
   */
  public String getCreateDate() {
    return createdate;
  }
  
  /**
   * get the update date
   */
  public String getUpdateDate() {
    return updatedate;
  }

  /** 
   * Get the document identifier (docid)
   */
  public String getDocID() {
    return docid;
  }
  
  /**
   *get the document title
   */
  public String getDocTitle() {
    return doctitle;
  }
  
  public String getUserowner() {
    return userowner;
  }
  
  public String getUserupdated() {
    return userupdated;
  }
  
  public int getServerlocation() {
    return serverlocation;
  }
  
  public int getPublicaccess() {
    return publicaccess;
  }
  
  public int getRev() {
    return rev;
  }

  /**
   * Print a string representation of the XML document
   */
  public String toString()
  {
    StringWriter docwriter = new StringWriter();
    this.toXml(docwriter);
    String document = docwriter.toString();
    return document;
  }

  /**
   * Get a text representation of the XML document as a string
   * This older algorithm uses a recursive tree of Objects to represent the
   * nodes of the tree.  Each object is passed the data for the document 
   * and searches all of the document data to find its children nodes and
   * recursively build.  Thus, because each node reads the whole document,
   * this algorithm is extremely slow for larger documents, and the time
   * to completion is O(N^N) wrt the number of nodes.  See toXml() for a
   * better algorithm.
   */
  public String readUsingSlowAlgorithm()
  {
    StringBuffer doc = new StringBuffer();

    // Create the elements from the downloaded data in the TreeSet
    rootNode = new ElementNode(nodeRecordList, rootnodeid);

    // Append the resulting document to the StringBuffer and return it
    doc.append("<?xml version=\"1.0\"?>\n");
      
    if (docname != null) {
      if ((doctype != null) && (system_id != null)) {
        doc.append("<!DOCTYPE " + docname + " PUBLIC \"" + doctype + 
                   "\" \"" + system_id + "\">\n");
      } else {
        doc.append("<!DOCTYPE " + docname + ">\n");
      }
    }
    doc.append(rootNode.toString());
  
    return (doc.toString());
  }

  /**
   * Print a text representation of the XML document to a Writer
   *
   * @param pw the Writer to which we print the document
   */
  public void toXml(Writer pw)
  {
    PrintWriter out = null;
    if (pw instanceof PrintWriter) {
      out = (PrintWriter)pw;
    } else {
      out = new PrintWriter(pw);
    }

    MetaCatUtil util = new MetaCatUtil();
    
    Stack openElements = new Stack();
    boolean atRootElement = true;
    boolean previousNodeWasElement = false;

    // Step through all of the node records we were given
    Iterator it = nodeRecordList.iterator();
    while (it.hasNext()) {
      NodeRecord currentNode = (NodeRecord)it.next();
      //util.debugMessage("[Got Node ID: " + currentNode.nodeid +
                          //" (" + currentNode.parentnodeid +
                          //", " + currentNode.nodeindex + 
                          //", " + currentNode.nodetype + 
                          //", " + currentNode.nodename + 
                          //", " + currentNode.nodedata + ")]");

      // Print the end tag for the previous node if needed
      //
      // This is determined by inspecting the parent nodeid for the
      // currentNode.  If it is the same as the nodeid of the last element
      // that was pushed onto the stack, then we are still in that previous
      // parent element, and we do nothing.  However, if it differs, then we
      // have returned to a level above the previous parent, so we go into
      // a loop and pop off nodes and print out their end tags until we get
      // the node on the stack to match the currentNode parentnodeid
      //
      // So, this of course means that we rely on the list of elements
      // having been sorted in a depth first traversal of the nodes, which
      // is handled by the NodeComparator class used by the TreeSet
      if (!atRootElement) {
        NodeRecord currentElement = (NodeRecord)openElements.peek();
        if ( currentNode.parentnodeid != currentElement.nodeid ) {
          while ( currentNode.parentnodeid != currentElement.nodeid ) {
            currentElement = (NodeRecord)openElements.pop();
            util.debugMessage("\n POPPED: " + currentElement.nodename);
            if (previousNodeWasElement) {
              out.print(">");
              previousNodeWasElement = false;
            }  
            out.print("</" + currentElement.nodename + ">" );
            currentElement = (NodeRecord)openElements.peek();
          }
        }
      }

      // Handle the DOCUMENT node
      if (currentNode.nodetype.equals("DOCUMENT")) {
        out.println("<?xml version=\"1.0\"?>");
      
        if (docname != null) {
          if ((doctype != null) && (system_id != null)) {
            out.println("<!DOCTYPE " + docname + " PUBLIC \"" + doctype + 
                       "\" \"" + system_id + "\">");
          } else {
            out.println("<!DOCTYPE " + docname + ">");
          }
        }

      // Handle the ELEMENT nodes
      } else if (currentNode.nodetype.equals("ELEMENT")) {
        if (atRootElement) {
          atRootElement = false;
        } else {
          if (previousNodeWasElement) {
            out.print(">");
          }
        }
        openElements.push(currentNode);
        util.debugMessage("\n PUSHED: " + currentNode.nodename);
        previousNodeWasElement = true;
        out.print("<" + currentNode.nodename);

      // Handle the ATTRIBUTE nodes
      } else if (currentNode.nodetype.equals("ATTRIBUTE")) {
        out.print(" " + currentNode.nodename + "=\""
                 + currentNode.nodedata + "\"");
      } else if (currentNode.nodetype.equals("TEXT")) {
        if (previousNodeWasElement) {
          out.print(">");
        }
        out.print(currentNode.nodedata);
        previousNodeWasElement = false;

      // Handle the COMMENT nodes
      } else if (currentNode.nodetype.equals("COMMENT")) {
        if (previousNodeWasElement) {
          out.print(">");
        }
        out.print("<!--" + currentNode.nodedata + "-->");
        previousNodeWasElement = false;

      // Handle the PI nodes
      } else if (currentNode.nodetype.equals("PI")) {
        if (previousNodeWasElement) {
          out.print(">");
        }
        out.print("<?" + currentNode.nodename + " " +
                        currentNode.nodedata + "?>");
        previousNodeWasElement = false;

      // Handle any other node type (do nothing)
      } else {
        // Any other types of nodes are not handled.
        // Probably should throw an exception here to indicate this
      }
      out.flush();
    }

    // Print the final end tag for the root element
    NodeRecord currentElement = (NodeRecord)openElements.pop();
    util.debugMessage("\n POPPED: " + currentElement.nodename);
    out.print("</" + currentElement.nodename + ">" );
    out.flush();
  }
  
  private boolean isRevisionOnly(DocumentIdentifier docid) throws Exception
  {
    //System.out.println("inRevisionOnly");
    PreparedStatement pstmt;
    String rev = docid.getRev();
    String newid = docid.getIdentifier();
    pstmt = conn.prepareStatement("select rev from xml_documents " +
                                  "where docid like '" + newid + "'");
    pstmt.execute();
    ResultSet rs = pstmt.getResultSet();
    boolean tablehasrows = rs.next();
    if(rev.equals("newest") || rev.equals("all"))
    {
      return false;
    }
    
    if(tablehasrows)
    {
      int r = rs.getInt(1);
      if(new Integer(rev).intValue() == r)
      { //the current revision in in xml_documents
        //System.out.println("returning false");
        return false;
      }
      else if(new Integer(rev).intValue() < r)
      { //the current revision is in xml_revisions.
        //System.out.println("returning true");
        return true;
      }
      else if(new Integer(rev).intValue() > r)
      { //error, rev cannot be greater than r
        throw new Exception("requested revision cannot be greater than " +
                            "the latest revision number.");
      }
    }
    throw new Exception("the requested docid '" + docid.toString() + 
                        "' does not exist");
  }

  private void getDocumentInfo(String docid) throws McdbException, 
                                                    AccessionNumberException
  {
    getDocumentInfo(new DocumentIdentifier(docid));
  }
  
  /**
   * Look up the document type information from the database
   *
   * @param docid the id of the document to look up
   */
  private void getDocumentInfo(DocumentIdentifier docid) throws McdbException 
  {
    PreparedStatement pstmt;
    String table = "xml_documents";
    
    try
    {
      if(isRevisionOnly(docid))
      { //pull the document from xml_revisions instead of from xml_documents;
        table = "xml_revisions";
      }
    }
    catch(Exception e)
    {
      System.out.println("error in getDocumentInfo: " + e.getMessage());
    }
    
    //deal with the key words here.
    
    if(docid.getRev().equals("all"))
    {
      
    }
    
    try {
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT docname, doctype, rootnodeid,doctitle, ");
      sql.append("date_created, date_updated, ");
      sql.append("user_owner, user_updated, server_location, ");
      sql.append("rev FROM ").append(table);
      sql.append(" WHERE docid LIKE '").append(docid.getIdentifier());
      sql.append("' and rev like '").append(docid.getRev()).append("'");
      //System.out.println(sql.toString());
      pstmt =
        conn.prepareStatement(sql.toString());
      // Bind the values to the query
      //pstmt.setString(1, docid.getIdentifier());
      //pstmt.setString(2, docid.getRev());

      pstmt.execute();
      ResultSet rs = pstmt.getResultSet();
      boolean tableHasRows = rs.next();
      if (tableHasRows) {
        this.docname        = rs.getString(1);
        this.doctype        = rs.getString(2);
        this.rootnodeid     = rs.getLong(3);
        this.doctitle       = rs.getString(4);
        this.createdate     = rs.getString(5);
        this.updatedate     = rs.getString(6);
        this.userowner      = rs.getString(7);
        this.userupdated    = rs.getString(8);
        this.serverlocation = rs.getInt(9);
        //this.publicaccess   = rs.getInt(10);
        this.rev            = rs.getInt(10);
      } 
      pstmt.close();

      if (this.doctype != null) {
        pstmt =
          conn.prepareStatement("SELECT system_id " +
                                  "FROM xml_catalog " +
                                 "WHERE public_id LIKE ?");
        // Bind the values to the query
        pstmt.setString(1, doctype);
  
        pstmt.execute();
        rs = pstmt.getResultSet();
        tableHasRows = rs.next();
        if (tableHasRows) {
          this.system_id  = rs.getString(1);
        } 
        pstmt.close();
      }
    } catch (SQLException e) {
      System.out.println("error in getDocumentInfo: " + e.getMessage());
      e.printStackTrace(System.out);
      throw new McdbException("Error accessing database connection.", e);
    }

    if (this.docname == null) {
      throw new McdbDocNotFoundException("Document not found: " + docid);
    }
  }

  /**
   * Look up the node data from the database
   *
   * @param rootnodeid the id of the root node of the node tree to look up
   */
  private TreeSet getNodeRecordList(long rootnodeid) throws McdbException 
  {
    PreparedStatement pstmt;
    TreeSet nodeRecordList = new TreeSet(new NodeComparator());
    long nodeid = 0;
    long parentnodeid = 0;
    long nodeindex = 0;
    String nodetype = null;
    String nodename = null;
    String nodedata = null;

    try {
      pstmt =
      conn.prepareStatement("SELECT nodeid,parentnodeid,nodeindex, " +
           "nodetype,nodename,"+               
           "replace(" +
           "replace(" +
           "replace(nodedata,'&','&amp;') " +
           ",'<','&lt;') " +
           ",'>','&gt;') " +
           "FROM xml_nodes WHERE rootnodeid = ?");

      // Bind the values to the query
      pstmt.setLong(1, rootnodeid);

      pstmt.execute();
      ResultSet rs = pstmt.getResultSet();
      boolean tableHasRows = rs.next();
      while (tableHasRows) {
        nodeid = rs.getLong(1);
        parentnodeid = rs.getLong(2);
        nodeindex = rs.getLong(3);
        nodetype = rs.getString(4);
        nodename = rs.getString(5);
        nodedata = rs.getString(6);

        // add the data to the node record list hashtable
        NodeRecord currentRecord = new NodeRecord(nodeid, parentnodeid, 
                                   nodeindex, nodetype, nodename, nodedata);
        nodeRecordList.add(currentRecord);

        // Advance to the next node
        tableHasRows = rs.next();
      } 
      pstmt.close();

    } catch (SQLException e) {
      throw new McdbException("Error accessing database connection.", e);
    }

    if (nodeRecordList != null) {
      return nodeRecordList;
    } else {
      throw new McdbException("Error getting node data: " + docid);
    }
  }
  
  /** creates SQL code and inserts new document into DB connection 
   default serverCode of 1*/
  private void writeDocumentToDB(String action, String user)
               throws SQLException, Exception
  {
    writeDocumentToDB(action, user, 1);
  }

 /** creates SQL code and inserts new document into DB connection */
  private void writeDocumentToDB(String action, String user, int serverCode) 
               throws SQLException, Exception {
    try {
      PreparedStatement pstmt = null;

      if (action.equals("INSERT")) {
        //AccessionNumber ac = new AccessionNumber();
        //this.docid = ac.generate(docid, "INSERT");
        pstmt = conn.prepareStatement(
            "INSERT INTO xml_documents " +
            "(docid, rootnodeid, docname, doctype, user_owner, " +
            "user_updated, date_created, date_updated, server_location) " +
            "VALUES (?, ?, ?, ?, ?, ?, sysdate, sysdate, ?)");
        //note that the server_location is set to 1. 
        //this means that "localhost" in the xml_replication table must
        //always be the first entry!!!!!
        
        // Bind the values to the query
        pstmt.setString(1, this.docid);
        pstmt.setLong(2, rootnodeid);
        pstmt.setString(3, docname);
        pstmt.setString(4, doctype);
        pstmt.setString(5, user);
        pstmt.setString(6, user);
        pstmt.setInt(7, serverCode);
      } else if (action.equals("UPDATE")) {

        // Save the old document entry in a backup table
        DocumentImpl.archiveDocRevision( conn, docid, user );
        DocumentImpl thisdoc = new DocumentImpl(conn, docid);
        int thisrev = thisdoc.getRev();
        thisrev++;
        // Delete index for the old version of docid
        // The new index is inserting on the next calls to DBSAXNode
        pstmt = conn.prepareStatement(
                "DELETE FROM xml_index WHERE docid='" + this.docid + "'");
        pstmt.execute();
        pstmt.close();

        // Update the new document to reflect the new node tree
        pstmt = conn.prepareStatement(
            "UPDATE xml_documents " +
            "SET rootnodeid = ?, docname = ?, doctype = ?, " +
            "user_updated = ?, date_updated = sysdate, " +
            "server_location = ?, rev = ? WHERE docid LIKE ?");
        // Bind the values to the query
        pstmt.setLong(1, rootnodeid);
        pstmt.setString(2, docname);
        pstmt.setString(3, doctype);
        pstmt.setString(4, user);
        pstmt.setInt(5, serverCode);
        pstmt.setInt(6, thisrev);
        pstmt.setString(7, this.docid);

      } else {
        System.err.println("Action not supported: " + action);
      }

      // Do the insertion
      pstmt.execute();
      pstmt.close();

    } catch (SQLException sqle) {
      throw sqle;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Get the document title
   */
  public String getTitle() {
    return doctitle;
  }

  /**
   * Set the document title
   *
   * @param title the new title for the document
   */

  public void setTitle( String title ) {
    this.doctitle = title;
    try {
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement(
            "UPDATE xml_documents " +
            " SET doctitle = ? " +
            "WHERE docid = ?");

      // Bind the values to the query
      pstmt.setString(1, doctitle);
      pstmt.setString(2, docid);

      // Do the insertion
      pstmt.execute();
      pstmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Write an XML file to the database, given a filename
   *
   * @param conn the JDBC connection to the database
   * @param filename the filename to be loaded into the database
   * @param action the action to be performed (INSERT OR UPDATE)
   * @param docid the docid to use for the INSERT OR UPDATE
   */
  public static String write(Connection conn,String filename,
                             String aclfilename,String dtdfilename,
                             String action, String docid, String user,
                             String group )
                throws Exception {
                  
    Reader acl = null;
    if ( aclfilename != null ) {
      acl = new FileReader(new File(aclfilename).toString());
    }
    Reader dtd = null;
    if ( dtdfilename != null ) {
      dtd = new FileReader(new File(dtdfilename).toString());
    }
    return write ( conn, new FileReader(new File(filename).toString()),
                   acl, dtd, action, docid, user, group);
  }

  public static String write(Connection conn,Reader xml,Reader acl,Reader dtd,
                             String action, String docid, String user,
                             String group )
                throws Exception {
    return write ( conn, xml, acl, dtd, action, docid, user, group, 1, false);
  }

  public static String write(Connection conn,Reader xml,Reader acl,
                             String action, String docid, String user,
                             String group )
                throws Exception {
    if(action.equals("UPDATE"))
    {//if the document is being updated then use the servercode from the 
     //originally inserted document.
      DocumentImpl doc = new DocumentImpl(conn, docid);
      int servercode = doc.getServerlocation();
      return write(conn,xml,acl,action,docid,user,group,servercode);
    }
    else
    {//if the file is being inserted then the servercode is always 1
      return write(conn, xml, acl, action, docid, user, group, 1);
    }
  }
  
  public static String write( Connection conn, Reader xml,
                              String action, String docid, String user,
                              String group, int serverCode )
                throws Exception
  {
    return write(conn,xml,null,action,docid,user,group,serverCode);
  }
  
  public static String write( Connection conn,Reader xml,Reader acl,
                              String action, String docid, String user,
                              String group, int serverCode) 
                throws Exception
  {
    return write(conn,xml,acl,null,action,docid,user,group,serverCode,false);
  }
  
  public static String write( Connection conn,Reader xml,Reader acl,
                              String action, String docid, String user,
                              String group, int serverCode, boolean override)
                throws Exception
  {
    return write(conn,xml,acl,null,action,docid,user,group,serverCode,override);
  }
  
  /**
   * Write an XML file to the database, given a Reader
   *
   * @param conn the JDBC connection to the database
   * @param xml the xml stream to be loaded into the database
   * @param action the action to be performed (INSERT OR UPDATE)
   * @param docid the docid to use for the INSERT OR UPDATE
   * @param user the user that owns the document
   * @param group the group to which user belongs
   * @param serverCode the serverid from xml_replication on which this document
   *        resides.
   * @param override flag to stop insert replication checking.
   *        if override = true then a document not belonging to the local server
   *        will not be checked upon update for a file lock.
   *        if override = false then a document not from this server, upon 
   *        update will be locked and version checked.
   */

  public static String write( Connection conn,Reader xml,Reader acl,Reader dtd,
                              String action, String docid, String user,
                              String group, int serverCode, boolean override)
                throws Exception
  {
    //MOVED DOWN IN RelationHandler obj before write of relations
    //if(action.equals("UPDATE"))
    //{
    //  RelationHandler.deleteRelations(docid);
    //}
    
    int updaterev;
    MetaCatUtil util = new MetaCatUtil();
        // Determine if the docid is OK for INSERT or UPDATE
    AccessionNumber ac = new AccessionNumber(conn);
    String newdocid = ac.generate(docid, action);
    
    MetaCatUtil.debugMessage("action: " + action + " servercode: " + 
                             serverCode + " override: " + override);
                        
    if((serverCode != 1 && action.equals("UPDATE")) && !override)
    { //if this document being written is not a resident of this server then
      //we need to try to get a lock from it's resident server.  If the
      //resident server will not give a lock then we send the user a message
      //saying that he/she needs to download a new copy of the file and
      //merge the differences manually.
      int istreamInt; 
      char istreamChar;
      DocumentImpl newdoc = new DocumentImpl(conn, docid);
      updaterev = newdoc.getRev();
      String server = MetacatReplication.getServer(serverCode);
      MetacatReplication.replLog("attempting to lock " + docid);
      URL u = new URL("http://" + server + "?action=getlock&updaterev=" + 
                      updaterev + "&docid=" + docid);
      System.out.println("sending message: " + u.toString());
      String serverResStr = MetacatReplication.getURLContent(u);
      String openingtag = serverResStr.substring(0, serverResStr.indexOf(">")+1);
      
      if(openingtag.equals("<lockgranted>"))
      {//the lock was granted go ahead with the insert
        try 
        {
          MetacatReplication.replLog("lock granted for " + docid + " from " +
                                      server);
          XMLReader parser = initializeParser(conn, action, newdocid, 
                                              user, group, serverCode, dtd);
          conn.setAutoCommit(false);
          parser.parse(new InputSource(xml));
          conn.commit();
          conn.setAutoCommit(true);
        } 
        catch (Exception e) 
        {
          conn.rollback();
          conn.setAutoCommit(true);
          throw e;
        }
                
        //after inserting the document locally, tell the document's home server
        //to come get a copy from here.
        ForceReplicationHandler frh = new ForceReplicationHandler(docid);
        
        if ( (docid != null) && !(newdocid.equals(docid)) ) 
        {
          return new String("New document ID generated:" + newdocid);
        } 
        else 
        {
          return newdocid;
        }
      }
      else if(openingtag.equals("<filelocked>"))
      {//the file is currently locked by another user
       //notify our user to wait a few minutes, check out a new copy and try
       //again.
        //System.out.println("file locked");
        MetacatReplication.replLog("lock denied for " + docid + " on " +
                                   server + " reason: file already locked");
        throw new Exception("The file specified is already locked by another " +
                            "user.  Please wait 30 seconds, checkout the " +
                            "newer document, merge your changes and try " +
                            "again.");
      }
      else if(openingtag.equals("<outdatedfile>"))
      {//our file is outdated.  notify our user to check out a new copy of the
       //file and merge his version with the new version.
        //System.out.println("outdated file");
        MetacatReplication.replLog("lock denied for " + docid + " on " +
                                    server + " reason: local file outdated");
        throw new Exception("The file you are trying to update is an outdated" +
                            " version.  Please checkout the newest document, " +
                            "merge your changes and try again.");
      }
    }
    
    if ( action.equals("UPDATE") ) {
      // check for 'write' permission for 'user' to update this document

      if ( !hasPermission(conn, user, group, docid) ) {
        throw new Exception("User " + user + 
              " does not have permission to update XML Document #" + docid);
      }          
    }

    try 
    { 
      XMLReader parser = initializeParser(conn, action, newdocid, 
                                          user, group, serverCode, dtd);
      conn.setAutoCommit(false);
      parser.parse(new InputSource(xml));
      conn.commit();
      conn.setAutoCommit(true);
    } 
    catch (Exception e) 
    {
      conn.rollback();
      conn.setAutoCommit(true);
      throw e;
    }
    
    //force replicate out the new document to each server in our server list.
    if(serverCode == 1)
    { //start the thread to replicate this new document out to the other servers
      ForceReplicationHandler frh = new ForceReplicationHandler(newdocid, 
                                                                action);
    }
      
    if ( (docid != null) && !(newdocid.equals(docid)) ) 
    {
      return new String("New document ID generated:" + newdocid);
    } 
    else 
    {
      return newdocid;
    }
  }

  /**
   * Delete an XML file from the database (actually, just make it a revision
   * in the xml_revisions table)
   *
   * @param docid the ID of the document to be deleted from the database
   */
  public static void delete( Connection conn, String docid,
                                 String user, String group )
                throws Exception {
    DocumentIdentifier id = new DocumentIdentifier(docid);
    docid = id.getSiteCode() + id.getSeparator() + id.getUniqueId();
    
    // Determine if the docid is OK for DELETE
    AccessionNumber ac = new AccessionNumber(conn);
    String newdocid = ac.generate(docid, "DELETE");

    // check for 'write' permission for 'user' to delete this document
    if ( !hasPermission(conn, user, group, docid) ) {
      throw new Exception("User " + user + 
              " does not have permission to delete XML Document #" + docid);
    }

    conn.setAutoCommit(false);
    // Copy the record to the xml_revisions table
    DocumentImpl.archiveDocRevision( conn, docid, user );

    // Now delete it from the xml_documents table
    
    Statement stmt = conn.createStatement();
    stmt.execute("DELETE FROM xml_index WHERE docid = '" + docid + "'");
    stmt.execute("DELETE FROM xml_documents WHERE docid = '" + docid + "'");
    stmt.execute("DELETE FROM xml_access WHERE docid = '" + docid + "'");
    stmt.execute("DELETE FROM xml_access WHERE accessfileid = '" + docid + "'");
    stmt.execute("DELETE FROM xml_relation WHERE docid = '" + docid + "'");
    stmt.close();
    conn.commit();
    conn.setAutoCommit(true);
    //IF this is a package document:
    //delete all of the relations that this document created.
    //if the deleted document is a package document its relations should 
    //no longer be active if it has been deleted from the system.
    
//    MOVED UP IN THE TRANSACTION
//    RelationHandler.deleteRelations(docid);
  }

  /** 
    * Check for "WRITE" permission on @docid for @user and/or @group 
    * from DB connection 
    */
  private static boolean hasPermission( Connection conn, String user,
                                        String group, String docid) 
                         throws SQLException 
  {
    // b' of the command line invocation
    if ( (user == null) && (group == null) ) {
      return true;
    }

    // Check for WRITE permission on @docid for @user and/or @group
    AccessControlList aclobj = new AccessControlList(conn);
    boolean hasPermission = aclobj.hasPermission("WRITE",user,docid);
    if ( !hasPermission && group != null ) {
      hasPermission = aclobj.hasPermission("WRITE",group,docid);
    }
    
    return hasPermission;
  }

  /**
   * Set up the parser handlers for writing the document to the database
   */
  private static XMLReader initializeParser(Connection conn, String action,
                                   String docid, String user, String group,
                                   int serverCode, Reader dtd) 
                           throws Exception 
  {
    XMLReader parser = null;
    //
    // Set up the SAX document handlers for parsing
    //
    try {
      ContentHandler chandler = new DBSAXHandler(conn, action, docid,
                                                 user, group, serverCode);
      EntityResolver eresolver= new DBEntityResolver(conn,
                                                 (DBSAXHandler)chandler, dtd);
      DTDHandler dtdhandler   = new DBDTDHandler(conn);

      // Get an instance of the parser
      MetaCatUtil util = new MetaCatUtil();
      String parserName = util.getOption("saxparser");
      parser = XMLReaderFactory.createXMLReader(parserName);

      // Turn off validation
      parser.setFeature("http://xml.org/sax/features/validation", false);
      
      // Set Handlers in the parser
      parser.setProperty("http://xml.org/sax/properties/declaration-handler",
                         chandler);
      parser.setProperty("http://xml.org/sax/properties/lexical-handler",
                         chandler);
      parser.setContentHandler((ContentHandler)chandler);
      parser.setEntityResolver((EntityResolver)eresolver);
      parser.setDTDHandler((DTDHandler)dtdhandler);
      parser.setErrorHandler((ErrorHandler)chandler);

    } catch (Exception e) {
      throw e;
    }

    return parser;
  }

  /** Save a document entry in the xml_revisions table */
  private static void archiveDocRevision(Connection conn, String docid,
                                         String user) 
                                         throws SQLException {
    // create a record in xml_revisions table 
    // for that document as selected from xml_documents
    PreparedStatement pstmt = conn.prepareStatement(
      "INSERT INTO xml_revisions " +
        "(revisionid, docid, rootnodeid, docname, doctype, doctitle, " +
        "user_owner, user_updated, date_created, date_updated, server_location, " +
        "rev)" +
      "SELECT null, ?, rootnodeid, docname, doctype, doctitle," + 
        "user_owner, ?, sysdate, sysdate, server_location, rev "+
      "FROM xml_documents " +
      "WHERE docid = ?");
    // Bind the values to the query and execute it
    pstmt.setString(1, docid);
    pstmt.setString(2, user);
    pstmt.setString(3, docid);
    pstmt.execute();
    pstmt.close();

  }

  /**
   * the main routine used to test the DBWriter utility.
   * <p>
   * Usage: java DocumentImpl <-f filename -a action -d docid>
   *
   * @param filename the filename to be loaded into the database
   * @param action the action to perform (READ, INSERT, UPDATE, DELETE)
   * @param docid the id of the document to process
   */
  static public void main(String[] args) {
     
    try {
      String filename    = null;
      String aclfilename = null;
      String dtdfilename = null;
      String action      = null;
      String docid       = null;
      boolean showRuntime = false;
      boolean useOldReadAlgorithm = false;

      // Parse the command line arguments
      for ( int i=0 ; i < args.length; ++i ) {
        if ( args[i].equals( "-f" ) ) {
          filename =  args[++i];
        } else if ( args[i].equals( "-c" ) ) {
          aclfilename =  args[++i];
        } else if ( args[i].equals( "-r" ) ) {
          dtdfilename =  args[++i];
        } else if ( args[i].equals( "-a" ) ) {
          action =  args[++i];
        } else if ( args[i].equals( "-d" ) ) {
          docid =  args[++i];
        } else if ( args[i].equals( "-t" ) ) {
          showRuntime = true;
        } else if ( args[i].equals( "-old" ) ) {
          useOldReadAlgorithm = true;
        } else {
          System.err.println
            ( "   args[" +i+ "] '" +args[i]+ "' ignored." );
        }
      }
      
      // Check if the required arguments are provided
      boolean argsAreValid = false;
      if (action != null) {
        if (action.equals("INSERT")) {
          if (filename != null) {
            argsAreValid = true;
          } 
        } else if (action.equals("UPDATE")) {
          if ((filename != null) && (docid != null)) {
            argsAreValid = true;
          } 
        } else if (action.equals("DELETE")) {
          if (docid != null) {
            argsAreValid = true;
          } 
        } else if (action.equals("READ")) {
          if (docid != null) {
            argsAreValid = true;
          } 
        } 
      } 

      // Print usage message if the arguments are not valid
      if (!argsAreValid) {
        System.err.println("Wrong number of arguments!!!");
        System.err.println(
          "USAGE: java DocumentImpl [-t] <-a INSERT> [-d docid] <-f filename> "+
          "[-c aclfilename] [-r dtdfilename]");
        System.err.println(
          "   OR: java DocumentImpl [-t] <-a UPDATE -d docid -f filename> " +
          "[-c aclfilename] [-r dtdfilename]");
        System.err.println(
          "   OR: java DocumentImpl [-t] <-a DELETE -d docid>");
        System.err.println(
          "   OR: java DocumentImpl [-t] [-old] <-a READ -d docid>");
        return;
      }
      
      // Time the request if asked for
      double startTime = System.currentTimeMillis();
      
      // Open a connection to the database
      MetaCatUtil util = new MetaCatUtil();
      Connection dbconn = util.openDBConnection();

      double connTime = System.currentTimeMillis();
      // Execute the action requested (READ, INSERT, UPDATE, DELETE)
      if (action.equals("READ")) {
          DocumentImpl xmldoc = new DocumentImpl( dbconn, docid );
          if (useOldReadAlgorithm) {
            System.out.println(xmldoc.readUsingSlowAlgorithm());
          } else {
            xmldoc.toXml(new PrintWriter(System.out));
          }
      } else if (action.equals("DELETE")) {
        DocumentImpl.delete(dbconn, docid, null, null);
        System.out.println("Document deleted: " + docid);
      } else {
        String newdocid = DocumentImpl.write(dbconn, filename, aclfilename,
                                             dtdfilename, action, docid,
                                             null, null);
        if ((docid != null) && (!docid.equals(newdocid))) {
          if (action.equals("INSERT")) {
            System.out.println("New document ID generated!!! ");
          } else if (action.equals("UPDATE")) {
            System.out.println("ERROR: Couldn't update document!!! ");
          }
        } else if ((docid == null) && (action.equals("UPDATE"))) {
          System.out.println("ERROR: Couldn't update document!!! ");
        }
        System.out.println("Document processing finished for: " + filename
              + " (" + newdocid + ")");
      }

      double stopTime = System.currentTimeMillis();
      double dbOpenTime = (connTime - startTime)/1000;
      double insertTime = (stopTime - connTime)/1000;
      double executionTime = (stopTime - startTime)/1000;
      if (showRuntime) {
        System.out.println("\n\nTotal Execution time was: " + 
                           executionTime + " seconds.");
        System.out.println("Time to open DB connection was: " + dbOpenTime + 
                           " seconds.");
        System.out.println("Time to insert document was: " + insertTime +
                           " seconds.");
      }
      dbconn.close();
    } catch (McdbException me) {
      me.toXml(new PrintWriter(System.err));
    } catch (AccessionNumberException ane) {
      System.out.println("ERROR: Couldn't delete document!!! ");
      System.out.println(ane.getMessage());
    } catch (Exception e) {
      System.err.println("EXCEPTION HANDLING REQUIRED");
      System.err.println(e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
