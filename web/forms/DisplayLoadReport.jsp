<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<!-- 
*   '$RCSfile: DisplayLoadReport.jsp,v $'
*     Purpose: web page for displaying the loading report
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: anderson $'
*      '$Date: 2004-05-06 22:39:07 $'
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
<head>@defaultHeadToken@
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
  <title>
  VEGBANK - Display Load Report
  </title>
  </head>

  <body>
  @vegbank_header_html_normal@
  <bean:write name="ErrorsReport" property="HTMLReport" filter="false"/>

  <p>
  <html:link action="MainMenu.do">Go to main menu</a>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  </body>
</html>
