<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: RegisterNewUser.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: farrell $'
*  '$Date: 2003-12-05 22:52:11 $'
*  '$Revision: 1.3 $'
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

<HEAD>
 
<TITLE>Register as a VegBank User</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
 
<meta http-equiv="Content-Type" content="text/html; charset=">

</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">@vegbank_header_html_normal@

  <br/>

  <html:errors/>

<!-- table that limits width of text -->
<table cellspacing="0" cellpadding="0" border="0" width="799">
<tr>
<h2 align="center"><font face="Georgia, Times New Roman, Times, serif">Register 
  as a VegBank User</font></h2>
<p><font face="Georgia, Times New Roman, Times, serif" size="2">We require that users register 
  so that we can </font></p>
<ul>
  <li><font face="Georgia, Times New Roman, Times, serif" size="2">track use of VegBank 
    data, </font></li>
  <li><font face="Georgia, Times New Roman, Times, serif" size="2">maintain user profiles 
    with preferences and use history, and </font></li>
  <li><font face="Georgia, Times New Roman, Times, serif" size="2">establish appropriate 
    levels of certification and permissions for advanced functions. </font></li>
</ul>
<p><font face="Georgia, Times New Roman, Times, serif" size="2">Visitors to www.vegbank.org 
  are guaranteed privacy. Information collected on users and their activities 
  at www.vegbank.org is kept private and never shared with other organizations 
  or persons. However any data, comments, interpretations, or the like that you 
  submit or affix to a record in the VegBank database will be considered public 
  except where confidentiality has been formally requested to protect endangered 
  species or rights of private land owners.</font></p>
<font face="Georgia, Times New Roman, Times, serif" size="2">Permission to use certain 
advanced features and function requires certification.<br>
After you have registered, you may click here to <a href="@forms_link@certification.html"><b>apply 
to become a Certified User</b></a></font><br>
<h3><font face="Georgia, Times New Roman, Times, serif" size="2"><br>
  Please fill in the information below to create an account.</font> 
  <!-- access servlet here -->
</h3>
</tr>
</table>

 <html:form action="/RegisterNewUser" onsubmit="return validateRegisterNewUserForm(this)">

<!-- table that limits width of text -->
<table cellspacing="0" cellpadding="0" border="0" width="799">
<tr>
  &nbsp;&nbsp;<font face="Georgia, Times New Roman, Times, serif"><b>To become 
  a Vegbank user, you must read and agree to the <a href="@general_link@terms.html">VegBank 
  terms of use</a>. Please read the terms and click on the button below to indicate 
  that you understand and accept those terms.</b></font> 
</tr>
</table>
  <table width="45%" border="1">
    <tr> 
      <td width="56%"> 
         <font face="Georgia, Times New Roman, Times, serif">
           <html:radio value="decline" property="termsaccept"/> 
           <b>I do not accept</b>
         </font>
      </td>
      <td width="44%"> 
        <font face="Georgia, Times New Roman, Times, serif"> 
          <html:radio value="accept" property="termsaccept"/> 
          <b>I accept</b>
        </font>
      </td>
    </tr>
  </table>
  <br>
  <TABLE>
    <TR> 
      <TD colspan="2"><font face="Arial, Helvetica, sans-serif"><b>Login Information</b></font></TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TR> 
      <TD width=158> 
        <font size="2"><font face="Arial, Helvetica, sans-serif">
          <b>Required fields = <font color="#FF0000">*</font></b>
        </font>
      </TD>
      <TD width=309>&nbsp;</TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TBODY> 
    <TR> 
      <TD width=158> 
        <DIV align=left>
          <FONT face="Geneva, Arial, Helvetica" size=2>
            <B>Email Address = login: <font color="#FF0000">*</font> </B>
          </FONT>
        </DIV>
      </TD>
      <TD width="309"> 
        <html:text property="user.email_address" size="40"/>
      </TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TR> 
      <TD width=158> 
        <DIV align=left>
          <FONT face="Geneva, Arial, Helvetica" size=2>
            <B>Password:<font color="#FF0000"> * </font></B>
          </FONT>
        </DIV>
      </TD>
      <TD width=309> 
        <html:password property="password1" size="40" redisplay="false"/>
      </TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TR> 
      <TD width=158> 
        <DIV align=left>
          <FONT face="Geneva, Arial, Helvetica" size=2>
            <B>Re-type Password: </B>
          </FONT>
        </DIV>
      </TD>
      <TD width=309> 
        <html:password property="password2" size="40" redisplay="false"/>
      </TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TR vAlign=top> 
      <TD align=left colSpan=2>&nbsp;</TD>
    </TR>
    <TR vAlign=top> 
      <TD align=left colSpan=2> 
        <h4>
          <FONT face="Arial, Helvetica, sans-serif">
            <B>Account Information</B>
          </FONT>
        </h4>
      </TD>
    </TR>
    <!-- first name-->
    <TR> 
      <TD width="158">
        <FONT face="Geneva, Arial, Helvetica" size=2>
          <B>First Name: <font color="#FF0000">*</font> </B>
        </FONT>
      </TD>
      <TD width="309"> 
      	<html:text property="party.givenname" size="40"/>
      </TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <!-- last name -->
    <TR> 
      <TD width="158">
        <FONT face="Geneva, Arial, Helvetica" size=2>
          <B>Last Name:<font color="#FF0000">* </font></B>
        </FONT>
      </TD>
      <TD width="309"> 
        <html:text property="party.surname" size="40"/>
      </TD>
      <TD width=1> 
        <DIV align=right></DIV>
      </TD>
    </TR>
    <!-- affiliated institution-->
    <TR> 
      <TD width="158">
        <FONT face="Arial, Helvetica, sans-serif"  size=2>
          <B>Affiliated Institution: </B>
        </FONT>
      </TD>
      <TD colSpan=3> 
	      <html:text property="party.organizationname" size="40"/>
      </TD>
    </TR>
    <TR> 
      <TD width=158>
        <font face="Arial, Helvetica, sans-serif"  size=2>
          <b>Address: </b>
        </font>
      </TD>
      <TD colSpan=3> 
        <html:text property="address.deliverypoint" size="40" />  
      </TD>
    </TR>
    <!-- american city -->
    <TR> 
      <TD width=158>
        <FONT face="Arial, Helvetica, sans-serif" size=2>
          <B>City:</B>
        </FONT>
      </TD>
      <TD colSpan=3> 
        <html:text property="address.city" size="40" />
      </TD>
    </TR>
    <TR> 
      <TD width=158>
        <FONT face="Arial, Helvetica, sans-serif"  size=2>
          <B>State/Province <br>(USA, Mexico &amp; Canada only):</B>
        </FONT>
      </TD>
      <TD width="309"> 
        <html:select property="address.administrativearea"> 
          <option value="">-- select one --</option>
          <html:optionsCollection name="statelistbean" property="allStatesNames"/>
        </html:select>
      </TD>
      <TD align=right width=1></TD>
      <TD width=0></TD>
    </TR>
    <TR> 
      <TD width=158>
        <FONT face="Arial, Helvetica, sans-serif"  size=2>
          <B>Country: </B>
        </FONT>
      </TD>
      <TD colSpan=3> 
        <html:select property="address.country">
          <option value="">-- select one --</option>
          <html:optionsCollection name="countrylistbean" property="allCountriesNames"/>
        </html:select>
      </TD>
    </TR>
    <TR> 
      <TD width=158>
        <FONT face="Arial, Helvetica, sans-serif">
           <b>Zip/Postal Code:</b> 
        </FONT>
      </TD>
      <TD colSpan=3> 
        <html:text property="address.postalcode" size="40" />
      </TD>
    </TR>
    <!-- phone number-->
    <TR> 
      <TD width=158> 
        <FONT  face="Arial, Helvetica, sans-serif" size="2">
          <B>Daytime Phone: </B>
        </FONT> 
      </TD>
      <TD width="309">
        <html:text property="telephone.phonenumber" size="40" />
      </TD>
    </TR>
    </TBODY> 
  </TABLE>
  <TABLE cellSpacing=0 cellPadding=0 width=472 border=0>
    <TBODY> 
    <TR> 
      <TD vAlign=top colSpan=4> 
        <html:submit property="submit" value="-- register user--" />
        &nbsp;&nbsp; 
        <html:submit property="reset" value="reset" />
    </TR>
    </TBODY> 
  </TABLE>
</html:form>
<h3><br>
  Certification</h3>
<p>Permission to use certain advanced features and function requires certification.<br>
  Click here to <a href="@forms_link@certification.html"><b>apply to become a Certified User</b></a></p>
<br>@vegbank_footer_html_onerow@
</BODY>
</HTML>
