<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>@defaultHeadToken@
<title>VegBank Home</title>

<link type="text/css" href="@stylesheet@" rel="stylesheet">
<script language="JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

MM_preloadImages(
		'@image_server@btn_3in1_on.jpg',
		'@image_server@btn_advplot_on.jpg',
		'@image_server@btn_login_on.jpg');


function preSubmit() {
	if (document.metasearch_form.xwhereParams.value == null ||
		document.metasearch_form.xwhereParams.value == "") {
		document.metasearch_form.xwhereParams.value="vb";
		document.metasearch_form.clearSearch.value="1";
	}
}
// -->
</script>
</head>


<body>


<div align="center">
<table border="0" cellspacing="0" cellpadding="0">
<tr><td colspan="2" align="left">
@vegbank_header_html_front@
</td></tr>

<tr>
	<td valign="top" align="left"><table width="1" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
			<td bgcolor="#333333"><img width="1" height="1" src="@image_server@transparent.gif" /></td>
		</tr>
		<tr valign="top"><td width="116px" height="600px">
		<%@ include file="../includes/HomePageImg.jsp" %>
		</td></tr><tr>
            <td align="left"> <!--
              <p class="sizetiny">Eventually: click the picture above to see 
                plot data for the above plot</p> -->
            </td>
          </tr></table></td>
    <!-- whole page --><td><table width="650" border="0" cellpadding="10" cellspacing="3">
  <tbody> 
                
</td>
  </tr>

  
  <tr> 
    
          <td colspan="3"  valign="top" > 
            
Vegetation plots are as central to ecology as DNA is to genetics. These 
fundamental units describe which plants grow where, and with what others to form 
plant communities. <b>VegBank</b> is an online archive for sharing plots, which 
the larger ecological community can then include in their research efforts.
              <a href="@general_link@info.html">More info</a> 
          </td>
		  </tr>
		  <tr>
          <td align="center"  colspan="3"> 
           <table>
<tr>
<td>
<!-- Main Feature Links -->

<a href="@forms_link@plot-query-simple.jsp"
		onMouseOver="MM_swapImage('3in1','','@image_server@btn_3in1_on.jpg',1)" 
		onMouseOut="MM_swapImgRestore()"><img src="@image_server@btn_3in1.jpg" 
		border="0" name="3in1" id="3in1"></a><img 
src="@image_server@dot_tall.jpg"><a href="@web_context@LoadPlotQuery.do"
		onMouseOver="MM_swapImage('advplot','','@image_server@btn_advplot_on.jpg',1)" 
		onMouseOut="MM_swapImgRestore()"><img src="@image_server@btn_advplot.jpg" 
		border="0" name="advplot" id="advplot"></a><img 
src="@image_server@dot_tall.jpg"><a href="@general_link@login.jsp"
		onMouseOver="MM_swapImage('login','','@image_server@btn_login_on.jpg',1)" 
		onMouseOut="MM_swapImgRestore()"><img src="@image_server@btn_login.jpg" 
		border="0" name="login" id="login"></a>

</td>

<!-- main table -->
</tr>
</table>
  <!-- Search box -->
	<br>
	<table align="center" cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
	<tr>
    	<td><img src="@image_server@uplt3.gif"/></td>
		<td></td>
		<td><img src="@image_server@uprt3.gif"/></td>
	</tr>

	<tr>
		<td></td>
		<td>
		<form action="@web_context@forms/metasearch.jsp" method="get" name="metasearch_form">
			<span class="greytext">
			&nbsp; Find anything in VegBank:
			</span>  

			<br>
			 <input type="hidden" name="clearSearch" value="">
			 <input type="text" name="xwhereParams" size="30" value=""/>
		 	 <html:submit value="search" onclick="javascript:preSubmit()"/>
		</td>
		</tr>
		<tr><td></td><td align="right">
			 <input type="checkbox" name="xwhereMatchAny" value="true" checked/>
			 	Match any word
			
		</td>
		<td></td>
	</tr>
	<tr>
    	<td><img src="@image_server@lwlt3.gif"/></td>
		<td></td>
		<td><img src="@image_server@lwrt3.gif"/></td>
	</tr>
	<form> 
	</table>

	<p > 
	
	<table align="center" cellpadding="7" cellspacing="0" border="0" bgcolor="#FFFFFF">
	<tr class="sizesmall">
	<td align="center">
		&raquo; <a href="@forms_link@PlantQuery.jsp">Explore Plant Taxa</a> 
	</td>
	<td align="center">&raquo; <a href="@forms_link@CommQuery.jsp">View Community Types</a>
	</td>	
	<td align="center">	

		&raquo; <a href="@web_context@DisplayUploadPlotAction.do">Share your data</a>
	</td>

	</tr></table>

	</td>
  </tr>
  
  <tr>
          <td  valign="top" > 
              <h3>News</h3>
              <ul>
                <li><span class="bright category">New</span>, November 19, 2004: VegBank.org has been
                rebuilt with new plots <a href="@forms_link@vbsummary.jsp">(~19,000)</a>, 
                new <a href="@forms_link@metasearch.jsp">querying tools</a>, a much faster server,
                an upgraded <a href="@vegbranch_link@vegbranch.html">VegBranch</a>, 
                and better integrated views of data. (examples: 
                <a href="@get_link@std/observation/3546">plot</a> |
                <a href="@get_link@std/plantconcept/10219">plant</a> |
                <a href="@views_link@commconcept_query.jsp?wparam=%25symplocos%25">communities</a>)
      
         
           
                <li>Version 4 of the ESA <i><a 
href="@panel_link@standards.html">Guidelines for Describing 
                  Associations and Alliances of the U.S. National Vegetation 
                  Classification</a></i> has been completed.</li>
                <li> Please report any 
                  errors and suggestions to <a href="mailto:help@vegbank.org">help@vegbank.org</a> 
                </li>
              </ul>
    </td>
	      <td valign="top" > 
            <!--   
	-->
            <span class="sizetiny"> VegBank is operated by the Ecological 
            Society of America's <a 
href="@panel_link@panel.html">Vegetation Panel</a> in cooperation with the <a 
href="http://www.nceas.ucsb.edu/">National 
            Center for Ecological Analysis and Synthesis</a>.  <br>
            <br> 
            This material is based upon work
			supported by the National Science Foundation under Grant Nos. DBI-9905838
			and DBI-0213794. Any opinions, findings, and conclusions or
			recommendations expressed in this material are those of the author(s) and
		do not necessarily reflect the views of the National Science Foundation.</span>
	
    </td>
  </tr>
  <tr><td><img src="@image_server@transparent.gif" width="400" 
height="1"/></td><td><img  src="@image_server@transparent.gif" width="200" 
height="1" /></td></tr>
  <tr><td colspan="2" align="center">
              <p class="sizetiny">&copy; 2004 Ecological Society of America<br>
                <a href="@general_link@terms.html">Terms of use</a> | <a href="@general_link@privacy.html">Privacy policy</a></p></td></tr>
 
  </tbody> 
</table>

<!-- end of top table that has left image --></td></tr></table></div><!-- centering div -->
</body></html>
