/*
 *	'$RCSfile: VegbankDenormTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2005-08-16 21:50:29 $'
 *	'$Revision: 1.1 $'
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


import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.struts.taglib.bean.WriteTag;

import org.vegbank.common.Constants;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.utility.Utility;
import org.vegbank.common.utility.ServletUtility;
import org.vegbank.ui.struts.VegbankAction;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.DatabaseUtility;

/**
 * Tag that selects the proper where clause key
 * to look up in SQLStore.properties.
 *
 * @author P. Mark Anderson
 * @version $Revision: 1.1 $ $Date: 2005-08-16 21:50:29 $
 */

public class VegbankDenormTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankDenormTag.class);
	private static ResourceBundle sqlStore =
			ResourceBundle.getBundle("org.vegbank.common.SQLStore");
	public static final String AUTH_USR_ID = "usr_id";
	public static final String AUTH_CUSTOM = "custom";


    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		try {
			//if (Utility.isStringNullOrEmpty(getEntity())) { log.error("Attribute 'entity' is required"); return SKIP_BODY;}
			//if (Utility.isArrayNullOrEmpty(getFieldNamesArray())) { log.error("Attribute 'fieldNames' is required"); return SKIP_BODY;}
			//if (Utility.isStringNullOrEmpty(getRecordId())) { log.error("Attribute 'recordId' is required"); return SKIP_BODY;}

            //String authScheme =  getAuth();
			//if (Utility.isStringNullOrEmpty(authScheme)) { authScheme = AUTH_USR_ID; }

            // instantiate this entity
            //Utility.createObject("org.vegbank.common.model." +
            //       Utility.upperCaseFirstLetter(entity.toLowerCase()));
			// Class entityClass = Class.forName("org.vegbank.common.model." +
           //         Utility.upperCaseFirstLetter(entity.toLowerCase()));
           // String pkName = (String)entityClass.getField("PKNAME").get(null);
           // String tableName = entity;

            // build UPDATE sql
            StringBuffer sql = new StringBuffer(128);
            //sql.append("UPDATE ").append(tableName).append(" SET ");


            String update = getUpdate();
            String denormtype = getDenormtype();
            if (Utility.isStringNullOrEmpty(denormtype)) { denormtype="null";  }

            log.debug("vegbank:denorm>> update: " + update + ", type=" + denormtype );
            String updateSQL = sqlStore.getString(update);
            String whereSQL = "  true ";
            if (denormtype.equals("all")) {
                // no where statement needed
                whereSQL = " true ";
			} else {
				// get the where statement from SQLStore
				whereSQL = sqlStore.getString(update + "_w_" + denormtype);
				if (whereSQL.indexOf("{0}") != -1 ) {
					//need to substitude wparams for {0}, etc
					MessageFormat format;
					format = new MessageFormat(whereSQL);
					//declare wparam as an array
					String[] whereParams =getWparamArray();
				    whereSQL = format.format(whereParams);
				    log.debug("vegbank:denorm>> swapped wparams for {0} stuff: " + whereSQL );
				}

			}
            //String whereSQL = sqlStore.getString("upd_wherenull_commname");
            sql.append(updateSQL).append(" AND ").append(whereSQL);

            // iterate through fields/values
/*
            String[] fieldNames = getFieldNamesArray();
            String[] fieldValues = getFieldValuesArray();
            //variables to hold length and current values
            String currfieldVal = null;
            int fieldvalueslength = 0;

            boolean first = true;
            for (int i=0; i<fieldNames.length; i++) {
                if (first) { first = false; }
                else { sql.append(", "); }

                //if they aren't null, use the values
                if (fieldValues != null) {
					currfieldVal = fieldValues[i] ;
					fieldvalueslength = fieldValues.length;
			    }
                else {  //otherwise, use 0 and null
					fieldvalueslength = 0;
					currfieldVal = null;
				}
                sql.append(Utility.encodeForDB(fieldNames[i])).append("=");
                if (fieldvalueslength <= i || currfieldVal == null) {
                    // no more values or value is null
                    sql.append("null");
                } else {
                    sql.append("'").append(Utility.encodeForDB(fieldValues[i])).append("'");
                }
            }

            sql.append(" WHERE ").append(pkName)
                .append("='").append(Utility.encodeForDB(getRecordId())).append("' ");

            if (authScheme.equals(AUTH_USR_ID)) {
                Long usrId = (Long)request.getSession().getAttribute(Constants.USER_KEY);
                if (usrId == null) {
                    log.error("Use of <vegbank:denorm> requires a valid web session");
                    return SKIP_BODY;
                }
               // sql.append(" AND usr_id='").append(usrId.longValue()).append("'");
            }
*/
            log.debug("vegbank:denorm>> running SQL: " + sql.toString());
            DatabaseAccess da = new DatabaseAccess();
            int results = da.issueUpdate(DatabaseUtility.removeSemicolons(sql.toString()));
            log.debug("vegbank:denorm>> SQL IS DONE: result:" + results );
            String resultsString = Integer.toString(results);

            request.setAttribute(update+"-RESULTS",resultsString);
		} catch (Exception ex) {
			log.error("Error while running <vegbank:denorm> ", ex);
		}


        return SKIP_BODY;
    }



    /**
     * Key in SQLStore.properties that defines SQL where clause.
     */
	protected String update;

    public String getUpdate() {
        return findAttribute("update", this.update);
    }

    public void setUpdate(String s) {
        this.update = s;
    }


    /**
     *  FROM vegbank:get
     */

    	protected String wparam = null;

	    public String getWparam() {
			String s = findAttribute("wparam", this.wparam);
		//	if (s != null) {
		//		s = s.toLowerCase();
		//	}

			return s.replace(';',' '); //no semicolons allowed.  could lead to security problem
    }

    public String[] getWparamArray() {
		String[] arr;

		// override if set explicitely
		String tmp = this.wparam;
		if (!Utility.isStringNullOrEmpty(tmp)) {
			arr = new String[1];
			//arr[0] = DatabaseUtility.makeSQLSafe(getWparam(), false);
			arr[0] = getWparam();
			log.debug(" has 1 WPARAM");
			return arr;
		}


		arr = findAttributeArray("wparam", null);
		if (arr != null) {
			for (int i=0; i<arr.length; i++) {
				///////////arr[i] = stripSingleQuotes(arr[i]);
				//arr[i] = DatabaseUtility.makeSQLSafe(arr[i], false);
				arr[i] = arr[i];
			}
		}

		return arr;
    }


    public void setWparam(String s) {
		this.wparam = s;
		//////////this.wparam = new String[1];
		//////////this.wparam[0] = s;
	}




    /**
     *  denormType is one strong of either: all, null, ac (null is default option)
     */
	protected String denormtype;

    public String getDenormtype() {
        return findAttribute("denormtype", this.denormtype);
    }

    public void setDenormtype(String s) {
        if (s == null) {
			s = "null"; // default
		}
        this.denormtype = s;
    }



}
