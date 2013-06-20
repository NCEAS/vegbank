/*
 *	'$RCSfile: AccessionCode.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-08-11 02:03:59 $'
 *	'$Revision: 1.5 $'
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.utility.AccessionGen;


/**
 * Everything you want to know about and do with
 * an accession code.
 *
 * @author anderson
 */
public class AccessionCode
{
	protected static Log log = LogFactory.getLog(AccessionCode.class);

    private static AccessionGen ag;

    private String ac = null;
    private String dbId = null;
    private String entityCode = null;
    private Long entityId = null;
    private String confirmation = null;


    /**
     *
     */
    public AccessionCode(String ac) {
        this.ac = ac;
        if (!Utility.isStringNullOrEmpty(ac)) {
          if (ac.indexOf("urn:lsid:") == 0) {
			  //have an lsid accessioncode, cannot parse this like a VegBank accessioncode
              //split into parts by colon:

            String[] lsidparts = this.ac.split(":");
            dbId = lsidparts[2];
			entityCode = getShortEntityCode(lsidparts[3]);
			//entityId = Long.valueOf(lsidparts[2]); //leave null, PK is not known without database lookup, which can be done in DatasetUtility.getTablePKFromAccessionCode
            confirmation = lsidparts[4];

            // log.debug("have lsid accession code: " + ac + " parses into db:" + dbId + " and entityCode:" + entityCode + " and confirmation:" + confirmation );
		  } else { //not lsid accessioncode
			String[] parts = this.ac.split("\\.");
            if (parts.length < 4) {
                log.error(ac + " is not a valid Accession Code");
                this.ac = null;
            } else {
                dbId = parts[0];
                entityCode = parts[1];
                entityId = Long.valueOf(parts[2]);
                confirmation = parts[3];
            }
	      }
        }

        if (ag == null) {
            synchronized (this) {
                ag = new AccessionGen();
            }
        }
    }

    /**
     * Gets the short entity code from the full name, e.g., Ob from observation
     */
     private String getShortEntityCode(String longCode) {
		 String shortCode = "";
		 if (longCode.compareToIgnoreCase( "observation" ) == 0) { shortCode = "Ob"; }
		 if (longCode.compareToIgnoreCase( "Plot" ) == 0) { shortCode = "Pl"; }
		 if (longCode.compareToIgnoreCase( "plantconcept" ) == 0) { shortCode = "PC"; }
		 if (longCode.compareToIgnoreCase( "commconcept" ) == 0) { shortCode = "CC"; }
		 if (longCode.compareToIgnoreCase( "party" ) == 0) { shortCode = "Py"; }
		 if (longCode.compareToIgnoreCase( "reference" ) == 0) { shortCode = "Rf"; }
		 if (longCode.compareToIgnoreCase( "referencejournal" ) == 0) { shortCode = "RJ"; }
		 if (longCode.compareToIgnoreCase( "stratummethod" ) == 0) { shortCode = "SM"; }
		 if (longCode.compareToIgnoreCase( "covermethod" ) == 0) { shortCode = "CM"; }
		 if (longCode.compareToIgnoreCase( "namedplace" ) == 0) { shortCode = "NP"; }
		 if (longCode.compareToIgnoreCase( "project" ) == 0) { shortCode = "Pj"; }
		 if (longCode.compareToIgnoreCase( "soiltaxon" ) == 0) { shortCode = "ST"; }
		 if (longCode.compareToIgnoreCase( "userdefined" ) == 0) { shortCode = "UD"; }
		 if (longCode.compareToIgnoreCase( "taxonobservation" ) == 0) { shortCode = "TO"; }
		 if (longCode.compareToIgnoreCase( "commclass" ) == 0) { shortCode = "Cl"; }
		 if (longCode.compareToIgnoreCase( "referenceparty" ) == 0) { shortCode = "RP"; }
		 if (longCode.compareToIgnoreCase( "aux_role" ) == 0) { shortCode = "AR"; }
		 if (longCode.compareToIgnoreCase( "userdataset" ) == 0) { shortCode = "DS"; }
		 if (longCode.compareToIgnoreCase( "taxoninterpretation" ) == 0) { shortCode = "TI"; }
		 if (longCode.compareToIgnoreCase( "plantconcept" ) == 0) { shortCode = "PS"; }
         if (longCode.compareToIgnoreCase( "commconcept" ) == 0) { shortCode = "CS"; }
         return shortCode;
	 }

    /**
     * Gets the database ID, the prefix.
     */
    public String getDatabaseId() {
        //return (dbId == null ? "": dbId);
        return dbId;
    }

    /**
     * Gets the entity code, the second field.
     */
    public String getEntityCode() {
        //return (entityCode == null ? "": entityCode);
        return entityCode;
    }

    /**
     * Gets the entity (table) name, the second field.
     */
    public String getEntityName() {
        String code = getEntityCode();
        if (Utility.isStringNullOrEmpty(code)) {
            return null;
        }
        return ag.getTableName(getEntityCode());
    }

    /**
     * Gets the record ID, the PK, the third field.
     */
    public Long getEntityId() {
        //return (entityId == null ? new Long(0): entityId);
        return entityId;
    }

    /**
     * Gets the confirmation code, the suffix.
     */
    public String getConfirmation() {
        //return (confirmation == null ? "": confirmation);
        return confirmation;
    }

    /**
     *
     */
    public String toString() {
        return ac;
    }

    /**
     *
     */
    public boolean equals(AccessionCode otherAc) {
        return ac.equals(otherAc.toString());
    }

    /**
     *
     */
    public boolean equals(String otherAc) {
        return ac.equals(otherAc);
    }

}

