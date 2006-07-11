@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
  <title>
   Dataset Download
  </title>

<script type="text/javascript">
function getHelpPageId() {
  return "download-datacart";
}

</script>

<style> 
li { padding: 6px; }
</style> 

<!-- VegBank Header -->
  @webpage_masthead_html@
  
        <h1>Dataset Download</h1> 

<%  	
String datasetId = (String)request.getParameter("dsId"); 
boolean ok = true;
if (Utility.isStringNullOrEmpty(datasetId)) {
    // error: must be given dataset ID
    ok = false;
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

      
<bean:define id="formatType" value="flat" />
    
   <p class="instructions"> 
      Please choose a download file data format, then click the download button.</p>
			<ul class="compact">
              <li class="evenrow padded"> <input type="radio" value="flat" name="formatType" checked="checked" /><img src="@images_link@csv_icon.gif" alt="csv format" />
              <strong>Comma-Separated Values - Plots only</strong> - <a target="_blank" href="@forms_link@examples/download_csv_explanation.jsp">More Information</a>

                  <blockquote> 
                    CSV files are generic spreadsheet files that can be imported into virtually any spreadsheet or database
                    program, such as Microsoft Excel, Works, Access, Lotus 1-2-3 and many others.

                    This download consists of two files: one for general plot data (location, name, ID, community, etc.)
                    and one for plants occurring on each plot (plant name, stratum, plot ID, cover percent).
                  </blockquote>
                <p>
                    &nbsp; &nbsp; &nbsp;
                    <input type="radio" name="blank_empties" value="true" checked="checked" />
                    Make empty values <strong>blank</strong> (e.g. "98GK44",,,"Wetlands")
                    <br />
                    &nbsp; &nbsp; &nbsp;
                    <input type="radio" name="blank_empties" value="false" />
                    Make empty values <strong>null</strong> (e.g. "98GK44",null,null,"Wetlands")
                </p>

              </li>  

              <li class="oddrow padded">
                <input type="radio" value="vegbranch" name="formatType" disabled="disabled"/>
                <img src="@vegbranch_link@images/vegbranch_logo_med.jpg" alt="VegBranch format"/>
                <strong>VegBranch Import</strong> - Coming soon!
                   <blockquote> 
                     Use this format to transfer data from the vegbank.org web site into your local 
                     <a href="@vegbranch_link@vegbranch.html">VegBranch</a> database.
                   </blockquote>
               </li>

              <li class="evenrow padded">
                <input type="radio" value="xml" name="formatType"/>
                <img src="@images_link@xml_icon.gif" alt="XML format" />
                <strong>XML</strong> - Warning, this can be slow if you have selected a lot of observations.
                <blockquote>XML documents are for advanced users.  VegBank's XML format
                   describes plots, plants, communities and other entities found in our database.  
                   Please see <a href="@NativeXMLIndexPage@">our XML index</a> for more sample XML 
                   files and schema files.
                </blockquote>
              </li>
              </ul>
                <input type="hidden" name="dataType" value="all" />
        
		
        <!-- download action -->
        <p class="instructions">Please note that all downloads will be "zipped" in a .zip file archive.  To 
        "unzip" the downloaded file, you'll need what most
        computers have: a decompression program like <a href="http://www.winzip.com">WinZip</a> 
        (Windows XP has its own decompression utility built in). </p>
        
        
        <input name="actionDownload" type="image" value="download" border="0" src="@image_server@btn_download.gif" />

        <p>&nbsp; </p>
      
   
  </html:form>
     
<%  	
}
%>  

 
@webpage_footer_html@


