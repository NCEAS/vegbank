<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	
<!-- 
*  '$RCSfile: plot-submit.jsp,v $'
*   Purpose: web form tosubmit community data to vegbank system
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2004-11-06 01:06:32 $'
*  '$Revision: 1.10 $'
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
<title> VEGBANK VEGETATION PLOT DATA SUBMITTAL WIZARD </title>
<link REL=STYLESHEET HREF="/vegbank/includes/default.css" TYPE="text/css">



</head>


<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">

@vegbank_header_html_normal@

<html:errors/>

<br> 

<html:form action="/UploadPlot"  enctype="multipart/form-data">
	<html:hidden value="false" property="updateArchivedPlot"/>
	<html:hidden value="2" property="archiveType"/>


<table  border="0" width="800" >
	<tr>
		<td bgcolor="white">
			<img align="center" border="0" height="100" src="@image_server@owlogoBev.jpg" alt = "Veg plots logo">
		</td>
		<td align="left" valign="middle">
			<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
				Vegetation Plot Submission
			</font>
		</td>
	</tr>
</table>



<br> 

<table border="0" width="550">
	<tr>
		<td colspan="2">
			<p>In order to share your plot observation data, it must be contained 
			in a VegBank native XML file.  
			<a href="http://gyro.nceas.ucsb.edu/vegdocs/vegbranch/vbr-overview.html">VegBranch</a>
			is a Microsoft Access tool which you can use to generate this XML file.

			<p>Please see the 
			<a href="http://gyro.nceas.ucsb.edu:8080/vegbank/general/faq.html#catg_loading">FAQ</a>
			to learn more about how to submit plots.  See the 
			<a href="@NativeXMLIndexPage@">VegBank native XML</a> page for more information, 
			sample XML documents and schemas.  Although the schema is still evolving, that 
			page will be updated.

		</td>
	</tr>
</table>

&nbsp;



<!-- PLOT FILE  -->
<table border="0" width="780" class="on_page_help">
	
	<tr>
		<td colspan="2">
			<b>Plot Data File Location:</b>
		</td>
	</tr>
	<tr>
		<td width="5%" align="center">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="on_page_help_text">
			Specify the location of a VegBank XML data file.
		</td>
	</tr>

</table>
&nbsp;

<table>
	
	<!-- LOCAL UPLOAD -->
	<tr>
		<td colspan="2" class="category">
			<html:radio value="local" property="dataFileLocation"/>
				Upload from your computer
		</td>
	</tr>
	<tr>
		<td class="vegbank_small">
			&nbsp; &nbsp; &nbsp; Local data file path:
		</td>
		<td>
			<html:file property="plotFile" size="50"/>
		</td>
	</tr>

	<tr>
		<td colspan="2"><br> &nbsp; </td>
	</tr>

	<!-- REMOTE DOWNLOAD -->
	<tr>
		<td colspan="2" class="category">
			<html:radio value="remote" property="dataFileLocation"/>
				Download from web address
		</td>
	</tr>
	<tr>
		<td class="vegbank_small">
			&nbsp; &nbsp; &nbsp; Data file URL:
		</td>
		<td>
			<html:text property="plotFileURL" size="50" value="http://"/>
		</td>
	</tr>


</table>

<table>


<table border="0">
	<tr>
	 <td>
	 	<br>
		<html:submit property="submit" value="continue"/>
	 </td>
	</tr>	
</table>

</html:form>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@

</body>
</html>
