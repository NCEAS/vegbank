@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
  <title>
   VegBank: Download Plots
  </title>
 
<!-- VegBank Header -->
  @webpage_masthead_html@


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
	String[] selectedPlots = (String[]) request.getAttribute("selectedPlots"); 
	for(int i=0; i < selectedPlots.length; i++)
	{
		// print out the hidden attribute
%>
 		<input type="hidden" name="selectedPlots" value="<%= selectedPlots[i] %>"/>
<%  	
	}   
%>  

      
        <h1>Download Plots<h1> 
    
   <p>
           Data Format <a href="@help-for-download-formats-href@"><img 
					border="0" src="@image_server@question.gif"></a></p>
			<ul>		
              <li> <html:radio value="xml" property="formatType"/>XML</li>
              <li> <html:radio value="vegbranch" property="formatType"/>VegBranch import</li>
              <li> <html:radio value="flat" property="formatType"/>Flat ASCII comma separated</li>
   </ul>
                <input type="hidden" name="dataType" value="all" />
           <br/><br/>
		
        <!-- download action -->
        <input name="actionDownload" type="image" value="download" border="0" height="19" src="@image_server@btn_download.gif" width="99">
      
   
  </html:form>
     
 
@webpage_footer_html@


