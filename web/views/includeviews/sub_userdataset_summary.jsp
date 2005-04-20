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

<table class="leftrightborders" cellpadding="1"><!--each field, only write when HAS contents-->
<tr>
<%@ include file="../autogen/userdataset_summary_head.jsp" %>
<th>ITEMS</th>
</tr>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->


<tr  class="@nextcolorclass@">
<%@ include file="../autogen/userdataset_summary_data.jsp" %>

<!-- show dataset items -->
<TD class="largefield">
<bean:define id="ud_id" name="onerowofuserdataset" property="userdataset_id" />
 
<vegbank:get id="userdatasetitem" select="userdatasetitem_counts" beanName="map" 
  where="where_userdataset_pk" wparam="ud_id" perPage="-1" />
   <logic:empty name="userdatasetitem-BEANLIST">
                Nothing is in this dataset!
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">

<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">

<bean:write name="onerowofuserdatasetitem" property="itemtable" /> (<bean:write name="onerowofuserdatasetitem" property="countudi" />)<br/>

</logic:iterate>

</logic:notEmpty>
</TD>

</tr>
</logic:iterate>
</table>




</logic:notEmpty>

<vegbank:pager />
