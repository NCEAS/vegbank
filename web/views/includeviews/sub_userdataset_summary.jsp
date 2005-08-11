<!-- this is a sub form that should be included in another .jsp that gets the datasets needed.  This form displays the contents of the dataset -->
<logic:notPresent name="forceAll" >
  <vegbank:pager />
</logic:notPresent>
 <logic:empty name="userdataset-BEANLIST">
   <p>Sorry, no Datasets found! 
      <% if ( strWebUserId == "-1" ) {  %>  
        You are not logged in. 
        <a href="@general_link@login.jsp">Login Here.</a> 
      <% } %>
   </p>
 </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->

<table class="thinlines" cellpadding="1"><!--each field, only write when HAS contents-->
<tr>
<th nowrap="nowrap">Datacart</th>
<!--%@ include file="../autogen/userdataset_summary_head.jsp" %-->
<%@ include file="../custom/userdataset_summary_head.jsp" %>
<th nowrap="nowrap">ITEMS</th>
</tr>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->


<logic:empty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" value="no description"/>
</logic:empty>
<logic:notEmpty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" name="onerowofuserdataset" property="datasetdescription"/>
</logic:notEmpty>

<tr class="@nextcolorclass@" id="<bean:write name="onerowofuserdataset" property="userdataset_id" />">
<td align="center" class="control_tab_link" rowspan="2" valign="top">
 <logic:equal name="onerowofuserdataset" property="datasettype" value="normal">
        <form method="post" action="@views_link@datacart_detail.jsp"><input type="hidden" name="delta" value="set" />
        <input type="hidden" name="deltaItems" value="<bean:write name='onerowofuserdataset' property='userdataset_id' />" />
        <input type="submit" value="activate" />
        <br />
        </form>
 </logic:equal>
 <logic:notEqual name="onerowofuserdataset" property="datasettype" value="load"><!-- dont edit load datasets -->
        <a href="#" onclick="editDatasetRow(this.parentNode);return false;" class="control_tab_link">edit</a>
   <logic:notEqual name="onerowofuserdataset" property="datasettype" value="datacart">
        <a href="#" onclick="removeDatasetRow(this.parentNode);return false;" class="nobg"><img src="@image_server@grey_x.gif"></a>
   </logic:notEqual>
 </logic:notEqual><!-- dont edit load datasets -->

     </td>
<!--%@ include file="../autogen/userdataset_summary_data.jsp" %-->
<%@ include file="../custom/userdataset_summary_data.jsp" %>

<!-- show dataset items -->
<TD class="largefield" rowspan="2" valign="top" nowrap="nowrap">
<bean:define id="ud_id" name="onerowofuserdataset" property="userdataset_id" />
 
<vegbank:get id="userdatasetitem" select="userdatasetitem_counts" beanName="map" 
  where="where_userdataset_pk" wparam="ud_id" perPage="-1" />
   <logic:empty name="userdatasetitem-BEANLIST">
                Empty dataset.
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">

<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">
<!-- need to lowercase the name of the view: -->
<bean:define id="viewtolink" type="java.lang.String" name="onerowofuserdatasetitem" property="itemtable" />
<bean:define id="wheresuffix" value="" />
<logic:equal name="viewtolink" value="observation">
  <bean:define id="wheresuffix" value="_obs" /><!-- special where needed -->
</logic:equal>

<logic:equal name="viewtolink" value="plot">
  <!-- if it's really plot, instead use observation -->
  <bean:define id="viewtolink" type="java.lang.String" value="observation" /> 
  <bean:define id="wheresuffix" value="_plot" /><!-- special where needed -->
</logic:equal>



<!-- go to summary view if > 4  items, else detail -->
<bean:define id="viewtype" value="summary" />
<logic:lessThan name="onerowofuserdatasetitem" property="countudi" value="5">
  <bean:define id="viewtype" value="detail" />
</logic:lessThan>

<a href='@get_link@<bean:write name="viewtype" />/<%= viewtolink.toLowerCase() %>/<bean:write name="onerowofuserdataset" property="accessioncode" />?where=where_inuserdataset_ac<bean:write name="wheresuffix" />'>
<bean:write name="onerowofuserdatasetitem" property="itemtable" /> (<bean:write name="onerowofuserdatasetitem" property="countudi" />)</a><br/>

</logic:iterate>

</logic:notEmpty>
</TD>

</tr>
<tr class="<%= rowClass %>" id="<bean:write name="onerowofuserdataset" property="userdataset_id" />-desc">
<td colspan="3" class="special_note"><bean:write name="dsDesc" /></td>
</tr>

</logic:iterate>
</table>



<!-- BEGIN EDIT FORM TEMPLATE -->
<table id="hidden_table" class="hidden"><tr id="edit_row_tpl" valign="top" style="background-color: #CAAFB1; border: 4px solid #f00;"><form onsubmit="saveCurrentRecord();return false;"><td class="control_tab" nowrap="nowrap"><a href="#" onclick="saveDatasetEdit(this.parentNode);return false;">save</a> | <a href="#" onclick="cancelDatasetEdit(this.parentNode);return false;">cancel</a><br/><img src="@images_link@i.gif" border="0" width="24" height="22" id="edit_row_busy_icon" class="busyicon"/></td><td><input id="name_input" name="dsname" size="27" style="font-size=9pt"></td><td colspan="3" align="center"><textarea id="desc_textarea" name="dsdesc" rows="3" cols="32" style="font-size:8pt;">Enter description</textarea></td></form></tr><tr id="hidden_table_row"><td></td></tr></table>
<!-- END EDIT FORM TEMPLATE -->


</logic:notEmpty>

<logic:notPresent name="forceAll" >
  <vegbank:pager />
</logic:notPresent>
