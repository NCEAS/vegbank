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
*  '$Author: anderson $'
*  '$Date: 2004-02-04 00:44:58 $'
*  '$Revision: 1.4 $'
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
<BODY>

@vegbank_header_html_normal@

  <br/>

  <html:errors/>

<!-- table that limits width of text -->
<table cellspacing="0" cellpadding="0" border="0" width="799">
<tr>
<h2 align="center">Register as a VegBank User</h2>

<p>We require that users register so that we can </p>
<ul>
  <li>track use of VegBank data, </li>
  <li>maintain user profiles with preferences and use history, and </li>
  <li>establish appropriate levels of certification and permissions for advanced functions. </li>
</ul>
<p>Visitors to www.vegbank.org 
  are guaranteed privacy. Information collected on users and their activities 
  at www.vegbank.org is kept private and never shared with other organizations 
  or persons. However any data, comments, interpretations, or the like that you 
  submit or affix to a record in the VegBank database will be considered public 
  except where confidentiality has been formally requested to protect endangered 
  species or rights of private land owners.</p>
Permission to use certain 
advanced features and function requires certification.<br>

<h3><br>
  Please fill in the information below to create an account. 
  <!-- access servlet here -->
</h3>
</tr>
</table>

 <html:form action="/RegisterNewUser.do" onsubmit="return validateRegisterNewUserForm(this)">

<!-- table that limits width of text -->
<table cellspacing="0" cellpadding="0" border="0" width="799">
<tr>
  &nbsp;&nbsp;<b>To become 
  a Vegbank user, you must read and agree to the <a href="@general_link@terms.html">VegBank 
  terms of use</a>. Please read the terms and click on the button below to indicate 
  that you understand and accept those terms.</b> 
</tr>
</table>
  <table width="45%" border="1">
    <tr> 
      <td width="56%"> 
         
           <html:radio value="decline" property="termsaccept"/> 
           <b>I do not accept</b>
         
      </td>
      <td width="44%"> 
         
          <html:radio value="accept" property="termsaccept"/> 
          <b>I accept</b>
        
      </td>
    </tr>
  </table>
  <br>
  <TABLE>
    <TR> 
      <TD colspan="2"><b>Login Information</b></TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TR> 
      <TD width=158> 
          <b>Required fields = <font color="#FF0000">*</font></b>
      </TD>
      <TD width=309>&nbsp;</TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <TBODY> 
    <TR> 
      <TD width=158> 
        <DIV align=left>
            <B>Email Address = username: <font color="#FF0000">*</font> </B>
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
            <B>Re-type Password: </B>
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
            <B>Optional Information</B>
        </h4>
      </TD>
    </TR>
    <!-- first name-->
    <TR> 
      <TD width="158">
          <B>First Name: <font color="#FF0000">*</font> </B>
      </TD>
      <TD width="309"> 
      	<html:text property="party.givenname" size="40"/>
      </TD>
      <TD align=right width=1>&nbsp;</TD>
    </TR>
    <!-- last name -->
    <TR> 
      <TD width="158">
          <B>Last Name:<font color="#FF0000">* </font></B>
      </TD>
      <TD width="309"> 
        <html:text property="party.surname" size="40"/>
      </TD>
      <TD width=1> 
        <DIV align=right></DIV>
      </TD>
    </TR>
    </TBODY> 
  </TABLE>
  <TABLE cellSpacing=0 cellPadding=0 width=472 border=0>
    <TBODY> 
    <TR> 
      <TD vAlign=top colSpan=4> 
        <html:submit property="submit" value="Register" />
        &nbsp;&nbsp; 
        <html:submit property="reset" value="reset" />
    </TR>
    </TBODY> 
  </TABLE>
</html:form>
<h3><br>
<br>
@vegbank_footer_html_onerow@
</BODY>
</HTML>
