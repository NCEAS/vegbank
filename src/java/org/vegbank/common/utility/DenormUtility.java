/*
 *	'$RCSfile: DenormUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-09-06 18:12:09 $'
 *	'$Revision: 1.7 $'
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
 
package org.vegbank.common.utility;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.DatabaseAccess;


/**
 * Populates denormalied fields in the DB.
 * 
 * @author anderson
 */
public class DenormUtility
{
	private Vector queuedTables = new Vector();
  private SortedTableVector stv = new SortedTableVector();
	protected static Log log = LogFactory.getLog(DenormUtility.class);
	private static ResourceBundle sqlStore =
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");
  private static Hashtable tableDenorms = null;


  /**
   * constructor
   */
  public DenormUtility() 
  {
    for (Enumeration e = sqlStore.getKeys(); e.hasMoreElements() ;) 
    { //sort the keys
      String key = (String)e.nextElement();
      SortedTableName stn = new SortedTableName(key);
      //use the stn to put the key, tablename and sortval into the stv
      stv.addElement(stn);
    } //now the keys are in a sorted list and the tables we're requested to
      //denorm can be compared to this list.
  }

  /**
   * 
   * @return number of records updated
   */
  public static long update(String update, String denormtype, String[] whereParams)
          throws SQLException {

      StringBuffer sql = new StringBuffer(128);
      if (Utility.isStringNullOrEmpty(denormtype)) { denormtype="null";  }

      log.debug("update: " + update + ", type=" + denormtype );
      String updateSQL = sqlStore.getString(update);
      String whereSQL = "  true ";
      if (denormtype.equals("all")) {
          // no where statement needed
          whereSQL = " true ";
      } else {
          // get the where statement from SQLStore
          whereSQL = sqlStore.getString(update + "_w_" + denormtype);
          if (whereSQL.toLowerCase().startsWith("where ")) {
              // strip leading "where "
              whereSQL = whereSQL.substring("where ".length());
          }

          if (whereSQL.indexOf("{0}") != -1 ) {
              //need to substitude wparams for {0}, etc
              MessageFormat format;
              format = new MessageFormat(whereSQL);
              //declare wparam as an array
              whereSQL = format.format(whereParams);
              log.debug("swapped wparams for {0} stuff: " + whereSQL );
          }
      }

      sql.append(updateSQL).append(" AND ").append(whereSQL);


      log.debug("running SQL: " + sql.toString());
      DatabaseAccess da = new DatabaseAccess();
      long results = da.issueUpdate(DatabaseUtility.removeSemicolons(sql.toString()));
      log.debug("SQL IS DONE.  Result:" + results );

      return results;
  }

  /**
   * Run all queries in sql store that begin with dnrm (and don't have _w_).
   */
  public static long updateAll() 
          throws SQLException {
      return updateAll(null);
  }

  /**
   * Run all queries in sql store that begin with dnrm (and don't have _w_)
   * for a given table (or null to do all tables).
   * @param null or table name
   */
  public static long updateAll(String table) 
          throws SQLException {
      if (tableDenorms == null) {
          init();
      }

      long count = 0;
      Vector v;
      if (table != null) {
          // update ONE table
          count = updateTable(table);
      } else {
          // update ALL tables
          Iterator tables = tableDenorms.keySet().iterator();
          while (tables.hasNext()) {
              count += updateTable((String)tables.next());
          }
      }
          
      return count;
  }
  
  /**
   * runs denorms on a table with a given denormtype
   */
  public static long updateAll(String table, String denormtype)
    throws SQLException
  {
    return updateTable(table, denormtype, null);
  }

  /**
   * Runs all denorms for one table.
   */
  public static long updateTable(String table) throws SQLException {
      return updateTable(table, null, null);
  }

  /**
   * updates a table with the type and where params provided.
   */
  public static long updateTable(String table, String denormtype, String[] whereParams) 
          throws SQLException {

      if (tableDenorms == null) {
          init();
      }

      long count = 0;

      Vector v = (Vector)tableDenorms.get(table);
      if (v == null) { return 0; }

      Iterator it = v.iterator();
      while (it.hasNext()) {
          count += update((String)it.next(), denormtype, whereParams);
      }

      return count;
  }

  /**
   * init the denorm utility
   */
  private static void init() {
      tableDenorms = new Hashtable();
      Hashtable unsorted = new Hashtable();
      String table = null;
      Vector v;

  // load the entity list
  for (Enumeration e = sqlStore.getKeys(); e.hasMoreElements() ;) {
    String key = (String)e.nextElement();
    if (key.startsWith("dnrm-") && key.indexOf("_w_") == -1) {

              try {
                  StringTokenizer st = new StringTokenizer(key, "-");

                  if (st.hasMoreTokens()) { st.nextToken(); }
                  if (st.hasMoreTokens()) { table = st.nextToken(); }

          v = (Vector)unsorted.get(table);

                  if (v == null) { v = new Vector(); }
                  v.add(key);
                  log.debug("init: adding denorm for " + table);

          unsorted.put(table, v);

              }  catch (Exception ex) {
                  log.debug("Can't parse denorm key: " + key);
              }
    }

  }

      // sort them
      Iterator kit = unsorted.keySet().iterator();
      while (kit.hasNext()) {
          table = (String)kit.next();
          v = (Vector)unsorted.get(table);
          Collections.sort(v);
          tableDenorms.put(table, v);
      }

      log.debug("INIT COMPLETE");
  }
  
  /**
   * add all of the tables to the queue
   */
  public void queueAllTables()
  {
    Vector sort = stv.getSortedVector();
    for(int i=0; i<sort.size(); i++)
    {
      SortedTableName stn = (SortedTableName)sort.elementAt(i);
      queuedTables.addElement(stn.tableName);
    }
  }
   
  /**
   * add a table to the queue to be denormalized.  the 'queue' is a priority
   * queue based on the sequencing number in the sql name in sqlStore.properties
   */
  public void queueTable(String tableName)
  {
    queuedTables.addElement(tableName);
  }
  
  /**
   * denorm the tables that are currently in the queue in the order specified
   * in the sqlStore.properties file.
   */
  public void executeQueuedDenorms()
    throws SQLException
  {
    executeQueuedDenorms(null);
  }
  
  /**
   * denorm the tables that are currently in the queue in the order specified
   * in the sqlStore.properties file.  use the specified type of denorm
   */
  public void executeQueuedDenorms(String type)
    throws SQLException
  {
    //get the order from stv of the tables that we've been requested to denorm
    Vector sort = stv.getSortedVector();
    for(int i=0; i<sort.size(); i++)
    {
      SortedTableName stn = (SortedTableName)sort.elementAt(i);
      String sTableName = stn.tableName;
      for(int j=0; j<queuedTables.size(); j++)
      {
        String qTableName = (String)queuedTables.elementAt(j);
        if(qTableName.trim().equals(sTableName.trim()))
        { //denorm it.  this should be in order.
          log.debug("Denorming queued table: " + stn);
          //System.out.println("Denorming sorted table " + stn.toString());
          if(type == null)
          {
            DenormUtility.updateTable(qTableName);
          }
          else
          {
            DenormUtility.updateTable(qTableName, type, null);
          }
          queuedTables.remove(j); //remove the table from the queue once it is run
        }
      }
    }
  }
   
  
  /**
   * private class to contain a table name and a table sort val
   */
  private class SortedTableName
  {
    public String tableName;
    public int sortVal;
    public String key;
    
    public SortedTableName(String key)
    {
      if (key.startsWith("dnrm-") && key.indexOf("_w_") == -1) 
      {
        StringTokenizer st = new StringTokenizer(key, "-");
        st.nextToken(); //get rid of the denorm
        this.key = key;
        tableName = st.nextToken(); //get the table name
        sortVal = new Integer((String)st.nextToken()).intValue(); //get the sort value
      }
    }
    
    /**
     * returns string rep.
     */
    public String toString()
    {
      return "{" + sortVal + ", " + tableName + ", " + key + "}";
    }
  }
  
  /**
   * a class to automatically sort and store the table keys from sqlStore
   */
  private class SortedTableVector
  {
    private Vector sort;
    
    /**
     * constructor
     */
    public SortedTableVector()
    {
      sort = new Vector();
    }
    
    /**
     * add an element to the SortedTableVector
     */
    public void addElement(SortedTableName stn)
    {
      if(stn.tableName == null)
      {
        return; //don't add nulls
      }
      
      sort.addElement(stn);
      for(int i=sort.size()-1; i>0; i--)
      { //good ol' bubble sort
        SortedTableName cur = (SortedTableName)sort.elementAt(i);
        SortedTableName prev = (SortedTableName)sort.elementAt(i-1);
        if(cur.sortVal < prev.sortVal)
        {//move cur up
          sort.removeElementAt(i);
          sort.insertElementAt(cur, i-1);
        }
      }
    }
    
    /**
     * returns a vector of SortedTableNames sorted smaller to larger sortVals
     */
    public Vector getSortedVector()
    {
      return sort;
    }
  }
  
  /**
   * main method to call from a utility script to denorm all of the tables
   */
  public static void main(String[] args)
  {
    if(args.length != 1 && args.length != 2)
    {
      System.out.println("This utility denorms tables in vegbank.");
      System.out.println("usage: DenormUtility [tableName] [denormType]");
      System.out.println("tableName can be the name of a table or " +
        "'all' which will denorm all tables.");
      System.out.println("denormType can be any of the denorm types listed " + 
        "in the sqlStore.  Leave denormType null if you want all " + 
        "denorms to run.");
      System.out.println("If you use 'all' as the tableName, the denorm " +
        "process may take a while and you will get no feedback during " +
        "the execution.");
      System.exit(0);
    }
    
    String table = null;
    String type = null;
    
    if(args.length == 1)
    {
      table = args[0];
    }
    else if(args.length == 2)
    {
      table = args[0];
      type = args[1];
    }
    
    try
    {
      if(table.equals("all") && type != null)
      {
        System.out.println("denorming all tables with denorm type: " + type);
        DenormUtility dutil = new DenormUtility();
        dutil.queueAllTables();
        dutil.executeQueuedDenorms(type);
        System.out.println("updates made.");
      }
      else if(table.equals("all") && type == null)
      {
        System.out.println("Denorming all tables with default denorm type.");
        DenormUtility dutil = new DenormUtility();
        dutil.queueAllTables();
        dutil.executeQueuedDenorms();
        System.out.println("updates made.");
      }
      else if(!table.equals("all") && type == null)
      {
        System.out.println("Denorming table " + table + " with no denorm type.");
        long count = updateTable(table);
        System.out.println("Done updating table " + table + ".  " + count + 
          " updates made.");
      }
      else if(!table.equals("all") && type != null)
      {
        System.out.println("Denorming table " + table + " with type " + type);
        long count = updateTable(table, type, null);
        System.out.println("Done updating table " + table + ".  " + count + 
          " updates made.");
      }
      
    }
    catch(SQLException sqle)
    {
      System.out.println("Could not denorm: " + sqle.getMessage());
      sqle.printStackTrace();
      System.exit(1);
    }
    
    System.exit(0);
  }
}
