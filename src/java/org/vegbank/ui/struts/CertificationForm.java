/* *	'$RCSfile: CertificationForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-26 00:46:40 $'
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
 
package org.vegbank.ui.struts;

import java.sql.*;

import org.apache.struts.action.ActionForm;
import org.vegbank.common.command.GenericCommand;
import org.vegbank.common.utility.DatabaseAccess;

/**
 * @author anderson
 */

public class CertificationForm extends ActionForm implements java.io.Serializable
{
	private String emailAddress;
	private String submittedEmail;
	private String surName;
	private String givenName ;
	private String phoneNumber;
	private String phoneType;
	private String currentCertLevel;
	private String cvDoc;
	private String highestDegree;
	private String degreeYear;
	private String currentInst;
	private String degreeInst;
	private String currentPos;
	private String esaPos;
	private String profExperienceDoc;
	private String relevantPubs;
	private String vegSamplingDoc;
	private String vegAnalysisDoc;
	private String usnvcExpDoc;
	private String vegbankExpDoc;
	private String useVegbank;
	private String plotdbDoc;
	private String nvcExpRegionA;
	private String nvcExpVegA;
	private String nvcExpFloristicsA;
	private String nvcExpNVCA;
	private String esaSponsorNameA ;
	private String esaSponsorEmailA;
	private String esaSponsorNameB;
	private String esaSponsorEmailB;
	private String peerReview;
	private String additionalStatements;


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
	public String getSubmittedEmail() {
		return submittedEmail;
	}

	/**
	 * @param string
	 */
	public void setSubmittedEmail(String submittedEmail) {
		this.submittedEmail = submittedEmail;
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
	public String getGivenName () {
		return givenName ;
	}

	/**
	 * @param string
	 */
	public void setGivenName (String givenName ) {
		this.givenName  = givenName ;
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
	 * @return
	 */
	public String getPhoneType() {
		return phoneType;
	}

	/**
	 * @param string
	 */
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	/**
	 * @return
	 */
	public String getCurrentCertLevel() {
		return currentCertLevel;
	}

	/**
	 * @param string
	 */
	public void setCurrentCertLevel(String currentCertLevel) {
		this.currentCertLevel = currentCertLevel;
	}

	/**
	 * @return
	 */
	public String getCvDoc() {
		return cvDoc;
	}

	/**
	 * @param string
	 */
	public void setCvDoc(String cvDoc) {
		this.cvDoc = cvDoc;
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
	public String getCurrentInst() {
		return currentInst;
	}

	/**
	 * @param string
	 */
	public void setCurrentInst(String currentInst) {
		this.currentInst = currentInst;
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
	public String getEsaPos() {
		return esaPos;
	}

	/**
	 * @param string
	 */
	public void setEsaPos(String esaPos) {
		this.esaPos = esaPos;
	}

	/**
	 * @return
	 */
	public String getProfExperienceDoc() {
		return profExperienceDoc;
	}

	/**
	 * @param string
	 */
	public void setProfExperienceDoc(String profExperienceDoc) {
		this.profExperienceDoc = profExperienceDoc;
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
	public String getVegSamplingDoc() {
		return vegSamplingDoc;
	}

	/**
	 * @param string
	 */
	public void setVegSamplingDoc(String vegSamplingDoc) {
		this.vegSamplingDoc = vegSamplingDoc;
	}

	/**
	 * @return
	 */
	public String getVegAnalysisDoc() {
		return vegAnalysisDoc;
	}

	/**
	 * @param string
	 */
	public void setVegAnalysisDoc(String vegAnalysisDoc) {
		this.vegAnalysisDoc = vegAnalysisDoc;
	}

	/**
	 * @return
	 */
	public String getUsnvcExpDoc() {
		return usnvcExpDoc;
	}

	/**
	 * @param string
	 */
	public void setUsnvcExpDoc(String usnvcExpDoc) {
		this.usnvcExpDoc = usnvcExpDoc;
	}

	/**
	 * @return
	 */
	public String getVegbankExpDoc() {
		return vegbankExpDoc;
	}

	/**
	 * @param string
	 */
	public void setVegbankExpDoc(String vegbankExpDoc) {
		this.vegbankExpDoc = vegbankExpDoc;
	}

	/**
	 * @return
	 */
	public String getUseVegbank() {
		return useVegbank;
	}

	/**
	 * @param string
	 */
	public void setUseVegbank(String useVegbank) {
		this.useVegbank = useVegbank;
	}

	/**
	 * @return
	 */
	public String getPlotdbDoc() {
		return plotdbDoc;
	}

	/**
	 * @param string
	 */
	public void setPlotdbDoc(String plotdbDoc) {
		this.plotdbDoc = plotdbDoc;
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
	public String getAdditionalStatements() {
		return additionalStatements;
	}

	/**
	 * @param string
	 */
	public void setAdditionalStatements(String additionalStatements) {
		this.additionalStatements = additionalStatements;
	}

}
