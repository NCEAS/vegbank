<!-- this is a sub form that should be included in another .jsp that gets the datasets needed.  This form displays the contents of the dataset -->
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<vegbank:pager />
<logic:empty name="userdataset-BEANLIST">
                <p>Sorry, no datasets found. <% if ( strWebUserId == "-1" ) {  %> you are not logged on. <% } %></p>
          </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
<br />
<table class="leftrightborders" cellpadding="3"><!--each field, only write when HAS contents-->

<%@ include file="../autogen/userdataset_detail_data.jsp" %>

<!-- show dataset items -->
<TR><TD colspan="2">
Items in this dataset:
<bean:define id="ud_id" name="onerowofuserdataset" property="userdataset_id" />
 
<vegbank:get id="userdatasetitem" select="userdatasetitem" beanName="map" 
  where="where_userdataset_pk" wparam="ud_id" perPage="-1"/>
   <logic:empty name="userdatasetitem-BEANLIST">
                Sorry, no userDatasetItem(s) are in the dataset!
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">
<form action="" method="GET" id="cartable">
<table class="leftrightborders" cellpadding="2" >
<tr>
<th><img src="@images_link@cart_star_off_dark.gif" border="0" id="datacart-results-icon"/></th>
<th>Item</th>
<%@ include file="../autogen/userdatasetitem_summary_head.jsp" %>
<th>More Info</th>
</tr>
<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">
<tr class='@nextcolorclass@'>
<bean:define id="delta_ac" name="onerowofuserdatasetitem" property="itemaccessioncode" />
<td><%@ include file="/includes/datacart_checkbox.jsp" %></td>
<td class="largefield"><a href='/cite/<bean:write name="onerowofuserdatasetitem" property="itemaccessioncode"/>'>link</a></td>
<%@ include file="../autogen/userdatasetitem_summary_data.jsp" %>
<td class="largefield">
  <logic:equal name="onerowofuserdatasetitem" property="itemtable" value="observation">
    <!-- add some info about observations if this is an observation -->  
      <bean:define id="obs_pk" name="onerowofuserdatasetitem" property="itemrecord" />
      <!--bean:write name="obs_pk" /-->
      <vegbank:get id="obs" select="plotandobservation" beanName="map"
        where="where_observation_pk" wparam="obs_pk" perPage="-1" pager="false" />
        <logic:notEmpty name="BEAN">
            <bean:write name="BEAN" property="authorplotcode" />: <bean:write name="BEAN" property="stateprovince" />
        </logic:notEmpty>
  </logic:equal>
&nbsp; </td>
</tr>
</logic:iterate>
</table>
</form>
@mark_datacart_items@
</logic:notEmpty>
</TD></TR>

</table>



</logic:iterate>
</logic:notEmpty>

<vegbank:pager />
