<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="bean" class="org.vegbank.ui.struts.UserProfileForm"/>

<html>
<!-- 
  * '$RCSfile: edit_user.jsp,v $'
  * Purpose: 
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: anderson $'
  * '$Date: 2004-03-01 01:07:10 $'
  * '$Revision: 1.1 $'
  *
  *
  -->
  
<HEAD>
<!-- #BeginEditable "doctitle" --> 
<TITLE>Edit Your Profile</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=">


</HEAD>
<BODY>
@vegbank_header_html_normal@

<br>
<br>
<h2 align="center" class="vegbank">Edit Your Profile</h2>

<html:errors/>

<p class="vegbank_normal">
	The following fields are all required for <html:link action="LoadCertification.do">certification</html:link>.
	<br/>
</p> 
	

<html:form  method="post" action="/UpdateUser.do">
 	<!--html:hidden property="action" value="update"/-->
	
	<table width="700" border="0" cellspacing="5" cellpadding="3">
	
	<tr> 
      <td colspan="2"><span class="vegbank_small">Email Address / Username: </span><br/>
        
        <html:text property="webuser.email"  size="25"/>
        <!--html:text property="email" name="webuser" size="25"/-->
		&nbsp;&nbsp;&nbsp;
        &raquo; Click here to <html:link forward="ChangePassword">change your password</html:link>.
	  </td>
    </tr>
	<!--
 Email address:(usr.email_address)
 Preferred Name:(usr.preferredName)
 Institution:(party.organizationName)
 Address:(address.deloveryPoint)
 City:(address.city)
 State:(address.administrativeArea)
 Country:(address.country)
 Zip Code:(address.postalCode)
 Day Phone: [delete]
 Number of connections: usr.ticketCount [readonly] 
 Permission Type: (usr.permission_type) [may have to revise text list 1=reg, 2=certified, 3&4=professional
	-->
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Preferred Name (nickname): </span><br/>
        <html:text property="webuser.preferredname"  size="25" maxlength="30"/>
        <!--html:text property="preferredname" name="webuser" size="25" maxlength="30"/-->
		<span class="vegbank_tiny"></span>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Institution / Organization: </span><br/>
        <html:text property="webuser.organizationname"  size="60" maxlength="200"/>
        <!--html:text property="organizationname" name="webuser" size="60" maxlength="200"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Address: </span><br/>
        <html:text property="webuser.address"  size="60" maxlength="100"/>
        <!--html:text property="address" name="webuser" size="60" maxlength="100"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">City: </span><br/>
        <html:text property="webuser.city"  size="20" maxlength="20"/>
        <!--html:text property="city" name="webuser" size="20" maxlength="20"/-->
		</td>
    </tr>

	<tr> 
      <td colspan="2"><span class="vegbank_small">State / Province (USA, Mexico &amp; Canada only): </span><br/>
        <!--html:select property="state" name="webuser"--> 
        <html:select property="webuser.state" > 
          <option value="">-- select one --</option>
          <html:optionsCollection name="statelistbean" property="allStatesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Country: </span><br/>
        <!--html:select property="country" name="webuser"-->
        <html:select property="webuser.country" >
          <option value="">-- select one --</option>
          <html:optionsCollection name="countrylistbean" property="allCountriesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Postal Code: </span><br/>
        <html:text property="webuser.postalcode"  size="20" maxlength="20"/>
        <!--html:text property="postalcode" name="webuser" size="20" maxlength="20"/-->
		</td>
    </tr>
		
	<tr> 
      <td>
	  <br/>
        <html:submit value="Update profile"/>
		&nbsp; &nbsp; &nbsp; <html:link forward="MainMenu">Cancel</html:link>
	  <br/>&nbsp;
      </td>
    </tr>
		
	</table>
	</html:form>
    <!-- Modify your password -->

@vegbank_footer_html_tworow@
</BODY>
</HTML>

