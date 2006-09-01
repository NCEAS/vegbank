@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<jsp:useBean id="bean" class="org.vegbank.ui.struts.UserProfileForm"/>
  @webpage_head_html@
<!-- 
  * '$RCSfile: edit_user.jsp,v $'
  * Purpose: 
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: mlee $'
  * '$Date: 2006-09-01 18:29:38 $'
  * '$Revision: 1.10 $'
  *
  *
  -->
  


<TITLE>Edit Your Profile</TITLE>

<script type="text/javascript">
function getHelpPageId() {
  return "update-user-profile";
}

</script>





@webpage_masthead_html@

<br />
<br />
<h2 align="center" class="vegbank">Edit Your Profile</h2>

<html:errors/>

<html:form  method="post" action="/UpdateUser.do">
 	<bean:define id="beanUsrId"><bean:write name="webuser" property="userid"/></bean:define>
 	<input type="hidden" name="usrId" value="<bean:write name="beanUsrId"/>"/>
	
	<table  border="0" cellspacing="5" cellpadding="3">
	<tr>
      <td colspan="2">
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
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

			<p class="sizenormal">
				The following fields are all required for <html:link action="LoadCertification.do">certification</html:link>.
				<br/>
			</p> 

		</td>
	</tr>
	
	<tr> 
      <td colspan="2"><span class="sizesmall">Email Address / Username: </span><br/>
        
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
      <td colspan="2"><span class="sizesmall">Preferred Name (nickname): </span><br/>
        <html:text name="upform" property="webuser.preferredname"  size="25" maxlength="50"/>
        <!--html:text property="preferredname" name="webuser" size="25" maxlength="30"/-->
		<span class="sizetiny"></span>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Institution / Organization: </span><br/>
        <html:text name="upform" property="webuser.organizationname"  size="60" maxlength="200"/>
        <!--html:text property="organizationname" name="webuser" size="60" maxlength="200"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Address: </span><br/>
        <html:text name="upform" property="webuser.address"  size="60" maxlength="100"/>
        <!--html:text property="address" name="webuser" size="60" maxlength="100"/-->
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">City: </span><br/>
        <html:text name="upform" property="webuser.city"  size="20" maxlength="20"/>
        <!--html:text property="city" name="webuser" size="20" maxlength="20"/-->
		</td>
    </tr>

	<tr> 
      <td colspan="2"><span class="sizesmall">State / Province (USA, Mexico &amp; Canada only): </span><br/>
        <!--html:select name="webuser" property="state" --> 
        <html:select name="upform" property="webuser.state"> 
          <option value="">-- select one --</option>
          <html:optionsCollection name="statelistbean" property="allStatesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Country: </span><br/>
        <!--html:select name="webuser" property="country"-->
        <html:select name="upform" property="webuser.country">
          <option value="">-- select one --</option>
          <html:optionsCollection name="countrylistbean" property="allCountriesNames"/>
        </html:select>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Postal Code: </span><br/>
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



@webpage_footer_html@

