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
*  '$Author: mlee $'
*  '$Date: 2004-08-27 19:30:27 $'
*  '$Revision: 1.8 $'
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
<BODY>
<div align="center"> 
@vegbank_header_html_normal@
 


  <h1 align="center">
    Welcome to Vegbank</h1>
  <span class="headequiv sizelarge">Here are your options:</span>	
	         
           
	<TABLE  cellpadding="0" cellspacing="0" border="0">
	
  
		  <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	<TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">View Data</span></TD><td/></TR>
	   
	<TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@magnglas.png" /> </TD><TD>	       
  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
  
     
      <span class="headequiv sizelarger">Search for <a href="@general_link@plots.html">Plots</a></span>
      </td><td valign="middle"> 
        <p>&raquo; <a href="@forms_link@plot-query-simple.jsp">Simple 3-in-1 query</a>  &raquo; 
        <a href="@plotquery_page@">Advanced query</a> </p>
      </td>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    

     
      <span class="headequiv sizelarge">Search for 
	  <a href="@forms_link@community-query.html">Community Types</a></span>
      </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

     
      <span class="headequiv sizelarge"><span>Search for  
	  <a href="@forms_link@PlantQuery.jsp">Plants</a> </span></span>
    </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

    
      <span class="headequiv sizelarge">
        View  
		<a href="@general_link@metadata.html">Supplemental Data</a> 
		
      
      </span>
    </td> <td valign="middle"> <p>e.g. Methods, People, Projects, References</p></td></tr>
	  </table>
	  </TD></TR>
	 <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
		  	   <TR><TD colspan="1" bgcolor="#CCCCCC" align="center"><span class="greytext">My Preferences</span></TD><td/></TR>
	 <TR valign="middle"><TD align="center"><img src="@images_link@users.png" /></TD><TD>
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
    
	<!--li> 
		<span class="headequiv sizelarge">Access or submit <a href="@general_link@parties.html">Parties</a> data</span>
		<span class="headequiv sizelarge">Access or Submit Parties data 
		<font color="#F90000"> <b> Coming soon! </b></font> </span>
    </li-->
<!--
     
      <span class="headequiv sizelarge">
          Edit your VegBank <html:link action="LoadUser.do">Profile</html:link>
        
      </span> 
-->			
	
      <span class="headequiv sizelarge">View your <a href="@general_link@account.html">VegBank Account </a> 
		 </span>
		</td> </tr>
	  </table>
	      </TD></TR>
	        <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	   <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Info</span></TD><td/></TR>
	   
		  	  
	<TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@info.png" /></TD><TD>

	  
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
    
  <!--  
      <span class="headequiv sizelarge">
        
          Apply to become a <html:link action="LoadCertification.do">Certified VegBank User</html:link>
	<a href="@help-for-certification-href@"><img border="0" src="@image_server@question.gif"></a>
      </span>
     -->
     
      <span class="headequiv sizelarge">See our 
	  	<a href="@general_link@sitemap.html">Site Map</a> </span></td><td valign="middle"> 
		<p><font color="#F90000"> Newly updated! </font></p>
    		

    </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class="headequiv sizelarge">See <a href="@forms_link@vbsummary.jsp">an overview of VegBank Data</a> 
      </span> 
      </td> </tr>
	  </table>
	  </TD></TR>
	  	<TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
		 <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Add Data</span></TD><td/></TR>
 
	  <TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@upload.png" /></TD><TD>
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
      <span class="headequiv sizelarge">Load <a href="@DisplayUploadPlotAction@">Plot Data</a></span>
     </td><td valign="middle"> 
	  <p>
      
     &raquo; Download<a href="@vegbranch_link@vegbranch.html"> VegBranch</a></p>
     </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

     
      <span class="headequiv sizelarge">Annotate data</span></td><td valign="middle"> 
	  <p><font color="#F90000"> Coming soon! </font></p>
    		
	
	<!--li>
      <span class="headequiv sizelarge"><a href="@general_link@client.html">Download</a> 
        the Desktop Client</span>
	</li-->
  </td></tr>
</table>
 
 <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
 
      
       <span class="headequiv sizelarge"><a href="@general_link@input.html">Input Data</a> In Forms</span></td><td valign="middle"> 
 	 
     		
 	
 	<!--li>
       <span class="headequiv sizelarge"><a href="@general_link@client.html">Download</a> 
         the Desktop Client</span>
 	</li-->
   </td></tr>
</table>
 
</TD></TR></TABLE>
	<!-- Admin -->
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (isAdmin.booleanValue()) {
%>
    <hr true/>
      <span class="headequiv sizelarge">ADMINISTRATION</span>
	  
		<span class="headequiv sizelarge">Go to the 
			<html:link action="AdminMenu.do">Admin Menu</html:link></span>
		
	  
<% 
		}
	} 
%>

<br/>
@vegbank_footer_html_tworow@
</div>
</BODY>
</HTML>
