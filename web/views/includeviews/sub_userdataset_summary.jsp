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

</tr>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->



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



</tr>
<logic:empty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" value="no description"/>
</logic:empty>
<logic:notEmpty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" name="onerowofuserdataset" property="datasetdescription"/>
</logic:notEmpty>
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
