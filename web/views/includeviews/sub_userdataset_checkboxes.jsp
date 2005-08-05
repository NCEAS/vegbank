<!-- this file is an included view that just shows a table of userdatasets, with checkboxes next to it.
  A form can easily wrap this file and do something with the array of includeds params -->



<!-- sets to force all datasets to be displayed, no pagination: -->
<bean:define id="forceNoPage" value="true" />
  <!-- do the right get -->
 <%@ include file="sub_getuserdatasets.jsp" %>
  <!-- display stuff -->

 <logic:empty name="userdataset-BEANLIST">
   <p>Sorry, no Datasets found! 
      <% if ( strWebUserId == "-1" ) {  %>  
        You are not logged in. 
        <a href="@general_link@login.jsp">Please Login Here.</a> 
      <% } %>
   </p>
 </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->



 
  <table class="thinlines" cellpadding="1">
    <tr>
      <th>Include Dataset</th>
      <th>View Dataset</th>
      <%@ include file="../autogen/userdataset_summary_head.jsp" %>
      <th>Plots</th>
    </tr>

  <logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
    <!-- only show those with observations -->
   
      <bean:define id="userdataset_pk" name="onerowofuserdataset" property="userdataset_id" />
     <tr>
       <td><input type="checkbox" value="<bean:write name='userdataset_pk' />" name="includeds" /></td>
       <td><a href="@get_link@summary/userdataset/<bean:write name='userdataset_pk' />">details</a>
       <%@ include file="../autogen/userdataset_summary_data.jsp" %>
       <td class="numeric">
       <a href='@get_link@std/observation/<bean:write name="onerowofuserdataset" property="itemaccessioncode" />?where=where_inuserdataset_ac_obs'>
       <bean:write name="onerowofuserdataset" property="countobs" /></a></td>
     </tr>


    

  </logic:iterate>
  </table>
  <bean:define id="successfulget" value="true" />
</logic:notEmpty>