<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"> 
<HTML>
<HEAD>
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
      <P><FONT face="Georgia, Times New Roman, Times, serif" color=#531100 
      size=+2><B>VegBank </B></FONT><FONT 
      face="Georgia, Times New Roman, Times, serif" color=#531100 size="3">is the vegetation 
        plot database of the Ecological Society of America's Panel on Vegetation 
        Classification. VegBank consists of three linked databases that contain 
        (1) the actual plot records, (2) vegetation types recognized in the U.S. 
        National Vegetation Classification and other vegetation types submitted 
        by users, and (3) all plant taxa recognized by ITIS/USDA as well as all 
        other plant taxa recorded in plot records. Vegetation records, community 
        types and plant taxa may be submitted to VegBank and may be subsequently 
        searched, viewed, annotated, revised, interpreted, downloaded, and cited. 
        </FONT></P>
    </TD>
    <TD vAlign=top colSpan=3 height=211></TD>
    <TD vAlign=top width=343 colSpan=10 height=333 rowSpan=2> 
      <FONT face="Georgia, Times New Roman, Times, serif" size="+2" color="#531100"><B>
      News</B></FONT>
      <UL>
      <LI class="constsize"><FONT size=3 face="Georgia, Times New Roman, Times, serif" 
	          color=#531100><font color="red" size="2">NEW!</font> Users can now view VegBank <a href="@general_link@metadata.html"> metadata.</a> 
	           </FONT> 
        </LI>
      <LI class="constsize"><FONT size=3 face="Georgia, Times New Roman, Times, serif" 
        color=#531100>VegBank's <a href="@general_link@sitemap.html">Site Map</a> is<font size="2" color="red"> updated!</font>  </FONT>   
        </LI>
        
        <LI class="constsize"><FONT size=3 face="Georgia, Times New Roman, Times, serif" 
        color=#531100>Version 2 of the ESA <I><A 
        href="@panel_link@standards.html">Guidelines for Describing Associations and Alliances of the U.S. National Vegetation Classification</A> 
          </I>has been completed. </FONT></LI>
        <LI class="constsize"><font size=3 face="Georgia, Times New Roman, Times, serif" color="#531100"> 
          Users of VegBank are encouraged to report errors and suggestions with 
          our <a href="http://bugzilla.ecoinformatics.org/enter_bug.cgi?product=VegBank">Bugzilla</a> 
          tool. </font><br>
          </LI>
        <LI class="constsize">
          <font size=3 face="Georgia, Times New Roman, Times, serif" color="#531100"> 
          New? Get <a href="@general_link@info.html">info</a> about us or a <a href="@general_link@vbsummary.html">summary 
          of data</a> in Vegbank.  Try VegBank out by <a href="@general_link@register.html">registering</a> (it's free).
          </font>
        </LI>
      </UL>
    </TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=122></TD>
    <TD vAlign=top height=149 rowSpan=2></TD>
    <TD vAlign=top width=336 colSpan=6 height=149 rowSpan=2> 

    <!-- Pull in Logon widget -->
    <jsp:include page="/includes/Logon.jsp" flush="true"/>

    </TD>
    <TD vAlign=top height=149 rowSpan=2></TD>
    <TD vAlign=top colSpan=3 height=149 rowSpan=2></TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=27></TD>
    <TD vAlign=top width=343 colSpan=10 height=1 rowSpan=1> 
   <!--   <P><FONT face="Georgia, Times New Roman, Times, serif" color=#531100 
      size=-2><I>It is interesting to contemplate a tangled bank, clothed with 
        many plants of many kinds, with birds singing on the bushes, with various 
        insects flitting about, and with worms crawling through the damp earth, 
        and to reflect that these elaborately constructed forms, so different 
        from each other, and dependent upon each other in so complex a manner, 
        have all been produced by laws acting around us. -- Darwin</I></FONT><FONT 
      face="Georgia, Times New Roman, Times, serif" 
  size=-7>&nbsp;</FONT></P> -->
    </TD>
  </TR>
  <TR> 
    <TD vAlign=top colSpan=2 height=78></TD>
    <TD vAlign=top width=417 colSpan=8 height=78> 
      <DIV align=left><FONT face="Georgia, Times New Roman, Times, serif" 
      color=#531100 size=2>VegBank is operated by the <A 
      href="@panel_link@panel.html">Panel on Vegetation 
        Classification</A> of the Ecological Society of America in cooperation 
        with the <A href="http://www.nceas.ucsb.edu/">National Center for Ecological 
        Analysis and Synthesis</A>. </FONT></DIV>
    </TD>
    <TD vAlign=top colSpan=3 height=78></TD>
                <TD vAlign=top width=343  colSpan=10 height=78><P><font color=#531100 face="Georgia, Times New Roman, Times, serif" size="-2">This material is based upon work
			supported by the National Science Foundation under Grant Nos. DBI-9905838
			and DBI-0213794. Any opinions, findings, and conclusions or
			recommendations expressed in this material are those of the author(s) and
		do not necessarily reflect the views of the National Science Foundation.</font></P></TD>

  </TR>
  <TR> 
    <TD vAlign=top colSpan=22 height=9></TD>
    <TD vAlign=top width=4 height=9></TD>
  </TR>
  <TR> 
    <TD vAlign=top width=14 height=1><IMG height=1 src="" width=14></TD>
    <TD vAlign=top width=11 height=1><IMG height=1 src="" width=11></TD>
    <TD vAlign=top width=22 height=1><IMG height=1 src="" width=22></TD>
    <TD vAlign=top width=202 height=1><IMG height=1 src="" width=202></TD>
    <TD vAlign=top width=6 height=1><IMG height=1 src="" width=6></TD>
    <TD vAlign=top width=43 height=1><IMG height=1 src="" width=43></TD>
    <TD vAlign=top width=13 height=1><IMG height=1 src="" width=13></TD>
    <TD vAlign=top width=10 height=1><IMG height=1 src="" width=10></TD>
    <TD vAlign=top width=62 height=1><IMG height=1 src="" width=62></TD>
    <TD vAlign=top width=59 height=1><IMG height=1 src="" width=59></TD>
    <TD vAlign=top width=7 height=1><IMG height=1 src="" width=7></TD>
    <TD vAlign=top width=4 height=1><IMG height=1 src="" width=4></TD>
    <TD vAlign=top width=17 height=1><IMG height=1 src="" width=17></TD>
    <TD vAlign=top width=8 height=1><IMG height=1 src="" width=8></TD>
    <TD vAlign=top width=14 height=1><IMG height=1 src="" width=14></TD>
    <TD vAlign=top width=15 height=1><IMG height=1 src="" width=15></TD>
    <TD vAlign=top width=13 height=1><IMG height=1 src="" width=13></TD>
    <TD vAlign=top width=9 height=1><IMG height=1 src="" width=9></TD>
    <TD vAlign=top width=27 height=1><IMG height=1 src="" width=27></TD>
    <TD vAlign=top width=25 height=1><IMG height=1 src="" width=25></TD>
    <TD vAlign=top width=92 height=1><IMG height=1 src="" width=92></TD>
    <TD vAlign=top width=136 height=1><IMG height=1 src="" width=136></TD>
    <TD vAlign=top width=4 height=1><IMG height=1 src="" width=4></TD>
  </TR>
  </TBODY> 
</TABLE>
@vegbank_footer_html_onerow@
</BODY>
</HTML>
