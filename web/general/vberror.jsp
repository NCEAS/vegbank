<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<HTML>
<!-- 
  *   '$RCSfile: vberror.jsp,v $'
  *     Purpose: Deliver an error message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-01-31 01:25:56 $'
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

<HEAD>
<title>Oops! VegBank Error</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<blockquote>
<h3><font color="red">Oops! You found a VegBank bug.</font></h3>
<span class="vegbank_large">Sorry, but the last thing you tried to do did not work.</span><br/>
<span class="vegbank_small">
Please try again right now, then try again later if it is still broken.<br/>
This problem has been reported to the developers.<br/></span>

	<br/> 
	<span class="vegbank_normal">
		Thank you for your patience<br/>
		<a href="mailto:help@vegbank.org">help@vegbank.org</a>
	</span>	

	<font color="red">ERROR MESSAGES:<br/>
	<html:errors/>

</blockquote>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


<!-- VEGBANK FOOTER -->
<!-- xxx -->
@vegbank_footer_html_tworow@ 
<!-- xxx -->
</BODY>
</HTML>
