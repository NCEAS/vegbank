<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html:html locale="true">

	
<!-- 
  *   '$RCSfile: EmailPassword.jsp,v $'
  *     Purpose: Jsp form to allow users to retrive lost passwords from the
  *              system over email. 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2004-04-08 05:44:28 $'
  *  '$Revision: 1.3 $'
  *
  *
  -->
  


<head>@defaultHeadToken@
		<title>VegBank Retrieve your lost password</title>
		<link REL=STYLESHEET HREF="@stylesheet@" TYPE="text/css">
 

	</head>


<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">
@vegbank_header_html_normal@


<html:errors/>

<!-- SECOND TABLE -->
<table align=left border="0" width=90% cellspacing=0 cellpadding=0>
	<tr>
		<td bgcolor="white">
			<img align=center border=0 height=144 src="@image_server@owlogoBev.jpg" alt = "Veg plots logo">
			<td align="left" valign="middle">
    			<table border="0" cellpadding="5">
    				<tr>
    					<td align="left" valign="bottom">
    					<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
							  Retrieve a Lost VegBank Password
    					</font><br/>
    					</td>
   					</tr>
					</table>
			</td>
		</td>
	</tr>
	<tr>
	
		<!-- LEFT MARGIN -->
		<td width="15%"  bgcolor="white" align="left" valign="top">
		</td>
	
		<td align="left">
			<table border="0" cellspacing=5 cellpadding=5>
			<html:form action="/EmailPassword" focus="email">

			<tr valign=top>
			  <td  align=left valign=middle colspan=2>
			    <font face="Helvetica,Arial,Verdana" size="2" color="209020">
				    <b>Use this form to request your VegBank Password. </b>
			    </font>
			    <br/>
			  </td>
			</tr>
		</td>
	</tr>
	</table>
	
	
	
	
<!-- PARTY DATA TABLE WITH DESCRIPTIONS-->
<table border="0" width="100%" bgcolor="#DFE5FA">
	<tr>
		<td  align=left valign=top  width="5%" >
			<font face="Helvetica,Arial,Verdana" size="3" color="23238E">
			<b>Directions:</b>
		</td>
		</font>
	</tr>
	
	<tr>
		<td align="center" width="5%">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="item">
		Please enter your email address to receive your password immediately.
		</td>
	</tr>
</table>
&nbsp;

<table border="0" class="category">
	
	<tr>
		<td align="left" width="20%">
			<font  size="2">Email address:</font>
		</td>
		<td align="left">
		  <html:text property="email" size="25" maxlength="70" />
		</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	</tr>
	
</table>



&nbsp;
&nbsp;
&nbsp;
&nbsp;


<small>
			<html:submit value="continue"/>
			&nbsp;&nbsp;
			<html:reset value="reset"/>
</small>
<br/>

</html:form>

&nbsp;
&nbsp;	
	
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td colspan="2">
<!-- VEGBANK FOOTER -->
@vegbank_footer_html_onerow@
</td></tr>
</table>

</body>
</html:html>
