<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<HTML>
<!-- 
  *   '$RCSfile: wait.jsp,v $'
  *     Purpose: Deliver an error message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2004-11-16 06:37:20 $'
  *  '$Revision: 1.5 $'
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

<HEAD>@defaultHeadToken@
<meta http-equiv="Refresh" content='5; URL=@web_context@PleaseWait.do'>
<META HTTP-EQUIV="expires" CONTENT="Wed, 12 Nov 2003 00:00:00 GMT">
<title>Please Wait</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<blockquote>
<h3>Please wait</h3>
<p class="sizelarge">Your request is being processed.</p><br/>
<br/>
<br/>
&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
<img src="@image_server@wait.gif">
<br/> 
<br/> 
<p>
<logic:messagesPresent message="true">
	STATUS: 
	<ul>
	<html:messages id="msg" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>

<br/> 
<p class="sizenormal">
	Thank you for your patience.<br/>
	<a href="mailto:help@vegbank.org">help@vegbank.org</a>
</p>	

</blockquote>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


<!-- VEGBANK FOOTER -->
<!-- xxx -->
@vegbank_footer_html_tworow@ 
<!-- xxx -->
</BODY>
</HTML>

