<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="enform" class="org.vegbank.ui.struts.EntityNameForm" scope="session" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<!-- 
*   '$RCSfile: EntityName.jsp,v $'
*     Purpose: to find VegBank's discrete name for a rock, plant, etc.
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: anderson $'
*      '$Date: 2003-10-06 20:50:23 $'
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

<head>
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
<title>
VEGBANK - Entity Name Search
</title>
</head>

<body>
@vegbank_header_html_normal@


  

<html:form action="EntityName.do" method="get">


  <!-- SECOND TABLE -->
  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">

    <tr>
      <td colspan="2" bgcolor="white">
	<img align="center" border="0" height="144" src="/vegbank/images/owlogoBev.jpg" alt="Veg plots logo "> 
      </td>
      <td align="left" valign="middle">
	<table border="0" cellpadding="5">
	  <tr>
	    <td align="left" valign="bottom">
	      <font face="Helvetica,Arial,Verdana" size="6" color="#23238E">Entity Name Search Form</font>
	      <br/>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>

    <!-- Instructions Row -->
    <tr>
      <!-- LEFT MARGIN -->
      <td width="10%"  bgcolor="white" align="left" valign="top"></td>
      <td width="5%"  bgcolor="white" align="left" valign="top"></td>
      
      <td align="left">

	<table border="0" align="center">
	  <tbody>
	  <tr valign="top">
	    <td align="left" colspan="2" valign="center">
	      <font color="#23238E" face="Helvetica,Arial,Verdana" size="2">
	      <b>This tool is used to find VegBank's discrete names for rocks, plants and communities.</b>
		  <br/><br/>

	      </font> 
	    </td>
	  </tr>
	  </tbody>
	</table>
      </td>
    </tr>

    <!-- ERROR DISPLAY -->
    <tr>
      <td colspan="3">
	<html:errors/>
      </td>
    </tr>
    

    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
		Please enter an entity name and choose its type. 	
		<br>
		<html:text property="searchText" size="20"/>
		<html:select property="entityType">
			<html:option value="1">plant concept</html:option>
			<html:option value="2">community concept</html:option>
			<html:option value="3">contributing party</html:option>
		</html:select>
		<html:submit value="search"/>&nbsp;&nbsp;

      </td>
    </tr>
    
	<logic:present name="searchResults">
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  <br/>
	  <h3>SEARCH RESULTS:</h3>
	  </td>
    </tr>

    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  <ul>
		<logic:iterate id="result" name="searchResults">
			<li>
			<logic:present name="result"><bean:write name="result"/></logic:present>
			</li> 
		</logic:iterate>
		</ul>
	  </td>
    </tr>
	</logic:present>

	<logic:notPresent name="searchResults">
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  - present link to alphabetical listing of plants, communities and parties
	  </td>
    </tr>
	</logic:notPresent>
	    

      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
	<td colspan="3">
	  <!-- VEGBANK FOOTER -->
	  @vegbank_footer_html_tworow@
	</td>
      </tr>
    </table>
    </html:form>    
    </body>
  </html>
