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
*  '$Author: mlee $'
*  '$Date: 2003-11-26 21:24:41 $'
*  '$Revision: 1.7 $'
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
<title> VEGBANK VEGETATION PLOT DATA SUBMITTAL WIZARD </title>
<link REL=STYLESHEET HREF="/vegbank/includes/default.css" TYPE="text/css">
 <script type="text/javascript">
<!--
function getHelp()
{
return "@manualFrameFor-submit-plots@"
}

-->
</script>


</head>


<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">

@vegbank_header_html_normal@

<html:errors/>

<br> 

<html:form action="/UploadPlot"  enctype="multipart/form-data" onsubmit="return validateUploadPlot(this)" >


<table  border="0" width="800" >
	<tr>
		<td bgcolor="white">
			<img align="center" border="0" height="100" src="@image_server@owlogoBev.jpg" alt = "Veg plots logo">
		</td>
		<td align="left" valign="middle">
			<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
				Vegetation Plots Submittal Wizard
			</font>
		</td>
	</tr>
</table>



<br> 

<table border="0">
	<tr>
		<td  align=left valign=middle colspan=2>
			<font face="Helvetica,Arial,Verdana" size="2" color="209020">
				<b>Use this form to initiate the plot loading process to VegBank. </b>
			</font>
		</td>
	</tr>
	<tr>
	<td>
		<small>
			<html:submit property="submit" value="continue"/>
			&nbsp;&nbsp;
			<html:submit property="reset" value="reset"/>
		</small>
	</td>
	</tr>	
</table>

<br>	

	
    <!-- required = red * -->
	
		<font size="2" >
			<font color="#F90000">
        <b>*</b>
      </font>
      <b> indicates a required field</b>
    </font>

&nbsp;
&nbsp;


<!-- PLOT DATA DESCRIPTION -->
<table border="0" width="799" bgcolor="#DFE5FA">
	<tr>
		<td  align=left valign=top width="5%" >
			<font face="Helvetica,Arial,Verdana" size="3" color="23238E">
				<b>Plots Archive:</b>
			</font>
		</td>
		
	</tr>
	<tr>
		<td align="center" width="5%">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="item">
			Please choose to either upload new plot(s)  or use previously uploaded plot(s).
		</td>
	</tr>
	
</table>
&nbsp;

<!-- CHOOSE A DATA SOURCE EITHER UPLOAD OR FROM THE USER DATABASE -->
<table class="category">
	<tr>
		<td>
			<html:radio value="false" property="updateArchivedPlot"/>
    			Upload new Plot(s) 
	 	</td>
	</tr>
	
	<tr>
		<td>
			<html:radio value="true" property="updateArchivedPlot" disabled="true"/> 
			Use a Plot Archive Previously Uploaded 
			<font color="#F90000" size="1"><b> -- not yet implemented</b></font><br>
		</td>
	</tr>
</table>

&nbsp;

<!-- PLOT DATA ARCHIVE TYPE -->
<table border="0" width="799" bgcolor="#DFE5FA">
	<tr>
		<td  align=left valign=top width="5%" >
			<font face="Helvetica,Arial,Verdana" size="3" color="23238E">
				<b>Archive Type:</b>
			</font>
		</td>
		
	</tr>
	<tr>
		<td align="center" width="5%">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="item">
		Please choose the type of archive file that you would like to send to VegBank.
		</td>
	</tr>
	
	<tr>
		<td  align="center" width="5%"> 	
		<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" > 
		</td>
		<td class="item">
		Currently this wizard only works with plots stored in the VegBank Native XML format. <br>
		</td>
	</tr>
	<tr>
	<td> </td>
	<td class="item">

		--For more information on VegBank Native  XML, including sample XML documents and schemas, please click 
		<a href="@NativeXMLIndexPage@">here.</a> The Schema is still evolving, and that page will show you the
		most recent version.

	</td>
	</tr>
</table>
&nbsp;



<!-- The codes for each format is defined in the UploadPlotForm.java, These two files need to be kept in sync -->
<table class="category">
	<tr>
		<td>
			<html:radio value="2" property="archiveType"/>VegBank XML Format 
		</td>
	</tr>
</table>

<!-- PLOT FILE  -->
<table border="0" width="799" bgcolor="#DFE5FA">
	
	<tr>
		<td  align=left valign=top width="5%" >
			<font face="Helvetica,Arial,Verdana" size="3" color="23238E">
			<b>PLOTS:</b>
			</font>		
		</td>
	</tr>

	<tr>
		<td align="center" width="5%">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="item">
		Please upload the data from your hard-drive to the server here. 
		<br>
		</td>
	</tr>
</table>
&nbsp;

<table class="category">
	
	<!--SELECT DATA FILE-->
	<tr align="left">
		<td width="30%" align=left valign=top>
			<font size="2" >
			<b>Plot Data File:</b><font color="#F90000"><b>*</b></font>:</font>
		</td>
		<td  align="left" valign="middle">
			<html:file property="plotFile"  size="50"/>
			<b><font face="Helvetica,Arial,Verdana" size="1"></font></b>
		</td>
	</tr>
</table>

<table>

<font size="2" >
	<font color="#F90000">
    <b>*</b>
  </font>
  <b> indicates a required field</b>
</font>

<table border="0">
	<tr>
	 <td>
		<small>
			<html:submit property="submit" value="continue"/>
			&nbsp;&nbsp;
			<html:submit property="reset" value="reset"/>
		</small>
	 </td>
	</tr>	
</table>

</html:form>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@

</body>
</html>
