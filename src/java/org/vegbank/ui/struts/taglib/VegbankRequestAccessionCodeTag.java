/*
 *	'$RCSfile: VegbankRequestAccessionCodeTag.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: mlee $'
 *	'$Date: 2006-09-04 19:35:34 $'
 *	'$Revision: 1.3 $'
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
 * Tag that asks the database to reserve new accession codes for client databases
 *    (i.e. VegBranch, Carolina Veg Survey, etc.)
 *
 *
 * @author M. Lee
 * @version $Revision: 1.3 $ $Date: 2006-09-04 19:35:34 $
 */

public class VegbankRequestAccessionCodeTag extends VegbankTag {

	private static final Log log = LogFactory.getLog(VegbankRequestAccessionCodeTag.class);
	//private static final String DATABASE_ACCESSION_KEY_PREASSIGN = Utility.DATABASE_ACCESSION_KEY_PREASSIGN;
	public static final String AUTH_CUSTOM = "custom";
    private static ResourceBundle tblabbreviations =
			ResourceBundle.getBundle("accession");

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {


		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		try {
           // TLD Does all this:
//		    if (Utility.isStringNullOrEmpty(getTable())) { log.error("Attribute 'table' is required"); return SKIP_BODY;}
			//if (Utility.isArrayNullOrEmpty(getFieldNamesArray())) { log.error("Attribute 'fieldNames' is required"); return SKIP_BODY;}
//			if (Utility.isStringNullOrEmpty(getRecordcount())) { log.error("Attribute 'recordcount' is required"); return SKIP_BODY;}
//			if (Utility.isStringNullOrEmpty(getRequestkey())) { log.error("Attribute 'requestkey' is required"); return SKIP_BODY;}
          //  String authScheme =  getAuth();
		  //	if (Utility.isStringNullOrEmpty(authScheme)) { authScheme = AUTH_USR_ID; }

            // instantiate this entity
            //Utility.createObject("org.vegbank.common.model." +
            //       Utility.upperCaseFirstLetter(entity.toLowerCase()));
		//	Class entityClass = Class.forName("org.vegbank.common.model." +
        //            Utility.upperCaseFirstLetter(entity.toLowerCase()));
        //    String pkName = (String)entityClass.getField("PKNAME").get(null);
        //    String tableName = entity;


            //the database key
            String dbKey = Utility.DATABASE_ACCESSION_KEY_PREASSIGN;
            //the table:
            log.debug("getting table abbreviations");
			String tableAbbrev = tblabbreviations.getString("abbr." + getTable());

            String reqkey = getRequestkey();
            int reccount = Integer.parseInt(getRecordcount());

              // build INSERT sql
			  StringBuffer sql = new StringBuffer(128);
			  //  StringBuffer selectsql = new StringBuffer(128);
			  //get main clause from SQLStore
			  sql.append("INSERT INTO dba_preassignacccode ( dba_requestnumber, databasekey, tableabbrev) ");
              sql.append(" values ('").append(reqkey).append("','").append(dbKey).append("','").append(tableAbbrev).append("')");

              log.debug("asking for accession codes via " + sql.toString());
              log.debug("asking for accession codes how many times: " + reccount);
              if (reccount > 3000 ) {
                  reccount = 3000; //hardcode limit to request.  clients can repeat requests if needed.  But helps prevent hogging of resources.
              }
              
           DatabaseAccess da = new DatabaseAccess();
          for (int i=0; i< reccount; i++) {
               da.issueUpdate(DatabaseUtility.removeSemicolons(sql.toString()));
		   }
		   log.debug("compiling accession codes");
		   da.issueUpdate("UPDATE dba_preassignacccode SET accessionCode = databasekey || '.' || tableabbrev || '.' || dba_preassignacccode_id || '.' || confirmcode  WHERE accessionCode is null");

           log.debug("finished with inserting new accessionCodes");
		} catch (Exception ex) {
			log.error("Error while running <vegbank:requestaccessioncode> ", ex);
		}


        return SKIP_BODY;
    }

    /**
	 *
     */
  	 protected String table;

      public String getTable() {
          return findAttribute("table", this.table);
      }

      public void setTable(String t) {
          this.table = t;
    }

    /**
	 *
     */
  	 protected String requestkey;

      public String getRequestkey() {
          return findAttribute("requestkey", this.requestkey);
      }

      public void setRequestkey(String rk) {
          this.requestkey = rk;
    }

 /**
	 *
     */
  	 protected String recordcount;

      public String getRecordcount() {
          return findAttribute("recordcount", this.recordcount);
      }

      public void setRecordcount(String r) {
          this.recordcount = r;
    }






}
