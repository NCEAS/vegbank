<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<HTML>
<!-- 
  *   '$RCSfile: failure.jsp,v $'
  *     Purpose: Deliver an error message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2004-04-08 05:44:28 $'
  *  '$Revision: 1.6 $'
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

<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<br/>
<h3><font color="red">A problem occured</font></h3>Please review the following error(s) before proceeding.

<logic:messagesPresent message="false">
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>

<br/>
<br/>


<!-- VEGBANK FOOTER -->
<!-- xxx -->
@vegbank_footer_html_tworow@ 
<!-- xxx -->
</BODY>
</HTML>
