 <!--
 *   '$RCSfile: resultset-download.jsp,v $'
 *   Purpose: 
 *   Copyright: 2000 Regents of the University of California and the
 *               National Center for Ecological Analysis and Synthesis
 *   Authors: @author@
 *
 *  '$Author: mlee $'
 *  '$Date: 2003-10-14 00:53:35 $'
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
 <head>
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


  <form action="@filedownloadservlet@" method="GET">

<!-- Need to pass the list of plots choosen in the previous screen -->
  <input type="hidden" name="plots" value='<%= (String) request.getAttribute("plots") %>'>
  <br>
    

   <table border="0" cellpadding="0" cellspacing="0" width="725" bgcolor="#ffffcc">
    <tbody>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="/vegbank/images/pix_clear.gif" width=
       "1"> 
      </td>
     </tr>
     <tr>
      <td rowspan="100" width="10">
       <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="10"> 
      </td>
      <td align="Left" height="45" valign="Bottom" width="77"> <br>
        <img alt="Resultset download wizard" border="0" height="23" src=
       "/vegbank/images/hdr_file_info.gif" title="Resultset Download Wizard" vspace="2" width=
       "189"> 
      </td>
      <td width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width=
       "10"> </td>
      <td width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="1"> 
      </td>
      <td valign="Bottom" width="361"> <img alt="" border="0" height="3" src="/vegbank/images/pix_clear.gif" width="1"> 
      </td>
      <td align="Left" valign="Bottom" width="80"> <br>
        <img alt="" border="0" height="3" src="/vegbank/images/pix_clear.gif" width=
       "5"> 
      </td>
      <td width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width=
       "10"> </td>
      <td width="126"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="1"> 
      </td>
      <td align="left" valign="Bottom" width="121"> </td>
     </tr>
     <tr>
      <td align="Center" bgcolor="#003366" colspan="8" height="10" valign="Top">
       <img alt="" border="0" height="1" src="/vegbank/images/pix_003366.gif" width=
       "1"> 
      </td>
     </tr>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="/vegbank/images/pix_clear.gif" width=
       "1"> 
      </td>
     </tr>
     <tr>
      <td width="77"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="10"> 
      </td>
      <td bgcolor="#cccc99" width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="10"> 
      </td>
      <td width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="10"> 
      </td>
      <td align="Center" valign="Top" width="361"> 
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
         <tr>
            <td valign="Top" width="116"> <font color="#23238e" face="Helvetica,Arial,Verdana" size="2"><b>Desired 
              data</b></font> </td>
            <td align="Left" valign="Top" width="229"> 
              <input name="dataType" type="radio" value="all" checked>Entire plot<br>
            <input name="dataType" type="radio" value=
           "environmental">Environmental data<br>
            <input name="dataType" type="radio" value="species">Species
           data<br>
            <br>
            
          </td>
         </tr>
         <tr>
            <td valign="Top" width="116"> <font color="#23238e" face="Helvetica,Arial,Verdana" size=
           "2"><b>Data format<a href="@help-for-download-formats-href@"> <img border="0" src="/vegbank/images/question.gif"></a></b></font> 
            </td>
            <td align="Left" valign="Top" width="229"> 
              <input  name="formatType" type="radio" value="xml" checked>xml<br>
        <input name="formatType" type="radio" value="flat">flat-ascii<br>
        <input name="formatType" type="radio" value="html">html<br>
            
	    <!-- THIS IS THE CONDENSED ASCII-FORMAT THAT CAN BE IMPORTED INTO THE ANALYTICAL TOOLS -->
	  <!--  <input name="formatType" type="radio" value="condensed">condensed-ascii -->
	    
            <br>
            
          </td>
         </tr>
         
				 <tr>
            <td height="20" valign="Top" width="116" align="Left"> <font color="#23238e" face="Helvetica,Arial,Verdana" size="2"><b>Compression</b></font> 
            </td>
            <td valign="center" width="229"> 
              <input name="aggregationType" type="radio" value="nocompression" checked><img alt="multiple zipped files" hspace="2" src=
           "/vegbank/images/icon_cat03.gif" > No Compression <br/>
		   <input name="aggregationType" type="radio" value="compress" /><img alt="Single compressed file" hspace="2" src=
           "/vegbank/images/icon_cat09.gif"> Zip and Compress File(s)
          </td>
         </tr>
         
			
        </tbody>
       </table>
      </td>
      <td width="80"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width=
       "10"> </td>
      <td bgcolor="#cccc99" width="10"> <img alt="" border="0" height="1" src="/vegbank/images/pix_clear.gif" width="10"> 
      </td>
      <td align="Left" valign="Top" width="126"> &nbsp; 
        <!-- download action -->
        <input name="actionDownload" type=
       "image" value="download" border="0" height="19" src=
       "/vegbank/images/btn_download.gif" width="99">
<!-- Edit the next line for debugging: 0 is for normal use -->       
        <input name="debugLevel" type="hidden" value="0"> &nbsp;<br>
     <!--   <br>
        
        &nbsp; <input alt="Update" border="0" height="19" name="upload" src=
       "/vegbank/images/btn_update.gif" title="Update" type="image" width="99"><br>
        <br>
        &nbsp; <img alt="Cancel" border="0" height="19" src=
       "/vegbank/images/btn_cancel.gif" title="Cancel" width="99"> -->
      </td>
     </tr>
     <tr>
      <td align="Center" colspan="8" height="10" valign="center">
       <img alt="" border="0" height="10" src="/vegbank/images/pix_clear.gif" width=
       "1"> 
      </td>
     </tr>
     <tr>
      <td align="Center" bgcolor="#003366" colspan="8" height="1" valign=
      "Bottom">
       <img alt="" border="0" height="10" src="/vegbank/images/pix_003366.gif" width=
       "1"> 
      </td>
     </tr>
    </tbody>
   </table>
   <table summary="navigation" border="0" cellpadding="0" cellspacing="5" width="725">
    <tbody>
    <!-- <tr>
      <td align="Center">
       <font class="sm" color="#666666" face=
       "Verdana, Arial, Helvetica, sans-serif" size="1"><a href=
       "/examples/servlet/pageDirector?pageType=reference" target="_blank"
       title="Click here to learn more about downloading plots.">About
       Downloading</a> | <a href="mailto:harris@nceas.ucsb.edu" title=
       "Click here to submit your feedback.">Feedback</a> |&nbsp; <a href=
       "http://www.nceas.ucsb.edu/collab/2180/docs/" title=
       "Database documentaion">Database Documentation</a></font> 
      </td>
     </tr> -->
     <tr>
      <td align="Center">
       <br>
        
      </td>
     </tr>
     <tr>
      <td align="Center">
       &nbsp; &nbsp; &nbsp;
      </td>
     </tr>
    </tbody>
   </table>
  </form>
   <!-- VegBank footer -->
   @vegbank_footer_html_tworow@
 </body>
</html>


