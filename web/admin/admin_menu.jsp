<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!--
*  '$Id: admin_menu.jsp,v 1.2 2004-04-08 05:44:30 mlee Exp $'
*   Purpose: Home for admins
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2004-04-08 05:44:30 $'
*  '$Revision: 1.2 $'
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

<html>

<head>@defaultHeadToken@
 
<title>VegBank Admin Main Menu</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
 
<meta http-equiv="content-type" content="text/html; charset=">
</head>
<body bgcolor="#ffffff" text="#531100" link="#0033cc" vlink="#005680" alink="#0066ff">
@vegbank_header_html_normal@
 
<blockquote> 
  <h2 align="center"><br/>
    <span class="vegbank">VEGBANK ADMINISTRATION</span></h2>
  <h4 class="vegbank">Do you wish to:</h4>
  <ul>
    <li> 
      <h4 class="vegbank">Administer <html:link forward="CertificationAdmin">user certification</html:link></h4>
    </li>
  </ul>
</blockquote>

<br/>
@vegbank_footer_html_tworow@
</body>
</html>
