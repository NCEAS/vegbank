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
<% if ( strWebUserId != "-1" ) {  %>  <!-- dont show for anon users, who can't edit or activate ds -->
<th>Manage Datasets</th><th>More</th>
<% } %>
<!--%@ include file="../autogen/userdataset_summary_head.jsp" %-->
<%@ include file="../custom/userdataset_summary_head.jsp" %>
<th title="This column displays the number of items in your datacart and provides links to the item." nowrap="nowrap">
<%@ include file="../custom/userdatasetitem_quicksummary_head.jsp" %>
</th>
</tr>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->



<tr class="@nextcolorclass@" id="<bean:write name="onerowofuserdataset" property="userdataset_id" />">
  <% if ( strWebUserId != "-1" ) {  %> <!-- dont show for anon users, who can't edit or activate ds -->
     <td align="center" class="control_tab_link" valign="top">
      <logic:equal name="onerowofuserdataset" property="datasettype" value="normal">
             <form method="post" action="@views_link@datacart_detail.jsp"><input type="hidden" name="delta" value="set" />
             <input type="hidden" name="deltaItems" value="<bean:write name='onerowofuserdataset' property='userdataset_id' />" />
             <input title="Make this dataset the current datacart" type="submit" value="activate" />
             <br />
             </form>
      </logic:equal>
      <logic:notEqual name="onerowofuserdataset" property="datasettype" value="load"><!-- dont edit load datasets -->
             <a title="Edit dataset name or description" href="#" onclick="editDatasetRow(this.parentNode);return false;" class="control_tab_link">edit</a>
        <logic:notEqual name="onerowofuserdataset" property="datasettype" value="datacart">
             <a title="Delete this dataset" href="#" onclick="removeDatasetRow(this.parentNode);return false;" class="nobg"><img src="@image_server@grey_x.gif"></a>
        </logic:notEqual>
      </logic:notEqual><!-- dont edit load datasets -->

          </td>
          <td><a href='@get_link@detail/userdataset/<bean:write name="onerowofuserdataset" property="userdataset_id" />'>details</a></td>
          
   <% } %>
<!--%@ include file="../autogen/userdataset_summary_data.jsp" %-->
<%@ include file="../custom/userdataset_summary_data.jsp" %>
<TD class="smallfield" valign="top" nowrap="nowrap">
<%@ include file="../custom/userdatasetitem_quicksummary_data.jsp" %>
</TD>

</tr>

</logic:iterate>
</table>



<!-- BEGIN EDIT FORM TEMPLATE -->
<table id="hidden_table" class="hidden"><tr id="edit_row_tpl" valign="top" style="background-color: #CAAFB1; border: 4px solid #f00;"><form onsubmit="saveCurrentRecord();return false;"><td class="control_tab" nowrap="nowrap" colspan="2"><a href="#" onclick="saveDatasetEdit(this.parentNode);return false;">save</a> | <a href="#" onclick="cancelDatasetEdit(this.parentNode);return false;">cancel</a><br/><img src="@images_link@i.gif" border="0" width="24" height="22" id="edit_row_busy_icon" class="busyicon"/></td><td colspan="2"><input id="name_input" name="dsname" size="27" style="font-size=9pt"><br/><textarea id="desc_textarea" name="dsdesc" rows="3" cols="32" style="font-size:8pt;">Enter description</textarea></td><td><select id="sharing_input" name="dssharing"><option value="private">private</option><option value="public">public</option></select></td><td colspan="2"></td></form></tr><tr id="hidden_table_row"><td></td></tr></table>
<!-- END EDIT FORM TEMPLATE -->


</logic:notEmpty>

<logic:notPresent name="forceAll" >
  <vegbank:pager />
</logic:notPresent>

<p class="instructions">Tip: You can <a href="@views_link@userdataset_deleted.jsp">undelete datasets</a> that you deleted within the past 30 days.</p>