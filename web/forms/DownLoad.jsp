@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
  <title>VegBank Plot Data Download</title>
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
  @webpage_masthead_html@


  <logic:messagesPresent message="false">
    <ul>
      <html:messages id="error" message="false">
        <li><bean:write name="error"/></li>
      
    </ul>
  </logic:messagesPresent>


<html:form action="/DownLoad">

<!-- Need to pass the list of plots choosen in the previous screen -->
<%  	
	String[] selectedPlots = (String[]) request.getAttribute("selectedPlots"); 
	for(int i=0; i < selectedPlots.length; i++)
	{
		// print out the hidden attribute
%>
 		<input type="hidden" name="selectedPlots" value="<%= selectedPlots[i] %>"/>
<%  	
	}   
%>  

  <br>
    

   <table border="0" cellpadding="0" cellspacing="0" width="725" bgcolor="#ffffcc">
    
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
        
         <tr>
            <td valign="top">
              <font color="#23238e" face="Helvetica,Arial,Verdana" size="2">
                <b>Data Format <a href="@help-for-download-formats-href@"><img 
					border="0" src="@image_server@question.gif"></a></b>
              </font> 
			  <br/>
              &nbsp; &nbsp; <html:radio value="xml" property="formatType"/>XML<br/>
              &nbsp; &nbsp; <html:radio value="vegbranch" property="formatType"/>VegBranch import<br/>
              &nbsp; &nbsp; <html:radio value="flat" property="formatType"/>Flat ASCII comma separated<br/>

                <input type="hidden" name="dataType" value="all" />

			<!--  <blockquote>
              	<font color="#23238e" face="Helvetica,Arial,Verdana" size="2">
			  	<b>Flat ASCII Options</b>
				</font>
				<br/>
				  &nbsp; &nbsp; <xx html:radio value="all" property="dataType"/>Entire plot<br/>
				  &nbsp; &nbsp; <xx html:radio value="environmental" property="dataType"/>Only environmental data<br/>
				  &nbsp; &nbsp; <xx html:radio value="species" property="dataType"/>Only species data<br/>
			  </blockquote> -->
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
   
     <tr>
       <td align="Center"><br/></td>
     </tr>
     <tr>
      <td align="Center">&nbsp; &nbsp; &nbsp;</td>
     </tr>
    </tbody>
   </table>
  
   <!-- VegBank footer -->
   @webpage_footer_html@
 



