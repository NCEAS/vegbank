@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=">
@defaultHeadToken@
 
<TITLE>VegBank: Registry of Certified Users</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>
@vegbank_header_html_normal@  <br>
<vegbank:get select="usercertification_approved" beanName="map"/>
<logic:empty name="BEANLIST">
                Sorry, no userCertifications are available in the database!
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->
<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<table border="0" cellpadding="0" cellspacing="0" class="item"><!--each field, only write when HAS contents-->

<tr><td><!--label:--><p><span class="category">Name:</span></p></td>
<td><p><span class="category"><bean:write name="onerow" property="givenname"/>&nbsp; <bean:write name="onerow" property="surname"/>&nbsp;
<logic:notEmpty name="onerow" property="organizationname">
organization:<bean:write name="onerow" property="organizationname"/>
</logic:notEmpty>
</span></p></td>
</tr>


<logic:notEmpty name="onerow" property="degree_institution">
<tr><td><!--label:--><p><span class="category">Degree Institution</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="degree_institution"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_org">
<tr><td><!--label:--><p><span class="category">Current Organization</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="current_org"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_pos">
<tr><td><!--label:--><p><span class="category">Current Position</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="current_pos"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="esa_member">
<tr><td><!--label:--><p><span class="category">ESA Certified Ecologist?</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="esa_member"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<tr><td colspan="2"><!--main tbl -->
<table><tr><th>#</th><th>Region</th><th>Vegetation</th><th>Floristics</th><th>US-NVC</th></tr>

<logic:notEmpty name="onerow" property="exp_region_a">
<tr>
<td><p><span class="item">1</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_a"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_a_veg"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_a_flor"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_a_nvc"/>&nbsp;</span></p></td>
</tr>

</logic:notEmpty>

<logic:notEmpty name="onerow" property="exp_region_b">
<tr>
<td><p><span class="item">2</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_b"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_b_veg"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_b_flor"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_b_nvc"/>&nbsp;</span></p></td>
</tr>

</logic:notEmpty>
<logic:notEmpty name="onerow" property="exp_region_c">
<tr>
<td><p><span class="item">3</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_c"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_c_veg"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_c_flor"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="exp_region_c_nvc"/>&nbsp;</span></p></td>

</tr>


</logic:notEmpty>



</table><!-- regions table -->
</td></tr><!-- main tbl -->
</table>
<br/><br/>
</logic:iterate>
</logic:notEmpty><br>
          @vegbank_footer_html_tworow@
