<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"> 
<HTML>
<HEAD>@defaultHeadToken@
<TITLE>VegBank Home</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<SCRIPT language=JavaScript>
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
// -->
</SCRIPT>
</HEAD>
<BODY text=#000000 vLink=#6699cc link=#005680 bgColor=#FFFFFF>
@vegbank_header_html_front@

<html:errors/>

<TABLE cellSpacing=0 cellPadding=0 border=0>
  <TBODY> 
  <TR> 
    <TD vAlign=top colSpan=23 height=8></TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=211></TD>
    <TD vAlign=top width=417 colSpan=8 height=211> 
		<P>
		<FONT size="+1"><B>VegBank</B></FONT> 
		is the vegetation plot database
		of the Ecological Society of America's Panel on Vegetation 
		Classification. VegBank consists of three linked databases that contain 
		(1) the actual plot records, (2) vegetation types recognized in the U.S. 
		National Vegetation Classification and other vegetation types submitted 
		by users, and (3) all plant taxa recognized by ITIS/USDA as well as all 
		other plant taxa recorded in plot records. Vegetation records, community 
		types and plant taxa may be submitted to VegBank and may be subsequently 
		searched, viewed, annotated, revised, interpreted, downloaded, and cited. 
		</P>
    </TD>
    <TD vAlign=top colSpan=3 height=211></TD>
    <TD vAlign=top width=343 colSpan=10  rowSpan=2> 
      <P><FONT size="+2"><B>News</B></FONT></P>
      <UL>
      <LI class="constsize"><font color="red">NEW!</font> VegBank workshop coming soon at a Portland ESA meeting <a href="@workshop_link@">workshop.</a> 
        </LI>
      <LI class="constsize">Users can now view VegBank <a href="@general_link@metadata.html"> metadata.</a> 
        </LI>
         <LI class="constsize">Version 2 of the ESA <I><A 
        href="@panel_link@standards.html">Guidelines for Describing Associations and Alliances of the U.S. National Vegetation Classification</A></I>
		has been completed.</LI>
        <LI class="constsize"> 
          Users of VegBank are encouraged to report errors and suggestions to <a href="mailto:help@vegbank.org">help@vegbank.org</a>
          </LI>
        <LI class="constsize">
          <font color="red">NEW!</font> Users may <a href="@general_link@login.jsp">log in as guest</a>, so you can search through VegBank without registering.
        </LI>
      </UL>
    </TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 ></TD>
    <TD vAlign=top height=149 rowSpan=2></TD>
    <TD vAlign=top width=336 colSpan=6 height=149 rowSpan=2> 

    <!-- Pull in Logon widget -->
    <%@ include file="../includes/Logon.jsp" %>

    </TD>
    <TD vAlign=top height=149 rowSpan=2></TD>
    <TD vAlign=top height=149 rowSpan=2></TD><td/><td />
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=27></TD>
    <TD vAlign=top width=343 colSpan=10 height=1 rowSpan=1> 
   <!--   <P><I>It is interesting to contemplate a tangled bank, clothed with 
        many plants of many kinds, with birds singing on the bushes, with various 
        insects flitting about, and with worms crawling through the damp earth, 
        and to reflect that these elaborately constructed forms, so different 
        from each other, and dependent upon each other in so complex a manner, 
        have all been produced by laws acting around us. &nbsp;&nbsp; - Darwin</I>&nbsp;</P> 
	-->
	<span class="vegbank_small">
      VegBank is operated by the Ecological Society of America's
	  <a href="@panel_link@panel.html">Panel on Vegetation Classification</A> 
	  in cooperation with the
	  <a href="http://www.nceas.ucsb.edu/">National Center for Ecological Analysis and Synthesis</a>.
	  </span>
	  <br/>
     <span class="vegbank_tiny">This material is based upon work
			supported by the National Science Foundation under Grant Nos. DBI-9905838
			and DBI-0213794. Any opinions, findings, and conclusions or
			recommendations expressed in this material are those of the author(s) and
		do not necessarily reflect the views of the National Science Foundation.</span>
	<br/>&nbsp;
    </TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=1></TD>
    <TD vAlign=top width=417 colSpan=8 height=1></TD>
    <TD vAlign=top colSpan=3 height=1></TD>
    <TD vAlign=top width=343  colSpan=10 height=1></TD>


  </TR>
  </TBODY> 
</TABLE>
@vegbank_footer_html_onerow@
</BODY>
</HTML>
