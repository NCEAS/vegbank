/*
 *	'$RCSfile: VegbankDatacartTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-30 02:06:07 $'
 *	'$Revision: 1.6 $'
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

package org.vegbank.ui.struts.taglib;


import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;

import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.common.utility.DatasetUtility;
import org.vegbank.common.model.Userdataset;
import org.vegbank.common.model.Userdatasetitem;

/**
 * Tag that displays a count of items in the datacart, which
 * is a userdataset that lives in the session.  This tag also
 * manages changes to the datacart.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.6 $ $Date: 2005-05-30 02:06:07 $
 */

public class VegbankDatacartTag extends VegbankTag {

    public static final String DELTA_DELIM = ":";
    public static final String DELTA_COUNT = "count";
    public static final String DELTA_ADD = "add";
    public static final String DELTA_DROP = "drop";
    public static final String DELTA_DROPALL = "dropall";
    public static final String DELTA_FINDADD = "findadd";

	private static final Log log = LogFactory.getLog(VegbankDatacartTag.class);
    private Long usrId;


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        long count = 0;
		HttpSession session = ((HttpServletRequest)pageContext.getRequest()).getSession();
		session.setMaxInactiveInterval(-1);  // never timeout
        usrId = Utility.getAuthenticatedUsrId(session);
        if (usrId == null || usrId.longValue() == 0) {
            usrId = DatasetUtility.ANON_USR_ID;
        }

        log.debug("usrId: " + usrId);

		try {
            // get all items involved in a change
            String d = getDelta();
            String arrDelta[];
            boolean hasDelta;
            DatasetUtility dsu = null;

            if (Utility.isStringNullOrEmpty(d)) {
                arrDelta = new String[1];
                arrDelta[0] = "";
                hasDelta = false;
            } else {
                arrDelta = d.split(DELTA_DELIM);
                hasDelta = true;
            }



            List deltaItems = null;
            Object oItems = getDeltaItems();
            if (hasDelta && oItems != null) {
                if (oItems instanceof String) {
                    // check for CSV
                    String items = oItems.toString();
                    //log.debug("got String item: " + items);

                    deltaItems = new ArrayList();
                    if (!Utility.isStringNullOrEmpty(items)) {
                        if (arrDelta[0].equals(DELTA_FINDADD) || items.indexOf(",") == -1) {
                            // add the single value for findadd or no CSV style
                            deltaItems.add(items);
                        } else {
                            //log.debug("parsing CSV");
                            StringTokenizer st = new StringTokenizer(items, ",");
                            while (st.hasMoreTokens()) {
                                // add each CSV 
                                deltaItems.add(st.nextToken());
                            }
                        }
                    }

                } else if (oItems instanceof String[]) {
                    //log.debug("got String[] of items");
                    deltaItems = Arrays.asList((String[])oItems);

                } else if (oItems instanceof java.util.List) {
                    //log.debug("got List of items");
                    deltaItems = (List)oItems;
                }


                // get the datacart
                // get userdataset_id from session
                log.debug("getting datacart...");
                dsu = new DatasetUtility(); 
                Userdataset datacart = dsu.getDatacart(session);

                if (datacart == null) {
                    // try to get extant datacart for user or create a new one
                    log.debug("get/create user datacart...");
                    datacart = dsu.getOrCreateDatacart(session.getId(), usrId);
                    session.setAttribute(Utility.DATACART_KEY, new Long(datacart.getUserdataset_id()));
                    log.debug("setting datacart ID in session: " + datacart.getUserdataset_id());
                }


                if (datacart != null) {
                    log.debug("handling delta");
                    // handle delta.  add by default
                    dsu.setCurDataset(datacart);
                    if (arrDelta[0].equals(DELTA_DROP)) { 
                        log.debug("setting up drop items: " + deltaItems.size());
                        dsu.dropItemsByAC(deltaItems);
                        log.debug("updating dataset...");
                        count = dsu.saveDataset();

                    } else if (arrDelta[0].equals(DELTA_DROPALL)) { 
                        long dsId = Long.parseLong((String)deltaItems.get(0));
                        Long testUsrId = dsu.getOwnerId(dsId);
                        // check if user is DS owner
                        log.debug("testusr: " + testUsrId);
                        log.debug("usr: " + usrId);
                        if ( (testUsrId == null && usrId == null) ||
                                (testUsrId != null && testUsrId.equals(usrId)) ) {
                            dsu.dropAllItems(dsId); 
                        } else {
                            log.warn("attempt to delete non-owned dataset by usrId " + usrId);
                        }

                        log.debug("updating dataset...");
                        count = dsu.saveDataset();

                    } else if (arrDelta[0].equals(DELTA_ADD)) { 
                        log.debug("setting up add items: " + deltaItems.size());
                        dsu.addItemsByAC(deltaItems); 
                        log.debug("updating dataset...");
                        count = dsu.saveDataset();

                    } else if (arrDelta[0].equals(DELTA_FINDADD)) { 
                        if (deltaItems != null && deltaItems.size() > 0) {
                            log.debug("adding items with findadd query");
                            String query = (String)deltaItems.get(0);

                            // FORM delta=findadd:itemType:itemTable:pkField:acField
                            // EXAMPLE delta=findadd:observation:observation:observation_id:observationaccessioncode
                            // EXAMPLE delta=findadd:plantconcept:plant:plantconcept_id:accessioncode

                            // query, itemType, itemTable, pkField, acField
                            dsu.insertItemsByQuery(query, arrDelta[1], arrDelta[2], arrDelta[3], arrDelta[4]);
                            count = dsu.countItems(null);

                        } else {
                            log.debug("no findadd query given");
                        }
                    }

                } else {
                    log.error("ERROR getting/creating datacart for usr_id: " + usrId);
                }

            } else if (arrDelta[0].equals(DELTA_COUNT)) {
                // get the datacart size
                count = dsu.countItems(null);

            } else {
                // no delta items, so get count from session (for efficiency)
                Long datacartCount = (Long)session.getAttribute(Utility.DATACART_COUNT_KEY);
                if (datacartCount == null) { 
                    // hasn't been set in session, so initialize it
                    count = 0;
                    //session.setAttribute(Utility.DATACART_COUNT_KEY, new Long(0));
                    log.debug("initializing datacart size");
                } else { 
                    // use the current value
                    count = datacartCount.longValue(); 
                    log.debug("got datacart size from session: " + count);
                }
            }
            
            // update the session
            log.debug("setting datacart size in session: " + count);
            session.setAttribute(Utility.DATACART_COUNT_KEY, new Long(count));

            log.debug("datacart count is " + count);
            if (getDisplay()) {
                String datacartHTML = null;
                if (count == 1) { datacartHTML = count + " item";
                } else { datacartHTML = count + " items"; }

                // return the count of items in the datacart
                log.debug("returning HTML: " + datacartHTML);
                pageContext.getOut().println(datacartHTML);
            }

		} catch (Exception ex) {
			log.error("Datacart error ", ex);
		}
		
        return SKIP_BODY;
    }


    /**
     * 
     */
	protected String delta = null;

    public String getDelta() {
		return findAttribute("delta", this.delta);
    }

    public void setDelta(String s) {
        this.delta = s;
    }

    /**
     * 
     */
	protected Object deltaItems = null;

    public Object getDeltaItems() {
        if (deltaItems == null || deltaItems instanceof String) {
		    return findAttribute("deltaItems", (String)this.deltaItems);
        } else {
            return deltaItems;
        }

    }

    public void setDeltaItems(Object s) {
        log.debug("setting delta items: " + s.toString());
        this.deltaItems = s;
    }


    /**
     * 
     */
	protected boolean display = true;

    public boolean getDisplay() {
		if (this.display) {
			return true;
		}

        setDisplay(findAttribute("display"));
        return this.display;
    }

    public void setDisplay(String s) {
        setDisplay(Utility.isStringTrue(s));
    }

    public void setDisplay(boolean b) {
        this.display = b;
    }

}
