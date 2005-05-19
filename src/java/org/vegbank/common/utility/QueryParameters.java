/*
 *	'$RCSfile: QueryParameters.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-05-19 01:29:44 $'
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

import java.util.*;
import org.vegbank.ui.struts.taglib.VegbankGetTag;

/**
 * Used by VegbankGetTag and GenericCommand.
 */
public class QueryParameters extends HashMap {
    // for now it's just a HashMap
    public QueryParameters() {
        super();
    }

    public QueryParameters(VegbankGetTag getTag) {
        super();
        this.put("select", getTag.getSelect());
        this.put("where", getTag.getWhere());
        this.put("beanName", getTag.getBeanName());
        this.put("pageNumber", getTag.getPageNumber());
        this.put("perPage", getTag.getPerPage());
        this.put("wparam", getTag.getWparam());
        this.put("id", getTag.getId());
        this.put("pager", new Boolean(getTag.getPager()));
        this.put("whereNumeric", getTag.getWhereNumeric());
        this.put("whereNonNumeric", getTag.getWhereNonNumeric());
        this.put("xwhereSearch", new Boolean(getTag.getXwhereSearch()));
        this.put("xwhereMatchAny", new Boolean(getTag.getXwhereMatchAny()));
        this.put("xwhereEnable", new Boolean(getTag.getXwhereEnable()));
        this.put("xwhereKey", getTag.getXwhereKey());
        this.put("xwhereParams", getTag.getXwhereParams());
        this.put("xwhereGlue", getTag.getXwhereGlue());
        this.put("orderBy", getTag.getOrderBy());
        this.put("allowOrderBy", new Boolean(getTag.getAllowOrderBy()));
        this.put("showQuery", new Boolean(getTag.getShowQuery()));
        this.put("save", getTag.getSave());
        this.put("load", getTag.getLoad());
    }


    /**
     * Get a parameter as a String.
     */
    public String getString(String key) {
        if (Utility.isStringNullOrEmpty(key)) {
            return null;
        } else {
            return (String)this.get(key);
        }
    }

    /**
     * Get a parameter as a String with a default value.
     */
    public String getString(String key, String def) {
        if (Utility.isStringNullOrEmpty(key)) {
            return def;
        } else {
            String value = (String)get(key);
            if (Utility.isStringNullOrEmpty(value)) {
                return def;
            } else {
                return value;
            }
        }
    }

}

