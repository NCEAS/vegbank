


@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
	
<!-- 
*  '$RCSfile: plot-drop.jsp,v $'
*   Purpose: web form to delete plots by their plot_id values
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2005-03-15 18:59:59 $'
*  '$Revision: 1.9 $'
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
  



<title>Drop VegBank Plots</title>






@webpage_masthead_html@

<br/> 
<table  border="0"  >
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

</html:form>

<!-- VEGBANK FOOTER -->

                                                                                                                                             
                                                                                                                                             
                                                                                                                                             

@webpage_footer_html@

