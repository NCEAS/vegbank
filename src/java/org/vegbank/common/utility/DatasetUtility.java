/*
 *	'$RCSfile: DatasetUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-09-07 23:27:10 $'
 *	'$Revision: 1.16 $'
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.AccessionGen;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.Constants;
import org.vegbank.common.model.Userdatasetitem;
import org.vegbank.common.model.Userdataset;
import org.vegbank.common.model.VBModelBean;



/**
 * Creates and manages datasets.
 *
 * @author anderson
 */
public class DatasetUtility
{

	public static final String TPL_SUCCESS = "dataload-success.vm";
	public static final String TPL_FAILURE = "dataload-failure.vm";
	public static final String TPL_ADMIN = "dataload-log.vm";
	public static final String TPL_LOG = "dataload-log.vm";
	public static final String DATACART_NAME = "datacart";
	public static final String DATACART_DESC = "datacart";
	public static final String SHARING_PRIVATE = "private";
	public static final String ITEM_DB = "vegbank";
	public static final String TYPE_NORMAL = "normal";
	public static final String TYPE_LOAD = "load";
	public static final String TYPE_DATACART = "datacart";
	public static final String DEF_DS_NAME = "unnamed dataset";
	public static final Long ANON_USR_ID = null;  // anon usr_id is null

	protected static Log log = LogFactory.getLog(DatasetUtility.class);


	private VBModelBeanToDB bean2db = null;
    private AccessionGen ag = null;
    private Userdataset curDataset = null;
    private boolean isDatacart = false;
    private Map newItemsToAdd = null;
    private Map newItemsToDrop = null;
	private DatabaseAccess da = null;


    /**
     *
     */
    public DatasetUtility() {
        try {
		    da = new DatabaseAccess();

            newItemsToAdd = new HashMap();
            newItemsToDrop = new HashMap();
        } catch(Exception vbex) {
            log.error("FATAL: problem initializing VBModelBeanToDB: " + vbex.getMessage());
        }
    }

    private void initBeanInserter() {
        try {
            if (bean2db == null) {
                this.bean2db = new VBModelBeanToDB();
            }

            if (bean2db.getDBConnection().isClosed()) {
                bean2db.initDB();
            }

            this.ag = new AccessionGen(bean2db.getDBConnection());
        } catch (Exception ex) {
            log.error("Problem initializing VBModelBeanToDB", ex);
        }
    }

    /**
     * Creates a new userdataset record optionally with its datasetitem children.
     * Does not update the DB.
     * @param beanList List of VBModelBean objects (could be Userdatasetitems) to store as
     * the items in this new dataset
     * @return new Userdataset instance
     */
    public Userdataset createDataset(List beanList, String dsName,
            String dsDescription, String dsType, String dsSharing, Long usrId)
            throws SQLException{

        if (Utility.isStringNullOrEmpty(dsSharing)) {
            dsSharing = SHARING_PRIVATE;
        }

        Userdataset dsBean = new Userdataset();
        dsBean.setDatasetname(dsName);
        dsBean.setDatasetdescription(dsDescription);
        dsBean.setDatasettype(dsType);
        dsBean.setDatasetsharing(dsSharing);
        if (usrId != null && usrId.longValue() != 0) {
            dsBean.setUsr_id(usrId.longValue());
        } else {
            log.debug("CREATING userdataset WITHOUT a usr_id (null)");
        }

        if (beanList != null) {
            log.debug("adding " + beanList.size() + " DSIs to the DS to be added");
            dsBean.setuserdataset_userdatasetitems(createDatasetItemList(beanList));
        }

        return dsBean;
    }


    /**
     * Inserts the given dataset into the DB.
     * Does not affect the curDataset.
     * @param beanList List of VBModelBean objects (could be Userdatasetitems) to store as
     * the items in this new dataset
     * @return accession code of new dataset or null
     */
    public AccessionCode insertDataset(List beanList, String dsName,
            String dsDescription, String dsType, String dsSharing, Long usrId)
            throws SQLException{

        log.debug("creating dataset - name:" + dsName + ", type:" + dsType + ", usr:" + usrId);
        Userdataset dsBean = createDataset(beanList, dsName, dsDescription,
                dsType, dsSharing, usrId);

        return insertDataset(dsBean);
    }


    /**
     * Inserts or updates the current dataset and its items in the DB.
     * @return number of items in dataset
     */
    public long saveDataset() throws SQLException {

        Set dropKeySet = newItemsToDrop.keySet();
        Iterator kit = dropKeySet.iterator();
        while (kit.hasNext()) {
            // remove drops from adds, if that could ever happen
            String str = (String)kit.next();
            //log.debug("removing from adds: " + str);
            newItemsToAdd.remove(str);
        }

        // handle new dataset
        Long dsId = getOrCreateCurrentDataset();

        // handle adds
        if (newItemsToAdd.size() > 0) {

            kit = newItemsToAdd.keySet().iterator();
            while (kit.hasNext()) {
                // new items are stored in a hash
                String key = (String)kit.next();
                Userdatasetitem dsi = (Userdatasetitem)newItemsToAdd.get(key);
                log.debug("inserting dsi: " + key);
                insertItem(dsi);
            }


            // PMA: why is this here?
            ///////////insertItem(null);
        }

        // handle drops: send the dataset ID and a list of ACs
        if (dropKeySet.size() > 0) {
            log.debug("dropping...");
            deleteItems(dsId, new ArrayList(dropKeySet));
        }

        // clean up
        newItemsToAdd = new HashMap();
        newItemsToDrop = new HashMap();

        return countItems(dsId);
    }


    /**
     * Inserts a new dataset along with all include dataset items.
     * @return accession code of new dataset
     */
    public AccessionCode insertDataset(Userdataset dsBean) throws SQLException {

        String newAC = null;
        try {
            initBeanInserter();

            List dsiList = dsBean.getuserdataset_userdatasetitems();

            log.debug("now inserting DS bean...");
            long dsPK = bean2db.insert(dsBean);
            log.debug("new dataset ID: " + dsPK);
            newAC = ag.buildAccession("userdataset", Long.toString(dsPK), dsBean.getDatasetname());
            bean2db.returnConnection();
            log.debug("inserted new userdataset with its children: " + newAC);

        } catch (Exception ex) {
            log.error("Exception while inserting dataset bean: ", ex);
            throw new SQLException("Problem inserting dataset bean: " + ex.toString());
        }

        return new AccessionCode(newAC);
    }


    /**
     * Inserts a new datasetitem.
     * @return new userdatasetitem_id
     */
    public long insertItem(Userdatasetitem dsi) {

        long dsiPK = -1;

        try {
            initBeanInserter();

            dsiPK = bean2db.insert(dsi);
            log.debug("inserted new userdatasetitem: " + dsiPK);
            bean2db.returnConnection();
        } catch (Exception ex) {
            log.debug("Exception while inserting dataset item: " + ex.toString());
        }

        return dsiPK;
    }


    /**
     * Deletes one datasetitem.
     * @param dsId the userdataset_id
     * @param itemAC an itemaccessioncodes as a String
     */
    public void deleteItem(Long dsId, String itemAC)
            throws SQLException {

        if (Utility.isStringNullOrEmpty(itemAC)) { return; }

        try {
            StringBuffer query = new StringBuffer(64)
                .append("DELETE FROM userdatasetitem WHERE userdataset_id='")
                .append(dsId)
                .append("' AND itemaccessioncode='").append(itemAC).append("'");

            //log.debug("deleting dataset item: " + query.toString());
            da.issueUpdate(DatabaseUtility.removeSemicolons(query.toString()));

        } catch (Exception ex) {
            log.error("Exception while deleting dataset item", ex);
            throw new SQLException("Problem deleting dataset item: " + ex.toString());
        }
    }


    /**
     * Deletes a group of datasetitems.
     * @param dsId the userdataset_id
     * @param itemACs a List of itemaccessioncodes as Strings
     */
    public void deleteItems(Long dsId, List itemACs)
            throws SQLException {

        log.debug("deleteItems");
        if (itemACs == null || itemACs.size() == 0) { return; }
        log.debug("deleteItems: " + itemACs.size());

        // make CSV with the ACs
        Iterator it = itemACs.iterator();
        boolean first = true;
        StringBuffer acCSV = new StringBuffer(itemACs.size()*32);
        while (it.hasNext()) {
            if (first) {
                first = false;
                acCSV.append("'");
            } else {
                acCSV.append("','");
            }

            acCSV.append((String)it.next());
        }
        acCSV.append("'");

        log.debug("acs: " + acCSV.toString());

        try {
            StringBuffer query = new StringBuffer(64)
                .append("DELETE FROM userdatasetitem WHERE userdataset_id='")
                .append(dsId)
                .append("' AND itemaccessioncode IN (")
                .append(acCSV.toString())
                .append(")");

            log.debug("deleting dataset items: " + query.toString());
            da.issueUpdate(DatabaseUtility.removeSemicolons(query.toString()));

        } catch (Exception ex) {
            log.error("Exception while deleting dataset items", ex);
            throw new SQLException("Problem deleting dataset items: " + ex.toString());
        }

    }


    /**
     * Given a list of eligible dataset item objects or Userdatasetitem objects,
     * returns a list of Userdatasetitem objects.
     */
    public List createDatasetItemList(List beanList) {
        List dsiList = new ArrayList();
        if (beanList != null) {
            String tmpAC, beanClassName;

            Userdatasetitem dsiBean = null;
            Iterator it = beanList.iterator();
            while (it.hasNext()) {
                VBModelBean bean = (VBModelBean)it.next();
                if (bean instanceof Userdatasetitem) {
                    // add the dsi as is
                    dsiList.add((Userdatasetitem)bean);

                } else {
                    beanClassName = bean.getClass().getName().toLowerCase();
                    if (Utility.canAddToDatasetOnLoad(beanClassName)) {
                        // make a new dsi out of the normal bean
                        // TODO: implement VBModelBean.toUserdatasetitem()
                        AccessionCode dsiAC = new AccessionCode(dsiBean.getAccessioncode());
                        dsiList.add( createDatasetItem(dsiAC, null) );
                    }
                }
            }
        }
        return dsiList;
    }


    /**
     * Add a Userdatasetitem to the current dataset.
     * Does not update the DB.
     */
    public void addItem(AccessionCode ac) {
        //log.debug("adding " + ac.toString());
        /////////////addItem(ac.getEntityName(), ac.getEntityId(), ac.toString());
        newItemsToAdd.put(ac.toString(), createDatasetItem(ac, new Long(curDataset.getUserdataset_id())));
    }

    /**
     * Add a Userdatasetitem to the current dataset.
     * Does not update the DB.
     */
    /*
       // not used 6/21/2005
    public void addItem(String tableName, Long PK, String ac) {
        // get dsi list and add this one
        newItemsToAdd.put(ac, createDatasetItem(tableName, PK, ac);
    }
    */

    /**
     * Add a Userdatasetitem to the current dataset.
     * Does not update the DB.
     */
    public static Userdatasetitem createDatasetItem(AccessionCode ac, Long dsId) {
        Userdatasetitem dsiBean = new Userdatasetitem();
        if (dsId != null) {
            dsiBean.setUserdataset_id(dsId.longValue());
        }
        dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
        dsiBean.setItemtype(ac.getEntityName());
        // get accessioncode from the db.  The accessionCode passed here is "ideal" but could be something else in db
        //dsiBean.setItemaccessioncode(ac.toString());
        String accCode = ac.toString() ; //MTL 2013-06-20, use the accessioncode passed, as it could be lsid.  Not sure about this orginal usage:;getTableAccessionCodeFromPK(ac.getEntityName(),ac.getEntityId().longValue());
        if (Utility.isStringNullOrEmpty(accCode)) {
            log.debug("couldn't get table accession code, so using ideal code");
            accCode = ac.toString();
        }
        dsiBean.setItemaccessioncode(accCode);
        dsiBean.setItemtable(ac.getEntityName());
        Long pkId = ac.getEntityId();
        if (pkId == null) {
	        pkId= getTablePKFromAccessionCode(ac.getEntityName(),accCode); //the PK will be null if lsid accessioncodes, so have to look it up manually in new function
		}
        dsiBean.setItemrecord(pkId.toString());
        dsiBean.setItemdatabase(ITEM_DB);
        log.debug("creating a DSI (ideal accCode): " + ac.toString() + ", itemtable: " + dsiBean.getItemtable() + " and using accCode:" + accCode);
        return dsiBean;
    }


    /**
     * Get the primary key of the row in the database that has the accessioncode given
     *
     * @param tableName
     * @param accessionCode
     * @return Long -- primary key  of the record
     */
   public static Long getTablePKFromAccessionCode( String tableName, String accessionCode )
    {
        StringBuffer sb = new StringBuffer();
        Long lngPK = null;
        try
        {
            sb.append(
                "SELECT " + Utility.getPKNameFromTableName(tableName) +" from "+
        tableName+" where " + Constants.ACCESSIONCODENAME + " = '" +
        accessionCode + "'"
            );
            //if (conn == null || conn.isClosed()) {
            //    log.debug("connection was null or closed, so opening a new one");
            //    conn=DBConnectionPool.getInstance().getDBConnection("Need connection for inserting dataset");
            //}
            // log.debug("trying to getting PK from db: " + sb.toString());
            DatabaseAccess daNew = new DatabaseAccess();
            ResultSet rs = daNew.issueSelect(sb.toString());

            //Statement query = conn.createStatement();
            //ResultSet rs = query.executeQuery(sb.toString());
            while (rs.next())
            {
                lngPK = new Long(rs.getString(1));
               // log.debug("have PK from db: " + lngPK);
            }
            rs.close();
        }
        catch ( SQLException se )
        {
           // this.filterSQLException(se, sb.toString());
           log.debug("ERROR in getting PK from db: " + se.toString());
        }
        //log.debug("Query: '" + sb.toString() + "' got PK = " + PK);
        return lngPK;
    }




    /**
     * Get the accessionCode of the row in the database that has the same PK given
     *
     * @param tableName
     * @param lngPK
     * @return string -- AccessionCode  of the record
     */
    public static String getTableAccessionCodeFromPK( String tableName, long lngPK )
    {
        StringBuffer sb = new StringBuffer();
        String accCode = "";
        try
        {
            sb.append(
                "SELECT " + Constants.ACCESSIONCODENAME +" from "+
        tableName+" where " + Utility.getPKNameFromTableName(tableName) + " = '" +
        lngPK + "'"
            );
            //if (conn == null || conn.isClosed()) {
            //    log.debug("connection was null or closed, so opening a new one");
            //    conn=DBConnectionPool.getInstance().getDBConnection("Need connection for inserting dataset");
            //}
            DatabaseAccess daNew = new DatabaseAccess();
            ResultSet rs = daNew.issueSelect(sb.toString());

            //Statement query = conn.createStatement();
            //ResultSet rs = query.executeQuery(sb.toString());
            while (rs.next())
            {
                accCode = rs.getString(1);
            }
            rs.close();
        }
        catch ( SQLException se )
        {
           // this.filterSQLException(se, sb.toString());
           log.debug("ERROR in getting accessionCode from db: " + se.toString());
        }
        //log.debug("Query: '" + sb.toString() + "' got PK = " + PK);
        return accCode;
    }


    /**
     * Adds items to the current dataset which will be
     * inserted on the next call to saveDataset().
     * Does not update the DB.
     */
    public void addItemsByAC(List items) {
        String ac = null;
        Iterator it = items.iterator();
        while (it.hasNext()) {
            try {
                ac = (String)it.next();
                if (!Utility.isStringNullOrEmpty(ac)) {
                    addItem(new AccessionCode(ac));
                }
            } catch (Exception ex) {
                log.error("Problem adding dataset item: " + ac, ex);
            }
        }
    }


    /**
     * Adds items to the current dataset which will be
     * inserted on the next call to saveDataset().
     * Runs a query that must select accessioncode so
     * that all results can be added.
     * @param query SQL to run
     * @param acField the name of the field containing the AC to add
     */
    public void addItemsByQuery(String query, String acField) {

        ResultSet rs = null;
        ResultSetMetaData meta = null;
        Userdataset ds = null;
        log.debug("adding items with query: " + query);

        try {
            rs = da.issueSelect(DatabaseUtility.removeSemicolons(query));
            while (rs.next()) {
                addItem(new AccessionCode(rs.getString(acField)));
            }
            da.closeStatement();
            rs.close();
        } catch (SQLException ex) {
            log.error("Problem adding items by query", ex);
        }
    }

    /**
     * Insert items now using the given query and current dataset.
     * Runs a query that must select accessioncode so
     * that all results can be added. Query must contain
     *  pk
     *  AC
     *
     * @param query SQL to run
     * @param acField the name of the field containing the AC to add
     */
    public void insertItemsByQuery(String query, String itemType, String itemTable, String pkField, String acField) {

        try {
            Long dsId = getOrCreateCurrentDataset();
            String insert =
                "INSERT INTO userdatasetitem (userdataset_id,itemtype,itemdatabase,itemtable,itemrecord,itemaccessioncode) " +
                "SELECT '" + curDataset.getUserdataset_id() + "','" + itemType + "','" + Utility.DATABASE_NAME + "','" + itemTable +
                "', " + pkField + ", " +  acField + " FROM (" + DatabaseUtility.removeSemicolons(query) +
                ") AS findadd_selection WHERE findadd_selection." + acField + " NOT IN " +
                "(SELECT udi.itemaccessioncode FROM userdatasetitem udi WHERE userdataset_id='" +
                curDataset.getUserdataset_id() + "')";

            //log.debug("inserting items with query: " + insert);

            da.issueUpdate(insert);
            da.closeStatement();
        } catch (SQLException ex) {
            log.error("Problem inserting items by query", ex);
        }
    }

    /**
     * Gets the usrId owner of a dataset.
     * @return Long usrId which could be null,
     *   or Long with value -1 if no dataset found.
     *
     */
    public Long getOwnerId(long dsId) {
        log.debug("getting DS owner for #" + dsId);
        Long usrId = null;
        ResultSet rs;
        try {
            StringBuffer query = new StringBuffer(96);
            query.append("SELECT usr_id FROM userdataset WHERE userdataset_id='")
                .append(dsId).append("'");
            rs = da.issueSelect(query.toString());
            if (rs.next()) {
                 long l = rs.getInt(1);
                 if (l != 0) {
                    usrId = new Long(l);
                 }
            } else {
                 usrId = new Long(-1);
            }
            rs.close();
            da.closeStatement();
        } catch (SQLException ex) {
            log.error("Problem getting ds owner", ex);
        }

        return usrId;
    }


    /**
     * Drops all items from a dataset.
     */
    public void dropAllItems(long dsId) {
        log.debug("dropping all items in " + dsId);
        try {
            StringBuffer query = new StringBuffer(96);
            query.append("DELETE FROM userdatasetitem WHERE userdataset_id='")
                .append(dsId).append("'");
            da.issueUpdate(query.toString());
            da.closeStatement();
        } catch (SQLException ex) {
            log.error("Problem dropping all items from ds", ex);
        }
    }


    /**
     * Drop a Userdatasetitem from the current dataset.
     * Make sure curDataset has been set first.
     * Does not update the DB.
     */
    public void dropItem(String ac) {
        // use a hash so that there are no duplicates
        log.debug("putting drop item: " + ac);
        newItemsToDrop.put(ac, "drop");
    }


    /**
     * Drop a Userdatasetitem to the current dataset.
     * Make sure curDataset has been set first.
     * Does not update the DB.
     */
    /*
    public void dropItem(String tableName, Long PK, String ac) throws SQLException {

        Userdatasetitem dsiBean = new Userdatasetitem();
        dsiBean.setUserdataset_id(curDataset.getUserdataset_id());
        dsiBean.setItemtype(tableName);
        dsiBean.setItemaccessioncode(ac);

        // TODO: delete all this method.  obsolete
        // get dsi list and add this one
        //newItemsToDrop.put(ac, dsiBean);
    }
    */

    /**
     *
     */
    public void dropItemsByAC(List items) {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            dropItem((String)it.next());
        }
    }


    /**
     * Populate a Userdatasetitem as much as possible and return it.
     */
    /*
       // not in use 6/21/2005
    public Userdatasetitem createItem(String tableName, Long PK) throws SQLException {
        Userdatasetitem dsiBean = new Userdatasetitem();
        dsiBean.setItemaccessioncode(ag.getAccession(tableName, PK.longValue()));
        dsiBean.setItemtype(tableName);
        //dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
        return dsiBean;
    }
    */


    // =================================================================
    // User Datasets & the Datacart
    // =================================================================

    /**
     * Get a dataset by userdataset_id.
     */
    public Userdataset getDataset(Long dsId) {
        StringBuffer query = new StringBuffer(64)
            .append("SELECT * FROM userdataset WHERE userdataset_id='")
            .append(dsId)
            .append("'");

        return retrieveDatasetFromDB(query.toString());
    }

    /**
     * Get a user's dataset by name.
     */
    public Userdataset getDatasetByName(String dsName, Long usrId) {
        StringBuffer query = new StringBuffer(64);
        query.append("SELECT * FROM userdataset WHERE usr_id");
        if (usrId == null) {
            query.append(" IS NULL ");
        } else {
            query.append("='").append(usrId).append("' ");
        }

        query.append(" AND datasetname='").append(dsName).append("'");
        return retrieveDatasetFromDB(query.toString());
    }

    /**
     * Get a user's datacart.  userdataset.datasettype = 'datacart'
     */
    public Userdataset getDatacartByUser(Long usrId) {
        StringBuffer query = new StringBuffer(64);

        query.append("SELECT * FROM userdataset WHERE usr_id");
        if (usrId == null) {
            query.append(" IS NULL ");
        } else {
            query.append("='").append(usrId).append("' ");
        }

        query.append(" AND datasettype='").append(TYPE_DATACART).append("'");
        return retrieveDatasetFromDB(query.toString());
    }

    /**
     * Get a dataset by its ID.
     */
    public Userdataset getDatasetById(Long dsId) {
        StringBuffer query = new StringBuffer(64)
            .append("SELECT * FROM userdataset WHERE userdataset_id='")
            .append(dsId.toString())
            .append("'");

        return retrieveDatasetFromDB(query.toString());
    }

    /**
     *
     */
    private Userdataset retrieveDatasetFromDB(String query) {
        ResultSet rs = null;
        ResultSetMetaData meta = null;
        Userdataset ds = null;

        try {
            rs = da.issueSelect(DatabaseUtility.removeSemicolons(query.toString()));
            meta = rs.getMetaData();
            ds = (Userdataset)Utility.buildBean(meta, rs, "Userdataset");
            da.closeStatement();
            rs.close();
        } catch (SQLException ex) {
            log.error("Problem retrieving dataset.", ex);
        }

        return ds;
    }




    /**
     * Returns the current datacart using the dataset_id
     * found in the session, or null.
     */
    public Userdataset getDatacart(HttpSession session) {
        Long did = getDatacartId(session);
        if (did == null || did.longValue() == 0) {
            return null;
        }

        log.debug("getting datacart from session: " + did);
        return getDataset(did);
    }


    /**
     * Checks if a user has a datacart and loads it if so,
     * else creates a new dataset as the given user's datacart.
     * Does not write to DB but does read.
     */
    /*
    public Userdataset getOrCreateAnonDatacart(String dsName) {
        Userdataset ds = null;
        try {
            ds = getDatasetByName(dsName, ANON_USR_ID);

            if (ds == null) {
                ds = createDataset(null, dsName, DATACART_DESC,
                        TYPE_DATACART, SHARING_PRIVATE, ANON_USR_ID);
            }
        } catch (SQLException ex) {
            log.error("Problem creating anon datacart named " + dsName, ex);
        }
        return ds;
    }
    */


    /**
     * Checks if a user has a datacart and loads it if so,
     * else creates a new dataset as the given user's datacart.
     * Sets current dataset (used by DatasetUtility).
     * Does not write to DB but does read.
     */
    public Userdataset getOrCreateDatacart(Long usrId) {
        Userdataset ds = null;
        try {
            if (usrId != null) {
                // a real user can only have one datacart
                ds = getDatacartByUser(usrId);
            }

            if (ds == null) {
                log.debug("+ + + creating new empty datacart + + + usr_id: " + usrId);
                ds = createDataset(null, DEF_DS_NAME, "",
                        TYPE_DATACART, SHARING_PRIVATE, usrId);
                AccessionCode dsAC = insertDataset(ds);
                if (dsAC != null) {
                    log.debug("inserted new dataset as datacart: " + dsAC.toString());
                    ds.setUserdataset_id(dsAC.getEntityId().longValue());
                }
            }

        } catch (SQLException ex) {
            log.error("Problem creating datacart", ex);
        }

        setCurDataset(ds);
        return ds;
    }

    /**
     *
     */
    public void setCurDataset(Userdataset ds) {
        curDataset = ds;
    }

    /**
     *
     */
    public Userdataset getCurDataset() {
        return curDataset;
    }


    /**
     *
     */
    public boolean datasetExists(Userdataset ds) {
        StringBuffer query = new StringBuffer(64)
            .append("SELECT count(*) FROM userdataset WHERE userdataset_id='")
            .append(ds.getUserdataset_id())
            .append("' OR accessioncode='")
            .append(ds.getAccessioncode())
            .append("'");

        ResultSet rs = null;
        boolean exists = false;

        try {
            rs = da.issueSelect(DatabaseUtility.removeSemicolons(query.toString()));
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            } else {
                exists = false;
            }
            da.closeStatement();
            rs.close();

        } catch (SQLException ex) {
            log.error("Problem checking for dataset existence", ex);
        }

        return exists;
    }

    /**
     *
     */
    public boolean datasetItemExists(Userdatasetitem dsi) {
        StringBuffer query = new StringBuffer(64)
            .append("SELECT count(1) FROM userdatasetitem WHERE userdataset_id='")
            .append(dsi.getUserdataset_id()).append("' AND (");

        long l = dsi.getUserdatasetitem_id();
        if (l != 0) {
            query.append("userdatasetitem_id='")
                .append(l).append("' OR ");
        }

        query.append("itemaccessioncode='")
            .append(dsi.getItemaccessioncode()).append("')");

        ResultSet rs = null;
        boolean exists = false;
        try {
            rs = da.issueSelect(DatabaseUtility.removeSemicolons(query.toString()));
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            } else {
                exists = false;
            }
            da.closeStatement();
            rs.close();
        } catch (SQLException ex) {
            log.error("Problem checking for dataset item existence", ex);
        }
        return exists;
    }

    /**
     * Asks the DB how many items are in the dataset.
     * @return number of items in dataset
     */
    public long countItems(Long dsId) {
        ResultSet rs = null;
        long l = 0;

        if (dsId == null) {
            l = curDataset.getUserdataset_id();
            if (l == 0) { return 0; }
            dsId = new Long(l);
        }
        if (dsId == null) {
            return 0;
        }

        try {
            StringBuffer query = new StringBuffer(64)
                .append("SELECT count(*) FROM userdatasetitem WHERE userdataset_id='")
                .append(dsId.toString()).append("'");
            rs = da.issueSelect(DatabaseUtility.removeSemicolons(query.toString()));

            if (rs.next()) {
                l = rs.getInt(1);
            }

            da.closeStatement();
            rs.close();
        } catch (SQLException ex) {
            log.error("Problem adding items by query", ex);
        }

        return l;
    }




    /**
     *
     */
    public static Long getDatacartId(HttpSession session) {
        Long l = (Long)session.getAttribute(Utility.DATACART_KEY);
        return (l == null) ? new Long(0): l;
    }


    /**
     * @return userdataset_id
     */
    private Long getOrCreateCurrentDataset() throws SQLException {
        Long dsId = null;

        if (datasetExists(curDataset)) {
            // use extant
            dsId = new Long(curDataset.getUserdataset_id());
        } else {
            // create new
            AccessionCode dsAC = insertDataset(curDataset);
            if (dsAC == null) { return null; }
            log.debug("inserted new dataset: " + dsAC.toString());
            dsId = dsAC.getEntityId();
            curDataset.setUserdataset_id(dsId.longValue());

        }
        return dsId;
    }

    /**
     * Sets a user's current datacart.  Changes the old
     * datacart to a normal datacart. There can be only one.
     * @param usr_id of dataset owner
     * @param dsId of the target dataset
     * @return count of items in chosen dataset
     */
    public long setDatacart(long usrId, long dsId)
            throws SQLException {
        if (usrId == 0) { log.warn("setDatacart was given usrId = 0"); }
        long count = 0;

        // check if datacart should be reset
        if (dsId == -1) {
            // reset datacart
            // just bump old datacart and end
            bumpDatacart(usrId);

        } else {
            // make sure user owns this dataset
            Long lUsrId = getOwnerId(dsId);
            if (lUsrId == null) {
                log.error("setDatacart: no owner for dsId = " + dsId); return 0;
            }

            if (lUsrId.longValue() != usrId) {
                log.warn("setDatacart: DS owner does not match given usrId = " + usrId); return 0;
            }

            // bump user's old datacart
            bumpDatacart(usrId);

            // activate given dataset as datacart
            activateDatacart(dsId);

            count = countItems(new Long(dsId));
        }


        return count;
    }

    /**
     * Sets given dataset as datacart.
     */
    private void activateDatacart(long dsId)
            throws SQLException {
        updateDatasetType(dsId, TYPE_DATACART);
    }


    /**
     * Sets a user's datacart to normal.
     */
    public void bumpDatacart(long usrId)
            throws SQLException {
                /*
        Userdataset uds = getDatacartByUser(new Long(usrId));
        if (uds != null) {
            long dsId = uds.getUserdataset_id();
            log.debug("bumping old datacart: " + dsId);
            updateDatasetType(dsId, TYPE_NORMAL);
        }
        */
        try {
            StringBuffer query = new StringBuffer(64)
                .append("UPDATE userdataset SET datasettype='")
                .append(TYPE_NORMAL)
                .append("' WHERE datasettype='")
                .append(TYPE_DATACART)
                .append("' AND usr_id='")
                .append(usrId)
                .append("'");

            log.debug("bumping datacarts: " + query.toString());
            da.issueUpdate(DatabaseUtility.removeSemicolons(query.toString()));
        } catch (Exception ex) {
            log.error("Exception while bumping datacarts", ex);
            throw new SQLException("Problem bumping datacarts" + ex.toString());
        }
    }


    /**
     *
     */
    private void updateDatasetType(long dsId, String newType)
            throws SQLException {
        if (Utility.isStringNullOrEmpty(newType)) {
            log.error("Given dataset type is empty");
            return;
        }

        try {
            StringBuffer query = new StringBuffer(64)
                .append("UPDATE userdataset SET datasettype='")
                .append(newType)
                .append("' WHERE userdataset_id='")
                .append(dsId)
                .append("'");

            log.debug("updating dataset type: " + query.toString());
            da.issueUpdate(DatabaseUtility.removeSemicolons(query.toString()));

        } catch (Exception ex) {
            log.error("Exception while updating dataset type", ex);
            throw new SQLException("Problem updating dataset type to " + newType + ": " + ex.toString());
        }
    }


    /**
     *
     */
    public void setOwner(long usrId, long dsId)
            throws SQLException {
        try {
            StringBuffer query = new StringBuffer(64)
                .append("UPDATE userdataset SET usr_id='")
                .append(usrId)
                .append("' WHERE userdataset_id='")
                .append(dsId)
                .append("'");

            log.debug("updating dataset owner: " + query.toString());
            da.issueUpdate(DatabaseUtility.removeSemicolons(query.toString()));

        } catch (Exception ex) {
            log.error("Exception while updating dataset owner", ex);
            throw new SQLException("Problem updating dataset owner to " + usrId + ": " + ex.toString());
        }
    }

}
