/**	
 *  '$RCSfile: CertificationForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-01-16 02:14:58 $'
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
 
package org.vegbank.ui.struts;

import java.sql.*;

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author anderson
 */

public class CertificationForm extends ValidatorForm 
			implements java.io.Serializable
{
	// prepopulated fields
	private String emailAddress;
	private String surName;
	private String givenName;
	private String phoneNumber;
	private String currentCertLevel;
	
	// user enters these fields
	private String requestedCert;
	private String highestDegree;
	private String degreeYear;
	private String degreeInst;
	private String currentOrg;
	private String currentPos;
	private String esaMember="no";

	private String profExp;
	private String relevantPubs;
	private String vegSamplingExp;
	private String vegAnalysisExp;
	private String usnvcExp;
	private String vbExp;
	private String vbIntention;
	private String toolsExp;

	private String nvcExpRegionA;
	private String nvcExpVegA;
	private String nvcExpFloristicsA;
	private String nvcExpNVCA;
	private String nvcExpRegionB;
	private String nvcExpVegB;
	private String nvcExpFloristicsB;
	private String nvcExpNVCB;
	private String nvcExpRegionC;
	private String nvcExpVegC;
	private String nvcExpFloristicsC;
	private String nvcExpNVCC;
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
//	public String getSubmittedEmail() {
//		return submittedEmail;
//	}

	/**
	 * @param string
	 */
//	public void setSubmittedEmail(String submittedEmail) {
//		this.submittedEmail = submittedEmail;
//	}

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
	public String getGivenName () {
		return givenName;
	}

	/**
	 * @param string
	 */
	public void setGivenName (String givenName ) {
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
	 * @return abbreviated name of role
	 */
	public String getCurrentCertLevel() {
		return currentCertLevel;
	}

	/**
	 * @param string must be either "cert" or "pro"
	 */
	public void setCurrentCertLevel(String currentCertLevel) {
		this.currentCertLevel = currentCertLevel;
	}

	/**
	 * Get value of 'requestedCert' property.
	 */
	public String getRequestedCert() {
		return this.requestedCert;
	}

	/**
	 * Set the 'requestedCert' property.
	 */
	public void setRequestedCert(String requestedCert) {
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
		this.esaMember = esaMember;
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
	public String getNvcExpRegionA() {
		return nvcExpRegionA;
	}

	/**
	 * @param string
	 */
	public void setNvcExpRegionA(String nvcExpRegionA) {
		this.nvcExpRegionA = nvcExpRegionA;
	}

	/**
	 * @return
	 */
	public String getNvcExpVegA() {
		return nvcExpVegA;
	}

	/**
	 * @param string
	 */
	public void setNvcExpVegA(String nvcExpVegA) {
		this.nvcExpVegA = nvcExpVegA;
	}

	/**
	 * @return
	 */
	public String getNvcExpFloristicsA() {
		return nvcExpFloristicsA;
	}

	/**
	 * @param string
	 */
	public void setNvcExpFloristicsA(String nvcExpFloristicsA) {
		this.nvcExpFloristicsA = nvcExpFloristicsA;
	}

	/**
	 * @return
	 */
	public String getNvcExpNVCA() {
		return nvcExpNVCA;
	}

	/**
	 * @param string
	 */
	public void setNvcExpNVCA(String nvcExpNVCA) {
		this.nvcExpNVCA = nvcExpNVCA;
	}

	/**
	 * Get value of 'nvcExpRegionB' property.
	 */
	public String getNvcExpRegionB() {
		return this.nvcExpRegionB;
	}

	/**
	 * Set the 'nvcExpRegionB' property.
	 */
	public void setNvcExpRegionB(String nvcExpRegionB) {
		this.nvcExpRegionB = nvcExpRegionB;
	}

	/**
	 * Get value of 'nvcExpVegB' property.
	 */
	public String getNvcExpVegB() {
		return this.nvcExpVegB;
	}

	/**
	 * Set the 'nvcExpVegB' property.
	 */
	public void setNvcExpVegB(String nvcExpVegB) {
		this.nvcExpVegB = nvcExpVegB;
	}

	/**
	 * Get value of 'nvcExpFloristicsB' property.
	 */
	public String getNvcExpFloristicsB() {
		return this.nvcExpFloristicsB;
	}

	/**
	 * Set the 'nvcExpFloristicsB' property.
	 */
	public void setNvcExpFloristicsB(String nvcExpFloristicsB) {
		this.nvcExpFloristicsB = nvcExpFloristicsB;
	}

	/**
	 * Get value of 'nvcExpNVCB' property.
	 */
	public String getNvcExpNVCB() {
		return this.nvcExpNVCB;
	}

	/**
	 * Set the 'nvcExpNVCB' property.
	 */
	public void setNvcExpNVCB(String nvcExpNVCB) {
		this.nvcExpNVCB = nvcExpNVCB;
	}

	/**
	 * Get value of 'nvcExpRegionC' property.
	 */
	public String getNvcExpRegionC() {
		return this.nvcExpRegionC;
	}

	/**
	 * Set the 'nvcExpRegionC' property.
	 */
	public void setNvcExpRegionC(String nvcExpRegionC) {
		this.nvcExpRegionC = nvcExpRegionC;
	}

	/**
	 * Get value of 'nvcExpVegC' property.
	 */
	public String getNvcExpVegC() {
		return this.nvcExpVegC;
	}

	/**
	 * Set the 'nvcExpVegC' property.
	 */
	public void setNvcExpVegC(String nvcExpVegC) {
		this.nvcExpVegC = nvcExpVegC;
	}

	/**
	 * Get value of 'nvcExpFloristicsC' property.
	 */
	public String getNvcExpFloristicsC() {
		return this.nvcExpFloristicsC;
	}

	/**
	 * Set the 'nvcExpFloristicsC' property.
	 */
	public void setNvcExpFloristicsC(String nvcExpFloristicsC) {
		this.nvcExpFloristicsC = nvcExpFloristicsC;
	}

	/**
	 * Get value of 'nvcExpNVCC' property.
	 */
	public String getNvcExpNVCC() {
		return this.nvcExpNVCC;
	}

	/**
	 * Set the 'nvcExpNVCC' property.
	 */
	public void setNvcExpNVCC(String nvcExpNVCC) {
		this.nvcExpNVCC = nvcExpNVCC;
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
		this.peerReview = peerReview;
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

}
