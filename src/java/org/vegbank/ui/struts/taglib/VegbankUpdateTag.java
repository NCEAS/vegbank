/*
 *	'$RCSfile: VegbankUpdateTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-11 01:29:22 $'
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
import java.text.SimpleDateFormat;
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
 * @version $Revision: 1.1 $ $Date: 2005-08-11 01:29:22 $
 */

public class VegbankUpdateTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankUpdateTag.class);
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
			if (Utility.isStringNullOrEmpty(getEntity())) { log.error("Attribute 'entity' is required"); return SKIP_BODY;}
			if (Utility.isArrayNullOrEmpty(getFieldNamesArray())) { log.error("Attribute 'fieldNames' is required"); return SKIP_BODY;}
			if (Utility.isStringNullOrEmpty(getRecordId())) { log.error("Attribute 'recordId' is required"); return SKIP_BODY;}

            String authScheme =  getAuth();
			if (Utility.isStringNullOrEmpty(authScheme)) { authScheme = AUTH_USR_ID; }

            // instantiate this entity
            //Utility.createObject("org.vegbank.common.model." +
            //       Utility.upperCaseFirstLetter(entity.toLowerCase()));
			Class entityClass = Class.forName("org.vegbank.common.model." +
                    Utility.upperCaseFirstLetter(entity.toLowerCase()));
            String pkName = (String)entityClass.getField("PKNAME").get(null);
            String tableName = entity;

            // build UPDATE sql
            StringBuffer sql = new StringBuffer(128);
            sql.append("UPDATE ").append(tableName).append(" SET ");

            // iterate through fields/values
            String[] fieldNames = getFieldNamesArray();
            String[] fieldValues = getFieldValuesArray();
            boolean first = true;
            for (int i=0; i<fieldNames.length; i++) {
                if (first) { first = false; }
                else { sql.append(", "); }

                sql.append(Utility.encodeForDB(fieldNames[i])).append("=");
                if (fieldValues.length <= i || fieldValues[i] == null) {
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
                    log.error("Use of <vegbank:update> requires a valid web session");
                    return SKIP_BODY;
                }
                sql.append(" AND usr_id='").append(usrId.longValue()).append("'");
            }

            log.debug(sql.toString());
            DatabaseAccess da = new DatabaseAccess();
            da.issueUpdate(DatabaseUtility.removeSemicolons(sql.toString()));

		} catch (Exception ex) {
			log.error("Error while running <vegbank:update> ", ex);
		}

		
        return SKIP_BODY;
    }


    /**
     * 
     */
	protected String entity;

    public String getEntity() {
		return this.entity;
    }

    public void setEntity(String s) {
        this.entity = s;
    }

    /**
     * 
     */
	protected String fieldNames;

    public String getFieldNames() {
        return this.fieldNames;
    }

    public String[] getFieldNamesArray() {
        return findAttributeArray("fieldNames");
    }

    public void setFieldNames(String s) {
        this.fieldNames = s;
    }

    /**
     * 
     */
	protected String fieldValues;

    public String getFieldValues() {
        return this.fieldValues;
    }

    public String[] getFieldValuesArray() {
        return findAttributeArray("fieldValues");
    }

    public void setValues(String s) {
        this.fieldValues = s;
    }

    /**
     * 
     */
	protected String recordId;

    public String getRecordId() {
        return findAttribute("recordId", this.recordId);
    }

    public void setRecordId(String s) {
        this.recordId = s;
    }

    /**
     * 
     */
	protected String auth;

    public String getAuth() {
        return this.auth;
    }

    public void setAuth(String s) {
        this.auth = s;
    }


}
