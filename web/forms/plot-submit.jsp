


@webpage_top_html@
  
  @webpage_head_html@
	
<!-- 
*  '$RCSfile: plot-submit.jsp,v $'
*   Purpose: web form tosubmit community data to vegbank system
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: anderson $'
*  '$Date: 2005-03-15 13:55:41 $'
*  '$Revision: 1.18 $'
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
  



<title>Vegbank XML Upload</title>


<script language="javascript">
function selectRadio(radioIndex) {
    document.UploadPlotForm.dataFileLocation[radioIndex].checked = true;
}
</script>






@webpage_masthead_html@

<html:errors/>

<br> 

<html:form action="/UploadPlot"  enctype="multipart/form-data">


<table  border="0" width="800" >
	<tr>
		<td bgcolor="white">
			<img align="center" border="0" height="100" src="@image_server@owlogoBev.jpg" alt = "Veg plots logo">
		</td>
		<td align="left" valign="middle">
			<font face="Helvetica,Arial,Verdana" size="6" color="23238E">
				Vegetation Plot Submission
			</font>
		</td>
	</tr>
</table>



<br> 

<table border="0" width="550">
	<tr>
		<td colspan="2">
			<p>In order to share your plot observation data, it must be contained 
			in a VegBank native XML file.  
			<a href="/vegdocs/vegbranch/vbr-overview.html">VegBranch</a>
			is a Microsoft Access tool which you can use to generate this XML file.

			<p>Please see the 
			<a href="/vegbank/general/faq.html#catg_loading">FAQ</a>
			to learn more about how to submit plots.  See the 
			<a href="@NativeXMLIndexPage@">VegBank native XML</a> page for more information, 
			sample XML documents and schemas.  Although the schema is still evolving, that 
			page will be updated.

		</td>
	</tr>
</table>

&nbsp;



<!-- PLOT FILE  -->
<table border="0" width="780" class="on_page_help">
	
	<tr>
		<td colspan="2">
			<b>Plot Data File Location:</b>
		</td>
	</tr>
	<tr>
		<td width="5%" align="center">
			<img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15" >
		</td>
		<td class="on_page_help_text">
			Specify the location of a VegBank XML data file.
		</td>
	</tr>

</table>
&nbsp;

<table>
	
	<!-- LOCAL UPLOAD -->
	<tr>
		<td colspan="2" class="category">
			<html:radio value="local" property="dataFileLocation"/>
				Upload from your computer
		</td>
	</tr>
	<tr>
		<td class="sizesmall">
			&nbsp; &nbsp; &nbsp; Local data file path:
		</td>
		<td>
			<html:file property="plotFile" size="50" onclick="selectRadio(0)"/>
		</td>
	</tr>

	<tr>
		<td colspan="2"><br> &nbsp; </td>
	</tr>

	<!-- REMOTE DOWNLOAD -->
	<tr>
		<td colspan="2" class="category">
			<html:radio value="remote" property="dataFileLocation"/>
				Download from web address
		</td>
	</tr>
	<tr>
		<td class="sizesmall">
			&nbsp; &nbsp; &nbsp; Data file URL:
		</td>
		<td>
			<html:text property="plotFileURL" size="50" value="http://" 
				onclick="if(this.value=='http://'){this.value='';} selectRadio(1);" 
				onblur="if(this.value==''){this.value='http://';}"/>
		</td>
	</tr>


</table>


<table border="0">
	<tr>
	 <td>
     <br>
After your data is loaded, an email will be sent to <bean:write name="email"/> containing load results.  
<br>
&nbsp;&raquo; <html:link action="/LoadUser.do">update email address</html:link>
	 	<br>
	 	<br>
		<html:submit property="submit" value="continue"/>
	 </td>
	</tr>	
</table>

</html:form>

<!-- VEGBANK FOOTER -->



@webpage_footer_html@
