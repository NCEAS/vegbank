/**	
 *  '$RCSfile: CertificationForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-04-30 13:03:53 $'
 *	'$Revision: 1.9 $'
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
 
package org.vegbank.ui.struts;

import java.sql.*;

import org.apache.struts.validator.ValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.utility.DatabaseAccess;
import org.vegbank.common.utility.PermComparison;

/**
 * @author anderson
 */

public class CertificationForm extends ValidatorForm 
			implements java.io.Serializable
{
	private static Log log = LogFactory.getLog(CertificationForm.class);

	// prepopulated fields
	private long usrId;
	private String emailAddress;
	private String surName;
	private String givenName;
	private String phoneNumber;
	private long currentCertLevel;
	private String currentCertLevelName;

	// admin fields
	private long certId;
	private String certificationstatus;
	private String certificationstatuscomments;
	
	// user enters these fields
	private long requestedCert;
	private String highestDegree;
	private String degreeYear;
	private String degreeInst;
	private String currentOrg;
	private String currentPos;
	private String esaMember="0";

	private String profExp;
	private String relevantPubs;
	private String vegSamplingExp;
	private String vegAnalysisExp;
	private String usnvcExp;
	private String vbExp;
	private String vbIntention;
	private String toolsExp;

	private String expRegionA;
	private String expRegionAVeg;
	private String expRegionAFlor;
	private String expRegionANVC;
	private String expRegionB;
	private String expRegionBVeg;
	private String expRegionBFlor;
	private String expRegionBNVC;
	private String expRegionC;
	private String expRegionCVeg;
	private String expRegionCFlor;
	private String expRegionCNVC;
	private String esaSponsorNameA;
	private String esaSponsorEmailA;
	private String esaSponsorNameB;
	private String esaSponsorEmailB;
	private String peerReview;
	private String addlStmt;


	/**
	 * Constructor
	 */
	public CertificationForm() {
		super();
	}
	
	/**
	 * Get value of 'certId' property.
	 */
	public long getCertId() {
		return this.certId;
	}

	/**
	 * Set the 'certId' property.
	 */
	public void setCertId(long certId) {
		this.certId = certId;
	}


	/**
	 * Get value of 'usrId' property.
	 */
	public long getUsrId() {
		return this.usrId;
	}

	/**
	 * Set the 'usrId' property.
	 */
	public void setUsrId(long usrId) {
		this.usrId = usrId;
	}

	/**
	 * @return
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param string
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return
	 */
	public String getSurName() {
		return surName;
	}

	/**
	 * @param string
	 */
	public void setSurName(String surName) {
		this.surName = surName;
	}

	/**
	 * @return
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @param string
	 */
	public void setGivenName(String givenName ) {
		this.givenName  = givenName;
	}

	/**
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param string
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return long sum of all roles
	 */
	public long getCurrentCertLevel() {
		return currentCertLevel;
	}

	/**
	 * @param currentCertLevel 
	 */
	public void setCurrentCertLevel(long currentCertLevel) {
		this.currentCertLevel = currentCertLevel;
	}

	/**
	 * Get value of 'currentCertLevelName' property.
	 * @return name of role
	 */
	public String getCurrentCertLevelName() {
		this.currentCertLevelName = 
			PermComparison.getRoleNamesCSVFromSum(this.currentCertLevel);
		return this.currentCertLevelName;
	}

	/**
	 * Set the 'currentCertLevelName' property.
	 */
	public void setCurrentCertLevelName(String currentCertLevelName) {
		this.currentCertLevelName = currentCertLevelName;
	}

	/**
	 * Get name based on 'requestedCert' property.
	 */
	public String getRequestedCertName() {
		return PermComparison.getRoleNamesCSVFromSum(this.requestedCert);
	}

	/**
	 * Set value of 'currentCertLevel' (long) by looking up String.
	 */
	public void setRequestedCertName(String requestedCertName) {
		this.requestedCert = 
			PermComparison.getRoleConstant(requestedCertName);
	}

	/**
	 * Get value of 'requestedCert' property.
	 */
	public long getRequestedCert() {
		return this.requestedCert;
	}

	/**
	 * Set the 'requestedCert' property.
	 */
	public void setRequestedCert(long requestedCert) {
		this.requestedCert = requestedCert;
	}

	/**
	 * @return
	 */
	public String getHighestDegree() {
		return highestDegree;
	}

	/**
	 * @param string
	 */
	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	/**
	 * @return
	 */
	public String getDegreeYear() {
		return degreeYear;
	}

	/**
	 * @param string
	 */
	public void setDegreeYear(String degreeYear) {
		this.degreeYear = degreeYear;
	}

	/**
	 * @return
	 */
	public String getCurrentOrg() {
		return currentOrg;
	}

	/**
	 * @param string
	 */
	public void setCurrentOrg(String currentOrg) {
		this.currentOrg = currentOrg;
	}

	/**
	 * @return
	 */
	public String getDegreeInst() {
		return degreeInst;
	}

	/**
	 * @param string
	 */
	public void setDegreeInst(String degreeInst) {
		this.degreeInst = degreeInst;
	}

	/**
	 * @return
	 */
	public String getCurrentPos() {
		return currentPos;
	}

	/**
	 * @param string
	 */
	public void setCurrentPos(String currentPos) {
		this.currentPos = currentPos;
	}

	/**
	 * @return
	 */
	public String getEsaMember() {
		return esaMember;
	}

	/**
	 * @param string
	 */
	public void setEsaMember(String esaMember) {
		if (esaMember == null) {
			this.esaMember = "0";
			return;
		}

		if (esaMember.equals("false") || esaMember.equalsIgnoreCase("f") || 
					esaMember.equalsIgnoreCase("0")) {
			// false
			this.esaMember = "0";

		} else if (esaMember.equals("true") || esaMember.equalsIgnoreCase("t") || 
					esaMember.equalsIgnoreCase("1")) {
			// true
			this.esaMember = "1";
		} else {
			// wTF?
			this.esaMember = esaMember;
		}
	}

	/**
	 * @return
	 */
	public String getProfExp() {
		return profExp;
	}

	/**
	 * @param string
	 */
	public void setProfExp(String profExp) {
		this.profExp= profExp;
	}

	/**
	 * @return
	 */
	public String getRelevantPubs() {
		return relevantPubs;
	}

	/**
	 * @param string
	 */
	public void setRelevantPubs(String relevantPubs) {
		this.relevantPubs = relevantPubs;
	}

	/**
	 * @return
	 */
	public String getVegSamplingExp() {
		return vegSamplingExp;
	}

	/**
	 * @param string
	 */
	public void setVegSamplingExp(String vegSamplingExp) {
		this.vegSamplingExp = vegSamplingExp;
	}

	/**
	 * @return
	 */
	public String getVegAnalysisExp() {
		return vegAnalysisExp;
	}

	/**
	 * @param string
	 */
	public void setVegAnalysisExp(String vegAnalysisExp) {
		this.vegAnalysisExp = vegAnalysisExp;
	}

	/**
	 * @return
	 */
	public String getUsnvcExp() {
		return usnvcExp;
	}

	/**
	 * @param string
	 */
	public void setUsnvcExp(String usnvcExp) {
		this.usnvcExp= usnvcExp;
	}

	/**
	 * @return
	 */
	public String getVbExp() {
		return vbExp;
	}

	/**
	 * @param string
	 */
	public void setVbExp(String vbExp) {
		this.vbExp= vbExp;
	}

	/**
	 * @return
	 */
	public String getVbIntention() {
		return vbIntention;
	}

	/**
	 * @param string
	 */
	public void setVbIntention(String vbIntention) {
		this.vbIntention = vbIntention;
	}

	/**
	 * @return
	 */
	public String getToolsExp() {
		return toolsExp;
	}

	/**
	 * @param string
	 */
	public void setToolsExp(String toolsExp) {
		this.toolsExp = toolsExp;
	}

	/**
	 * @return
	 */
	public String getExpRegionA() {
		return expRegionA;
	}

	/**
	 * @param string
	 */
	public void setExpRegionA(String expRegionA) {
		this.expRegionA = expRegionA;
	}

	/**
	 * @return
	 */
	public String getExpRegionAVeg() {
		return expRegionAVeg;
	}

	/**
	 * @param string
	 */
	public void setExpRegionAVeg(String expRegionAVeg) {
		this.expRegionAVeg = expRegionAVeg;
	}

	/**
	 * @return
	 */
	public String getExpRegionAFlor() {
		return expRegionAFlor;
	}

	/**
	 * @param string
	 */
	public void setExpRegionAFlor(String expRegionAFlor) {
		this.expRegionAFlor = expRegionAFlor;
	}

	/**
	 * @return
	 */
	public String getExpRegionANVC() {
		return expRegionANVC;
	}

	/**
	 * @param string
	 */
	public void setExpRegionANVC(String expRegionANVC) {
		this.expRegionANVC = expRegionANVC;
	}

	/**
	 * Get value of 'expRegionB' property.
	 */
	public String getExpRegionB() {
		return this.expRegionB;
	}

	/**
	 * Set the 'expRegionB' property.
	 */
	public void setExpRegionB(String expRegionB) {
		this.expRegionB = expRegionB;
	}

	/**
	 * Get value of 'expRegionBVeg' property.
	 */
	public String getExpRegionBVeg() {
		return this.expRegionBVeg;
	}

	/**
	 * Set the 'expRegionBVeg' property.
	 */
	public void setExpRegionBVeg(String expRegionBVeg) {
		this.expRegionBVeg = expRegionBVeg;
	}

	/**
	 * Get value of 'expRegionBFlor' property.
	 */
	public String getExpRegionBFlor() {
		return this.expRegionBFlor;
	}

	/**
	 * Set the 'expRegionBFlor' property.
	 */
	public void setExpRegionBFlor(String expRegionBFlor) {
		this.expRegionBFlor = expRegionBFlor;
	}

	/**
	 * Get value of 'expRegionBNVC' property.
	 */
	public String getExpRegionBNVC() {
		return this.expRegionBNVC;
	}

	/**
	 * Set the 'expRegionBNVC' property.
	 */
	public void setExpRegionBNVC(String expRegionBNVC) {
		this.expRegionBNVC = expRegionBNVC;
	}

	/**
	 * Get value of 'expRegionC' property.
	 */
	public String getExpRegionC() {
		return this.expRegionC;
	}

	/**
	 * Set the 'expRegionC' property.
	 */
	public void setExpRegionC(String expRegionC) {
		this.expRegionC = expRegionC;
	}

	/**
	 * Get value of 'expRegionCVeg' property.
	 */
	public String getExpRegionCVeg() {
		return this.expRegionCVeg;
	}

	/**
	 * Set the 'expRegionCVeg' property.
	 */
	public void setExpRegionCVeg(String expRegionCVeg) {
		this.expRegionCVeg = expRegionCVeg;
	}

	/**
	 * Get value of 'expRegionCFlor' property.
	 */
	public String getExpRegionCFlor() {
		return this.expRegionCFlor;
	}

	/**
	 * Set the 'expRegionCFlor' property.
	 */
	public void setExpRegionCFlor(String expRegionCFlor) {
		this.expRegionCFlor = expRegionCFlor;
	}

	/**
	 * Get value of 'expRegionCNVC' property.
	 */
	public String getExpRegionCNVC() {
		return this.expRegionCNVC;
	}

	/**
	 * Set the 'expRegionCNVC' property.
	 */
	public void setExpRegionCNVC(String expRegionCNVC) {
		this.expRegionCNVC = expRegionCNVC;
	}

	/**
	 * @return
	 */
	public String getEsaSponsorNameA () {
		return esaSponsorNameA ;
	}

	/**
	 * @param string
	 */
	public void setEsaSponsorNameA (String esaSponsorNameA ) {
		this.esaSponsorNameA  = esaSponsorNameA ;
	}

	/**
	 * @return
	 */
	public String getEsaSponsorEmailA() {
		return esaSponsorEmailA;
	}

	/**
	 * @param string
	 */
	public void setEsaSponsorEmailA(String esaSponsorEmailA) {
		this.esaSponsorEmailA = esaSponsorEmailA;
	}

	/**
	 * @return
	 */
	public String getEsaSponsorNameB() {
		return esaSponsorNameB;
	}

	/**
	 * @param string
	 */
	public void setEsaSponsorNameB(String esaSponsorNameB) {
		this.esaSponsorNameB = esaSponsorNameB;
	}

	/**
	 * @return
	 */
	public String getEsaSponsorEmailB() {
		return esaSponsorEmailB;
	}

	/**
	 * @param string
	 */
	public void setEsaSponsorEmailB(String esaSponsorEmailB) {
		this.esaSponsorEmailB = esaSponsorEmailB;
	}

	/**
	 * @return
	 */
	public String getPeerReview() {
		return peerReview;
	}

	/**
	 * @param string
	 */
	public void setPeerReview(String peerReview) {
		if (peerReview == null) {
			this.peerReview = "0";
			return;
		}

		if (peerReview.equals("false") || peerReview.equalsIgnoreCase("f") || 
					peerReview.equalsIgnoreCase("0")) {
			// false
			this.peerReview = "0";

		} else if (peerReview.equals("true") || peerReview.equalsIgnoreCase("t") || 
					peerReview.equalsIgnoreCase("1")) {
			// true
			this.peerReview = "1";
		} else {
			// wTF?
			this.peerReview = peerReview;
		}
	}

	/**
	 * @return
	 */
	public String getAddlStmt() {
		return addlStmt;
	}

	/**
	 * @param string
	 */
	public void setAddlStmt(String addlStmt) {
		this.addlStmt = addlStmt;
	}

	/**
	 * Get value of 'certificationstatuscomments' property.
	 */
	public String getCertificationstatuscomments() {
		return this.certificationstatuscomments;
	}

	/**
	 * Set the 'certificationstatuscomments' property.
	 */
	public void setCertificationstatuscomments(String certificationstatuscomments) {
		this.certificationstatuscomments = certificationstatuscomments;
	}


	/**
	 * Get value of 'certificationstatus' property.
	 */
	public String getCertificationstatus() {
		return this.certificationstatus;
	}

	/**
	 * Set the 'certificationstatus' property.
	 */
	public void setCertificationstatus(String certificationstatus) {
		this.certificationstatus = certificationstatus;
	}

	/**
	 *
	 */
	public static String getDBFieldNames(boolean headersOnly) {
		StringBuffer sb = new StringBuffer(512);
		if (headersOnly) {
			sb.append(" current_cert_level, requested_cert_level, usr_id, ")
				.append(" certificationstatus, certificationstatuscomments, usercertification_id");
		} else {
			sb.append(" current_cert_level, requested_cert_level, ")
				.append(" highest_degree, degree_year, degree_institution, current_org, ")
				.append(" current_pos, esa_member, prof_exp, relevant_pubs, veg_sampling_exp, ")
				.append(" veg_analysis_exp, usnvc_exp, vb_exp, tools_exp, vb_intention,")
				.append(" exp_region_a, exp_region_a_veg, exp_region_a_flor, exp_region_a_nvc, ")
				.append(" exp_region_b, exp_region_b_veg, exp_region_b_flor, exp_region_b_nvc, ")
				.append(" exp_region_c, exp_region_c_veg, exp_region_c_flor, exp_region_c_nvc, ")
				.append(" esa_sponsor_name_a,  esa_sponsor_email_a, ")
				.append(" esa_sponsor_name_b,  esa_sponsor_email_b, ")
				.append(" peer_review, addl_stmt, usr_id, ")
				.append(" certificationstatus, certificationstatuscomments, usercertification_id");
		}

		return sb.toString();
	}

	/**
	 * 
	 */
	public void initFromResultSet(ResultSet results, boolean headersOnly) 
			throws SQLException
	{
		if (results == null) {
			return;
		}

		if (headersOnly) {
			this.setCurrentCertLevel( results.getLong(1) );
			this.setRequestedCert( results.getLong(2) );
			this.setUsrId( results.getInt(3) );
			this.setCertificationstatus( results.getString(4) );
			this.setCertificationstatuscomments( results.getString(5) );
			this.setCertId( results.getLong(6) );

		} else {
			this.setCurrentCertLevel( results.getLong(1) );
			this.setRequestedCert( results.getLong(2) );
			this.setHighestDegree( results.getString(3) );
			this.setDegreeYear( results.getString(4) );
			this.setDegreeInst( results.getString(5) );
			this.setCurrentOrg( results.getString(6) );
			this.setCurrentPos( results.getString(7) );
			this.setEsaMember( results.getString(8) );
			this.setProfExp( results.getString(9) );
			this.setRelevantPubs( results.getString(10) );
			this.setVegSamplingExp( results.getString(11) );
			this.setVegAnalysisExp( results.getString(12) );
			this.setUsnvcExp( results.getString(13) );
			this.setVbExp( results.getString(14) );
			this.setToolsExp( results.getString(15) );
			this.setVbIntention( results.getString(16) );
			this.setExpRegionA( results.getString(17) );
			this.setExpRegionAVeg( results.getString(18) );
			this.setExpRegionAFlor( results.getString(19) );
			this.setExpRegionANVC( results.getString(20) );
			this.setExpRegionB( results.getString(21) );
			this.setExpRegionBVeg( results.getString(22) );
			this.setExpRegionBFlor( results.getString(23) );
			this.setExpRegionBNVC( results.getString(24) );
			this.setExpRegionC( results.getString(25) );
			this.setExpRegionCVeg( results.getString(26) );
			this.setExpRegionCFlor( results.getString(27) );
			this.setExpRegionCNVC( results.getString(28) );
			this.setEsaSponsorNameA( results.getString(29) );
			this.setEsaSponsorEmailA( results.getString(30) );
			this.setEsaSponsorNameB( results.getString(31) );
			this.setEsaSponsorEmailB( results.getString(32) );
			this.setPeerReview( results.getString(33) );
			this.setAddlStmt( results.getString(34) );
			this.setUsrId( results.getInt(35) );
			this.setCertificationstatus( results.getString(36) );
			this.setCertificationstatuscomments( results.getString(37) );
			this.setCertId( results.getLong(38) );
		}
	}
}
