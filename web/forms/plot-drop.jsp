<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	
<!-- 
*  '$RCSfile: plot-drop.jsp,v $'
*   Purpose: web form to delete plots by their plot_id values
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2003-10-17 01:22:12 $'
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
-->
  


<head>
<title>Drop VegBank Plots</title>
<link REL=STYLESHEET HREF="/vegbank/includes/default.css" TYPE="text/css">
</head>


<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">

@vegbank_header_html_normal@

<br/> 
<table  border="0" width="800" >
	<tr>
		<td bgcolor="white">
			<img align="center" border="0" height="100" src="/vegbank/images/owlogoBev.jpg" alt = "Veg plots logo">
		</td>
		<td align="left" valign="middle">
			<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
				Drop VegBank Plots
			</font>
		</td>
	</tr>
</table>

<br/> 
<font face="Helvetica,Arial,Verdana" size="2" color="209020">
	<b>Enter the VegBank plot_id values for each field to delete. </b>
</font>

<br/>&nbsp;

<html:form action="/DropPlot">
<table border="0">
	<tr>
		<td bgcolor="#99DD99">
			<html:textarea property="plotIdList" rows="10" cols="60"/>
		</td>
	</tr>	
	<tr>
		<td>
			<html:submit property="submit" value="continue"/>
		</td>
	</tr>	
</table>

</html:form>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@
                                                                                                                                             
                                                                                                                                             
                                                                                                                                             
</body>
</html>

