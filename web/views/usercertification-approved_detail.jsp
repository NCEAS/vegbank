@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>VegBank: Registry of Certified Users</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>
@vegbank_header_html_normal@

<vegbank:get select="usercertification_approved" beanName="map" pager="true"/>

<logic:empty name="BEANLIST">
				<p>
                There are no certified users.
				<p> &nbsp;
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->

<h2>Certified VegBank Users</h2>

<table border="0" bgcolor="#FFFFFF" cellpadding="1" cellspacing="0">
<tr><td>

<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="item"><!--each field, only write when HAS contents-->

<tr><td colspan="2"><p><span class="sizelarge">
<bean:write name="onerow" property="givenname"/>&nbsp;<bean:write name="onerow" property="surname"/>
</span>
<logic:notEmpty name="onerow" property="organizationname">
<span class="normal">from <bean:write name="onerow" property="organizationname"/></span>
</logic:notEmpty>
</p></td>
</tr>


<logic:notEmpty name="onerow" property="degree_institution">
<tr><td width="40%" align="left"><!--label:--><p><span class="category">Degree Institution</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="degree_institution"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_org">
<tr><td align="left"><!--label:--><p><span class="category">Current Organization</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="current_org"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_pos">
<tr><td align="left"><!--label:--><p><span class="category">Current Position</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="current_pos"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="esa_member">
<tr><td align="left"><!--label:--><p><span class="category">ESA Certified Ecologist?</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="esa_member"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<tr><td colspan="2"><!--main tbl -->
<table><tr><th>#</th><th>Region</th><th>Vegetation</th><th>Floristics</th><th>US-NVC</th></tr>

<logic:notEmpty name="onerow" property="exp_region_a">
<tr align="center">
<td>1</td>
<td align="left"><bean:write name="onerow" property="exp_region_a"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_nvc"/>&nbsp;</td>
</tr>

</logic:notEmpty>

<logic:notEmpty name="onerow" property="exp_region_b">
<tr align="center">
<td>2</td>
<td align="left"><bean:write name="onerow" property="exp_region_b"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_nvc"/>&nbsp;</td>
</tr>

</logic:notEmpty>
<logic:notEmpty name="onerow" property="exp_region_c">
<tr align="center">
<td>3</td>
<td align="left"><bean:write name="onerow" property="exp_region_c"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_nvc"/>&nbsp;</td>

</tr>


</logic:notEmpty>



</table><!-- regions table -->
<hr noshade="true">
</td></tr>

<!--tr bgcolor="#CCCCCC"><td colspan="5"><img src="@image_server@/transparent.gif" width="420" height="1"></td></tr>
<tr bgcolor="#AAAAAA"><td colspan="5"><img src="@image_server@/transparent.gif" width="1" height="1"></td></tr-->

<!-- main tbl -->
</table>
<br/><br/>
</logic:iterate>
</td></tr> </table>

</logic:notEmpty>

<vegbank:pager/>

          @vegbank_footer_html_tworow@
