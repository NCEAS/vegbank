<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<HTML>
<!-- 
  *   '$RCSfile: vberror.jsp,v $'
  *     Purpose: Deliver an error message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2004-11-16 07:25:19 $'
  *  '$Revision: 1.8 $'
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
<title>Oops! VegBank Error</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<blockquote>
<h3 class="error">Oops! You found a VegBank bug.</h3>
<p class="sizelarge">Sorry, but the last thing you tried to do did not work.</p>
<p class="sizesmall">
Please try again right now by reloading/refreshing this page or going back.<br/>
If it is still broken, please try again later.<br/>
This problem has been reported to the developers.</p>

	<p class="sizenormal">
		Thank you for your patience.<br/>
		<a href="mailto:help@vegbank.org">help@vegbank.org</a>
	</p>	

	<p>
	<span class="error">ERROR MESSAGES:</span>
<logic:messagesPresent>
	<ul>
	<html:messages id="err">
		<li><bean:write name="err"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>

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
