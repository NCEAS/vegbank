<HTML>
<!-- 
  *   '$RCSfile: message.jsp,v $'
  *     Purpose: Deliver a message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: farrell $'
  *      '$Date: 2003-05-01 18:35:06 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->

<HEAD>

<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<br/>

<%= request.getAttribute("message") %>.

<br/>
<br/>


<!-- VEGBANK FOOTER -->
<!-- xxx -->
@vegbank_footer_html_tworow@ 
<!-- xxx -->
</BODY>
</HTML>
