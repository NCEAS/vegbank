@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<title>Please Wait</title>
  @webpage_masthead_html@


<blockquote>
<h3>Please wait</h3>
<p class="sizelarge">Your request is being processed.</p><br/>
<br/>
<br/>
&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
<img src="@image_server@wait.gif">
<br/> 
<br/> 
<p>
<logic:messagesPresent message="true">
	STATUS: 
	<ul>
	<html:messages id="msg" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>

<br/> 
<p class="sizenormal">
	Thank you for your patience.<br/>
	<a href="mailto:help@vegbank.org">help@vegbank.org</a>
</p>	

</blockquote>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


<!-- VEGBANK FOOTER -->

@webpage_footer_html@ 




