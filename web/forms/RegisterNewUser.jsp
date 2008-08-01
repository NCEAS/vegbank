
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!--
*   '$RCSfile: RegisterNewUser.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2008-08-01 18:32:41 $'
*  '$Revision: 1.21 $'
*
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
-->


 
<TITLE>VegBank Registration</TITLE>

 <script type="text/javascript">
 function getHelpPageId() {
   return "register-as-new-user";
 }
 
 </script>





@webpage_masthead_html@

  <br/>
<!-- FORM -->
 <html:form action="RegisterNewUser.do">
 
 <!-- table that limits width of text -->
<table cellspacing="2" cellpadding="0" border="0" >
<tr>
 <td colspan="8">
		<h2 align="center">VegBank Registration</h2>
		&nbsp;
 </td>
</tr>




<tr>
<td valign="top">
<span class="sizelarge">Register here.</span>
  <br/>
  <br/>
  <span class="sizesmall">
  <html:errors/>
  </span>

  <TABLE  border="0">
    <TR> 
      <TD>
	  	<b><span class="sizenormal">Login Information</span></b>
	  </TD>
    </TR>

    <TR> 
      <TD>
	  	<font color="#FF0000">*</font>
	  	<span class="sizesmall">e-Mail Address (login name): </span>
		<br/>
                <span class="sizesmall">Please use all lowercase letters.</span><br/>
	  	<html:text property="usr.email_address" size="30"/>
      </TD>
    </TR>

    <TR> 
      <TD>
		<font color="#FF0000">*</font>
		<span class="sizesmall">Password:</span>
		<br/>
		<html:password property="password1" size="30" redisplay="true"/>
      </TD>
      
    </TR>

    <TR> 
      <TD>
	 	 	<font color="#FF0000">*</font>
            <span class="sizesmall">Confirm Password: </span>
			<br/>
        	<html:password property="password2" size="30" redisplay="true"/>
      </TD>
    </TR>

    <TR>
      <TD>&nbsp;</TD>
    </TR>

    <TR>
      <TD> 
         <b><span class="sizenormal">Personal Information</span></b>
      </TD>
    </TR>

    <!-- first name-->
    <TR> 
      <TD>
	 	  <font color="#FF0000">*</font>
          <span class="sizesmall">First Name: </span>
		  <br/>
		  <html:text property="party.givenname" size="30"/>
      </TD>
    </TR>

    <!-- last name -->
    <TR> 
      <TD>
	 	  <font color="#FF0000">*</font>
          <span class="sizesmall">Last Name:</span>
		  <br/>
		  <html:text property="party.surname" size="30"/>
          <!-- insert default value: -->
          <html:hidden property="party.partypublic" value="false"  />
      </TD>
    </TR>
  </TABLE>
  &nbsp;

<!-- TERMS -->
<table bgcolor="#EEEEEE" cellpadding="5" border="0" >
<tr>
	<td>
		<span class="psmall">Acceptance of the  
		<a href="@general_link@terms.html">VegBank terms of use</a>
		is required.
		</span>
  </td>
</tr>
</table>

  <table cellspacing="2" cellpadding="2"  border="1">
    <tr> 
      <td>
          <font color="red">*</font> 
          <html:radio value="accept" property="termsaccept"/> 
          <span class="sizesmall">I accept these terms</span>
      </td>
      <td>
           <html:radio value="decline" property="termsaccept"/> 
           <span class="sizesmall">I decline</span>
      </td>
    </tr>

  </table>


          <p><b><font color="red">*</font> 
		  <span class="sizesmall">denotes required field</span>
		  </b></p>

		<html:submit property="submit" value="Register Now" />

	</td>


<!-- SPACER -->
<td bgcolor="#AAAAAA" width="1"><img src="@image_server@pix_clear" width="1" height="1" /></td>
<td bgcolor="#DDDDDD" width="1"><img src="@image_server@pix_clear" width="1" height="1" /></td>
<td width="1"><img src="@image_server@pix_clear" width="8" height="1" /></td>

<!-- TEXT -->
<td valign="top">
<h3>Learn more here.</h3>
  
<p>Although you may 
 use VegBank freely without registration, registered users are able to...</p>

<ul>
  <li class="sizesmall">add your data and annotate extant data (certification required)</li>
  <li class="sizesmall">build personal datasets and queries (coming soon)</li>
  <li class="sizesmall">request permission to view non-public data (coming soon)</li>
</ul>

<p>Visitors to vegbank.org are <b>guaranteed privacy</b>. 
  Information collected on users and their activities 
  at vegbank.org is kept private and <b>never shared</b> with other organizations 
  or persons.</p>
  
  <p>Any data, comments, interpretations, or the like that you 
  submit or affix to a record in the VegBank database will be considered public 
  except where confidentiality has been formally requested to protect endangered 
  species or rights of private land owners.</p>

<p>Permission to use certain advanced features requires 
registration and <html:link action="LoadCertification.do">certification</html:link>.
</p>
	</td>
</tr>
</table>
</html:form>

<br />
<br />



@webpage_footer_html@
