<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
*   '$RCSfile: AuthenticationException.jsp,v $'
*     Purpose: web page querying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: farrell $'
*      '$Date: 2003-12-05 22:49:35 $'
*  '$Revision: 1.1 $'
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

<html:html locale="true">
<head>
<meta http-equiv="expires" content="0">
<title>Vegbank Authentication Exeception</title>
</head>
<body>
@vegbank_header_html_normal@

<p>
	An Authentication Exception was thrown. This happened, because you tried to access
	an action that you are not allowed to access without being correctly logged in.
</p>
<p>
	Click <html:link href="@general_link@login.jsp">here</html:link> to log in.
</p>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@
</body>
</html:html>

