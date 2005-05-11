@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
  <title>
   Dataset Download
  </title>
 
<!-- VegBank Header -->
  @webpage_masthead_html@



        <h1>Dataset Download</h1> 

<%  	
String datasetId = (String)request.getAttribute("dsId"); 
boolean ok = true;
if (Utility.isStringNullOrEmpty(datasetId)) {
    // error: must be given dataset ID
    ok = true;
%>  
    <ul><li class="error">ERROR: dataset ID must be given</li></ul>

<%
}
%>  


  <logic:messagesPresent message="false">
    <ul>
      <html:messages id="error" message="false">
        <li><bean:write name="error"/></li>
      </html:messages> 
    </ul>
  </logic:messagesPresent>


<%  	
if (ok) {
%>  


<html:form action="/Download">
 		<input type="hidden" name="dsId" value="<%= datasetId %>"/>

      
    
   <p class="instructions"> 
      Please choose a data format for downloaded plots:</p>
			<ul class="compact">		
              <li> <html:radio value="flat" property="formatType"/><img src="@images_link@csv_icon.gif" alt="a csv icon" />Comma-Separated Values file
                <UL class="compact">
                  <LI> 
                    CSV files are generic spreadsheet files that can be imported into virtually any spreadsheet or database
                    program, such as Microsoft Excel or Works, IBM Lotus 1-2-3, Microsoft Access, and many others. <br/>
                    This download will consist of two files, one for general plot data (location, name, ID, community, etc.)
                    and one for plants occurring on each plot (plant name, stratum, plot ID, cover percent).
                  </LI>
                 </UL>
              </li>  

              <li> <html:radio value="vegbranch" property="formatType"/><img src="@vegbranch_link@images/vegbranch_logo_med.jpg" alt="VegBranch icon"/>VegBranch import</li>
                 <UL class="compact">
                   <LI> 
                     VegBranch downloads are for taking data from VegBank on the web and adding them to your 
                     local <a href="@vegbranch_link@vegbranch.html">VegBranch</a> database.
                   </LI>
                 </UL>
              <li> <html:radio value="xml" property="formatType"/><img src="@images_link@xml_icon.gif" alt="XML icon" /> XML 
                 <UL class="compact">
                   <LI> XML documents are for advanced users.  VegBank has an XML standard
                   that allows complete description of plots and other elements in our database.  Please 
                   see <a href="@NativeXMLIndexPage@">our xml index</a> for more sample XML files and schema files.</LI>
                 </UL>
              </li>
              </ul>
                <input type="hidden" name="dataType" value="all" />
        
		
        <!-- download action -->
        <p class="instructions">Please note that all downloads will be "zipped" in a .zip file archive.  To 
        "unzip" the downloaded file, you'll need what most
        computers have: a decompression software like <a href="http://www.winzip.com">WinZip</a> 
        (Windows XP has its own decompression utility built in). </p>
        
        <input name="actionDownload" type="image" value="download" border="0" height="19" src="@image_server@btn_download.gif" width="99" />
      
   
  </html:form>
     
<%  	
}
%>  

 
@webpage_footer_html@


