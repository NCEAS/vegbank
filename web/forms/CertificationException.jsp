<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
*   '$RCSfile: CertificationException.jsp,v $'
*     Purpose: web page querying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: mlee $'
*      '$Date: 2004-04-08 05:44:28 $'
*  '$Revision: 1.3 $'
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
<head>@defaultHeadToken@
<meta http-equiv="expires" content="0">
<title>Certification Required</title>
</head>
<body>
@vegbank_header_html_normal@

<blockquote>
<p class="vegbank_large">
	<br/>
	<span class="vegbank_small">The page you tried to access requires a higher level of certification.
	<br/>
	Please use the <html:link action="LoadCertification.do">certification form</html:link> 
	to increase your ability to use VegBank more effectively.<br/>
	</span>
	<br/>
	<br/>
	<span class="vegbank_normal">
		Thank you<br/>
		<a href="mailto:help@vegbank.org">help@vegbank.org</a>
	</span>	
</p>
</blockquote>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@
</body>
</html:html>

