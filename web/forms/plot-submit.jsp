@webpage_top_html@
 @stdvegbankget_jspdeclarations@  
  @webpage_head_html@


<title>Vegbank XML Upload</title>


<script language="javascript">
function selectRadio(radioIndex) {
    document.UploadPlotForm.dataFileLocation[radioIndex].checked = true;
}
</script>






@webpage_masthead_html@

<html:errors/>

<br /> 

<html:form action="/UploadPlot"  enctype="multipart/form-data">


			
			<h1>	Vegetation Plot Submission </h1>
			
	


	      <p>In order to share your plot observation data, it must be contained 
			in a VegBank native XML file.  
			<a href="/vegdocs/vegbranch/vbr-overview.html">VegBranch</a>
			is a Microsoft Access tool which you can use to generate this XML file.
          </p>
		  <p>Please see the 
			<a href="/vegbank/general/faq.html#catg_loading">FAQ</a>
			to learn more about how to submit plots.  See the 
			<a href="@NativeXMLIndexPage@">VegBank native XML</a> page for more information, 
			sample XML documents and schemas.  Although the schema is still evolving, that 
			page will be updated.
          </p>
		


<!-- PLOT FILE  -->
<p class="instructions">
	
	
			<b>Plot Data File Location:</b>
		
	  <br/>
			Please specify the location of a VegBank XML data file.</p>
		
         <p>

			<html:radio value="local" property="dataFileLocation"/>
				Upload from your computer
        <br/>&nbsp;&nbsp; Local data file path:
		
			<html:file property="plotFile" size="50" onclick="selectRadio(0)"/>
		
        </p>
        <p>
	
			<html:radio value="remote" property="dataFileLocation"/>
				Download from web address <br/>
        &nbsp;&nbsp; Data file URL:
		
			<html:text property="plotFileURL" size="50" value="http://" 
				onclick="if(this.value=='http://'){this.value='';} selectRadio(1);" 
				onblur="if(this.value==''){this.value='http://';}"/>
		
     </p>
     <p>


After your data is loaded, an email will be sent to <u><i><bean:write name="email"/></i></u> containing load results.  
<br />
&nbsp;&raquo; <html:link action="/LoadUser.do">update email address</html:link>
	 	<br />
	 	<br />
		<html:submit property="submit" value="continue"/>
	
 </p>
</html:form>

<!-- VEGBANK FOOTER -->



@webpage_footer_html@
