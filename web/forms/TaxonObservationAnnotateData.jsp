<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: TaxonObservationAnnotateData.jsp,v $'
*   Purpose: View a summary of all TaxonObservations in vegbank to annotate one
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2004-06-29 06:51:57 $'
*  '$Revision: 1.1 $'
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
*
-->
<head><script src="/vegbank/includes/utils.js"></script>

<title>Interpret a Taxon -- choose taxon on plot</title>
<link rel="stylesheet" href="/vegbank/includes/default.css" type="text/css"/>
  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  </style>
  <meta http-equiv="Content-Type" content="text/html; charset=">
  </head>

  <body>

  <!--xxx -->
  <script src="/vegbank/includes/utils.js"></script><table bgcolor="#336633" border="0" cellpadding="0" cellspacing="0" width="779"><tr align="left"><td valign="top" colspan="3" height="6" align="left"><img src="/vegbank/images/uplt3.gif" align="top" height="6" width="6" /></td><td width="6"></td></tr><tr><td valign="top" width="14" rowSpan="1">&#160;</td><td valign="center" width="241" colSpan="1" rowSpan="1" align="center"><font face="Georgia, Times New Roman, Times, serif" color="#ffffff" size="5"><b><a href="/vegbank/"><img src="/vegbank/images/vegbanklogo4.gif" height="43" width="169" alt="VegBank" border="0"/></a></b></font></td><td vAlign="bottom" width="558" colSpan="1" align="right"><select name="vegbankNav" onChange="MM_jumpMenu('parent',this,0)"><option value="#">---Common Pages---</option><option value="/vegbank/">VegBank Home</option><option value="/vegbank/DisplayMainMenu.do">Main Menu</option><option value="/vegbank/LoadPlotQuery.do">Search for Plots</option><option value="/vegbank/forms/PlantQuery.jsp">Search for Plants</option><option value="/vegbank/forms/community-query.html">Search Communities</option><option value="/vegdocs/vegbranch/vegbranch.html">VegBranch</option><option value="/vegbank/general/info.html">Information</option><option value="/vegbank/design/erd/vegbank_erd.pdf">--ERD</option><option value="/vegbank/dbdictionary/dd-index.html">--Data Dictionary</option><option value="/vegbank/general/sitemap.html">VegBank Sitemap</option></select></td><td width="6"></td></tr><tr><td colspan="3" height="6" valign="bottom" align="right"></td><td width="6"><img src="/vegbank/images/lwrt3.gif" align="bottom" height="6" width="6"/></td></tr></table>
  <!--xxx -->


  <h2>Interpret a Taxon -- choose taxon on plot</h2>
  <p>Please choose one of these taxa to interpret. </p>
  <blockquote>
  <table border="0" cellspacing="1" cellpadding="0"><tr bgcolor="#666666"><td>
   <table border="0" cellspacing="1" cellpadding="4">

    <tr class="listhead">
      <td>Taxon Observation</td>
      <td>Plant Code</td>
      <td>INTERPRET?</td>
    </tr>

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="taxonobservation" name="genericBean" type="org.vegbank.common.model.Taxonobservation">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr class="item" valign="top" bgcolor="<%= bgColor %>" >
  	<td>
		<bean:write name="taxonobservation" property="accessioncode"/>
	</td>


<!-- plant Name -->    
  <td>
	<bean:write name="taxonobservation" property="authorplantname"/>
  </td>


<!-- interp link -->
<td>
	<html:link action="InterpretTaxonObservation" paramId="tobsAC" paramName="taxonobservation" paramProperty="accessioncode">
	interpret...</html:link>
</td>

    </tr>
    </logic:iterate>

  </table>
    </td></tr></table>
	</blockquote>

  <br/>

  <!-- VEGBANK FOOTER -->
  <table bgColor="#eed85b" border="0" cellpadding="0" cellspacing="0" width="779"><tr><td valign="top" width="6" height="6" align="left"><img src="/vegbank/images/uplt3.gif" align="top" height="6" width="6" /></td><td width="787"></td><td valign="top" width="6" height="6" align="right"><img src="/vegbank/images/uprt3.gif" align="top" height="6" width="6" /></td></tr><tr><td width="779" colspan="3" valign="center" align="center"><font size="2" face="Georgia, Times New Roman, Times, serif"><a href="/vegbank/general/../index.jsp">VegBank Home</a> | <a href="/vegbank/general/info.html">About VegBank</a> | <a href="/vegbank/general/instructions.html">Instructions</a> | <a href="/vegbank/general/../panel/panel.html">ESA Vegetation Panel</a> | <a href="/vegbank/general/contact.html">Contact</a> | <a href="/vegbank/general/help.html">Help</a></font><font size="2" face="Georgia, Times New Roman, Times, serif"><br/><font size="2" face="Georgia, Times New Roman, Times, serif"><a href="/vegbank/forms/RegisterNewUser.jsp">Register</a> | <a href="/vegbank/general/login.jsp">Login</a> | <a href="/vegbank/DisplayMainMenu.do">use VegBank</a> | <a href="/vegbank/Logoff.do">Logout</a> | <a href="/vegbank/general/account.html">My VegBank Account</a></font></font></td></tr><tr><td valign="bottom" width="6" height="6" align="left"><img src="/vegbank/images/lwlt3.gif" align="bottom" height="6" width="6" /></td><td width="786"></td><td valign="bottom" width="6" height="6" align="right"><img src="/vegbank/images/lwrt3.gif" align="bottom" height="6" width="6" /></td></tr></table><table bgColor="#ffffff" border="0" cellpadding="0" cellspacing="0"><tr><td width="779" height="24" valign="top" align="center"><font size="1" face="Georgia, Times New Roman, Times, serif" color="#808000">&#169; 2003 Ecological Society of America<br/><a href="/vegbank/general/terms.html">Terms of use</a> | <a href="/vegbank/general/privacy.html">Privacy policy</a></font></td></tr></table>
  <!-- END FOOTER -->

  </body>
  </html>
