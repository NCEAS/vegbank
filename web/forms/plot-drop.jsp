@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
<title>Drop VegBank Plots</title>
@webpage_masthead_html@

<br/> 
<table  border="0" width="800" >
	<tr>
		<td bgcolor="white">
			<img align="center" border="0" height="100" src="@image_server@owlogoBev.jpg" alt = "Veg plots logo">
		</td>
		<td align="left" valign="middle">
			<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
				Drop VegBank Plots
			</font>
		</td>
	</tr>
</table>

<br/> 
<font face="Helvetica,Arial,Verdana" size="2" color="209020">
	<b>Enter the VegBank plot_id values for each field to delete.<br/>
	Separate IDs by SPACES or NEW LINES.</b>
</font>

<br/>&nbsp;

<html:form action="/DropPlot">
<table border="0">
	<tr>
		<td bgcolor="#99DD99">
			<html:textarea property="plotIdList" rows="10" cols="60"/>
		</td>
	</tr>	
	<tr>
		<td>
			<html:submit property="submit" value="continue"/>
		</td>
	</tr>	
</table>



<!-- VEGBANK FOOTER -->
@webpage_footer_html@
                                                                                                                                             
                                                                                                                                             
                                                                                                                                             



