/*
 *	'$RCSfile: Constants.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-17 02:53:19 $'
 *	'$Revision: 1.10 $'
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
 
package org.vegbank.common;

/**
 * Interface  for holding Constant values. Based on Pattern titled,
 * aptly enough, "Define Constants in Interfaces" [Trost98].
 * 
 * I want to store the the keys here rather than the values ... but the XML 
 * config file reader is not in place yet so store only the values.
 * 
 * @author farrell
 */

public interface Constants
{
	
	public static final String PLANT_CLASS_SPECIES = "Species"; 
	public static final String PLANT_CLASS_SUBSPECIES = "Subspecies"; 	
	public static final String PLANT_CLASS_VARIETY = "Variety"; 
	public static final String PLANT_CLASS_GENUS = "Genus"; 
	public static final String PLANT_CLASS_HYBRID = "Hybrid"; 
	public static final String PLANT_CLASS_FAMILY = "Family"; 
			
	public static final String USAGE_NAME_SCIENTIFIC = "Scientific"; 
	public static final String USAGE_NAME_SCIENTIFIC_NOAUTHORS = "Scientific without authors";
	public static final String USAGE_NAME_CODE = "Code";
	public static final String USAGE_NAME_COMMON = "English Common"; 
	
	public static final String USAGE_NAME_STATUS_STANDARD =  "Standard";
	public static final String USAGE_NAME_STATUS_NOT_STANDARD =  "Not Standard";
	public static final String USAGE_NAME_STATUS_UNDETERMINED =  "Undetermined";	
	
	public static final String CONCEPT_STATUS_ACCEPTED = "accepted";
	public static final String CONCEPT_STATUS_NOT_ACCEPTED = "not accepted";

	// For the web application
	public static final String USER_KEY = "USER";
	public static final String GUEST_USER_KEY = "GUESTUSER@VEGBANK.ORG";
	
	// Application Scope Beans
	public static final String STATELISTBEAN_KEY = "statelistbean"; 
	public static final String COUNTRYLISTBEAN_KEY = "countrylistbean";
	public static final String REGIONLISTBEAN_KEY = "regionlistbean";
	public static final String CERTSTATUSLISTBEAN_KEY = "certstatuslistbean";

	// Common Field Names
	public static final String ACCESSIONCODENAME = "accessionCode";
}
