@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  
 
 
      <!-- look for special set of dataset params: -->
     <!-- the contents of the following include are commented out: 
       <%@ include file="includeviews/get_datasetparams.jsp" %> 
     -->
      <!-- get the main page address -->
      <% String mainFrameSrc = "@datasetmappingurl@"  ; %>
          
      
      
      <!-- if no params, what to do?? -->
      <logic:equal name="wparambad" value="false">
         <!-- success, so forward to right place -->
         <meta http-equiv="refresh" content="0;url=<%= mainFrameSrc %><bean:write name='wparam' ignore='true'/>">
     </logic:equal>
 
 
<TITLE>Map VegBank Datasets</TITLE>
 
 @webpage_masthead_html@ 
    
<logic:equal name="wparambad" value="false">
<p><img src="@images_link@maplogo.gif" /> 
The Mapping request has been processed and you should be forwarded automatically to the map.
If the forward fails, you can find
<a href="<%= mainFrameSrc %><bean:write name='wparam' ignore='true'/>">the map here.</a>
</p>

</logic:equal>

<logic:equal name="wparambad" value="true">
  <!-- failed, report errors here -->
  <p>
   Sorry, but VegBank didn't access any datasets to map.  
   Please Press <a href="javascript:history.back()">back</a> 
   (the back button on your browser if that doesn't work),
   and try again.
   
   </p>
   
 
</logic:equal>

<p>
You can also go <a href="@web_context@">Back to the Home Page.</a>
</p>

     @webpage_footer_html@
 
