<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="certBean" class="org.vegbank.ui.struts.CertificationForm"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
  *   '$Id: view_certification.jsp,v 1.2 2004-04-08 05:44:30 mlee Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2004-04-08 05:44:30 $'
  *  '$Revision: 1.2 $'
  *
  *
  -->
<html>
<HEAD>@defaultHeadToken@

<TITLE>VegBank Certification Administration</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=">


</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005660" alink="#0066FF">

@vegbank_header_html_normal@

        <h2 align="center" class="vegbank">Certification Application: 
			<i><bean:write name="reqCertBean" property="surName"/></i>
		</h2>

		<html:errors/>

        <p class="vegbank_normal">
        </p> 
	
	<!-- main table -->
	<table width="700" border="0" cellspacing="5" cellpadding="2">
	<html:form method="post" action="ViewCertification.do">
        <input type="hidden" value="cert" name="<bean:write property="cert_id"/>">

    <tr> 
      <td colspan="2">
	    <!-- user personal info table -->
		<table bgcolor="#BBBBBB" cellpadding="1" cellspacing="0" border="0">
		<tr>
		<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

		<td>
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" width="360">
			<tr bgcolor="#EBF3F8"> 
			  <td align="center">Personal Information</td> 
			</tr>
			<tr bgcolor="#FEFEFE">
			  <td>
				<bean:write name="reqCertBean" property="givenName"/> 
				<bean:write name="reqCertBean" property="surName"/>
        		(<bean:write name="reqCertBean" property="currentCertLevelName"/>)
				<br/>

				<logic:notEqual name="reqCertBean" property="emailAddress" value="null">
					<bean:write name="reqCertBean" property="emailAddress"/>
				</logic:notEqual> 
				<logic:notEqual name="reqCertBean" property="phoneNumber" value="null">
					<bean:write name="reqCertBean" property="phoneNumber"/>
				</logic:notEqual>
				<br/>
			  </td>
			</tr>
			</table>
		</td></tr>
		</table>
    </tr>
    <tr> 
      <td colspan="3"> 
		&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" value="APPROVE" name="action">  
		&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" value="DECLINE" name="action">
		<br/>&nbsp;
           
	  </td>
	</tr>
    <tr> 
      <td> 
		 
        <span class="vegbank_small">
		Requested certification 
		</span>
		<br/>
		&raquo; <logic:equal name="reqCertBean" property="requestedCert" value="2">
	    	CERTIFIED
		</logic:equal>
		<logic:equal name="reqCertBean" property="requestedCert" value="4">
	    	PROFESSIONAL
		</logic:equal>
	  </td>
    </tr>
    <tr> 
      <td>
		 
	    <span class="vegbank_small">
		Highest degree
	    </span>
        <br/> 
	    &raquo; <bean:write name="reqCertBean" property="highestDegree"/>
        </td>
    </tr>
    <tr>
      <td>
	    <span class="vegbank_small">Year of degree</span>
        <br/> 
        &raquo; <bean:write name="reqCertBean" property="degreeYear"/>
        </td>
    </tr>
    <tr> 
      <td>
		
	    <span class="vegbank_small">
		Institution of highest degree
		</span>
        <br/> 
        &raquo; <bean:write name="reqCertBean" property="degreeInst"/>
        </td>
    </tr>
    <tr> 
      <td>
		
		 
	    <span class="vegbank_small">
		Current organization  
		</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="currentOrg"/>
        </td>
    </tr>
    <tr> 
      <td>
        
		 
	    <span class="vegbank_small">
		Current position </span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="currentPos"/>
        </td>
    </tr>
    <tr> 
      <td>
        
		 
	    <span class="vegbank_small">
		ESA Certified Ecologist?</span>
		<br/>
		&raquo;
		<logic:equal name="reqCertBean" property="esaMember" value="1">
		 	YES
		</logic:equal>
		<logic:equal name="reqCertBean" property="esaMember" value="0">
		 	NO
		</logic:equal>
        </td>
    </tr>
    <tr> 
      <td colspan="2">
	    <br/>
        <span class="vegbank_small">Other <b>PROFESSIONAL</b> certification or experience?</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="profExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
		<br/>
	    <span class="vegbank_small">
        List up to 5 of your relevant <b>PUBLICATIONS/THESES</b>:
		</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="relevantPubs"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="vegbank_small">
		Briefly describe your background and expertise in vegetation <b>SAMPLING</b>:</span>
        <br/>
        &raquo; <bean:write name="reqCertBean" property="vegSamplingExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="vegbank_small">
		Briefly describe your background and expertise with vegetation <b>ANALYSIS</b>, description &amp; classification:</span>
        <br/>
        &raquo; <bean:write name="reqCertBean" property="vegAnalysisExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="vegbank_small">
		Briefly describe your prior experience with the <b>US National Vegetation Classification</b>, if any:</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="usnvcExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
	    <span class="vegbank_small">
		Briefly describe your prior experience with <b>VEGBANK</b>, if any:</span>
        <br/>
        &raquo; <bean:write name="reqCertBean" property="vbExp"/>
      </td>
    </tr>
  <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="vegbank_small">
		How do you <b>INTEND TO USE VEGBANK</b>?</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="vbIntention"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/> 
	    <span class="vegbank_small">
		Briefly describe your prior experience with vegetation plot <b>DATABASES</b> and/or analytical <b>TOOLS</b>:</span>
		<br/>
        &raquo; <bean:write name="reqCertBean" property="toolsExp"/>
      </td>
    </tr> 
	<tr bgcolor="#FFFFFF"> 
	  <td colspan="4">
	    <br/> 
	     
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
			<html:select name="reqCertBean" property="expRegionA">
			  <option value="">--select a region--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="reqCertBean" property="expRegionAVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionAFlor"/>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionANVC"/>
		  </td>
		</tr>
		<tr bgcolor="#EEEEEE"> 
		  <td align="center">
			<html:select name="reqCertBean" property="expRegionB">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionBVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionBFlor"/>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionBNVC"/>
		  </td>
		</tr>
		<tr bgcolor="#FFFFFF"> 
		  <td align="center">
			<html:select name="reqCertBean" property="expRegionC">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
			</td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="reqCertBean" property="expRegionCVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="reqCertBean" property="expRegionCFlor"/>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="reqCertBean" property="expRegionCNVC"/>
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
				  <td><bean:write name="reqCertBean" property="esaSponsorNameA"/>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><bean:write name="reqCertBean" property="esaSponsorEmailA"/>
				</tr>
				</table>
			  </td>

			  <td colspan="2">
			    <table>
				<tr>
				  <td align="right"><span class="vegclass_small">Name: </span></td>
				  <td><bean:write name="reqCertBean" property="esaSponsorNameB"/>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><bean:write name="reqCertBean" property="esaSponsorEmailB"/>
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
		&nbsp;&nbsp;&nbsp;&raquo; 
		<logic:equal name="reqCertBean" property="peerReview" value="1">
		 	YES, I am interested in being a peer reviewer.
		</logic:equal>
		<logic:equal name="reqCertBean" property="peerReview" value="0">
		 	NO thank you.
		</logic:equal>
		</td>
    </tr>

    <tr> 
      <td colspan="2">
	    <br/>
		<span class="vegbank_small">
        Additional statements:
		</span>
        <br/>
		<bean:write name="reqCertBean" property="addlStmt"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
		&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" value="APPROVE" name="action">  
		&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" value="DECLINE" name="action">
      </td>
    </tr>
</html:form>
  </table>
           

@vegbank_footer_html_onerow@
</BODY>
</HTML>
