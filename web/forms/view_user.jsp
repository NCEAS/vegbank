
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
  * '$Id :  $'
  * Purpose: Display a user's profile read-only
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: mlee $'
  * '$Date: 2005-03-15 18:59:59 $'
  * '$Revision: 1.6 $'
  *
  *
  -->
  

<!-- #BeginEditable "doctitle" --> 
<TITLE>Edit Your Profile</TITLE>

<!-- #EndEditable --> 





@webpage_masthead_html@

<br>
<br>
<h2 align="center" class="vegbank">View User Profile</h2>

<html:errors/>

<p class="sizenormal">
	If this information needs a change, please <a href="@web_context@UserProfile.do?action=edit_user">update it</a> here.
	<br/>
</p> 
	

	<table  border="0" cellspacing="5" cellpadding="3">
	
	<tr> 
      <td colspan="2"><span class="sizesmall">Email Address / Username: </span><br/>
        
        <bean:write name="webuser" property="email"/>
		&nbsp;&nbsp;&nbsp;
        &raquo; Click here to <a href="@web_context@UserProfile.do?action=change_pwd">change your password</a>.
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
        <bean:write name="webuser" property="preferredname"/>
		<span class="sizetiny"></span>
	  </td>
    </tr>
		
	<!--tr> 
      <td colspan="2"><span class="sizesmall">First Name: </span><br/>
        <bean:write name="webuser" property="givenname"/>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Last Name: </span><br/>
        <bean:write name="webuser" property="surname"/>
	  </td>
    </tr-->
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Institution / Organization: </span><br/>
        <bean:write name="webuser" property="organizationname"/>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Address: </span><br/>
        <bean:write name="webuser" property="address"/>
	  </td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">City: </span><br/>
        <bean:write name="webuser" property="city"/>
		</td>
    </tr>

	<tr> 
      <td colspan="2"><span class="sizesmall">State / Province: </span><br/>
        <bean:write name="webuser" property="state"> 
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Country: </span><br/>
        <bean:write name="webuser" property="country">
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Postal Code: </span><br/>
        <bean:write name="webuser" property="postalcode"/>
		</td>
    </tr>
		
	<tr> 
      <td colspan="2"><span class="sizesmall">Logins: </span>
        <bean:write name="webuser" property="ticketcount"/>
		</td>
    </tr>
<!--		
	<tr> 
      <td colspan="2"><span class="sizesmall">Role(s): </span>
        <bean:write name="webuser" property="permissiontype"/>
		</td>
    </tr>
-->
		
		
	</table>
    <!-- Modify your password -->



@webpage_footer_html@

