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

// -->
</script></head>


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
              <p class="vegbank_tiny">Eventually: click the picture above to see 
                plot data for the above plot</p> -->
            </td>
          </tr></table></td>
    <!-- whole page --><td><table width="650" border="0" cellpadding="10" cellspacing="3">
  <tbody> 
  <!-- vegbank std header as small as possible 
  <tr>
  <td colspan="3" valign="top" align="center">
  <table cellspacing="0" 
cellpadding="0" border="0" bgcolor="#336633"><tbody>
<tr height="6" align="left">
<td align="left" height="6" valign="top"><img width="6" height="6" align="top" src="@image_server@uplt3.gif"></td>
<td width="6" colspan="2"></td></tr>
<tr><td valign="top"></td><td align="center" valign="top"><font size="7" color="#ffffff" face="Georgia, Times New Roman, Times, serif"><b><img alt="VegBank" src="@image_server@vegbanklogo4.gif"></b></font></td>
<td width="6"></td></tr>
<tr><td align="right" valign="bottom" height="6" colspan="2"></td><td width="6"><img width="6" height="6" align="bottom" src="@image_server@lwrt3.gif"></td></tr></tbody></table>
-->
              
</td>
  </tr>

  
  <tr> 
    
          <td colspan="3" class="vegbank" valign="top" > 
            <font color="#000000">
Vegetation plots are as central to ecology as DNA is to genetics. These 
fundamental units describe which plants grow where, and with what others to form 
plant communities. <b>VegBank</b> is an online archive for sharing plots, which 
the larger ecological community can then include in their research efforts.</font>
              <a href="@general_link@info.html">More info</a> 
          </td>
		  </tr>
		  <tr>
          <td align="center" class="vegbank" colspan="3"> 
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
		<html:form action="/PlotQuery" method="get">
		 <!-- <html:select property="state" size="1" multiple="true">
						<%@ include file="../includes/StatesList.jsp" %>
			  </html:select> -->
			<span class="greytext">
			&nbsp; Find <b>plots</b> by <b>plant name</b>:
			
			<!--  <br/>
			&nbsp; (% is wildcard) example: Acer spic% --><br /></span>  
			 <input type="text" name="plantName" size="30" value=""/>
			 <html:submit value="search"/>
			
			
			
			
		</td>
		<td></td>
	</tr>
	<tr>
    	<td><img src="@image_server@lwlt3.gif"/></td>
		<td></td>
		<td><img src="@image_server@lwrt3.gif"/></td>
	</tr>
	</html:form> 
	</table>

	<p class="vegbank"> 
	
	<table align="center" cellpadding="7" cellspacing="0" border="0" bgcolor="#FFFFFF">
	<tr class="vegbank_small">
	<td align="center">
		&raquo; <a href="@forms_link@PlantQuery.jsp">Explore Plant Taxa</a> 
	</td>
	<td align="center">&raquo; <a href="@forms_link@community-query.html">View Community Types</a>
	</td>	
	<td align="center">	

		&raquo; <a href="@web_context@DisplayUploadPlotAction.do">Share your data</a>
	</td>

	</tr></table>

	</td>
  </tr>
  
  <tr>
          <td class="vegbank" valign="top" > 
              <p class="vegbank_large">News</p>
              <ul>
                <li class="constsize">
					Try the simple new 
					<a href="@web_context@forms/plot-query-simple.jsp">3-in-1 Query</a>
					to find plots, plants and communities.
                </li>
               
                <li class="constsize">Take the VegBank <a href="@general_link@instructions.html"> 
                  tutorial.</a> </li>
                <li class="constsize"><font color="red">NEW!</font> July 2004: Version 4 of the ESA <i><a 
href="@panel_link@standards.html">Guidelines for Describing 
                  Associations and Alliances of the U.S. National Vegetation 
                  Classification</a></i> has been completed.</li>
                <li class="constsize"> Please report any 
                  errors and suggestions to <a href="mailto:help@vegbank.org">help@vegbank.org</a> 
                </li>
              </ul>
    </td>
	      <td valign="top" > 
            <!--   
	-->
            <span class="vegbank_tiny"> <!--VegBank is operated by the Ecological 
            Society of America's <a 
href="@panel_link@panel.html">Vegetation Panel</a> in cooperation with the <a 
href="http://www.nceas.ucsb.edu/">National 
            Center for Ecological Analysis and Synthesis</a>.  <br>
            <br> -->
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
              <p class="vegbank_tiny">&copy; 2004 Ecological Society of America<br>
                <a href="@general_link@terms.html">Terms of use</a> | <a href="@general_link@privacy.html">Privacy policy</a></p></td></tr>
 
  </tbody> 
</table>

<!-- end of top table that has left image --></td></tr></table></div><!-- centering div -->
</body></html>
