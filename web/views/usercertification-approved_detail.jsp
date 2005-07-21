@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@





 
<TITLE>VegBank: Registry of Certified Users</TITLE>



  
 
 
<%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@
 @possibly_center@ 
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<vegbank:get select="usercertification_approved" beanName="map" pager="true"/>
<vegbank:pager />
<logic:empty name="BEANLIST">
				<p>
                There are no certified users.
				</p> 
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->

<h2>Certified VegBank Users</h2>

<logic:iterate id="onerow" name="BEANLIST">
<table  class="leftrightborders" cellpadding="2" >
<!--each field, only write when HAS contents-->

<tr><th colspan="2" class="major"><strong>
<bean:write name="onerow" property="givenname"/>&nbsp;<bean:write name="onerow" property="surname"/>
</strong></th>
</tr>
<logic:notEmpty name="onerow" property="organizationname">
<tr class='@nextcolorclass@'><td class="datalabel">Organization</td><td> <bean:write name="onerow" property="organizationname"/></td></tr>
</logic:notEmpty>




<logic:notEmpty name="onerow" property="degree_institution">
<tr class='@nextcolorclass@'><td width="40%" align="left" class="datalabel">Degree Institution</td>
<td><bean:write name="onerow" property="degree_institution"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_org">
<tr class='@nextcolorclass@'><td align="left" class="datalabel">Current Organization</td>
<td><bean:write name="onerow" property="current_org"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="current_pos">
<tr class='@nextcolorclass@'><td align="left" class="datalabel">Current Position</td>
<td><bean:write name="onerow" property="current_pos"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="esa_member">
<tr class='@nextcolorclass@'><td align="left" class="datalabel">ESA Certified Ecologist?</td>
<td><bean:write name="onerow" property="esa_member"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<tr><td colspan="2"><!--main tbl -->
<table class="thinlines">
<tr><th colspan="4">Knowledge of Regions: (1=Weak, 5=Expert)</th></tr>
<tr><th>Region</th><th>Vegetation</th><th>Floristics</th><th>US-NVC</th></tr>

<logic:notEmpty name="onerow" property="exp_region_a">
<tr class='@nextcolorclass@' align="center">
<td align="left"><bean:write name="onerow" property="exp_region_a"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_a_nvc"/>&nbsp;</td>
</tr>

</logic:notEmpty>

<logic:notEmpty name="onerow" property="exp_region_b">
<tr class='@nextcolorclass@' align="center">
<td align="left"><bean:write name="onerow" property="exp_region_b"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_b_nvc"/>&nbsp;</td>
</tr>

</logic:notEmpty>
<logic:notEmpty name="onerow" property="exp_region_c">
<tr class='@nextcolorclass@' align="center">
<td align="left"><bean:write name="onerow" property="exp_region_c"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_veg"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_flor"/>&nbsp;</td>
<td><bean:write name="onerow" property="exp_region_c_nvc"/>&nbsp;</td>

</tr>


</logic:notEmpty>



</table><!-- regions table -->
<!--<hr noshade="true">-->
</td></tr>


<!--tr bgcolor="#CCCCCC"><td colspan="5"><img src="@image_server@/transparent.gif" width="420" height="1"></td></tr>
<tr bgcolor="#AAAAAA"><td colspan="5"><img src="@image_server@/transparent.gif" width="1" height="1"></td></tr-->

<!-- main tbl -->
</table>
<br/>&nbsp;<br/>
</logic:iterate>


</logic:notEmpty>

<vegbank:pager/>

          @webpage_footer_html@
