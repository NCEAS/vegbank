<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!--
*  '$RCSfile: MainMenu.jsp,v $'
*   Purpose: web form tosubmit community data to vegbank system
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2004-04-26 20:49:43 $'
*  '$Revision: 1.5 $'
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

<HTML>

<HEAD>@defaultHeadToken@
 
<TITLE>VegBank Main Menu</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
 
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">
@vegbank_header_html_normal@
 
<blockquote> 
  <h2 align="center"><br>
    <span class="VegBank">Welcome to Vegbank</span></h2>
  <h4 class="VegBank">Here are your options:</h4>

  <ul>
    <li> 
      <h4 class="VegBank">Access or submit 
	  <a href="@general_link@plots.html">Plot</a> data</h4>
    </li>

    <li> 
      <h4 class="VegBank">Access or submit 
	  <a href="@general_link@types.html">Community Type</a> data</h4>
    </li>

    <li> 
      <h4 class="VegBank"><span class="VegBank">Access or submit 
	  <a href="@general_link@plants.html">Plant Taxon</a> data</span></h4>
    </li>

    <li>
      <h4 class="VegBank">
        <span class="VegBank">Access or submit 
		<a href="@general_link@metadata.html">Supplemental Data in VegBank</a>, 
		e.g. Methods, People, Projects, References<br/>
        </span>
      </h4>
    </li>
    
    
	<!--li> 
		<h4 class="VegBank">Access or submit <a href="@general_link@parties.html">Parties</a> data</h4>
		<h4 class="VegBank">Access or Submit Parties data 
		<font color="#F90000"> <b> Coming soon! </b></font> </h4>
    </li-->

    <li> 
      <h4 class="VegBank">
          Edit your VegBank <html:link action="LoadUser.do">Profile</html:link>
        
      </h4>
			
		<!--
      <h4 class="VegBank">View your VegBank Account  
		<font color="#F90000"> <b> Coming soon! </b></font> </h4>
		-->
    </li>
    
    <li>
      <h4 class="VegBank">
        
          Apply to become a <html:link action="LoadCertification.do">Certified VegBank User</html:link>
        <font color="#F90000"> <b> New! </b></font>
	<a href="@help-for-certification-href@"><img border="0" src="@image_server@question.gif"></a>
      </h4>
    </li>
    
    <li> 
      <h4 class="VegBank"><span class="VegBank">See our 
	  	<a href="@general_link@sitemap.html">Site Map</a></span>
		<font color="#F90000"><b> Newly updated! </b></font></h4>
    </li>		

    <li> 
      <h4 class="VegBank"><span class="VegBank">Annotate data</span>
	  <font color="#F90000"><b> Coming soon! </b></font></h4>
    </li>		
		
	<!--li>
      <h4 class="VegBank"><a href="@general_link@client.html">Download</a> 
        the Desktop Client</h4>
	</li-->
  </ul>

	<!-- Admin -->
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (isAdmin.booleanValue()) {
%>
    <hr noshade="true"/>
      <h4 class="VegBank">ADMINISTRATION
	  <ul>
		<li><h4 class="VegBank">Go to the 
			<html:link action="AdminMenu.do">Admin Menu</html:link></h4>
		</li>
	  </ul>
<% 
		}
	} 
%>
</blockquote>
<br/>
@vegbank_footer_html_tworow@
</BODY>
</HTML>
