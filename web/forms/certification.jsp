<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="bean" class="org.vegbank.ui.struts.CertificationForm"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
  *   '$RCSfile: certification.jsp,v $'
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-03-02 22:31:01 $'
  *  '$Revision: 1.8 $'
  *
  *
  -->
<html>
<HEAD>

<TITLE>VegBank Certification</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=">


</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005660" alink="#0066FF">

@vegbank_header_html_normal@

        <h2 align="center" class="vegbank">Certification Application</h2>

		<html:errors/>

        <p class="vegbank_normal">
			Become a certified or professional user to start contributing data to VegBank.
			<br/><b>Please see the <a href="@help-for-certification-href@">explanation here</a></b>
			for more information.
			<br/>
        </p> 
	
	<!-- main table -->
	<table width="700" border="0" cellspacing="5" cellpadding="2">
<html:form method="post" action="SaveCertification.do">
	<html:hidden property="usrId"/>

    <tr> 
      <td colspan="2">
	    <!-- user personal info table -->
		<table bgcolor="#BBBBBB" cellpadding="1" cellspacing="0" border="0">
		<tr>
		<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

		<td>
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" width="360">
			<tr bgcolor="#EBF3F8"> 
			  <td align="center">Your Personal Information</td> 
			</tr>
			<tr bgcolor="#FEFEFE">
			  <td>
			    <p>
				<bean:write name="reqAttribBean" property="givenName"/> 
				<bean:write name="reqAttribBean" property="surName"/>
        		(<bean:write name="reqAttribBean" property="currentCertLevelName"/>)
				<br/>
				<logic:notEqual name="reqAttribBean" property="emailAddress" value="null">
					<bean:write name="reqAttribBean" property="emailAddress"/>
				</logic:notEqual> 
				<logic:notEqual name="reqAttribBean" property="phoneNumber" value="null">
					<bean:write name="reqAttribBean" property="phoneNumber"/>
				</logic:notEqual>
				<br/>
				&nbsp;&raquo; <html:link action="/LoadUser.do">update info</html:link>
			  </td>
			</tr>
			</table>
		</td></tr>
		</table>
    </tr>
    <tr> 
      <td colspan="3"> 
          <font color="red">*</font> 
		  <span class="vegbank_small"> Required field</span>
		  <br/>
          <font color="blue" size=-1>#</font>
		  <span class="vegbank_small"> Field listed in the public registry of certified users</span>
		  <br/>&nbsp;
	  </td>
	</tr>
    <tr> 
      <td> 
		<font color="red">*</font> 
        <span class="vegbank_small">
		Requested certification 
		<br/>
	    <html:select property="requestedCert" size="1">
          <option value="certified">Certified User</option>
          <option value="professional">Professional User</option>
	    </html:select>
		&nbsp;&nbsp;
		<a href="@help-for-userlevels-href@">Which level is right for me?</a>
		</span>
	  </td>
    </tr>
<!--
    <tr> 
      <td><span class="vegbank_small">
        Attach brief CV</span></td>
      <td> 
        <input type="file" name="cv">
        </td>
    </tr>
-->
    <tr> 
      <td>
		<font color="red">*</font> 
	    <span class="vegbank_small">
		Highest degree
        <br/> 
	    <html:select property="highestDegree" size="1">
          <option value="None">None</option>
          <option value="BA/BS">BA/BS</option>
          <option value="MA/MS">MA/MS</option>
          <option value="PhD">PhD</option>
	    </html:select></span>
        </td>
    </tr>
    <tr>
      <td>
	    <span class="vegbank_small">Year of degree</span>
        <br/> 
        <html:text property="degreeYear" size="4" maxlength="4"/>
        </td>
    </tr>
    <tr> 
      <td>
		<font color="blue" size=-1>#</font>
	    <span class="vegbank_small">
		Institution of highest degree
		</span>
        <br/> 
        <html:text property="degreeInst" size="50" maxlength="50"/>
        </td>
    </tr>
    <tr> 
      <td>
		<font color="red">*</font>
		<font color="blue" size=-1>#</font> 
	    <span class="vegbank_small">
		Current organization  
		</span>
		<br/>
        <html:text property="currentOrg" size="50" maxlength="50"/>
        </td>
    </tr>
    <tr> 
      <td>
        <font color="red">*</font>
		<font color="blue" size=-1>#</font> 
	    <span class="vegbank_small">
		Current position </span>
		<br/>
        <html:text property="currentPos" size="50" maxlength="50"/>
        </td>
    </tr>
    <tr> 
      <td>
        <font color="red">*</font>
		<font color="blue" size=-1>#</font> 
	    <span class="vegbank_small">
		ESA Certified Ecologist?</span>
		<br/>
        &nbsp;&nbsp;&nbsp;&raquo;No <html:radio property="esaMember" value="0" title="I am not ESA certified"/>;
        &nbsp;&nbsp;&nbsp;Yes <html:radio property="esaMember" title="I am ESA certified" value="1"/>
        </td>
    </tr>
    <tr> 
      <td colspan="2">
	    <br/>
        <span class="vegbank_small">Other <b>PROFESSIONAL</b> certification or experience?</span>
		<br/>
        <html:textarea property="profExp" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
		<br/>
	    <span class="vegbank_small">
        List up to 5 of your relevant <b>PUBLICATIONS/THESES</b>:
		</span>
		<br/>
        <html:textarea property="relevantPubs" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
        <font color="red">*</font> 
	    <span class="vegbank_small">
		Briefly describe your background and expertise in vegetation <b>SAMPLING</b>:</span>
        <br/>
        <html:textarea property="vegSamplingExp" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
        <font color="red">*</font> 
	    <span class="vegbank_small">
		Briefly describe your background and expertise with vegetation <b>ANALYSIS</b>, description &amp; classification:</span>
        <br/>
        <html:textarea property="vegAnalysisExp" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
        <font color="red">*</font> 
	    <span class="vegbank_small">
		Briefly describe your prior experience with the <b>US National Vegetation Classification</b>, if any:</span>
		<br/>
        <html:textarea property="usnvcExp" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
	    <span class="vegbank_small">
		Briefly describe your prior experience with <b>VEGBANK</b>, if any:</span>
        <br/>
        <html:textarea property="vbExp" cols="60" rows="3"/>
      </td>
    </tr>
  <tr> 
      <td colspan="2">
        <br/>
        <font color="red">*</font> 
	    <span class="vegbank_small">
		How do you <b>INTEND TO USE VEGBANK</b>?</span>
		<br/>
        <html:textarea property="vbIntention" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/> 
	    <span class="vegbank_small">
		Briefly describe your prior experience with vegetation plot <b>DATABASES</b> and/or analytical <b>TOOLS</b>:</span>
		<br/>
        <html:textarea property="toolsExp" cols="60" rows="3"/>
      </td>
    </tr> 
	<tr bgcolor="#FFFFFF"> 
	  <td colspan="4">
	    <br/> 
	    <font color="blue" size=-1>#</font> 
		<span class="vegbank_small">
	    Please rate your expertise for the <b>3 REGIONS</b> you know best in terms 
	    of knowledge of the vegetation, the flora, and the US-NVC (1=Weak, 5=Expert) 
		</span>
	    <br/>

		<!-- Region expertise table -->
		<table bgcolor="#FFFFFF" cellpadding="1" cellspacing="0" border="0">
		<tr>
		<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

		<td bgcolor="#BBBBBB">
		<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" width="560">
		<tr bgcolor="#EBF3F8"> 
		  <td width="40%" align="center">Region</td> 
		  <td width="20%" align="center">Vegetation</td>
		  <td width="20%" align="center">Floristics</td>
		  <td width="20%" align="center">US-NVC</td>
		</tr>
		<tr bgcolor="#666666"> 
		  <td colspan="7"><img src="@image_server@pix_clear" width="1" height="1"></td>
		</tr>

		<tr bgcolor="#FFFFFF"> 
		  <td align="center"> 
			<html:select property="expRegionA">
			  <option value="">--select a region--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<html:select property="expRegionAVeg">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionAFlor">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionANVC">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		</tr>
		<tr bgcolor="#EEEEEE"> 
		  <td align="center">
			<html:select property="expRegionB">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionBVeg">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionBFlor">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionBNVC">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		</tr>
		<tr bgcolor="#FFFFFF"> 
		  <td align="center">
			<html:select property="expRegionC">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
			</td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<html:select property="expRegionCVeg">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td align="center"> 
			<html:select property="expRegionCFlor">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<html:select property="expRegionCNVC">
			  <option value="0">0</option>
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			</html:select>
		  </td>
		</tr>
		  </table>
		</td></tr>
		</table>
      </td>
    </tr>
    <tr> 
      <td colspan="2"></td>
    </tr>
	<tr bgcolor="#FFFFFF">
	  <td colspan="5">
	  	<br/>
		  <span class="vegbank_small">
		  List up to two ESA members who are familiar with your background and 
		  expertise and who have agreed to sponsor your certification.
		  </span>
		  <br/>
		    <!-- sponsor table -->
			<table bgcolor="#FFFFFF" cellpadding="1" cellspacing="1" border="0">
			<tr>
			<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

			<td bgcolor="#BBBBBB">
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" width="560">
			<tr bgcolor="#EBF3F8"> 
			  <td colspan="2" align="center">Member #1</td>
			  <td colspan="2" align="center">Member #2</td>
			</tr>
			<tr bgcolor="#666666"> 
			  <td colspan="5"><img src="@image_server@pix_clear" width="1" height="1"></td>
			</tr>

			<tr bgcolor="#FFFFFF"> 
			  <td colspan="2">
			    <table>
				<tr>
				  <td align="right"><span class="vegclass_small">Name: </span></td>
				  <td><html:text property="esaSponsorNameA" size="25" maxlength="50"/></td>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><html:text property="esaSponsorEmailA" size="25"/></td>
				</tr>
				</table>
			  </td>

			  <td colspan="2">
			    <table>
				<tr>
				  <td align="right"><span class="vegclass_small">Name: </span></td>
				  <td><html:text property="esaSponsorNameB" size="25" maxlength="50"/></td>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><html:text property="esaSponsorEmailB" size="25"/></td>
				</tr></table>
			  </td>
			</tr>

			</table>
			</td></tr>
			</table>

   <tr> 
      <td colspan="2">
		<br/>
	    <span class="vegbank_small">
        Are you interested in serving as a <b>PEER REVIEWER</b> for proposed changes in the US-NVC?
        </span>
		<br/>
		&nbsp;&nbsp;&nbsp;&raquo; <html:checkbox property="peerReview" value="1" title="Be a peer reviewer"/>
		 Yes, I am interested in being a peer reviewer.
		</td>
    </tr>

    <tr> 
      <td colspan="2">
	    <br/>
		<span class="vegbank_small">
        Additional statements:
		</span>
        <br/>
		<html:textarea property="addlStmt" rows="3" cols="60"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
        <html:submit value="Request Certification"/>
      </td>
    </tr>
</html:form>
  </table>
          <font color="red">*</font> 
		  <span class="vegbank_small"> Required fields</span>
		  <br/>
          <font color="blue" size=-1>#</font>
		  <span class="vegbank_small"> Field listed in the public registry of certified users</span>
          <br/> &nbsp;


@vegbank_footer_html_onerow@
</BODY>
</HTML>
