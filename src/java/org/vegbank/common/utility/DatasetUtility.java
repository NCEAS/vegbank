/*
 *	'$RCSfile: DatasetUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-30 02:05:36 $'
 *	'$Revision: 1.8 $'
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
import org.vegbank.common.model.Userdataset;
import org.vegbank.common.model.Userdatasetitem;
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
            log.debug("CREATING userdataset WITHOUT A usr_id");
        }

        if (beanList != null) {
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

            initBeanInserter();
            kit = newItemsToAdd.keySet().iterator();
            while (kit.hasNext()) {
                // new items are stored in a hash
                String key = (String)kit.next();
                Userdatasetitem dsi = (Userdatasetitem)newItemsToAdd.get(key);
                //if (!datasetItemExists(dsi)) {
                    bean2db.addBean(dsi);
                //}
            }
            insertItem(null);
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

            long dsPK = bean2db.insert(dsBean);
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
                    if (Utility.canBeDatasetItem(beanClassName)) {
                        // make a new dsi out of the normal bean
                        // TODO: implement VBModelBean.toUserdatasetitem()
                        AccessionCode dsiAC = new AccessionCode(dsiBean.getAccessioncode());
                        dsiBean = new Userdatasetitem();
                        dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
                        dsiBean.setItemtype(beanClassName);
                        dsiBean.setItemaccessioncode(dsiAC.toString());
                        dsiBean.setItemdatabase(dsiAC.getDatabaseId());
                        dsiBean.setItemtable(dsiAC.getEntityName());
                        dsiBean.setItemrecord(dsiAC.getEntityId().toString());

                        dsiList.add(dsiBean);
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
        addItem(ac.getEntityName(), ac.getEntityId(), ac.toString());
    }


    /**
     * Add a Userdatasetitem to the current dataset.
     * Does not update the DB.
     */
    public void addItem(String tableName, Long PK, String ac) {

        Userdatasetitem dsiBean = new Userdatasetitem();
        dsiBean.setUserdataset_id(curDataset.getUserdataset_id()); 
        dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
        dsiBean.setItemtype(tableName);
        dsiBean.setItemaccessioncode(ac);
        dsiBean.setItemdatabase(ITEM_DB);
        dsiBean.setItemtable(tableName);
        dsiBean.setItemrecord(PK.toString());

        // get dsi list and add this one
        newItemsToAdd.put(ac, dsiBean);
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
                "', observation_id, observationaccessioncode FROM (" + DatabaseUtility.removeSemicolons(query) +
                ") AS findadd_selection WHERE findadd_selection.observationaccessioncode NOT IN " +
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
    public Userdatasetitem createItem(String tableName, Long PK) throws SQLException {
        Userdatasetitem dsiBean = new Userdatasetitem();
        dsiBean.setItemaccessioncode(ag.getAccession(tableName, PK.longValue()));
        dsiBean.setItemtype(tableName);
        //dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
        return dsiBean;
    }


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

        query.append(" AND datasettype='").append(DATACART_NAME).append("'");
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
        
        return getDataset(getDatacartId(session));
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
                        DATACART_NAME, SHARING_PRIVATE, ANON_USR_ID);
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
    public Userdataset getOrCreateDatacart(String sessionId, Long usrId) {
        Userdataset ds = null;
        try {
            if (usrId == null) { 
                // get the anon datacart using sessionId
                ds = getDatasetByName(sessionId, usrId);
            } else {
                // a real user can only have one datacart
                ds = getDatacartByUser(usrId);
            }

            if (ds == null) {
                log.debug("+ + + creating new empty datacart + + + usr_id: " + usrId);
                ds = createDataset(null, sessionId, sessionId, 
                        DATACART_NAME, SHARING_PRIVATE, usrId);
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

}
