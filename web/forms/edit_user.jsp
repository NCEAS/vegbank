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
  * '$Date: 2004-04-30 13:10:21 $'
  * '$Revision: 1.3 $'
  *
  *
  -->
  
<HEAD>@defaultHeadToken@
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

<html:form  method="post" action="/UpdateUser.do">
 	<bean:define id="beanUsrId"><bean:write name="webuser" property="userid"/></bean:define>
 	<input type="hidden" name="usrId" value="<bean:write name="beanUsrId"/>"/>
	
	<table width="700" border="0" cellspacing="5" cellpadding="3">
	<tr>
      <td colspan="2">
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" width="360">
			<tr bgcolor="#EBF3F8"> 
			  <td align="center">Personal Information</td> 
			</tr>
			<tr bgcolor="#FEFEFE">
			  <td>
			  	<p>
				<bean:write name="webuser" property="givenname"/> 
				<bean:write name="webuser" property="surname"/>
				<br/>
        		(<bean:write name="webuser" property="permissions"/>)
				<br/>

				<logic:notEqual name="webuser" property="email" value="null">
					<bean:write name="webuser" property="email"/>
				</logic:notEqual> 
				<logic:notEqual name="webuser" property="dayphone" value="null">
					<bean:write name="webuser" property="dayphone"/>
				</logic:notEqual>
				</p>
			  </td>
			</tr>
			</table>

			<p class="vegbank_normal">
				The following fields are all required for <html:link action="LoadCertification.do">certification</html:link>.
				<br/>
			</p> 

		</td>
	</tr>
	
	<tr> 
      <td colspan="2"><span class="vegbank_small">Email Address / Username: </span><br/>
        
        <!--html:text name="webuser" property="email"  size="25"/-->
        <html:text name="upform" property="webuser.email"  size="25"/>
		&nbsp;&nbsp;&nbsp;
        &raquo; Click here to 
		<html:link forward="ChangePassword" paramId="usrId" paramName="beanUsrId">change 
				password</html:link>.
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
        <html:text name="upform" property="webuser.preferredname"  size="25" maxlength="30"/>
        <!--html:text property="preferredname" name="webuser" size="25" maxlength="30"/-->
		<span class="vegbank_tiny"></span>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Institution / Organization: </span><br/>
        <html:text name="upform" property="webuser.organizationname"  size="60" maxlength="200"/>
        <!--html:text property="organizationname" name="webuser" size="60" maxlength="200"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Address: </span><br/>
        <html:text name="upform" property="webuser.address"  size="60" maxlength="100"/>
        <!--html:text property="address" name="webuser" size="60" maxlength="100"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">City: </span><br/>
        <html:text name="upform" property="webuser.city"  size="20" maxlength="20"/>
        <!--html:text property="city" name="webuser" size="20" maxlength="20"/-->
		</td>
    </tr>

	<tr> 
      <td colspan="2"><span class="vegbank_small">State / Province (USA, Mexico &amp; Canada only): </span><br/>
        <!--html:select name="webuser" property="state" --> 
        <html:select name="upform" property="webuser.state"> 
          <option value="">-- select one --</option>
          <html:optionsCollection name="statelistbean" property="allStatesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Country: </span><br/>
        <!--html:select name="webuser" property="country"-->
        <html:select name="upform" property="webuser.country">
          <option value="">-- select one --</option>
          <html:optionsCollection name="countrylistbean" property="allCountriesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="vegbank_small">Postal Code: </span><br/>
        <html:text name="upform" property="webuser.postalcode"  size="20" maxlength="20"/>
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

