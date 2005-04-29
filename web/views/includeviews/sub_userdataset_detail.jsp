<!-- this is a sub form that should be included in another .jsp that gets the datasets needed.  This form displays the contents of the dataset -->
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>
<vegbank:pager />
<logic:empty name="userdataset-BEANLIST">
                <p>Sorry, no Datasets found! <% if ( strWebUserId == "-1" ) {  %> you are not logged on. <% } %></p>
          </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
<br>
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
<table class="leftrightborders" cellpadding="2" >
<tr>
<th><img src="@images_link@cart1.gif" border="0" /></th>
<th>item</th>
<%@ include file="../autogen/userdatasetitem_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">
<tr class='@nextcolorclass@'>
<bean:define id="delta_ac" name="onerowofuserdatasetitem" property="itemaccessioncode" />
<td><%@ include file="/includes/datacart_checkbox.jsp" %></td>
<td class="largefield"><a href='/cite/<bean:write name="onerowofuserdatasetitem" property="itemaccessioncode"/>'>link</a></td>
<%@ include file="../autogen/userdatasetitem_summary_data.jsp" %>

</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD></TR>

</table>



</logic:iterate>
</logic:notEmpty>

<vegbank:pager />
