/*
 *	'$RCSfile: DatasetUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-16 20:18:13 $'
 *	'$Revision: 1.2 $'
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
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.VBModelBeanToDB;
import org.vegbank.common.utility.AccessionGen;
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

	protected static Log log = LogFactory.getLog(DatasetUtility.class);


	private VBModelBeanToDB bean2db = null;



    /**
     *
     */
    public DatasetUtility() {
        try {
            this.bean2db = new VBModelBeanToDB();
        } catch(Exception vbex) {
            log.error("FATAL: problem initializing VBModelBeanToDB: " + vbex.getMessage());
        }
    }


    /**
     * Inserts a new userdataset record optionally with its children
     * @param beanList List of VBModelBean objects (could be Userdatasetitems) to store as
     * the items in this new dataset
     * @return new userdataset accession code 
     */
    public String insertDataset(List beanList, String dsName, 
            String dsDescription, String dsType, String dsSharing, long usrId) 
            throws SQLException{

        String newAC = null;

        if (Utility.isStringNullOrEmpty(dsSharing)) {
            dsSharing = "private";
        }

        log.debug("inserting dataset: " + dsName + " for usr_id: " + usrId);

        Userdataset dsBean = new Userdataset();
        dsBean.setDatasetname(dsName);
        dsBean.setDatasetdescription(dsDescription);
        dsBean.setDatasettype(dsType);
        dsBean.setDatasetsharing(dsSharing);
        dsBean.setUsr_id(usrId);

        if (beanList != null) {
            List dsiList = new ArrayList();
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
                        dsiBean = new Userdatasetitem();
                        dsiBean.setUserdatasetitem_id(-1); // don't check for duplicates
                        dsiBean.setItemtype(beanClassName);
                        dsiBean.setItemaccessioncode(dsiBean.getAccessioncode());
                        dsiList.add(dsiBean);
                    }
                }
            }

            dsBean.setuserdataset_userdatasetitems(dsiList); 
        }

        try {
            //log.debug("inserting bean");
            long dsPK = bean2db.insert(dsBean);
            AccessionGen ag = new AccessionGen(bean2db.getDBConnection());
            //log.debug("getting accession code for new dataset");
            newAC = ag.buildAccession("userdataset", Long.toString(dsPK), dsName);
            log.debug("inserted new userdataset with its children: " + newAC);

        } catch (Exception ex) {
            log.error("Exception while inserting model bean", ex);
            throw new SQLException("Problem inserting bean: " + ex.toString());
        }

        return newAC;
    }

}
