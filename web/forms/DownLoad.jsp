<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
 
<!--
 *   '$RCSfile: DownLoad.jsp,v $'
 *   Purpose: 
 *   Copyright: 2000 Regents of the University of California and the
 *               National Center for Ecological Analysis and Synthesis
 *   Authors: @author@
 *
 *  '$Author: mlee $'
 *  '$Date: 2004-04-08 05:44:28 $'
 *  '$Revision: 1.5 $'
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
 <head>@defaultHeadToken@
  <meta name="generator" content="HTML Tidy, see www.w3.org"><!--  -->
  <title>
   VegBank plots data download
  </title>
  <meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<link REL=STYLESHEET HREF="/vegbank/includes/default.css" TYPE="text/css">
<style type="text/css">
.tiny {
    FONT-SIZE: 10px
}
.sm {
    FONT-SIZE: 11px
}
.med {
    FONT-SIZE: 12px
}
.lg {
    FONT-SIZE: 14px
}
.xlg {
    FONT-SIZE: 16px
}
.xxlg {
    FONT-SIZE: 18px
}
</style>
 </head>
 <!--access to the fileDownload servlet-->
 <body bgcolor="#ffffff">
<!-- VegBank Header -->
  @vegbank_header_html_normal@


  <logic:messagesPresent message="false">
    <ul>
      <html:messages id="error" message="false">
        <li><bean:write name="error"/></li>
      </html:messages> 
    </ul>
  </logic:messagesPresent>


<html:form action="/DownLoad">

<!-- Need to pass the list of plots choosen in the previous screen -->
<%  	
	String[] plotsToDownLoad = (String[]) request.getAttribute("plotsToDownLoad"); 
	for(int i=0; i < plotsToDownLoad.length; i++)
	{
		// print out the hidden attribute
%>
 		<input type="hidden" name="plotsToDownLoad" value="<%= plotsToDownLoad[i] %>"/>
<%  	
	}   
%>  

  <br>
    

   <table border="0" cellpadding="0" cellspacing="0" width="725" bgcolor="#ffffcc">
    <tbody>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="@image_server@pix_clear.gif" width="1"> 
      </td>
     </tr>
     <tr>
      <td align="Left" height="45" valign="Bottom" width="77"> <br>
        <img alt="Resultset download wizard" border="0" height="23" src="@image_server@hdr_file_info.gif" title="Resultset Download Wizard" vspace="2" width="189"> 
      </td>
     </tr>
     <tr>
      <td align="Center" bgcolor="#003366" colspan="8" height="10" valign="Top">
       <img alt="" border="0" height="1" src="@image_server@pix_003366.gif" width="1"> 
      </td>
     </tr>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="@image_server@pix_clear.gif" width="1"> 
      </td>
     </tr>
     <tr>
      <td width="77"> <img alt="" border="0" height="1" src="@image_server@pix_clear.gif" width="10"> </td>
      <td bgcolor="#cccc99" width="10"> <img alt="" border="0" height="1" src="@image_server@pix_clear.gif" width="10"> </td>
      <td align="Center" valign="Top" width="361"> 
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
         <tr>
            <td valign="Top" width="116"> <font color="#23238e" face="Helvetica,Arial,Verdana" size="2"><b>Desired 
              data</b></font> </td>
            <td align="Left" valign="Top" width="229"> 
              <html:radio value="all" property="dataType"/>Entire plot<br/>
              <html:radio value="environmental" property="dataType"/>Only environmental data<br/>
              <html:radio value="species" property="dataType"/>Only species data<br/>
              <br/>
          </td>
         </tr>
         <tr>
            <td valign="Top" width="116"> 
              <font color="#23238e" face="Helvetica,Arial,Verdana" size="2">
                <b>Data format<a href="@help-for-download-formats-href@"> <img border="0" src="@image_server@question.gif"></a></b>
              </font> 
            </td>
            <td align="Left" valign="Top" width="229"> 
              <html:radio value="xml" property="formatType"/>xml<br/>
              <html:radio value="flat" property="formatType"/>flat-ascii<br/>
              <br/>
            </td>
         </tr>
        </tbody>
       </table>
      </td>
      <td width="80"> <img alt="" border="0" height="1" src="@image_server@pix_clear.gif" width="10"> </td>
      <td bgcolor="#cccc99" width="10"> <img alt="" border="0" height="1" src="@image_server@pix_clear.gif" width="10"> </td>
      <td align="Left" valign="Top" width="126"> &nbsp; 
        <!-- download action -->
        <input name="actionDownload" type="image" value="download" border="0" height="19" src="@image_server@btn_download.gif" width="99">
        <br>
      </td>
     </tr>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="@image_server@pix_clear.gif" width=
       "1"> 
      </td>
     </tr>
     <tr>
      <td align="Center" bgcolor="#003366" colspan="8" height="1" valign=
      "Bottom">
       <img alt="" border="0" height="10" src="@image_server@pix_003366.gif" width=
       "1"> 
      </td>
     </tr>
    </tbody>
   </table>
   <table summary="navigation" border="0" cellpadding="0" cellspacing="5" width="725">
   <tbody>
     <tr>
       <td align="Center"><br/></td>
     </tr>
     <tr>
      <td align="Center">&nbsp; &nbsp; &nbsp;</td>
     </tr>
    </tbody>
   </table>
  </html:form>
   <!-- VegBank footer -->
   @vegbank_footer_html_tworow@
 </body>
</html>


